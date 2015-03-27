package org.saliya.threads.basic;

public class AffinityAssignerTester {
    public static void main(String[] args) {
        int numThreads = 2;
//        String threadAffinityMask = "00010001";
        String threadAffinityMask = "10001000";
        int prevCore = threadAffinityMask.length();
        for (int i = 0; i < numThreads; ++i){
            prevCore -= 1;
            for (int j = prevCore; j >= 0; --j){
                if (threadAffinityMask.charAt(j) == '1'){
                    prevCore = j;
                    break;
                }
            }
            System.out.println("Assign T " + i  + " to " + ((threadAffinityMask.length() - 1) - prevCore));
        }
    }
}
