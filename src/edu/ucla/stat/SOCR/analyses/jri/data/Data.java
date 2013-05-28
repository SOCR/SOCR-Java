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
package edu.ucla.stat.SOCR.analyses.jri.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.jri.gui.*;
import edu.ucla.stat.SOCR.analyses.data.*;


// DO SHORT VERSION FIRST AND TEST THEN DO LONG VERSION.

public class Data {
	private HashMap<String, Column> mapX;
	private HashMap<String, Column> mapY;
	public static final String INDEPENDENT_VAR = "INDEPENDENT_VAR";
	public static final String DEPENDENT_VAR = "DEPENDENT_VAR";
	public static final String SIGNIFICANCE_LEVEL = "SIGNIFICANCE_LEVEL";
	private double significanceLevel = 0.05; // default in case anyone forgets.
	private int numberVariable = 2;
	private int numberCase = 0;
	/****************************************************************
	NOTE:
	ALL THE QUANTITATIVE DATA ARE FORCED TO CHANGE TO THE FORM OF double[].
	ALL THE FACTOR/QUALITATIVE DATA ARE FORCED TO CHANGE TO THE FORM OF String[].
	****************************************************************/
	// use double for all quantitative data
	// use byte for all factor data
	// use String for all character data
	// use Object for everything else, e.g. graphics.
	//private byte[] dataFactorX;
	//private double[] dataDoubleX;
	//private String[] dataStringX; // declare string separately for create new var.
	//private Object[] dataObjectX;

