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
/* annie chu. chea@stat.ucla.edu 20070813
*/

package edu.ucla.stat.SOCR.analyses.data;

import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.*;
import java.math.BigInteger;

public class ContingencyTable {
	private int grandTotal;

	private String[] rowName;
	private String[] colName;

	private int[] rowSumObserved;
	private int[] colSumObserved;
	private double[] rowSumExpected;
	private double[] colSumExpected;

	private int numberRow;
	private int numberCol;

	private double[][] observed; // first index is for row, second index for column
	private double[][] expected;

	public ContingencyTable(double[][] observed) {
		try {
			this.observed = observed;
			this.numberRow = observed.length;
			this.numberCol = observed[0].length;
			this.rowSumObserved = new int[this.numberRow];
			this.colSumObserved = new int[this.numberCol];
			double[][] currentColumnArray = new double[this.numberCol][this.numberRow]; // reverse column and row index for summing up by using AnalysisUtility.sum

			for (int i = 0; i < this.numberRow; i++) {
				rowSumObserved[i] = (int)AnalysisUtility.sum(observed[i]);
				for (int j = 0; j < this.numberCol; j++) {
					currentColumnArray[j][i] = observed[i][j]; // switch index.
					this.grandTotal += observed[i][j];
				}

			}

			for (int j = 0; j < this.numberCol; j++) {
				colSumObserved[j] = (int)AnalysisUtility.sum(currentColumnArray[j]);
			}

		} catch (DataIsEmptyException e) { // should throw something.....
		} catch (NullPointerException e) {
		}

	}


	public ContingencyTable(double[][] observed, String[] rowName, String[] colName) {
		try {
			this.observed = observed;
			this.numberRow = observed.length;
			this.numberCol = observed[0].length;
			this.rowName = rowName;// new String[this.numberRow];
			this.colName = colName;//new String[this.numberCol];
			this.rowSumObserved = new int[this.numberRow];
			this.colSumObserved = new int[this.numberCol];
			double[][] currentColumnArray = new double[this.numberCol][this.numberRow]; // reverse column and row index for summing up by using AnalysisUtility.sum

			for (int i = 0; i < this.numberRow; i++) {
				rowSumObserved[i] = (int)AnalysisUtility.sum(observed[i]);
				for (int j = 0; j < this.numberCol; j++) {
					currentColumnArray[j][i] = observed[i][j]; // switch index.
					this.grandTotal += observed[i][j];
				}

			}

			for (int j = 0; j < this.numberCol; j++) {
				colSumObserved[j] = (int)AnalysisUtility.sum(currentColumnArray[j]);
			}

		} catch (DataIsEmptyException e) { // should throw something.....
		} catch (NullPointerException e) {
		}

	}

	public ContingencyTable(double[][] observed, double[][] expected, String[] rowName, String[] colName) {

		new ContingencyTable(observed, rowName, colName);
		this.setExpected(expected);

	}

	public void setExpected(double[][] expected) {
		double[][] currentColumnArray = new double[this.numberCol][this.numberRow]; // reverse column and row index for summing up by using AnalysisUtility.sum
		try {
			for (int i = 0; i < this.numberRow; i++) {
				rowSumExpected[i] = (int)AnalysisUtility.sum(expected[i]);
				for (int j = 0; j < this.numberCol; j++) {
					currentColumnArray[j][i] = expected[i][j]; // switch index.
					//this.grandTotal += expected[i][j];
				}

			}

			for (int j = 0; j < this.numberCol; j++) {
				colSumExpected[j] = (int)AnalysisUtility.sum(currentColumnArray[j]);
			}

		} catch (DataIsEmptyException e) { // should throw something.....
			//System.out.println("DataIsEmptyException = " + e);
		} catch (NullPointerException e) {
			//System.out.println("NullPointerException = " + e);
		}
	}

	public void setExpectedProbabilities()  throws DataException{
		double[] rowProbability = new double[this.numberRow];
		double[] colProbability = new double[this.numberCol];
		for (int i = 0; i < this.numberRow; i++) {
			rowProbability[i] = (double)rowSumObserved[i] / (double)grandTotal;
		}
		for (int i = 0; i < this.numberCol; i++) {
			colProbability[i] = (double)colSumObserved[i] / (double)grandTotal;
		}


		rowSumExpected = new double[this.numberRow];
		colSumExpected = new double[this.numberCol];
		expected = new double[this.numberRow][this.numberCol];

		for (int i = 0; i < this.numberRow; i++) {

			expected[i] = new double[this.numberCol];
			rowSumExpected[i] = ( (double) grandTotal * rowProbability[i]);
			//System.out.println("rowSumExpected ["+i+"] = " + rowSumExpected[i]);
			for (int j = 0; j < this.numberCol; j++) {
				if (i == 0) { // just do it once, doesn't matter which i.
					colSumExpected[j] =  ( (double)grandTotal * colProbability[j] );
					/////System.out.println("colSumExpected ["+j+"] = " + colSumExpected[j]);
				}
				expected[i][j] = grandTotal *  rowProbability[i] * colProbability[j];
				/////System.out.println("expected["+i+"]["+j+"] = " + expected[i][j]);


			}
		}
	}


