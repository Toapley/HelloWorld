
public class Fraction {

	private int _numerator;
	private int _denominator;
	
	public Fraction() {
		this._numerator = 1;
		this._denominator =1;		
	}
	
	public Fraction(int num, int den) {
		this._numerator = num;
		this._denominator = den;
	}
	
	public void setNumerator(int num) {
		this._numerator = num;
	}
	
	public int getNumerator() {
		return this._numerator;
	}
	
	public void setDenominator(int num) {
		this._denominator = num;
	}
	
	public int getDenominator() {
		return this._denominator;
	}
	
	public String toString() {
		return _numerator + "/" +  _denominator;
	}
	
	public Fraction Multiply(Fraction o) {
		Fraction f = new Fraction(getNumerator() * o.getNumerator(), getDenominator() * o.getDenominator());
		return f;
				
	}

}

/* + some client code that looks like this:
Fraction f1 = new Fraction(2,4);
Fraction f2 = new Fraction(3,5);
println(f1.multiply(f2));
*/