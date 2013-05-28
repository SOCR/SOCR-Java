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
/* 	modified annieche 200508.
	separate the gui part from the model part
*/

package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.BoxLayout;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.modeler.Modeler;
import edu.ucla.stat.SOCR.modeler.gui.ModelerGui;
import edu.ucla.stat.SOCR.modeler.NormalFit_Modeler;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

public class NormalPower extends Analysis implements MouseListener, ActionListener, KeyListener, AdjustmentListener, MouseMotionListener, PropertyChangeListener {

	//public JTabbedPane tabbedPanelContainer;
	private JToolBar toolBar;
	private Frame frame;

	private  boolean useNE = false;
	private  boolean useLT = false;
	private  boolean useGT = false;
	private boolean useSampleSize = true;
	private boolean usePower = false;
	private boolean useCV = false;

	private JCheckBox checkSampleSizeBox = new JCheckBox("Get Power Using Sample Size", useSampleSize);
	private JCheckBox checkPowerBox = new JCheckBox("Get Sample Size Using Power", usePower);
	private JCheckBox criticalValueBox = new JCheckBox("Get Z-Score and P-Value", useCV);

	private JCheckBox checkNE = new JCheckBox("!=", useNE);
	private JCheckBox checkLT = new JCheckBox("<", useLT);
	private JCheckBox checkGT = new JCheckBox(">", useGT);
	private JComboBox alphaCombo = new JComboBox(new String[]{"0.1","0.05","0.01","0.001"}); //
	private	JTextField sampleSizeText = new JTextField("", 10);
	private	JTextField sigmaText = new JTextField("", 16); // SD
	private	JTextField sigmaZTestText = new JTextField("", 16); // SD
	private	JTextField mu0Text = new JTextField("", 20);
	private	JTextField mu0ZTestText = new JTextField("", 20);
	private	JTextField muAText = new JTextField("", 16);
	private	JTextField powerText = new JTextField("", 10);
	private	JTextField xValueText = new JTextField("", 16);

	private	JLabel h0Label = new JLabel();
	private	JLabel hALabelPrefix = new JLabel();
	private	JLabel hALabelSurfix = new JLabel();
	private	JPanel h0Panel = new JPanel();
	private	JPanel hAPanel = new JPanel();
	private	JPanel hACheckBoxPanel = new JPanel();
	private	JPanel labelPanel = null;//new JPanel();
	private	JPanel inputPanel = null;//new JPanel();

	private	JPanel checkSampleSizePanel = new JPanel();
	private	JPanel checkPowerPanel = new JPanel();
	private	JPanel sampleSizePanel = new JPanel();
	private	JPanel sigmaPanel = new JPanel();
	private	JPanel sigmaZTestPanel = new JPanel();
	private	JPanel mu0Panel = new JPanel();
	private	JPanel mu0ZTestPanel = new JPanel();
	private	JPanel muAPanel = new JPanel();
	private	JPanel alphaPanel = new JPanel();
	private	JPanel powerPanel = new JPanel();
	private	JPanel xValuePanel = new JPanel();
	private	JPanel subPanel = new JPanel(new BorderLayout());
	private	JPanel hypothesisPanel = new JPanel();
	private	JPanel parameterPanel = new JPanel();
	private	JPanel choicePanel = new JPanel();
	private	JLabel sampleSizeLabel = new JLabel("Sample Size (n) ");
	private	JLabel sigmaLabel =      new JLabel("Standard Deviation (sigma)");
	private	JLabel sigmaZTestLabel =      new JLabel("Standard Deviation (sigma)");
	private	JLabel mu0Label =         new JLabel("Mean of Null (mu_0) ");
	private	JLabel mu0ZTestLabel =         new JLabel("Mean of Null (mu_0) ");
	private	JLabel muALabel =        new JLabel("Mean of Alternative (mu_A) ");
	private	JLabel alphaLabel =      new JLabel("Significance Level (alpha) ");
	private	JLabel powerLabel =      new JLabel("Power ");
	private	JLabel xValueLabel =      new JLabel("Enter Point \"X Value\" for Z-Score ");

	private 	   int sampleSize = 0;
	private	 double power = 0;
	private	 double alpha = 0.05;
	private	 double beta = 0;
	private	 double sigma = 0;
	private	 double mu0 = 0;
	private	 double muA = 0;
	private	 double value = 0;
	private	 double critocalValue = 0;
	private	 double zScore = 0;
	private	 double xValue = 0;
	private	 double sigmaZTest = 0;
	private	 double mu0ZTest = 0;

	private	 double probGreater = 0;
	private	 String plotDescription = "";

	private	double[] meanPlotPoints = null;
	private	double[] powerPlotPoints = null;
	private	double[][] multipleMeanPlotPoints = null;
	private	double[][] multiplePowerPlotPoints = null;
	//private String hypothesisType = null;
	private ObservableWrapper observablewrapper = new ObservableWrapper();
	private 		String resultHypothesisType = null;
	private static boolean randomDataStep = false;
	private	double[] yData = null;
	private double xScaleMin;
	private double xScaleMax;
	private double yScaleMax;
	private JPanel legendPanelRawData = new JPanel();
	private JPanel legendPanelSampleMean = new JPanel();
	private JPanel legendPanel = new JPanel();
	private JPanel innerPanel;
	private static int xPosition, yPosition;
	private double sampleSE;
	private NormalFit_Modeler modelObject;
	//***** BEGIN MAKING THE SAMPLE SIZE BAR: "POWER BAR"
	// initial(default) value is 20.
	// each extent = 1
	// min is 1
	// max is 500.

	JScrollBar sampleSizeBar = new JScrollBar(JScrollBar.HORIZONTAL, 20, 1, 1, 500);
	//***** BEGIN MAKING THE "POWER BAR" ( in percentage)

	JScrollBar powerBar = new JScrollBar(JScrollBar.HORIZONTAL, 95, 1, 1, 100);

	//public NormalCurve graphRawData = new NormalCurve(ModelerGui.GUI_LOWER_LIMIT, ModelerGui.GUI_UPPER_LIMIT, 1);

	NormalPowerResult result;
	double sampleMean = 0, sampleVar = 0;
	int df = 0;
	
	/**Initialize the Analysis*/
	public void init(){
		mapIndep = false;
		showInput = false;
		super.init();
		analysisType = AnalysisType.NORMAL_POWER;
		analysisName = "Normal Distribution Power Computation";
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = new boolean[10];

		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var

		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName(analysisName);
		// Create the toolBar
		////////////////////////System.out.println("NormalPower init done variable");
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		////////////////////////System.out.println("NormalPower init this.getContentPane() = " + this.getContentPane());
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		////////////////////////System.out.println("NormalPower init start add listener");

		checkSampleSizeBox.addActionListener(this);
		checkPowerBox.addActionListener(this);
		criticalValueBox.addActionListener(this);

		checkNE.addActionListener(this);
		checkLT.addActionListener(this);
		checkGT.addActionListener(this);
		sampleSizeText.addActionListener(this);
		sigmaText.addActionListener(this);
		sigmaText.addKeyListener(this);

		mu0Text.addActionListener(this);
		muAText.addActionListener(this);
		powerText.addActionListener(this);
		alphaCombo.addActionListener(this);
		sampleSizeBar.addAdjustmentListener(this);
		powerBar.addAdjustmentListener(this);

		checkSampleSizeBox.addMouseListener(this);
		checkPowerBox.addMouseListener(this);
		criticalValueBox.addMouseListener(this);

		checkNE.addMouseListener(this);
		checkLT.addMouseListener(this);
		checkGT.addMouseListener(this);
		sampleSizeText.addMouseListener(this);
		sigmaText.addMouseListener(this);
		mu0Text.addMouseListener(this);
		muAText.addMouseListener(this);
		powerText.addMouseListener(this);
		alphaCombo.addMouseListener(this);
		/********** alphaCombo is not editable because I can't get the critical point for ANY x values. **********/
		alphaCombo.setEditable(false);

		alphaCombo.setSelectedIndex(1);

		tools2.remove(addButton2);
          tools2.remove(removeButton2);
          depLabel.setText(VARIABLE);
		indLabel.setText("");
          listIndepRemoved.setBackground(Color.LIGHT_GRAY);
          mappingInnerPanel.remove(listIndepRemoved);
          addButton1.addActionListener(this);
		chartFactory = new Chart();
		validate();
	}


