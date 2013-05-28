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
/* modified annie che 2005/08
*/

package edu.ucla.stat.SOCR.analyses.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.example.AnovaOneWayExamples;
import edu.ucla.stat.SOCR.analyses.example.AnovaTwoWayExamples;
import edu.ucla.stat.SOCR.analyses.example.CIExamples;
import edu.ucla.stat.SOCR.analyses.example.ChiSquareContingencyTableExamples;
import edu.ucla.stat.SOCR.analyses.example.ChiSquareModelFitExamples;
import edu.ucla.stat.SOCR.analyses.example.DichotomousProportionExamples;
import edu.ucla.stat.SOCR.analyses.example.ExampleData;
import edu.ucla.stat.SOCR.analyses.example.ExampleDataRandom;
import edu.ucla.stat.SOCR.analyses.example.FlignerKilleenExamples;
import edu.ucla.stat.SOCR.analyses.example.KolmogorovSmirnoffExamples;
import edu.ucla.stat.SOCR.analyses.example.LogisticRegressionExamples;
import edu.ucla.stat.SOCR.analyses.example.ClusteringExamples;
import edu.ucla.stat.SOCR.analyses.example.MultiLinearRegressionExamples;
import edu.ucla.stat.SOCR.analyses.example.OneTExamples;
import edu.ucla.stat.SOCR.analyses.example.OneZExamples;
import edu.ucla.stat.SOCR.analyses.example.PrincipalComponentAnalysisExamples;
import edu.ucla.stat.SOCR.analyses.example.SimpleLinearRegressionExamples;
import edu.ucla.stat.SOCR.analyses.example.SurvivalExamples;
import edu.ucla.stat.SOCR.analyses.example.TwoIndependentFriedmanExamples;
import edu.ucla.stat.SOCR.analyses.example.TwoIndependentKruskalWalliesExamples;
import edu.ucla.stat.SOCR.analyses.example.TwoIndependentTExamples;
import edu.ucla.stat.SOCR.analyses.example.TwoIndependentWilcoxonExamples;
import edu.ucla.stat.SOCR.analyses.example.TwoPairedSignTestExamples;
import edu.ucla.stat.SOCR.analyses.example.TwoPairedSignedRankExamples;
import edu.ucla.stat.SOCR.analyses.example.TwoPairedTExamples;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.core.SOCRAnalyses;
import edu.ucla.stat.SOCR.modeler.gui.ModelerGui;
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.JScrollPaneAdjuster;
import edu.ucla.stat.SOCR.util.JTableRowHeaderResizer;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.util.NormalCurve;
import edu.ucla.stat.SOCR.util.OKDialog;
import edu.ucla.stat.SOCR.util.RowHeaderRenderer;

/**This class defines a basic type of Statistical Analysis procedure that can be
 * subclassed by the specific types of analyses
 * (e.g., ANOVA, Regression, prediction, etc.)*/
public class Analysis extends JApplet implements Runnable, MouseListener, ActionListener,MouseMotionListener, WindowListener,KeyListener {

	/****************** Start Variable Declaration **********************/

	// CONSTANT decalration

	public static final int DEFAULT_PANE_WIDTH = SOCRAnalyses.PRESENT_PANEL_DEFAULT_WIDTH;
	public static final int DEFAULT_PANE_HEIGHT = 550;

	public static final int DEFAULT_DATA_PANEL_WIDTH = 1000;
	public static final int DEFAULT_DATA_PANEL_HEIGHT = 500;

	protected static final int DEFAULT_RESULT_PANEL_WIDTH = 900;
	protected static final int DEFAULT_RESULT_PANEL_HEIGHT = 750;

	protected static final int RESULT_PANEL_ROW_SIZE = 250;
	protected static final int RESULT_PANEL_COL_SIZE = 200;

	protected final String EXAMPLE_1 = " EXAMPLE 1 ";
	protected final String EXAMPLE_2 = " EXAMPLE 2 ";
	protected final String EXAMPLE_3 = " EXAMPLE 3 ";
	protected final String EXAMPLE_4 = " EXAMPLE 4 ";
	protected final String EXAMPLE_5 = " EXAMPLE 5 ";
	protected final String EXAMPLE_6 = " EXAMPLE 6 ";
	protected final String EXAMPLE_7 = " EXAMPLE 7 ";

	protected final String VARIABLE = " VARIABLE";
	protected final String VARIABLE_1 = " VARIALBE 1";
	protected final String VARIABLE_2 = " VARIABLE 2";

	//protected final String EXAMPLE 	= " EXAMPLE ";
	protected final String CALCULATE 	= " CALCULATE ";
	protected final String CLEAR 	= " CLEAR ";
	protected final String LOAD_FILE 	= " LOAD FILE ";
	protected final String USER_INPUT 	= " USER INPUT ";
	protected final String USE_SERVER 	= " CALL SERVER ";

	protected final String DATA 		= "DATA";
	protected final String RESULT 	= "RESULT";
	protected final String GRAPH 	= "GRAPH";
        protected final String DENDRO	= "DENDROGRAM";
        protected final String PCARESULT = "PCA RESULT";
	protected final String MAPPING 	= "MAPPING";
	protected final String SELECT 	= "SELECT PARAMETERS";
	protected final String ADD 		= " ADD  ";
	protected final String REMOVE 		= "REMOVE";
	protected final String VISUALIZE 		= "COMPARE CURVES";
	protected String INPUT 		= "INPUT";
	protected final String SHOW_ALL 		= "SHOW ALL";
	
	protected boolean showData= true;
        protected boolean showPCA= false;
	protected boolean showInput= true;
	protected boolean showMapping= true;
	protected boolean showVisualize= true;
	protected boolean showSelect= true;
	protected boolean showGraph= true;
        protected boolean showDendro = false;

	protected final String RANDOM_EXAMPLE 		= "RANDOM EXAMPLE";
	protected String FIRST_BUTTON_LABEL = "";
	protected String SECOND_BUTTON_LABEL = "";
	public static final String DEFAULT_HEADER 		= "C";
	public static final String DEFAULT_HEADER_1 = "C";
	public static final String DEFAULT_HEADER_2	= "C";

	protected final String DATA_MISSING_MESSAGE 		= "MISSING DATA: Select an EXAMPLE data first and then click on MAPPING to continue.";
	protected final String VARIABLE_MISSING_MESSAGE 		= "VARIABLE MISSING: Map columns to variables first, by clicking on MAPPING tab.";
	protected final String DATA_COLINEAR_MESSAGE 		= "DATA CLOSE TO COLINEAR: Please remove colinearity before continuing.";
	protected final String DATA_ERROR_MESSAGE 		= "Some of the variables were not selected correctly. \n\tPlease go to MAPPING and try again.";
	protected final String NULL_VARIABLE_MESSAGE 		= "You have selected a variable that does not have data. \n\tPlease try again.";
	protected static final String outputFontFace = "Helvetica";
	protected static final int outputFontSize = 12;
	public static final int SURVIVAL_LIST_LENGTH = 10;

	protected String onlineDescription = "http://en.wikipedia.org/wiki/Statistical_analysis";
	protected String onlineHelp = "http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_Analyses";

	//private static final int TEXT_AREA_ROW_SIZE = 120;
	//private static final int TEXT_AREA_COLUMN_SIZE = 350;
 
	protected int independentLength = 0;

	/*	Size of the plots */
	protected int plotWidth = 500; 	// the width for each of the several residual plots
	protected int plotHeight = 500;	// height

	protected int independentIndex = -1; 	// "x"
	protected int dependentIndex = -1;		// "y"

	protected int timeIndex = -1; 	// survival time
	protected int censorIndex = -1;		// survival censor
	protected int groupNamesIndex = -1;		// survival group names

	protected String analysisName = "";

	/************** flags that controls the actions **************/
	//protected boolean useStaticExample = true;
	protected boolean useRandomExample = true;
	protected boolean useLocalExample = true;
	protected boolean useInputExample = false;
	protected boolean useServerExample = true;
	protected boolean useGraph = true; // subclass calls doGraph()

	protected boolean[] useStaticExample = {true, true, true, true, true, true, true, true, true, true};
	protected boolean isInitialInput = true;
	protected boolean hasExample = false;		//
	protected boolean hasInput = false;		//
	/************** ************** ************** **************/

	protected Action exampleStaticAction1; // example data from ExampleData class.
	protected Action exampleStaticAction2; // example data from ExampleData class.
	protected Action exampleStaticAction3; // example data from ExampleData class.
	protected Action exampleStaticAction4; // example data from ExampleData class.
	protected Action exampleStaticAction5; // example data from ExampleData class.
	protected Action exampleStaticAction6; // example data from ExampleData class.
	protected Action exampleStaticAction7; // example data from ExampleData class.


