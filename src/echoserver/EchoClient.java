package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {
		Socket socket = new Socket("localhost", PORT_NUMBER);
		InputStream socketInputStream = socket.getInputStream();
		OutputStream socketOutputStream = socket.getOutputStream();

		UserInputProcessor userInput = new UserInputProcessor(socket, socketOutputStream);
		ServerOutputProcessor serverOutput = new ServerOutputProcessor(socket, socketInputStream);

		Thread userInputThread = new Thread(userInput);
		Thread serverOutputThread = new Thread(serverOutput);
		
		userInputThread.start();
		serverOutputThread.start();

		try {
			userInputThread.join();
			serverOutputThread.join();
		} catch (InterruptedException e) {
			System.out.println("InterruptedException: " + e.getMessage());
			System.exit(1);
		}

		socket.close();
		
	}

	public class UserInputProcessor implements Runnable {

		Socket socket;
		OutputStream toServerStream;
		
		public UserInputProcessor(Socket socket, OutputStream socketOutputStream){
			this.socket = socket;
			this.toServerStream = socketOutputStream;
		}

		public void run	() {
			try{
				
				int keyboardInput = System.in.read();
				while (keyboardInput != -1){
					toServerStream.write(keyboardInput);
					keyboardInput = System.in.read();
				}

				toServerStream.flush();
				socket.shutdownOutput();

			} catch(IOException e) {
				System.out.println("Error reading user input");
				System.out.println(e);
				System.exit(1);
			}
		}
	}
	
	
	public class ServerOutputProcessor implements Runnable {
		
		Socket socket;
		InputStream fromServerStream;
		
		public ServerOutputProcessor(Socket socket, InputStream socketInputStream){
			this.socket = socket;
			this.fromServerStream = socketInputStream;
		}

		public void run	() {
			try{
				
				int serverOutput = fromServerStream.read();
				while (serverOutput != -1){
					System.out.write(serverOutput);
					serverOutput = fromServerStream.read();
				}

				System.out.flush();
				socket.shutdownInput();

			} catch(IOException e) {
				System.out.println("Error reading user input");
				System.out.println(e);
				System.exit(1);
			}
		}
	}
}