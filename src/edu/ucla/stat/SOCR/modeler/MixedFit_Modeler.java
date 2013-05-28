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

package edu.ucla.stat.SOCR.modeler;

import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.util.*;

import javax.swing.*;
import JSci.maths.*;

//import edu.ucla.stat.SOCR.gui.*;
//import edu.ucla.stat.SOCR.util.normalMixture;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.KolmogorovSmirnoffResult;
import edu.ucla.stat.SOCR.core.*;

/**This class Designs a Normal Mixture model fitting curve. */
public class MixedFit_Modeler extends Modeler {
	double[] modelPts;
	double[] mType;
	public JTextField paramField = new JTextField("2",3); // was 1 (what's this anyway???)
	public JLabel paramLabel = new JLabel("Mixture Count");
	public JTextField IterationsField = new JTextField("10",3);
	public JLabel IterationsLabel = new JLabel("Iterations");
	//private JButton reinit = new JButton("RE-INIT");
	public float kernalVar = 0;
	private double minX = 0;
	private double maxX = 0;
	public double data[][];
	private Data KS_InputData;
	public int modelCt = 1;
	
	// public double modelX[], modelY[];

	public DoubleVector coeffs;
	private int dataPts;
	public javax.swing.table.TableColumn clm2;
	private int runs = 0;
	//  public JTextField kernalVarField = new JTextField("1", 3);
	private JTextArea statsTable = new JTextArea(6, 25);
	private double mn = 0;
	private double sd = 1;
	public JCheckBox rawCheck = new JCheckBox("Raw Data",false);
	public static normalMixture normalmixture;
	private double[] modelX;
	private double[] modelY;
	private int modelType = CONTINUOUS_DISTRIBUTION_TYPE; // normal mixed model.
	private static boolean isContinuous = false;
	public static final int SLICE_SIZE =  8001;
	//private static boolean reset = true;
	private static float[] dummyRawData;
	private static int curveFittingCount = 0;
	private static edu.ucla.stat.SOCR.distributions.NormalDistribution standardNormal;
	public edu.ucla.stat.SOCR.distributions.MixtureDistribution mixtureDistributionObject;
	private edu.ucla.stat.SOCR.distributions.NormalDistribution[] mixureComponents;
	
    public MixedFit_Modeler() {
		//reset = true;
		curveFittingCount = 0;

		//normalmixture = new normalMixture(1,dummyRawData,(double)0.001);

		////System.out.println("MixedFit_Modeler constructor modelType =  " + modelType);
		////System.out.println("MixedFit_Modeler constructor curveFittingCount =  " + curveFittingCount);

		standardNormal = new edu.ucla.stat.SOCR.distributions.NormalDistribution(0,1);
		
    }


    public MixedFit_Modeler(JPanel controlpanel) {
		//reset = true;
		//normalmixture = new normalMixture(1,dummyRawData,(double)0.001);

		////System.out.println("MixedFit_Modeler constructor JPanel modelType =  " + modelType);
		addParams(controlpanel);
		// buttonGroup.add(estimateParams);
		// buttonGroup.add(userParams);
		controlpanel.repaint();
		//controlpanel.
		standardNormal = new edu.ucla.stat.SOCR.distributions.NormalDistribution(0,1);
    }


    public int getModelCount() {
        return modelCt;
        }

	public double getLowerLimit() {
		return Double.NEGATIVE_INFINITY;
	}

	public double getUpperLimit() {
		return Double.POSITIVE_INFINITY;
	}

	public void addParams(JPanel controlpanel) {
		controlpanel.add(paramLabel);
		controlpanel.add(paramField);
		controlpanel.add(IterationsLabel);
		controlpanel.add(IterationsField);
		//controlpanel.add(reinit);
		//SOCRModeler.addToGridBag(controlpanel,paramLabel , 0, 0, 1, 1, 1.0, 0.0);
		//SOCRModeler.addToGridBag(controlpanel,paramField , 0, 1, 1, 1, 1.0, 0.0);
		//SOCRModeler.addToGridBag(controlpanel,IterationsLabel , 0, 2, 1, 1, 1.0, 0.0);
		//SOCRModeler.addToGridBag(controlpanel,IterationsField , 0, 3, 1, 1, 1.0, 0.0);

	}

