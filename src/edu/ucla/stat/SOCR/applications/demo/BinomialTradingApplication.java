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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import edu.ucla.stat.SOCR.util.ValueSlider;
import edu.ucla.stat.SOCR.core.IExperiment;
import edu.ucla.stat.SOCR.util.FloatTextField;

import javax.swing.*;

import edu.uah.math.devices.Parameter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.PieDataset;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import edu.ucla.stat.SOCR.applications.Application;


/**
 * The Binomial Trading Application demonstrates the Price of a stock call with time:
 * 
 * Inputs:
 * S0 		= Price of the stock at time zero (now).
 * E 		= Exercise price (it is exercised at the end if price of stock at the end > E).
 * t 		= Time until expiration in years, e.g. if t = 73 days, then t = 0.2.
 * r 		= Interest rate per year.
 * sigma 	= Annual volatility.
 * n 		= Number of periods that we divide the time to expiration (here n = 2).
 * 
 */
public class BinomialTradingApplication extends Application  implements Observer, IExperiment, ActionListener{
	protected final int CHART_SIZE_X = 500;
    /**
	 * @uml.property  name="cHART_SIZE_Y"
	 */
    protected final int CHART_SIZE_Y = 400;
    
    protected final String GRAPH 	= "Binomial Option Pricing Model";
  //  protected final String GRAPH2 	= "GRAPH2";
    protected JPanel jgraphPanel;
    protected ChartPanel chartPanel2; 
    protected JGraph jgraph;
    int nodeCount = 0;
	int edgeCount = 0;
	int cellCount = 0;
	int YSize=40;
	int XSize=100;
	
	 private DecimalFormat formatter = new DecimalFormat("#0.0##");
	DefaultGraphCell[] cells = new DefaultGraphCell[100];
	
    protected JSplitPane upContainer;

    protected int tabbedPaneCount = 0;
	public JTabbedPane tabbedPanelContainer;
	 
    JToolBar toolBar;
	JPanel leftPanel = new JPanel();
	Box inPanel = Box.createVerticalBox();
	Box inputPanel = Box.createVerticalBox();

	
	protected JTextField inSo, inEP, inR, inSigma;
	//ValueSlider tDaysSlider;
	FloatTextField nSlider, tDaysSlider;
/*	public JPanel masterPanel;
    public JSplitPane vSplitPane;
    public JPanel controlPanel = new JPanel(new BorderLayout());
    public JPanel graphPanel = new JPanel(new BorderLayout());*/
    
	// Binomial Trading Application Variables
	double 	So = 30;	//Price of the stock at time zero (now).
	double 	EP = 29;	//Exercise price (it is exercised at the end if price of stock at the end > EP). 
	int 	t_days=73;	//0<=t_days<=365; Time until expiration in days, e.g. t_days = 73 days,
	double 	t = ((double)(t_days))/365;		
						//0<=t<=1; Time until expiration in years, e.g. if t_days = 73 days, then t = 0.2.
	double 	rate=0.05; 	//Interest rate per year.
	double 	sigma=0.3;	//Annual volatility.
	int		n = 3;		//Number of periods that we divide the time to expiration (here n = 2).
	double  rp=Math.pow(1+rate, t/n)-1;
						// rp = interest rate-per-period
	double 	up= Math.exp(sigma*Math.sqrt(t/n));
						// proportion UP price
	double 	down= 1/up;	// proportion DOWN price
	double 	probUp= (1+rp-down)/(up-down);
						// Probability of Going UP. Prob of going DOWN is respectively (1-p)

	double[][] 	Price;	// Price of the Stock Price[k][l] is the price of the stock at level k (0<=k<=n)
						// and outcome l (0<=l<n+1)
	double[][] 	Diff;	// Difference = Price of the Call Diff[k][l] is the Call-Price of the stock at 
						// level k (1<=k<=n) and outcome l (0<=l<n+1)
	String	choice;		// Whether the option pricing is European or American, Call or Put
	JComboBox gChoices;
	
