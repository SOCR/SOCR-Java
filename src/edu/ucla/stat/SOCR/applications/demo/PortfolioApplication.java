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

import edu.ucla.stat.SOCR.applications.Application;
import edu.ucla.stat.SOCR.core.IExperiment;
import edu.ucla.stat.SOCR.util.FloatSlider;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * The triangle experiment is to break a stick at random and see if the pieces
 * form a triangle. If so, is the triangle acute or obtuse?
 */
public class PortfolioApplication extends Application  implements Observer, IExperiment, ChartMouseListener{

	private static final float LEFT_ALIGNMENT = 0;
	protected final int CHART_SIZE_X = 500;
    /**
	 * @uml.property  name="cHART_SIZE_Y"
	 */
    protected final int CHART_SIZE_Y = 400;
    protected ChartPanel chartPanel;

	 // statistical input variables
    protected double[] R;
    protected double[][] CORR;

    private DecimalFormat formatter = new DecimalFormat("#0.0####");
    private DecimalFormat tooltip_formatter = new DecimalFormat("#0.0#");

    boolean mouseClicked= false;
    String tooltip;
   /* protected double CORRab;
    protected double CORRac;
    protected double CORRbc;
    protected double CORRad;
    protected double CORRbd;
    protected double CORRcd;
    protected double CORRae;
    protected double CORRbe;
    protected double CORRce;
    protected double CORRde;*/


    // number of stock option combinations
    protected int numOptions;

    // step (granunarity) of x
    protected double step_x;

    // lower bound of x
    protected double min_x;

    // upper bound of x
    protected double max_x;

    // error bound of x
    protected double error_bound_x;

    protected double mouse_x;
    protected double mouse_y;

    // temp variable for stock option enumeration
    protected double tmp_x[];

    // lookup table for options by numStocks stocks
    protected LinkedList<double[]> stockOptions;
    LinkedList<DataPoint> dataPoints;
    // data points
    DataPoint minPoint;
    DataPoint mousePoint;

    private int time = 0, stopFreq = 10;

   // int numStocks = 5;

    JToolBar toolBar;
    JPanel sliderPanel;

    FloatSlider[]  rSliders, mSliders;

	XYSeries e_serie;
	XYSeries b_serie;
	XYSeries s_serie;
	XYSeries m_serie;

	 private JButton stepJButton = new JButton("Step"), runJButton = new JButton(
     "Run"), stopJButton = new JButton("Stop");


    /**
     * This method initializes the experiment, including the toolbar, triangle,
     * scatterplot, random variable graph and table
     */
    public PortfolioApplication() {

    	setName("PortfolioExperiment");
    	 initSliders();

     	dataPoints = new LinkedList<DataPoint>();

     	updateGraph();
     	addGraph(chartPanel);
     	addToolbar(sliderPanel);
     	emptyTool();
     	emptyTool2();

     	e_serie = new XYSeries("equal", false);
		b_serie = new XYSeries("bigger", false);
		s_serie = new XYSeries("smaller", false);
		m_serie = new XYSeries("mouse", false);

    }

    void initGraph(){

		JFreeChart chart = createEmptyChart(null);	//create a empty graph first
		chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

    }

