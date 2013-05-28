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
package edu.ucla.stat.SOCR.chart.demo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartExampleData;
import edu.ucla.stat.SOCR.chart.SuperXYChart;
import edu.ucla.stat.SOCR.chart.gui.CustomJTable;
import edu.ucla.stat.SOCR.chart.gui.SOCRChartPanel;
import edu.ucla.stat.SOCR.chart.util.Mixture;
import edu.ucla.stat.SOCR.chart.util.MixtureEMExperiment;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class SOCR_EM_MixtureModelChartDemo extends SuperXYChart implements PropertyChangeListener, MouseListener{

	final static int GL_MIX = MixtureEMExperiment.GL_MIX;
	final static int CG_MIX = MixtureEMExperiment.CG_MIX;;
	
	final static int TOGETHER = 1;
	final static int SEPARATE = 2;
	
	final static int FAST_SPEED = 10;
	final static int SLOW_SPEED = 1000;
	final static int  NORMAL_SPEED = 200;
	
	final static int MAX_SERIES = 10;
	final static int MAX_KERNELS = 10;
	
	final static int NUM_RANDOM_PTS = 10;
	
	JPanel  controlPanel;
	
	JComboBox  nKernels;
	JComboBox  selectmix;
	JComboBox  selectModelAll;
//JComboBox  EMswitch;
	JComboBox  selectSpeed;
//	JComboBox  updateChoice;
//	JComboBox  stopChoice;
	
	JButton stepJButton ;
	JButton runJButton;
	JButton stopJButton;
	JButton segmentJButton;
	
	public Thread    EM_Thread = null;
	
	double[]           ws, ws2;
	int updateFreq, stopFreq;

	JButton            RandomPoints;
	JButton            ClearPoints;
    JButton            InitializeKernel;
    JButton            switchBackToAuto;
    JButton            removeLast;
    
    JSplitPane	toolContainer;
    JPanel toolBarContainer;
    
    //moved to EM
   // JTable tempResultsTable;
    //CustomJTable resultsTable;
   // DefaultTableModel tModel_results;
    
    int mixSelected = CG_MIX;
    int speedSelected = NORMAL_SPEED;
    int nkSelected = 1;
    int modelAllSelected = TOGETHER; 
    
    MixtureEMExperiment[] mEMexperiment;
    int num_series=1;
    XYDataset[][] kernels;
  //  int num_kernels;
    
    XYSeriesCollection storage_ds;

    //moved to EM
   // XYDataset[] kernels = null;
   // int num_kernels;
   // int group_points;
   // int[] num_pts_in_kernel;
   //  int num_group;
   // Paint[] color_groups;
    //Paint[] color_kernels= new Paint[10]; 
    //Paint color_mainGroup;
    
    boolean segment_flag;
    boolean manualKernel= false;
  //  boolean show_initKernelButton= true;
  //  boolean show_kernelChoice = true;
    private boolean isPressingAlt = false;
    Point2D startPt, endPt;
    
    Shape[] series_shapes = new Shape[MAX_SERIES];
    Stroke[] series_strokes= new Stroke[MAX_SERIES];
  

    CustomJTable resultsTables[];
    SOCRChartPanel chartPaneltest;
    JFreeChart chart;
    
    protected void createActionComponents(JToolBar toolBar){
		super.createActionComponents(toolBar);
		JButton button;
		
		/**************** wiki Tab ****************/
        Action linkAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
			
		    		try {
		    			//popInfo("SOCRChart: About", new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_PowerTransformFamily_Graphs"), "SOCR: Power Transform Graphing Activity");
		    			parentApplet.getAppletContext().showDocument(
		                        new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_2D_PointSegmentation_EM_Mixture"), 
		                			"SOCR EduMaterials Activities 2D PointSegmentation EM Mixture");
		               } catch (MalformedURLException Exc) {
		            	   JOptionPane.showMessageDialog(null, Exc,
		           				"MalformedURL Error",JOptionPane.ERROR_MESSAGE);
		                        Exc.printStackTrace();
		               }
		    	
			}
		};
	
		button = toolBar.add(linkAction);
       	button.setText(" WIKI_Activity ");
       	button.setToolTipText("Press this Button to go to SOCR 2D PointSegmentation EM Mixture Activity wiki page");  
	}
    
	public void init(){
		CLEAR_BUTTON = false;
		LEGEND_SWITCH= false;
		//DefaultDrawingSupplier supplier = new DefaultDrawingSupplier();
		DrawingSupplier supplier = new DefaultDrawingSupplier(
	            DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE,
	            DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
	            DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
	            DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
	            DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
	        );
		//series_shapes = supplier.createStandardSeriesShapes();
		
		//color_mainGroup = supplier.getNextPaint(); //moved to EM
		for (int i=0; i<10; i++){
			//color_kernels[i] = supplier.getNextPaint();
			//series_strokes[i] = supplier.getNextStroke();
			series_shapes[i] = supplier.getNextShape();
		}
		
		series_strokes[0] =new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, 
				                    BasicStroke.JOIN_BEVEL);
		
		series_strokes[1] =new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, 
                BasicStroke.JOIN_BEVEL);
		
		series_strokes[2] =new BasicStroke(3.0f, BasicStroke.CAP_SQUARE, 
                BasicStroke.JOIN_BEVEL);
		
		series_strokes[3] = new BasicStroke(
                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
                1.0f, new float[] {10.0f, 6.0f}, 0.0f
            );
		series_strokes[4] = new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
                1.0f, new float[] {10.0f, 6.0f}, 0.0f
            );
		
		series_strokes[5] = new BasicStroke(
                3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
                1.0f, new float[] {10.0f, 6.0f}, 0.0f
            );
		series_strokes[6]= new BasicStroke(
                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            );
		series_strokes[7]= new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            );
		series_strokes[8]= new BasicStroke(
                3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            );
		
		series_strokes[9]= new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {2.0f, 6.0f}, 0.0f
            );
			
		controlPanel = new JPanel();
		modelAllSelected = TOGETHER; 
		num_series=1;
		mEMexperiment  = new MixtureEMExperiment[num_series];
		mEMexperiment[0] = new MixtureEMExperiment();
		resultsTables= new CustomJTable[num_series];
		resultsTables[0] = mEMexperiment[0].getResultsTable();
		
		initControlPanel();
		//initResutlsTable();
		SHOW_STATUS_TEXTAREA = false;

		super.init();
	
		indLabel = new JLabel("X");
		depLabel = new JLabel("Y");
		
		mEMexperiment[0].resetSize();
		
		packControlArea();
	}
	
	private void packControlArea(){
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		toolBarContainer  = new JPanel();
		toolBarContainer.add(toolBar);

		toolContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBarContainer, new JScrollPane(controlPanel));
		toolContainer.setContinuousLayout(true);
		toolContainer.setDividerLocation(0.6);
		this.getContentPane().add(toolContainer,BorderLayout.NORTH);
	}
	
	void setEM(){
	
		getMapping();
		
		if (modelAllSelected==TOGETHER)
			num_series=1;
		else {
			//System.out.println(independentVarLength);
			if (independentVarLength==0 || dependentVarLength==0){
				showMessageDialog("Map X/Y first!");
				resetChart();
				return;
			}
			num_series = independentVarLength;
		}
		
		//System.out.println("setEM: num_series="+num_series);
		mEMexperiment  = new MixtureEMExperiment[num_series];
		resultsTables= new CustomJTable[num_series];
		for (int s=0; s<num_series; s++ ){
			mEMexperiment[s] = new MixtureEMExperiment();
		//	resultsTables[s] = mEMexperiment[s].getResultsTable();
		}
		
		createDataset(false);//set mEMexperiemts here
	}
	
	 protected void setChart(){
			// update graph
			//System.out.println("setChart called");

			graphPanel.removeAll();
			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

			graphPanel.add(chartPaneltest);
			graphPanel.validate();


			// get the GRAPH panel to the front
			if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) 
				tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			else {
				graphPanel2.removeAll();
				chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
				graphPanel2.add(chartPaneltest);
				graphPanel2.validate();	
				summaryPanel.validate();
			}
	    }
	 
	void initControlPanel(){
		//System.out.println("initContrlPanel get called + manualKernel="+manualKernel);
		ws = new double[Mixture.maxkp];
		ws2 = new double[Mixture.maxkp];
		ws[0] = 0.1;
		ws2[0] = 10;
		
		for (int i = 1; i < Mixture.maxkp; i++)
			ws[i] = ws2[i] = 1;
			
		selectModelAll = new JComboBox();  //not done
		selectmix = new JComboBox();
		RandomPoints = new JButton("RandomPts");
		ClearPoints = new JButton("ClearPts");
		InitializeKernel = new JButton("InitKernels");
		switchBackToAuto = new JButton("AutoInitKernels");
		removeLast = new JButton("RemoveLastKernel");
		selectSpeed = new JComboBox();
		nKernels = new JComboBox();
	//	EMswitch = new JComboBox();
		//updateChoice = new JComboBox();
		//stopChoice = new JComboBox();  
		
		nKernels.setToolTipText("Select number of Kernels");  
	//	EMswitch.setToolTipText("");
		//updateChoice.setToolTipText("Choose for result report frequence.");
		//controlPanel.setLayout(new FlowLayout());
		//controlPanel.setLayout(new GridLayout(3,3));
		controlPanel.removeAll();
		
		controlPanel.setLayout(new BorderLayout());
		 
		stepJButton = new JButton("Step");
		runJButton = new JButton("Run");
		stopJButton = new JButton("Stop");
		segmentJButton = new JButton("Segment");
		stepJButton.addActionListener(this);
		runJButton.addActionListener(this);
		stopJButton.addActionListener(this);	
		segmentJButton.addActionListener(this);	
		
		JPanel controlPanel1 = new JPanel();
		controlPanel1.add(stepJButton);
		controlPanel1.add(runJButton);
		controlPanel1.add(stopJButton);
		controlPanel1.add(new JLabel("|"));
		controlPanel1.add(segmentJButton);
		controlPanel1.add(selectModelAll);
		controlPanel.add(controlPanel1, BorderLayout.NORTH);
		
		
	/*	updateChoice.addItem("Result Report Freq: 1");
        updateChoice.addItem("Result Report Freq: 10");
        updateChoice.addItem("Result Report Freq: 100");
        updateChoice.addItem("Result Report Freq: 1000");
        updateChoice.setSelectedIndex(0);
        stopChoice.addItem("Run Continuously");
        stopChoice.addItem("Number of Experiments: 10");
        stopChoice.addItem("Number of Experiments: 100");
        stopChoice.addItem("Number of Experiments: 1000");
        stopChoice.addItem("Number of Experiments: 10000");
        stopChoice.setSelectedIndex(1);        
        stopChoice.addItemListener(this);
        updateChoice.addItemListener(this);
        JPanel controlPanel2 = new JPanel();
        controlPanel2.add(updateChoice);
        controlPanel2.add(stopChoice);
        controlPanel.add(controlPanel2, BorderLayout.CENTER);*/
        
		setMixtureSelect(CG_MIX);
		selectmix.addItem("GaussMix");
		selectmix.addItem("LineMix");
        selectmix.setSelectedItem(Integer.toString(CG_MIX));
        mixSelected = CG_MIX;
        selectmix.addActionListener(this);
     
        selectModelAll.addItem("Model all X/Y series Together");
		selectModelAll.addItem("Model all X/Y serues independently");
		selectModelAll.setSelectedItem(Integer.toString(TOGETHER));
		modelAllSelected = TOGETHER; 
		selectModelAll.addActionListener(this);
        
        selectSpeed.addItem("Normal");
        selectSpeed.addItem("Fast");
        selectSpeed.addItem("Slow");
        selectSpeed.setSelectedItem(Integer.toString(0));
        speedSelected = NORMAL_SPEED;
        selectSpeed.addActionListener(this);
                
        RandomPoints.addActionListener(this);
        ClearPoints.addActionListener(this);
        InitializeKernel.addActionListener(this);
        switchBackToAuto.addActionListener(this);
        removeLast.addActionListener(this);
        
        for (int i = 1; i < 10; i++)
            nKernels.addItem(Integer.toString(i));
        nKernels.setSelectedItem(Integer.toString(nkSelected));
        nKernels.addActionListener(this);
        
        JPanel controlPanel3 = new JPanel();  
        controlPanel3.add(new JLabel("Mixture model:"));
        controlPanel3.add(selectmix);
        controlPanel3.add(new JLabel("Speed:"));
        controlPanel3.add(selectSpeed);
        controlPanel3.add(RandomPoints);
        controlPanel3.add(ClearPoints);
        if (!manualKernel){
        	controlPanel3.add(InitializeKernel);    
        	JLabel nk = new JLabel("Number of kernels:");
        	controlPanel3.add(nk);
        	controlPanel3.add(nKernels);
        	switchBackToAuto = null;
        	removeLast = null;
        }
        else {
        	controlPanel3.add(switchBackToAuto);
        	controlPanel3.add(removeLast);
        }
 
        controlPanel.add(controlPanel3, BorderLayout.SOUTH);
	}
	
	 public void setMixtureSelect(int sel) {
		 for (int s=0; s<num_series; s++){
			 if (sel==CG_MIX || sel==GL_MIX){
				 mixSelected = sel;
				 mEMexperiment[s].setMixSelected(sel);
			 }
		 }
     }
	 
	 public void setSpeed(String newSpeed) {
		 if (newSpeed.toLowerCase().startsWith("fa")) // FAST Speed requested
        	 speedSelected = FAST_SPEED;
         if (newSpeed.toLowerCase().startsWith("sl")) // Slow Speed requested
        	 speedSelected = SLOW_SPEED;
         else // Normal Speed requested
        	 speedSelected = NORMAL_SPEED;
     }
	 
	 public void doExperiment() {
		// updateCount = 0;
	      //  getRecordTable().append("\n" + getTime());
	        EM_Thread = null; 
	        for (int s=0; s<num_series; s++){
	        	mEMexperiment[s].EM(ws);
	        	}
	    } 

	 
	 public void stop() {
		 segment_flag= false;
		// System.out.println("stoped");
		 EM_Thread = null; 
	    }
	 
	 public void run() {
		 Thread thisThread = Thread.currentThread();		 
         while (EM_Thread == thisThread) {
        	
             try {
            	 for (int s=0; s<num_series; s++){
            		 for (int i = 0; i < 5; i++) {
            			 mEMexperiment[s].EM(ws);
            		 }
            	 }
                 redoChart(); // don't redoTable
                 Thread.sleep(speedSelected);
                 Thread.sleep(300);  // needed for chart drawing
             } catch (InterruptedException e) {}
         }
     }
	 public void start() {
		 segment_flag= false;
         if (EM_Thread == null) {
             EM_Thread = new Thread(this);  
             EM_Thread.start();
         } 
     }

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == addButton1) {
    		addButtonDependent();
    		return;
    	}	
    	else if (event.getSource() == removeButton1) {
    		removeButtonDependent();
    		return;
    	}	
    	else if (event.getSource() == addButton2) {
    		addButtonIndependent();
    		return;
    	}	
    	else if (event.getSource() == removeButton2) {
    		removeButtonIndependent();
    		return;
    	}	
     		
        if (event.getSource() == stepJButton) {
            stop();
            for (int s=0; s<num_series; s++){
            	mEMexperiment[s].EM(ws);
            }
         }  else if (event.getSource() == runJButton) {
        	 stop();
        	 start();
        	 return;
         } else if (event.getSource() == stopJButton) {
            stop();
         }
         else if (event.getSource() == segmentJButton) {
        	
        	 stop();
        	 if (mixSelected == GL_MIX )
        		 return;
        	 
             XYDataset[] ds1 = new XYDataset[num_series];
             for (int s =0; s<num_series; s++)
            	 	ds1[s]=mEMexperiment[s].getSegmentedPoints();
          //   resultsTable = mEMexperiment.getResultsTable();
             segment_flag=true;
             
     		if (ds1 != null){
     			setTable(storage_ds);
     			//if (redoTable)
     			kernels = addKernels();
     			chart = createChart(ds1);	
     			refreshChartPanel();
     			setChart();
     		}
     		return;
         }
         else if (event.getSource() instanceof JComboBox) {
            JComboBox JCB = (JComboBox) event.getSource();
            String JCB_Value = (String) JCB.getSelectedItem();
          //  System.out.println("JCB_Value = " + JCB_Value);
            
            if (JCB_Value.indexOf("Together")!=-1) {
                // System.out.println("GaussMix");
            	modelAllSelected= TOGETHER;
            	setEM();
             	updateKernels();
             }else if (JCB_Value.indexOf("independently")!=-1) {
                 // System.out.println("GaussMix");
            	 modelAllSelected=  SEPARATE;
            	setEM();
              	updateKernels();
              }
             else if (JCB_Value.equals("GaussMix")) {
               // System.out.println("GaussMix");
            	setMixtureSelect(CG_MIX);
            	updateKernels();
            } else if (JCB_Value.equals("LineMix")) {
              //  System.out.println("LinearMix");
            	setMixtureSelect(GL_MIX);
            	updateKernels();
            } else if (JCB_Value.equals("Fast")) {
               // System.out.println("Speed selected: Fast");
                setSpeed("Fast");
            } else if (JCB_Value.equals("Normal")) {
               // System.out.println("Speed selected: Normal");
                setSpeed("Normal");
            } else if (JCB_Value.equals("Slow")) {
               // System.out.println("Speed selected: Slow");
                setSpeed("Slow");
            } else // Number of Kernels
            {
                stop();
                nkSelected = Integer.parseInt(JCB_Value);
                for (int s=0; s<num_series; s++){
                	mEMexperiment[s].setNumOfKernels(nkSelected);
                }
                updateKernels();
                if (EM_Thread!= null) 
                	start();
            }
        } else if (event.getActionCommand().equals("ClearPts")) {
    		resetMappingList();
    		 for (int s=0; s<num_series; s++){
    			 mEMexperiment[s].getDB().clearPoints();
    			 mEMexperiment[s].resetSize();
    		 }
        } else if (event.getActionCommand().equals("RandomPts")) {    
        	 for (int s=0; s<num_series; s++){
        		 mEMexperiment[s].getDB().randomPoints(NUM_RANDOM_PTS)  ;     		
        		 mEMexperiment[s].resetSize();
        	 }
        	 addRandomPts2Storage();
        	//updateKernels();
           // System.out.println("ButtonRandomPts::DB.nPoints()=" + mEMexperiment.getDB().nPoints());
        } else if (event.getActionCommand().equals("InitKernels")) {       	
        	 updateKernels();
        } else if (event.getActionCommand().equals("AutoInitKernels")) {       	
        	 manualKernel = false;
        	 isPressingAlt = false;
        	 for (int s=0; s<num_series; s++){
     			mEMexperiment[s].setManualKernel(false);            	
             }
        	 initControlPanel();
        	 this.getContentPane().add(toolContainer,BorderLayout.NORTH);
 			 updateKernels();
        }else if (event.getActionCommand().equals("RemoveLastKernel")){
        	removeLastManualKernel();
        	
        }

       // System.out.println("Action performed : inde="+independentVarLength);
        redoChart(); // redotable
      //  System.out.println("Action performed after redoChart : inde="+independentVarLength);
        return;
     }
	
	public void addRandomPts2Storage(){
		//System.out.println("adding random point to storage");
		for (int s=0; s<num_series; s++){
			XYSeries s1 = storage_ds.getSeries(s);
			int npt=mEMexperiment[s].getDB().nPoints();
			for (int i=npt-NUM_RANDOM_PTS; i<npt; i++){
				//System.out.println("i="+i +"("+mEMexperiment[s].getDB().xVal(i)+","+mEMexperiment[s].getDB().yVal(i)+")");
				s1.add(mEMexperiment[s].getDB().xVal(i),mEMexperiment[s].getDB().yVal(i));
			}	
			
			XYSeries s2 = storage_ds.getSeries(s);
			/*for (int j=0; j<18; j++)
				System.out.println(s2.getX(j)+","+s2.getY(j));*/
		}
		setTable(storage_ds);
	}
	
	public void updateKernels(){	
		 for (int s=0; s<num_series; s++){
			 mEMexperiment[s].setnk(nkSelected, ws);
		 }
	}
	
	
     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo){
        	
        	storage_ds = new XYSeriesCollection();
        	modelAllSelected = TOGETHER;
        	
        	XYSeries s1 = new XYSeries("Series1", false);
        	s1.add(10, 10);
        	s1.add(20, 40);
        	s1.add(30, 30);
        	s1.add(40, 50);
        	s1.add(50, 50);
        	s1.add(60, 70);
        	s1.add(70, 70);
        	s1.add(80, 80);
        	XYSeries s2 = new XYSeries("Series2", false);
        	s2.add(10, 50);
        	s2.add(20, 70);
        	s2.add(30, 60);
        	s2.add(40, 80);
        	s2.add(50, 40);
        	s2.add(60, 40);
        	s2.add(70, 20);
        	s2.add(80, 10);
        /*	XYSeries s3 = new XYSeries("Series3", false);
        	s3.add(30, 40);
        	s3.add(40, 30);
        	s3.add(50, 20);
        	s3.add(60, 30);
        	s3.add(70, 60);
        	s3.add(80, 30);
        	s3.add(90, 40);
        	s3.add(100, 30);*/
        	storage_ds.addSeries(s1);
        	storage_ds.addSeries(s2);
        	//storage_ds.addSeries(s3);
        	
        	mEMexperiment[0].reset();
        	mEMexperiment[0].setName("Series all");
        	mEMexperiment[0].getDB().push(10, 10);
	        mEMexperiment[0].getDB().push(20, 40);
	        mEMexperiment[0].getDB().push(30, 30);
	        mEMexperiment[0].getDB().push(40, 50);
	        mEMexperiment[0].getDB().push(50, 50);
	        mEMexperiment[0].getDB().push(60, 70);
	        mEMexperiment[0].getDB().push(70, 70);
	        mEMexperiment[0].getDB().push(80, 80);
	
	        mEMexperiment[0].getDB().push(10, 50);
	        mEMexperiment[0].getDB().push(20, 70);
	        mEMexperiment[0].getDB().push(30, 60);
	        mEMexperiment[0].getDB().push(40, 80);
	        mEMexperiment[0].getDB().push(50, 40);
	        mEMexperiment[0].getDB().push(60, 40);
	        mEMexperiment[0].getDB().push(70, 20);
	        mEMexperiment[0].getDB().push(80, 10);
	
	      /*  mEMexperiment[0].getDB().push(30, 40);
	        mEMexperiment[0].getDB().push(40, 30);
	        mEMexperiment[0].getDB().push(50, 20);
	        mEMexperiment[0].getDB().push(60, 30);
	        mEMexperiment[0].getDB().push(70, 60);
	        mEMexperiment[0].getDB().push(80, 30);
	        mEMexperiment[0].getDB().push(90, 40);
	        mEMexperiment[0].getDB().push(100, 30);*/
	        
	        mEMexperiment[0].getDB().setRange();
	        mEMexperiment[0].resetSize();
	       //return mEMexperiment[0].getDB().getDataset();
	        return storage_ds;
        
	        }
		else {

			setArrayFromTable();
			
			//System.out.println(independentVarLength+","+dependentVarLength);
			if (independentVarLength != dependentVarLength){
				showMessageDialog("The number of X and Y doesn't match!");
				resetChart();
				return null;
			}

			String[][] x= new String[xyLength][independentVarLength];
			double[][] y= new double[xyLength][dependentVarLength];

		//	System.out.println("SOCR_EM creatDatabase: xylength="+xyLength);
	
			//System.out.println("SOCR_EM creatDatabase: independentVarLength="+independentVarLength);
			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			try{
				for (int index=0; index<dependentVarLength; index++)
					for (int i = 0; i < xyLength; i++) {
						
						if (depValues[i][index]!=null && depValues[i][index]!="null" && depValues[i][index].length()!=0) {
							//System.out.println("depValues["+i+"]["+index+"]"+depValues[i][index]);
							y[i][index] = Double.parseDouble(depValues[i][index]);
						}
					}
			}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
					return null;}

			// create the dataset... 
			storage_ds = new XYSeriesCollection(); 
			XYSeries series;	
			
			//dependent 
			try{
			for (int i=0; i<independentVarLength; i++){
				String serieName ;
				if (independentHeaders[i].lastIndexOf(":")!=-1)
					serieName= independentHeaders[i].substring(0, independentHeaders[i].lastIndexOf(":"));
				else 
					serieName= "Serie:"+(i+1);
			
				series = new XYSeries(serieName, false);
				for (int j=0; j<xyLength; j++){
					if (x[j][i]!=null && x[j][i]!="null"&&x[j][i]!="NaN"&&x[j][i].length()!=0)
						series.add(Double.parseDouble(x[j][i]), y[j][i]);
					//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
				}
				storage_ds .addSeries(series);
			}}catch(NumberFormatException e)
				{
					showMessageDialog(" Data format error!");
					return null;}
	
		// mEMexperiment.getDB().clearPoints();
		
		 if (modelAllSelected == TOGETHER){	
			 mEMexperiment[0].setName("Series1");
			 for (int s=0; s<storage_ds.getSeriesCount(); s++)
				 for (int i=0; i<storage_ds .getItemCount(s); i++){
					 mEMexperiment[0].getDB().push(storage_ds .getXValue(s, i), storage_ds.getYValue(s, i));
			 }
		 }else{
			// System.out.println("-----print storage_ds----");
			// printDataset(storage_ds);
			 for (int s=0; s<storage_ds.getSeriesCount(); s++){
				 String serieName ;
					if (independentHeaders[s].lastIndexOf(":")!=-1)
						serieName= independentHeaders[s].substring(0, independentHeaders[s].lastIndexOf(":"));
					else 
						serieName= "Serie:"+(s+1);
					
				 mEMexperiment[s].getDB().clearPoints();
				 mEMexperiment[s].setName(serieName);
				 for (int i=0; i<storage_ds .getItemCount(s); i++){
					// System.out.println("push ("+storage_ds .getXValue(s, i)+","+storage_ds .getYValue(s, i)+"for series:"+s);
					 mEMexperiment[s].getDB().push(storage_ds .getXValue(s, i), storage_ds.getYValue(s, i));
				 }
			 }
		 }
		//  System.out.println("SOCR_EM creatDatabase:3");
		 for (int s=0; s<num_series; s++){
			 mEMexperiment[s].getDB().setRange();
			 mEMexperiment[s].resetSize();
		 }
		 //return mEMexperiment.getDB().getDataset();
		 return storage_ds;
		}
        
    }
    
    public void doChart(){
    //	System.out.println("doChart get called");
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}
		
		XYDataset dataset = createDataset(false);	
		XYDataset[] ds1 = new XYDataset[num_series];
		ds1[0]=dataset;
		 
		chart = createChart(ds1);	
		refreshChartPanel();
		setChart();
	}
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
   // protected JFreeChart createChart(XYDataset dataset, double[][] polygons, int num_poly) {
  
    protected JFreeChart createChart(XYDataset[] dataset) {
    	//System.out.println("createChart get called");
    	boolean legend = false;
    	/*if (num_series>1&&segment_flag==false)
    		legend = true;*/
    	
    //	System.out.println("createchart: inde="+independentVarLength);
        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset[0],                  // data
            PlotOrientation.VERTICAL,
            legend,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

    	//printDataset(dataset, num_series);
    	
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
       // get a reference to the plot for further customisation...
        XYPlot subplot1 = (XYPlot) chart.getPlot();
        subplot1.setBackgroundPaint(Color.lightGray);
        subplot1.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        subplot1.setDomainGridlinePaint(Color.white);
        subplot1.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer1 
        = (XYLineAndShapeRenderer) subplot1.getRenderer();
     
        renderer1.setBaseLinesVisible(false);
        renderer1.setBaseShapesVisible(true);
        renderer1.setBaseShapesFilled(true);
        
    
        int[] num_group = new int[num_series];
        Paint color_mainGroup = mEMexperiment[0].getColorOfMainGroup();
        
        // use same color   for all series if EM together    
     //  System.out.println("dataset0. seriescount="+dataset[0].getSeriesCount()+" itemcount="+dataset[0].getItemCount(0));
     if (num_series==1)
      for (int s =0; s<dataset[0].getSeriesCount(); s++){
            renderer1.setSeriesLinesVisible(s, false);
            renderer1.setSeriesShapesVisible(s, true);
            renderer1.setSeriesShapesFilled(s,true);
            renderer1.setSeriesShape(s, series_shapes[0]);     // for the shape of the dots
            renderer1.setSeriesPaint(s, color_mainGroup);
        }
  
        
    //  System.out.println("num_series="+num_series);
     //   System.out.println("num_group="+num_group);
    //  System.out.println("segment_flag="+segment_flag);
        
      if (legend){
      LegendItemCollection legends_old =subplot1.getFixedLegendItems();
      LegendItemCollection legends_new = new LegendItemCollection();
      for (int s=0; s<num_series; s++){
    	  legends_new.add(legends_old.get(s));
      }
      subplot1.setFixedLegendItems(legends_new);
  
      }
        //segment
        
        for (int s =0; s<num_series; s++){
        	num_group[s] = mEMexperiment[s].getNumOfGroup();
        }
        
       // System.out.println("num_group[0]="+num_group[0]);
        
        if (num_group[0]>0 && segment_flag && (mixSelected==CG_MIX)){
        	Paint[] color_groups = mEMexperiment[0].getColorOfGroups();
        	
        	for (int i = 0; i<num_group[0]-1; i++){
        		//System.out.println("color "+ i +"=" + color_groups[i].toString());
        		renderer1.setSeriesPaint(i,color_groups[i]);
        	}
        		//System.out.println("color main ="+ color_mainGroup.toString());
        		renderer1.setSeriesPaint(nkSelected,color_mainGroup);
       	}else 
        		renderer1.setBasePaint(color_mainGroup);
        	
       // renderer1.setShape(series_shapes[0]);   
        
       // subplot1.setDataset(0, dataset[0]);
        //System.out.println("setting renderer for " +0);
     //   subplot1.setRenderer(0, renderer1);
      // System.out.println("SOCR_EM creatChart: pt0="+dataset.getXValue(0, 0)+ ","+dataset.getYValue(0, 0));
     
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) subplot1.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);
	//	rangeAxis.setUpperMargin(0);
      //  rangeAxis.setLowerMargin(0);

        NumberAxis domainAxis = (NumberAxis) subplot1.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setAutoRangeIncludesZero(true);
      //  domainAxis.setUpperMargin(0);
      //  domainAxis.setLowerMargin(0);
        
       XYLineAndShapeRenderer[] renderer1a = new XYLineAndShapeRenderer[num_series];
       if (num_series>1){
        	for (int s=0; s<num_series; s++){
        //	System.out.println("adding pt for series:"+s +": seriesCount "+ dataset[s].getSeriesCount()+"itemcount="+dataset[s].getItemCount(0));
        	 renderer1a[s] = new XYLineAndShapeRenderer();
        	 if (num_group[0]>0 && segment_flag && (mixSelected==CG_MIX)){
              	Paint[] color_groups = mEMexperiment[0].getColorOfGroups();
              	
              	for (int i = 0; i<num_group[0]-1; i++){
              		//System.out.println("color "+ i +"=" + color_groups[i].toString());
              		renderer1a[s].setSeriesPaint(i,color_groups[i]);
              	}
              		//System.out.println("color main ="+ color_mainGroup.toString());
              		renderer1a[s].setSeriesPaint(nkSelected,color_mainGroup);
             	}else 
              		renderer1a[s].setBasePaint(color_mainGroup);
        	 renderer1a[s].setBaseLinesVisible(false);
             renderer1a[s].setBaseShapesVisible(true);
             renderer1a[s].setBaseShapesFilled(true);
             renderer1a[s].setBaseShape(series_shapes[s]);    // different shape of dots for each pair of input    
             subplot1.setDataset(s, dataset[s]);
            // System.out.println("setting renderer for " +s);
             subplot1.setRenderer(s, renderer1a[s]);
        	}
        	
        	 
        	 
       }
        
      if (kernels!=null){
    	  /*  System.out.println("draw kernels");
    	    System.out.println("num_series="+num_series);
    	    System.out.println("nkSelected="+nkSelected);*/
    	  XYLineAndShapeRenderer[][] renderer2 = new XYLineAndShapeRenderer[num_series][nkSelected];
    	  for (int s=0; s<num_series; s++){
   	  
    		  Paint[] color_kernels =mEMexperiment[s].getColorOfKernels();
        // System.out.println("SOCR_EM creatChart adding kernels:" +num_kernels+ "ws[0]="+ws[0]);
    		  for (int i =0; i<nkSelected; i++){
    			 // System.out.println("drawing kernel for series: "+s +" kernel:" +i);
        	//  System.out.println("SOCR_EM creatChart: kernel "+i+" pt0="+kernels[i].getXValue(0, 0)+","+kernels[i].getYValue(0, 0));
    			  renderer2[s][i] = new XYLineAndShapeRenderer();
    			  renderer2[s][i].setBaseStroke(series_strokes[s]);
    			  renderer2[s][i].setBaseLinesVisible(true);
    			  renderer2[s][i].setBaseShapesVisible(false);
    			//  renderer2[s][i].setShapesFilled(false);
    			  renderer2[s][i].setBasePaint(color_kernels[i]);
    			  subplot1.setDataset(num_series-1+s*num_series+i+1, kernels[s][i]);
    			 // System.out.println("setting renderer for " +(num_series-1+s*num_series+i+1));
    			  subplot1.setRenderer(num_series-1+s*num_series+i+1, renderer2[s][i]);
        	//subplot1.setRangeAxis(i+1, rangeAxis);
    		  }//i
    	  }//s
        }
        
      segment_flag = false;
        return chart;
        
    }