	public void setExpectedProbabilities(double[] rowProbability, double[] colProbability)  throws DataException{
		int rowLength = rowProbability.length;
		int colLength = colProbability.length;

		if (rowLength != this.numberRow || colLength != this.numberCol) {
			throw new DataException("Probablities not in correct dimension.");
		}

		rowSumExpected = new double[rowLength];
		colSumExpected = new double[colLength];
		expected = new double[rowLength][colLength];

		System.out.println("ContingencyTable: setExpceted row 2nd index ="+rowLength +"col 2st index= "+colLength);
		for (int i = 0; i < rowLength; i++) {

			expected[i] = new double[colLength];
			rowSumExpected[i] = ( (double) grandTotal * rowProbability[i]);
			//System.out.println("rowSumExpected ["+i+"] = " + rowSumExpected[i]);
			for (int j = 0; j < rowLength; j++) {
				if (i == 0) { // just do it once, doesn't matter which i.
					colSumExpected[j] =  ( (double)grandTotal * colProbability[j] );
					/////System.out.println("colSumExpected ["+j+"] = " + colSumExpected[j]);
				}
				expected[i][j] = grandTotal *  rowProbability[i] * colProbability[j];
				/////System.out.println("expected["+i+"]["+j+"] = " + expected[i][j]);


			}
		}

	}

	public int getGrandTotal() {
		//System.out.println("grandTotal = " + this.grandTotal);
		return this.grandTotal;
	}
	public int[] getRowSumObserved() {
		for (int i = 0; i < this.numberRow; i++) {
			//System.out.println("rowSum["+i+"] = " + this.rowSumObserved[i]);

		}
		//System.out.println("");
		return this.rowSumObserved;
	}

	public int[] getColSumObserved() {
		return this.colSumObserved;
	}
	public String[] getRowNames() {
		return this.rowName;
	}
	public int getNumberRow() {
		return this.numberRow;
	}
	public int getNumberCol() {
		return this.numberCol;
	}
	public String[] getColNames() {
		return this.colName;
	}

	// getPCutoff is for Fisher's exact test, cutoff of p-value.
	public double getPCutoff() throws NumberTooBigException {
		BigInteger[] rowFactorial = new BigInteger[this.numberRow];
		BigInteger[] colFactorial = new BigInteger[this.numberCol];

		BigInteger totalFactorial = AnalysisUtility.factorialBigInt(this.grandTotal);

		//System.out.println("totalFactorial = " + totalFactorial);

		for (int i = 0; i < this.numberRow; i++) {
			rowFactorial[i] = AnalysisUtility.factorialBigInt((int)this.rowSumObserved[i]);
			//System.out.println("BigInteger rowFactorial["+i+"] = " + rowFactorial[i]);
		}
		for (int j = 0; j < this.numberCol; j++) {
			try {
				colFactorial[j] = AnalysisUtility.factorialBigInt((int)this.colSumObserved[j]);
				//System.out.println("BigInteger colFactorial["+j+"] = " + colFactorial[j]);
			} catch (Exception e) {
				//System.out.println("colFactorial Exception = " + e);
			}
		}

		BigInteger nume = new BigInteger(0 + "");
		BigInteger deno = new BigInteger(1 + "");

		try {
			nume = AnalysisUtility.product(rowFactorial).multiply(AnalysisUtility.product(colFactorial));
			for (int i = 0; i < this.numberRow; i++) {
				for (int j = 0; j < this.numberCol; j++) {
					deno = deno.multiply(AnalysisUtility.factorialBigInt((int)observed[i][j]));
				}
			}
		} catch (Exception e) {
			//System.out.println("nume deno e = " + e);
		}
		deno = deno.multiply(AnalysisUtility.factorialBigInt((int)this.grandTotal));

		double result = nume.doubleValue() / deno.doubleValue();
		//System.out.println("getPCutoff BigInteger result = " + result);
		if ((new Double(result)).equals(new Double(Double.NaN))) {
			//System.out.println("getPCutoff BigInteger result == NaN!!");
			throw new NumberTooBigException("Some numbers in the calculation exceed tolarance");
		}
		return result;
	} // end getPCutoff

