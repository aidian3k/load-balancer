package ee.pw.microservice.load_balancer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
class WorkerLoads {

	private final List<AtomicInteger> workerLoads;

	private WorkerLoads() {
		this.workerLoads = new ArrayList<>();
	}

	public static WorkerLoads fromWorkerClusterSize(int clusterSize) {
		WorkerLoads workerLoads = new WorkerLoads();
		final int initialWorkerLoadCount = 0;

		IntStream
			.of(0, clusterSize)
			.forEach(element ->
				workerLoads
					.getWorkerLoads()
					.add(new AtomicInteger(initialWorkerLoadCount))
			);

		return workerLoads;
	}

	public int getSpecificWorkerLoadBasedOnIndex(int workerIndex) {
		return this.getWorkerLoads().get(workerIndex).get();
	}

	public synchronized int getMinLoadServer() {
		AtomicInteger minimumLoadNumber = workerLoads.get(0);
		workerLoads.forEach(workerLoadNumber ->
			minimumLoadNumber.set(
				Math.min(workerLoadNumber.get(), minimumLoadNumber.get())
			)
		);

		return minimumLoadNumber.get();
	}

	public synchronized void incrementParticularWorkerLoad(int workerIndex) {
		workerLoads.set(
			workerIndex,
			new AtomicInteger(workerLoads.get(workerIndex).incrementAndGet())
		);
	}

	public synchronized void decrementParticularWorkerLoad(int workerIndex) {
		workerLoads.set(
			workerIndex,
			new AtomicInteger(workerLoads.get(workerIndex).decrementAndGet())
		);
	}
}