/*	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}*/

	/*public void itemStateChanged(ItemEvent event) {
		// TODO Auto-generated method stub
		 if (event.getSource() == updateChoice) {
	            updateFreq = (int) (Math.pow(10, updateChoice.getSelectedIndex()));
	        } else if (event.getSource() == stopChoice) {
	            int j = stopChoice.getSelectedIndex();
	            if (j > 0) stopFreq = (int) (Math.pow(10, j));
	            else stopFreq = -1;
	        }
	}*/
	public String getLocalHelp(){
		return super.getLocalHelp()+ "\n\n"
			+ "This experiments demonstrates the Expectation Maximization (EM) algorithm. \n"
			+ "In this setting the EM is applied as a tool for classisfication.\n"
			+ "\n1. Select random points in the 2D plane (manually by clicking the mouse in the \n"
			+"field of view or by clicking the <RandomPnts> button.\n"
			+"2. Then select the number of cluster that you want to identify\n"
			+"3. Click <InitKernels> to get a different starting condition (EM algorithm is VERY sensitive \n"
			+"to the starting conditions!)\n"
			+"4. Select Normal/Fast/Slow spead of the algorithm (for demo purposes choose Slow)\n"
			+"5. Choose Gaussian or Linear fit for your mixture model\n"
			+"6. Click <EM Run> to start the algorithm. Observe the evolution of the process (convergence is guaranteed!)\n"
			+"7. Finally, use <EM Stop> or <EM 1 Step> to terminate the or take one step at a time\n"
			+"\n You can Segment the initial points based on your Linear/Gaussian fit by pressing <Segment>";
	}
	

	
