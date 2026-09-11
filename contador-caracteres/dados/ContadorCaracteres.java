import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ContadorCaracteres {

    // 4 Threads foi a opção mais rapida no emu computador.
    private static final int qntThreads = 4;

    private static final String amostra = "./dados/amostra";
    private static final String todosArqs = "./dados/todosArquivos";

    private static char letraBase(char c) {
        c = Character.toLowerCase(c);
        if (c >= 'a' && c <= 'z') {
            return c;
        }
        switch (c) { // Acentos
            case '\u00e0': case '\u00e1': case '\u00e2': case '\u00e3': case '\u00e4':
                return 'a';
            case '\u00e8': case '\u00e9': case '\u00ea': case '\u00eb':
                return 'e';
            case '\u00ec': case '\u00ed': case '\u00ee': case '\u00ef':
                return 'i';
            case '\u00f2': case '\u00f3': case '\u00f4': case '\u00f5': case '\u00f6':
                return 'o';
            case '\u00f9': case '\u00fa': case '\u00fb': case '\u00fc':
                return 'u';
            case '\u00e7':
                return 'c';
            case '\u00f1':
                return 'n';
            default:
                return 0;
        }
    }

    private static File[] listarTxt(String pasta) {
        File dir = new File(pasta);
        File[] todos = dir.listFiles();
        if (todos == null) {
            return new File[0];
        }
        int qtd = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].isFile() && todos[i].getName().endsWith(".txt")) {
                qtd++;
            }
        }
        File[] txt = new File[qtd];
        int j = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].isFile() && todos[i].getName().endsWith(".txt")) {
                txt[j] = todos[i];
                j++;
            }
        }
        return txt;
    }

    private static void contarArquivo(File arquivo, int[] contadores) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(arquivo));
        int lido;
        while ((lido = br.read()) != -1) {
            char base = letraBase((char) lido);
            if (base != 0) {
                contadores[base - 'a']++; 
            }
        }
        br.close();
    }

    private static void processar(String pasta) throws Exception {
        long inicio = System.nanoTime();

        File[] arquivos = listarTxt(pasta);
        Thread[] threads = new Thread[qntThreads];
        int[][] parciais = new int[qntThreads][26];
        int bloco = arquivos.length / qntThreads;

        for (int i = 0; i < qntThreads; i++) {
            final int inicioBloco = i * bloco;
            final int fimBloco = (i == qntThreads - 1) ? arquivos.length : inicioBloco + bloco;
            final int indice = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int a = inicioBloco; a < fimBloco; a++) {
                        contarArquivo(arquivos[a], parciais[indice]);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < qntThreads; i++) {
            threads[i].join();
        }

        int[] totais = new int[26];
        for (int i = 0; i < qntThreads; i++) {
            for (int j = 0; j < 26; j++) {
                totais[j] += parciais[i][j];
            }
        }

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1_000_000.0;

        System.out.println("Diretorio: " + pasta);
        System.out.println("Arquivos: " + arquivos.length);
        System.out.println("Threads: " + qntThreads);
        for (int i = 0; i < 26; i++) {
            char letra = (char) ('a' + i);
            System.out.println(letra + ": " + totais[i]);
        }
        System.out.printf("Tempo: %.2f ms%n%n", tempoMs);
    }

    public static void main(String[] args) throws Exception {
        processar(amostra);
        processar(todosArqs);
    }
}
