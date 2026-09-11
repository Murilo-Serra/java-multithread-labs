import java.util.Scanner;

public class VetorMultithread {

    private static int inputNumThreads() {
        try (Scanner teclado = new Scanner(System.in)) {
            System.out.print("Numero de threads: ");
            if (!teclado.hasNextInt()) {
                System.err.println("Informe um numero inteiro valido.");
                System.exit(1);
            }
            int threads = teclado.nextInt();
            if (threads < 1) {
                System.err.println("O numero de threads deve ser maior ou igual a 1.");
                System.exit(1);
            }
            return threads;
        }
    }

    private static void inicializar(double[] vetor, int qntThreads) throws InterruptedException {
        Thread[] lista = new Thread[qntThreads];
        int bloco = TAMANHO / qntThreads;

        for (int t = 0; t < qntThreads; t++) {
            final int inicio = t * bloco;
            final int fim = (t == qntThreads - 1) ? TAMANHO : inicio + bloco;
            lista[t] = new Thread(() -> {
                for (int i = inicio; i < fim; i++) {
                    vetor[i] = Math.random();
                }
            });
            lista[t].start();
        }

        for (int t = 0; t < qntThreads; t++) {
            lista[t].join();
        }
    }

    private static long contar(double[] vetor, int qntThreads,
            double limiteInferior, double limiteSuperior) throws InterruptedException {
        Thread[] lista = new Thread[qntThreads];
        long[] parciais = new long[qntThreads];
        int bloco = TAMANHO / qntThreads;

        for (int t = 0; t < qntThreads; t++) {
            final int inicio = t * bloco;
            final int fim = (t == qntThreads - 1) ? TAMANHO : inicio + bloco;
            final int indice = t;
            lista[t] = new Thread(() -> {
                long contagem = 0;
                for (int i = inicio; i < fim; i++) {
                    if (vetor[i] > limiteInferior && vetor[i] < limiteSuperior) {
                        contagem++;
                    }
                }
                parciais[indice] = contagem;
            });
            lista[t].start();
        }

        for (int t = 0; t < qntThreads; t++) {
            lista[t].join();
        }

        long total = 0;
        for (int t = 0; t < qntThreads; t++) {
            total += parciais[t];
        }
        return total;
    }

    private static final int TAMANHO = 200_000_000;
    
    public static void main(String[] args) throws InterruptedException {
        int threads = inputNumThreads();
        double[] vetor = new double[TAMANHO];

        inicializar(vetor, threads);
        System.out.println("Encerrou inicalizacao");

        long quantidade = contar(vetor, threads, 0.25, 0.75);
        System.out.println("Quantidade no intervalo (0.25, 0.75): " + quantidade);
    }
}
