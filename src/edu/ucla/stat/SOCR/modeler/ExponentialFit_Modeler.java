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
import edu.ucla.stat.SOCR.util.ObservableWrapper;
import java.awt.event.*;
import edu.ucla.stat.SOCR.core.*;
import javax.swing.*;

/**This class Designs a simple polynomial model fitting curve. */
public class ExponentialFit_Modeler extends Modeler implements ItemListener {
	public edu.ucla.stat.SOCR.distributions.ExponentialDistribution stdExponential;
	public float kernalVar = 0;
	private double minX = 0;
	private double maxX = 0;
	private double[] modelX;
	private double[] modelY;
	private ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);
	private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;
	private int modelCt=1;
	public JTextField rateParamField = new JTextField("1.0",3);
	public JLabel rateParamLabel = new JLabel("Rate");
	public JTextField shiftParamField = new JTextField("0.0",3);
	public JLabel shiftParamLabel = new JLabel("Shift");
	private int numberOfQuantiles=100;

	public static final int SLICE_SIZE = 4001;

	// public DoubleVector coeffs;
	private int dataPts;
	public javax.swing.table.TableColumn clm2;
	private int runs = 0;
	public JTextField kernalVarField = new JTextField("1", 3);
	private JTextArea statsTable = new JTextArea(6, 25);
	private double mn = 0;
	private double sd = 1;
	//  public JCheckBox rawCheck = new JCheckBox("Raw Data",false);
	/**This method initializes the applet, including the toolbar,
	* scatterplot, and statistics table.*/

    public ExponentialFit_Modeler() {
    }


	public ExponentialFit_Modeler(JPanel controlpanel) {
		controlpanel.add(estimateParams);
		controlpanel.add(userParams);
		addParams(controlpanel);
		buttonGroup.add(estimateParams);
		buttonGroup.add(userParams);
		estimateParams.addItemListener(this);
		userParams.addItemListener(this);
		controlpanel.repaint();
	}


	public double getLowerLimit() {
		return Double.NEGATIVE_INFINITY;
	}

	public double getUpperLimit() {
		return Double.POSITIVE_INFINITY;
	}


    /**This method resets the experiment, including the scatterplot,
     * the record table, and the statistics table.*/
	public void addParams(JPanel controlpanel) {
		controlpanel.add(rateParamLabel);
		controlpanel.add(rateParamField);
		controlpanel.add(shiftParamLabel);
		controlpanel.add(shiftParamField);
	}

	public void registerObservers(ObservableWrapper o) {
		o.addJCheckBox(estimateParams);
		o.addJCheckBox(userParams);
		o.addJTextField(rateParamField);
		o.addJTextField(shiftParamField);

	}

	public int getModelType() {
		return modelType;
	}

     public void toggleParams(boolean istrue) {
        if(istrue) {
			rateParamLabel.setVisible(true);
			rateParamField.setVisible(true);
			shiftParamLabel.setVisible(true);
			shiftParamField.setVisible(true);

        }else {
			rateParamLabel.setVisible(false);
			rateParamField.setVisible(false);
			shiftParamLabel.setVisible(false);
			shiftParamField.setVisible(false);

 		}
    }

	public double[] generateSamples( int sampleCount) {
		double[] dat = new double[sampleCount];
		stdExponential = new edu.ucla.stat.SOCR.distributions.ExponentialDistribution(Double.parseDouble(rateParamField.getText()) , Double.parseDouble(shiftParamField.getText()));
		for(int i = 0; i< sampleCount;i++) {
			dat[i] = stdExponential.simulate();
		}
		return dat;
	}
	public double[] returnModelX() {
		return modelX;
	}

	public double[] returnModelY() {
		return modelY;
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
	public int getModelCount() {
		return modelCt;
	}
	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}

	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		double[] Exponentialdat;
		int datalength = 0;
		for (int i = 0; i < rawDat.length; i++) {
		}
		try{
			int count = 0;

			if(estimateParams.isSelected()) {
				stdExponential = new edu.ucla.stat.SOCR.distributions.ExponentialDistribution(rawDat);
			} else {
				stdExponential = new edu.ucla.stat.SOCR.distributions.ExponentialDistribution(Double.parseDouble(rateParamField.getText()), Double.parseDouble(shiftParamField.getText()));
			}
			double rateInverse = 1/stdExponential.getRate();
			double resultShift = stdExponential.getShift();

            	resultPanelTextArea.setText("\nRate = "+ Double.toString(stdExponential.getRate()) + "\nMean = "+ rateInverse + "\nShift = " + resultShift+"\n\n");
			float ind = 1;
			int number = SLICE_SIZE; // number of delta x. was 21

			ind = (maxx-minx)/ (number - 1);

			modelX = new double[number];
			modelY = new double[number];

			for(int i = 0; i<number ;i++) {
				modelX[i] = minx+ind*i;
				modelY[i] = stdExponential.getDensity(modelX[i]);
				try { // this is just to make the plot look better. annie che. 200606.
					if (modelY[i-1] == 0 && modelY[i] != 0) {
						modelX[i-1] = modelX[i];
					}
				} catch (Exception e) {
					// index out of bound, and we don't care if exception occurs.
				}
				//if (i < (int) (SLICE_SIZE/100))
				//	////////System.out.println("ExponentialFit_Modeler modelX["+i+"] = " + modelX[i] + " modelY["+i+"] = " + modelY[i]);

			}
			double maxy = modelY[0];
			for(int i = 0; i<modelY.length;i++){
				if(modelY[i]>maxy)
					maxy = modelY[i];
			}
			
			double[] temp = new double [rawDat.length];
			double[] x = new double[numberOfQuantiles];
			double[] y = new double[numberOfQuantiles];
				//	 Model Quantiles
			for (int i = 0; i < numberOfQuantiles; i++)  y[i] = stdExponential.getQuantile((i+0.5)/numberOfQuantiles);
				//	 Data Quantiles
			for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
			x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
			resultPanelTextArea.append(getKSModelTestString(stdExponential.getName(), numberOfQuantiles, x, y));

			
		} catch(Exception e) {
			e.printStackTrace();
			resultPanelTextArea.setText(e.getMessage());
		}
	}

	public String getDescription() {
		String desc = new String();
		desc = "none";
		return desc;
	}
	/** return the instructions for using this modeler*/
    	public String getInstructions() {
    		String instructions = new String();
    	    instructions = "1. none\n 2. none";
    	    return instructions;
    	}
    	/** return the references for this modeler*/
    	public String getResearch() {
    		String research = new String();
    	    research = "none";
    	    return research;
    	}
    	public boolean isContinuous() {
		return GammaFit_Modeler.isContinuous;
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




