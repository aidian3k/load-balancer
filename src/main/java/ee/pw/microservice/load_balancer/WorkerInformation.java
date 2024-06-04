package ee.pw.microservice.load_balancer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
class WorkerInformation {

	private final String workerUrl;
	private final int port;
}
