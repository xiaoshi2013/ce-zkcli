/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.cc.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hyperic.sigar.Sigar;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import com.cc.monitor.MonitorService;

public class HttpClient extends Thread {

	private final URI uri;

	private final Sigar sigar = new Sigar();
	
	public static volatile boolean over = false;

	public HttpClient(URI uri) {
		this.uri = uri;

	}

	public void run() {
		String host = uri.getHost() == null ? "localhost" : uri.getHost();
		int port = uri.getPort();

		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("codec", new HttpClientCodec());

				pipeline.addLast("handler", new HttpClientHandler());
				return pipeline;
			}
		});

		// bootstrap.setOption("sendBufferSize", 1048576*10);
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.getRawPath());
		request.setHeader(HttpHeaders.Names.HOST, host);
		request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		request.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/x-www-form-urlencoded");

		

		MonitorService service = new MonitorService(sigar);
		Map<String, Object> map = service.statsMap();
		ObjectMapper mapper = new ObjectMapper();
		String message = null;
		try {
			message = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String params = "message=" + message;

		ChannelBuffer cb = ChannelBuffers.copiedBuffer(params, CharsetUtil.UTF_8);

		request.setHeader(HttpHeaders.Names.CONTENT_LENGTH, cb.readableBytes());

		request.setContent(cb);

		// Send the HTTP request.
		channel.write(request);

		channel.getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();

	}

	public static void main(String[] args) throws Exception {

		URI uri = new URI("http://localhost:8080/monitor/agent.do");

		new HttpClient(uri).start();

	}
}
