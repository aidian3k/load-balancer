package ee.pw.microservice.worker;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import ee.pw.microservice.helpers.SocketHelpers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class WorkerTask extends Thread {

	private final Socket loadBalancingSocket;
	private final Connection databaseConnection;
	private static final Logger logger = LoggerFactory.getLogger(
		WorkerTask.class
	);

	@Override
	@SneakyThrows
	public void run() {
		BufferedWriter loadBalancerWriter = new BufferedWriter(
			new OutputStreamWriter(
				loadBalancingSocket.getOutputStream(),
				StandardCharsets.UTF_8
			)
		);
		BufferedReader loadBalancerReader = new BufferedReader(
			new InputStreamReader(
				loadBalancingSocket.getInputStream(),
				StandardCharsets.UTF_8
			)
		);

		int packageId = Integer.parseInt(loadBalancerReader.readLine());
		String query = String.format(
			"select id, city, owner_name from packages where id=%d",
			packageId
		);

		Statement sqlStatement = databaseConnection.createStatement(
			ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY
		);
		ResultSet queryResultSet = sqlStatement.executeQuery(query);
		queryResultSet.first();

		List<String> packageTableColumnNames = List.of("id", "city", "owner_name");
		JsonObject responseJsonObject = new JsonObject();

		for (String columnName : packageTableColumnNames) {
			responseJsonObject.addProperty(
				columnName,
				queryResultSet.getString(columnName)
			);
		}

		logger.info(
			"Sending response object with value=[{}] to the load-balancer",
			responseJsonObject
		);

		SocketHelpers.writeStringToSocket(responseJsonObject.toString(), loadBalancingSocket);
	}
}
