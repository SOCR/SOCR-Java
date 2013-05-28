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

//import edu.ucla.stat.SOCR.distributions.*;
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
//import JSci.maths.statistics.*;
//import edu.ucla.stat.SOCR.gui.*;
/**This class Designs a Poisson model fitting curve. */
public class PoissonFit_Modeler extends Modeler implements ItemListener {

	//  public JSci.maths.statistics.NormalDistribution stdNorm;
	public edu.ucla.stat.SOCR.distributions.PoissonDistribution dPoisson;

	public JTextField paramField = new JTextField("1.0",3);
	public JLabel paramLabel = new JLabel("Parameter");
	public float kernalVar = 0;
	//private double minX = 0;
	//private double maxX = 0;
	public double data[][];
	//  public double modelX[], modelY[];
	private int numberOfQuantiles=100;
	
	
	public DoubleVector coeffs;
	private int dataPts;
	public javax.swing.table.TableColumn clm2;
	private int runs = 0;
	//  public JTextField kernalVarField = new JTextField("1", 3);
	private JTextArea statsTable = new JTextArea(6, 25);
	private double mn = 0;
	private double sd = 1;
	public JCheckBox rawCheck = new JCheckBox("Raw Data",false);
	private double[] modelX;
	private double[] modelY;
	private ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);
	private int modelType = DISCRETE_DISTRIBUTION_TYPE;
	private int modelCt = 1;
	private static boolean isContinuous = false;
	public static final int SLICE_SIZE = 4001;
	public JLabel shiftParamLabel = new JLabel("Shift");
	public JTextField shiftParamField = new JTextField("0.0",3);
	private boolean estimateParameters = false;

	public PoissonFit_Modeler() {
	}

	public PoissonFit_Modeler(JPanel controlpanel) {
		controlpanel.add(estimateParams);
		controlpanel.add(userParams);
		//SOCRModeler.addToGridBag(controlpanel, estimateParams, 0, 0, 1, 1, 1.0, 0.0);
		// SOCRModeler.addToGridBag(controlpanel, userParams, 0, 1, 1, 1, 1.0, 0.0);
		addParams(controlpanel);
		buttonGroup.add(estimateParams);
		buttonGroup.add(userParams);
		estimateParams.addItemListener(this);
		userParams.addItemListener(this);
		controlpanel.repaint();
	}

	public double getLowerLimit() {
		return 0;
	}

	public double getUpperLimit() {
		return Double.POSITIVE_INFINITY;
	}

	public void addParams(JPanel controlpanel) {
		controlpanel.add(paramLabel);
		controlpanel.add(paramField);
		controlpanel.add(shiftParamLabel);
		controlpanel.add(shiftParamField);
	}

	public void registerObservers(ObservableWrapper o) {
		o.addJCheckBox(estimateParams);
		o.addJCheckBox(userParams);
		o.addJTextField(paramField);
		o.addJTextField(shiftParamField);

	}

	public void toggleParams(boolean istrue) {
		if(istrue) {

			paramLabel.setVisible(true);
			paramField.setVisible(true);
			shiftParamLabel.setVisible(true);
			shiftParamField.setVisible(true);

		}else{
			paramLabel.setVisible(false);
			paramField.setVisible(false);
			shiftParamLabel.setVisible(false);
			shiftParamField.setVisible(false);

		}
	}

	public int getModelType() {
		return modelType;
	}

	public double[] generateSamples( int sampleCount) {
		double[] dat = new double[sampleCount];
		double lambda = Double.parseDouble(paramField.getText());
		int shift = (int)(Math.floor(Double.parseDouble(shiftParamField.getText())));
		dPoisson = new edu.ucla.stat.SOCR.distributions.PoissonDistribution(shift, lambda);

		for(int i = 0; i< sampleCount;i++)
			dat[i] = dPoisson.simulate();

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
		//System.out.println("PoissonFit_Modeler fitCurve started");
		//for (int i = 0; i < rawDat.length; i++) {
			////System.out.println("PoissonFit_Modeler fitCurve rawDat["+i+ "] = " + rawDat[i]);
		//}
		//graph.clear();
		double[] betaDat;
		int datalength = 0;
		try{
			//  resultPanelTextArea.setText("Mean = "+ Double.toString(stdNorm.getMean())+"\n variance = " + Double.toString(stdNorm.getVariance())  );
			////////System.out.println("before poisson");
			if(estimateParams.isSelected()) {
				//System.out.println("PoissonFit_Modeler estimateParams.isSelected");
				dPoisson = new edu.ucla.stat.SOCR.distributions.PoissonDistribution(rawDat);
				//System.out.println("PoissonFit_Modeler Lambda = " + Double.toString(dPoisson.getLambda()));
				//System.out.println("PoissonFit_Modeler Shift = " + Double.toString(dPoisson.getShift()));

			}
			else {
				//System.out.println("PoissonFit_Modeler fitCurve else shiftParamField.getText() = " + shiftParamField.getText());
				int tempShift = (int)(Math.floor(Double.parseDouble(shiftParamField.getText())));
				dPoisson = new edu.ucla.stat.SOCR.distributions.PoissonDistribution(
						tempShift, Double.parseDouble(paramField.getText())-tempShift);
			}

			resultPanelTextArea.setText("\nLambda = "+ Double.toString(dPoisson.getLambda()) + "\nShift = "+ Integer.toString(dPoisson.getShift())+"\n\n");

			double shift = dPoisson.getShift();
			float ind = 1;
			//minx = (int)ModelerConstant.GRAPH_LOWER_LIMIT;//
			minx = (int) Math.floor(shift);//(float)dPoisson.getLeftLimit();
			////System.out.println("PoissonFit_Modeler fitCurve minx = " + minx);

			maxx = (int)ModelerConstant.GRAPH_UPPER_LIMIT;//(float) Double.POSITIVE_INFINITY;//(float)dPoisson.getRightLimit(); //graph.getMaxX();
			int number = SLICE_SIZE; // number of delta x. was 41
			ind = (maxx-minx)/(number - 2);

			//System.out.println("PoissonFit_Modeler fitCurve minx = " + minx + " maxx = " + maxx + " ind + " + ind);

			modelX = new double[number];
			modelY = new double[number];

			modelX[0] = minx;
			modelX[number - 1] = maxx;
			modelY[0] = 0;
			modelY[number - 1] = 0;

			modelX[1] = minx;
			modelX[number - 2] = maxx;
			modelY[1] = dPoisson.getDensity(minx);
			modelY[number - 2] = dPoisson.getDensity(maxx);

			for(int i = 2; i < number-2; i++) { // was i < 41.
				modelX[i] = minx+ind*i;
				modelY[i] = dPoisson.getDensity(modelX[i]);
			}
			
			
			double[] temp = new double [rawDat.length];
			double[] x = new double[numberOfQuantiles];
			double[] y = new double[numberOfQuantiles];
				//	 Model Quantiles
			for (int i = 0; i < numberOfQuantiles; i++)  y[i] = dPoisson.getQuantile((i+0.5)/numberOfQuantiles);
				//	 Data Quantiles
			for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
			x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
			resultPanelTextArea.append(getKSModelTestString(dPoisson.getName(), numberOfQuantiles, x, y));
			
		} catch(Exception e) {
			//resultPanelTextArea.setText("");
			e.printStackTrace();
			//System.out.println(e.getMessage());
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
		instructions = "\tInstructions on using the Poisson modeler.";
		return instructions;
	}
	/** return the references for this modeler*/
	public String getResearch() {
		String research = new String();
		research = "none";
		return research;
	}
	public boolean isContinuous() {
		return this.isContinuous;
	}
	public double getGraphLowerLimit() {
		return 0;
	}
	public double getGraphUpperLimit() {
		return ModelerConstant.GRAPH_UPPER_LIMIT;
	}
	public boolean useInitButton() {
		return false;
	}
}
