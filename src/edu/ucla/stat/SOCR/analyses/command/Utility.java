/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org

All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.

SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.

http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
It s Online, Therefore, It Exists!
****************************************************/
package edu.ucla.stat.SOCR.analyses.command;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.exception.*;

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


	public static double[] listToDoubleArray(ArrayList<Double> list) {
		int size = list.size();
		double[] result = new double[size];

		//for (int i = size-1; i >= 0; i--) {
		for (int i = 0; i < size; i++) {

			result[i] = ((Double)list.get(i)).doubleValue();
			//////System.out.println("
		}
		return result;
	}
	public static int[] listToIntArray(ArrayList<Integer> list) {
		int size = list.size();
		int[] result = new int[size];

		//for (int i = size-1; i >= 0; i--) {
		for (int i = 0; i < size; i++) {

			result[i] = ((Integer)list.get(i)).intValue();
			//////System.out.println("
		}
		return result;
	}

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

	public static void main(String[] args) {
		double[][] x = {{0.1234567890, 1.2345678}, {23.4567890, 456.456}, {5, 6, 7}};
		String[][] s = truncateDigits(x, 3);
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");

		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				System.out.println("s["+i+"]["+j+"] = " + s[i][j]);
			}
		}
	}

	public static String getErrorMessage(String analysisType) {
		return "\nIncorrect CSV file format for SOCR " + analysisType + " analysis. Please see the default data format for this type of analysis by going to SOCR Analysis (http://www.socr.ucla.edu/htmls/SOCR_Analyses.html) and looking at the default Example 1 format. Then construct your text input CSV file according to the schema in this example dataset. You may also refer to the SOCR Analysis instructions on the web (http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_Analyses).\n";
	}

}