    /**
     * This is the main constructor of BinomialTradingApplication objects.
     */
    public BinomialTradingApplication() {
    	setName("BinormialTradingApplication");
    	initInputPanel();
    	init();
    }

    /**
     * This method initializes the application.
     */
    public void init(){  
    	tabbedPanelContainer = new JTabbedPane();
    
    	initJGraphPanel();
    	initGraphPanel2();
    	
    	addTabbedPane(GRAPH, jgraphPanel);
    	//addTabbedPane("GRAPH2", graphPanel2);
    	tabbedPanelContainer.addChangeListener(new ChangeListener(){
    		public void stateChanged(ChangeEvent e) {
    			if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==GRAPH) {
    				jgraphPanel.removeAll();
    				//setChart();

    			}/*else 	if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==GRAPH2) {
    				//					
    				//setInputPanel();
    			}*/
    		}
    	});
    	
    	/* The following components need to be set
    	 * 1. Control JPanel
    	 * 		1.a Floating-point Text-Field for So = Price of the stock at time zero, So>=0.
    	 * 		1.b Floating-Point Text-Field for EP = Exercise price (it is exercised at the end if price of stock at the end > EP), EP>=0.
    	 * 		1.c Integer Slider for t = Time until expiration in years, 0<=t<=365.
    	 * 		1.d Floating-Point Text-Field for r = Interest rate per year (0.000<=r).
    	 * 		1.e Floating Point Text-Field for sigma = Annual volatility (0.000<=sigma).
    	 * 		1.f Integer Slider for n = Number of periods that we divide the time to expiration (0<=n<=1000).
    	 * 
    	 * 2. Graph JPanel
    	 * 		2.a For now, make this Graph panel just include a JTextArea where we will print out results
    	 * 				for debugging the calculator. Later, we'll replace this by a (Node, Edge)-Graph object
    	 * 
    	 */
    	
    	/*masterPanel = new JPanel();
    	masterPanel.setSize(new Dimension (500,500));
    	controlPanel.add(new JTextArea("Control Panel"));
    	graphPanel.add(new JTextArea("Graph Panel"));
    	
    	vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
    			new JScrollPane(controlPanel), new JScrollPane(graphPanel));
    	vSplitPane.setOneTouchExpandable(true);
    	vSplitPane.setDividerLocation(150);
        //add(vSplitPane);
    	// What should this be - how/where can we attach this pane to the main Panel?
		masterPanel.add(vSplitPane);
		masterPanel.validate();
		getDisplayPane().removeAll();
		getDisplayPane().add(masterPanel, BorderLayout.CENTER);*/
	  	
