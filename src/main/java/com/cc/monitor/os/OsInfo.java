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

import java.io.IOException;
import java.io.Serializable;

import org.omg.CORBA.portable.Streamable;

/**
 *
 */
public class OsInfo implements Serializable {

    long refreshInterval;

    int availableProcessors;

    Cpu cpu = null;

    Mem mem = null;

    Swap swap = null;

    OsInfo() {
    }

    public long refreshInterval() {
        return this.refreshInterval;
    }

    public long getRefreshInterval() {
        return this.refreshInterval;
    }

    public int availableProcessors() {
        return this.availableProcessors;
    }

    public int getAvailableProcessors() {
        return this.availableProcessors;
    }

    public Cpu cpu() {
        return this.cpu;
    }

    public Cpu getCpu() {
        return cpu();
    }

    public Mem mem() {
        return this.mem;
    }

    public Mem getMem() {
        return mem();
    }

    public Swap swap() {
        return this.swap;
    }

    public Swap getSwap() {
        return swap();
    }

    public static class Swap implements Serializable {

        long total = -1;

        Swap() {

        }
    }

    public static class Mem implements Serializable {

        long total = -1;

        Mem() {

        }

    }

    public static class Cpu implements Serializable {

        String vendor = "";
        String model = "";
        int mhz = -1;
        int totalCores = -1;
        int totalSockets = -1;
        int coresPerSocket = -1;
        long cacheSize = -1;

        Cpu() {

        }

        public String vendor() {
            return this.vendor;
        }

        public String getVendor() {
            return vendor();
        }

        public String model() {
            return model;
        }

        public String getModel() {
            return model;
        }

        public int mhz() {
            return mhz;
        }

        public int getMhz() {
            return mhz;
        }

        public int totalCores() {
            return totalCores;
        }

        public int getTotalCores() {
            return totalCores();
        }

        public int totalSockets() {
            return totalSockets;
        }

        public int getTotalSockets() {
            return totalSockets();
        }

        public int coresPerSocket() {
            return coresPerSocket;
        }

        public int getCoresPerSocket() {
            return coresPerSocket();
        }

    }
}
