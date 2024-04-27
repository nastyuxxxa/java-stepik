package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
	private ServerSocket serverSocket;
	private boolean finished;
	private ExecutorService threadPool;

	public ChatServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(100000);
		finished = false;
		threadPool = Executors.newFixedThreadPool(10);
	}

	public void close() {
		finished = true;
		threadPool.shutdown();
	}

	public void start() {
		java.util.logging.Logger.getGlobal().info("Server started");
		System.out.println("Server started");
		while (!finished) {
			try {
				Socket clientSocket = serverSocket.accept();
				threadPool.execute(new ChatSocket(clientSocket, this));
			} catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}