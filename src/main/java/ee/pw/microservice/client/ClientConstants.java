package ee.pw.microservice.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientConstants {

	public static final String LOAD_BALANCING_SERVER_URL = "localhost";
	public static final int LOAD_BALANCING_SERVER_PORT = 7123;
	public static final int TIME_OF_WAITING_BETWEEN_TASKS_IN_MILLISECONDS =
		1000 * 2;
	public static final int NUMBER_OF_PACKAGES_DATA_IN_DATABASE = 4;
}
