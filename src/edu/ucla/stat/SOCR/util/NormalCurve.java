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
/* created by annie che 20060915.  */
package edu.ucla.stat.SOCR.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import edu.ucla.stat.SOCR.analyses.result.NormalPowerResult;
import edu.ucla.stat.SOCR.distributions.Domain;
import edu.ucla.stat.SOCR.distributions.IntervalData;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import edu.ucla.stat.SOCR.modeler.Modeler;
import edu.ucla.stat.SOCR.modeler.gui.ModelerColor;

/**
 * This class models an interactive histogram.  The user can click on the horizontal axes to add points to the data set.
 */
public class NormalCurve extends ModelerHistogram {
	protected boolean drawData = false;
	protected double[] rawData = null;
	protected NormalDistribution dataDist = null;
	protected int[] freq = null;
	protected int sampleSize;
	protected Domain domain;
	protected IntervalData intervalData;
	protected double maxRelFreq = -1;
	protected Frequency frequency = null;
	protected HashMap map = null;
	private double mu0;
	private double muA;
	private double sigma;
	private double sampleSE;
	private double ciLeft;
	private double ciRight;
	private NormalDistribution normal0 = null;
	private NormalDistribution normalA = null;
	private boolean fillArea = true;
	private Color fillColor1 = Color.PINK;
	private Color fillColor2 = fillColor1.brighter();
	private Color fillColor3 = Color.YELLOW;
	private Color fillColor4 = fillColor3.brighter();
	private Color ciColor = Color.GREEN;
	private double xIntersect;
	private double yIntersect;
	private boolean useSampleMean = false;
	private String hypothesisType = null;

	private static byte NORMAL_CURVE_THICKNESS = 1;

	public NormalCurve(double a, double b, double w) {
		super(a, b, w);
		this.modelType =  Modeler.CONTINUOUS_DISTRIBUTION_TYPE;
		setDrawUserClicks(false);

	}
	public NormalCurve() {
		super();
		setDrawUserClicks(false);
	}

	/**
	 * @param rawData  the rawData to set
	 * @uml.property  name="rawData"
	 */
	public void setRawData(double[] input) {
		sampleSize = input.length;
		try {
			this.rawData = input;
			double dataMax = 0;
			try {
				dataMax = QSortAlgorithm.max(this.rawData);
			} catch (Exception e) {
			}
			double dataMin = 0;
			try {
				dataMin = QSortAlgorithm.min(this.rawData);
			} catch (Exception e) {
			}

			domain = new Domain(dataMin, dataMax, 1);
			intervalData = new IntervalData(domain, null);
			setIntervalData(intervalData);

			frequency = new Frequency(rawData);
			map = frequency.getMap();
			frequency.computeFrequency();
			maxRelFreq = frequency.getMaxRelFreq();
		} catch (Exception e) {
		}
	}

	public void setRawDataDistribution(NormalDistribution normal) {
		this.dataDist = normal;
	}

