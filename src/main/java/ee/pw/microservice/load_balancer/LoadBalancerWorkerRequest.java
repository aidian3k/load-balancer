package ee.pw.microservice.load_balancer;

import java.net.Socket;

import ee.pw.microservice.helpers.SocketHelpers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
class LoadBalancerWorkerRequest extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(
		LoadBalancerMain.class
	);

	private final Socket clientSocket;
	private final Socket workerSocket;
	private final WorkerLoads workerLoads;
	private final int currentServer;

	@Override
	@SneakyThrows
	public void run() {
		String packageIdToFetchFromClientSocket = SocketHelpers.extractStringFromSocket(clientSocket);
		SocketHelpers.writeStringToSocket(packageIdToFetchFromClientSocket, workerSocket);
		logger.info(
			"Load balancer sent the request to worker with index=[{}] on port=[{}]",
			currentServer,
			workerSocket.getPort()
		);

		String responseJsonObjectFromWorker = SocketHelpers.extractStringFromSocket(workerSocket);
		SocketHelpers.writeStringToSocket(responseJsonObjectFromWorker, clientSocket);
		logger.info(
			"Worker with index=[{}] sent the response to loadbalancer",
			currentServer
		);

		workerSocket.close();
		clientSocket.close();

		workerLoads.decrementParticularWorkerLoad(currentServer);
	}
}
