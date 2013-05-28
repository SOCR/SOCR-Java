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
/* SOCR Sampling Distribution Experiment - Main Frame
/* SamplingExperimentApplet 
 *
 * This class is part of the SOCR Experiment: Sampling Distribution (CLT) Experiment,
 * which demonstrates the properties of the sampling distributions of various sample statistics.
 */

package edu.ucla.stat.SOCR.util;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Toolkit;
import edu.uah.math.experiments.*;

public class SamplingExperimentMainFrame extends JFrame implements ActionListener, ItemListener
{
    SamplingExperimentGenerateSample stx;

	// SamplingExperimentHistograms
    public SamplingExperimentHistogram h;
    public SamplingExperimentHistogram hh;
    public SamplingExperimentHistogram hhh;
    public SamplingExperimentHistogram hhhh;
    public SamplingExperimentAnimateSample samplingExperimentAnimateSample2;
    public SamplingExperimentAnimateSample samplingExperimentAnimateSample3;
    public SamplingExperimentAnimateSample samplingExperimentAnimateSample4;
	
	//Panels
    JPanel P1;
    JPanel P2;
    JPanel P3;
    JPanel P4;

	//Buttons
    JButton cb;
    JButton db2;
    JButton db3;
    JButton db4;
    JButton db5;
    JButton db6;
	//public JButton refreshStatsTableButton;

    public JProgressBar progressBar;

    String theDist;
    int hHeight;
    int yintervals;
    int hWidth = 460;
    public int N = 5;	// Requested sample-size of First estimate/statistics
    public int N2 = 5;	// Requested sample-size of Second estimate/statistics
    int k; // Number of Bins!
    int ns = 0;
    int ns2 = 0;
    final int sampMax = 10000;
    float[] xdata;
    float[] xdata2;
    float[] statD;
    float[] statD2;
    double temp;
    
    int[] sampleFdata;	// Sample Frequency Data
    int[] fdata;		// The Frequency counts for the distribution we sample from (analytic or user-drawn)
    
    Color[] colors
	= { Color.blue, Color.blue, Color.red, Color.green, Color.black,
	    Color.cyan, Color.darkGray, Color.darkGray, Color.lightGray };
    Color distColor = Color.darkGray;
    final int[] normdata
	= { 2, 3, 3, 6, 8, 14, 19, 32, 45, 60, 78, 97, 116, 133, 147, 156, 160,
	    156, 147, 133, 116, 97, 78, 60, 45, 32, 19, 14, 8, 6, 3, 3, 2 };
    int[] skewedData;
    public String astring;
    public String statName1;
    public String statName2;
    public JLabel mean;
    public JComboBox dist;
    public JComboBox sampleSize;
    public JComboBox sampleSize2;
    public JComboBox stat;
    public JComboBox stat2c;
    public JCheckBox fit1;
    public JCheckBox fit2;
    boolean[] statsOn;
    boolean[] stats;
    edu.uah.math.experiments.Experiment applet;

	// Constructors
    public SamplingExperimentMainFrame(edu.uah.math.experiments.Experiment _applet) {
    	this.setTitle("Sampling Distributions");
    	//this.setBackground(parameters.getBackGroundColor());
    	this.applet = _applet;
    	initialize();
    }

    public SamplingExperimentMainFrame() {
	this.setTitle("Sampling Distributions");
	//this.setBackground(parameters.getBackGroundColor());
	this.applet = null;
	initialize();
    }