    void updateGraph(){
    	//System.out.println("UpdateGraph get called");
    	setupStockOptions();
    	computeDataPoints();

    		e_serie = new XYSeries("equal", false);
    		b_serie = new XYSeries("bigger", false);
    		s_serie = new XYSeries("smaller", false);
    		m_serie = new XYSeries("mouse", false);


    	StringBuffer text= new StringBuffer();
    	text.append("mouse clicked at "+tooltip+"\n");
    	text.append("The stock combinations are:");

    	 Iterator i = dataPoints.iterator();
    	  while ( i.hasNext() ) {
    		  DataPoint dp = (DataPoint) i.next();

    			  if ( dp == minPoint ){
    				  e_serie.add(dp.std, dp.mean);
    			  }
    			  else if ( dp.mean < minPoint.mean )
    				  s_serie.add(dp.std, dp.mean);
    			  else
    				  b_serie.add(dp.std, dp.mean);

    			  if (mouseClicked) {
    				  if ( Double.parseDouble(tooltip_formatter.format(dp.std))==Double.parseDouble(tooltip_formatter.format(mouse_x)) &&
    						  Double.parseDouble(tooltip_formatter.format(dp.mean))==Double.parseDouble(tooltip_formatter.format(mouse_y)) ){

    					  for (int j=0; j<numStocks; j++)
    						  text.append("Stock "+(j+1)+": x["+(j+1)+"]="+tooltip_formatter.format(dp.x[j])+" ,");

    					  text.append("\n---\n");
    				  }
    			  }


    	  }
    	  if (!mouseClicked){
      		mouse_x=minPoint.std;
      		mouse_y=minPoint.mean;
    	  }
    	  m_serie.add(mouse_x,mouse_y);

    	  XYSeriesCollection ds = new XYSeriesCollection();
    	  ds.addSeries(e_serie);
    	  ds.addSeries(m_serie);

    	  ds.addSeries(b_serie);
    	  ds.addSeries(s_serie);


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

    	  renderer1.setSeriesPaint(1, Color.red);
    	  renderer1.setSeriesPaint(3, Color.blue);
    	  renderer1.setSeriesPaint(2, Color.blue); //grey
    	  renderer1.setSeriesPaint(0, Color.blue); //green
    	  Shape shape = renderer1.getBaseShape();
    	  renderer1.setSeriesShape(2, shape);
    	  renderer1.setSeriesShape(3, shape);
    	  renderer1.setBaseLinesVisible(false);
    	  renderer1.setBaseShapesVisible(true);
    	  renderer1.setBaseShapesFilled(true);

    	  chartPanel = new ChartPanel(chart, false);
    	  chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
    	  chartPanel.addChartMouseListener(this);
    	  super.updateGraph(chartPanel);
    	 //

    	  if(mouseClicked){

			getRecordTable().setText(text.toString());}
    	  else {
    		  text = new StringBuffer();
    		  text.append("("+tooltip_formatter.format(minPoint.mean)+" , "+ tooltip_formatter.format(minPoint.std)+")\n");
    		  for (int j=0; j<numStocks; j++)
  				text.append("Stock "+(j+1)+": x["+(j+1)+"]="+tooltip_formatter.format(minPoint.x[j])+",");
  			text.append("\n---\n");
    		  getRecordTable().setText(text.toString());;
    	  }

    	 mouseClicked= false;
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

    void newMSlider(int y, int x, FloatSlider slider, double v, JPanel panel) {
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = x;
    	c.gridy = y;

    	slider.setFloatValue(v);
    	slider.addObserver(this);
        panel.add(slider, c);
    }

    private void initSliders(){
    	sliderPanel = new JPanel();

        JPanel rPanel = new JPanel();
        JPanel rPanel2 = new JPanel();
    	rSliders = new FloatSlider[numStocks];
    	R = new double[numStocks];
    	double[] r={0.0064, 0.0022, 0.02117, 0.01, 0.0134};

    	JPanel matrixPanel =  new JPanel();
    	mSliders = new FloatSlider[15];
    	CORR= new double[numStocks][numStocks];
    	double[] m={0.0101, 0.0045, 0.0122, 0.0041, 0.0026, 0.0119,0.0012, 0.0011, 0.0015, 0.0141, 0.0043, 0.0022, 0.0058, 0.005, 0.0144};

    	rPanel.setLayout(new GridBagLayout());
    	rPanel.add(new JLabel("Expected Return:"));
    	for (int i =0; i<numStocks; i++){
    		R[i]=r[i];
    		rSliders[i] = new edu.ucla.stat.SOCR.util.FloatSlider("E(R"+(i+1)+")", -0.1, 0.1, r[i], false);
    		rSliders[i].setPreferredSize(new Dimension(CHART_SIZE_X/3,80));
    		rSliders[i].setToolTipText("Slider for adjusting the value of expected return for stock "+(i+1)+".");
    		newMSlider(1,i,rSliders[i],r[i],rPanel);

    	}

    	rPanel2.setLayout(new GridBagLayout());
    	rPanel2.add(new JLabel("Covariance:"));
    	for (int i =0; i<numStocks; i++){
    		R[i]=r[i];
    		mSliders[i] = new edu.ucla.stat.SOCR.util.FloatSlider("VAR(R"+(i+1)+")", 0, 0.5);
    		mSliders[i].setPreferredSize(new Dimension(CHART_SIZE_X/3,80));
    		mSliders[i].setToolTipText("Slider for adjusting the value of covariance "+(i+1)+".");
    		newMSlider(1,i,mSliders[i],m[i],rPanel2);
    	}

    	matrixPanel.setLayout(new GridBagLayout());
    	matrixPanel.add(new JLabel("Correlation Matrix:"));
    	int k=0;
    	for (int i=0; i<numStocks; i++)
    		for (int j=0; j<=i; j++){
    			CORR[i][j]= m[k];
    			if (i==j){
    				mSliders[k] = new edu.ucla.stat.SOCR.util.FloatSlider("VAR(R"+(i+1)+")", 0, 0.5);
    			//	newMSlider(i,j,mSliders[k],m[k],matrixPanel);
    		}
    			else {
    				mSliders[k] = new edu.ucla.stat.SOCR.util.FloatSlider("COV"+(j+1)+(i+1), 0, 0.5);
    				mSliders[k].setPreferredSize(new Dimension(CHART_SIZE_X*5/12,80));
    				mSliders[k].setToolTipText("Slider for adjusting the value of covariance matrix ("+(j+1)+","+(i+1)+").");
    				newMSlider(i,j,mSliders[k], m[k],matrixPanel);
    			}
    			k++;
    		}

    	sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
    	/*Box box = Box.createVerticalBox();
    	box.add(rPanel);
    	box.add(rPanel2);
    	box.add(new JLabel("Correlation Matrix:"));
    	box.add(matrixPanel);
    	sliderPanel.add(box);*/
    	rPanel.setAlignmentX(LEFT_ALIGNMENT);
    	rPanel2.setAlignmentX(LEFT_ALIGNMENT);
    	matrixPanel.setAlignmentX(LEFT_ALIGNMENT);
    	sliderPanel.add(rPanel);
    	sliderPanel.add(rPanel2);
    	sliderPanel.add(Box.createRigidArea(new Dimension(0,5)));
    	sliderPanel.add(matrixPanel);
    	sliderPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    	//sliderPanel.setBackground(Color.white);
    }


    public int getTime(){return time;};

    /** This method stops the simulation thread */
    public void stop(){};



    /**
     * This method is the default step method, that runs the process one time
     * unit. This method can be overridden to add sound or other elements
     */
    public void step(){};

    /**
     * This method is the default reset method, that resets the process to its
     * initial state. This method should be overridden.
     */
    public void reset(){};

    /**
     * This method is the default update method and defines how the display is
     * updated. This method should be overridden.
     */
    public void update(Observable arg0, Object arg1) {

    	for (int i=0; i<numStocks; i++){

    		//System.out.println("i="+i+" R[i]="+R[i]+" rslider[i]="+rSliders[i].getValue());

    		if (R[i]!=rSliders[i].getFloatValue()){
    			R[i]=rSliders[i].getFloatValue();
    			updateGraph();
    			return;
    		}
    	}

    	int k=0;
    	for (int i=0; i<numStocks; i++){
    		for (int j=0; j<=i; j++){
    			System.out.println("i="+i+", j="+j +" k="+k+ ", Corr["+i+"]["+j+"]="+CORR[i][j]+", mslider["+k+"]="+mSliders[k].getFloatValue());
    			if (CORR[i][j]!=mSliders[k].getFloatValue()){
    				CORR[i][j]=mSliders[k].getFloatValue();
    				updateGraph();
    				return;
    			}
    			k++;
    		}
    	}

    }
    public void mouseClicked(MouseEvent event){}

    /**
     * This method defines the boolean variable that stops the process, when the
     * simulation is in run mode
     */
    public void setStopNow(boolean b){};

    /** This method returns the stop frequency */
    public int getStopFreq(){ return stopFreq;};

    /** This method sets the stop frequency */
    public void setStopFreq(int i){};

    public String getOnlineDescription(){ return new String("http://socr.stat.ucla.edu/");};


	// Needs to be overwritten by objects that extend this class
    public String getAppletInfo(){return new String ("SOCR Experiments: http://www.socr.ucla.edu \n");}


	public void doExperiment() {
		// TODO Auto-generated method stub

	}


	public Container getDisplayPane() {
		 JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
	                getMainPanel(), getTextPanel() );
	        return container;
	}


