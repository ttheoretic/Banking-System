import java.util.LinkedList;
import java.util.Queue;

public class TransactionQueue {
    private final Queue<Transaction> queue = new LinkedList<>();
    private final int capacity;

    public TransactionQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(Transaction transaction) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(transaction);
        notifyAll();
    }

    public synchronized Transaction dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        Transaction transaction = queue.poll();
        notifyAll();
        return transaction;
    }
}