	protected Action exampleRandomAction; // applet code generate some random data
	protected Action exampleLocalAction; // use data on user's local machine
	protected Action exampleRemoteAction; // get example stored on server remotely
	protected Action exampleInputAction; // user input some data to work on
	protected Action callServerAction; // call R on server to do analysis work

	protected Action computeAction;
	protected Action clearAction;
	protected Action userDataAction;
	protected Action fileLoadAction;

	protected short analysisType = -1;
	protected short exampleID = -1;

	protected String analysisDescription1 = "";
	protected String analysisDescription2 = "";
	protected String analysisDescription3 = "";
	protected String analysisDescription4 = "";
	protected String analysisDescription5 = "";
	protected String analysisDescription6 = "";
	protected String analysisDescription7 = "";

	protected String[] independentHeaderArray = null;
	protected int exampleSampleSize = 0;


	private int time = 0,
	updateCount = 0,
	stopCount = 0,
	tabbedPaneCount = 0,
	toolbarCount = 0;
	public int selectedInd = 0;
	public static JTable dataTable;
        public static JTable PCATable;
	public static JTable headerTable;
	protected EditableHeader dTableHeader;
	
	public Object [][] dataObject;
	protected String dataText = "";
	public static int DEFAULT_MAX_COLUMN_NUMBER 	= 16;
	protected int columnNumber 		= DEFAULT_MAX_COLUMN_NUMBER;
	protected int rowNumber 		= 10;
	public String [] columnNames;
	public static javax.swing.table.DefaultTableModel tModel;
	public static javax.swing.table.DefaultTableModel hModel;
        protected static JPanel controlPanel, dataPanel, resultPanel, graphPanel, mappingInnerPanel, mappingPanel, inputPanel, selectPanel, visualizePanel;
        public static JPanel dendroPanel;
        public static JPanel PCAPanel;
	protected JPanel bPanel;

	public JTextArea resultPanelTextArea;

	protected TableColumnModel columnModel;
	public JList listAdded, listDepRemoved, listIndepRemoved;
	public JList listTime, listCensor, listGroupNames; // for Survival.

	public JButton addButton1 = new JButton(ADD);

	public JButton addButton2 = new JButton(ADD);
	public JButton addButton3 = new JButton(ADD);
	public JButton removeButton1  = new JButton(REMOVE);
	public JButton removeButton2 = new JButton(REMOVE);

	public JButton removeButton3 = new JButton(REMOVE);

	JButton exampleButton1 = null;
	JButton exampleButton2 = null;
	JButton exampleButton3 = null;
	JButton exampleButton4 = null;
	JButton exampleButton5 = null;
	JButton exampleButton6 = null;
	JButton exampleButton7 = null;

	JButton calculateButton = null;
	JButton clearButton = null;
	JButton inputButton = null;
	JButton randomButton = null;
	JButton serverButton = null;

	DefaultListModel lModel1,lModel2,lModel3,lModel4;
	protected  int depMax = 1;
	protected  int indMax = 2;
	protected  int currentDepIndex = -1;
	protected  int currentIndepIndex = -1;
	//protected ArrayList independentList = null;
	protected LinkedList<Integer> independentList = null;
	protected boolean mapDep=true, mapIndep=true;
	protected int independentListCursor = 0;

	public int[] listIndex;
	JToolBar tools1, tools2, tools3;
	protected JScrollPane dependentPane = null;

	//protected JScrollPane mixPanelContainer;
	//Thread
	protected Thread analysis = null;

	protected boolean stopNow = false;

	public JTabbedPane tabbedPanelContainer;

	// tabbedPanelContainer is a container for the JTabbedPane's.
	// Usually there will be the following tabbed panes in each
	// Statistical Analysis:
	// control TabbedPane
	// Data
	// Textual results
	// Graphical results
	protected Chart chartFactory;

	public static Font font = new Font("SansSerif", Font.PLAIN, 12);

	private JFrame frame;
	//Format
	private DecimalFormat decimalFormat = new DecimalFormat();

	public static String FORMAT001 = "0.001";
    public static String FORMAT00001 = "0.00001";
    public static String FORMATALL= "All";
    protected DecimalFormat dFormat = new DecimalFormat("#.000");

	public JPanel leftAnalysisChoicePanel;
	protected JLabel depLabel;
	protected JLabel indLabel;
	protected JLabel varLabel = new JLabel(VARIABLE);
	protected String inputXMLString = null;
	protected Data data = null;
	protected String xmlInputString = null;
	protected String xmlOutputString = null;

	protected static boolean randomDataStep = false;

	protected String hypothesisType = null;
	protected NormalCurve graphRawData = new NormalCurve(ModelerGui.GUI_LOWER_LIMIT, ModelerGui.GUI_UPPER_LIMIT, 1);
	protected NormalCurve graphSampleMean = new NormalCurve(ModelerGui.GUI_LOWER_LIMIT, ModelerGui.GUI_UPPER_LIMIT, 1);
	protected NormalCurve graphZScore = new NormalCurve(ModelerGui.GUI_LOWER_LIMIT, ModelerGui.GUI_UPPER_LIMIT, 1);

	//protected ChartPanel chartPanel;
	protected	JPanel leftPanel = new JPanel();
	protected	JPanel rightPanel = new JPanel();


	public void setFormat(String f){
		if (f.equals(FORMAT001)){
				dFormat = new DecimalFormat("#.000");
				updateResults();
		}
		else if (f.equals(FORMAT00001)){
				dFormat = new DecimalFormat("#.00000");
				updateResults();
		}
		else if (f.equals(FORMATALL)){
			dFormat = new DecimalFormat("#.0000000000");
			updateResults();
	}
	}

