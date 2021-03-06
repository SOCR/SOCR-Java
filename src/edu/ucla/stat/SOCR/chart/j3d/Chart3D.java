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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.chart.j3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import edu.ucla.stat.SOCR.chart.ChartExampleData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinned2DData;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;
import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.JScrollPaneAdjuster;
import edu.ucla.stat.SOCR.util.JTableRowHeaderResizer;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.util.RowHeaderRenderer;


/**This class defines a basic type of Statistical Chart that can be
 * subclassed by the specific types of chart
 **/
public class Chart3D extends JApplet implements Runnable, MouseListener, ActionListener, MouseMotionListener, WindowListener, AdjustmentListener, KeyListener{

//	protected boolean trimColumn = false;  // false :allow the column size to be different in the dataTable
	
	 //set to true to turn on the test button
	protected static final boolean TEST_API= false;
	protected boolean CLEAR_BUTTON= true;
	protected boolean SHOW_STATUS_TEXTAREA = false;
	protected boolean SHOW_MAP_PANEL= false;
	
//	protected boolean legendPanelOn= false;
//	protected int max_numberForLegendPanelOff =6;
	
//	protected boolean LEGEND_SWITCH = false;
//	protected final String LEGENDON	= "LEGENDON";
//	protected final String LEGENDOFF	= "LEGENDOFF";
	 
	
    protected final int CHART_SIZE_X = 500;
    protected final int CHART_SIZE_Y = 400;

	// CONSTANT decalration
  
    protected final String EXAMPLE 	= "DEMO";
	protected final String DOCHART 	= " UPDATE_CHART ";
    protected final String CLEAR 	= " CLEAR ";
    protected final String TEST 	= "TEST";

    public static final String DATA 	= "DATA";
    protected final String GRAPH 	= "GRAPH";
    protected final String MAPPING 	= "MAPPING";
    protected final String INPUT 	= "INPUT";
  //  public static final String ALL 	= "SHOW ALL";
    protected String view ="Graph";

    protected JApplet parentApplet;
    
 	protected int inputFileType = 1;  //3D only

  
    protected final String ADD 		= " ADD  ";
    protected final String REMOVE 	= "REMOVE";
    protected final String DEPENDENT 		= "DEPENDENT";
    protected final String INDEPENDENT 		= "INDEPENDENT";
    protected final String VARIABLE 		= "VARIABLE";

//	protected final String DEFAULT_HEADER 		= "C";

	protected final String DATA_MISSING_MESSAGE 		= "DATA MISSING: Enter data first and click on MAPPING to continue.";
	
	protected final String VARIABLE_MISSING_MESSAGE 		= "VARIABLE MISSING: Enter data first and click on MAPPING to continue.";
	protected final String DELIMITERS = ",;\t ";


   	/**
	 * @uml.property  name="hasExample"
	 */
   	protected boolean hasExample = true;		//default is a empty example
 
 	protected String chartTitle  = "SOCR Chart3D";
	protected String xLabel;
	protected String yLabel;
	protected String zLabel;

	protected boolean isDemo = true; // use demo data to init

//	protected DataConvertor convertor = new DataConvertor();
	protected boolean useStaticExample = true; 
	protected Action exampleStaticAction;
	/*protected Action exampleRandomAction;
	
	protected boolean useRandomExample = false;*/

	
	protected String onlineDescription = "";
	
	protected String onlineHelp = "";

	// the toolbar buttonsnew 
	
	protected Action computeAction;
	protected Action testAction;
	protected Action clearAction;
	protected Action userDataAction;
	protected Action fileLoadAction;

	protected boolean printError = false;
	protected static final String outputFontFace = "Helvetica";
	protected static int outputFontSize = 12;

	protected String chartDescription = "";

    protected int tabbedPaneCount = 0;
    public int selectedInd = 0;
   
    public JTable dataTable;
    public JTable headerTable;
  
    public Object [][] dataObject;
    public Object [][] headerDataObject;
 
    protected String dataText = "";
   
    protected int columnNumber 	= 10;
    protected int rowNumber 		= 10;
   
    public String [] columnNames;
    protected TableColumnModel columnModel;
    public javax.swing.table.DefaultTableModel tModel;
    public javax.swing.table.DefaultTableModel hModel;
    
    
    protected JPanel dataPanel;  
    protected JPanel graphPanel;
    protected JPanel mapPanel;
    protected JPanel bPanel;  
    protected JPanel inputPanel;  
  //  protected JPanel mixPanel;
  //  protected JScrollPane mixPanelContainer;
//  protected JPanel dataPanel2;
    //  protected JPanel graphPanel2;
	protected JTextArea summaryPanel;
  
	private JTextPane statusTextArea;
	
	//fields in inputPanel
	protected JTextField xIndexField, yIndexField, valueField;
 
    public JList listAdded;
    public JList listDepRemoved;
    public JList listIndepRemoved;
    public JButton addButton1 = new JButton(ADD);
    public JButton addButton2 = new JButton(ADD);
    public JButton removeButton1  = new JButton(REMOVE);
    public JButton removeButton2 = new JButton(REMOVE);

	protected static final int INDEX_0 = 0;
  	protected static final int INDEX_1 = 1;
 
    protected int independentLength = 0; 	// "x"
    protected int dependentLength = 0; 	// "y"
	protected int independentIndex = -1; 	// "x"
	protected int dependentIndex = -1;		// "y"

	protected DefaultListModel lModelAdded;
	protected DefaultListModel lModelDep;
	protected DefaultListModel lModelIndep;
  
    protected  int depMax; // max number of dependent var
    protected  int indMax; // max number of independent var	
    protected ArrayList<Integer> independentList = null;
	protected int independentListCursor = 0;	
 
    protected ArrayList<Integer> dependentList = null;
	protected int dependentListCursor = 0;	

    protected int[] listIndex;
    protected JToolBar tools1;
    protected JToolBar tools2;

    protected Thread chart = null;
    protected boolean stopNow = false;
	
	protected JToolBar toolBar;
   
    public JTabbedPane tabbedPanelContainer;
    // tabbedPanelContainer is a container for the JTabbedPane's.

    public static Font font = new Font("SansSerif", Font.PLAIN, 12);
	
	//protected ChartPanel chartPanel;
	//protected ChartPanel legendPanel;
	protected URL   url;
 
  //  public JPanel leftChartChoicePanel;
  //  public JPanel toolBarPanel;
 
 	protected JLabel depLabel = new JLabel(DEPENDENT);

