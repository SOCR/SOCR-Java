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
/* SamplingExperimentGenerateSample
 *
 * This class is part of the SOCR Experiment: Sampling Distribution (CLT) Experiment,
 * which demonstrates the properties of the sampling distributions of various sample statistics.
 */

package edu.ucla.stat.SOCR.util;

class SamplingExperimentGenerateSample implements Runnable
{
    SamplingExperimentMainFrame s;
    int nsamp;
    int statIndex1;
    int statIndex2;
    Thread SampleT;
    float[] D;
    float[] D2;
    float[] statR;
    int[] fd2;
    double s1;
    double s2;
    double sq1;
    double sq2;
    
    public SamplingExperimentGenerateSample(SamplingExperimentMainFrame sampling, int i, int i_0_, int i_1_) {
	((SamplingExperimentGenerateSample) this).s = sampling;
	((SamplingExperimentGenerateSample) this).nsamp = i;
	((SamplingExperimentGenerateSample) this).statR = new float[2];
	((SamplingExperimentGenerateSample) this).D
	    = new float[((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).N];
	((SamplingExperimentGenerateSample) this).D2
	    = new float[((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).N2];
	((SamplingExperimentGenerateSample) this).s1 = 0.0;
	((SamplingExperimentGenerateSample) this).sq1 = 0.0;
	((SamplingExperimentGenerateSample) this).s2 = 0.0;
	((SamplingExperimentGenerateSample) this).sq2 = 0.0;
	((SamplingExperimentGenerateSample) this).statIndex1 = i_0_;
	((SamplingExperimentGenerateSample) this).statIndex2 = i_1_;
	((SamplingExperimentGenerateSample) this).fd2
	    = new int [((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k];
    }
    
    public void run() {
	((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hh.clear();
	((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).ns = 0;
	int i = ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).N2;
	((SamplingExperimentGenerateSample) this).statR[0] = 0.0F;
	((SamplingExperimentGenerateSample) this).statR[1] = 0.0F;

	// Re-Set the Progress report Bar
	((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).progressBar.setMaximum(((SamplingExperimentGenerateSample) this).nsamp);

	for (int i_2_ = 0; i_2_ < ((SamplingExperimentGenerateSample) this).nsamp; i_2_++) {
	    ((SamplingExperimentGenerateSample) this).D = (((SamplingExperimentGenerateSample) this).s.sample
		(((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).fdata,
		((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k,
		((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).N));
	    if (((SamplingExperimentGenerateSample) this).s.statName2 != "None") {
		if (((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).N == i) {
		    for (int i_3_ = 0; i_3_ < i; i_3_++)
			((SamplingExperimentGenerateSample) this).D2[i_3_]
			    = ((SamplingExperimentGenerateSample) this).D[i_3_];
		} else
		    ((SamplingExperimentGenerateSample) this).D2
			= (((SamplingExperimentGenerateSample) this).s.sample
			   (((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).fdata,
			    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k,
			    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).N2));
	    }
	    ((SamplingExperimentGenerateSample) this).statR
		= ((SamplingExperimentGenerateSample) this).s.getStats(((SamplingExperimentGenerateSample) this).D,
						 ((SamplingExperimentGenerateSample) this).D2);
	    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).statD2
		[((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).ns]
		= ((SamplingExperimentGenerateSample) this).statR[1];
	    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).statD
		[((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).ns]
		= ((SamplingExperimentGenerateSample) this).statR[0];
	    ((SamplingExperimentGenerateSample) this).s1 += (double) ((SamplingExperimentGenerateSample) this).statR[0];
	    ((SamplingExperimentGenerateSample) this).sq1
		+= (double) (((SamplingExperimentGenerateSample) this).statR[0]
			     * ((SamplingExperimentGenerateSample) this).statR[0]);
	    ((SamplingExperimentGenerateSample) this).s2 += (double) ((SamplingExperimentGenerateSample) this).statR[1];
	    ((SamplingExperimentGenerateSample) this).sq2
		+= (double) (((SamplingExperimentGenerateSample) this).statR[1]
			     * ((SamplingExperimentGenerateSample) this).statR[1]);
	    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).ns++;
	    if (i_2_ % 200 == 0 && ((SamplingExperimentGenerateSample) this).nsamp > 9) {
		int i_4_ = ((SamplingExperimentGenerateSample) this).nsamp - i_2_;

		// Progress Report!!
		//((SamplingExperimentGenerateSample) this).f.setLabel("Samples left : " + i_4_);
		((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).progressBar.setValue(i_2_);

		try {
		    Thread.sleep(8L);
		} catch (Exception exception) {
		    /* empty */
		}
	    }
	}
	int[] is = new int[((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k];
	((SamplingExperimentGenerateSample) this).fd2
	    = (((SamplingExperimentGenerateSample) this).s.tally
	       (((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).statD,
		((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhh).xdata,
		((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k,
		((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).ns, is, false));
	((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhh.setColor
			(((SamplingExperimentMainFrame)
				((SamplingExperimentGenerateSample) this).s).colors
					[(((SamplingExperimentGenerateSample) this).statIndex1)]);
	((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhh.setData
	    (((SamplingExperimentGenerateSample) this).fd2, ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k,
	     true, ((SamplingExperimentGenerateSample) this).s1, ((SamplingExperimentGenerateSample) this).sq1);
	if (!((SamplingExperimentGenerateSample) this).s.statName2.equals("None")) {
	    ((SamplingExperimentGenerateSample) this).statIndex2
		= ((SamplingExperimentGenerateSample) this).s
		      .chooseStat(((SamplingExperimentGenerateSample) this).s.statName2);
	    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhhh.setColor
		(((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).colors
		 [((SamplingExperimentGenerateSample) this).statIndex2]);
	    ((SamplingExperimentGenerateSample) this).fd2
		= (((SamplingExperimentGenerateSample) this).s.tally
		   (((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).statD2,
		    (((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhhh)
		     .xdata),
		    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k,
		    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).ns, is, false));
	    ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhhh.setData
		(((SamplingExperimentGenerateSample) this).fd2, ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).k,
		 true, ((SamplingExperimentGenerateSample) this).s2, ((SamplingExperimentGenerateSample) this).sq2);
	    if (((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhhh).fmax
		> ((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhh).fmax)
		((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhh.setFmax
		    (((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhhh)
		     .fmax);
	    else if (((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhh).fmax
		     > (((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhhh)
			.fmax))
		((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).hhhh.setFmax
		    (((SamplingExperimentHistogram) ((SamplingExperimentMainFrame) 
			((SamplingExperimentGenerateSample) this).s).hhh).fmax);
	}
	if (((SamplingExperimentGenerateSample) this).nsamp > 9)
	    //((SamplingExperimentGenerateSample) this).f.dispose();

	// Re-Set the Progress report Bar
	((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) this).s).progressBar.setValue(((SamplingExperimentMainFrame) 
			((SamplingExperimentGenerateSample)this).s).progressBar.getMinimum());
    }
    
    public void start() {
	if (((SamplingExperimentGenerateSample) this).nsamp > 9)
	 {   ((SamplingExperimentMainFrame) ((SamplingExperimentGenerateSample) 
			this).s).progressBar.setMaximum(((SamplingExperimentGenerateSample) this).nsamp);
	  }
	((SamplingExperimentGenerateSample) this).SampleT = new Thread(this);
	((SamplingExperimentGenerateSample) this).SampleT.start();
    }
    
    public void stop() {
	((SamplingExperimentGenerateSample) this).SampleT = null;
    }
}
