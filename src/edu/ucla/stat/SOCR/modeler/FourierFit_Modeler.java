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
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import JSci.maths.DoubleVector;
import edu.ucla.stat.SOCR.core.Game;
import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.util.ObservableWrapper;

//import JSci.maths.statistics.*;
//import edu.ucla.stat.SOCR.gui.*;
/**This class Designs a Fourier model fitting curve. */
//public class FourierFit_Modeler extends Game implements ModelerInterface, ItemListener {

public class FourierFit_Modeler extends Modeler implements ItemListener {

	Dimension winSize;
	Image dbimage;
	public JTextField paramField = new JTextField("5", 3);
	public JLabel paramLabel = new JLabel("number of terms");
	public float kernalVar = 0;
	private double minX = 0;
	private double maxX = 0;
	public double data[][];
	Random random;
	public int sampleCount = 720;
	public int halfSampleCount = sampleCount / 2;
	public double halfSampleCountFloat = sampleCount / 2;
	private static boolean isContinuous = true;


	public String getAppletInfo() {
		return "Fourier Series by Paul Falstad";
	}

	public DoubleVector coeffs;
	private int dataPts;
	public javax.swing.table.TableColumn clm2;
	private int runs = 0;
	private JTextArea statsTable = new JTextArea(6, 25);
	private double mn = 0;
	private double sd = 1;
	public JCheckBox rawCheck = new JCheckBox("Raw Data", true);
	private double[] modelX;
	private double[] modelY;
	private ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox estimateParams = new JCheckBox("Magnitude&Phase");
	
	private int modelType = FOURIER_TYPE;
	private int modelCt = 5;
	double magcoef[];
	double phasecoef[];
	static final double pi = 3.14159265358979323846;
	double step = 2 * pi / sampleCount;

	//double func[];
	float func[];
	float real[], imaginary[];
	int maxTerms = 160;
	int terms = 120;
	int selectedCoef;
	static final int SEL_NONE = 0;
	static final int SEL_FUNC = 1;
	static final int SEL_MAG = 2;
	static final int SEL_PHASE = 3;

	int selection;
	int dragX, dragY;
	boolean dragging;
	
	private Game game;

	int getrand(int x) {
		int q = random.nextInt();
		if (q < 0)
			q = -q;
		return q % x;
	}

	public FourierFit_Modeler() {
	}

	public FourierFit_Modeler(JPanel controlpanel) {

		controlpanel.add(estimateParams);
		addParams(controlpanel);
		controlpanel.add(estimateParams);
		//buttonGroup.add(userParams);
		estimateParams.addItemListener(this);
		//userParams.addItemListener(this);
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
	}

	public void registerObservers(ObservableWrapper o) {
		o.addJCheckBox(estimateParams);
		o.addJTextField(paramField);
	}

	public void toggleParams(boolean istrue) {
		if (istrue) {
			paramLabel.setVisible(true);
			paramField.setVisible(true);
		} else {
			paramLabel.setVisible(false);
			paramField.setVisible(false);
		}
	}

	public int getModelType() {
		return modelType;

	}

	public double[] generateSamples(int sampleCount) {
		double[] dat = new double[sampleCount];
		return dat;

	}

	public double[] returnModelX() {
		return modelX;
	}

	public double[] returnModelY() {
		return modelY;
	}

	public void itemStateChanged(ItemEvent event) {
	/*	if (event.getSource() == estimateParams) {
			if (estimateParams.isSelected())
				toggleParams(false);
			else
				toggleParams(true);
		}*/
	}

	public int getModelCount() {
		return modelCt;
	}

	public double getCDF(double x) {
		//updateGame();
		return x;
	}


	static int freqs[] = { 25, 32, 40, 50, 64, 80, 100, 125, 160, 200, 250,
			320, 400, 500, 800, 1000, 1600, 2000 };