    protected JLabel indLabel = new JLabel(INDEPENDENT);        
    protected JLabel varLabel = new JLabel(VARIABLE);
    protected String[][] indepValues;
    protected String[][] depValues;	
	protected String dependentHeaders[];	
	protected String independentHeaders[];
	protected int independentVarLength; 
	protected int dependentVarLength;  
 
    protected int xyLength;	
    protected boolean mapDep=true, mapIndep=true;

	/********************* Start Methods **************************/
    /**This method initializes the Gui, by setting up the basic tabbedPanes.*/
    public void init(){
    	
		depMax =1;
		indMax =1;
		chartTitle = this.getClass().getName();
		chartTitle = chartTitle.substring(chartTitle.lastIndexOf(".")+1);
		String fileName = "demo"+System.getProperty("file.separator")+chartTitle+".html";

		url = Chart3D.class.getResource(fileName);

	   	setName(chartTitle);
	    
		//Get frame of the applet
		//frame = getFrame(this.getContentPane());
	
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		
		tabbedPanelContainer = new JTabbedPane();

		dataObject = new Object[rowNumber][columnNumber];
		columnNames = new String[columnNumber];
		independentList = new ArrayList<Integer>();
		dependentList = new ArrayList<Integer>();
		for (int i = 0; i < columnNumber; i++)
			columnNames[i] = String.valueOf(i+1);

		initTable();
		initGraphPanel();
		if(SHOW_MAP_PANEL)
			initMapPanel();
		initInputPanel();
		
	//	initMixPanel();
	//	mixPanelContainer = new JScrollPane(mixPanel);
	//	mixPanelContainer.setPreferredSize(new Dimension(600,CHART_SIZE_Y+100));

		addTabbedPane(GRAPH, graphPanel);
		addTabbedPane(DATA, dataPanel);
		if(SHOW_MAP_PANEL)
			addTabbedPane(MAPPING, bPanel);
		addTabbedPane(INPUT, inputPanel);
	//	addTabbedPane(ALL, mixPanelContainer);

		tabbedPanelContainer.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					/*if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ALL) {
						mixPanel.removeAll();
						setMixPanel();
						mixPanel.validate();
					}else */
					if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==GRAPH) {
						graphPanel.removeAll();
						//setGraphPanel();
						//graphPanel.validate();
						setChart();

					}else 	if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==DATA) {
						//					
						setTablePane();
					}else 	if(SHOW_MAP_PANEL &&	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==MAPPING) {
						bPanel.removeAll();
						mapPanel.setPreferredSize(new Dimension(CHART_SIZE_X, CHART_SIZE_Y));
						bPanel.add(mapPanel,BorderLayout.CENTER);
						bPanel.validate();
					}	
				}
		});
		if(SHOW_MAP_PANEL)
			bPanel.addComponentListener(new ComponentListener() {
				public void componentResized(ComponentEvent e) {}
				public void componentMoved(ComponentEvent e) {}
				public void componentShown(ComponentEvent e) {
					// resultPanelTextArea.append("sucess");
					// System.out.print("Success");
					// Add code here for updating the Panel to show proper heading
					paintMappingLists(listIndex);
				}
				public void componentHidden(ComponentEvent e) {}
	
	        });

		
		// the right side (including top and bottom panels)
		statusTextArea = new JTextPane();  //right side lower
		statusTextArea.setEditable(false);
		JScrollPane statusContainer = new JScrollPane(statusTextArea);
		statusContainer.setPreferredSize(new Dimension(600, 140));

		if (SHOW_STATUS_TEXTAREA){
			JSplitPane	container = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tabbedPanelContainer), statusContainer);
			container.setContinuousLayout(true);
			container.setDividerLocation(0.6);
			this.getContentPane().add(container, BorderLayout.CENTER);
			}
		else {
			
			this.getContentPane().add(new JScrollPane(tabbedPanelContainer), BorderLayout.CENTER);
		}

		updateStatus(url);
    }
    
    // override in sub class
	public void setMapping(){
		   
	   }
	
    public void setTitle(String title){
    	chartTitle = title;
    }
    public void setXLabel(String label){   
    	xLabel = label;
    	//update JTable in sub
    }
    public void setYLabel(String label){
    	yLabel = label;
    	//update JTable in sub class
    }
    public void setZLabel(String label){
    	zLabel = label;
    	//update JTable in sub class
    }
    
    public void setDataTable (String input){
    	//override in sub class
    }
    
    public void setIsDemo(boolean in){
    	isDemo = in;
    }
    
    public void setView(String tabName){
    	view = tabName.toLowerCase();
    	
    	/*if (view.indexOf("all")!=-1){
    		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(mixPanelContainer));
    	}
    	else */
    	if (view.equalsIgnoreCase(GRAPH))
    		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
    	else if (view.equalsIgnoreCase(DATA))
    		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(dataPanel));
    	else if (view.equalsIgnoreCase(MAPPING))
    		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(mapPanel));
    }
    
    protected void setTablePane(){
     	 
    	dataPanel.removeAll();
    	
    	JScrollPane tablePanel = new JScrollPane(dataTable);
    	tablePanel.setRowHeaderView(headerTable);
		dataPanel.add(tablePanel);
		
		JScrollPane st = new JScrollPane(summaryPanel);
		st.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
		

		dataPanel.add(new JScrollPane(st));
		dataPanel.validate();
    }
    
    public void setApplet(JApplet applet) {
        this.parentApplet = applet;       
      //  System.out.println("set parent Applet");
    }
    
   /* protected void setGraphPanel(){
    	chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
		graphPanel.add(chartPanel);
    }*/
    
/**
 * update the content displayed in the right-buttom status panel
 *
 */
    public void updateStatus() {
		statusTextArea.setText("The Chart Description:"+"\n"+chartDescription);
	}

/**
 * update the status panel's content using the given html page
 * @param sourceURL
 */
	public void updateStatus(java.net.URL sourceURL) { 
		if (url == null ){
			statusTextArea.setText("No description file is found for "+chartTitle+".");
		}
		else 	try{
			statusTextArea.setPage(sourceURL);
		}
		catch(IOException e){
			statusTextArea.setText("Attempted to read a bad url: "+sourceURL);
		}
	}
	
/**
 *  update the status panel's content with the provided message
 * @param msg
 */
    public void updateStatus(String msg) {
		statusTextArea.setText(msg);
	}


    /**This method returns basic copyright, author, and other metadata information*/
    public String getAppletInfo(){
        return "\nUCLA Department of Statistics: SOCR Resource\n"+
        "http://www.stat.ucla.edu\n";
    }
	