	public void update() {
		// TODO Auto-generated method stub
		updateGraph();
	};

	 void computeDataPoints() {
		    minPoint = null;
		    dataPoints = new LinkedList<DataPoint>();
		    Iterator i = stockOptions.iterator();

		   // double sqrtVARb = Math.sqrt(VARb);

		 //    MAX_X_VALUE = 0.1;
		 //    MAX_Y_VALUE = 0.02;

		    while (i.hasNext()){
		    	double[] oneStockOption = (double[])i.next();

		        DataPoint dp = new DataPoint(oneStockOption);
		       // System.out.println(dp.x[0]+","+dp.x[1]+","+dp.x[2]+","+dp.x[3]+","+dp.x[4]);
		        for(int j=0;j<numStocks;j++){
		            dp.mean += dp.x[j]*R[j];
		            for(int k=0;k<numStocks;k++){
		                dp.std += dp.x[j]*dp.x[k]*CORR[j][k];
		            }
		        }

		        try{
		        dp.std = Double.parseDouble(formatter.format(Math.sqrt(dp.std)));
		        dp.mean =Double.parseDouble(formatter.format(dp.mean));
		        }catch(Exception e){
		        	System.out.println("Excecption catched: sqrt. dp.std=" +Math.sqrt(dp.std) + "dp.std="+dp.std);
			        System.out.println("dp.mean="+dp.mean);
		        }
		      /*  dp.windowX = d.windowX(d.convertX(dp.std));
		        dp.windowY = d.windowY(d.convertY(dp.mean));*/

		   /*     if (dp.std > MAX_X_VALUE)
		        	MAX_X_VALUE = dp.std;
		        if (dp.mean > MAX_Y_VALUE)
		        	MAX_Y_VALUE = dp.mean;*/

		        // record minimum
		        if ( minPoint == null || dp.std < minPoint.std)
		            minPoint = dp;

		        dataPoints.add(dp);
		    }

		 /*   MAX_X_VALUE = MAX_X_VALUE*1.1;
		    MAX_Y_VALUE = MAX_Y_VALUE*1.1;
		    X_TICK= MAX_X_VALUE/10;
		    Y_TICK = MAX_Y_VALUE/10;

		    Iterator j = dataPoints.iterator();
		    while (j.hasNext()) {
		        DataPoint dp = (DataPoint)j.next();
		        dp.windowX = d.windowX(d.convertX(dp.std));
		        dp.windowY = d.windowY(d.convertY(dp.mean));
		        }*/

		}