	/**
	 * @return  the rawData
	 * @uml.property  name="rawData"
	 */
	public double[] getRawData() {
		return this.rawData;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D G2 = (Graphics2D) g;
		G2.setStroke(new BasicStroke(NORMAL_CURVE_THICKNESS)); // how thick the pen it.
		float[] HSBVal = new float[3];


		double x = 0;
		double width = .5;
		double d = 1;
		try {
			if (rawData.length > 0) {
				Set keySet = map.keySet();
				Iterator iterator = keySet.iterator();
				int freqSize = map.size();
				int dataCount = -1;
				String id = null;
				while (iterator.hasNext()) {
					id = (String)iterator.next();
					dataCount = ((Integer)map.get(id)).intValue();;
					x = Double.parseDouble(id);
					double dataCountDouble = dataCount;
					double sampleSizeDouble = sampleSize;
					d = dataCountDouble/sampleSizeDouble;
					g.setColor(Color.PINK);
					drawBox(g, x - width / 2, 0, x + width / 2, d);
					g.setColor(Color.PINK);
					fillBox(g, x - width / 2, 0, x + width / 2, d);

				}
			}
		} catch (Exception e) {
		}

		if (modelX1 != null && modelX1.length > 0 && modelX2 != null && modelX2.length > 0) {
			double maxXVal1 = modelX1[0], maxYVal1 = modelY1[0];
			int terms1 = modelY1.length; // how many dx.

			double maxXVal2 = modelX2[0], maxYVal2 = modelY1[0];
			int term2s = modelY2.length; // how many dx.


			double xa, ya;
			int subLth = 0;
			subLth = (int) (modelX1.length / modelCount);
			double x1 = 0;
			double y1 = 0;
			//for (int j = 0; j < modelCount; j++) {
			int j = 0;
				x1 = (double) modelX1[0 + j * subLth];
				y1 = (double) modelY1[0 + j * subLth];
				for (int i = 1; i < subLth; i++) {
					xa = modelX1[i + j * subLth];
					ya = modelY1[i + j * subLth];
					x1 = xa;
					y1 = ya;
				}
				G2.setStroke(new BasicStroke(NORMAL_CURVE_THICKNESS));
				G2.setColor(this.getOutlineColor2());


				x1 = (double) modelX2[0 + j * subLth];
				y1 = (double) modelY2[0 + j * subLth];

				for (int i = 1; i < subLth; i++) {
					xa = modelX2[i + j * subLth];
					ya = modelY2[i + j * subLth];

					if (fillArea) {
						G2.setColor(Color.YELLOW);
						G2.setStroke(new BasicStroke(3f));
						if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_GT) && (x1 > ciRight)) {
							//fillBox(G2, x1,     normal0.getDensity(xa), xa,    normalA.getDensity(xa) && (x1 < ciLeft)) {

							fillBox(G2, x1,     0, xa,    normalA.getDensity(xa));

						} else if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_LT)&& (x1 < ciLeft)) {
							//fillBox(G2, x1,     normal0.getDensity(xa), xa,    normalA.getDensity(xa));
							fillBox(G2, x1,     0, xa,    normalA.getDensity(xa));

						} else if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_NE)&& (((x1 > ciRight)) || (x1 < ciLeft))) {
							//fillBox(G2, x1,     normal0.getDensity(xa), xa,    normalA.getDensity(xa));
							fillBox(G2, x1,     0, xa,    normalA.getDensity(xa));

						}
					}

					x1 = xa;
					y1 = ya;
				}

				// model curve
				G2.setStroke(new BasicStroke(NORMAL_CURVE_THICKNESS));
				G2.setColor(this.getOutlineColor1());


				x1 = (double) modelX1[0 + j * subLth];
				y1 = (double) modelY1[0 + j * subLth];
				////////////////////System.outprintln("NormalCurve mu0 = " + mu0);

				for (int i = 1; i < subLth; i++) {
					xa = modelX1[i + j * subLth];
					ya = modelY1[i + j * subLth];
					//if (x1 < mu0 + 5 * sigma || x1 > mu0 - 5 * sigma)
					drawLine(G2, x1, y1, xa, ya); // when modelCount == any #

					x1 = xa;
					y1 = ya;
				}


			subLth = (int) (modelX2.length / modelCount);

				// model curve
				G2.setStroke(new BasicStroke(NORMAL_CURVE_THICKNESS));
				G2.setColor(this.getOutlineColor2());


				x1 = (double) modelX2[0 + j * subLth];
				y1 = (double) modelY2[0 + j * subLth];

				////////////////////System.outprintln("NormalCurve muA = " + muA);
				for (int i = 1; i < subLth; i++) {
					xa = modelX2[i + j * subLth];
					ya = modelY2[i + j * subLth];
					//if (x1 < muA + 5 * sigma || x1 > muA - 5 * sigma)
					drawLine(G2, x1, y1, xa, ya); // when modelCount == any #

					x1 = xa;
					y1 = ya;
				}

		}
		// draw it second time (looks nicer)
		G2.setStroke(new BasicStroke(NORMAL_CURVE_THICKNESS));
		g.setColor(Color.BLACK);
	 	super.drawAxis(g, -yMax, yMax, 0.1 * yMax, xMin, VERTICAL); // 6 args
		super.drawAxis(g, xMin, xMax, (xMax - xMin) / 10, 0, HORIZONTAL, axisType, listOfTicks); // c must be 0.

	}
	protected void drawAxisWithDomain(Graphics g, Domain domain, double c, int orientation, int type, ArrayList list){

		double t;
		double currentUpperBound = domain.getUpperBound(); // of the model (distribution)
		double currentLowerBound = domain.getLowerBound();
		int domainSize = domain.getSize();
		if (orientation == HORIZONTAL){
			this.drawLine(g, currentLowerBound, c, currentUpperBound, c);
			//Draw tick marks, depending on type
			for (int i = 0; i < domainSize; i++){
				if (type == MIDPOINTS) {
					t = domain.getValue(i);
				} else {
					t = domain.getBound(i);
				}
				g.setColor(ModelerColor.HISTOGRAM_TICKMARK);
				//g.setStroke(new BasicStroke(3.05f));

				//drawTick(g, t, c, VERTICAL);
			}
			if (type == BOUNDS) {
				t =  domain.getUpperBound();
				drawTick(g, t, c, VERTICAL);
			}
			//Draw labels
			if (type == MIDPOINTS) {
				t = domain.getLowerValue();
			} else {
				t = domain.getLowerBound();
			}
			drawLabel(g, format(t), t, c, BELOW);
			if (type == MIDPOINTS) {
				t = domain.getUpperValue();
				} else {
				t = domain.getUpperBound();
			}
			drawLabel(g, format(t), t, c, BELOW);
			//double mu0 = 0;
			//double muA = 0;
			//double sigma = 0;
			//ciLeft = 0;
			//ciRight = 0;
			//double sampleSE = 0;
			//NormalDistribution normal0 = null;
			//NormalDistribution normalA = null;
			if (list != null) {
				//for (int i = 0; i < list.size(); i++) {
					try {
						mu0 = Double.parseDouble(((String)list.get(0)));
						muA = Double.parseDouble(((String)list.get(1)));
						sigma = Double.parseDouble(((String)list.get(2)));
						////////////System.outprintln("NormalCurve mu0 = " + mu0);
						////////////System.outprintln("NormalCurve muA = " + muA);

						//sampleSE = Double.parseDouble(((String)list.get(3)));
						normal0 = new NormalDistribution(mu0, sigma);
						normalA = new NormalDistribution(muA, sigma);
						//ciLeft = mu0 - 1.96 * sigma;
						//ciRight = mu0 + 1.96 * sigma;
						//t = Double.parseDouble(((String)list.get(i)));
						drawLabel(g, format(mu0), mu0, c, BELOW);
						drawLabel(g, format(muA), muA, c, BELOW);
						Color oldColor = g.getColor();
						g.setColor(this.getOutlineColor1());
						drawLine(g, mu0,     0, mu0,    normal0.getMaxDensity());
						//drawLine(g, ciLeft,  0, ciLeft, normal0.getDensity(ciLeft));
						//drawLine(g, ciRight, 0, ciRight, normal0.getDensity(ciRight));

						g.setColor(this.getOutlineColor2());
						drawLine(g, muA,     0, muA,    normalA.getMaxDensity());
						double density = 0;
						if (hypothesisType == null)
							hypothesisType = (String)list.get(5);
						////////System.outprintln("NormalCurve hypothesisType = " + hypothesisType);
						if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_NE)){
							try {
								g.setColor(this.getOutlineColor1());
								ciLeft = Double.parseDouble(((String)list.get(3)));
								//drawLabel(g, format(ciLeft), ciLeft, c, BELOW);
								density = Math.max(normal0.getDensity(ciLeft), normalA.getDensity(ciLeft));
								g.setColor(ciColor);
								drawLine(g, ciLeft,     0, ciLeft,    density);
								g.setColor(this.getOutlineColor1());
								//hypothesisType = (String)list.get(5);
								//////////////System.outprintln("NormalCurve ciLeft = " + ciLeft + " density = " + density);

							}
							catch (Exception e) {
								//////////////System.outprintln("NormalCurve e = " + e);
							}
							try {
								g.setColor(this.getOutlineColor1());
								ciRight = Double.parseDouble(((String)list.get(4)));
								//drawLabel(g, format(ciLeft), ciLeft, c, BELOW);
								density = Math.max(normal0.getDensity(ciRight), normalA.getDensity(ciRight));
								g.setColor(ciColor);
								drawLine(g, ciRight,     0, ciRight,    density);
								g.setColor(this.getOutlineColor1());
								//hypothesisType = (String)list.get(5);
								//////////////System.outprintln("NormalCurve ciRight = " + ciRight + " density = " + density);
							}
							catch (Exception e) {
								//////////////System.outprintln("NormalCurve e = " + e);
							}
						}
						else if (muA < mu0) {
							hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_LT;
						}
						else if (muA > mu0) {
							hypothesisType = NormalPowerResult.HYPOTHESIS_TYPE_GT;
						}


						if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_LT)) {
							try {

								g.setColor(this.getOutlineColor1());
								ciLeft = Double.parseDouble(((String)list.get(3)));
								//drawLabel(g, format(ciLeft), ciLeft, c, BELOW);
								density = Math.max(normal0.getDensity(ciLeft), normalA.getDensity(ciLeft));
								g.setColor(ciColor);
								drawLine(g, ciLeft,     0, ciLeft,    density);
								g.setColor(this.getOutlineColor1());
								//hypothesisType = (String)list.get(5);
								////////System.outprintln("NormalCurve ciLeft = " + ciLeft + " density = " + density);

							} catch (Exception e) {
								////////System.outprintln("NormalCurve Exception e = " + e);
							}
						} else if (hypothesisType.equalsIgnoreCase(NormalPowerResult.HYPOTHESIS_TYPE_GT)) {
							try {
								g.setColor(this.getOutlineColor1());
								ciRight = Double.parseDouble(((String)list.get(4)));
								//drawLabel(g, format(ciLeft), ciLeft, c, BELOW);
								density = Math.max(normal0.getDensity(ciRight), normalA.getDensity(ciRight));
								g.setColor(ciColor);
								drawLine(g, ciRight,     0, ciRight,    density);
								g.setColor(this.getOutlineColor1());
								//hypothesisType = (String)list.get(5);
								////////System.outprintln("NormalCurve ciRight = " + ciRight + " density = " + density);

							} catch (Exception e) {
								////////System.outprintln("NormalCurve Exception e = " + e);
							}

						}

						////////////System.outprintln("NormalCurve hypothesisType = " + hypothesisType);
						double x1 = 0, y1 = 0, xa = 0;

						g.setColor(oldColor);


					} catch (Exception e) {
						//////////////System.outprintln("NormalCurve last e = " + e);
					}

				//}


			}
		}
		else{
			//Draw thte line
			drawLine(g, c, domain.getLowerBound(), c, domain.getUpperBound());
			//drawLine(g, c, -10, c, 10);

			//Draw tick marks, depending on type
			for (int i = 0; i < domain.getSize(); i++){
				if (type == MIDPOINTS) t = domain.getValue(i); else t = domain.getBound(i);
				//drawTick(g, c, t, HORIZONTAL);
			}
			if (type == BOUNDS) drawTick(g, c, domain.getUpperBound(), HORIZONTAL);
			//Draw labels
			if (type == MIDPOINTS) t = domain.getLowerValue(); else t = domain.getLowerBound();

			g.setColor(ModelerColor.HISTOGRAM_LABEL);
			drawLabel(g, format(t), c, t, LEFT);
			if (type == MIDPOINTS) t = domain.getUpperValue(); else t = domain.getUpperBound();

			drawLabel(g, format(t), c, t, LEFT);
		}
		int sum = Math.abs(currentXUpperBound) + Math.abs(currentXLowerBound); //
		int diff = Math.abs(currentXUpperBound) - Math.abs(currentXLowerBound); //
	}

	/**
	 * @return  the maxRelFreq
	 * @uml.property  name="maxRelFreq"
	 */
	public double getMaxRelFreq() {
		return this.maxRelFreq;
	}
	/**
	 * @param fillArea  the fillArea to set
	 * @uml.property  name="fillArea"
	 */
	public void setFillArea(boolean fillArea) {
		this.fillArea = fillArea;
	}
	/*
	private void findIntersection(double[] x1, double[] y1, double[] x2, double[] y2) {
		double numberTooSmall = 1E-10;
		boolean[] willUse = new boolean[x1.length];

		for (int i = 0; i < x1.length; i++) {
			if (y1[i] < numberTooSmall || y2[i] < numberTooSmall) {
				willUse[i] = false;
			}
			else {
				willUse[i] = true;
			}
		}
	}
	*/
	public void setSampleMeanOption(boolean input) {
		this.useSampleMean = input;
	}
	public boolean withinSampleMeanCurve(double x, double y) {//, double scale) {
		double f = normalA.getDensity(x);
		//////////System.outprintln("NormailCurve x = " + x + ", y = " + y + ", f = " + f);
		if (f <= y && f >= 0.0001)  {
			return true;
		}
		else {
			return false;
		}
	}
	public void resetHypotheseType() {
		hypothesisType = null;
	}
}