protected void redoChart(){		
		
	//empty 
		if (mEMexperiment[0].getDB().nPoints()==0){
			//System.out.println("EM redoChart: empty db");
			emptyResultsTable();
			resetTable();
			//resetMappingList();
			resetChart();
			kernels= null;
			ChartExampleData exampleNull = new ChartExampleData(0, 0);
			updateExample(exampleNull);
			if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ALL) 
				setMixPanel();
			return;
		}
		
		XYDataset[] ds1 = new XYDataset[num_series];
		for (int s =0; s<num_series; s++){
			ds1[s]=mEMexperiment[s].getPoints();
			resultsTables[s] = mEMexperiment[s].getResultsTable();
		}
		
			//if (redoTable)
		setTable(storage_ds);
		kernels = addKernels();
	
		//System.out.println("redoChart before createChart get called");
		//printDataset(ds1, num_series);
		chart = createChart(ds1);	
		refreshChartPanel();
		setChart();
		//	System.out.println("redraw done \n\n");
		
	}


protected XYDataset[][] addKernels(){
	XYDataset[][] ks = new XYDataset[num_series][];
		
	for (int s =0; s<num_series; s++){
		ks[s] = mEMexperiment[s].getKernels();
	}
	return ks;
}

protected void resetRTableRows(int n) {
	for (int s=0; s<num_series; s++){
		DefaultTableModel tModel_results = (javax.swing.table.DefaultTableModel) resultsTables[s].getModel();
		tModel_results.setRowCount(n);
		resultsTables[s].setModel(tModel_results);
	}
}

