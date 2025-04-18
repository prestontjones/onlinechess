import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class GameServer {

    private Server server;

    public GameServer() throws IOException {
        server = new Server();
        registerPackets(server.getKryo());

        server.addListener(new Listener() {
            public void connected(Connection connection) {
                System.out.println("Client connected: " + connection.getID());
            }

            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage msg = (ChatMessage) object;
                    System.out.println("Received message: " + msg.text);
                    server.sendToAllTCP(msg); // echo back to everyone
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("Client disconnected: " + connection.getID());
            }
        });

        server.bind(54555, 54777); // TCP port, UDP port
        server.start();

        System.out.println("Server started on ports 54555 (TCP) and 54777 (UDP)");
    }

    private void registerPackets(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(ChatMessage.class);
        // Add more packets here as needed
    }

    public static void main(String[] args) throws IOException {
        new GameServer();
    }
}