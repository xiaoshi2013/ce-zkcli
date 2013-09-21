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

import java.io.Serializable;

/**
 *
 */
public class OsStats implements Serializable {

    public static final double[] EMPTY_LOAD = new double[0];


    long timestamp;

    double[] loadAverage = EMPTY_LOAD;

    long uptime = -1;

    Cpu cpu = null;

    Mem mem = null;

    Swap swap = null;

    OsStats() {
    }

  

    public long getTimestamp() {
        return timestamp;
    }

  

    public double[] getLoadAverage() {
        return loadAverage;
    }

    public Cpu getCpu() {
        return cpu;
    }



    public Mem getMem() {
        return mem;
    }


    public Swap getSwap() {
        return swap;
    }

   


    public static class Swap implements Serializable {

        long free = -1;
        long used = -1;
        public long getFree() {
			return free;
		}
		public long getUsed() {
			return used;
		}
		

       
    }

    public static class Mem implements Serializable {

        long free = -1;
        short freePercent = -1;
        long used = -1;
        short usedPercent = -1;
     
        public long getFree() {
			return free;
		}

		public long getUsed() {
			return used;
		}


		public short getUsedPercent() {
			return usedPercent;
		}


		public short getFreePercent() {
            return freePercent;
        }
    }

    public static class Cpu implements Serializable {

        short sys = -1;
        short user = -1;
        short idle = -1;
        short stolen = -1;

      

        public short sys() {
            return sys;
        }

        public short getSys() {
            return sys();
        }

        public short user() {
            return user;
        }

        public short getUser() {
            return user();
        }

        public short idle() {
            return idle;
        }

        public short getIdle() {
            return idle();
        }

        public short stolen() {
            return stolen;
        }

        public short getStolen() {
            return stolen();
        }
    }
}
