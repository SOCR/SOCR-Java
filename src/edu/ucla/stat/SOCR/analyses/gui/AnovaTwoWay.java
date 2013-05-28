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

import edu.ucla.stat.SOCR.analyses.gui.*;
import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.beans.*;
import java.util.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.AnovaTwoWayExamples;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

/** One-Way ANOVA */
public class AnovaTwoWay extends Analysis implements PropertyChangeListener {
	
	JRadioButton interactionOnSwitch;
    JRadioButton interactionOffSwitch;
    
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.
	 
	private String[][] xDataArray = null;
	private String[] xData = null;

	private double[] yData = null;
	private double[][][] yValue = null;
	private double[] predicted = null;
	private double[] residuals = null;
	private double[] sortedResiduals = null;
	private double[] sortedStandardizedResiduals = null;
	private int[] sortedResidualsIndex = null;
	private double[] sortedNormalQuantiles = null;
	private double[] sortedStandardizedNormalQuantiles = null;

	private String dependentHeader = null, independentHeader = null;
	private String[] independentHeaderArray = null;

	private String[][] xNameData = null;
	private int seriesCount = 0; // box plot row size
	private int categoryCount = 1; // box plot column size, which is 1 for Anova 1 way.
	private String[] seriesName = null;
	private String[] categoryName = null;
	private String[] varList = null;

	private String xAxisLabel = "", yAxisLabel = null, boxPlotTitle = null, residualBoxPlotTitle = null;

	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	AnovaTwoWayResult result;

	int xLength = 0;
	int yLength = 0;
	
