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

/**This class Designs a Normal Fit model fitting curve. */
public class NormalFit_Modeler extends Modeler implements ItemListener {
	private ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);
	public edu.ucla.stat.SOCR.distributions.NormalDistribution stdNorm;
	public float kernalVar = 0;
	private double minX = 0;
	private double maxX = 0;
	private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;
	public JTextField meanParamField = new JTextField("1.0",3);
	public JLabel meanParamLabel = new JLabel("Mean");
	public JTextField stdevParamField = new JTextField("1.0",3);
	public JLabel stdevParamLabel = new JLabel("Std. Dev.");
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
    public NormalFit_Modeler() {
    }

    /** 
     * NormalFit_Modeler Constructor.
     */
    public NormalFit_Modeler(JPanel controlpanel) {
		controlpanel.add(estimateParams);
		controlpanel.add(userParams);
		//SOCRModeler.addToGridBag(controlpanel, estimateParams, 0, 0, 1, 1, 1.0, 0.0);
		//SOCRModeler.addToGridBag(controlpanel, userParams, 0, 1, 1, 1, 1.0, 0.0);
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
	    o.addJTextField(meanParamField);
	    o.addJTextField(stdevParamField);
    }

    public void addParams(JPanel controlpanel) {

        controlpanel.add(meanParamLabel);
        controlpanel.add(meanParamField);
        controlpanel.add(stdevParamLabel);
        controlpanel.add(stdevParamField);
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
            meanParamLabel.setVisible(true);
            meanParamField.setVisible(true);
            stdevParamLabel.setVisible(true);
            stdevParamField.setVisible(true);
        } else {
            meanParamLabel.setVisible(false);
            meanParamField.setVisible(false);
            stdevParamLabel.setVisible(false);
            stdevParamField.setVisible(false);
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
		stdNorm = new edu.ucla.stat.SOCR.distributions.NormalDistribution(Double.parseDouble(meanParamField.getText()),Double.parseDouble(stdevParamField.getText()));
		for(int i = 0; i< sampleCount;i++)
			dat[i] = stdNorm.simulate();
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
		double[] Normaldat;
		int datalength = 0;
		try{
			int count = 0;

			if(estimateParams.isSelected())
				stdNorm = new edu.ucla.stat.SOCR.distributions.NormalDistribution(rawDat);
			else
				stdNorm = new edu.ucla.stat.SOCR.distributions.NormalDistribution(Double.parseDouble(meanParamField.getText()),Double.parseDouble(stdevParamField.getText()));

			String meanString = Double.toString(stdNorm.getMean());
			String varString = Double.toString(stdNorm.getVariance());
			resultPanelTextArea.setText("Mean = "+ meanString+"\nvariance = " + varString+"\n\n");
			float ind = 1;
			int number = SLICE_SIZE; // number of delta x. was 80
			ind = (maxx-minx)/(number - 2);

			modelX = new double[number];
			modelY = new double[number];
			for(int i = 0; i<number ;i++) {
				modelX[i] = minx+ind*i;
				modelY[i] = stdNorm.getDensity(modelX[i]);
			}
			double maxy = modelY[0];
			for(int i = 0; i<modelY.length;i++){
				maxy = modelY[i];
			}

			double[] temp = new double [rawDat.length];
			double[] x = new double[numberOfQuantiles];
			double[] y = new double[numberOfQuantiles];
				//	 Model Quantiles
			for (int i = 0; i < numberOfQuantiles; i++)  y[i] = stdNorm.getQuantile((i+0.5)/numberOfQuantiles);
				//	 Data Quantiles
			for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
			x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
			resultPanelTextArea.append(getKSModelTestString(stdNorm.getName(), numberOfQuantiles, x, y));

			
			
		} catch(Exception e) {
			e.printStackTrace();
			resultPanelTextArea.setText(e.getMessage());
		}
	}

	/* fitCurve for Normal Model
	 */
	public void fitCurve(float[] rawDat, float minx, float maxx, double mu0, double sigma, boolean plotBlocks, boolean scaleUp) {
		double[] Normaldat;
		int datalength = 0;
		try{
			int count = 0;
			plotBlocks = false;
			if(plotBlocks) stdNorm = new edu.ucla.stat.SOCR.distributions.NormalDistribution(rawDat);
			else stdNorm = new edu.ucla.stat.SOCR.distributions.NormalDistribution(mu0, sigma);

			String meanString = Double.toString(stdNorm.getMean());
			String varString = Double.toString(stdNorm.getVariance());

			float ind = 1;
			int number = SLICE_SIZE; // number of delta x. was 80
			ind = (maxx-minx)/(number - 2);

			modelX = new double[number];
			modelY = new double[number];
			for(int i = 0; i<number ;i++) {
				modelX[i] = minx+ind*i;
				modelY[i] =  stdNorm.getDensity(modelX[i]);
			}

			double maxy = modelY[0];
			for(int i = 0; i<modelY.length;i++){
				maxy = modelY[i];
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String getDescription() {
		String desc = new String();
		desc = "\t Description field returned by the Normal modeler.";
		return desc;
	}
	
    /** Returns the instructions for using this modeler*/
    public String getInstructions() {
    	    String instructions = new String();
    	    instructions = "tInstructions on using the normal modeler.";
    	    return instructions;
    }
    
    /** Returns the references for this modeler*/
    public String getResearch() {
    	    String research = new String();
    	    research = "\t Research field returned by the Normal modeler.";
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
