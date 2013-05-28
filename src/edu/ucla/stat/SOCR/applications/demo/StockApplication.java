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
public class StockApplication extends Application  implements Observer, IExperiment, ActionListener{

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

    JComboBox  gChoices;

    String tooltip;

    JToolBar toolBar;
    JPanel leftPanel = new JPanel();
    Box inPanel = Box.createVerticalBox();
    Box inputPanel = Box.createVerticalBox();

	XYSeries s_serie;
	XYSeries c_serie;
	XYSeries t_serie;
	XYSeries m_serie;

	double stepS0 = 1;
	int numSs = 60;

	// intermediate computation results
    double ST [];
    double S [];
    double Profit [];
    double Total[];
    double Cmax [];
    double Cmin [];
    double Cmed [];


    static final int numberOfStrategy= 19;
    StockOptionInfo[] info;
    /**
     *
     */
    public StockApplication() {

    	setName("StockApplication");
    	initInfo();
    	initInputPanel();
    	init();

    }

    public void init(){

    	updateGraph();

     	s_serie = new XYSeries("S", false);
		c_serie = new XYSeries("C", false);
		t_serie = new XYSeries("Total", false);
		m_serie = new XYSeries("M", false);

	  	upContainer= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(leftPanel), new JScrollPane(chartPanel));

