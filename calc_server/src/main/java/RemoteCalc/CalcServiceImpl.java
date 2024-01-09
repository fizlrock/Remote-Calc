
package RemoteCalc;

import RemoteCalc.grpc.Calc.MathRequest;
import RemoteCalc.grpc.Calc.MathResponce;
import RemoteCalc.grpc.CalcServiceGrpc.CalcServiceImplBase;
import io.grpc.stub.StreamObserver;

import MathLogic.*;

public class CalcServiceImpl extends CalcServiceImplBase {

	public CalcServiceImpl() {
		super();
		ep = new ExpressionParser();
	}

	private IExpressionParser ep;

	@Override
	public void calculate(MathRequest request, StreamObserver<MathResponce> responseObserver) {

		System.out.printf("Новый запрос: %s \n", request);
		String expression = request.getExpression();

		double result = ep.calcValue(expression);

		MathResponce responce = MathResponce
				.newBuilder()
				.setValue(result)
				.setComment("have a nice day!")
				.build();

		responseObserver.onNext(responce);
		responseObserver.onCompleted();
	}

}
