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
package edu.ucla.stat.SOCR.applications.demo;

import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.ucla.stat.SOCR.applications.Application;
import edu.ucla.stat.SOCR.core.IExperiment;
import edu.ucla.stat.SOCR.util.FloatTextField;
import edu.ucla.stat.SOCR.util.Matrix;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * The triangle experiment is to break a stick at random and see if the pieces
 * form a triangle. If so, is the triangle acute or obtuse?
 */
public class PortfolioApplication2 extends Application  implements Observer, IExperiment, ChartMouseListener,ActionListener{

	private static final float LEFT_ALIGNMENT = 0;
	protected final int CHART_SIZE_X = 500;
	protected final int SLIDER_SIZE_X = 900;
    protected final int CHART_SIZE_Y = 400;
    protected ChartPanel chartPanel;

    protected final String GRAPH 	= "GRAPH";
    protected final String INPUT 	= "INPUT";
    protected final String ALL 	= "SHOW ALL";

    protected boolean SHOW_STATUS_TEXTAREA = false;

    protected JPanel graphPanel, graphPanel2;
    protected JPanel mixPanel;
    protected JPanel inputPanel, sliderPanel,sliderPanel2, leftControl;
    private JTextPane statusTextArea;

    protected boolean show_tangent;

    protected double mouse_x;
    protected double mouse_y;

	 // statistical input variables
    protected double[][] ExpectedReturn;    // expected return
    protected double[][] COVR;  // Covariance
    protected double[][] CORR;  // correlation
    Matrix covr_matrix, ER_matrix, corr_matrix;
    boolean covarianceFlag=true; //true Covertiance, false Correlation;
    boolean switchedFlag=true; //true changed, need to switch slider panel

    private DecimalFormat formatter = new DecimalFormat("#0.0####");
    private DecimalFormat tooltip_formatter = new DecimalFormat("#0.000");

    boolean mouseClicked= false;
    String tooltip;

    protected double t1_x, t2_x;
    protected double t1_y, t2_y;

  //  LinkedList dataPoints;
    // data points
    SimulatedDataPoints simulatedPoints;
    ChartDataPoints chartDataPoints;

 //   int numStocks = 5;
    int numSimulate = 2000;

    JToolBar toolBar;

    public static String COVR_Switch = "Covariance Matrix";
    public static String CORR_Switch = "CorrelationMatrix";
    String[] switchArray ={COVR_Switch, CORR_Switch};
    JRadioButton[] rButtons;

   /* FloatSlider[]  rSliders, mSliders;
	FloatSlider tSlider;*/
    FloatTextField[]  rSliders, mSliders;
	FloatTextField tSlider;
	XYSeries p_serie, m_serie, t_serie, s_serie;

	protected int tabbedPaneCount = 0;
	public JTabbedPane tabbedPanelContainer;

	public static String STOCK2 = "2";
	public static String STOCK3 = "3";
	public static String STOCK4 = "4";
	public static String STOCK5 = "5";
	String[] numStocksArray ={STOCK2,STOCK3,STOCK4,STOCK5};

	String[] on_off = {"On", "Off"};

	public double[] r={0.0064, 0.0022, 0.02117, 0.01, 0.0134};
	public double[] c={0.0101,0.0122, 0.0119, 0.0141,0.0144}; // VARIANCES
	public double[] m={0.0045,0.0041, 0.0026, 0.0012, 0.0011, 0.0015, 0.0043, 0.0022, 0.0058, 0.005}; // COVARIANCE matrix
	/* private JButton stepJButton = new JButton("Step"), runJButton = new JButton(
     "Run"), stopJButton = new JButton("Stop");*/


    /**
     * This method initializes the experiment, including the toolbar, triangle,
     * scatterplot, random variable graph and table
     */

    public PortfolioApplication2() {

    	setName("PortfolioExperiment");
    	init();

    }

    public void setNumberStocks(String number){
		numStocks= Integer.parseInt(number);
		init();
		statusTextArea.setText("setting stock number to "+numStocks);
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

 	protected JFreeChart createEmptyChart(String chartTitle, PieDataset dataset) {

		JFreeChart chart = ChartFactory.createPieChart(
			chartTitle,  // chart title
            null,             // data
            true,                // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
}

    protected void initGraphPanel(){
		//System.out.println("initGraphPanel called");
		graphPanel = new JPanel();

		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		JFreeChart chart = createEmptyChart("SOCR Applications", null);	//create a empty graph first
		chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));


		graphPanel.add(chartPanel);
		graphPanel.validate();
	}

    protected void initMixPanel(){
		sliderPanel2 = new JPanel();
		sliderPanel2.setLayout(new BoxLayout(sliderPanel2, BoxLayout.Y_AXIS));

		graphPanel2 = new JPanel();
		//		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));

		mixPanel = new JPanel(new BorderLayout());
		//		resetChart();

		setMixPanel();
	}

    protected void initInputPanel(){
    	sliderPanel = new JPanel();
    	sliderPanel2 = new JPanel();
    	inputPanel = new JPanel();
    	inputPanel.setAlignmentX(LEFT_ALIGNMENT);
    	covarianceFlag= true;
    	initSliders();
    	setSliders();

    }

