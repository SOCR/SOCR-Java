//package etas.util;
package edu.ucla.stat.SOCR.util;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
//import etas.exception.*;

public class Utility {

	public static double digitTruncator(double input) {
		double result = 0;
		if (input == 2.7199999999999998) {
			result = 2.72;
		} else if (input == 2.7800000000000002) {
			result = 2.78;
		} else if (input == 3.1399999999999997) {
			result = 3.14;
		} else if (input == 3.6799999999999997) {
			result = 3.68;
		} else {
			result = input;
		}
		//System.out.println("digitTruncator input = " + input + ", result = " + result);
		return result;
	}

	// augmentator is incorrect English.
	public static String digitAugmentator(double input) {
		String result = null;
		if (input == 2.0) {
			result = "2.00";
		} else if (input == 2.3) {
			result = "2.30";
		} else if (input == 2.6) {
			result = "2.60";
		} else if (input == 2.9) {
			result = "2.90";
		} else if (input == 3.2) {
			result = "3.20";
		} else if (input == 3.5) {
			result = "3.50";
		} else {
			result = input + "";
		}
		//System.out.println("digitTruncator input = " + input + ", result = " + result);
		return result;
	}

	public static int sign(double input) {
		int result = result = (input>0) ? 1 : ((input == 0) ? 0 : -1);

		return result;
	}
	public static int[] sign(double[] input) {
		int[] result = new int[input.length];
		// if input is positive, return 1; if input is negative, return -1. if input is 0, return 0.
		for (int i = 0; i < input.length; i++) {
			result[i] = (input[i]>0) ? 1 : ((input[i] == 0) ? 0 : -1);
		}
		return result;
	}
	public static double prod(double[] input) {
		double result = input[0];
		for (int i = 1; i < input.length; i++) {
			result *= input[i]; // check 0 and jump out?
		}
		return result;
	}

	public static double min(double[] input) {
		double result = input[0];
		for (int i = 1; i < input.length; i++) {
			result = Math.min(result, input[i]);
		}
		return result;
	}


