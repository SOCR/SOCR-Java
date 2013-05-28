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

/* 	modified by annie che 200508.
	separate the gui part from the model part
*/

package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.ChiSquareModelFitExamples;
import edu.ucla.stat.SOCR.analyses.example.ClusteringExamples;
import edu.ucla.stat.SOCR.analyses.xml.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;


import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog.TipLog;  // added

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.inicial.*;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;
import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.XYBox;
import edu.ucla.stat.SOCR.analyses.util.parser.EscalaFigures;
import edu.ucla.stat.SOCR.analyses.util.parser.EscaladoBox;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Cercle;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Escala;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Linia;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Marge;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.NomsDendo;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.NomsLabelsEscala;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.rotacioNoms;
import edu.ucla.stat.SOCR.analyses.util.utils.BoxFont;
import edu.ucla.stat.SOCR.analyses.util.definicions.BoxContainer;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;
import edu.ucla.stat.SOCR.analyses.util.definicions.Dimensions;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.*;

// below from Jpan_btn

import edu.ucla.stat.SOCR.analyses.gui.*;
import edu.ucla.stat.SOCR.analyses.util.importExport.DadesExternes;
import edu.ucla.stat.SOCR.analyses.util.importExport.FitxerDades;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import edu.ucla.stat.SOCR.analyses.util.methods.Reagrupa;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmInternalFrame;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.InternalFrameData;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.FrmPiz;
import edu.ucla.stat.SOCR.analyses.util.parser.Fig_Pizarra;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.metodo;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;
import edu.ucla.stat.SOCR.analyses.util.definicions.MatriuDistancies;

// added to enalbe "Frame"

import edu.ucla.stat.SOCR.analyses.gui.Analysis;
import edu.ucla.stat.SOCR.analyses.gui.AnalysisPanel;
import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.util.EditableHeader;

import java.awt.*;
//import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

// added to get graphPanel

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.ChiSquareModelFitExamples;
import edu.ucla.stat.SOCR.analyses.example.ClusteringExamples;
import edu.ucla.stat.SOCR.analyses.xml.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;


import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog.TipLog;  // added

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.inicial.*;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;
import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.util.Vector;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.Panels.Jpan_btn;

/** this class is for Logistic Regression only. */
public class Clustering extends Analysis implements PropertyChangeListener {
  
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
        private edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk f;
        private static LinkedList<String[]> ClusteringData;
	private double[] xData = null;
	private double[][] xDataArray = null;
	private double[] yData = null;
	private double[] predicted = null;
	private double[] residuals = null;
	private double[] sortedResiduals = null;
	private double[] sortedStandardizedResiduals = null;
	private int[] sortedResidualsIndex = null;
	private double[] sortedNormalQuantiles = null;
	private double[] sortedStandardizedNormalQuantiles = null;

	private String dependentHeader = null, independentHeader = null;
	static int times = 0;

	FileDialog fileDialog;
	Frame fileDialogFrame = new Frame();
	File file;
	//FileInputStream fstream;
	private String fileName = "";
	private boolean useHeader = true;
	private String header = null;

	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	private String xmlInputString = null;
	
	ClusteringResult result;
	int independentListLength;
	int xLength, yLength;
	

