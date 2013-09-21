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


/**
 */
public class FsStats {

	public static class Info {

		String path;

		String mount;

		String dev;

		long totalKB = -1;
		long freeKB = -1;
		long availableKB = -1;
		long reads = -1;
		long writes = -1;
		long readKB = -1;
		long writeKB = -1;
		double diskQueue = -1;
		double diskServiceTime = -1;

		public String getPath() {
			return path;
		}

		public String getMount() {
			return mount;
		}

		public String getDev() {
			return dev;
		}

		public long getTotalKB() {
			return totalKB;
		}

		public long getFreeKB() {
			return freeKB;
		}

		public long getAvailableKB() {
			return availableKB;
		}

		public long getReadKB() {
			return readKB;
		}

		public long getWriteKB() {
			return writeKB;
		}

		public long getReads() {
			return this.reads;
		}

		public long getWrites() {
			return this.writes;
		}

		public double getDiskQueue() {
			return diskQueue;
		}

		public double getDiskServiceTime() {
			return diskServiceTime;
		}

	}

	long timestamp;
	Info[] infos;

	public Info[] getInfos() {
		return infos;
	}

	FsStats() {

	}

	FsStats(long timestamp, Info[] infos) {
		this.timestamp = timestamp;
		this.infos = infos;
	}

	public long getTimestamp() {
		return timestamp;
	}

	

}
