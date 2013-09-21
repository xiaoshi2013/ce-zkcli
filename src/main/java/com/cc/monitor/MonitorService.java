package com.cc.monitor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.hyperic.sigar.Sigar;

import com.cc.monitor.fs.FsProbe;
import com.cc.monitor.fs.FsStats;
import com.cc.monitor.fs.SigarFsProbe;
import com.cc.monitor.os.OsProbe;
import com.cc.monitor.os.OsStats;
import com.cc.monitor.os.SigarOsProbe;
import com.cc.monitor.process.ProcessProbe;
import com.cc.monitor.process.ProcessStats;
import com.cc.monitor.process.SigarProcessProbe;

public class MonitorService {

	private final OsProbe osProbe;

	private final ProcessProbe processProbe;

	private final FsProbe fsProbe;

	public MonitorService(Sigar sigar) {
		
		this.osProbe = new SigarOsProbe(sigar);
		this.processProbe = new SigarProcessProbe(sigar);
		this.fsProbe = new SigarFsProbe(sigar);
	}
	
	
	public Map<String, Object> statsMap(){
		ProcessStats processStats = processProbe.processStats();
		OsStats osStats=osProbe.osStats();
		FsStats fsStats=fsProbe.stats();
		
		Map<String, Object> map =new HashMap<String, Object>();
		
		map.put("processStats", processStats);
		map.put("osStats", osStats);
		map.put("fsStats", fsStats);
		
		
		
		
		return map;
	}
	
	
	public byte[] stats(){
		ProcessStats processStats = processProbe.processStats();
		OsStats osStats=osProbe.osStats();
		FsStats fsStats=fsProbe.stats();
		
		Map<String, Object> map =new HashMap<String, Object>();
		
		map.put("processStats", processStats);
		map.put("osStats", osStats);
		map.put("fsStats", fsStats);
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
		try {
			byte[] bytes=mapper.writeValueAsBytes(map);
			
			
			return bytes;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		Sigar sigar=new Sigar();
		
		MonitorService service=new MonitorService(sigar);
		
		service.stats();
		
	}

}
