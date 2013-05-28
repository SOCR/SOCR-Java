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
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
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
public class StockSimulationApplication extends Application  implements Observer, IExperiment, ActionListener{

	private static final float LEFT_ALIGNMENT = 0;
	protected final int CHART_SIZE_X = 500;
    /**
	 * @uml.property  name="cHART_SIZE_Y"
	 */
    protected final int CHART_SIZE_Y = 500;
    protected ChartPanel chartPanel;
    protected JSplitPane upContainer;

	 // statistical input variables
    protected double[] input= new double[10];
    protected JTextField[] in = new JTextField[10];

    private DecimalFormat formatter = new DecimalFormat("#0.0####");
    private DecimalFormat tooltip_formatter = new DecimalFormat("#0.0#");

    String choice;
    int numInput;
    String title,xAxis, yAxis;

    //JComboBox  gChoices;
   // protected JTextField inMu, inDelta, inDt, inS0, inN;
 //   double Mu, Delta, Dt, S0, N;
    String tooltip;

    JToolBar toolBar;
    JPanel leftPanel = new JPanel();
    Box inPanel = Box.createVerticalBox();
    Box inputPanel = Box.createVerticalBox();

	XYSeries s_serie;

	double stepS0 = 1;
	int numSs = 60;

	// intermediate computation results
    double ST [];
    double S [];


    StockOptionInfo info;
    /**
     *
     */
    public StockSimulationApplication() {

    	setName("StockSimulationApplication");
    	initInfo();
    	initInputPanel();
    	init();

    }

    public void init(){

    	updateGraph();

     	s_serie = new XYSeries("S", false);

	  	upContainer= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(leftPanel), new JScrollPane(chartPanel));

