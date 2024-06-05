package ee.pw.microservice.client;

import java.net.Socket;
import java.util.Random;

import ee.pw.microservice.helpers.SocketHelpers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class ClientRequestSender extends Thread {

	private final Socket loadBalancingSocket;
	private static final Logger logger = LoggerFactory.getLogger(
		ClientRequestSender.class
	);

	@Override
	@SneakyThrows
	public void run() {
		int packageId = new Random()
			.nextInt(ClientConstants.NUMBER_OF_PACKAGES_DATA_IN_DATABASE) +
		1;

		SocketHelpers.writeStringToSocket(String.valueOf(packageId), loadBalancingSocket);

		String responseValueFromLoadBalancer = SocketHelpers.extractStringFromSocket(loadBalancingSocket);
		logger.info(
			"Client got the response from load-balancer with value=[{}]",
			responseValueFromLoadBalancer
		);
	}
}
