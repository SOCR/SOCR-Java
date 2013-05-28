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
package edu.ucla.stat.SOCR.analyses.jri.gui;

import edu.ucla.stat.SOCR.analyses.example.ExampleData;
import edu.ucla.stat.SOCR.analyses.example.ExampleDataRandom;
import edu.ucla.stat.SOCR.analyses.gui.Chart;
import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.analyses.jri.*;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import java.net.URLConnection;
import java.net.URL;
import java.net.HttpURLConnection;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.result.Result;
//import edu.ucla.stat.SOCR.analyses.server.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.core.Modeler;
import edu.ucla.stat.SOCR.core.ModelerGui;
import edu.ucla.stat.SOCR.servlet.util.*;

/*import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import java.security.Key;
import java.security.Security;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;*/
import javax.crypto.*;
import java.security.*;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;
import com.sun.crypto.provider.SunJCE;

/**This class defines a basic type of Statistical Analysis procedure that can be
 * subclassed by the specific types of analyses
 * (e.g., ANOVA, Regression, prediction, etc.)*/
public class Analysis extends JApplet implements Runnable, MouseListener, ActionListener,MouseMotionListener, WindowListener {

	/****************** Start Variable Declaration **********************/
	private static String DEFAULT_USER_NAME = "SoCr";
	private static String DEFAULT_PASSWORD = "StatistiCS";


	// CONSTANT decalration
	protected final String EXAMPLE 	= " EXAMPLE ";
	protected final String CALCULATE 	= " CALCULATE ";
	protected final String CLEAR 	= " CLEAR ";
	protected final String LOAD_FILE 	= " LOAD FILE ";
	protected final String USER_INPUT 	= " USER INPUT ";
	protected final String USE_SERVER 	= CALCULATE;

	protected final String DATA 		= "DATA";
	protected final String RESULT 	= "RESULT";
	protected final String GRAPH 	= "GRAPH";
	protected final String MAPPING 	= "MAPPING";
	//protected final String SELECT 	= "SELECT PARAMETERS";
	protected final String ADD 		= " ADD  ";
	protected final String REMOVE 		= "REMOVE";
	//protected final String VISUALIZE 		= "COMPARE CURVES";
	protected final String SHOW_ALL 		= "SHOW ALL";

	protected final String RANDOM_EXAMPLE 		= "RANDOM EXAMPLE";
	protected final String DEPENDENT 		= "DEPENDENT";
	protected final String INDEPENDENT 		= "INDEPENDENT";
	protected final String VARIABLE 		= "VARIABLE";
	protected final String DEFAULT_HEADER 		= "C";
	protected final String DATA_MISSING_MESSAGE 		= "DATA MISSING: Click on EXAMPLE for data first and click on MAPPING to continue.";
	protected final String VARIABLE_MISSING_MESSAGE 		= "VARIABLE MISSING: Map variables first by clicking on MAPPING.";
	protected final String DATA_COLINEAR_MESSAGE 		= "DATA CLOSE TO COLINEAR: Please remove colinearity before continue.";

	//protected static final int INDEX_0 = 0;
	//protected static final int INDEX_1 = 1;
	//protected static final int INDEX_2 = 2;

	protected static final String outputFontFace = "Helvetica";
	protected static final int outputFontSize = 12;

	protected String onlineDescription = "http://mathworld.wolfram.com/";
	protected String onlineHelp = "http://en.wikipedia.org/wiki/Statistical_analysis";

	private static final int TEXT_AREA_ROW_SIZE = 100;
	private static final int TEXT_AREA_COLUMN_SIZE = 100;

	protected int independentLength = 0;

	/*	Size of the plots */
	protected int plotWidth = 300; 	// the width for each of the several residual plots
	protected int plotHeight = 300;	// height

	protected int independentIndex = -1; 	// "x"
	protected int dependentIndex = -1;		// "y"

	protected int timeIndex = -1; 	// survival time
	protected int censorIndex = -1;		// survival censor
	protected int groupNamesIndex = -1;		// survival group names

