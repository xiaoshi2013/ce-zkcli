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


import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;


public class HttpClientHandler extends SimpleChannelUpstreamHandler {

	private static Logger _LOG = Logger.getLogger(HttpClientHandler.class);
    
    public HttpClientHandler( ) {
       
               
    }
 

    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, final ChannelStateEvent e) {

    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
    	
    	   HttpResponse response = (HttpResponse) e.getMessage();
    		
    	   _LOG.info("STATUS: " + response.getStatus());
    	   _LOG.info("VERSION: " + response.getProtocolVersion());

           if (!response.getHeaderNames().isEmpty()) {
               for (String name: response.getHeaderNames()) {
                   for (String value: response.getHeaders(name)) {
                	   _LOG.info("HEADER: " + name + " = " + value);
                   }
               }
           }
           
           ChannelBuffer content = response.getContent();
           
           System.out.println("--"+ new String(content.array()));
   
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
    	_LOG.warn("Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
}
