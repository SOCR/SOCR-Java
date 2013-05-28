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
/* SamplingExperimentAnimateSample
 *
 * This class is part of the SOCR Experiment: Sampling Distribution (CLT) Experiment,
 * which demonstrates the properties of the sampling distributions of various sample statistics.
 */

package edu.ucla.stat.SOCR.util;

import java.awt.Color;
import java.awt.Graphics;

class SamplingExperimentAnimateSample implements Runnable
{
    SamplingExperimentHistogram h;
    Thread moveIt;
    SamplingExperimentAnimateSample nextThread;
    SamplingExperimentAnimateSample nextThread2;
    int[] R = new int[4];
    int[] sp = new int[4];
    int[] bins;
    int[] bottoms;
    int[] fd2;
    int increment;
    int xoff;
    int g2;
    int N;
    int k;
    Graphics g;
    Color c;
    boolean clear;
    boolean[] stats;
    double sum;
    double ssq;
    
    public SamplingExperimentAnimateSample(SamplingExperimentHistogram SamplingExperimentHistogram, boolean[] bools, int i, int[] is,
		    int[] is_0_, int[] is_1_, int i_2_, boolean bool,
		    Color color, SamplingExperimentAnimateSample SamplingExperimentAnimateSample_3_) {
	((SamplingExperimentAnimateSample) this).stats = new boolean[bools.length];
	((SamplingExperimentAnimateSample) this).nextThread = SamplingExperimentAnimateSample_3_;
	((SamplingExperimentAnimateSample) this).sum = 0.0;
	((SamplingExperimentAnimateSample) this).ssq = 0.0;
	((SamplingExperimentAnimateSample) this).N = i_2_;
	for (int i_4_ = 0; i_4_ < bools.length; i_4_++)
	    ((SamplingExperimentAnimateSample) this).stats[i_4_] = bools[i_4_];
	((SamplingExperimentAnimateSample) this).bottoms = new int[((SamplingExperimentAnimateSample) this).N];
	((SamplingExperimentAnimateSample) this).bins = new int[((SamplingExperimentAnimateSample) this).N];
	((SamplingExperimentAnimateSample) this).clear = bool;
	((SamplingExperimentAnimateSample) this).h = SamplingExperimentHistogram;
	((SamplingExperimentAnimateSample) this).c = color;
	for (int i_5_ = 0; i_5_ < ((SamplingExperimentAnimateSample) this).N; i_5_++) {
	    ((SamplingExperimentAnimateSample) this).bottoms[i_5_] = is[i_5_];
	    ((SamplingExperimentAnimateSample) this).bins[i_5_] = is_0_[i_5_];
	}
	((SamplingExperimentAnimateSample) this).increment = i;
	((SamplingExperimentAnimateSample) this).g2 = ((SamplingExperimentHistogram) ((SamplingExperimentAnimateSample) this).h).gap / 2;
	((SamplingExperimentAnimateSample) this).xoff = 10;
	((SamplingExperimentAnimateSample) this).R[2] = ((SamplingExperimentHistogram) ((SamplingExperimentAnimateSample) this).h).width;
	for (int i_6_ = 0; i_6_ < 4; i_6_++)
	    ((SamplingExperimentAnimateSample) this).sp[i_6_] = is_1_[i_6_];
    }
    
