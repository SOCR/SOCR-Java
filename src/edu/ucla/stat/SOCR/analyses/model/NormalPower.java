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
/* annieche 200608.
One-sided normal with known variance power computation
*/

package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.io.*;


public class NormalPower implements Analysis {
	public final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	public final static String HYPOTHESIS_TYPE_LT = "HYPOTHESIS_TYPE_LT"; // less then
	public final static String HYPOTHESIS_TYPE_GT = "HYPOTHESIS_TYPE_GT"; // greater then
	public final static String HYPOTHESIS_TYPE_NE = "HYPOTHESIS_TYPE_NE"; // not equal.
	private static double INCREMENT=.0045; // using this will generate approximate power .05 to 1.
	private static double INCREMENT_POWER = .0045; // using this will generate approximate
	private static int SAMPLE_SIZE_INCREMENT = 10;
	private static double DEFAULT_NORMAL_MAX = 3.5;
	private static double DEFAULT_NORMAL_MIN = -3.5;
	private static double DEFAULT_NORMAL_MEDIUM = 0;
	private static double DEFAULT_PLOT_POWER_MAX = 1.0;

	private String type = "NormalPower";
	private HashMap<String,Object> resultMap = null;
	private static String dataOutputFileName = "C:\\STAT\\SOCR\\test\\normalpowerdata.R";
	private String plotDescription = "";

	public int numberPointsHalf = 200; // default if it is not set by caller.
	public int numberPoints = 2 * numberPointsHalf;

	public int numberOfCurves = 5; // for plotting.

	public int sampleSize = 0;
	public double sigma = 0;
	public double alpha = 0;
	public double power = 0;
	public double z = 0;
	public double pv = 0; // p-value
	public String hypothesisType = null;

	public double beta = 0; // type II error.
	public double mu0 = 0;
	public double muA = 0;

	public double[] meanPlotPoints;// = new double[numberPoints];
	public double[] powerPlotPoints;// = new double[numberPoints];
	public double[][] multipleMeanPlotPoints;// = new double[numberOfCurves][numberPoints];
	public double[][] multiplePowerPlotPoints;// = new double[numberOfCurves][numberPoints];

	// default when random data avaiable
	public NormalPower() {
	}


	// find z score and/or critical value
	// mu0: mean of the null, sigma: SD of the null, x: any point of which you'd like to find z-value.
	public NormalPower(double mu0, double x, double sigma) {
		this.sigma = sigma;
		this.mu0 = mu0;
		if (x == mu0) {
			z = 0;
		}
		else {
			z = (x - mu0) / sigma;
		}
		NormalDistribution normal = new NormalDistribution(0, 1);
		/*if (z >= 0) {
			////System.out.println("normal z = " + z);
			pv = 1 - normal.getCDF(z);
			//pv = normal.getCDF(z);
			////System.out.println("normal z = " + z);

		} else {
			pv = 1 - normal.getCDF(z);
		}*/

		pv = 1 - normal.getCDF(z);
		////System.out.println("normal z = " + z + ", pv = "+ pv);
		////////////////////System.out.println("NormalPower constructor mu0 = " + mu0 + ", sigma = " + sigma + ", x = " + x + ", z = " + z + ", pv = " + pv);
	}


	// if sample size is known and look for power.

