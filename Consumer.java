import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Consumer extends Thread {
    private final TransactionQueue queue;
    private final Map<Integer, Account> accountMap;
    private static final AtomicLong completedTransactions = new AtomicLong(0);

    public Consumer(TransactionQueue queue, Map<Integer, Account> accountMap) {
        this.queue = queue;
        this.accountMap = accountMap;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Transaction transaction = queue.dequeue();
                if (transaction != null) {
                    processTransaction(transaction);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processTransaction(Transaction transaction) {
        Account fromAccount = accountMap.get(transaction.getFromKontonummer());
        Account toAccount = accountMap.get(transaction.getToKontonummer());

        if (fromAccount != null && toAccount != null) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    if (fromAccount.getSaldo() >= transaction.getBetrag()) {
                        fromAccount.abheben(transaction.getBetrag());
                        completedTransactions.incrementAndGet();
                        toAccount.einzahlen(transaction.getBetrag());
                        completedTransactions.incrementAndGet();
                        System.out.println("Transfer successful: " + transaction.getBetrag() +
                                " cents from Account " + fromAccount.getKontonummer() +
                                " to Account " + toAccount.getKontonummer());
                    }
                }
            }
        }
    }


    public static long getCompletedTransactions() {
        return completedTransactions.get();
    }

    public static void resetCompletedTransactions() {
        completedTransactions.set(0);
    }
}