	public Data () {
		mapX = new HashMap<String, Column>();
		mapY = new HashMap<String, Column>();
	}
	private double[] toDoubleArray(int[] data) {
		double[] doubleArray = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			doubleArray[i] = data[i];
		}
		return doubleArray;
	}
	private double[] toDoubleArray(long[] data) {
		double[] doubleArray = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			doubleArray[i] = data[i];
		}
		return doubleArray;

	}
	private double[] toDoubleArray(float[] data) {
		double[] doubleArray = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			doubleArray[i] = data[i];
		}
		return doubleArray;
	}
	/**************** appendX with two parameters ****************/
	public void addPredictor (int[] data, String dataType) {
		appendX(toDoubleArray(data), dataType);
	}
	public void addPredictor (long[] data, String dataType) {
		appendX(toDoubleArray(data), dataType);

	}
	public void addPredictor (float[] data, String dataType) {
		appendX(toDoubleArray(data), dataType);
	}

	public void addPredictor (double[] data, String dataType) {
		//if (!dataType.equalsIgnoreCase(DataType.QUANTITATIVE)) {
		//	throw new WrongDataTypeException();
		//}
		numberCase = data.length;
		mapX.put(INDEPENDENT_VAR, new Column(data, dataType));
	}

	public void addPredictor(String[] data, String dataType){
		//if (!dataType.equalsIgnoreCase(DataType.FACTOR)) {
		//	throw new WrongDataTypeException();
		//}
		numberCase = data.length;
		mapX.put(INDEPENDENT_VAR, new Column(data, dataType));
	}

	public void appendX (int[] data, String dataType) {
		appendX(toDoubleArray(data), dataType);
	}
	public void appendX (long[] data, String dataType) {
		appendX(toDoubleArray(data), dataType);

	}
	public void appendX (float[] data, String dataType) {
		appendX(toDoubleArray(data), dataType);
	}
	public void addSignificanceLevel(double input) {
		this.significanceLevel = input;
	}
	public double getSignificanceLevel() {
		return this.significanceLevel;
	}
	public void appendX (double[] data, String dataType) {
		//if (!dataType.equalsIgnoreCase(DataType.QUANTITATIVE)) {
		//	throw new WrongDataTypeException();
		//}
		numberCase = data.length;
		mapX.put(INDEPENDENT_VAR, new Column(data, dataType));
	}
	public void appendX(String[] data, String dataType){
		//if (!dataType.equalsIgnoreCase(DataType.FACTOR)) {
		//	throw new WrongDataTypeException();
		//}
		numberCase = data.length;
		mapX.put(INDEPENDENT_VAR, new Column(data, dataType));
	}


	/**************** appendY with two parameters ****************/
	public void addResponse(int[] data, String dataType) {
		appendY(toDoubleArray(data), dataType);
	}
	public void addResponse(long[] data, String dataType) {
		appendY(toDoubleArray(data), dataType);

	}
	public void addResponse(float[] data, String dataType) {
		appendY(toDoubleArray(data), dataType);
	}
	public void addResponse(double[] data, String dataType){
		//if (!dataType.equalsIgnoreCase(DataType.QUANTITATIVE)) {
		//	throw new WrongDataTypeException();
		//}
		mapY.put(DEPENDENT_VAR, new Column(data, dataType));
	}

	public void addResponse(String[] data, String dataType){
		//if (!dataType.equalsIgnoreCase(DataType.FACTOR)) {
		//	throw new WrongDataTypeException();
		//}
		numberCase = data.length;
		mapY.put(DEPENDENT_VAR, new Column(data, dataType));
	}

	public void appendY(int[] data, String dataType) {
		appendY(toDoubleArray(data), dataType);
	}
	public void appendY(long[] data, String dataType) {
		appendY(toDoubleArray(data), dataType);

	}
	public void appendY(float[] data, String dataType) {
		appendY(toDoubleArray(data), dataType);
	}
	public void appendY(double[] data, String dataType){
		//if (!dataType.equalsIgnoreCase(DataType.QUANTITATIVE)) {
		//	throw new WrongDataTypeException();
		//}
		mapY.put(DEPENDENT_VAR, new Column(data, dataType));
	}

	public void appendY(String[] data, String dataType){
		//if (!dataType.equalsIgnoreCase(DataType.FACTOR)) {
		//	throw new WrongDataTypeException();
		//}
		numberCase = data.length;
		mapY.put(DEPENDENT_VAR, new Column(data, dataType));
	}

	/**************** appendY with three parameters ****************/
	//public void appendX(String name, int[] data, String dataType){
	//	mapX.put(name, new Column(data, dataType));
	//}
	public void addPredictor(String name, String[] data, String dataType){

		mapX.put(name, new Column(data, dataType));
	}
	public void addPredictor(String name, double[] data, String dataType){
		mapX.put(name, new Column(data, dataType));
	}
	public void addPredictor(String name, Object[] data, String dataType){
		mapX.put(name, new Column(data, dataType));
	}
	public void appendX(String name, String[] data, String dataType){

		mapX.put(name, new Column(data, dataType));
	}
	public void appendX(String name, double[] data, String dataType){
		mapX.put(name, new Column(data, dataType));
	}
	public void appendX(String name, Object[] data, String dataType){
		mapX.put(name, new Column(data, dataType));
	}


	//public void appendY(String name, int[] data, String dataType){
	//	mapY.put(name, new Column(data, dataType));
	//}
	public void addResponse(String name, String[] data, String dataType){
		mapY.put(name, new Column(data, dataType));
	}
	public void addResponse(String name, double[] data, String dataType){
		mapY.put(name, new Column(data, dataType));
	}
	public void addResponse(String name, Object[] data, String dataType){
		mapY.put(name, new Column(data, dataType));
	}
	public void appendY(String name, String[] data, String dataType){
		mapY.put(name, new Column(data, dataType));
	}
	public void appendY(String name, double[] data, String dataType){
		mapY.put(name, new Column(data, dataType));
	}
	public void appendY(String name, Object[] data, String dataType){
		mapY.put(name, new Column(data, dataType));
	}
	public Object getX(String name) {
		return mapX.get(name);

	}
	public Object getY(String name) {
		return mapY.get(name);

	}
	public int getSampleSize() {
		return numberCase;
	}
	public double[] getDoubleX(String name) {
		Set keySet = mapX.keySet();
		Iterator iterator = keySet.iterator();
		String keys = "";
		double x[] = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
			} catch (Exception e) {}
			Column xColumn = (Column) mapX.get(keys);
			//////////////////System.out.println("Data " + xColumn.getDataType());

			x = xColumn.getDoubleArray();
			for (int i = 0; i < x.length; i++) {
				//////////////////System.out.println("Data " + x[i]);
			}
		}
		return x;
	}

	public double[] getDoubleY(String name) {
		Set<String> keySet = mapY.keySet();
		Iterator<String> iterator = keySet.iterator();
		String keys = "";
		double y[] = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			
			try {
				Class cls = keys.getClass();
			} catch (Exception e) {}
			
			Column yColumn = (Column) mapY.get(keys);
			//////////////////System.out.println("Data " + yColumn.getDataType());

			y = yColumn.getDoubleArray();
			for (int i = 0; i < y.length; i++) {
				//////////////////System.out.println("Data " + y[i]);
			}
		}
		return y;
	}

	public HashMap<String, Column> getMapX() {
		return mapX;
	}
	public HashMap<String, Column> getMapY() {
		return mapY;
	}
	// Look up because we do not want the caller to know about Analysis class names

	public Result getAnalysis(short analysisType) throws Exception {
		Result result = null;
		String className = "";

		className = AnalysisType.lookup(analysisType);
		//System.out.println("Data " + "ClassName = "+className);
		Class cls;
		HashMap javaError = new HashMap();
		/*
		try {

			cls = Class.forName(className);

			if (cls == null) return null;
			//////////System.out.println("Data before try " + cls.getName());
			try {

				//((edu.ucla.stat.SOCR.analyses.model.Analysis)cls.newInstance()).getAnalysisType(); // return the type as a String.
				//////////System.out.println("Data in try " + ((Analysis)cls.newInstance()).getAnalysisType());

				//result = ((edu.ucla.stat.SOCR.analyses.model.Analysis)cls.newInstance()).analyze(this, analysisType);

				//System.out.println("Data result " + result);

			} catch (DataIsEmptyException e) {
				e.printStackTrace();
				result = new ErrorResult(javaError);
				javaError.put(Result.JAVA_ERROR, e);
				throw e;

			} catch (WrongAnalysisException e) {
				//System.out.println("Data WrongAnalysisException");
				e.printStackTrace();
				result = new ErrorResult(javaError);
				javaError.put(Result.JAVA_ERROR, e);
				throw e;

			} catch (InstantiationException e) {
				result = new ErrorResult(javaError);
				javaError.put(Result.JAVA_ERROR, e);
				throw e;

			} catch (IllegalAccessException e) {
				result = new ErrorResult(javaError);
				javaError.put(Result.JAVA_ERROR, e);
				throw e;
			}


			//String test = cls.getTestString();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			result = new ErrorResult(javaError);
			javaError.put(Result.JAVA_ERROR, e);
			throw e;
		}
		//System.out.println("Data OK result " + result);
		*/
		return result ;

	}

	/****************************************************************************/

	public String getAnalysisXMLInputString(short analysisType) throws Exception {
		String resultString = null;
		String className = "";
		//System.out.println("Data getAnalysisXMLInputString analysisType = "+analysisType);

		className = JRIAnalysisType.lookup(analysisType);
		//System.out.println("Data getAnalysisXMLInputString " + "Class Name = "+className);
		Class cls;
		HashMap javaError = new HashMap();
		try {

			cls = Class.forName(className);

			if (cls == null) return null;
			//System.out.println("Data " + cls.getName());
			try {

				((JRIAnalysis)cls.newInstance()).getAnalysisType();
				//System.out.println("Data in try before ");

				//resultString  = ((JRIAnalysis)cls.newInstance()).getAnalysisInputXMLString(this, analysisType);
				//System.out.println("Data in try after resultString = " + resultString);

			} catch (InstantiationException e) {
				////System.out.println("Data e = " + e);
				resultString = null;
				e.printStackTrace();
				throw e;
			} catch (IllegalAccessException e) {
				////System.out.println("Data e = " + e);
				resultString = null;
				e.printStackTrace();
				throw e;
			}
		} catch (ClassNotFoundException e) {
				////System.out.println("Data e = " + e);
			e.printStackTrace();

		}
		////System.out.println("\n\nData resultString returns " + resultString );

		return resultString ;

	}


	/************* Linear Models ****************/
	public SimpleLinearRegressionResult modelSimpleLinearRegression() throws Exception {
		return (SimpleLinearRegressionResult) this.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);

	}
	public MultiLinearRegressionResult modelMultiLinearRegression() throws Exception {
		return (MultiLinearRegressionResult) this.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
	}
	public AnovaOneWayResult modelAnovaOneWay() throws Exception {
		return (AnovaOneWayResult) this.getAnalysis(AnalysisType.ANOVA_ONE_WAY);
	}
	public AnovaTwoWayResult modelAnovaTwoWay() throws Exception {
		return (AnovaTwoWayResult) this.getAnalysis(AnalysisType.ANOVA_TWO_WAY);
	}


	/************* Parametric Testing ****************/

	public TwoIndependentTResult modelTwoIndependentT(double[] x, double[] y) throws Exception {
		this.appendX("X", x, DataType.QUANTITATIVE);
		this.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			return (TwoIndependentTResult) this.getAnalysis(AnalysisType.TWO_INDEPENDENT_T);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	public TwoPairedTResult modelTwoPairedT(double[] x, double[] y) throws Exception {
		this.appendX("X", x, DataType.QUANTITATIVE);
		this.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			return (TwoPairedTResult) this.getAnalysis(AnalysisType.TWO_PAIRED_T);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	public OneTResult modelOneT(double[] y) throws Exception {
		//////////////////System.out.println("Data modelOneT this = " + this);
		this.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			return (OneTResult) this.getAnalysis(AnalysisType.ONE_T);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/************* Non-Parametric Testing ****************/

	public TwoIndependentWilcoxonResult modelTwoIndependentWilcoxon(double[] x, double[] y) throws Exception {
		this.appendX("X", x, DataType.QUANTITATIVE);
		this.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			return (TwoIndependentWilcoxonResult) this.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	public TwoPairedSignedRankResult modelTwoPairedSignedRank(double[] x, double[] y) throws Exception {
		this.appendX("X", x, DataType.QUANTITATIVE);
		this.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			return (TwoPairedSignedRankResult) this.getAnalysis(AnalysisType.TWO_PAIRED_SIGNED_RANK);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
/*	public TwoPairedSignedRankResult getNormalPowerAnalysis(double[] x, double[] y) throws Exception {
		this.appendX("X", x, DataType.QUANTITATIVE);
		this.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			return (TwoPairedSignedRankResult) this.getAnalysis(AnalysisType.TWO_PAIRED_SIGNED_RANK);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
*/
	public NormalPowerResult getNormalAnalysis(double mu0, double x, double sigma) throws Exception {
		NormalPower normal = new NormalPower(mu0, x, sigma);
		try {
			return (NormalPowerResult) normal.getNormalAnalysis();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	public NormalPowerResult getNormalPower(int sampleSize, double sigma, double alpha, double mu0 , double muA, String hypothesisType) throws Exception {
		NormalPower normal = new NormalPower(sampleSize, sigma, alpha, mu0 , muA, hypothesisType);
		try {
			////////System.out.println("data getNormalPower");
			return (NormalPowerResult) normal.getPowerResult();

		} catch (Exception e) {
			////////System.out.println("data getNormalPower Exception ");
			throw new Exception(e);
		}
	}
	public NormalPowerResult getNormalPowerSampleSize(double power, double sigma, double alpha, double mu0 , double muA, String hypothesisType) throws Exception {
		NormalPower normal = new NormalPower(power, sigma, alpha, mu0 , muA, hypothesisType);
		////////System.out.println("Data getNormalPowerSampleSize " + power + ", " + sigma + ", " + alpha + ", " + mu0 + ", " + muA + ", " + hypothesisType);
		try {
			////////System.out.println("data getNormalPowerSampleSize");
			return (NormalPowerResult) normal.getSampleSizeResult();

		} catch (Exception e) {
			////////System.out.println("Data getNormalPowerSampleSize e = " + e);
			throw new Exception(e);
		}

	}

	public SurvivalResult getSurvivalResult(double[] time, byte[] censor, String[] groupNames) throws Exception {
		Survival survival = new Survival(time, censor, groupNames);
		//////System.out.println("data survival = " + survival);
		//////System.out.println("data groupNames.length = " +  groupNames.length );

		SurvivalResult result = null;
		try {
			//////System.out.println("data getSurvivalResult");
			result = (SurvivalResult) survival.getSurvivalResult();
			//////System.out.println("data survival try result = " + result);
			return result;


		} catch (Exception e) {
			//////System.out.println("Data survival e = " + e);
			throw new Exception(e);
		}

	}

	/*
	public NormalPowerResult getNormalSampleSize(int sampleSize, double sigma, double alpha, double mu0 , double muA, String hypothesisType) throws Exception {
		NormalPower test = new NormalPower(sampleSize, sigma, alpha, mu0 , muA, hypothesisType);
		try {
			return (NormalPowerResult) this.getPower();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}*/
	// test
	static void simpleLinearRegressionTest() {
		Data data = new Data();
		// this example is from Computational Statistics by Jennrich.
		double[] x = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		//String[] a = new String[]{"68", "49","60","68","97"};
		//String[] b = new String[]{"75", "63","57","88","88"};

		data.appendX("midterm", x, DataType.QUANTITATIVE);
		data.appendY("final", y, DataType.QUANTITATIVE);
		//data.appendX("midterm", a, DataType.QUANTITATIVE);
		//data.appendY("final", b, DataType.QUANTITATIVE);

		try {
			Result result = data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);
			// Result.getTexture() returns a HashMap that holds some result data.
			HashMap texture = result.getTexture();

		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}
	static void multiLinearRegressionTest2() { // 2 regressors
		Data data = new Data();
		double[] x1 = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] x2 = {60,94,91,81,80,92,74,89,96,87,86,94,94,94,79,50,92,82,94,78};
		double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		data.appendX("midterm1", x1, DataType.QUANTITATIVE);
		data.appendX("midterm2", x2, DataType.QUANTITATIVE);

		data.appendY("final", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}
	static void multiLinearRegressionTestSingular() { // 2 regressors
		Data data = new Data();
		double[] x1 = {1,2,3,4};
		double[] x2 = {3,4,5,6};
		double[] y = {10,30,20,25};
		data.appendX("X_1", x2, DataType.QUANTITATIVE);
		data.appendX("X_2", x1, DataType.QUANTITATIVE);

		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}

	static void multiLinearRegressionTest21() { // 3 regressors
		Data data = new Data();
		double[] x1 = {13.8, 12.6, 11.1, 11.3, 12.2, 14.7, 11.6, 14.9, 13.6,13.1, 11.5, 11.2, 14.2, 14.8, 13.2, 12.0, 14.8, 14.6, 12.7, 12.7};
		double[] x2 = {95, 104 ,99 ,102, 95, 106, 100, 108, 104, 97, 105, 103,97, 101, 96, 98, 105, 106, 95, 97};
		double[] y = {97, 98, 92 ,94, 93 ,106, 94, 109, 102, 96, 105, 97, 100,103, 97, 95, 105, 104, 95, 98};

		data.appendX("midterm1", x1, DataType.QUANTITATIVE);
		data.appendX("midterm2", x2, DataType.QUANTITATIVE);

		data.appendY("final", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
			//String testString = data.getAnalysisXMLInputString(AnalysisType.MULTI_LINEAR_REGRESSION);
			//////////////////System.out.println("testString " + testString);

		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}
	static void multiLinearRegressionTest3() { // 3 regressors
		Data data = new Data();
		double[] x1 = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] x2 = {60,94,91,81,80,92,74,89,96,87,86,94,94,94,79,50,92,82,94,78};
		double[] x3 = {6,4,1,1,8,2,4,9,6,7,6,4,4,4,9,0,2,2,9,7};
		double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		data.appendX("midterm1", x1, DataType.QUANTITATIVE);
		data.appendX("midterm2", x2, DataType.QUANTITATIVE);
		data.appendX("midterm3", x3, DataType.QUANTITATIVE);

		data.appendY("final", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}
	static void multiLinearRegressionTest4() { // 3 regressors
		Data data = new Data();
		double[] x1 = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] x2 = {60,94,91,81,80,92,74,89,96,87,86,94,94,94,79,50,92,82,94,78};
		double[] x3 = {6,4,1,1,8,2,4,9,6,7,6,4,4,4,9,0,2,2,9,7};
		double[] x4 = {.66,4.5,1.1,1.3,.88,.2,4,9,.64,7,.66,.54,9.4,64,9.1,0.3,2.3,2.8,9.5,7.1};

		double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		data.appendX("midterm1", x1, DataType.QUANTITATIVE);
		data.appendX("midterm2", x2, DataType.QUANTITATIVE);
		data.appendX("midterm3", x3, DataType.QUANTITATIVE);
		data.appendX("score", x3, DataType.QUANTITATIVE);

		data.appendY("final", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}

	static void anovaOneWayTest() {
		Data data = new Data();
		String[] group = {"1","1","1","1","1","1", "2","2","2","2","2","2","2","2", "3","3","3","3","3"};

		double[] score = {93,67,77,92,97,62, 136,120,115,104,115,121,102,130, 198,217,209,221,190};

		data.appendX("GROUP", group, DataType.FACTOR);
		data.appendY("Y_VALUE", score, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.ANOVA_ONE_WAY);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}

	static void anovaTwoWayTest() {
		Data data = new Data();
		//String[] group1 = {"1","1","1","1","1","2","2","2","2","2"};
		//String[] group2 = {"1","1","1","2","2","2","2","3","3","3"};
		//double[] score = {100,120,130,140,150,110,120,140,140,160};
		String[] group1 = {"1","1","1","2","2","2"};
		String[] group2 = {"1","2","3","1","2","3"};
		double[] score = {93,136,198,88,148,279};

		data.appendX("I", group1, DataType.FACTOR);
		data.appendX("J", group2, DataType.FACTOR);

		data.appendY("Y", score, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.ANOVA_TWO_WAY);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}

	static void oneTTest() {
		Data data = new Data();
		double[] score = {93,67,77,92,97,62,136,120,115,104,115,121,102,130, 198,217,209,221,190};

		//data.appendX("score", score, DataType.FACTOR);
		data.appendY("score", score, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.ONE_T);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}
	static Result oneTTest(double[] data, String nameOfData) {
		return null;
	}
	static void twoIndenpdentTTest() {
		Data data = new Data();
		double[] x = {95, 104, 99, 102, 95, 106, 100, 108, 104, 97};
		double[] y = {97, 98, 92, 94, 93, 106, 94, 109};
		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.TWO_INDEPENDENT_T);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}

	}
	static void twoIndenpdentWilcoxon() {
		Data data = new Data();
		//double[] x = {95, 104, 99, 102, 95, 106, 100, 108, 104, 97};
		//double[] y = {97, 98, 92, 94, 93, 106, 94, 109};
		double[] x = {79.98, 80.04, 80.02, 80.04, 80.03, 80.03, 80.04, 79.97, 80.05, 80.03, 80.02, 80.00, 80.02};
		double[] y = {80.02, 79.94, 79.98, 79.97, 79.97, 80.03, 79.95, 79.97};

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}

	}
	static void twoIndenpdentWilcoxon2() {
		Data data = new Data();
		double[] x = {1.94, 1.94, 2.92, 2.92, 2.92, 2.92, 3.27, 3.27, 3.27, 3.27, 3.7, 3.7, 3.74};
		double[] y = {3.27, 3.27, 3.27, 3.7, 3.7, 3.74};
		////System.out.println("twoIndenpdentWilcoxon2 x.length = "+ x.length);
		////System.out.println("twoIndenpdentWilcoxon2 y.length = "+ y.length);

		//double[] x = {79.98, 80.04, 80.02, 80.04, 80.03, 80.03, 80.04, 79.97, 80.05, 80.03, 80.02, 80.00, 80.02};
		//double[] y = {80.02, 79.94, 79.98, 79.97, 79.97, 80.03, 79.95, 79.97};

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}

	}
	static void twoIndenpdentWilcoxon3() {
		Data data = new Data();
		//double[] x = {7.3, 6.9, 7.2, 7.4, 7.2}; // more ties
		//double[] y = {7.4, 6.7, 6.9, 6.7, 7.1};
		double[] y = {7.3, 6.9, 7.2, 7.8, 7.2};
		double[] x = {7.4, 6.8, 6.9, 6.7, 7.1};
		//double[] y = {10, 18};
		//double[] x = {11, 15, 19};
		//double[] x = {10, 11, 12, 13, 19, 21, 22, 23, 24};
		//double[] y = {14};

		////System.out.println("twoIndenpdentWilcoxon2 x.length = "+ x.length);
		////System.out.println("twoIndenpdentWilcoxon2 y.length = "+ y.length);

		//double[] x = {79.98, 80.04, 80.02, 80.04, 80.03, 80.03, 80.04, 79.97, 80.05, 80.03, 80.02, 80.00, 80.02};
		//double[] y = {80.02, 79.94, 79.98, 79.97, 79.97, 80.03, 79.95, 79.97};


		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {

		//////////////////System.out.println("Data " + e.toString());
		}

	}

	// from http://www.stat.umn.edu/geyer/old02/5601/examp/ranksum.html
	static void twoIndenpdentWilcoxon4() {
		Data data = new Data();
		double[] x = {.73, .8, .83, 1.04, 1.38, 1.45, 1.46, 1.64, 1.89, 1.91};
		double[] y = {.74, .88, .9, 1.15, 1.21};

		////System.out.println("twoIndenpdentWilcoxon2 x.length = "+ x.length);
		////System.out.println("twoIndenpdentWilcoxon2 y.length = "+ y.length);

		//double[] x = {79.98, 80.04, 80.02, 80.04, 80.03, 80.03, 80.04, 79.97, 80.05, 80.03, 80.02, 80.00, 80.02};
		//double[] y = {80.02, 79.94, 79.98, 79.97, 79.97, 80.03, 79.95, 79.97};

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}

	}


	// very simple example
	static void twoIndenpdentWilcoxon5() {
		Data data = new Data();
		double[] x = {10, 15};
		double[] y = {11, 18, 19};

		////System.out.println("twoIndenpdentWilcoxon2 x.length = "+ x.length);
		////System.out.println("twoIndenpdentWilcoxon2 y.length = "+ y.length);

		//double[] x = {79.98, 80.04, 80.02, 80.04, 80.03, 80.03, 80.04, 79.97, 80.05, 80.03, 80.02, 80.00, 80.02};
		//double[] y = {80.02, 79.94, 79.98, 79.97, 79.97, 80.03, 79.95, 79.97};

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}

	static void twoIndenpdentKruskalWallis() {
		Data data = new Data();
		double[] xA = {83, 91, 94, 89, 89, 96, 91, 92, 90};
		double[] xB = {91, 90, 81, 83, 84, 83, 88, 91, 89, 84};
		double[] xC = {101, 100, 91, 93, 96, 95, 94};
		double[] xD = {78, 82, 81, 77, 79, 81, 80, 81};

		data.appendX("A", xA, DataType.QUANTITATIVE);
		data.appendX("B", xB, DataType.QUANTITATIVE);
		data.appendX("C", xC, DataType.QUANTITATIVE);
		data.appendX("D", xD, DataType.QUANTITATIVE);
		data.addSignificanceLevel(0.05);

		try {
			Result result = data.getAnalysis(AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS);
		} catch (Exception e) {

		//////////////////System.out.println("Data " + e.toString());
		}
	}

	static void twoPairedTTest() {
		Data data = new Data();
		//double[] x = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		//double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		double x []= {95, 104, 99, 102, 95, 106, 100, 108, 104, 97, 105, 103, 97, 101, 96, 98, 105, 106,   95, 97};
		double y[] = {97, 98, 92, 94, 93, 106, 94, 109, 102, 96, 105, 97, 100, 103, 97, 95, 105, 104, 95, 95};
		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.TWO_PAIRED_T);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}
	static void twoPairedSignedRankTest() {
		Data data = new Data();
		double[] s = {3,3,3,0,3,0,0,2,3};
		double[] p = {3,1,3,2,3,1,2,3,3};
		//{.32,.4,.11,.47,.32,.35,.32,.63,.5,.6,.38,.46,.2,.31,.62,.52,.77,.23,.3,.7,.41,.53,.19,.31,.48};
		//double[] p = {.39,.47,.11,.43,.42,.3,.43,.98,.86,.79,.33,.45,.22,.3,.6,.53,.85,.21,.33,.57,.43,.49,.2,.35,.4};
		data.appendX("X", s, DataType.QUANTITATIVE);
		data.appendY("Y", p, DataType.QUANTITATIVE);
		//////////////////System.out.println("s.length = " + s.length);
		//////////////////System.out.println("p.length = " + p.length);

		double[] diff = new double[s.length];
		for (int i = 0; i < s.length; i++) {
			diff[i] = p[i] - s[i];
			//////////////////System.out.println("diff["+i+"] = " + diff[i]);
		}
		try {
			Result result = (TwoPairedSignedRankResult)data.getAnalysis(AnalysisType.TWO_PAIRED_SIGNED_RANK);
		} catch (Exception e) {
			//////////////////System.out.println("Data " + e.toString());
		}
	}


	static void chiSquareModelFitTest() {
		Data data = new Data();
		double x []= {95, 104, 99, 102, 95, 106, 100, 108, 104, 97,
				105, 103, 97, 101, 96, 98, 105, 106,   95, 97};
		double y[] = {97, 98, 92, 94, 93, 106, 94, 109, 102, 96,
				105, 97, 100, 103, 97, 95, 105, 104, 95, 95};
		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);

		//Result result = data.getAnalysis(AnalysisType.CHI_SQUARE_MODEL_FIT);
	}


	static void logisticRegressionTest() {
		Data data = new Data();


		double x[]= {28, 29, 30, 31, 32, 33};
		double y[] = {-.6931, -.4055, 1.2528, 1.2528, 1.3863, 2.6391};

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);
		try {
			MultiLinearRegressionResult result = (MultiLinearRegressionResult)data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
			////////System.out.println("alpha = " + result.getBeta()[0]);
			////////System.out.println("beta = " + result.getBeta()[1]);

		} catch (Exception e) {
			////////System.out.println("Data " + e.toString());
		}
	}

	static void survivalTest() {

		double[] time = {1,10,22,7,3,32,12,23,8,22,17,6,2,16,11,34,8,32,12,25,2,11,5,20,4,19,15,6,8,17,23,35,5,6,11,13,4,9,1,6,8,10};
		byte[] censor = {1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,0};
		String[] treat = {"control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",   "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP"};

		try {
			Data data = new Data();
			SurvivalResult result = (SurvivalResult)data.getSurvivalResult(time, censor, treat);
			double[][] survivalTimeArray = result.getSurvivalTime();
			double[][] survivalRateArray = result.getSurvivalRate();
			double[][] upperCIArray = result.getSurvivalUpperCI();
			double[][] lowerCIArray = result.getSurvivalLowerCI();
			int[][] atRiskArray = result.getSurvivalAtRisk();
			double[][] survivalSEArray = result.getSurvivalSE();

			//////System.out.println(" survivalTimeArray = " + survivalTimeArray);
			//////System.out.println(" survivalRateArray = " + survivalRateArray);
			//////System.out.println(" upperCIArray = " + upperCIArray);
			//////System.out.println(" lowerCIArray = " + lowerCIArray);
			//////System.out.println(" atRiskArray = " + atRiskArray);
			//////System.out.println(" survivalSEArray = " + survivalSEArray);

			for (int i = 0; i < survivalTimeArray.length; i++) {
				for (int j =0; j < survivalTimeArray[0].length; j++) {
					//////System.out.println("S = " + survivalTimeArray[i][j] + ", R = " + survivalRateArray[i][j] + ", U = " + upperCIArray[i][j] + ", L = " + lowerCIArray[i][j] + " E = " + survivalSEArray[i][j] + ", A = " + atRiskArray[i][j]);
				}
			}
		} catch (Exception e) {
			//////System.out.println("Data " + e.toString());
		}
	}



