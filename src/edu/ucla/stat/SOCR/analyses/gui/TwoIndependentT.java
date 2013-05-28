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

/* 	modified annie che 200508.
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
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.analyses.example.TwoIndependentTExamples;
import java.util.ArrayList;

/** Two Independent T Test */
public class TwoIndependentT extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.

	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;

	/**Initialize the Analysis*/
	public void init(){

		showInput = false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		analysisType = AnalysisType.TWO_INDEPENDENT_T;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = TwoIndependentTExamples.availableExamples;
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		depLabel.setText(VARIABLE_1);
		indLabel.setText(VARIABLE_2);
		validate();
		reset();
	}


	/** Create the actions for the buttons */
       protected void createActionComponents(JToolBar toolBar){
       		super.createActionComponents(toolBar);
      }


	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){
	}

	/**This method defines the specific statistical Analysis to be carried our on the user specified data. ANOVA is done in this case. */
	public void doAnalysis(){
		////System.out.println("TwoIndependentT start doAnalysis");
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();

		if (! hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}

		String varHeader0 = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		String varHeader1 = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();
		//System.out.println("TwoIndependentT varHeader0 = "+varHeader0);
		//System.out.println("TwoIndependentT varHeader1 = "+varHeader0);
		//JOptionPane.showMessageDialog(this, "In regression, DependentIndex = "+dependentIndex);
		//JOptionPane.showMessageDialog(this, "In regression, IndependentIndex = "+independentIndex);

		Data data = new Data();
		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/


		int xLength = 0;
		int yLength = 0;
		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<String> yList = new ArrayList<String>();
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

			for (int k =0; k < dataTable.getRowCount(); k++) {
				try {
					cellValue = ((String)dataTable.getValueAt(k, independentIndex)).trim();
					if (cellValue != null && !cellValue.equals("")) {
						xList.add(xLength , cellValue);
						xLength++;
					}
					else {
						continue; // to the next for
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			//System.out.println("Exception In outer catch: " + e );
		}
		if (xLength <= 0 || yLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}

		double[] x = new double[xLength];
		double[] y = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			y[i] = Double.parseDouble((String)yList.get(i));
			//resultPanelTextArea.append("\nY = "+y[i] );

		}
		for (int i = 0; i < xLength; i++) {
			x[i] = Double.parseDouble((String)xList.get(i));
			//resultPanelTextArea.append("\nX = "+x[i] );

		}

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);

		TwoIndependentTResult result = null;
		try {
			result = (TwoIndependentTResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_T);
		} catch (Exception e) {
		}
		// Retreive the data from Data Object using HashMap
		int df = 0;
		double dfAdjusted = 0;
		double meanX = 0, meanY = 0, varX = 0, varY = 0, sdX = 0, sdY = 0;
		double poolSampleVar = 0, poolSampleSD = 0;
		double tStatPooled = 0;
		double pValueOneSidedPooled = 0, pValueTwoSidedPooled = 0;
		try {
			df = result.getDF();//((Integer)result.getTexture().get(TwoIndependentTResult.DF)).intValue();
		} catch (Exception e) {
			//showError("Excepion1 " +e + "");
		}
		try {
			dfAdjusted = result.getDFAdjusted();//((Integer)result.getTexture().get(TwoIndependentTResult.DF)).intValue();
		} catch (Exception e) {
			//showError("Excepion1 " +e + "");
		}

		try {
			meanX = result.getMeanX();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanY = result.getMeanY();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			varX = result.getSampleVarianceX();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			varY = result.getSampleVarianceY();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			sdX = result.getgetSampleSDX();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			sdY = result.getgetSampleSDY();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}


		try {
			poolSampleVar = result.getPoolSampleVariance();//((Double)result.getTexture().get(TwoIndependentTResult.SAMPLE_VAR)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");

		}

		try {
			poolSampleSD = result.getPoolSampleSD();//((Double)result.getTexture().get(TwoIndependentTResult.SAMPLE_VAR)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");

		}
		try {
			tStatPooled = result.getTStatPooled();//((Double)result.getTexture().get(TwoIndependentTResult.T_STAT)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion3 " +e + "");
		}
		try {
			pValueOneSidedPooled = result.getPValueOneSidedPooled();//((String)result.getTexture().get(TwoIndependentTResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			pValueTwoSidedPooled = result.getPValueTwoSidedPooled();//((String)result.getTexture().get(TwoIndependentTResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}

		//resultPanelTextArea.append("\n\tVariable 1 = " + varHeader0);

		//resultPanelTextArea.append("\n\tVariable 2 = " + varHeader1);

		resultPanelTextArea.append("\n\n\tResult of Two Independent Sample T-Test:\n" );

		resultPanelTextArea.append("\n\tVariable 1 = " + varHeader0);
		resultPanelTextArea.append("\n\tSample Size = " + yLength );
		resultPanelTextArea.append("\n\tSample Mean = " + meanY);
		resultPanelTextArea.append("\n\tSample Variance = " + varY);
		resultPanelTextArea.append("\n\tSample SD = " + sdY);

		resultPanelTextArea.append("\n\n\tVariable 2 = " + varHeader1);
		resultPanelTextArea.append("\n\tSample Size = " + xLength);
		resultPanelTextArea.append("\n\tSample Mean = " + meanX);
		resultPanelTextArea.append("\n\tSample Variance = " + varX);
		resultPanelTextArea.append("\n\tSample SD = " + sdX);

		resultPanelTextArea.append("\n\n\tDegrees of Freedom = " + df);
		resultPanelTextArea.append("\n\tPooled Sample Variance = " + poolSampleVar);
		resultPanelTextArea.append("\n\tPooled Sample SD = " + poolSampleSD);

		resultPanelTextArea.append("\n\tT-Statistics = " + tStatPooled);
		resultPanelTextArea.append("\n\tOne-Sided P-Value = " + AnalysisUtility.enhanceSmallNumber(pValueOneSidedPooled));
		resultPanelTextArea.append("\n\tTwo-Sided P-Value = " + AnalysisUtility.enhanceSmallNumber(pValueTwoSidedPooled));
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