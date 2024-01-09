
package MathLogic;

public interface IExpressionParser {
	/**
	 Вычисляет значение математического выражения.
	 Допустимые операциии: 
	 	1. Умножение
		2. Деление
		3. Сложение
		4. Вычитание
		5. Установка приоритета операции с помощью скобок
	 * @param expression
	 * @return
	 */
	public Double calcValue(String expression);
}