    public void initialize()
    {
	// Progress Reporting
	progressBar = new JProgressBar();
      progressBar.setMinimum(0);
	progressBar.setValue(0);
      progressBar.setStringPainted(true);

      //refreshStatsTableButton = new JButton("Refresh Stats Table");
      
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	((SamplingExperimentMainFrame) this).stx = new SamplingExperimentGenerateSample(this, 1, 1, 1);
	((SamplingExperimentMainFrame) this).hHeight = Math.min(dimension.height - 80, 600);
	((SamplingExperimentMainFrame) this).yintervals
	    = Math.round((float) (((SamplingExperimentMainFrame) this).hHeight / 100));
	this.setSize(640, ((SamplingExperimentMainFrame) this).hHeight);
	((SamplingExperimentMainFrame) this).hHeight = ((SamplingExperimentMainFrame) this).hHeight / 4 - 15;
	((SamplingExperimentMainFrame) this).statsOn = new boolean[10];
	((SamplingExperimentMainFrame) this).stats = new boolean[10];
	for (int i = 0; i < 10; i++) {
	    ((SamplingExperimentMainFrame) this).statsOn[i] = true;
	    ((SamplingExperimentMainFrame) this).stats[i] = false;
	}
	((SamplingExperimentMainFrame) this).statsOn[3] = false;
	((SamplingExperimentMainFrame) this).statsOn[5] = false;
	((SamplingExperimentMainFrame) this).statsOn[6] = false;
	
		// Default Statistics to compute, these are reset by drop-down lists "stats" and "stats2c"
	statName1 = "Mean";
	statName2 = "None";
	
	((SamplingExperimentMainFrame) this).k = 33;	// Number of Bins
	this.getContentPane().setLayout(new FlowLayout(0));
	((SamplingExperimentMainFrame) this).statD = new float[10000];
	((SamplingExperimentMainFrame) this).statD2 = new float[10000];
	((SamplingExperimentMainFrame) this).xdata = new float[((SamplingExperimentMainFrame) this).k];
	((SamplingExperimentMainFrame) this).xdata2 = new float[((SamplingExperimentMainFrame) this).k];
	((SamplingExperimentMainFrame) this).sampleFdata = new int[((SamplingExperimentMainFrame) this).k];
	((SamplingExperimentMainFrame) this).skewedData = new int[((SamplingExperimentMainFrame) this).k];
	((SamplingExperimentMainFrame) this).fdata = new int[((SamplingExperimentMainFrame) this).k];
	
	float f = 8.0F;	// Gaussian SD
	float f_0_ = -f;
	for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++) {
	    f_0_ += f;
	    ((SamplingExperimentMainFrame) this).xdata[i] = (float) i;
	    ((SamplingExperimentMainFrame) this).xdata2[i] = f_0_;
	}
	f_0_ = (float) ((SamplingExperimentMainFrame) this).k;
	f_0_ /= 6.0F;
	float f_1_ = (float) (((SamplingExperimentMainFrame) this).k - 1);
	f_1_ /= 2.0F;
	((SamplingExperimentMainFrame) this).temp
	    = Math.sqrt(1.0 / (6.283185307179586 * (double) f_0_
			       * (double) f_0_));
	double d = Math.pow((double) (((SamplingExperimentMainFrame) this).xdata
				      [((SamplingExperimentMainFrame) this).k - 1]),
			    2.0);
	for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++) {
	    double d_2_
		= -1.0 * Math.pow((double) (((SamplingExperimentMainFrame) this).xdata[i] - f_1_),
				  2.0) / (double) (2.0F * f_0_ * f_0_);
	    d_2_ = Math.exp(d_2_) * ((SamplingExperimentMainFrame) this).temp;
	    ((SamplingExperimentMainFrame) this).skewedData[i]
		= (int) Math.round(Math.pow((double) (((SamplingExperimentMainFrame) this).xdata
						      [(((SamplingExperimentMainFrame) this).k - i
							- 1)]),
					    2.0) / d * 5000.0);
	}
	((SamplingExperimentMainFrame) this).skewedData[0] = ((SamplingExperimentMainFrame) this).skewedData[6];
	((SamplingExperimentMainFrame) this).skewedData[1] = ((SamplingExperimentMainFrame) this).skewedData[5];
	((SamplingExperimentMainFrame) this).skewedData[2] = ((SamplingExperimentMainFrame) this).skewedData[4];
	for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++)
	    ((SamplingExperimentMainFrame) this).sampleFdata[i] = 0;
	((SamplingExperimentMainFrame) this).theDist = "Normal";
	setDist();
	for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++) {
	    /* empty */
	}


	// Population Distribution SamplingExperimentHistogram 1
	((SamplingExperimentMainFrame) this).P1 = new JPanel();
	((SamplingExperimentMainFrame) this).P2 = new JPanel();
	((SamplingExperimentMainFrame) this).P3 = new JPanel();
	((SamplingExperimentMainFrame) this).P4 = new JPanel();

	((SamplingExperimentMainFrame) this).h
	    = new SamplingExperimentHistogram("Native Populaton",
			    ((SamplingExperimentMainFrame) this).xdata, ((SamplingExperimentMainFrame) this).fdata,
			    ((SamplingExperimentMainFrame) this).k, ((SamplingExperimentMainFrame) this).statsOn,
			    false, ((SamplingExperimentMainFrame) this).hWidth,
			    ((SamplingExperimentMainFrame) this).hHeight,
			    ((SamplingExperimentMainFrame) this).yintervals, true, false, 0,
			    ((SamplingExperimentMainFrame) this).colors);
	((SamplingExperimentMainFrame) this).h.setColor(((SamplingExperimentMainFrame) this).distColor);
	((SamplingExperimentMainFrame) this).h.setEndsOnly(true);
	((SamplingExperimentMainFrame) this).h.setPreferredSize(new Dimension(350, 150));
	((SamplingExperimentMainFrame) this).h.setLayout(new FlowLayout(FlowLayout.CENTER));
	this.getContentPane().add(((SamplingExperimentMainFrame) this).h);

	((SamplingExperimentMainFrame) this).cb = new JButton("Reset Applet");
	cb.addActionListener(this);
	((SamplingExperimentMainFrame) this).P1.add(((SamplingExperimentMainFrame) this).cb);
	dist = new JComboBox();
	dist.addItem("Normal");
	dist.addItem("Uniform");
	dist.addItem("Skewed");
	dist.addItem("Custom");
	dist.setBackground(Color.white);
	dist.addItemListener(this);
	((SamplingExperimentMainFrame) this).P1.setLayout(new GridLayout(0, 1));
	((SamplingExperimentMainFrame) this).P1.add(dist);



	// Sample Data SamplingExperimentHistogram 2
	this.getContentPane().add(((SamplingExperimentMainFrame) this).P1);
	((SamplingExperimentMainFrame) this).hh
	    = new SamplingExperimentHistogram("Sample Distribution", ((SamplingExperimentMainFrame) this).xdata,
			    ((SamplingExperimentMainFrame) this).sampleFdata, ((SamplingExperimentMainFrame) this).k,
			    ((SamplingExperimentMainFrame) this).stats, true,
			    ((SamplingExperimentMainFrame) this).hWidth,
			    ((SamplingExperimentMainFrame) this).hHeight,
			    ((SamplingExperimentMainFrame) this).yintervals, false, false, 0,
			    ((SamplingExperimentMainFrame) this).colors);
	((SamplingExperimentMainFrame) this).hh.setStats(false);
	((SamplingExperimentMainFrame) this).hh.setPreferredSize(new Dimension(350, 150));
	((SamplingExperimentMainFrame) this).hh.setLayout(new FlowLayout(FlowLayout.CENTER));
	((SamplingExperimentMainFrame) this).hh.setEndsOnly(true);

	this.getContentPane().add(((SamplingExperimentMainFrame) this).hh);

	((SamplingExperimentMainFrame) this).db2 = new JButton("Animated Sample");
	((SamplingExperimentMainFrame) this).P4.add(((SamplingExperimentMainFrame) this).db2);
	db2.addActionListener(this);

	((SamplingExperimentMainFrame) this).db3 = new JButton("5 Samples");
	((SamplingExperimentMainFrame) this).P4.add(((SamplingExperimentMainFrame) this).db3);
	db3.addActionListener(this);

	((SamplingExperimentMainFrame) this).db4 = new JButton("1,000 Samples");
	db4.addActionListener(this);
	((SamplingExperimentMainFrame) this).P4.setLayout(new GridLayout(0, 1));
	((SamplingExperimentMainFrame) this).P4.add(((SamplingExperimentMainFrame) this).db4);

	((SamplingExperimentMainFrame) this).db5 = new JButton("10,000 Samples");
	((SamplingExperimentMainFrame) this).P4.add(((SamplingExperimentMainFrame) this).db5);
	db5.addActionListener(this);

	((SamplingExperimentMainFrame) this).db6 = new JButton("100,000 Samples");
	((SamplingExperimentMainFrame) this).P4.add(((SamplingExperimentMainFrame) this).db6);
	db6.addActionListener(this);

	this.getContentPane().add(((SamplingExperimentMainFrame) this).P4);


	// Sampling Distribution (1) SamplingExperimentHistogram 3
	sampleSize = new JComboBox();
	sampleSize.addItem("N=2");
	sampleSize.addItem("N=4");
	sampleSize.addItem("N=5");
	sampleSize.addItem("N=10");
	sampleSize.addItem("N=16");
	sampleSize.addItem("N=20");
	sampleSize.addItem("N=25");
	sampleSize.addItem("N=50");
	sampleSize.addItem("N=100");
	sampleSize.addItem("N=1000");
	sampleSize.setSelectedIndex(3);
		String string = (String)sampleSize.getSelectedItem();
		int i = string.indexOf('=') + 1;
		(this).N = Integer.valueOf(string.substring(i)).intValue();
	sampleSize.addItemListener(this);

	sampleSize.setBackground(Color.white);
	((SamplingExperimentMainFrame) this).hhh
	    = new SamplingExperimentHistogram("Sampling Dist. of \n"+statName1+ "s, " +sampleSize.getSelectedItem(),
			    ((SamplingExperimentMainFrame) this).xdata,
			    ((SamplingExperimentMainFrame) this).sampleFdata, ((SamplingExperimentMainFrame) this).k,
			    ((SamplingExperimentMainFrame) this).statsOn, true,
			    ((SamplingExperimentMainFrame) this).hWidth,
			    ((SamplingExperimentMainFrame) this).hHeight,
			    ((SamplingExperimentMainFrame) this).yintervals, false, false, 0,
			    ((SamplingExperimentMainFrame) this).colors);
	((SamplingExperimentMainFrame) this).hhh.setEndsOnly(true);
	((SamplingExperimentMainFrame) this).hhh.setPreferredSize(new Dimension(350, 150));
	((SamplingExperimentMainFrame) this).hhh.setLayout(new FlowLayout(FlowLayout.CENTER));
	this.getContentPane().add(((SamplingExperimentMainFrame) this).hhh);

	((SamplingExperimentMainFrame) this).P2.setLayout(new GridLayout(0, 1));
	stat = new JComboBox();
	stat.addItem("Mean");
	stat.addItem("Median");
	stat.addItem("SD");
	stat.addItem("Variance");
	stat.addItem("Variance (U)");
	stat.addItem("MAD");
	stat.addItem("Range");
	stat.setSelectedIndex(0);
	stat.setBackground(Color.white);
	stat.addItemListener(this);
	statName1 = (String)(stat.getSelectedItem());

  	// refreshStatsTableButton
	//((SamplingExperimentMainFrame) this).refreshStatsTableButton = new JButton("Refresh Stats Table");
	//refreshStatsTableButton.addActionListener(this);
	//((SamplingExperimentMainFrame) this).P2.add(((SamplingExperimentMainFrame) this).refreshStatsTableButton);

	((SamplingExperimentMainFrame) this).fit1 = new JCheckBox("Fit normal (1)");
	fit1.addItemListener(this);
	((SamplingExperimentMainFrame) this).P2.add(stat);
	((SamplingExperimentMainFrame) this).P2.add(sampleSize);
	((SamplingExperimentMainFrame) this).P2.add(((SamplingExperimentMainFrame) this).fit1);
	// Why doesn't this show?  ((SamplingExperimentMainFrame) this).P2.add(refreshStatsTableButton);
	this.getContentPane().add(((SamplingExperimentMainFrame) this).P2);

	sampleSize2 = new JComboBox();
	sampleSize2.addItem("N=2");
	sampleSize2.addItem("N=4");
	sampleSize2.addItem("N=5");
	sampleSize2.addItem("N=10");
	sampleSize2.addItem("N=16");
	sampleSize2.addItem("N=20");
	sampleSize2.addItem("N=25");
	sampleSize2.addItem("N=50");
	sampleSize2.addItem("N=100");
	sampleSize2.addItem("N=1000");
	sampleSize2.setSelectedIndex(3);
		string = (String)sampleSize2.getSelectedItem();
		i = string.indexOf('=') + 1;
		(this).N2 = Integer.valueOf(string.substring(i)).intValue();
	sampleSize2.setBackground(Color.white);
	sampleSize2.addItemListener(this);

	((SamplingExperimentMainFrame) this).P3.setLayout(new GridLayout(0, 1));

	stat2c = new JComboBox();
	stat2c.addItem("None");
	stat2c.addItem("Mean");
	stat2c.addItem("Median");
	stat2c.addItem("SD");
	stat2c.addItem("Variance");
	stat2c.addItem("Variance (U)");
	stat2c.addItem("MAD");
	stat2c.addItem("Range");
	stat2c.setBackground(Color.white);
	stat2c.setSelectedIndex(4);
	stat2c.addItemListener(this);
	statName2 = (String)(stat2c.getSelectedItem());

	// Sampling Distribution (2) SamplingExperimentHistogram 4
	((SamplingExperimentMainFrame) this).hhhh
	    = new SamplingExperimentHistogram("Sampling Dist. of \n"+statName2+ "s, " + sampleSize2.getSelectedItem(), 
	    		((SamplingExperimentMainFrame) this).xdata,
			    ((SamplingExperimentMainFrame) this).sampleFdata, ((SamplingExperimentMainFrame) this).k,
			    ((SamplingExperimentMainFrame) this).statsOn, true,
			    ((SamplingExperimentMainFrame) this).hWidth,
			    ((SamplingExperimentMainFrame) this).hHeight,
			    ((SamplingExperimentMainFrame) this).yintervals, false, false, 0,
			    ((SamplingExperimentMainFrame) this).colors);
	((SamplingExperimentMainFrame) this).hhhh.setEndsOnly(true);
	((SamplingExperimentMainFrame) this).hhhh.setPreferredSize(new Dimension(350, 150));
	((SamplingExperimentMainFrame) this).hhhh.setLayout(new FlowLayout(FlowLayout.CENTER));
	this.getContentPane().add(((SamplingExperimentMainFrame) this).hhhh);

	((SamplingExperimentMainFrame) this).fit2 = new JCheckBox("Fit normal (2)");
	fit2.addItemListener(this);

	((SamplingExperimentMainFrame) this).P3.add(stat2c);
	((SamplingExperimentMainFrame) this).P3.add(sampleSize2);
	((SamplingExperimentMainFrame) this).P3.add(((SamplingExperimentMainFrame) this).fit2);
	this.getContentPane().add(((SamplingExperimentMainFrame) this).P3);
	this.getContentPane().add(progressBar);
	//this.getContentPane().add(refreshStatsTableButton);
    }
    
    public double ComputeMAD(float[] fs, int i) {
	int[] is = new int[((SamplingExperimentMainFrame) this).k];
	is = tally(fs, ((SamplingExperimentMainFrame) this).xdata, ((SamplingExperimentMainFrame) this).k, i, is,
		   false);
	double d
	    = ((SamplingExperimentMainFrame) this).h.ComputeMAD(is,
					     (((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).h)
					      .xdata),
					     ((SamplingExperimentMainFrame) this).k);
	return d;
    }
    
    public double ComputeMean(float[] fs, int i) {
	double d = 0.0;
	for (int i_3_ = 0; i_3_ < i; i_3_++)
	    d += (double) fs[i_3_];
	d /= (double) i;
	return d;
    }
    
    public double ComputeMedian(float[] fs, int i) {
	int i_4_ = i / 2;
	int[] is = new int[((SamplingExperimentMainFrame) this).k];
	is = tally(fs, ((SamplingExperimentMainFrame) this).xdata, ((SamplingExperimentMainFrame) this).k, i, is,
		   false);
	double d
	    = ((SamplingExperimentMainFrame) this).h.ComputeGroupedMedian(is, 
			((SamplingExperimentMainFrame) this).k, i);
	return d;
    }
    
    public double ComputeRange(float[] fs, int i) {
	int[] is = new int[((SamplingExperimentMainFrame) this).k];
	is = tally(fs, ((SamplingExperimentMainFrame) this).xdata, ((SamplingExperimentMainFrame) this).k, i, is,
		   false);
	double d
	    = ((SamplingExperimentMainFrame) this).h.ComputeGroupedRange(is,
						      ((SamplingExperimentMainFrame) this).xdata,
						      ((SamplingExperimentMainFrame) this).k);
	return d;
    }
    
    public double ComputeSD(float[] fs, int i) {
	double d = 0.0;
	double d_5_ = 0.0;
	for (int i_6_ = 0; i_6_ < i; i_6_++) {
	    d += (double) fs[i_6_];
	    d_5_ += (double) (fs[i_6_] * fs[i_6_]);
	}
	d = Math.sqrt((d_5_ - d * d / (double) i) / (double) i);
	return d;
    }
  
    /* Sets the string name of the user-selected distribution
     * could be "SOCR Distribution" or any of the default 5 distributions, incl. "Custom"
     */
    public void setDistributionName (String str)
    {	this.theDist = str;    	
    }
    
	/* Event Item Listener 
	 */
	public void itemStateChanged(ItemEvent event) {
    		Object source = event.getItemSelectable();
		String string;

	      if (source == dist) {
			JComboBox cb = (JComboBox)event.getSource();
        		string = (String)cb.getSelectedItem();
			((SamplingExperimentMainFrame) this).theDist = string;
			setDist();
	    	}
	    	else if (source == sampleSize) {
			JComboBox cb = (JComboBox)event.getSource();
        		string = (String)cb.getSelectedItem();
			int i = string.indexOf('=') + 1;
			((SamplingExperimentMainFrame) this).N = Integer.valueOf(string.substring(i)).intValue();
			((SamplingExperimentMainFrame) this).hhh.setTitle("Sampling Dist. of \n"
					       + stat.getSelectedItem() + "s, "
					       + sampleSize.getSelectedItem());
			if (applet!=null)
			   ((SamplingDistributionExperiment)applet).stat1TitleButton.setText("Sampling Dist. of \n"
				+ ((SamplingDistributionExperiment)applet).firstSamplingDistJComboBox.getSelectedItem() 
				+ "s, " + sampleSize.getSelectedItem());
			clearAll();
			((SamplingExperimentMainFrame) this).ns = 0;
			setXdata2();
	    	}
	    	else if (source == stat) {
			JComboBox cb = (JComboBox)event.getSource();
        		string = (String)cb.getSelectedItem();
			statName1 = string;
			((SamplingExperimentMainFrame) this).hhh.setTitle("Sampling Dist. of \n" + string
					       + "s, " + sampleSize.getSelectedItem());
			clearAll();
			if (applet!=null)
				((SamplingDistributionExperiment)applet).stat1TitleButton.setText("Sampling Dist. of \n" 
						+ string + "s, " + sampleSize.getSelectedItem());
			((SamplingExperimentMainFrame) this).ns = 0;
			setXdata2();
	    	}
	    	else if (source == stat2c) {
			JComboBox cb = (JComboBox)event.getSource();
        		string = (String)cb.getSelectedItem();
			statName2 = string;
			clearAll();
			((SamplingExperimentMainFrame) this).ns2 = 0;
			if (string.equals("None")) ((SamplingExperimentMainFrame) this).hhhh.setTitle("");
			else ((SamplingExperimentMainFrame) this).hhhh.setTitle("Sampling Dist. of \n"
				+ stat2c.getSelectedItem() + "s, "
				+ sampleSize2.getSelectedItem());
			if (applet!=null)
				((SamplingDistributionExperiment)applet).stat2TitleButton.setText("Sampling Dist. of \n"
						+ string + "s, " + sampleSize2.getSelectedItem());
			setXdata2();
	    	}
	    	else if (source == sampleSize2) {
			JComboBox cb = (JComboBox)event.getSource();
        		string = (String)cb.getSelectedItem();
			int i = string.indexOf('=') + 1;
			((SamplingExperimentMainFrame) this).N2
		    		= Integer.valueOf(string.substring(i)).intValue();
			((SamplingExperimentMainFrame) this).hhhh.setTitle("Sampling Dist. of \n"
						+ stat2c.getSelectedItem()
						+ "s, "
						+ sampleSize2
						      .getSelectedItem());
			if (applet!=null)
			  ((SamplingDistributionExperiment)applet).stat2TitleButton.setText("Sampling Dist. of \n" 
				+ ((SamplingDistributionExperiment)applet).secondSamplingDistJComboBox.getSelectedItem() 
				+ "s, " + sampleSize2.getSelectedItem());

			clearAll();
			((SamplingExperimentMainFrame) this).ns2 = 0;
			setXdata2();
	    	}

		boolean bool;
		if (event.getStateChange() == java.awt.event.ItemEvent.DESELECTED) bool=false;
		else bool=true;

      	if (source == fit1)
			((SamplingExperimentMainFrame) this).hhh.SetNorm(bool);    
		else if (source == fit2)
			((SamplingExperimentMainFrame) this).hhhh.SetNorm(bool);
	}


	/* Event Action Listener 
	 */
    public void actionPerformed(ActionEvent event) {
    	Object source = event.getSource();
    	if (db2==source) {
    		int sampleSizeInt = (((SamplingExperimentMainFrame) this).N+((SamplingExperimentMainFrame) this).N2)/2;
    		if (sampleSizeInt <= 19) ((SamplingExperimentMainFrame) this).sample1(1);
    		else ((SamplingExperimentMainFrame) this).sample1(sampleSizeInt);
    		//((SamplingExperimentMainFrame) this).sample1(1);
    	}
    	else 	if (cb==source) ((SamplingExperimentMainFrame) this).clearAll();
    	else 	if (db3==source) ((SamplingExperimentMainFrame) this).sample1(5);
    	else 	if (db4==source) ((SamplingExperimentMainFrame) this).sample1(1000);
    	else 	if (db5==source) ((SamplingExperimentMainFrame) this).sample1(10000);
    	else 	if (db6==source) ((SamplingExperimentMainFrame) this).sample1(100000);
    	//else if (source == refreshStatsTableButton) 
    		//((edu.uah.math.experiments.SamplingDistributionExperiment)this.applet).updateSummaryStatsTables();
    }
    
    public int chooseStat(String string) {
	int i = 0;
	if (string == "Mean")
	    i = 0;
	else if (string == "Median")
	    i = 1;
	else if (string == "SD")
	    i = 2;
	else if (string == "Range")
	    i = 3;
	else if (string == "MAD")
	    i = 5;
	else if (string == "Variance")
	    i = 6;
	else if (string == "Variance (U)")
	    i = 6;
	return i;
    }
    
    public void clearAll() {
	if (((SamplingExperimentMainFrame) this).hhh != null)
	    ((SamplingExperimentMainFrame) this).hhh.clear();
	if (((SamplingExperimentMainFrame) this).hh != null)
	    ((SamplingExperimentMainFrame) this).hh.clear();
	if (((SamplingExperimentMainFrame) this).hhhh != null)
	    ((SamplingExperimentMainFrame) this).hhhh.clear();
    }
    
    public void clearData(SamplingExperimentHistogram SamplingExperimentHistogram) {
	if (SamplingExperimentHistogram != null) {
	    int i = ((SamplingExperimentHistogram) SamplingExperimentHistogram).numInt;
	    int[] is = new int[i];
	    for (int i_7_ = 0; i_7_ < i; i_7_++)
		is[i_7_] = 0;
	    SamplingExperimentHistogram.setData(is, i, false, 0.0, 0.0);
	}
    }
    
    public double computeVar(float[] fs, int i) {
	float f = 0.0F;
	float f_8_ = 0.0F;
	for (int i_9_ = 0; i_9_ < i; i_9_++) {
	    f += fs[i_9_];
	    f_8_ += fs[i_9_] * fs[i_9_];
	}
	f_8_ = (f_8_ - f * f / (float) i) / (float) i;
	return (double) f_8_;
    }
    
    public float getStat(String string, float[] fs, int i) {
	float f = 0.0F;
	if (string == "Mean")
	    f = (float) ComputeMean(fs, i);
	else if (string == "Range")
	    f = (float) ComputeRange(fs, i);
	else if (string == "Median")
	    f = (float) ComputeMedian(fs, i);
	else if (string == "SD")
	    f = (float) ComputeSD(fs, i);
	else if (string == "MAD")
	    f = (float) ComputeMAD(fs, i);
	else if (string == "Variance")
	    f = (float) computeVar(fs, i);
	else if (string == "Variance (U)")
	    f = ((float) computeVar(fs, i) * (float) ((SamplingExperimentMainFrame) this).N
		 / (float) (((SamplingExperimentMainFrame) this).N - 1));
	return f;
    }
    
    public float[] getStats(float[] fs, float[] fs_10_) {
	float[] fs_11_ = new float[2];
	fs_11_[0] = getStat(statName1, fs, ((SamplingExperimentMainFrame) this).N);
	fs_11_[1] = getStat(statName2, fs_10_, ((SamplingExperimentMainFrame) this).N2);
	return fs_11_;
    }
    
    //public void paint(Graphics graphics) {
	// /* empty */
    //}
    
    public int[] prepAnimate(SamplingExperimentHistogram SamplingExperimentHistogram, int[] is, int i) {
	int[] is_12_ = new int[i];
	double d = ((SamplingExperimentHistogram) SamplingExperimentHistogram).sv2p;
	int i_13_ = ((SamplingExperimentHistogram) SamplingExperimentHistogram).width;
	double d_14_ = (double) ((SamplingExperimentHistogram) SamplingExperimentHistogram).inter;
	for (int i_15_ = 0; i_15_ < ((SamplingExperimentMainFrame) this).k; i_15_++)
	    ((SamplingExperimentMainFrame) this).sampleFdata[i_15_]
		= ((SamplingExperimentHistogram) SamplingExperimentHistogram).fdata[i_15_];
	for (int i_16_ = 0; i_16_ < i; i_16_++) {
	    int i_17_ = is[i_16_];
	    is_12_[i_16_]
		= (int) (d * (double) ((SamplingExperimentMainFrame) this).sampleFdata[i_17_] + d
			 + d_14_ + 16.0);
	    ((SamplingExperimentMainFrame) this).sampleFdata[i_17_]++;
	}
	return is_12_;
    }
    
    public synchronized float[] sample(int[] is, int i, int i_18_) {
	int i_19_ = i;
	int i_20_ = 0;
	float[] fs = new float[i_18_];
	float f
	    = (((SamplingExperimentMainFrame) this).xdata[1] - ((SamplingExperimentMainFrame) this).xdata[0]) / 2.0F;
	int[] is_21_ = new int[i_19_];
	for (int i_22_ = 0; i_22_ < i_19_; i_22_++) {
	    i_20_ += ((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).h).fdata[i_22_];
	    is_21_[i_22_] = i_20_;
	}
	for (int i_23_ = 0; i_23_ < i_18_; i_23_++) {
	    int i_24_ = (int) Math.floor(Math.random() * (double) i_20_) + 1;
	    for (int i_25_ = 0; i_25_ < i; i_25_++) {
		if (i_24_ <= is_21_[i_25_]) {
		    fs[i_23_] = ((SamplingExperimentMainFrame) this).xdata[i_25_];
		    fs[i_23_] += (Math.random() - 0.5) * (double) f;
		    break;
		}
	    }
	}
	return fs;
    }
    
    public void sample1(int i) {
	Object object = null;
	double d = 0.0;
	double d_26_ = 0.0;
	double d_27_ = 0.0;
	double d_28_ = 0.0;
	int i_29_ = chooseStat(statName1);
	int i_30_ = 0;
	float f = 0.0F;
	float f_31_ = 0.0F;
	float[] fs = new float[2];
	int[] is = new int[((SamplingExperimentMainFrame) this).N];
	int[] is_32_ = new int[((SamplingExperimentMainFrame) this).N2];
	int[] is_33_ = new int[((SamplingExperimentMainFrame) this).N];
	int[] is_34_ = new int[((SamplingExperimentMainFrame) this).N];
	int[] is_35_ = new int[((SamplingExperimentMainFrame) this).N];
	int[] is_36_ = new int[((SamplingExperimentMainFrame) this).N2];
	int[] is_37_ = new int[((SamplingExperimentMainFrame) this).N];
	int[] is_38_ = new int[((SamplingExperimentMainFrame) this).N];
	int[] is_39_ = new int[((SamplingExperimentMainFrame) this).k];
	int[] is_40_ = new int[4];
	float[] fs_41_ = new float[((SamplingExperimentMainFrame) this).N];
	float[] fs_42_ = new float[((SamplingExperimentMainFrame) this).N];
	int i_43_ = i;

		// For Animated Samples (i==i_43 == 1)
	if (i_43_ == 1) {
	    is_40_[0] = 5;
	    is_40_[1] = 5;
	    is_40_[2] = 3;
	    fs_41_ = sample(((SamplingExperimentMainFrame) this).fdata, ((SamplingExperimentMainFrame) this).k,
			    ((SamplingExperimentMainFrame) this).N);
	    if (((SamplingExperimentMainFrame) this).N == ((SamplingExperimentMainFrame) this).N2) {
		for (int i_44_ = 0; i_44_ < ((SamplingExperimentMainFrame) this).N; i_44_++)
		    fs_42_[i_44_] = fs_41_[i_44_];
	    } else
		fs_42_ = sample(((SamplingExperimentMainFrame) this).fdata, ((SamplingExperimentMainFrame) this).k,
				((SamplingExperimentMainFrame) this).N2);
	    fs = getStats(fs_41_, fs_42_);
	    f = fs[0];
	    f_31_ = fs[1];
	    is_39_
		= tally(fs_41_, ((SamplingExperimentMainFrame) this).xdata, ((SamplingExperimentMainFrame) this).k,
			((SamplingExperimentMainFrame) this).N, is_33_, true);
	    for (int i_45_ = 0; i_45_ < ((SamplingExperimentMainFrame) this).N; i_45_++) {
		d += (double) fs_41_[i_45_];
		d_26_ += (double) (fs_41_[i_45_] * fs_41_[i_45_]);
	    }
	    is = prepAnimate(((SamplingExperimentMainFrame) this).hh, is_33_,
			     ((SamplingExperimentMainFrame) this).N);
	    is_39_ = tally(fs, ((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).hhh).xdata,
			   ((SamplingExperimentMainFrame) this).k, 1, is_35_, true);
	    is_34_ = prepAnimate(((SamplingExperimentMainFrame) this).hhh, is_35_, 1);
	    ((SamplingExperimentMainFrame) this).hhh.setStats(false);
	    ((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).hhh).sum += (double) f;
	    ((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).hhh).ssq += (double) (f * f);
	    if (statName2 != "None") {
		for (int i_46_ = 0; i_46_ < ((SamplingExperimentMainFrame) this).N2; i_46_++) {
		    d_27_ += (double) fs_42_[i_46_];
		    d_28_ += (double) (fs_42_[i_46_] * fs_42_[i_46_]);
		}
		((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).hhhh).sum += (double) f_31_;
		((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).hhhh).ssq
		    += (double) (f_31_ * f_31_);
		i_30_ = chooseStat(statName2);
		fs[0] = f_31_;
		is_39_ = tally(fs, ((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) this).hhhh).xdata,
			       ((SamplingExperimentMainFrame) this).k, 1, is_37_, true);
		is_38_ = prepAnimate(((SamplingExperimentMainFrame) this).hhhh, is_37_, 1);
		is_39_ = tally(fs_42_, ((SamplingExperimentMainFrame) this).xdata,
			       ((SamplingExperimentMainFrame) this).k, ((SamplingExperimentMainFrame) this).N2,
			       is_36_, true);
		is_32_ = prepAnimate(((SamplingExperimentMainFrame) this).hh, is_36_,
				     ((SamplingExperimentMainFrame) this).N2);
	    }
	    is_40_[1] = 800;
	    SamplingExperimentAnimateSample samplingExperimentAnimateSample;
	    if (statName2 == "None")
		samplingExperimentAnimateSample = 
			new SamplingExperimentAnimateSample(((SamplingExperimentMainFrame) this).hhh,
					((SamplingExperimentMainFrame) this).statsOn, 1, is_34_,
					is_35_, is_40_, 1, false,
					((SamplingExperimentMainFrame) this).colors[i_29_], null);
	    else {
		is_40_[1] = 800;
		SamplingExperimentAnimateSample samplingExperimentAnimateSample_47_
		    = new SamplingExperimentAnimateSample(((SamplingExperimentMainFrame) this).hhhh,
				   ((SamplingExperimentMainFrame) this).statsOn, 1, is_38_,
				   is_37_, is_40_, 1, false,
				   ((SamplingExperimentMainFrame) this).colors[i_30_], null);
		is_40_[1] = 5;
		for (int i_48_ = 0; i_48_ < ((SamplingExperimentMainFrame) this).stats.length;
		     i_48_++)
		    ((SamplingExperimentMainFrame) this).stats[i_48_] = false;
		((SamplingExperimentMainFrame) this).stats[i_30_] = true;
		SamplingExperimentAnimateSample samplingExperimentAnimateSample_49_
		    = new SamplingExperimentAnimateSample(((SamplingExperimentMainFrame) this).hh,
				   ((SamplingExperimentMainFrame) this).stats, 1, is_32_, is_36_,
				   is_40_, ((SamplingExperimentMainFrame) this).N2, true,
				   ((SamplingExperimentMainFrame) this).distColor, samplingExperimentAnimateSample_47_);
		((SamplingExperimentAnimateSample) samplingExperimentAnimateSample_49_).sum = d_27_;
		((SamplingExperimentAnimateSample) samplingExperimentAnimateSample_49_).ssq = d_28_;
		is_40_[1] = 800;
		samplingExperimentAnimateSample = new SamplingExperimentAnimateSample(((SamplingExperimentMainFrame) this).hhh,
					((SamplingExperimentMainFrame) this).statsOn, 1, is_34_,
					is_35_, is_40_, 1, false,
					((SamplingExperimentMainFrame) this).colors[i_29_],
					samplingExperimentAnimateSample_49_);
	    }
	    is_40_[1] = 5;
	    for (int i_50_ = 0; i_50_ < ((SamplingExperimentMainFrame) this).stats.length; i_50_++)
		((SamplingExperimentMainFrame) this).stats[i_50_] = false;

	    ((SamplingExperimentMainFrame) this).stats[i_29_] = true;
	    SamplingExperimentAnimateSample samplingExperimentAnimateSample_51_
			= new SamplingExperimentAnimateSample(((SamplingExperimentMainFrame) this).hh, 							
					((SamplingExperimentMainFrame) this).stats,
			       1, is, is_33_, is_40_, ((SamplingExperimentMainFrame) this).N,
			       true, ((SamplingExperimentMainFrame) this).distColor, samplingExperimentAnimateSample);
	    ((SamplingExperimentAnimateSample) samplingExperimentAnimateSample_51_).sum = d;
	    ((SamplingExperimentAnimateSample) samplingExperimentAnimateSample_51_).ssq = d_26_;
	    samplingExperimentAnimateSample_51_.start();
	} else {			// For regular (Non-Animated) Samples (i==i_43 > 1)
	    ((SamplingExperimentMainFrame) this).stx = 
	    	new SamplingExperimentGenerateSample(this, i_43_, i_29_, i_30_);
	    ((SamplingExperimentMainFrame) this).stx.start();
	}
    }
    
    public void sample1NoAnimation(int i) {
    	int i_43_ = i;
    	int i_29_ = chooseStat(statName1);
    	int i_30_ = chooseStat(statName2);
    	
    		//    	 For regular (Non-Animated) Samples (i==i_43 > 1)
	    ((SamplingExperimentMainFrame) this).stx = 
	    	new SamplingExperimentGenerateSample(this, i_43_, i_29_, i_30_);
	    ((SamplingExperimentMainFrame) this).stx.start();
    }

    public void setDist() {
	if (((SamplingExperimentMainFrame) this).h != null)
	    ((SamplingExperimentMainFrame) this).h
		.setTitle("Parent populaton (can be changed with the mouse)");
	if (((SamplingExperimentMainFrame) this).theDist == "Normal") {
	    for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++)
		((SamplingExperimentMainFrame) this).fdata[i] = ((SamplingExperimentMainFrame) this).normdata[i];
	} else if (((SamplingExperimentMainFrame) this).theDist == "Uniform") {
	    for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++)
		((SamplingExperimentMainFrame) this).fdata[i] = 15;
	} else if (((SamplingExperimentMainFrame) this).theDist == "Skewed") {
	    for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++)
		((SamplingExperimentMainFrame) this).fdata[i] = ((SamplingExperimentMainFrame) this).skewedData[i];
	} else if (((SamplingExperimentMainFrame) this).theDist == "Custom") {
	    for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++)
		((SamplingExperimentMainFrame) this).fdata[i] = 0;
	    ((SamplingExperimentMainFrame) this).h.setTitle("Paint distribution with mouse.");
	} else if (((SamplingExperimentMainFrame) this).theDist == "SOCR Distribution") {
		int [] temp_fdata = ((SamplingDistributionExperiment)(this.applet)).getFreqSOCRDistributionData();
		
		for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++)
		{	((SamplingExperimentMainFrame) this).fdata[i] = temp_fdata[i];
			//System.out.println("fdata["+i+"]="+fdata[i]);
		}
	}
	
	if (((SamplingExperimentMainFrame) this).h != null) {
	    double d = 0.0;
	    double d_52_ = 0.0;
	    for (int i = 0; i < ((SamplingExperimentMainFrame) this).k; i++) {
		d += (double) (((SamplingExperimentMainFrame) this).xdata[i]
			       * (float) ((SamplingExperimentMainFrame) this).fdata[i]);
		d_52_ += (double) (((SamplingExperimentMainFrame) this).xdata[i]
				   * ((SamplingExperimentMainFrame) this).xdata[i]
				   * (float) ((SamplingExperimentMainFrame) this).fdata[i]);
	    }
	    ((SamplingExperimentMainFrame) this).h.setData(((SamplingExperimentMainFrame) this).fdata,
					((SamplingExperimentMainFrame) this).k, false, d, d_52_);
	    if (((SamplingExperimentMainFrame) this).theDist.equals("Custom"))
		((SamplingExperimentMainFrame) this).h.setFmax(40.0);
	    ((SamplingExperimentMainFrame) this).ns = 0;
	    clearAll();
	}
	setXdata2();
    }
    
    /* Returns the numner of bins in the sampling distribution
     * ((SamplingExperimentMainFrame) this).k
     */
    public int getBinNumbers()
    {	return this.k;
    }
    
    public void setXdata2() {
	int i = 1;
	if (statName1.indexOf("Variance") != -1
	    || statName2.indexOf("Variance") != -1) {
	    int i_53_;
	    if (statName2.indexOf("Variance") == -1)
		i_53_ = ((SamplingExperimentMainFrame) this).N;
	    else if (statName1.indexOf("Variance") == -1)
		i_53_ = ((SamplingExperimentMainFrame) this).N2;
	    else
		i_53_ = Math.min(((SamplingExperimentMainFrame) this).N, ((SamplingExperimentMainFrame) this).N2);
	    if (((SamplingExperimentMainFrame) this).theDist.equals("Custom"))
		i = 8;
	    else if (((SamplingExperimentMainFrame) this).theDist.equals("Normal")) {
		switch (i_53_) {
		case 2:
		    i = 6;
		    break;
		case 5:
		    i = 4;
		    break;
		case 10:
		    i = 3;
		    break;
		case 16:
		    i = 3;
		    break;
		case 20:
		    i = 2;
		    break;
		case 25:
		    i = 2;
		    break;
		default:
		    i = 1;
		}
	    } else if (((SamplingExperimentMainFrame) this).theDist.equals("Skewed")) {
		switch (i_53_) {
		case 2:
		    i = 8;
		    break;
		case 5:
		    i = 6;
		    break;
		case 10:
		    i = 4;
		    break;
		case 16:
		    i = 4;
		    break;
		case 20:
		    i = 3;
		    break;
		case 25:
		    i = 3;
		    break;
		default:
		    i = 1;
		}
	    } else if (((SamplingExperimentMainFrame) this).theDist.equals("Uniform")) {
		switch (i_53_) {
		case 2:
		    i = 8;
		    break;
		case 5:
		    i = 8;
		    break;
		case 10:
		    i = 6;
		    break;
		case 16:
		    i = 6;
		    break;
		case 20:
		    i = 5;
		    break;
		case 25:
		    i = 5;
		    break;
		default:
		    i = 1;
		}
	    } else
		i = 1;
	}
	for (int i_54_ = 1; i_54_ < ((SamplingExperimentMainFrame) this).k; i_54_++)
	    ((SamplingExperimentMainFrame) this).xdata2[i_54_]
		= ((SamplingExperimentMainFrame) this).xdata2[i_54_ - 1] + (float) i;
	if (((SamplingExperimentMainFrame) this).hhh != null)
	    ((SamplingExperimentMainFrame) this).hhh.setData(((SamplingExperimentMainFrame) this).xdata2);
	if (((SamplingExperimentMainFrame) this).hhhh != null)
	    ((SamplingExperimentMainFrame) this).hhhh.setData(((SamplingExperimentMainFrame) this).xdata2);
    }
    
    public int[] tally(float[] fs, float[] fs_55_, int i, int i_56_, int[] is,
		       boolean bool) {
	int[] is_57_ = new int[i];
	for (int i_58_ = 0; i_58_ < i; i_58_++)
	    is_57_[i_58_] = 0;
	float f = fs_55_[1] - fs_55_[0];
	for (int i_59_ = 0; i_59_ < i_56_; i_59_++) {
	    int i_60_
		= Math.min(Math.max(Math.round(fs[i_59_] / f), 0), i - 1);
	    if (bool)
		is[i_59_] = i_60_;
	    is_57_[i_60_]++;
	}
	return is_57_;
    }
}