	protected String analysisName = "";

	/************** flags that controls the actions **************/
	protected boolean useStaticExample = true;
	protected boolean useRandomExample = true;
	protected boolean useLocalExample = true;
	protected boolean useInputExample = true;
	protected boolean useServerExample = true;
	protected boolean callServer = true; // to get R to work.
	protected boolean useGraph = true; // subclass calls doGraph()

	protected boolean useDataPanel = true;
	protected boolean useResultPanel = true;
	protected boolean useGraphPanel = true;
	protected boolean useMapPanel = true;
	protected boolean useSelectPanel = true;
	protected boolean useVisualizePanel = true;
	protected boolean usemixPanel = true;

	protected boolean isInitialInput = true;
	protected boolean hasExample = false;		//
	/************** ************** ************** **************/

	protected Action exampleStaticAction; // example data from ExampleData class.
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
	protected String analysisDescription = "";
	protected String[] independentHeaderArray = null;
	protected int exampleSampleSize = 0;


	private int time = 0,
	updateCount = 0,
	stopCount = 0,
	tabbedPaneCount = 0,
	toolbarCount = 0;
	public int selectedInd = 0;
	public static JTable dataTable;
	public Object [][] dataObject;
	protected String dataText = "";
	protected int columnNumber 	= 10;
	protected int rowNumber 		= 10;
	public String [] columnNames;
	public static javax.swing.table.DefaultTableModel tModel;
	//protected String resultText = "Enter Data and then Click <Compute>";
	protected static JPanel controlPanel, dataPanel, resultPanel, graphPanel, mappingInnerPanel, mappingPanel, selectPanel, visualizePanel, mixPanel;
	//protected static JPanel controlPanel2, dataPanel2, resultPanel2, graphPanel2, mappingInnerPanel2, mappingPanel2, selectPanel2, visualizePanel2; // they go into the mix panel

	public JTextArea resultPanelTextArea;

	// public JPanel topPanel, bottomPanel;
	protected TableColumnModel columnModel;
	public JList listAdded, listDepRemoved, listIndepRemoved;
	public JList listTime, listCensor, listGroupNames; // for Survival.

	public JButton addButton1 = new JButton(ADD);
	public JButton addButton2 = new JButton("ADD 2");
	public JButton removeButton1  = new JButton(REMOVE);
	public JButton removeButton2 = new JButton(REMOVE);

	public JButton addButton3 = new JButton(ADD);
	public JButton removeButton3 = new JButton(REMOVE);

	JButton exampleButton = null;
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
	protected ArrayList<Integer> independentList = null;
	protected int independentListCursor = 0;

	public int[] listIndex;
	JToolBar tools1, tools2, tools3;
	//protected Distribution distribution;
	protected JScrollPane mixPanelContainer;
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

	public JPanel leftAnalysisChoicePanel;
	protected JLabel depLabel = new JLabel(DEPENDENT);
	protected JLabel indLabel = new JLabel(INDEPENDENT);
	protected JLabel varLabel = new JLabel(VARIABLE);
	protected String inputXMLString = null;
	protected Data data = null;
	protected String xmlInputString = null;
	protected String xmlOutputString= null;

	protected static boolean randomDataStep = false;

	protected String hypothesisType = null;
	protected NormalCurve graphRawData = new NormalCurve(ModelerGui.GUI_LOWER_LIMIT, ModelerGui.GUI_UPPER_LIMIT, 1);
	protected NormalCurve graphSampleMean = new NormalCurve(ModelerGui.GUI_LOWER_LIMIT, ModelerGui.GUI_UPPER_LIMIT, 1);
	protected NormalCurve graphZScore = new NormalCurve(ModelerGui.GUI_LOWER_LIMIT, ModelerGui.GUI_UPPER_LIMIT, 1);

	//protected ChartPanel chartPanel;
	protected	JPanel leftPanel = new JPanel();
	protected	JPanel rightPanel = new JPanel();
	protected	static JButton test1 = new JButton();
	protected	static JButton test2 = new JButton();

