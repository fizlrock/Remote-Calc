
package MathLogic;

public class MathOperand {
		public MathOperand(Double value) {
		IsValue = true;
		IsOperator = !IsValue;
		Operator = null;
		Value = value;
	}

	public MathOperand(MathOperator operator) {
		IsValue = false;
		IsOperator = !IsValue;
		Operator = operator;
		Value = null;
	}

	public final MathOperator Operator;
	public final Double Value;
	public final boolean IsValue;
	public final boolean IsOperator;

	@Override
	public String toString() {

		return IsValue ? Value.toString() : Operator.name();
	}

}
