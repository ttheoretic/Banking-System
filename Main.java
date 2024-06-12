import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static final AtomicLong completedTransactions = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        int[] numberOfBranchThreadsArray = {5, 10, 20};
        int[] numberOfCentralThreadsArray = {10, 20, 40};
        int[] numberOfAccountsArray = {1000, 10000};

        for (int F : numberOfBranchThreadsArray) {
            for (int Z : numberOfCentralThreadsArray) {
                for (int K : numberOfAccountsArray) {
                    runExperiment(F, Z, K);
                }
            }
        }
    }

    private static void runExperiment(int F, int Z, int K) throws InterruptedException {
        System.out.println("Experiment mit F=" + F + ", Z=" + Z + ", K=" + K + " startet...");

        TransactionQueue queue = new TransactionQueue(100);
        Map<Integer, Account> accountMap = new HashMap<>();
        for (int i = 1; i <= K; i++) {
            accountMap.put(i, new Account(i, 100000));
        }

        List<Producer> producers = new ArrayList<>();
        List<Consumer> consumers = new ArrayList<>();
        completedTransactions.set(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < F; i++) {
            Producer producer = new Producer(queue, new ArrayList<>(accountMap.values()), 1000);
            producers.add(producer);
            producer.start();
        }

        for (int i = 0; i < Z; i++) {
            Consumer consumer = new Consumer(queue, accountMap);
            consumers.add(consumer);
            consumer.start();
        }

        for (Producer producer : producers) {
            producer.join();
        }

        Thread.sleep(5000);

        consumers.forEach(Consumer::interrupt);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        double transactionsPerMs = (double) completedTransactions.get() / duration;

        System.out.println("Laufzeit des Experiments: " + duration + " ms");
        System.out.println("Transaktionen pro Millisekunde: " + transactionsPerMs);

        long totalAmount = accountMap.values().stream().mapToLong(Account::getSaldo).sum();
        System.out.println("Gesamtbetrag aller Konten: " + totalAmount + " Cents");
    }

}
