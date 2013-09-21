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

package com.cc.monitor.process;

import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.portable.Streamable;

/**
 *
 */
public class JvmInfo implements Serializable {

    private static JvmInfo INSTANCE;

    static {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        // returns the <process id>@<host>
        long pid;
        String xPid = runtimeMXBean.getName();
        try {
            xPid = xPid.split("@")[0];
            pid = Long.parseLong(xPid);
        } catch (Exception e) {
            pid = -1;
        }
        JvmInfo info = new JvmInfo();
        info.pid = pid;
        info.startTime = runtimeMXBean.getStartTime();
        info.version = runtimeMXBean.getSystemProperties().get("java.version");
        info.vmName = runtimeMXBean.getVmName();
        info.vmVendor = runtimeMXBean.getVmVendor();
        info.vmVersion = runtimeMXBean.getVmVersion();
      
    
        info.inputArguments = runtimeMXBean.getInputArguments().toArray(new String[runtimeMXBean.getInputArguments().size()]);
        info.bootClassPath = runtimeMXBean.getBootClassPath();
        info.classPath = runtimeMXBean.getClassPath();
        info.systemProperties = runtimeMXBean.getSystemProperties();

        INSTANCE = info;
    }

    public static JvmInfo jvmInfo() {
        return INSTANCE;
    }

    long pid = -1;

    String version = "";
    String vmName = "";
    String vmVersion = "";
    String vmVendor = "";

    long startTime = -1;

    String[] inputArguments;

    String bootClassPath;

    String classPath;

    Map<String, String> systemProperties;

    private JvmInfo() {
    }

    /**
     * The process id.
     */
    public long pid() {
        return this.pid;
    }

    /**
     * The process id.
     */
    public long getPid() {
        return pid;
    }

    public String version() {
        return this.version;
    }

    public String getVersion() {
        return this.version;
    }

    public int versionAsInteger() {
        try {
            int i = 0;
            String sVersion = "";
            for (; i < version.length(); i++) {
                if (!Character.isDigit(version.charAt(i)) && version.charAt(i) != '.') {
                    break;
                }
                if (version.charAt(i) != '.') {
                    sVersion += version.charAt(i);
                }
            }
            if (i == 0) {
                return -1;
            }
            return Integer.parseInt(sVersion);
        } catch (Exception e) {
            return -1;
        }
    }

    public int versionUpdatePack() {
        try {
            int i = 0;
            String sVersion = "";
            for (; i < version.length(); i++) {
                if (!Character.isDigit(version.charAt(i)) && version.charAt(i) != '.') {
                    break;
                }
                if (version.charAt(i) != '.') {
                    sVersion += version.charAt(i);
                }
            }
            if (i == 0) {
                return -1;
            }
            Integer.parseInt(sVersion);
            int from;
            if (version.charAt(i) == '_') {
                // 1.7.0_4
                from = ++i;
            } else if (version.charAt(i) == '-' && version.charAt(i + 1) == 'u') {
                // 1.7.0-u2-b21
                i = i + 2;
                from = i;
            } else {
                return -1;
            }
            for (; i < version.length(); i++) {
                if (!Character.isDigit(version.charAt(i)) && version.charAt(i) != '.') {
                    break;
                }
            }
            if (from == i) {
                return -1;
            }
            return Integer.parseInt(version.substring(from, i));
        } catch (Exception e) {
            return -1;
        }
    }

    public String vmName() {
        return vmName;
    }

    public String getVmName() {
        return vmName;
    }

    public String vmVersion() {
        return vmVersion;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public String vmVendor() {
        return vmVendor;
    }

    public String getVmVendor() {
        return vmVendor;
    }

    public long startTime() {
        return startTime;
    }

    public long getStartTime() {
        return startTime;
    }


    public String[] inputArguments() {
        return inputArguments;
    }

    public String[] getInputArguments() {
        return inputArguments;
    }

    public String bootClassPath() {
        return bootClassPath;
    }

    public String getBootClassPath() {
        return bootClassPath;
    }

    public String classPath() {
        return classPath;
    }

    public String getClassPath() {
        return classPath;
    }

    public Map<String, String> systemProperties() {
        return systemProperties;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

  

  
  


  
}
