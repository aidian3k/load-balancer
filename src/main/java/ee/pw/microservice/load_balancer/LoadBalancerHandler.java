package ee.pw.microservice.load_balancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;

public final class LoadBalancerHandler {

	private static final String FILE_NAME = "workers_list.txt";
	private static final Integer LOAD_BALANCER_PORT = 7123;
	private static final Logger LOGGER = Logger.getLogger(
		LoadBalancerHandler.class.getName()
	);

	@SneakyThrows
	public static void startLoadBalancer(
		LoadBalancingAlgorithm loadBalancingAlgorithm
	) {
		List<WorkerInformation> workers = loadWorkerInformationFromFile();
		WorkerLoads workerLoads = WorkerLoads.fromWorkerClusterSize(workers.size());
		ServerSocket loadBalancerServerSocket = new ServerSocket(
			LOAD_BALANCER_PORT
		);

		int currentWorkerIndex = 0;

		while (!Thread.interrupted()) {
			Socket clientSocket = loadBalancerServerSocket.accept();

			currentWorkerIndex =
				getCurrentWorkerIndexBasedOnLoadBalancingAlgorithm(
					loadBalancingAlgorithm,
					currentWorkerIndex,
					workerLoads
				);

			Socket workerSocket = new Socket(
				workers.get(currentWorkerIndex).getWorkerUrl(),
				workers.get(currentWorkerIndex).getPort()
			);

			Thread loadBalancerWorkerRequest = new LoadBalancerWorkerRequest(
				clientSocket,
				workerSocket,
				workerLoads,
				currentWorkerIndex
			);

			loadBalancerWorkerRequest.start();
		}
	}

	private static int getCurrentWorkerIndexBasedOnLoadBalancingAlgorithm(
		LoadBalancingAlgorithm loadBalancingAlgorithm,
		int currentWorkerIndex,
		WorkerLoads workerLoads
	) {
		switch (loadBalancingAlgorithm) {
			case ROUND_ROBIN -> {
				currentWorkerIndex =
					(currentWorkerIndex + 1) % workerLoads.getWorkerLoads().size();

				LOGGER.log(
					Level.INFO,
					"In round robin algorithm, worker with index=[{0}] has been chosen",
					currentWorkerIndex
				);
			}
			case LEAST_CONNECTIONS -> {
				currentWorkerIndex = workerLoads.getMinLoadServer();
				int chosenWorkerWorkLoad = workerLoads.getSpecificWorkerLoadBasedOnIndex(
					currentWorkerIndex
				);
				LOGGER.log(
					Level.INFO,
					String.format(
						"In least connections algorithm, worker with index=[%d] and workLoad=[%d] has been chosen",
						currentWorkerIndex,
						chosenWorkerWorkLoad
					)
				);

				workerLoads.incrementParticularWorkerLoad(currentWorkerIndex);
			}
		}

		return currentWorkerIndex;
	}

	private static List<WorkerInformation> loadWorkerInformationFromFile()
		throws IOException {
		InputStream inputStream = Objects.requireNonNull(
			ClassLoader.getSystemResourceAsStream(FILE_NAME)
		);
		BufferedReader bufferedReader = new BufferedReader(
			new InputStreamReader(inputStream)
		);

		String readLine;
		List<WorkerInformation> workerInformation = new ArrayList<>();

		final int hostUrlArrayFilePosition = 0;
		final int portNumberArrayFilePosition = 1;

		while ((readLine = bufferedReader.readLine()) != null) {
			String[] splitLineWithWorkerArguments = readLine.split(",");
			workerInformation.add(
				new WorkerInformation(
					splitLineWithWorkerArguments[hostUrlArrayFilePosition],
					Integer.parseInt(
						splitLineWithWorkerArguments[portNumberArrayFilePosition]
					)
				)
			);
		}

		return workerInformation;
	}
}
