package com.cc.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.zookeeper.KeeperException;
import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cc.monitor.MonitorService;
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

	private boolean isOpen = true;

	private final Set<String> serviceNames = new HashSet<String>();

	private static Logger _log = LoggerFactory.getLogger(NodeCoreAction.class);

	private final AtomicBoolean isUpdate = new AtomicBoolean();
	
	private ZkController zkController;

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

		
	}
	

	public static synchronized NodeCoreAction getInstance(String jsonPath) {
		if (singleton == null) {
			singleton = new NodeCoreAction(jsonPath);
			singleton.init();
		}
		return singleton;

	}

	
	private void init() {

		Map<String, Object> jsonMap = Utils.loadZkinitJSON(jsonPath);

		List<CeNodeProps> nodeList = new ArrayList<CeNodeProps>();

		String zkUrl = (String) jsonMap.remove(ZkStateReader.ZKURL);

		if (zkUrl == null || zkUrl.equals("")) {
			throw new RuntimeException("zkUrl is null ");
		}

		_log.info("zkUrl " + zkUrl);

		
		String serviceName = (String) jsonMap.get("service");
		if(serviceName==null || serviceName.equals("")){
			throw new RuntimeException("serviceName is null");
		}
		
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

		zkController=new ZkController(zkUrl, ZkStateReader.ZKCLIENTTIMEOUT,
				ZkStateReader.ZKCLIENTCONNECTTIMEOUT, nodeList);
		
		
		
		
		try {
			zkController.sendState(obtainStatus());
		} catch (KeeperException e) {
			_log.error("error",e);
		} catch (InterruptedException e) {
			_log.error("error",e);
		}

	}

	
	private byte[] obtainStatus(){

		Sigar sigar=new Sigar();
		
		MonitorService service=new MonitorService(sigar);
		
		return service.stats();
		
		
	}

	public static void main(String[] args) {

		NodeCoreAction action= NodeCoreAction.getInstance("D:\\workspaceN\\ce-zkcli\\src\\main\\resources\\zkinit.json");

		 while(true){
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
		
				Set<String> set=action.zkController.getZkStateReader().getLiveNodes();
				
			System.out.println("WebNodeCoreAction.main() "+set);
				
			}

	
	}

}