	public NormalPower(int sampleSize, double sigma, double alpha, double mu0, double muA, String hypothesisType) {
		this.sampleSize = sampleSize;
		this.sigma = sigma;
		this.alpha = alpha;
		this.mu0 = mu0;
		this.muA = muA;
		this.hypothesisType = hypothesisType;

		if (this.hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_LT) || this.hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_GT) ) {
			this.meanPlotPoints = new double[numberPointsHalf];
			this.powerPlotPoints = new double[numberPointsHalf];
		}
		else {
			this.meanPlotPoints = new double[numberPoints];
			this.powerPlotPoints = new double[numberPoints];
		}

	}

	// if power is known (preset) and look for sample size.
	public NormalPower(double power, double sigma, double alpha, double mu0,  double muA, String hypothesisType) {
		this.beta = 1 - power;
		this.sigma = sigma;
		this.power = power;
		this.alpha = alpha;
		this.mu0 = mu0;
		this.muA = muA;
		this.hypothesisType = hypothesisType;

		double zAlpha = Math.abs(getCriticalPoint(alpha));
		double zBeta = Math.abs(getCriticalPoint(this.beta));;
		double nume = (zAlpha + zBeta) * sigma;
		nume = nume * nume;
		double deno = muA - mu0;
		deno = deno * deno;
		this.sampleSize = (int) Math.ceil(nume / deno);
		this.meanPlotPoints = new double[numberOfCurves * numberPoints];
		this.powerPlotPoints = new double[numberOfCurves * numberPoints];
		this.multipleMeanPlotPoints = new double[numberOfCurves][numberPoints];
		this.multiplePowerPlotPoints = new double[numberOfCurves][numberPoints];


		////////////////////System.out.println("NormalPower constructor power = " + power + ", sampleSize = " + sampleSize);
	}

	public String getAnalysisType() {
		//////////////////////System.out.println("NormalPower getAnalysisType = " + type);
		return type;
	}

	public Result getNormalAnalysis() {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		NormalPowerResult result = new NormalPowerResult(texture);
		texture.put(NormalPowerResult.P_VALUE, new Double(pv));
		texture.put(NormalPowerResult.Z_SCORE, new Double(z));
		return result;

	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		////////////////////////System.out.println("Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.NORMAL_POWER)
			throw new WrongAnalysisException();

		//HashMap xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (yMap == null)
			throw new WrongAnalysisException();
		//Column[] xColumn = new Column();
		Set<String> keySet = yMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		//Column xColumn[] = new Column[];
		////////////////////////System.out.println("In linear iterator.hasNext() = " + iterator.hasNext());
		String keys = "";
		//double y[] = null;
		double y[] = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			////////////////////////System.out.println("Analysis while keys = " + keys);

			try {
				Class cls = keys.getClass();
				////////////////////////System.out.println(cls.getName());
			} catch (Exception e) {}
			Column yColumn = (Column) yMap.get(keys);
			////////////////////////System.out.println(yColumn.getDataType());
			String yDataType = yColumn.getDataType();

			if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			////////////////////////System.out.println("Analysis Type yxColumn = " + yColumn);

			y = yColumn.getDoubleArray();
			////////////////////////System.out.println("Analysis Type y = " + y);

			for (int i = 0; i < y.length; i++) {
				////////////////////////System.out.println(y[i]);
			}
		}
		return regression(y);
	}

	private NormalPowerResult regression(double[] y) throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		NormalPowerResult result = new NormalPowerResult(texture);

		// do confidence interval for sample mean.

		int sampleSize = y.length;