	public static double max(double[] input) {
		double result = input[0];
		for (int i = 1; i < input.length; i++) {
			result = Math.max(result, input[i]);
		}
		return result;
	}
	public static double[] exp(double[] input) {
		double[] result = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			result[i] = Math.exp(input[i]);
		}
		return result;
	}


	public static double[] listToDoubleArray(ArrayList list) {
		int size = list.size();
		double[] result = new double[size];

		//for (int i = size-1; i >= 0; i--) {
		for (int i = 0; i < size; i++) {

			result[i] = ((Double)list.get(i)).doubleValue();
			//////System.out.println("
		}
		return result;
	}
	public static int[] listToIntArray(ArrayList list) {
		int size = list.size();
		int[] result = new int[size];

		//for (int i = size-1; i >= 0; i--) {
		for (int i = 0; i < size; i++) {

			result[i] = ((Integer)list.get(i)).intValue();
			//////System.out.println("
		}
		return result;
	}


	public static byte[] listToByteArray(ArrayList list) {
		int size = list.size();
		byte[] result = new byte[size];

		for (int i = 0; i < size; i++) {
			result[i] = ((Byte)list.get(i)).byteValue(); //Byte.parseByte((String)list.get(i));
		}
		return result;
	}


	// x and y can be all various length, not necs 2D or 3D.
	// Just two statistical vector of the same length is okay.
	public static double[] innerProduct(double[] x, double[] y) throws WrongDataFormatException {
		if (x.length == 0 || y.length == 0 || x.length != y.length) {
			throw new WrongDataFormatException("different length of vector in inner product calculation.");
		}
		double[] result = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i] * y[i];
		}
		return result;
	}

	// start is included, end is not. just like substring.
	public static double[] truncateArray(double[] input, int start, int end) throws WrongDataFormatException{
		if (input.length == 0 || start > end || start > input.length || end > input.length) {
			throw new WrongDataFormatException("wrong size of input or inappropriate start and or end.");
		}
		double[] result = new double[end - start];
		for (int i = 0; i < end - start; i++) {
			result[i] = input[start + i];
		}
		return result;
	}
	public static String[][] truncateDigits(double[][] input, int numberDigits) {
		String[][] result = new String[input.length][];
		int indexDot = -1;
		String integerPart = null, decimalPart = null, dot = ".";
		String wholePart = null;
		//int maxNumberDigit = 0;
		for (int i = 0; i < input.length; i++) {
			result[i] = new String[input[i].length];
			for (int j = 0; j < input[i].length; j++) {

				System.out.println("input["+i+"]["+j+"] = " + input[i][j]);

				wholePart = input[i][j] + "";
				indexDot = (wholePart).indexOf(dot);
				if (indexDot>=0) {
					integerPart = wholePart.substring(0, indexDot);
					try {
						decimalPart = wholePart.substring(indexDot + 1, indexDot + 1 + numberDigits);
					} catch (Exception e) {
						decimalPart = wholePart.substring(indexDot + 1, wholePart.length());

						//decimalPart = wholePart.substring(indexDot + 1, );
					}
					result[i][j] = integerPart + dot + decimalPart;
				} else {
					result[i][j] = input[i][j] + "";
				}
			}
		}
		return result;
	}
	// only works with triangular shape, inc by 1 in row size.
	// quick and dirty, ad hoc for Alex's code using ETAS Ogata 2.3

	public static double[][] reverseDoubleIndex(double[][] input) {
		int iLength = input.length; // assume square
		double[][] result = new double[iLength][iLength];
		for (int i = 0; i < iLength; i++) {
			for (int j = 0; j < iLength; j++) {
				try {
					result[i][j] = input[j][i];
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	public static void print(double[] array, String name) {
		System.out.println("Print 1D double array ");

		for (int i = 0; i < array.length; i++) {
			System.out.println(name + " array[" + i + "] = " + array[i]);
		}
		System.out.println();
	}

	public static void print(double[][] array, String name) {
		System.out.println("Print 2D double array");
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.println(name + " array[" + i + "][" + j + "] = " + array[i][j]);
			}
		}
		System.out.println();
	}

	// base e
	public static double[] getLogArray(double[] input) {
		double[] output = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			output[i] = Math.log(input[i]);
		}
		return output;
	}

	// base 10
	public static double[] getLog10Array(double[] input) {
		double[] output = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			output[i] = Math.log10(input[i]); // change here.
		}
		return output;
	}

	public static double log10(double input) { // log of base 10
		return Math.log(input) / Math.log(10);
	}

	public static ArrayList doubleArrayToList(double[] input) {
		ArrayList<Object> result = new ArrayList<Object>();
		for (int i = 0; i < input.length; i++) {
			result.add(new Double(input[i]));
		}
		return result;
	}

	public static double findKolmogorovProb(double z) {
		int maxStep = 1000;
		double p = 0;
		for (int j = 1; j <= maxStep; j++) {
			double onePower = (j%2==0) ? -1 : 1;
			p+= onePower * Math.exp(-2 * j * j * z * z);
		}

		p*=2;
		System.out.println("findKolmogorovPValue p = " + p);
		return p;
	}

	// is this thing call critical point??
	public static double getStudentTCriticalPoint95CI(int df) { // unsigned, all positive.
		double alpha = 0.05;
		double cp = 0;
		double area = 1 - .5 * alpha;

		// use alpha = .05 for now.
		if (area == .975) {
			if (df >= 120) cp = 1.979930;
			else if (df >= 60) cp = 2.000298;
			else if (df >= 40) cp = 2.021075;
			else
				cp = criticalPointLookUp[df-1]; // use df-1 as the array index. indexp = 0 for DF = 1, index = 1 for DF = 2, etc.
		}
		//////System.out.println("getCriticalPoint cp = " + cp);
		return cp;
	}

	public static double getFlignerKilleenNormalQuantile(int totalSize, double rank) {
		//(1 + rank(abs(x)) / (n + 1)) / 2
		double ans = (1 + (rank/(((double)totalSize)+1)) )/2; // same as R results
		//System.out.println("totalSize = " + totalSize + " rank = " + rank + ",NormalQuantile = " + ans);
		return ans;
	}

	// from DF = 1 to DF = 40.
	private static double[] criticalPointLookUp = {12.7062047361747,  4.30265272974946,  3.18244630528371,  2.77644510519779,  2.57058183563631,  2.44691185114497,  2.36462425159278,  2.30600413520417,  2.26215716279820,  2.22813885198627,  2.20098516009164,  2.17881282966723,  2.16036865646279,  2.14478668791780,  2.13144954555978,  2.11990529922125,  2.10981557783332,  2.10092204024104,  2.09302405440831,  2.08596344726586,  2.07961384472768,  2.07387306790403,  2.06865761041905,  2.06389856162803,  2.05953855275330,  2.05552943864287,  2.05183051648029,  2.04840714179525,  2.04522964213270,  2.04227245630124,  2.03951344639641,  2.0369333434601, 2.03451529744934,  2.03224450931772,  2.03010792825034,  2.02809400098045,  2.02619246302911,  2.02439416391197,  2.02269092003676,  2.02107539030627
	};
	/*
	public static double[][] reverseDoubleIndex(double[][] input) {
		int iLength = input.length;
		int maxLength = input[iLength - 1].length; // assume last one has most enties
		double[][] result = new double[maxLength][];
		for (int i = 0; i < iLength; i++) {
			System.out.println("input[i].length = " + input[i].length);
			result[i] = new double[maxLength - i];
			System.out.println("result[i].length = " + result[i].length);
			for (int j = 0; j < result[i].length; j++) {
				try {
					result[i][j] = input[j][i];
				} catch (Exception e) {
				}
			}
		}
		return result;
	}
	*/

	// note: shift 1 to match the computation in Alex's code. first one not considered.
	/*
	public static double[] vectorProbabilitySum(double[][] input) {
		int iLength = input.length;
		double[] result = new double[iLength];
		for (int j = 1; j < iLength; j++) { //
			double sum = 0;
			for (int i = 0; i < iLength; i++) {
				try {
					sum = sum + input[i][j-1];
				} catch (Exception e) {
				}
			}
			result[j] = sum;
			//System.out.println("sum = " + result[j] );
		}

		return result;
	}

	public static void main(String[] args) {
		double[][] x = new double[20][];
		x[0] = new double[]{};
		x[1] = new double[]{1.75725559136931e-05};
		x[2] = new double[]{7.85830456315309e-06, 0.000211267103391022};
		x[3] = new double[]{1.47654613248894e-10, 4.00223895237924e-09, 0.999984213087831};
		x[4] = new double[]{1.76691591498257e-06, 1.27996600589313e-05, 0.0150325890059397, 0.00393675407756643};
		x[5] = new double[]{9.0511443041041e-05, 0.000121590070555276, 0.00301060336110301, 0.00136993122949686, 0.00109480418967252};
		x[6] = new double[]{7.79199677919156e-06, 9.8633507143518e-07, 1.61962623306073e-05, 5.97695094982603e-06, 1.44671254216322e-05, 4.35182778844555e-05};
		x[7] = new double[]{2.11598790556263e-06, 8.71040493427726e-06, 0.00605373184477667, 0.00207646230367675, 0.000110261445159181, 9.84518431691484e-05, 0.000842657752128598};
		x[8] = new double[]{1.45758584376858e-11, 1.32605236227117e-10, 2.32781666035819e-07, 5.11940034572517e-07, 4.24295481734904e-10, 1.03188610099619e-09, 3.63424486811665e-09, 0.999992837415882};
		x[9] = new double[]{1.11542823791946e-13, 4.61818488943784e-13, 3.27808389837998e-10, 1.09543062992982e-10, 6.02900039426077e-12, 5.15774221780354e-12, 4.4560248326189e-11, 0.999438279816362, 0.00056166670954702};
		x[10] = new double[]{7.95589781497386e-13, 2.46961941800005e-12, 8.75326609470802e-10, 2.96984475773809e-10, 4.18798748978091e-11, 3.26622436966312e-11, 3.75341449810046e-10, 0.803062320771626, 0.00141711516867299, 0.195520204909079};

		x[11] = new double[]{1.91134482555746e-11, 7.751796653084e-11, 3.76187328370069e-08, 8.4822102644379e-09, 2.75934984532852e-09, 7.3180438285441e-10, 8.95876531928211e-09, 0.826525294461127, 0.00598569134219699, 0.131582721245959, 0.0358936632487693};
		x[12] = new double[]{2.9303386003408e-12, 1.40754387658788e-11, 1.564286943596e-08, 4.28308571347876e-09, 1.96643211448978e-10, 1.36557823336214e-10, 1.11553797260739e-09, 0.863175574668834, 0.00149689194175135, 0.125550711484131, 0.00437125328448489, 0.00540400101958594};
		x[13] = new double[]{1.85765246152293e-12,7.69288776600744e-12,5.35012940806217e-09,1.93168564056817e-09,9.02944223453299e-11,8.8033840830164e-11,7.09908917572248e-10,0.883615404228713,0.000511622950652515,0.0938986435838464,0.00336293980070918,0.000558155354959664,0.0180523759925112};

		x[14] = new double[]{9.1307788649012e-10,1.93176081831019e-09,2.65243716320265e-07,1.26948270958197e-07,1.49381437157592e-08,4.27130160699057e-08,3.62669239382253e-07,0.716917853056652,0.0175632855542594,0.0744392083431624,0.0331087657190408,0.00518065345442331,0.0134798513535067,0.139062402182962};
		x[15] = new double[]{2.25991683380964e-10,8.96810975834726e-10,5.51360837079209e-07,1.51872109635123e-07,1.72595512750991e-08,9.50354346908539e-09,7.95286486716461e-08,0.80924092197187,0.000978279328888828,0.104962074723992,0.0162688662062901,0.0100390987653041,0.0234799511330188,0.0344738366225252,0.000434670964307095};

		x[16] = new double[]{1.58306859388617e-05,6.61185702734457e-07,7.20734229371076e-06,2.70971155858813e-06,5.71538394297558e-06,3.49949655342468e-05,0.00812021571905964,0.00475906860313153,0.000331238965723754,0.000501910655995123,0.00016866976968942,0.000120989498713749,8.68314767080562e-05,0.000265407506904228,0.000210544533711743,0.000640662151759877};
		x[17] = new double[]{2.22827633372407e-06,5.29868130069041e-07,1.22114620015782e-05,4.56999280289291e-06,8.13447019419397e-06,2.06996889189497e-05,0.00480140142510174,0.0116036740657538,0.000575223917068235,0.00122137880385030,0.000479908494667524,0.000289818756332817,0.000197275441729495,0.000643553826232044,0.000655781871553387,0.00158842703695601,0.586971285595083};
		x[18] = new double[]{ 1.57853632945469e-07,1.21252891010296e-06,1.50342742587895e-05,4.09998404429453e-06,9.68801221003625e-05,4.18810377819816e-06,5.87604950415025e-06,0.00276908071545394,0.000239790967717263,0.00029897605520467,7.74209627326293e-05,0.000117749404693158,6.56687228573415e-05,0.000149445091351049,4.28682339816948e-05,0.000430096975491353,0.000159449692218336,0.000136867861445099};

		x[19] = new double[]{8.35138775913864e-07,1.71631489266904e-06,0.000214167847607468,6.03836226670367e-05,0.000113028456062189,1.91518736680776e-05,2.58492349170539e-05,0.0975797540413294,0.00132992077657896,0.0108031047957755,0.00628768884837051,0.0109853646118118,0.00190644147097746,0.0047717964037965,0.00102082058661234,0.0248697756762282,6.21385525843932e-05,0.000168284873673489,1.70957382144747e-05};

		//double[][] x = new double[][]{{1}, {2, 3}, {4, 5, 6}, {7, 8, 9, 10}};
		//double[][] x = new double[][]{x0, x1, x2, x3};

		double[] y = vectorProbabilitySum(x);
		for (int i = 0; i < y.length; i++) {
			System.out.println("y = " + y[i] + " ");
		}


	}
	*/

	/*
	public static void main(String[] args) {
		double[][] x = {{0.1234567890, 1.2345678}, {23.4567890, 456.456}, {5, 6, 7}};
		String[][] s = truncateDigits(x, 3);
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				System.out.println("s["+i+"]["+j+"] = " + s[i][j]);
			}
		}




	}

	/*
	public static void main(String[] args) {
		double[] x = {1, 2, 3, 4, 5, 6, 7};
		try {
			x = truncateArray(x, 1, 3);
			for (int i = 0; i < x.length; i++) {
				System.out.println(x[i]);
			}
		} catch (Exception e) {
		}
	}


	/*
	public static void main(String args[]) {
		//int[] test = sign(new double[] {-1.1, 0, 0.1, 100, 2f});
		//for (int i = 0; i < 5; i++) {
		//	System.out.println(test[i]);
		//}

		double[] test = {-1, 2, 3};
		double[] result = exp(test);
		for (int i = 0; i < test.length; i++) {
			System.out.println(result[i]);
		}
	}*/
	public static void main(String args[]) {
		getFlignerKilleenNormalQuantile(14, 1);
	}



}