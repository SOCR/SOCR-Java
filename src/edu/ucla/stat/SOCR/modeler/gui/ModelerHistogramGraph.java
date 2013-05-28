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


import edu.ucla.stat.SOCR.distributions.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import edu.ucla.stat.SOCR.modeler.Modeler;
import edu.ucla.stat.SOCR.modeler.gui.ModelerColor;
import edu.ucla.stat.SOCR.util.Graph;

/*Histogram is a basic graph for displaying the density and moments for a specified
interval data set. */
public class ModelerHistogramGraph extends Graph implements MouseListener {
	
	private boolean debug= false;
	//Constants
	public final static int FREQ = 0, REL_FREQ = 1, DENSITY = 2; // looks like relative frequency.
	public final static int NONE = 0, MSD = 1, BOX = 2, MAD = 3, MMM = 4;
	//Variables
 	protected int axisType, summaryStats = 1, type = REL_FREQ, intervals;
 	//public double xMin, xMax, yMax, yMin;
 	protected double width; // change to protected because I need to modify it in ModelHistogram.
 //	protected double left, right; // used to set domain
 	
	protected int currentXUpperBound = -5;
	protected int currentXLowerBound = 5;
	//protected double[] modelX = null, modelY = null;
	//Objects

 	protected IntervalData data;

	protected Domain domain = null; // x domain
	//private Font f = new Font("sansserif", Font.PLAIN, 11);
	protected int modelType;
	/**This general constructor creates a new data graph with a specified data set and axis type.*/
	protected ArrayList listOfTicks = new ArrayList();

	private int count;
	static final double pi = 3.14159265358979323846;
	
	//store input data in graph panel
	private Vector<Double> graphInputDataX;
	private Vector<Integer> graphInputDataY;
		

	Dimension winSize;
	
	private Vector<Double> values = new Vector<Double>();
	private int valCount = 0;
	public double[] modelX = null, modelY = null;
	public double[] modelX1 = null, modelY1 = null;
	public double[] modelX2 = null, modelY2 = null;
	protected int modelCount = 1;
	
	//change all left/graphLeft to xMin ---Jenny
	//private double left = -5;
	//private double right = 5;
	//protected double graphLeft = ModelerConstant.GRAPH_LOWER_LIMIT;
	//protected double graphRight = ModelerConstant.GRAPH_UPPER_LIMIT;
	protected boolean drawUserClicks = true,updateGuiSlider= false;
	
	private static boolean zoomIn = false;
	private static boolean zoomOut = false;
	
	protected ModelerGui modelerGuiLink;
	
	public ModelerHistogramGraph(double a, double b, double w) {
		this.addMouseListener(this);
		winSize = this.getSize();
		winSize.width = (int) w;
		axisType=1;
	//	left = a;
	//	right = b;
		xMin= a; xMax = b;  //Jenny
		width = w;
	//	System.out.println("graph init width ="+width+ " left="+a+" xmin ="+xMin);
		setIntervalData();

		count = data.getDomain().getSize();
		
		graphInputDataX = new Vector<Double>(10, 10);
		graphInputDataY = new Vector<Integer>(10, 10);
	}
	public ModelerHistogramGraph(IntervalData d, int t){
		
		setMargins(35, 20, 30, 10);
		axisType = t;
		setIntervalData(d);
	//	left = 5;
	//	right = -5;
		yMax = 20;
		xMin = -5;
		xMax = 5;
	}
	
	public ModelerHistogramGraph(IntervalData d, int t, int modelType){
		this(d, t);
		this.modelType = modelType;
	}
	/**This default constructor creates a new data graph with a new data set on the interval [0, 1]
	with subintervals of length 0.1.*/
	public ModelerHistogramGraph(){
		this(new IntervalData(0, 1, 0.1), 1); // calls constructor 1 with default values
	}

