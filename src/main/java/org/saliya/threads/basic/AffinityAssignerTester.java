package org.saliya.threads.basic;

public class AffinityAssignerTester {
    public static void main(String[] args) {
//        affinityThreadAffinityAssigner();
        hjAffinityAssigner();
    }

    private static void affinityThreadAffinityAssigner() {
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

    public static void  hjAffinityAssigner(){
        int numThreads = 4;
        String parentThreadAffinityMask = "00110011";
        for (int threadIndex = 0; threadIndex < numThreads; ++threadIndex) {
            int bindTo = -1;
            int count = 0;
            for (int i = parentThreadAffinityMask.length() - 1; i >= 0; --i) {
                if (parentThreadAffinityMask.charAt(i) == '1') {
                    if (count == threadIndex) {
                        bindTo = ((parentThreadAffinityMask.length() - 1) - i);
                        break;
                    } else {
                        ++count;
                    }
                }
            }
            System.out.println("Thread: " + threadIndex  + " bindTo: " + bindTo);
        }
    }
}