protected JPanel layoutResults(){
	
	String serieName ;
	
	if (independentHeaders!=null && independentHeaders[0].lastIndexOf(":")!=-1)
		serieName= independentHeaders[0].substring(0, independentHeaders[0].lastIndexOf(":"));
	else 
		serieName= "Serie1";
	
	JPanel j = new JPanel();
	j.setLayout(new BoxLayout(j, BoxLayout.Y_AXIS));
	j.add(new JLabel("ResultTable for "+serieName));
	j.add(resultsTables[0]);
	//System.out.println("setTablePane: num_series="+num_series);
	
	for (int s=1; s<num_series; s++){
		if (independentHeaders!=null && independentHeaders[s].lastIndexOf(":")!=-1)
			serieName= independentHeaders[s].substring(0, independentHeaders[s].lastIndexOf(":"));
		else 
			serieName= "Serie"+(s+1);
		j.add(new JLabel("ResultTable for "+serieName));
		j.add(resultsTables[s]);
	}
	return j;
}

protected void setMixPanel(){
	dataPanel2.removeAll();
	graphPanel2.removeAll();

	if (chartPaneltest!=null){		
		chartPaneltest.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
		graphPanel2.add(chartPaneltest);
		graphPanel2.validate();
	}

	dataPanel2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3, CHART_SIZE_Y));
	
	dataPanel2.add(new JLabel(" "));
	dataPanel2.add(new JLabel("Data"));
		
	JScrollPane st = new JScrollPane(layoutResults());

	JScrollPane dt = new JScrollPane(dataTable);
	dt.setRowHeaderView(headerTable);
	
	//dt.setPreferredSize(new Dimension(CHART_SIZE_X*2/3, CHART_SIZE_Y/2));
	JSplitPane	container = new JSplitPane(JSplitPane.VERTICAL_SPLIT, dt, st);
	container.setMinimumSize(new Dimension(CHART_SIZE_X*2/3, CHART_SIZE_Y/2));
	container.setContinuousLayout(true);
	container.setDividerLocation(0.7);
	dataPanel2.add(container);	
	
	dataPanel2.add(new JLabel(" "));
	dataPanel2.add(new JLabel("Mapping"));
	mapPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3, CHART_SIZE_Y/2));
	dataPanel2.add(mapPanel);

	dataPanel2.validate();

	mixPanel.removeAll();
	mixPanel.add(graphPanel2, BorderLayout.WEST);
	mixPanel.add(dataPanel2, BorderLayout.CENTER);
	mixPanel.validate();	
}

