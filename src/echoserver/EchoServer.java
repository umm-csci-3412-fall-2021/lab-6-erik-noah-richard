package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EchoServer {
	
	// REPLACE WITH PORT PROVIDED BY THE INSTRUCTOR
	public static final int PORT_NUMBER = 6013;
	public static final int NUM_THREADS = 4;
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {

		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

		ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);

		while (true) {

			Socket socket = serverSocket.accept();

			EchoRunnable run = new EchoRunnable(socket);

			Thread newThread = new Thread(run);

			threadPool.submit(newThread);
			// t.start();
		}
	}

	public class EchoRunnable implements Runnable {

		private Socket socket;
		
		public EchoRunnable(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				InputStream fromClientStream = socket.getInputStream();
				OutputStream toClientStream = socket.getOutputStream();
												
				int clientInput = fromClientStream.read();

				while (clientInput != -1) {
					toClientStream.write(clientInput);
					clientInput = fromClientStream.read();
				}

				toClientStream.flush();
				socket.shutdownOutput();

			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}