	/**Initialize the Analysis*/
	public void init(){
            mapIndep= false;  // added
            showMapping= false;  // added
            showDendro = true;  // added: only for Clustering
            showGraph = false;  // added
            
		showInput = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		
		analysisType = AnalysisType.Clustering;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		
		useStaticExample = ClusteringExamples.availableExamples;

		onlineDescription = "http://en.wikipedia.org/wiki/Logistic_regression";
		depMax = 1; // max number of dependent var
		indMax = 15; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

		// use the new JFreeChar function. annie che 20060312
		chartFactory = new Chart();
		resetGraph();
		validate();
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
	
                if (!hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}

		Data data = new Data();

                String dendroData[][] = new String[dataTable.getRowCount()][dataTable.getColumnCount()];

		for (int k =0; k < dataTable.getRowCount(); k++) 
                    for (int j = 0; j < dataTable.getColumnCount(); j++){
                    
		    if (dataTable.getValueAt(k, j) != null && !dataTable.getValueAt(k, j).equals("")) {
			dendroData[k][j] = (String)dataTable.getValueAt(k, j);
                    }
		}

               ClusteringData = new LinkedList<String[]>();  // added (for lstdades)
                
               Vector<String> vectorDataRow = new Vector();
               
               for (int k = 0; k < dataTable.getRowCount(); k++)
               {
                    for (int j = 0; j < dataTable.getColumnCount(); j++)
                    {
                        if (dendroData[k][j] != null && !dendroData[k][j].equals(""))
                        vectorDataRow.add(dendroData[k][j]);
                    }
                    String stringDataRow[] = new String[vectorDataRow.size()];
                    
                    for (int i = 0; i < vectorDataRow.size(); i++)
                    {
                        stringDataRow[i] = vectorDataRow.get(i);
                    }
                    
                    if (vectorDataRow.size() != 0)
                    {
                        ClusteringData.add(k, stringDataRow);  // now exactly same as lstdades
                        vectorDataRow.clear();
                    }
               }
                
                f.getPwBtn().doLoad("Calculate");
                
                resultPanelTextArea.append("Click on DENDROGRAM panel to view the graph"+
                		"\nSee SOCR Hierarchical Clustering Activity:\n"+
                		"http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysisActivities_HierarchicalClustering");


		/* every one has to have its own, otherwise one Exception spills the whole. */

		/* doGraph is underconstruction thus commented out. annie che 20060314 */
		//if (useGraph)
	}
	
