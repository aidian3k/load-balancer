package ee.pw.microservice.worker;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class WorkerTask extends Thread {

	private final Socket loadBalancingSocket;
	private final Connection databaseConnection;

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

		Statement sqlStatement = databaseConnection.createStatement();
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

		loadBalancerWriter.write(responseJsonObject.toString());
	}
}