//		throw new DataIsEmptyException();

		double meanX = AnalysisUtility.mean(y);
		////////////////////////System.out.println("X mean = " + meanX);
		double sXX=  AnalysisUtility.sampleVariance(y);
		////////////////////////System.out.println("S y = " + sXX);

		int df = sampleSize - 1; // only one sample mean to be estimated

		//		double tScore = ;
		//////////////////////System.out.println("NormalPowerResult meanX = " + meanX);
		//////////////////////System.out.println("NormalPowerResult sXX = " + sXX);
		//////////////////////System.out.println("NormalPowerResult df = " + df);
		texture.put(NormalPowerResult.SAMPLE_MEAN, new Double(meanX));
		texture.put(NormalPowerResult.SAMPLE_VAR, new Double(sXX));
		texture.put(NormalPowerResult.SAMPLE_SIZE, new Integer(sampleSize));
		return result;

	}

	// sigma = standard deviation, a = alpha = significant level, sampleSize = sample size.
	// H_0: mu = mu0
	// H_A: mu > mu0, muA: mean of H_A.
	public double getPowerOneSidedGT() {

		NormalDistribution nDistribution = new NormalDistribution();
		double z = getCriticalPoint(alpha);
		double deno = (sigma / Math.sqrt(sampleSize));
		double nume = (mu0 + z * deno - muA);
		double zScore = nume / deno;
		double[] zScorePlot = new double[numberPointsHalf];
		double increment = 0; // like a multiple/incremen
		double maxCP = getEnhancePlotParameter(sampleSize);;


		double powerMax = DEFAULT_PLOT_POWER_MAX;
		//this.power = 1 - nDistribution.getCDF(zScore);
		double zScoreOfEndCDF = DEFAULT_NORMAL_MAX;


		maxCP += z;
		double delta = (maxCP - z) / Math.sqrt(sampleSize);
		double muStart = mu0 + delta * sigma;
		double muMiddle = 0;
		// change muAEnd (the end point of plot) to make the plot looks better.
		// for GT, LT and NE cases. annie chu 2007018.
		double muAEnd = mu0 + z * deno + zScoreOfEndCDF * deno;

		//double muAEnd = mu0 + delta * numberPointsHalf * Math.sqrt(sampleSize);
		INCREMENT = (muAEnd - muStart) / numberPointsHalf;
		//////////////System.out.println("NormalPower call method maxCP = " + maxCP);
		//System.out.println("NormalPower call method z = " + z + ", deno = " + deno);
		//System.out.println("NormalPower call method delta = " + delta);
		//System.out.println("NormalPower call method sigma = " + sigma);

		//System.out.println("NormalPower call method muStart = " + muStart);
		//System.out.println("NormalPower call method muAEnd = " + muAEnd);
		//System.out.println("ONE SIDE GT **************NormalPower call method INCREMENT = " + INCREMENT);

		for (int i = 0; i < numberPointsHalf; i++) {
			this.meanPlotPoints[i] = mu0 + i * INCREMENT;
			nume = (mu0 + z * deno - this.meanPlotPoints[i]);
			zScorePlot[i] = nume / deno;//-(zScore + increment * Math.sqrt(sampleSize)); // - - this works.
			// opposite sign of LT.
			//this.meanPlotPoints[i] = mu0 + increment * deno;
			this.powerPlotPoints[i] = 1 - nDistribution.getCDF(zScorePlot[i]);
			// this works.
			//System.out.println(INCREMENT + ", " + zScorePlot[i] + ", " + meanPlotPoints[i] + " " + powerPlotPoints[i]);
			//increment += INCREMENT;
		}

		this.power = 1 - nDistribution.getCDF(zScore);
		if (mu0 < Double.POSITIVE_INFINITY || muA < Double.POSITIVE_INFINITY)
			this.plotDescription = "mu0 = " + mu0 + ", muA = " + muA;

		return this.power;
	}
	public double getPowerOneSidedLT() {
		////////////////////System.out.println("NormalPower call method getPowerOneSidedLT mu0 = " + mu0 + ", muA = " + muA);
		NormalDistribution nDistribution = new NormalDistribution();
		double z = getCriticalPoint(alpha);
		double deno = (sigma / Math.sqrt(sampleSize));
		double nume = (mu0 - z * deno - muA);
		double zScore = nume / deno;
		double[] zScorePlot = new double[numberPointsHalf];
		double maxCP = getEnhancePlotParameter(sampleSize);
		double delta = (maxCP - z) / Math.sqrt(sampleSize);

		double powerMin = DEFAULT_PLOT_POWER_MAX;
		//this.power = 1 - nDistribution.getCDF(zScore);
		double zScoreOfEndCDF = DEFAULT_NORMAL_MIN;
		double muAEnd = mu0 - z * deno + zScoreOfEndCDF * deno;


		//double muAEnd = mu0 - delta * numberPointsHalf * Math.sqrt(sampleSize);
		double muStart = mu0 - delta * sigma;
		INCREMENT = Math.abs(muAEnd - muStart) / numberPointsHalf;
		//////////////System.out.println("NormalPower call method z = " + z + ", deno = " + deno);
		//////////////System.out.println("NormalPower call method muAEnd = " + muAEnd);
		//////////////System.out.println("NormalPower call method muStart = " + muStart);
		////System.out.println("ONE SIDE LT *************** NormalPower call method INCREMENT = " + INCREMENT);
		for (int i = 0; i < numberPointsHalf; i++) {
			this.meanPlotPoints[i] = mu0 - i * INCREMENT;
			nume = (mu0 - z * deno - this.meanPlotPoints[i]);
			zScorePlot[i] = -nume / deno;//-(zScore + increment * Math.sqrt(sampleSize)); // - - this works.
			// opposite sign of LT.
			//this.meanPlotPoints[i] = mu0 + increment * deno;
			this.powerPlotPoints[i] = 1 - nDistribution.getCDF(zScorePlot[i]);
			// this works.
			//////////////System.out.println(INCREMENT + ", " + zScorePlot[i] + ", " + meanPlotPoints[i] + " " + powerPlotPoints[i]);
			//increment += INCREMENT;
		}

		this.power = nDistribution.getCDF(zScore);
		if (mu0 < Double.POSITIVE_INFINITY || muA < Double.POSITIVE_INFINITY)
			this.plotDescription = "mu0 = " + mu0 + ", muA = " + muA;

		return this.power;
	}
	public double getPowerTwoSidedNE() {
		////////////////System.out.println("NormalPower call method getPowerTwoSidedNE mu0 = " + mu0 + ", muA = " + muA  + ", numberPoints = " + numberPoints);
		////////////////System.out.println("NormalPower call method getPowerTwoSidedNE meanPlotPoints.length = " + meanPlotPoints.length + ", powerPlotPoints.length = " + powerPlotPoints.length);


		NormalDistribution nDistribution = new NormalDistribution();
		double z = getCriticalPoint(alpha);
		double deno = 0, nume = 0, zScorePlus = 0, zScoreMinus = 0;
		INCREMENT = 0.009;
		double rescaleFactor = sigma / Math.abs(mu0 - muA) / Math.sqrt(sampleSize);
		deno = (sigma / Math.sqrt(sampleSize));
		nume = (mu0 + z * deno - muA); // + then -
		zScorePlus = nume / deno;
		nume = (mu0 - z * deno - muA); // - then -
		zScoreMinus = nume / deno;
		double zScore = nume / deno;
		double[] zScorePlot = new double[numberPoints];


		double increment = 0; // like a multiple/incremen
		double maxCP = getEnhancePlotParameter(sampleSize);;
		maxCP += z;
		double delta = (maxCP - z) / Math.sqrt(sampleSize);
		double muStart = mu0 + delta * sigma;
		double muMiddle = 0;

		double powerMax = DEFAULT_PLOT_POWER_MAX;
		//this.power = 1 - nDistribution.getCDF(zScore);
		double zScoreOfEndCDF = DEFAULT_NORMAL_MAX;

		double muAEnd = mu0 + z * deno + zScoreOfEndCDF * deno;

		//double muAEnd = mu0 + delta * numberPointsHalf * Math.sqrt(sampleSize);
		//double muAEnd = muStart + 2 * (muMiddle - muStart);
		INCREMENT = (muAEnd - muStart) / numberPointsHalf;


		if (muA > mu0) {
			zScore = zScorePlus;
		}
		else if (muA < mu0){
			zScore = zScoreMinus;

		}
		////System.out.println("TWO SIDE *************** NormalPower call method INCREMENT = " +INCREMENT);
		for (int i = 0; i < .5 * numberPoints; i++) {
			this.meanPlotPoints[i] = mu0 + i * INCREMENT;
			nume = (mu0 + z * deno - this.meanPlotPoints[i]);
			zScorePlot[i] = nume / deno;//-(zScore + increment * Math.sqrt(sampleSize)); // - - this works.
			// opposite sign of LT.
			//this.meanPlotPoints[i] = mu0 + increment * deno;
			this.powerPlotPoints[i] = 1 - nDistribution.getCDF(zScorePlot[i]);
			// this works.
			//////////////System.out.println(INCREMENT + ", " + zScorePlot[i] + ", " + meanPlotPoints[i] + " " + powerPlotPoints[i]);
			//increment += INCREMENT;
		}

		// the left half (muA less than mu0)
		maxCP = getEnhancePlotParameter(sampleSize);;
		maxCP += z;
		delta = (maxCP - z) / Math.sqrt(sampleSize);
		muStart = mu0 - delta * sigma;
		muMiddle = 0;
		powerMax = DEFAULT_PLOT_POWER_MAX;
		//this.power = 1 - nDistribution.getCDF(zScore);
		zScoreOfEndCDF = DEFAULT_NORMAL_MIN;

		muAEnd = mu0 - z * deno + zScoreOfEndCDF * deno;
		//muAEnd = mu0 - delta * numberPointsHalf * Math.sqrt(sampleSize);
		INCREMENT = Math.abs(muAEnd - muStart) / numberPointsHalf;
		//////////////System.out.println("NormalPower call method numberPointsHalf = " + numberPointsHalf);
		//////////////System.out.println("NormalPower call method delta = " + delta);
		//////////////System.out.println("NormalPower call method muStart = " + muStart);
		//////////////System.out.println("NormalPower call method muAEnd = " + muAEnd);
		//////////////System.out.println("NormalPower call method INCREMENT = " + INCREMENT);
		for (int i = (int) (.5 * numberPoints); i < numberPoints; i++) {
			this.meanPlotPoints[i] = mu0 - (i - (int) (.5 * numberPoints)) * INCREMENT;
			nume = (mu0 - z * deno - this.meanPlotPoints[i]);
			zScorePlot[i] = -nume / deno;//-(zScore + increment * Math.sqrt(sampleSize)); // - - this works.
			// opposite sign of LT.
			//this.meanPlotPoints[i] = mu0 + increment * deno;
			this.powerPlotPoints[i] = 1 - nDistribution.getCDF(zScorePlot[i]);
			// this works.
			//////////////System.out.println(INCREMENT + ", " + zScorePlot[i] + ", " + meanPlotPoints[i] + " " + powerPlotPoints[i]);
			//increment += INCREMENT;
		}

		//this.powerPlotPoints[(int) (.5 * numberPoints)] = this.powerPlotPoints[1];
		////////////////System.out.println("model.NormalPower deno = " + deno + ", " + nume + " " + nume + ", zScorePlus = " + zScorePlus + ", zScoreMinus = " + zScoreMinus);

		increment = 0;


		if (muA > mu0) {
			this.power = (1 - nDistribution.getCDF(zScorePlus)) + (nDistribution.getCDF(zScoreMinus));
		}
		else {
			this.power = (nDistribution.getCDF(zScoreMinus)) + (1 - nDistribution.getCDF(zScorePlus));
		}
		if (mu0 < Double.POSITIVE_INFINITY || muA < Double.POSITIVE_INFINITY)
			this.plotDescription = "mu0 = " + mu0 + ", muA = " + muA;

		return this.power;

	}

	private static double getCriticalPoint(double alpha) { // unsigned, all positive.
		// alpha = significant level
		if (alpha == 1) {
			return Double.POSITIVE_INFINITY;
		}
		else if (alpha == 0) {
			return Double.NEGATIVE_INFINITY;
		}
		double cp = 0;
		if (alpha == .1) {
			cp = 1.281552; // qnorm(.9)
		}
		else if (alpha == .01) {
			cp = 2.326348; // qnorm(.999)
		}
		else if (alpha == .001) {
			cp = 3.090232; // qnorm(.999)
		}
		else if (alpha == .05) {
			cp = 1.644854; // qnorm(.95)
		}
		else {
			double ci = 1 - alpha;
			int lookUpIndex = (int) (100 * ci) - 1;
			return criticalPoints[lookUpIndex];
		}
		return cp;
	}

	public double[] getMeanPlotPoints() {
		for (int i = 0; i < this.meanPlotPoints.length; i++) {
			////////////////////System.out.println("model method meanPlotPoints["+i+"] = " + this.meanPlotPoints[i]);

		}
		return this.meanPlotPoints;
	}
	public double[] getPowerPlotPoints() {
		//////////////////////System.out.println("getPowerPlotPoints length = " + this.powerPlotPoints);
		for (int i = 0; i < this.powerPlotPoints.length; i++) {
			////////////////////System.out.println("model method powerPlotPoints["+i+"] = " + this.powerPlotPoints[i]);

		}
		return this.powerPlotPoints;
	}
	public double[][] getMultipleMeanPlotPoints() {
		return this.multipleMeanPlotPoints;
	}
	public double[][] getMultiplePowerPlotPoints() {
		return this.multiplePowerPlotPoints;
	}

	// use power to get sample size
	public NormalPowerResult getSampleSizeResult() { //double power, double sigma, double alpha, double mu0 , double muA, String hypothesisType) {

		HashMap<String,Object> texture = new HashMap<String,Object>();
		NormalPowerResult result = new NormalPowerResult(texture);
		NormalPower normal = new NormalPower(this.power, this.sigma, this.alpha, this.mu0 , this.muA, hypothesisType);
		////////////System.out.println("NormalPowerResult getSampleSizeResult this.sampleSize = " + this.sampleSize);
		////////////System.out.println("NormalPowerResult getSampleSizeResult hypothesisType = " + this.hypothesisType);
		////////////System.out.println("NormalPowerResult getSampleSizeResult mu0  = " + mu0 + ", muA = " + muA);

		NormalDistribution nDistribution = new NormalDistribution();
		double z = getCriticalPoint(alpha);
		double deno = 0, nume = 0, zScorePlus = 0, zScoreMinus = 0;
		int tempSampleSize = this.sampleSize;
		this.multipleMeanPlotPoints = new double[numberOfCurves][numberPoints];
		this.multiplePowerPlotPoints = new double[numberOfCurves][numberPoints];
		// c is the index of numberOfCurves
		//numberOfCurves = 5;
		double oldDeno = 0;

		if (hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_LT) && muA < mu0) {
			for (int c = 0; c < numberOfCurves; c++) {
				////////////System.out.println("NormalPowerResult tempSampleSize = " + tempSampleSize);
				oldDeno = (sigma / Math.sqrt(this.sampleSize));
				deno = (sigma / Math.sqrt(tempSampleSize));
				nume = (mu0 + z * deno - muA);
				zScorePlus = nume / deno;
				nume = (mu0 - z * deno - muA);
				zScoreMinus = nume / deno;
				double zScore = nume / deno;
				double[] zScorePlot = new double[numberPoints];
				double increment = 0; // like a multiple/increment
				for (int i = 0; i < numberPoints; i++) {
					zScorePlot[i] = -z + increment * Math.sqrt(tempSampleSize);
					this.multipleMeanPlotPoints[c][i] = mu0 - increment * oldDeno;
					this.multiplePowerPlotPoints[c][i] = nDistribution.getCDF(zScorePlot[i]);
					increment +=INCREMENT_POWER;
					//////////System.out.println("zScorePlot = " + zScorePlot[i] + ", multipleMeanPlotPoints = " + this.multipleMeanPlotPoints[c][i] + ", multipleMeanPlotPoints " + this.multiplePowerPlotPoints[c][i]);
				}
				tempSampleSize = tempSampleSize + SAMPLE_SIZE_INCREMENT; // SAMPLE SIZE FOR PLOT: increase 10 each time.
			}
			this.plotDescription = "Sample Size = " + sampleSize +" to " + (sampleSize + (numberOfCurves-1) * SAMPLE_SIZE_INCREMENT) + ", increasing by " + (SAMPLE_SIZE_INCREMENT)+ ".";

		} else if (hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_GT) && muA > mu0){

			tempSampleSize = this.sampleSize;
			for (int c = 0; c < numberOfCurves; c++) {
				////////////System.out.println("NormalPowerResult tempSampleSize = " + tempSampleSize);
				oldDeno = (sigma / Math.sqrt(this.sampleSize));
				deno = (sigma / Math.sqrt(tempSampleSize));
				nume = (mu0 + z * deno - muA);
				zScorePlus = nume / deno;
				nume = (mu0 - z * deno - muA);
				zScoreMinus = nume / deno;
				double zScore = nume / deno;
				double[] zScorePlot = new double[numberPoints];
				double increment = 0; // like a multiple/increment

				for (int i = 0; i < numberPoints; i++) {
					//zScorePlot[i] = -z + increment * Math.sqrt(tempSampleSize);
					zScorePlot[i] = -(zScore + increment * Math.sqrt(tempSampleSize)); // - - this works.
					this.multipleMeanPlotPoints[c][i] = mu0 - increment * oldDeno;
					this.multiplePowerPlotPoints[c][i] = nDistribution.getCDF(zScorePlot[i]);
					//////////System.out.println("zScorePlot = " + zScorePlot[i] + ", multipleMeanPlotPoints = " + this.multipleMeanPlotPoints[c][i] + ", multipleMeanPlotPoints " + this.multiplePowerPlotPoints[c][i]);
					increment +=INCREMENT_POWER;
				}
				tempSampleSize = tempSampleSize + SAMPLE_SIZE_INCREMENT; // SAMPLE SIZE FOR
				//////////System.out.println(" tempSampleSize = " + tempSampleSize);
				// SAMPLE SIZE FOR PLOT: increase 10 each time.
			}
			this.plotDescription = "Sample Size = " + sampleSize +" to " + (sampleSize + (numberOfCurves-1) * SAMPLE_SIZE_INCREMENT) +  ", increasing by " + (SAMPLE_SIZE_INCREMENT)+ ".";
		} else {
			tempSampleSize = this.sampleSize;
			////////////System.out.println("NormalPowerResult else numberPoints = " + numberPoints);

			////////////System.out.println("NormalPowerResult else numberOfCurves = " + numberOfCurves);
			for (int c = 0; c < numberOfCurves; c++) {
				//////////////////System.out.println("NormalPowerResult else tempSampleSize = " + tempSampleSize);
				oldDeno = (sigma / Math.sqrt(this.sampleSize));
				deno = (sigma / Math.sqrt(tempSampleSize));
				nume = (mu0 + z * deno - muA);
				zScorePlus = nume / deno;
				nume = (mu0 - z * deno - muA);
				zScoreMinus = nume / deno;
				double zScore = nume / deno;
				double[] zScorePlot = new double[numberPoints];
				double increment = 0; // like a multiple/increment
				if (muA > mu0) {
					zScore = zScorePlus;
				}
				else if (muA < mu0){
					zScore = zScoreMinus;

				}
				// muA LT mu0

				for (int i = 0; i < .5 * numberPoints; i++) {
					if (muA > mu0) {
						zScorePlot[i] = +(zScorePlus + increment * Math.sqrt(tempSampleSize)); // - +
					}
					else if (muA < mu0){
						zScorePlot[i] = -(zScoreMinus - increment * Math.sqrt(tempSampleSize)); // - +
					}
					this.multipleMeanPlotPoints[c][i] = mu0 - increment * deno;
					this.multiplePowerPlotPoints[c][i] = nDistribution.getCDF(zScorePlot[i]);
					////////System.out.println("zScorePlot = " + zScorePlot[i] + ", multipleMeanPlotPoints = " + this.multipleMeanPlotPoints[c][i] + ", multipleMeanPlotPoints " + this.multiplePowerPlotPoints[c][i]);
					increment +=INCREMENT_POWER;
				}
				increment = 0;
				//////////System.out.println("\n===============================================\n");
				// muA GT mu0
				for (int i = (int) (.5 * numberPoints); i < numberPoints; i++) {
					if (muA > mu0) {
						zScorePlot[i] = -(zScorePlus + increment * Math.sqrt(tempSampleSize));
					}
					else if (muA < mu0){
						zScorePlot[i] = +(zScoreMinus - increment * Math.sqrt(tempSampleSize)); // - +
					}
					this.multipleMeanPlotPoints[c][i] = mu0 + increment * deno;
					this.multiplePowerPlotPoints[c][i] = 1- nDistribution.getCDF(zScorePlot[i]);
					//////////System.out.println("zScorePlot = " + zScorePlot[i] + ", multipleMeanPlotPoints = " + this.multipleMeanPlotPoints[c][i] + ", multipleMeanPlotPoints " + this.multiplePowerPlotPoints[c][i]);
					increment +=INCREMENT_POWER;
				}
				tempSampleSize = tempSampleSize + SAMPLE_SIZE_INCREMENT; // SAMPLE SIZE FOR PLOT: increase 10 each time.

				//////System.out.println("tempSampleSize = " + tempSampleSize);
			}
			this.plotDescription = "Sample Size = " + sampleSize +" to " + (sampleSize + (numberOfCurves-1) * SAMPLE_SIZE_INCREMENT) +  ", increasing by " + (SAMPLE_SIZE_INCREMENT) + ".";
		}



		int j = 0;
		for (int c = 0; c < numberOfCurves; c++) {

			for (int i = 0; i < numberPoints; i++) {
				try {

					this.meanPlotPoints[j] = multipleMeanPlotPoints[c][i];
					this.powerPlotPoints[j] = multiplePowerPlotPoints[c][i];
					if (i != numberPoints - 1) {
						j++;
					}

				} catch (Exception e) {
					//////////System.out.println("NormalPower for Exception j = " + j);
					continue;
				}
				//if (!(c == numberOfCurves - 1 && i == numberPoints - 1)) {
					//j++;
				//}
			}
			//if (!(c == numberOfCurves - 1 && i == numberPoints - 1)) {
			j++;
			//}

		}


		////////////System.out.println("model.NormalPower plotDescription = " + this.plotDescription);

		texture.put(NormalPowerResult.POWER_PLOT_POINTS, this.getPowerPlotPoints());
		texture.put(NormalPowerResult.MEAN_PLOT_POINTS, this.getMeanPlotPoints());
		texture.put(NormalPowerResult.MULTIPLE_POWER_PLOT_POINTS, this.multiplePowerPlotPoints);
		texture.put(NormalPowerResult.MULTIPLE_MEAN_PLOT_POINTS, this.multipleMeanPlotPoints);
		texture.put(NormalPowerResult.SAMPLE_SIZE, new Integer(this.sampleSize));
		texture.put(NormalPowerResult.PLOT_DESCRIPTION, this.plotDescription);

		////////////System.out.println("NormalPowerResult this.sampleSize = " + this.sampleSize);

		texture.put(NormalPowerResult.HYPOTHESIS_TYPE, this.hypothesisType);

		return result;
	}

	public double getEnhancePlotParameter(int sampleSize) {
		double maxCP = 0;
		if (sampleSize >= 400) maxCP = .08;
		else if (sampleSize >= 200) maxCP = .1;
		else if (sampleSize >= 100) maxCP = .15;
		else if (sampleSize > 75) maxCP = .2;

		else if (sampleSize >= 60) maxCP = .3;
		else if (sampleSize >= 50) maxCP = .5;
		else if (sampleSize >= 20) maxCP = 1;
		else if (sampleSize >= 10) maxCP = 1.1;
		else if (sampleSize >= 5) maxCP = 1.2;
		else if (sampleSize >= 3) maxCP = 2;
		else maxCP = 2.5;
		return maxCP;
	}

	// getPowerResult: given sample size, get power
	public NormalPowerResult getPowerResult() {

		HashMap<String,Object> texture = new HashMap<String,Object>();
		NormalPowerResult result = new NormalPowerResult(texture);
		//////////////////System.out.println("NormalPowerResult getPowerResult hypothesisType = " + hypothesisType);


		//NormalPower normal = new NormalPower(this.sampleSize, this.sigma, this.alpha, this.mu0 , this.muA, this.hypothesisType);
		if (this.hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_LT) && this.muA < this.mu0) {
			//////////////////System.out.println("NormalPower use getPowerOneSidedLT");
			NormalDistribution nDistribution = new NormalDistribution();
			this.power = this.getPowerOneSidedLT();

		}
		else if (this.hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_GT) && this.mu0 < this.muA){
			//////////////////System.out.println("NormalPower use getPowerOneSidedGT");
			NormalDistribution nDistribution = new NormalDistribution();
			this.power = this.getPowerOneSidedGT();

		}
		else {
			//////////////////System.out.println("NormalPower use getPowerTwoSidedNE");
			NormalDistribution nDistribution = new NormalDistribution();
			this.power = this.getPowerTwoSidedNE();

		}
		NormalDistribution nDistribution = new NormalDistribution();

		texture.put(NormalPowerResult.POWER_PLOT_POINTS, this.getPowerPlotPoints());
		texture.put(NormalPowerResult.MEAN_PLOT_POINTS, this.getMeanPlotPoints());
		texture.put(NormalPowerResult.MULTIPLE_POWER_PLOT_POINTS, this.multiplePowerPlotPoints);
		texture.put(NormalPowerResult.MULTIPLE_MEAN_PLOT_POINTS, this.multipleMeanPlotPoints);
		texture.put(NormalPowerResult.POWER, new Double(this.power));
		texture.put(NormalPowerResult.HYPOTHESIS_TYPE, this.hypothesisType);
		texture.put(NormalPowerResult.PLOT_DESCRIPTION, this.plotDescription);

		return result;
	}

	public void setNumberOfCurves(int input) {
		this.numberOfCurves = input;
	}