protected void setTablePane(){
	dataPanel.removeAll();
	
	JScrollPane st = new JScrollPane(layoutResults());
	JScrollPane dt = new JScrollPane(dataTable);
	dt.setRowHeaderView(headerTable);
	JSplitPane	container = new JSplitPane(JSplitPane.VERTICAL_SPLIT, dt, st);
	container.setContinuousLayout(true);
	container.setDividerLocation(0.8);
	dataPanel.add(container);
	
	dataPanel.validate();
	
}

protected void emptyResultsTable(){
	String[] resultsHeading = new String[2];
	resultsHeading[0] = "Kernel";
	resultsHeading[1] = "Resutls";
	String[][] results = new String[1][2];
	JTable tempResultsTable= new JTable(results, resultsHeading);
	resetRTableRows(tempResultsTable.getRowCount()); 
	    
	for(int i=0;i<tempResultsTable.getRowCount();i++)
		 for(int j=0;j<tempResultsTable.getColumnCount();j++) {
			 for (int s=0; s<num_series; s++){
				 resultsTables[s].setValueAt(tempResultsTable.getValueAt(i,j),i,j);
			 }
		 }
}



protected void setTable(XYDataset ds){
		 // update results table after segment
	//System.out.println("setTable get called");
	//printDataset(ds);
	
		 if (segment_flag){
			// System.out.println("settable: setting segment resultTable");
			 resultsTables = new CustomJTable[num_series];
			 for (int s=0; s<num_series; s++){
				 resultsTables[s] = mEMexperiment[s].getResultsTable();
			 }
	     }
		 else {
			convertor.dataset2Table((XYDataset)ds);
			JTable tempDataTable = convertor.getTable();

			resetTableRows(tempDataTable.getRowCount()+1);
			resetTableColumns(tempDataTable.getColumnCount());
				
	     for(int i=0;i<tempDataTable.getColumnCount();i++) {
	         columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
				//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
	         }

			columnModel = dataTable.getColumnModel();
			dataTable.setTableHeader(new EditableHeader(columnModel));

	     for(int i=0;i<tempDataTable.getRowCount();i++)
	         for(int j=0;j<tempDataTable.getColumnCount();j++) {
	        	
	        	 String v = (String)tempDataTable.getValueAt(i,j); 
	        	 //if (v!="null" && v!="NaN")
	        		 dataTable.setValueAt(v,i,j);
			 }
		 }//no segment
	     
		// int columnCount = dataTable.getColumnCount();
		 
		 //attention this is a quick fix for losted mapping, need revisit!!!!!
			for(int i=0; i<num_series; i++){
				//System.out.println("mapping");
				addButtonIndependent();
				addButtonDependent();
				
			}
			
		 setTablePane();
		 
	        // don't bring graph to the front
	        if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
			//	tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
	        }
	        else{
	        	setMixPanel();
	        }
		}

