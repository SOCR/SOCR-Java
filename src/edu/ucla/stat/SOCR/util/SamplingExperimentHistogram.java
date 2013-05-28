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
/* SamplingExperimentHistogram
 *
 * This class is part of the SOCR Experiment: Sampling Distribution (CLT) Experiment,
 * which demonstrates the properties of the sampling distributions of various sample statistics.
 */

package edu.ucla.stat.SOCR.util;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLabel;

public class SamplingExperimentHistogram extends JPanel implements MouseMotionListener, MouseListener
{
    int[] f;
    int yinc;
    final int yoffset = 5; // 30;
    final int yoffset2 = 5; //16;
    final int xoffset = 5; //120;
    int[] fdata;
    int[] mRect;
    int yintervals;
    int XaxisDec;
    int numInt;
    int width;
    int rWidth;
    int rHeight;
    int gap;
    int theBin;
    int inter;
    int Nobs;
    double fmin;
    double fmax;
    double sp2v;
    double sv2p;
    double ip2v;
    double iv2p;
    double sum;
    double ssq;
    boolean[] stats;
    float[] xdata;
    boolean top;
    boolean norm;
    boolean showStat;
    boolean clickable;
    boolean labelXaxis;
    boolean endsOnly;
    Color[] colors;
    public String title;
    Font font;
    Font fontN;
    JLabel testlabel;
    Color color;

    double mean111, median111, sd111, skewness111, kurtosis111;

    
    public SamplingExperimentHistogram(String string, float[] fs, int[] is, int i,
		     boolean[] bools, boolean bool, int i_0_, int i_1_,
		     int i_2_, boolean bool_3_, boolean bool_4_, int i_5_,
		     Color[] colors) {
	initialize(string, fs, is, i, bools, bool, i_0_, i_1_, i_2_, bool_3_,
		   bool_4_, i_5_, colors);
    }
    
    public SamplingExperimentHistogram(float[] fs, int[] is, int i) {
	String string = "";
	boolean[] bools = new boolean[8];
	for (int i_6_ = 0; i_6_ < 8; i_6_++)
	    bools[i_6_] = false;
	int i_7_ = 5;
	boolean bool = true;
	((SamplingExperimentHistogram) this).endsOnly = false;
	int i_8_ = 400;
	int i_9_ = 200;
	Color[] colors
	    = { Color.blue, Color.magenta, Color.red, Color.green, Color.black,
		Color.cyan, Color.darkGray, Color.lightGray };
	boolean bool_10_ = false;
	boolean bool_11_ = true;
	int i_12_ = 0;
	initialize(string, fs, is, i, bools, bool, i_8_, i_9_, i_7_, bool_10_,
		   bool_11_, i_12_, colors);
    }
    
    public void AdRect(int i, Graphics graphics) {
	graphics.clearRect(((SamplingExperimentHistogram) this).mRect[0],
			   ((SamplingExperimentHistogram) this).mRect[1],
			   ((SamplingExperimentHistogram) this).mRect[2],
			   ((SamplingExperimentHistogram) this).mRect[3]);
	((SamplingExperimentHistogram) this).mRect[1] += i;
	graphics.fillRect(((SamplingExperimentHistogram) this).mRect[0],
			  ((SamplingExperimentHistogram) this).mRect[1],
			  ((SamplingExperimentHistogram) this).mRect[2],
			  ((SamplingExperimentHistogram) this).mRect[3]);
    }
    
    public double ComputeGroupedMean(int[] is, float[] fs, int i) {
	double d = 0.0;
	int i_13_ = 0;
	for (int i_14_ = 0; i_14_ < i; i_14_++) {
	    d += (double) (fs[i_14_] * (float) is[i_14_]);
	    i_13_ += is[i_14_];
	}
	if (i_13_ > 0)
	    d /= (double) i_13_;
	return d;
    }
    
    public double ComputeGroupedMedian(int[] is, int i, int i_15_) {
	double d = 0.0;
	boolean bool = false;
	int i_16_ = i_15_ / 2;
	boolean bool_17_;
	if (i_16_ * 2 == i_15_)
	    bool_17_ = true;
	else {
	    bool_17_ = false;
	    i_16_++;
	}
	int i_18_ = 0;
	int i_19_ = 0;
	for (int i_20_ = 0; i_20_ < i; i_20_++) {
	    i_18_ += is[i_20_];
	    if (i_18_ >= i_16_) {
		i_19_ = i_20_;
		break;
	    }
	}
	if (bool_17_) {
	    if (i_18_ == i_16_) {
		int i_21_ = i_19_ + 1;
		for (int i_22_ = i_21_; i_22_ < i; i_22_++) {
		    if (is[i_22_] != 0) {
			i_21_ = i_22_;
			break;
		    }
		}
		d = (double) (((SamplingExperimentHistogram) this).xdata[i_19_]
			      + ((SamplingExperimentHistogram) this).xdata[i_21_]) / 2.0;
	    } else
		d = (double) ((SamplingExperimentHistogram) this).xdata[i_19_];
	} else
	    d = (double) ((SamplingExperimentHistogram) this).xdata[i_19_];
	return d;
    }
    
    public double ComputeGroupedRange(int[] is, float[] fs, int i) {
	boolean bool = false;
	boolean bool_23_ = false;
	int i_24_ = 0;
	int i_25_;
	for (i_25_ = -1; i_24_ == 0 && i_25_ < i - 1; i_24_ = is[i_25_])
	    i_25_++;
	int i_26_ = i_25_;
	i_24_ = is[i - 1];
	for (i_25_ = i - 1; i_24_ <= 0 && i_25_ > 0; i_24_ = is[i_25_])
	    i_25_--;
	int i_27_ = i_25_;
	double d = (double) (fs[i_27_] - fs[i_26_]);
	return d;
    }
    
