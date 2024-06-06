package ee.pw.microservice.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;

public final class SocketHelpers {

	@SneakyThrows
	public static String extractStringFromSocket(Socket socket) {
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
		);

		return reader.readLine();
	}

	@SneakyThrows
	public static void writeStringToSocket(String string, Socket socket) {
		BufferedWriter bufferedWriter = new BufferedWriter(
			new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
		);
		bufferedWriter.write(string + "\n");
		bufferedWriter.flush();
	}
}
