public class Polynomial{
	double coefficients[];
	
	public Polynomial() {
		coefficients = new double [1];
		coefficients[0] = 0;
	}
	
	public Polynomial(double array[]){
		int len = array.length;
		coefficients = new double[len];
		for (int i=0;i<len;i++) {
			coefficients[i] = array[i];
		}
	}
	
	public Polynomial add(Polynomial number) {
		int len1 = number.coefficients.length;
		int len2 = coefficients.length;
		if (len1>len2) {
			Polynomial result = new Polynomial(number.coefficients);
			for (int i=0;i<len2;i++) {
				result.coefficients[i]+=coefficients[i];
			}
			return result;
		}
		else {
			Polynomial result  = new Polynomial(coefficients);
			for (int i=0;i<len1;i++) {
				result.coefficients[i]+=number.coefficients[i];
			}
			return result;	
		}
	}
	
	public double evaluate(double x) {
		double sum = 0;
		for(int i=0; i<coefficients.length; i++)
		{
			sum+=coefficients[i] * Math.pow(x, i);
		}
		return sum;
	}
	
	public boolean hasRoot(double x) {
		double result = evaluate(x);
		if (result == 0.0) {
			return true;
		}
		return false;
	}
	
	
	
	
}