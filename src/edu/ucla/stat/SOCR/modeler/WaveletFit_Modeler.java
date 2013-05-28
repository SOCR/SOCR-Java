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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import JSci.maths.DoubleVector;
import JSci.maths.wavelet.FWT;
import edu.ucla.stat.SOCR.core.Game;
import edu.ucla.stat.SOCR.modeler.Modeler;
import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.core.SOCRCodeBase;
import edu.ucla.stat.SOCR.core.SOCRJComboBox;
import edu.ucla.stat.SOCR.core.WaveletLoader;
import edu.ucla.stat.SOCR.util.ObservableWrapper;
import edu.ucla.stat.SOCR.util.topPercent;

//import JSci.maths.statistics.*;
//import edu.ucla.stat.SOCR.gui.*;
/**This class Designs a Fourier model fitting curve. */
//public class WaveletFit_Modeler extends Game implements ModelerInterface, ItemListener,
//		ActionListener, AdjustmentListener {

public class WaveletFit_Modeler extends Modeler implements ItemListener, ActionListener, AdjustmentListener {

	Dimension winSize;
	Image dbimage;
	FWT fwt;
	Random random;
	public JTextField paramField = new JTextField("40", 3);
	public JLabel paramLabel = new JLabel("% of terms");
	public static int sampleCount = 127;
	public static int halfSampleCount = sampleCount / 2;
	public static double halfSampleCountFloat = sampleCount / 2;
	JLabel termLabel;
	JScrollBar termBar;
     private static boolean isContinuous = true;

 	private Game game;
     
     
	public String getAppletInfo() {
		return "Wavelet Analysis Game by Ivo Dinov, Sam Prabhu Rubandhas and Dushyanth Krishnamurty";
	}



	public WaveletLoader waveletloader;

	topPercent tops;

	static final int to_ulaw[] = { 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3,
			3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8,
			9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13,
			13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 17, 17, 18, 18, 19,
			19, 20, 20, 21, 21, 22, 22, 23, 23, 24, 24, 25, 25, 26, 26, 27, 27,
			28, 28, 29, 29, 30, 30, 31, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
			41, 42, 43, 44, 45, 46, 47, 49, 51, 53, 55, 57, 59, 61, 63, 66, 70,
			74, 78, 84, 92, 104, 254, 231, 219, 211, 205, 201, 197, 193, 190,
			188, 186, 184, 182, 180, 178, 176, 175, 174, 173, 172, 171, 170,
			169, 168, 167, 166, 165, 164, 163, 162, 161, 160, 159, 159, 158,
			158, 157, 157, 156, 156, 155, 155, 154, 154, 153, 153, 152, 152,
			151, 151, 150, 150, 149, 149, 148, 148, 147, 147, 146, 146, 145,
			145, 144, 144, 143, 143, 143, 143, 142, 142, 142, 142, 141, 141,
			141, 141, 140, 140, 140, 140, 139, 139, 139, 139, 138, 138, 138,
			138, 137, 137, 137, 137, 136, 136, 136, 136, 135, 135, 135, 135,
			134, 134, 134, 134, 133, 133, 133, 133, 132, 132, 132, 132, 131,
			131, 131, 131, 130, 130, 130, 130, 129, 129, 129, 129, 128, 128,
			128, 128 };

	public DoubleVector coeffs;

	JToolBar toolbar2;

	private int dataPts;

	public javax.swing.table.TableColumn clm2;

	private int runs = 0;

	private JTextArea statsTable = new JTextArea(6, 25);

	private double mn = 0;

	private double sd = 1;

	public JCheckBox rawCheck = new JCheckBox("Raw Data", false);

	public SOCRJComboBox combobox;

	private double[] modelX;

	private double[] modelY;

	private ButtonGroup buttonGroup = new ButtonGroup();

	public JCheckBox estimateParams = new JCheckBox("Coefficients");

	private int modelType = WAVELET_TYPE;

	private int modelCt = 6;

	double[] magcoef;

	double[] phasecoef;

	int terms = 1;

	static final double pi = 3.14159265358979323846;

	OB ob;

	double step = 2 * pi / sampleCount;

	double func[];
	//float func[];

	int maxTerms = 160;

	int selectedCoef;

	static final int SEL_NONE = 0;

	static final int SEL_FUNC = 1;

	static final int SEL_MAG = 2;

	static final int SEL_PHASE = 3;

	int selection;

	int dragX, dragY;

	boolean dragging;

	int getrand(int x) {
		int q = random.nextInt();
		if (q < 0)
			q = -q;
		return q % x;
	}

	public class OB implements Observer {
		public OB() {
			//System.out.println("init OB");
		}

		public void update(Observable o, Object arg) {
			////System.out.println("2. performing fwt in wavelet");
			fwt = (FWT) waveletloader.getCurrentItem();

		}
	}
	public WaveletFit_Modeler() {
	}

