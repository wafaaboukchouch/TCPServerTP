import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    // Constructeur - reçoit la socket du client
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Récupérer l'IP du client
            String clientIP = clientSocket.getInetAddress().getHostAddress();
            String threadName = Thread.currentThread().getName();

            System.out.println("Thread " + threadName + " traite le client: " + clientIP);

            // Créer les flux pour lire et écrire
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter output = new PrintWriter(
                    clientSocket.getOutputStream(), true
            );

            // Message de bienvenue
            output.println("Bienvenue ! Commandes: hello, time, bye");

            String message;
            // Lire les messages du client
            while ((message = input.readLine()) != null) {
                System.out.println("Message reçu de " + clientIP + ": " + message);

                // Traiter la commande
                String response = traiterCommande(message);
                output.println(response);

                // Si bye, fermer la connexion
                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Client " + clientIP + " déconnecté");
                    break;
                }
            }

            // Fermer les ressources
            input.close();
            output.close();
            clientSocket.close();

        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private String traiterCommande(String message) {
        switch (message.toLowerCase()) {
            case "hello":
                return "Bonjour client !";
            case "time":
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                return "Date et heure: " + now.format(formatter);
            case "bye":
                return "Connexion fermée. Au revoir !";
            default:
                return "Message reçu: " + message;
        }
    }
}