/*
	static void twoPairedSignedTest() {
		Data data = new Data();
		//double[] x = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		//double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		double x[]= {.32, .40, .11, .47, .32, .35, .32, .63, .50, .60, .38, .46, .20, .31, .62, .52, .77, .23, .30, .70, .41, .53, .19, .31, .48};
		double y[] = {.39, .47, .11, .43, .42, .30, .43, .98, .86, .79, .33, .45, .22, .30, .60, .53, .85, .21, .33, .57, .43, .49, .20, .35, .40};
		//////////////////System.out.println("Data " + "In Data Class x.length = " + x.length);
		//////////////////System.out.println("Data " + "In Data Class y.length = " + y.length);

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);

		Result result = data.getAnalysis(AnalysisType.TWO_PAIRED_SIGNED);
	}
*/
	// Annie's TEST code
	public static void main(String args[]) {
		//double[] x1 = {1,2,3,4.0000001};
		//double[] x2 = {3,4,5,6};
		//multiLinearRegressionTestSingular();
		//multiLinearRegressionTest21();
		//multiLinearRegressionTest3();
		//multiLinearRegressionTest4();
		//simpleLinearRegressionTest();
		//anovaOneWayTest();
		//anovaTwoWayTest();
		//oneTTest();
		//twoIndenpdentTTest();
		//twoPairedTTest();
		//twoIndenpdentWilcoxon2();
		//twoIndenpdentWilcoxon3();
		//twoIndenpdentWilcoxon4();
		//twoIndenpdentWilcoxon5();
		twoIndenpdentKruskalWallis();
		//twoPairedSignedRankTest();
		//survivalTest();

		/*int[] test = new int[]{1, 2, 3};
		Data testd = new Data();
		try {
			testd.appendX(test, DataType.QUANTITATIVE);
		} catch (Exception e) {
		}*/
		/*
		NormalPowerResult result = null;
		Data data = new Data();
		try {
			//result = (NormalPowerResult) data.getNormalPower(sampleSize, sigma, alpha, mu0 , muA, hypothesisType);
			result = (NormalPowerResult) data.getNormalPower(100, 3500, 0.05, 2500 , 2350, NormalPowerResult.HYPOTHESIS_TYPE_NE);
		} catch (Exception e) {
			//////////System.out.println("in data " + e);
		}*/
	}

}