private void refreshChartPanel(){
	chartPaneltest = new SOCRChartPanel(chart, false); 
	chartPaneltest.addMouseListener(this);
	addKeyEventListener();
}

public void resetExample() {

    XYDataset dataset= createDataset(true);	
   // System.out.println("resetExample num_series="+num_series);
    XYDataset[] ds1 = new XYDataset[num_series];
    ds1[0]=dataset;  
    
    kernels = null;
	chart = createChart(ds1);	
	refreshChartPanel();
	setChart();

    hasExample = true;
	//		convertor.dataset2Table((TimeSeriesCollection)dataset);				
	convertor.dataset2Table(dataset);				
	JTable tempDataTable = convertor.getTable();

	resetTableRows(tempDataTable.getRowCount()+1);
	resetTableColumns(tempDataTable.getColumnCount());
			

    for(int i=0;i<tempDataTable.getColumnCount();i++) {
        columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
		//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
        }

	columnModel = dataTable.getColumnModel();
	dataTable.setTableHeader(new EditableHeader(columnModel));

    for(int i=0;i<tempDataTable.getRowCount();i++)
        for(int j=0;j<tempDataTable.getColumnCount();j++) {
            dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
	 }
    dataPanel.removeAll();
    dataPanel.add(new JScrollPane(dataTable));
	dataTable.setGridColor(Color.gray);
	dataTable.setShowGrid(true);
	dataTable.doLayout();
	// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
	try { 
		dataTable.setDragEnabled(true);  
	} catch (Exception e) {
	}

    dataPanel.validate();

	// do the mapping
	int columnCount = dataTable.getColumnCount();
	for(int i=0; i<columnCount/2; i++){
		//System.out.println("mapping");
		addButtonIndependent();
		addButtonDependent();
		
	}
	
	getMapping();
//	System.out.println("resetExample get called indepvar"+independentVarLength );
	updateStatus(url);
}

