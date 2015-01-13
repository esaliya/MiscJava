package org.saliya.common;

import java.io.IOException;

public class Utils {
    private final static int TASKSET_OUT_LENGTH = "pid 's current affinity list: ".length();
    public static String getPid() throws IOException {
        byte[] bo = new byte[100];
        String[] cmd = {"bash", "-c", "echo $PPID"};
        Process p = Runtime.getRuntime().exec(cmd);
        p.getInputStream().read(bo);
        return new String(bo).trim();
    }

    public static long getProcAffinityMask(int pid) throws IOException {
        byte[] bo = new byte[100];
        String pidString = String.valueOf(pid);
        String[] cmd = {"bash", "-c", "taskset -p " + pidString};
        Process p = Runtime.getRuntime().exec(cmd);
        int length = TASKSET_OUT_LENGTH + pidString.length();
        p.getInputStream().read(bo);
//        return Long.parseLong(new String(bo).substring(length).trim());
        return Long.parseLong(new String(bo).substring(length).trim(), 16);
    }
}
