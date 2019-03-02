
public class Bubba {
	
	

	public static void main(String[] args) {
		
		
		
		// TODO Auto-generated method stub

		int numAPBio = 11;
		int numAPChem = 4;
		int numAPCS = 28;
		int numAPEng = 37;
		int numAPSpanish = 37;

		int numClasses = 5;

		/* compute average AP class size */
		double average = (numAPBio + numAPChem + numAPCS + numAPEng + numAPSpanish) / (double) numClasses;

	
		System.out.println(
			"The average AP class size is " + average);

        
	}

}