	/********************* Start Methods **************************/
    /**This method initializes the Analysis, by setting up the basic tabbedPanes.*/
    public void init(){
		setFont(font);
		setName("SOCR: Statistical Analysis");
		//Get frame of the applet
		frame = getFrame(this.getContentPane());

		setMainPanel();

		dataPanel = new JPanel();
		resultPanel = new JPanel();
		resultPanelTextArea = new JTextArea();
		graphPanel = new JPanel();
        	mappingPanel = new JPanel(new BorderLayout());
        	mappingInnerPanel = new JPanel(new GridLayout(2,3,50, 50));
		//selectPanel = new JPanel();
		//visualizePanel = new JPanel();

		mixPanelContainer = new JScrollPane(mixPanel);
		mixPanelContainer.setPreferredSize(new Dimension(400,400));
		mixPanel = new JPanel();

		dataPanel.setPreferredSize(new Dimension(400, 400));
		graphPanel.setPreferredSize(new Dimension(400, 400));
		resultPanel.setPreferredSize(new Dimension(400, 400));

        	tools1 = new JToolBar(JToolBar.VERTICAL);
        	tools2 = new JToolBar( JToolBar.VERTICAL);
        	tools3 = new JToolBar( JToolBar.VERTICAL);


		addTabbedPane(DATA, dataPanel);
		addTabbedPane(GRAPH, graphPanel);
		addTabbedPane(MAPPING, mappingPanel);
		addTabbedPane(RESULT, resultPanel);

		setDataPanel();
		setGraphPanel();
		setMappingPanel();
		setResultPanel();

        this.getContentPane().add(new JScrollPane(tabbedPanelContainer), BorderLayout.CENTER);
		tabbedPanelContainer.addChangeListener(new ChangeListener(){

				public void stateChanged(ChangeEvent e) {

				}
		});

    }

