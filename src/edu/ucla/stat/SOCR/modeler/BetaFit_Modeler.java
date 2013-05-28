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
/* modified by annieche 20060514.
for: the upper and lower limits.
*/

package edu.ucla.stat.SOCR.modeler;

import edu.ucla.stat.SOCR.util.*;
import JSci.awt.*;
//import java.lang.*;
//import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import JSci.maths.*;
import JSci.maths.statistics.*;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.KolmogorovSmirnoffResult;
import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.modeler.*;
import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;

//import edu.ucla.stat.SOCR.gui.*;

/**This class Designs a Beta model fitting curve. */
public class BetaFit_Modeler extends Modeler implements ItemListener{
	// Variables for fitcurve
	public edu.ucla.stat.SOCR.distributions.BetaGeneralDistribution dBeta;
	private double minX = 0;
	private double maxX = 0;
	private double[] modelX;
	private double[] modelY;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private int modelCt = 1;
	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);

	public JTextField leftParamField = new JTextField("1.0",3);
	public JLabel leftParamLabel = new JLabel("(Alpha) Left Parameter");
	public JTextField rightParamField = new JTextField("1.0",3);
	public JLabel rightParamLabel = new JLabel("(Beta) Right Parameter");

	public JTextField leftLimitField = new JTextField("0.0",3);
	public JLabel leftLimitLabel = new JLabel("(Domain) Left Limit");
	public JTextField rightLimitField = new JTextField("1.0",3);
	public JLabel rightLimitLabel = new JLabel("(Domain) Right Limit");

	public float kernalVar = 0;
	// public DoubleVector coeffs;
	private int dataPts;
	private int runs = 0;
	public JTextField kernalVarField = new JTextField("1", 3);
	private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;
	private static boolean isContinuous = true;
	public static final int SLICE_SIZE = 4001;
	int numberOfQuantiles=100;
	
	// private JTextArea statsTable = new JTextArea(6, 25);
	//private double mn = 0;
	// private double sd = 1;
	// public JCheckBox rawCheck = new JCheckBox("Raw Data",false);

	/*  public void init(){
	//super.modelType = 1;
	// super.init();
	// rawCheck.addItemListener(this);
	//setName("SOCR Beta Fitting Model");
	//statsTable.setEditable(false);
	reset();
	} */

    public BetaFit_Modeler() {
    }

    public BetaFit_Modeler(JPanel controlpanel) {
		buttonGroup.add(estimateParams);
		buttonGroup.add(userParams);
		estimateParams.addItemListener(this);
		userParams.addItemListener(this);
		controlpanel.add(estimateParams);
		controlpanel.add(userParams);
		addParams(controlpanel);
		controlpanel.repaint();
    }

      public void addParams(JPanel controlpanel) {
		controlpanel.add(leftParamLabel);
		controlpanel.add(leftParamField);
		controlpanel.add(rightParamLabel);
		controlpanel.add(rightParamField);
		controlpanel.add(leftLimitLabel);
		controlpanel.add(leftLimitField);
		controlpanel.add(rightLimitLabel);
		controlpanel.add(rightLimitField);
    }

      public void registerObservers(ObservableWrapper o) {
		   o.addJCheckBox(estimateParams);
		   o.addJCheckBox(userParams);
		   o.addJTextField(leftParamField);
		   o.addJTextField(rightParamField);
		   o.addJTextField(leftLimitField);
		   o.addJTextField(rightLimitField);
        }

      public double getLowerLimit() {
      	//return Double.NEGATIVE_INFINITY;
      	return 0;
      }

      public double getUpperLimit() {
      	//return Double.POSITIVE_INFINITY;
      	return 1;
      }

    public void toggleParams(boolean istrue) {
        if(istrue) {

            leftParamLabel.setVisible(true);
            leftParamField.setVisible(true);
            rightParamLabel.setVisible(true);
            rightParamField.setVisible(true);
            leftLimitLabel.setVisible(true);
            leftLimitField.setVisible(true);
            rightLimitLabel.setVisible(true);
            rightLimitField.setVisible(true);
        }else {
            leftParamLabel.setVisible(false);
            leftParamField.setVisible(false);
            rightParamLabel.setVisible(false);
            rightParamField.setVisible(false);
            leftLimitLabel.setVisible(false);
            leftLimitField.setVisible(false);
            rightLimitLabel.setVisible(false);
            rightLimitField.setVisible(false);
        }
    }

	public double[] returnModelX() {
		return modelX;
	}

	public double[] returnModelY() {
		return modelY;
	}

	public int getModelCount() {
		////System.out.println("BetaFit_Modeler getModelCount modelCt = " + modelCt);
		return modelCt;
	}

	public double[] generateSamples(int sampleCount) {
		double[] dat = new double[sampleCount];
		dBeta = new edu.ucla.stat.SOCR.distributions.BetaGeneralDistribution(Double.parseDouble(leftParamField.getText()),Double.parseDouble(rightParamField.getText()),Double.parseDouble(leftLimitField.getText()),Double.parseDouble(rightLimitField.getText()));
		////System.out.println("BetaFit_Modeler generateSamples leftParamField = " + Double.parseDouble(leftParamField.getText()));
		////System.out.println("BetaFit_Modeler generateSamples rightParamField = " + Double.parseDouble(rightParamField.getText()));
		////System.out.println("BetaFit_Modeler generateSamples leftLimitField = " + Double.parseDouble(leftLimitField.getText()));
		////System.out.println("BetaFit_Modeler generateSamples rightLimitField = " + Double.parseDouble(rightLimitField.getText()));


		// JSci.maths.statistics.BetaDistribution rBeta = new JSci.maths.statistics.BetaDistribution(1, 0);
		// ////System.out.println("\n test beta  " + rBeta.cumulative(.6));
		for(int i = 0; i< sampleCount;i++)
			dat[i] = dBeta.simulate();
		return dat;
	}

	public void itemStateChanged(ItemEvent event){
		if(event.getSource() == userParams || event.getSource() == estimateParams) {
			if(estimateParams.isSelected())
				//estimateParams.
				//javax.swing.JCheckbox kjd = new JCheckBox();
				toggleParams(false);
			else
				toggleParams(true);

		}
	}

	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}

	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		double[] betaDat;
		int datalength = 0;
		try{

		if(estimateParams.isSelected())
			dBeta = new edu.ucla.stat.SOCR.distributions.BetaGeneralDistribution(rawDat);
		else
			dBeta = new edu.ucla.stat.SOCR.distributions.BetaGeneralDistribution(Double.parseDouble(leftParamField.getText()),Double.parseDouble(rightParamField.getText()),Double.parseDouble(leftLimitField.getText()),Double.parseDouble(rightLimitField.getText()));

		resultPanelTextArea.setText("Left Parameter = "+ Double.toString(dBeta.getLeft())+"\nRight Parameter = " + Double.toString(dBeta.getRight())  );

		//  float minx = 0;
		// float maxx = 1;
		////System.out.println("BetaFit_Modeler fitCurve dBeta left = " + leftLimitField.getText() + " right = " + rightLimitField.getText());

		float ind = 1;
		minx = (float)dBeta.getLeftLimit(); //graph.getMinX();
		maxx = (float)dBeta.getRightLimit(); //graph.getMaxX();
		int number = SLICE_SIZE; // number of delta x. was 41
		ind = (maxx-minx)/(number - 2);

		//////System.out.println("BetaFit_Modeler fitCurve minx = " + minx + " maxx = " + maxx + " ind + " + ind);

		modelX = new double[number];
		modelY = new double[number];

		modelX[0] = minx;
		modelX[number - 1] = maxx;
		modelY[0] = 0;
		modelY[number - 1] = 0;

		modelX[1] = minx;
		modelX[number - 2] = maxx;
		modelY[1] = dBeta.getDensity(minx);
		modelY[number - 2] = dBeta.getDensity(maxx);
		if (modelY[number - 1] == Double.POSITIVE_INFINITY) {
			modelY[number - 1] = 190;
		}
		if (modelY[number - 2] == Double.POSITIVE_INFINITY) {
			modelY[number - 2] = 190;
		}
		if  (dBeta.getLeft() == 1 &&  dBeta.getRight() == 1) {
			for(int i = 2; i<number - 2 ;i++) {
				modelX[i] = minx+ind*i;
				modelY[i] = 1/(maxx - minx); // uniform
			}
		}
		else {
			for(int i = 2; i<number - 1 ;i++) { // was i < 41.
				modelX[i] = minx+ind*i;
				modelY[i] = dBeta.getDensity(modelX[i]);
				//if (modelY[i] == Double.POSITIVE_INFINITY) {
				//	modelY[i] = 198;
			 	//}
				//////System.out.println("BetaFit_Modeler modelX["+i+"] = " + modelX[i] + " modelY["+i+"] = " + modelY[i]);
			}
		}
		resultPanelTextArea.append("\nLeft Limit = "+ Double.toString(dBeta.getLeftLimit())+
				"\nRight Limit = " + Double.toString(dBeta.getRightLimit()) + "\n");
		
		double[] temp = new double [rawDat.length];
		double[] x = new double[numberOfQuantiles];
		double[] y = new double[numberOfQuantiles];
			//	 Model Quantiles
		for (int i = 0; i < numberOfQuantiles; i++)  y[i] = dBeta.getQuantile((i+0.5)/numberOfQuantiles);
			//	 Data Quantiles
		for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
		x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
		resultPanelTextArea.append(getKSModelTestString(dBeta.getName(), numberOfQuantiles, x, y));
		
		/* double maxy = modelY[0];
		for(int i = 0; i<modelY.length;i++){
		if(modelY[i]>maxy)
		maxy = modelY[i];
		}*/
		// double dataMaxy = (double) graph.getMaxY();

		//graph.setYExtrema(0, (float)Math.max(maxy, dataMaxy));
		// graph.setModel(modelX.length, modelX, modelY);
		} catch(Exception e) {
			resultPanelTextArea.setText(e.getMessage());
		}
	}


	public int getModelType() {
		return modelType;
	}

    /** return the description for this modeler -- like tutorial/instriction for users. */
    public String getDescription() {
        String desc = new String();
        desc = "\tdescription field returned by the Beta modeler.";
        return desc;
        }
	/** return the instructions for using this modeler*/
	public String getInstructions() {
		String instructions = new String();
		//instructions = "\t1. Instructions on using the Beta modeler.\n " + "\t2. Step 2.";
		instructions = "\tInstructions on using the Beta modeler.";
		return instructions;
	}
	/** return the references for this modeler*/
	public String getResearch() {
		String research = new String();
		research = "\tresearch field returned by the Beta modeler.";
		return research;
	}


	/* type:
	1. continuous: does not have to use width = 1.
	2. discrete: have to use width = 1 (or whaever integer).
	2. mixture of continuous and discrete: use width = 1.
	*/
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
		return false;
	}


}