	public void setDebug(boolean setting){
		debug= setting;
	}
	public void setGuiLink(ModelerGui l){
		this.modelerGuiLink = l;
	}
	/**This method paints the graph of the getDensity function, empirical getDensity function,
	moment bar, and empirical moment bar*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if (debug)
			System.out.println("ModelerHistogramGrap: paintComponent" );
		double x, d;
		int size = data.getSize();
		domain = data.getDomain();
		double newWidth = (xMax - xMin) / 10; // 10 looks good on graph.

		width = domain.getWidth();
		
		intervals = domain.getSize();
		if (debug)
			System.out.println("ModelerHistogramGrap: before graph domain width ="+width+ " intervals= "+intervals +" yMax="+yMax);
		//Determine yMax
		if (type == DENSITY) {
			yMax = data.getMaxDensity();
		}
		else if (type == REL_FREQ) {
			yMax = data.getMaxRelFreq(); 
		}else if (type == FREQ) {
			yMax = data.getMaxFreq(); 
		}

		if (yMax == 0) {
			yMax = 1; // why???
		}
		if(modelerGuiLink.getYScaleMax()>yMax)
			yMax = modelerGuiLink.getYScaleMax();
		//elseif(modelerGuiLink.getYScaleMax()<yMax)
		//modelerGuiLink.setYScale(yMax); could cause loop Jenny
		
		if (debug){
			System.out.println("ModelerHistogramGrap: after graph domain width ="+width+ " intervals= "+intervals +" yMax="+yMax);
			System.out.println("ModelerHistogramGrap: Graph paint xMin="+xMin+ " yMax="+yMax+ " xMax="+xMax);
		}
		setScale(xMin, xMax, 0, yMax);
		g.setColor(ModelerColor.HISTOGRAM_AXIS); // this is the coordinate grids.
		if (modelType == Modeler.FOURIER_TYPE || modelType == Modeler.WAVELET_TYPE) {
			xMin = 0;
			setScale(xMin, xMax, -yMax, yMax);
	 		super.drawAxis(g, -yMax, yMax, 0.1 * yMax, xMin, VERTICAL); // 6 args
			//super.drawAxis(g, xMin, xMax, (xMax - xMin) / 10, 0, HORIZONTAL, axisType, listOfTicks); // c must be 0.
	 		//System.out.println("graph drawAxis xMin="+xMin+" width="+width);
	 		super.drawAxis(g, xMin, xMax, (xMax-xMin)/10, 0, HORIZONTAL, axisType, listOfTicks); // c must be 0.

		} else {
	 		super.drawAxis(g, -yMax, yMax, 0.1 * yMax, xMin, VERTICAL); // 6 args
			//super.drawAxis(g, xMin, xMax, (xMax - xMin) / 10, 0, HORIZONTAL, axisType, listOfTicks); // c must be 0.
	 		//System.out.println("graph drawAxis xMin="+xMin+ " width="+width+" axisType="+axisType);
	 		super.drawAxis(g, xMin, xMax, (xMax-xMin)/10, 0, HORIZONTAL, axisType, listOfTicks); // c must be 0.
			
		}

		if (size > 0){
			for (int i = 0; i < intervals; i++){
				x = domain.getValue(i);
				if (type == FREQ) {
					d = data.getFreq(x);
					////////System.out.println("varHistogram fillBox type == FREQ");
				}
				else if (type == REL_FREQ) {
					d = data.getRelFreq(x);
					////////System.out.println("varHistogram fillBox type == REL_FREQ");

				}
				else  {
					d = data.getDensity(x);
					////////System.out.println("varHistogram fillBox else");

				}
				if (modelType == Modeler.CONTINUOUS_DISTRIBUTION_TYPE
					|| modelType == Modeler.DISCRETE_DISTRIBUTION_TYPE) {
					g.setColor(ModelerColor.HISTOGRAM_BAR_FILL); // Color.red. This is the bar fill-in. annie che.
					if (d > 0){
						//	System.out.println("graph fillBox width="+width+" x = " + x + ", x - width / 2 = " + (x - width / 2) + ", x + width / 2 = " + (x + width / 2) + ", d = " + d);
							fillBox(g, x - width / 2, 0, x + width / 2, d);
					}
					g.setColor(ModelerColor.HISTOGRAM_BAR_OUTLINE); // Color.black. This is the bar border. annie che.
					drawBox(g, x - width / 2, 0, x + width / 2, d);
				}

			}
			//Draw summary statistics
			
			if (modelType == Modeler.CONTINUOUS_DISTRIBUTION_TYPE
				|| modelType == Modeler.DISCRETE_DISTRIBUTION_TYPE) {
				
			
				g.setColor(ModelerColor.MODEL_OUTLINE); // Color.red
				int j = getSize().height - 10;
	
			//	System.out.println("graph paint modelType="+modelType+" mean="+data.getMean()+" sd="+data.getSD()+" j="+j);
				
				switch(summaryStats){
				case 1:		//True mean and standard deviation
					fillBoxPlot(g, data.getMean(), data.getSD(), j);
					break;
				case 2:		//Interval mean and standard deviation
					fillBoxPlot(g, data.getIntervalMean(), data.getIntervalSD(), j);
					break;
				case 3:		//Five number summary
					fillBoxPlot(g, data.getMinValue(), data.getQuartile(1), data.getMedian(), data.getQuartile(3), data.getMaxValue(), j);
					break;
				case 4:		//mMedian and mean absolute deviation
					fillBoxPlot(g, data.getMedian(), data.getMAD(), j);
					break;
				}
			}
  		}
		
		paintModelDistribution(g);
	}
	
	protected void paintModelDistribution(Graphics g) {

		 int MULTIPLE_DEFAULT_VALUE = 3;
		 
		Graphics2D G2 = (Graphics2D) g;
		G2.setStroke(new BasicStroke(3));
		float[] HSBVal = new float[3];
		if (modelX != null && modelX.length > 0) {
			double maxXVal = modelX[0], maxYVal = modelY[0];
			int terms = modelY.length; // how many dx.
			
			if (debug)
				System.out.println("paintModelDistribution yMax="+yMax);
		// look for the max value of x (r.v. values) and max of density. (to rescale?)
			for (int j=1; j< terms; j++)
			{
				if ( modelX[j]!=Double.POSITIVE_INFINITY && modelX[j] > maxXVal )
					maxXVal = modelX[j];
				if (modelY[j]!=Double.POSITIVE_INFINITY && modelY[j] > maxYVal )
					maxYVal = modelY[j];
			}
			if(maxYVal>yMax){				
				//System.out.println("paintModelDistribution maxYVal="+maxYVal);
				yMax = maxYVal;
			}
			
			if(debug)
				System.out.println("ModelerHistogramGraph paintComponent maxXVal = " + maxXVal + "; maxYVal = " + maxYVal +"yMax="+yMax);
			// enlarge all the values 3 times. (why??)
			boolean rescale = false;
			int MULTIPLE = MULTIPLE_DEFAULT_VALUE; // was 3.
			if (modelType == Modeler.FOURIER_TYPE || modelType == Modeler.WAVELET_TYPE) {
				MULTIPLE = MULTIPLE_DEFAULT_VALUE;
				rescale = true;
			}
			
			if (rescale) {
				//System.out.println("rescaling !");
				for (int j=1; j< terms; j++)
				{
					if ( modelX[j] ==  maxXVal )
						modelX[j] = MULTIPLE;
					else
						modelX[j] = (float)(modelX[j]/maxXVal)* MULTIPLE;
					if ( modelY[j] ==  maxYVal )
						modelY[j] = MULTIPLE;
					else
						modelY[j] = (float)(modelY[j]/maxYVal)* MULTIPLE;
				}
			}
			for (int j=1; j< terms; j++) {
				if (modelY[j] == Float.POSITIVE_INFINITY) {
					modelY[j] = (float)(yMax - 0.01);
				}
			}


			/*********************************************************/
			/************ start  block modelCount = 8, 7, 6, 5 *******/