	public WaveletFit_Modeler(JPanel controlpanel) {
		try {
			waveletloader = new WaveletLoader(SOCRCodeBase.getCodeBase());
			fwt = (FWT) waveletloader.getCurrentItem();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ob = new OB();
		waveletloader.addObserver(ob);
		combobox = (SOCRJComboBox) waveletloader.getJComboBox();
		combobox.addActionListener(this);
		controlpanel.add(estimateParams);
		estimateParams.addItemListener(this);
		controlpanel.add(combobox);



		addParams(controlpanel);
		//combobox.addItemListener(this);
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
		o.addJComboBox(combobox);


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
		;
	}
	public void actionPerformed(ActionEvent e)
	{
		/*//System.out.println("I am being invoked:2");
		if(e.getSource() == combobox) {
			String classname = null;
			classname = combobox.getSelectedClassName();
			//System.out.println(classname);
			try {
			fwt = (FWT) Class.forName(classname).newInstance();
			}
			catch (Throwable error) {

	            error.printStackTrace();
	        }

        }*/
	}

	public int getModelCount() {
		return modelCt;
	}

	public double getCDF(double x) {
		//updateGame();
		return x;
	}


	void doSquare() {
		int x;
		for (x = 0; x < halfSampleCount; x++) {
			func[x] = -3;
			func[x + halfSampleCount] = 3;
		}
		//func[sampleCount-1] = func[0];

	}

	void doNoise() {
		int x;
		int blockSize = 3;
		for (x = 0; x < sampleCount / blockSize; x++) {
			double q = java.lang.Math.random() * 2 - 1;
			int i;
			for (i = 0; i < blockSize; i++)
				func[x * blockSize + i] = (float) q;
		}
		func[sampleCount] = func[0];

	}


	static int freqs[] = { 25, 32, 40, 50, 64, 80, 100, 125, 160, 200, 250,
			320, 400, 500, 800, 1000, 1600, 2000 };

	void transform() {
		int x, y;
		double epsilon = .00001;

		float pt = (terms);
		System.arraycopy(func, 0, magcoef, 0, func.length-1);

		fwt.transform(magcoef);
		tops = new topPercent(magcoef, pt);
	}
	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}

	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		int beg = 0;
		int last = 0;

		String classname = null;
		////System.out.println("I am being invoked");
		classname = combobox.getSelectedClassName();
		////System.out.println(classname);
		try {
			fwt = (FWT) Class.forName(classname).newInstance();
		} catch (Throwable error) {

			error.printStackTrace();
		}

		terms = (int) Double.parseDouble(paramField.getText());
		sampleCount = rawDat.length;
		double iPower = 0;
		double cnt = 0;
		////System.out.println("I am being invoked:2");
		while (iPower <  sampleCount)
		{
			iPower = java.lang.Math.pow(2,cnt);
			cnt += 1;
		}
		////System.out.println("I am being invoked:3");
		if ( iPower != sampleCount)
		{
			beg = (int)(iPower - sampleCount)/2;
			last = (int)(iPower - sampleCount) - beg;
		}
		halfSampleCount = sampleCount/2;

		func = new double[(int)iPower];
		magcoef = new double[(int)iPower];
		phasecoef = new double[(int)iPower];
		////System.out.println("I am being invoked:4");
		////System.out.println(iPower);
		for (int i = 0; i < (iPower); i++) {
			if ( i < beg)
				func[i] = 0;
			else if ( i >= (sampleCount+beg))
				func[i] = 0;
		}
		for ( int i = 0; i < rawDat.length; i ++)
		{
			func[i+beg] = rawDat[i];
		}
		////System.out.println("I am being invoked:5");
		//doSquare();
		transform();
		modelX = new double[(int)iPower ];
		System.arraycopy(func, 0, modelX, 0, func.length);
		modelY = new double[magcoef.length];
		System.arraycopy(magcoef, 0, modelY, 0, magcoef.length);

		if ( !estimateParams.isSelected())
		{
			fwt.invTransform(modelY);
			modelCt = 6;
		}
		else
		{
			modelCt = 7;
		}
		try {
			/* Calculating the standard error*/
			double diff, sum_sqrd_diff = 0;
			for (int i = 0; i < iPower; i++) {
				diff = modelX[i] - modelY[i];
				sum_sqrd_diff += diff * diff;

			}
			resultPanelTextArea.setText("standard error = "
					+ java.lang.Math.sqrt(sum_sqrd_diff) + "\n\n");


		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
	}

	int getFreq() {
		return (int) (27.5 * java.lang.Math
				.exp(game.getValueSetter(1).getValue() * .04158883084));
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
		desc = "Implements the Wavelet fit";
		return desc;
	}

	/** return the instructions for using this modeler*/
	public String getInstructions() {
		String instructions = new String();
		instructions = "1. red line is the fitted curve\n 2. blue line is the user entered data\n 3. After changing the wavelet reenter the % of terms";
		return instructions;
	}

	/** return the references for this modeler*/
	public String getResearch() {
		String research = new String();
		research = "none";
		return research;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.AdjustmentListener#adjustmentValueChanged(java.awt.event.AdjustmentEvent)
	 */
	public void adjustmentValueChanged(AdjustmentEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == termBar) {
			transform();
			;
		}
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
