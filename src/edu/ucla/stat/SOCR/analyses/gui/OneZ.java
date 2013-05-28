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
import edu.ucla.stat.SOCR.analyses.example.OneTExamples;

public class OneZ extends Analysis implements PropertyChangeListener {

	//public JTabbedPane tabbedPanelContainer;
	private JToolBar toolBar;
	private Frame frame;
	
	public static double sampleMeanInput = 0, sampleMeanDiff = 0, sampleVar = 0;
	double zStat = 0, pValueOneSided = 0, pValueTwoSided = 0;
	int yLength = 0;
	public static double p_value, p_valueTwoSided, CDF_value=0;
	public static double z_stat=0, testMean = 0;
        public static double testVariance = 1;
	String dependentHeader =  null;
	OneZResult result;
	
	/**Initialize the Analysis*/
	public void init(){
		mapIndep= false;
		showInput = false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		analysisType = AnalysisType.ONE_T;
		analysisName = "One Sample T Test";
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = OneTExamples.availableExamples;

		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName(analysisName);
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

		tools2.remove(addButton2);
          tools2.remove(removeButton2);
          tools2.remove(removeButton2);
          depLabel.setText(VARIABLE);
		indLabel.setText("");
           listIndepRemoved.setBackground(Color.LIGHT_GRAY);
          mappingInnerPanel.remove(listIndepRemoved);

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


		// DO NOT NEED TO PRINT THIS FOR ONE_T
		// ONE_T has only one variable, we use Y to contain it (not X).
		// Reason of not using X but Y: Y is at the top of the panel.
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
					}
			}

		} catch (Exception e) {
			//////////System.out.println("Exception In outer catch: " + e );
		}


		double[] y = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			y[i] = Double.parseDouble((String)yList.get(i));
			//resultPanelTextArea.append("\nY = "+y[i] );
		}

		/******* add option to get test mean not zero, user input. *******/

		 String testMeanMessage = "Enter a number for test mean. Default is zero.";
                 String testVarianceMessage = "Enter a number for test variance. Default is one.";
		 String testMeanWarning = "You didn't enter a number. Default zero will be used. \nClick on CALCULATE if you'd like to change it. \nOr, click on RESULT to see the results.";
                 String testVarianceWarning = "You didn't enter a number. Default variance = 1 will be used. \nClick on CALCULATE if you'd like to change it. \nOr, click on RESULT to see the results.";



		 try {
			 testMean = Double.parseDouble(JOptionPane.showInputDialog(testMeanMessage));
		 } catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, testMeanWarning);
			 //testMean = Double.parseDouble(JOptionPane.showInputDialog(testMeanMessage));
		 } catch (Exception e) {
		 }
                 try {
			 testVariance = Double.parseDouble(JOptionPane.showInputDialog(testVarianceMessage));
		 } catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, testVarianceWarning);
			 //testMean = Double.parseDouble(JOptionPane.showInputDialog(testMeanMessage));
		 } catch (Exception e) {
		 }
                 
		//System.out.println("in gui testMean = " + testMean);
		data.appendY("X", y, DataType.QUANTITATIVE);
		data.appendY("X", y, DataType.QUANTITATIVE);
		data.setParameter(analysisType, edu.ucla.stat.SOCR.analyses.model.OneZ.TEST_MEAN, testMean + "");
                data.setParameter(analysisType, edu.ucla.stat.SOCR.analyses.model.OneZ.TEST_VARIANCE, testVariance + "");

		result = null;
		try {
			result = (OneZResult)data.getAnalysis(AnalysisType.ONE_Z);
		} catch (Exception e) {
			////////System.out.println(e);
		}
		/* every one has have its own, otherwise one Exception spills the whole. */

		

		try {
			sampleMeanInput = result.getSampleMeanInput();//((Double)result.getTexture().get(OneTResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
                }
            
		try {
			sampleMeanDiff = result.getSampleMean();//((Double)result.getTexture().get(OneTResult.SAMPLE_MEAN)).doubleValue();
		} catch (NullPointerException e) {
			//////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			sampleVar = result.getSampleVariance();//((Double)result.getTexture().get(OneTResult.SAMPLE_VAR)).doubleValue();
		} catch (NullPointerException e) {
			//////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		
		try {
			zStat = result.getZStat();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			pValueOneSided = result.getPValueOneSided();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			pValueTwoSided = result.getPValueTwoSided();//((String)result.getTexture().get(TwoPairedTResult.P_VALUE));
		} catch (Exception e) {
			////System.out.println("pValueTwoSided e = " + e);
		}
			////System.out.println(" pValueTwoSided= " + pValueTwoSided);

	
		String p_valueS = null;
		z_stat = sampleMeanDiff/Math.sqrt(testVariance/yLength);
                
                System.out.println("testMean, testVariance, SampleMean, SampleMeanDiff " + testMean + " " + testVariance + " " + sampleMeanInput + " " + sampleMeanDiff);
                
		CDF_value =
			 (new edu.ucla.stat.SOCR.distributions.NormalDistribution(testMean, Math.sqrt(testVariance))).getCDF(z_stat*Math.sqrt(testVariance)+testMean);
                if (z_stat >= 0)
                    p_value = 1 - CDF_value;
                else
                    p_value = CDF_value;
                
                p_valueTwoSided =
			 2*p_value;
		p_valueS = AnalysisUtility.enhanceSmallNumber(p_value);
                
		updateResults();
	}

	public void updateResults(){

		if (result==null)
		return;
	
		result.setDecimalFormat(dFormat);	
	

	setDecimalFormat(dFormat);
	
	resultPanelTextArea.setText("\n"); //clear first
	resultPanelTextArea.append("\n\tSample size = " + yLength + " \n" );

	resultPanelTextArea.append("\n\tVariable  = " + dependentHeader + " \n" );
	resultPanelTextArea.append("\n\tTest against mean of " + result.getFormattedDouble(testMean) + ", variance of " +  result.getFormattedDouble(testVariance) + "\n" );


	resultPanelTextArea.append("\n\tResult of One Sample Z-Test:\n" );


	resultPanelTextArea.append("\n\tSample Mean of " + dependentHeader + " = " + result.getFormattedDouble(sampleMeanInput));
	resultPanelTextArea.append("\n\tSample Mean of Difference    = " + result.getFormattedDouble(sampleMeanDiff));
	resultPanelTextArea.append("\n\n\tSample Variance     = " + result.getFormattedDouble(sampleVar));

	resultPanelTextArea.append("\n\tStandard Error     = " + result.getFormattedDouble(Math.sqrt(sampleVar / yLength)));
	//resultPanelTextArea.append("\n\tT-Statistics  = " + t_stat);
	//resultPanelTextArea.append("\n\tP-Value  = " + p_valueS);


	resultPanelTextArea.append("\n\tZ-Statistics             = " + result.getFormattedDouble(z_stat));
	//resultPanelTextArea.append("\n\tOne-Sided P-Value = " + AnalysisUtility.enhanceSmallNumber(pValueOneSided));
	//resultPanelTextArea.append("\n\tTwo-Sided P-Value = " + AnalysisUtility.enhanceSmallNumber(pValueTwoSided));
	resultPanelTextArea.append("\n\tOne-Sided P-Value = " + result.getFormattedDouble(p_value));
	resultPanelTextArea.append("\n\tTwo-Sided P-Value = " + result.getFormattedDouble(p_valueTwoSided));
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

		System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));

			System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
    }
    
	public Container getDisplayPane() {
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		return this.getContentPane();
	}
	
    public String getOnlineDescription(){
        return new String("http://en.wikipedia.org/wiki/T_test");
    }
}