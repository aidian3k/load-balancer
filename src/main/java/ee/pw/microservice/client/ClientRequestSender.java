package ee.pw.microservice.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ClientRequestSender extends Thread {

	private final Socket loadBalancingSocket;
	private static final Logger LOGGER = Logger.getLogger(
		ClientRequestSender.class.getName()
	);

	@Override
	@SneakyThrows
	public void run() {
		BufferedWriter loadBalancerWriter = new BufferedWriter(
			new OutputStreamWriter(
				loadBalancingSocket.getOutputStream(),
				StandardCharsets.UTF_8
			)
		);
		BufferedReader loadBalancerReader = new BufferedReader(
			new InputStreamReader(
				loadBalancingSocket.getInputStream(),
				StandardCharsets.UTF_8
			)
		);

		int packageId = new Random().nextInt(4) + 1;

		loadBalancerWriter.write(packageId + "\n");
		loadBalancerWriter.flush();

		String jsonString = loadBalancerReader.readLine();
		LOGGER.log(Level.INFO, jsonString);
	}
}