    public double ComputeGroupedsd(int[] is, float[] fs, int i) {
	double d = 0.0;
	double d_28_ = 0.0;
	int i_29_ = 0;
	for (int i_30_ = 0; i_30_ < i; i_30_++) {
	    d += (double) (fs[i_30_] * (float) is[i_30_]);
	    d_28_ += (double) (fs[i_30_] * fs[i_30_] * (float) is[i_30_]);
	    i_29_ += is[i_30_];
	}
	d_28_ = Math.sqrt((d_28_ - d * d / (double) i_29_) / (double) i_29_);
	return d_28_;
    }
    
    public double ComputeKurt(int[] is, float[] fs, int i, double d) {
	double d_31_ = ComputeGroupedsd(is, fs, i);
	double d_32_ = 0.0;
	int i_33_ = 0;
	for (int i_34_ = 0; i_34_ < i; i_34_++) {
	    d_32_
		+= Math.pow((double) fs[i_34_] - d, 4.0) * (double) is[i_34_];
	    i_33_ += is[i_34_];
	}
	if (d_31_ == 0.0 || i_33_ < 2)
	    d_32_ = 0.0;
	else
	    d_32_ = d_32_ / (double) i_33_ / Math.pow(d_31_, 4.0) - 3.0;
	return d_32_;
    }
    
    public double ComputeMAD(int[] is, float[] fs, int i) {
	int i_35_ = 0;
	double d = 0.0;
	double d_36_ = 0.0;
	for (int i_37_ = 0; i_37_ < i; i_37_++) {
	    i_35_ += is[i_37_];
	    d += (double) (fs[i_37_] * (float) is[i_37_]);
	}
	d /= (double) i_35_;
	for (int i_38_ = 0; i_38_ < i; i_38_++)
	    d_36_ += Math.abs((double) fs[i_38_] - d) * (double) is[i_38_];
	d_36_ /= (double) i_35_;
	return d_36_;
    }
    
    public double ComputeSkew(int[] is, float[] fs, int i, double d) {
	double d_39_ = ComputeGroupedsd(is, fs, i);
	double d_40_ = 0.0;
	int i_41_ = 0;
	for (int i_42_ = 0; i_42_ < i; i_42_++) {
	    d_40_
		+= Math.pow((double) fs[i_42_] - d, 3.0) * (double) is[i_42_];
	    i_41_ += is[i_42_];
	}
	if (d_39_ == 0.0 || i_41_ < 2)
	    d_40_ = 0.0;
	else
	    d_40_ = d_40_ / (double) i_41_ / Math.pow(d_39_, 3.0);
	return d_40_;
    }
    
    private float[] MeanSd() {
	float[] fs = new float[3];
	int i = 0;
	float f = 0.0F;
	float f_43_ = 0.0F;
	for (int i_44_ = 0; i_44_ < ((SamplingExperimentHistogram) this).numInt; i_44_++) {
	    i += ((SamplingExperimentHistogram) this).fdata[i_44_];
	    f += (((SamplingExperimentHistogram) this).xdata[i_44_]
		  * (float) ((SamplingExperimentHistogram) this).fdata[i_44_]);
	    f_43_ += (((SamplingExperimentHistogram) this).xdata[i_44_]
		      * ((SamplingExperimentHistogram) this).xdata[i_44_]
		      * (float) ((SamplingExperimentHistogram) this).fdata[i_44_]);
	}
	fs[0] = (float) i;
	fs[1] = f / fs[0];
	fs[2] = (float) Math.sqrt((double) ((f_43_ - f * f / fs[0]) / fs[0]));
	return fs;
    }
    
    public int PrepAdd(double d) {
	float f = ((SamplingExperimentHistogram) this).xdata[1] - ((SamplingExperimentHistogram) this).xdata[0];
	float f_45_ = ((SamplingExperimentHistogram) this).xdata[0] - f / 2.0F;
	((SamplingExperimentHistogram) this).theBin
	    = (int) Math.floor((d - (double) f_45_) / (double) f);
	//int i = (120 + ((SamplingExperimentHistogram) this).theBin * ((SamplingExperimentHistogram) this).width
	int i = (10 + ((SamplingExperimentHistogram) this).theBin * ((SamplingExperimentHistogram) this).width
		 + ((SamplingExperimentHistogram) this).gap / 2);
	((SamplingExperimentHistogram) this).mRect[0] = i;
	((SamplingExperimentHistogram) this).mRect[2] = ((SamplingExperimentHistogram) this).width;
	((SamplingExperimentHistogram) this).mRect[3] = ((SamplingExperimentHistogram) this).yinc;
	((SamplingExperimentHistogram) this).mRect[1] = 16 - ((SamplingExperimentHistogram) this).mRect[3];
	int i_46_ = (((SamplingExperimentHistogram) this).f[((SamplingExperimentHistogram) this).theBin]
		     - ((SamplingExperimentHistogram) this).yinc);
	return i_46_;
    }
    
    public final void SetNorm(boolean bool) {
	((SamplingExperimentHistogram) this).norm = bool;
	this.repaint();
    }
    
    public void addX(int i) {
	int i_47_ = i;
	((SamplingExperimentHistogram) this).fdata[i_47_]++;
	((SamplingExperimentHistogram) this).f[i_47_]
	    = (int) Math.round((((SamplingExperimentHistogram) this).sv2p
				* (double) ((SamplingExperimentHistogram) this).fdata[i_47_])
			       + (double) ((SamplingExperimentHistogram) this).inter);
	if ((double) ((SamplingExperimentHistogram) this).fdata[i_47_]
	    >= ((SamplingExperimentHistogram) this).fmax) {
	    ((SamplingExperimentHistogram) this).fmax += (double) ((SamplingExperimentHistogram) this).yintervals;
	    setSlopes();
	} else
	    ((SamplingExperimentHistogram) this).Nobs++;
	update(this.getGraphics());
    }
    