/*
    public String format(double x, String precision){
        setDecimalFormat(new DecimalFormat(precision));
        return decimalFormat.format(x);
    }

    //This method sets the decimal format, so that the properties of the decimal
   //  * format can then be changed
    public void setDecimalFormat(DecimalFormat d){
        decimalFormat = d;
    }
	*/

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


    /**This method runs the chart thread*/
    public void run(){
		/*Thread thread = Thread.currentThread();
        while (chart == thread){
            doChart();
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
			}*/
    }

    /**This method stops the chart thread*/
	   public void stop(){
		   //    chart= null;
		   //    stopCount = 0;
		}
	

    /**This method defines the boolean variable that stops the process
     * when the process is in run mode*/
	  public void setStopNow(boolean b){
		  //    stopNow = b;
    }


	public void update(){
	   }

	public void setChoice(String choice){
	}

     public void reset(){
     	hasExample =true;	
		isDemo = false;
		dependentIndex = -1;
     	independentIndex = -1;
		resetMappingList();
	 }

/**
 * setup GUI layout for the show-all panel
 *
 */
	/*protected void initMixPanel(){
		dataPanel2 = new JPanel();
		dataPanel2.setLayout(new BoxLayout(dataPanel2, BoxLayout.Y_AXIS));

		graphPanel2 = new JPanel();
		//		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));

		mixPanel = new JPanel(new BorderLayout()); 
		//		resetChart();

		setMixPanel();
	}*/

	/**
	 * make the show_all panel
	 */
/*	protected void setMixPanel(){
		dataPanel2.removeAll();
		graphPanel2.removeAll();

		graphPanel2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));
		if (chartPanel!=null)
			 graphPanel2.add(chartPanel);
		if (legendPanelOn){
			if (legendPanel !=null)
				legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));
			JScrollPane	 legendPane = new JScrollPane(legendPanel);
		
			legendPane.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));  
			graphPanel2.add(legendPane);
		}
		graphPanel2.validate();

		dataPanel2.add(new JLabel(" "));
		dataPanel2.add(new JLabel("Data"));
		JScrollPane dt = new JScrollPane(dataTable);
		dt.setRowHeaderView(headerTable);
		dt.setPreferredSize(new Dimension(CHART_SIZE_X/3, CHART_SIZE_Y*3/8));

		dataPanel2.add(dt);
		JScrollPane st = new JScrollPane(summaryPanel);
		st.setPreferredSize(new Dimension(CHART_SIZE_X/3,CHART_SIZE_Y/6));
		dataPanel2.add(st);
		st.validate();

		dataPanel2.add(new JLabel(" "));
		dataPanel2.add(new JLabel("Mapping"));
		JScrollPane st2 = new JScrollPane(mapPanel);
		st2.setPreferredSize(new Dimension(CHART_SIZE_X/3, CHART_SIZE_Y/2));
		dataPanel2.add(st2);

		dataPanel2.validate();

		mixPanel.removeAll();
		mixPanel.add(graphPanel2, BorderLayout.WEST);
		mixPanel.add(new JScrollPane(dataPanel2), BorderLayout.CENTER);
		mixPanel.validate();	
	}*/
