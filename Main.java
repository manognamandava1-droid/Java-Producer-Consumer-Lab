class SharedBuffer {
    private int data;
    private boolean available = false;

    synchronized void produce(int value)
            throws InterruptedException {
        while (available) {
            wait();
        }

        data = value;
        System.out.println("Produced: " + data);
        available = true;
        notify();
    }

    synchronized void consume()
            throws InterruptedException {
        while (!available) {
            wait();
        }

        System.out.println("Consumed: " + data);
        available = false;
        notify();
    }
}

class Producer extends Thread {
    private SharedBuffer buffer;

    Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.produce(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer extends Thread {
    private SharedBuffer buffer;

    Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.consume();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();

        new Producer(buffer).start();
        new Consumer(buffer).start();
    }
}
