package ee.pw.microservice.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientConstants {

	public static final String LOAD_BALANCING_SERVER_URL = "localhost";
	public static final int LOAD_BALANCING_SERVER_PORT = 7123;
}