/*

	public static void main(String args[]) {
		double mu0 = 25000;
		double muA = 23500;
		int sampleSize = 100;
		double s = 3500;
		double alpha = 0.05;
		double power = 0;
		// test
		String hypothesisType = HYPOTHESIS_TYPE_NE; // or GT, or NE
		File outputFile = new File(dataOutputFileName);
		NormalPower test = null; //new NormalPower(sampleSize, s, alpha, mu0 , muA, );
		if (hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_LT) && muA < mu0) {
			//////////////////System.out.println("Main Use LT");
			test = new NormalPower(sampleSize, s, alpha, mu0 , muA, HYPOTHESIS_TYPE_LT);
			power = test.getPowerOneSidedLT();
		}
		else if (hypothesisType.equalsIgnoreCase(HYPOTHESIS_TYPE_GT) && muA > mu0){
			//////////////////System.out.println("Main Use GT");
			test = new NormalPower(sampleSize, s, alpha, mu0 , muA, HYPOTHESIS_TYPE_GT);
			power = test.getPowerOneSidedGT();

		}
		else {
			//////////////////System.out.println("Main Use NE");
			test = new NormalPower(sampleSize, s, alpha, mu0 , muA, HYPOTHESIS_TYPE_NE);
			power = test.getPowerTwoSidedNE();

		}
		////////////////////System.out.println("Main power = " + power);
		double[] meanPoints = test.getMeanPlotPoints();

		double[] powerPoints = test.getPowerPlotPoints();
		double x = .1;

		try {
			//Writer out = new BufferedWriter(new OutputStreamWriter(outputFile));
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(dataOutputFileName)));

			out.write("mu power \n");
			for (int i = 0; i < test.numberPoints; i++) {
				out.write(meanPoints[i] + " "  + powerPoints[i] + "\n");
			}

			out.flush();
		} catch (IOException e) {
			//////////////////////System.out.println(e);
		}
	}
*/
	//Test: to get sample size with given power.

	public static void main(String args[]) {
		double mu0 = 1;//250;
		double muA = 2;//265;
		int sampleSize = 100;
		double sigma = 1;//35;
		double alpha = 0.05;
		double power = 0.95;
		String hypothesisType = HYPOTHESIS_TYPE_NE;
		double[] meanPlotPoints = null;
		double[] powerPlotPoints = null;
		NormalPowerResult result = null;
		NormalPower normal = new NormalPower(sampleSize, sigma, alpha, mu0 , muA, hypothesisType);
		File outputFile = new File(dataOutputFileName);
		try {
			result = (NormalPowerResult) normal.getPowerResult();
			//result = (NormalPowerResult) normal.getSampleSizeResult();
			try {
				meanPlotPoints = result.getMeanPlotPoints();
				//resultPanelTextArea.append("\nmeanPlotPoints = "+meanPlotPoints[0] );

			} catch (NullPointerException e) {
				//////////////////////////System.out.println("NullPointerException meanPlotPoints = " + e);

			} catch (Exception e) {
				//////////////////////////System.out.println("Exception = " + e);
			}

			try {
				powerPlotPoints = result.getPowerPlotPoints();
				//resultPanelTextArea.append("\npowerPlotPoints = "+powerPlotPoints[0] );
			} catch (NullPointerException e) {
				//////////////////////////System.out.println("NullPointerException powerPlotPoints = " + e);

			} catch (Exception e) {
				//////////////////////////System.out.println("Exception powerPlotPoints = " + e);
			}
		} catch (Exception e) {
			////////////////////System.out.println("result Exception " + e);
		}

		for (int i = 0; i < meanPlotPoints.length; i++) {
			////System.out.println("meanPlotPoints["+i+"] = " + meanPlotPoints[i] + ", powerPlotPoints[" + i + "] =" + powerPlotPoints[i]);

		}

		try {
			//Writer out = new BufferedWriter(new OutputStreamWriter(outputFile));
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(dataOutputFileName)));

			out.write("mu power \n");
			for (int i = 0; i < normal.numberPoints; i++) {
				out.write(meanPlotPoints[i] + " "  + powerPlotPoints[i] + "\n");
			}

			out.flush();
		} catch (IOException e) {
			//////////////////////System.out.println(e);
		}

	}

	public static double[] criticalPoints = 
	{-2.326348, -2.053749, -1.880794, -1.750686, -1.644854, -1.554774, -1.475791, 
		-1.405072, -1.340755, -1.281552, -1.226528, -1.174987, -1.126391, -1.080319, 
		-1.036433, -0.9944579, -0.9541653, -0.915365, -0.8778963, -0.8416212, 
		-0.8064212, -0.7721932, -0.7388468, -0.7063026, -0.6744898, -0.6433454, 
		-0.612813, -0.5828415, -0.5533847, -0.5244005, -0.4958503, -0.4676988, 
		-0.4399132, -0.4124631, -0.3853205, -0.3584588, -0.3318533, -0.3054808, 
		-0.2793190, -0.2533471, -0.2275450, -0.2018935, -0.1763742, -0.1509692, 
		-0.1256613, -0.1004337, -0.07526986, -0.05015358, -0.02506891, 0, 0.02506891, 
		0.05015358, 0.07526986, 0.1004337, 0.1256613, 0.1509692, 0.1763742, 0.2018935, 
		0.2275450, 0.2533471, 0.2793190, 0.3054808, 0.3318533, 0.3584588, 0.3853205, 
		0.4124631, 0.4399132, 0.4676988, 0.4958503, 0.5244005, 0.5533847, 0.5828415, 
		0.612813, 0.6433454, 0.6744898, 0.7063026, 0.7388468, 0.7721932, 0.8064212, 
		0.8416212, 0.8778963, 0.915365, 0.9541653, 0.9944579, 1.036433, 1.080319, 
		1.126391, 1.174987, 1.226528, 1.281552, 1.340755, 1.405072, 1.475791, 1.554774, 
		1.644854, 1.750686, 1.880794, 2.053749, 2.326348
	};

}
