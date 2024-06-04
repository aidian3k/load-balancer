package ee.pw.microservice.load_balancer;

public class LoadBalancerMain {

	public static void main(String... args) {
		LoadBalancingAlgorithm loadBalancingAlgorithm = args.length > 0
			? LoadBalancingAlgorithm.valueOf(args[0])
			: LoadBalancingAlgorithm.ROUND_ROBIN;

		LoadBalancerHandler.startLoadBalancer(loadBalancingAlgorithm);
	}
}
