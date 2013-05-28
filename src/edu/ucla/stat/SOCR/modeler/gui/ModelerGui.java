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

package edu.ucla.stat.SOCR.modeler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import edu.ucla.stat.SOCR.core.SOCRModeler;
import edu.ucla.stat.SOCR.core.sampler;
import edu.ucla.stat.SOCR.modeler.Modeler;
import edu.ucla.stat.SOCR.util.PluginLoader;
import edu.ucla.stat.SOCR.util.RowHeaderRenderer;
import edu.ucla.stat.SOCR.util.SOCRJTable;
/**	This class defines a basic modeler object. Typically, the user generates some data by drawing with the mouse or reading URL/files. Then a model for the data is developed to fit the data. Usually the model has analytical (in terms of standard math functions), digital (discrete form of the analytical model) and graphical (visualization) components/representations.

 This class must be sub-classed by any SOCR Modeler Object to add the appropriate functionality.

 This class needs to be implemented fully. It's not complete as of Aug. 2003.
 */

public class ModelerGui extends JApplet implements FocusListener, KeyListener,
		ActionListener, MouseListener, MouseMotionListener, Observer{

	boolean debug = false;
	private ButtonGroup buttonGroup = new ButtonGroup();
	//Variables
	private int time = 0, toolbarCount = 0, graphCount = 0,
			tabbedPaneCount = 0, tableCount = 0;
	private URL codeBase;
	//Panels
	private JPanel modelerApplet = new JPanel(); //The center panel
	private JPanel tables = new JPanel(); //The bottom panel
	public Clipboard clipboard;
	//Buttons
	private JButton resetButton = new JButton("Reset");
	private JButton topAboutButton = new JButton("About");
	private JButton fitButton = new JButton("Fit Model");
	public float xhist[];
	public float yhist[];
	public float rawDat[];
	public int histBinNos = 10;
	public int scale = 0;
	public double[] modelXData, modelYData;
	public JSci.awt.DefaultGraph2DModel model;
	public boolean scaleUp = false;
	static int CURVE_FIT = 0;
	// modeltype 0 = Curve Fit
	// modeltype 1 = Paramteric Model
	// modeltype 3 = Non-parametric model
	public int modelType = 1;
	public Modeler modelObject;
	//Record table
	private JTextArea recordTable = new JTextArea(5, 15);
	//Dialog
	private JFrame appletFrame;
	//private edu.ucla.stat.SOCR.util.AboutDialog aboutDialog;
	//Format
	private DecimalFormat decimalFormat = new DecimalFormat();
	//tabbed panels
	protected JPanel dataPanel, resultPanel, graphPanel, datGenPanel,infoPanel;
	public JTabbedPane tabbedPanelContainer; // the tabs.
	public JTextArea resultPanelTextArea, infoPanelTextArea;
	public SOCRJTable dataTable;
	public JTable headerTable;
	public JTextField DataPts = new JTextField("10", 3);
	public JLabel lDataPts = new JLabel("Number of Data Points");
	private JButton fillTable = new JButton("Fill Table");
	public DefaultTableModel tModel;
	public Object[][] dataObject;
	protected String dataText = "";
	public int columnNumber = 2, rowNumber = 100;
	public String[] columnNames;

	public final static double GUI_UPPER_LIMIT = ModelerConstant.GRAPH_UPPER_LIMIT;
	public final static double GUI_LOWER_LIMIT = ModelerConstant.GRAPH_LOWER_LIMIT;
//	public double modelUpperlimit = ModelerConstant.GRAPH_UPPER_LIMIT;
//	public double modelLowerlimit = ModelerConstant.GRAPH_LOWER_LIMIT;
	public double defaultXMax = 10;
	public double defaultXMin = -10;
	public double defaultYMin = 0;
	public double defaultYMax = 10;

	private double xScaleMin;
	private double xScaleMax;
	private double yScaleMax;

	private boolean xIsPos = false;
	//private boolean xIsPosNeg = true;

	public ModelerHistogramGraph graph = null;//new ModelerHistogram(GUI_LOWER_LIMIT, GUI_UPPER_LIMIT, 1);

	private Dimension tabbedPaneDim = new Dimension(ModelerDimension.TABBED_PANE_WIDTH, ModelerDimension.TABBED_PANE_HEIGHT);
	public sampler samplePanel;
	public PluginLoader pLoader;
	private TableColumnModel columnModel;
	/**This method initializes the experiment*/
	public final static String VALUE_COLUMN_NAME = "Value"; //
	public final static String FREQUENCY_COLUMN_NAME = "Frequency"; // frequency.
	private boolean reinitMixtureModel = true;
	private boolean rescaleClicked = false;

	//----------------------------------------------
	//for changing graph panel scale
	public final static double DEFAULT_MAX_X = 5;
	public final static double DEFAULT_MAX_Y = 10;
	public final static double DEFAULT_MIN_X = -5;
	
	private boolean dataFromTable = false;
	private boolean isRawData = false;
	private SOCRModeler guiLink = null;
	//----------------------------------------------
	
//	public JCheckBox reinitCheckBox = new JCheckBox("Scale Up",false);


	public ModelerGui() {
		graph = new ModelerHistogramGraph(ModelerConstant.GRAPH_LOWER_LIMIT, ModelerConstant.GRAPH_UPPER_LIMIT, 1);
		graph.setGuiLink(this);
		////////System.out.println("ModelerGui constructor");
		//codeBase = base;
		//	pLoader = loader;
	}

	
	public void setDebug(boolean s){
		debug = s;
		if(graph!=null)
			graph.setDebug(debug);
	}
	
	public void setCodeBase(URL codebase) {
		////////System.out.println("ModelerGui setCodeBase  URL = " + codebase);
		codeBase = codebase;
	}
	public void setGuiLink(SOCRModeler l) {
		////////System.out.println("ModelerGui setCodeBase  URL = " + codebase);
		guiLink = l;
	}
	public void init() {
	
		graph.setType(0);
		setName("ModelerGui");
		setBackground(ModelerColor.GRAPH_BACKGROUND);//lightGray);
		//Layout
		this.getContentPane().setLayout(new BorderLayout());

		resultPanel = new JPanel();
		resultPanelTextArea = new JTextArea(ModelerDimension.RESULT_ROW, ModelerDimension.RESULT_COL); //(rows, cols)
		resultPanel.add(new JScrollPane(resultPanelTextArea));
		//resultPanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_RESULT_WIDTH, ModelerDimension.PANEL_RESULT_HEIGHT));

		infoPanel = new JPanel();
		infoPanelTextArea = new JTextArea(ModelerDimension.INFO_ROW, ModelerDimension.INFO_COL);
		infoPanel.add(new JScrollPane(infoPanelTextArea));
		//infoPanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_INFO_WIDTH, ModelerDimension.PANEL_INFO_HEIGHT));

		graphPanel = new JPanel(new BorderLayout());
		graph.addMouseListener(this);
		graphPanel.add(graph, BorderLayout.CENTER); // CENTER

		dataObject = new Object[rowNumber][columnNumber];
		columnNames = new String[columnNumber];

		//for (int i = 0; i < columnNumber; i++)
		//	columnNames[i] = new String("testColumn_" + (i + 1));
		// change to column name that makes more sense. annie che 20060503.
		columnNames[0] = new String(VALUE_COLUMN_NAME); //testColumn_" + (i + 1));
		columnNames[1] = new String(FREQUENCY_COLUMN_NAME);
		tModel = new DefaultTableModel(dataObject, columnNames);
		dataTable = new SOCRJTable(tModel);
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);
		
		DefaultTableModel hModel = new DefaultTableModel(0, 1);
		for (int i = 0; i < tModel.getRowCount(); i++)
            hModel.addRow(new Object[] { (i+1)+":" } );
		
		headerTable = new JTable(hModel);
		headerTable.setCellSelectionEnabled(false);
		headerTable.setGridColor(Color.gray);
		headerTable.setShowGrid(true);
		headerTable.setEnabled(false);
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
        corner.setForeground(Color.gray);
		//dataTable.setSize(ModelerDimension.DATA_TABLE_WIDTH, ModelerDimension.DATA_TABLE_HEIGHT);
		dataPanel = new JPanel();
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		JScrollPane tablePanel = new JScrollPane(dataTable);
		tablePanel.setRowHeaderView(headerTable);
		dataPanel.add(new JScrollPane(tablePanel, v, h));
	
	     tabbedPanelContainer = new JTabbedPane();
	     //tabbedPanelContainer.setBackground(Color.MAGENTA);
		/////tabbedPanelContainer.setMaximumSize(tabbedPaneDim);
		//tabbedPanelContainer.setPreferredSize(tabbedPaneDim);
		datGenPanel = new JPanel();
		datGenPanel.add(lDataPts);
		datGenPanel.add(DataPts);
		fillTable.addActionListener(this);
		datGenPanel.add(fillTable);

		samplePanel = new sampler(dataTable, codeBase);
		samplePanel.addGuiLink(this);
		infoPanelTextArea.setText("Data Modeler \n Specific information unavailable");
		//setBasedatGenParams(datGenPanel);
		//setModelerdatGenParams(datGenPanel);

		JPanel sampleGetPanel = samplePanel.getPanel();
		JPanel sampleBackgroundPanel = new JPanel(new BorderLayout());

		sampleBackgroundPanel.add(sampleGetPanel, BorderLayout.CENTER);
		//graphPanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_GRAPH_WIDTH, ModelerDimension.PANEL_GRAPH_HEIGHT));
		//graph.setPreferredSize(new Dimension(ModelerDimension.PANEL_GRAPH_WIDTH, ModelerDimension.PANEL_GRAPH_HEIGHT));
		dataPanel.setBackground(ModelerColor.PANEL_DATA_BACKGROUND);
		graphPanel.setBackground(ModelerColor.PANEL_GRAPH_BACKGROUND);
		resultPanel.setBackground(ModelerColor.PANEL_RESULT_BACKGROUND);
		infoPanel.setBackground(ModelerColor.PANEL_ABOUT_BACKGROUND);
		sampleGetPanel.setBackground(ModelerColor.PANEL_SAMPLE_BACKGROUND);

		graphPanel.setForeground(ModelerColor.PANEL_DATA_FOREGROUND);
		resultPanel.setForeground(ModelerColor.PANEL_RESULT_FOREGROUND);
		infoPanel.setForeground(ModelerColor.PANEL_ABOUT_FOREGROUND);
		sampleGetPanel.setForeground(ModelerColor.PANEL_SAMPLE_BACKGROUND);
		//////////System.out.println("ModelerGui Sample Get Size = " +sampleGetPanel.getPreferredSize());

		// add to tabbedPanelContainer.
		addTabbedPane("Data", dataPanel);
		addTabbedPane("Graphs", graphPanel);
		addTabbedPane("Results", resultPanel);
		addTabbedPane("About the Model", infoPanel);
		addTabbedPane("Data Generation", sampleBackgroundPanel);

		//addTabbedPane("DAta Generation", sampleGetPanel);
		//Construct toolbar
		this.getContentPane().setBackground(ModelerColor.WHITE);
		this.getContentPane().add(tabbedPanelContainer, BorderLayout.NORTH); // was CENTER.
		//Record table
		recordTable.setEditable(false);
		dataTable.addFocusListener(this);
		dataPanel.addFocusListener(this);
		columnModel = dataTable.getColumnModel();
		//int ModelType = 0;
	}

	public static Modeler getInstance(String classname) throws Exception {
		  ////////System.out.println("ModelerGui getInstance classname = " + classname);
	       Class cls = Class.forName(classname);

	       if (cls == null) return null;
	       return (Modeler)  cls.newInstance();
	   }


	public void fitC(boolean rescaleClicked, boolean status, boolean reinitMixtureModel) {
		//System.out.println("ModelerGui fitC(boolean boolean boolean) get callled");
		this.rescaleClicked = rescaleClicked;
		scaleUp = status;
		this.reinitMixtureModel = reinitMixtureModel;
		
		if(debug)
			System.out.println("ModelerGui: fitC(boolean) rescaleClicked = " + rescaleClicked + " status = " + scaleUp +", reinitMixtureModel = " + reinitMixtureModel);
		fitC();
	}


	public void fitC(boolean status) {
		fitC();
	}

	public void fitC() { // looks like fit curve??
		//System.out.println("ModelerGui fitC() rawDat = " + rawDat);
		//System.out.println("ModelerGui fitC() rawDat.length = " + rawDat.length+" dataTableRowCount="+dataTable.getRowCount());

		//System.out.println("ModelerGui fitC() modelObject = " + modelObject);
		//System.out.println("ModelerGui fitC modelType = " + modelType);
		double minOfModel = 0, maxOfModel = 0; // of the model doamin.
		//System.out.println("ModelerGui fitC() xScaleMin="+xScaleMin);
	//	System.out.println("ModelerGui fitC() called");
		
		if (rawDat != null && rawDat.length > 0  && modelObject != null) {
			setScale(graph, modelObject);
		//	minOfModel = modelObject.getGraphLowerLimit();
		//	maxOfModel = modelObject.getGraphUpperLimit();
			minOfModel = graph.xMin;//jenny ???
			maxOfModel = graph.xMax;
				
			if(debug)
			 System.out.println("ModelerGui fitC() processing raw minOfModel = " + minOfModel+"maxOfModel = " + maxOfModel);

			//System.out.println("ModelerGui fitC() if scaleUp = " + scaleUp);
			//System.out.println("ModelerGui fitC() if reinit.isSelected = " + reinitMixtureModel);
			//System.out.println("ModelerGui fitC() if calls fitCurve");
			//System.out.println("ModelerGui fitC() if modelType = " + modelType);

			modelObject.fitCurve(rawDat, (float)minOfModel, (float)maxOfModel, resultPanelTextArea, rescaleClicked, scaleUp, reinitMixtureModel);
			// modelerObject: e.g. BetaFit_Modeler, etc.

			modelXData = modelObject.returnModelX();
			modelYData = modelObject.returnModelY();
			modelType = modelObject.getModelType();
			int arrayLegnth = modelXData.length;

			if (modelObject.getModelType() == Modeler.WAVELET_TYPE) {
				////////System.out.println(" in Wavelet fit");
				//modelObject.getCDF(6);
			}

			if (scaleUp && (modelType == Modeler.CONTINUOUS_DISTRIBUTION_TYPE
				|| modelType == Modeler.DISCRETE_DISTRIBUTION_TYPE)) {
				float area = 0;
				if (dataTable.getColumnCount() == 2) {
					float xWidth = xhist[1] - xhist[0];
					for (int k = 1; k < yhist.length; k++)
						area = area + yhist[k];
					area = area * xWidth;
				} else {
					float xWidth = xhist[1] - xhist[0];
					for (int k = 1; k < yhist.length; k++)
						area = area + yhist[k];
					area = area * xWidth;
				}
				for (int j = 0; j < modelYData.length; j++)
					modelYData[j] = modelYData[j] * area;
			}

			graph.setModelCount(modelObject.getModelCount());
			graph.setModel(modelXData.length, modelXData, modelYData);

			if ( /** (modelObject.getCDF(0) == Double.NaN) || **/ (modelType == 5 || modelType == 6 )) {
				resultPanelTextArea.append("\n Chi-Square not implemented");
			} else {

			}

			// Should we have these X-range Graph-Window resets here (01/20/08)?
			/****
			double xRangeMin, xRangeMax, xRange;
			double yRangeMax;
			
			if (yhist.length>0) {
			 	yRangeMax = yhist[0];
			 	for (int k = 1; k < yhist.length; k++) {
						if (yRangeMax < yhist[k]) yRangeMax = yhist[k];
				}
				if (yRangeMax>0) graph.setPlotYMax(1.1*yRangeMax);
			}			

			if (rawDat.length>0) {
			 	xRangeMin = rawDat[0];
			 	xRangeMax = rawDat[0];
			 	
			 	for (int k = 1; k < rawDat.length; k++) {
						if (xRangeMin > rawDat[k]) xRangeMin = rawDat[k];
						if (xRangeMax < rawDat[k]) xRangeMax = rawDat[k];
				}
				xRange = xRangeMax - xRangeMin;
			
				if (xRange >0) {
					graph.setYExtrema((float)0.0, (float)(xRangeMax+0.1*xRange));
					graph.setXExtrema((float)(xRangeMin+0.1*xRange), 
							(float)(xRangeMax+0.1*xRange));
					//graph.setPlotXMin(xRangeMin+0.1*xRange);
					//graph.setPlotXMax(xRangeMax+0.1*xRange);
				}
			}			
			*******/
			
			graph.validate();
			graph.setModelType(modelObject.getModelType());
			graph.repaint();
			this.graphPanel.validate();
			graphPanel.repaint();
		}
		//System.out.println("ModelerGui fitC exit");

	}

	public void syncMouseData() {
		if(debug)
			System.out.println("ModelerGui syncMouseData " );
		////System.out.println("ModelerGui modelObject.getModelType() = " + modelObject.getModelType() );

		//////System.out.println("model series length=" + model.seriesLength());
		if ((graph.getdataCursor() > 0) && (modelObject.getModelType() != Modeler.FOURIER_TYPE)
				&& (modelObject.getModelType() != Modeler.WAVELET_TYPE)) {

			////System.out.println("ModelerGui syncMouseData if" );

			xhist = graph.getXData();
			yhist = graph.getYData();

			// graph.clear();
			//graph.setxy(xhist,yhist,xhist.length,0);
			histToRaw();

			dataTable.clearTable();
			//System.out.println("rowData.length="+rawDat.length+" dataTable rowCount="+dataTable.getRowCount());
			if (dataTable.getColumnCount() == 1) {
			//	System.out.println("ModelerGui syncMouseData if dataTable.getColumnCount() == 1 rawDat length="+ rawDat.length);
				
				if (dataTable.getRowCount() < rawDat.length) {
				
					int origRowCount = dataTable.getRowCount();
					int addedRowCount = rawDat.length - origRowCount;
				
					dataTable.appendTableRows(rawDat.length
							- dataTable.getRowCount());
					
					DefaultTableModel hModel = (DefaultTableModel)headerTable.getModel();
					for (int i=0; i<addedRowCount; i++)
						hModel.addRow(new Object[] { (origRowCount+i+1)+":" });
					headerTable.setModel(hModel);
					//System.out.println("hModel length="+headerTable.getRowCount());
					////////System.out.println("ModelerGui syncMouseData if dataTable.getRowCount() < rawDat.length" );
				}
				for (int i = 0; i < rawDat.length; i++) {
					dataTable.setValueAt(Float.toString(rawDat[i]), i, 0);
					//////////System.out.println("ModelerGui syncMouseData for dataTable.setValueAt["+i+"] " +rawDat[i]);
				}
				
				//System.out.println("done ModelerGui syncMouseData if dataTable.getColumnCount() == 1" );
			} else {
				////////System.out.println("ModelerGui syncMouseData else dataTable.getColumnCount() != 1");
				if (dataTable.getRowCount() < xhist.length) {
					//System.out.println("rowData.length= "+xhist.length+" appending");
					int origRowCount = dataTable.getRowCount();
					int addedRowCount = xhist.length - origRowCount;
				
					dataTable.appendTableRows(xhist.length
							- dataTable.getRowCount());
					DefaultTableModel hModel = (DefaultTableModel)headerTable.getModel();
					for (int i=0; i<addedRowCount; i++)
						hModel.addRow(new Object[] { (origRowCount+i+1)+":" });
					headerTable.setModel(hModel);
					//System.out.println("hModel length="+headerTable.getRowCount());
					////////System.out.println("ModelerGui syncMouseData else dataTable.getRowCount() < xhist.length");
				}


				for (int i = 0; i < xhist.length; i++) {
				//System.out.println("ModelerGui syncMouseData for xhist.length = " + xhist.length + ", " + xhist[i] + ", " + yhist[i]);
					dataTable.setValueAt(Float.toString(xhist[i]), i, 0);
					dataTable.setValueAt(Float.toString(yhist[i]), i, 1);
				}

			}

		}
		//System.out.println("after rowData.length="+rawDat.length+" dataTable rowCount="+dataTable.getRowCount());
		//////System.out.println("ModelerGui syncMouseData passed if graph.getdataCursor() = " + graph.getdataCursor());
		//System.out.println("graph.getdataCursor()"+graph.getdataCursor());
	//	if (graph.getdataCursor() > 1 && !isRawData) {
		if (graph.getdataCursor() > 1 ) {
			//System.out.println("ModelerGui syncMouseData graph.getdataCursor() > 1 so fitC ");
			fitC();
		} //Jenny this could cause a loop for RawData

	}

	public void syncData() {
		//System.out.println("ModelerGui syncData dataTable rowCount="+dataTable.getRowCount() );
		
		rawDat = null;
		xhist = null;
		yhist = null;
		if ( modelObject == null )
		{
			//System.out.println("ModelerGui syncData if modelObject == null" );
		
			if (dataTable.getColumnCount() == 1) {
				//System.out.println("ModelerGui syncData if modelObject == null columnCount==1" );
				rawDat = dataTable.getTableVal(0);
				//System.out.println("ModelerGui syncData if dataTable.getColumnCount() == 1 rawDat = " + rawDat );

				if (rawDat != null&& rawDat.length !=0) {
					//System.out.println("ModelerGui syncData if rawDat != null" );

					//dataPanel.requestFocus();
					//dataPanel.setVisible(true);
					//rawToHist();
					graph.clear();
					graph.setxy(rawDat);
					xhist = graph.getXData();
					yhist = graph.getYData();
					fitC();
				}
			} else {
			//	System.out.println("ModelerGui syncData if dataTable.getColumnCount() != 1 ");

				// dataPanel.setVisible(true);
				xhist = dataTable.getTableVal(0);
				yhist = dataTable.getTableVal(1);
				
				if (xhist != null && yhist != null) {
					//System.out.println("ModelerGui syncData if xhist != null && yhist != null");

					if (xhist.length == yhist.length) {
						histToRaw();
						graph.clear();
						graph.setxy(rawDat);
						fitC();
					}
				}
			}
		
		}
		else if ( (modelObject.getModelType() != 5) && (modelObject.getModelType() != 6))
		{
			//System.out.println("ModelerGui syncData else modelObject not null type not 5 not 6");

			if (dataTable.getColumnCount() == 1) {
				rawDat = dataTable.getTableVal(0);
			//	System.out.println("ModelerGui syncData else dataTable.getColumnCount() == 1 rawDat length = " + rawDat.length);

			//	dataFromTable = true;
				
				if (rawDat != null && rawDat.length!=0) {
					//dataPanel.requestFocus();
					//dataPanel.setVisible(true);
					//rawToHist();
					graph.clear();
					graph.setxy(rawDat);
					
					float minx = rawDat[0];
					float maxx = rawDat[0];
			
					for (int i = 1; i < rawDat.length; i++) {
						if (rawDat[i] > maxx)
							maxx = rawDat[i];
						if (rawDat[i] < minx)
							minx = rawDat[i];
					}
				//	if(xScaleMin>minx)
						xScaleMin = minx;
				//	if(xScaleMax<maxx)
						xScaleMax = maxx;
				//	System.out.println("syncData xScaleMax = "+xScaleMax);
					xhist = graph.getXData();
					yhist = graph.getYData();
					fitC();
				}
			} else {
				// dataPanel.setVisible(true);
				xhist = dataTable.getTableVal(0);
				yhist = dataTable.getTableVal(1);
				
				
				//calculate graph panel scale based on input in graph panel
				dataFromTable = true;
				
				
				if (xhist != null && yhist != null) {
					if (xhist.length == yhist.length) {
						histToRaw();
						graph.clear();
						graph.setxy(rawDat);
						fitC();
					}
				}
			}
		} else {
	//	System.out.println("ModelerGui syncData else modelObject not null type == 5 or 6");
			if(debug)
				System.out.println("ModelerGui syncData else modelObject not null type == 5 or 6 rawDat length="+rawDat.length);
			rawDat = dataTable.getTableVal(0);
			if (rawDat != null&& rawDat.length !=0)
			{
				graph.clear();
				graph.setxy(rawDat);
				fitC();
			}
		}
	}

	/**This method returns basic copyright information*/
	public String getAppletInfo() {
		return "\n UCLA Department of Statistics: SOCR Resource:: Modeler\n\n";
	}

	/**This method sets up the About dialog box*/
	public void start() {
		////////System.out.println("ModelerGui start " );
		appletFrame = getFrame(ModelerGui.this.getContentPane());
	}

	/**This method handles the events for the About button and the Reset button.*/
	public void actionPerformed(ActionEvent event) {
		////////System.out.println("ModelerGui actionPerformed event = " +event.getSource());

		if (event.getSource() == resetButton)
			reset();
		if (event.getSource() == topAboutButton) {

			try {
				//aboutDialog.show();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, getOnlineDescription());
			}
		}

		/*if (event.getSource() == fillTable) {
			//System.out.println("fill Table");
			if (dataTable.getColumnCount() > 1) {
				OKDialog OKD = new OKDialog(null, true,
						"Only raw data can be filled. Check Raw Data box and try again");
				OKD.setVisible(true);
			} else {
				clearData();
				int genCt = Integer.parseInt(DataPts.getText());
				if (genCt < 1) {
					OKDialog OKD = new OKDialog(null, true,
							"Number of points must be greater than 0");
					OKD.setVisible(true);
				} else {

					if (dataTable.getRowCount() < genCt){
						//System.out.println("call appendTableRows");
						
						dataTable.appendTableRows(genCt
								- dataTable.getRowCount());
					}
					//generateSamples(genCt);
				}
				System.out.println("SamplerButton changed");
			}
		}*/
	}

	//Mouse event
	public void mouseClicked(MouseEvent event) {
		////System.out.println("ModelerGui mouseClicked event " + event.getSource());
		if (event.getSource() == graph)
			////System.out.println("ModelerGui mouse Event == graph");
		syncMouseData();
	}

	public void mouseEntered(MouseEvent event) {
		//////////System.out.println("ModelerGui mouseEntered event " + event.getSource());
	}

	public void mouseExited(MouseEvent event) {
		//////////System.out.println("ModelerGui mouseExited event " + event.getSource());
		/* if(event.getSource() == graph)
		 syncMouseData(); */
	}

	public void mousePressed(MouseEvent event) {
		//////////System.out.println("ModelerGui mousePressed event " + event.getSource());
	}

	public void mouseReleased(MouseEvent event) {
		//////////System.out.println("ModelerGui mouseReleased event " + event.getSource());

	}

	public void mouseMoved(MouseEvent event) {
		//////////System.out.println("ModelerGui mouseMoved event " + event);

	}

	public void mouseDragged(MouseEvent event) {
		//////////System.out.println("ModelerGui mouseDragged event " + event);

	}

	/**This is the method for resetting the Model and should be overridden.*/
	public void reset() {
		////////System.out.println("ModelerGui reset time = " + time);
		this.reinitMixtureModel = true;
		time = 0;
	}

	/**This method formats a number to a specified pattern*//*
	public String format(double x) {
		return decimalFormat.format(x);
	}

	*//**This method gets the frame containing a specified component*/
	private static JFrame getFrame(Container component) {
		JFrame frame = null;
		while ((component = component.getParent()) != null) {
			if (component instanceof JFrame)
				frame = (JFrame) component;
		}
		return frame;
	}

	/**This method returns an online description of this Model.
	 It should be overwritten by each specific Modeler.*/
	public String getOnlineDescription() {
		return new String("http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_Modeler");
	}

	public void addTabbedPane(String name, JComponent c) {
		////////System.out.println("ModelerGui addTabbedPane name " + name);
		tabbedPanelContainer.addTab(name, c);
		tabbedPaneCount++;
	}

	public void clearDataTable(){
		dataObject = new Object[100][2];
		columnNames = new String[columnNumber];

		columnNames[0] = new String(VALUE_COLUMN_NAME); //testColumn_" + (i + 1));
		columnNames[1] = new String(FREQUENCY_COLUMN_NAME);
		tModel = new DefaultTableModel(dataObject, columnNames);
		
		
		dataTable.setModel(tModel);
		if(isRawData){
			TableColumn clm2 = dataTable.getColumn(ModelerGui.FREQUENCY_COLUMN_NAME); //testColumn_2
			dataTable.removeColumn(clm2);
		}
	//	System.out.println("clearData tModel rowCount="+tModel.getRowCount());
		DefaultTableModel hModel = new DefaultTableModel(0, 1);
		for (int i = 0; i < tModel.getRowCount(); i++)
            hModel.addRow(new Object[] { (i+1)+":" } );
		headerTable.setModel(hModel);
		
		dataTable.clearTable();
	}
	
	public void clearData() {
		////////System.out.println("ModelerGui clearData " );

		graph.clear();
		
		 //reset to 100 row
		
		clearDataTable();
		resultPanelTextArea.setText("");
		if (modelType == 0) {
			graph.setYExtrema(-5, 5);
			graph.setXExtrema(-5, 5);
		} else {
			graph.setYExtrema(1, 5);
			graph.setXExtrema(-5, 5);
		}

		//reset graph panel scale to default size
		graph.setxMax(DEFAULT_MAX_X);
		graph.setyMax(DEFAULT_MAX_Y);
		if(!xIsPos)
			graph.setxMin(DEFAULT_MIN_X);
		
		
/*		if (modelType == 0) {
			graph.setYExtrema(-10, 10);
			graph.setXExtrema(-10, 10);
		} else {
			graph.setYExtrema(1, 10);
			graph.setXExtrema(-10, 10);
		}
*/
	}

	public void toggleParams(boolean isTrue) {
		////////System.out.println("ModelerGui toggleParams isTrue = " + isTrue);

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == e.VK_TAB) {

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}

	public void focusGained(FocusEvent e) {
	//	System.out.println("Focus dataPanel");
		if (e.getSource() == dataTable){
			DefaultTableModel hModel = (DefaultTableModel)headerTable.getModel();
			int hRowCount= hModel.getRowCount();
			int dRowCount= dataTable.getRowCount();
			if(hRowCount<dRowCount){
			for (int i=0; i<dRowCount-hRowCount; i++)
				hModel.addRow(new Object[] { (hRowCount+i+1)+":" });
			    headerTable.setModel(hModel);			    
			}
			dataPanel.validate();
		}
		
	}
		

	public void focusLost(FocusEvent e) {
//System.out.println("ModelerGUi focusLost get called");
		if (e.getSource() == dataTable)
			syncData();
	}

	public void rawToHist() {
		
		xhist = new float[histBinNos];
		yhist = new float[histBinNos];
		float minx = rawDat[0];
		float maxx = rawDat[0];
		for (int i = 1; i < rawDat.length; i++) {
			if (rawDat[i] > maxx)
				maxx = rawDat[i];
			if (rawDat[i] < minx)
				minx = rawDat[i];
		}
		for (int i = 0; i < xhist.length; i++)
			xhist[i] = minx + i * (maxx - minx) / histBinNos;
		yhist = histBins(rawDat, minx, maxx);

	}

	public float[] histBins(float[] xdata, float minx, float maxx) {

		// float[] xhist = new float[10];
		// float[] yhist = new float[10];
		for (int i = 0; i < histBinNos; i++)
			yhist[i] = 0;

		float bar = (maxx - minx) / histBinNos;
		for (int i = 0; i < xdata.length; i++) {
			for (int j = 0; j < histBinNos; j++) {
				if (xdata[i] >= minx + j * bar
						&& xdata[i] < minx + (j + 1) * bar)
					yhist[j]++;

			}

		}
		return yhist;
	}

	public void histToRaw() {

		int datalength = 0;
		
		for (int i = 0; i < yhist.length; i++)
			datalength = datalength + (int) yhist[i];

		rawDat = new float[datalength];
		int count = 0;
		// resultPanelTextArea.setText("datalength is" + Integer.toString(datalength));
		for (int i = 0; i < xhist.length; i++) {
			for (int j = 0; j < (int) yhist[i]; j++) {
				
				rawDat[count] = xhist[i];
			//	System.out.println("histToRaw rawDat["+count+"]="+rawDat[count]);
				count++;
			}
		}
	}


	public void setBins(int bins) {
		histBinNos = bins;
		//System.out.println("ModelerGui setBins histBinNos = " + histBinNos);
		graph.setBins(bins);
		syncData();

	}