	public void init(){

	  //	dataPoints = new LinkedList();
	 	t1_x=t2_x=0; t1_y=t2_y=0.001;
	  	simulatedPoints = setupUniformSimulate(numStocks, numSimulate);
	  	tabbedPanelContainer = new JTabbedPane();
	    show_tangent= true;

	  //	addGraph(chartPanel);
	  //	addToolbar(sliderPanel);
	  	initGraphPanel();
	  	initMixPanel();
	  	initInputPanel();
	  	emptyTool();
	  	emptyTool2();
	  	leftControl = new JPanel();
	  	leftControl.setLayout(new BoxLayout(leftControl, BoxLayout.PAGE_AXIS));
	  	addRadioButton2Left("Number of Stocks:", "", numStocksArray, numStocks-2, this);
	  	addRadioButton2Left("Show Tangent Line :", "", on_off, 0, this);

	  	JScrollPane mixPanelContainer = new JScrollPane(mixPanel);
		mixPanelContainer.setPreferredSize(new Dimension(600,CHART_SIZE_Y+100));

	  	addTabbedPane(GRAPH, graphPanel);

		addTabbedPane(INPUT, inputPanel);

		addTabbedPane(ALL, mixPanelContainer);

		setChart();

		tabbedPanelContainer.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ALL) {
					mixPanel.removeAll();
					setMixPanel();
					mixPanel.validate();
				}else 	if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==GRAPH) {
					graphPanel.removeAll();
					setChart();

				}else 	if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==INPUT) {
					//
					setInputPanel();
				}
			}
		});

	  	statusTextArea = new JTextPane();  //right side lower
	  	statusTextArea.setEditable(false);
	  	JScrollPane statusContainer = new JScrollPane(statusTextArea);
	  	statusContainer.setPreferredSize(new Dimension(600, 140));

	  	JSplitPane	upContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(leftControl), new JScrollPane(tabbedPanelContainer));

	  	this.getMainPanel().removeAll();
	  	if (SHOW_STATUS_TEXTAREA){
				JSplitPane	container = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(upContainer), statusContainer);
				container.setContinuousLayout(true);
				container.setDividerLocation(0.6);
				this.getMainPanel().add(container, BorderLayout.CENTER);
				}
			else {

				this.getMainPanel().add(new JScrollPane(upContainer), BorderLayout.CENTER);
			}
	  	this.getMainPanel().validate();

		}

	protected void setInputPanel(){

	 	inputPanel.removeAll();

	 	setSliders();
	 	JScrollPane sp = new JScrollPane(sliderPanel);
		inputPanel.add(sp);

	 	inputPanel.validate();
	 }

	public void addRadioButton2Left(String text, String toolTipText, String[] bValues,int defaultIndex, ActionListener l) {

	 	 JPanel in = new JPanel();

	 	in.add(Box.createVerticalGlue());
	 	in.add(new JLabel(text));
	 	 ButtonGroup group = new ButtonGroup();


	 	 rButtons = new JRadioButton[bValues.length];
	 	 for (int i=0; i<bValues.length; i++){
	 		rButtons[i] = new JRadioButton(bValues[i]);
	 		rButtons[i].setName(bValues[i]);
	 		rButtons[i].addActionListener(l);
	 		rButtons[i].setActionCommand(bValues[i]);
	 		in.add(rButtons[i]);
	 		 group.add(rButtons[i]);
	 		 if (defaultIndex==i)
	 			rButtons[i].setSelected(true);
	 	 }

	 	leftControl.add(in);
	 }

	protected void setMixPanel(){
			sliderPanel2.removeAll();
			graphPanel2.removeAll();

			graphPanel2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
			graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));
			if (chartPanel!=null)
				 graphPanel2.add(chartPanel);
			/*if (legendPanelOn){
				if (legendPanel !=null)
					legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));
				JScrollPane	 legendPane = new JScrollPane(legendPanel);

				legendPane.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));
				graphPanel2.add(legendPane);
			}*/
			graphPanel2.validate();

			sliderPanel2.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
			sliderPanel2.add(new JLabel(" "));
			JScrollPane dt = new JScrollPane(sliderPanel);
			dt.setPreferredSize(new Dimension(CHART_SIZE_X*3/2, CHART_SIZE_Y));

			sliderPanel2.add(dt);
			/*JScrollPane st = new JScrollPane(summaryPanel);
			st.setPreferredSize(new Dimension(CHART_SIZE_X/3,CHART_SIZE_Y/6));
			sliderPanel2.add(st);
			st.validate();

			sliderPanel2.add(new JLabel(" "));
		sliderPanel2.add(new JLabel("Mapping"));
			mapPanel.setPreferredSize(new Dimension(CHART_SIZE_X/3, CHART_SIZE_Y/2));
			sliderPanel2.add(mapPanel);*/

			sliderPanel2.validate();

			mixPanel.removeAll();
			mixPanel.add(graphPanel2, BorderLayout.WEST);
			mixPanel.add(new JScrollPane(sliderPanel2), BorderLayout.CENTER);
			mixPanel.validate();
		}

    SimulatedDataPoints setupUniformSimulate(int numS, int numE){
    	SimulatedDataPoints dp = new SimulatedDataPoints(numS, numE);
    	ContinuousUniformDistribution uniform = new ContinuousUniformDistribution(-2, 2);
    	for (int j=0; j<numE; j++){
    		double stocks[] = new double[numStocks];
    		for (int i=0;i<numS; i++)
    			stocks[i]=uniform.simulate();
    		dp.addPoint(stocks);
    	}

    	return dp;
    }

    void initGraph(){

    	mouse_x = mouse_y = 0;
		JFreeChart chart = createEmptyChart(null);	//create a empty graph first
		chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

    }

    public void addRadioButton(int x, int y, String text, String toolTipText, String[] bValues,int defaultIndex, JPanel panel, ActionListener l) {
    	JPanel p = new JPanel();

    	p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = x;
    	c.gridy = y;

     	 ButtonGroup group = new ButtonGroup();

     	 p.setToolTipText(toolTipText);
     	 p.add(new JLabel(text));

     	 rButtons = new JRadioButton[bValues.length];
     	 for (int i=0; i<bValues.length; i++){
     		rButtons[i] = new JRadioButton(bValues[i]);
     		rButtons[i].setName(bValues[i]);
     		rButtons[i].addActionListener(l);
     		rButtons[i].setActionCommand(bValues[i]);
     		 p.add(rButtons[i]);
     		 group.add(rButtons[i]);
     		 if (defaultIndex==i)
     			rButtons[i].setSelected(true);
     	 }
     	 panel.add(p,c);
     }

    private boolean isAllPositive(int sdpPointer)
    {
    	//double[][] sdp= new double[1][numStocks];
    	double[][] sdp = simulatedPoints.getRow(sdpPointer);
		boolean positive = true;

		for (int i=0; i < numStocks; i++)
		{
			if(sdp[0][i] < 0)
			{
				positive = false;
				break;
			}
		}

		return positive;
    }

    void updateGraph(){
    /*	System.out.println("updateGraph get called----------------mouseClicked="+mouseClicked);
    	Exception e = new Exception();
    	e.printStackTrace();*/
    //	printMatrix(COVR, "after COVR");
    //	printMatrix(CORR, "after CORR");

    	//setupStockOptions();

    	p_serie = new XYSeries("Stock", false);
        t_serie = new XYSeries("Tangent", false);
        m_serie = new XYSeries("M", false);
        s_serie = new XYSeries("Positives", false);

        StringBuffer text= new StringBuffer();
    	text.append("mouse clicked at (Risk, Expected Return) = "+tooltip+"\n");
    //	text.append("The stock combinations are:");

    	for (int i=0; i<chartDataPoints.getPointCount();i++){
    		double point_x =chartDataPoints.getX(i);
    		double point_y =chartDataPoints.getY(i);
    		p_serie.add(point_x, point_y);
    		//  System.out.println("chart point " +(i+1)+" is ("+point_x+", "+point_y+")");
    		if(isAllPositive(chartDataPoints.getSDPpointer(i)))
    		{
    			s_serie.add(point_x, point_y);
    		}
    	}

    	if (show_tangent){
    		t_serie.add(t1_x, t1_y);
    		t_serie.add(t2_x, t2_y);
    	}

	 	if (mouseClicked){
	    	//	System.out.println("mouse_x="+mouse_x+", mouse_y="+mouse_y);
    		m_serie.add(mouse_x, mouse_y);
		}

    	XYSeriesCollection ds = new XYSeriesCollection();

    	ds.addSeries(m_serie);
       	ds.addSeries(t_serie);
       	ds.addSeries(s_serie);
    	ds.addSeries(p_serie);


   	  JFreeChart chart = ChartFactory.createXYLineChart(
    	            "",      // chart title
    	            "Risk (Standard Deviation)",                      // x axis label
    	            "Expected Return",                      // y axis label
    	            ds,                  // data
    	            PlotOrientation.VERTICAL,
    	            false,                     // include legend
    	            true,                     // tooltips
    	            false                     // urls
    	        );
    	  chart.setBackgroundPaint(Color.white);
    	  XYPlot subplot1 = (XYPlot) chart.getPlot();
    	  XYLineAndShapeRenderer renderer1
          = (XYLineAndShapeRenderer) subplot1.getRenderer();

    	  NumberAxis xAxis = (NumberAxis) subplot1.getDomainAxis();
    	  NumberAxis yAxis = (NumberAxis) subplot1.getRangeAxis();
    	  if (t2_x>0)
    		  xAxis.setRange(0, t2_x);
    	  else
    		  xAxis.setRange(t2_x,0);

    	  if (t2_y>0)
    		  yAxis.setRange(-t2_y, t2_y);
    	  else
    		  yAxis.setRange(t2_y, -t2_y);

    	  renderer1.setSeriesPaint(3, Color.blue);
    	  renderer1.setSeriesPaint(2, Color.red);
    	  renderer1.setSeriesPaint(1, Color.red);
    	  renderer1.setSeriesPaint(0, Color.green);

    	  Shape shape = renderer1.getBaseShape();
    	  renderer1.setSeriesShape(1, shape);
    	  renderer1.setSeriesShape(3, shape);

    	  renderer1.setSeriesLinesVisible(0, false);
    	  renderer1.setSeriesLinesVisible(1, true);
    	  renderer1.setSeriesLinesVisible(2, false);
    	  renderer1.setSeriesLinesVisible(3, false);
    	 // renderer1.setLinesVisible();
    	  renderer1.setBaseShapesVisible(true);
    	  renderer1.setBaseShapesFilled(true);


    	  graphPanel.removeAll();
    	  chartPanel = new ChartPanel(chart, false);
    	  chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
    	  chartPanel.addChartMouseListener(this);
    	  graphPanel.add(chartPanel);
    	  graphPanel.validate();
    	//  super.updateGraph(chartPanel);
    	 //

    	  if(mouseClicked){
			getRecordTable().setText(text.toString());}
    	  else {
    		  text = new StringBuffer();
    		 /* text.append("("+tooltip_formatter.format(minPoint.mean)+" , "+ tooltip_formatter.format(minPoint.std)+")\n");
    		  for (int j=0; j<numStocks; j++)
  				text.append("Stock "+(j+1)+" : x["+(j+1)+"]="+tooltip_formatter.format(minPoint.x[j])+"\n");
  			text.append("---\n");*/
    		  getRecordTable().setText(text.toString());;
    	  }


    	 mouseClicked= false;
    }

    void setChart(){


    	computeTangent();
    	computeChartDataPoints();

    	updateGraph();
    	graphPanel.removeAll();
 		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

 		graphPanel.add(chartPanel);
 		graphPanel.validate();


 		// get the GRAPH panel to the front
 		if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL)
 			tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
 		else {
 			graphPanel2.removeAll();
 			chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
 			graphPanel2.add(chartPanel);
 			graphPanel2.validate();
 		//	summaryPanel.validate();
 		}

    }

	// this is only for creat a empty chart
    protected JFreeChart createEmptyChart(PieDataset dataset) {

 		JFreeChart chart = ChartFactory.createPieChart(
             "SOCR Chart",  // chart title
             null,             // data
             true,                // include legend
             true,
             false
         );

         PiePlot plot = (PiePlot) chart.getPlot();
         plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
         plot.setNoDataMessage("No data available");
         plot.setCircular(false);
         plot.setLabelGap(0.02);
         return chart;
 }

