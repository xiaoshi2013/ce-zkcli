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

package com.cc.monitor.os;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

/**
 *
 */
public class SigarOsProbe implements OsProbe {

	private final Sigar sigar;

    public SigarOsProbe(Sigar sigar) {
      
        this.sigar=sigar;
    }

    @Override
    public OsInfo osInfo() {
        OsInfo info = new OsInfo();
        try {
            CpuInfo[] infos = sigar.getCpuInfoList();
            info.cpu = new OsInfo.Cpu();
            info.cpu.vendor = infos[0].getVendor();
            info.cpu.model = infos[0].getModel();
            info.cpu.mhz = infos[0].getMhz();
            info.cpu.totalCores = infos[0].getTotalCores();
            info.cpu.totalSockets = infos[0].getTotalSockets();
            info.cpu.coresPerSocket = infos[0].getCoresPerSocket();
            if (infos[0].getCacheSize() != Sigar.FIELD_NOTIMPL) {
                info.cpu.cacheSize = infos[0].getCacheSize();
            }
        } catch (SigarException e) {
            // ignore
        }

        try {
            Mem mem = sigar.getMem();
            info.mem = new OsInfo.Mem();
            info.mem.total = mem.getTotal();
        } catch (SigarException e) {
            // ignore
        }

        try {
            Swap swap = sigar.getSwap();
            info.swap = new OsInfo.Swap();
            info.swap.total = swap.getTotal();
        } catch (SigarException e) {
            // ignore
        }


        return info;
    }

    @Override
    public OsStats osStats() {
        OsStats stats = new OsStats();
        stats.timestamp = System.currentTimeMillis();
        try {
            stats.loadAverage = sigar.getLoadAverage();
        } catch (SigarException e) {
          
        }

        try {
            stats.uptime = (long) sigar.getUptime().getUptime();
        } catch (SigarException e) {
            // ignore
        }

        try {
            CpuPerc cpuPerc = sigar.getCpuPerc();
            stats.cpu = new OsStats.Cpu();
           
            
            stats.cpu.sys = (short) (cpuPerc.getSys() * 100);
            stats.cpu.user = (short) (cpuPerc.getUser() * 100);
            stats.cpu.idle = (short) (cpuPerc.getIdle() * 100);
            stats.cpu.stolen = (short) (cpuPerc.getStolen() * 100);
        } catch (SigarException e) {
            // ignore
        }

        try {
            Mem mem = sigar.getMem();
            stats.mem = new OsStats.Mem();
            stats.mem.free = mem.getFree();
            stats.mem.freePercent = (short) mem.getFreePercent();
            stats.mem.used = mem.getUsed();
            stats.mem.usedPercent = (short) mem.getUsedPercent();
         
        
        } catch (SigarException e) {
            // ignore
        }

        try {
            Swap swap = sigar.getSwap();
            stats.swap = new OsStats.Swap();
            stats.swap.free = swap.getFree();
            stats.swap.used = swap.getUsed();
        
        } catch (SigarException e) {
           e.printStackTrace();
        }

        return stats;
    }
}
