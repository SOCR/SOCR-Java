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
import edu.ucla.stat.SOCR.analyses.example.TwoPairedSignTestExamples;

/** The Sign-Test for Paired Samples */
public class TwoPairedSignTest extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.


	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;

	double pValue=0;
	String varHeader0=null;
	String varHeader1=null;
	int positiveDifference = 0, negativeDifference = 0;
	int numberForBinomial = 0;
	int numberOfZeroDiff=0;
	int lengthCombo=0;

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
		useStaticExample = TwoPairedSignTestExamples.availableExamples;

		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("The Sign-Test for Paired Samples");
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
		//System.out.println("varHeader0 = " + varHeader0);
		//System.out.println("varHeader1 = " + varHeader1);



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
							System.out.println("TwoPairedSignTest  e = " + e);

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
					System.out.println("TwoPairedSignTest  e = " + e);

				}
			}
		} catch (Exception e) {
			System.out.println("Exception In outer catch: " + e );
		}
		if (xLength <= 0 || yLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}
		if (xLength != yLength)
		{   //	System.out.println("Unequal sample-sizes are not " +
			//	"allowed for paired tests, xLength = "+ xLength +
			//	"; yLength = " + yLength+"!");
			resultPanelTextArea.append("Unequal sample-sizes are not " +"allowed for paired tests, xLength = " + xLength + "; yLength = " + yLength + "!" );
			return;
		 }

		double [] x = new double[xLength];
		double [] y = new double[yLength];

		for (int i = 0; i < yLength; i++)
			y[i] = Double.parseDouble((String)yList.get(i));
		for (int i = 0; i < xLength; i++)
			x[i] = Double.parseDouble((String)xList.get(i));

		double diff[] = new double[yLength];
		lengthCombo = 0;
		positiveDifference = 0;
		negativeDifference = 0;
		numberOfZeroDiff = 0;
		numberForBinomial = 0;
		for (int i = 0; i < yLength; i++) {
			diff[i] = y[i] - x[i];
			if (diff[i] != 0) {
				lengthCombo++;
			}
			//System.out.println("diff["+i+"] = " + diff[i]);
		}


		/************************************************************************/

		for (int i = 0; i < yLength; i++) {
			diff[i] = y[i] - x[i];
			if (diff[i] > 0) {
				positiveDifference++;
				numberForBinomial++;
				//System.out.println("diff  > 0 when diff["+i+"] = " + diff[i]);
			} else if (diff[i] < 0) {
				negativeDifference++;
				//System.out.println("diff  > 0 when diff["+i+"] = " + diff[i]);

			} else {
				//System.out.println("diff <= 0 when diff["+i+"] = " + diff[i]);
			}
		}
		//System.out.println("positiveDifference = " + positiveDifference);

		if (numberForBinomial < yLength/2) {
			numberForBinomial = negativeDifference; // to small, switch.
			//positiveDifference = yLength - positiveDifference;
		}

		/*double pValue = 1.0 -
			(new edu.ucla.stat.SOCR.distributions.BinomialDistribution(
			xLength, 0.5)).getCDF(positiveDifference-1);
		*/
		// change to exclude the cases of diff == 0.
		pValue = 1.0 -
			(new edu.ucla.stat.SOCR.distributions.BinomialDistribution(
			lengthCombo, 0.5)).getCDF(numberForBinomial-1);

		//System.out.println("pValue = " + pValue);
		//System.out.println("getCDF(numberForBinomial-1) = " + (new edu.ucla.stat.SOCR.distributions.BinomialDistribution(lengthCombo, 0.5)));
		//System.out.println("lengthCombo = " + lengthCombo);
		//System.out.println("numberForBinomial = " + numberForBinomial);

		/************************************************************************/
		numberOfZeroDiff = xLength - lengthCombo;
		//System.out.println("numberOfZeroDiff = " + numberOfZeroDiff);

		updateResults();
	}

	public void updateResults(){

		/*if (result==null)
		return;

		result.setDecimalFormat(dFormat);	*/


	setDecimalFormat(dFormat);

	resultPanelTextArea.setText("\n"); //clear first


	resultPanelTextArea.append("\n\tVariable 1 =" + varHeader0 + " \n" );
	resultPanelTextArea.append("\n\tVariable 2 =" + varHeader1 + " \n" );
	resultPanelTextArea.append("\n\tLet Difference = " + varHeader0 +" - "+ varHeader1);

	resultPanelTextArea.append("\n\n\tResult of Two Paired Sample Sign Test:\n" );

	resultPanelTextArea.append("\n\tNumber of Cases with Difference > 0: " + positiveDifference + " case(s).");
	resultPanelTextArea.append("\n\tNumber of Cases with Difference < 0: " + negativeDifference + " case(s).");
	resultPanelTextArea.append("\n\tNumber of Cases with Difference = 0: " +  numberOfZeroDiff + " case(s).");

	resultPanelTextArea.append("\n\n\tSign-Test Statistic = " +
			positiveDifference+ " ~ B(n="+lengthCombo+", p="+0.5+")\n");
	//resultPanelTextArea.append("\n\tOne-Sided P-Value = " +dFormat.format((0.5 * pValue))+ "\n");
	//resultPanelTextArea.append("\n\tTwo-Sided P-Value = " +dFormat.format(pValue)+ "\n");
	resultPanelTextArea.append("\n\tOne-Sided P-Value = " +dFormat.format((0.5 * pValue))+ "\n");
	resultPanelTextArea.append("\n\tTwo-Sided P-Value = " +dFormat.format(pValue)+ "\n");



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
        return new String("http://en.wikipedia.org/wiki/Sign_test"); // M-W is not paired.
    }
}