	/********************* Start Methods **************************/
    /**This method initializes the Analysis, by setting up the basic tabbedPanes.*/
    public void init(){
		setFont(font);
		setName("SOCR: Statistical Analysis");
		//Get frame of the applet
		frame = getFrame(this.getContentPane());

		setMainPanel();

		dataPanel = new JPanel();
                PCAPanel = new JPanel();
		resultPanel = new JPanel();
		resultPanelTextArea = new JTextArea();
		graphPanel = new JPanel();
                dendroPanel = new JPanel();
                PCATable = new JTable();
		mappingPanel = new JPanel(new BorderLayout());
        	//inputPanel= new JPanel(); //new BorderLayout());
		//inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel= new JPanel(new BorderLayout());
		
		mappingInnerPanel = new JPanel(new GridLayout(2,3,50, 50));
	
		selectPanel = new JPanel();
		visualizePanel = new JPanel();

		//dataPanel.setPreferredSize(new Dimension(DEFAULT_DATA_PANEL_WIDTH, DEFAULT_DATA_PANEL_HEIGHT));
		//dataPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		selectPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		inputPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		graphPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
                dendroPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		visualizePanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		resultPanel.setPreferredSize(new Dimension(DEFAULT_RESULT_PANEL_WIDTH, DEFAULT_RESULT_PANEL_HEIGHT));

		tools1 = new JToolBar(JToolBar.VERTICAL);
		tools2 = new JToolBar(JToolBar.VERTICAL);
		tools3 = new JToolBar(JToolBar.VERTICAL);

        	//tools1.setBackground(Color.BLUE);
		//listIndepRemoved.addListSelectionListener(this);


		//addButton1.setBackground(Color.GREEN);
		setDataPanel();
		setMappingPanel();
		setInputPanel();
		setResultPanel();
		setGraphPanel();
                setDendroPanel();
		setSelectPanel();
		setVisualizePanel();

		if(showData)
			addTabbedPane(DATA, dataPanel);
		//addTabbedPane(MAPPING, mappingPanel);		
		if(showInput)
			addTabbedPane(INPUT, inputPanel);
		if(showMapping)
			addTabbedPane(MAPPING, bPanel);
		addTabbedPane(RESULT, resultPanel);
		if(showGraph)
			addTabbedPane(GRAPH, graphPanel);
                if (showDendro)
                        addTabbedPane(DENDRO, dendroPanel);
		if(showSelect)
			addTabbedPane(SELECT, selectPanel);
		if(showVisualize)
		addTabbedPane(VISUALIZE, visualizePanel);
                if(showPCA)
			addTabbedPane(PCARESULT, PCAPanel);

 		//this.getContentPane().setSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
 		
 		JScrollPane js = new JScrollPane(tabbedPanelContainer);
 		js.setPreferredSize(new Dimension(DEFAULT_DATA_PANEL_WIDTH, DEFAULT_DATA_PANEL_HEIGHT));
		js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		js.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//js.setSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
        this.getContentPane().add(js, BorderLayout.CENTER);
		tabbedPanelContainer.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==MAPPING) {
					bPanel.removeAll();
				//	mappingPanel.setPreferredSize(new Dimension(CHART_SIZE_X, CHART_SIZE_Y));
					bPanel.add(mappingPanel,BorderLayout.CENTER);
					bPanel.validate();
				}
			}

		});
		bPanel.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {
				// resultPanelTextArea.append("sucess");
				// //System.out.print("Success");
				// Add code here for updating the Panel to show proper heading
				paintTable(listIndex);
			}
			public void componentHidden(ComponentEvent e) {}
        });

		//System.err.println("Test0");
    }

    protected void setDataPanel() {
	    //////////////////System.out.println("setDataPanel");
                if (showPCA == true)
                    rowNumber = 100;
                        
		dataObject = new Object[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		independentList = new LinkedList<Integer>();

		for (int i = 0; i < columnNumber; i++) {
			columnNames[i] = new String(DEFAULT_HEADER+(i+1) + "");
		}

		hModel = new DefaultTableModel(0, 1);
		tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames); // table model

		for (int i = 0; i < tModel.getRowCount(); i++)
            hModel.addRow(new Object[] { (i+1)+":" } );

		headerTable = new JTable(hModel);

		//tModel.set
		dataTable = new JTable(tModel);
		dataTable.addKeyListener(this);
		//dataTable.setPreferredScrollableViewportSize(new Dimension(DEFAULT_DATA_PANEL_WIDTH, DEFAULT_DATA_PANEL_HEIGHT));

		dataTable.setGridColor(Color.LIGHT_GRAY);
		dataTable.setShowGrid(true);
		headerTable.setGridColor(Color.LIGHT_GRAY);
		headerTable.setShowGrid(true);
		
		dataTable.doLayout();
		//dataTable.setBackground(Color.PINK);
		//dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setEnabled(false);
		headerTable.setCellSelectionEnabled(false);
		LookAndFeel.installColorsAndFont(headerTable, "TableHeader.background","TableHeader.foreground", "TableHeader.font");
		headerTable.setIntercellSpacing(new Dimension(0, 0));

        Dimension d = headerTable.getPreferredScrollableViewportSize();

        d.width = headerTable.getPreferredSize().width-20;
        //System.out.println("d.witch="+d.width);
        headerTable.setPreferredScrollableViewportSize(d);
        headerTable.setRowHeight(dataTable.getRowHeight());
        headerTable.setDefaultRenderer(Object.class, new RowHeaderRenderer());
        
        JTableHeader corner = headerTable.getTableHeader();
        corner.setReorderingAllowed(false);
        corner.setResizingAllowed(false);
      
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try {
			dataTable.setDragEnabled(true);
		} catch (Exception e) {
			////////////////////System.out.println("dataTable.setDragEnabled e = " + e);
		}
		columnModel = dataTable.getColumnModel();
		//columnModel.setPreferredSize(100);
		//columnModel.setResizable(true);
		for (int i = 0; i < columnNumber; i++) {
			try {
				//columnModel.getColumn(i).setWidth(200);
				columnModel.getColumn(i).setResizable(true);
				//columnModel.getColumn(i).setPreferredWidth(200);
			} catch (Exception e) {
				//////////////////System.out.println("columnModel.getColumn("+i+") Exception = " + e);
			}

		}
		try {
			columnModel.getColumn(0).getCellEditor().stopCellEditing();
		} catch (Exception e) {
			//////////////////System.out.println("columnModel.getColumn(0) Exception = " + e);
		}

		dTableHeader =new EditableHeader(columnModel);
		dataTable.setTableHeader(dTableHeader);
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dataTable.setCellSelectionEnabled(true);
		dataTable.setColumnSelectionAllowed(true);
		dataTable.setRowSelectionAllowed(true);


		hookTableAction();
		//JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//JScrollPane tablePanel = new JScrollPane(dataTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//new JScrollPaneAdjuster(tablePanel);
		//	new JTableRowHeaderResizer(tablePanel).setEnabled(true);
		
		displayDataPanel();
		//dataPanel.setBackground(Color.GREEN);

	}
    
    protected void setPCADataPanel() {
	    //////////////////System.out.println("setDataPanel");
		dataObject = new Object[100][columnNumber];
		columnNames = new String[columnNumber];
		independentList = new LinkedList<Integer>();

		for (int i = 0; i < columnNumber; i++) {
			columnNames[i] = new String(DEFAULT_HEADER+(i+1) + "");
		}

		hModel = new DefaultTableModel(0, 1);
		tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames); // table model

		for (int i = 0; i < tModel.getRowCount(); i++)
            hModel.addRow(new Object[] { (i+1)+":" } );

		headerTable = new JTable(hModel);

		//tModel.set
		PCATable = new JTable(tModel);
		PCATable.addKeyListener(this);
		//dataTable.setPreferredScrollableViewportSize(new Dimension(DEFAULT_DATA_PANEL_WIDTH, DEFAULT_DATA_PANEL_HEIGHT));

		PCATable.setGridColor(Color.LIGHT_GRAY);
		PCATable.setShowGrid(true);
		headerTable.setGridColor(Color.LIGHT_GRAY);
		headerTable.setShowGrid(true);
		
		PCATable.doLayout();
		//dataTable.setBackground(Color.PINK);
		//dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		headerTable.setEnabled(false);
		headerTable.setCellSelectionEnabled(false);
		LookAndFeel.installColorsAndFont(headerTable, "TableHeader.background","TableHeader.foreground", "TableHeader.font");
		headerTable.setIntercellSpacing(new Dimension(0, 0));

        Dimension d = headerTable.getPreferredScrollableViewportSize();

        d.width = headerTable.getPreferredSize().width-20;
        //System.out.println("d.witch="+d.width);
        headerTable.setPreferredScrollableViewportSize(d);
        headerTable.setRowHeight(PCATable.getRowHeight());
        headerTable.setDefaultRenderer(Object.class, new RowHeaderRenderer());
        
        JTableHeader corner = headerTable.getTableHeader();
        corner.setReorderingAllowed(false);
        corner.setResizingAllowed(false);
      
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try {
			PCATable.setDragEnabled(true);
		} catch (Exception e) {
			////////////////////System.out.println("dataTable.setDragEnabled e = " + e);
		}
		columnModel = PCATable.getColumnModel();
		//columnModel.setPreferredSize(100);
		//columnModel.setResizable(true);
		for (int i = 0; i < columnNumber; i++) {
			try {
				//columnModel.getColumn(i).setWidth(200);
				columnModel.getColumn(i).setResizable(true);
				//columnModel.getColumn(i).setPreferredWidth(200);
			} catch (Exception e) {
				//////////////////System.out.println("columnModel.getColumn("+i+") Exception = " + e);
			}

		}
		try {
			columnModel.getColumn(0).getCellEditor().stopCellEditing();
		} catch (Exception e) {
			//////////////////System.out.println("columnModel.getColumn(0) Exception = " + e);
		}

		dTableHeader =new EditableHeader(columnModel);
		PCATable.setTableHeader(dTableHeader);
		PCATable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		PCATable.setCellSelectionEnabled(true);
		PCATable.setColumnSelectionAllowed(true);
		PCATable.setRowSelectionAllowed(true);


		hookTableAction();
		//JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//JScrollPane tablePanel = new JScrollPane(dataTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//new JScrollPaneAdjuster(tablePanel);
		//	new JTableRowHeaderResizer(tablePanel).setEnabled(true);
		
		displayPCADataPanel();
		//dataPanel.setBackground(Color.GREEN);

	}

    /**
     * Add customized table actions.
     * Clicking  tab in the last cell will add one new column.
     * Clicking return in the last cell will add one new row.
     *
     */
    	protected void hookTableAction() {

    		//Tab--add column
    		String actionName = "selectNextColumnCell";
    		final Action tabAction = dataTable.getActionMap().get(actionName);
    		Action myAction = new AbstractAction() {
    				public void actionPerformed(ActionEvent e) {
    					//if (dataTable.isEditing() && isLastCell()) {
    					if (isLastCell()) {
    						resetTableColumns(dataTable.getColumnCount()+1);
    					}
    					else tabAction.actionPerformed(e);
    				}
    			};
    		dataTable.getActionMap().put(actionName, myAction);

    		//Enter--append row
    		String actionName2 = "selectNextRowCell";
    		final Action enterAction = dataTable.getActionMap().get(actionName2);

    		Action myAction2 = new AbstractAction() {
    				public void actionPerformed(ActionEvent e) {
    					//if (dataTable.isEditing() && isLastCell()) {
    					if (isLastCell()) {
    						appendTableRows(1);
    					}
    					else enterAction.actionPerformed(e);
    				}
    			};

    		dataTable.getActionMap().put(actionName2, myAction2);
    }
    	private	 boolean isLastCell()
        {
            int rows = dataTable.getRowCount();
            int cols = dataTable.getColumnCount();
            int selectedRow = dataTable.getSelectedRow();
            int selectedCol = dataTable.getSelectedColumn();

            if((rows == (selectedRow+1)) && (cols == (selectedCol+1)))
                return true;
            else
                return false;
        }
    	
    	public void resetTableColumns(int n) {
            tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
    		tModel.setColumnCount(n);
            dataTable.setModel(tModel);
    		listIndex = new int[dataTable.getColumnCount()];
            for(int j=0;j<listIndex.length;j++)
                listIndex[j]=1;

        }
    	public void resetTableColumns(TableColumnModel columnModel) {
            tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
    		tModel.setColumnCount(columnModel.getColumnCount());	
            dataTable.setModel(tModel);
            
            dTableHeader =new EditableHeader(columnModel);
    		dataTable.setTableHeader(dTableHeader);
            
            TableColumnModel columns = dataTable.getColumnModel();
            for(int ii=0; ii<columnModel.getColumnCount(); ii++){ 
          	   columns.getColumn(ii).setHeaderValue(columnModel.getColumn(ii).getHeaderValue());
          	  // System.out.println(columnModel.getColumn(ii).getHeaderValue()+" " +columns.getColumn(ii).getHeaderValue());
             }
            dTableHeader =new EditableHeader(columns);
    		dataTable.setTableHeader(dTableHeader);
        
    		listIndex = new int[dataTable.getColumnCount()];
            for(int j=0;j<listIndex.length;j++)
                listIndex[j]=1;

        }
	protected void setResultPanel() {
		//JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//resultPanelTextArea.setLineWrap(true);
		resultPanelTextArea.setLineWrap(false);

		resultPanelTextArea.setRows(RESULT_PANEL_ROW_SIZE);
		resultPanelTextArea.setColumns(RESULT_PANEL_COL_SIZE);
		resultPanel.setBackground(Color.WHITE);
		resultPanelTextArea.setEditable(true);

		resultPanelTextArea.setBackground(Color.WHITE);
		resultPanelTextArea.setForeground(Color.BLACK);

		//resultPanel.add(new JScrollPane(resultPanelTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		JScrollPane scrollPane = new JScrollPane(resultPanelTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		resultPanel.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		resultPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));

	}

	protected void setGraphPanel() {
		graphPanel.setLayout(new BorderLayout());
		//graphPanel.add(new JScrollPane(dataTable));

	}
        protected void setDendroPanel() {
		dendroPanel.setLayout(new BorderLayout());
		//dendro.add(new JScrollPane(dataTable));

	}
        

	protected void setVisualizePanel() {
		visualizePanel.removeAll();
	}


	protected void setSelectPanel() {

	}
	protected void setInputPanel() {

	}


    /** Sets the amin Stat Analysis GUI with left-analysis-choice & right-data-cointrol
     * Jpanel's.*/
    public void setMainPanel() {
        tabbedPanelContainer = new JTabbedPane();
        //tabbedPanelContainer.setBackground(Color.PINK);
        tabbedPanelContainer.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
    }

    /**This method returns basic copyright, author, and other metadata information*/
    public String getAppletInfo(){
        return "\nUCLA Department of Statistics: SOCR Resource\n"+
        "http://www.SOCR.ucla.edu\n";
    }


    public String format(double x, String precision){
        setDecimalFormat(new DecimalFormat(precision));
        return decimalFormat.format(x);
    }

    /**This method sets the decimal format, so that the properties of the decimal
     * format can then be changed*/
    public void setDecimalFormat(DecimalFormat d){
        decimalFormat = d;
    }

    /**This class method returns the frame that contains a given component*/
    static JFrame getFrame(Container component){
        JFrame frame = null;
        while((component = component.getParent()) != null){
            if (component instanceof JFrame) frame = (JFrame)component;
        }
        return frame;
    }

    /**This method add a new component to the tabbed panel.*/
    public void addTabbedPane(String name, JComponent c){
        tabbedPanelContainer.addTab(name, c);
        tabbedPaneCount++;

    }

    /**This method add a new component to the tabbed panel.*/
    public void addTabbedPane(String title, Icon icon, JComponent component, String tip)
    {	tabbedPanelContainer.addTab(title, icon, component, tip);
        tabbedPaneCount++;
    }

    /**This method removes a component from the tabbed panel.*/
    public void removeTabbedPane(int index){
        tabbedPanelContainer.removeTabAt(index);
        tabbedPaneCount--;
    }

    /**This method sets a component in the tabbed panel to a specified new component.*/
    public void setTabbedPaneComponent(int index, JComponent c){
        tabbedPanelContainer.setComponentAt(index, c);
    }


    /**This method gets the time parameter of the analysis process.
     * May have to be overwritten .*/
    public int getTime(){
        return time;
    }

    /**This method runs the analysis thread*/
    public void run(){
        Thread thread = Thread.currentThread();
        while (analysis == thread){
            doAnalysis();
            stopCount++;
            updateCount++;
            if (stopNow){
                stop();
                if (updateCount != 0) update();
            }
            try {Thread.sleep(1);}
            catch (InterruptedException e){
                stop();
            }
        }
    }

    /**This method stops the analysis thread*/
    public void stop(){
        analysis = null;
        stopCount = 0;
    }


    /**This method defines the boolean variable that stops the analysis,
     * when the process is in run mode*/
    public void setStopNow(boolean b){
        stopNow = b;
    }

    /**This method is the default update method and defines how the display is updated.
     * This method should be overridden by the specific analysis.*/
    public void update(){
    }


    /**This method is the default reset method, that resets the analysis
     * process to its initial state. It should be overridden by the specific
     * analysis tools.*/

     public void reset(){
    	//System.out.println("reset hasExample");
     	hasExample = false;
     	dependentIndex = -1;
     	independentIndex = -1;

     	independentHeaderArray = null;
     	independentList.clear();
		resultPanelTextArea.setText("");
		resultPanel.validate();
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();

		resetTable();
		
		time = 0;
		if(graphPanel!=null)
			resetGraph();
                if (dendroPanel!=null)
                        resetDendro();
		resetVisualize();
		resetParameterSelect();
	 }
     
 	public void updateEditableHeader(TableColumnModel  aColumnModel){
		dTableHeader = new EditableHeader(aColumnModel);
		/*for (int i=0; i<aColumnModel.getColumnCount(); i++){
			System.out.println("H "+aColumnModel.getColumn(i).getHeaderValue());
		}*/
		dTableHeader.setEditingColumn(-1);
		dataTable.setTableHeader(dTableHeader);
		dataTable.validate();		
		
		displayDataPanel();
                displayPCADataPanel();
		//System.out.println("table Header updated");
	}

	public int getSelectedHeaderColumn(){
		//System.out.println("selected header index ="+ dTableHeader.getEditingColumn());
		return dTableHeader.getEditingColumn();
	}
	
     public void resetTable(){
    		//	System.out.println("resetTable get called");
    			for (int i = 0; i < columnNumber; i++)
    				columnNames[i] = new String(DEFAULT_HEADER+(i+1));

    			tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
    			hModel = new DefaultTableModel(0, 1);
    			
    			for (int i = 0; i < tModel.getRowCount(); i++)
    	            hModel.addRow(new Object[] { (i+1)+":" } );
    			
    			dataTable = new JTable(tModel);
    			dataTable.addKeyListener(this);
    			dataTable.setGridColor(Color.gray);
    			dataTable.setShowGrid(true);
    			dataTable.doLayout();
    			dataTable.setCellSelectionEnabled(true);
    			dataTable.setColumnSelectionAllowed(true);
    			dataTable.setRowSelectionAllowed(true);
    	         
    			columnModel = dataTable.getColumnModel();
    			dTableHeader =new EditableHeader(columnModel);
    			dataTable.setTableHeader(dTableHeader);
    			
    			
    			listIndex = new int[dataTable.getColumnCount()];
    	        for(int j=0;j<listIndex.length;j++)
    	            listIndex[j]=1;
    			
    			hookTableAction();

    			headerTable = new JTable(hModel);
    			headerTable.setCellSelectionEnabled(false);
    			headerTable.setGridColor(Color.LIGHT_GRAY);
    			headerTable.setShowGrid(true);
    			
    			LookAndFeel.installColorsAndFont
    	        (headerTable, "TableHeader.background", 
    	        "TableHeader.foreground", "TableHeader.font");
    			headerTable.setIntercellSpacing(new Dimension(0, 0));
    	        Dimension d = headerTable.getPreferredScrollableViewportSize();
    	        d.width = headerTable.getPreferredSize().width;
    	        headerTable.setPreferredScrollableViewportSize(d);
    	        headerTable.setRowHeight(dataTable.getRowHeight());
    	        headerTable.setDefaultRenderer(Object.class, new RowHeaderRenderer());	
    	        
    	        JTableHeader corner = headerTable.getTableHeader();
    	        corner.setReorderingAllowed(false);
    	        corner.setResizingAllowed(false);
    	        
    	       displayDataPanel();	 
               displayPCADataPanel();
    		}
     
	public void resetMappingList() {

		dependentIndex = -1;
		independentIndex = -1;
		independentList.clear();
		removeButtonDependentAll();
		removeButtonIndependentAll();
		//removeButtonDependent();
		//removeButtonIndependentAll();
	}
	public void resetMappingListGUI() {
		dependentIndex = -1;
		independentIndex = -1;
	     independentList.clear();
		removeButtonDependentAll();
		removeButtonIndependentAll();
//		removeButtonDependent();
		//removeButtonIndependentAll();
	}
    /**This method defines what the analysis really does, and should be overridden
     * by the specific analysis tools.*/
    public void doAnalysis(){
        // this method is overriden in sub classes.
    }
    public void updateResults(){
        // this method is overriden in sub classes.
    }

    /**This method is the default step method, that runs the analysis one time unit.
     * This method may be overridden by the specific analysis tools.*/
    public void step(){
        doAnalysis();
        update();
    }


    /**This method handles the action events associated with the action
     * buttons in the Analysis Control JTabbedPane (Panel). It needs to
     * overridden by the specific analysis tools.*/
    public void actionPerformed(ActionEvent event) {
    	if(event.getSource() == addButton1) {
    		addButtonDependent();
    	}
    	else if (event.getSource() == removeButton1) {
    		removeButtonDependent();
    	}
    	else if (event.getSource() == addButton2) {
    		addButtonIndependent();

    	}
    	else if (event.getSource() == removeButton2) {
    		//removeButtonIndependent();
    		////////////System.out.println("AnalysisType = " + analysisType);
    		if (analysisType == AnalysisType.ANOVA_TWO_WAY || analysisType == AnalysisType.PRINCIPAL_COMPONENT_ANALYSIS || analysisType == AnalysisType.MULTI_LINEAR_REGRESSION || analysisType == AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS || analysisType == AnalysisType.TWO_INDEPENDENT_FRIEDMAN) {
    			removeButtonIndependentAll();
			} else {
				removeButtonIndependent();
			}
    	}


    }
    protected void addButtonDependent() {
            int ct1=-1;

            int sIdx = listAdded.getSelectedIndex();
            int idx2 = lModel2.getSize();
		  dependentIndex = 0;
            if(sIdx >-1 && idx2 <depMax) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
                        dependentIndex=i;
                        break;
                    }
                }
                listIndex[dependentIndex]=2;
                paintTable(listIndex);
            }
	}
	protected void removeButtonDependent() {
		   dependentIndex = -1;
            int ct1=-1;
            int idx1 = 0;
            int sIdx = listDepRemoved.getSelectedIndex();
           if(sIdx >-1) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==2)
                        ct1++;
                    if(ct1==sIdx) {
                        idx1=i;
                        break;
                    }
                }

                listIndex[idx1]=1;
                paintTable(listIndex);
            }

	}
	private void addButtonIndependent() {
		independentLength++;
		//independentIndex++; // original code:

            int ct1=-1;
            int sIdx = listAdded.getSelectedIndex();
		  ////////////////////////System.out.println("addButtonIndependent sIdx = " + sIdx);
            int idx3 = lModel3.getSize();
            if(sIdx >-1 && idx3 <indMax) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
					independentIndex=i;
					////////////System.out.println("\naddButtonIndependent sIdx = " + sIdx);
 					////////////System.out.println("addButtonIndependent independentIndex = " + independentIndex);
 				     ////////////System.out.println("addButtonIndependent independentListCursor = " + independentListCursor);
                         independentList.add(independentListCursor, new Integer(independentIndex));

                        //independentList.add(new Integer(independentIndex));
                        break;
                    }
                }
		      ////////////System.out.println("addButtonIndependent listIndex[independentIndex] = " + listIndex[independentIndex]);

                listIndex[independentIndex]=3;
                paintTable(listIndex);
            }

		for (int i = 0; i < independentList.size(); i++) {
			int varIndex = ( (Integer)independentList.get(i)).intValue();
			String varHeader = columnModel.getColumn(varIndex).getHeaderValue().toString().trim();

			////////////System.out.println("addButtonIndependent varHeader at ["+i+"] = " + varHeader);

		}

        }

	protected void removeButtonIndependent() {

		//////////////System.out.println("Analysis removeButtonIndependent");
		if (independentLength > 0)
			independentLength--;
		int ct1=-1;
		int idx1 = 0;
		int sIdx = listIndepRemoved.getSelectedIndex();
		if(sIdx >-1) {
			for(int i=0;i<listIndex.length;i++) {
				if(listIndex[i] ==3)
				ct1++;
				if(ct1==sIdx) {

					////////////System.out.println("\nremoveButtonIndependent sIdx = " + sIdx);

					idx1=i;
					////////////System.out.println("removeButtonIndependent idx1 = " + idx1);

  					////////////System.out.println("removeButtonIndependent independentListCursor = " + independentListCursor);
 					////////////System.out.println("removeButtonIndependent idx1 = " + idx1);

					////////////System.out.println("removeButtonIndependent listIndepRemoved.getSelectedIndex = " + listIndepRemoved.getSelectedIndex());
					int jListSelectedIndex = listIndepRemoved.getSelectedIndex();
					//String jListSelectedName =
  					if (independentList != null && independentList.size() > 0) {
                         	//independentList.remove(0);//idx1);
                         	independentList.remove(sIdx);
                         	//independentList.remove(ct1);
  						////////////System.out.println("removeButtonIndependent independentList");
					}

					break;
				}
			}
			//////////////System.out.println("removeButtonIndependent listIndex[idx1] = " + listIndex[idx1]);

			listIndex[idx1]=1;
			paintTable(listIndex);
		}

		for (int i = 0; i < independentList.size(); i++) {
			int varIndex = ( (Integer)independentList.get(i)).intValue();
			String varHeader = columnModel.getColumn(varIndex).getHeaderValue().toString().trim();

			////////////System.out.println("removeButtonIndependent varHeader at ["+i+"] = " + varHeader);

		}

    }




	protected void removeButtonDependentAll() {
		//dependentLength = 0;
		dependentIndex = -1;

		for (int i = 0; i < depMax; i++) {
			try {
				removeButtonDependent();
			} catch (Exception e) {
			}
		}
		paintTable(listIndex);
    }

	protected void removeButtonIndependentAll() {
		//////////////////////////////System.out.println("removeButtonIndependentAll");
		independentLength = 0;
		independentIndex = -1;
		int ct1=-1;
		int idx1 = 0;
		int sIdx = 0;
		try {listIndepRemoved.getSelectedIndex();
			for (int i = 0; i < indMax; i++) {
				try {
					removeButtonIndependent();
				} catch (Exception e) {
					//////////////////////////////System.out.println("removeButtonIndependentAll e = " + e);
				}
			}
		}catch (Exception e) {
		}
		paintTable(listIndex);
    }

    /**This method returns an online description of this Statistical Analysis.
     * It should be overwritten by each specific analysis method.*/
    public String getOnlineDescription(){
        return onlineDescription;
    }
    public String getOnlineHelp(){
        return onlineHelp;
    }


    /**Event methods to be subclassed*/
    /**Mouse events*/
    public void mouseClicked(MouseEvent event){}
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mousePressed(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}

    /**Mouse motion events*/
    public void mouseMoved(MouseEvent event){}
    public void mouseDragged(MouseEvent event){}

    /**Window events*/
    public void windowOpened(WindowEvent event){}
    public void windowClosing(WindowEvent event){}
    public void windowClosed(WindowEvent event){}
    public void windowIconified(WindowEvent event){}
    public void windowDeiconified(WindowEvent event){}
    public void windowActivated(WindowEvent event){}
    public void windowDeactivated(WindowEvent event){}

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
	   if (mapDep) {
    	   tools1.add(depLabel);
    	   tools1.add(addButton1);
    	   tools1.add(removeButton1);}
       if (mapIndep) {
    	   tools2.add(indLabel);
    	   tools2.add(addButton2);
           tools2.add(removeButton2);
       }
        tools1.setFloatable(false);
		tools2.setFloatable(false);

        JPanel emptyPanel = new JPanel();
        JPanel emptyPanel2 = new JPanel();
        JPanel emptyPanel3 = new JPanel();
	   //mappingInnerPanel.setBackground(Color.RED);
        mappingInnerPanel.add(new JScrollPane(listAdded));
       
        if(mapDep){ 
        	mappingInnerPanel.add(tools1);
        	mappingInnerPanel.add(dependentPane);
        	mappingInnerPanel.add(emptyPanel);        	
           if (mapIndep){
        	   mappingInnerPanel.add(tools2);       
        	   mappingInnerPanel.add(new JScrollPane(listIndepRemoved));
           }else {
        	   mappingInnerPanel.add(emptyPanel2);     
        	   mappingInnerPanel.add(emptyPanel3);     
           }
        }
        else {
        	mappingInnerPanel.add(tools2);
        	if (mapIndep){
        		mappingInnerPanel.add(new JScrollPane(listIndepRemoved));
        		mappingInnerPanel.add(emptyPanel);
        		mappingInnerPanel.add(emptyPanel2);     
        		mappingInnerPanel.add(emptyPanel3);  
        	}
        	else {
        		mappingInnerPanel.add(emptyPanel);
        		mappingInnerPanel.add(emptyPanel2);     
        		mappingInnerPanel.add(emptyPanel3);           		
        	}
        }
        //listIndepRemoved.setBackground(Color.GREEN);
    }

    public void paintTable(int[] lstInd) {
        lModel1.clear();
        lModel2.clear();
        lModel3.clear();

        for(int i=0;i<lstInd.length;i++) {
		//////////////////////////System.out.println("paintTable lstInd["+i+"] = " + lstInd[i]);
            switch(lstInd[i]) {
                case 0:
                    break;
                case 1:
                    lModel1.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listAdded.setSelectedIndex(0);

                    break;
                case 2:
                    lModel2.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listDepRemoved.setSelectedIndex(0);
                    break;
                case 3:
                    lModel3.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listIndepRemoved.setSelectedIndex(0);

                    break;
                default:
                    break;

            }
		String temp = columnModel.getColumn(i).getHeaderValue().toString().trim();
        }
        listAdded.setSelectedIndex(0);
    }

    public void appendTableRows(int n) {
    //	//System.out.println("appendtableRow called n="+n);
        int cl= dataTable.getSelectedColumn();
        int ct = dataTable.getColumnCount();
        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
        for(int j=0;j<n;j++)
            tModel.addRow(new java.util.Vector(ct));
        dataTable.setModel(tModel);

        hModel = (javax.swing.table.DefaultTableModel) headerTable.getModel();
        int rowCount= hModel.getRowCount();
        for(int j=0;j<n;j++) {
            hModel.addRow(new Object[] {(rowCount+j+1)+":"});
        }
        headerTable.setModel(hModel);
    }

    public void resetTableRows(int n) {
    	////System.out.println("resettableRow called n="+n);
        int cl= dataTable.getSelectedColumn();
        int ct = dataTable.getColumnCount();
        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
        tModel.setNumRows(n);
        dataTable.setModel(tModel);

      // hModel = (javax.swing.table.DefaultTableModel) headerTable.getModel();
      //  int rowCount= hModel.getRowCount();
      //  //System.out.println("resettableRow called headerCount="+rowCount);
        hModel = new DefaultTableModel(0, 1);
        for(int j=0;j<n;j++) {
            hModel.addRow(new Object[] {(j+1)+":"});
        }
        headerTable.setModel(hModel);
    }


    public void appendTableColumns(int n) {
		
    	
    	   columnModel = dataTable.getColumnModel();
	       String[] savedHeaders= new String[columnModel.getColumnCount()];
	       for(int i=0; i<columnModel.getColumnCount(); i++){ 
	    	   //System.out.println("header"+i+"is "+columnModel.getColumn(i).getHeaderValue());
	    	   savedHeaders[i] = (String)columnModel.getColumn(i).getHeaderValue();
	       }
	       
	       int ct = dataTable.getColumnCount();
	       tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();

	       for(int j=0;j<n;j++)
	    	   tModel.addColumn(DEFAULT_HEADER_2+(ct+j+1),new java.util.Vector(ct));

	       dataTable.setModel(tModel);
	       columnModel = dataTable.getColumnModel();		       
	       for (int i=0; i< savedHeaders.length; i++)
	    	   columnModel.getColumn(i).setHeaderValue(savedHeaders[i]);

	       dTableHeader =new EditableHeader(columnModel);
	       dataTable.setTableHeader(dTableHeader);
			
	       int[] listIndex2 = new int[dataTable.getColumnCount()];
	        for(int j=0;j<listIndex.length;j++)
	            listIndex2[j]= listIndex[j];
	        for(int j=listIndex.length;j<listIndex2.length;j++)
	        	listIndex2[j]=1;

	        listIndex = new int[dataTable.getColumnCount()];
	        for(int j=0;j<listIndex2.length;j++)
	        	listIndex[j]= listIndex2[j];
	   
	   }

    public int getDistinctElements(Matrix Cl) {
        int rowCt = Cl.rows;
        int count=1;
        double clData =0;
        double[] distinctElements = new double[rowCt];

        distinctElements[0] = Cl.element[0][0];

        //  Double.parseDouble(table.getValueAt(0, column).toString());
        for(int i=1;i<rowCt;i++) {
            // clData[i] = table.getValueAt(i, column);
            clData =  Cl.element[i][0];

            //Double.parseDouble(table.getValueAt(i, column).toString());
            int flag =0;
            for(int j=0; j < count; j++) {
                if(clData == distinctElements[j]) {
                    flag = 1;
                    break;

                }

            }
            if(flag ==0) {
                distinctElements[count] = Cl.element[i][0];
                //Double.parseDouble(table.getValueAt(i, column).toString());
                count++;
            }
        }
        return count;
    }


	public void updateExample(ExampleData example) {
	//	System.out.println("updateExample");
		try {
			removeButtonIndependentAll();
		} catch (Exception e) {
			// some analysis do not have this type of buttons but i don't care. same for below.
		}
		try {
			removeButtonDependent();
		} catch (Exception e) {
		}
		try {
			removeButtonTime();
		} catch (Exception e) {
		}
		try {
			removeButtonCensor();
		} catch (Exception e) {
		}
		try {
			removeButtonGroupNames();
		} catch (Exception e) {
		}
		
		
	   //System.out.println("updateExample example = " + example );
		JTable tempDataTable =  example.getExample();


		//if(tempDataTable.getRowCount()>dataTable.getRowCount())
		//appendTableRows(tempDataTable.getRowCount()-dataTable.getRowCount());
		resetTableRows(tempDataTable.getRowCount()+2);
		if (tempDataTable.getRowCount()>0)
			hasExample = true;
		resetTableColumns(tempDataTable.getColumnCount()+2);
		for(int i=0;i<tempDataTable.getColumnCount();i++) {
			columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			columnModel.getColumn(i).setResizable(true);
		}

		for(int i=0;i<tempDataTable.getRowCount();i++)
		for(int j=0;j<tempDataTable.getColumnCount();j++) {
			dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
		}
		
		displayDataPanel();
                displayPCADataPanel();

        /* added to retreive the header */

		tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try {
			dataTable.setDragEnabled(true);
		} catch (Exception e) {
		}
		columnModel = dataTable.getColumnModel();
		dTableHeader =new EditableHeader(columnModel);
		dataTable.setTableHeader(dTableHeader);
		
		//dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dataTable.setCellSelectionEnabled(true);
		dataTable.setColumnSelectionAllowed(true);
		dataTable.setRowSelectionAllowed(true);

		paintTable(listIndex);
    }
	
	private void displayDataPanel(){
		dataPanel.removeAll();
		JScrollPane tablePanel = new JScrollPane(dataTable);
		tablePanel.setPreferredSize(new Dimension(DEFAULT_DATA_PANEL_WIDTH-30, DEFAULT_DATA_PANEL_HEIGHT));
		tablePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		tablePanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    	tablePanel.setRowHeaderView(headerTable);
		dataPanel.add(tablePanel);
	//	dataPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		
		dataPanel.validate();
	}
        private void displayPCADataPanel(){
		PCAPanel.removeAll();
		JScrollPane tablePanel = new JScrollPane(PCATable);
		tablePanel.setPreferredSize(new Dimension(DEFAULT_DATA_PANEL_WIDTH-30, DEFAULT_DATA_PANEL_HEIGHT));
		tablePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		tablePanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    	tablePanel.setRowHeaderView(headerTable);
		PCAPanel.add(tablePanel);
	//	dataPanel.setPreferredSize(new Dimension(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT));
		
		PCAPanel.validate();
	}

  public int chkDataIntegrity() {
	  int error = 0;
        if(dataTable.isEditing())
            dataTable.getCellEditor().stopCellEditing();
        OKDialog  OKD;
        String d = dataText;
        int dep=-1, fac1 = -1, fac2 = -1,flg = 0;
        for(int p=0;p<listIndex.length ;p++) {
            if(listIndex[p] == 2)
                dep=p;
            if(listIndex[p] == 3 && flg==0) {
                fac1=p;
                flg = 1;
            }
            if(listIndex[p] + fac1>=3 && listIndex[p] ==3 )
                fac2 = p;

        }

        if(dep==-1 || fac1 ==-1) {
            OKD = new OKDialog(null, true, "Map Fields First");
            OKD.setVisible(true);
            return error;
        }

        int i, j, k;

        final double zero = 0.00001;
        String newln = System.getProperty("line.separator");


        int dependantCount = 0;
        int factorsCount = 1;
        if(fac2>-1)
            factorsCount = 2;

        for(int n=0;n<dataTable.getRowCount();n++) {
            if(dataTable.getValueAt(n, dep)==null  || dataTable.getValueAt(n,dep).toString().trim().equals("")) {
                break;
            }
            dependantCount++;
        }
        if(dependantCount==0) {
            OKD = new OKDialog(null, true, "Dependant Column missing values");
            OKD.setVisible(true);
            return error;
        }

        int[] facs = new int[3];
        facs[0] = dep;
        facs[1] = fac1;
        facs[2] = fac2;

        int flag = 0;
        //int cRow = 0;
        for(int n=1;n<=factorsCount;n++) {
            for(int m=0;m<dependantCount;m++) {
                // cRow = m;
                if(dataTable.getValueAt(m, facs[n]) == null || dataTable.getValueAt(m, facs[n]).toString().trim().equals("")) {
                    flag = 1;
                    break;
                }
            }
        }
        if(flag==1) {
            OKD = new OKDialog(null, true, "Factors missing values");
            OKD.setVisible(true);
            return error;
        }

        if(factorsCount==0) {
            OKD = new OKDialog(null, true, "Factor column missing values");
            OKD.setVisible(true);
            return error;

        }
	   return     error;

    }