    public void changeDist(int i, int i_48_) {
	int i_49_ = ((SamplingExperimentHistogram) this).width + ((SamplingExperimentHistogram) this).gap;
	Graphics graphics = this.getGraphics();
	graphics.setColor(((SamplingExperimentHistogram) this).color);
	//int i_50_ = ((i - 120 - ((SamplingExperimentHistogram) this).gap / 2)
	int i_50_ = ((i - 10 - ((SamplingExperimentHistogram) this).gap / 2)
		     / ((SamplingExperimentHistogram) this).width);
	if (i_50_ >= 0 && i_50_ < ((SamplingExperimentHistogram) this).numInt) {
	    i_48_ = Math.max(i_48_, 16);
	    int i_51_ = i_48_ - 16;
	    ((SamplingExperimentHistogram) this).fdata[i_50_]
		= Math.max(0, (int) Math.round((((SamplingExperimentHistogram) this).sp2v
						* (double) i_51_)
					       + ((SamplingExperimentHistogram) this).ip2v));
	    ((SamplingExperimentHistogram) this).f[i_50_]
		= (int) (((double) ((SamplingExperimentHistogram) this).fdata[i_50_]
			  * ((SamplingExperimentHistogram) this).sv2p)
			 + (double) ((SamplingExperimentHistogram) this).inter);
	    //int i_52_ = 120 + ((SamplingExperimentHistogram) this).gap / 2 + i_50_ * i_49_;
	    int i_52_ = 10 + ((SamplingExperimentHistogram) this).gap / 2 + i_50_ * i_49_;
	    graphics.fillRect(i_52_, ((SamplingExperimentHistogram) this).f[i_50_] + 16,
			      ((SamplingExperimentHistogram) this).width,
			      (((SamplingExperimentHistogram) this).rHeight
			       - ((SamplingExperimentHistogram) this).f[i_50_]));
	    graphics.clearRect(i_52_, 16, ((SamplingExperimentHistogram) this).width,
			       Math.min(((SamplingExperimentHistogram) this).f[i_50_],
					((SamplingExperimentHistogram) this).rHeight));
	}
    }
    
    public void clear() {
	for (int i = 0; i < ((SamplingExperimentHistogram) this).numInt; i++)
	    ((SamplingExperimentHistogram) this).fdata[i] = 0;
		setData(((SamplingExperimentHistogram) this).fdata, 
				((SamplingExperimentHistogram) this).numInt, false, 0.0, 0.0);
		((SamplingExperimentHistogram) this).Nobs = 0;
    }
    
    private void drawAxes(Graphics graphics) {
	graphics.setColor(Color.black);
	graphics.setFont(((SamplingExperimentHistogram) this).font);
	FontMetrics fontmetrics
	    = graphics.getFontMetrics(((SamplingExperimentHistogram) this).font);
	FontMetrics fontmetrics_53_
	    = graphics.getFontMetrics(((SamplingExperimentHistogram) this).fontN);
	int i = fontmetrics.getAscent();
	i /= 2;
	//int i_54_ = 120 - ((SamplingExperimentHistogram) this).gap / 2;
	int i_54_ = 10 - ((SamplingExperimentHistogram) this).gap / 2;
	int i_55_ = i_54_ - 4;
	graphics.drawLine(i_54_ - 1, ((SamplingExperimentHistogram) this).rHeight + 16,
			  i_54_ - 1, 11);
	int i_56_ = (int) Math.round(((SamplingExperimentHistogram) this).fmax
				     / (double) ((SamplingExperimentHistogram) this).yintervals);
	double d
	    = ((SamplingExperimentHistogram) this).fmax / (double) ((SamplingExperimentHistogram) this).yintervals;
	int i_57_ = ((SamplingExperimentHistogram) this).yintervals * i_56_;
	int i_58_ = ((SamplingExperimentHistogram) this).rHeight + 16;
	if (((SamplingExperimentHistogram) this).top == true) {
	    for (int i_59_ = 0; i_59_ <= ((SamplingExperimentHistogram) this).yintervals;
		 i_59_++) {
		i_58_ = (int) Math.round(((double) i_59_ * ((SamplingExperimentHistogram) this).sv2p * d)
					 + (double) ((SamplingExperimentHistogram) this).rHeight + 16.0);
		graphics.setColor(Color.lightGray);
		graphics.drawLine(i_54_ - 2, i_58_, i_55_, i_58_);
		graphics.setColor(Color.black);
		String string = String.valueOf(i_59_ * i_56_) + " ";
		int i_60_ = fontmetrics.stringWidth(string) + 3;
		graphics.drawString(string, i_54_ - i_60_, i_58_ + i);
	    }
	}
	//int i_61_ = 120 + ((SamplingExperimentHistogram) this).gap / 2;
	int i_61_ = 10 + ((SamplingExperimentHistogram) this).gap / 2;
	int i_62_ = ((SamplingExperimentHistogram) this).width + ((SamplingExperimentHistogram) this).gap;
	i_61_ -= i_62_;
	int i_63_ = ((SamplingExperimentHistogram) this).rHeight + 16 + 4;
	int i_64_ = i_63_ + 4;
	graphics.setFont(((SamplingExperimentHistogram) this).fontN);
	for (int i_65_ = 0; i_65_ < ((SamplingExperimentHistogram) this).numInt; i_65_++) {
	    i_61_ += i_62_;
	    if (((SamplingExperimentHistogram) this).labelXaxis
		|| (((SamplingExperimentHistogram) this).endsOnly
		    && (i_65_ == 0
			|| i_65_ == ((SamplingExperimentHistogram) this).numInt - 1))) {
		double d_66_ = (double) ((SamplingExperimentHistogram) this).xdata[i_65_];
		String string = format(d_66_, ((SamplingExperimentHistogram) this).XaxisDec);
		int i_67_ = fontmetrics_53_.stringWidth(string);
		int i_68_ = (i_62_ - i_67_) / 2 + 1;
		graphics.drawString(string, i_61_ + i_68_, i_64_);
	    }
	    graphics.setColor(Color.lightGray);
	    graphics.drawLine(i_61_, ((SamplingExperimentHistogram) this).rHeight + 16, i_61_,
			      i_63_);
	    graphics.setColor(Color.black);
	}
	i_61_ += i_62_;
	graphics.setColor(Color.lightGray);
	graphics.drawLine(i_61_, ((SamplingExperimentHistogram) this).rHeight + 16, i_61_,
			  i_63_);
	graphics.setColor(Color.black);
	graphics.drawLine(i_54_ - 1, ((SamplingExperimentHistogram) this).rHeight + 16, i_61_,
			  ((SamplingExperimentHistogram) this).rHeight + 16);
    }
    