	private boolean interactionOn = false;
	protected final String INTERACTIONON	= "INTERACTIONON";
	protected final String INTERACTIONOFF	= "INTERACTIONOFF";
	public final static String INTERACTION_SWITCH	= "INTERACTION";
	/**Initialize the Analysis*/
	public void init(){
		showInput = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		analysisType = AnalysisType.ANOVA_TWO_WAY;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = false;
		//useServerExample = false;
		useStaticExample = AnovaTwoWayExamples.availableExamples;
		onlineDescription = "http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysisActivities_ANOVA_2";
		depMax = 1; // max number of dependent var
		indMax = 2; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Two Way ANOVA");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		// use the new JFreeChar function. annie che 20060312
		chartFactory = new Chart();
		resetGraph();
		validate();
	}


	

	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){
	}
	protected void setMappingPanel() {
        listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;

        bPanel = new JPanel(new BorderLayout());
        bPanel.add(mappingPanel,BorderLayout.CENTER);
        mappingPanel.add(mappingInnerPanel,BorderLayout.CENTER);

        addButton1.addActionListener(this);
        addButton2.addActionListener(this);
        removeButton1.addActionListener(this);
        removeButton2.addActionListener(this);

        lModel1 = new DefaultListModel();
        lModel2 = new DefaultListModel();
        lModel3 = new DefaultListModel();

        int cellWidth = 10;

        listAdded = new JList(lModel1);
        listAdded.setSelectedIndex(0);
        listDepRemoved = new JList(lModel2);
        listIndepRemoved = new JList(lModel3);

        paintTable(listIndex);
        listAdded.setFixedCellWidth(cellWidth);
        listDepRemoved.setFixedCellWidth(cellWidth);
        listIndepRemoved.setFixedCellWidth(cellWidth);
	   dependentPane = new JScrollPane(listDepRemoved);
	   FIRST_BUTTON_LABEL 		= "DEPENDENT";
	   SECOND_BUTTON_LABEL 		= "INDEPENDENT";
	   depLabel = new JLabel(FIRST_BUTTON_LABEL);
	   indLabel = new JLabel(SECOND_BUTTON_LABEL);
        tools1.add(depLabel);
        tools2.add(indLabel);

        tools1.add(addButton1);
        tools1.add(removeButton1);
        tools2.add(addButton2);
        tools2.add(removeButton2);

        tools1.setFloatable(false);
		tools2.setFloatable(false);
		
			//
			JPanel choicesPanel = new JPanel();
			choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
			interactionOnSwitch = new JRadioButton("On");
			interactionOnSwitch.addActionListener(this);
			interactionOnSwitch.setActionCommand(INTERACTIONON);
			interactionOnSwitch.setSelected(false);
			interactionOn = false;	

			interactionOffSwitch = new JRadioButton("Off");
			interactionOffSwitch.addActionListener(this);
			interactionOffSwitch.setActionCommand(INTERACTIONOFF);
			interactionOffSwitch.setSelected(true);
			
			ButtonGroup group = new ButtonGroup();
			group.add(interactionOnSwitch);
			group.add(interactionOffSwitch);
			choicesPanel.add(new JLabel("Turn the interaction:"));
			choicesPanel.add(interactionOnSwitch);
			choicesPanel.add(interactionOffSwitch);
			choicesPanel.setPreferredSize(new Dimension(200,100));
			

        JPanel emptyPanel = new JPanel();
	   //mappingInnerPanel.setBackground(Color.RED);
        mappingInnerPanel.add(new JScrollPane(listAdded));
        mappingInnerPanel.add(tools1);
        mappingInnerPanel.add(dependentPane);
       // mappingInnerPanel.add(emptyPanel);
        mappingInnerPanel.add(choicesPanel);
        mappingInnerPanel.add(tools2);
        mappingInnerPanel.add(new JScrollPane(listIndepRemoved));
        //listIndepRemoved.setBackground(Color.GREEN);
    }

	public void turnInteractionOn(){
		interactionOn = true;
		doAnalysis();
	}
	
	public void turnInteractionOff(){
		interactionOn = false;
		doAnalysis();
	}
	
	/** Create the actions for the buttons */
    protected void createActionComponents(JToolBar toolBar){
        super.createActionComponents(toolBar);
   }

    public void actionPerformed(ActionEvent event) {
 	   if (event.getActionCommand().equals(INTERACTIONON)) {       
        	turnInteractionOn();          
 	   }
 	   else if (event.getActionCommand().equals(INTERACTIONOFF)) {       
 		   turnInteractionOff();              
 	   }
    	else super.actionPerformed(event);
    } 
    

	/**This method defines the specific statistical Analysis to be carried our on the user specified data. ANOVA is done in this case. */
	public void doAnalysis(){
		//System.out.println("gui.AnovaTwoWay calling doAnalysis");

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
		Object[] independentVar = independentList.toArray();

		dependentHeader = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		independentHeader = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();
		yAxisLabel = dependentHeader;

		////System.out.println("doAnalysis dependentHeader = " + dependentHeader);
		boxPlotTitle = "Box Plot "+ yAxisLabel + " vs. ";

		//resultPanelTextArea.append("\ndependentIndex   = " + dependentIndex + " \n" );
		int varIndex = -1;
		int varIndexList[] = new int[independentVar.length];
		int independentListLength = independentList.size();
		independentHeaderArray = new String[independentVar.length];

		for (int i = 0; i < independentVar.length; i++) {
			varIndex = ( (Integer)independentList.get(i)).intValue();
			independentHeader = columnModel.getColumn(varIndex).getHeaderValue().toString().trim();
			independentHeaderArray[i] = independentHeader;
			////System.out.println("independentHeaderArray = " + independentHeaderArray[i]);
			//resultPanelTextArea.append("\nINDEPENDENT FACTOR  = "  + independentHeader);// + " original index = " + varIndex );
			xAxisLabel = independentHeader + "  " + xAxisLabel;
			varIndexList[i] = varIndex;
		}
		boxPlotTitle += xAxisLabel;
		Data data = new Data();
		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/
	
		//resultPanelTextArea.append("\n\tRESULTS:\n" );
		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<String> yList = new ArrayList<String>();
		yLength=0;
		xLength=0;
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
			} // end for k

			if (yLength <= 0) {
				JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
				return;
			}
			xDataArray = new String[independentListLength][];
			for (int index = 0; index < independentListLength; index++) {
				xList = new ArrayList<String>();
				// for each independent variable
				String varName = columnModel.getColumn(varIndexList[index]).getHeaderValue().toString().trim();
				//resultPanelTextArea.append("\nindependentIndex(ordered)  = "  + varName + " \n" );
				////System.out.println("\nvarIndexList[index] = " +varIndexList[index]);
				xLength = 0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k, varIndexList[index])).trim();
						if (cellValue != null && !cellValue.equals("")) {
							xList.add(xLength , cellValue);
							xLength++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) {
						////System.out.println("Exception: " + e );
					}
				}
				////System.out.println("xLength = " + xLength);
				xData = new String[xLength];
				try {
					for (int i = 0; i < xLength; i++) {
						xData[i] = Integer.parseInt((String)xList.get(i)) +"";
						xData[i] = xData[i].trim();
					}
				} catch (NumberFormatException e) {
					for (int i = 0; i < xLength; i++) {
						xData[i] = (String)xList.get(i);
						xData[i] = xData[i].trim();

					}
				}
				xDataArray[index] = xData;
				data.appendX(varName, xData, DataType.FACTOR);
				if (xLength <= 0) {
					JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
				return;
				}
			} // end for index
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception In outer catch: " + e );
		}
		//yValue = new double

		yData = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			yData[i] = Double.parseDouble((String)yList.get(i));
			//resultPanelTextArea.append("\nY = "+yData[i] );
		}
		/*
		for (int i = 0; i < xLength; i++) {
			x[i] = (String)xList.get(i);
			resultPanelTextArea.append("\nX = "+x[i] );
		}
		*/
		//data.appendX("I", x, DataType.FACTOR);
		//data.appendX("J", x, DataType.FACTOR);

		data.appendY("Y", yData, DataType.QUANTITATIVE);

		data.setParameter(AnalysisType.ANOVA_TWO_WAY, INTERACTION_SWITCH, Boolean.valueOf(interactionOn));
		result = null;
		try {
			result = (AnovaTwoWayResult)data.getAnalysis(AnalysisType.ANOVA_TWO_WAY);
			//resultPanelTextArea.append("\nresult = " +result+"\n");

		} catch (Exception e) {
			////System.out.println("result Exception " + e);
		}
		
		updateResults(); //set format in this method
		
		/* doGraph is underconstruction thus commented out. annie che 20060314 */
		//if (useGraph)
		doGraph();

	}
	public void updateResults(){
		//System.out.println("gui.AnovaTwoWay Calling updateResults");
		
		int dfCTotal = 0, dfError = 0, dfModel = 0;
		double rssModel = 0, rssError = 0;
		double mssModel = 0, mssError = 0;
		double rssTotal = 0, fValue = 0;
		double pValue = 0;
		String rSquareString = null;
		String rssModelString=null , rssErrorString = null;
		String mssModelString=null, mssErrorString=null;
		String rssTotalString=null, fValueString=null;
	
		int[] dfGroup = null;
		double[] rssGroup = null, mseGroup = null;
		double[] fValueGroup = null;
		double[] pValueGroup = null;
		String[] rssGroupString = null, mseGroupString = null;
		String[] fValueGroupString = null;
		String[] pValueGroupString = null;
		//ArrayList varName = null;

		if (result==null)
			return;
		
		result.setDecimalFormat(dFormat);
		
		////System.out.println("GUI AnovaTwoWay start try");
		try {
			varList = result.getVariableList();//(ArrayList)(result.getTexture().get(AnovaTwoWayResult.VARIABLE_LIST));
		} catch (Exception e) {
			////System.out.println("varList Exception " + e);
			//showError("\nException  = " + e);
		}

		try {
			dfCTotal = result.getDFTotal();//((Integer)result.getTexture().get(AnovaTwoWayResult.DF_TOTAL)).intValue();
		} catch (Exception e) {
			////System.out.println("in gui dfCTotal e = " + e);
		}


		try {
			dfError = result.getDFError();//((Integer)result.getTexture().get(AnovaTwoWayResult.DF_ERROR)).intValue();
		} catch (Exception e) {
			////System.out.println("in gui dfError e = " + e);
		}


		try {

			dfModel= result.getDFModel();//((Integer)result.getTexture().get(AnovaTwoWayResult.DF_MODEL)).intValue();
		} catch (Exception e) {
			////System.out.println("in gui dfModel e = " + e);
		}


		try {
			rssModelString= result.getFormattedDouble(result.getRSSModel());//((String)result.getTexture().get(AnovaTwoWayResult.RSS_MODEL));
		} catch (Exception e) {
			////System.out.println("in gui rssModel e = " + e);
		}

		try {
			rssErrorString = result.getFormattedDouble(result.getRSSError());//((String)result.getTexture().get(AnovaTwoWayResult.RSS_ERROR));

		} catch (Exception e) {
			////System.out.println("in gui rssError e = " + e);
		}

		try {
			mssModelString = result.getFormattedDouble(result.getMSSModel());//((String)result.getTexture().get(AnovaTwoWayResult.MSS_MODEL));
		} catch (Exception e) {
			////System.out.println("in gui mssModel e = " + e);
		}

		try {
			mssErrorString = result.getFormattedDouble(result.getMSSError());//((String)result.getTexture().get(AnovaTwoWayResult.MSS_ERROR));
			////System.out.println("in gui mssError = " + mssError);
		} catch (Exception e) {
			//showError("\nException = " + e);
		}
 		try {
			rssTotalString = result.getFormattedDouble(result.getRSSTotal());//((String)result.getTexture().get(AnovaTwoWayResult.RSS_TOTAL));

		} catch (Exception e) {
			////System.out.println("in gui rssTotal e = " + e);
		}

		try {
			fValueString = result.getFormattedDouble(result.getFValue());//((String)result.getTexture().get(AnovaTwoWayResult.F_VALUE));
			fValue= result.getFValue();
		} catch (Exception e) {
			////System.out.println("in gui fValue e = " + e);
		}
		
		try {
			//pValue = ((String)result.getTexture().get(AnovaTwoWayResult.P_VALUE));
			pValue = result.getPValue();
		} catch (Exception e) {
			System.out.println("in gui pValue e = " + e);
		}
		

		try {
			dfGroup = result.getDFGroup();//((int[])result.getTexture().get(AnovaTwoWayResult.DF_GROUP));

		} catch (Exception e) {
			////System.out.println("in gui dfGroup e = " + e);
		}

		try {
			rssGroupString = result.getFormattedGroup(result.getRSSGroup());//((String[])result.getTexture().get(AnovaTwoWayResult.RSS_GROUP));

		} catch (Exception e) {
			////System.out.println("in gui rssGroup e = " + e);
		}
		try {
			mseGroupString = result.getFormattedGroup(result.getMSEGroup());//((String[])result.getTexture().get(AnovaTwoWayResult.MSE_GROUP));

		} catch (Exception e) {
			////System.out.println("in gui mseGroup e = " + e);
		}

		try {
			fValueGroupString= result.getFormattedGroup(result.getFValueGroup());//((String[])result.getTexture().get(AnovaTwoWayResult.F_VALUE_GROUP));

		} catch (Exception e) {
			//showError("\nException = " + e);
		}

		try {
			pValueGroupString = result.getFormattedGroup(result.getPValueGroup());//((String[])result.getTexture().get(AnovaTwoWayResult.P_VALUE_GROUP));

		} catch (Exception e) {
			System.out.println("pValueGroup e = " + e);
		}

		try {
			predicted = result.getPredicted();//(double[])(result.getTexture().get(AnovaTwoWayResult.PREDICTED));
		} catch (Exception e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			residuals = result.getResiduals();//(double[])(result.getTexture().get(AnovaTwoWayResult.RESIDUALS));
		} catch (Exception e) {
			//showError("NullPointerException  = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);

		try {
			sortedResiduals = result.getSortedResiduals();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_RESIDUALS);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedResiduals = result.getSortedStandardizedResiduals();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_STANDARDIZED_RESIDUALS);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedResidualsIndex = result.getSortedResidualsIndex();//(int[])residualMap.get(AnovaTwoWayResult.SORTED_RESIDUALS_INDEX);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedNormalQuantiles = result.getSortedNormalQuantiles();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_NORMAL_QUANTILES);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}


		try {
			seriesCount = result.getBoxPlotSeriesCount();//((Integer)(result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE))).intValue();
			//Integer.parseInt((String)result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE));
		} catch (Exception e) {
			//showError("\nException = " + e);
			e.printStackTrace();
		}
		try {
			categoryCount = result.getBoxPlotCategoryCount();//((Integer)(result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_COLUMN_SIZE))).intValue();
		} catch (Exception e) {
			//showError("\nException = " + e);
			e.printStackTrace();
		}

		try {
			seriesName = result.getBoxPlotSeriesName();//((String[])result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_FACTOR_NAME));
			for (int i = 0; i < seriesName.length; i++) {
				//////System.out.println("------>seriesName = " + seriesName[i]);
				}

		} catch (Exception e) {
			//////System.out.println(" seriesName Exception " + e);
			//showError("\nException = " + e);
		}
		try {
			categoryName = result.getBoxPlotCategoryName();//((String[])result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_COLUMN_FACTOR_NAME));

			for (int i = 0; i < categoryName.length; i++) {
					//////System.out.println("------>categoryName = " + categoryName[i]);
				}
		} catch (Exception e) {
			////System.out.println("categoryName Exception " + e);
			//showError("\nException = " + e);
		}
		try {
			yValue = result.getBoxPlotResponseValue();//((double[][][])result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_RESPONSE_VALUE));

		} catch (Exception e) {
			////System.out.println("yValue Exception " + e);
		}
		try {
			rSquareString = result.getFormattedDouble(result.getRSquare());//((String)result.getTexture().get(AnovaOneWayResult.P_VALUE));

		} catch (NullPointerException e) {
			System.out.println("rSquare Exception = " + e);
		}

		resultPanelTextArea.setText("\n");//clear first
		resultPanelTextArea.append("\tSample Size = " + xLength);

		resultPanelTextArea.append("\n\tDependent Variable = " + dependentHeader);
		resultPanelTextArea.append("\n\tIndependent Variable(s) = ");
		//resultPanelTextArea.append("varList.lengtht = " + varList.length);

		for (int i = 0; i < varList.length; i++) {
			try {
				resultPanelTextArea.append("\t" +varList[i] );
			} catch (Exception e) {
				//resultPanelTextArea.append("VARIABLE(S) e = " + e);
			}
		}

		resultPanelTextArea.append("\n");
		resultPanelTextArea.append("\n\t*** Two-Way Analysis of Variance Results ***\n");

		// Print Standard 2-Way ANOVA, table, according to:
		//	http://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_ANOVA_2Way
		/*
		 * Variance Source 	 Degrees of Freedom (df) 	 Sum of Squares (SS) 	 Mean Sum of Squares (MS) 	 F-Statistics 	 P-value
		 * Main Effect A 	df(A)=a-1 	SS(A)=r\times b\times\sum_{i=1}^{a}{(\bar{y}_{i,.,.}-\bar{y})^2} 	{SS(A)\over df(A)} 	F_o = {MS(A)\over MSE} 	P(F(df(A),df(E)) > Fo)
		 * Main Effect B 	df(B)=b-1 	SS(B)=r\times a\times\sum_{j=1}^{b}{(\bar{y}_{., j,.}-\bar{y})^2} 	{SS(B)\over df(B)} 	F_o = {MS(B)\over MSE} 	P(F(df(B),df(E)) > Fo)
		 * A vs.B Interaction 	df(AB)=(a-1)(b-1) 	SS(AB)=r\times \sum_{i=1}^{a}{\sum_{j=1}^{b}{((\bar{y}_{i, j,.}-\bar{y}_{i, .,.})+(\bar{y}_{., j,.}-\bar{y}))^2}} 	{SS(AB)\over df(AB)} 	F_o = {MS(AB)\over MSE} 	P(F(df(AB),df(E)) > Fo)
		 * Error 	N-a\times b 	SSE=\sum_{k=1}^r{\sum_{i=1}^{a}{\sum_{j=1}^{b}{(\bar{y}_{i, j,k}-\bar{y}_{i, j,.})^2}}} 	{SSE\over df(Error)} 
		 * Total 	N-1 	SST=\sum_{k=1}^r{\sum_{i=1}^{a}{\sum_{j=1}^{b}{(\bar{y}_{i, j,k}-\bar{y}_{., .,.})^2}}} 		
		 */
		resultPanelTextArea.append("\n\tStandard 2-Way ANOVA Table. See:");
		resultPanelTextArea.append("\n\thttp://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_ANOVA_2Way\n");
		
		resultPanelTextArea.append("\t==============================================================================================\n");
		resultPanelTextArea.append("\tVarianceSource \t DF \t RSS \t MSS \t F-Statistics \t P-value");
		for (int i = 0; i < varList.length; i++) {
			if (!varList[i].startsWith("Interact")) 
				resultPanelTextArea.append("\n\tMainEffect:"+varList[i]+"\t\t"+dfGroup[i]+"\t"+rssGroupString[i]+"\t"+mseGroupString[i]+"\t "+fValueGroupString[i]+"\t "+pValueGroupString[i]);
			else 
				resultPanelTextArea.append("\n\t"+varList[i]+"\t\t"+dfGroup[i]+"\t"+rssGroupString[i]+"\t"+mseGroupString[i]+"\t "+fValueGroupString[i]+"\t "+pValueGroupString[i]);
		}
		resultPanelTextArea.append("\n\tError"+"\t\t"+dfError+"\t"+rssErrorString+"\t"+mssErrorString);
		resultPanelTextArea.append("\n\tTotal:"+"\t\t"+dfCTotal+"\t"+rssTotalString);
		resultPanelTextArea.append("\n\t==============================================================================================\n");
		
		for (int i = 0; i < varList.length; i++) {
			resultPanelTextArea.append("\n\tVariable: " + varList[i]);//varName.get(i) );
			resultPanelTextArea.append("\n\tDegrees of Freedom = "+ dfGroup[i]);
			resultPanelTextArea.append("\n\tResidual Sum of Squares = " + rssGroupString[i]);
			resultPanelTextArea.append("\n\tMean Square Error = " + mseGroupString[i]);
			resultPanelTextArea.append("\n\tF-Value = " + fValueGroupString[i]);
			resultPanelTextArea.append("\n\tP-Value = " + pValueGroupString[i]);
			resultPanelTextArea.append("\n");
		}

		resultPanelTextArea.append("\n\n\tResidual:");
		resultPanelTextArea.append("\tDegrees of Freedom = " + dfError);
		resultPanelTextArea.append("\n\tResidual Sum of Squares = " + rssErrorString);
		resultPanelTextArea.append("\n\tMean Square Error = " + mssErrorString);
		resultPanelTextArea.append("\n\tF-Value = " + fValueString);
		resultPanelTextArea.append("\n\tP-Value = " + pValue);

		resultPanelTextArea.append("\n\n\tR-Square = " + rSquareString);

		resultPanelTextArea.append("\n");

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
	public String getOnlineDescription() {
	    return onlineDescription;
    }
   	protected void doGraph() {
		graphPanel.removeAll();
		// 1. Box plot of data: yData vs. xData (where xData is categorical)
		JPanel innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		graphPanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		String[][] xNameData = new String[seriesCount][categoryCount]; // xNameData: category names.
		//////System.out.println("xNameData = "+xNameData.length);
		for (int i = 0; i < seriesCount; i++) {
			for (int j = 0; j < categoryCount; j++) {
				xNameData[i][j] = categoryName[j];
				//////System.out.println("xNameData = " + xNameData[i][j]);
			}
		}

		/* the parameters to be passed are:
		1.boxPlotTitle, xAxisLabel, YAxisLabel.
		2. serieCount, categoryCount
		3. seriesname (e.g. sex, race, etc)
		4. category's name (e.g. height, weight, etc)--xData
		5. yValue(double),
		*/
		ChartPanel chartPanel = null;
		double xDataDouble[] = null;
		TreeSet<String> treeSet = new TreeSet<String>();
		String groupLegend = "";
		boolean groupHasChar = false;
		// 1. scatter plot of data: yData vs. xData
		try {
			for (int i =  xDataArray.length-1; i >=0; i--) {
				xData = xDataArray[i];
				xDataDouble = new double[xData.length];
				try {
					for (int j = 0; j < xData.length; j++) {
						xDataDouble[j] = (new Double(xData[j])).doubleValue();
					}
				} catch (Exception e) {
					for (int j = 0; j < xData.length; j++) {
						treeSet.add((String)xData[j]);
					}
					groupHasChar = true;
				}


				Iterator iterator = treeSet.iterator();
				int groupIndex = 1;
				String groupName = null;
				while (iterator.hasNext()) {
					groupName = (String)iterator.next();
					//xDataDouble[groupIndex-1] = groupIndex;
					groupLegend += ("\t" + groupName + "="+groupIndex + "  ");
					for (int j = 0; j < xData.length; j++) {
						if (xData[j].equalsIgnoreCase(groupName)){ // xData[i] is a String.
							xDataDouble[j] = (double)groupIndex;
						}
					}
					groupIndex++;

				}
				independentHeader = independentHeaderArray[i];
				//JFreeChart scatterChart = chartFactory.getLineChart("Scatter Plot of " + dependentHeader + " vs. " + independentHeader, independentHeader, dependentHeader, xDataDouble, yData);//getChart(title, xlabel, ylabel, xdata,ydata)

				JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot of " + dependentHeader + " vs. " + independentHeader, independentHeader, dependentHeader, "Residuals  ", xDataDouble, yData,  "   " +groupLegend, 0, 0, "");
				////System.out.println("doGraph dependentHeader = " + dependentHeader);
				////System.out.println("doGraph independentHeader = " + independentHeader);
				chartPanel = new ChartPanel(scatterChart, false);
				chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
				innerPanel.add(chartPanel);

				if (groupHasChar) {
					groupLegend = "Group Names for " + independentHeaderArray[i] + ": " + groupLegend;
					/*JLabel legendLabel1 = new JLabel(groupLegend);
					legendLabel1.setBackground(Color.WHITE);
					JPanel labelPanel1 = new JPanel();
					labelPanel1.add(legendLabel1, BorderLayout.NORTH);
					innerPanel.add(labelPanel1);
					*/
				}
				groupLegend = "";
				treeSet = new TreeSet<String>();

			}
		} catch (Exception e) {
			////System.out.println("doGraph Scatter Exception = " + e);

		}

		groupHasChar = false;
		// 2. residual on fit plot: residuals vs. predicted

		//JFreeChart rfChart = chartFactory.getLineChart("Residual on Fit Plot", "Predicted", "Residuals", predicted, residuals, "Other Stuff");
		JFreeChart rfChart = chartFactory.getQQChart("Residual on Fit Plot", "Predicted " + dependentHeader, "Residuals", "Residuals  ", predicted, residuals,  "At Residual = 0", 0, 0, "");

		for (int i = 0; i < predicted.length; i++) {
			//System.out.println("predicted["+i+"] = " + predicted[i] + ". residuals["+i+"] = " + residuals[i]);
		}
		chartPanel = new ChartPanel(rfChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);

		// 3. residual on fit plot: residuals vs. xData
		xDataDouble = new double[xData.length];
		groupLegend = "";
		boolean useStringLegent = false;
		treeSet = new TreeSet<String>();
		try {
			for (int i =  xDataArray.length-1; i >=0; i--) {
				xData = xDataArray[i];
				xDataDouble = new double[xData.length];
				try {
					for (int j = 0; j < xData.length; j++) {
						xDataDouble[j] = (new Double(xData[j])).doubleValue();
					}
				} catch (Exception e) {
					for (int j = 0; j < xData.length; j++) {
						treeSet.add((String)xData[j]);
					}
					groupHasChar = true;
				}


				Iterator<String> iterator = treeSet.iterator();
				int groupIndex = 1;
				String groupName = null;
				while (iterator.hasNext()) {
					groupName = (String)iterator.next();
					//xDataDouble[groupIndex-1] = groupIndex;
					groupLegend += ("\t" + groupName + "="+groupIndex + "  ");
					for (int j = 0; j < xData.length; j++) {
						if (xData[j].equalsIgnoreCase(groupName)){ // xData[i] is a String.
							xDataDouble[j] = (double)groupIndex;
						}
					}
					groupIndex++;

				}

				independentHeader = independentHeaderArray[i];
				//JFreeChart scatterChart = chartFactory.getLineChart("Residual on Covariate Plot, Residual vs. " + independentHeaderArray[i], independentHeaderArray[i], dependentHeader, xDataDouble, residuals, "Other Stuff 2");//getChart(title, xlabel, ylabel, xdata,ydata)
				JFreeChart scatterChart = chartFactory.getQQChart("Residual on Covariate Plot: Variable = " + independentHeaderArray[i], independentHeaderArray[i], "Residuals", "Residual  ", xDataDouble, residuals,  "   " + groupLegend, 0, 0, "noline");


				chartPanel = new ChartPanel(scatterChart, false);
				chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
				innerPanel.add(chartPanel);

				if (groupHasChar) {
					groupLegend = "Group Names for " + independentHeaderArray[i] +": " + groupLegend;
					/*
					JLabel legendLabel2 = new JLabel(groupLegend);
					legendLabel2.setBackground(Color.WHITE);
					JPanel labelPanel2 = new JPanel();
					labelPanel2.add(legendLabel2, BorderLayout.NORTH);
					innerPanel.add(labelPanel2);
					*/

				}
				groupLegend = "";
				treeSet = new TreeSet<String>();


			}
		} catch (Exception e) {
			////System.out.println("doGraph Residuals Exception = " + e);

		}

		// 4. Normal QQ plot: need residuals and standardized normal scores
		//JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals);
		JFreeChart qqChart = chartFactory.getQQChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", "Standardized Residual Value  ", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals,  "At Standardized Residual = 0", 0, 1, "noshape");
		//JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals);

		//JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals, "noline");
		chartPanel = new ChartPanel(qqChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);


		graphPanel.validate();

	}
   	
    public void reset(){
		 super.reset();
		 interactionOnSwitch.setSelected(false);
		 interactionOffSwitch.setSelected(true);
		 interactionOn= false;
	 }
    
	protected void resetGraph()
	{

		JFreeChart chart = chartFactory.createChart(); // an empty  chart
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(400,300));
		graphPanel.removeAll();
		graphPanel.add(chartPanel);
		graphPanel.validate();

	}
}


