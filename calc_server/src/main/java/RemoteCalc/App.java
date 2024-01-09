package RemoteCalc;

import java.io.IOException;

import io.grpc.*;

public class App {
	public static void main(String[] args) {
		Server server = ServerBuilder
				.forPort(8080)
				.addService(new CalcServiceImpl())
				.build();

		try {
			server.start();
			System.out.println("Сервер запущен");
			server.awaitTermination();
			System.out.println("Сервер остановлен");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
}
