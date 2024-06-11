package ee.pw.microservice.load_balancer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
class WorkerLoads {

	private final List<Integer> workerLoads;

	private WorkerLoads() {
		this.workerLoads = new ArrayList<>();
	}

	public static WorkerLoads fromWorkerClusterSize(int clusterSize) {
		WorkerLoads workerLoads = new WorkerLoads();
		final int initialWorkerLoadCount = 0;

		for (int i = 0; i < clusterSize; ++i) {
			workerLoads.workerLoads.add(initialWorkerLoadCount);
		}

		return workerLoads;
	}

	public int getSpecificWorkerLoadBasedOnIndex(int workerIndex) {
		return this.getWorkerLoads().get(workerIndex);
	}

	public synchronized int getMinLoadServer() {
		int minimalLoadServer = 0;

		for (int i = 0; i < workerLoads.size(); ++i) {
			if (workerLoads.get(i) < workerLoads.get(minimalLoadServer)) {
				minimalLoadServer = i;
			}
		}

		return minimalLoadServer;
	}

	public synchronized void incrementParticularWorkerLoad(int workerIndex) {
		workerLoads.set(workerIndex, workerLoads.get(workerIndex) + 1);
	}

	public synchronized void decrementParticularWorkerLoad(int workerIndex) {
		workerLoads.set(workerIndex, workerLoads.get(workerIndex) - 1);
	}
}
