package calculadora;

public class Division implements Operation{
	@Override
	public double execute(double a, double b) {
		if (b==0) {
			throw new ArithmeticException("Não é possível dividir por zero.");
		}
		return a / b;
	}
}
