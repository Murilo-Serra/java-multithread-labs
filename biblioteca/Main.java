public class Main {

    public static void main(String[] args) throws InterruptedException {
        Biblioteca biblioteca = new Biblioteca(10);

        Usuario usuario1 = new Usuario(1, biblioteca);
        Usuario usuario2 = new Usuario(2, biblioteca);
        Usuario usuario3 = new Usuario(3, biblioteca);

        usuario1.start();
        usuario2.start();
        usuario3.start();

        Thread.sleep(15000);

        usuario1.interrupt();
        usuario2.interrupt();
        usuario3.interrupt();

        usuario1.join();
        usuario2.join();
        usuario3.join();
    }
}

class Biblioteca {

    private final boolean[] disponivel;
    private final Object[] bloqueio;
    private final Object bloqueioSaida;

    public Biblioteca(int quantidadeLivros) {
        disponivel = new boolean[quantidadeLivros + 1];
        bloqueio = new Object[quantidadeLivros + 1];
        bloqueioSaida = new Object();

        for (int i = 1; i <= quantidadeLivros; i++) {
            disponivel[i] = true;
            bloqueio[i] = new Object();
        }
    }

    public void emprestar(int idUsuario, int idLivro) throws InterruptedException {
        synchronized (bloqueio[idLivro]) {
            if (!disponivel[idLivro]) {
                imprimir("Usuário " + idUsuario + " – Esperando livro " + idLivro + " ficar disponível");
                while (!disponivel[idLivro]) {
                    bloqueio[idLivro].wait();
                }
            }
            disponivel[idLivro] = false;
            imprimir("Usuário " + idUsuario + " – Emprestou livro " + idLivro);
        }
    }

    public void devolver(int idUsuario, int idLivro) {
        synchronized (bloqueio[idLivro]) {
            disponivel[idLivro] = true;
            imprimir("Usuário " + idUsuario + " – Devolveu livro " + idLivro);
            bloqueio[idLivro].notifyAll();
        }
    }

    private void imprimir(String mensagem) {
        synchronized (bloqueioSaida) {
            System.out.println(mensagem);
        }
    }
}

class Usuario extends Thread {

    private final int id;
    private final Biblioteca biblioteca;
    private final java.util.Random aleatorio = new java.util.Random();

    public Usuario(int id, Biblioteca biblioteca) {
        this.id = id;
        this.biblioteca = biblioteca;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int livro = aleatorio.nextInt(10) + 1;

                biblioteca.emprestar(id, livro);
                Thread.sleep(aleatorio.nextInt(1000) + 1000);

                biblioteca.devolver(id, livro);
                Thread.sleep(aleatorio.nextInt(1000) + 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
