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
import edu.ucla.stat.SOCR.analyses.example.TwoPairedTExamples;

/** Two Paired Sample T Test */
public class TwoPairedT extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.

	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	
	int df = 0;
	double meanX = 0, meanY = 0, meanDiff = 0;
	double sampleVar = 0;
	double tStat = 0;
	double pValueOneSided = 0, pValueTwoSided = 0;
	int xLength = 0;
	int yLength = 0;
	String varHeader0 =null;
	String varHeader1 =null;
	TwoPairedTResult result = null;

	/**Initialize the Analysis*/
	public void init(){

		showInput = false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		super.init();

		analysisType = AnalysisType.TWO_PAIRED_T;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = TwoPairedTExamples.availableExamples;
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		//ScatterPlot = new RegressionScatterPlot();
		//graphPanel.add( "Center", ScatterPlot);
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
		varHeader0 = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		varHeader1 = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();

		Data data = new Data();
		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<String> yList = new ArrayList<String>();
		xLength=0;
		yLength=0;
	
		try {
			for (int k =0; k < dataTable.getRowCount(); k++) {
				try {
					cellValue = ((String)dataTable.getValueAt(k, dependentIndex)).trim();
					if (cellValue != null && !cellValue.equals("")) {
						yList.add(yLength , cellValue);
						//System.out.println("adding y:"+cellValue+" at "+yLength);
						yLength++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?
						//e.printStackTrace();
					}
			}

			for (int k =0; k < dataTable.getRowCount(); k++) {
				try {
					cellValue = ((String)dataTable.getValueAt(k, independentIndex)).trim();
					if (cellValue != null && !cellValue.equals("")) {
						xList.add(xLength , cellValue);
					//	System.out.println("adding x:"+cellValue+" at "+xLength);
						xLength++;
					}
					else {
						continue; // to the next for
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			System.out.println("Exception In outer catch: " + e );
		}
		if (xLength <= 0 || yLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}

		double[] x = new double[xLength];
		double[] y = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			y[i] = Double.parseDouble((String)yList.get(i));
		//	System.out.println("getting Y = "+y[i] );
			//resultPanelTextArea.append("\nY = "+y[i] );
		}
		for (int i = 0; i < xLength; i++) {
			x[i] = Double.parseDouble((String)xList.get(i));
		//	System.out.println("getting X = "+x[i] );
			//resultPanelTextArea.append("\nX = "+x[i] );
		}

		// Set up data first
		// Call the Controller method getAnalysis() delegate the work to Model

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);

		try {
			result = (TwoPairedTResult)data.getAnalysis(AnalysisType.TWO_PAIRED_T);
		} catch (Exception e) {
		}
		
		try {
			df = result.getDF();//((Integer)result.getTexture().get(TwoPairedTResult.DF)).intValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanX = result.getMeanX();//((Double)result.getTexture().get(TwoPairedTResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanY = result.getMeanY();//((Double)result.getTexture().get(TwoPairedTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanDiff = result.getMeanDifference();//((Double)result.getTexture().get(TwoPairedTResult.MEAN_DIFF)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			sampleVar = result.getSampleVariance();//((Double)result.getTexture().get(TwoPairedTResult.SAMPLE_VAR)).doubleValue();
		} catch (Exception e) {
			//
		}
		try {
			tStat = result.getTStat();//((Double)result.getTexture().get(TwoPairedTResult.T_STAT)).doubleValue();
		} catch (Exception e) {
			//
		}
		try {
			pValueOneSided = result.getPValueOneSided();//((String)result.getTexture().get(TwoPairedTResult.P_VALUE));
		} catch (Exception e) {
			//
		}
		try {
			pValueTwoSided = result.getPValueTwoSided();//((String)result.getTexture().get(TwoPairedTResult.P_VALUE));
		} catch (Exception e) {
			//System.out.println("pValueTwoSided e = " + e);
		}
		//System.out.println("-----------");

		updateResults();
	}

	public void updateResults(){

		if (result==null)
		return;
	
		result.setDecimalFormat(dFormat);	
	

		setDecimalFormat(dFormat);
	
		resultPanelTextArea.setText("\n"); //clear first
	
		resultPanelTextArea.append("\n\tSample size = " + xLength );

		resultPanelTextArea.append("\n\tVariable 1 = " + varHeader0  );
		resultPanelTextArea.append("\n\tVariable 2 = " + varHeader1  );
		resultPanelTextArea.append("\n\n\tResults of Two Paired Sample T-Test:\n" );


		resultPanelTextArea.append("\n\tLet Difference = Variable 2 - Variable 1");

		resultPanelTextArea.append("\n\n\tMean of Variable 1 = " + result.getFormattedDouble(meanY));
		resultPanelTextArea.append("\n\tMean of Variable 2 = " + result.getFormattedDouble(meanX));
		resultPanelTextArea.append("\n\tMean of Difference  = " + result.getFormattedDouble(meanDiff));


		resultPanelTextArea.append("\n\tVariance of Difference  = " + result.getFormattedDouble(sampleVar));
		resultPanelTextArea.append("\n\tStandard Error of Difference  = " + result.getFormattedDouble(sampleVar / Math.sqrt(xLength)));


		resultPanelTextArea.append("\n\tDegrees of Freedom = " + df);

		resultPanelTextArea.append("\n\tT-Statistics             = " + result.getFormattedDouble(tStat));
		resultPanelTextArea.append("\n\tOne-Sided P-Value = " + result.getFormattedDouble(pValueOneSided));
		resultPanelTextArea.append("\n\tTwo-Sided P-Value = " + result.getFormattedDouble(pValueTwoSided));

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

