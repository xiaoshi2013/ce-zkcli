package com.cc.zk.common;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cc.zk.CeNodeProps;


/**
 * Handle ZooKeeper interactions.
 * @author zhanglei
 *
 */
public class ZkController {

	private static Logger log = LoggerFactory.getLogger(ZkController.class);

	private ZkClient zkClient;
	private ZkStateReader zkStateReader;

	private ZkCmdExecutor zkCmdExecutor;

	private List<CeNodeProps> nodePropsList;


	public ZkController(String zkServerAddress, int zkClientTimeout, int zkClientConnectTimeout,
			List<CeNodeProps> nodePropsList) {
		this.zkClient = new ZkClient(zkServerAddress, zkClientTimeout, zkClientConnectTimeout,
		// on reconnect, reload cloud info
				new OnReconnect() {

					@Override
					public void command() {
						System.out.println("ZkController.ZkController(...).new OnReconnect() {...}.command()");
						try {
							//zkStateReader.createClusterStateWatchersAndUpdate();

							createEphemeralLiveNode();

						
						} catch (Exception e) {
							CeException.log(log, "", e);
							throw new ZooKeeperException(CeException.ErrorCode.SERVER_ERROR, "", e);
						}

					}
				});

		this.zkCmdExecutor = new ZkCmdExecutor((int) (zkClient.getZkClientTimeout() / 1000.0 + 3000));
		this.nodePropsList = nodePropsList;
		zkStateReader = new ZkStateReader(zkClient);
		this.init();
	}

	public boolean pathExists(String path) throws KeeperException, InterruptedException {
		return zkClient.exists(path, true);
	}

	private void createEphemeralLiveNode() throws KeeperException, InterruptedException {

		for (int i = 0; i < nodePropsList.size(); i++) {
			String nodeName = nodePropsList.get(i).getNodeName();
			String nodePath = ZkStateReader.LIVE_NODES_ZKNODE + "/"+ nodePropsList.get(i).getServiceGroup()+"/" + nodeName;
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

	

	/**
	 * 将本节点注册到zk上
	 */
	private void init() {
		try {
			zkCmdExecutor.ensureExists(ZkStateReader.LIVE_NODES_ZKNODE, zkClient);
			createEphemeralLiveNode();
		} catch (KeeperException e) {
			log.error("", e);
			throw new ZooKeeperException(CeException.ErrorCode.SERVER_ERROR, "", e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("", e);
			throw new ZooKeeperException(CeException.ErrorCode.SERVER_ERROR, "", e);
		}

	}

	public ZkStateReader getZkStateReader() {
		return zkStateReader;
	}


	

}
