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
	separate the jri part from the model part
*/

/*
	should change all the var to Java convention
*/


package edu.ucla.stat.SOCR.analyses.jri.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.jri.data.*;
import edu.ucla.stat.SOCR.analyses.jri.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.gui.Chart;
import edu.ucla.stat.SOCR.analyses.xml.XMLComposer;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

/** this class is for logistic Regression only. */
public class LogisticRegression extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.
	// Perhaps I will figure out a better way to do it. annieche.

	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	private double[] xData = null;
	private double[] yData = null;
	private double[] predicted = null;
	private double[] residuals = null;
	private double[] sortedResiduals = null;
	private double[] sortedStandardizedResiduals = null;
	private int[] sortedResidualsIndex = null;
	private double[] sortedNormalQuantiles = null;
	private double[] sortedStandardizedNormalQuantiles = null;
	private double slope, intercept;

	private String dependentHeader = null, independentHeader = null;

	static int times = 0;

	FileDialog fileDialog;
	Frame fileDialogFrame = new Frame();
	File file;
	//FileInputStream fstream;
	private String fileName = "";
	private boolean useHeader = true;
	private String header = null;
	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	private JScrollPane graphPane= new JScrollPane();

  	/**Initialize the Analysis*/
	public void init(){
		super.init();

		analysisType = AnalysisType.LOGISTIC_REGRESSION;
		////System.out.println("SLR analysisType = " + analysisType);
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = true;
		useGraph = true;
		callServer = true;

		onlineDescription = "http://mathworld.wolfram.com/LinearRegression.html";
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

		chartFactory = new Chart();
		resetGraph();
		validate();
//		reset();
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
		resultPanelTextArea.append("\nSample size=" + dataTable.getRowCount() + " \n" );
		dependentHeader = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		independentHeader = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();
		resultPanelTextArea.append("\nDEPENDENT = " + dependentHeader + " \n" );
		resultPanelTextArea.append("\nINDEPENDENT   = "  + independentHeader + " \n" );
		Data data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/
		int xLength = 0;
		int yLength = 0;
		//resultPanelTextArea.append("\nRESULT:\n" );
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
			//////////////System.out.println("Exception In outer catch: " + e );
		}

		// Call the Controller method getAnalysis() delegate the work to Model

		xData = new double[xLength];
		yData = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			yData[i] = Double.parseDouble((String)yList.get(i));
			//resultPanelTextArea.append(" Y = "+yData[i] );
		}
		for (int i = 0; i < xLength; i++) {
			xData[i] = Double.parseDouble((String)xList.get(i));
			//resultPanelTextArea.append(" X = "+xData[i] );
		}

		/*********** plotting data is ready here ***************/
		/* where: x = data of x coordiante entered from the user */
		/*        y = data of y coordiante entered from the user */

		data.appendX("X", xData, DataType.QUANTITATIVE);
		data.appendY("Y", yData, DataType.QUANTITATIVE);

		LogisticRegressionResult result = null;
		boolean errorOccurs = false;
		String errorMessage = "";
		String errorTagStart = "<error_message>";
		String errorTagEnd = "</error_message>";

		try {

			xmlInputString = data.getAnalysisXMLInputString(AnalysisType.LOGISTIC_REGRESSION);
			setXMLInputString(xmlInputString);
			//////System.out.println("\n\nLogisticRegression xmlInputString =" + xmlInputString );

			xmlOutputString = getAnalysisOutputFromServer(xmlInputString);
			////////System.out.println("\n\nLogisticRegression PRINT FROM SERVER......... xmlOutputString =" + xmlOutputString );
			if (xmlOutputString.indexOf(errorTagStart) > 0) {
				//////////System.out.println("xmlOutputString.indexOf IF error = " + xmlOutputString.indexOf("error"));
				errorOccurs = true;
				errorMessage = xmlOutputString.substring(xmlOutputString.indexOf(errorTagStart) + errorTagStart.length(), xmlOutputString.indexOf(errorTagEnd));
			}
			else {
				//////////System.out.println("xmlOutputString.indexOf ELSE error = " + xmlOutputString.indexOf("error"));

				result = new LogisticRegressionResult(xmlOutputString);
			}


		} catch (Exception e) {
			//////////System.out.println("LogisticRegression callServer Exception " + e );
		}
		//////////System.out.println("LogisticRegression errorOccurs " + errorOccurs );
		// Retreive the data from Data Object using HashMap
		if (errorOccurs) {
			resultPanelTextArea.append("\nTHERE IS A PROBLEM:  " + errorMessage);
			resultPanelTextArea.setForeground(Color.BLACK);

		}
		else {
			double beta = 0, alpha = 0, meanX = 0, meanY = 0, sdAlpha = 0, sdBeta = 0;
			double tStatAlpha = 0, tStatBeta = 0;
			String pvAlpha = null, pvBeta = null, betaName = null;
			residuals = new double[xLength];
			predicted = new double[xLength];

			try {
				residuals = result.getResiduals();

				for (int i = 0; i < residuals.length; i++) {
					////////////System.out.println("result residuals["+i+"] = " + residuals[i]);
				}
			} catch (NullPointerException e) {
				////////////System.out.println("residuals Exception " + e);
			}

			try {
				predicted = result.getPredicted();
				for (int i = 0; i < predicted.length; i++) {
					////////////System.out.println("result predicted["+i+"] = " + predicted[i]);
				}
			} catch (NullPointerException e) {
				////////////System.out.println("predicted Exception " + e);
			}


			try {
				beta = result.getBeta();
			} catch (NullPointerException e) {
				//showError("NullPointerException  = " + e);
			}
			try {
				betaName = result.getBetaName();
			} catch (NullPointerException e) {
				//showError("NullPointerException  = " + e);
			}
			try {
				alpha = result.getAlpha();
			} catch (NullPointerException e) {
				//////////System.out.println("try alpha NullPointerException  = " + e);
			}

			try {
				sdAlpha = result.getAlphaSE();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				sdBeta = result.getBetaSE();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				tStatAlpha =  result.getAlphaTStat();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				tStatBeta = result.getBetaTStat();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				pvAlpha = result.getAlphaPValue();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				pvBeta = result.getBetaPValue();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}

			try {
				sortedResiduals = result.getSortedResiduals();//(double[])
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				sortedStandardizedResiduals = result.getSortedStandardizedResiduals();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}

			try {
				sortedResidualsIndex = result.getSortedResidualsIndex();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				sortedNormalQuantiles = result.getSortedNormalQuantiles();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}
			try {
				sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();
			} catch (NullPointerException e) {
				//showError("\nNullPointerException  = " + e);
			}

			resultPanelTextArea.append("\nNUMBER OF OBSERVATIONS = " + xLength);
			resultPanelTextArea.append("\n                                            INTERCEPT            SLOPE");

			resultPanelTextArea.append("\nESTIMATES                           " + alpha + "          " + beta);
			resultPanelTextArea.append("\nSTANDARD ERRORS           " + sdAlpha + "          " + sdBeta);
			resultPanelTextArea.append("\nT-STATISTICS                      " + tStatAlpha + "          " + tStatBeta);
			resultPanelTextArea.append("\nP-VALUE                                " + pvAlpha + "          " + pvBeta);
	/*
			resultPanelTextArea.append("\nSE of ALPHA      = " + sdAlpha);
			resultPanelTextArea.append("\nSE of BETA       = " + sdBeta);
			resultPanelTextArea.append("\nT-STAT of ALPHA  = " + tStatAlpha);
			resultPanelTextArea.append("\nT_STAT of BETA   = " + tStatBeta);
			resultPanelTextArea.append("\nP-VALUE of ALPHA = " + pvAlpha);
			resultPanelTextArea.append("\nP-VALUE of BETA  = " + pvBeta);
	*/

			resultPanelTextArea.append("\n\nPREDICTED VALUES                 RESIDUALS " );

			for (int i = 0; i < xLength; i++) {
				try {
					resultPanelTextArea.append("\n" + predicted[i]+ "        " + residuals[i]);
				} catch (Exception e) {
				}
			}
	/*
			resultPanelTextArea.append("\nRESIDUALS:          " );
			for (int i = 0; i < xLength; i++) {
				try {
					resultPanelTextArea.append(" " + residuals[i]);
				} catch (Exception e) {
				}
			}
	/*
			resultPanelTextArea.append("\nRESIDUALS SORTED= " );

			for (int i = 0; i < xLength; i++) {
				resultPanelTextArea.append(" " + sortedResiduals[i]);
			}
			resultPanelTextArea.append("\nRESIDUALS INDEX SORTED= " );

			for (int i = 0; i < xLength; i++) {
				resultPanelTextArea.append(" " + sortedResidualsIndex[i]);
			}

			resultPanelTextArea.append("\nSORTED RESIDUALS NORMAL QUANTILES = " );

			for (int i = 0; i < xLength; i++) {
				resultPanelTextArea.append(" " + sortedNormalQuantiles[i]);
			}
	*/
			resultPanelTextArea.setForeground(Color.BLUE);

			slope = beta;
			intercept = alpha;
			doGraph();
		}

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

   	protected void doGraph() {
		// graphComponent is available here
		// data: variables double xData, yData, residuals, predicted are available here after doAnalysis() is run.
		////////////System.out.println("start doGraph");

		graphPanel.removeAll();

		// 1. scatter plot of data: yData vs. xData
		//JFreeChart scatterChart = chartFactory.getLineChart("Scatter Plot Y vs X", independentHeader, dependentHeader, xData, yData);//getChart(title, xlabel, ylabel, xdata,ydata)

		JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot Y vs X", independentHeader, dependentHeader, "" , xData, yData,  "", intercept, slope, "");
		ChartPanel chartPanel = new ChartPanel(scatterChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		graphPanel.add(chartPanel);


		scatterChart = chartFactory.getQQChart("Scatter Plot X vs Y", dependentHeader, independentHeader, "" , yData, xData,  "", 0, 0, "");
		chartPanel = new ChartPanel(scatterChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		graphPanel.add(chartPanel);

		// this is only a test for having more than one charts in a boxlayout


		// 2. residual on fit plot: residuals vs. predicted

		JFreeChart rfChart2 = chartFactory.getQQChart("Residual on Fit Plot", "Predicted", "Residuals", "" , predicted, residuals,  "", 0, 0, "");
		ChartPanel chartPanel2 = new ChartPanel(rfChart2, false);
		chartPanel2.setPreferredSize(new Dimension(plotWidth,plotHeight));
		graphPanel.add(chartPanel2);
/*
		// 3. residual on fit plot: residuals vs. xData
		JFreeChart rxChart = chartFactory.getQQChart("Residual on Covariate Plot", "Covariate", "Residuals", "", xData, residuals,  "", 0, 0, "");
		chartPanel = new ChartPanel(rxChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		graphPanel.add(chartPanel);

		// 4. Normal QQ plot: need residuals and standardized normal scores

		JFreeChart qqChart = chartFactory.getQQChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", "", sortedNormalQuantiles, sortedStandardizedResiduals,  "", 0, 0, "");
		chartPanel = new ChartPanel(qqChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		graphPanel.add(chartPanel);

		// 5. scale-location plot -- maybe later.
*/
		graphPanel.validate();

	}

	protected void resetGraph()
	{
		////////////System.out.println("reset graph in SLR");
		JFreeChart chart = chartFactory.createChart(); // an empty  chart
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(400,300));
		graphPanel.removeAll();
		graphPanel.add(chartPanel);

	}

    public String getOnlineDescription(){
        return new String("http://en.wikipedia.org/wiki/Linear_regression");
    }
}


