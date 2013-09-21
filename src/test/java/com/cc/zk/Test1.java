package com.cc.zk;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;


public class Test1 {
	
	
	private Map<String,String> testMap=new HashMap<String, String>();
	
	public int getOwnPid(){
		   RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
	        String name = runtime.getName(); // format: "pid@hostname"
	        
	        System.out.println(name);
	        
	        return Integer.parseInt(name.substring(0, name.indexOf('@')));
	}
	
	
	public static void testConcurrentLinkedQueue(){
		
		ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue();
		
		for (int i = 0; i < 10; i++) {
			queue.offer("a"+i);
		}
		while(true){
			
			if(!queue.isEmpty()){
				String str=queue.poll();
				System.out.println(str);
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
			/*else{
				System.out.println(queue);
			}*/
		}
		
	}

	
	public static void testRemoveSet(){
		
		Map<String,String> nodeStates=new HashMap<String, String>();
		nodeStates.put("1", "a");
		nodeStates.put("2", "aas");
		nodeStates.put("3", "sas");
		nodeStates.put("4", "sds");
		nodeStates.put("5", "sds");
		nodeStates.put("6", "sds");
		
		
		Set<String> keys=nodeStates.keySet();
		
		Set<String> tempKeys=new HashSet<String>(keys);
		Set<String> liveNodesSet=new HashSet<String>(keys);
		liveNodesSet.remove("1");
		liveNodesSet.remove("2");
		liveNodesSet.remove("3");
		
		System.out.println("tempKeys "+tempKeys.size());
		System.out.println("nodeStates "+nodeStates.size());
		
		for (String node : tempKeys) {
			if(!liveNodesSet.contains(node)){
				
				nodeStates.remove(node);
			}
			
		}
		
		System.out.println(liveNodesSet.size());
		System.out.println(nodeStates);
	}
	
	
	public static void loadZkinitJSON(String jsonPath){
		String str=Thread.currentThread().getContextClassLoader().getResource("//").getPath();
		
		
		System.out.println(str);
	//	System.out.println(System.getenv());
		
		String dir= System.getProperty("user.dir");
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			List result = (List)mapper.readValue(new File(jsonPath),List.class);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}
	
	
	
	public void testMap( Map map){
		
		
		
		map=changeMap();
		
		System.out.println(map);
		
	}
	
	private Map<String,String> changeMap(){
		Map<String,String> map1=new HashMap<String, String>();
		
		map1.put("aaa", "1");
		
		return map1;
	}
	
	public static void main(String[] args) throws Exception {
		
		//testConcurrentLinkedQueue();
		//loadZkinitJSON("D:\\workspaceN\\ce-zkcli\\src\\main\\resources\\zkinit.json");
		
		Map<String,Object> map=new HashMap<String, Object>();
		for (int i = 0; i <10; i++) {
			String str="aaa"+i;
			
			map.put(str, new HashMap());
		}
		
		Map<String,Object> map1=new HashMap<String, Object>();
		for (int i = 0; i <10; i++) {
			String str="aaa"+i;
			
			map1.put(str, new HashMap());
		}
		
		System.out.println(map.equals(map1));
		System.out.println(map==map1);
		
		
		Test1 test1=new Test1();
		test1.testMap(test1.testMap);
		System.out.println(test1.testMap);
		
		
		
		
		
	}
}
