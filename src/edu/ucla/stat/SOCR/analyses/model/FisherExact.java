/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and 

distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the 

Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
/* annieche 200608.
One-sided normal with known variance power computation
*/

package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;

import java.math.BigInteger;
import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class FisherExact implements Analysis {
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	public final static String OBSERVED_DATA = "OBSERVED_DATA";
	public final static String SIGNIFICANCE_LEVEL = "SIGNIFICANCE_LEVEL";
	public final static String ROW_NAMES = "ROW_NAMES";
	public final static String COL_NAMES = "COL_NAMES";
	private String[] rowNames;
	private String[] colNames;
	private String type = "FisherExact";
	private HashMap resultMap = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException, 

NumberTooBigException {
		Result result = null;
		//////System.out.println("FisherExact Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.FISHER_EXACT)
			throw new WrongAnalysisException();

		double alpha = Double.parseDouble((String)data.getParameter(analysisType, SIGNIFICANCE_LEVEL));
		double[][] observed = ((double[][])data.getInput(analysisType, OBSERVED_DATA));

		rowNames = ((String[])data.getInput(analysisType, ROW_NAMES));
		colNames = ((String[])data.getInput(analysisType, COL_NAMES));
		try {
			for (int i = 0; i < rowNames.length; i++) {
				//////System.out.println("in model pack rowNames = " + rowNames[i]);
			}

			for (int i = 0; i < colNames.length; i++) {
				//////System.out.println("in model pack colNames = " + colNames[i]);
			}
		} catch (Exception e) {
		}


		return getResult(observed, alpha);
	}

	private FisherExactResult getResult(double[][] observed, double alpha) throws DataIsEmptyException, 

NumberTooBigException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		FisherExactResult result = new FisherExactResult(texture);


		ContingencyTable ct = new ContingencyTable(observed);

		try {
			//ct.setExpectedProbabilities(rowP, colP);
			ct.setExpectedProbabilities();
		} catch (Exception e) {
			//////System.out.println("setExpectedProbabilities e = " + e);
		}

		double pearsonChiSquare = ct.findPearsonChiSquare();
		int df = ct.getDF();
		int grandTotal = ct.getGrandTotal();

		ct.getRowNames();
		ct.getColNames();
		ct.getRowSumObserved();
		ct.getColSumObserved();
		ct.getGrandTotal();

		//double[][] expected = ct.getExpected();

		int[] rowSum = ct.getRowSum();
		int[] colSum = ct.getColSum();

		//System.out.println("model rowSum.length= " + rowSum.length);
		//System.out.println("model colSum.length= " + colSum.length);
		//texture.put(FisherExactResult.EXPECTED_DATA, expected);
		//texture.put(FisherExactResult.ROW_NAMES, rowNames);
		texture.put(FisherExactResult.COL_NAMES, colNames);
		texture.put(FisherExactResult.DF, new Integer(df));

		texture.put(FisherExactResult.ROW_SUM, rowSum);
		texture.put(FisherExactResult.COL_SUM, colSum);
		texture.put(FisherExactResult.GRAND_TOTAL, new Integer(grandTotal));

		double pCutoff = FisherExactResult.INVALID_P_VALUE_CUTOFF;
		double pCutoff1Tail = FisherExactResult.INVALID_P_VALUE_CUTOFF_1_TAIL;
		double pCutoff2Tail = FisherExactResult.INVALID_P_VALUE_CUTOFF_2_TAIL;
		try {
			pCutoff = ct.getPCutoff();
			texture.put(FisherExactResult.P_VALUE_CUTOFF, new Double(pCutoff));
			pCutoff1Tail = get1TailPCutoff(ct, pCutoff);
			texture.put(FisherExactResult.P_VALUE_CUTOFF_1_TAIL, new Double(pCutoff1Tail));
			pCutoff2Tail = get2TailPCutoff(ct, pCutoff)+pCutoff1Tail;
			if (pCutoff2Tail>1) pCutoff2Tail=1;
			texture.put(FisherExactResult.P_VALUE_CUTOFF_2_TAIL, new Double(pCutoff2Tail));
		} catch (Exception e) {
			texture.put(FisherExactResult.P_VALUE_CUTOFF, new Double(pCutoff));
		}

		return result;
	}
	// See data example here: http://mathworld.wolfram.com/FishersExactTest.html
	
	
	// get2TailPCutoff returns the (right) 1-Tailed P-value for Fisher's exact test.
	// See the protocol here: http://en.wikipedia.org/wiki/Fisher%27s_exact_test
	// Examples: http://www.physics.csbsju.edu/stats/exact2.html 
	public double get1TailPCutoff(ContingencyTable contTable, double p_naught) 
			throws NumberTooBigException {
		/*
		 * p_naught is the p-value = ct.getPCutoff();
		 * 
		 * 1. This 1-sided p-value formula gives the exact probability of observing 
		 * this particular arrangement of the data, assuming the given marginal totals, 
		 * on the null hypothesis (independence). 
		 * 
		 * Fisher showed that we could deal only with cases where the 
		 * *marginal totals are the same* as in the observed table.
		 * 
		 * In order to calculate the significance of the observed data, 
		 * i.e. the total probability of observing data as extreme or more extreme 
		 * if the null hypothesis is true, we have to calculate the values of p for 
		 * both these tables, and add them together. This gives a one-tailed test; 
		 * for a two-tailed test we must also consider tables that are equally extreme 
		 * but in the opposite direction. Unfortunately, classification of the tables 
		 * according to whether or not they are 'as extreme' is problematic. 
		 * An approach used by the R programming language is to compute the p-value 
		 * by summing the probabilities for all tables, whose probabilities are 
		 * less than or equal to that of the observed table. 
		 * 
		 * For tables with small counts, the 2-sided p-value can differ substantially 
		 * from twice the 1-sided value, unlike the case with test statistics that have 
		 * a symmetric sampling distribution.
		 */
		
		//Variables
		double pValue1tail=0;	// The iteratively-computed New 1-sizded p-value
		int min1=0, max1;	//Limit of the iterations, max1 = min row or column sum!
		
		int i, j;
		int a, b, c, d;	// these are the updated ContingencyTable "observed/derived" values

		//		 Table must have >= 2 columens and >= 2 rows!
		if (contTable.getNumberCol()<2 || contTable.getNumberRow()<2)
			return 1;
		else if (contTable.getNumberCol()>2 || contTable.getNumberRow()>2)
			return get1TailPCutoffHigherOrder(contTable, p_naught);
		//		For matrices of higher order (column or row count >2)
		
		//	Else for a 2x2 matrix do:
		
		ContingencyTable ct = new ContingencyTable(contTable.getObserved());
		int rowOrColumn=0;		//rowOrColumn=0 for min row-sum, 
								//rowOrColumn=1 for min col-sum
		int pivot_i, pivot_j;	//pivoting valie of the min row or column sum
							// the value at this (X,Y) position will vary [0, UpperLimit]
		
		//System.err.println("Inside method: 
		//     get2TailPCutoff(ContingencyTable contTable, double p_naught)\n");
		
		// Find max1, pivot_i, pivot_j and rowOrColumn
		max1=contTable.getRowSum()[0]; rowOrColumn=0; 
			if (contTable.getObserved(0,0)<=contTable.getObserved(0,1)) {
				pivot_i=0; pivot_j=0;
			} else {
				pivot_i=0; pivot_j=1;
			}
		if (max1>contTable.getRowSum()[1]) {
			max1=contTable.getRowSum()[1]; rowOrColumn=0;
			if (contTable.getObserved(1,0)<=contTable.getObserved(1,1)) {
				pivot_i=1; pivot_j=0;
			} else {
				pivot_i=1; pivot_j=1;
			}
		}
		if (max1>contTable.getColSum()[0]) {
			max1=contTable.getColSum()[0]; rowOrColumn=1;
			if (contTable.getObserved(0,0)<=contTable.getObserved(1,0)) {
				pivot_i=0; pivot_j=0;
			} else {
				pivot_i=1; pivot_j=0;
			}
		}
		if (max1>contTable.getColSum()[1]) {
			max1=contTable.getColSum()[1]; rowOrColumn=1;
			if (contTable.getObserved(0,1)<=contTable.getObserved(1,1)) {
				pivot_i=0; pivot_j=1;
			} else {
				pivot_i=1; pivot_j=1;
			}
		}
					
		if (min1==max1) return 1;	//trivial case
		
		// LOOP
		for (int k=min1; k<max1; k++) {
			// set new CT matrix: 
			//	a	b
			//	c	d
			
			if (pivot_i==0 && pivot_j==0) {			// Pivot = a
				double[][] test = {
					{k, contTable.getRowSum()[0]-k}, 
					{contTable.getColSum()[0]-k, 
						contTable.getRowSum()[1]-contTable.getColSum()[0]+k}};
				ct = new ContingencyTable(test);
			} else if (pivot_i==0 && pivot_j==1) {	// Pivot = b
				double[][] test = {
					{contTable.getRowSum()[0]-k, k}, 
					{contTable.getRowSum()[1]-contTable.getColSum()[1]+k, 
						contTable.getColSum()[1]-k}};
				ct = new ContingencyTable(test);
			} else if (pivot_i==1 && pivot_j==0) {	// Pivot = c
				double[][] test = {
						{contTable.getColSum()[0]-k, 
							contTable.getRowSum()[0]-contTable.getColSum()[0]+k}, 
						{k, contTable.getRowSum()[1]-k}};
					ct = new ContingencyTable(test);
			} else if (pivot_i==1 && pivot_j==1) {	// Pivot = d
				double[][] test = {
						{contTable.getRowSum()[0]-contTable.getColSum()[1]+k, 
							contTable.getColSum()[1]-k}, 
						{contTable.getRowSum()[1]-k, k}};
				ct = new ContingencyTable(test);
			}

			// Check to make sure that row & column sums are UNCHANGED
			for (i = 0; i < ct.getNumberRow(); i++) {
				if (contTable.getRowSum()[i]!=ct.getRowSum()[i]) {
					System.err.println("OLD.getRowSum()["+i+"]="+
							contTable.getRowSum()[i]+
							"\t new.getRowSum()["+i+"]="+ct.getRowSum()[i]);
				}
			}
			for (j = 0; j < ct.getNumberCol(); j++) {
				if (contTable.getColSum()[j]!=ct.getColSum()[j]) {
					System.err.println("OLD.getColSum()["+j+"]="+
							contTable.getColSum()[j]+
							"\t new.getColSum()["+j+"]="+ct.getColSum()[j]);
				}
			}

			
			BigInteger[] rowFactorial = new BigInteger[ct.getNumberRow()];
			BigInteger[] colFactorial = new BigInteger[ct.getNumberCol()];

			for (i = 0; i < ct.getNumberRow(); i++) {
				rowFactorial[i] = 
					AnalysisUtility.factorialBigInt((int)ct.getRowSumObserved()[i]);
			}
			for (j = 0; j < ct.getNumberCol(); j++) {
				try {
					colFactorial[j] = 
						AnalysisUtility.factorialBigInt((int)ct.getColSumObserved()[j]);
				} catch (Exception e) {
				}
			}

			BigInteger nume = new BigInteger(0 + "");
			BigInteger deno = new BigInteger(1 + "");

			try {
				nume = AnalysisUtility.product(rowFactorial).multiply(
						AnalysisUtility.product(colFactorial));
				for (i = 0; i < ct.getNumberRow(); i++) {
					for (j = 0; j < ct.getNumberCol(); j++) {
						deno = deno.multiply(AnalysisUtility.factorialBigInt(
							(int)ct.getObserved(i, j)));
					}
				}
			} catch (Exception e) {
				//System.out.println("nume deno e = " + e);
			}
			deno = deno.multiply(AnalysisUtility.factorialBigInt((int)ct.getGrandTotal()));

			double result = nume.doubleValue() / deno.doubleValue();
			if ((new Double(result)).equals(new Double(Double.NaN))) {
				throw new NumberTooBigException("" +
						"Some numbers in the calculation exceed tolarance");
			}
			
			if (0< result && result < p_naught) {
				pValue1tail += result;
			}
		}  // END LOOP
		
		pValue1tail+= p_naught;

		if (pValue1tail<0) pValue1tail=0;
		else if (pValue1tail>1) pValue1tail=1;

		return pValue1tail;
	} // end get1TailPCutoff()
	
	// get2TailPCutoff returns the 2-Tailed P-value for Fisher's exact test.
	// This is *NOT* 2*p_Value, as the distribution is not symmetric
	// See the protocol here: http://en.wikipedia.org/wiki/Fisher%27s_exact_test
	public double get2TailPCutoff(ContingencyTable contTable, double p_naught) 
			throws NumberTooBigException {
		/*
		 * p_naught is the p-value = ct.getPCutoff();
		 * 
		 * 1. This 1-sided p-value formula gives the exact probability of observing 
		 * this particular arrangement of the data, assuming the given marginal totals, 
		 * on the null hypothesis (independence). 
		 * 
		 * Fisher showed that we could deal only with cases where the 
		 * *marginal totals are the same* as in the observed table.
		 * 
		 * In order to calculate the significance of the observed data, 
		 * i.e. the total probability of observing data as extreme or more extreme 
		 * if the null hypothesis is true, we have to calculate the values of p for 
		 * both these tables, and add them together. This gives a one-tailed test; 
		 * for a two-tailed test we must also consider tables that are equally extreme 
		 * but in the opposite direction. Unfortunately, classification of the tables 
		 * according to whether or not they are 'as extreme' is problematic. 
		 * An approach used by the R programming language is to compute the p-value 
		 * by summing the probabilities for all tables, whose probabilities are 
		 * less than or equal to that of the observed table. 
		 * 
		 * For tables with small counts, the 2-sided p-value can differ substantially 
		 * from twice the 1-sided value, unlike the case with test statistics that have 
		 * a symmetric sampling distribution.
		 */
		
		//Variables
		double pValue2tail=0;	// The iteratively-computed New 2-sizded p-value
		int min1=0, max1;	//Limit of the iterations, max1 = min row or column sum!
		
		int i, j;
		int a, b, c, d;	// these are the updated ContingencyTable "observed/derived" values

		//		 Table must have >= 2 columens and >= 2 rows!
		if (contTable.getNumberCol()<2 || contTable.getNumberRow()<2)
			return 1;
		else if (contTable.getNumberCol()>2 || contTable.getNumberRow()>2)
			return get2TailPCutoffHigherOrder(contTable, p_naught);
		//		For matrices of higher order (column or row count >2)
		
		//	Else for a 2x2 matrix do:
		
		ContingencyTable ct = new ContingencyTable(contTable.getObserved());
		int rowOrColumn=0;		//rowOrColumn=0 for min row-sum, 
								//rowOrColumn=1 for min col-sum
		int pivot_i, pivot_j;	//pivoting valie of the min row or column sum
							// the value at this (X,Y) position will vary [0, UpperLimit]
		
		//System.err.println("Inside method: 
		//     get2TailPCutoff(ContingencyTable contTable, double p_naught)\n");
		
		// Find max1, pivot_i, pivot_j and rowOrColumn
		max1=contTable.getRowSum()[0]; rowOrColumn=0; 
			if (contTable.getObserved(0,0)<=contTable.getObserved(0,1)) {
				pivot_i=0; pivot_j=0;
			} else {
				pivot_i=0; pivot_j=1;
			}
		if (max1>contTable.getRowSum()[1]) {
			max1=contTable.getRowSum()[1]; rowOrColumn=0;
			if (contTable.getObserved(1,0)<=contTable.getObserved(1,1)) {
				pivot_i=1; pivot_j=0;
			} else {
				pivot_i=1; pivot_j=1;
			}
		}
		if (max1>contTable.getColSum()[0]) {
			max1=contTable.getColSum()[0]; rowOrColumn=1;
			if (contTable.getObserved(0,0)<=contTable.getObserved(1,0)) {
				pivot_i=0; pivot_j=0;
			} else {
				pivot_i=1; pivot_j=0;
			}
		}
		if (max1>contTable.getColSum()[1]) {
			max1=contTable.getColSum()[1]; rowOrColumn=1;
			if (contTable.getObserved(0,1)<=contTable.getObserved(1,1)) {
				pivot_i=0; pivot_j=1;
			} else {
				pivot_i=1; pivot_j=1;
			}
		}
					
		if (min1==max1) return 1;	//trivial case
		
		// LOOP
		for (int k=min1; k<max1; k++) {
			// set new CT matrix: 
			//	a	b
			//	c	d
			
			if (pivot_i==0 && pivot_j==0) {			// Pivot = a
				double[][] test = {
					{max1-k, contTable.getRowSum()[0]-max1+k}, 
					{contTable.getColSum()[0]-max1+k, 
						contTable.getRowSum()[1]-contTable.getColSum()[0]+max1-k}};
				ct = new ContingencyTable(test);
			} else if (pivot_i==0 && pivot_j==1) {	// Pivot = b
				double[][] test = {
					{contTable.getRowSum()[0]-max1+k, max1-k}, 
					{contTable.getRowSum()[1]-contTable.getColSum()[1]+max1-k, 
						contTable.getColSum()[1]-max1+k}};
				ct = new ContingencyTable(test);
			} else if (pivot_i==1 && pivot_j==0) {	// Pivot = c
				double[][] test = {
						{contTable.getColSum()[0]-max1+k, 
							contTable.getRowSum()[0]-contTable.getColSum()[0]+max1-k}, 
						{max1-k, contTable.getRowSum()[1]-max1+k}};
					ct = new ContingencyTable(test);
			} else if (pivot_i==1 && pivot_j==1) {	// Pivot = d
				double[][] test = {
						{contTable.getRowSum()[0]-contTable.getColSum()[1]+max1-k, 
							contTable.getColSum()[1]-max1+k}, 
						{contTable.getRowSum()[1]-max1+k, max1-k}};
				ct = new ContingencyTable(test);
			}

			// Check to make sure that row & column sums are UNCHANGED
			for (i = 0; i < ct.getNumberRow(); i++) {
				if (contTable.getRowSum()[i]!=ct.getRowSum()[i]) {
					System.err.println("OLD2.getRowSum()["+i+"]="+
							contTable.getRowSum()[i]+
							"\t New2.getRowSum()["+i+"]="+ct.getRowSum()[i]);
				}
			}
			for (j = 0; j < ct.getNumberCol(); j++) {
				if (contTable.getColSum()[j]!=ct.getColSum()[j]) {
					System.err.println("OLD2.getColSum()["+j+"]="+
							contTable.getColSum()[j]+
							"\t New2.getColSum()["+j+"]="+ct.getColSum()[j]);
				}
			}

			
			BigInteger[] rowFactorial = new BigInteger[ct.getNumberRow()];
			BigInteger[] colFactorial = new BigInteger[ct.getNumberCol()];

			for (i = 0; i < ct.getNumberRow(); i++) {
				rowFactorial[i] = 
					AnalysisUtility.factorialBigInt((int)ct.getRowSumObserved()[i]);
			}
			for (j = 0; j < ct.getNumberCol(); j++) {
				try {
					colFactorial[j] = 
						AnalysisUtility.factorialBigInt((int)ct.getColSumObserved()[j]);
				} catch (Exception e) {
				}
			}

			BigInteger nume = new BigInteger(0 + "");
			BigInteger deno = new BigInteger(1 + "");

			try {
				nume = AnalysisUtility.product(rowFactorial).multiply(
						AnalysisUtility.product(colFactorial));
				for (i = 0; i < ct.getNumberRow(); i++) {
					for (j = 0; j < ct.getNumberCol(); j++) {
						deno = deno.multiply(AnalysisUtility.factorialBigInt(
							(int)ct.getObserved(i, j)));
					}
				}
			} catch (Exception e) {
				//System.out.println("nume deno e = " + e);
			}
			deno = deno.multiply(AnalysisUtility.factorialBigInt((int)ct.getGrandTotal()));

			double result = nume.doubleValue() / deno.doubleValue();
			if ((new Double(result)).equals(new Double(Double.NaN))) {
				throw new NumberTooBigException("" +
						"Some numbers in the calculation exceed tolarance");
			}
			
			if (0< result && result < p_naught) {
				pValue2tail += result;
			}
		}  // END LOOP
		
		if (pValue2tail<0) pValue2tail=0;
		else if (pValue2tail>1) pValue2tail=1;
		
		return pValue2tail;
	} // end get2TailPCutoff()

	// get1TailPCutoffHigherOrder returns the 1-Tailed P-value for Fisher's exact test.
	// for IxJ matrices of order > 2x2
	// This uses the Mask-Metropolis Algorithm:
	// Randomization of real-valued matrices for assessing the significance of data mining results
	//  by Markus Ojala, Niko Vuokko, Aleksi Kallio, Niina Haiminen, Heikki Mannila, SIAM 2008
	// http://www.siam.org/proceedings/datamining/2008/dm08_45_ojala.pdf 
	public double get1TailPCutoffHigherOrder(ContingencyTable contTable, double p_naught) 
			throws NumberTooBigException {
		/*
		 * p_naught is the p-value = ct.getPCutoff();
		 * 
		 * Mask-Metripolis Algorithm:
		 * Inputs:	Matrix A=contTable, 
		 * 			attempts K (K=1,000), 
		 * 			limiter w > 0 (w=0.01), 
		 * 			scale s > 0 (s=min(i1, i2, j1, j2)/2)
		 * ct = getNewMaskMetropolisMatrix(ct, k, w, s);
		 */
		
		//Variables
		double pValue1tail=0;	// The iteratively-computed New 1-sided p-value
		int incidenceCounter = 0;	// This is the counter of number of the (maxIter)
									// randomly generated matrices that have p-values < p_naught
		
		// Now many random matrices will we be generating (maxIter)
		int maxIter = 100*contTable.getNumberCol()*contTable.getNumberRow();
		
		//		 Table must have > 2 columns or > 2 rows!
		if (contTable.getNumberCol()<=2 && contTable.getNumberRow()<=2)
			return get1TailPCutoff(contTable, p_naught);
				
		ContingencyTable ct;
		BigInteger nume, deno;
		BigInteger[] rowFactorial, colFactorial;
		
		// LOOP
		for (int iter=0; iter<maxIter; iter++) {
			// Get a new Random Matrix
			System.err.println("get2TailPCutoffHigherOrder:: Iteration: "+iter+"/"+maxIter);

			ct = getNewMaskMetropolisMatrix(contTable, p_naught);
			rowFactorial = new BigInteger[ct.getNumberRow()];
			colFactorial = new BigInteger[ct.getNumberCol()];

			// Check to make sure that row & column sums are UNCHANGED
			for (int row = 0; row < ct.getNumberRow(); row++) {
				if (contTable.getRowSum()[row]!=ct.getRowSum()[row]) {
					System.err.println("OLD.getRowSum()["+row+"]="+
							contTable.getRowSum()[row]+
							"\t new.getRowSum()["+row+"]="+ct.getRowSum()[row]);
				}
			}
			for (int col = 0; col < ct.getNumberCol(); col++) {
				if (contTable.getColSum()[col]!=ct.getColSum()[col]) {
					System.err.println("OLD.getColSum()["+col+"]="+
							contTable.getColSum()[col]+
							"\t new.getColSum()["+col+"]="+ct.getColSum()[col]);
				}
			}
			
			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -5");
			
			for (int row = 0; row < ct.getNumberRow(); row++) {
				rowFactorial[row] = 
					AnalysisUtility.factorialBigInt((int)ct.getRowSumObserved()[row]);
			}
			
			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -4");
			
			for (int col = 0; col < ct.getNumberCol(); col++) {
				try {
					colFactorial[col] = 
						AnalysisUtility.factorialBigInt((int)ct.getColSumObserved()[col]);
				} catch (Exception e) {
				}
			}

			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -3");
			
			nume = new BigInteger(0 + "");
			deno = new BigInteger(1 + "");
			
			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -2");
			
			try {
				nume = AnalysisUtility.product(rowFactorial).multiply(
						AnalysisUtility.product(colFactorial));
				
				for (int row = 0; row < ct.getNumberRow(); row++) {
					for (int col = 0; col < ct.getNumberCol(); col++) {
						System.err.println("::get2TailPCutoffHigherOrder:: "
								+"row="+row+", col="+col+"; value="+
								ct.getObserved(row, col));
						deno = deno.multiply(AnalysisUtility.factorialBigInt(
							(int)Math.abs(ct.getObserved(row, col))));
						System.err.println("::get2TailPCutoffHigherOrder:: "
								+"row="+row+", col="+col+"; deno="+deno);
					}
				}
			} catch (Exception e) {
				System.err.println("::get2TailPCutoffHigherOrder:: Test-point 0");
			}
			
			System.err.println("::get1TailPCutoffHigherOrder:: Test-point 1");

			nume = nume.divide(AnalysisUtility.factorialBigInt((int)ct.getGrandTotal()));
			//deno = deno.multiply(AnalysisUtility.factorialBigInt((int)ct.getGrandTotal()));

			double result = nume.doubleValue() / deno.doubleValue();
			if ((new Double(result)).equals(new Double(Double.NaN))) {
				throw new NumberTooBigException("" +
						"Some numbers in the calculation exceed tolarance");
			}
			
			System.err.println("::get2TailPCutoffHigherOrder:: RESULT = "+result);

			if (0<= result && result < p_naught) {
				incidenceCounter += 1;
			}
		}  // END LOOP
		
		pValue1tail = p_naught + (1.0*incidenceCounter)/maxIter;

		if (pValue1tail<0) pValue1tail=0;
		else if (pValue1tail>1) pValue1tail=1;

		return pValue1tail;
	} // end get1TailPCutoffHigherOrder()

	// get2TailPCutoffHigherOrder returns the 2-Tailed P-value for Fisher's exact test.
	// for IxJ matrices of order > 2x2
	// This uses the Mask-Metropolis Algorithm:
	// Randomization of real-valued matrices for assessing the significance of data mining results
	//  by Markus Ojala, Niko Vuokko, Aleksi Kallio, Niina Haiminen, Heikki Mannila, SIAM 2008
	// 
	// http://www.siam.org/proceedings/datamining/2008/dm08_45_ojala.pdf 
	public double get2TailPCutoffHigherOrder(ContingencyTable contTable, double p_naught) 
			throws NumberTooBigException {
		/*
		 * p_naught is the p-value = ct.getPCutoff();
		 * 
		 * Mask-Metripolis Algorithm:
		 * Inputs:	Matrix A=contTable, 
		 * 			attempts K (K=1,000), 
		 * 			limiter w > 0 (w=0.01), 
		 * 			scale s > 0 (s=min(i1, i2, j1, j2)/2)
		 * ct = getNewMaskMetropolisMatrix(ct, k, w, s);
		 */
		
		//Variables
		double pValue2tail=0;	// The iteratively-computed New 2-sizded p-value
		int incidenceCounter = 0;	// This is the counter of number of the (maxIter)
									// randomly generated matrices that have p-values < p_naught
		
		// Now many random matrices will we be generating (maxIter)
		int maxIter = 100*contTable.getNumberCol()*contTable.getNumberRow();
		
		//		 Table must have > 2 columns or > 2 rows!
		if (contTable.getNumberCol()<=2 && contTable.getNumberRow()<=2)
			return get2TailPCutoff(contTable, p_naught);
				
		ContingencyTable ct;
		BigInteger nume, deno;
		BigInteger[] rowFactorial, colFactorial;
		
		// LOOP
		for (int iter=0; iter<maxIter; iter++) {
			// Get a new Random Matrix
			System.err.println("get2TailPCutoffHigherOrder:: Iteration: "+iter+"/"+maxIter);

			ct = getNewMaskMetropolisMatrix(contTable, p_naught);
			rowFactorial = new BigInteger[ct.getNumberRow()];
			colFactorial = new BigInteger[ct.getNumberCol()];

			// Check to make sure that row & column sums are UNCHANGED
			for (int row = 0; row < ct.getNumberRow(); row++) {
				if (contTable.getRowSum()[row]!=ct.getRowSum()[row]) {
					System.err.println("OLD2.getRowSum()["+row+"]="+
							contTable.getRowSum()[row]+
							"\t new2.getRowSum()["+row+"]="+ct.getRowSum()[row]);
				}
			}
			for (int col = 0; col < ct.getNumberCol(); col++) {
				if (contTable.getColSum()[col]!=ct.getColSum()[col]) {
					System.err.println("OLD2.getColSum()["+col+"]="+
							contTable.getColSum()[col]+
							"\t new2.getColSum()["+col+"]="+ct.getColSum()[col]);
				}
			}
			
			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -5");
			
			for (int row = 0; row < ct.getNumberRow(); row++) {
				rowFactorial[row] = 
					AnalysisUtility.factorialBigInt((int)ct.getRowSumObserved()[row]);
			}
			
			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -4");
			
			for (int col = 0; col < ct.getNumberCol(); col++) {
				try {
					colFactorial[col] = 
						AnalysisUtility.factorialBigInt((int)ct.getColSumObserved()[col]);
				} catch (Exception e) {
				}
			}

			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -3");
			
			nume = new BigInteger(0 + "");
			deno = new BigInteger(1 + "");
			
			//System.err.println("::get2TailPCutoffHigherOrder:: Test-point -2");
			
			try {
				nume = AnalysisUtility.product(rowFactorial).multiply(
						AnalysisUtility.product(colFactorial));
				
				for (int row = 0; row < ct.getNumberRow(); row++) {
					for (int col = 0; col < ct.getNumberCol(); col++) {
						System.err.println("::get2TailPCutoffHigherOrder:: "
								+"row="+row+", col="+col+"; value="+
								ct.getObserved(row, col));
						deno = deno.multiply(AnalysisUtility.factorialBigInt(
							(int)Math.abs(ct.getObserved(row, col))));
						System.err.println("::get2TailPCutoffHigherOrder:: "
								+"row="+row+", col="+col+"; deno="+deno);
					}
				}
			} catch (Exception e) {
				System.err.println("::get2TailPCutoffHigherOrder:: Test-point 0");
			}
			
			System.err.println("::get2TailPCutoffHigherOrder:: Test-point 1");

			nume = nume.divide(AnalysisUtility.factorialBigInt((int)ct.getGrandTotal()));
			//deno = deno.multiply(AnalysisUtility.factorialBigInt((int)ct.getGrandTotal()));

			double result = nume.doubleValue() / deno.doubleValue();
			if ((new Double(result)).equals(new Double(Double.NaN))) {
				throw new NumberTooBigException("" +
						"Some numbers in the calculation exceed tolarance");
			}
			
			System.err.println("::get2TailPCutoffHigherOrder:: RESULT = "+result);

			if (0<= result && result < p_naught) {
				incidenceCounter += 1;
			}
		}  // END LOOP
		
		pValue2tail = (1.0*incidenceCounter)/maxIter;

		if (pValue2tail<0) pValue2tail=0;
		else if (pValue2tail>1) pValue2tail=1;

		return pValue2tail;
	} // end get2TailPCutoffHigherOrder()

	// See getNewMaskMetropolisMatrix() method and references
	public double ErrorMatrix(ContingencyTable A1, ContingencyTable  A2) {
		double matrixError = 0; 
		
		if (A1.getNumberRow()!=A2.getNumberRow() && A1.getNumberCol()!=A2.getNumberCol())
			return 100;
		
		for (int row = 0; row < A1.getNumberRow(); row++)
				matrixError += 
					Math.pow(A1.getRowSum()[row]-A2.getRowSum()[row], 2);
		for (int col = 0; col < A1.getNumberCol(); col++)
			matrixError += 
				Math.pow(A1.getColSum()[col]-A2.getColSum()[col], 2);
		return matrixError;
	}
		
	// http://www.siam.org/proceedings/datamining/2008/dm08_45_ojala.pdf 
	public ContingencyTable getNewMaskMetropolisMatrix(ContingencyTable contTable, double p_naught) 
			throws NumberTooBigException {
		/* Mask-Metripolis Algorithm:
		 * E(A,A ) is the error of the sample A  as defined by
		 *
		 * Inputs:	Matrix A=contTable, 
		 * 			# attempts K (K=1,000), 
		 * 			limiter w > 0 (w=0.01), 
		 * 			scale s > 0 (s=min(i1, i2, j1, j2)/2)
		 * 			The parameter transition scale s defines the
		 * 			range [-s, s], from which a is selected uniformly at random.
		 * 
		 * 1: A1 = A
		 * 2: 	for (i=0; i< K; i++)
		 * 3: 	{	Pick (i1 != i2 && j1 != j2 randomly)
		 * 4: 		a = Uniform(-s,s)
		 * 5: 		A2 = AddMask(A1, a, i1, i2, j1, j2)
		 * 6: 		if for all i, j : 0 < A'[i][j])
		 * 7: 		{	u = Uniform(0,1)
		 * 8: 			if (u < exp{-w(E(A,A2) - E(A,A1))})
		 * 9: 			{	A1 = A2
		 * 10:				matrix_counter++
		 * 11:			}
		 * 12: 		}
		 * 13: 	 }
		 * 14: return A1
		 * 
		 * Error(A1, A2) = 	\sum_{i}{(A1_Row_i - A2_Row_i)^2} + 
		 * 					\sum_{j}{(A1_Col_j - A2_Col_j)^2 }
		 * 
		 * AddMask - auxiliary method that adds this mask to A=CT matrix
		 *     ..... j1 .... j2 ....
		 *     ..... | ..... | .....
		 *  i1 .... +a .... -a .....
		 *     ..... | ..... | .....
		 *  i2 .... -a .... +a .....
		 *     ..... | ..... | .....
		 */
		ContingencyTable A1 = contTable;
		ContingencyTable A2;
		double[][] matrix2;
		
		int K = 15; 				// Number of attempts
		int matrix_counter = 0;
		int i1, i2, j1, j2;
		double a=0;					// a = Uniform(-s,s)
		double s;					//s=min(i1, i2, j1, j2)/2
		double w=0.01;				// Error parameter specifying efficiency of mixing
		
		edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution dist;
		
		for (int i=0; i< K; i++){	
			//System.err.println("getNewMaskMetropolisMatrix()::iteration="+i+"/"+K);

				// 1. Pick (i1 != i2 && j1 != j2 randomly)
			i1 = (int)(A1.getNumberRow()*Math.random());
			j1 = (int)(A1.getNumberCol()*Math.random());
		
			i2 = (int)(A1.getNumberRow()*Math.random());
			j2 = (int)(A1.getNumberCol()*Math.random());
		
			while (i2==i1)
				i2 = (int)(A1.getNumberRow()*Math.random());
			while (j2==j1)
				j2 = (int)(A1.getNumberCol()*Math.random());
			
			 	// a = Uniform(-s,s)
			s = 1+ Math.min(Math.min(i1, i2), Math.min(j1, j2));
			dist = new edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution(-s, s);
			a = dist.simulate();
			//System.err.println("getNewMaskMetropolisMatrix()::a="+a);

				//	A2 = AddMask(A1, a, i1, i2, j1, j2)
			matrix2 = new double[A1.getNumberRow()][A1.getNumberCol()];
			for (int row = 0; row < A1.getNumberRow(); row++) {
				for (int col = 0; col < A1.getNumberCol(); col++) {
					if (row==i1 && col==j1)
						matrix2[i1][j1] = A1.getObserved(i1, j1)+a;
					else if (row==i1 && col==j2)
						matrix2[i1][j2] = A1.getObserved(i1, j2)-a;
					else if(row==i2 && col==j1)
						matrix2[i2][j1] = A1.getObserved(i2, j1)-a;
					else if(row==i2 && col==j2)
						matrix2[i2][j2] = A1.getObserved(i2, j2)+a;
					else matrix2[row][col] = A1.getObserved(row, col);
				}
			}
						
			A2 = new ContingencyTable(matrix2);
				//		Note that A2[i][j] >=0 for all i and j
			//System.err.println("getNewMaskMetropolisMatrix():: A2 ready");

				// a = Uniform(0,1)
			dist = new edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution(0,1);
			a = dist.simulate();
			
			//System.err.println("getNewMaskMetropolisMatrix()::TEST: iteration="+i+"/"+K);

			 	// if (a < exp{-w(E(A,A2) - E(A,A1))})
			 	//		{	A1 = A2; matrix_counter++; }
			//if (a < Math.exp(-w*(ErrorMatrix(contTable, A2) - ErrorMatrix(contTable, A1)))) {
			if (ErrorMatrix(contTable, A2)<1) {
				A1 = A2;
				matrix_counter++;
			}
		}

		//System.err.println("getNewMaskMetropolisMatrix()::A1=A2 swap? matrix_counter="+matrix_counter+"/"+K);
		return A1;
	}	// End getNewMaskMetropolisMatrix
}