	/** Create the actions for the buttons */
	protected void createActionComponents (JToolBar toolBar){
		super.createActionComponents(toolBar);
	}


	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){
	}

	/**This method defines the specific statistical Analysis to be carried our on the user specified data. ANOVA is done in this case. */
	public void doAnalysis(){

		////////////System.out.println("doAnalysis");
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		Data data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		int yLength = 0;

		String cellValue = null;

		ArrayList<String> yList = new ArrayList<String>();
		resultPanelTextArea.append("\n\tNormal Distribution Power Analysis Results:\n");
		try {
			for (int k =0; k < dataTable.getRowCount(); k++) {
				try {
					cellValue = ((String)dataTable.getValueAt(k, dependentIndex)).trim();
					if (cellValue != null && !cellValue.equals("")) {
						yList.add(yLength , cellValue);
						yLength++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?
					}
			}

		} catch (Exception e) {
			//////System.out.println("dataTable Exception = " +e);

		}

		yData = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			yData[i] = Double.parseDouble((String)yList.get(i));
		}

		// But the Analyses tools package takes the data in X HashMap.

		result = null;
		if (yLength > 0) {
			randomDataStep = true;
		}

		if (randomDataStep) {
			data.appendY("Y", yData, DataType.QUANTITATIVE);
			try {
				result = (NormalPowerResult) data.getAnalysis(analysisType);

				df = 0;
				sampleMean = 0;sampleVar = 0;

				try {
					sampleMean = result.getSampleMean();
				} catch (Exception e) {

				}
				try {
					sampleVar = result.getSampleVariance();
				} catch (Exception e) {

				}
				try {
					sampleSize = result.getSampleSize();

				} catch (Exception e) {

				}
				sampleSE = Math.sqrt(sampleVar);
				sampleSizeText.setText(sampleSize + "");
				if (power == Double.NaN) {
					power = .95;
				}
				powerText.setText(power + "");

				sigmaText.setText(sampleSE + "");
				mu0Text.setText(sampleMean + "");

				resultPanelTextArea.append("\n\tSample Mean   = " + sampleMean);
				resultPanelTextArea.append("\n\tSample variance = " + sampleVar);

				resultPanelTextArea.append("\n\tStandard Deviation = " + sampleSE);

			} catch (Exception e) {
			}
		}

		try {
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		} catch (Exception e) {
			alpha = 0.05;
		}
		try {
			sampleSize = Integer.parseInt((String)(sampleSizeText.getText()));
		} catch (Exception e) {
			sampleSize = 0;
		}
		if (useCV) {
			try {
				sigma = Double.parseDouble((String)(sigmaZTestText.getText()));
			} catch (Exception e) {
				sigma = 0;
			}
		} else {
			try {
				sigma = Double.parseDouble((String)(sigmaText.getText()));
			} catch (Exception e) {
				sigma = 0;
			}

		}
		try {
			mu0 = Double.parseDouble((String)(mu0Text.getText()));
		} catch (Exception e) {
			mu0 = Double.POSITIVE_INFINITY;
		}

		try {
			muA = Double.parseDouble((String)(muAText.getText()));
		} catch (Exception e) {
			muA = Double.POSITIVE_INFINITY;
		}
		try {
			power = Double.parseDouble((String)(powerText.getText()));
		} catch (Exception e) {
			power = 0;
		}

		try {
			xValue = Double.parseDouble((String)(xValueText.getText()));
		} catch (Exception e) {
			xValue = Double.POSITIVE_INFINITY;
		}

		try {
			sigmaZTest = Double.parseDouble((String)(sigmaZTestText.getText()));
		} catch (Exception e) {
			sigmaZTest = Double.POSITIVE_INFINITY;
		}

		try {
			mu0ZTest = Double.parseDouble((String)(mu0ZTestText.getText()));
		} catch (Exception e) {
			mu0ZTest = Double.POSITIVE_INFINITY;
		}
		if (alpha == 0)
			alpha = 0.05; // default.

		if (sampleSize > 0) {
			useSampleSize = true;
			usePower = false;
			useCV = false;
			power = 0;

			if (hypothesisType == null && !randomDataStep && power == 0) {
				JOptionPane.showMessageDialog(this, "Generate Random Data or Select Hypothesis Parameters First!");
				return;
			}
		}
		else if (power > 0) {
			//////////////System.out.println("Has power");
			usePower = true;
			useSampleSize = false;
			useCV = false;
			sampleSize = 0;

		}

		if (useCV) {
			randomDataStep = false;

			if (mu0ZTest == Double.POSITIVE_INFINITY || xValue == Double.POSITIVE_INFINITY || sigmaZTest <= 0) {
				JOptionPane.showMessageDialog(this, "Enter Proper Parameters First!");

			}

			try {
				result = (NormalPowerResult) data.getNormalAnalysis(mu0ZTest, xValue, sigmaZTest);
			} catch (Exception e) {
			}		
		}
		
		if (useSampleSize) {
			randomDataStep = false;
			if (sampleSize == 0) {
				JOptionPane.showMessageDialog(this, "Select Parameters or Generante Random Data First!");

			}
			else if (sampleSize != 0 && (mu0 == Double.POSITIVE_INFINITY || muA == Double.POSITIVE_INFINITY || sigma <= 0 || hypothesisType == null)) {
				JOptionPane.showMessageDialog(this, "You need to complete all fields in the SELECT PARAMETER seciont!");

			}
			/*
			if (muA != Double.POSITIVE_INFINITY && hypothesisType == null) {
				JOptionPane.showMessageDialog(this, "Select Hypothesis Sign First!");
				return;
			}
			*/
			else if (muA != Double.POSITIVE_INFINITY && muA > mu0 && hypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_LT)) {
				JOptionPane.showMessageDialog(this, "Are you sure mu_A is less than mu_0?");
				return;

			}
			else if (muA != Double.POSITIVE_INFINITY && muA < mu0 && hypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_GT)) {
				JOptionPane.showMessageDialog(this, "Are you sure mu_A is greater than mu_0?");
				return;
			}
			else if(muA == mu0) {
				JOptionPane.showMessageDialog(this, "mu_A is equal to mu_0.");
				return;
			}
			////////////////System.out.println("in gui do try ");

			try {
				result = (NormalPowerResult) data.getNormalPower(sampleSize, sigma, alpha, mu0 , muA, hypothesisType);
				//result = (NormalPowerResult) data.getNormalPower(100, 3500, 0.05, 2500 , 2450, hypothesisType);
			} catch (Exception e) {
				//////////////////System.out.println("in gui useSampleSize " + e);
			}
		}
		else if (usePower) {
			 randomDataStep = false;

			if (power == 0 || mu0 == Double.POSITIVE_INFINITY || muA == Double.POSITIVE_INFINITY || sigma <= 0 || hypothesisType == null) {
				JOptionPane.showMessageDialog(this, "Select Parameters First!");
				return;
			}
			else if (muA != Double.POSITIVE_INFINITY && muA > mu0 && hypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_LT)) {
				JOptionPane.showMessageDialog(this, "Are you sure mu_A is less than mu_0?");
				return;

			}
			else if (muA != Double.POSITIVE_INFINITY && muA < mu0 && hypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_GT)) {
				JOptionPane.showMessageDialog(this, "Are you sure mu_A is greater than mu_0?");
				return;
			}
			else if(muA == mu0) {
				JOptionPane.showMessageDialog(this, "mu_A is equal to mu_0.");
				return;
			}
			try {
				result = (NormalPowerResult) data.getNormalPowerSampleSize(power, sigma, alpha, mu0 , muA, hypothesisType);
				//result = (NormalPowerResult) data.getNormalPower(100, 3500, 0.05, 2500 , 2450, hypothesisType);
				////////////System.out.println("in gui usePower try result =  " + result);

			} catch (Exception e) {
				////////////System.out.println("in gui usePower " + e);
			}
		}
		else if (useCV) {
			try {
				zScore = result.getZScore();
				resultPanelTextArea.append("\n\tGiven:");
				resultPanelTextArea.append("\n\tMean of the Normal Distribution (mu_0) = "+mu0ZTest);
				resultPanelTextArea.append("\n\tStandard Deviation of the Normal Distribution (sigma) = "+sigma);

				resultPanelTextArea.append("\n\tValue of the point (X) = "+ xValue);
				resultPanelTextArea.append("\n\n\tZ-Score = "+zScore);

			} catch (Exception e) {
			}

			try {
				probGreater = result.getPValue();
				resultPanelTextArea.append("\n\tP(X > mu_0) = "+probGreater);
				resultPanelTextArea.append("\n\tP(X < mu_0) = "+ (1 - probGreater));
			} catch (Exception e) {
			}
		}

		if (usePower || useSampleSize) {
			try {
				sampleSize = result.getSampleSize();
			} catch (Exception e) {
			}

			try {
				power = result.getPower();
			} catch (Exception e) {
			}

			try {
				meanPlotPoints = result.getMeanPlotPoints();

			} catch (Exception e) {
			}

			try {
				powerPlotPoints = result.getPowerPlotPoints();
			} catch (Exception e) {
			}
			try {
				multipleMeanPlotPoints = result.getMultipleMeanPlotPoints();
			} catch (Exception e) {
			}

			try {
				multiplePowerPlotPoints = result.getMultiplePowerPlotPoints();
			} catch (Exception e) {
			}
			try {
				plotDescription = result.getPlotDescription();
			} catch (Exception e) {
			}
			try {
				resultHypothesisType = result.getResultHypothesisType();
				//resultPanelTextArea.append("\n\tpowerPlotPoints = "+powerPlotPoints[0] );

			} catch (Exception e) {
			}

			if (sampleSize > 0)
				resultPanelTextArea.append("\n\tSample Size      = " + sampleSize);
			if (power > 0) {
				try {
					resultPanelTextArea.append("\n\tPower            = " + (power + "").substring(0, 15));
				} catch (Exception e) {
					resultPanelTextArea.append("\n\tPower            = " + power);
				}
			}


			if (resultHypothesisType != null) {
				if (resultHypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_NE)) {
					resultPanelTextArea.append("\n\n\tAlternative Hypothesis: mu_A is not equal to mu_0.");
				}
				else if (resultHypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_LT)) {
					resultPanelTextArea.append("\n\n\tAlternative Hypothesis: mu_A is less than mu_0.");

				}
				else if (resultHypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_GT)) {
					resultPanelTextArea.append("\n\n\tAlternative Hypothesis: mu_A is greater than mu_0.");

				}
			}
			resultPanelTextArea.append("\n\n\tmu_0        = " + mu0);

			if (muA < Double.POSITIVE_INFINITY) {
				resultPanelTextArea.append("\n\tmu_A        = " + muA);
			}
			resultPanelTextArea.append("\n\tsigma        = " + sigma);
		}

		resultPanelTextArea.setForeground(Color.BLUE);
		
		updateResults();
		
		try {
			doGraph();
		} catch (Exception e) {
		}
		if (useSampleSize || usePower) {
			try {
				doRawDataNormalCurve();
			} catch (Exception e) {
			}
			try {
				modelObject = new NormalFit_Modeler( );

				sampleSE = sigma/Math.sqrt(sampleSize);

				doSampleMeanNormalCurve();
			} catch (Exception e) {
			}
		} else if (useCV) {

			try {
				doZTestNormalCurve();
			} catch (Exception e) {
			}
		}

	}

	public void updateResults(){

		if (result==null)
		return;
	
		result.setDecimalFormat(dFormat);	
	

	setDecimalFormat(dFormat);
	
	resultPanelTextArea.setText("\n"); //clear first
	
	resultPanelTextArea.append("\n\tNormal Distribution Power Analysis Results:\n");

	resultPanelTextArea.append("\n\tSample Mean   = " + result.getFormattedDouble(sampleMean));
	resultPanelTextArea.append("\n\tSample variance = " + result.getFormattedDouble(sampleVar));

	resultPanelTextArea.append("\n\tStandard Deviation = " + result.getFormattedDouble(sampleSE));

	
	if (useCV) {
		resultPanelTextArea.append("\n\tGiven:");
		resultPanelTextArea.append("\n\tMean of the Normal Distribution (mu_0) = "+result.getFormattedDouble(mu0ZTest));
		resultPanelTextArea.append("\n\tStandard Deviation of the Normal Distribution (sigma) = "+result.getFormattedDouble(sigma));

		resultPanelTextArea.append("\n\tValue of the point (X) = "+ xValue);
		resultPanelTextArea.append("\n\n\tZ-Score = "+result.getFormattedDouble(zScore));
		resultPanelTextArea.append("\n\tP(X > mu_0) = "+result.getFormattedDouble(probGreater));
		resultPanelTextArea.append("\n\tP(X < mu_0) = "+ result.getFormattedDouble((1 - probGreater)));
	}
	
	if (usePower || useSampleSize) {
		if (sampleSize > 0)
			resultPanelTextArea.append("\n\tSample Size      = " + sampleSize);
		if (power > 0) {
			try {
				resultPanelTextArea.append("\n\tPower            = " +result.getFormattedDouble(power));
				//resultPanelTextArea.append("\n\tPower            = " + (power + "").substring(0, 15));
			} catch (Exception e) {
				resultPanelTextArea.append("\n\tPower            = " + power);
			}
		}
		
		if (resultHypothesisType != null) {
			if (resultHypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_NE)) {
				resultPanelTextArea.append("\n\n\tAlternative Hypothesis: mu_A is not equal to mu_0.");
			}
			else if (resultHypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_LT)) {
				resultPanelTextArea.append("\n\n\tAlternative Hypothesis: mu_A is less than mu_0.");

			}
			else if (resultHypothesisType.equals(NormalPowerResult.HYPOTHESIS_TYPE_GT)) {
				resultPanelTextArea.append("\n\n\tAlternative Hypothesis: mu_A is greater than mu_0.");

			}
		}
		resultPanelTextArea.append("\n\n\tmu_0        = " + result.getFormattedDouble(mu0));

		if (muA < Double.POSITIVE_INFINITY) {
			resultPanelTextArea.append("\n\tmu_A        = " + result.getFormattedDouble(muA));
		}
		resultPanelTextArea.append("\n\tsigma        = " + result.getFormattedDouble(sigma));
	}//usePower || useSampleSize
	
	resultPanelTextArea.setForeground(Color.BLUE);
	}
	/******************************************************************************/
	/** convert a generic string s to a fixed length one. */
    	public String monoString(String s) {
		String sAdd = new String(s + "                                      ");
		return sAdd.substring(0,14);
    	}

	/** convert a generic double s to a "nice" fixed length string */
    	public String monoString(double s) {
		final double zero = 0.00001;
        	Double sD = new Double(s);
		String sAdd = new String();
		if(s>zero)
	    		sAdd = new String(sD.toString());
		else  sAdd = "<0.00001";

		sAdd=sAdd.toLowerCase();
		int i=sAdd.indexOf('e');
		if(i>0)
	    		sAdd = sAdd.substring(0,4)+"E"+sAdd.substring(i+1,sAdd.length());
		else if(sAdd.length()>10)
				sAdd = sAdd.substring(0,10);

		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}

	/** convert a generic integer s to a fixed length string */
    	public String monoString(int s) {
		Integer sD = new Integer(s);
		String sAdd = new String(sD.toString());
		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}

    /** Implementation of PropertyChageListener.*/
    public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));

		}
   }
	public Container getDisplayPane() {
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		return this.getContentPane();
	}
    public String getOnlineDescription(){
        return new String("http://en.wikipedia.org/wiki/Statistical_power");
    }

     public void actionPerformed(ActionEvent event) {
		//System.out.println("call actionPerformed");
		if (event.getSource() == criticalValueBox) {
			hypothesisPanel.setVisible(false);
			sampleSizePanel.setVisible(false);
			powerPanel.setVisible(false);
			muAPanel.setVisible(false);
			alphaPanel.setVisible(false);
			xValuePanel.setVisible(true);
			mu0ZTestPanel.setVisible(true);
			sigmaZTestPanel.setVisible(false);

			useCV = true;
			usePower = false;
			useSampleSize = false;
		}

		if (event.getSource() == checkSampleSizeBox){
			useSampleSize = true;
			usePower = false;
			useCV = false;

			if (useSampleSize) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(true);
				powerPanel.setVisible(false);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);


				this.power = 0; powerText.setText("");

			}
			else if (usePower) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(false);
				powerPanel.setVisible(true);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);


				this.sampleSize = 0; sampleSizeText.setText("");
			}
		}
		if (event.getSource() == checkPowerBox){
			usePower = true;
			useSampleSize = false;
			useCV = false;

			if (useSampleSize) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(true);
				powerPanel.setVisible(false);
				xValuePanel.setVisible(false);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);

				this.power = 0; powerText.setText("");

			}
			else if (usePower) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(false);
				powerPanel.setVisible(true);
				xValuePanel.setVisible(false);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);

				this.sampleSize = 0; sampleSizeText.setText("");

			}
		}

		if (event.getSource() == checkNE){
			useNE = true;
			useLT = false;
			useGT = false;
			hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_NE;
			graphSampleMean.resetHypotheseType();
		}
		else if (event.getSource() == checkLT){
			useNE = false;
			useLT = true;
			useGT = false;
			hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_LT;
			graphSampleMean.resetHypotheseType();
		}
		else if (event.getSource() == checkGT){
			useNE = false;
			useLT = false;
			useGT = true;
			hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_GT;
			graphSampleMean.resetHypotheseType();


		}
		else if (event.getSource() == alphaCombo){
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		}
		else if (event.getSource() == sampleSizeText){
			sampleSize = Integer.parseInt((String)(sampleSizeText.getText()));

			sigmaText.requestFocus();
		}
		else if (event.getSource() == sigmaText){
			sigma = Double.parseDouble((String)(sigmaText.getText()));
			mu0Text.requestFocus();
		}
		else if (event.getSource() == mu0Text){
			mu0 = Double.parseDouble((String)(mu0Text.getText()));
			////////////////////System.out.println("actionPerformed mu0 = " + mu0);
			muAText.requestFocus();
		}
		else if (event.getSource() == muAText){
			muA = Double.parseDouble((String)(muAText.getText()));
			alphaCombo.requestFocus();
		}
		else if (event.getSource() == alphaCombo){
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
			powerText.requestFocus();

		}
		else if (event.getSource() == powerText){
			power = Double.parseDouble((String)(powerText.getText()));
			////////////////////System.out.println("power = " + power);
			sigmaText.requestFocus();

		}
		else if (event.getSource() == xValueText){

			xValue = Double.parseDouble((String)(xValueText.getText()));
			sigmaText.requestFocus();

		}
		else if (event.getSource() == sigmaZTestText){

			sigmaZTest = Double.parseDouble((String)(sigmaZTestText.getText()));
			mu0Text.requestFocus();

		}

		if(event.getSource() == addButton1) {
			addButtonDependent();
		}
		else if (event.getSource() == removeButton1) {
			removeButtonDependent();
		}
		checkSampleSizeBox.setSelected(useSampleSize);
		checkPowerBox.setSelected(usePower);
		criticalValueBox.setSelected(useCV);

		checkNE.setSelected(useNE);
		checkLT.setSelected(useLT);
		checkGT.setSelected(useGT);
	}

	public void mouseClicked(MouseEvent event) {

		if (event.getSource() == criticalValueBox) {
			hypothesisPanel.setVisible(false);
			sampleSizePanel.setVisible(false);
			powerPanel.setVisible(false);
			muAPanel.setVisible(false);
			sigmaPanel.setVisible(false);
			mu0Panel.setVisible(false);
			alphaPanel.setVisible(false);
			mu0ZTestPanel.setVisible(true);
			sigmaZTestPanel.setVisible(true);
			xValuePanel.setVisible(true);

			useCV = true;
			useSampleSize = false;
			usePower = false;

		}
		if (event.getSource() == checkSampleSizeBox){
			useSampleSize = true;
			usePower = false;
			useCV = false;

			if (useSampleSize) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(true);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);
				powerPanel.setVisible(false);
				xValuePanel.setVisible(false);
				sigmaZTestPanel.setVisible(false);
				mu0ZTestPanel.setVisible(false);

				this.power = 0;
				powerText.setText("");


			}
			else if (usePower) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(false);
				powerPanel.setVisible(true);
				xValuePanel.setVisible(false);
				sigmaZTestPanel.setVisible(false);
				mu0ZTestPanel.setVisible(false);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);

				this.sampleSize = 0;
				sampleSizeText.setText("");
			}
		}
		if (event.getSource() == checkPowerBox){
			usePower = true;
			useSampleSize = false;
			useCV = false;
			if (useSampleSize) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(true);
				powerPanel.setVisible(false);
				xValuePanel.setVisible(false);
				sigmaZTestPanel.setVisible(false);
				mu0ZTestPanel.setVisible(false);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);

				this.power = 0;
				powerText.setText("");

			}
			else if (usePower) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(false);
				powerPanel.setVisible(true);
				xValuePanel.setVisible(false);
				sigmaZTestPanel.setVisible(false);
				mu0ZTestPanel.setVisible(false);
				mu0Panel.setVisible(true);
				muAPanel.setVisible(true);
				sigmaPanel.setVisible(true);
				alphaPanel.setVisible(true);


				this.sampleSize = 0;
				sampleSizeText.setText("");

			}
		}

		if (event.getSource() == checkNE){
			useNE = true;
			useLT = false;
			useGT = false;
			try {
				hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_NE;
				graphSampleMean.resetHypotheseType();

			} catch (Exception e) {

			}
		}
		else if (event.getSource() == checkLT){
			useNE = false;
			useLT = true;
			useGT = false;
			try {
				hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_LT;
				graphSampleMean.resetHypotheseType();

			}catch (Exception e) {

			}

		}
		else if (event.getSource() == checkGT){

			useNE = false;
			useLT = false;
			useGT = true;
			try {
				hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_GT;
				graphSampleMean.resetHypotheseType();
			} catch (Exception e) {

			}
		}
		else if (event.getSource() == alphaCombo){
			try {
				alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
			} catch (Exception e) {
			}
		}
		else if (event.getSource() == sampleSizeText){
			try {
				sampleSize = Integer.parseInt((String)(sampleSizeText.getText()));
			} catch (Exception e) {
			}
		}
		else if (event.getSource() == sigmaText){
			try {
				sigma = Double.parseDouble((String)(sigmaText.getText()));
			} catch (Exception e) {
			}
		}
		else if (event.getSource() == mu0Text){
			try {
				mu0 = Double.parseDouble((String)(mu0Text.getText()));
			} catch (Exception e) {
			}
		}
		else if (event.getSource() == muAText){
			try {
				muA = Double.parseDouble((String)(muAText.getText()));
			} catch (Exception e) {
			}
		}
		else if (event.getSource() == powerText){
			try {
				power = Double.parseDouble((String)(powerText.getText()));
			} catch (Exception e) {
			}
		}
		else if (event.getSource() == xValueText){
			try {
				xValue = Double.parseDouble((String)(xValueText.getText()));
			} catch (Exception e) {
			}
		}
	}
	public void mouseReleased(MouseEvent event) {
		mouseClicked(event);
	}

	public void mouseExited(MouseEvent event) {
		//mouseClicked(event);
	}
	public void mousePressed(MouseEvent event) {
		mouseClicked(event);
	}


	public void keyReleased(KeyEvent  event){
		keyTyped(event);
	}
	public void keyPressed(KeyEvent  event){
		keyTyped(event);

	}

	public void keyTyped(KeyEvent  event){
		if(event.getKeyCode() == KeyEvent.VK_TAB) {
			System.out.println("rowNames  keyTyped event.getKeyCode() == KeyEvent.VK_TAB");
		}

		System.out.println("rowNames  keyTyped event == " + event);

			if (event.getSource() == checkNE){
				useNE = true;
				useLT = false;
				useGT = false;
				try {
					hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_NE;
					graphSampleMean.resetHypotheseType();
				} catch (Exception e) {

					////////////////System.out.println("keyTyped event.getSource() NE " + e);
				}
			}
			else if (event.getSource() == checkLT){
				useNE = false;
				useLT = true;
				useGT = false;
				try {
					hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_LT;
					graphSampleMean.resetHypotheseType();
				} catch (Exception e) {
					////////////////System.out.println("keyTyped event.getSource() LT " + e);

				}
			}
			else if (event.getSource() == checkGT){
				useNE = false;
				useLT = false;
				useGT = true;
				try {
					hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_GT;
					graphSampleMean.resetHypotheseType();
				} catch (Exception e) {
					////////////////System.out.println("keyTyped event.getSource() GT "  +e );

				}
			}

			if (event.getSource() == alphaCombo){
				try {
					alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
				graphSampleMean.resetHypotheseType();
				} catch (Exception e) {
				}
			}
			else if (event.getSource() == sampleSizeText){
				try {
					sampleSize = Integer.parseInt((String)(sampleSizeText.getText()));
					////////////////////System.out.println("itemStateChanged sampleSize = " + sampleSize);
				} catch (Exception e) {
				}
			}
			else if (event.getSource() == sigmaText){
				try {
					sigma = Double.parseDouble((String)(sigmaText.getText()));
					//System.out.println("keyTyped sigma = " + sigma);
				} catch (Exception e) {
				}
			}
			else if (event.getSource() == mu0Text){
				try {
					mu0 = Double.parseDouble((String)(mu0Text.getText()));
					////////////////////System.out.println("keyTyped mu0 = " + mu0);
				} catch (Exception e) {
				}
			}
			else if (event.getSource() == muAText){
				try {
					muA = Double.parseDouble((String)(muAText.getText()));
					////////////////////System.out.println("keyTyped muA = " + muA);
				} catch (Exception e) {
				}
			}
			else if (event.getSource() == powerText){
				try {
					power = Double.parseDouble((String)(powerText.getText()));
					////////////////////System.out.println("keyTyped power = " + power);
				} catch (Exception e) {
				}
			}
			else if (event.getSource() == xValueText){
				try {
					xValue = Double.parseDouble((String)(xValueText.getText()));
				} catch (Exception e) {
				}
			}
	}


	public void adjustmentValueChanged(AdjustmentEvent e) {
		if(e.getSource() == sampleSizeBar) {
			int tempSampleSize = sampleSizeBar.getValue();
			sampleSizeText.setText(tempSampleSize + "");
			this.sampleSize = tempSampleSize;
			////////////System.out.println("sampleSizeBar.getValue() = " + tempSampleSize);

			if (useSampleSize) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(true);
				powerPanel.setVisible(false);
				this.power = 0; powerText.setText("");
			}
			else if (usePower) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(false);
				powerPanel.setVisible(true);
				this.sampleSize = 0; sampleSizeText.setText("");
			}
		}
		else if(e.getSource() == powerBar) {
			int tempPower = powerBar.getValue();
			if (tempPower == Double.NaN) {
				tempPower = 95;
			}
			powerText.setText("." + tempPower);
			this.power = .01 * tempPower;

			////////////System.out.println("set powerBar.getValue() = " + tempPower);
			if (useSampleSize) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(true);
				powerPanel.setVisible(false);
				this.power = 0; powerText.setText("");
			}
			else if (usePower) {
	    			hypothesisPanel.setVisible(true);
				sampleSizePanel.setVisible(false);
				powerPanel.setVisible(true);
				this.sampleSize = 0; sampleSizeText.setText("");
			}
		}
	}

    protected void setSelectPanel() {
		selectPanel.removeAll();
		selectPanel.setPreferredSize(new Dimension(400, 400));
	    	selectPanel.add(subPanel, BorderLayout.EAST);
	    	selectPanel.add(subPanel, BorderLayout.CENTER);
	    	selectPanel.add(subPanel, BorderLayout.WEST);

		subPanel.add(parameterPanel,BorderLayout.SOUTH);
	    	subPanel.add(hypothesisPanel,BorderLayout.CENTER);
	    	subPanel.add(choicePanel,BorderLayout.NORTH);

		hypothesisPanel.setBackground(Color.LIGHT_GRAY);
		parameterPanel.setBackground(Color.LIGHT_GRAY);
		choicePanel.setBackground(Color.LIGHT_GRAY);
	     /*********** Begin Check/Choice Setting ********************/
	    	//choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
		choicePanel.setLayout(new BorderLayout());
		choicePanel.add(checkSampleSizeBox, BorderLayout.NORTH);
		choicePanel.add(checkPowerBox, BorderLayout.CENTER);
		choicePanel.add(criticalValueBox, BorderLayout.SOUTH);

	     /*********** End Check/Choice Setting ********************/

	     /*********** Begin Hypothesis Setting ********************/

		h0Label = new JLabel("Null: mu      =       mu_0;  ");
		hALabelPrefix = new JLabel("Alternative: mu_A ");
		hALabelSurfix = new JLabel(" mu_0");

		h0Panel.setBackground(Color.LIGHT_GRAY);

		hAPanel.setBackground(Color.LIGHT_GRAY);
		hACheckBoxPanel.setBackground(Color.LIGHT_GRAY);

		hypothesisPanel.add(h0Panel, BorderLayout.CENTER);
		hypothesisPanel.add(hAPanel, BorderLayout.SOUTH);

		h0Panel.add(h0Label, BorderLayout.CENTER);

		hAPanel.add(hALabelPrefix, BorderLayout.WEST);
		hAPanel.add(hACheckBoxPanel, BorderLayout.CENTER);
		hAPanel.add(hALabelSurfix, BorderLayout.EAST);

		hACheckBoxPanel.add(checkNE, BorderLayout.CENTER);
		hACheckBoxPanel.add(checkLT, BorderLayout.EAST);
		hACheckBoxPanel.add(checkGT, BorderLayout.WEST);
	     /*********** End Hypothesis Setting ********************/

	     /*********** Begin Parameter Setting *******************/
		sampleSizePanel = new JPanel();
		sigmaPanel = new JPanel();
		mu0Panel = new JPanel();
		sigmaZTestPanel = new JPanel();
		mu0ZTestPanel = new JPanel();
		muAPanel = new JPanel();
		alphaPanel = new JPanel();
		powerPanel = new JPanel();
		xValuePanel = new JPanel();

		sampleSizePanel.add(sampleSizeLabel, BorderLayout.WEST);
		sampleSizePanel.add(sampleSizeBar, BorderLayout.CENTER);
		sampleSizePanel.add(sampleSizeText, BorderLayout.EAST);
		sigmaPanel.add(sigmaLabel, BorderLayout.WEST);
		sigmaPanel.add(sigmaText, BorderLayout.EAST);
		sigmaZTestPanel.add(sigmaZTestLabel, BorderLayout.WEST);
		sigmaZTestPanel.add(sigmaZTestText, BorderLayout.EAST);
		mu0Panel.add(mu0Label, BorderLayout.WEST);
		mu0Panel.add(mu0Text, BorderLayout.EAST);
		mu0ZTestPanel.add(mu0ZTestLabel, BorderLayout.WEST);
		mu0ZTestPanel.add(mu0ZTestText, BorderLayout.EAST);
		muAPanel.add(muALabel, BorderLayout.WEST);
		muAPanel.add(muAText, BorderLayout.EAST);
		alphaPanel.add(alphaLabel, BorderLayout.WEST);
		alphaPanel.add(alphaCombo, BorderLayout.EAST);
		powerPanel.add(powerLabel, BorderLayout.WEST);
		powerPanel.add(powerBar, BorderLayout.CENTER);
		powerPanel.add(powerText, BorderLayout.EAST);
		xValuePanel.add(xValueLabel, BorderLayout.WEST);
		xValuePanel.add(xValueText, BorderLayout.EAST);

		parameterPanel.setLayout(new BoxLayout(parameterPanel, BoxLayout.Y_AXIS));

		parameterPanel.add(sampleSizePanel);
		parameterPanel.add(powerPanel);

		parameterPanel.add(xValuePanel);
		parameterPanel.add(sigmaPanel);
		parameterPanel.add(mu0Panel);
		parameterPanel.add(muAPanel);
		parameterPanel.add(alphaPanel);

		parameterPanel.add(sigmaZTestPanel);
		parameterPanel.add(mu0ZTestPanel);

		parameterPanel.add(alphaPanel);
		parameterPanel.add(alphaPanel);

		hypothesisPanel.setVisible(true);
		sampleSizePanel.setVisible(true);
		powerPanel.setVisible(false);
		xValuePanel.setVisible(false);
		sigmaZTestPanel.setVisible(false);
		mu0ZTestPanel.setVisible(false);

	}


	protected void doGraph() {
		// graphComponent is available here
		// data: variables double xData, yData, residuals, predicted are available here after doAnalysis() is run.
		graphPanel.removeAll();
		////////////System.out.println("doGraph");
		/*try {
			///FileHelper fh = new FileHelper();
			//fh.openWriter("C:\\STAT\\SOCR\\test\\PlotData.txt");
			for (int i = 0; i < meanPlotPoints.length; i++) {
				//fh.write(meanPlotPoints[i] + " " + powerPlotPoints[i]);
				////////////System.out.println(i + " m = " + meanPlotPoints[i] + " p = " + powerPlotPoints[i]);

			}
			//fh.closeWriter();
		} catch (Exception e) {
		}*/
		String lineType = "excludesZeroNoShape";
		if (usePower) {
			lineType = "excludesZeroNoShape";
		}

		//JFreeChart scatterChart  = chartFactory.getLineChart("Power vs. Mean", "Mean", "Power", meanPlotPoints, powerPlotPoints, lineType);//getChart(title, xlabel, ylabel, xdata,ydata)
		//ChartPanel chartPanel = new ChartPanel(scatterChart, false);
		//chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		//////////////////System.out.println("graphPanel useSampleSize = " + useSampleSize + ", usePower " + usePower );
		//////////////////System.out.println("graphPanel plotDescription = " + plotDescription);
		JLabel description;

		//graphPanel.add(description, BorderLayout.SOUTH);
		JFreeChart scatterChart = null;
		ChartPanel chartPanel = null;
		if(useSampleSize) {
			//////////////System.out.println("graphPanel useSampleSize = " + useSampleSize);
			//////////////System.out.println("graphPanel meanPlotPoints.length = " + meanPlotPoints.length);


			lineType = "excludesZeroNoShape";

			for (int i = 0; i < meanPlotPoints.length; i++) {
				//////////////System.out.println("meanPlotPoints["+i+"] = " + meanPlotPoints[i] + ", powerPlotPoints[" + i + "] =" + powerPlotPoints[i]);

			}

			scatterChart  = chartFactory.getLineChart("Power vs. Mean", "Mean", "Power", meanPlotPoints, powerPlotPoints, lineType);//getChart(title, xlabel, ylabel, xdata,ydata)
			chartPanel = new ChartPanel(scatterChart, false);
			chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));

		} else if (usePower) {
			chartFactory = new Chart();

			/*double[][] multipleMeanPlotPoints =  {{1,2,3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8,9,10}, {1,2,3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8,9,10}};
			double[][] multiplePowerPlotPoints = {{.1,.2,.3,.4,.5,.6,.7,.8,.9,1},{1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0},{2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,3.0},{3.1,3.2,3.3,3.4,3.5,3.6,3.7,3.8,3.9,4.0},{4.1,4.2,4.3,4.4,4.5,4.6,4.7,4.8,4.9,5}};
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 200; j++) {
					//////////////System.out.println("doGraph multipleMeanPlotPoints[" + i + "][" + j + "] = " + multipleMeanPlotPoints[i][j]  + " "+meanPlotPoints[i] );
				}
			}/*
			for (int i = 0; i < 5; i++) {
					//for (int j = 0; j < 200; j++) {
						int j = 100;
						//////////////System.out.println("doGraph multiplePowerPlotPoints[" + i + "][" + j + "] = " + multiplePowerPlotPoints[i][j] );						resultPanelTextArea.append(" "+meanPlotPoints[i] );
					//}
			}*/
			//String lineType = "excludesZeroNoShape";
			//String[] lineNumbers = new String[]{"1", "2", "3", "4", "5"};
			//scatterChart = chartFactory.getLineChart("Power vs. Mean", "Mean", "Power", 5, lineNumbers, multipleMeanPlotPoints, multiplePowerPlotPoints, lineType);

			lineType = "excludesZeroNoShape";
			//////////////////////System.out.println("graphPanel usePower = " + usePower + ", lineType " + lineType);
        		//double x = new double[numberOfLines][numberOfPoints];
       		String[] lineNumbers = new String[]{sampleSize + "", sampleSize + 10 + "", (sampleSize + 20) + "", (sampleSize + 30) + "", (sampleSize + 40) + ""};
       		scatterChart = chartFactory.getLineChart("Power vs. Mean", "Mean", "Power", 5, lineNumbers, multipleMeanPlotPoints, multiplePowerPlotPoints, lineType);
			chartPanel = new ChartPanel(scatterChart, false);
			chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));

		} else if (useCV) {
		}
		graphPanel.add(chartPanel, BorderLayout.NORTH);


		//if (usePower) {
		//	plotDescription = "Sample Size: PINK = " + sampleSize + ",  YELLOW = " + (sampleSize + 10) + ",  GREEN = " + (sampleSize + 20) + ",  BLUE = " + (sampleSize + 30) + ",  RED = " + (sampleSize + 40) + ".";
		//}
		description = new JLabel("           " + plotDescription + "           ");
		graphPanel.add(description);

		graphPanel.validate();

	}

	protected void doRawDataNormalCurve()
	{
		NormalFit_Modeler modelObject = new NormalFit_Modeler( );
		modelObject.setSliceSize(101);
		graphRawData.setType(0);
		graphRawData.addMouseListener(this);
		double minOfMu = Math.min(mu0, muA);
		double maxOfMu = Math.max(mu0, muA);
		graphRawData.setHistogramRight(maxOfMu + 4 * sigma);
		graphRawData.setHistogramLeft(minOfMu - 4 * sigma);

		double minOfModel = 0, maxOfModel = 0; // of the model doamin.

		minOfModel = minOfMu - 4 * sigma; //modelObject.getGraphLowerLimit();
		maxOfModel = maxOfMu + 4 * sigma;//modelObject.getGraphUpperLimit();
		//minOfModel = modelObject.getGraphLowerLimit();
		//maxOfModel = modelObject.getGraphUpperLimit();
		float[] yFloat = new float[yData.length];
		for (int i = 0; i < yData.length; i++) {
			yFloat[i] = (float) yData[i];
			////////////////System.out.println("yFloat["+i+"] = " + yFloat[i]);
		}
		NormalDistribution normal = new NormalDistribution(mu0, sigma);
		graphRawData.setRawData(yData);
		graphRawData.setRawDataDistribution(normal);
		double graphMaxY = Math.max(normal.getMaxDensity(), graphRawData.getMaxRelFreq());
		//////////////System.out.println("In doRawDataNormalCurve graphRawData.getMaxRelFreq() = " + graphRawData.getMaxRelFreq());
		graphRawData.setPlotYMax(graphMaxY);

		//////////////System.out.println("In doRawDataNormalCurve getMaxDensity = " + normal.getMaxDensity());
		//setScale(
		graphRawData.setPlotYMin(0);
		double maxBound = maxOfMu + 4 * sigma;
		double minBound = minOfMu - 4 * sigma;

		graphRawData.setPlotXMax(maxOfMu + 4 * sigma);
		graphRawData.setPlotXMin(minOfMu - 4 * sigma);
		////////////System.out.println("In doRawDataNormalCurve setPlotXMax = " + maxBound);
		////////////System.out.println("In doRawDataNormalCurve setPlotXMin = " + minBound);
		/************* mu0 ****************/
		modelObject.fitCurve(yFloat, (float)minOfModel, (float)maxOfModel, mu0, sigma, true, true);
		double[] modelXData0 = modelObject.returnModelX();
		double[] modelYData0 = modelObject.returnModelY();

		int modelType = modelObject.getModelType();
		//int arrayLegnth = modelXData.length;

		/************* muA ****************/

		modelObject.fitCurve(yFloat, (float)minOfModel, (float)maxOfModel, muA, sigma, true, true);
		double[] modelXDataA = modelObject.returnModelX();
		double[] modelYDataA = modelObject.returnModelY();
		modelType = modelObject.getModelType();
		//int arrayLegnth = modelXData.length;

		graphRawData.setModelCount(modelObject.getModelCount());
		graphRawData.setTwoModel(modelXData0.length + modelXDataA.length, modelXData0, modelYData0, modelXDataA, modelYDataA, Color.BLUE, Color.RED);
		String mu0Tick = "" + mu0, muATick = "" + muA, sigmaTick = "" + sigma;

		//String sampleSETick =
		int indexOfPoint = -1;

		ArrayList<String> listA = new ArrayList<String>();
		////////////////////////System.out.println("add to listA mu0Tick " + mu0Tick);
		listA.add(0, mu0Tick);
		listA.add(1, muATick);
		listA.add(2, sigmaTick);

		graphRawData.setListOfTicks(listA);
		graphRawData.setOutlineColor1(Color.BLUE);
		graphRawData.setOutlineColor2(Color.RED);
		graphRawData.setFillArea(false);
		graphRawData.validate();
		graphRawData.repaint();
		graphRawData.setPreferredSize(new Dimension(400, 60));
		graphRawData.setBackground(Color.WHITE);

		this.visualizePanel.validate();
		innerPanel.add(graphRawData);
		legendPanel.setVisible(true);

	}

	protected void doSampleMeanNormalCurve()
	{
		/*
		NormalFit_Modeler modelObject = new NormalFit_Modeler( );

		sampleSE = sigma/Math.sqrt(sampleSize);
		*/
		graphSampleMean.setType(0);
		graphSampleMean.addMouseListener(this);
		double minOfMu = Math.min(mu0, muA);
		double maxOfMu = Math.max(mu0, muA);
		graphSampleMean.setHistogramRight(maxOfMu + 4 * sigma);
		graphSampleMean.setHistogramLeft(minOfMu - 4 * sigma);

		double minOfModel = 0, maxOfModel = 0; // of the model doamin.

		minOfModel = Math.floor(minOfMu - 4 * sampleSE);//modelObject.getGraphLowerLimit();
		maxOfModel = Math.ceil(maxOfMu + 4 * sampleSE);//modelObject.getGraphUpperLimit();

		float[] yFloat = new float[yData.length];
		for (int i = 0; i < yData.length; i++) {
			yFloat[i] = (float) yData[i];
		}

		NormalDistribution normal = new NormalDistribution(mu0, sampleSE);

		graphSampleMean.setPlotYMax(normal.getMaxDensity());
		graphSampleMean.setPlotYMin(0);
		graphSampleMean.setPlotXMax(maxOfMu + 4 * sigma);
		graphSampleMean.setPlotXMin(minOfMu - 4 * sigma);

		/************* mu0 ****************/
		modelObject.fitCurve(yFloat, (float)minOfModel, (float)maxOfModel, mu0, sampleSE, true, true);
		double[] modelXData0 = modelObject.returnModelX();
		double[] modelYData0 = modelObject.returnModelY();

		int modelType = modelObject.getModelType();
		//int arrayLegnth = modelXData.length;

		/************* muA ****************/

		modelObject.fitCurve(yFloat, (float)minOfModel, (float)maxOfModel, muA, sampleSE, true, true);
		double[] modelXDataA = modelObject.returnModelX();
		double[] modelYDataA = modelObject.returnModelY();
		modelType = modelObject.getModelType();
		//int arrayLegnth = modelXData.length;

		graphSampleMean.setModelCount(modelObject.getModelCount());
		graphSampleMean.setTwoModel(modelXData0.length + modelXDataA.length, modelXData0, modelYData0, modelXDataA, modelYDataA, Color.BLUE, Color.RED);
		String mu0Tick = "" + mu0, muATick = "" + muA, sampleSETick = "" + sampleSE;
		String sigmaTick = "" + sigma;
		int indexOfPoint = -1;
		String ciTickLeft = "", ciTickRight = "";
		ArrayList<String> listA = new ArrayList<String>();
		////////System.out.println("add to listA mu0Tick alpha =  " + alpha);
		listA.add(0, mu0Tick);
		listA.add(1, muATick);
		listA.add(2, sampleSETick);
		//NormalDistribution nd = new NormalDistribution(0, 1);
		if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_LT)) {
			double criticalPoint = this.getNormalCriticalPoint(alpha);
			////////System.out.println("1 add to listA mu0Tick criticalPoint =  " +criticalPoint);


			ciTickLeft = (mu0 - criticalPoint * sampleSE) + "";
			ciTickRight = (mu0 + criticalPoint * sampleSE) + "";
			listA.add(3, ciTickLeft);
			listA.add(4, ciTickRight);

		}
		else if(hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_GT)) {
			double criticalPoint = this.getNormalCriticalPoint(alpha);;
			////////System.out.println("2 add to listA mu0Tick criticalPoint =  " +criticalPoint);
			ciTickLeft = (mu0 - criticalPoint * sampleSE) + "";
			ciTickRight = (mu0 + criticalPoint * sampleSE) + "";
			listA.add(3, ciTickLeft);
			listA.add(4, ciTickRight);
		}
		else {
			double criticalPoint = this.getNormalCriticalPoint(alpha);
			////////System.out.println("3 add to listA mu0Tick criticalPoint =  " +criticalPoint);
			ciTickLeft = (mu0 - criticalPoint * sampleSE) + "";
			ciTickRight = (mu0 + criticalPoint * sampleSE) + "";
			listA.add(3, ciTickLeft);
			listA.add(4, ciTickRight);
		}

		listA.add(5, hypothesisType);

		graphSampleMean.setListOfTicks(listA);
		graphSampleMean.setOutlineColor1(Color.BLUE);
		graphSampleMean.setOutlineColor2(Color.RED);
		graphSampleMean.setFillArea(true);

		graphSampleMean.validate();
		graphSampleMean.repaint();
		//listA = null;

		graphSampleMean.setPreferredSize(new Dimension(400, 120));
		graphSampleMean.setBackground(Color.WHITE);

		//////System.out.println(" i need to doSampleMeanNormalCurve");
		this.visualizePanel.validate();
		innerPanel.add(graphSampleMean);
		legendPanel.setVisible(true);
		visualizePanel.repaint();
		visualizePanel.validate();

	}
	protected void doZTestNormalCurve()
	{
		
		//////////////System.out.println("doZTestNormalCurve ");
		graphZScore.setType(0);
		graphZScore.addMouseListener(this);
		double minOfMu = Math.min(xValue, mu0ZTest - 4 * sigmaZTest);
		double maxOfMu = Math.max(xValue, mu0ZTest + 4 * sigmaZTest);
		graphZScore.setHistogramRight(maxOfMu);
		graphZScore.setHistogramLeft(minOfMu);

		double minOfModel = 0, maxOfModel = 0; // of the model doamin.

		//minOfModel = Math.floor(minOfMu - 4 * sampleSE);//modelObject.getGraphLowerLimit();
		//maxOfModel = Math.ceil(maxOfMu + 4 * sampleSE);//modelObject.getGraphUpperLimit();

		float[] yFloat = new float[yData.length];
		for (int i = 0; i < yData.length; i++) {
			yFloat[i] = (float) yData[i];
		}

		NormalDistribution normal = new NormalDistribution(mu0ZTest, sigmaZTest);
		//////////////System.out.println("doZTestNormalCurve xValue = " + xValue);
		//////////////System.out.println("doZTestNormalCurve mu0ZTest = " + xValue);
		//////////////System.out.println("doZTestNormalCurve xValue = " + xValue);
		//////////////System.out.println("doZTestNormalCurve zScore = " + zScore);


		graphZScore.setPlotYMax(normal.getMaxDensity());
		graphZScore.setPlotYMin(0);
		graphZScore.setPlotXMax(maxOfMu);
		graphZScore.setPlotXMin(minOfMu);
		//////////////System.out.println("doZTestNormalCurve modelObject = " + modelObject);

		/************* mu0 ****************/
		modelObject.fitCurve(yFloat, (float)minOfModel, (float)maxOfModel, mu0ZTest, sigmaZTest, true, true);
		double[] modelXData0 = modelObject.returnModelX();
		double[] modelYData0 = modelObject.returnModelY();

		int modelType = modelObject.getModelType();
		//////////////System.out.println("doZTestNormalCurve modelType = " + modelType);

		/************* muA ****************/

		modelObject.fitCurve(yFloat, (float)minOfModel, (float)maxOfModel, mu0ZTest, sigmaZTest, true, true);
		double[] modelXDataA = modelObject.returnModelX();
		double[] modelYDataA = modelObject.returnModelY();
		modelType = modelObject.getModelType();
		//int arrayLegnth = modelXData.length;
		//////////////System.out.println("doZTestNormalCurve graphZScore = " + graphZScore);

		graphZScore.setModelCount(modelObject.getModelCount());
		graphZScore.setModel(modelXData0.length, modelXData0, modelYData0);
		String mu0Tick = "" + mu0ZTest;
		String sigmaTick = "" + sigmaZTest;
		int indexOfPoint = -1;
		String ciTickLeft = "", ciTickRight = "";
		ArrayList<String> listA = new ArrayList<String>();
		//////////////////////System.out.println("add to listA mu0Tick " + mu0Tick);
		listA.add(0, mu0Tick);
		listA.add(1, "");
		listA.add(2, sigmaTick);
		if (zScore < 0 ) {
			hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_LT;
		} else if (zScore > 0 ) {
			hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_GT;
		} else {
			hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_NE;
		}
		//////////////System.out.println("call graphZScore hypothesisType = " + hypothesisType);
		// there is really no user-specified hypothesisType for z score test.
		// hypothesisType is set and used here to determine which side of the curve has color.
		if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_LT)) {
			ciTickLeft = (mu0ZTest - 1.645 * sigmaZTest) + "";
			ciTickRight = (mu0ZTest + 1.645 * sigmaZTest) + "";
			listA.add(3, ciTickLeft);
			listA.add(4, ciTickRight);

		}
		else if(hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_GT)) {
			ciTickLeft = (mu0ZTest - 1.645 * sigmaZTest) + "";
			ciTickRight = (mu0ZTest + 1.645 * sigmaZTest) + "";
			listA.add(3, ciTickLeft);
			listA.add(4, ciTickRight);
		}
		else {
			ciTickLeft = (mu0ZTest - 1.96 * sigmaZTest) + "";
			ciTickRight = (mu0ZTest + 1.96 * sigmaZTest) + "";
			listA.add(3, ciTickLeft);
			listA.add(4, ciTickRight);
		}

		listA.add(5, hypothesisType);
		hypothesisType = null;
		//////////////System.out.println("call graphZScore hypothesisType = " + hypothesisType);

		graphZScore.setListOfTicks(listA);
		graphZScore.setOutlineColor1(Color.BLUE);
		graphZScore.setOutlineColor2(Color.BLUE);
		graphZScore.setFillArea(true);

		graphZScore.validate();
		//////////////System.out.println(" call graphZScore.repaint() graphZScore = " + graphZScore);
		graphZScore.repaint();
		//listA = null;

		graphZScore.setPreferredSize(new Dimension(400, 120));
		graphZScore.setBackground(Color.PINK);

		this.visualizePanel.validate();
		innerPanel.add(graphZScore);
		legendPanel.setVisible(true);
		visualizePanel.repaint();
		visualizePanel.validate();
		
	} // end doZTestNormalCurve

	protected void resetParameterSelect(){

		checkSampleSizeBox.setSelected(true);
		checkPowerBox.setSelected(false);
		criticalValueBox.setSelected(false);

		useSampleSize = true;
		usePower = false;
		useCV = false;

		useNE = false;
		useLT = false;
		useGT = false;
		checkNE.setSelected(useNE); // default
		checkLT.setSelected(useLT);
		checkGT.setSelected(useGT);

		sampleSizeText.setText("");
		sigmaText.setText("");
		mu0Text.setText("");
		muAText.setText("");
		powerText.setText("");
		xValueText.setText("");
		sigmaZTestText.setText("");
		mu0ZTestText.setText("");

		powerPanel.setVisible(false);
		sampleSizePanel.setVisible(true);

		alphaCombo.setSelectedIndex(1);

		alpha = 0;
		sampleSize = 0;
		sigma = 0;
		mu0 = Double.POSITIVE_INFINITY;
		muA = Double.POSITIVE_INFINITY;
		power = 0;
		zScore = 0;
		meanPlotPoints = null;
		powerPlotPoints = null;
		//resetGraph();
		repaint();
	}


	protected void setVisualizePanel() {
		visualizePanel.setLayout(new BoxLayout(visualizePanel, BoxLayout.Y_AXIS));
		visualizePanel.setPreferredSize(new Dimension(400, 400));
		visualizePanel.setBackground(Color.WHITE);
/*
		visualizePanel2.setLayout(new BoxLayout(visualizePanel2, BoxLayout.Y_AXIS));
		visualizePanel2.setPreferredSize(new Dimension(400, 400));
		visualizePanel2.setBackground(Color.WHITE);
*/
		//legendPanel = new JPanel();

		Dimension top = graphRawData.getSize(null);

		graphRawData.setPreferredSize(new Dimension(400, 60));
		graphSampleMean.setPreferredSize(new Dimension(400, 120));

		legendPanel.setBackground(Color.WHITE);
		JLabel descriptionTop = new JLabel("   Top: normal curves based on the data. Blue for the null hypothesis; red for the alternative hypothesis.   ");
		JLabel descriptionBottom = new JLabel("   Bottom: normal curves of two sample means.   ");

		legendPanel.add(descriptionTop, BorderLayout.NORTH);
		legendPanel.add(descriptionBottom, BorderLayout.CENTER);
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
		legendPanel.setBackground(Color.PINK);
		graphSampleMean.addMouseMotionListener(this);

		visualizePanel.removeAll();
		innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		visualizePanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		visualizePanel.setLayout(new BoxLayout(visualizePanel, BoxLayout.Y_AXIS));

		innerPanel.add(legendPanel);
		//visualizePanel.setVisible(false);

		legendPanel.setVisible(false);
		/*
		visualizePanel2.removeAll();
		visualizePanel2.add(graphRawData);
		visualizePanel2.add(graphSampleMean);
		visualizePanel2.add(legendPanel);
		*/
	}
	public void reset() {
		super.reset();
		//randomDataStep = false;
		//useSampleSize = false;
		//usePower = false;
		//useCV = false;
		legendPanel.removeAll();
		setVisualizePanel();
	}


	protected void resetGraph()
	{
		////////////////////////////System.out.println("reset graph in NP");
		JFreeChart chart = chartFactory.createChart(); // an empty  chart
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(400,300));
		graphPanel.removeAll();
		graphPanel.repaint();
		graphPanel.add(chartPanel);

	}
	protected void resetVisualize()
	{
		////////////////////////System.out.println("reset Visualize in NormalPower");
		graphSampleMean.removeAll();
		graphRawData.removeAll();
		graphZScore.removeAll();

		visualizePanel.removeAll();
		visualizePanel.setPreferredSize(new Dimension(400,480));
		visualizePanel.repaint();

		//visualizePanel2.removeAll();
		//visualizePanel2.setPreferredSize(new Dimension(400,480));
		//visualizePanel2.repaint();

	}
    public void mouseDragged(MouseEvent evt) {
		//////////////System.out.println("mouseDragged     evt = " + evt);
		//int x = evt.getX();

		xPosition = evt.getX();
		yPosition = evt.getY();
		double xPositionOld = muA;
		double yPositionOld = 0;
		double xPositionNew = graphSampleMean.xGraphInverse(xPosition);
		double yPositionNew = graphSampleMean.yGraphInverse(yPosition);


		double diff = xPositionNew - xPositionOld;
		//double scale = Math.abs(mu0 - muA) / 100;
		//////////////System.out.println("mouseDragged mu0 = " + mu0 + ", muA = " + muA + " sampleSE = " + sampleSE + ", scale = " + scale);

		if (/*Math.abs(xPositionOld - xPositionNew) > scale  && */graphSampleMean.withinSampleMeanCurve(xPositionNew, yPositionNew)) {

			muA = muA + diff;
			graphSampleMean.setSampleMeanOption(true);
			//////////////////////System.out.println("New muA = " + muA);
			try {
				doSampleMeanNormalCurve();
				//doAnalysis();
			} catch (Exception e) {
				//////////////////////System.out.println(e);
			}
			muAText.setText(muA + "");
			repaint();
			////System.gc();
		}
    }


	private static double getNormalCriticalPoint(double alpha) { // unsigned, all positive.
		// alpha = significant level for the Normal distribution.
		// the numbers are from R code qnorm(1 - alpha/2)
		/*	R code:
			> format(qnorm(1 - 0.1/2), digits = 15)
			[1] "1.64485362695147"
			> format(qnorm(1 - 0.05/2), digits = 15)
			[1] "1.95996398454005"
			> format(qnorm(1 - 0.01/2), digits = 15)
			[1] "2.5758293035489"
			> format(qnorm(1 - 0.001/2), digits = 15)
			[1] "3.29052673149193"
		*/

		double cp = 0;
		if (alpha == 0.1) {
			cp = 1.64485362695147;
		} else if (alpha == 0.05) {
			cp = 1.95996398454005;
		} else if (alpha == 0.01) {
			cp = 2.5758293035489;
		} else if (alpha == 0.001) {
			cp = 3.29052673149193;
		}

		return cp;
	}

}

