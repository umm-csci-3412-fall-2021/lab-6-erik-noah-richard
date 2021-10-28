package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EchoServer {
	public static final int PORT_NUMBER = 6013;
	public static final int NUM_THREADS = 4;
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {

		// Create a server socket
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

		// Create a thread pool
		ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);

		// Loop forever
		while (true) {

			// Accept a connection
			Socket socket = serverSocket.accept();
			
			// Create a new thread to handle the connection
			EchoRunnable run = new EchoRunnable(socket);

			Thread newThread = new Thread(run);

			// Start the new thread
			threadPool.submit(newThread);
		}
	}

	public class EchoRunnable implements Runnable {

		// The socket
		private Socket socket;
	
		public EchoRunnable(Socket socket) {
			this.socket = socket;
		}
		
		// Handle the connection
		public void run() {
			try {
				// Get the input and output streams
				InputStream fromClientStream = socket.getInputStream();
				OutputStream toClientStream = socket.getOutputStream();
												
				// Read the message from the client
				int clientInput = fromClientStream.read();
				// Write the message to the client
				while (clientInput != -1) {
					toClientStream.write(clientInput);
					clientInput = fromClientStream.read();
				}
				// Close the socket
				toClientStream.flush();
				socket.shutdownOutput();

			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}