/*	public void setXScale(int XSize, boolean positiveOnly) {
		//positiveOnly = false;
		////System.out.println("ModelerGui setXScale XSize = " + XSize);
		graph.setxMax((double) XSize);
		if (positiveOnly) {
			xScaleMin = 0;
		} else {
			xScaleMin = (double) -XSize;
		}

		xScaleMax = (double) XSize;
		graph.setxMin((double) -XSize);
		////System.out.println("ModelerGui setXScale xScaleMin = " + xScaleMin);

		syncData();
	}*/
	public void setXScale(double XSize, boolean positiveOnly) {
		xIsPos = positiveOnly;
		//positiveOnly = false;
		////System.out.println("ModelerGui setXScale XSize = " + XSize);
		xScaleMax =  XSize;
		if (positiveOnly) {
			xScaleMin = 0;
		} else {
			xScaleMin = (double) -XSize;
		}
		graph.setxMin(xScaleMin);		
		graph.setxMax(xScaleMax);
		
		syncData();
	}
	
	public void setXScaleMax(double XSize) {
		//positiveOnly = false;
		//System.out.println("ModelerGui setXScale XSize = " + XSize);
		graph.setxMax( XSize);	
		xScaleMax =  XSize;		
		syncData();
	}
	
	public void setXScaleMin(double XSize) {
		//positiveOnly = false;
		////System.out.println("ModelerGui setXScale XSize = " + XSize);
		if(!xIsPos){
			graph.setxMin( XSize);
			xScaleMin = XSize;
		}
		syncData();
	}
	public void setYScaleMax(double YSize) {
		//////System.out.println("ModelerGui setYScale YSize = " + YSize);
		yScaleMax = YSize;
		if(graph.getYMax()<YSize)
		 graph.setyMax(YSize);
		syncData();
	}
	
	public int getYScaleMax(){
		return (int)yScaleMax;
	}
	public int getXScaleMax(){
		return (int)xScaleMax;
	}
	public void setYScale(double YSize) {
		//////System.out.println("ModelerGui setYScale YSize = " + YSize);
		yScaleMax = YSize;
		if(graph.getYMax()!=YSize)
			graph.setyMax( YSize);
		syncData();
	}
	public void panLeft() {
		graph.panLeft();
		// syncData();
	}

	public void panRight() {
		graph.panRight();
		// syncData();
	}

	public int zoomIn(int bins, boolean positiveOnly) {
		//positiveOnly = false;
		////System.out.println("ModelerGui zoomIn bins = " + bins);
		int ret = graph.zoomIn();
		////System.out.println("ModelerGui zoomIn ret = " + ret);
		if (positiveOnly) {
			xScaleMin = 0;
		} else {
			xScaleMin /= 2;
		}
		xScaleMax /= 2;
		graph.setxMin(xScaleMin);
		graph.setxMax(xScaleMax);

		if (ret == 1) {
			syncData();
			this.setBins(bins);
		}
		return ret;
	}

	public int zoomOut(int bins, boolean positiveOnly) {
	//	positiveOnly = false;
		////System.out.println("ModelerGui zoomOut bins = " + bins);
		int ret = graph.zoomOut();
		////System.out.println("ModelerGui zoomOut ret = " + ret);
		if (positiveOnly) {
			xScaleMin = 0;
		} else {
			xScaleMin *= 2;
		}
		xScaleMax *= 2;
		graph.setxMin(xScaleMin);
		graph.setxMax(xScaleMax);

		if (ret == 1) {
			syncData();
			this.setBins(bins);
		}
		return ret;
	}

	public void zoomInY(double maxY, boolean positiveOnly) {
		////System.out.println("ModelerGui zoomInY maxY = " + maxY);
//		int ret = graph.zoomIn();
		//////System.out.println("ModelerGui zoomIn ret = " + ret);
		yScaleMax = maxY;
		graph.setyMax(yScaleMax);

//		if (ret == 1) {
			syncData();
//			this.setBins(bins);
//		}
//		return ret;
	}

