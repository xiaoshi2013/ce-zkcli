package com.cc.zk.common;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.jackson.map.ObjectMapper;

import com.cc.zk.CeNodeProps;

public class Utils {

	public static long UPDATE_DELAY = 4;
	public static long timeout = 4;

	public static String getIP() {
		String ipstr = null;
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();// 返回所有网络接口的一个枚举实例

			while (e.hasMoreElements()) {
				NetworkInterface network = e.nextElement();// 获得当前网络接口
				if (network != null && network.isUp()) {

					List<InterfaceAddress> en = network.getInterfaceAddresses();

					Iterator<InterfaceAddress> it = en.iterator();
					while (it.hasNext()) {
						InterfaceAddress inter = it.next();
						String ip = inter.getAddress().getHostAddress();
						if (!determineIpAddress(ip)) {
							System.out.println("--InetAddress " + ip);
							ipstr = ip;
							break;

						}

					}

					if (ipstr != null) {
						break;
					}

				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipstr;
	}

	private static boolean determineIpAddress(String ipAddress) {

		/**
		 * Internet保留地址(10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16)
		 */
		// 10.0.0.0/8
		final String reservedAddress1 = "00001010";
		// 172.16.0.0/12
		final String reservedAddress2 = "101011000001";
		// 192.168.0.0/16
		final String reservedAddress3 = "1100000010101000";
		// 127.0.0.1
		final String reservedAddress4 = "01111111000000000000000000000001";

		StringBuilder result = new StringBuilder(32);
		/** 字符串的二进制形式 */
		String binString = "";
		StringTokenizer stringTokenizer = new StringTokenizer(ipAddress, ".");
		for (int i = 0; i < 4; i++) {
			try {
				binString = Integer.toBinaryString(Integer.parseInt(stringTokenizer.nextToken()));
			} catch (Exception e) {
				return true;
			}

			if (binString.length() > 8) {
				return true;
			}

			binString = "0000000000".substring(0, (8 - binString.length())).concat(binString);
			result.append(binString);
		}

		String sIPBinResult = result.toString();
		if (sIPBinResult.startsWith(reservedAddress1) || sIPBinResult.startsWith(reservedAddress2)
				|| sIPBinResult.startsWith(reservedAddress3) || sIPBinResult.equals(reservedAddress4)) {

			return true;
		}
		return false;
	}

	public static String getOwnPid() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName(); // format: "pid@hostname"

		System.out.println(name);

		return name.substring(0, name.indexOf('@'));
	}



	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadZkinitJSON(String jsonPath) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			Map<String, Object> result = mapper.readValue(new File(jsonPath), Map.class);
			System.out.println(result);

			return result;
		} catch (IOException e) {

			throw new RuntimeException(e);
		}

	}

	public static String getHostNameForLiunx() {
		try {
			return (InetAddress.getLocalHost()).getHostName();
		} catch (UnknownHostException uhe) {
			String host = uhe.getMessage(); // host = "hostname: hostname"
			if (host != null) {
				int colon = host.indexOf(':');
				if (colon > 0) {
					return host.substring(0, colon);
				}
			}
			return "UnknownHost";
		}
	}

	public static String getHostName() {
		if (System.getenv("COMPUTERNAME") != null) {
			return System.getenv("COMPUTERNAME");
		} else {
			String name = getHostNameForLiunx();
			System.out.println("hostname " + name);
			return name;
		}
	}

	public static void main(String[] args) {
		// Utils.loadZkinitJSON("D:\\workspaceN\\ce-zkcli\\src\\main\\resources\\zkinit.json");

		getIP();
		getHostName();
	}
}
