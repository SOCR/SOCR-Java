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

import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.util.ObservableWrapper;

public class VonMisesFit_Modeler extends Modeler implements ItemListener{
	
	public edu.ucla.stat.SOCR.distributions.VonMisesDistribution VonMisesDistr;	

	private double[] modelX;
	private double[] modelY;	
	private int modelCt = 1;
	private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;	
	private static boolean isContinuous = true;
	public static final int SLICE_SIZE = 4001;
	private int numberOfQuantiles=100;

	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);

	public JLabel locationParamLabel = new JLabel("Location Parameter");
	public JTextField locationParamField = new JTextField("1.0",3);
	public JLabel concentrationParamLabel = new JLabel("Concentration Parameter");
	public JTextField concentrationParamField = new JTextField("1.0",3);
	
	private ButtonGroup buttonGroup = new ButtonGroup();

	//default constructor
	public VonMisesFit_Modeler(){
	}
	
	//constructor
	public VonMisesFit_Modeler(JPanel controlpanel) {
		controlpanel.add(estimateParams);
		controlpanel.add(userParams);
		buttonGroup.add(estimateParams);
		buttonGroup.add(userParams);
		estimateParams.addItemListener(this);
		userParams.addItemListener(this);
		addParams(controlpanel);
		controlpanel.repaint();	
	}
	
	
	public void addParams(JPanel controlpanel){
		controlpanel.add(locationParamLabel);
		controlpanel.add(locationParamField);
		controlpanel.add(concentrationParamLabel);
		controlpanel.add(concentrationParamField);
	}

	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}
	
	
	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {

		try{
			if(estimateParams.isSelected()){
				double rawDat_d[] = new double[rawDat.length];
				//convert float[] to double[]				
				for(int i = 0; i < rawDat.length; i++){
					Float f = rawDat[i];
					rawDat_d[i] = f.doubleValue();
				}
				//VonMisesDistr = new edu.ucla.stat.SOCR.distributions.VonMisesDistribution(rawDat_d);
				VonMisesDistr.paramEstimate(rawDat_d);
				
			}
			else
				VonMisesDistr = new edu.ucla.stat.SOCR.distributions.VonMisesDistribution(Double.parseDouble(locationParamField.getText()),Double.parseDouble(concentrationParamField.getText()));

			resultPanelTextArea.setText("Location Parameter = "+ Double.toString(VonMisesDistr.getMean())+"\nConcentration Parameter = " + Double.toString(VonMisesDistr.getK())+"\n\n");

			float ind = 1;
			int number = SLICE_SIZE;
			ind = (maxx-minx)/ (number - 2);
			
			modelX = new double[number];
			modelY = new double[number];
			for(int i = 0; i<number ;i++) {
				modelX[i] = minx+ind*i;
				modelY[i] = VonMisesDistr.getDensity(modelX[i]);
			}
			
			
			double[] temp = new double [rawDat.length];
			double[] x = new double[numberOfQuantiles];
			double[] y = new double[numberOfQuantiles];
				//	 Model Quantiles
			for (int i = 0; i < numberOfQuantiles; i++)  y[i] = VonMisesDistr.getQuantile((i+0.5)/numberOfQuantiles);
				//	 Data Quantiles
			for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
			x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
			resultPanelTextArea.append(getKSModelTestString(VonMisesDistr.getName(), numberOfQuantiles, x, y));
			
			
		} catch(Exception e) {
			resultPanelTextArea.setText(e.getMessage());
		}
		
	}
	
	
	public double[] generateSamples(int sampleCount) {
		VonMisesDistr = new edu.ucla.stat.SOCR.distributions.VonMisesDistribution(Double.parseDouble(locationParamField.getText()) , Double.parseDouble(concentrationParamField.getText()));
		double[] dat = new double[sampleCount];
		
		for(int i = 0; i< sampleCount;i++)
			dat[i] = VonMisesDistr.simulate();
		return dat;
	}
	
	public String getDescription() {
		String desc = new String();
		desc = "URL: http://socr.ucla.edu/htmls/dist/VonMises_Distribution.html";
		return desc;
	}
	
	public double getGraphLowerLimit() {
		return ModelerConstant.GRAPH_LOWER_LIMIT;
	}
	
	public double getGraphUpperLimit() {
		return ModelerConstant.GRAPH_UPPER_LIMIT;
	}
	
	public String getInstructions() {
		String instructions = new String();
		instructions = "\tInstructions on using the modeler.";
		return instructions;
	}
	
	
    public double getLowerLimit() {
    	return Double.NEGATIVE_INFINITY;
    }
	
	public int getModelCount() {
		return modelCt;
	}
	
	public int getModelType() {
		return modelType;
	}
	
	
	public String getResearch() {
		String research = new String();
		research = "\tresearch field returned by the modeler.";
		return research;
	}
	
	
	public double getUpperLimit() {
		return Double.POSITIVE_INFINITY;
	}
	
	public boolean isContinuous() {
		return VonMisesFit_Modeler.isContinuous;
	}
	
	public void itemStateChanged(ItemEvent event){
		if(event.getSource() == userParams || event.getSource() == estimateParams) {
			if(estimateParams.isSelected())
				toggleParams(false);
			else
				toggleParams(true);
		}
	}
	
    public void registerObservers(ObservableWrapper o) {
    	o.addJCheckBox(estimateParams);
		o.addJCheckBox(userParams);
		o.addJTextField(locationParamField);
		o.addJTextField(concentrationParamField);
    }
	
	public double[] returnModelX() {
		return modelX;
	}

	public double[] returnModelY() {
		return modelY;
	}	
	
    public void toggleParams(boolean istrue) {
        if(istrue) {
            locationParamLabel.setVisible(true);
            locationParamField.setVisible(true);
            concentrationParamLabel.setVisible(true);
            concentrationParamField.setVisible(true);

        }else {
            locationParamLabel.setVisible(false);
            locationParamField.setVisible(false);
            concentrationParamLabel.setVisible(false);
            concentrationParamField.setVisible(false);
        }
    }
	
	
	public boolean useInitButton() {
		return false;
	}
	
}