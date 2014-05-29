package org.saliya.threads.parallel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class Parallel {

    public static void For(int fromInclusive, int toExclusive, ExecutorService threadPoolExecutor, final Task task)
            throws InterruptedException {
        if (fromInclusive >= toExclusive) return;

        final CountDownLatch stopLatch = new CountDownLatch(toExclusive - fromInclusive);
        for (int i = fromInclusive; i < toExclusive; ++i) {
            final int threadIndex = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    task.perform(threadIndex);
                    stopLatch.countDown();
                }
            });
        }
        stopLatch.await();
    }

}