	public void registerObservers(ObservableWrapper o) {
		//   o.addJCheckBox(estimateParams);
		//  o.addJCheckBox(userParams);
		o.addJTextField(paramField);
		o.addJTextField(IterationsField);
		//o.addJButton(reinit);
	}

    public void toggleParams(boolean istrue) {
        if(istrue) {

            paramLabel.setVisible(true);
            paramField.setVisible(true);
        }else {
            paramLabel.setVisible(false);
            paramField.setVisible(false);
        }
    }


    public int getModelType() {
        return modelType;

    }
    public double[] returnModelX() {
	    ////System.out.println("MixedFit_Modeler returnModelX modelX = " + modelX);
        return modelX;
        }

        public double[] returnModelY() {
        return modelY;
        }

        public double[] generateSamples( int sampleCount) {
            return null;

        }
	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {

		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}

	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		//System.out.println("MixedFit_Modeler fitCurve rescaleClicked = " + rescaleClicked);
		//System.out.println("MixedFit_Modeler fitCurve scaleUp = " + scaleUp);
		//System.out.println("MixedFit_Modeler fitCurve initReset = " + initReset);
		//System.out.println("MixedFit_Modeler fitCurve curveFittingCount = " + curveFittingCount);
		boolean reFitModel = true;
		if (rescaleClicked) {
			reFitModel = false;
		}
		else { //if ((curveFittingCount == 0) || (!rescaleClicked && initReset)) {
			//System.out.println("MixedFit_Modeler fitCurve else if new normalmixture");
			//normalmixture = new normalMixture(1,rawDat,(double)0.001);
		}

		//System.out.println("MixedFit_Modeler fitCurve start reFitModel = " + reFitModel);
		//System.out.println("===========================================================");


		//if (curveFittingCount > 0 && !scaleUp && !initReset) return;
		//if (scaleUp) initReset = false;
		////System.out.println("MixedFit_Modeler fitCurve rawDat.length = " + rawDat.length);
		////System.out.println("MixedFit_Modeler fitCurve start modelType = " + modelType);