// added by annieche 200508
    public static Analysis getInstance(String classname) throws Exception {

        return (Analysis) Class.forName(classname).newInstance();
    }

    public Container getDisplayPane() {
		Container container1 = new Container();
		Container container2 = new Container();
		container1.setBackground(Color.black);
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBackground(Color.BLUE);
		container1.add(scrollPane);
		JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		container.setBackground(Color.BLUE);
		container.add(tabbedPanelContainer);
		return container;
    }

  /**
   * used for some sublcass to initialize befrore be used
   */
	public void initialize() {}

	protected void createActionComponents(JToolBar toolBar){
			//JButton button = null;
		 toolBar.setFloatable(false);
		/**************** COMPUTE BUTTON ****************/
			computeAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {

					resultPanelTextArea.setText("\n");

					for (int k =0; k < dataTable.getRowCount(); k++) {
						for (int i = 0; i < dataTable.getColumnCount();  i++) {
							String dataCase = (String)dataTable.getValueAt(k, i);
							if (dataCase != null) {
								//resultPanelTextArea.append(""+ dataCase+" ");
								hasExample= true;
							}
						}
					}

						doAnalysis();
						//if (useGraph)
						//doGraph();
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(resultPanel));
				}
			};

			calculateButton = toolBar.add(computeAction);
	       	calculateButton.setText(CALCULATE);
	       	calculateButton.setToolTipText("Press this Button to Do the Analysis");



			try {
				ExampleData exampleStatic = getStaticExample(analysisType,ExampleData.NULL_EXAMPLE);
				/********** parameter (short)1 -- for example 1 as default. *********/
				////System.out.println("exampleStatic = " + exampleStatic);
				////System.out.println("ExampleData.NULL_EXAMPLE = " + ExampleData.NULL_EXAMPLE);

				updateExample(exampleStatic);
			} catch (Exception e) {
				// if the analysis model doens't have an example, still won't break.
			}


	       	/**************** USE_STATIC_EXAMPLE ****************/

			if (useStaticExample[0]) {
				ExampleData exampleNull = new ExampleData(0, 0);
				//System.out.println("analysisType = " + analysisType);

					exampleStaticAction1 = new AbstractAction(){
					public void actionPerformed(ActionEvent e) {
						//System.out.println("actionPerformed exampleStaticAction1 ");
					// Create First Example
						exampleID = 1;
						//System.out.println("analysisType = " + analysisType);
						//System.out.println("exampleID = " + exampleID);
						ExampleData exampleStatic = getStaticExample(analysisType, exampleID);
						//System.out.println("exampleStatic = " + exampleStatic + ", if NullPointerException is seen, go to getStaticExample method (line 1641 something to add a new analysis)");

						/* somehow updateExample has to be called more than once to set to the correct header. I'll leave it like this before I figure out why. annie che 20051123 -_- */
						ExampleData exampleNull = new ExampleData(0, 0);

						updateExample(exampleNull);
						reset();
						updateExample(exampleStatic);
						
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
						}
					};
				exampleButton1 = toolBar.add(exampleStaticAction1);
				exampleButton1.setText(EXAMPLE_1);
				exampleButton1.setToolTipText(analysisDescription1);
			}

			if (useStaticExample[1]) {

				exampleStaticAction2 = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					// Create First Example
						//reset();
						ExampleData exampleStatic = null;
						exampleID = 2;
					
						exampleStatic = getStaticExample(analysisType, exampleID);
						//System.out.println("exampleID = " + exampleID + ", exampleStatic = " + exampleStatic);
						ExampleData exampleNull = new ExampleData(0, 0);
						updateExample(exampleNull);
						reset();
						updateExample(exampleStatic);
						
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
					}
				};
				exampleButton2 = toolBar.add(exampleStaticAction2);
				exampleButton2.setText(EXAMPLE_2);
				exampleButton2.setToolTipText(analysisDescription2);
			}

			if (useStaticExample[2]) {

				exampleStaticAction3 = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					// Create First Example
						//reset();
						ExampleData exampleStatic = null;
						exampleID = 3;
						//hasExample = true;
						exampleStatic = getStaticExample(analysisType, exampleID);
						//System.out.println("exampleID = " + exampleID + ", exampleStatic = " + exampleStatic);

						ExampleData exampleNull = new ExampleData(0, 0);
						updateExample(exampleNull);
						reset();
						updateExample(exampleStatic);
						
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
					}
				};
				exampleButton3 = toolBar.add(exampleStaticAction3);
				exampleButton3.setText(EXAMPLE_3);
				exampleButton3.setToolTipText(analysisDescription3);
			}

			if (useStaticExample[3]) {

				exampleStaticAction4 = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					// Create First Example
						//reset();
						ExampleData exampleStatic = null;
						exampleID = 4;
						exampleStatic = getStaticExample(analysisType, exampleID);
						//System.out.println("exampleID = " + exampleID + ", exampleStatic = " + exampleStatic);

						ExampleData exampleNull = new ExampleData(0, 0);
						updateExample(exampleNull);
						reset();
						updateExample(exampleStatic);
						
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
					}
				};
				exampleButton4 = toolBar.add(exampleStaticAction4);
				exampleButton4.setText(EXAMPLE_4);
				exampleButton4.setToolTipText(analysisDescription4);
			}

			if (useStaticExample[4]) {

				exampleStaticAction5 = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					// Create First Example
						//reset();
						ExampleData exampleStatic = null;
						exampleID = 5;
						exampleStatic = getStaticExample(analysisType, exampleID);

						ExampleData exampleNull = new ExampleData(0, 0);
						updateExample(exampleNull);
						reset();
						updateExample(exampleStatic);
					
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
					}
				};
				exampleButton5 = toolBar.add(exampleStaticAction5);
				exampleButton5.setText(EXAMPLE_5);
				exampleButton5.setToolTipText(analysisDescription5);
			}
			if (useStaticExample[5]) {

				exampleStaticAction6 = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					// Create First Example
						//reset();
						ExampleData exampleStatic = null;
						exampleID = 6;
						exampleStatic = getStaticExample(analysisType, exampleID);

						ExampleData exampleNull = new ExampleData(0, 0);

						updateExample(exampleNull);
						reset();
						updateExample(exampleStatic);
						
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
					}
				};
				exampleButton6 = toolBar.add(exampleStaticAction6);
				exampleButton6.setText(EXAMPLE_6);
				exampleButton6.setToolTipText(analysisDescription6);
			}

			if (useStaticExample[6]) {

				exampleStaticAction7 = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					// Create First Example
						//reset();
						ExampleData exampleStatic = null;
						exampleID = 7;
						exampleStatic = getStaticExample(analysisType, exampleID);

						ExampleData exampleNull = new ExampleData(0, 0);

						updateExample(exampleNull);
						reset();
						updateExample(exampleStatic);
						
						tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
					}
				};
				exampleButton7 = toolBar.add(exampleStaticAction7);
				exampleButton7.setText(EXAMPLE_7);
				exampleButton7.setToolTipText(analysisDescription7);
			}



	 		/**************** CLEAR BUTTON ****************/
	      	clearAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
				/* somehow reset has to be called more than once to set to the correct header. I'll leave it like this before I figure out why. annie che 20051123 -_- */
					reset();	// Need to work out what this means
				
					//tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
					//dataTable = new JTable(tModel);
					removeButtonIndependentAll();
					removeButtonDependent();
					ExampleData exampleNull = new ExampleData(0, 0);
					/* A Null Example (with no data) is used here
					to reset the table so that when "CLEAR" button is pressed, the cells of dataTable is NOT null.
					annieche 20060110. */
					updateExample(exampleNull);
					graphPanel.removeAll();
                                        dendroPanel.removeAll();
					randomDataStep = false;

					hypothesisType = null;
					// update the mapping panel for this table change
				//	resetTableColumns(dataTable.getColumnCount());
					//reset();
					tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
				}
			};

	       	clearButton = toolBar.add(clearAction);
	       	clearButton.setText(CLEAR);
	       	clearButton.setToolTipText("Clears All Windows");


	  		/**************** USE_RANDOM_EXAMPLE ****************/
			if (!showDendro && useRandomExample) {
				exampleRandomAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					reset();
					//exampleSampleSize = getExampleSampleSize();
					ExampleData exampleRandom = new ExampleDataRandom(analysisType,0, exampleSampleSize);
					ExampleData exampleNull = new ExampleData(0, 0);
					updateExample(exampleNull);
					reset();
					updateExample(exampleRandom);
					
					tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
				}
			};
			randomButton = toolBar.add(exampleRandomAction);
			randomButton.setText(RANDOM_EXAMPLE);
			randomButton.setToolTipText("This is a RANDOMLY GENERATED Example");

			}
			/********* USE_LOCAL_EXAMPLE: load local data files *********/
			/**************** USE_INPUT_EXAMPLE ****************/

			 if (useInputExample) {
				   exampleInputAction = new AbstractAction(){
				   public void actionPerformed(ActionEvent e) {
						if (isInitialInput) {
								reset();
							}
						}
					  };

				inputButton = toolBar.add(exampleInputAction);
				inputButton.setText(USER_INPUT);
				inputButton.setToolTipText("");
			}

 	}

	protected  void doGraph(){
	}
	protected void resetGraph(){
	}
        protected void resetDendro(){
        }
	protected void resetVisualize(){
	}
	protected void resetParameterSelect(){
	}


    protected void addButtonTime() {
    }
    protected void removeButtonTime() {
    }
    protected void addButtonCensor() {
    }

    protected void removeButtonCensor() {
    }
    protected void addButtonGroupNames() {
    }
    protected void removeButtonGroupNames() {
    }

    public static ExampleData getStaticExample(short analysisType, short exampleID) {
	    ExampleData exampleStatic = null;

	    if (exampleID == ExampleData.NULL_EXAMPLE) {
	    		return new ExampleData(analysisType, exampleID);
	    }
	    //////System.out.println("analysisType = " + analysisType + ", exampleID = " + exampleID);
	    switch (analysisType) {
			case AnalysisType.ANOVA_ONE_WAY:
				exampleStatic = new AnovaOneWayExamples(analysisType, exampleID);
				break;
			case AnalysisType.ANOVA_TWO_WAY:
				exampleStatic = new AnovaTwoWayExamples(analysisType, exampleID);
				break;
			case AnalysisType.MULTI_LINEAR_REGRESSION:
				exampleStatic = new MultiLinearRegressionExamples(analysisType, exampleID);
				break;
			case AnalysisType.ONE_T: {
				exampleStatic = new OneTExamples(analysisType, exampleID);
				//////System.out.println("ONE_T exampleStatic = " + exampleStatic);
				break;
			}
                        case AnalysisType.ONE_Z: {
				exampleStatic = new OneTExamples(analysisType, exampleID);
				//////System.out.println("ONE_T exampleStatic = " + exampleStatic);
				break;
			}
                        case AnalysisType.PRINCIPAL_COMPONENT_ANALYSIS: {
				exampleStatic = new PrincipalComponentAnalysisExamples(analysisType, exampleID);
				//////System.out.println("ONE_T exampleStatic = " + exampleStatic);
				break;
                        }
			case AnalysisType.SIMPLE_LINEAR_REGRESSION:
				exampleStatic = new SimpleLinearRegressionExamples(analysisType, exampleID);
				break;
			case AnalysisType.SURVIVAL:
				exampleStatic = new SurvivalExamples(analysisType, exampleID);
				break;
			case AnalysisType.TWO_INDEPENDENT_T:
				exampleStatic = new TwoIndependentTExamples(analysisType, exampleID);
				break;
			case AnalysisType.TWO_INDEPENDENT_WILCOXON:
				////////////////System.out.println("AnalysisType.TWO_INDEPENDENT_WILCOXON");
				exampleStatic = new TwoIndependentWilcoxonExamples(analysisType, exampleID);
				break;

			case AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS:
				exampleStatic = new TwoIndependentKruskalWalliesExamples(analysisType, exampleID);
				break;
			case AnalysisType.TWO_INDEPENDENT_FRIEDMAN: {
				exampleStatic = new TwoIndependentFriedmanExamples(analysisType, exampleID);
				break;
			}

			case AnalysisType.TWO_PAIRED_T:
				exampleStatic = new TwoPairedTExamples(analysisType, exampleID);
				break;
			case AnalysisType.TWO_PAIRED_SIGNED_RANK: // wilcoxon signed rank test
				exampleStatic = new TwoPairedSignedRankExamples(analysisType, exampleID);
				break;
			case AnalysisType.TWO_PAIRED_SIGN_TEST: // binomial with null = B(0.5)
				exampleStatic = new TwoPairedSignTestExamples(analysisType, exampleID);
				break;
			case AnalysisType.DICHOTOMOUS_PROPORTION: {
				exampleStatic = new DichotomousProportionExamples(analysisType, exampleID);
				break;
			}
			case AnalysisType.CHI_SQUARE_MODEL_FIT: {
				exampleStatic = new ChiSquareModelFitExamples(analysisType, exampleID);
				break;
			}

			case AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE: {
				exampleStatic = new ChiSquareContingencyTableExamples(analysisType, exampleID);
				break;
			}
			case AnalysisType.KOLMOGOROV_SMIRNOFF: {
				exampleStatic = new KolmogorovSmirnoffExamples(analysisType, exampleID);
				break;

			}
			case AnalysisType.FLIGNER_KILLEEN: {
				exampleStatic = new FlignerKilleenExamples(analysisType, exampleID);
				break;
			}	
			case AnalysisType.CI: {
				exampleStatic = new CIExamples(analysisType, exampleID);
				break;
			}
			case AnalysisType.LOGISTIC_REGRESSION: {
				exampleStatic = new LogisticRegressionExamples(analysisType, exampleID);
				break;
			}
                        case AnalysisType.Clustering: {
				exampleStatic = new ClusteringExamples(analysisType, exampleID);
				break;
			}
                            
			/*
			case AnalysisType.FISHER_EXACT: {
				exampleStatic = new FisherExactExamples(analysisType, exampleID);
				break;
			}*/
		}
		return exampleStatic;
	}

    public void keyPressed(KeyEvent e) {
    	//System.out.println("Analysis: keyPressed="+e.getKeyCode()+"dataTable.getSelectedRow()="+dataTable.getSelectedRow());
		if (e.getKeyCode()==127||e.getKeyCode()==8){
		if ((dataTable.getSelectedRow() >= 0) && (dataTable.getSelectedColumn() >= 0)){
			int[] rows=dataTable.getSelectedRows();
			int[] cols=dataTable.getSelectedColumns();
			for (int i=rows[0]; i<=rows[rows.length-1]; i++)
			 for (int j = cols[0]; j<=cols[cols.length-1]; j++){
				 dataTable.setValueAt(null, i, j);
			}
		}
		
		displayDataPanel();
                displayPCADataPanel();
		}
	}


	public void keyReleased(KeyEvent e) {
	
	}

	public void keyTyped(KeyEvent e) {

	}
}


