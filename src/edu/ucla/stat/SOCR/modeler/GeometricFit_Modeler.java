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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import JSci.maths.DoubleVector;
import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.util.ObservableWrapper;

/**This class Designs a Geometric Fit model fitting curve. */
public class GeometricFit_Modeler extends Modeler implements ItemListener {
	private ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);
	public edu.ucla.stat.SOCR.distributions.GeometricDistribution GeometricDistr;
	public float kernalVar = 0;
	private double minX = 0;
	private double maxX = 0;
	private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;
	public JTextField probabilityParamField = new JTextField("0.5",3);
	public JLabel probabilityParamLabel = new JLabel("Probability");
	private double[] modelX;
	private double[] modelY;

	private int numberOfQuantiles=100;
	
	public DoubleVector coeffs;
	private int dataPts;
	public javax.swing.table.TableColumn clm2;
	private int runs = 0;
	public JTextField kernalVarField = new JTextField("1", 3);
	private JTextArea statsTable = new JTextArea(6, 25);
	private double mn = 0;
	private double sd = 1;
	private int modelCt=1;
	public JCheckBox rawCheck = new JCheckBox("Raw Data",false);
	private static boolean isContinuous = true;
	public static int SLICE_SIZE = 8001;

     /** 
      * This method initializes the applet, including the toolbar,
      * scatterplot, and statistics table.
      */
    public GeometricFit_Modeler() {
    }

    /** 
     * GeometricFit_Modeler Constructor.
     */
    public GeometricFit_Modeler(JPanel controlpanel) {
		controlpanel.add(estimateParams);
		controlpanel.add(userParams);
		buttonGroup.add(estimateParams);
		buttonGroup.add(userParams);
		estimateParams.addItemListener(this);
		userParams.addItemListener(this);
		addParams(controlpanel);
		controlpanel.repaint();
    }

    public void registerObservers(ObservableWrapper o) {
	    o.addJCheckBox(estimateParams);
	    o.addJCheckBox(userParams);
	    o.addJTextField(probabilityParamField);
    }

    public void addParams(JPanel controlpanel) {
    	controlpanel.add(probabilityParamLabel);
        controlpanel.add(probabilityParamField);
    }

    public int getModelType() {
        return modelType;
    }

	public double getLowerLimit() {
		return Double.NEGATIVE_INFINITY;
	}

	public double getUpperLimit() {
		return Double.POSITIVE_INFINITY;
	}

	public double[] returnModelX() {
		return modelX;
	}

	public double[] returnModelY() {
		return modelY;
	}
     public void toggleParams(boolean istrue) {
        if(istrue) {
        	probabilityParamLabel.setVisible(true);
            probabilityParamField.setVisible(true);
        } else {
        	probabilityParamLabel.setVisible(false);
        	probabilityParamField.setVisible(false);
        }
    }

    public void itemStateChanged(ItemEvent event){
         if(event.getSource() == userParams || event.getSource() == estimateParams) {
          if(estimateParams.isSelected())
             toggleParams(false);
          else
              toggleParams(true);
        }
    }

	public double[] generateSamples( int sampleCount) {
		double[] dat = new double[sampleCount];
		GeometricDistr = new edu.ucla.stat.SOCR.distributions.GeometricDistribution(
				Double.parseDouble(probabilityParamField.getText()));
		for(int i = 0; i< sampleCount;i++)
			dat[i] = GeometricDistr.simulate();
		return dat;
	}
	
	public int getModelCount() {
		return modelCt;
	}
	
	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}

	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		int datalength = 0;
		try{
			int count = 0;

			if(estimateParams.isSelected())
				GeometricDistr = new edu.ucla.stat.SOCR.distributions.GeometricDistribution(rawDat);
			else
				GeometricDistr = new edu.ucla.stat.SOCR.distributions.GeometricDistribution(
						Double.parseDouble(probabilityParamField.getText()));
			
			double _probability = GeometricDistr.getProbability();
			String probabilityString = Double.toString(_probability);

			resultPanelTextArea.setText("probability = "+ probabilityString+"\n\n");
			float ind = 1;
			int number = SLICE_SIZE; // number of delta x. was 80
			ind = (maxx-minx)/(number - 2);

			modelX = new double[number];
			modelY = new double[number];
			for(int i = 0; i<number ;i++) {
				modelX[i] = minx+ind*i;
				modelY[i] = GeometricDistr.getDensity(modelX[i]);
			}
			
			
			double[] temp = new double [rawDat.length];
			double[] x = new double[numberOfQuantiles];
			double[] y = new double[numberOfQuantiles];
				//	 Model Quantiles
			for (int i = 0; i < numberOfQuantiles; i++)  y[i] = GeometricDistr.getQuantile((i+0.5)/numberOfQuantiles);
				//	 Data Quantiles
			for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
			x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
			resultPanelTextArea.append(getKSModelTestString(GeometricDistr.getName(), numberOfQuantiles, x, y));

			
		} catch(Exception e) {
			e.printStackTrace();
			resultPanelTextArea.setText(e.getMessage());
		}
	}

	public String getDescription() {
		String desc = new String();
		desc = "\t Description field returned by the Geometric modeler.";
		return desc;
	}
	
    /** Returns the instructions for using this modeler*/
    public String getInstructions() {
    	    String instructions = new String();
    	    instructions = "\tInstructions on using the Geometric modeler.";
    	    return instructions;
    }
    
    /** Returns the references for this modeler*/
    public String getResearch() {
    	    String research = new String();
    	    research = "\t Research field returned by the Geometric modeler.";
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
		return false;
	}
	
	public void setSliceSize(int size) {
		SLICE_SIZE = size;
	}
}
