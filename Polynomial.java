import java.io.File;
import java.io.FileNotFoundException;
import java.lang.String;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Polynomial {
	double non_zero[];
	int exponent[];

	public Polynomial() {
		non_zero = new double[1];
		non_zero[0]=0;
		exponent = new int[1];
		exponent[0]=0;
	}

	public Polynomial(double nz[], int exp[]){
		int len = nz.length;
		non_zero = new double[len];
		exponent = new int[len];
		for (int i=0;i<len;i++) {
			non_zero[i] = nz[i];
			exponent[i] = exp[i];
		}
	}

	public Polynomial(File file) {
		Scanner scanline;
		try {
			scanline = new Scanner(file);
			String line = scanline.nextLine();
			line = line.replace("-", "+-");
			String parts[] = line.split("\\+");
			int len = parts.length;
			String list[][] = new String[len][2];
		
				
			for (int i=0;i<len;i++){
				if (parts[i].indexOf("x")==-1)
				{
					list[i][0] = parts[i];
					list[i][1] = "0";
				}
				else {
					list[i] = parts[i].split("x");
				}
			}
			non_zero = new double[len];
			exponent = new int[len];
			for (int k=0;k<len;k++){
				if (list[k][0]!="" && list[k][1]!="") {
					non_zero[k] = Double.parseDouble(list[k][0]);
					exponent[k] = Integer.parseInt(list[k][1]);
				}
			}
			if (non_zero[0]==0.0) {
				int newlen = non_zero.length-1;
				double array1[] = new double[newlen];
				int array2[] = new int[newlen];
				
				for (int i=1;i<len;i++) {
					array1[i-1] = non_zero[i];
					array2[i-1] = exponent[i];
				}
				non_zero = array1;
				exponent = array2;
			}
			scanline.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int get_index(int ary[], int x){
		int len = ary.length;
		for (int i=0;i<len;i++) {
			if (ary[i]==x) {
				return i;
			}
		}
		return -1;
	}
	
	public int countnz(double ary[]) {
		int len = ary.length;
		int sum=0;
		for (int i=0;i<len;i++) {
			if (ary[i]!=0) {
				sum++;
			}
		}
		return sum;
	}

	public Polynomial add(Polynomial number) {
		//find highest degree
		int len1 = exponent.length;
		int max1 = exponent[0];
		for (int i=0;i<len1;i++) {
			max1 = Math.max(max1, exponent[i]);
		}
		int len2 = number.exponent.length;
		int max2 = number.exponent[0];
		for (int j=0;j<len2;j++) {
			max2 = Math.max(max2, number.exponent[j]);
		}
		int max = Math.max(max1, max2)+1;
		// create new array
		double newNZ1[] = new double[max];
		double newNZ2[] = new double[max];
		int newExp[] = new int[max];
		for (int u=0;u<max;u++) {
			newExp[u] = u;
		}
		//populate the non zero coefficient arrays 
		//Called object
		for (int v=0;v<max;v++) {
			int ind1 = get_index(exponent,v);
			if (ind1!=-1) {
				newNZ1[v] = non_zero[ind1];
			}
			else {
				newNZ1[v] = 0;
			}	
		}
		//argument object
		for (int v=0;v<max;v++) {
			int ind2 = get_index(number.exponent,v);
			if (ind2!=-1) {
				newNZ2[v] = number.non_zero[ind2];
			}
			else {
				newNZ2[v] = 0;
			}
		}
		//Add the nonzero coefficient arrays together
		for (int v=0;v<max;v++) {
			newNZ1[v]+=newNZ2[v];
		}
		//newNZ1 is the main one now
		//Now remove non existing degrees from newExp
		for (int v=0;v<max;v++) {
			if (newNZ1[v]==0) {
				newExp[v] = 0;
			}
		}
		//Now, my array of coefficients (newNZ1) and my array of degrees (newExp) 
		//which both contains the same amount of zeros. I should remove the zeroes to finish
		
		int p = countnz(newNZ1);
		double coefficient[] = new double[p];
		int degrees[] = new int[p];
		
		int l=0,c=0;
		for (int i=0;i<max;i++){
			if (newNZ1[i]!=0) {
				coefficient[l]=newNZ1[i];
				l++;
			}
		}
		for (int i=0;i<max;i++) {
			if (newNZ1[i]!=0){
				degrees[c]=newExp[i];
				c++;
			}
		}
		Polynomial answer = new Polynomial(coefficient, degrees);
		return answer;
	}

	public double evaluate(double x) {
		double sum = 0;
		for (int i = 0; i < non_zero.length; i++) {
			sum += non_zero[i] * Math.pow(x, exponent[i]);
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

	public Polynomial multi(double x, int y) {
		int len = non_zero.length;
		Polynomial result = new Polynomial(non_zero, exponent);
		for (int i = 0; i < len; i++) {
			result.non_zero[i] *= x;
			result.exponent[i] += y;
		}
		return result;

	}

	public Polynomial multiply(Polynomial p) {
		int len = p.non_zero.length;
		Polynomial zero = new Polynomial();
		Polynomial base = new Polynomial(non_zero, exponent);
		for (int i = 0; i < len; i++) {
			Polynomial x = base.multi(p.non_zero[i], p.exponent[i]);
			zero = zero.add(x);
		}
		return zero;
	}
	
	public void saveToFile(String filename) {
		int len = non_zero.length;
		String str[] = new String[len];
		for (int i=0;i<len;i++) {
			if (exponent[i]==0) {
				String x = Double.toString(non_zero[i]);
				str[i] = x;
			}
			else {
				if (non_zero[i]>0) {
					String x = "+" + Double.toString(non_zero[i]) + "x" + Integer.toString(exponent[i]);
					str[i] = x;
				}
				else {
					String x = Double.toString(non_zero[i]) + "x" + Integer.toString(exponent[i]);
					str[i] = x;
				}
			}		
		}
		String join = String.join("",str);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(join);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
				
	}
}
	
	
	