    protected void setDataPanel() {
	     ////////////System.out.println("setDataPanel");
		dataObject = new Object[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		independentList = new ArrayList<Integer>();
		for (int i = 0; i < columnNumber; i++) {
			columnNames[i] = new String(DEFAULT_HEADER+(i+1));
		}

		tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames); // table model

		dataTable = new JTable(tModel);
		dataTable.doLayout();
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try {
			dataTable.setDragEnabled(true);
		} catch (Exception e) {
		}
		columnModel = dataTable.getColumnModel();

		dataTable.setTableHeader(new EditableHeader(columnModel));
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dataTable.setCellSelectionEnabled(true);
		dataTable.setColumnSelectionAllowed(true);
		dataTable.setRowSelectionAllowed(true);
		dataPanel.add(new JScrollPane(dataTable));
	}
	protected void setResultPanel() {

		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		resultPanelTextArea.setLineWrap(true);

		resultPanelTextArea.setRows(60);
		resultPanelTextArea.setColumns(60);
		resultPanel.setBackground(Color.WHITE);
		resultPanelTextArea.setEditable(true);

		resultPanelTextArea.setBackground(Color.WHITE);
		resultPanelTextArea.setForeground(Color.BLACK);

		resultPanel.add(new JScrollPane(resultPanelTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		resultPanel.setPreferredSize(new Dimension(400, 400));


	}

	protected void setGraphPanel() {
		graphPanel.setLayout(new BorderLayout());
	}

	protected void setVisualizePanel() {
		visualizePanel.removeAll();
	}


	protected void setSelectPanel() {

	}


	protected void setShowAllPanel() {
		mixPanel.setBackground(Color.PINK);
		leftPanel.add(test1);
		rightPanel.add(test2);

		mixPanel.add(leftPanel, BorderLayout.WEST);
		mixPanel.add(rightPanel, BorderLayout.EAST);

	}

    /** Sets the amin Stat Analysis GUI with left-analysis-choice & right-data-cointrol
     * Jpanel's.*/
    public void setMainPanel() {
        tabbedPanelContainer = new JTabbedPane();
    }

    /**This method returns basic copyright, author, and other metadata information*/
    public String getAppletInfo(){
        return "\nUCLA Department of Statistics: SOCR Resource\n"+
        "http://www.stat.ucla.edu\n";
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
     	hasExample = false;
     	dependentIndex = -1;
     	independentIndex = -1;

         	independentHeaderArray = null;
     	independentList.clear();
		resultPanelTextArea.setText("");
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();


		dataPanel.removeAll();
		dataPanel.add(new JScrollPane(dataTable));
		dataPanel.validate();

		for(int i =0; i<dataTable.getRowCount(); i++) {
			for(int j=0;j< dataTable.getColumnCount();j++) {
				dataTable.setValueAt("", i, j);

			}
		}
		for (int i = 0; i < columnNumber; i++) {
			columnNames[i] = new String(DEFAULT_HEADER+(i+1));
			columnModel.getColumn(i).setHeaderValue(columnNames[i]);
		}
		time = 0;
		resetGraph();
		resetVisualize();
		resetParameterSelect();
	 }

	public void resetMappingList() {
		dependentIndex = -1;
		independentIndex = -1;
		independentList.clear();
		removeButtonDependent();
		removeButtonIndependentAll();
	}
	public void resetMappingListGUI() {
		dependentIndex = -1;
		independentIndex = -1;
	     independentList.clear();
		removeButtonDependent();
		removeButtonIndependentAll();
	}
    /**This method defines what the analysis really does, and should be overridden
     * by the specific analysis tools.*/
    public void doAnalysis(){
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
    		removeButtonIndependent();
    	}


    }
    private void addButtonDependent() {
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
		independentIndex = 0;
            int ct1=-1;
            int sIdx = listAdded.getSelectedIndex();
            int idx3 = lModel3.getSize();
            if(sIdx >-1 && idx3 <indMax) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
                        independentIndex=i;
                        independentList.add(independentListCursor, new Integer(independentIndex));
                        break;
                    }
                }
                listIndex[independentIndex]=3;
                paintTable(listIndex);
            }
        }
	private void removeButtonIndependent() {
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
					idx1=i;
					break;
				}
			}
			listIndex[idx1]=1;
			paintTable(listIndex);
		}
    }
	protected void removeButtonIndependentAll() {
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
				}
			}
		}catch (Exception e) {
		}
		paintTable(listIndex);
    }

    /**This method returns an online description of this Statistical Analysis.
     * It should be overwritten by each specific analysis method.*/
    public String getOnlineDescription(){
        return new String("http://socr.stat.ucla.edu/");
    }
    public String getOnlineHelp(){
        return new String("http://en.wikipedia.org/wiki/Statistical_analysis");
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
        ////////////System.out.println("initMappingPanel mappingPanel = " + mappingPanel);
        listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;

        mappingPanel.add(mappingInnerPanel,BorderLayout.CENTER);

        //addButton1.addActionListener(this);
        //addButton2.addActionListener(this);
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

        tools1.add(depLabel);
        tools2.add(indLabel);

        tools1.add(addButton1);
        tools1.add(removeButton1);
        tools2.add(addButton2);
        tools2.add(removeButton2);

        JPanel emptyPanel = new JPanel();

        mappingInnerPanel.add(new JScrollPane(listAdded));
        mappingInnerPanel.add(tools1);
        mappingInnerPanel.add(new JScrollPane(listDepRemoved));
        mappingInnerPanel.add(emptyPanel);
        mappingInnerPanel.add(tools2);
        mappingInnerPanel.add(new JScrollPane(listIndepRemoved));
    }

    public void paintTable(int[] lstInd) {
        lModel1.clear();
        lModel2.clear();
        lModel3.clear();

        for(int i=0;i<lstInd.length;i++) {

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
        int cl= dataTable.getSelectedColumn();
        int ct = dataTable.getColumnCount();
        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
        for(int j=0;j<n;j++)
            tModel.addRow(new java.util.Vector(ct));
        dataTable.setModel(tModel);

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
		//////////System.out.println("updateExample");
		try {
			removeButtonIndependentAll();
		} catch (Exception e) {
			// some analysis do not have this type of buttons but i don't care. same for below.
		}

		hasExample = true;
		JTable tempDataTable =  example.getExample();
		if(tempDataTable.getRowCount()>dataTable.getRowCount())
		appendTableRows(tempDataTable.getRowCount()-dataTable.getRowCount());
		for(int i=0;i<tempDataTable.getColumnCount();i++) {
			columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
		}

		for(int i=0;i<tempDataTable.getRowCount();i++)
		for(int j=0;j<tempDataTable.getColumnCount();j++) {
			dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
		}
		dataPanel.removeAll();
		dataPanel.add(new JScrollPane(dataTable));
		dataPanel.validate();

        /* added to retreive the header */

		tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try {
			dataTable.setDragEnabled(true);
		} catch (Exception e) {
		}
		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));

		paintTable(listIndex);
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


		/**************** USE_STATIC_EXAMPLE ****************/
			if (useStaticExample) {
				exampleStaticAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
				// Create First Example
					//reset();
					ExampleData exampleStatic = new ExampleData(analysisType, 0);

					/* somehow updateExample has to be called more than once to set to the correct header. I'll leave it like this before I figure out why. annie che 20051123 -_- */
					updateExample(exampleStatic);
					updateExample(exampleStatic);

				}
			};

			exampleButton = toolBar.add(exampleStaticAction);
			exampleButton.setText(EXAMPLE);
			exampleButton.setToolTipText(analysisDescription);
		}

		////////System.out.println("callServer = " + callServer);
		 if (callServer) {
			   callServerAction = new AbstractAction(){
			   public void actionPerformed(ActionEvent e) {
					resultPanelTextArea.append("\n************");

					doAnalysis();

					//Key key = KeyUtil.generateKey();
					//String keyString = KeyUtil.generatePublicKey();

					try {

						/*
						DHParameterSpec dhSkipParamSpec = new DHParameterSpec(KeyUtil.skip1024Modulus, KeyUtil.skip1024Base);
						///////////////////System.out.println("\nIn run, ALICE: Generate DH keypair ...");
						KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
						aliceKpairGen.initialize(dhSkipParamSpec);
						KeyPair aliceKpair = aliceKpairGen.generateKeyPair();

						// Alice creates and initializes her DH KeyAgreement object
						///////////////////System.out.println("\nIn run, ALICE: Initialization ...");
						KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
						aliceKeyAgree.init(aliceKpair.getPrivate());


						// Alice encodes her public key, and sends it over to Bob.

						byte[] alicePubKeyEnc = aliceKpair.getPublic().getEncoded();
						//resultPanelTextArea.append("\nxmlInputString = " + xmlInputString + "\n");
						*/


						Data data = new Data();

						//xmlInputString = data.getAnalysisXMLInputString(AnalysisType.LOGISTIC_REGRESSION);

						xmlOutputString = getAnalysisOutputFromServer(getXMLInputString());
						//System.out.println("Analysis xmlOutputString = " + xmlOutputString);


						/*
						//xmlInputString = new String(alicePubKeyEnc);//keyString;
						ByteArrayOutputStream compressedInputStream = GZIP.compressString(xmlInputString);
						//String encryptedInput = EncryptUtil.encrypt(xmlInputString, key);
						//ServletCaller test = new ServletCaller();

						ByteArrayOutputStream xmlOutputStream = getAnalysisOutputStreamFromServer(compressedInputStream);//encryptedInput);
						//resultPanelTextArea.append(xmlOutputStream);
						////////////////System.out.println("Analysis now setXMLOutputString = " + xmlOutputStream);
						String decompressedOutput = GZIP.decompressString(xmlOutputStream);
						setXMLOutputString(decompressedOutput);
						resultPanelTextArea.append("Analysis xmlOutputStream = " +xmlOutputStream);
						*/


					} catch (Exception ex) {
						////////////////System.out.println("Exception	" + ex);
					}


					//passDataToServer();
					}
				  };

			serverButton = toolBar.add(callServerAction);
			serverButton.setText(USE_SERVER);
			serverButton.setToolTipText("Call Server");
		}
			/**************** COMPUTE BUTTON ****************
			computeAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {

					resultPanelTextArea.setText("\n");

					for (int k =0; k < dataTable.getRowCount(); k++) {
						for (int i = 0; i < dataTable.getColumnCount();  i++) {
							String dataCase = (String)dataTable.getValueAt(k, i);
							if (dataCase != null) {
								//resultPanelTextArea.append(""+ dataCase+" ");
							}
						}
					}
						doAnalysis();
						//if (useGraph)
						//	doGraph();

				}
			};

			calculateButton = toolBar.add(computeAction);
	       	calculateButton.setText(CALCULATE);
	       	calculateButton.setToolTipText("Press this Button to Do the Analysis");
			*/
	 		/**************** CLEAR BUTTON ****************/
	      	clearAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
				/* somehow reset has to be called more than once to set to the correct header. I'll leave it like this before I figure out why. annie che 20051123 -_- */
					reset();	// Need to work out what this means
					reset();	// need two to clear all -- why?
					//resetMappingList();
					resultPanelTextArea.setText("");

					tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
					dataTable = new JTable(tModel);
					removeButtonIndependentAll();
					removeButtonDependent();
					ExampleData exampleNull = new ExampleData(0, 0);
					/* A Null Example (with no data) is used here
					to reset the table so that when "CLEAR" button is pressed, the cells of dataTable is NOT null.
					annieche 20060110. */
					updateExample(exampleNull);
					updateExample(exampleNull);
					graphPanel.removeAll();
					randomDataStep = false;

					hypothesisType = null;

				}
			};

	       	clearButton = toolBar.add(clearAction);
	       	clearButton.setText(CLEAR);
	       	clearButton.setToolTipText("Clears All Windows");


	  		/**************** USE_RANDOM_EXAMPLE ****************/
			if (useRandomExample) {
				exampleRandomAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					reset();
					//exampleSampleSize = getExampleSampleSize();
					ExampleData exampleRandom = new ExampleDataRandom(analysisType,0, exampleSampleSize);
					updateExample(exampleRandom);



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
				inputButton.setToolTipText("Simple Regression Example");
			}


 	}

     //public String passDataToServer() {
     	// this is to be overriden by sub classes.
     	//return null;
     //}
	protected  void doGraph(){
	}
	protected void resetGraph(){
	}
	protected void resetVisualize(){
	}
	protected void resetParameterSelect(){
	}
	/*
	protected void initSelectPanel(){
	}
	protected void initMixPanel(){
	}
	protected void initVisualizePanel(){
	}*/

	// to be overriding by sub classes.
	protected  ByteArrayOutputStream getAnalysisOutputStreamFromServer(ByteArrayOutputStream input) {


		return null;

	}

	// should instead do the GZIP and encrypt/decrypt here.
	protected  String getAnalysisOutputFromServer(String input) {//Data data, short analysisType) {
		java.io.BufferedWriter bWriter = null;
		URLConnection connection = null;
		String inputXML = null;
		String encryptedInputXML = null;
		Object zippedInput = null;
		String resultString = "";

		////System.out.println("Analysis getAnalysisOutputFromServer input = " + input);

		bWriter = null;
		connection = null;
		String target= ServletConstant.ANALYSIS_SERVLET;
		////System.out.println("Analysis target = " + target);

		Key someKey = CryptoUtil.generateKey();
		String encrptedResult = CryptoUtil.encrypt(getXMLInputString(), someKey);
		ClientObject clientObject = new ClientObject(someKey, getXMLInputString(), DEFAULT_USER_NAME, DEFAULT_PASSWORD);
		//ClientObject clientObject = new ClientObject(someKey, encrptedResult, DEFAULT_USER_NAME, DEFAULT_PASSWORD);

		//System.out.println("In Main someKey = " + clientObject.getKey());
		//System.out.println("In Main getStringAttachment = " + clientObject.getStringAttachment());
		//System.out.println("In Main getStringAttachment = " + clientObject.getUserName());
		//System.out.println("In Main getStringAttachment = " + clientObject.getPassword());
		// note that everything the key will be different and the result will be different.
		String decryptedResult = null;//CryptoUtil.decrypt(result.getBytes(), someKey);

		// see http://www2.sys-con.com/ITSG/virtualcd/Java/archives/0309/darby/index.html
		// for a tutorial of stream i/o
		try {
			// construct url connection.
			URL url = new URL(target);
			connection = (HttpURLConnection) url.openConnection();
			((HttpURLConnection)connection).setRequestMethod("POST");
			connection.setDoOutput(true);
			// send data to the server


			ObjectOutputStream outputToHost = new ObjectOutputStream(connection.getOutputStream());
			// serialize the object
			outputToHost.writeObject(clientObject);
			outputToHost.flush();
			outputToHost.close();
			// receive data (some analysis output) from the server
			////System.out.println("In jri.Analysis before bReader");

			java.io.BufferedReader bReader = null;
			////System.out.println("In jri.Analysis bReader = " + bReader);

			bReader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));

			////System.out.println("In jri.Analysis before StringBuffer bReader = " + bReader);

			/*			String line;
						StringBuffer sb = new StringBuffer();
						while ( (line = bReader.readLine()) != null) {
						sb.append(line);
					}
					resultString = sb.toString();
			*/
			
			InputStream inputStreamFromServlet = connection.getInputStream();
			ObjectInputStream dataFromServlet = new ObjectInputStream(connection.getInputStream());
			ServerObject serverObject = (ServerObject) dataFromServlet.readObject();

			resultString = serverObject.getStringAttachment();

			////System.out.println("In jri.Analysis resultString = " + resultString);

			//bReader.close();

			((HttpURLConnection)connection).disconnect();
		} catch (java.io.IOException ex) {
			////System.out.println("In jri.Analysis java.io.IOException = " + ex);
			resultString += ex.toString();
			//throw ex;
		} catch (Exception e) {
			////System.out.println("In jri.Analysis java.io.IOException = " + ex);
			resultString += e.toString();
		} finally {
			if (bWriter != null) {
				try {
					bWriter.close();
				} catch (Exception ex) {
					resultString += ex.toString();
				}
			}
			if (connection != null) {
				try {
					((HttpURLConnection)connection).disconnect();
				} catch (Exception ex) {
					resultString += ex.toString();
				}
			}
			setXMLOutputString(resultString);
			//return resultString;
		}
		return resultString;
		//return null;
	}
	
	protected String getXMLInputString() {
		return this.xmlInputString;
	}
	
	protected void setXMLInputString(String input) {
		this.xmlInputString = input;
	}
	
	protected void setXMLOutputString(String input) {
		////////////////System.out.println("=======================Analysis setXMLOutputString = " + input);
		this.xmlOutputString = input;
	}
	protected String getXMLOutputString() {
		////////////////System.out.println("Analysis this.xmlOutputStream = " + this.xmlOutputStream);
		return this.xmlOutputString;
	}
	//private String inputXmlString = "";//"<analysis_input><analysis_model><analysis_name>LINEAR_MODEL</analysis_name></analysis_model><data><variable><variable_name>x</variable_name><variable_type>INDENPEDENT</variable_type><data_type>QUANTITATIVE</data_type><data_value>68.0, 49.0, 60.0, 68.0, 97.0, 82.0, 59.0, 50.0, 73.0, 39.0, 71.0, 95.0, 61.0, 72.0, 87.0, 40.0, 66.0, 58.0, 58.0 77.0</data_value></variable><variable><variable_name>t</variable_name><variable_type>DENPEDENT</variable_type><data_type>QUANTITATIVE</data_type><data_value>75.0, 63.0, 57.0, 88.0, 88.0, 79.0, 82.0, 73.0, 90.0, 62.0, 70.0,96.0, 76.0, 75.0, 85.0, 40.0, 74.0, 70.0, 75.0, 72.0</data_value></variable></data></analysis_input>";

}


