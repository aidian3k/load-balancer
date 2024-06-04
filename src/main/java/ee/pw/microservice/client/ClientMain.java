package ee.pw.microservice.client;

import java.net.Socket;
import lombok.SneakyThrows;

public class ClientMain {

	@SneakyThrows
	public static void main(String... args) {
		while (!Thread.interrupted()) {
			Socket loadBalancerSocket = new Socket(
				ClientConstants.LOAD_BALANCING_SERVER_URL,
				ClientConstants.LOAD_BALANCING_SERVER_PORT
			);
			Thread clientRequestSender = new ClientRequestSender(loadBalancerSocket);
			clientRequestSender.start();

			Thread.sleep(2000);
		}
	}
}
