/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.cc.monitor.fs;

import java.io.File;
import java.util.Map;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.SigarCommandBase;

import com.google.common.collect.Maps;

/**
 */
public class SigarFsProbe extends SigarCommandBase  implements FsProbe {

  
	private final Sigar sigar;

    private Map<File, FileSystem> fileSystems = Maps.newHashMap();

 
    public SigarFsProbe(Sigar sigar) {
       
        this.sigar = sigar;
    }

    @Override
    public synchronized FsStats stats() {
		FileSystem[] fslist;
		try {
			fslist = this.proxy.getFileSystemList();
		} catch (SigarException e1) {
			e1.printStackTrace();
			return null;
		}
    	  
    	  File[] dataLocations =new File[fslist.length];
    	  for (int i=0;i<fslist.length; i++) {
    		File dataPath= new File(  fslist[i].getDevName());
    		dataLocations[i]=dataPath;
		}
        FsStats.Info[] infos = new FsStats.Info[dataLocations.length];
        for (int i = 0; i < dataLocations.length; i++) {
            File dataLocation = dataLocations[i];

            FsStats.Info info = new FsStats.Info();
            info.path = dataLocation.getAbsolutePath();

            try {
                FileSystem fileSystem = fileSystems.get(dataLocation);
              
                if (fileSystem == null) {
                    FileSystemMap fileSystemMap = sigar.getFileSystemMap();
                    if (fileSystemMap != null) {
                        fileSystem = fileSystemMap.getMountPoint(dataLocation.getPath());
                        fileSystems.put(dataLocation, fileSystem);
                    }
                }
                if (fileSystem != null) {
                    info.mount = fileSystem.getDirName();
                    info.dev = fileSystem.getDevName();
                    FileSystemUsage fileSystemUsage = sigar.getFileSystemUsage(fileSystem.getDirName());
                    if (fileSystemUsage != null) {
                        // total/free/available seem to be in megabytes?
                        info.totalKB = fileSystemUsage.getTotal();
                        info.freeKB = fileSystemUsage.getFree() ;
                        info.availableKB = fileSystemUsage.getAvail();
                        info.reads = fileSystemUsage.getDiskReads();
                        info.writes = fileSystemUsage.getDiskWrites();
                        info.readKB = fileSystemUsage.getDiskReadBytes()/1024;
                        info.writeKB = fileSystemUsage.getDiskWriteBytes()/1024;
                        info.diskQueue = fileSystemUsage.getDiskQueue();
                        info.diskServiceTime = fileSystemUsage.getDiskServiceTime();
                    }
                }
            } catch (SigarException e) {
                // failed...
            }

            infos[i] = info;
        }

        return new FsStats(System.currentTimeMillis(), infos);
    }

	@Override
	public void output(String[] arg0) throws SigarException {
		
		
	}
}
