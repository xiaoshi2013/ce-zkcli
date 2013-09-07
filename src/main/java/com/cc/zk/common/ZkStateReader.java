package com.cc.zk.common;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkStateReader {
	private static Logger log = LoggerFactory.getLogger(ZkStateReader.class);

	public static final String LIVE_NODES_ZKNODE = "/ce_live_nodes";

	public static final String ESCLIENT = "esclient";
	public static final String AGENT = "agent";
	public static final String REDIS = "redis";
	public static final String FTP_SERVER = "ftp_server";
	public static final String FTP_MONITOR = "ftp_monitor";

	//private volatile ClusterState clusterState;

	//public static final String CLUSTER_STATE = "/ce_clusterstate.json";

	public static final String ACTIVE = "active";
	public static final String DOWN = "down";
	public static final String SYNC = "sync";

	public static final int ZKCLIENTTIMEOUT = 15000;
	public static final int ZKCLIENTCONNECTTIMEOUT = 15000;
	
	public static String service = null;

	public static final String SERVICE_ZKNODE = LIVE_NODES_ZKNODE + "/" + service;

	private static class ZKTF implements ThreadFactory {
		private static ThreadGroup tg = new ThreadGroup("ZkStateReader");

		@Override
		public Thread newThread(Runnable r) {
			Thread td = new Thread(tg, r);
			td.setDaemon(true);
			return td;
		}
	}

	private ZkClient zkClient;

	private boolean closeClient = false;

	private ZkCmdExecutor cmdExecutor;

	private volatile boolean closed = false;

	public ZkStateReader(ZkClient zkClient) {
		this.zkClient = zkClient;
		initZkCmdExecutor(zkClient.getZkClientTimeout());
	}

	private void initZkCmdExecutor(int zkClientTimeout) {
		// we must retry at least as long as the session timeout
		cmdExecutor = new ZkCmdExecutor(zkClientTimeout);
	}


	public Object getUpdateLock() {
		return this;
	}

	public void close() {
		this.closed = true;
		if (closeClient) {
			zkClient.close();
		}
	}

	abstract class RunnableWatcher implements Runnable {
		Watcher watcher;

		public RunnableWatcher(Watcher watcher) {
			this.watcher = watcher;
		}

	}

	public ZkClient getZkClient() {
		return zkClient;
	}

/*	public synchronized void createClusterStateWatchersAndUpdate() throws KeeperException, InterruptedException {
		synchronized (ZkStateReader.this.getUpdateLock()) {
			List<String> liveNodes = zkClient.getChildren(LIVE_NODES_ZKNODE, new Watcher() {

				@Override
				public void process(WatchedEvent event) {
					// session events are not change events,
					// and do not remove the watcher
					if (EventType.None.equals(event.getType())) {
						return;
					}
					try {

						synchronized (ZkStateReader.this.getUpdateLock()) {
							List<String> liveNodes = zkClient.getChildren(LIVE_NODES_ZKNODE, this, true);

							Set<String> liveNodesSet = new HashSet<String>();
							liveNodesSet.addAll(liveNodes);
							ClusterState clusterState = new ClusterState(liveNodesSet);
							ZkStateReader.this.clusterState = clusterState;
						}
					} catch (KeeperException e) {
						if (e.code() == KeeperException.Code.SESSIONEXPIRED
								|| e.code() == KeeperException.Code.CONNECTIONLOSS) {
							log.warn("ZooKeeper watch triggered, but Solr cannot talk to ZK");
							return;
						}
						log.error("", e);
						throw new ZooKeeperException(CeException.ErrorCode.SERVER_ERROR, "", e);
					} catch (InterruptedException e) {
						// Restore the interrupted status
						Thread.currentThread().interrupt();
						log.warn("", e);
						return;
					}
				}

			}, true);

			Set<String> liveNodeSet = new HashSet<String>();
			liveNodeSet.addAll(liveNodes);
			ClusterState clusterState = new ClusterState(liveNodeSet);
			this.clusterState = clusterState;
			log.info("--" + this.clusterState);

		}
	}
*/
	/**
	 * TODO 获得活着的节点集合
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void getLiveNodes() throws KeeperException, InterruptedException {
		List<String> liveNodes = zkClient.getChildren(ZkStateReader.LIVE_NODES_ZKNODE, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// session events are not change events,
				// and do not remove the watcher
				if (EventType.None.equals(event.getType())) {
					return;
				}
				try {
					// delayed approach
					// ZkStateReader.this.updateClusterState(false,
					// true);
					synchronized (ZkStateReader.this.getUpdateLock()) {
						List<String> liveNodes = zkClient.getChildren(LIVE_NODES_ZKNODE, this, true);
						log.info("Updating live nodes... ({})", liveNodes.size());
						Set<String> liveNodesSet = new HashSet<String>();
						liveNodesSet.addAll(liveNodes);

						ClusterState cstate = new ClusterState(liveNodesSet);

						ZkStateReader.this.clusterState = cstate;

					}
				} catch (KeeperException e) {
					if (e.code() == KeeperException.Code.SESSIONEXPIRED
							|| e.code() == KeeperException.Code.CONNECTIONLOSS) {
						log.warn("ZooKeeper watch triggered, but Solr cannot talk to ZK");
						return;
					}
					log.error("", e);
					throw new ZooKeeperException(CeException.ErrorCode.SERVER_ERROR, "", e);
				} catch (InterruptedException e) {
					// Restore the interrupted status
					Thread.currentThread().interrupt();
					log.warn("", e);
					return;
				}
			}

		}, true);
	}

	public static Object fromJSON(byte[] utf8, Class clazz) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			Object result = mapper.readValue(new String(utf8, "UTF-8"), clazz);
			return result;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static byte[] toJSON(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			byte[] bytes = mapper.writeValueAsBytes(o);
			return bytes;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
