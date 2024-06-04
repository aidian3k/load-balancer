package ee.pw.microservice.worker;

import static ee.pw.microservice.worker.WorkerConstants.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import lombok.SneakyThrows;

public class WorkerMain {

	@SneakyThrows
	public static void main(String... args) {
		Class.forName(DATABASE_PACKAGE_JAR);
		Connection databaseConnection = DriverManager.getConnection(
			DATABASE_URL,
			DATABASE_USER,
			DATABASE_PASSWORD
		);
		ServerSocket workerSocket = new ServerSocket(Integer.parseInt(args[0]));

		while (!Thread.interrupted()) {
			Socket loadBalancerSocket = workerSocket.accept();

			Thread workerTask = new Thread(
				new WorkerTask(loadBalancerSocket, databaseConnection)
			);

			workerTask.start();
		}
	}
}