	  	upContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(leftPanel), new JScrollPane(tabbedPanelContainer));
	 
		this.getMainPanel().removeAll();
		this.getMainPanel().add(new JScrollPane(upContainer), BorderLayout.CENTER);			
	  	this.getMainPanel().validate();
	  	
    	//initGraph();
	  	updateAllNodes();
    }
    
    public void addTabbedPane(String name, JComponent c){
        tabbedPanelContainer.addTab(name, c);
        tabbedPaneCount++;
    }
    
    /* The following components need to be set
	 * 1. Control JPanel
	 * 		1.a Floating-point Text-Field for So = Price of the stock at time zero, So>=0.
	 * 		1.b Floating-Point Text-Field for EP = Exercise price (it is exercised at the end if price of stock at the end > EP), EP>=0.
	 * 		1.c Integer Slider for t = Time until expiration in years, 0<=t<=365.
	 * 		1.d Floating-Point Text-Field for r = Interest rate per year (0.000<=r).
	 * 		1.e Floating Point Text-Field for sigma = Annual volatility (0.000<=sigma).
	 * 		1.f Integer Slider for n = Number of periods that we divide the time to expiration (0<=n<=1000).
	 */
    private void initInputPanel(){
    	leftPanel = new JPanel();

    	//inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
    	//inPanel.setLayout(new BoxLayout(inPanel, BoxLayout.PAGE_AXIS));
    	JPanel jp0 = new JPanel();  	
    	jp0.add(new JLabel("S0"));   	
    	inSo = new JTextField(14);
    	inSo.addActionListener(this);
    	inSo.setText("30");
    	inSo.setActionCommand("S0");
    	jp0.add(inSo);
    	inputPanel.add(jp0);
    	
    	JPanel jp1 = new JPanel();  	
    	jp1.add(new JLabel("E"));
    	inEP = new JTextField(14);
    	inEP.setText("29");
    	inEP.addActionListener(this);
    	inEP.setActionCommand("EP");
    	jp1.add(inEP);
    	inputPanel.add(jp1);
    		
    	//tDaysSlider = new ValueSlider("Days", 0,  365, t_days, true);
    	tDaysSlider = new FloatTextField("Days to expiration", t_days, 0, 365, false);
    	tDaysSlider.addObserver(this);
    	inputPanel.add(tDaysSlider);
    	
    	JPanel jp3 = new JPanel();  	
    	jp3.add(new JLabel("Interest"));
    	inR = new JTextField(14);
    	inR.setText("0.05");
    	inR.addActionListener(this);
    	inR.setActionCommand("Rate");
    	jp3.add(inR);
    	inputPanel.add(jp3);
    	
    	JPanel jp4 = new JPanel();  	
    	jp4.add(new JLabel("Sigma"));
    	inSigma = new JTextField(14);
    	inSigma.addActionListener(this);
    	inSigma.setText("0.3");
    	inSigma.setActionCommand("Sigma");
    	jp4.add(inSigma);
    	inputPanel.add(jp4);
    	 	
    	
    	nSlider = new FloatTextField("Number of steps", n, 0,1000, false);
    	nSlider.addObserver(this);
    	inputPanel.add(nSlider);

		// European Option Pricing or American Option Pricing
		gChoices = new JComboBox();
		gChoices.setToolTipText("Select Binomial Option Pricing Method");
		gChoices.addItem("European Call");
		gChoices.addItem("European Put");
		gChoices.addItem("American Call");
		gChoices.addItem("American Put");

		gChoices.addActionListener(this);

		JPanel jp5 = new JPanel();
		jp5.add(new JLabel("Select Binomial Option Pricing Method"));
		inputPanel.add(jp5);

		JPanel jp6 = new JPanel();
		jp6.add(gChoices);
		inputPanel.add(jp6);

		choice = "European Call";
		inputPanel.add(inPanel);
    	      	
    	leftPanel.add(inputPanel, BorderLayout.NORTH);
    	
    	String text= " European Option: No early exercise allowed \n American Option: Early exercise allowed \n S: Stock Price \n C: Call Price \n P: Put Price \n\n At each node:\n First value = stock price\n Second value = call price";
    	getRecordTable().setText(text);

    }
    
    void initJGraphPanel(){
    	jgraphPanel = new JPanel();
    	jgraphPanel.setLayout(new BoxLayout(jgraphPanel, BoxLayout.Y_AXIS));
		// This method defines the Graph structure as an empty Graph object

    	GraphModel model = new DefaultGraphModel();
    	jgraph= new JGraph(model);

    	jgraphPanel.add(jgraph);
		jgraphPanel.validate();
    }
    
    void initGraphPanel2(){
    //	graphPanel2 = new JPanel();
    //	graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));
		// This method defines the Graph structure as an empty Graph object
    	JFreeChart chart = createEmptyChart(null);	//create a empty graph first
		chartPanel2 = new ChartPanel(chart, false); 
		chartPanel2.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
	//	graphPanel2.add(chartPanel2);
	//	graphPanel2.validate();
    }
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
    void updateGraph(){
    	//This method will later update/redraw/repaint the (Node, Edge)-Graph
    	// For now, we just print out the results in JTextArea
    	
    	// Price of the Stock Price[k][l] is the price of the stock at level k (0<=k<=n)
		// and outcome l (0<=l<n+1)
    	GraphModel model = new DefaultGraphModel();
    	jgraph= new JGraph(model);
    	cells = new DefaultGraphCell[((n+1)*(n+2)/2)*3];
    	cellCount = 0;
    	
    	int XSpace = 130;
    	int YSpace = 30;
    	int YMiddle= 20+n*YSpace;  //150
    	
    	for (int k=0; k<=n; k++) {
    		//System.out.println("============Start-of-level("+k+")==================");
    		for (int l=0; l<=k; l++) {
    			// Print to GraphPanel's JTextArea
    			int j= -k+l*2;
    			 
    			if(choice.charAt(9)=='P'){			//For put option
    				if (k==n) {			// For the last column, k=n, (in the money), 
    									// we need to color GREEN or BLUE the node-background 
    									// to indicate if the Call Price (C) is > 0 (green) or
    									// <= 0 (blue)
    					if (j<0) {
    						if (Diff[k][l]>0)
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace-20), Color.green);
    						else
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace-20), Color.blue);
    					}
    					else if (j>0) {
    						if (Diff[k][l]>0)
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace+20), Color.green);
    						else
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace+20), Color.blue);
    					}
    					else { 
    						if (Diff[k][l]>0)
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), YMiddle, Color.green); 
    						else 
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), YMiddle, Color.blue); 
    					}
    				} else {		// For all columns before the last (in the money) column, k<n)!
    					if (j<0)
    						addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    								" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    								(10+k*XSpace), (YMiddle+j*YSpace-20));
    					else if (j>0)
    						addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    								" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    								(10+k*XSpace), (YMiddle+j*YSpace+20));
    					else addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    							" P["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    							(10+k*XSpace), YMiddle);
    				}
    			} else {		//For call option
    				if (k==n) {			// For the last column, k=n, (in the money), 
    									// we need to color GREEN or BLUE the node-background 
    									// to indicate if the Call Price (C) is > 0 (green) or
    									// <= 0 (blue)
    					if (j<0) {
    						if (Diff[k][l]>0)
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace-20), Color.green);
    						else
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace-20), Color.blue);
    					}
    					else if (j>0) {
    						if (Diff[k][l]>0)
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace+20), Color.green);
    						else
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), (YMiddle+j*YSpace+20), Color.blue);
    					}
    					else { 
    						if (Diff[k][l]>0)
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), YMiddle, Color.green); 
    						else 
    							addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    									" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    									(10+k*XSpace), YMiddle, Color.blue); 
    					}
    				} else if (choice.charAt(0) == 'A' && Diff[k][l] == Math.abs(Price[k][l] - EP)){
    					// For all columns before the last (in the money) column, k<n)! and possibly exercised early
    					if (j<0)
    						addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    								" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    								(10+k*XSpace), (YMiddle+j*YSpace-20), Color.magenta);
    					else if (j>0)
    						addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
									" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
									(10+k*XSpace), (YMiddle+j*YSpace+20), Color.magenta);
    					else
    						addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    								" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
									(10+k*XSpace), YMiddle, Color.magenta);
    				} else {		// For all columns before the last (in the money) column, k<n)!
    					if (j<0)
    						addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    								" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    								(10+k*XSpace), (YMiddle+j*YSpace-20));
    					else if (j>0)
    						addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    								" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    								(10+k*XSpace), (YMiddle+j*YSpace+20));
    					else addNode("S["+k+","+l+"]="+formatter.format(Price[k][l])+
    							" C["+k+","+l+"]="+formatter.format(Diff[k][l]), 
    							(10+k*XSpace), YMiddle);
    					// Also Print to STD_OUT
    					//	System.out.println("j="+j);
    					//	System.out.println("Price["+k+"]["+l+"]="+Price[k][l]);
    					//	System.out.println("Diff["+k+"]["+l+"]="+Diff[k][l]);
    				}
    			}
    		}
    		//System.out.println("============End-of-level("+k+")==================\n");
    	}
    	//System.out.println("cellCount="+cellCount);
    
    	
    	for (int k=0; k<n; k++) {
    		//System.out.println("============Start-of-level("+k+")==================");
    		for (int l=0; l<=k; l++) {
    		//	System.out.println((k*(k+1)/2+l)+"->"+((k+1)*(k+2)/2+l) );
    			
    		addEdge((k*(k+1)/2+l),((k+1)*(k+2)/2+l));
    		//System.out.println((k*(k+1)/2+l)+"->"+((k+1)*(k+2)/2+l+1) );
    		addEdge((k*(k+1)/2+l),((k+1)*(k+2)/2+l+1));
    		}
    	//	System.out.println("============End-of-level("+k+")==================\n");
    	}
    	
    //	System.out.println("cellCount="+cellCount);
    	DefaultGraphCell[] cellsCache = new DefaultGraphCell[cellCount];
    	
    	for (int i=0;i<cellCount; i++)
    		cellsCache[i]=cells[i];
    	
    	
    	jgraph.getGraphLayoutCache().insert(cellsCache);
    	Dimension d = jgraph.getPreferredSize();	
    	//System.out.println(d.width +","+d.height);
    	jgraph.setPreferredSize(new Dimension(d.width+150, d.height));
    
    	jgraphPanel.removeAll();
    	JScrollPane jsp = new JScrollPane(jgraph);
    	jgraphPanel.add(jsp);
    	jgraphPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
    	JViewport jv = jsp.getViewport();
    	jv.setViewPosition(new Point(0, YMiddle-150));
		jgraphPanel.validate();
    } 
       
    
    public void actionPerformed(ActionEvent evt) {
    	/* Here we need the action listeners for all of the user-specified parameters in the COntrol-Panel
    	 * See init() method!!!
    	 * 		1.a Floating-point Text-Field for So = Price of the stock at time zero, So>=0.
    	 * 		1.b Floating-Point Text-Field for EP = Exercise price (it is exercised at the end if price of stock at the end > EP), EP>=0.
    	 * 		1.c Integer Slider for t = Time until expiration in years, 0<=t<=365.
    	 * 		1.d Floating-Point Text-Field for r = Interest rate per year (0.000<=r).
    	 * 		1.e Floating Point Text-Field for sigma = Annual volatility (0.000<=sigma).
    	 * 		1.f Integer Slider for n = Number of periods that we divide the time to expiration (0<=n<=1000).
		 */
    	//System.out.println(evt.getActionCommand());

		if (evt.getSource() instanceof JComboBox)
		{
			JComboBox JCB = (JComboBox)evt.getSource();
			choice = (String)JCB.getSelectedItem();
			choice= choice.toLowerCase();
			chartPanel2.validate();
			inputPanel.validate();
		}else if (evt.getActionCommand().equals("S0")) {
    		 So = Double.parseDouble(inSo.getText());
    	}else if (evt.getActionCommand().equals("EP")) {
    		 EP = Double.parseDouble(inEP.getText());
    	}else if (evt.getActionCommand().equals("Sigma")) {
    		 sigma = Double.parseDouble(inSigma.getText());
    	}else if (evt.getActionCommand().equals("Rate")) {
    		 rate = Double.parseDouble(inR.getText());
    	}
    	
    	updateAllNodes();
    }
    
    public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
		if (n!=nSlider.getFloatValue()){
			n = (int)nSlider.getFloatValue();	
			//System.out.println("set n = " +n);
		}else if (t_days!=tDaysSlider.getFloatValue()){
			t_days = (int)tDaysSlider.getFloatValue();	
			//System.out.println("set t_days = " +t_days);
		}
		
		updateAllNodes();
		return;
	}
    
    public void mouseClicked(MouseEvent event){}
    
    public String getOnlineDescription(){ 
    	return new String("http://www.socr.ucla.edu/");
    }
    public Container getDisplayPane() {
		 JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
	                getMainPanel(), getTextPanel() );
	        return container;
	}
	// Needs to be overwritten by objects that extend this class
    public String getAppletInfo(){
    	return new String ("SOCR Applications: http://www.socr.ucla.edu/htmls/app \n");
    }

	public void update() {
		updateAllNodes();
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
		// DO we need this?
	}

	 public DefaultGraphCell createVertex(String name, double x,
				double y, double w, double h, Color bg, boolean raised) {

			// Create vertex with the given name
			DefaultGraphCell cell = new DefaultGraphCell(name);
		/*	ob.x = x; 
			ob.y = y;*/
		//	System.out.println("adding node:"+ob.toString()+" to cell "+cellCount +"at ("+x+","+y+")");
			// Set bounds
			GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(
					x, y, w, h));

			// Set fill color
			if (bg != null) {
				GraphConstants.setGradientColor(cell.getAttributes(), bg);
				GraphConstants.setOpaque(cell.getAttributes(), true);
			}

			// Set raised border
			if (raised)
				GraphConstants.setBorder(cell.getAttributes(), BorderFactory
						.createRaisedBevelBorder());
			else
				// Set black border
				GraphConstants.setBorderColor(cell.getAttributes(), Color.black);

			// Add a Floating Port
			cell.addPort();

			return cell;
		}
	 
	 
	 public void addNode(String name, int x, int y ){
	    	//System.out.println("adding node:"+node.toString()+" to cell "+cellCount);
	    	
	    	cells[cellCount] = createVertex(name, x, y, XSize, YSize, Color.ORANGE, true);
	    	nodeCount++;
	    	cellCount++;
	 }

	 public void addNode(String name, int x, int y, Color backgroundColor){
	    	//System.out.println("adding node:"+node.toString()+" to cell "+cellCount);
	    	
	    	cells[cellCount] = createVertex(name, x, y, XSize, YSize, backgroundColor, true);
	    	nodeCount++;
	    	cellCount++;
	 }

	    public void addEdge( int source, int target){
	    	//System.out.println("adding edge: "+source+"->"+ target+ "to cell "+cellCount);
	    	DefaultEdge edge = new DefaultEdge();
	    	edge.setSource(cells[source].getChildAt(0));
			edge.setTarget(cells[target].getChildAt(0));
			int arrow = GraphConstants.ARROW_CLASSIC;
			GraphConstants.setLineEnd(edge.getAttributes(), arrow);
			GraphConstants.setEndFill(edge.getAttributes(), true);
		//	Node s = (Node)cells[source].getUserObject();
		//	Node t = (Node)cells[target].getUserObject();
		/*	_edge.arrow_x=(int)(s.x+t.x)/2;
			_edge.arrow_y=(int)(s.y+t.y)/2;*/
			
	    	cells[cellCount] = edge;
	    	edgeCount++;
	    	cellCount++;
	    }
	    
	 
	/* This method updates all the Nodes in the Binomial Trading Application
	 * using the current values of the user-specified parameters (see top).
	 */
	public void updateAllNodes() {
		//System.out.println("so="+So+" E="+EP+" Days="+t_days+" interest="+rate+" Sigma="+sigma+" n="+n);
    	
		if (0<=t_days && t_days <=365) t = ((double)(t_days))/365;
		else {
			System.out.println("Error -- The time (in days) must be in the range [0; 365]!");
			t_days = 0;
			t = 0;
		}

		up= Math.exp(sigma*Math.sqrt(t/n));    	// proportion UP price
		down= 1/up;								// proportion DOWN price

		Price = new double [n+1][n+1];	
								// Price of the Stock Price[k][l] is the price of the stock at
								// level k (0<=k<=n) and outcome l (0<=l<n+1)
		Diff = new double [n+1][n+1];	
								// Difference = Price of the Call Diff[k][l] is the Call-Price of the stock at 
								// level k (1<=k<=n) and outcome l (0<=l<n+1)

		rp=Math.pow(1+rate, t/n)-1; 		// rp = interest rate-per-period
		//probUp= (1+rp-down)/(up-down);		// Probability of Going UP. Prob of going DOWN is respectively (1-p)
		double dt = t/n;
		double a = Math.pow(Math.E,rate*dt);
		probUp = (a-down)/(up-down);
		// Compute the Stock Prices
    	for (int k=0; k<=n; k++) {
    		//System.out.println("============Computing-StockPrices-at-level("+k+")==================");
    		for (int l=0; l<=k; l++) {
    			Price[k][l] = So * Math.pow(up, k-l)*Math.pow(down, l);
    			
    			// Print to GraphPanel's JTextArea
    			
    			// Also Print to STD_OUT
    			//System.out.println("Price["+k+"]["+l+"]="+Price[k][l]);
    		}
    		//System.out.println("============End-of-level("+k+")==================\n");
    	}

    	// Compute the DIFF values, Prices of calls
    	for (int k=n; k>=0; k--) {
    		//System.out.println("============Computing DIFF (Price of call/put) at level("+k+")==================");
    		if(choice.indexOf("put")!=-1){		//put
    			if (k==n) {			// These are the last leaf-nodes of the Binomial tree
    				for (int l=0; l<=k; l++) {
    					if (Price[k][l] < EP)  Diff[k][l] = EP - Price[k][l];
    					else Diff[k][l] = 0;	// Price is NOT Excercised
    				}
    			}
    			else {
    				for (int l=0; l<=k; l++) {
    					// Print to GraphPanel's JTextArea
    					//Diff[k][l] = (Diff[k+1][l]*probUp + Diff[k+1][l+1]*(1-probUp)) / (1+rp);  --save
    					Diff[k][l] = (Diff[k+1][l]*probUp + Diff[k+1][l+1]*(1-probUp))*Math.pow(Math.E, -rate*dt);
						if (choice.indexOf("american")!=-1 && (Diff[k][l] < EP - Price[k][l]))
						{
							Diff[k][l] = Price[k][l] - EP;
						}
 
    					// Also Print to STD_OUT
    					//System.out.println("Diff["+k+"]["+l+"] = "+Diff[k][l]);
    				}
    				//System.out.println("============End-of-level("+k+")==================\n");
    			}
    		} else {						//call
    			if (k==n) {			// These are the last leaf-nodes of the Binomial tree
    				for (int l=0; l<=k; l++) {
    					if (Price[k][l] > EP)  Diff[k][l] = Price[k][l] - EP;
    					else Diff[k][l] = 0;	// Price is NOT Excercised
    				}
    			}
    			else {
    				for (int l=0; l<=k; l++) {
    					// Print to GraphPanel's JTextArea
    					//Diff[k][l] = (Diff[k+1][l]*probUp + Diff[k+1][l+1]*(1-probUp)) / (1+rp);  --save
    					Diff[k][l] = (Diff[k+1][l]*probUp + Diff[k+1][l+1]*(1-probUp))*Math.pow(Math.E, -rate*dt);
						if (choice.indexOf("american")!=-1 && (Diff[k][l] < Price[k][l] - EP))
						{
							//int americanCall = (Price[k][l]*probUp + Price[k][l]*(1-probUp))*Math.pow(Math.E, -rate*dt);
							//if (americanCall > Diff[k][l])
							//	Diff[k][l] = americanCall;
							Diff[k][l] = Price[k][l] - EP;
						}
 
    					// Also Print to STD_OUT
    					//System.out.println("Diff["+k+"]["+l+"] = "+Diff[k][l]);
    				}
    				//System.out.println("============End-of-level("+k+")==================\n");
    			}
    		}
    	}
   	
		updateGraph();
	}
}
