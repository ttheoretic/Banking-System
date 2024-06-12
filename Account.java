public class Account {

    private int kontonummer;
    private int saldo;

    private static int tmp = 1;

    public Account(int kontonummer, int i){
        this.kontonummer = tmp++;
        this.saldo = 100000;
    }

    public synchronized void einzahlen(int betrag){
        this.saldo += betrag;
    }

    public synchronized boolean abheben(int betrag) {
        if (betrag <= saldo) {
            saldo -= betrag;
            return true;
        } else {
            return false;
        }
    }

    public int getKontonummer() {
        return kontonummer;
    }

    public int getSaldo() {
        return saldo;
    }
}