	public void updateResults(){
		/*******************************************************/
		////System.out.println("FINISH try result = " + result);
		// Retreive the data from Data Object using HashMap
		int varLength = independentListLength + 1;
		double[] beta = null;
		double[] seBeta =null;
		double[] tStat = null;
		double[] pValue = null;
		int dfError = 0;
		double rSquare = 0;

		//ArrayList varName = null;
		String[] varList = null;
		
		if (result==null)
			return;
		
		result.setDecimalFormat(dFormat);
		
		resultPanelTextArea.setText("\n");//clear first
		resultPanelTextArea.append("\tNumber of Independent Variable(s) = "  + independentListLength);


		resultPanelTextArea.append("\n\tSample Size =" + xLength);

		resultPanelTextArea.append("\n\tDependent Variable  = "  + dependentHeader);// + " original index = " + dependentIndex );
		resultPanelTextArea.append("\n\tIndependent Variable(s) = ");
		
		for (int i = 0; i < independentListLength; i++) {
			resultPanelTextArea.append("  "  + independentHeaderArray[i]);// + " original index = " + varIndex );
		}
		
		try {
			//varName = (ArrayList)(result.getTexture().get(MultiLinearRegressionResult.VARIABLE_LIST));
			varList = result.getVariableList();
			////System.out.println("varList = " + varList);

			//(String[])(result.getTexture().get(MultiLinearRegressionResult.VARIABLE_LIST));
		} catch (NullPointerException e) {
			////System.out.println("varList e = " + e);

			//showError("NullPointerException  = " + e);
		}
		try {
			beta = result.getBeta();//(double[])(result.getTexture().get(MultiLinearRegressionResult.BETA));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			seBeta = result.getBetaSE();//(double[])(result.getTexture().get(MultiLinearRegressionResult.BETA_SE));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			tStat = result.getBetaTStat();//(double[])(result.getTexture().get(MultiLinearRegressionResult.T_STAT));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			pValue = (double[]) (result.getBetaPValue());//(String[])(result.getTexture().get(MultiLinearRegressionResult.P_VALUE));
		} catch (NullPointerException e) {
		}
		try {
			rSquare = result.getRSquare();//(String[])(result.getTexture().get(MultiLinearRegressionResult.P_VALUE));
		} catch (NullPointerException e) {
		}

		try {
			dfError = result.getDF();//(Integer)result.getTexture().get(MultiLinearRegressionResult.DF_ERROR)).intValue();
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		try {
			predicted = result.getPredicted();//(double[])(result.getTexture().get(MultiLinearRegressionResult.PREDICTED));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			residuals = result.getResiduals();//(double[])(result.getTexture().get(MultiLinearRegressionResult.RESIDUALS));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);

		try {
			sortedResiduals = result.getSortedResiduals();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			sortedStandardizedResiduals = result.getSortedStandardizedResiduals();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_STANDARDIZED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		try {
			sortedResidualsIndex = result.getSortedResidualsIndex();//(int[])residualMap.get(MultiLinearRegressionResult.SORTED_RESIDUALS_INDEX);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			sortedNormalQuantiles = result.getSortedNormalQuantiles();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		resultPanelTextArea.append("\n\n\tRegression Model:\n\t\t" + dependentHeader + " = ");
                resultPanelTextArea.append(" "+ "1/(1+exp(-Z)\n\t\t");
                resultPanelTextArea.append("where Z = ");
                
		for (int i = 0; i < beta.length; i++) {
			if (i==0) resultPanelTextArea.append(" "+ result.getFormattedDouble(beta[i]));
			else if (beta[i]<0)
				resultPanelTextArea.append(" "+ result.getFormattedDouble(beta[i])+"*"+varList[i]);
			else resultPanelTextArea.append(" +"+ result.getFormattedDouble(beta[i])+"*"+varList[i]);
		}
		resultPanelTextArea.append(".\n\n");
		
		for (int i = 0; i < beta.length; i++) {
			//resultPanelTextArea.append("\n\n"+varName.get(i) + "\n\tEstimate = "+ beta[i] + "\n\tStd. Error" + seBeta[i] + "\n\t t-valuer" + tStat[i] + "\n\tp-value " + pValue[i]);

			resultPanelTextArea.append("\n\n\t"+varList[i] + ":\n\tEstimate = "+ result.getFormattedDouble(beta[i]) + "\n\tStandard Error = " + result.getFormattedDouble(seBeta[i]) + "\n\tT-Value = " + result.getFormattedDouble(tStat[i]));

			/*if (pValue[i].equals("0.0")) {
				resultPanelTextArea.append("\n\tP-Value: <2E-16");
			}
			else {*/
			//resultPanelTextArea.append("\n\tP-Value = " + AnalysisUtility.enhanceSmallNumber(pValue[i]));
			resultPanelTextArea.append("\n\tP-Value = " +result.getFormattedDouble(pValue[i]));

		}
		resultPanelTextArea.append("\n\n\tR-Square = " + result.getFormattedDouble(rSquare));


/*
		resultPanelTextArea.append("\nPREDICTED        = " );


		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + predicted[i]);
		}
		resultPanelTextArea.append("\nRESIDUALS        = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + residuals[i]);
		}

		resultPanelTextArea.append("\nRESIDUALS SORTED= " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedResiduals[i]);
		}
		resultPanelTextArea.append("\nRESIDUALS INDEX SORTED= " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedResidualsIndex[i]);
		}
		resultPanelTextArea.append("\nRESIDUALS NORMAL QUANTILES = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedNormalQuantiles[i]);
		}

		resultPanelTextArea.append("\nStandardized RESIDUALS Standardized = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedStandardizedResiduals[i]);
		}
		resultPanelTextArea.append("\nStandardized NORMAL QUANTILES = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedStandardizedNormalQuantiles[i]);
		}

*/		resultPanelTextArea.append("\n" );

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

        public void reset() {
        	super.reset();
        	independentHeaderArray = null;
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
   	protected void doGraph() {

	}
	protected void resetGraph()
	{
		f = new edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk("MultiDendrogram");
                
                Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = f.getSize();
		f.setLocation(0, 0);
		f.setVisible(true);
		dendroPanel.removeAll();
                dendroPanel.add(f.pan_West, BorderLayout.WEST);
                dendroPanel.add(f.pan_Center, BorderLayout.CENTER);
		dendroPanel.validate();

	}
        
        public static LinkedList<String[]> getClusteringData(){
            return ClusteringData;
        }
	public String getOnlineDescription(){
		return onlineDescription;
	}
}