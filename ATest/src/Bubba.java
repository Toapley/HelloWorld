import java.util.Arrays;

public class Bubba {
	
	public static int[][] copyArray(int[][] original) {
		/* Missing code 1 */
		int[][] copy = new int[original.length][original[0].length];


		for (int i = 0; i < original.length; i++) {
			for (int j = 0; j < original[0].length; j++) {
				copy[j][i] = original[j][i];
			}
		}
		return copy;
	}


	public static void main(String[] args) {
		
		int[][] a = {{1,2,3},{4,5,6}},b;
		b = copyArray(a);
		
		
		System.out.println(Arrays.deepToString(b));
		
		
		
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
