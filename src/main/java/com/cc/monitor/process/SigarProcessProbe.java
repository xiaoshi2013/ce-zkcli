package com.cc.monitor.process;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SigarProcessProbe implements ProcessProbe {

	private final Sigar sigar;

	public SigarProcessProbe(Sigar sigar) {
		this.sigar = sigar;

	}

	public synchronized ProcessStats processStats() {

		ProcessStats stats = new ProcessStats();
		stats.timestamp = System.currentTimeMillis();
		stats.openFileDescriptors = JmxProcessProbe.getOpenFileDescriptorCount();

		try {
			ProcCpu cpu = sigar.getProcCpu(sigar.getPid());
			stats.cpu = new ProcessStats.Cpu();
			stats.cpu.percent = (short) (cpu.getPercent() * 100);
			stats.cpu.sys = cpu.getSys();
			stats.cpu.user = cpu.getUser();
			stats.cpu.total = cpu.getTotal();

		} catch (SigarException e) {

		}

		try {
			ProcMem mem = sigar.getProcMem(sigar.getPid());
			stats.mem = new ProcessStats.Mem();
			stats.mem.totalVirtual = mem.getSize();
			stats.mem.resident = mem.getResident();
			stats.mem.share = mem.getShare();
		} catch (SigarException e) {
			// ignore
		}
		return stats;
	}

	@Override
	public ProcessInfo processInfo() {
		return new ProcessInfo(sigar.getPid(), JmxProcessProbe.getMaxFileDescriptorCount());
	}

}