		try {
			int mixCount = 0;
			int iterations = 0;
			////System.out.println("MixedFit_Modeler fitCurve in try curveFittingCount = " + curveFittingCount);
			////System.out.println("MixedFit_Modeler fitCurve in try initReset = " + initReset);

			if ((curveFittingCount == 0) || (!rescaleClicked && initReset)) {
				//normalmixture = new normalMixture(1,rawDat,(double)0.001);

				////System.out.println("MixedFit_Modeler fitCurve in try in if");

				try {
					mixCount = Integer.parseInt(paramField.getText());
					if (mixCount <= 0) {
						mixCount = 1;
					}
				} catch (Exception e) { // not a interger.
					mixCount = 1;
				}
				////System.out.println("MixedFit_Modeler fitCurve mixCount = " + mixCount);

				normalmixture = new normalMixture(mixCount,rawDat,(double)0.001);

				////System.out.println("MixedFit_Modeler fitCurve normalmixture = " + normalmixture);
				////System.out.println("MixedFit_Modeler fitCurve normalmixture.getCount() = " + normalmixture.getCount());
				iterations = Integer.parseInt(IterationsField.getText());
				////System.out.println("MixedFit_Modeler fitCurve iterations = " + iterations);
				//if (reFitModel) { //(curveFittingCount == 0) || (!rescaleClicked && initReset)) {
					for(int i=0;i<iterations;i++) {
						normalmixture.updateEStep();
						normalmixture.updateMStep();
					}
					initReset = false;
				//}
			}

			float ind = 1;
			//    minx = graph.getMinX();
			//   maxx = graph.getMaxX();
			int number = SLICE_SIZE; // was
			ind = (maxx-minx)/(number - 1);
			////System.out.println("MixedFit_Modeler fitCurve rawDat = " + rawDat);
			//int count = normalmixture.getCount();
			modelX = new double[number*(1+normalmixture.getCount())];
			modelY = new double[number*(1+normalmixture.getCount())];
			//////System.out.println("MixedFit_Modeler fitCurve modelX = " + modelX);
			//////System.out.println("MixedFit_Modeler fitCurve modelY = " + modelY);
			//////System.out.println("MixedFit_Modeler fitCurve normalmixture.getCount() = " + normalmixture.getCount());

			//////System.out.println("MixedFit_Modeler fitCurve modelX.length = " + modelX.length);
			//////System.out.println("MixedFit_Modeler fitCurve modelY.length = " + modelY.length);

			for(int i = 0; i < number; i++) {
				modelX[i] = minx + ind * i;
				modelY[i] = normalmixture.getMixDensity(modelX[i]);
			}

			for(int j = 1; j < normalmixture.getCount()+1; j++){
				for(int i = 0; i<number; i++) {
					modelX[i+number*j] = minx + ind * i;
					modelY[i+number*j] = normalmixture.getKernalDensity(j-1, modelX[i+number*j]);
				}
			}

			double maxY = modelY[0]; // maximum of Y
			int increment = 0;
			boolean zoomInY = false;
			double zoomScale = 10 * 10 * 10;
			for(int i = 0; i < modelY.length; i++){
				if(!(new Double(modelY[i]).isNaN()) && modelY[i] > maxY) {
					maxY = modelY[i];
				}

				if (!(new Double(modelY[i]).isNaN())) {
					;
				}
			}
			////System.out.println("MixedFit_Modeler maxY = " + maxY);

			////System.out.println("MixedFit_Modeler zoomInY = " + zoomInY);
			//if (count == 4 || count == 5 || count == 6 || count == 7) {
			//	////System.out.println("MixedFit_Modeler count is 4, 5, 6, 7");
			//}

			//double dataMaxy = (double) graph.getMaxY();

			resultPanelTextArea.setText("");
			String modelID = "";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");
			
			//	First, Order the mixtures left (smalest-mean) to right (largest Mean)
			double [] rawMeans = new double[normalmixture.getCount()];
			double [] orderedMeans = new double[normalmixture.getCount()];
			int [] indexOrdering = new int[normalmixture.getCount()]; // indexOrdering[order-index] = raw-real-native-index
			QSortAlgorithm quick = new QSortAlgorithm();
			
			for (int k=0; k<normalmixture.getCount(); k++) {
				rawMeans[k] = normalmixture.getMean(k);
				orderedMeans[k] = normalmixture.getMean(k);
			}
			quick.sort(orderedMeans);
			
			for (int k=0; k<normalmixture.getCount(); k++) {
				for (int p=0; p<normalmixture.getCount(); p++) {
					if (orderedMeans[k]==rawMeans[p]) indexOrdering[k]=p;
				}
			}
			
			for(int i = 0; i < normalmixture.getCount();i++) {
				modelID = i + "";
				resultPanelTextArea.append("Mixture Model " +modelID+ ": \n\tWeight =" + 
						Double.toString(normalmixture.getWeight(indexOrdering[i])) + 
						"\n\tMean = "+df.format(normalmixture.getMean(indexOrdering[i])) + 
						"\n\tVariance = "+df.format(normalmixture.getVariance(indexOrdering[i])) +  
						"\n\n" );
				
				// Second, compute the Z-statistics and p-values (on the pre-ordered Gaussian Mixtures!!!)
				if (i>0 && normalmixture.getVariance(indexOrdering[i-1])!= normalmixture.getVariance(indexOrdering[i])) {	
							// for all pairs of Gausian mixture components, do a statistical
							// test to represent if the 2 Gaussians have distinct means
							// Zo=(m1-m2)/(Sqrt{Sigma1^2/N1+ Sigma1^2/N1})~N(0,1^2). 
					
					double zStat = Math.abs(normalmixture.getMean(indexOrdering[i-1])-normalmixture.getMean(indexOrdering[i]))/
							Math.sqrt(normalmixture.getVariance(indexOrdering[i-1])/
									(normalmixture.getWeight(indexOrdering[i-1])*normalmixture.getCount())+ 
									normalmixture.getVariance(indexOrdering[i])/
									(normalmixture.getWeight(indexOrdering[i])*normalmixture.getCount())); 
					//System.err.println("zStat="+zStat);
					
					// Third, report the appropriate stats
					if (!(new Double(zStat).isNaN())) {
						double pValue = 1-standardNormal.getCDF(zStat);
						//System.err.println("pValue="+pValue);
						//System.err.println("Comparison of Statistical Differences Between the Means of Model(" 
						//	+(i-1)+") and Model("+i+"): Zo= "+zStat+" p-value = "+ pValue+"\n\n" );
					
						resultPanelTextArea.append("Comparison of Statistical Differences Between the Means of Model(" 
							+(i-1)+") and Model("+i+"): \n Zo= "+zStat+"; p-value = "+ pValue+"\n\n" );
					}
				}
			}

			resultPanelTextArea.append("\nINTERSECTION POINT(S): \n");
			double a,b,c,ratio,est0,est1;
			double mu0, var0, wt0, mu1, var1, wt1;
			for(int i = 0; i< normalmixture.getCount()-1;i++) {
				mu0 = normalmixture.getMean(i);
				var0 = normalmixture.getVariance(i);
				wt0 = normalmixture.getWeight(i);

				mu1 = normalmixture.getMean(i+1);
				var1 = normalmixture.getVariance(i+1);
				wt1 = normalmixture.getWeight(i+1);
				ratio = wt0/wt1;
				a = var1 - var0;
				b = 2*(var0*mu1 - var1*mu0);
				c = var1*mu0*mu0 - var0*mu1*mu1 - 2*var0*var1*Math.log(ratio*Math.sqrt(var1/var0));

				est0 = (-b - Math.sqrt(b*b-4*a*c))/(2*a);
				est1 = (-b + Math.sqrt(b*b-4*a*c))/(2*a);

				if(est0< Math.max(mu0,mu1) &&    est0 > Math.min(mu0,mu1))
					resultPanelTextArea.append("" + Double.toString(est0)+"\n");
				else  {
					if(est1< Math.max(mu0,mu1) &&    est1 > Math.min(mu0,mu1))
						resultPanelTextArea.append(Double.toString(est1)+"\n");
					else
						resultPanelTextArea.append("none \n");
				}
			} // end for i

			//	Forth, report the KolmogorovSmirnoff test statistics of the match between the 
			//	100 quantiles of the data and their corresponding Mixture-distribution quartile counterparts!
				KS_InputData = new Data();
				mixureComponents = new edu.ucla.stat.SOCR.distributions.NormalDistribution[normalmixture.getCount()];
				for (int i=0; i<normalmixture.getCount(); i++)
					mixureComponents[i] = new edu.ucla.stat.SOCR.distributions.NormalDistribution(
							normalmixture.getMean(indexOrdering[i]), 
							Math.sqrt(normalmixture.getVariance(indexOrdering[i])));
				
				mixtureDistributionObject = new edu.ucla.stat.SOCR.distributions.MixtureDistribution(
						mixureComponents, normalmixture.getWeights());
				
				int numberOfQuantiles = 100;
				double [] temp = new double [rawDat.length];
				double[] x = new double[numberOfQuantiles];
				double[] y = new double[numberOfQuantiles];
				
				//	 Model Quantiles
				for (int i = 0; i < numberOfQuantiles; i++) {
					y[i] = mixtureDistributionObject.getQuantile((i+0.5)/numberOfQuantiles);
					//System.out.println("\nY = "+y[i]);
				}
				//	 Data Quantiles
				for (int i= 0; i<rawDat.length; i++) 
					temp[i] = (double)rawDat[i];
				x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
				
				KS_InputData.appendX("X", x, DataType.QUANTITATIVE);
				KS_InputData.appendY("Y", y, DataType.QUANTITATIVE);
				KolmogorovSmirnoffResult result = null;
				
				try {
					result = (KolmogorovSmirnoffResult)KS_InputData.getAnalysis(AnalysisType.KOLMOGOROV_SMIRNOFF);
				} catch (Exception e) {;}
				     
				resultPanelTextArea.append("Kolmogorov-Smirnoff Test for Differences Between the Data "+
						"and Mixure-Model Distribution:\n" 
						+ "\n\tHypotheses: Ho: The Data follow the Model Distribution\n\t\t vs. "
						+"H1: Data and Model Distributions are distinct\n"
						+"\n\t KS D-Statistics = "+df.format(result.getDStat())+
						"\n\t Z-Statistics = " + df.format(result.getZStat())+
						"\n\t CDF(" + df.format(result.getDStat()) + ") = " + 
								df.format(result.getProb())+
						"\n\t P-value = " + df.format((1-result.getProb())));
				resultPanelTextArea.append("\n\nDetails about the Kolmogorov-Smirnoff Test are available here:\n");
				resultPanelTextArea.append("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysisActivities_KolmogorovSmirnoff \n");
			
			modelCt = normalmixture.getCount()+1; // why add 1?? and this var is not even used anywhere????
			curveFittingCount++;
			////System.out.println("MixedFit_Modeler fitCurve curveFittingCount = " + curveFittingCount);

		} catch(Exception e) {
			////System.out.println("MixedFit_Modeler fitCurve Exception = " + e.toString());
			e.printStackTrace();
		}

	}

    public String getDescription() {
        String desc = new String();
        desc = "See: http://repositories.cdlib.org/socr/EM_MM";
        return desc;
        }
        /** return the instructions for using this modeler*/
    	public String getInstructions() {
    		String instructions = new String();
    	    instructions = "See: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_ModelerActivities";
    	    return instructions;
    	}
    	/** return the references for this modeler*/
    	public String getResearch() {
    		String research = new String();
    	    research = "http://repositories.cdlib.org/socr/EM_MM \n"+
    	    	"http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_2D_PointSegmentation_EM_Mixture";
    	    return research;
    	}
    	public boolean isContinuous() {
		return this.isContinuous;
	}
	public double getGraphLowerLimit() {
		return ModelerConstant.GRAPH_LOWER_LIMIT;
	}
	public double getGraphUpperLimit() {
		return ModelerConstant.GRAPH_UPPER_LIMIT;
	}
	public boolean useInitButton() {
		return true;
	}

	public static void main(String[] args) {
		int mixCount = 4;
		float[] rawDat = new float[] {-3.5f,-3.5f,-3.5f,-3.5f,-1.5f,-1.5f,-1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,2.5f,2.5f,3.5f,3.5f,3.5f};
		normalMixture normalmixture = new normalMixture(mixCount,rawDat,(double)0.001);
		int iterations = 10;
		for(int i=0;i<iterations;i++) {
			normalmixture.updateEStep();
			normalmixture.updateMStep();
		}
		int number = 101;
		double[] modelX = new double[number*(1+normalmixture.getCount())];
		double[] modelY = new double[number*(1+normalmixture.getCount())];
		////System.out.println("MixedFit_Modeler fitCurve modelX = " + modelX);
		////System.out.println("MixedFit_Modeler fitCurve modelY = " + modelY);
		////System.out.println("MixedFit_Modeler normalmixture.getCount() = " + normalmixture.getCount());

		////System.out.println("MixedFit_Modeler modelX.length = " + modelX.length);
		////System.out.println("MixedFit_Modeler modelY.length = " + modelY.length);
		double minx = -10;
		double maxx = 10;
		double ind = (maxx-minx)/(number - 1);
		////System.out.println("MixedFit_Modeler minx = " + minx);
		////System.out.println("MixedFit_Modeler maxx = " + maxx);
		////System.out.println("MixedFit_Modeler ind = " + ind);

		for(int i = 0; i < number; i++) {
			modelX[i] = minx + ind * i;
			modelY[i] = normalmixture.getMixDensity(modelX[i]);
			////System.out.println("MixedFit_Modeler modelX["+i+"] = " + modelX[i] + " modelY["+i+"] = " + modelY[i]);

		}

	}
}
