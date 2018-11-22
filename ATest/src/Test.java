public class Test {


	public static void main(String[] args) {
	
		for (int i = 1; i <= 5; i++)
		{
			for (	int j = i; j >= 1; j-- )
			{
				System.out.print("*");
			}

			System.out.println();
		}


		boolean i = 3 * 4 - 3 > 84 % 20 + 20 || (int) 12.5 / 2 + 4 > 3 && !(-2 == -1);
		System.out.println( i );
		
		
		boolean x = true;
		boolean y = true;
				
		String output = "";
		output += 234;
		output += 234;
		System.out.println(output);
		
		System.out.println("TT = " + Boolean.toString( (x && !y) || (!x && y)));
		
		x = true;
		y = false;
		
		System.out.println("TF = " + Boolean.toString( (x && !y) || (!x && y)));
		
		x = false;
		y = true;
		System.out.println("FT = " + Boolean.toString( (x && !y) || (!x && y)));
		
		x = false;		
		y = false;
		
		System.out.println("FF = " + Boolean.toString((x && !y) || (!x && y)));
				
		
		
	}
}
