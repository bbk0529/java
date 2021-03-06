package basic.method;

public class MethodTest10_2 {
	public static void main(String[] args) {
		int[] n = {-23, -12, 5, 1999, 2342, 23, 10, 70, 80, 60, 25};
		print(n);
		maxNumber(n);
		
		
	}

	
	private static void print(int... n) {
		for(int i=0;i<n.length;i++) System.out.print( n[i] + "\t");
		System.out.println();
		
	}

	private static void maxNumber(int[] n) {
		for (int j=1;j<n.length-1;j++)	{
			for (int i=(n.length-1);i>=j;i--) {
					if (n[i]<n[i-1]) swap (n, i, i-1);				
			}
			print(n);
		}
	}

	
	
	private static void swap(int[] n, int i, int j) {
		int temp;
		temp = n[i];
		n[i] = n[j];
		n[j] = temp;		
	}
}