/*	public void usePosX() {
		//////System.out.println("ModelerGui usePosX xScaleMax = " + xScaleMax);
		if (!xIsPos) {
			xScaleMax *= 2;
			xIsPos = true;
			//xIsPosNeg = false;
		}

		graph.setxMin(0);
		graph.setxMax(xScaleMax);
		syncData();

	}
	public void usePosNegX() {
		//////System.out.println("ModelerGui useRealX");
		if (xIsPos) {
			xScaleMax /= 2;
			xIsPos = false;
		}

		graph.setxMin(xScaleMin);
		graph.setxMax(xScaleMax);
		syncData();

	}*/
	
	// these should also depending on distibtion. e.g. normal takes all real line but Poisson only +.
   public void setxMax(double input) {
	   // this.modelUpperlimit = input;
	   xScaleMax = input;
	   graph.setxMax(xScaleMax);
	    ////////System.out.println("ModelerGui setUpperLimit = " + this.modelUpperlimit);
    }
    public void setxMin(double input) {
    	
    //	System.out.println("ModelerGui setxMin ="+input);
    //	Exception e = new Exception();
    //	e.printStackTrace();
    	xScaleMin = input;
    	graph.setxMin(xScaleMin);
	  //  this.modelLowerlimit = input;
    	    ////////System.out.println("ModelerGui setLowerLimit = " + this.modelLowerlimit);
    }
	
	

    private void setScale(ModelerHistogramGraph graph, Modeler currentModel) {
		//graph.setLeft(modelLowerlimit); // set limits. see ModelHistogram methods.
	//	graph.setRight(modelUpperlimit);
	//	graph.setGraphLeft(ModelerConstant.GRAPH_LOWER_LIMIT); // set limits. see ModelHistogram methods.
	//	graph.setGraphRight(ModelerConstant.GRAPH_UPPER_LIMIT);
    	//xScaleMin = modelObject.getGraphLowerLimit();
    	//xScaleMax = modelObject.getGraphUpperLimit();
		graph.setxMin(xScaleMin);
		graph.setxMax(xScaleMax);
		if (currentModel.isContinuous()) { 		// for continuous.
			//graph.setBarWidth((modelUpperlimit - modelLowerlimit) / 10);
			graph.setBarWidth((xScaleMax - xScaleMin) / 10);
		}
		else { // for discrete or mixture...
			graph.setBarWidth(1);
		}
		
		//implement auto graph adjustment

		double adj_max_x = 0, adj_max_y = 0, adj_min_x = 0;
		double max_x = 0, max_y = 0, min_x = 0;
		
		//for data entered from table
		if(dataFromTable){
			max_x = findMaxInput(xhist);
			if(isRawData)
				max_y = graph.getMaxRawY();
			else
				max_y = findMaxInput(yhist);
			min_x = findMinInput(xhist);
		}
		//for data entered from graph
		else{
			max_x = graph.getMaxInputX();
			max_y = graph.getMaxInputY();
			min_x = graph.getMinInputX();
		}

		//add 20% margin on each side  this causes loop Jenny 6/25/10
	/*	adj_max_x = max_x*1.2;
		adj_max_y = max_y*1.2;
		adj_min_x = min_x*1.2;
		
		//only adjust value if input data exceed default scale
		//if(adj_max_x > DEFAULT_MAX_X)
		if(adj_max_x > xScaleMax ) { //Jenny
			
			setXScaleMax(adj_max_x);
			guiLink.updateXScaleSlider(adj_max_x);
		}
		//if(adj_max_y > DEFAULT_MAX_Y)
		if(adj_max_y > yScaleMax){
			setYScaleMax(adj_max_y);
			guiLink.updateYScaleSlider(adj_max_y);
		}
		//if(adj_min_x < DEFAULT_MIN_X)
		if(adj_min_x < DEFAULT_MIN_X){
			
			setXScaleMin(adj_min_x);
			guiLink.updateXScaleSlider(Math.abs(adj_min_x));
		}
	*/
    }
    
    public void updateScale(double xMin, double xMax, double yMin, double yMax, int bins){
    	xScaleMin=xMin;
    	xScaleMax=xMax;
    	yScaleMax=yMax;
    	graph.setxMin(xScaleMin);
		graph.setxMax(xScaleMax);
		graph.setyMax(yScaleMax);
    	guiLink.updateYScaleSlider(yScaleMax);
    	guiLink.updateXScaleSlider(xScaleMax);
    	guiLink.updateBinsScaleSlider(bins);
    }

	public boolean useInitButton() {
		return modelObject.useInitButton();
	}
	
	//used to find maximum or minimum value among input data
	//(used for scaling the graph panel)
	public double findMaxInput(float [] inputArray){
		float temp = 0;
		for(int i = 0; i < inputArray.length; i++){
			if(inputArray[i] > temp)
				temp = inputArray[i];
		}
		return (double)temp;
	}

	public double findMinInput(float [] inputArray){
		float temp = 0;
		for(int i = 0; i < inputArray.length; i++){
			if(inputArray[i] < temp)
				temp = inputArray[i];
		}
		return (double)temp;
	}
	
	public void setDataFromFile(float[] xData, float[] yData){
	
	//	System.out.println("setdatafromfile");
		
		
		dataFromTable = true;
		
		if(isRawData){
			xhist = new float[yData.length];
			yhist = new float[yData.length];

			System.arraycopy(yData, 0, xhist, 0, yData.length);
//			Arrays.fill(yhist, 1);
			
			rawDat = dataTable.getTableVal(0);

			graph.clear();

			graph.setxy(rawDat);
		}
		else{
			xhist = new float[xData.length];
			yhist = new float[yData.length];

			System.arraycopy(xData, 0, xhist, 0, xData.length);
		
			System.arraycopy(yData, 0, yhist, 0, yData.length);

			histToRaw();

			graph.clear();

			graph.setxy(rawDat);
		}
		
		fitC();
		
	}
	
	public void setRawData(boolean isRaw){
		isRawData = isRaw;
	}
 
	public void dataTableUpdated() {	
		if(debug)
			System.out.println("ModelerGui after dataTableUpdated do nothing ?? SamplerButton clicked modelerObject ="+ modelObject.toString() +"\n  removed calling  FitC(reinit), then call setBins(10) -> syncData ->FitC");			
			
		//	boolean initClicked = false;
		//	boolean rescaleClicked = true;
		//	fitC(rescaleClicked, guiLink.rescale.isSelected(), initClicked);
			
		//	setBins(10);
	}

	public void update(Observable arg0, Object arg1) {
	//System.out.println("ModelerGui. update getCalled");
		syncData();
	}
	
}