    public void run() {
	((SamplingExperimentAnimateSample) this).h.setStats(false);
	if (((SamplingExperimentAnimateSample) this).clear) {
	    ((SamplingExperimentAnimateSample) this).k = ((SamplingExperimentHistogram) ((SamplingExperimentAnimateSample) this).h).numInt;
	    ((SamplingExperimentAnimateSample) this).fd2 = new int[((SamplingExperimentAnimateSample) this).k];
	    for (int i = 0; i < ((SamplingExperimentAnimateSample) this).k; i++)
		((SamplingExperimentAnimateSample) this).fd2[i] = 0;
	    ((SamplingExperimentAnimateSample) this).h.setData(((SamplingExperimentAnimateSample) this).fd2,
					((SamplingExperimentAnimateSample) this).k, false,
					((SamplingExperimentAnimateSample) this).sum,
					((SamplingExperimentAnimateSample) this).ssq);
	    ((SamplingExperimentAnimateSample) this).h.repaint();
	}
	((SamplingExperimentAnimateSample) this).g = ((SamplingExperimentAnimateSample) this).h.getGraphics();
	if (((SamplingExperimentAnimateSample) this).c != null) {
	    ((SamplingExperimentAnimateSample) this).g.setColor(((SamplingExperimentAnimateSample) this).c);
	    ((SamplingExperimentAnimateSample) this).h.setColor(((SamplingExperimentAnimateSample) this).c);
	}
	for (int i = 0; i < ((SamplingExperimentAnimateSample) this).N; i++) {
	    ((SamplingExperimentAnimateSample) this).R[0]
		= (((SamplingExperimentAnimateSample) this).xoff
		   + ((SamplingExperimentAnimateSample) this).R[2] * ((SamplingExperimentAnimateSample) this).bins[i]
		   + ((SamplingExperimentAnimateSample) this).g2);
	    ((SamplingExperimentAnimateSample) this).R[1] = 16;
	    ((SamplingExperimentAnimateSample) this).R[3] = ((SamplingExperimentHistogram) ((SamplingExperimentAnimateSample) this).h).yinc;
	    try {
		Thread.sleep((long) ((SamplingExperimentAnimateSample) this).sp[0]);
	    } catch (Exception exception) {
		/* empty */
	    }
	    ((SamplingExperimentAnimateSample) this).g.fillRect(((SamplingExperimentAnimateSample) this).R[0],
					 ((SamplingExperimentAnimateSample) this).R[1],
					 ((SamplingExperimentAnimateSample) this).R[2],
					 ((SamplingExperimentAnimateSample) this).R[3]);
	    try {
		Thread.sleep((long) ((SamplingExperimentAnimateSample) this).sp[1]);
	    } catch (Exception exception) {
		/* empty */
	    }
	    while (((SamplingExperimentAnimateSample) this).R[1] < ((SamplingExperimentAnimateSample) this).bottoms[i]) {
		((SamplingExperimentAnimateSample) this).g.clearRect(((SamplingExperimentAnimateSample) this).R[0],
					      ((SamplingExperimentAnimateSample) this).R[1],
					      ((SamplingExperimentAnimateSample) this).R[2],
					      ((SamplingExperimentAnimateSample) this).increment);
		((SamplingExperimentAnimateSample) this).R[1] += ((SamplingExperimentAnimateSample) this).increment;
		((SamplingExperimentAnimateSample) this).g.fillRect(((SamplingExperimentAnimateSample) this).R[0],
					     ((SamplingExperimentAnimateSample) this).R[1],
					     ((SamplingExperimentAnimateSample) this).R[2],
					     ((SamplingExperimentAnimateSample) this).R[3]);
		try {
		    Thread.sleep((long) ((SamplingExperimentAnimateSample) this).sp[2]);
		} catch (Exception exception) {
		    /* empty */
		}
	    }
	    ((SamplingExperimentAnimateSample) this).h.addX(((SamplingExperimentAnimateSample) this).bins[i]);
	    try {
		Thread.sleep(1L);
	    } catch (Exception exception) {
		/* empty */
	    }
	}
	((SamplingExperimentAnimateSample) this).h.setStats(((SamplingExperimentAnimateSample) this).stats);
	((SamplingExperimentAnimateSample) this).h.setStats(true);
	try {
	    Thread.sleep(2L);
	} catch (Exception exception) {
	    /* empty */
	}
	if (((SamplingExperimentAnimateSample) this).nextThread != null)
	    ((SamplingExperimentAnimateSample) this).nextThread.start();
    }
    
    public void start() {
	((SamplingExperimentAnimateSample) this).moveIt = new Thread(this);
	((SamplingExperimentAnimateSample) this).moveIt.start();
    }
    
    public void stop() {
	((SamplingExperimentAnimateSample) this).moveIt = null;
    }
}
