package org.saliya.threads.basic;

import net.openhft.affinity.AffinityStrategy;

import java.io.IOException;

public class AsProcAffinity implements AffinityStrategy {

    private int cpuId;

    public AsProcAffinity(String affinityMask) throws IOException {
//        String procAffinityMask = Long.tohUtils.getProcAffinityMask(pid);
    }

    @Override
    public boolean matches(int cpuId, int cpuId2) {
        return false;
    }
}
