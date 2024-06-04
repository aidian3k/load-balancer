package ee.pw.microservice.worker;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkerConstants {

	public static final String DATABASE_URL =
		"jdbc:postgresql://localhost:5432/postgres";
	public static final String DATABASE_USER = "postgres";
	public static final String DATABASE_PASSWORD = "root";
	public static final String DATABASE_PACKAGE_JAR = "org.postgresql.Driver";
}
