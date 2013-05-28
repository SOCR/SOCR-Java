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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.analyses.command.volume.Regressor;
import edu.ucla.stat.SOCR.analyses.command.volume.RegressorComparator;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataCase;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

/***** 
 * Usage: 
 *		java -ms200m -mx500m -cp /SOCR_LibPath/SOCR_core.jar:/SOCR_LibPath/SOCR_plugin.jar \
 *			edu.ucla.stat.SOCR.analyses.command.MultiRegressionCSV Input_Text_Data.txt -h \
 * 			-response ResponseVariableName -regressors Regressor1,Regressor2,Regressor1*Regressor2
 * 
 * See: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineMultiRegression
 * See: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression 
 * 
*****/

public class MultiRegressionCSV {
	private static final String MISSING_MARK = ".";
    static ArrayList<Regressor> regressorList;

	public static void main(String[] args) {
		int i,j,k;

		String regressorString = "";
		String responseString = "";
		
		String designMatrixInputFile = null;
		boolean header=false;
		boolean filesLoaded = false;
        HashMap<String, Integer> regressorColumnIndices;
        	// These indices contain the column-indices of the user-specified regressor columns

        regressorList = new ArrayList<Regressor>();

		System.out.println(
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");
		System.out.println(
        	"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");

		try {
			for (i = 0; i < args.length; i++) {
				if (args[i].compareToIgnoreCase("-h") == 0) {
					header = true;
				}  else if (args[i].compareToIgnoreCase("-regressors") == 0) {
        			regressorString = args[++i];
        			System.out.println("regressorString=" + regressorString);
            		// Parse the regressors list
            		StringTokenizer Tok = new StringTokenizer(regressorString, ",");
            		if (!parseRegressors(Tok)) {
            			System.err.println("The regressor list is invalid; make sure that the component variables\nof each interaction are also included as regressors");
                		System.exit(1);
            		}
        		} else if (args[i].compareToIgnoreCase("-response") == 0) {
        			responseString = args[++i];
        			System.out.println("responseString=" + responseString);
        		} else if (args[i].compareToIgnoreCase("-dm") == 0) {
            		designMatrixInputFile = args[++i];
            		filesLoaded = true;
            		System.out.println("designMatrixInputFile=" + designMatrixInputFile);
        		} else if (args[i].compareToIgnoreCase("-help") == 0) {
            		System.out.println("Please see the Web syntax for involking this tool: \n"
                    	+ "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
            		return;
        		} 
    		}
		} catch (Exception e) {
    		e.printStackTrace();
    		System.err.println("Incorrect call 1! Please see the syntax for invoking this tool\n"
    				+ "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
		}
				
        	// stores the number of regressors
        int independentLength = regressorList.size();
        	System.out.println("independentLength = " + independentLength);

        if (independentLength < 1) {
            System.err.println("No Independent Variable Specified (independentLength=" + independentLength
                    + ")\n Cannot run the regression\n"
                    + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
            return;
        }
				
		MultiLinearRegressionResult result = null;

		StringTokenizer st = null;
        int designMatrixNumberColumns = 1000;	// Maximum number of DesignMatrix columns

        // stores total number of subjects in the design matrix
        int xLength = 0;

        // stores actual number of subjects (after subjects with missing values have been removed)
        int actualLength = 0;

//		String[] varHeader = new String[1];
        regressorColumnIndices = new HashMap<String, Integer>();

        ArrayList<Double>[] xList = new ArrayList[independentLength];
        double[][] xDataArray = new double[independentLength][xLength];
        ArrayList<Double> yList = new ArrayList<Double>();
        
        for (i = 0; i < xList.length; i++) {
            xList[i] = new ArrayList<Double>();
        }

        String line = null, stringTmp;

        boolean read = true;
        if (header) {
            read = false;
        }

        ArrayList<String>[] designMatrix = null;

        int row = 0;

        // keep track of subjects that have missing values; should be excluded from regression analysis
        ArrayList<Integer> subjectsToExclude = new ArrayList<Integer>();
       
        System.err.println("Test 1!!!");
        
	    // read in values from design matrix
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(designMatrixInputFile));
            boolean exclude = false;
            while ((line = bReader.readLine()) != null) {
                exclude = false;
                //System.out.println("line = " + line);
                st = new StringTokenizer(line, ",; \t");

                if (regressorColumnIndices.isEmpty() && header) {		// Read Header Row
                    int n = st.countTokens();

                    designMatrixNumberColumns = n;
                    //System.out.println("Initializing designMatrix data structure");
                    designMatrix = new ArrayList[n];
                    // System.out.println("Reading DM Header!\t designMatrixNumberColumns="
                    //        + designMatrixNumberColumns);

                    st = new StringTokenizer(line, ",; \t");
                    // extract the column headers from the design matrix
//                        varHeader = new String[n];
                    String dmHeader = "";
                    for (k = 0; k < designMatrixNumberColumns; k++) {
                        dmHeader = st.nextToken().trim();
//                            varHeader[k] = dmHeader;
                        regressorColumnIndices.put(dmHeader, k);
                    }

                    // Include a test to make sure this array is defined, as if there is
                    // no variable match in the DM, then there is no need for any analysis.
                    for (j = 0; j < independentLength; j++) {
                        if (regressorColumnIndices.size() < 2) {
                            System.err.println("\nNo variable (" + regressorList.get(j).getRegressorString() + ") match!!!\n");
                            System.exit(1);
                        }
                    }

                    read = true;
                } else if (!header) {		// Must have a header row to deduce the predictors
                    System.err.println("No Independent Variable Specified 1. Cannot run the regression\n"
                            + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
                    return;
                } else {
                    // READ DATA From Design-Matrix
                    if (designMatrix == null) {
                        System.err.println("The design matrix has not been initialized; this means the header line was not read in");
                    }

                    int column = 0;
                    while (st.hasMoreTokens()) {
                        if (designMatrix[column] == null) {
                            designMatrix[column] = new ArrayList<String>();
                        }
                        String tmp = st.nextToken();
                        designMatrix[column].add(tmp);
                        column++;
                    }

                    int regCounter = 0;
                    boolean missing;
                    double[] valuesX = new double[independentLength];
                    for (Regressor regressor : regressorList) {
                        missing = false;
                        if (regressor.getComponentCount() > 1) {
                            double product = 1.0;
                            for (String str : regressor.getComponents()) {
                                // check for missing value
                                if (str.equalsIgnoreCase(MISSING_MARK)){
                                    missing = true;
                                    break;
                                } else {
                                    int index = regressorColumnIndices.get(str);
                                    ArrayList<String> colVals = designMatrix[index];
                                    String strVal = colVals.get(row);
                                    double doubleVal = Double.parseDouble(strVal);
                                    product = product * doubleVal;
                                }
                            }
                            valuesX[regCounter] = product;
                        } else {
                            String firstComponent = regressor.getComponents().get(0);
                            int index = regressorColumnIndices.get(firstComponent);
                            ArrayList<String> colVals = designMatrix[index];
                            String strVal = colVals.get(row);
                            if (strVal.equalsIgnoreCase(MISSING_MARK)) {
                                missing = true;
                            } else {
                                double doubleVal = Double.parseDouble(strVal);
                                valuesX[regCounter] = doubleVal;
                            }
                        }
                        if (missing){
                            exclude = true;
                            break;
                        }
                        regCounter++;
                    }
                                        
                    if (exclude){
                        subjectsToExclude.add(row);
                    }else{
                        for (int reg = 0; reg < independentLength; reg++){
                            xList[reg].add(valuesX[reg]);
                        }
                        
                        //Manage the response column
                        double valueY; 
                        missing = false;
                        int index = regressorColumnIndices.get(responseString);
                        ArrayList<String> colVals = designMatrix[index];
                        String strVal = colVals.get(row);
                        if (strVal.equalsIgnoreCase(MISSING_MARK)) missing = true;
                        else {
                            double doubleVal = Double.parseDouble(strVal);
                            valueY = doubleVal;
                            yList.add(valueY);
                        }
                     	//End managing Response
                    }
                    row++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }			
			
		if (!header)
			for (k=0; k<independentLength; k++)
				regressorColumnIndices.put("variable"+k, k);

        xLength = designMatrix[1].size();
        actualLength = xLength - subjectsToExclude.size();
        System.out.println("Total number of volumes in design matrix = " + xLength);
        System.out.println("Actual number of volumes after missing values have been removed = " + actualLength);

        // xData array will store the predictor values
        double[] xData = null;
        //String [] xData = null;

        // yData array will store the voxel intensity data
        double[] yData = new double[actualLength];
		
		Data data = new Data();
		System.err.println("Printing All variables (actualLength="+actualLength+")!!!\n");
		      
		for(j=0; j<actualLength; j++)
		{
			try {		
				//System.err.println("j = " + j + " Y = " + yList.get(j).doubleValue());
				yData[j] = yList.get(j).doubleValue();
			} catch (NumberFormatException e) {
				System.out.println("Line " + (j+1) + " is not in correct numerical format.");
				return;
			}
		}
		data.appendY(responseString, yData, DataType.QUANTITATIVE);
		
		for (i = 0; i < independentLength; i++) {
			xData = new double[actualLength];
			for(j=0; j<actualLength; j++)
			{
				try {
					xData[j] = xList[i].get(j).doubleValue();
					//System.out.println("i = " + i + " j = " + j + " X = " +xList[i].get(j));
				} catch (NumberFormatException e) {
					System.out.println("Line " + (j+1) + " is not in correct numerical format.");
					return;
				}
			}
			data.appendX(regressorList.get(i).getRegressorString(), xData, DataType.QUANTITATIVE);
		}
	
        
		/***************** MLR Analysis *****************/
		try {
			System.err.println("MultiLinearRegression result start" );
			result = (MultiLinearRegressionResult)data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
			result.setDecimalFormat(new java.text.DecimalFormat("#.000"));
			//System.out.println("MultiLinearRegression applet begin result = " + result);
		} catch (Exception e) {
				//e.printStackTrace();
		}
		if (result==null) return;

			/***************************************************/
		double[] predicted = null;
		double[] residuals = null;
		double[] sortedResiduals = null;
		double[] beta = null;
		double slope;
		double[] seBeta =null;
		double[] tStat = null;
		double[] pValue = null;
		int dfError = 0;
		double rSquare = 0;
		String[] varList = null;

		try {
			//varName = (ArrayList)(result.getTexture().get(MultiLinearRegressionResult.VARIABLE_LIST));
			varList = result.getVariableList();
		} catch (NullPointerException e) {
		}
		try {
			beta = result.getBeta();//(double[])(result.getTexture().get(MultiLinearRegressionResult.BETA));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			seBeta = result.getBetaSE();//(double[])(result.getTexture().get(MultiLinearRegressionResult.BETA_SE));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			tStat = result.getBetaTStat();//(double[])(result.getTexture().get(MultiLinearRegressionResult.T_STAT));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			pValue = (double[]) (result.getBetaPValue());//(String[])(result.getTexture().get(MultiLinearRegressionResult.P_VALUE));
		} catch (NullPointerException e) {
		}
		try {
			rSquare = result.getRSquare();//(String[])(result.getTexture().get(MultiLinearRegressionResult.P_VALUE));
		} catch (NullPointerException e) {
		}
		try {
			dfError = result.getDF();//(Integer)result.getTexture().get(MultiLinearRegressionResult.DF_ERROR)).intValue();
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			predicted = result.getPredicted();//(double[])(result.getTexture().get(MultiLinearRegressionResult.PREDICTED));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			residuals = result.getResiduals();//(double[])(result.getTexture().get(MultiLinearRegressionResult.RESIDUALS));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		System.out.println("\n");//clear first
				 
		System.out.println("\tNumber of Independent Variable(s) = "  + independentLength);
		System.out.println("\n\tSample Size =" + xLength);
		System.out.println("\n\tDependent Variable  = "  + responseString);
		System.out.println("\n\tIndependent Variable(s) = "+regressorString);
		
		System.out.println("\n\tRegression Model:\n\t\t" + responseString + " = ");
		result.setDecimalFormat(new java.text.DecimalFormat("##.######"));
		
		for (i = 0; i < beta.length; i++) {
			if (i==0) System.out.println("\t\t"+ result.getFormattedDouble(beta[i])+"*"+varList[i]);
			else if (beta[i]<0)
				System.out.println("\t\t"+ result.getFormattedDouble(beta[i])+"*"+varList[i]);
			else System.out.println("\t\t+"+ result.getFormattedDouble(beta[i])+"*"+varList[i]);
		}
		System.out.println("\t\t + E.\n");
		
		for (i = 0; i < beta.length; i++) {
			//System.out.println("\n\n"+varName.get(i) + "\n\tEstimate = "+ beta[i] + 
			// "\n\tStd. Error" + seBeta[i] + "\n\t t-valuer" + tStat[i] + "\n\tp-value " + pValue[i]);
			System.out.println("\n\n\t"+varList[i] + ":\n\tEstimate = "+ beta[i] + "\n\tStandard Error = " + seBeta[i] + "\n\tT-Value = " + tStat[i]);
			System.out.println("\n\tP-Value = " +pValue[i]);
		}
		System.out.println("\n\n\tR-Square = " + rSquare);
	}
	
    /** check regressor list for interactions
     * in particular, each interaction variable must be composed of individual regressor variables
     * that are also included in the regressor list; for instance, if we have an interaction variable A*B*C,
     * we must have (minimally): -regressors A,B,C,A*B,B*C,A*C,A*B*C
     */
    public static boolean parseRegressors(StringTokenizer st) {
        HashSet<String> validated = new HashSet<String>();

        int regressorIndex = 0;
        String reg;
        while (st.hasMoreTokens()) {
            reg = st.nextToken();
            System.out.println("reg = " + reg);
            regressorList.add(new Regressor(reg));
            regressorIndex++;
        }

        // impose an order on these regressors (least interactions to most)
        // at that point, we can iterate through them and just check the HashSet of already validated
        // regressors in order to validate a given regressor
        Collections.sort(regressorList, new RegressorComparator());
        boolean isValid = true;
        for (Regressor regressor : regressorList) {
            isValid = isRegressorValid(validated, regressor);
            if (isValid) {
                validated.add(regressor.getRegressorString());
            } else {
                return false;
            }
        }

        return true;
    }

    /** this method helps the parseRegressors method above
     * in particular, it takes a list of validated regressor variables and a not-yet-validated regressor variable
     * if the new variable is valid, this method returns true
     * if not, then it returns false; the criteria for validity is described in the description of parseRegressors
     */
    public static boolean isRegressorValid(HashSet<String> validRegressors, Regressor newRegressor) {
        if (newRegressor.getComponentCount() == 1) {
            return true;
        }

        ArrayList<String> regressorComponents = newRegressor.getComponents();
        for (String s : regressorComponents) {
            if (!validRegressors.contains(newRegressor.leaveComponentOut(s))) {
                return false;
            }
        }

        return true;
    }
    
}
