package MathLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.*;

public class ExpressionParser implements IExpressionParser {

	private final static Pattern RegexNum = Pattern.compile("(\\d+(?:\\.\\d+)?)");

	@Override
	public Double calcValue(String expression) {
		var parsedExpr = parseExpression(expression);
		var polis_expr = trasformToPOLIS(parsedExpr);
		var result = calcPOLIS(polis_expr);
		return result;
	}

	private Double calcPOLIS(List<MathOperand> polis) {
		Double result = Double.NaN;

		Stack<Double> buffer = new Stack<>();

		for (MathOperand oper : polis) {
			if (oper.IsValue) {
				buffer.push(oper.Value);
			} else {
				switch (oper.Operator) {
					case Divide:
						if (buffer.size() < 2) {
							return result;
						} else {
							Double value1, value2, value;
							value1 = buffer.pop();
							value2 = buffer.pop();
							value = value2 / value1;
							buffer.push(value);
						}
						break;
					case Minus:
						if (buffer.size() < 2) {
							return result;
						} else {
							Double value1, value2, value;
							value1 = buffer.pop();
							value2 = buffer.pop();
							value = value2 - value1;
							buffer.push(value);
						}
						break;
					case Multiply:
						if (buffer.size() < 2) {
							return result;
						} else {
							Double value1, value2, value;
							value1 = buffer.pop();
							value2 = buffer.pop();
							value = value2 * value1;
							buffer.push(value);
						}
						break;
					case Plus:
						if (buffer.size() < 2) {
							return result;
						} else {
							Double value1, value2, value;
							value1 = buffer.pop();
							value2 = buffer.pop();
							value = value2 + value1;
							buffer.push(value);
						}
						break;
					case Pow:
						if (buffer.size() < 2) {
							return result;
						} else {
							Double value1, value2, value;
							value1 = buffer.pop();
							value2 = buffer.pop();
							value = Math.pow(value2, value1);
							buffer.push(value);
						}
						break;
					case Sqrt:
						if (buffer.isEmpty()) {
							return result;
						} else {
							Double value = buffer.pop();
							value = Math.sqrt(value);
							buffer.push(value);
						}
						break;
					case UnaryMinus:
						if (buffer.isEmpty()) {
							return result;
						} else {
							Double value = buffer.pop();
							value = -value;
							buffer.push(value);
						}
						break;
				}
			}
		}

		result = buffer.pop();

		return result;
	}

	private List<MathOperand> trasformToPOLIS(List<MathOperand> expression) {
		List<MathOperand> result = new ArrayList<MathOperand>();

		/*
		 * Алгоритм преобразования строки в польскую инверсную запись использует
		 * вспомогательный стек и формирует на выходе новую строку в формате ПОЛИЗ:
		 * 
		 * если исходная строка пуста нужно вытолкнуть из стека все содержимое в
		 * выходную строку и завершить работу;
		 * 
		 * если в начале строки располагается число — оно переписывается в выходную
		 * строку, остальная часть строки обрабатывается рекурсивно;
		 * 
		 * если в начале строки стоит открывающая скобка — она добавляется в стек,
		 * остальная строка обрабатывается рекурсивно;
		 * 
		 * если в начале строки стоит закрывающая скобка — из стека в выходную строку
		 * вытесняется все до тех пор, пока не встретится открывающая скобка (сами
		 * скобки в строку не вытесняются);
		 * 
		 * если в начале строки стоит оператор — из стека в выходную строку вытесняется
		 * все до тех пор, пока не встретится оператор с меньшим приоритетом или скобка.
		 * 
		 * Считанный оператор добавляется в обновленный стек, а остальная часть строки
		 * обрабатывается рекурсивно.
		 */

		Stack<MathOperand> stack = new Stack<>();

		for (MathOperand oper : expression) {

		
			if (oper.IsValue) {
				result.add(oper);
			} else if (oper.Operator == MathOperator.BracketOpen) {
				stack.push(oper);
			} else if (oper.Operator == MathOperator.BracketClosed) {
				MathOperand temp;
				while ((temp = stack.pop()).Operator != MathOperator.BracketOpen) {
					result.add(temp);
				}
			} else {

				while (!stack.isEmpty() &&
						(stack.peek().Operator.Priority >= oper.Operator.Priority) &&
						(stack.peek().Operator != MathOperator.BracketOpen)) {
					result.add(stack.pop());
				}

				stack.push(oper);
			}

		}

		while (!stack.isEmpty()) {
			result.add(stack.pop());
		}


		return result;
	}

	private List<MathOperand> parseExpression(String str) {
		// Заменить , на .
		// Исключить пробелы
		// ведущие нули разрешены
		// Алгоритм: Разбить строку на массив, в котором последовательно содержаться
		// числа и операции
		// Массив в полиз. В полизе проверка на корректность и вычисление
		// Поиск унарного минуса?

		str = str.replaceAll("\\s", ""); // Удаляем пробелы
		str = str.replaceAll(",", ".");

		if (str.startsWith("-")) {
			str = str.substring(1);
			str = "neg" + str;
		}
		str = str.replaceAll("\\(-", "(neg");
		str = str.replaceAll("\\(+", "(");

		str = str.replaceAll("\\-", "sub");
		str = str.replaceAll("\\+", "add");
		str = str.replaceAll("\\/", "div");
		str = str.replaceAll("\\*", "mul");
		str = str.replaceAll("\\^", "pow");
		str = str.replaceAll("sqrt", "sqr");
		str = str.replaceAll("\\(", "bro");
		str = str.replaceAll("\\)", "brc");

		List<MathOperand> expression = new ArrayList<MathOperand>();

		int index = 0;
		var match = RegexNum.matcher(str);

		while (index < str.length()) {
			boolean finded = match.find(index);
			if (finded && match.start() == index) {

				Double value = Double.parseDouble(match.group());
				expression.add(new MathOperand(value));
				index = match.end();

			} else {
				String func = str.substring(index, index + 3);
				MathOperator oper = MathOperator.findByKey(func);
				if (oper != null) {
					expression.add(new MathOperand(oper));
					index += 3;
				} else {
					throw new IllegalArgumentException(String.format("Неизвестная функция: %s", func));
				}

			}
		}
		return expression;

	}
}
