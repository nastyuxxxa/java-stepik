package chat;

import java.io.*;
import java.net.Socket;

public class ChatSocket extends Thread {
	private Socket clientSocket;
	private ChatServer chatServer;

	public ChatSocket(Socket clientSocket, ChatServer chatServer) {
		this.clientSocket = clientSocket;
		this.chatServer = chatServer;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String message = in.readLine();
			java.util.logging.Logger.getGlobal().info(message);
			if (!message.equals("Bye.")) {
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
				out.write(message);
				out.newLine();
				out.flush();
			}
			else {
				chatServer.close();
			}
			in.close();
			clientSocket.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
    }
}