package com.cc.monitor.process;

import java.io.Serializable;

public class ProcessStats {

	  long timestamp = -1;

	    long openFileDescriptors;

	   

		Cpu cpu = null;

	    Mem mem = null;
	    
	    ProcessStats() {
	    }
	    

	    public long getTimestamp() {
	        return timestamp;
	    }

	 
	    public long getOpenFileDescriptors() {
			return openFileDescriptors;
		}
	   
	  
	    public Cpu getCpu() {
	        return cpu;
	    }

	  
	    public Mem getMem() {
	        return mem;
	    }
	    
	    public static class Mem implements Serializable {

	        long totalVirtual = -1;
	        long resident = -1;
	        long share = -1;
			public long getTotalVirtual() {
				return totalVirtual;
			}
			public long getResident() {
				return resident;
			}
			public long getShare() {
				return share;
			}


	    }
	    
	    public static class Cpu implements Serializable {

	        short percent = -1;
	        long sys = -1;
	        long user = -1;
	        long total = -1;
			public short getPercent() {
				return percent;
			}
			public long getSys() {
				return sys;
			}
			public long getUser() {
				return user;
			}
			public long getTotal() {
				return total;
			}

	     

	       

	      
	        
	      
	     
	    }

}
