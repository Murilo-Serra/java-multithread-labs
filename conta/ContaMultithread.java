class Conta {
    private double valor;

    public synchronized void Deposito(double quantia) {
        valor += quantia;
    }

    public synchronized void Saque(double quantia) {
        valor -= quantia;
    }

    public synchronized double Saldo() {
        return valor;
    }
}

class Deposito extends Thread {
    private Conta conta;
    private int id;

    public Deposito(Conta conta, int id) {
        this.conta = conta;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                double quantia = Math.random() * 100;
                synchronized (conta) {
                    conta.Deposito(quantia);
                    System.out.println("Deposito " + id + " | +" + quantia + " | Saldo: " + conta.Saldo());
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

class Saque extends Thread {
    private Conta conta;
    private int id;

    public Saque(Conta conta, int id) {
        this.conta = conta;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                synchronized (conta) {
                    double saldo = conta.Saldo();
                    if (saldo > 0) {
                        double quantia = Math.random() * 100;
                        if (quantia > saldo) {
                            quantia = saldo;
                        }
                        conta.Saque(quantia);
                        System.out.println("Saque " + id + " | -" + quantia + " | Saldo: " + conta.Saldo());
                    }
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

public class ContaMultithread {
    public static void main(String[] args) throws InterruptedException {
        Conta conta = new Conta();

        Deposito d1 = new Deposito(conta, 1);
        Deposito d2 = new Deposito(conta, 2);
        Saque s1 = new Saque(conta, 1);
        Saque s2 = new Saque(conta, 2);
        Saque s3 = new Saque(conta, 3);

        d1.start();
        d2.start();
        s1.start();
        s2.start();
        s3.start();

        d1.join();
        d2.join();
        s1.join();
        s2.join();
        s3.join();

        System.out.println("Saldo final: " + conta.Saldo());
    }
}
