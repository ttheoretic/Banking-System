import java.util.List;

public class Producer extends Thread {
    private final TransactionQueue queue;
    private final int numberOfTransactions;
    private final List<Account> accounts;

    public Producer(TransactionQueue queue, List<Account> accounts, int numberOfTransactions) {
        this.queue = queue;
        this.accounts = accounts;
        this.numberOfTransactions = numberOfTransactions;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfTransactions; i++) {
            try {
                int fromIndex = (int) (Math.random() * accounts.size());
                int toIndex = (int) (Math.random() * accounts.size());
                while (fromIndex == toIndex) {
                    toIndex = (int) (Math.random() * accounts.size());
                }
                Account fromAccount = accounts.get(fromIndex);
                Account toAccount = accounts.get(toIndex);
                int amount = (int) (Math.random() * 10000) + 1;

                Transaction transaction = new Transaction(fromAccount.getKontonummer(), toAccount.getKontonummer(), amount);

                queue.enqueue(transaction);

                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

