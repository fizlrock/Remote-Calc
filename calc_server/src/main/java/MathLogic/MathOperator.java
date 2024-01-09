
package MathLogic;

import java.util.HashMap;
import java.util.Map;

public enum MathOperator {

	UnaryMinus("neg", 3),
	Minus("sub", 1),
	Plus("add", 1),
	Multiply("mul", 2),
	Divide("div", 2),
	Pow("pow", 3),
	Sqrt("sqr", 3),
	BracketOpen("bro", -1),
	BracketClosed("brc", 0);

	public final String Mark;
	public final int Priority;

	MathOperator(String mark, int priority) {
		Mark = mark;
		Priority = priority;
	}

	private static final Map<String, MathOperator> map;
	static {
		map = new HashMap<String, MathOperator>();
		for (MathOperator v : MathOperator.values()) {
			map.put(v.Mark, v);
		}
	}

	public static MathOperator findByKey(String i) {
		return map.get(i);
	}
}