	  public void setupStockOptionsRec(int id, double share) {

	        //System.out.println("id="+id+",share="+share);

	        if(id == 1){
	            tmp_x[0] = (Math.abs(share) < error_bound_x)?0.0:share;
	            double xArray [] = new double[numStocks];
	            for(int i=0;i<numStocks;i++){
	                xArray[i] = tmp_x[i];
	            }

	            stockOptions.add(xArray);
	            return;
	        }

	        for(double i=min_x;i<=share;i+=step_x){
	         tmp_x[id-1] = (Math.abs(i) < error_bound_x)?0.0:i;
	            setupStockOptionsRec(id-1,share-i);
	        }
	    }



	    public void setupStockOptions() {

	        step_x = 0.1;
	        min_x = -0.1;
	        max_x = 1.1;
	        error_bound_x = step_x/100;

	        tmp_x = new double [numStocks];
	        stockOptions = new LinkedList<double[]>();

	        setupStockOptionsRec(numStocks, max_x);
	        numOptions = stockOptions.size();

	       // System.out.println("numOptions="+numOptions);

	    }


	    class DataPoint {
	      //  int windowX;
	      //  int windowY;
	        double x[];
	        double std;
	        double mean;

	        DataPoint(double [] x_values) {
	            x = new double[numStocks];

	            for(int i=0;i<numStocks;i++){
	                x[i] = x_values[i];
	            }

	            std = mean = 0.0;

	        }


	     /*   boolean intersect(int wx, int wy) {
	            return close(wx - windowX) && close(wy - windowY);
	        }

	       boolean close(int val) {
	            return val <= POINT_WIDTH && val >= -POINT_WIDTH;
	        }*/
	    }


		public void initialize() {
			// TODO Auto-generated method stub

		}


		public void chartMouseClicked(ChartMouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println(arg0.getEntity().getShapeCoords());
			if(arg0.getEntity()==null)
				return;

			tooltip = arg0.getEntity().getToolTipText();
			//System.out.println("mouse clicked:"+tooltip+"---");

			mouse_x = Double.parseDouble(tooltip.substring(tooltip.indexOf("(")+1, tooltip.indexOf(",")));
			mouse_y = Double.parseDouble(tooltip.substring(tooltip.indexOf(",")+1, tooltip.indexOf(")")));

			tooltip ="("+mouse_x+","+mouse_y+")";

			mouseClicked = true;
			updateGraph();
			//System.out.println("------");
		}


		public void chartMouseMoved(ChartMouseEvent arg0) {
			// TODO Auto-generated method stub
			//getRecordTable().setText("mouse moved " +arg0.getEntity().toString());
		}


		public JTable getResultTable() {
			// TODO Auto-generated method stub
			return null;
		}


		public void graphUpdate() {
			// TODO Auto-generated method stub

		}


		public void setShowModelDistribution(boolean flag) {
			// TODO Auto-generated method stub

		}

}
