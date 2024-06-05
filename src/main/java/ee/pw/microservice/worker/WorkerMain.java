package ee.pw.microservice.worker;

import static ee.pw.microservice.worker.WorkerConstants.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerMain {

	private static final Logger logger = LoggerFactory.getLogger(
		WorkerMain.class
	);

	@SneakyThrows
	public static void main(String... args) {
		Class.forName(DATABASE_PACKAGE_JAR);
		Connection databaseConnection = DriverManager.getConnection(
			DATABASE_URL,
			DATABASE_USER,
			DATABASE_PASSWORD
		);
		ServerSocket workerSocket = new ServerSocket(Integer.parseInt(args[0]));
		logger.info(
			"Started worker program with thread name=[{}]",
			Thread.currentThread().getName()
		);

		while (!Thread.interrupted()) {
			Socket loadBalancerSocket = workerSocket.accept();

			Thread workerTask = new Thread(
				new WorkerTask(loadBalancerSocket, databaseConnection)
			);

			workerTask.start();
		}
	}
}
