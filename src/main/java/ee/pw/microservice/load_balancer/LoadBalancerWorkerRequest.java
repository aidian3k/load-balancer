package ee.pw.microservice.load_balancer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
class LoadBalancerWorkerRequest extends Thread {

	private final Socket clientSocket;
	private final Socket workerSocket;
	private final WorkerLoads workerLoads;
	private final int currentServer;

	@Override
	@SneakyThrows
	public void run() {
		BufferedWriter workerWriter = new BufferedWriter(
			new OutputStreamWriter(
				workerSocket.getOutputStream(),
				StandardCharsets.UTF_8
			)
		);
		BufferedWriter clientWriter = new BufferedWriter(
			new OutputStreamWriter(
				clientSocket.getOutputStream(),
				StandardCharsets.UTF_8
			)
		);
		BufferedReader workerReader = new BufferedReader(
			new InputStreamReader(
				workerSocket.getInputStream(),
				StandardCharsets.UTF_8
			)
		);
		BufferedReader clientReader = new BufferedReader(
			new InputStreamReader(
				clientSocket.getInputStream(),
				StandardCharsets.UTF_8
			)
		);

		workerWriter.write(clientReader.readLine());
		workerWriter.flush();

		clientWriter.write(workerReader.readLine());
		clientWriter.flush();

		workerSocket.close();
		clientSocket.close();

		workerLoads.decrementParticularWorkerLoad(currentServer);
	}
}