    private void fitNormal(Graphics graphics) {
	int i = ((SamplingExperimentHistogram) this).rHeight + 16;
	float[] fs = new float[3];
	float f = ((SamplingExperimentHistogram) this).xdata[1] - ((SamplingExperimentHistogram) this).xdata[0];
	int i_69_ = ((((SamplingExperimentHistogram) this).width + ((SamplingExperimentHistogram) this).gap)
		     * ((SamplingExperimentHistogram) this).numInt);
	boolean bool = false;
	fs = MeanSd();
	int i_70_ = 5;
	float f_71_ = fs[1];
	float f_72_ = fs[2];
	float f_73_ = f / (float) ((SamplingExperimentHistogram) this).width;
	f_73_ = f_73_ /= f_72_;
	double d = (double) (f / 2.0F / f_72_);
	double d_74_ = (0.5 - zprob(d)) * 2.0;
	d_74_ *= (double) ((SamplingExperimentHistogram) this).Nobs;
	double d_75_ = Math.sqrt(0.15915494309189535);
	d_75_ = d_75_ * d_74_ / 0.3989;
	//int i_76_ = 120 + ((SamplingExperimentHistogram) this).gap / 2;
	int i_76_ = 10 + ((SamplingExperimentHistogram) this).gap / 2;

	double d_77_
	    = (double) ((((SamplingExperimentHistogram) this).xdata[0] - f / 2.0F - f_71_)
			/ f_72_);
	float f_78_ = (float) (d_75_ * Math.exp(-d_77_ * d_77_ / 2.0));
	int i_79_
	    = (int) Math.min((((SamplingExperimentHistogram) this).sv2p * (double) f_78_
			      + (double) ((SamplingExperimentHistogram) this).inter + 16.0),
			     (double) i);
	if ((double) f_72_ > 0.0) {
	    for (int i_80_ = 0; i_80_ < i_69_; i_80_ += i_70_) {
		d_77_ += (double) ((float) i_70_ * f_73_);
		f_78_ = (float) (d_75_ * Math.exp(-d_77_ * d_77_ / 2.0));
		int i_81_
		    = (int) Math.min((((SamplingExperimentHistogram) this).sv2p * (double) f_78_
				      + (double) ((SamplingExperimentHistogram) this).inter
				      + 16.0),
				     (double) i);
		graphics.drawLine(i_76_, i_79_, i_76_ + i_70_, i_81_);
		i_76_ += i_70_;
		i_79_ = i_81_;
	    }
	}
    }
    
    static String format(double d, int i) {
	String string;
	if (d < 0.0)
	    string = "-";
	else
	    string = "";
	String string_82_;
	if (i == 0) {
	    int i_83_ = (int) Math.rint(d);
	    string_82_ = String.valueOf(i_83_);
	} else {
	    double d_84_ = Math.pow(10.0, (double) i);
	    int i_85_ = (int) Math.rint(d_84_ * Math.abs(d));
	    if (i_85_ == 0) {
		string_82_ = "0.";
		for (int i_86_ = 0; i_86_ < i; i_86_++)
		    string_82_ += "0";
	    } else {
		string_82_ = String.valueOf(i_85_);
		int i_87_ = string_82_.length();
		if (i_87_ < i) {
		    string_82_ = ".";
		    for (int i_88_ = 0; i_88_ < i - i_87_; i_88_++)
			string_82_ += "0";
		    string_82_ += i_85_;
		} else {
		    String string_89_ = string_82_.substring(0, i_87_ - i);
		    String string_90_ = string_82_.substring(i_87_ - i);
		    string_82_ = string_89_ + "." + string_90_;
		}
	    }
	}
	if (string_82_.indexOf(".") == 0)
	    string_82_ = "0" + string_82_;
	string_82_ = string + string_82_;
	return string_82_;
    }
    
    public int getNumSamples() {
	return ((SamplingExperimentHistogram) this).Nobs;
    }
    

