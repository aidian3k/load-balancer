package ee.pw.microservice.load_balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadBalancerMain {

	private static final Logger logger = LoggerFactory.getLogger(
		LoadBalancerMain.class
	);

	public static void main(String... args) {
		logger.info(
			"Started load-balancer program with thread name=[{}]",
			Thread.currentThread().getName()
		);

		LoadBalancingAlgorithm loadBalancingAlgorithm = args.length > 0
			? LoadBalancingAlgorithm.valueOf(args[0])
			: LoadBalancingAlgorithm.ROUND_ROBIN;

		LoadBalancerHandler.startLoadBalancer(loadBalancingAlgorithm);
	}
}