/**
 * setup GUI layout for the data panel including datatable and summary textArea
 *
 */
	protected void initTable(){

		tModel = new DefaultTableModel(dataObject, columnNames);
		hModel = new DefaultTableModel(0, 1);
		
		dataTable = new JTable(tModel);
		dataTable.addKeyListener(this);
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);
		
		for (int i = 0; i < tModel.getRowCount(); i++)
            hModel.addRow(new Object[] { (i+1)+":" } );
		
		headerTable = new JTable(hModel);
		headerTable.setCellSelectionEnabled(false);
		headerTable.setEnabled(false);
		headerTable.setGridColor(Color.gray);
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

		hookTableAction();

		dataPanel = new JPanel();

		summaryPanel = new JTextArea();
		//		summaryPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/2));
		Color bg = dataPanel.getBackground();
		summaryPanel.setBackground(bg);
		summaryPanel.setEditable(false);
		summaryPanel.setForeground(Color.BLACK);

		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		JScrollPane tablePanel = new JScrollPane(dataTable);
    	tablePanel.setRowHeaderView(headerTable);
    	tablePanel.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
    	new JScrollPaneAdjuster(tablePanel);

    	new JTableRowHeaderResizer(tablePanel).setEnabled(true); 
         
		dataPanel.add(tablePanel);
		JScrollPane st = new JScrollPane(summaryPanel);
		st.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
		dataPanel.add(new JScrollPane(st));

		dataPanel.validate();
	}

	 public void adjustJTable(int row, int column){
    	 int ct = dataTable.getColumnCount();
    	 if (column>ct)
    		 appendTableColumns(column-ct);
    	 if(column < ct){
    		  tModel.setColumnCount(column);
    	 }
    	 
    	 int rt = dataTable.getRowCount();
    	 if (row>rt)
    		 appendTableRows(row-rt);
    	 if (row<rt)
    		 tModel.setRowCount(row);  	
    	 
    	 dataTable.setModel(tModel);
    }
	 
	 public void setJTable(SOCRBinned2DData data){
	    	adjustJTable(data.xBins(), data.yBins());
	    	for (int i=0; i<data.xBins(); i++)
				 for (int j=0; j<data.yBins(); j++){
					// System.out.println("setData"+dataTable.getValueAt(i, j));
					 dataTable.setValueAt(data.zAt(i, j), i, j);
				 }
	    }
	/**
	 * clear data panel
	 *
	 */
	public void resetTable(){

	//	System.out.println("resetTable get called");
		for (int i = 0; i < columnNumber; i++)
			columnNames[i] = String.valueOf(i+1);

		tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
		hModel = new DefaultTableModel(0, 1);
		
		for (int i = 0; i < tModel.getRowCount(); i++)
            hModel.addRow(new Object[] { (i+1)+":" } );
		
		dataTable = new JTable(tModel);
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);
		dataTable.doLayout();
		dataTable.setCellSelectionEnabled(true);
		dataTable.setColumnSelectionAllowed(true);
		dataTable.setRowSelectionAllowed(true);
         
		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));
		
		listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;
		
		hookTableAction();

		headerTable = new JTable(hModel);
		headerTable.setCellSelectionEnabled(false);
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
        
        
		//		summaryPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/2));
		summaryPanel.removeAll();

		dataPanel.removeAll();
		JScrollPane tablePanel = new JScrollPane(dataTable);
    	tablePanel.setRowHeaderView(headerTable);
    	tablePanel.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
    	new JScrollPaneAdjuster(tablePanel);
    	new JTableRowHeaderResizer(tablePanel).setEnabled(true); 
		dataPanel.add(tablePanel);
		
		JScrollPane st = new JScrollPane(summaryPanel);
		st.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/3));
		dataPanel.add(new JScrollPane(st));
		dataPanel.validate();
		 
	}

	protected void resetTableRows(int n) {
		//	System.out.println("Chart resetTableRows:" +n);
	        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
			tModel.setRowCount(n);
	        dataTable.setModel(tModel);
	        
	       
	        hModel = (javax.swing.table.DefaultTableModel) headerTable.getModel();
	        int b= hModel.getRowCount();
			hModel.setRowCount(n);
			headerTable.setModel(hModel);
			for (int i = b; i < hModel.getRowCount(); i++)
	            hModel.setValueAt((i+1)+":",i,0);    
	    }

	// reset mapping also
		public void resetTableColumns(int n) {
		//	System.out.println("resetTableColumns get Called " +n);
			removeButtonDependentAll();
			removeButtonIndependentAll();
			
			tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();	      
			tModel.setColumnCount(n);
	        dataTable.setModel(tModel);  
	       
			listIndex = new int[dataTable.getColumnCount()];
	        for(int j=0;j<listIndex.length;j++)
	            listIndex[j]=1;
	      
	    }
		
	   public void appendTableRows(int n) {
	        int ct = dataTable.getColumnCount();
	        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
	        
	        for(int j=0;j<n;j++) 
	        	tModel.addRow(new java.util.Vector(ct));  
	        dataTable.setModel(tModel);
	        columnModel = dataTable.getColumnModel();
	        dataTable.setTableHeader(new EditableHeader(columnModel));
	      /*  for(int i=0; i<columnModel.getColumnCount(); i++)
		    	   System.out.println("appendrow_header"+i+"is "+columnModel.getColumn(i).getHeaderValue());
	      */
	        hModel = (javax.swing.table.DefaultTableModel) headerTable.getModel();
	        int rowCount= hModel.getRowCount();
	        for(int j=0;j<n;j++) {
                hModel.addRow(new Object[] {(rowCount+j+1)+":"});  
	        }
	        headerTable.setModel(hModel);
	    }

	   
	   public void appendTableColumns(int n) {
	 // System.out.println("appending column="+n);

		   columnModel = dataTable.getColumnModel();
	 /*      String[] savedHeaders= new String[columnModel.getColumnCount()];
	       for(int i=0; i<columnModel.getColumnCount(); i++){ 
	    	//   System.out.println("header"+i+"is "+columnModel.getColumn(i).getHeaderValue());
	    	   savedHeaders[i] = (String)columnModel.getColumn(i).getHeaderValue();
	       }*/
	       
	       int ct = dataTable.getColumnCount();
	       tModel =  (DefaultTableModel) dataTable.getModel();
	       for(int j=0;j<n;j++) {
	    	   tModel.addColumn(ct+j+1,new java.util.Vector(ct)); 
	    	 //  System.out.println("adding header:"+(DEFAULT_HEADER+(ct+j+1)));
	       }
	         
	      
	       dataTable.setModel(tModel);
	/*       columnModel = dataTable.getColumnModel();	
	       
	       for (int i=0; i< savedHeaders.length; i++)
	    	   columnModel.getColumn(i).setHeaderValue(savedHeaders[i]);
	   */    
	   /* 	   
	       for(int i=0; i<columnModel.getColumnCount(); i++){ 
	    	   System.out.println("2header"+i+"is "+columnModel.getColumn(i).getHeaderValue());
	       }*/
	       dataTable.setTableHeader(new EditableHeader(columnModel));
	    
	       int[] listIndex2 = new int[dataTable.getColumnCount()];
	        for(int j=0;j<listIndex.length;j++)
	            listIndex2[j]= listIndex[j];
	        for(int j=listIndex.length;j<listIndex2.length;j++)
	        	listIndex2[j]=1;
	        
	        listIndex = new int[dataTable.getColumnCount()];
	        for(int j=0;j<listIndex2.length;j++)
	        	listIndex[j]= listIndex2[j];
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
						//resetTableColumns(dataTable.getColumnCount()+1);
						appendTableColumns(1);
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
		
		
	/*	
		String actionName3 = "deleteSelectedData";
		final Action delAction = dataTable.getActionMap().get(actionName3);

		Action myAction3 = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if ((dataTable.getSelectedRow() >= 0) && (dataTable.getSelectedColumn() >= 0)){
					int[] rows=dataTable.getSelectedRows();
					int[] cols=dataTable.getSelectedColumns(); 
					for (int i=rows[0]; i<cols[rows.length-1]; i++)
					 for (int j = cols[0]; j<cols[cols.length-1]; j++){
						 dataTable.setValueAt("", i, j);
					}
				}else delAction.actionPerformed(e);
					
			}
		};
		dataTable.getActionMap().put("delete", myAction3);*/

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

	public void resetMappingList() {
		//System.out.println("Chart: resetMappingList get Called");
		dependentIndex = -1;
		independentIndex = -1;
	    independentList.clear();
		dependentList.clear();
		removeButtonDependentAll();
		removeButtonIndependentAll();
	}

	/*implemented by each Super*Chart  */
    public void doChart(){
	}

    /* implemented by each chart*/
    public void doTest(){
    	showMessageDialog("the api test is not implemented for this chart!");
	}

	protected void initGraphPanel(){
	}

	protected void initInputPanel(){
		
		JToolBar toolbar = new JToolBar();
		JToolBar toolbar2 = new JToolBar();
		toolbar.setFloatable(false);
		toolbar2.setFloatable(false);
		
	//	toolbar.setPreferredSize(new Dimension(80,25));
		toolbar2.setPreferredSize(new Dimension(50,40));
		inputPanel = new JPanel();
		
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		xIndexField= new JTextField(10);
		yIndexField= new JTextField(10);
		xIndexField.setText("1");
		yIndexField.setText("1");
		
		valueField= new JTextField(10);
		valueField.setPreferredSize(new Dimension(50, 40));
		
		toolbar.add(new JLabel("x index:"));
		toolbar.add(xIndexField);
		toolbar.add(new JLabel("y index:"));
		toolbar.add(yIndexField);
		
		toolbar2.add(new JLabel("value:"));
		toolbar2.add(valueField);	
		JPanel empty1= new JPanel();
		empty1.setPreferredSize(new Dimension(50,300));
		JPanel empty2= new JPanel();
		empty2.setPreferredSize(new Dimension(50,300));
		inputPanel.add(empty1);
		inputPanel.add(toolbar);		
		inputPanel.add(toolbar2);
		inputPanel.add(empty2);
	
		setValueField();
        xIndexField.addActionListener(this);
        yIndexField.addActionListener(this);
        valueField.addActionListener(this);
        
	}
	
	protected void setJTableAt(){
		int xIndex=0, yIndex=0;
		
		xIndex= Integer.parseInt(xIndexField.getText())-1;
		yIndex= Integer.parseInt(yIndexField.getText())-1;
		String value = valueField.getText();
		
		if(dataTable!=null){
			dataTable.setValueAt(Float.parseFloat(value), xIndex, yIndex);
		}
	}
	protected void setValueField(){
		int xIndex=0, yIndex=0;
			
		xIndex= Integer.parseInt(xIndexField.getText())-1;
		yIndex= Integer.parseInt(yIndexField.getText())-1;
		
		//System.out.println("reading value for "+xIndex+" "+yIndex);
		if(dataTable!=null){
			if( dataTable.getValueAt(xIndex, yIndex)!=null){
			String v = dataTable.getValueAt(xIndex, yIndex).toString();
			if(v!=null && v.length()!=0)
				valueField.setText(v);
			}
		}
	}
    /**
     * 
     *
     */
    protected void setChart(){
    }

	protected void resetChart(){
	
		graphPanel.removeAll();
	
	}


    /**
     * 
     * */
    public void actionPerformed(ActionEvent event) {
    /*	if (event.getActionCommand().equals(LEGENDON)) {       
            	turnLegendPanelOn();          
    	}
    	else if (event.getActionCommand().equals(LEGENDOFF)) {       
    		turnLegendPanelOff();              
    	}
    	else */
    	if(event.getSource() == xIndexField || event.getSource() == yIndexField) {
    	
    		setValueField();
    	}
    	if(event.getSource() == valueField) {
    		setJTableAt();
    	}
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
    	else 	
    		;
     
    }
	
    protected void addButtonDependent() {
            int ct1=-1;

            int sIdx = listAdded.getSelectedIndex();
            int idx2 = lModelDep.getSize();

            if(sIdx >-1 && idx2 <depMax) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
                        dependentIndex=i;
						dependentList.add(dependentListCursor, new Integer(i));
                        break;
                    }
                }
                listIndex[dependentIndex]=2;
				dependentLength++;
                paintMappingLists(listIndex);
            }else 	updateStatus("depMax="+depMax+ " can't add more dependent");
			//  System.out.println("addButtonDependent listAdded:"+" dependentIndex="+dependentIndex);
	}

	protected void removeButtonDependent() {
	
		if (dependentLength>0) 
			dependentLength--;
	
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
                paintMappingLists(listIndex);
            }
	}


	protected void removeButtonDependentAll() {
		dependentLength = 0;
		dependentIndex = -1;
		
		for (int i = 0; i < depMax; i++) {
			try {
				removeButtonDependent();
			} catch (Exception e) {
			}			
		}

		paintMappingLists(listIndex);
    }
	
 	protected void addButtonIndependent() {

            int ct1=-1;
            int sIdx = listAdded.getSelectedIndex(); 
            int idx3 = lModelIndep.getSize();

			//System.out.println("idx3="+idx3+" sIdx="+sIdx+" listIndex.length="+listIndex.length);

            if(sIdx >-1 && idx3 <indMax) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
						independentIndex=i;
						independentList.add(independentListCursor, new Integer(i));
                        break;
                    }
                }
			   	listIndex[independentIndex]=3;
                //System.out.println("addButtonIndependent listIndex = " + listIndex);
				independentLength++;
                paintMappingLists(listIndex);
            }
			else 	updateStatus("indMax="+indMax+ " can't add more independent");
			//			System.out.println("Finish addButtonIndependent, independentIndex = " + independentIndex+"\n");
        }

	protected void removeButtonIndependent() {
		if (independentLength>0) 
			independentLength--;
		/*{System.out.println("Chart Applet: removeButtonIndependent: independentLength = " + independentLength);
		Exception e = new Exception();
		e.printStackTrace();
		}*/
		int ct1=-1;
		int idx1 = 0;
		int sIdx = listIndepRemoved.getSelectedIndex();
		//JOptionPane.showMessageDialog(this, "sIdx = " + sIdx + " idx1 = " + idx1 + " listIndex.length = "+listIndex.length);
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
			paintMappingLists(listIndex);
		}
    }

	protected void removeButtonIndependentAll() {
		independentLength = 0;
		independentIndex = -1;
		/*int ct1=-1;
		int idx1 = 0;
		int sIdx = listIndepRemoved.getSelectedIndex();*/
		for (int i = 0; i < indMax; i++) {
			try {
				removeButtonIndependent();
			} catch (Exception e) {
			}			
		}
		
		paintMappingLists(listIndex);
    }

    /**This method returns an online description 
     * */
    public String getOnlineDescription(){
        return new String("http://socr.stat.ucla.edu/");
    }
    
   public String getOnlineHelp(){
        return new String("http://en.wikipedia.org/wiki/Chart");
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

    public void initMapPanel() {
        listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;
        bPanel = new JPanel(new BorderLayout());
        // topPanel = new JPanel(new FlowLayout());
        //bottomPanel = new JPanel(new FlowLayout());
        mapPanel = new JPanel(new GridLayout(2,3,50, 50));
		//  bPanel.add(new JPanel(),BorderLayout.EAST);
		//   bPanel.add(new JPanel(),BorderLayout.WEST);
		//   bPanel.add(new JPanel(),BorderLayout.SOUTH);
        bPanel.add(mapPanel,BorderLayout.CENTER);
		//   bPanel.add(new JPanel(),BorderLayout.NORTH);

        addButton1.addActionListener(this);
        addButton2.addActionListener(this);
        removeButton1.addActionListener(this);
        removeButton2.addActionListener(this);

        lModelAdded = new DefaultListModel();
        lModelDep = new DefaultListModel();
        lModelIndep = new DefaultListModel();

        //JLabel depLabel = new JLabel(DEPENDENT);
        //JLabel indLabel = new JLabel(INDEPENDENT);        
		//JLabel varLabel = new JLabel(VARIABLE);

        int cellWidth = 10;


        listAdded = new JList(lModelAdded);
        listAdded.setSelectedIndex(0);
        listDepRemoved = new JList(lModelDep);
        listIndepRemoved = new JList(lModelIndep);

        paintMappingLists(listIndex);
        listAdded.setFixedCellWidth(cellWidth);
        listDepRemoved.setFixedCellWidth(cellWidth);
        listIndepRemoved.setFixedCellWidth(cellWidth);

        tools1 = new JToolBar(JToolBar.VERTICAL);
        tools2 = new JToolBar(JToolBar.VERTICAL);

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
       
       /*  topPanel.add(listAdded);
       topPanel.add(addButton);
       topPanel.add(listDepRemoved);
       bottomPanel.add(listIndepRemoved);
       bottomPanel.add(addButton2);
       bottomPanel.add(list4); */

        JRadioButton legendPanelOnSwitch;
        JRadioButton legendPanelOffSwitch;
		//
	/*	JPanel choicesPanel = new JPanel();
		choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
		legendPanelOnSwitch = new JRadioButton("On");
		legendPanelOnSwitch.addActionListener(this);
		legendPanelOnSwitch.setActionCommand(LEGENDON);
		legendPanelOnSwitch.setSelected(false);
		legendPanelOn = false;	

		legendPanelOffSwitch = new JRadioButton("Off");
		legendPanelOffSwitch.addActionListener(this);
		legendPanelOffSwitch.setActionCommand(LEGENDOFF);
		legendPanelOffSwitch.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(legendPanelOnSwitch);
		group.add(legendPanelOffSwitch);
		choicesPanel.add(new JLabel("Turn the legend panel:"));
		choicesPanel.add(legendPanelOnSwitch);
		choicesPanel.add(legendPanelOffSwitch);
		choicesPanel.setPreferredSize(new Dimension(200,100));
		*/

        mapPanel.add(new JScrollPane(listAdded));
        JPanel emptyPanel = new JPanel();
        JPanel emptyPanel2 = new JPanel();
        JPanel emptyPanel3 = new JPanel();
        
        if (mapDep) {
        	 mapPanel.add(tools1);      
        	 mapPanel.add(new JScrollPane(listDepRemoved));
        	/* if (LEGEND_SWITCH)
        		 mapPanel.add(choicesPanel);
        	 else*/
        		 mapPanel.add(emptyPanel);
       
              if (mapIndep) {
              	 mapPanel.add(tools2);       
              	 mapPanel.add(new JScrollPane(listIndepRemoved));
              }else {
          		mapPanel.add(emptyPanel2);
          		mapPanel.add(emptyPanel3);
          	}
        }
        else {    
        	mapPanel.add(emptyPanel);
        	mapPanel.add(emptyPanel2);
        	     		
        	//if (LEGEND_SWITCH)
        		mapPanel.add(emptyPanel3);
        	//else 
        	//	mapPanel.add(choicesPanel);
        	if (mapIndep) { 
        		 mapPanel.add(tools2);  
              	 mapPanel.add(new JScrollPane(listIndepRemoved));
    			}
        }
      
    }
    
   
    public void paintMappingLists(int[] lstInd) {
    	if (!SHOW_MAP_PANEL)
    		return;
        lModelAdded.clear();
        lModelDep.clear();
        lModelIndep.clear();
        
        for(int i=0;i<lstInd.length;i++) {

            switch(lstInd[i]) {
                case 0:
                    break;
                case 1:
                    lModelAdded.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listAdded.setSelectedIndex(0);

                    break;
                case 2:
                    lModelDep.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listDepRemoved.setSelectedIndex(0);
                    break;
                case 3:
                    lModelIndep.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listIndepRemoved.setSelectedIndex(0);

                    break;
                default:
                    break;

            }
			//String temp = columnModel.getColumn(i).getHeaderValue().toString().trim();

        }
        listAdded.setSelectedIndex(0);


    }
/*
    protected void appendTableRows(int n) {
        int ct = dataTable.getColumnCount();
        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
        for(int j=0;j<n;j++)
            tModel.addRow(new java.util.Vector(ct));
        dataTable.setModel(tModel);

    }*/



    private int getDistinctElements(Matrix Cl) {
        int rowCt = Cl.rows;
        int count=1;
        double clData =0;
        double[] distinctElements = new double[rowCt];

        distinctElements[0] = Cl.element[0][0];

        for(int i=1;i<rowCt;i++) {
            // clData[i] = table.getValueAt(i, column);
            clData =  Cl.element[i][0];

			int flag =0;
            for(int j=0; j < count; j++) {
                if(clData == distinctElements[j]) {
                    flag = 1;
                    break;

                }

            }
            if(flag ==0) {
                distinctElements[count] = Cl.element[i][0];
				count++;
            }
        }
        return count;
    }

	// reset dataTable and chart using demo data, should be overwite in each sub chart
    public void resetExample() {

	}
    
    public void updateExample(ChartExampleData example) {
		//  reset();
		
        hasExample = true;
        JTable tempDataTable =  example.getExample();
		
		/* if(tempDataTable.getRowCount()>dataTable.getRowCount())
            appendTableRows(tempDataTable.getRowCount()-dataTable.getRowCount());
			else	*/
        resetTableRows(tempDataTable.getRowCount());

        for(int i=0;i<tempDataTable.getColumnCount();i++) {
            columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
            }

		dataTable.setTableHeader(new EditableHeader(columnModel));

        for(int i=0;i<tempDataTable.getRowCount();i++)
            for(int j=0;j<tempDataTable.getColumnCount();j++) {
                dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
		 }
        dataPanel.removeAll();
        JScrollPane tablePanel = new JScrollPane(dataTable);
    	tablePanel.setRowHeaderView(headerTable);
		dataPanel.add(tablePanel);
		dataPanel.add(new JScrollPane(summaryPanel));
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);

		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try { 
			dataTable.setDragEnabled(true);  
		} catch (Exception e) {
		}

        dataPanel.validate();
		//updateStatus("Example updated, please redo the mapping and update the chart");
    }

    public static Chart3D getInstance(String classname) throws Exception {

        return (Chart3D) Class.forName(classname).newInstance();
    }

    public Container getDisplayPane() {
	
		return this.getContentPane();
    }

	protected void showError(String message) {
		if (printError) {
			updateStatus(message);
		}
	}
	/**
	 * add the tabs to the toolbar
	 * @param toolBar
	 */
	protected void createActionComponents(JToolBar toolBar){
			JButton button = null;
	       	
			toolBar.setFloatable(false);

		/**************** Demo Tab****************/
			if (useStaticExample) {
				exampleStaticAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
				// Create First Example
					reset();
					resetTable();
					resetMappingList();
					resetExample();
				//	if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ALL) 
				//		setMixPanel();

					updateStatus(url);
					validate();
				}
					
			};
			button = toolBar.add(exampleStaticAction);
			button.setText(EXAMPLE);
			button.setToolTipText(chartDescription);
			}
			
		/**************** DO-CHART Tab ****************/
			computeAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					doChart();
				}
			};
		
				button = toolBar.add(computeAction);
				button.setText(DOCHART);
	       	button.setToolTipText("Press this Button to Generate the Chart");
			
	 		/**************** CLEAR Tab****************/
	      	clearAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {

				/* somehow reset has to be called more than once to set to the correct header. I'll leave it like this before I figure out why. annie che 20051123 -_- */
					reset();	// Need to work out what this means
					//	reset();

					resetTable();
//					 update the mapping panel for this table change
					resetTableColumns(dataTable.getColumnCount());  
					
					resetMappingList();
					resetChart();
					ChartExampleData exampleNull = new ChartExampleData(0, 0);
					/* A Null Example (with no data) is used here
					to reset the table so that when "CLEAR" button is pressed, the cells of dataTable is NOT null. 
					annieche 20060110. */
					updateExample(exampleNull);
					//if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ALL) 
				//		setMixPanel();
					
					updateStatus("The Chart has been reset!");
					//updateExample(exampleNull);
				}
					
			};
	
	       	if (CLEAR_BUTTON){
	       		button = toolBar.add(clearAction);
	       		button.setText(CLEAR);
	       		button.setToolTipText("Clears All Windows");
	       	}
	       	
	       	/**************** TEST Tab ****************/
			testAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					doTest();
				}
			};
			if (TEST_API){
				button = toolBar.add(testAction);
				button.setText(TEST);
				button.setToolTipText("Press this Button to test the API");	
			}
	     
			       
     }


   public String getLocalHelp() {
   
	   return "Introduction: The SOCRChart applet is a collection of data plotting tool, and it allows you to:\n"+
		"\t1. Visualize your own data graphically.\n"+
		"\t2. Generate some statistical plots such as:\n\t    StatisticalBarChart(BoxPlot), StatisticalLineChart(ScatterPlot), NormalDistributionPlot, BoxAndWhiskerChart.\n\n"+
	      "How to use the SOCRChart Applets:\n"+
		"\t1. Select a chart from the SOCRChart list from the left control panel, a demo of the selected chart type will be\n\t    shown in the right display panel.\n"+
		"\t2. Click on the \"DATA\" tab in the right display panel to view the data used in the demo in a table form. \n\t   The data and the headings are all editable for you to enter your own data. \n"+
		"\t3. For each chart type, the \"MAPPING\" is set for the demo data.  You can make change as needed using the \n\t   \"Add\"/\"Remove\" button.\n"+
		"\t4. After making data/mapping changes, click on the \"UPDATE_CHART\" button to get the plot updated.\n"+
		"\t5. Click the \"DEMO\" button will reset everything to the demo state.\n"+
		"\t6. Click the \"CLEAR\" button will clear all for you to start entering your own data.\n\n"+
	     "Notes: \n"+
	    "\t1.You can select table cells and use the \"COPY\"/\"PASTE\" button to copy/paste data in the data table.\n"+
	    "\t2.The \"SNAPSHOT\" button can be used to save a snapshot of the graph to your own computer.\n"+
		"\t3.To add a extra row to the data table, hit \"enter\" key in the last cell. Hit \"tab\" key in the last cell will add a extra \n\t   column.\n"+
		"\t4. To report bugs or make recommendations please visit: http://www.socr.ucla.edu/\n" +
	    "\n SOCRChart is based on JFreeChart and uses it's rendering engine to render the chart image. See www.jfree.org/jfreechart/ \n \t   for more information."
		;
}

   public String getLocalAbout() {
       
	   String helpName = "demo"+System.getProperty("file.separator")+chartTitle+".html";
	   URL helpUrl = Chart3D.class.getResource(helpName);
	   StringBuffer fBuf =  new StringBuffer();
  
	   //a. the chart control 

	   fBuf.append("General Chart Control:\n");
	   fBuf.append("\t1. Right click to show the popup menu.\n");

       fBuf.append("\t2.To zoom in: Choose \"ZoomIn\" from the popup menu or hold on the left mouse button      \n\t    and focus on the area you want to zoom in and grag toward lower_right corner. \n");
	   fBuf.append("\t3.To zoom out: Choose \"ZoomOut\" from the the popup menu or hold on the left mouse       \n\t    button and grag toward upper_left corner.\n" );
	   fBuf.append("\t4.To change chart properties such as background color, Font, outline stroke:\n\t   Choose \"Properties\" from the popup menu and make the change to Title/Plot.\n\n ");

	   //b. chart description
	   fBuf.append("General Chart Description: See http://en.wikipedia.org/wiki/Chart for more infomantion.\n");
	   if (chartTitle.indexOf("Bar")!=-1 ||chartTitle.indexOf("Water")!=-1)
		   fBuf.append("\t A bar chart is a chart with rectangular bars of lengths usually proportional \n to the magnitudes or frequencies of what they represent.\n");
	   else if (chartTitle.indexOf("Pie")!=-1 ||chartTitle.indexOf("Ring")!=-1)
		   fBuf.append("\t A pie chart is a circular chart divided into segments, illustrating relative magnitudes\n or frequencies. In a pie chart, the arc length of each segment and consequently its central angle \n and area, is proportional to the quantity it represents. Together, the segments create a full disk.\n");
	   fBuf.append("\n\n");

	   //c. the html content
	   fBuf.append("About -- ");
	   // fBuf.append(readNoHTMLfile(helpUrl));
	   if (helpUrl!=null)
		   fBuf.append(readHTMLFile(helpUrl));
	   else fBuf.append("No description file is found for "+chartTitle+".");
	   fBuf.append("\n");


	   return fBuf.toString();
   }
   public String getWikiAbout(){
	   return new String("http://wiki.stat.ucla.edu/socr/index.php/About_pages_for_SOCR_3D_Charts");
   }

   public String getWikiHelp(){
	   return new String("http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_3D_Charts");
   }
	// do nothing to the tags
	String readNoHTMLfile(URL helpUrl){
		String line;
		StringBuffer sb = new StringBuffer();
		try{
		   InputStream in = helpUrl.openStream();
		   BufferedReader dis = new BufferedReader(new InputStreamReader(in));
		 
		 
		   while((line= dis.readLine())!=null) {
			   sb.append(line +"\n");
		   }
		}catch(IOException e){
		   sb.append("No info about "+chartTitle+".\n");
		}
		return sb.toString();

	}

	// remove the tags
	String readHTMLFile(URL helpUrl){
		String line;
		StringBuffer sb = new StringBuffer();

		try{
		   InputStream in = helpUrl.openStream();
		   BufferedReader dis = new BufferedReader(new InputStreamReader(in));
		 	 
		   while((line= dis.readLine())!=null) {
			   sb.append(line +"\n");
		   }
		}catch(IOException e){
			sb.append("No info about "+chartTitle+".\n");
		}

		return sb.toString().replaceAll("\\<.*?\\>","");

		/* HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback();
		   Reader rd = new StringReader(sb.toString());
		   new ParserDelegator().parse(rd, callback,true);*/

		//"\\<[^<]*?\\>"
	}

	// this is for the refreshing problem
	public void adjustmentValueChanged(AdjustmentEvent event) {
		//System.out.println("Chart:adjustmentEvent:"+event.paramString());
		this.getContentPane().repaint();
	}

	protected void getMapping(){
		independentVarLength = lModelIndep.getSize();
		dependentVarLength = lModelDep.getSize();
	}
	

