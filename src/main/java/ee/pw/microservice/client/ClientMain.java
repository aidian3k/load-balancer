package ee.pw.microservice.client;

import ee.pw.microservice.load_balancer.LoadBalancerMain;
import java.net.Socket;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {

	private static final Logger logger = LoggerFactory.getLogger(
		ClientMain.class
	);

	@SneakyThrows
	public static void main(String... args) {
		logger.info(
			"Started client program with thread name=[{}]",
			Thread.currentThread().getName()
		);

		while (!Thread.interrupted()) {
			Socket loadBalancerSocket = new Socket(
				ClientConstants.LOAD_BALANCING_SERVER_URL,
				ClientConstants.LOAD_BALANCING_SERVER_PORT
			);

			logger.info("Sent the load-balancer request for data");
			Thread clientRequestSender = new ClientRequestSender(loadBalancerSocket);
			clientRequestSender.start();

			Thread.sleep(
				ClientConstants.TIME_OF_WAITING_BETWEEN_TASKS_IN_MILLISECONDS
			);
		}
	}
}
