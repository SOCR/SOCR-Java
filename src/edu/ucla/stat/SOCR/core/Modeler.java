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
package edu.ucla.stat.SOCR.core;

import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.KolmogorovSmirnoffResult;
import edu.ucla.stat.SOCR.util.*;

import javax.swing.JTextArea;

public class Modeler{

	//public static final int DISTRIBUTION_TYPE = 0;
	public static final int CONTINUOUS_DISTRIBUTION_TYPE = 1;
	public static final int DISCRETE_DISTRIBUTION_TYPE = 2;
	public static final int MIXED_DISTRIBUTION_TYPE = 3;

	public static final int FOURIER_TYPE = 101;
	public static final int WAVELET_TYPE = 102;
	
	private int modelType;
	private double[] modelX;
	private double[] modelY;

	
	/** takes data along with x, y limits and fits  a pdf to the
	 * data range and stores the resulting model fit in data arrays
	 * that must be returned by calls to returnModelX() and returnModelY()
	 */
    public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset){
    	
    }
		
	/** generates samples from the distribution and returns a double[] data type
	 */
	public double[] generateSamples(int sampleCount){
		double[] dat = new double[sampleCount];
		return dat;
	}


	/** return the description for this modeler*/
	public String getDescription(){
        String desc = new String();
        desc = "\tURL: http://socr.ucla.edu/htmls/dist/Binomial_Distribution.html";
        return desc;
	}


	public double getGraphUpperLimit(){
		return 0;
	}
	
    public double getGraphLowerLimit(){
    	return 0;
    }

	
	/** return the instructions for using this modeler
	 */
	public String getInstructions(){
		String instructions = new String();
		instructions = "\tURL: http://en.wikipedia.org/wiki/Binomial_distribution.";
		return instructions;
	}

	
	/**
	 * getKSModelTestString computes the KolmogorovSmirnoff test statistics 
	 * of the match between the 100 quantiles of the data and their 
	 * corresponding model-distribution quartile counterparts!
	 * 
	 * @param distributionModelName name of the distribution model-fit
	 * @param numberOfQuantiles number of quantiles to use (e.g., 100)
	 * @param x data quantiles array
	 * @param y model quantiles array
	 */
    public String getKSModelTestString(String distributionModelName, int numberOfQuantiles, 
    		double[] x, double[] y) {
    	Data KS_InputData = new Data();
				
		KS_InputData.appendX("X", x, DataType.QUANTITATIVE);
		KS_InputData.appendY("Y", y, DataType.QUANTITATIVE);
		KolmogorovSmirnoffResult result = null;
		
		try {
			result = (KolmogorovSmirnoffResult)KS_InputData.getAnalysis(AnalysisType.KOLMOGOROV_SMIRNOFF);
		} catch (Exception e) {;}
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");
        
		return ("Kolmogorov-Smirnoff Test for Differences Between the Data Distribution and\n"+
				"the Model Distribution (" + this.toString()+"\n"+
				")\n\tHypotheses: Ho: The Data follow the Model Distribution\n\t\t vs. " +
				"H1: The distributions of the Data and the Model are distinct\n" +
				"\n\t KS D-Statistics = "+df.format(result.getDStat())+
				"\n\t Z-Statistics = " + df.format(result.getZStat())+
				"\n\t CDF(" + df.format(result.getDStat()) + ") = " + 
					df.format(result.getProb())+
				"\n\t P-value = " + df.format((1-result.getProb()))+
				"\n\nDetails about the Kolmogorov-Smirnoff Test are available here:\n"+
				"http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysisActivities_KolmogorovSmirnoff"+
				"\n");
    }
	
	
	/** return the allowable x limit values. This method should return the lower
     * limit. eg: for a normal distribution lowerlimit = NEGATIVE_INFINITY
     */
	public double getLowerLimit(){
		return 0;
	}

	
    /** return the number of models to be plotted.
     * If n models are returned, the vectors from  returnModelX and
     * returnModelY will be split into n equal sub sections and plotted
     * @return
     */
	public int getModelCount(){
		return 1;
	}
	
	
    /** returns one if model is of distribution and needs
     *  to be rescaled for display. Returns 0 if model does not require scaling.
     * EG: Polynomial fit is type 0 and normal distribution is type 1.
     */
    public int getModelType(){
    	return modelType;
    }
	

	/** return the references for this modeler
	 */
	public String getResearch(){
		String research = new String();
		research = "\t http://socr.ucla.edu/";
		return research;
	}

	
	
	/** return the allowaable x limit values. This method should return the upper
     * limit. eg: for a normal distribution upperlimit = POSITIVE_INFINITY
     */
	public double getUpperLimit(){
		return 0;
	}

    public boolean isContinuous(){
    	return true;
    }

	
	
	public void registerObservers(ObservableWrapper o){}

	/** returns the fitted model values for X axis
	 */
	public double[] returnModelX() {
		return modelX;
	}

	/** returns the fitted densisty for corresponding X axis values
	 */
	public double[] returnModelY() {
		return modelY;
	}


	
	/** What is this method used for? */
	public boolean useInitButton(){
    	return false;
    }

	//public assessModelFit(data, distribution)
}