package calculadora;

public class Percentage implements Operation {
	@Override
	public double execute(double total, double percentage) {
		return total * (percentage / 100);
	}
}
