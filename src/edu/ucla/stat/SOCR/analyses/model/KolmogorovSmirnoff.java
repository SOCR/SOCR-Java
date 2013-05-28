// java edu.ucla.stat.SOCR.analyses.model.KolmogorovSmirnoff

// what's the relationship between Smirnoff and Smirff?
/****************************************************

****************************************************/
// this is actually for multi groups. not just two groups.


package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.util.*;

import org.jfree.data.statistics.Statistics;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.data.DataCase;

public class KolmogorovSmirnoff implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	private static double alpha = 0.05;

	private static double SEQUENCE_STEPS = 500;

	private String type = "KolmogorovSmirnoff";
	private HashMap resultMap = null;
	private String[] varNames = null;
	private TreeSet varNameList = new TreeSet();

	private ArrayList<Object> varValueList = new ArrayList<Object>();

	private boolean approx = false; // use approximation or exact, see Conover page 231.

	private String[] varList = null;
	private double significanceLevel = 0.05;
	private final static String SIGNIFICANCE_LEVEL = "SIGNIFICANCE_LEVEL";

	private static double[] diff;
	private static double[] absDiff;

	private static String zFormula = null;
	private static boolean useKSProb; // if the two samples don't overlap, then don't compute D

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {

		Result result = null;

		// must check if both x and y have positive numbers because we'll take log



		if (analysisType != AnalysisType.KOLMOGOROV_SMIRNOFF)
			throw new WrongAnalysisException();

		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();
		try {
			significanceLevel = Double.parseDouble((String)data.getParameter(analysisType, SIGNIFICANCE_LEVEL));
		} catch (Exception e) {
		}

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		//////System.out.println("In linear");
		String keys = "";
		double x[] = null;
		double y[] = null;
		Column xColumn = null;

		while (iterator.hasNext()) {

			keys = (String) iterator.next();

			try {
				Class cls = keys.getClass();
				//////System.out.println(cls.getName());
			} catch (Exception e) {}
			xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}

			x = xColumn.getDoubleArray();

			//for (int i = 0; i < x.length; i++) {
			//	//System.out.println("model KS x["+i+"] = "+x[i]);
			//}
		}

		keySet = yMap.keySet();
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				//////System.out.println(cls.getName());
			} catch (Exception e) {
				//////System.out.println(e);
			}

		}
		Column yColumn = (Column) yMap.get(keys);
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
		}

		y = yColumn.getDoubleArray();
		//for (int i = 0; i < y.length; i++) {
		//	//System.out.println("model KS y["+i+"] = "+y[i]);
		//}

		return getKSResult(x, y);
		//return null;
	}

	private KolmogorovSmirnoffResult getKSResult(double[] x1, double[] x2) throws DataIsEmptyException {

		HashMap<String,Object> texture = new HashMap<String,Object>();

		/*
		double[][] varX = new double[sampleSize][numberColumns];

		int dfChiSq = groupNames.length - 1;
		double chiSquarePValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(
				dfChiSq)).getCDF(tStat);
		*/
		int size1 = x1.length;
		QSortAlgorithm quick = new QSortAlgorithm();
		KolmogorovSmirnoffResult result = new KolmogorovSmirnoffResult(texture);

		try {
			quick.sort(x1);
			quick.sort(x2);

			double[] xnew1 = doubleThisArray(x1);
			double[] ystep1 = getStepArray(size1);
			double[] ynew1 = getDoubleStepArray(size1);
			xnew1 = increaseXElement(xnew1);
			ynew1 = increaseYElement(ynew1);

			// Issue here: should take log of base 10 but I only have exp = 2.718 base.
			// so the scales are different. need to change base somehow.

			double[] logX1 = Utility.getLog10Array(xnew1);

			int size2 = x2.length;
			double[] xnew2 = doubleThisArray(x2);
			double[] ystep2 = getStepArray(size2);
			double[] ynew2 = getDoubleStepArray(size2);
			xnew2 = increaseXElement(xnew2);
			ynew2 = increaseYElement(ynew2);

			double[] logX2 = Utility.getLog10Array(xnew2);

			double[] xstep = makeSequence(0.01, 51);


			double max1 = Utility.max(x1);
			double max2 = Utility.max(x2);
			double min1 = Utility.min(x1);
			double min2 = Utility.min(x2);
			double dStat = 0, prob = 0, z = 0;

			// if there's intersection
			if (!(max1<min2 || max2<min1)) {
				useKSProb = true;

				//System.out.println("useKSProb = " + useKSProb);

				dStat = findDStatistic(x1, ystep1, x2, ystep2);
				//System.out.println("dStat = " + dStat );
				z = 0;
				if (size1 == size2) {
					//z=Math.sqrt(m*n/(m+n))*(d+1/m)
					z = Math.sqrt(size1*size2/(size1+size2))*(dStat + 1/size1);
					zFormula = "z = sqrt(m*n/(m+n))*(d+1/m)";

				} else {
					//z=Math.sqrt(m*n/(m+n))*(d+1/(m+n))
					z = Math.sqrt(size1*size2/(size1+size2))*(dStat + 1/(size1 + size2));
					zFormula = "z = sqrt(m*n/(m+n))*(d+1/(m+n))";
				}

				prob = computeProb(size1, size2, dStat);
			} else {
				dStat = -1;
			}

			//System.out.println("useKSProb = " + useKSProb);

			double mean1 = AnalysisUtility.mean(x1);
			double mean2 = AnalysisUtility.mean(x2);

			double sd1 = Math.sqrt(AnalysisUtility.sampleVariance(x1));
			double sd2 = Math.sqrt(AnalysisUtility.sampleVariance(x2));

			//System.out.println("min1 = " + min1 + ", max1 = " + max1);
			//System.out.println("min2 = " + min2 + ", max2 = " + max2);



			//System.out.println("mean1 = " + mean1);
			//System.out.println("mean2 = " + mean2);
			//System.out.println("sd1 = " + sd1);
			//System.out.println("sd2 = " + sd2);

			int df1 = x1.length - 1;
			int df2 = x2.length - 1;

			double tValue1 = Utility.getStudentTCriticalPoint95CI(df1);
			double tValue2 = Utility.getStudentTCriticalPoint95CI(df2);

			// i need t here but where can i find it??
			// i might have to write a look-up table.
			double ci95Upper1 = mean1 + tValue1 * sd1 / Math.sqrt(x1.length);
			double ci95Lower1 = mean1 - tValue1 * sd1 / Math.sqrt(x1.length);

			double ci95Upper2 = mean2 + tValue2 * sd2 / Math.sqrt(x2.length);
			double ci95Lower2 = mean2 - tValue2 * sd2 / Math.sqrt(x2.length);

			double[] ci95X1 = new double[]{ci95Lower1, ci95Upper1};
			double[] ci95X2 = new double[]{ci95Lower2, ci95Upper2};

			//System.out.println("ci95Lower1 = " + ci95Lower1);
			//System.out.println("ci95Upper1 = " + ci95Upper1);
			//System.out.println("ci95Lower2 = " + ci95Lower2);
			//System.out.println("ci95Upper2 = " + ci95Upper2);

			double median1 = Statistics.calculateMedian(Utility.doubleArrayToList(x1));
			double median2 = Statistics.calculateMedian(Utility.doubleArrayToList(x2));

			//System.out.println("median1 = " + median1);
			//System.out.println("median2 = " + median2);


			int halfSize1 = (x1.length%2==0)?((int)(0.5*x1.length)):((int)(0.5*x1.length+1));
			int halfSize2 = (x2.length%2==0)?((int)(0.5*x2.length)):((int)(0.5*x2.length+1));

			//System.out.println("halfSize1 = " + halfSize1);
			//System.out.println("halfSize2 = " + halfSize2);

			double[] firstHalfArray1 = new double[halfSize1];
			double[] secondHalfArray1 = new double[halfSize1];
			double[] firstHalfArray2 = new double[halfSize2];
			double[] secondHalfArray2 = new double[halfSize2];


			// has ArrayOutOfBoundException for size, need to fix it
			for (int i = 0; i < halfSize1; i++) {
				firstHalfArray1[i] = x1[i];
				if ((size1 - halfSize1) == halfSize1) {
					//System.out.println("1 1");
					secondHalfArray1[i] = x1[i + halfSize1];
				} else {
					//System.out.println("1 2");
					secondHalfArray1[i] = x1[i + halfSize1 - 1];
				}
			}
			//System.out.println("halfSize1 = " + halfSize1);
			//System.out.println("size1 - halfSize1 = " + (size1 - halfSize2));


			for (int i = 0; i < halfSize2; i++) {
				firstHalfArray2[i] = x2[i];
				if ((size2 - halfSize2) == halfSize2) {
					//System.out.println("2 1");
					secondHalfArray2[i] = x2[i + halfSize2];
				} else {
					//System.out.println("2 2");
					secondHalfArray2[i] = x2[i + halfSize2 - 1];
				}

				//System.out.println("i = " + i + " secondHalfArray2[" + i + "]=" +  secondHalfArray2[i]);
			}
			//System.out.println("halfSize2 = " + halfSize2);
			//System.out.println("size2 - halfSize2 = " + (size2 - halfSize2));

			double q1x1 = Statistics.calculateMedian(Utility.doubleArrayToList(firstHalfArray1));
			//System.out.println("q1x1 = " + q1x1);
			double q3x1 = Statistics.calculateMedian(Utility.doubleArrayToList(secondHalfArray1));
			//System.out.println("q3x1 = " + q3x1);

			double q1x2 = Statistics.calculateMedian(Utility.doubleArrayToList(firstHalfArray2));
			//System.out.println("q1x2 = " + q1x2);
			double q3x2 = Statistics.calculateMedian(Utility.doubleArrayToList(secondHalfArray2));
			//System.out.println("q3x2 = " + q3x2);

			// someone (future me maybe) should put these in a utility class method.
			String lowerOutlier1 = "";
			String lowerOutlier2 = "";
			String upperOutlier1 = "";
			String upperOutlier2 = "";

			double iqr1 = q3x1 - q1x1;
			double iqr2 = q3x2 - q1x2;

			double lowerOutlierThreshold1 = q1x1 - 1.5 * iqr1;
			double lowerOutlierThreshold2 = q1x1 - 1.5 * iqr1;
			double upperOutlierThreshold1 = q3x2 + 1.5 * iqr2;
			double upperOutlierThreshold2 = q3x2 + 1.5 * iqr2;

			//System.out.println("iqr1 = " + iqr1);
			//System.out.println("iqr2 = " + iqr2);
			//System.out.println("lowerOutlierThreshold1 = " + lowerOutlierThreshold1);
			//System.out.println("upperOutlierThreshold1 = " + upperOutlierThreshold1);
			//System.out.println("lowerOutlierThreshold2 = " + lowerOutlierThreshold2);
			//System.out.println("upperOutlierThreshold2 = " + upperOutlierThreshold2);



			for (int i = 0; i < size1; i++) {
				if (x1[i] < lowerOutlierThreshold1) {
					lowerOutlier1 += x1[i] + "  ";
				}
				if (x1[i] > upperOutlierThreshold1) {
					upperOutlier1 += x1[i] + "  ";
				}
			}
			for (int i = 0; i < size2; i++) {
				if (x2[i] < lowerOutlierThreshold2) {
					lowerOutlier2 += x2[i] + "  ";
				}
				if (x2[i] > upperOutlierThreshold2) {
					upperOutlier2 += x2[i] + "  ";
				}
			}

			if (lowerOutlier1.length() == 0) {
				lowerOutlier1 = "NONE";
			}
			if (lowerOutlier2.length() == 0) {
				lowerOutlier2 = "NONE";
			}
			if (upperOutlier1.length() == 0) {
				upperOutlier1 = "NONE";
			}
			if (upperOutlier2.length() == 0) {
				upperOutlier2 = "NONE";
			}
			//System.out.println("lowerOutlier1 = " + lowerOutlier1);
			//System.out.println("upperOutlier1 = " + upperOutlier1);
			//System.out.println("lowerOutlier2 = " + lowerOutlier2);
			//System.out.println("upperOutlier2 = " + upperOutlier2);


			/*
			double rankMedian1 = ((double)size1 + 1) / 2;
			double rankMedian2 = ((double)size2 + 1) / 2;

			int q1Rank1 =  ((double)Math.floor(rankMedian1) + 1) / 2;
			double q1Rank2 = ((double)Math.floor(rankMedian2) + 1) / 2;


			//System.out.println("rankMedian1 = " + rankMedian1);
			//System.out.println("q1Rank1 = " + q1Rank1);

			//System.out.println("rankMedian2 = " + rankMedian2);
			//System.out.println("q1Rank2 = " + q1Rank2);
			double q1x1 = 0, q1x2 = 0, q3x1 = 0, q3x2 = 0;
			*/

			double[] normalQuantile1 = AnalysisUtility.getNormalQuantile(x1.length - 2);
			double[] logNormalQuantile1 = AnalysisUtility.getNormalQuantile(logX1.length - 2);

			double[] normalQuantile2 =AnalysisUtility.getNormalQuantile(x2.length - 2);
			double[] logNormalQuantile2 = AnalysisUtility.getNormalQuantile(logX2.length - 2);

			texture.put(KolmogorovSmirnoffResult.VAR_1_Y, ynew1);
			texture.put(KolmogorovSmirnoffResult.VAR_2_Y, ynew2);

			texture.put(KolmogorovSmirnoffResult.VAR_1_X, xnew1);
			texture.put(KolmogorovSmirnoffResult.VAR_2_X, xnew2);

			texture.put(KolmogorovSmirnoffResult.VAR_1_LOG_X, logX1);
			texture.put(KolmogorovSmirnoffResult.VAR_2_LOG_X, logX2);

			texture.put(KolmogorovSmirnoffResult.DIFF, diff);
			texture.put(KolmogorovSmirnoffResult.ABS_DIFF, absDiff);


			texture.put(KolmogorovSmirnoffResult.MAX_1, max1);
			texture.put(KolmogorovSmirnoffResult.MAX_2, max2);
			texture.put(KolmogorovSmirnoffResult.MIN_1, min1);
			texture.put(KolmogorovSmirnoffResult.MIN_2, min2);

			texture.put(KolmogorovSmirnoffResult.MEAN_1, mean1);
			texture.put(KolmogorovSmirnoffResult.MEAN_2, mean2);

			texture.put(KolmogorovSmirnoffResult.SD_1, sd1);
			texture.put(KolmogorovSmirnoffResult.SD_2, sd2);

			texture.put(KolmogorovSmirnoffResult.MEDIAN_1, median1);
			texture.put(KolmogorovSmirnoffResult.MEDIAN_2, median2);
			texture.put(KolmogorovSmirnoffResult.FIRST_QUARTILE_1, q1x1);
			texture.put(KolmogorovSmirnoffResult.FIRST_QUARTILE_2, q1x2);
			texture.put(KolmogorovSmirnoffResult.THIRD_QUARTILE_1, q3x1);
			texture.put(KolmogorovSmirnoffResult.THIRD_QUARTILE_2, q3x2);


			texture.put(KolmogorovSmirnoffResult.CI_95_1, ci95X1);
			texture.put(KolmogorovSmirnoffResult.CI_95_2, ci95X2);

			texture.put(KolmogorovSmirnoffResult.BOX_LOWER_OUTLIER_STRING_1, lowerOutlier1);
			texture.put(KolmogorovSmirnoffResult.BOX_LOWER_OUTLIER_STRING_2, lowerOutlier2);
			texture.put(KolmogorovSmirnoffResult.BOX_UPPER_OUTLIER_STRING_1, upperOutlier1);
			texture.put(KolmogorovSmirnoffResult.BOX_UPPER_OUTLIER_STRING_2, upperOutlier2);

			if (useKSProb) {
				// output this to the user so that they don't bother me with formula
				texture.put(KolmogorovSmirnoffResult.D_STAT, dStat);
				texture.put(KolmogorovSmirnoffResult.Z_FORMULA, zFormula);
				texture.put(KolmogorovSmirnoffResult.Z_STAT, z);

				// output this to the user so that they don't bother me with formula
				texture.put(KolmogorovSmirnoffResult.PROB, prob);
				texture.put(KolmogorovSmirnoffResult.ONE_MINUS_PROB, (1 - prob));
			}

			texture.put(KolmogorovSmirnoffResult.NORMAL_SCORE_1, normalQuantile1);
			texture.put(KolmogorovSmirnoffResult.NORMAL_SCORE_2, normalQuantile2);

			texture.put(KolmogorovSmirnoffResult.LOG_NORMAL_SCORE_1, logNormalQuantile1);
			texture.put(KolmogorovSmirnoffResult.LOG_NORMAL_SCORE_2, logNormalQuantile2);

		} catch (Exception e) {
			//System.out.println("KolmogorovSmirnoff after texture Exception e = " + e);
		}

		return result;
	}

	// generate "xnew1" in R code
	private static double[] doubleThisArray(double[] input) {
		int length = input.length;
		double[] output = new double[length * 2];
		for (int i = 0; i < length; i++) {
			output[2 * i] = input[i];
			output[2 * i + 1] = input[i];
		}
		return output;
	}

	// generate "y1" in R code
	private static double[] getStepArray(int length) {
		//System.out.println("getStepArray length = " + length);
		double[] output = new double[length + 1];
		for (int i = 1; i < output.length; i++) { // start from 1 because the 0-th element is always 0 here.
			////System.out.println("output["+i+"] = " + output[i]);
			output[i] = output[i-1] + 1 / (double)length;
		}
		return output;
	}


	// generate "ynew1" in R code
	private static double[] getDoubleStepArray(int length) { // like making double cheese burger
		//System.out.println("getDoubleStepArray length = " + length);
		double[] output = new double[2 * length];
		//System.out.println("getDoubleStepArray output.length = " + output.length);

		for (int i = 1; i < length; i++) { // start from 1 because the 0-th element is always 0 here.
			output[2 * i - 1] = output[2 * i - 2] + 1 / (double)length;
			output[2 * i] = output[2 * i - 1];
		}
		output[output.length - 1] = 1;

		return output;

	}

	// increase a little bit to make plotting look nicer
	private static double[] increaseXElement(double[] input) {
		double[] output = new double[input.length + 1];
		for (int i = 0; i < input.length; i++) {
			output[i] = input[i];
		}
		output[output.length - 1] = output[output.length - 2] + 1;
		return output;
	}
	private static double[] increaseYElement(double[] input) {
		double[] output = new double[input.length + 1];
		for (int i = 0; i < input.length; i++) {
			output[i] = input[i];
		}
		output[output.length - 1] = 1;
		return output;
	}

	// this is my sn function in R code
	private static double getCDFValue(double[] xseries, double x, double[] y) {
		int index = 0;
		int size = xseries.length;
		int currentIndex;
		for (int i = 0; i < size - 1; i++) {

			if (xseries[i] <= x && x < xseries[i+1]) {
				index = i + 1;
				////System.out.println("IN if i = " + i + ", x = " + x + " xseries[i] = " + xseries[i]);
				break;
			}
		}

		if (x >= xseries[size - 1]) {
			index = size;
			////System.out.println("in if getCDFValue  index = " + index);
		}
		double answer = y[index];
		return answer;
	}

	public static double[] makeSequence(double min, double max, double inc) {
		////System.out.println("makeSequence min = " + min + ", max = " + max);
		double amount = (max - min) / inc;
		int step = (int)Math.ceil(amount);
		////System.out.println("makeSequence amount = " + amount);
		////System.out.println("makeSequence inc = " + inc);
		double[] seq = new double[step];
		seq[0] = min;
		////System.out.println("seq[0] = " + seq[0]);
		for (int i = 1; i < seq.length; i++) {
			seq[i] = seq[0] + i * inc;
			////System.out.println("seq[" + i + "] = " + seq[i]);
		}
		return seq;
	}

	public static double[] makeSequence(double min, double max) {
		double inc = (max - min) / ((double) SEQUENCE_STEPS);
		return makeSequence(min, max, inc);
	}
	public static double findDStatistic(double[] x1, double[] ystep1, double[] x2, double[] ystep2) {
		double commonMax = Math.ceil(Math.max(Utility.max(x1), Utility.max(x2)));
		double commonMin = Math.floor(Math.min(Utility.min(x1), Utility.min(x2)));
		//System.out.println("commonMin = " + commonMin);
		//System.out.println("commonMax = " + commonMax);

		double[] seq = makeSequence(commonMin, commonMax);
		int length = seq.length;
		double[] z1 = new double[length];
		double[] z2 = new double[length];
		diff = new double[length];
		absDiff = new double[length];
		for (int i = 0; i < length; i++) {
			z1[i] = getCDFValue(x1, seq[i], ystep1);
			z2[i] = getCDFValue(x2, seq[i], ystep2);

			diff[i] = (z1[i] - z2[i]);
			absDiff[i] = Math.abs(diff[i]);
		}
		return Utility.max(absDiff);
	}


	public static void main(String[] args) {

		// see http://www.physics.csbsju.edu/stats/KS-test.html for test example
		double[] x1 = {0.38, 0.08, 0.10, 0.15, 0.17, 0.34, 0.42, 0.49, 0.24, 0.50, 0.70, 0.94, 0.95, 1.26, 1.37, 1.55, 1.75, 3.20, 6.98, 50.57};
		//double[] x1 = {0.08, 0.10, 0.15, 0.17, 0.24, 0.34, 0.38, 0.42, 0.49, 0.50, 0.70, 0.94, 0.95, 1.26, 1.37, 1.55, 1.75, 3.20, 6.98, 50.57};;
		int size1 = x1.length;
		double[] xnew1 = doubleThisArray(x1);
		//Utility.print(xnew1, "xnew1");
		double[] ystep1 = getStepArray(size1);
		//Utility.print(ystep1, "ystep1");
		double[] ynew1 = getDoubleStepArray(size1);
		//Utility.print(ynew1, "ynew1");
		xnew1 = increaseXElement(xnew1);
		//Utility.print(xnew1, "xnew1");
		ynew1 = increaseYElement(ynew1);
		//Utility.print(ynew1, "ynew1");

		double[] logX1 = Utility.getLog10Array(xnew1);
		//Utility.print(logX1, "logX1");
		double currentX = 0, yValue = 0;

		/*
		currentX = .07;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .08;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .0801;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .1;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .1001;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);


		currentX = .1499;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .15;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .1501;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = 50.5699;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = 50.57;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = 50.5701;
		yValue = getCDFValue(x1, currentX, ystep1);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);
		*/


		/***********************************************************************************/


		//double[] x2 = {2, 3, 5, 6, 7,  7, 8, 9, 9, 9, 8};
		double[] x2 = {0.11, 0.18, 0.23, 0.51, 1.19, 1.30, 1.32, 1.73, 2.06, 2.16, 2.37, 2.91, 4.50, 4.51, 4.66, 14.68, 14.82, 27.44, 39.41, 41.04};

		//double[] x2 = {0.11, 0.18, 0.23, 0.51, 1.19, 1.30, 1.32, 1.73, 2.06, 2.16, 2.37, 2.91, 4.50, 4.51, 4.66, 14.68, 14.82, 27.44, 39.41};

		int size2 = x2.length;
		double[] xnew2 = doubleThisArray(x2);
		//Utility.print(xnew2, "xnew2");
		double[] ystep2 = getStepArray(size2);
		//Utility.print(ystep1, "ystep1");
		double[] ynew2 = getDoubleStepArray(size2);
		//Utility.print(ynew1, "ynew1");
		xnew2 = increaseXElement(xnew2);
		//Utility.print(xnew2, "xnew2");
		ynew2 = increaseYElement(ynew2);
		//Utility.print(ynew2, "ynew2");

		double[] logX2 = Utility.getLog10Array(xnew2);
		//Utility.print(logX2, "logX2");


		/*
		currentX = .109;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .11;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .1101;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .179;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .18;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .1801;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .23;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = .2301;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = 41.03;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = 41.04;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);

		currentX = 41.044;
		yValue = getCDFValue(x2, currentX, ystep2);
		//System.out.println("currentX = " + currentX + " getCDFValue = " + yValue);
		*/
		double[] xstep = makeSequence(0.01, 51);
		double dStat = findDStatistic(x1, ystep1, x2, ystep2);
		//System.out.println("dStat = " + dStat );
		try {
			(new KolmogorovSmirnoff()).getKSResult(x1, x2);
		} catch (Exception e) {
		}

		//double z = dStat*Math.sqrt(x1.length);

		//System.out.println("dStat = " + dStat + " size1 = " + size1 + ", size2 = " + size2);

		double z = 0;

		if (size1 == size2) {
        		//z=Math.sqrt(m*n/(m+n))*(d+1/m)
			z = Math.sqrt(size1*size2/(size1+size2))*(dStat + 1/size1);
		} else {
        		//z=Math.sqrt(m*n/(m+n))*(d+1/(m+n))
        		z = Math.sqrt(size1*size2/(size1+size2))*(dStat + 1/(size1 + size2));
		}
		//System.out.println("z = " + z );

		double pV = findKolmogorovProb(dStat); // only returns probability
		//System.out.println("findKolmogorovProb(dStat) = " + 2 * (1 - pV));

		//System.out.println("**************************************************************");

		//System.out.println("computeProb = " + computeProb(20, 20, 0.45));
	}

	public static double findKolmogorovProb(double z) {
		int maxStep = 1000;
		double p = 0;
		for (int j = 1; j <= maxStep; j++) {
			double onePower = (j%2==0) ? -1 : 1;
			p+= onePower * Math.exp(-2 * j * j * z * z);
		}

		p*=2;
		//System.out.println("findKolmogorovPValue p = " + p);
		return p;
	}


	/************************* Ferguson's code ********************************/
	// see http://www.math.ucla.edu/~tom/distributions/KolSmir2.html
	// for javascript

	private static double lcm(double m, double n) {
		double k = 0;
		double mn = m * n;
		while (m > 0) {
			k = n % m;
			n = m;
			m = k;
		}

		return mn / n;
	}

	private static double smircdf(double d, double m, double n) {
		double v = 0, w = 0;
		double c = m * n * d + .0000001;
		//System.out.println("smircdf c = " + c);
		double[] u = new double[(int)(n+2)];
		u[0]=1;
		int j = 0;
		for (int i = 0; i < n; i++) {
			j = i + 1;
			if (m*j>c) {
				u[i+1]=0;
			} else {
				u[i+1]=1;
			}
		}
		for (int i = 0; i < m; i++) {
			j = i + 1;
			w = j / (j + n);
			if (n*i>c) {
				u[0]=0;
			} else {
				u[0]=w*u[0];
			}
			for(int k=0;k<n;k++) {
				if (Math.abs(n*i-m*k)>c) {
					u[k+1]=0;
				} else {
					u[k+1]=u[k]+u[k+1]*w;
				}
			}
		}
		for (int i = 0; i < u.length; i++) {
			//System.out.println(" u[" + i + "] = " + u[i]);
		}
		return u[(int)(n)]; // I don't know!!
	}

	private static double kolsmir(double z) {
		double ks = 0.5, y = 0;
		int i = 0;
		if (z>.27) {
			ks = 0;
			y = -2 * z * z;
			for (i = 27; i >= 1; i = i - 2) {
				ks = Math.exp((double)i * y) * (1 - ks);
			}
		}
		return 1 - 2 * ks;
	}

	private static double computeProb(int m, int n, double x) { // two sample size and dStat
		double prob = 0, klcm = 0;
		//x=eval(form.argument.value);
    		//m=Math.floor(eval(form.sample1.value));
    		//n=Math.floor(eval(form.sample2.value));
    		if ((n<1)||(m<1)) {
    			//System.out.println("m and n must be positive integers");
    		}
    		klcm = lcm(m,n);
		//System.out.println("klcm = " + klcm);
    		double d=Math.floor(x*klcm)/klcm;
		if (x<=0) {
			//System.out.println("x<=0 prob=0");
			prob=0;
		} else if (x>=1) {
			//System.out.println("x>==0 prob=1");
		 	prob=1;
		} else if (m>n) {
			prob=smircdf(d,n,m);
			//System.out.println(" 3 m>n prob = " + prob);
		} else {
     		prob=smircdf(d,m,n);
			//System.out.println(" 4 prob = " + prob);
		}

		//System.out.println(" round prob = " + prob);
	    	double z = 0;
		//System.out.println("dStat = " + x + " m = " + m + ", n = " + n);

	    	if (m==n) {
        		z=Math.sqrt(m*n/(m+n))*(d+1/m);
	    	} else {
        		z=Math.sqrt(m*n/(m+n))*(d+1/(m+n));
    		}
		//System.out.println(" z = " + z);

    		double appr=kolsmir(z);

		//System.out.println("prob = " + prob);
		//System.out.println("appr = " + appr);
		return prob;
	}

}