public class Transaction {

    private int fromKontonummer;
    private int toKontonummer;
    private int betrag;

    public Transaction(int fromKontonummer, int toKontonummer, int betrag){
        this.fromKontonummer = fromKontonummer;
        this.toKontonummer = toKontonummer;
        this.betrag = betrag;
    }

    public int getFromKontonummer() {
        return fromKontonummer;
    }

    public int getToKontonummer() {
        return toKontonummer;
    }

    public int getBetrag() {
        return betrag;
    }
}