/**
 * make the content of the popup dialog highlightable
 * @param m
 */
	public void showMessageDialog(String m){
			JOptionPane popup = new JOptionPane(); 
			JTextArea msg = new JTextArea(m);
			Color bg = popup.getBackground();	
			msg.setBackground(bg);
			popup.showMessageDialog(this, msg);
	}

	 public void popInfo(String noConnectionInfo, URL isConnectedUrl, String target){
	     	try{	             		         
	  		  boolean isIt = java.net.InetAddress.getLocalHost().isLoopbackAddress();
	   		  if (isIt) {
	   					//System.out.println("not conected to internet");
	   					SOCROptionPane.showMessageDialog(this, noConnectionInfo);
	   					
	   			}else{
	   				//System.out.println("conected to internet");
	   				parentApplet.getAppletContext().showDocument(isConnectedUrl,target);          					
	   			}          	 
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
		     	e.printStackTrace();
			}
	    }
	 
	 /******calculation needed by PowerTransformed Charts******/
	  public double[] normalize(String[] raw_x, double[] transformed_x, int row_count){
	    	double min,max,min_t, max_t;
	    	double a, b;
	    	double[] normalized_x = new double[row_count];
	    	
	    	min = getMin(raw_x, row_count);
	    	max = getMax(raw_x, row_count);
	    	min_t = getMin(transformed_x, row_count);
	    	max_t = getMax(transformed_x, row_count);
	    	
	    	
	    	a = (max-min)/(max_t-min_t);
	    	b = min-min_t*a;
	    	//System.out.println("min="+min+" max="+max+ " min_t="+min_t+" max_t="+max_t);	    	
	    	//System.out.println("a="+a+" b="+b);
	    	
	    	for (int i=0; i<row_count; i++){
	    		normalized_x[i]=a*transformed_x[i]+b;
	    		//System.out.println("transformed_x["+i+"]="+transformed_x[i]);
	    		//System.out.println("normalized_x["+i+"]="+normalized_x[i]);
	    	}
	    	//System.out.println("---");
	    	return normalized_x;
	    }
	  
	  public double[] calculate_power(String[] raw_x, int row_count, double power){
			double[] transformed_x = new double[row_count];
			for (int i=0; i<row_count; i++){
				if (power==0) 
				{
					if(raw_x[i]!=null && raw_x[i].length()!=0)
						transformed_x[i] = Math.log(Math.abs(Double.parseDouble(raw_x[i])));
		       
				}
				else {
					//transformed_x[i] = (Math.pow(Math.abs(Double.parseDouble(raw_x[i])), power)-1)/power;
					if(raw_x[i]!=null && raw_x[i].length()!=0)
						transformed_x[i] = Math.pow(Math.abs(Double.parseDouble(raw_x[i])), power);
				}
			}
		   return transformed_x; 		
		} 
	 
	    public double getMin(String[] raw, int count){
	    	double min = Double.parseDouble(raw[0]);
	    	int i =0;
	    	
	    	for (; i<count; i++)
	    		if (Double.parseDouble(raw[i])<min)
	    			min = Double.parseDouble(raw[i]);
	    	
	    	return min;
	    
	    }
	    public double getMin(double[] raw, int count){
	    	double min = raw[0];
	    	int i =0;
	    	
	    	for (; i<count; i++)
	    		if (raw[i]<min)
	    			min = raw[i];
	    	
	    	return min;
	    
	    }
	    
	    public double getMax(String[] raw, int count){
	    	double max = Double.parseDouble(raw[0]);
	    	int i =0;
	    	
	    	for (; i<count; i++)
	    		if (Double.parseDouble(raw[i])>max)
	    			max = Double.parseDouble(raw[i]);
	    	
	    	return max;
	    
	    }
	    
	    public double getMax(double[] raw, int count){
	    	double max = raw[0];
	    	int i =0;
	    	
	    	for (; i<count; i++)
	    		if (raw[i]>max)
	    			max = raw[i];
	    	
	    	return max;
	    
	    }


	public void keyPressed(KeyEvent e) {
		//System.out.println("Chart: keyPressed="+e.getKeyCode()+"dataTable.getSelectedRow()="+dataTable.getSelectedRow());
		if (e.getKeyCode()==127||e.getKeyCode()==8){			
		if ((dataTable.getSelectedRow() >= 0) && (dataTable.getSelectedColumn() >= 0)){
			int[] rows=dataTable.getSelectedRows();
			int[] cols=dataTable.getSelectedColumns(); 
			for (int i=rows[0]; i<=rows[rows.length-1]; i++)
			 for (int j = cols[0]; j<=cols[cols.length-1]; j++){
				 dataTable.setValueAt(null, i, j);
			}
		}
		 dataPanel.removeAll();
		 JScrollPane tablePanel = new JScrollPane(dataTable);
		 tablePanel.setRowHeaderView(headerTable);
		 dataPanel.add(tablePanel);
		 dataPanel.add(new JScrollPane(summaryPanel));
		 dataTable.setGridColor(Color.gray);
		 dataTable.setShowGrid(true);
		 dataPanel.validate();
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
	
	}
	 
	public int getInputFileType(){
		return inputFileType;
	}
	
}

