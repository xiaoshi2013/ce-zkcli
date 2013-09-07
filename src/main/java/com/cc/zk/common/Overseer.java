/*package com.cc.zk.common;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cc.zk.CeNodeProps;

public class Overseer {
	private static Logger log = LoggerFactory.getLogger(Overseer.class);

	public static final String QUEUE_OPERATION = "operation";
	public static final String DELETE = "delete";
	public static final String CREATE = "create";


	private final ZkClient zkClient;

														// state updates

	public static final ConcurrentLinkedQueue<ClusterState> stateQueue = new ConcurrentLinkedQueue<ClusterState>();

	public Overseer(final ZkStateReader reader) {

		this.reader = reader;
		this.zkClient = reader.getZkClient();
	}


	

	


	
	*//**
	 * 删除节点
	 * @param nodePropsList
	 * @throws KeeperException
	 * @throws InterruptedException
	 *//*
	private void deleteEphemeralLiveNode(List<CeNodeProps> nodePropsList) throws KeeperException, InterruptedException {
		for (int i = 0; i < nodePropsList.size(); i++) {
			String nodeName = nodePropsList.get(i).getNodeName();
			String nodePath = ZkStateReader.LIVE_NODES_ZKNODE + "/" + nodeName;
			log.info("will remove node in ZooKeeper:" + nodePath);

			try {
				boolean nodeDeleted = true;
				try {

					zkClient.delete(nodePath, -1, true);
				} catch (KeeperException.NoNodeException e) {
					nodeDeleted = false;
					log.info("NoNodeException when removing " + nodePath);
				}
				if (nodeDeleted) {
					log.info("Found node " + nodePath + " - remove it");
				}
			} catch (KeeperException e) {
				if (e.code() != KeeperException.Code.NODEEXISTS) {
					throw e;
				}
			}
		}

	}
	
	private void createEphemeralLiveNode(List<CeNodeProps> nodePropsList) throws KeeperException, InterruptedException {

		for (int i = 0; i < nodePropsList.size(); i++) {
			String nodeName = nodePropsList.get(i).getNodeName();
			String nodePath = ZkStateReader.LIVE_NODES_ZKNODE + "/" + nodeName;
			log.info("Register node as live in ZooKeeper:" + nodePath);

			try {
				boolean nodeDeleted = true;
				try {

					zkClient.delete(nodePath, -1, true);
				} catch (KeeperException.NoNodeException e) {
					// fine if there is nothing to delete
					// TODO: annoying that ZK logs a warning on us
					nodeDeleted = false;
				}
				if (nodeDeleted) {
					log.info("Found a previous node that still exists while trying to register a new live node "
							+ nodePath + " - removing existing node to create another.");
				}
				zkClient.makePath(nodePath, CreateMode.EPHEMERAL, true);
			} catch (KeeperException e) {
				// its okay if the node already exists
				if (e.code() != KeeperException.Code.NODEEXISTS) {
					throw e;
				}
			}
		}

	}


}
*/