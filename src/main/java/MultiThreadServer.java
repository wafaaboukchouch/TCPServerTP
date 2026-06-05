import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    public static void main(String[] args) {
        int port = 5000;
        int poolSize = 5;

        // Créer le pool de threads
        ExecutorService threadPool = Executors.newFixedThreadPool(poolSize);

        System.out.println("=== SERVEUR TCP MULTI-THREAD ===");
        System.out.println("Démarrage sur le port " + port);
        System.out.println("Pool de threads: " + poolSize + " threads");
        System.out.println("En attente des connexions...\n");

        try {
            // Créer le serveur socket
            ServerSocket serverSocket = new ServerSocket(port);

            // Boucle infinie pour accepter les clients
            while (true) {
                // Attendre un client (bloquant)
                Socket clientSocket = serverSocket.accept();

                // Afficher l'IP du client
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                System.out.println("✓ Nouveau client connecté: " + clientIP);

                // Créer un handler pour ce client
                ClientHandler handler = new ClientHandler(clientSocket);

                // Soumettre au pool de threads
                threadPool.submit(handler);

                // Afficher les stats du pool
                java.util.concurrent.ThreadPoolExecutor tpe =
                        (java.util.concurrent.ThreadPoolExecutor) threadPool;
                System.out.println("  → Threads actifs: " + tpe.getActiveCount() + "/" + poolSize);
            }

        } catch (Exception e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
        }
    }
}