	// getPCutoff is for Fisher's exact test, cutoff of p-value.
	public double getPCutoff(boolean b) {
		double[] rowFactorial = new double[this.numberRow];
		double[] colFactorial = new double[this.numberCol];

		double totalFactorial = AnalysisUtility.factorial((int)this.grandTotal);

		//System.out.println("totalFactorial = " + totalFactorial);
		for (int i = 0; i < this.numberRow; i++) {
			rowFactorial[i] = AnalysisUtility.factorial((int)this.rowSumObserved[i]);
				//System.out.println("rowFactorial[i]        = " + rowFactorial[i]);
		}
		for (int j = 0; j < this.numberCol; j++) {
			try {
				colFactorial[j] = AnalysisUtility.factorial((int)this.colSumObserved[j]);
				//System.out.println("colFactorial[j]        = " + colFactorial[j]);
			} catch (Exception e) {
				//System.out.println("colFactorial Exception = " + e);
			}
		}

		double nume = 0;
		double deno = 1;

		try {
			nume = AnalysisUtility.product(rowFactorial) * AnalysisUtility.product(colFactorial);

			for (int i = 0; i < this.numberRow; i++) {
				for (int j = 0; j < this.numberCol; j++) {
					deno *= AnalysisUtility.factorial((int)observed[i][j]);
				}
			}
		} catch (Exception e) {
			//System.out.println("nume deno e = " + e);
		}

		deno *= totalFactorial;
		return nume/deno;
	} // end getPCutoff


	// assume null hypothesis asserts independent.
	// find pearson chi-sq = sum of (e - o) * (e - o) / e
	public double findPearsonChiSquare() {
		double[][] diff = new double[this.numberRow][this.numberCol];
		double chi = 0;
		for (int i = 0; i < this.numberRow; i++) {
			for (int j = 0; j < this.numberCol; j++) {
				diff[i][j] = expected[i][j] - observed[i][j];
				chi += (diff[i][j] * diff[i][j]) /  expected[i][j];
			}
		}
		//System.out.println("findPearsonChiSquare = " + chi);
		return chi;

	}

	public double[][] getExpected() {
		return this.expected;
	}

	public double getObserved(int i, int j) {
		if (0<=i && i< this.numberRow && 0<=j && j<this.numberCol)
			return this.observed[i][j];
		else return 0;
	}

	public double[][] getObserved() {
		return this.observed;
	}

	public int[] getRowSum() {
		return this.rowSumObserved;
	}

	public int[] getColSum() {
		return this.colSumObserved;
	}
	public int getDF() {
		int df =  ((this.numberRow - 1) * (this.numberCol - 1));
		//System.out.println("df = " + df);

		return df;
	}
	// example from here: http://mathworld.wolfram.com/FishersExactTest.html

	public static void main(String[] args) {
		double[][] test = {{100, 200, 10}, {1, 4, 10}};
		//double[][] test = {{4, 1}, {2, 3}};
		//double[][] test = {{3, 2}, {3, 2}};
		//double[][] test = {{2, 3}, {4, 1}};
		//double[][] test = {{550, 61}, {681, 144}};
		//double[][] test = {{1, 4}, {3, 5}};

		//double[][] test = {{147,186,101}, {25, 26, 11}, {32, 39, 15}, {94, 105, 37}, {59, 74, 28}, {18, 10, 10}};
		//String[] rname = {"math", "biology"};
		//String[] cname = {"Mathematics Magazine", "Science"};
		String[] rname = {"a", "an", "this", "that", "what", "without"};
		String[] cname = {"Sense and Sensibitilty", "Emma", "Sanction I"};
		//String[] rname = {"promote", "hold file"};
		//String[] cname = {"male", "female"};



		ContingencyTable ct = new ContingencyTable(test);//, rname, cname);
		ct.getRowNames();
		ct.getColNames();
		ct.getRowSumObserved();
		ct.getColSumObserved();
		ct.getGrandTotal();
		////System.out.println("pCutoff = " + ct.getPCutoff(false));
		try {
			System.out.println("pCutoff = " + ct.getPCutoff());
		} catch (NumberTooBigException e) {
			//System.out.println("pCutoff NumberTooBigException = " + e);
		}


		//System.out.println("");
		double[] colP = {.5, .5};

		try {
			//ct.setExpectedProbabilities(rowP, colP);
			ct.setExpectedProbabilities();
		} catch (Exception e) {
			//System.out.println("setExpectedProbabilities e = " + e);
		}

		ct.findPearsonChiSquare();
		ct.getDF();
	}


}