void printDataset(XYDataset[] ds, int num_s){
	System.out.println("debugging: print dataset nums="+num_s);
	for (int s=0; s<num_s; s++){
		System.out.println("  debugging: print ds["+s+"]");
		for (int i=0; i<ds[s].getSeriesCount(); i++){
			System.out.println("   debugging: print ds["+s+"]: series:"+i);
			for (int j=0; j<ds[s].getItemCount(i); j++)
				System.out.println("      debugging: print ds["+s+"]: series:"+i+"pt "+j+"("+ds[s].getXValue(i, j)+","+ds[s].getYValue(i, j)+")");
		}//i
	}//s
	
}

void printDataset(XYDataset ds){
	
		System.out.println("  debugging: print single ds");
		for (int i=0; i<ds.getSeriesCount(); i++){
			System.out.println("   debugging: print ds: series:"+i);
			for (int j=0; j<ds.getItemCount(i); j++)
				System.out.println("      debugging: print ds: series:"+i+"pt "+j+"("+ds.getXValue(i, j)+","+ds.getYValue(i, j)+")");
		}//i
	
}

private void addKeyEventListener(){

	// add key listener for press alt key event
	InputMap inputMap = chartPaneltest.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	
	KeyStroke metaPressedStroke = KeyStroke.getKeyStroke("alt ALT");
	Action metaPressedAction = new AbstractAction(){
		public void actionPerformed(ActionEvent actionEvent){
			if (!isPressingAlt){			
			isPressingAlt = true;
			chartPaneltest.setIsPressingAlt(true);
			//System.out.println("key Event EMDemo set " + isPressingAlt);
			}
		}
	};
	inputMap.put(metaPressedStroke, "alt ALT");
	chartPaneltest.getActionMap().put("alt ALT", metaPressedAction);
	//System.out.println("binding EMDemo alt ALT key Event");
	
	KeyStroke metaReleasedStroke = KeyStroke.getKeyStroke("released ALT");
	
	Action metaReleasedAction = new AbstractAction(){
		public void actionPerformed(ActionEvent actionEvent){
			//System.out.println("reseting " + isPressingAlt);
			if (isPressingAlt){				
				isPressingAlt = false;
				chartPaneltest.setIsPressingAlt(false);
			//	System.out.println("key Event EMDemo reset " + isPressingAlt);
			}
		}
	};
	
	inputMap.put(metaReleasedStroke, "released ALT");
	chartPaneltest.getActionMap().put("released ALT", metaReleasedAction);
	//System.out.println("binding EMDemo released ALT key Event");
}