	  	this.getMainPanel().removeAll();
		this.getMainPanel().add(new JScrollPane(upContainer), BorderLayout.CENTER);
	  	this.getMainPanel().validate();
    }

    void initInfo(){
    	//System.out.println("heren umberOfStrategy = "+numberOfStrategy);
    	info= new StockOptionInfo[numberOfStrategy];

    	for (int i=0; i<numberOfStrategy; i++)
    		info[i]= new StockOptionInfo();


    	info[0].callName = "A1";
    	info[0].name = "Profit from buying a call option";
    	info[0].inputCount = 2;
    	info[0].inputNames= new String[info[0].inputCount];
    	info[0].inputLongNames= new String[info[0].inputCount];
    	info[0].inputNames[0]= "E"; info[0].inputNames[1]= "C";

    	info[0].inputLongNames[0]="Exercise price";
    	info[0].inputLongNames[1]="Price of call";

    	info[0].inputDefault= new double[info[0].inputCount];
    	info[0].inputValue= new double[info[0].inputCount];
    	info[0].inputDefault[0] = 45; info[0].inputDefault[1] = 5;
    	info[0].outputCount = 1;
    	info[0].outputName= new String[info[0].outputCount];
    	info[0].outputName[0]= "";


    	info[1].callName = "A2";
    	info[1].name = "Profit from selling a call option";
    	info[1].inputCount = 2;
    	info[1].inputNames= new String[info[1].inputCount];
    	info[1].inputLongNames= new String[info[1].inputCount];
    	info[1].inputNames[0]= "E";info[1].inputNames[1]= "C";
    	info[1].inputLongNames[0]="Exercise price";
    	info[1].inputLongNames[1]="Price of call";
    	info[1].inputDefault= new double[info[1].inputCount];
    	info[1].inputDefault[0] = 45; info[1].inputDefault[1] = 5;
    	info[1].outputCount = 1;
    	info[1].outputName= new String[info[1].outputCount];
    	info[1].outputName[0]= "";

    	info[2].callName = "A3";
    	info[2].name = "Profit from buying a put";
    	info[2].inputCount = 2;
    	info[2].inputNames= new String[info[2].inputCount];
    	info[2].inputLongNames= new String[info[2].inputCount];
    	info[2].inputNames[0]= "E";info[2].inputNames[1]= "P";
    	info[2].inputLongNames[0]="Exercise price";
    	info[2].inputLongNames[1]="Price of put";
    	info[2].inputDefault= new double[info[2].inputCount];
    	info[2].inputDefault[0] = 45; info[2].inputDefault[1] = 5;
    	info[2].outputCount = 1;
    	info[2].outputName= new String[info[2].outputCount];
    	info[2].outputName[0]= "";

    	info[3].callName = "A4";
    	info[3].name = "Profit from selling a put";
    	info[3].inputCount = 2;
    	info[3].inputNames= new String[info[3].inputCount];
    	info[3].inputLongNames= new String[info[3].inputCount];
    	info[3].inputNames[0]= "E";info[3].inputNames[1]= "P";
    	info[3].inputLongNames[0]="Exercise price";
    	info[3].inputLongNames[1]="Price of put";
    	info[3].inputDefault= new double[info[3].inputCount];
    	info[3].inputDefault[0] = 45; info[3].inputDefault[1] = 5;
    	info[3].outputCount = 1;
    	info[3].outputName= new String[info[3].outputCount];
    	info[3].outputName[0]= "";
    	/*----------------*/
    	info[4].callName = "B1";
    	info[4].name = "Sell Stock - Buy Call";
    	info[4].inputCount = 3;
    	info[4].inputNames= new String[info[4].inputCount];
    	info[4].inputLongNames= new String[info[4].inputCount];
    	info[4].inputNames[0]= "S0";info[4].inputNames[1]= "E";info[4].inputNames[2]= "C";
    	info[4].inputLongNames[0]="Stock price at t = 0";
    	info[4].inputLongNames[1]="Exercise price";
    	info[4].inputLongNames[2]="Price of call";
    	info[4].inputDefault= new double[info[4].inputCount];
    	info[4].inputDefault[0] = 40; info[4].inputDefault[1] = 50;info[4].inputDefault[2] = 5;
    	info[4].outputCount = 3;
    	info[4].outputName= new String[info[4].outputCount];
    	info[4].outputName[0]= "Stock";info[4].outputName[1]= "Call";info[4].outputName[2]= "Total";

    	info[5].callName = "B2";
    	info[5].name = "Buy Stock - Sell Call";
    	info[5].inputCount = 3;
    	info[5].inputNames= new String[info[5].inputCount];
    	info[5].inputLongNames= new String[info[5].inputCount];
    	info[5].inputNames[0]= "S0";info[5].inputNames[1]= "E";info[5].inputNames[2]= "C";
    	info[5].inputLongNames[0]="Stock price at t = 0";
    	info[5].inputLongNames[1]="Exercise price";
    	info[5].inputLongNames[2]="Price of call";
    	info[5].inputDefault= new double[info[5].inputCount];
    	info[5].inputDefault[0] = 40; info[5].inputDefault[1] = 50;info[5].inputDefault[2] = 5;
    	info[5].outputCount = 3;
    	info[5].outputName= new String[info[5].outputCount];
    	info[5].outputName[0]= "Stock";info[5].outputName[1]= "Call";info[5].outputName[2]= "Total";

    	info[6].callName = "B3";
    	info[6].name = "Buy Stock - Buy Put";
    	info[6].inputCount = 3;
    	info[6].inputNames= new String[info[6].inputCount];
    	info[6].inputLongNames= new String[info[6].inputCount];
    	info[6].inputNames[0]= "S0";info[6].inputNames[1]= "E";info[6].inputNames[2]= "P";
    	info[6].inputLongNames[0]="Stock price at t = 0";
    	info[6].inputLongNames[1]="Exercise price";
    	info[6].inputLongNames[2]="Price of put";
    	info[6].inputDefault= new double[info[6].inputCount];
    	info[6].inputDefault[0] = 40; info[6].inputDefault[1] = 50;info[6].inputDefault[2] = 15;
    	info[6].outputCount = 3;
    	info[6].outputName= new String[info[6].outputCount];
    	info[6].outputName[0]= "Stock";info[6].outputName[1]= "Put";info[6].outputName[2]= "Total";

    	info[7].callName = "B4";
    	info[7].name = "Sell Stock - Sell Put";
    	info[7].inputCount = 3;
    	info[7].inputNames= new String[info[7].inputCount];
    	info[7].inputLongNames= new String[info[7].inputCount];
    	info[7].inputNames[0]= "S0";info[7].inputNames[1]= "E";info[7].inputNames[2]= "P";
    	info[7].inputLongNames[0]="Stock price at t = 0";
    	info[7].inputLongNames[1]="Exercise price";
    	info[7].inputLongNames[2]="Price of put";
    	info[7].inputDefault= new double[info[7].inputCount];
    	info[7].inputDefault[0] = 40; info[7].inputDefault[1] = 50;info[7].inputDefault[2] = 5;
    	info[7].outputCount = 3;
    	info[7].outputName= new String[info[7].outputCount];
    	info[7].outputName[0]= "Stock";info[7].outputName[1]= "Put";info[7].outputName[2]= "Total";
    	/*----------------*/
    	info[8].callName = "C1";
    	info[8].name = "Bull Spread Using Call Options";
    	info[8].inputCount = 4;
    	info[8].inputNames= new String[info[8].inputCount];
    	info[8].inputLongNames= new String[info[8].inputCount];
    	info[8].inputNames[0]= "E1";info[8].inputNames[1]= "C1";info[8].inputNames[2]= "E2";info[8].inputNames[3]= "C2";
    	info[8].inputLongNames[0]="Exercise Price of Long Call Position";
    	info[8].inputLongNames[1]="Price of Long Call";
    	info[8].inputLongNames[2]="Exercise Price of Short Call Position";
    	info[8].inputLongNames[3]="Price of Short Call";
    	info[8].note = "Restrictions : E1 < E2 ; C1 > C2";
    	info[8].inputDefault= new double[info[8].inputCount];
    	info[8].inputDefault[0] = 50; info[8].inputDefault[1] = 10;info[8].inputDefault[2] = 70; info[8].inputDefault[3] = 7;
    	info[8].outputCount = 3;
    	info[8].outputName= new String[info[8].outputCount];
    	info[8].outputName[0]= "Long Call";info[8].outputName[1]= "Short Call";info[8].outputName[2]= "Total";

    	info[9].callName = "C2";
    	info[9].name = "Bull Spread Using Put Options";
    	info[9].inputCount = 4;
    	info[9].inputNames= new String[info[9].inputCount];
    	info[9].inputLongNames= new String[info[9].inputCount];
    	info[9].inputNames[0]= "E1";info[9].inputNames[1]= "P1";info[9].inputNames[2]= "E2";info[9].inputNames[3]= "P2";
    	info[9].inputLongNames[0]="Exercise Price of Long Put Position";
    	info[9].inputLongNames[1]="Price of Long Put";
    	info[9].inputLongNames[2]="Exercise Price of Short Put Position";
    	info[9].inputLongNames[3]="Price of Short Put";
    	info[9].note = "Restriction : E1 < E2 ; P1 < P2";
    	info[9].inputDefault= new double[info[9].inputCount];
    	info[9].inputDefault[0] = 50; info[9].inputDefault[1] = 7;info[9].inputDefault[2] = 70; info[9].inputDefault[3] = 10;
    	info[9].outputCount = 3;
    	info[9].outputName= new String[info[9].outputCount];
    	info[9].outputName[0]= "Long Put";info[9].outputName[1]= "Short Put";info[9].outputName[2]= "Total";

    	info[10].callName = "C3";
    	info[10].name = "Bear Spread Using Call Options";
    	info[10].inputCount = 4;
    	info[10].inputNames= new String[info[10].inputCount];
    	info[10].inputLongNames= new String[info[10].inputCount];
    	info[10].inputNames[0]= "E1";info[10].inputNames[1]= "C1";info[10].inputNames[2]= "E2";info[10].inputNames[3]= "C2";
    	info[10].inputLongNames[0]="Exercise Price of Long Call Position";
    	info[10].inputLongNames[1]="Price of Long Call";
    	info[10].inputLongNames[2]="Exercise Price of Short Call Position";
    	info[10].inputLongNames[3]="Price of Short Call";
    	info[10].note = "Restrictions : E1 > E2 ; C1 < C2";
    	info[10].inputDefault= new double[info[10].inputCount];
    	info[10].inputDefault[0] = 70; info[10].inputDefault[1] = 7;info[10].inputDefault[2] = 50; info[10].inputDefault[3] = 10;
    	info[10].outputCount = 3;
    	info[10].outputName= new String[info[10].outputCount];
    	info[10].outputName[0]= "Long Call";info[10].outputName[1]= "Short Call";info[10].outputName[2]= "Total";

    	info[11].callName = "C4";
    	info[11].name = "Bear Spread Using Put Options";
    	info[11].inputCount = 4;
    	info[11].inputNames= new String[info[11].inputCount];
    	info[11].inputLongNames= new String[info[11].inputCount];
    	info[11].inputNames[0]= "E1";info[11].inputNames[1]= "P1";info[11].inputNames[2]= "E2";info[11].inputNames[3]= "P2";
    	info[11].inputLongNames[0]="Exercise Price of Long Put Position";
    	info[11].inputLongNames[1]="Price of Long Put";
    	info[11].inputLongNames[2]="Exercise Price of Short Put Position";
    	info[11].inputLongNames[3]="Price of Short Put";
    	info[11].note = "Restriction : E1 > E2 ; P1 > P2";
    	info[11].inputDefault= new double[info[11].inputCount];
    	info[11].inputDefault[0] = 70; info[11].inputDefault[1] = 10;info[11].inputDefault[2] = 50; info[11].inputDefault[3] = 7;
    	info[11].outputCount = 3;
    	info[11].outputName= new String[info[11].outputCount];
    	info[11].outputName[0]= "Long Put";info[11].outputName[1]= "Short Put";info[11].outputName[2]= "Total";

    	/*-------*/
    	info[12].callName = "D1";
    	info[12].name = "Butterfly Spread Using Call Options";
    	info[12].inputCount = 6;
    	info[12].inputNames= new String[info[12].inputCount];
    	info[12].inputLongNames= new String[info[12].inputCount];
    	info[12].inputNames[0]= "E1";info[12].inputNames[1]= "C1";info[12].inputNames[2]= "E2";info[12].inputNames[3]= "C2";info[12].inputNames[4]= "E3";info[12].inputNames[5]= "C3";
    	info[12].inputLongNames[0]="Exercise Price of Long Call Position";
    	info[12].inputLongNames[1]="Price of Long Call";
    	info[12].inputLongNames[2]="Exercise Price of Long Call Position";
    	info[12].inputLongNames[3]="Price of Long Call";
    	info[12].inputLongNames[4]="Exercise Price of Short Call Position";
    	info[12].inputLongNames[5]="Price of Short Call";
    	info[12].note = "Note: Selling two call options with price C3 \n Restrictions: E1 < E3 < E2 ; C1 > C3 > C2";
    	info[12].inputDefault= new double[info[12].inputCount];
    	info[12].inputDefault[0] = 70; info[12].inputDefault[1] = 10;info[12].inputDefault[2] = 50; info[12].inputDefault[3] = 7;info[12].inputDefault[4] = 60; info[12].inputDefault[5] = 7;
    	info[12].outputCount = 4;
    	info[12].outputName= new String[info[12].outputCount];
    	info[12].outputName[0]= "Long Call";info[12].outputName[1]= "Short Call";info[12].outputName[2]= "Long Call";info[12].outputName[3]= "Total";

    	/*---------*/
    	info[13].callName = "E1";
    	info[13].name = "Holder of a Straddle (1 call and 1 put)";
    	info[13].inputCount = 3;
    	info[13].inputNames= new String[info[13].inputCount];
    	info[13].inputLongNames= new String[info[13].inputCount];
    	info[13].inputNames[0]= "E"; info[13].inputNames[1]= "C";info[13].inputNames[2]= "P";
    	info[13].inputLongNames[0]="Exercise price";
    	info[13].inputLongNames[1]="Price of call";
    	info[13].inputLongNames[2]="Price of put";
    	info[13].inputDefault= new double[info[13].inputCount];
    	info[13].inputValue= new double[info[13].inputCount];
    	info[13].inputDefault[0] = 45; info[13].inputDefault[1] = 6; info[13].inputDefault[2] = 4;
    	info[13].outputCount = 1;
    	info[13].outputName= new String[info[13].outputCount];
    	info[13].outputName[0]= "";

    	info[14].callName = "E2";
    	info[14].name = "Writer of a Straddle (1 call and 1 put)";
    	info[14].inputCount = 3;
    	info[14].inputNames= new String[info[14].inputCount];
    	info[14].inputLongNames= new String[info[14].inputCount];
    	info[14].inputNames[0]= "E"; info[14].inputNames[1]= "C";info[14].inputNames[2]= "P";
    	info[14].inputLongNames[0]="Exercise price";
    	info[14].inputLongNames[1]="Price of call";
    	info[14].inputLongNames[2]="Price of put";
    	info[14].inputDefault= new double[info[14].inputCount];
    	info[14].inputValue= new double[info[14].inputCount];
    	info[14].inputDefault[0] = 45; info[14].inputDefault[1] = 6; info[14].inputDefault[2] = 4;
    	info[14].outputCount = 1;
    	info[14].outputName= new String[info[14].outputCount];
    	info[14].outputName[0]= "";

    	info[15].callName = "E3";
    	info[15].name = "Holder of a Strip (2 put and 1 call)";
    	info[15].inputCount = 3;
    	info[15].inputNames= new String[info[15].inputCount];
    	info[15].inputLongNames= new String[info[15].inputCount];
    	info[15].inputNames[0]= "E"; info[15].inputNames[1]= "C";info[15].inputNames[2]= "P";
    	info[15].inputLongNames[0]="Exercise price";
    	info[15].inputLongNames[1]="Price of call";
    	info[15].inputLongNames[2]="Price of put";
    	info[15].inputDefault= new double[info[15].inputCount];
    	info[15].inputValue= new double[info[15].inputCount];
    	info[15].inputDefault[0] = 45; info[15].inputDefault[1] = 6; info[15].inputDefault[2] = 4;
    	info[15].outputCount = 1;
    	info[15].outputName= new String[info[15].outputCount];
    	info[15].outputName[0]= "";

    	info[16].callName = "E4";
    	info[16].name = "Writer of a Strip (2 put and 1 call)";
    	info[16].inputCount = 3;
    	info[16].inputNames= new String[info[16].inputCount];
    	info[16].inputLongNames= new String[info[16].inputCount];
    	info[16].inputNames[0]= "E"; info[16].inputNames[1]= "C";info[16].inputNames[2]= "P";
    	info[16].inputLongNames[0]="Exercise price";
    	info[16].inputLongNames[1]="Price of call";
    	info[16].inputLongNames[2]="Price of put";
    	info[16].inputDefault= new double[info[16].inputCount];
    	info[16].inputValue= new double[info[16].inputCount];
    	info[16].inputDefault[0] = 45; info[16].inputDefault[1] = 6; info[16].inputDefault[2] = 4;
    	info[16].outputCount = 1;
    	info[16].outputName= new String[info[16].outputCount];
    	info[16].outputName[0]= "";

    	info[17].callName = "E5";
    	info[17].name = "Holder of a Strap (2 calls and 1 put)";
    	info[17].inputCount = 3;
    	info[17].inputNames= new String[info[17].inputCount];
    	info[17].inputLongNames= new String[info[17].inputCount];
    	info[17].inputNames[0]= "E"; info[17].inputNames[1]= "C";info[17].inputNames[2]= "P";
    	info[17].inputLongNames[0]="Exercise price";
    	info[17].inputLongNames[1]="Price of call";
    	info[17].inputLongNames[2]="Price of put";
    	info[17].inputDefault= new double[info[17].inputCount];
    	info[17].inputValue= new double[info[17].inputCount];
    	info[17].inputDefault[0] = 45; info[17].inputDefault[1] = 6; info[17].inputDefault[2] = 4;
    	info[17].outputCount = 1;
    	info[17].outputName= new String[info[17].outputCount];
    	info[17].outputName[0]= "";

    	info[18].callName = "E6";
    	info[18].name = "Writer of a Strap (2 calls and 1 put)";
    	info[18].inputCount = 3;
    	info[18].inputNames= new String[info[18].inputCount];
    	info[18].inputLongNames= new String[info[18].inputCount];
    	info[18].inputNames[0]= "E"; info[18].inputNames[1]= "C";info[18].inputNames[2]= "P";
    	info[18].inputLongNames[0]="Exercise price";
    	info[18].inputLongNames[1]="Price of call";
    	info[18].inputLongNames[2]="Price of put";
    	info[18].inputDefault= new double[info[18].inputCount];
    	info[18].inputValue= new double[info[18].inputCount];
    	info[18].inputDefault[0] = 45; info[18].inputDefault[1] = 6; info[18].inputDefault[2] = 4;
    	info[18].outputCount = 1;
    	info[18].outputName= new String[info[18].outputCount];
    	info[18].outputName[0]= "";

    }
    void initGraph(){

		JFreeChart chart = createEmptyChart(null);	//create a empty graph first
		chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

    }

    void updateGraph(){
    	//System.out.println("UpdateGraph get called")
    	//	System.out.println("S0="+S0+" E="+E+" P="+P);

    	calculate(choice);

    	XYSeriesCollection ds = createDataset(choice);

    	  JFreeChart chart = ChartFactory.createXYLineChart(
    	            title,      // chart title
    	            xAxis,                      // x axis label
    	            yAxis,                      // y axis label
    	            ds,                  // data
    	            PlotOrientation.VERTICAL,
    	            true,                     // include legend
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

    protected XYSeriesCollection createDataset(String choice){
    	//System.out.println("stepS0="+stepS0);
    	if(choice.equalsIgnoreCase("B4")||  	 //B1
    			choice.equalsIgnoreCase("B1")||   //B8
    			choice.equalsIgnoreCase("B3")||  //B9
    			choice.equalsIgnoreCase("B2")){  //B10
	    	s_serie = new XYSeries(info[7].outputName[0], false);
	    	c_serie = new XYSeries(info[7].outputName[1], false);
	    	t_serie = new XYSeries(info[7].outputName[2], false);

	    	for (int i=0; i<=numSs; i++){

	    		s_serie.add(i*stepS0, (S[i]));
	    		c_serie.add(i*stepS0,(Profit[i]));
	    		t_serie.add(i*stepS0,(Total[i]));

	    	}

	    	  XYSeriesCollection ds = new XYSeriesCollection();
	    	  ds.addSeries(s_serie);
	    	  ds.addSeries(c_serie);
	    	  ds.addSeries(t_serie);

	    	  return ds;
    	}else if(choice.equalsIgnoreCase("A1")||  //B4
    			choice.equalsIgnoreCase("A2")||   //B5
    			choice.equalsIgnoreCase("A3")||   //B6
    			choice.equalsIgnoreCase("A4") ||    //B7
    			choice.equalsIgnoreCase("E1") ||
    			choice.equalsIgnoreCase("E2") ||
    			choice.equalsIgnoreCase("E3") ||
    			choice.equalsIgnoreCase("E4") ||
    			choice.equalsIgnoreCase("E5")  ||
    			choice.equalsIgnoreCase("E6")
    			){
    		c_serie = new XYSeries(info[0].outputName[0], false);
    		for (int i=0; i<=numSs; i++)
	    		c_serie.add(i*stepS0,(Profit[i]));

    		 XYSeriesCollection ds = new XYSeriesCollection();
    		 ds.addSeries(c_serie);
    		 return ds;
    	}
    	else if(choice.equalsIgnoreCase("C3")||   //B2
    			choice.equalsIgnoreCase("C2")||   //B11
    			choice.equalsIgnoreCase("C1")||   //B12
    			choice.equalsIgnoreCase("C4")){   //B13
    		s_serie = new XYSeries(info[10].outputName[0], false);
	    	c_serie = new XYSeries(info[10].outputName[1], false);
	    	t_serie = new XYSeries(info[10].outputName[2], false);

            for(int i=0;i<=numSs;i++) {
            	s_serie.add(i*stepS0, (Cmax[i]));
	    		c_serie.add(i*stepS0,(Cmin[i]));
	    		t_serie.add(i*stepS0,(Total[i]));
            }

            XYSeriesCollection ds = new XYSeriesCollection();
	    	  ds.addSeries(s_serie);
	    	  ds.addSeries(c_serie);
	    	  ds.addSeries(t_serie);

	    	  return ds;
    	}else if(choice.equalsIgnoreCase("D1")){
    		s_serie = new XYSeries(info[12].outputName[0], false);
	    	c_serie = new XYSeries(info[12].outputName[1], false);
	    	m_serie = new XYSeries(info[12].outputName[2], false);
	    	t_serie = new XYSeries(info[12].outputName[3], false);

	    	for(int i=0;i<=numSs;i++) {
	    		s_serie.add(i*stepS0, (Cmax[i]));
	    		c_serie.add(i*stepS0,(Cmin[i]));
	    		t_serie.add(i*stepS0,(Total[i]));
	    		m_serie.add(i*stepS0,(Cmed[i]));
	    	}
	    	XYSeriesCollection ds = new XYSeriesCollection();
	    	ds.addSeries(s_serie);
	    	ds.addSeries(m_serie);
	    	ds.addSeries(c_serie);
	    	ds.addSeries(t_serie);

	    	return ds;
    	}
    	return null;
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
    	leftPanel = new JPanel();
    	gChoices = new JComboBox();


    	gChoices.setToolTipText("Select Trading Strategy");
    	for (int i=0; i<numberOfStrategy; i++)
    		gChoices.addItem(info[i].callName+":"+info[i].name);


    	gChoices.addActionListener(this);

    	//inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
    	//inPanel.setLayout(new BoxLayout(inPanel, BoxLayout.PAGE_AXIS));
    	JPanel jp0 = new JPanel();
    	jp0.add(new JLabel("Select Trading Strategy"));
    	inputPanel.add(jp0);

    	JPanel jp1 = new JPanel();
    	jp1.add(gChoices);
    	inputPanel.add(jp1);

    	choice= "A1";
    	setupInput("A1");
    	inputPanel.add(inPanel);

    	leftPanel.add(inputPanel, BorderLayout.NORTH);
    }

    private void setupInput(String choice){
    	inPanel.removeAll();

    	int k = findInfo(choice);
    	if (k ==-1){
    		System.out.println("k=-1");
    		return;
    	}

    	xAxis="Stock Price at Expiration(S_T)";
		yAxis ="Profit";
    	title = info[k].name;
    	numInput=info[k].inputCount;

    	for (int i=0; i<numInput; i++){
    		JPanel jp = new JPanel();
    		in[i]=new JTextField(14);
    		jp.add(new JLabel(info[k].inputNames[i]));
    		input[i]=(info[k].inputDefault[i]);
    		in[i].setText(String.valueOf(input[i]));
    		jp.add(in[i]);
    		inPanel.add(jp);

    		in[i].addActionListener(this);
    		in[i].setActionCommand("Input"+(i+1));
    	}

    	inPanel.validate();

    	String text="";
    	for (int i=0; i<numInput; i++){
    		text +=" "+info[k].inputNames[i]+": "+info[k].inputLongNames[i]+"\n";
    	}

    	if(info[k].note!=null)
    		text +=info[k].note;
    	 getRecordTable().setText(text);
    }

    int findInfo(String callName){

    	for (int i=0; i<numberOfStrategy; i++){
    		if (info[i].callName.equals(callName))
    			return i;
    	}
    	return -1;
}

    /**
     * This method is the default update method and defines how the display is
     * updated. This method should be overridden.
     */
    public void update(Observable arg0, Object arg1) {



    }

    public void calculate(String choice) {
    	if (choice.equalsIgnoreCase("B4")){  //B1
    		double S0 = input[0];
    		double E= input[1];
    		double P = input[2];
//System.out.println("S0="+S0 +" E="+E +" P="+P);
        // allocate memory for arrays

	        stepS0 = S0*2 / numSs;
	        ST = new double[numSs+1];
	        S  = new double[numSs+1];
	        Profit = new double[numSs+1];
	        Total = new double[numSs+1];

	        double curST = 0;
	        for(int i=0;i<=numSs;i++) {
	            ST[i] = curST;
	            curST += stepS0;

	            S[i] = S0 - ST[i];
	            Profit[i] = Math.min(ST[i] - E + P, P);
	            Total[i] = S[i] + Profit[i];
	        }
    	}else if (choice.equalsIgnoreCase("C3")){  //B2
    		double E1 = input[0];
    		double C1= input[1];
    		double E2=input[2];
    		double C2=input[3];

        	stepS0 = Math.max(E1,E2)*2  / numSs;


            ST    = new double[numSs+1];
            Cmax  = new double[numSs+1];
            Cmin  = new double[numSs+1];
            Total = new double[numSs+1];

            // initialize ST [0, stepS0, ..., 2*S0]
            // calculate S, Profit and Total

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;

                Cmax[i] = Math.max(ST[i] - E1 - C1, -C1);
                Cmin[i] = Math.min(E2 - ST[i] + C2, C2);
                Total[i] = Cmax[i] + Cmin[i];

            }

    	}
    	else if (choice.equalsIgnoreCase("D1")){
    		double E1= input[0];
    		double C1= input[1];
    		double E2= input[2];
    		double C2= input[3];
    		double E3= input[4];
    		double C3= input[5];

        	stepS0 = Math.max(Math.max(E1, E2), E3) * 2 /numSs;

             ST    = new double[numSs+1];
             Cmax  = new double[numSs+1];
             Cmed  = new double[numSs+1];
             Cmin  = new double[numSs+1];
             Total = new double[numSs+1];

             double curST = 0;
             for(int i=0;i<=numSs;i++) {

                 ST[i] = curST;
                 curST += stepS0;

                 Cmax[i] = Math.max(ST[i] - E1 - C1, -C1);
                 Cmed[i] = Math.max(ST[i] - E2 - C2, -C2);
                 Cmin[i] = 2 * Math.min(E3 - ST[i] + C3, C3);
                 Total[i] = Cmax[i] + Cmin[i] + Cmed[i];

             }
    	}
    	else if (choice.equalsIgnoreCase("A1")){  //B4
    	//	double S0 = input[0];
        	double E = input[0];
        	double P = input[1];

        	stepS0 = E*2 / numSs;

            ST = new double[numSs+1];
         //   S  = new double[numSs+1];
            Profit = new double[numSs+1];
         //   Total = new double[numSs+1];

            // initialize ST [0, stepS0, ..., 2*S0]
            // calculate S, Profit and Total

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;

             //   S[i] = S0 - ST[i];
                Profit[i] = Math.max(ST[i] - E - P, -P);
            //    Total[i] = S[i] + Profit[i];

            }

    	}else if (choice.equalsIgnoreCase("A2")){  //B5
    	//	double S0 = input[0];
    		double E= input[0];
    		double P=input[1];
        	stepS0 = E*2 / numSs;

              ST = new double[numSs+1];
              S  = new double[numSs+1];
              Profit = new double[numSs+1];
              Total = new double[numSs+1];

              // initialize ST [0, stepS0, ..., 2*S0]
              // calculate S, Profit and Total

              double curST = 0;
              for(int i=0;i<=numSs;i++) {

                  ST[i] = curST;
                  curST += stepS0;

                 // S[i] = ST[i] - S0;
                  Profit[i] = Math.min(E - ST[i] + P, P);
                  Total[i] = S[i] + Profit[i];

              }

    	}else if (choice.equalsIgnoreCase("A3")){  //B6
    		//double  S0 = input[0];
    		double E= input[0];
    		double P=input[1];
        	stepS0 = E*2 / numSs;

              ST = new double[numSs+1];
              S  = new double[numSs+1];
              Profit = new double[numSs+1];
              Total = new double[numSs+1];

              // initialize ST [0, stepS0, ..., 2*S0]
              // calculate S, Profit and Total

              double curST = 0;
              for(int i=0;i<=numSs;i++) {
                  ST[i] = curST;
                  curST += stepS0;

                 // S[i] = ST[i] - S0;
                  Profit[i] = Math.max(E - ST[i] - P, -P);
                  Total[i] = S[i] + Profit[i];
              }

    	}else if (choice.equalsIgnoreCase("A4")){  //B7
    	//	double S0 = input[0];
    		double E= input[0];
    		double P=input[1];
        	stepS0 = E*2 / numSs;


              ST = new double[numSs+1];
              S  = new double[numSs+1];
              Profit = new double[numSs+1];
              Total = new double[numSs+1];

              // initialize ST [0, stepS0, ..., 2*S0]
              // calculate S, Profit and Total

              double curST = 0;
              for(int i=0;i<=numSs;i++) {
            	  ST[i] = curST;
                  curST += stepS0;

                //  S[i] = S0 - ST[i];
                  Profit[i] = Math.min(ST[i] - E + P, P);
                  Total[i] = S[i] + Profit[i];
              }

    	}
    	else if (choice.equalsIgnoreCase("B1")){  //B8
    		double S0 = input[0];
    		double E= input[1];
    		double P=input[2];
        	stepS0 = S0*2 / numSs;

              ST = new double[numSs+1];
              S  = new double[numSs+1];
              Profit = new double[numSs+1];
              Total = new double[numSs+1];

              // initialize ST [0, stepS0, ..., 2*S0]
              // calculate S, Profit and Total

              double curST = 0;
              for(int i=0;i<=numSs;i++) {
            	  ST[i] = curST;
                  curST += stepS0;

                  S[i] = S0 - ST[i];
                  Profit[i] = Math.max(ST[i] - E - P, -P);
                  Total[i] = S[i] + Profit[i];
              }

    	}else if (choice.equalsIgnoreCase("B3")){  //B9
    		double S0 = input[0];
    		double E= input[1];
    		double P=input[2];
    		stepS0 = S0*2 / numSs;

              ST = new double[numSs+1];
              S  = new double[numSs+1];
              Profit = new double[numSs+1];
              Total = new double[numSs+1];

              // initialize ST [0, stepS0, ..., 2*S0]
              // calculate S, Profit and Total

              double curST = 0;
              for(int i=0;i<=numSs;i++) {
            	  ST[i] = curST;
                  curST += stepS0;

                  S[i] = ST[i] - S0;
                  Profit[i] = Math.max(E - ST[i] - P, -P);
                  Total[i] = S[i] + Profit[i];
              }

    	}else if (choice.equalsIgnoreCase("B2")){  //B10
    		double S0 = input[0];
    		double E= input[1];
    		double P=input[2];
        	stepS0 = S0*2 / numSs;

              ST = new double[numSs+1];
              S  = new double[numSs+1];
              Profit = new double[numSs+1];
              Total = new double[numSs+1];

              // initialize ST [0, stepS0, ..., 2*S0]
              // calculate S, Profit and Total

              double curST = 0;
              for(int i=0;i<=numSs;i++) {
            	  ST[i] = curST;
                  curST += stepS0;

                  S[i] = ST[i] - S0;
                  Profit[i] = Math.min(E - ST[i] + P, P);
                  Total[i] = S[i] + Profit[i];
              }

    	}else if (choice.equalsIgnoreCase("C2")){ //B11
    		double E1 = input[0];
    		double C1= input[1];
    		double E2=input[2];
    		double C2=input[3];

        	stepS0 = Math.max(E1,E2)*2 / numSs;

            ST    = new double[numSs+1];
            Cmax  = new double[numSs+1];
            Cmin  = new double[numSs+1];
            Total = new double[numSs+1];

            // initialize ST [0, stepS0, ..., 2*S0]
            // calculate S, Profit and Total

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

            	ST[i] = curST;
                curST += stepS0;

                Cmax[i] = Math.max(E1 - ST[i] - C1, -C1);
                Cmin[i] = Math.min(ST[i] -E2 + C2, C2);
                Total[i] = Cmax[i] + Cmin[i];

            }

    	}else if (choice.equalsIgnoreCase("C1")){  //B12
    		double E1 = input[0];
    		double C1= input[1];
    		double E2=input[2];
    		double C2=input[3];

        	stepS0 = Math.max(E1,E2)*2 / numSs;

            ST    = new double[numSs+1];
            Cmax  = new double[numSs+1];
            Cmin  = new double[numSs+1];

            // initialize ST [0, stepS0, ..., 2*S0]
            // calculate S, Profit and Total

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

            	ST[i] = curST;
                curST += stepS0;

                Cmax[i] = Math.max(ST[i] - E1 - C1, -C1);
                Cmin[i] = Math.min(E2 - ST[i] + C2, C2);
                Total[i] = Cmax[i] + Cmin[i];

            }

    	}else if (choice.equalsIgnoreCase("C4")){  //B13
    		double E1 = input[0];
    		double C1= input[1];
    		double E2=input[2];
    		double C2=input[3];

        	stepS0 = Math.max(E1,E2)*2 / numSs;

            ST    = new double[numSs+1];
            Cmax  = new double[numSs+1];
            Cmin  = new double[numSs+1];
            Total = new double[numSs+1];

            // initialize ST [0, stepS0, ..., 2*S0]
            // calculate S, Profit and Total

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

            	ST[i] = curST;
                curST += stepS0;

                Cmax[i] = Math.max(E1 - ST[i] - C1, -C1);
                Cmin[i] = Math.min(ST[i] -E2 + C2, C2);
                Total[i] = Cmax[i] + Cmin[i];

            }

    	}else if (choice.equalsIgnoreCase("E1")){
        	double E = input[0];
        	double C = input[1];
        	double P = input[2];

        	stepS0 = E*2 / numSs;

            ST = new double[numSs+1];
            Profit = new double[numSs+1];

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;
                Profit[i] = Math.max(ST[i] - E - C, -C)+Math.max(E-ST[i] - P, -P);
            }
    	}else if (choice.equalsIgnoreCase("E2")){
        	double E = input[0];
        	double C = input[1];
        	double P = input[2];

        	stepS0 = E*2 / numSs;

            ST = new double[numSs+1];
            Profit = new double[numSs+1];

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;
                Profit[i] = -Math.max(ST[i] - E - C, -C)-Math.max(E-ST[i] - P, -P);
            }
    	}else if (choice.equalsIgnoreCase("E3")){
        	double E = input[0];
        	double C = input[1];
        	double P = input[2];

        	stepS0 = E*2 / numSs;

            ST = new double[numSs+1];
            Profit = new double[numSs+1];

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;
                Profit[i] = Math.max(ST[i] - E - C, -C)+2*Math.max(E-ST[i] - P, -P);
            }
    	}else if (choice.equalsIgnoreCase("E4")){
        	double E = input[0];
        	double C = input[1];
        	double P = input[2];

        	stepS0 = E*2 / numSs;

            ST = new double[numSs+1];
            Profit = new double[numSs+1];

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;
                Profit[i] = -Math.max(ST[i] - E - C, -C)-2*Math.max(E-ST[i] - P, -P);
            }
    	}else if (choice.equalsIgnoreCase("E5")){
        	double E = input[0];
        	double C = input[1];
        	double P = input[2];

        	stepS0 = E*2 / numSs;

            ST = new double[numSs+1];
            Profit = new double[numSs+1];

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;
                Profit[i] = 2*Math.max(ST[i] - E - C, -C)+Math.max(E-ST[i] - P, -P);
            }
    	}else if (choice.equalsIgnoreCase("E6")){
        	double E = input[0];
        	double C = input[1];
        	double P = input[2];

        	stepS0 = E*2 / numSs;

            ST = new double[numSs+1];
            Profit = new double[numSs+1];

            double curST = 0;
            for(int i=0;i<=numSs;i++) {

                ST[i] = curST;
                curST += stepS0;
                Profit[i] = -2*Math.max(ST[i] - E - C, -C)-Math.max(E-ST[i] - P, -P);
            }
    	}
    }


    public void actionPerformed(ActionEvent evt) {

    	if (evt.getSource() instanceof JComboBox) {
    		 JComboBox JCB = (JComboBox) evt.getSource();
             String JCB_Value = (String) JCB.getSelectedItem();
             choice = JCB_Value.substring(0, JCB_Value.indexOf(":"));
    		 setupInput(choice);
    		 updateGraph();
    		 chartPanel.validate();
    		 inputPanel.validate();
    	}
    	for (int i=0; i<numInput; i++)
    	 if (evt.getActionCommand().equals("Input"+(i+1))) {
    		 input[i] = Double.parseDouble(in[i].getText());
    		 updateGraph();
    		 chartPanel.validate();
    	 }

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
