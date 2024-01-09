package RemoteCalc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import RemoteCalc.grpc.*;
import RemoteCalc.grpc.Calc.MathRequest;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder
				.forTarget("localhost:8080")
				.usePlaintext()
				.build();

		CalcServiceGrpc.CalcServiceBlockingStub stub = CalcServiceGrpc.newBlockingStub(channel);

		
		MathRequest request = MathRequest
				.newBuilder()
				.setExpression("2+2")
				.build();

		System.out.printf("Отправка запроса: %s \n", request);
		var responce = stub.calculate(request);
		System.out.printf("Ответ: %s \n", responce);

	}
}