	/* Initialize the SamplingExperimentHistogram Panel
 	 */
    void initialize(String string, float[] fs, int[] is, int i,
		    boolean[] bools, boolean bool, int i_91_, int i_92_,
		    int i_93_, boolean bool_94_, boolean bool_95_, int i_96_,
		    Color[] colors) {
	((SamplingExperimentHistogram) this).font = new Font("TimesRoman", 0, 12);
	((SamplingExperimentHistogram) this).showStat = false;
	((SamplingExperimentHistogram) this).fontN = new Font("TimesRoman", 0, 9);
	((SamplingExperimentHistogram) this).title = string;
	((SamplingExperimentHistogram) this).labelXaxis = bool_95_;
	((SamplingExperimentHistogram) this).XaxisDec = 0;
	((SamplingExperimentHistogram) this).clickable = bool_94_;
	((SamplingExperimentHistogram) this).yintervals = i_93_;
	((SamplingExperimentHistogram) this).norm = false;
	((SamplingExperimentHistogram) this).sum = 0.0;
	((SamplingExperimentHistogram) this).ssq = 0.0;
	((SamplingExperimentHistogram) this).Nobs = 0;
	((SamplingExperimentHistogram) this).stats = new boolean[bools.length];
	for (int i_97_ = 0; i_97_ < bools.length; i_97_++)
	    ((SamplingExperimentHistogram) this).stats[i_97_] = bools[i_97_];
	int i_98_ = colors.length;
	((SamplingExperimentHistogram) this).colors = new Color[i_98_];
	for (int i_99_ = 0; i_99_ < i_98_; i_99_++)
	    ((SamplingExperimentHistogram) this).colors[i_99_] = colors[i_99_];
	((SamplingExperimentHistogram) this).top = bool;
	((SamplingExperimentHistogram) this).gap = i_96_;
	((SamplingExperimentHistogram) this).numInt = i;
	((SamplingExperimentHistogram) this).f = new int[i];
	((SamplingExperimentHistogram) this).mRect = new int[4];
	((SamplingExperimentHistogram) this).mRect[0] = 0;
	((SamplingExperimentHistogram) this).mRect[1] = 0;
	((SamplingExperimentHistogram) this).mRect[2] = ((SamplingExperimentHistogram) this).width;
	((SamplingExperimentHistogram) this).mRect[3] = 0;
	((SamplingExperimentHistogram) this).fdata = new int[i];
	((SamplingExperimentHistogram) this).xdata = new float[i];
	((SamplingExperimentHistogram) this).fmin = 0.0;
	((SamplingExperimentHistogram) this).fmax = 0.0;
	boolean bool_100_ = true;

	for (int i_101_ = 0; i_101_ < i; i_101_++) {
	    ((SamplingExperimentHistogram) this).fdata[i_101_] = is[i_101_];
	    ((SamplingExperimentHistogram) this).xdata[i_101_] = fs[i_101_];
	    ((SamplingExperimentHistogram) this).fmin
		= Math.min(((SamplingExperimentHistogram) this).fmin,
			   (double) ((SamplingExperimentHistogram) this).fdata[i_101_]);
	    ((SamplingExperimentHistogram) this).fmax
		= Math.max(((SamplingExperimentHistogram) this).fmax,
			   (double) ((SamplingExperimentHistogram) this).fdata[i_101_]);
	    ((SamplingExperimentHistogram) this).sum
		+= (double) (((SamplingExperimentHistogram) this).xdata[i_101_]
			     * (float) ((SamplingExperimentHistogram) this).fdata[i_101_]);
	    ((SamplingExperimentHistogram) this).ssq
		+= (double) (((SamplingExperimentHistogram) this).xdata[i_101_]
			     * ((SamplingExperimentHistogram) this).xdata[i_101_]
			     * (float) ((SamplingExperimentHistogram) this).fdata[i_101_]);
	    ((SamplingExperimentHistogram) this).Nobs += ((SamplingExperimentHistogram) this).fdata[i_101_];
	    if (((SamplingExperimentHistogram) this).fdata[i_101_]
		!= ((SamplingExperimentHistogram) this).fdata[0])
		bool_100_ = false;
	}
	if (((SamplingExperimentHistogram) this).fmax == 0.0)
	    ((SamplingExperimentHistogram) this).fmax = (double) ((SamplingExperimentHistogram) this).yintervals;
	else if (bool_100_)
	    ((SamplingExperimentHistogram) this).fmax += 5.0;
	if (((SamplingExperimentHistogram) this).fmax < (double) ((SamplingExperimentHistogram) this).yintervals)
	    ((SamplingExperimentHistogram) this).fmax = (double) ((SamplingExperimentHistogram) this).yintervals;
	((SamplingExperimentHistogram) this).rWidth = i_91_;
	//((SamplingExperimentHistogram) this).width = ((((SamplingExperimentHistogram) this).rWidth - 120 - 10)
	((SamplingExperimentHistogram) this).width = ((((SamplingExperimentHistogram) this).rWidth - 10 - 10)
				    / ((SamplingExperimentHistogram) this).numInt);
	this.setSize(((SamplingExperimentHistogram) this).rWidth, i_92_);
	((SamplingExperimentHistogram) this).rHeight = i_92_ - 16 - 30;
	setSlopes();
	((SamplingExperimentHistogram) this).addMouseListener(this);
	((SamplingExperimentHistogram) this).addMouseMotionListener(this);
    }
    
    public void mouseDragged(MouseEvent e) 
    {		//Invoked when a mouse button is pressed on a component and then dragged.
		if (((SamplingExperimentHistogram) this).clickable) changeDist(e.getX(), e.getY());
		((SamplingExperimentHistogram) this).repaint();
    }

   public void mouseMoved(MouseEvent e)
   {		//Invoked when the mouse cursor has been moved onto a 
		//component but no buttons have been pushed.
   }

   public void mouseClicked(MouseEvent e)
   {       	//Invoked when the mouse button has been clicked 
		//(pressed and released) on a component.
    }
   public  void 	mouseEntered(MouseEvent e)
   {       //Invoked when the mouse enters a component.
    }
    public void 	mouseExited(MouseEvent e)
    {      //Invoked when the mouse exits a component.
     }
    public void 	mousePressed(MouseEvent e)
    {       //Invoked when a mouse button has been pressed on a component.
		if (((SamplingExperimentHistogram) this).clickable) changeDist(e.getX(), e.getY());
		((SamplingExperimentHistogram) this).repaint();
     }
    public void 	mouseReleased(MouseEvent e)
    {     	//Invoked when a mouse button has been released on a component.
		//int i = e.getX();
		//i_105_ = e.getY();

		((SamplingExperimentHistogram) this).sum = 0.0;
		((SamplingExperimentHistogram) this).ssq = 0.0;
		((SamplingExperimentHistogram) this).Nobs = 0;
		for (int i_105_ = 0; i_105_ < ((SamplingExperimentHistogram) this).numInt; i_105_++) {
	    		((SamplingExperimentHistogram) this).sum
				+= (double) (((SamplingExperimentHistogram) this).xdata[i_105_]
			     		* (float) ((SamplingExperimentHistogram) this).fdata[i_105_]);
	    		((SamplingExperimentHistogram) this).ssq
				+= (double) (((SamplingExperimentHistogram) this).xdata[i_105_]
			     		* ((SamplingExperimentHistogram) this).xdata[i_105_]
			     		* (float) ((SamplingExperimentHistogram) this).fdata[i_105_]);
	    		((SamplingExperimentHistogram) this).Nobs += ((SamplingExperimentHistogram) this).fdata[i_105_];
		}
		((SamplingExperimentHistogram) this).repaint();
     }

