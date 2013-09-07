package com.cc.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cc.zk.common.Utils;
import com.cc.zk.common.ZkController;
import com.cc.zk.common.ZkStateReader;

/**
 * 每个jvm起一个NodeCoreAction负责
 * 
 * @author zhanglei
 * 
 */
public class NodeCoreAction {

	private static NodeCoreAction singleton;

	private final String jsonPath;

	private ZkController zkController;


	public static final String ZKURL = "zkUrl";
	// private int num = 6;

	// private int pingTimeout = 30000;

	/**
	 * key service名城 value是同一service下节点的集合
	 */
	private final ConcurrentHashMap<String, Set<Map>> serviceMap = new ConcurrentHashMap<String, Set<Map>>(); // 可用的服务节点

	private boolean isOpen = true;

	private final Set<String> serviceNames = new HashSet<String>();

	private static Logger _log = LoggerFactory.getLogger(NodeCoreAction.class);

	private final AtomicBoolean isUpdate = new AtomicBoolean();


	public AtomicBoolean getIsUpdate() {
		return isUpdate;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Set<String> getServiceNames() {
		return serviceNames;
	}

	private NodeCoreAction(String jsonPath) {
		this.jsonPath = jsonPath;

		init();
	}

	public static synchronized NodeCoreAction getInstance(String jsonPath) {
		if (singleton == null) {
			singleton = new NodeCoreAction(jsonPath);
		}
		return singleton;

	}

	
	private void init() {

		Map<String, Object> jsonMap = Utils.loadZkinitJSON(jsonPath);

		List<CeNodeProps> nodeList = new ArrayList<CeNodeProps>();

		String zkUrl = (String) jsonMap.remove(ZKURL);

		if (zkUrl == null || zkUrl.equals("")) {
			throw new RuntimeException("zkUrl is null ");
		}

		_log.info("zkUrl " + zkUrl);

		
		String serviceName = (String) jsonMap.get("service");
		if(serviceName==null || serviceName.equals("")){
			throw new RuntimeException("serviceName is null");
		}
		ZkStateReader.service=serviceName;
		
		String ip = Utils.getIP();
		ip="127.0.0.1";
		if(ip==null || ip.equals("")){
			throw new RuntimeException("ip is null");
		}
		String hostname = Utils.getHostName();
		if(ip==null || ip.equals("")){
			throw new RuntimeException("hostname is null");
		}
		
		String nodeName = serviceName+"&"+hostname+"&"+ip;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CeNodeProps.SERVICEGROUP, serviceName);
		map.put(CeNodeProps.IP, ip);
		map.put(CeNodeProps.HOSTNAME, hostname);
		map.put(CeNodeProps.NODENAME, nodeName);
		map.put(CeNodeProps.STATE, ZkStateReader.ACTIVE);
		
		CeNodeProps nodeProps = new CeNodeProps(map);
		nodeList.add(nodeProps);

		System.out.println(nodeList);

		

		zkController = new ZkController(zkUrl, ZkStateReader.ZKCLIENTTIMEOUT,
				ZkStateReader.ZKCLIENTCONNECTTIMEOUT, nodeList);

	
	}


	public static void main(String[] args) {

		 NodeCoreAction.getInstance("D:\\workspaceN\\ce-zkcli\\src\\main\\resources\\zkinit.json");

	
	}

}