	  	this.getMainPanel().removeAll();
		this.getMainPanel().add(new JScrollPane(upContainer), BorderLayout.CENTER);
	  	this.getMainPanel().validate();
    }

    void initInfo(){
    	//System.out.println("heren umberOfStrategy = "+numberOfStrategy);
    	info= new StockOptionInfo();


    	info.callName = "A1";
    	info.name = "Simulation of Stock's Path";
    	info.inputCount = 5;
    	info.inputNames= new String[info.inputCount];
    	info.inputLongNames= new String[info.inputCount];
    	info.inputNames[0]= "mu"; info.inputNames[1]= "sigma";info.inputNames[2]= "dt";info.inputNames[3]= "S0";info.inputNames[4]= "n";

    	info.inputLongNames[0]="Annual mean return.";
    	info.inputLongNames[1]="Annual standard deviation (volatility).";
    	info.inputLongNames[2]="Time interval (in years).";
    	info.inputLongNames[3]="Price of the stock at time 0.";
    	info.inputLongNames[4]="Number of intervals.";

    	info.inputDefault= new double[info.inputCount];
    	info.inputValue= new double[info.inputCount];
    	info.inputDefault[0] = 0.14; info.inputDefault[1] = 0.20; info.inputDefault[2] = 0.01; info.inputDefault[3] = 20; info.inputDefault[4] = 100;
    	info.outputCount = 1;
    	info.outputName= new String[info.outputCount];
    	info.outputName[0]= "";


    }
    void initGraph(){

		JFreeChart chart = createEmptyChart(null);	//create a empty graph first
		chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

    }

    void updateGraph(){
    	//System.out.println("UpdateGraph get called")
    	calculate();

    	XYSeriesCollection ds = createDataset();

    	  JFreeChart chart = ChartFactory.createXYLineChart(
    	            title,      // chart title
    	            xAxis,                      // x axis label
    	            yAxis,                      // y axis label
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


    	  renderer1.setSeriesPaint(0, Color.red);
    	  renderer1.setSeriesPaint(1, Color.blue);
    	  renderer1.setSeriesPaint(2, Color.green);
    	  renderer1.setSeriesPaint(3, Color.gray);

    	 /* Shape shape = renderer1.getBaseShape();
    	  renderer1.setSeriesShape(2, shape);
    	  renderer1.setSeriesShape(3, shape);*/
    	  renderer1.setBaseLinesVisible(true);
    	  renderer1.setBaseShapesVisible(true);
    	  renderer1.setBaseShapesFilled(true);

    	  chartPanel = new ChartPanel(chart, false);
    	  chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

    	  upContainer= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(leftPanel), new JScrollPane(chartPanel));

  	  	this.getMainPanel().removeAll();
  		this.getMainPanel().add(new JScrollPane(upContainer), BorderLayout.CENTER);
  	  	this.getMainPanel().validate();
    	//  getRecordTable().setText("Any Explaination goes here.");

    	 //
    }

    protected XYSeriesCollection createDataset(){
    	//System.out.println("stepS0="+stepS0);

    	//System.out.println("createDataset start");
    	s_serie = new XYSeries("S", false);
	    	for (int i=0; i<numSs; i++){
	    		s_serie.add(i, (S[i]));
	    	}

	    	  XYSeriesCollection ds = new XYSeriesCollection();
	    	  ds.addSeries(s_serie);

	    	  return ds;

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



    private void initInputPanel(){
    	//System.out.println("initInputPanel start");

    	inPanel.removeAll();

    	xAxis="Time";
		yAxis ="Price";
    	title = info.name;
    	numInput=info.inputCount;

    	for (int i=0; i<numInput; i++){
    		JPanel jp = new JPanel();
    		in[i]=new JTextField(14);
    		jp.add(new JLabel(info.inputNames[i]));
    		input[i]=(info.inputDefault[i]);
    		in[i].setText(String.valueOf(input[i]));
    		jp.add(in[i]);
    		inputPanel.add(jp);

    		in[i].addActionListener(this);
    		in[i].setActionCommand("Input"+(i+1));
    	}

    	inPanel.validate();

    	String text="";
    	for (int i=0; i<numInput; i++){
    		text +=" "+info.inputNames[i]+": "+info.inputLongNames[i]+"\n";
    	}

    	if(info.note!=null)
    		text +=info.note;
    	 getRecordTable().setText(text);

    	leftPanel.add(inputPanel, BorderLayout.NORTH);
    }



    /**
     * This method is the default update method and defines how the display is
     * updated. This method should be overridden.
     */
    public void update(Observable arg0, Object arg1) {



    }

    public void calculate() {
    	//System.out.println("calculate start");
    	double Mu = input[0];
		double Delta = input[1];
		double Dt = input[2];
		double S0 = input[3];
		double N = input[4];
    	NormalDistribution nd = new NormalDistribution(0,1);

    	numSs= (int)N;
    	S = new double[numSs];
    	S[0]=S0;

    	//System.out.println("numSs="+numSs);
    	double sq = Math.sqrt(Delta);
    	double e;
    	double ds;
    	for (int i=0; i<numSs; i++){
    		e = nd.simulate();
    		ds = Mu*S0*Dt+Delta*S0*sq*e;
    		if (i!=0)
    			S[i]= S[i-1]+ds;
    	}
    }


    public void actionPerformed(ActionEvent evt) {
    //	System.out.println("action start");
    	for (int i=0; i<numInput; i++)
       	 if (evt.getActionCommand().equals("Input"+(i+1))) {
       		 input[i] = Double.parseDouble(in[i].getText());
       		// System.out.println("getting input["+i+"]="+input[i]);
       		 updateGraph();
       		 chartPanel.validate();
       	 }
    /*	if (evt.getActionCommand().equals("Mu")) {
   		 Mu = Double.parseDouble(inMu.getText());
   	}else if (evt.getActionCommand().equals("Delta")) {
   		 Delta = Double.parseDouble(inDelta.getText());
   	}else if (evt.getActionCommand().equals("Dt")) {
   		 Dt = Double.parseDouble(inDt.getText());
   	}else if (evt.getActionCommand().equals("S0")) {
   		 S0 = Double.parseDouble(inS0.getText());
   	}else if (evt.getActionCommand().equals("N")) {
   		 N = Double.parseDouble(inN.getText());
   	}


    	updateGraph();
    	chartPanel.validate(); 		*/


   	  this.getMainPanel().validate();
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
		updateGraph();
	};



	public void initialize() {
			// TODO Auto-generated method stub

		}


}