    public void paintComponent(Graphics graphics) {
    	super.paintComponent(graphics);
    	graphics.setFont(((SamplingExperimentHistogram) this).font);
    		// int i = 120 + ((SamplingExperimentHistogram) this).gap / 2;
    	int i = 10+ ((SamplingExperimentHistogram) this).gap / 2;
    	int i_106_ = ((SamplingExperimentHistogram) this).width + ((SamplingExperimentHistogram) this).gap;
    	graphics.setColor(Color.black);
	
    	//graphics.drawString(((SamplingExperimentHistogram) this).title, 124, 12);

    	graphics.setColor(((SamplingExperimentHistogram) this).color);
    	for (int i_107_ = 0; i_107_ < ((SamplingExperimentHistogram) this).numInt; i_107_++) {
    		graphics.fillRect(i, ((SamplingExperimentHistogram) this).f[i_107_] + 16,
			      ((SamplingExperimentHistogram) this).width,
			      (((SamplingExperimentHistogram) this).rHeight
			       - ((SamplingExperimentHistogram) this).f[i_107_]));
    		i += i_106_;
    	}
    	drawAxes(graphics);
    	if (((SamplingExperimentHistogram) this).norm && ((SamplingExperimentHistogram) this).Nobs > 2)
    		fitNormal(graphics);
	
    	computeStats();

    	//if (((SamplingExperimentHistogram) this).showStat)
	    //plotStats(graphics);
    }
   
    public void computeStats() {
	mean111 = 0.0; sd111 = 0.0;

	mean111 = ((SamplingExperimentHistogram) this).sum / (double) ((SamplingExperimentHistogram) this).Nobs;
	if (((SamplingExperimentHistogram) this).Nobs == 1)
	     sd111 = 0.0;
	else sd111= Math.sqrt((((SamplingExperimentHistogram) this).ssq
			     - (((SamplingExperimentHistogram) this).sum * ((SamplingExperimentHistogram) this).sum
				/ (double) ((SamplingExperimentHistogram) this).Nobs))
			    / (double) ((SamplingExperimentHistogram) this).Nobs);

	int i_108_ = 0;
	for (int i_110_ = 0; i_110_ < ((SamplingExperimentHistogram) this).numInt; i_110_++)
	    i_108_ += ((SamplingExperimentHistogram) this).fdata[i_110_];

	median111 = ComputeGroupedMedian(((SamplingExperimentHistogram) this).fdata,
				((SamplingExperimentHistogram) this).numInt, i_108_);
	skewness111 = ComputeSkew(((SamplingExperimentHistogram) this).fdata,
				((SamplingExperimentHistogram) this).xdata,
				((SamplingExperimentHistogram) this).numInt, mean111);
	kurtosis111 = ComputeKurt(((SamplingExperimentHistogram) this).fdata,
				((SamplingExperimentHistogram) this).xdata,
				((SamplingExperimentHistogram) this).numInt, mean111);
    }

    public double [] getComputedHistoStats()
    {	double [] stats = new double[5];
    computeStats();
		stats[0]=mean111; stats[1]=median111; stats[2]=sd111; 
		stats[3]=skewness111; stats[4]=kurtosis111;
		return stats;
     }


