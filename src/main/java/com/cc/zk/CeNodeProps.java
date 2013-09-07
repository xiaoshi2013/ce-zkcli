package com.cc.zk;

import java.util.HashMap;
import java.util.Map;

import com.cc.zk.common.ZkStateReader;

/**
 * 节点属性
 * @author zhanglei
 *
 */
public class CeNodeProps {

	public static final String NODENAME="nodeName";
	public static final String IP="ip";
	public static final String STATE="state";
	public static final String HOSTPORT="hostPort";
	public static final String HOSTNAME="hostName";
	public static final String PID="pid";
	public static final String SERVICEGROUP="serviceGroup";
	public static final String SERVICEID="serviceID";
	public static final String FTPDIR="ftpDir";
	
	
	private String nodeName; // 节点名称标识 必须
	private String hostName; // 节点hostname
	private String hostPort; // 节点端口 同一节点 可多个端口 逗号隔开
	private String ip; // 节点 ip地址 必须
	private String serviceGroup; // 节点所属的服务名称 必须
	private String state; // 节点状态 必须
	private String pid; // java进程id
	private String serviceID; // 服务id 标识用
	private String ftpDir; // ftp文件目录

	public CeNodeProps(Map<String, Object> propMap) {
		this.nodeName = propMap.get(NODENAME).toString();
		this.hostName = (String)propMap.get(HOSTNAME);
		this.ip = propMap.get(IP).toString();
		this.serviceGroup = propMap.get(SERVICEGROUP).toString();
		this.state = (String)propMap.get(STATE);
		// this.operation = propMap.get("operation").toString();
		this.hostPort = (String)propMap.get(HOSTPORT);
		this.pid = (String)propMap.get(PID);
		this.serviceID = (String)propMap.get(SERVICEID);
		this.ftpDir = (String)propMap.get(FTPDIR);

	}



	public static CeNodeProps load(byte[] bytes) {
		Map<String, Object> props = (Map<String, Object>) ZkStateReader.fromJSON(bytes, HashMap.class);
		return new CeNodeProps(props);

	}
	
	public Map<String, String> toMap(){
		Map<String, String> map=new HashMap<String, String>();
		map.put(NODENAME, this.nodeName);
		map.put(IP, this.ip);
		map.put(STATE, this.state);
		map.put(HOSTPORT, this.hostPort);
		map.put(SERVICEGROUP, this.serviceGroup);
		map.put(SERVICEID, this.serviceID);
		map.put(HOSTNAME, this.hostName);
		map.put(PID, this.pid);
		map.put(FTPDIR, this.ftpDir);
		return map;
		
	}

	public String getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(String serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getHostPort() {
		return hostPort;
	}

	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ipStr) {
		this.ip = ipStr;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getPid() {
		return pid;
	}
	
	public String getFtpDir() {
		return ftpDir;
	}

	public void setFtpDir(String ftpDir) {
		this.ftpDir = ftpDir;
	}
}