	void transform() {
		int x, y;
		double epsilon = .00001;
		magcoef = new double[sampleCount];
		phasecoef = new double[sampleCount];
		double coef = 0;
		for (y = 0; y < terms; y++) {
			coef = 0;
			for (x = 0; x < sampleCount-1; x++) {
				int simp=1;
				double s = java.lang.Math.cos( step * (x) * y);
				real[y] += s*func[x] * simp;

			}
			double acoef = real[y]  / sampleCount;

			coef = 0;
			for (x = 0; x < sampleCount-1; x++) {
				int simp=1;
				double s = java.lang.Math.sin(step * (x) * y);
				imaginary[y] -= s * func[x] * simp;
			}
			double bcoef = imaginary[y]/ sampleCount;
			if (y == 0) {
                magcoef[0] = acoef / 2;
                phasecoef[0] = 0;
            } else {
            	magcoef[y] = java.lang.Math.sqrt(acoef * acoef + bcoef * bcoef);
            	phasecoef[y] = java.lang.Math.atan2(-bcoef, acoef);
            }
		}

	} // end transform.

	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}

	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {


		terms = (int) Double.parseDouble(paramField.getText());
		////System.out.println("FourierFit_Modeler fitCurve terms = " + terms);
		sampleCount = rawDat.length;
		halfSampleCount = sampleCount/2;
		////System.out.println("FourierFit_Modeler fitCurve sampleCount = " + sampleCount);
		////System.out.println("FourierFit_Modeler fitCurve halfSampleCount = " + halfSampleCount);

		func = new float[sampleCount+1];
		real = new float[terms];
		imaginary = new float[terms];
		for (int i = 0; i < rawDat.length; i++) {

			func[i] = rawDat[i];
			////System.out.println("FourierFit_Modeler fitCurve func["+i+"] = " + func[i]);

		}

		step = 2 * pi / sampleCount;
		try {
			transform();
		} catch (Exception e) {
			e.printStackTrace();
		}

		modelX = new double[sampleCount];
		modelY = new double[sampleCount];

		for ( int i = 0; i < terms; i++)
		{
			real[i] /= halfSampleCount;
			imaginary[i] /= -1 * halfSampleCount;
		}
		real[0] = real[0]/2;
		real[terms-1] = real[terms-1]/2;
		if ( !estimateParams.isSelected())
		{
			//////System.out.println("FourierFit_Modeler if !estimateParams.isSelected()");
			modelCt=5;
			for (int i = 0;i < terms; i++)
			{
				for (int j =0; j < sampleCount; j++)
				{
					modelY[j] += real[i]* java.lang.Math.cos(step * i * j);
					modelY[j] += imaginary[i]* java.lang.Math.sin(step * i * j);
				}

			}
			for (int i=0; i< sampleCount; i++)
				modelX[i] = func[i];
		}
		else
		// seems like this never happens -- there is no such selection in GUI ???
		{
			//////System.out.println("FourierFit_Modeler if !estimateParams.isSelected()");
			for (int i=0; i< terms; i++)
			{
				modelX[i] = magcoef[i];
				modelY[i] = phasecoef[i];
				////System.out.println("FourierFit_Modeler magcoef modelX["+i+"] = " + modelX[i] +" phasecoef modelY["+i+"] = " + modelY[i]);
			}

			modelCt = 8;
		}

		try {
				/* Calculating the standard error*/
				double diff, sum_sqrd_diff=0;
				for (int i = 0; i < sampleCount; i++)
				{
					diff = modelX[i]-modelY[i];
					sum_sqrd_diff += diff*diff;
				}
				resultPanelTextArea.setText("standard error = " + java.lang.Math.sqrt(sum_sqrd_diff)+"\n\n");
				////System.out.println("standard error = " + java.lang.Math.sqrt(sum_sqrd_diff));
				double magnitude = 0, phase = 0;
				for (int i = 0; i < terms; i++) {
					magnitude = java.lang.Math.sqrt(real[i] * real[i] + imaginary[i]*imaginary[i]);
					phase = java.lang.Math.atan2(imaginary[i],real[i]);
					resultPanelTextArea.append("Magnitude "+ i+ " = "+ Double.toString(magnitude)+"\nphase " + i +" = "+Double.toString(phase)+"\n\n" );
					////System.out.println("magnitude " + i + " " + magnitude);
					////System.out.println("phase " + i + " " + phase);
				}
		} catch (Exception e) {
			e.printStackTrace();
			//////System.out.println(e.getMessage());
		}
	}

	int getFreq() {
		return (int) (27.5 * java.lang.Math.exp(game.getValueSetter(1).getValue() * .04158883084));
	}

	int getPanelHeight() {
		return winSize.height / 3;
	}

	void centerString(Graphics g, String s, int y) {
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, (winSize.width - fm.stringWidth(s)) / 2, y);
	}


	int getTermWidth() {
		int terms = game.getValueSetter(0).getValueAsInt();
		int termWidth = winSize.width / terms;
		int maxTermWidth = winSize.width / 30;
		if (termWidth > maxTermWidth)
			termWidth = maxTermWidth;
		termWidth &= ~1;
		return termWidth;
	}

	public String getDescription() {
		String desc = new String();
		desc = "Implements the fourier fit";
		return desc;
	}

	/** return the instructions for using this modeler*/
	public String getInstructions() {
		String instructions = new String();
		instructions = "1. red line is the fitted curve\n 2. blue line is the user entered data";
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