			/*********************************************************/
			/************ end block modelCount = 8, 7, 6, 5 **********/
			////////////System.out.println("ModelerHistogram end block 5,6 ,7 ,8");
			double xa, ya;
			int subLth = 0;
			double[] modelLineOfY = new double[modelY.length];
			double[] dataLineOfY = new double[modelY.length];
			double[] modelLineOfX = new double[modelY.length];
			if (modelType == Modeler.FOURIER_TYPE || modelType == Modeler.WAVELET_TYPE) {
				subLth = (int) (modelX.length);
			

				double xMaxData = 0, yMaxData = -10000, yMinData = 10000;
				double xMaxModel = 0, yMaxModel = -10000, yMinModel = 10000;
				double yMaxPlot = 0, yMinPlot = 0, xMinPlot = 0, xMaxPlot = subLth + 1;
				for (int i = 0; i < subLth; i++) {
					dataLineOfY[i] = modelX[i];//midy + (int) (ymult * modelX[i]);
					modelLineOfY[i] = modelY[i];//midy + (int) (ymult * modelY[i]);
					modelLineOfX[i] = i;//periodWidth * (i+1) / subLth;
				 	yMaxData = Math.max(yMaxData, Math.max(dataLineOfY[i], yMaxData));
					yMinData = Math.min(yMinData, Math.min(dataLineOfY[i], yMinData));
				 	yMaxModel = Math.max(yMaxData, Math.max(modelLineOfY[i], yMaxData));
					yMinModel = Math.min(yMinData, Math.min(modelLineOfY[i], yMinModel));
				}

				yMaxPlot = Math.max(yMaxData, yMaxModel);
				yMinPlot = Math.max(yMinData, yMinModel);
				
			/*	super.setPlotXMin(xMinPlot);
				super.setPlotXMax(xMaxPlot);
				super.setPlotYMin(yMinPlot - 1);
				super.setPlotYMax(yMaxPlot + 1);*/ //Jenny

				// End Change modelX and modelY.

				double x1 = (double) modelX[0];
				double y1 = (double) modelY[0];
				// first draw data
				for (int j = 1; j < subLth; j++) {
					x1 = modelLineOfX[j-1];
					y1 = dataLineOfY[j-1];
					xa = modelLineOfX[j];
					ya = dataLineOfY[j];
					G2. setColor(ModelerColor.HISTOGRAM_FOURIER_DATA);
					G2.setStroke(new BasicStroke(2f));

					drawLine(G2, xa, ya, x1, y1); // when modelCount == any #
					
				}
				
				// next draw model
				int m = 1; // test

				for (int j = 1; j < subLth; j++) {
					x1 = modelLineOfX[j-1];
					y1 = modelLineOfY[j-1];
					xa = modelLineOfX[j];
					ya = modelLineOfY[j];
					G2. setColor(ModelerColor.HISTOGRAM_FOURIER_MODEL);
					G2.setStroke(new BasicStroke(2f));
					drawLine(G2, xa, ya, x1, y1);
				}
			}
			else {
				subLth = (int) (modelX.length / modelCount);
				//System.out.println("subLth="+subLth);
				for (int j = 0; j < modelCount; j++) {
					if (j == 0) {
						// model curve
						G2.setStroke(new BasicStroke(3f));
						G2.setColor(ModelerColor.MODEL_OUTLINE);

					} else { // for more than one model such as mixed normal.
						G2.setStroke(new BasicStroke(2f));
						Color.RGBtoHSB((int) Math.floor((Math.random() * 256)),
								(int) Math.floor((Math.random() * 256)), (int) Math.floor((Math.random() * 256)), HSBVal);
						G2.setColor(Color.getHSBColor(HSBVal[0], HSBVal[1],
								HSBVal[2]));
					}
					double x1 = (double) modelX[0 + j * subLth];
					double y1 = (double) modelY[0 + j * subLth];
					//System.out.println("beginning subLth="+subLth+" xMax="+xMax+" xMin="+xMin+" x1="+x1+" y1="+y1);
					for (int i = 1; i < subLth; i++) {
						xa = modelX[i + j * subLth];
						ya = modelY[i + j * subLth];
						drawLine(G2, x1, y1, xa, ya); // when modelCount == any #
					
						x1 = xa;
						y1 = ya;
					}
					//System.out.println("ending xMax="+xMax+" xMin="+xMin+" x1="+x1+" y1="+y1);
				}
			} // not fourier or wavelet.
		}
	}

	
	 /**This method assigns the data and sets up graph paramters*/
	public void setIntervalData(IntervalData d){
		data = d;
		repaint();
	}
	public void setIntervalData() {
		//data = new IntervalData(left, right, width);
		if(modelerGuiLink!=null){
			width = (this.xMax - this.xMin) /modelerGuiLink.histBinNos;  //Jenny
			width /= 2;
			//System.out.println("graph setIntervalData xmin="+xMin+" xMax="+xMax+" bins=" +modelerGuiLink.histBinNos+" width="+width + "?=?"+(xMax-xMin)/(modelerGuiLink.histBinNos*2));	
		}
		
			//	Exception e = new Exception();
//	e.printStackTrace();
//	System.out.println("----");
		data = new IntervalData(xMin, xMax, width);  
		setIntervalData(data);
	}
	/**This method returns the data set.*/
	public IntervalData getIntervalData(){
		return data;
	}

	/**This method sets the plot style*/
	public void setType(int i){
		if (i < 0) i = 0; else if (i > 2) i = 2;
		type = i;
		repaint();
	}

	/**This method sets the axis type.*/
	public void setAxisType(int i){
		if (i < 0) i = 0; else if (i > 1) i = 1;
		axisType = i;
	}

	/**This method specifies the moments to display */
	public void showSummaryStats(int n){
		summaryStats = n;
		repaint();
	}

	public void setxMax(double xm) {
	//	System.out.println("graph setxMax="+xm);
		if(xm == Double.POSITIVE_INFINITY)
			xm = ModelerConstant.GRAPH_UPPER_LIMIT;
	//	System.out.println("graph setxMax="+xm);
		xMax = xm;
		//width = (this.xMax - this.xMin) / guiLink.histBinNos;  //Jenny
		//width /= 2;
		data = new IntervalData();
		setHist();
	}
	public void setxMin(double xm) {
		//System.out.println("graph setxMin="+xm);
	//	Exception e = new Exception();
	//	e.printStackTrace();
		if(xm == Double.NEGATIVE_INFINITY)
			xm = ModelerConstant.GRAPH_LOWER_LIMIT;
		xMin = xm;
		//width = (this.xMax - this.xMin) / guiLink.histBinNos;  //Jenny
		//width /= 2;
		data = new IntervalData();
		setHist();
	}
	public void setyMax(double ym) {
		
		//Exception e =new Exception();
		//e.printStackTrace();
		yMax = ym;
	}
	public void setyMin(double ym) {
		yMin = ym;
	}

     // added for varHistogram's sub classes like ModelHostogram (its only child so far!).
 	public void setPlotXMin(double input) {
 		//System.out.println("setting PlotXMin"+input);
		this.xMin = input;
	}
	public void setPlotXMax(double input) {
		//System.out.println("setting PlotXMax"+input);
		this.xMax = input;
	//	modelerGuiLink.updateScale(input, input, input, input, bins)
	}
	/*
	public void setPlotYMax(double input) {
		this.yMax = input;
	}
	public void setPlotYMin(double input) {
		this.yMin = input;
	}*/
	
	public void setModelType(int modelType) {
		this.modelType = modelType;
	}
	
	public void setListOfTicks(ArrayList listOfTicks) {

		this.listOfTicks = listOfTicks;
	}
	public ArrayList getListOfTicks(int modelType) {
		return this.listOfTicks;
	}
	
	public void setModelCount(int ct) {
		modelCount = ct;
	}
	public int getdataCursor() {
		return values.size();
	}
	public float[] getXData() {
	//	System.out.println("graph getXData domain lowerBound="+data.getDomain().lowerBound+" upperBound="+data.getDomain().upperBound);
		float[] xRang = new float[data.getDomain().getSize()];
		for (int i = 0; i < data.getDomain().getSize(); i++) {
			xRang[i] = (float) data.getDomain().getValue(i);
		}
		return xRang;
	}

	public float[] getYData() {
		float[] yRang = new float[data.getDomain().getSize()];
		for (int i = 0; i < data.getDomain().getSize(); i++) {
			yRang[i] = (float) data.getFreq(data.getDomain().getValue(i));
		}
		return yRang;
	}
	public void panRight() {
	//	right = right + width;
	//	left = left + width;
		xMin= xMin+width;
		xMax= xMax+width;
		setIntervalData();
		//////System.out.println("ModelerHistogram panRight call setHist");

		setHist();
		repaint();
	}

	public void panLeft() {
		//left = left - width;
	//	right = right - width;
		xMin= xMin-width;
		xMax= xMax-width;
		setIntervalData();
		//////System.out.println("ModelerHistogram panLeft call setHist");

		setHist();
		repaint();
	}
	public void setZoomInIntervalData() {
		zoomIn = true;

		//data = new IntervalData(this.graphLeft, this.graphRight, 2 * width);
		data = new IntervalData(xMin, xMax, 2 * width); //Jenny
		width = 2*width;
		this.data = data;
		repaint();

	}
	public void setZoomOutIntervalData() {
		zoomOut = true;

		//data = new IntervalData(graphLeft, graphRight, .5*width);
		data = new IntervalData(xMin, xMax, .5*width);  //Jenny
		width = .5*width;
		setIntervalData(data);
	}
	
	public int zoomOut() {
		int ret = 0;
		//   if(right-left>width) {
	//	left = left - width;
	//	right = right + width;
		xMin = xMin - width;   //Jenny
		xMax = xMax + width;
		setZoomOutIntervalData();
		ret = 1;
		return ret;
	}
	
	public int zoomIn() {
		int ret = 0;
	//	if (right - left > width) {
	//		left = left + width;
	//		right = right - width;
		if (xMax - xMin > width) {  //Jenny
			xMin = xMin + width;
			xMax = xMax - width;
			setZoomInIntervalData();
			ret = 1;
		}
		return ret;

	}

	public void setBins(int n) {
		//width = (right - left) / n;
		
	//	width = (this.graphRight - this.graphLeft) / n;
		width = (this.xMax - this.xMin) / n;  //Jenny
		width /= 2;
	//	System.out.println("graph setBins "+n+" width="+width);
		setIntervalData();

	}
	public void setXExtrema(float a, float b) {
	}

	public void setYExtrema(float a, float b) {
	}
	
	public void setModel(int cnt, double[] xMod, double[] yMod) {
		if(debug){
			System.out.println("setModel get called");
			Exception e = new Exception();
			e.printStackTrace();
		}
		modelX = xMod;
		modelY = yMod;
		//System.out.println("Modeler graph setModel: modelX[0]="+modelX[0]+ " y[0]="+modelY[0]+" modelX[last]= "+modelX[xMod.length-1]+"Y[last]"+modelY[xMod.length-1]);
		repaint();
	}
	public void clear() {
		values.removeAllElements();
		data.reset();
		modelX = null;
		modelY = null;
		repaint();
	}

	public void setxy(float[] raw) {
		
		//System.out.println("Histogram: inside setxy");
		clear();

		double XDom = 0;
		for (int i = 0; i < raw.length; i++) {
			//////System.out.println("ModelerHistogram setxy raw["+i+"] = " + raw[i]);
			if (modelType == Modeler.DISCRETE_DISTRIBUTION_TYPE) {
				XDom = raw[i] + 0.5;
				////System.out.println("ModelerHistogram setxy DISCRETE_DISTRIBUTION_TYPE XDom = " + XDom);
			}
			else {
				// assume all the other distributions are continuous until we add more. annie che.
				XDom = data.getDomainValue(raw[i]);
			}

			//System.out.println("XDom=" + XDom);
			values.add(new Double(XDom));
		}
		
		//System.out.println("ModelerHistogram setxy call setHist");
		setHist();
	}

	private void setHist() {
		data.reset();
		
	//	System.out.println("Histogram: inside setHist");
		//System.out.println("values size = " + values.size());
		//System.out.println("graph setHist domain lowerBound="+data.getDomain().lowerBound+" upperBound="+data.getDomain().upperBound);
		
		if (values.size()<1) return;
		
		double min=Double.valueOf(values.elementAt(0).toString()).doubleValue();
		double max=Double.valueOf(values.elementAt(0).toString()).doubleValue();
		double temp = 0;
		
		for (int i = 0; i < values.size(); i++) {
		//	System.out.println("ModelHistogram setHist value = " + values.elementAt(i).toString()); // values is of type Vector.
			temp = Double.valueOf(values.elementAt(i).toString()).doubleValue();
			if (min>temp) min=temp;
			if (max<temp) max=temp;
		} 
		
		
		if((min-(max-min)/10)<xMin)
			setPlotXMin(min-(max-min)/10);
		if((max+(max-min)/10)>xMax)
			setPlotXMax(max+(max-min)/10);
		
		//this will update domain
		setIntervalData();
		
		for (int i = 0; i < values.size(); i++) {
		//	System.out.println("ModelHistogram setHist value = " + values.elementAt(i).toString()); // values is of type Vector.
			temp = Double.valueOf(values.elementAt(i).toString()).doubleValue();
			//System.out.println("graph setHist adding temp=" + temp);
			
			data.setValue(temp);
		} 
		
		/** setLeft(min-(max-min)/10);
			setRight(max+(max-min)/10);
			setHistogramLeft(min-(max-min)/10);
			setHistogramRight(max+(max-min)/10);
			setxMin(min-(max-min)/10);
			setxMax(max+(max-min)/10);
			System.out.println("ModelHistogram setHist min = " + min+"\tMax="+max);
		**/
	//	System.out.println("***graph after setHist min = " + xMin+"\tMax="+xMax);
	//	System.out.println("after graph setHist domain lowerBound="+data.getDomain().lowerBound+" upperBound="+data.getDomain().upperBound);
		
		repaint();
	}

	/*public void setLeft(double input) {
		this.left = input;
	}
	public void setRight(double input) {
		this.right = input;
	}
	public void setGraphLeft(double input) {
		this.left = input;
	}
	public void setGraphRight(double input) {
		this.right = input;
	}*/
	public void setBarWidth(double input) {
		//System.out.println("graph setBarwith="+input);
		this.width = input;
	}
	public int getMaxRawY(){
		return data.getMaxFreq();		
	}
	//used to implement flexible graph
	public double getMaxInputX(){
		double temp = 0;
		for(int i = 0; i < graphInputDataX.size(); i++){
			if (graphInputDataX.get(i) > temp)
				temp = graphInputDataX.get(i);
		}
		return temp;
	}

	public double getMinInputX(){
		double temp = 0;
		for(int i = 0; i < graphInputDataX.size(); i++){
			if (graphInputDataX.get(i) < temp)
				temp = graphInputDataX.get(i);
		}
		return temp;
	}

	public int getMaxInputY(){
		int temp = 0;
		for(int i = 0; i < graphInputDataY.size(); i++){
			if (graphInputDataY.get(i) > temp)
				temp = graphInputDataY.get(i);
		}
		return temp;		
	}
	
	protected void padding(double x, double y){
	//	System.out.println("padding for x="+x+" y="+y+" xmin="+xMin+" xMax="+xMax+" yMax="+yMax+"yMin="+yMin)
		
		boolean changed = false;
		if(y>yMax){
			yMax= (yMax-yMin)*0.2+y;
			changed=true;
		}else if((yMax-y)<yMax*0.1){
			yMax= (yMax-yMin)*.2+yMax;
			changed=true;
		}
		
		if((xMax-x)<(xMax-xMin)*0.1){
			double margin=(xMax-xMin)*0.2;
			xMax= xMax+margin;
			xMin = xMin-margin;
			changed=true;
		}
		else if((x-xMin)<(xMax-xMin)*0.1){
			double margin=(xMax-xMin)*0.2;
			xMax= xMax+margin;
			xMin = xMin-margin;
			changed=true;
		}
		
		//System.out.println("after padding for x="+x+" y="+y+" xmin="+xMin+" xMax="+xMax+" yMax="+yMax+"yMin="+yMin);
		if(changed){
			data.setDomain(new Domain(xMin, xMax, this.width));
			
			updateGuiSlider= true;
			setScale(xMin, xMax, yMin, yMax);
			updateGuiSlider= false;
		}
	}
	
	/**This method sets the minimum and maximum values on the x and y axes*/
	public void setScale(double x0, double x1, double y0, double y1){
	//	System.out.println("Graph setScale xmax = " + x1+" yMax="+y1 + " updateGuiSlider ="+ updateGuiSlider);
		//Exception e = new Exception();
		//e.printStackTrace();
		xMin = x0; xMax = x1;
		yMin = y0; yMax = y1;
		
		//??? Jenny 6/25/10  keep bin or width constant??? 
		if(modelerGuiLink!=null && updateGuiSlider==true ){ // bins constant
			// int bins = (int) ((xMax-xMin)/(2*width));   // width constant
		    // modelerGuiLink.updateScale(xMin, xMax, yMin, yMax, modelerGuiLink.histBinNos);
		
			width = (this.xMax - this.xMin) /modelerGuiLink.histBinNos;  //Jenny
			width /= 2;
		//	System.out.println("Graph setScale bins="+bins);
			modelerGuiLink.updateScale(xMin, xMax, yMin, yMax, modelerGuiLink.histBinNos);
		}
	
	}
	/** This method handles the events corresponding to mouse clicks. */
	public void mouseClicked(MouseEvent event) {
		
		if (drawUserClicks) {
			int y0 = yGraph(0);
			int y = (int) Math.ceil(yScale(event.getY()));
			double x = xScale(event.getX());
			double XDom = data.getDomainValue(x);

			int ydash = y - data.getFreq(XDom);
			valCount = valCount + ydash;
			//add new graph input data
			graphInputDataX.add(XDom);
			graphInputDataY.add(y);
			
		//	System.out.println("mouseClicked before padding xDom="+XDom +" y="+y +" ydash="+ydash+" event gety="+event.getY()+" yScale "+yScale(event.getY()));
			//padding 20% for data close to the current limit
			padding(x,y);
			
			if (ydash > 0) {
				for (int i = 0; i < ydash; i++) {
				//	System.out.println("graph mouseClicked values.add XDom = " + XDom);
					values.add(new Double(XDom));
				}
				//////System.out.println("ModelerHistogram mouseClicked if call setHist");

				setHist();
			} else {
				for (int i = 0; i < -ydash; i++) {
					values.removeElement(new Double(XDom));
				}
				//////System.out.println("ModelerHistogram mouseClicked else call setHist");

				setHist();
				repaint();
			}
		}
	}

	//The following method correspond to mouse events that are not handled.
	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mousePressed(MouseEvent event) {
	}

	public void mouseReleased(MouseEvent event) {
	}

}