public Point2D getPointInChart(MouseEvent e){
	Insets insets = getInsets();
	//System.out.println("inset.top="+insets.top+" inset.left="+insets.left);
	//System.out.println("scaleX="+chartPaneltest.getScaleX()+" scaleY="+chartPaneltest.getScaleY());
	//System.out.println(e.getX());
//	int mouseX = (int) ((e.getX() - insets.left) / chartPaneltest.getScaleX());
//	int mouseY = (int) ((e.getY() - insets.top) / chartPaneltest.getScaleY());
	int mouseX = (int) (e.getX() - insets.left);
	int mouseY = (int) (e.getY() - insets.top);
//	Point2D pt = new Point2D.Double();
	//pt.setLocation(mouseX, mouseY);
	//return pt;
	
//	System.out.println("x = " + mouseX + ", y = " + mouseY);
	
	Point2D p = chartPaneltest.translateScreenToJava2D(new Point(mouseX, mouseY));
	XYPlot plot = (XYPlot) chart.getPlot();
	ChartRenderingInfo info = chartPaneltest.getChartRenderingInfo();
	Rectangle2D dataArea = info.getPlotInfo().getDataArea();
	
	ValueAxis domainAxis = plot.getDomainAxis();
	RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
	ValueAxis rangeAxis = plot.getRangeAxis();
	RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
	
	double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
	double chartY = rangeAxis.java2DToValue(p.getY(), dataArea, rangeAxisEdge);
	Point2D pt2 = new Point2D.Double();
	
	//double scale = (double)CHART_SIZE_X/(double)CHART_SIZE_Y;
	//System.out.println("scale="+scale);
	pt2.setLocation(chartX, chartY);
	//System.out.println("Chart: x = " + (chartX) + ", y = " + chartY);
	return pt2;
	
	
}

public void mousePressed(MouseEvent e){

	if (isPressingAlt){
		
		//System.out.println("in EMDemo alt mousePressed get called");
	//	System.out.println("EM off zoom");
		chartPaneltest.setMouseZoomable(false,true);	
		startPt = getPointInChart(e);
		
		chartPaneltest.mousePressed(e);	
	//	System.out.println("EM on zoom");
		chartPaneltest.setMouseZoomable(true,false);	
		
	}
	else
		chartPaneltest.mousePressed(e);
	
}

public void mouseReleased(MouseEvent e){
	
	if (isPressingAlt){
		
	//	System.out.println("in EMDemo alt mouseReleased get called");
		//System.out.println("EM off zoom");
		chartPaneltest.setMouseZoomable(false,true);	
		endPt = getPointInChart(e);
		chartPaneltest.mousePressed(e);	
		//System.out.println("EM on zoom");
		chartPaneltest.setMouseZoomable(true,false);
		
	
		double xSize = Math.abs(endPt.getX()-startPt.getX());
		double ySize = Math.abs(endPt.getY()-startPt.getY());
		double xStart, yStart;
		
		if (startPt.getX() < endPt.getX())
			xStart= startPt.getX();
		else xStart = endPt.getX();
		if (startPt.getY() < endPt.getY())
			yStart= startPt.getY();
		else yStart = endPt.getY();
	
		 // System.out.println("SOCREM demo  mouseReleased xsiz="+xSize+" ysiz="+ySize+" xStart="+xStart+" yStart="+yStart);
		stop();       
		addManualKernel(xSize, ySize, xStart,yStart);
        redoChart(); 
        
       // isPressingAlt = false;
	}
	else
		chartPaneltest.mouseReleased(e);
}

private void removeLastManualKernel(){
	nkSelected--;
	for (int s=0; s<num_series; s++){
		mEMexperiment[s].setNumOfKernels(nkSelected);
		mEMexperiment[s].setManualKernel(true);
    }

    updateKernels();
    if (EM_Thread!= null) 
    	start();
}

private void addManualKernel(double xSize, double ySize, double xStart, double yStart){
	if (! manualKernel){  //first manualKernel
		nkSelected = 1;
		manualKernel = true;
		initControlPanel();
		this.getContentPane().add(toolContainer,BorderLayout.NORTH);
	}else{
		if (nkSelected<=8)
			nkSelected++;
	}
	 	
	for (int s=0; s<num_series; s++){
		mEMexperiment[s].setNumOfKernels(nkSelected);
		mEMexperiment[s].setManualKernel(true);
		mEMexperiment[s].resetSize(nkSelected, xSize, ySize, xStart, yStart);
    }

    updateKernels();
    if (EM_Thread!= null) 
    	start();
}
}