    public void plotStats(Graphics graphics) {
	graphics.setFont(((SamplingExperimentHistogram) this).font);
	FontMetrics fontmetrics
	    = graphics.getFontMetrics(((SamplingExperimentHistogram) this).font);
	int i = fontmetrics.getMaxAscent() + 4;
	double d = 0.0;
	int i_108_ = 0;
	double d_109_ = 0.0;
	d = ((SamplingExperimentHistogram) this).sum / (double) ((SamplingExperimentHistogram) this).Nobs;
	if (((SamplingExperimentHistogram) this).Nobs == 1)
	    d_109_ = 0.0;
	else
	    d_109_
		= Math.sqrt((((SamplingExperimentHistogram) this).ssq
			     - (((SamplingExperimentHistogram) this).sum * ((SamplingExperimentHistogram) this).sum
				/ (double) ((SamplingExperimentHistogram) this).Nobs))
			    / (double) ((SamplingExperimentHistogram) this).Nobs);
	for (int i_110_ = 0; i_110_ < ((SamplingExperimentHistogram) this).numInt; i_110_++)
	    i_108_ += ((SamplingExperimentHistogram) this).fdata[i_110_];
	float f = ((SamplingExperimentHistogram) this).xdata[1] - ((SamplingExperimentHistogram) this).xdata[0];
	float f_111_
	    = (float) (((SamplingExperimentHistogram) this).width + ((SamplingExperimentHistogram) this).gap) / f;
	int i_112_ = 12;
	int i_113_ = 0;
	int i_114_ = ((SamplingExperimentHistogram) this).rHeight + 16;
	int i_115_ = i_114_ + 10;
	int i_116_ = i_115_ + 5;
	i_114_ = ((SamplingExperimentHistogram) this).rHeight + 16;
	int i_117_
	    //= 120 + (int) Math.round((d - (double) ((SamplingExperimentHistogram) this).xdata[0]
	    = 10 + (int) Math.round((d - (double) ((SamplingExperimentHistogram) this).xdata[0]
				      + (double) f / 2.0) * (double) f_111_);
	if (i_108_ > 0) {
	    if (((SamplingExperimentHistogram) this).top) {
		String string = String.valueOf(i_108_);
		int i_118_ = 80 - fontmetrics.stringWidth(string);
		graphics.drawString("N=", 4, i_112_);
		graphics.drawString(string, i_118_, i_112_);
		i_112_ += i;
	    }
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		printStat(d, true, f, f_111_, i_112_, i_113_, i_115_, i_116_,
			  "Mean=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		double d_119_;
		if (((SamplingExperimentHistogram) this).stats[0] && i_108_ <= 2)
		    d_119_ = d;
		else
		    d_119_ = ComputeGroupedMedian(((SamplingExperimentHistogram) this).fdata,
						  ((SamplingExperimentHistogram) this).numInt,
						  i_108_);
		int i_120_;
		if (((int) Math.round((d_119_ + (double) f / 2.0)
				      * (double) f_111_)
		     == (int) Math.round((d + (double) f / 2.0)
					 * (double) f_111_))
		    && ((SamplingExperimentHistogram) this).stats[0])
		    i_120_ = i_115_ + 3;
		else
		    i_120_ = i_115_;
		printStat(d_119_, true, f, f_111_, i_112_, i_113_, i_120_,
			  i_116_, "Median=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		boolean bool = ((SamplingExperimentHistogram) this).stats[0] ^ true;
		int i_121_ = (int) Math.round(d_109_ * (double) f_111_);
		graphics.setColor(((SamplingExperimentHistogram) this).colors[i_113_]);
		if (d_109_ > 0.0) {
		    graphics.drawLine(i_117_ - i_121_, i_114_ + 8,
				      i_117_ + i_121_, i_114_ + 8);
		    graphics.drawLine(i_117_ - i_121_, i_114_, i_117_ - i_121_,
				      i_114_ + 8);
		    graphics.drawLine(i_117_ + i_121_, i_114_, i_117_ + i_121_,
				      i_114_ + 8);
		} else
		    bool = false;
		printStat(d_109_, bool, f, f_111_, i_112_, i_113_, i_115_,
			  i_116_, "SD=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		double d_122_ = ComputeGroupedRange(((SamplingExperimentHistogram) this).fdata,
						    ((SamplingExperimentHistogram) this).xdata,
						    ((SamplingExperimentHistogram) this).numInt);
		printStat(d_122_, true, f, f_111_, i_112_, i_113_, i_115_,
			  i_116_, "Range=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		double d_123_ = ComputeSkew(((SamplingExperimentHistogram) this).fdata,
					    ((SamplingExperimentHistogram) this).xdata,
					    ((SamplingExperimentHistogram) this).numInt, d);
		printStat(d_123_, false, f, f_111_, i_112_, i_113_, i_115_,
			  i_116_, "Skewness=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		double d_124_ = ComputeMAD(((SamplingExperimentHistogram) this).fdata,
					   ((SamplingExperimentHistogram) this).xdata,
					   ((SamplingExperimentHistogram) this).numInt);
		printStat(d_124_, false, f, f_111_, i_112_, i_113_, i_115_,
			  i_116_, "MAD=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		double d_125_ = d_109_ * d_109_;
		printStat(d_125_, false, f, f_111_, i_112_, i_113_, i_115_,
			  i_116_, "Variance=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		double d_126_ = ComputeKurt(((SamplingExperimentHistogram) this).fdata,
					    ((SamplingExperimentHistogram) this).xdata,
					    ((SamplingExperimentHistogram) this).numInt, d);
		printStat(d_126_, false, f, f_111_, i_112_, i_113_, i_115_,
			  i_116_, "Kurtosis=", graphics, fontmetrics);
		i_112_ += i;
	    }
	    i_113_++;
	    if (((SamplingExperimentHistogram) this).stats[i_113_]) {
		double d_126_ = ComputeKurt(((SamplingExperimentHistogram) this).fdata,
					    ((SamplingExperimentHistogram) this).xdata,
					    ((SamplingExperimentHistogram) this).numInt, d);
		printStat(d_126_, false, f, f_111_, i_112_, i_113_, i_115_,
			  i_116_, title, graphics, fontmetrics);
		i_112_ += i;
	    }
	    graphics.setColor(Color.darkGray);
	}
    }
    
    public void printStat(double d, boolean bool, float f, float f_127_, int i,
			  int i_128_, int i_129_, int i_130_, String string,
			  Graphics graphics, FontMetrics fontmetrics) {
	graphics.setColor(((SamplingExperimentHistogram) this).colors[i_128_]);
	int i_131_
	    //= 120 + (int) Math.round((d - (double) ((SamplingExperimentHistogram) this).xdata[0]
	    = 10 + (int) Math.round((d - (double) ((SamplingExperimentHistogram) this).xdata[0]
				      + (double) f / 2.0) * (double) f_127_);
	String string_132_ = format(d, 2);
	String string_133_ = string + string_132_;
	if (bool)
	    graphics.drawLine(i_131_, i_129_, i_131_, i_130_);
	int i_134_ = 80 - fontmetrics.stringWidth(string_132_);
	graphics.drawString(string, 4, i);
	graphics.drawString(string_132_, i_134_, i);
    }
    
    public void setClickable(boolean bool) {
	((SamplingExperimentHistogram) this).clickable = bool;
    }
    
    final void setColor(Color color) {
	Graphics graphics = this.getGraphics();
	((SamplingExperimentHistogram) this).color = color;
    }
    
    public void setData(float[] fs) {
	((SamplingExperimentHistogram) this).numInt = fs.length;
	for (int i = 0; i < ((SamplingExperimentHistogram) this).numInt; i++)
	    ((SamplingExperimentHistogram) this).xdata[i] = fs[i];
    }
    
    public void setData(int[] is, int i, boolean bool, double d,
			double d_135_) {
	((SamplingExperimentHistogram) this).numInt = i;
	((SamplingExperimentHistogram) this).fmax = (double) ((SamplingExperimentHistogram) this).yintervals;
	int i_136_ = 0;
	if (bool) {
	    ((SamplingExperimentHistogram) this).sum += d;
	    ((SamplingExperimentHistogram) this).ssq += d_135_;
	} else {
	    ((SamplingExperimentHistogram) this).sum = d;
	    ((SamplingExperimentHistogram) this).ssq = d_135_;
	}
	for (int i_137_ = 0; i_137_ < i; i_137_++) {
	    if (bool)
		((SamplingExperimentHistogram) this).fdata[i_137_] += is[i_137_];
	    else
		((SamplingExperimentHistogram) this).fdata[i_137_] = is[i_137_];
	    if (((SamplingExperimentHistogram) this).fdata[i_137_] > i_136_)
		i_136_ = ((SamplingExperimentHistogram) this).fdata[i_137_];
	}
	if ((double) i_136_ > ((SamplingExperimentHistogram) this).fmax)
	    ((SamplingExperimentHistogram) this).fmax
		= ((Math.floor((double) (i_136_
					 / ((SamplingExperimentHistogram) this).yintervals))
		    * (double) ((SamplingExperimentHistogram) this).yintervals)
		   + (double) ((SamplingExperimentHistogram) this).yintervals);
	setSlopes();
	for (int i_138_ = 0; i_138_ < ((SamplingExperimentHistogram) this).numInt; i_138_++)
	    ((SamplingExperimentHistogram) this).f[i_138_]
		= (int) Math.round((((SamplingExperimentHistogram) this).sv2p
				    * (double) (((SamplingExperimentHistogram) this).fdata
						[i_138_]))
				   + ((SamplingExperimentHistogram) this).iv2p);

	// Commented by Ivo 01//22/07:
	// drawAxes(this.getGraphics());
	
	this.repaint();
    }
    
    public void setEndsOnly(boolean bool) {
	((SamplingExperimentHistogram) this).endsOnly = bool;
    }
    
    public void setFmax(double d) {
	((SamplingExperimentHistogram) this).fmax = d;
	setSlopes();
	this.repaint();
    }
    
    public void setGap(int i) {
	((SamplingExperimentHistogram) this).gap = i;
	this.repaint();
    }
    
    private final void setSlopes() {
	((SamplingExperimentHistogram) this).sp2v
	    = ((((SamplingExperimentHistogram) this).fmax - ((SamplingExperimentHistogram) this).fmin)
	       / (double) -((SamplingExperimentHistogram) this).rHeight);
	((SamplingExperimentHistogram) this).ip2v
	    = ((((SamplingExperimentHistogram) this).fmax + ((SamplingExperimentHistogram) this).fmin) / 2.0
	       - (((SamplingExperimentHistogram) this).sp2v * (double) ((SamplingExperimentHistogram) this).rHeight
		  / 2.0));
	((SamplingExperimentHistogram) this).sv2p = 1.0 / ((SamplingExperimentHistogram) this).sp2v;
	((SamplingExperimentHistogram) this).iv2p
	    = ((double) (((SamplingExperimentHistogram) this).rHeight / 2)
	       - ((SamplingExperimentHistogram) this).sv2p * (((SamplingExperimentHistogram) this).fmax
					    + ((SamplingExperimentHistogram) this).fmin) / 2.0);
	((SamplingExperimentHistogram) this).yinc = (int) Math.round(((SamplingExperimentHistogram) this).sv2p);
	((SamplingExperimentHistogram) this).yinc = -((SamplingExperimentHistogram) this).yinc;
	((SamplingExperimentHistogram) this).inter
	    = (int) Math.round(((SamplingExperimentHistogram) this).iv2p) + 1;
	((SamplingExperimentHistogram) this).Nobs = 0;
	for (int i = 0; i < ((SamplingExperimentHistogram) this).numInt; i++) {
	    ((SamplingExperimentHistogram) this).Nobs += ((SamplingExperimentHistogram) this).fdata[i];
	    ((SamplingExperimentHistogram) this).f[i]
		= (int) ((((SamplingExperimentHistogram) this).sv2p
			  * (double) ((SamplingExperimentHistogram) this).fdata[i])
			 + (double) ((SamplingExperimentHistogram) this).inter);
	}
    }
    
    public void setStats(boolean bool) {
	((SamplingExperimentHistogram) this).showStat = bool;
	this.repaint();
    }
    
    public void setStats(boolean[] bools) {
	for (int i = 0; i < 10; i++)
	    ((SamplingExperimentHistogram) this).stats[i] = bools[i];
	this.repaint();
    }
    
    public void setTitle(String string) {
	((SamplingExperimentHistogram) this).title = string;
	this.repaint();
    }
    
    public void setXaxisDec(int i) {
	((SamplingExperimentHistogram) this).XaxisDec = i;
    }
    
    public void update(Graphics graphics) {
	Dimension dimension = this.getSize();
	Image image = this.createImage(dimension.width, dimension.height);
	Graphics graphics_139_ = image.getGraphics();
	graphics_139_.setColor(this.getBackground());
	graphics_139_.fillRect(0, 0, dimension.width, dimension.height);
	graphics_139_.setColor(graphics.getColor());
	// paintComponent(graphics_139_);
	this.repaint();
	graphics.drawImage(image, 0, 0, this);
    }
    
    public double zprob(double d) {
	if (d < -7.0)
	    return 0.0;
	if (d > 7.0)
	    return 1.0;
	boolean bool;
	if (d < 0.0)
	    bool = true;
	else
	    bool = false;
	d = Math.abs(d);
	double d_140_ = 0.0;
	double d_141_ = Math.sqrt(2.0) / 3.0 * d;
	double d_142_ = 0.5;
	for (int i = 0; i < 12; i++) {
	    double d_143_ = (Math.exp(-d_142_ * d_142_ / 9.0)
			     * Math.sin(d_142_ * d_141_) / d_142_);
	    d_140_ += d_143_;
	    d_142_++;
	}
	double d_144_ = 0.5 - d_140_ / 3.141592653589793;
	if (bool)
	    d_144_ = 1.0 - d_144_;
	return d_144_;
    }
}