/*    void newMSlider(int y, int x, ValueSliderFloat2 slider, double v, JPanel panel) {
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = x;
    	c.gridy = y;

    	slider.setValue(v);
    	slider.addObserver(this);
        panel.add(slider, c);
    }*/

    void newMSlider(int y, int x, FloatTextField slider,  JPanel panel) {
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = x;
    	c.gridy = y;

    	slider.addObserver(this);
        panel.add(slider, c);

    }

    /*this will get called by SOCRApplicaitons load file*/
    public void loadSlider(double[] r1, double[] c1, double[] m1 ){

	   r = new double[r1.length];
	   c = new double[c1.length];
	   m = new double[m1.length];

	   for (int i=0; i<r1.length; i++)
		   r[i]=r1[i];
	   for (int i=0; i<c1.length; i++)
		   c[i]=c1[i];
	   for (int i=0; i<m1.length; i++)
		   m[i]=m1[i];

	   initSliders();
	   setSliders();
   }

    private void initSliders(){
    	ExpectedReturn = new double[numStocks][1];

    	//double[] r={0.14,0.08,0.20, 0.01, 0.0134};

    	COVR= new double[numStocks][numStocks];  // COVARIANCE matrix
    	CORR= new double[numStocks][numStocks];  //CORRELATION matrix

    	for (int i =0; i<numStocks; i++){
    		ExpectedReturn[i][0]=r[i];
    		COVR[i][i]=c[i];
    	}

    	int k=numStocks;
    	for (int i=1; i<numStocks; i++){
			for (int j=0; j<i; j++){
				COVR[i][j]= m[k-numStocks];
				k++;
			}
    	}

    	fillMatrix(COVR);

    }

    private void setSliders(){
    	if (sliderPanel!=null)
    		sliderPanel.removeAll();
    	else sliderPanel = new JPanel();

        JPanel rPanel = new JPanel();
       // rPanel.setBackground(Color.blue);
        JPanel rPanel2 = new JPanel();
       // rPanel2.setBackground(Color.green);
        rPanel.setAlignmentX(LEFT_ALIGNMENT);
    	rPanel2.setAlignmentX(LEFT_ALIGNMENT);


    	rSliders = new FloatTextField[numStocks]; //expected return

    	JPanel matrixPanel =  new JPanel();
    	matrixPanel.setAlignmentX(LEFT_ALIGNMENT);
    	mSliders = new FloatTextField[15];

    	JPanel tPanel = new JPanel();
    	tPanel.setAlignmentX(LEFT_ALIGNMENT);
    //	tPanel.setBackground(Color.blue);


    	//tSlider = new ValueSliderFloat2("Tangency:", -0.5, 0.5, false);
    	tSlider = new FloatTextField("Tangency:", t1_y, 0, 1);
    	//tSlider.setPreferredSize(new Dimension(SLIDER_SIZE_X,45));

    	newMSlider(1, 0, tSlider,tPanel);

    	//expected return
    	rPanel.setLayout(new GridBagLayout());
    	rPanel.add(new JLabel("Expected Return:"));
    	for (int i =0; i<numStocks; i++){
    		rSliders[i] = new FloatTextField("E(R"+(i+1)+")", ExpectedReturn[i][0], -0.2, 0.2);
    		rSliders[i].setPreferredSize(new Dimension(SLIDER_SIZE_X/numStocks,45));
    		rSliders[i].setToolTipText("Adjusting the value of expected return for stock "+(i+1)+".");
    		newMSlider(1,i,rSliders[i],rPanel);
    	}

    	ER_matrix = new Matrix(numStocks,1, ExpectedReturn);

    	// Covariance
    	rPanel2.setLayout(new GridBagLayout());
    	rPanel2.add(new JLabel("Variance:"));
    	for (int i =0; i<numStocks; i++){
    		mSliders[i] = new FloatTextField("VAR(R"+(i+1)+")", COVR[i][i], 0.0, 0.2);
    		mSliders[i].setPreferredSize(new Dimension(SLIDER_SIZE_X/numStocks,45));
    		mSliders[i].setToolTipText("Adjusting the value of variance "+(i+1)+".");
    		newMSlider(1,i,mSliders[i],rPanel2);
    	}

    	matrixPanel.setLayout(new GridBagLayout());


    	if (covarianceFlag)
    		addRadioButton(0, 0, "Switch Input Matrix:", "Switch Input Matrix", switchArray, 0, matrixPanel, (ActionListener) this);
    	else
    		addRadioButton(0, 0, "Switch Input Matrix:", "Switch Input Matrix", switchArray, 1, matrixPanel, (ActionListener) this);
    	switchedFlag=false;


    	if (covarianceFlag){
    		// matrixPanel.add(new JLabel("Covariance Matrix:"));

    		int k=numStocks;
    		for (int i=1; i<numStocks; i++)
    			for (int j=0; j<i; j++){
    				mSliders[k] = new FloatTextField("COV"+(i+1)+(j+1), COVR[i][j], -0.5, 0.5);
    				mSliders[k].setPreferredSize(new Dimension(SLIDER_SIZE_X/numStocks,45));
    				mSliders[k].setToolTipText("Adjusting the value of correlation "+(i+1)+","+(j+1)+".");
    				newMSlider(i,j,mSliders[k],  matrixPanel);

    				k++;
    			}
    		}
    	else{
        	//	matrixPanel.add(new JLabel("Correlation Matrix:"));
        		int k=numStocks;
        		for (int i=1; i<numStocks; i++)
        			for (int j=0; j<i; j++){
        				mSliders[k] = new FloatTextField("CORR"+(i+1)+(j+1), CORR[i][j], -0.5, 0.5);
        				mSliders[k].setPreferredSize(new Dimension(SLIDER_SIZE_X/numStocks,45));
        				mSliders[k].setToolTipText("Adjusting the value of correlation "+(i+1)+","+(j+1)+".");
        				newMSlider(i,j,mSliders[k], matrixPanel);
        				if (CORR[i][j]>1)
        					System.out.println("setting slider CORR["+i+"]["+j+"]="+CORR[i][j]);

        				k++;
        			}
    	}

    	try{
    		CORR = covr2corr(COVR, ExpectedReturn, numStocks);
    	}catch(Exception e){
    		System.out.println("corrlation out of range.");
    	}

    	covr_matrix = new Matrix(numStocks, numStocks, COVR);
    	corr_matrix = new Matrix(numStocks, numStocks, CORR);

    	sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
    	/*Box box = Box.createVerticalBox();
    	box.add(rPanel);
    	box.add(rPanel2);
    	box.add(new JLabel("Correlation Matrix:"));
    	box.add(matrixPanel);
    	sliderPanel.add(box);*/
    	//sliderPanel.setBackground(Color.red);
    	matrixPanel.validate();
    	sliderPanel.add(rPanel);
    	sliderPanel.add(rPanel2);
    	sliderPanel.add(Box.createRigidArea(new Dimension(0,5)));
    	sliderPanel.add(matrixPanel);
    	sliderPanel.add(tPanel);
    	sliderPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    	sliderPanel.validate();
    	//sliderPanel.setBackground(Color.white);
    }

    /**
     * This method is the default update method and defines how the display is
     * updated. get called when slider value changes
     */
    public void update(Observable arg0, Object arg1) {

    	//System.out.println("update(arg0, arg1 )get called, slider changed");
    //	printMatrix(COVR, "before COVR");
    //	printMatrix(CORR, "before CORR");

    	if (tSlider.getFloatValue()!=t1_y){
    		t1_y=tSlider.getFloatValue();

    		setChart();
    		return;
    	}

    	for (int i=0; i<numStocks; i++){

    		//
    		if (ExpectedReturn[i][0]!=rSliders[i].getFloatValue()){

    		//	System.out.println("ER() slider changed i="+i+" Expected Return[i]="+ExpectedReturn[i]+" rslider[i]="+rSliders[i].getValue());

    			ExpectedReturn[i][0]=rSliders[i].getFloatValue();
    			ER_matrix = new Matrix(numStocks,1, ExpectedReturn);
    			setChart();
    			return;
    		}
    	}

    	double[][] tmpcovr = new double[numStocks][numStocks];
    	double[][] tmpcorr = new double[numStocks][numStocks];

    	for (int i=0; i<numStocks; i++){

    		if (COVR[i][i]!=mSliders[i].getFloatValue()){
    			if (mSliders[i].getFloatValue()==0.0)  // variance can't be 0
    				return;
    		//	System.out.println("Variance changed COVR["+i+"]["+i+"]"+COVR[i][i]+" <> mSlider["+i+"]="+mSliders[i].getValue());
    			// save them first
    			double tmps=COVR[i][i];
    			for (int r=0; r<numStocks; r++)
    				for (int c=0; c<numStocks; c++){
    					tmpcovr[r][c]=COVR[r][c];
    					tmpcorr[r][c]=CORR[r][c];
    				}


    			try{
    				tmps=mSliders[i].getFloatValue();
    				tmpcovr[i][i]=tmps;
    				tmpcorr = covr2corr(tmpcovr, ExpectedReturn, numStocks);
    			}catch(Exception e){
    				e.printStackTrace();
    				System.out.println("slider out of range: Variance changed back to  mSlider["+i+"]="+COVR[i][i]);
    				mSliders[i].setFloatValue(COVR[i][i]);
    				return;
    			}

    			COVR[i][i] = tmps;
    			for (int r=0; r<numStocks; r++)
    				for (int c=0; c<numStocks; c++){
    					COVR[r][c]=tmpcovr[r][c];
    					CORR[r][c]=tmpcorr[r][c];
    				}

    	    	corr_matrix = new Matrix(numStocks, numStocks, CORR);
    	    	covr_matrix = new Matrix(numStocks, numStocks, COVR);
    			setChart();
				return;
    		}
    	}//end of variance changes

    	//System.out.println("numStocks="+numStocks);
    	int k=numStocks;
    	if (covarianceFlag){
    		for (int i=1; i<numStocks; i++){
    			for (int j=0; j<i; j++){
    			//	System.out.println("i="+i+", j="+j +" k="+k+ ", Corr["+i+"]["+j+"]="+CORR[i][j]+", mslider["+k+"]="+mSliders[k].getValue());
    				if (COVR[i][j]!=mSliders[k].getFloatValue()){
    					// save them first
    					double tmps=COVR[i][j];
    	    			for (int r=0; r<numStocks; r++)
    	    				for (int c=0; c<numStocks; c++){
    	    					tmpcovr[r][c]=COVR[r][c];
    	    					tmpcorr[r][c]=CORR[r][c];
    	    				}

    					try{
    					tmps=mSliders[k].getFloatValue();
    					tmpcovr[i][j]=tmps;
    				//	System.out.println("changed Covr["+i+","+j+"] set covr["+i+","+j+"]="+COVR[i][j]);

    					tmpcorr = covr2corr(tmpcovr, ExpectedReturn, numStocks);
    					fillMatrix(tmpcovr);


    					}catch(Exception e){
    						e.printStackTrace();
    						System.out.println("slider out of range: return Covr["+i+","+j+"] set covr["+i+","+j+"] to "+COVR[i][j]);
    						mSliders[k].setFloatValue(COVR[i][j]);
    						//fillMatrix(COVR);
    						return;
    					}
    					COVR[i][j] = tmps;
            			for (int r=0; r<numStocks; r++)
            				for (int c=0; c<numStocks; c++){
            					CORR[r][c]=tmpcorr[r][c];
            					COVR[r][c]=tmpcovr[r][c];
            				}

            			//System.out.println("changed Covr["+i+","+j+"] set covr["+i+","+j+"]="+COVR[i][j]);
    					corr_matrix = new Matrix(numStocks, numStocks, CORR);
    					covr_matrix = new Matrix(numStocks, numStocks, COVR);
    					setChart();
    					return;
    				}
    				k++;
    			}}
    	}//covarianceFlag
    	else{  // correlation matrix
    	for (int i=1; i<numStocks; i++){
    		for (int j=0; j<i; j++){
    		//	System.out.println("i="+i+", j="+j +" k="+k+ ", Corr["+i+"]["+j+"]="+CORR[i][j]+", mslider["+k+"]="+mSliders[k].getValue());
    			double value = mSliders[k].getFloatValue();
    			if (CORR[i][j]!= value  && value<=1 && value>=-1){

//    				 save them first

					double tmps=CORR[i][j];
	    			for (int r=0; r<numStocks; r++)
	    				for (int c=0; c<numStocks; c++){
	    					tmpcorr[r][c]=CORR[r][c];
	    					tmpcovr[r][c]=COVR[r][c];
	    				}

	    			try{
	    				tmps=mSliders[k].getFloatValue();
	    				tmpcorr[i][j]=tmps;

	    				fillMatrix(tmpcorr);
	    				tmpcovr = corr2covr(tmpcorr, ExpectedReturn, numStocks);


	    			}catch(Exception e){
	    				e.printStackTrace();
	    				System.out.println("slider out of range: return Corr["+i+","+j+"] set corr["+i+","+j+"] to "+CORR[i][j]);
	    				mSliders[k].setFloatValue(CORR[i][j]);
	    				return;
	    			}
	    			CORR[i][j]=tmps;
	    			for (int r=0; r<numStocks; r++)
        				for (int c=0; c<numStocks; c++){
        					CORR[r][c]=tmpcorr[r][c];
        					COVR[r][c]=tmpcovr[r][c];
        				}
	    	//		System.out.println("changed Corr["+i+","+j+"] set corr["+i+","+j+"]="+CORR[i][j]);
    				corr_matrix = new Matrix(numStocks, numStocks, CORR);
        	    	covr_matrix = new Matrix(numStocks, numStocks, COVR);
    				setChart();
    				return;
    			}
    		k++;
    		}
    	}
    	}//covarianceFlag==false


    }

    double[][] covr2corr(double[][] covr, double[][] r, int dim) throws Exception {
    	double[][] tmp = new double[dim][dim];

    //	System.out.println("covr2corr dim="+dim);
    	double[] sqrtR = new double[dim];
    	for (int i=0; i<dim; i++){
    	//	System.out.print("variance["+i+"]="+COVR[i][i]);
    		sqrtR[i]=Math.sqrt(COVR[i][i]);
    	//	System.out.println(" sqrtcovR["+i+"]="+sqrtR[i]);
    	}

    	for (int i=0; i<dim; i++)
    		for (int j=0; j<dim; j++)
    			if (i==j){
    				tmp[i][j]=1;
    				//System.out.println("covr["+i+"]["+j+"]="+covr[i][j]+" sqrtR[i]="+ sqrtR[i] +" sqrtR[j]="+sqrtR[j]+" corr[i][j]="+tmp[i][j]);
    			}
    			else {
    			//	System.out.println("covr["+i+"]["+j+"]="+covr[i][j]+" sqrtR[i]="+ sqrtR[i] +" sqrtR[j]="+sqrtR[j]);
    			//	System.out.println("corr["+i+"]["+j+"]="+covr[i][j]/(sqrtR[i]*sqrtR[j]));
    				tmp[i][j]=Double.parseDouble(formatter.format(covr[i][j]/(sqrtR[i]*sqrtR[j])));
    				if (tmp[i][j]>1 || tmp[i][j]<-1)
    					throw new Exception();
    			//	System.out.println("covr["+i+"]["+j+"]="+covr[i][j]+" sqrtR[i]="+ sqrtR[i] +" sqrtR[j]="+sqrtR[j]+" corr[i][j]="+tmp[i][j]);
    			}
    //	System.out.println("end covr2corr");
    	return tmp;
    }

    double[][] corr2covr(double[][] corr, double[][] r, int dim){
    //	System.out.println("corr2covr");
    	double[][] tmp = new double[dim][dim];
    	double[] sqrtR = new double[dim];
    	for (int i=0; i<dim; i++){
    		sqrtR[i]=Math.sqrt(COVR[i][i]);
    }

    	for (int i=0; i<dim; i++)
    		for (int j=0; j<dim; j++)
    			if (i==j){
    				tmp[i][j]=COVR[i][j]; // don't change variance value
    			//	System.out.println("corr["+i+"]["+j+"]="+corr[i][j]+" sqrtR[i]="+ sqrtR[i] +" sqrtR[j]="+sqrtR[j]+" covr[i][j]="+tmp[i][j]);
    			}
    			else {
    			//	System.out.println("corr["+i+"]["+j+"]="+corr[i][j]+" sqrtR[i]="+ sqrtR[i] +" sqrtR[j]="+sqrtR[j]);
    			//	System.out.println("covr["+i+"]["+j+"]="+corr[i][j]*sqrtR[i]*sqrtR[j]);
    				tmp[i][j]=Double.parseDouble(formatter.format(corr[i][j]*sqrtR[i]*sqrtR[j]));
    			//	System.out.println("corr["+i+"]["+j+"]="+corr[i][j]+" sqrtR[i]="+ sqrtR[i] +" sqrtR[j]="+sqrtR[j]+" covr[i][j]="+tmp[i][j]);
    			}
    	//System.out.println("end corr2covr");
    	return tmp;
    }

    protected void fillMatrix(double[][] m){
    	for (int i=0; i<numStocks; i++)
    		for (int j=i+1; j<numStocks; j++){
    			m[i][j]=m[j][i];
    		}
    }

    void printMatrix(double[][] m, String name){
    	System.out.println("---Print "+name+"-------");
    	for (int i=0; i<numStocks; i++){
    		for (int j=0; j<numStocks; j++)
    			System.out.print(name+i+","+j+"="+m[i][j]+"   ");
    		System.out.println("\n");
    	}
    	System.out.println("----------");
    }

    public void mouseClicked(MouseEvent event){}

    public String getOnlineDescription(){ return new String("http://socr.stat.ucla.edu/");};

	// Needs to be overwritten by objects that extend this class
    public String getAppletInfo(){return new String ("SOCR Experiments: http://www.socr.ucla.edu \n");}

	public Container getDisplayPane() {
		 JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
	                getMainPanel(), getTextPanel() );
	        return container;
	}

	public void update() {
		setChart();
	};

	public void setTangent(boolean t){
		show_tangent = t;
		setChart();
	}

	void computeTangent() {
	//	System.out.println("calling computeTangent");
	//	Matrix.print(covr_matrix);
		 Matrix z1 = Matrix.inverse(covr_matrix);
	//Matrix.print(z1);

		 double[][] r = new double[numStocks][1];

		 for (int i=0; i<numStocks; i++){
			// System.out.println("r-0.05="+ExpectedReturn[i][0]+"-0.05");
			 r[i][0] = ( ExpectedReturn[i][0] - t1_y);
		 }


		 Matrix zr = new Matrix(numStocks, 1, r);
//Matrix.print(zr);

		 Matrix z = Matrix.multiply(z1, zr);
//Matrix.print(z);

		 double zSum=0;
		 double[] z0=z.getColumn(0);
		 for (int i=0; i<numStocks; i++)
			 zSum += z0[i];

	//	 System.out.println("zsum="+zSum);

		double[][] x = new double[numStocks][1];
		for (int i=0; i<numStocks; i++)
		 x[i][0]= z0[i]/zSum;

		Matrix mx= new Matrix(numStocks,1, x);
		Matrix mx1= mx.transpose();

		Matrix my=Matrix.multiply(mx1,ER_matrix);
		t2_y= Double.parseDouble(my.toString());

		Matrix mx2= Matrix.multiply(mx1,covr_matrix);
		Matrix mx3= Matrix.multiply(mx2,mx);
		t2_x=Math.sqrt(Double.parseDouble(mx3.toString()));

		System.out.println("t1_x="+t1_x+" t1_y="+t1_y);
		System.out.println("t2_x="+t2_x+" t2_y="+t2_y);
		double delta = (t2_y-t1_y)/(t2_x-t1_x);
		t2_x= t2_x*2;
		t2_y= t1_y+t2_x*delta;


		// System.out.println("computeTangent: t1_x="+t1_x+ " t1_y="+t1_y+" t2_x="+t2_x+" t2_y="+t2_y);
		// System.out.println("computeTangent done");
	 }

	void computeChartDataPoints() {
		 double point_x, point_y;
		 Matrix x1, x2;
		 chartDataPoints = new ChartDataPoints(numSimulate);
		//

	//	System.out.println("computeChartDataPoints get called numSimulate="+numSimulate);
		 for (int i=0; i<numSimulate; i++){

			 x1= new Matrix(1, numStocks, simulatedPoints.getRow(i));

			 x2= x1.transpose();

			Matrix x3 = Matrix.multiply(x1,covr_matrix);

			Matrix x4 = Matrix.multiply(x3,x2);

			point_x=Math.sqrt(Double.parseDouble(x4.toString()));

			Matrix x5 = Matrix.multiply(x1, ER_matrix);

			point_y= Double.parseDouble(x5.toString());

			if (point_x!=0 && point_x<=t2_x && point_y <=t2_y)
				chartDataPoints.addPoint(point_x, point_y, i);
			//System.out.println("chart point " +(i+1)+" is ("+point_x+", "+point_y+")");
		}
	 }

    class ChartDataPoints {
    	double x[]; double y[];
    	int pointCount;
    	int[] simulatedDP_pointer;

    	ChartDataPoints(int numSimulate){
    		x = new double[numSimulate];
    		y = new double[numSimulate];
    		simulatedDP_pointer = new int[numSimulate];
    		pointCount=0;
    	}
    	public void addPoint(double x, double y, int sdp_pointer){
    		this.x[pointCount] = x;
    		this.y[pointCount] = y;
    		this.simulatedDP_pointer[pointCount] = sdp_pointer;
    		pointCount++;
    	}

    	public double getX(int pointer){
    		return x[pointer];
    	}
    	public double getY(int pointer){
    		return y[pointer];
    	}

    	public int getSDPpointer(int pointer){
    		return simulatedDP_pointer[pointer];
    	}
    	public int getPointCount(){
	    	return pointCount;
	    }
    }

    class SimulatedDataPoints {
        double x[][];
        int numStocks;
        int pointCount;

        SimulatedDataPoints(int numStocks, int numSimulate) {
        	x=new double[numStocks][numSimulate];
        	pointCount=0;
        	this.numStocks= numStocks;
        }

        public void addPoint(double[] xValues){
        	double sum=0;
        	for (int i=0; i<numStocks; i++)
        		sum +=xValues[i];

        	if(sum>0.1 ||sum<-0.1){
        		for (int i=0; i<numStocks; i++)
        		x[i][pointCount]=xValues[i]/sum;
        	}

        	pointCount++;
        }

        public double[][] getRow(int pointer){
        	double[][] x1= new double[1][numStocks];
        	 for (int i=0; i<numStocks; i++)
        		 x1[0][i]=x[i][pointer];

        	return x1;
        }

        public double[][] getColumn(int pointer){
        	double[][] x1= new double[numStocks][1];
        	 for (int i=0; i<numStocks; i++)
        		 x1[i][0]=x[i][pointer];

        	return x1;
        }

        public int getPointCount(){
        	return pointCount;
        }
    }

	public void chartMouseClicked(ChartMouseEvent arg0) {
		//System.out.println(arg0.getEntity().getShapeCoords());
		if(arg0.getEntity()==null)
			return;

		tooltip = arg0.getEntity().getToolTipText();

		mouse_x = Double.parseDouble(tooltip.substring(tooltip.indexOf("(")+1, tooltip.indexOf(",")));
		mouse_y = Double.parseDouble(tooltip.substring(tooltip.indexOf(",")+1, tooltip.indexOf(")")));

		tooltip = "("+mouse_x+","+mouse_y+")\n";
		for (int i=0; i<chartDataPoints.pointCount; i++){
			double x =Double.parseDouble(tooltip_formatter.format(chartDataPoints.getX(i)));
			double y =Double.parseDouble(tooltip_formatter.format(chartDataPoints.getY(i)));
			//System.out.println(i+":"+x+","+y);
			if ( x==mouse_x && y==mouse_y ){
				int sdpId= chartDataPoints.getSDPpointer(i);
				tooltip += "==> Stocks(";
				double[][] sdp= new double[1][numStocks];
				sdp = simulatedPoints.getRow(sdpId);

				for (int j=0; j<numStocks-1; j++)
					 tooltip+=tooltip_formatter.format(sdp[0][j])+", ";
				tooltip+=tooltip_formatter.format(sdp[0][numStocks-1])+")\n";
			}

		}

		mouseClicked = true;
		updateGraph();
	//	setChart();
		//System.out.println("------");
	}

	// switch between covraiance and correlation matrix
	public void actionPerformed(ActionEvent evt) {
		for (int i=0; i<2; i++)
    		if (evt.getActionCommand().equals(switchArray[i])) {
    			switchedFlag= true;
    			covarianceFlag= !covarianceFlag;
    			setSliders();
    	}
		for (int i=0; i<numStocksArray.length; i++)
    		if (evt.getActionCommand().equals(numStocksArray[i])) {
    			setNumberStocks(numStocksArray[i]);
    	}

    	for (int i=0; i<on_off.length; i++)
    		if (evt.getActionCommand().equals(on_off[i])) {
    			if (i==0)
    				setTangent(true);
    			else setTangent(false);
    	}
	}

	public void chartMouseMoved(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}
