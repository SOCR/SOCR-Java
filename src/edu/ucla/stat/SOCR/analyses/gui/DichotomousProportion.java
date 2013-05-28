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

import java.beans.*;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.DichotomousProportionExamples;

public class DichotomousProportion extends Analysis implements PropertyChangeListener {

	//public JTabbedPane tabbedPanelContainer;
	private JToolBar toolBar;
	private Frame frame;

	private JComboBox alphaCombo = new JComboBox(new String[]{"0.1","0.05","0.01","0.001"}); //

	private	JPanel alphaPanel = null;
	private	JLabel alphaLabel = new JLabel("Select Significance Level:");
	private double alpha = 0.05;

	DichotomousProportionResult result;
	double sampleProportionP = 0, sampleProportionQ = 0;
	double adjustedProportionP = 0, adjustedProportionQ = 0;
	double sampleSEP = 0, sampleSEQ = 0;
	double adjustedSEP = 0, adjustedSEQ = 0;
	String ciTextPString = null, ciTextQString = null;
	String[] valueList = null;
	int[] sampleProportion = null;
	int yLength = 0;
	String dependentHeader =  null;

	/**Initialize the Analysis*/
	public void init(){
		mapIndep= false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		analysisType = AnalysisType.DICHOTOMOUS_PROPORTION;
		analysisName = "Proportion Test for Dichotomous Distribution";
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = DichotomousProportionExamples.availableExamples;


		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName(analysisName);
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

	//	tools2.remove(addButton2);
	//	tools2.remove(removeButton2);
	//	tools2.remove(removeButton2);
		depLabel.setText(VARIABLE);
	//	indLabel.setText("");
	//       listIndepRemoved.setBackground(Color.LIGHT_GRAY);
    //      mappingInnerPanel.remove(listIndepRemoved);

		alphaCombo.addActionListener(this);
		alphaCombo.addMouseListener(this);
		alphaCombo.setEditable(false);
		alphaCombo.setSelectedIndex(1);

		validate();
		reset();
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
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();


		try {
			dependentHeader= columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		} catch (Exception e) {
		}


		if (! hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}

		if (dependentIndex < 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}

		Data data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		String cellValue = null;

		ArrayList<String> yList = new ArrayList<String>();
		yLength=0;
	
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
						//System.out.println("Exception In inner catch: " + e );

					}
			}

		} catch (Exception e) {
			//System.out.println("Exception In outer catch: " + e );
		}


		String[] y = new String[yLength];
		for (int i = 0; i < yLength; i++) {
			y[i] = (String)yList.get(i);
			//resultPanelTextArea.append("\nY = "+y[i] );
		}

		/******* add option to get test mean not zero, user input. *******/

		 //String alphaMessage = "Enter the significance level for test mean. Default is zero.";
		 //String alphaMeanWarning = "You didn't enter a number. Default zero will be used. Click on CALCULATE if you'd like to change it.";
		alpha = 0.05;
		try {
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		} catch (Exception e) {
			alpha = 0.05;
			////////////System.out.println("in gui use random data Exception alpha = " + alpha);
		}
		data.appendX("X", y, DataType.FACTOR);

		data.setParameter(analysisType, edu.ucla.stat.SOCR.analyses.model.DichotomousProportion.SIGNIFICANCE_LEVEL, alpha + "");

		result = null;
		try {
			result = (DichotomousProportionResult)data.getAnalysis(AnalysisType.DICHOTOMOUS_PROPORTION);
		} catch (Exception e) {
			//System.out.println(e);
		}
		/* every one has have its own, otherwise one Exception spills the whole. */


		
		try {
			valueList = (String[])result.getValueList();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		try {
			sampleProportion = (int[])result.getSampleProportion();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			sampleProportionP = result.getSampleProportionP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			////////////System.out.println("Exception = " + e);
		}

		try {
			sampleProportionQ = result.getSampleProportionQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		try {
			adjustedProportionP = result.getAdjustedProportionP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			////////////System.out.println("Exception = " + e);
		}

		try {
			adjustedProportionQ = result.getAdjustedProportionQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		try {
			sampleSEP = result.getSampleSEP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			////////////System.out.println("Exception = " + e);
		}
		try {
			sampleSEQ = result.getSampleSEQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			adjustedSEP = result.getAdjustedSEP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			adjustedSEQ = result.getAdjustedSEQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}



		try {
			ciTextPString = result.getCITextP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			ciTextQString = result.getCITextQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		updateResults();
		

	}

	public void updateResults(){

		if (result==null)
			return;
		
		result.setDecimalFormat(dFormat);
		
		resultPanelTextArea.setText("\n");//clear first
		resultPanelTextArea.append("\tSample size = " + yLength + " \n" );
		resultPanelTextArea.append("\n\tVariable  = " + dependentHeader + " \n" );
		resultPanelTextArea.append("\n\tGroup " + valueList[0] + ": \tFrequency = " + sampleProportion[0]);

		resultPanelTextArea.append("\n\tGroup " + valueList[1] + ": \tFrequency = " + sampleProportion[1]);

		resultPanelTextArea.append("\n\n\tResults of Dichotomous Proportion Test:\n" );
		resultPanelTextArea.append("\n\tSignificance Level = " + result.getFormattedDouble(alpha));



		resultPanelTextArea.append("\n\n\t********** Without Adjustment **********"  );
		resultPanelTextArea.append("\n\tGroup " + valueList[0] + ": \n\tProportion = " + result.getFormattedDouble(sampleProportionP) + "\n\tStandard Error = " + result.getFormattedDouble(sampleSEP));

		resultPanelTextArea.append("\n\n\tGroup " + valueList[1] + ": \n\tProportion = " + result.getFormattedDouble(sampleProportionQ) + "\n\tStandard Error = " + result.getFormattedDouble(sampleSEQ));
		
		String ciString = null;
		try {
			ciTextPString = result.getCiString();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		
		String ciWidth=null,lowerP=null, upperP=null, lowerQ=null, upperQ=null;

		try {
			 ciWidth= result.getFormattedDouble(result.getCiWidth());
			 lowerP= result.getFormattedDouble(result.getLowerP());
			 upperP= result.getFormattedDouble(result.getUpperP());
			 lowerQ= result.getFormattedDouble(result.getLowerQ());
			 upperQ= result.getFormattedDouble(result.getUpperQ());
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		
		resultPanelTextArea.append("\n\n\t********** With Adjustment **********"  );
		resultPanelTextArea.append("\n\tGroup " + valueList[0] + ": \n\tProportion = " +
				result.getFormattedDouble(adjustedProportionP) + "\n\tStandard Error = " + result.getFormattedDouble(adjustedSEP));
		//resultPanelTextArea.append("\n\t" + ciTextPString);
		resultPanelTextArea.append("\n\t" + ciString + "% CI = " + result.getFormattedDouble(adjustedProportionP) + " +/- " + ciWidth + "\n\t= (" + lowerP + ", " + upperP + ")");

		resultPanelTextArea.append("\n\n\tGroup " + valueList[1] + ": \n\tProportion = " + result.getFormattedDouble(adjustedProportionQ) + "\n\tStandard Error = " + result.getFormattedDouble(adjustedSEQ));
		//resultPanelTextArea.append("\n\t" + ciTextQString);
		resultPanelTextArea.append("\n\t" + ciString + "% CI = " + result.getFormattedDouble(adjustedProportionQ) + " +/- " + ciWidth + "\n\t= (" + lowerQ + ", " + upperQ + ")");


		resultPanelTextArea.setForeground(Color.BLUE);

	
	}
	
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

		//System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));

			//System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
   }
	public Container getDisplayPane() {
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		return this.getContentPane();
	}
    public String getOnlineDescription(){
        return new String("http://en.wikipedia.org/wiki/Statistical_hypothesis_testing");
    }
    protected void setInputPanel() {
		//System.out.println("dp setInputPanel");
		inputPanel.removeAll();
	
		inputPanel.setPreferredSize(new Dimension(400, 400));
		//inputPanel.setBackground(Color.WHITE);
		alphaPanel = new JPanel();
		alphaPanel.add(alphaLabel);
		alphaPanel.add(alphaCombo);
		inputPanel.add(alphaPanel);

	}
     public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);

		if (event.getSource() == alphaCombo){
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		}
	}

	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);

		if (event.getSource() == alphaCombo){
			try {
				alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
			} catch (Exception e) {
			}
		}

	}
}

