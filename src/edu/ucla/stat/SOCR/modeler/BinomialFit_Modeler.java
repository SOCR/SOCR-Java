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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.util.ObservableWrapper;

//import edu.ucla.stat.SOCR.gui.*;

/**This class Designs a Binomial model fitting curve. */
public class BinomialFit_Modeler extends Modeler implements ItemListener{
	// Variables for fitcurve
	public edu.ucla.stat.SOCR.distributions.BinomialDistribution dBinomial;
	private double minX = 0;
	private double maxX = 0;
	private double[] modelX;
	private double[] modelY;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private int modelCt = 1;
	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);

	public JTextField leftParamField = new JTextField("2",3);
	public JLabel leftParamLabel = new JLabel("Sample Size (n)");
	public JTextField rightParamField = new JTextField("0.5",3);
	public JLabel rightParamLabel = new JLabel("Probability (p)");

	public float kernalVar = 0;
	public JTextField kernalVarField = new JTextField("1", 3);
	private int modelType = DISCRETE_DISTRIBUTION_TYPE;
	private static boolean isContinuous = false;
	public static final int SLICE_SIZE = 4001;
	private int numberOfQuantiles=100;
	
    public BinomialFit_Modeler() {
    }

    public BinomialFit_Modeler(JPanel controlpanel) {
		buttonGroup.add(estimateParams);
		buttonGroup.add(userParams);
		estimateParams.addItemListener(this);
		userParams.addItemListener(this);
		controlpanel.add(estimateParams);
		controlpanel.add(userParams);
		addParams(controlpanel);
		controlpanel.repaint();
    }

	public double getLowerLimit() {
		return 0;
	}

	public double getUpperLimit() {
		return Double.POSITIVE_INFINITY;
	}

	public void addParams(JPanel controlpanel) {
		controlpanel.add(leftParamLabel);
		controlpanel.add(leftParamField);
		controlpanel.add(rightParamLabel);
		controlpanel.add(rightParamField);
    }

      public void registerObservers(ObservableWrapper o) {
		   o.addJCheckBox(estimateParams);
		   o.addJCheckBox(userParams);
		   o.addJTextField(leftParamField);
		   o.addJTextField(rightParamField);
        }

    public void toggleParams(boolean istrue) {
        if(istrue) {

            leftParamLabel.setVisible(true);
            leftParamField.setVisible(true);
            rightParamLabel.setVisible(true);
            rightParamField.setVisible(true);
        }else {
            leftParamLabel.setVisible(false);
            leftParamField.setVisible(false);
            rightParamLabel.setVisible(false);
            rightParamField.setVisible(false);
        }
    }

	public int getModelType() {
		return modelType;
	}

	public double[] returnModelX() {
		return modelX;
	}

	public double[] returnModelY() {
		return modelY;
	}

	public int getModelCount() {
		return modelCt;
	}

	public double[] generateSamples(int sampleCount) {
		double[] dat = new double[sampleCount];
		dBinomial = new edu.ucla.stat.SOCR.distributions.BinomialDistribution(
				Integer.parseInt(leftParamField.getText()),
				Double.parseDouble(rightParamField.getText()));

		for(int i = 0; i< sampleCount;i++)
			dat[i] = dBinomial.simulate();
		return dat;
	}

	public void itemStateChanged(ItemEvent event){
		if(event.getSource() == userParams || event.getSource() == estimateParams) {
			if(estimateParams.isSelected())
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

		try{
			if(estimateParams.isSelected()) {
				dBinomial = new edu.ucla.stat.SOCR.distributions.BinomialDistribution(rawDat);
			}
			else {
				dBinomial = new edu.ucla.stat.SOCR.distributions.BinomialDistribution(
						Integer.parseInt(leftParamField.getText()), 
						Double.parseDouble(rightParamField.getText()));
			}

			resultPanelTextArea.setText("\nSample-Size = "+ 
					Integer.toString(dBinomial.getTrials()) + 
					"\nProbability = "+ Double.toString(dBinomial.getProbability())+"\n\n");

			float ind = 1;
			minx = 0;//(float)dBinomial.getLeftLimit();
			maxx = dBinomial.getTrials();
			int number = SLICE_SIZE; // number of delta x. was 41
			ind = (maxx-minx)/(number - 2);

			modelX = new double[number];
			modelY = new double[number];

			modelX[0] = minx;
			modelX[number - 1] = maxx;
			modelY[0] = 0;
			modelY[number - 1] = 0;

			modelX[1] = minx;
			modelX[number - 2] = maxx;
			modelY[1] = dBinomial.getDensity(minx);
			modelY[number - 2] = dBinomial.getDensity(maxx);

			for(int i = 2; i < number-2; i++) { // was i < 41.
				modelX[i] = minx+ind*i;
				modelY[i] = dBinomial.getDensity(modelX[i]);
			}
			
			double[] temp = new double [rawDat.length];
			double[] x = new double[numberOfQuantiles];
			double[] y = new double[numberOfQuantiles];
				//	 Model Quantiles
			for (int i = 0; i < numberOfQuantiles; i++)  y[i] = dBinomial.getQuantile((i+0.5)/numberOfQuantiles);
				//	 Data Quantiles
			for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
			x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
			resultPanelTextArea.append(getKSModelTestString(dBinomial.getName(), numberOfQuantiles, x, y));

			
			
			
		} catch(Exception e) {
			//resultPanelTextArea.setText("");
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
	}

    /** return the description for this modeler -- like tutorial/instriction for users. */
    public String getDescription() {
        String desc = new String();
        desc = "\tURL: http://socr.ucla.edu/htmls/dist/Binomial_Distribution.html";
        return desc;
        }
	/** return the instructions for using this modeler*/
	public String getInstructions() {
		String instructions = new String();
		//instructions = "\t1. Instructions on using the Beta modeler.\n " + "\t2. Step 2.";
		instructions = "\tURL: http://en.wikipedia.org/wiki/Binomial_distribution.";
		return instructions;
	}
	/** return the references for this modeler*/
	public String getResearch() {
		String research = new String();
		research = "\t http://socr.ucla.edu/";
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