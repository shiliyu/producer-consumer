package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer2 extends Thread {
    private Container2 container;
    private ReentrantLock lock;

    public Consumer2(Container2 container, ReentrantLock lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                while (!container.getValue().isPresent()) {
                    try {
                        container.getNotProducedYet().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer v = container.getValue().get();
                container.setValue(Optional.empty());
                System.out.println("Consuming " + v);
                container.getNotConsumedYet().signal();
            } finally {
                lock.unlock();
            }

        }
    }
}