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
/* ConfidenceInterval.java */

/*
 * ConfidenceInterval.java illustrates construction & utiliation of CI's @author
 * Ivo Dinov
 * 
 * @version 1.0 Feb. 19 2004
 */

package edu.ucla.stat.SOCR.experiments;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.experiments.util.*;

public class ConfidenceIntervalExperimentSimple extends Experiment {
    ConfidenceCanvasSimple cc;
    ConfidenceControlPanelSimple ccp;
    JPanel upperPanel;
    FlowLayout fl;
    int n = 20; //number of datapoints, sample-size
    int nTrials = 20; // number of experiments
    double[][] normalData;
    double[] xBar;
    double[] s; // Sample SD of the trial/experiment
    Random random;
    int cvIndex = 1; // Critical Value index and values (alpha-values)
    double cvValue;
    Container c;


    /**
     * initialization method
     */

    public ConfidenceIntervalExperimentSimple() {
        int hypothesis = 0; // initially, Ha: mu>mu0
        getRecordTable().setText("Experiment\tMissed Intervals:\n");
        //Container c = getContentPane();

        cc = new ConfidenceCanvasSimple(n, nTrials);

        addGraph(cc);
        cc.setSize(150, 125);
        ccp = new ConfidenceControlPanelSimple(this);
        ccp.setNumberOfTrials(nTrials);
        ccp.setSampleSize(n);
        // ccp.sampleSizeText.setText(Integer.toString(cc.getSize().width ));
        //ccp.numberOfTrialsText.setText(Integer.toString(cc.getSize().height));
        fl = new FlowLayout();
        upperPanel = new JPanel();
        upperPanel.setLayout(fl);
        upperPanel.add(ccp);
        addToolbar(ccp.toolbar);
        /*
         * c.add(upperPanel, BorderLayout.NORTH); c.add(new
         * JTextArea("http://socr.stat.ucla.edu"+ " Confidence Interval
         * Construction and Utilization Applet."+ " Simulates data from N(mu,
         * sigma^2)"), BorderLayout.SOUTH );
         */

        cc.setBackground(Color.white);
        random = new Random();

    }

    /**
     * play generates sample dat
     * 
     * @param nTrials is the number of tests to do
     */
    public void play(int nTrials) {
    	System.out.println("Simple play get called");
        cvIndex = ccp.getAlphaIndex();
        n = ccp.getSampleSize();
        nTrials = ccp.getNumberOfTrials();

        //System.out.println("sample-size=" + n + " NumberExperiments=" +
        // nTrials);

        cc.setSampleSize_NumberExperiments(n, nTrials);

        int sumx = 0;
        normalData = new double[nTrials][n];
        xBar = new double[nTrials];
        s = new double[nTrials];
        generateNormalData(nTrials, n, normalData, xBar, s);
        cc.update(cvIndex, normalData, xBar, s);
    }

    public void reset() {
        super.reset();
        cc.clear(ccp.getSampleSize(), ccp.getNumberOfTrials());
        getRecordTable().setText("Experiment\tMissed Intervals:\n");
    }

    public void doExperiment() {
    	super.doExperiment();
        cvIndex = ccp.getAlphaIndex();
        n = ccp.getSampleSize();
        nTrials = ccp.getNumberOfTrials();
        //nTrials = 1;
        //System.out.println("sample-size=" + n + " NumberExperiments=" +
        // nTrials);

        cc.setSampleSize_NumberExperiments(n, nTrials);

        int sumx = 0;
        normalData = new double[nTrials][n];
        xBar = new double[nTrials];
        s = new double[nTrials];
        generateNormalData(nTrials, n, normalData, xBar, s);
        cc.update(cvIndex, normalData, xBar, s);    
       // getRecordTable().append("\t" + Integer.toString(cc.getMissedCount()));
    }
    
    public void update() {
    	super.update();
    	System.out.println("Simple update get called");
    	cc.repaint();
    	System.out.println("Simple update get called +"+ cc.getMissedCount());
        getRecordTable().append("\t" + Integer.toString(cc.getMissedCount()));    
    }

       /**
	* This method returns basic information about the applet, including copyright
	* information, descriptive informaion, and instructions.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "This experiments demonstrates Confidence intervals for sample Means. \n"
			+ "\n1. Alpha level, Number of experiments and sample-size [number of random simulations from N(0,1)].\n"
			+"field of view or by clicking the <RandomPnts> button.\n"
			+"2. Click <STEP> or <RUN> to initiate the process of random sampling and construction\n"
			+" of the corresponding confidence intervals for the mean(s)\n"
			+"A confidence interval which misses the origin (0) is indicated by a large green dot!\n"
			+"This experiment demostrates that our non-constructive definitiion of confidence intervals\n"
			+" and our protocol for obtaining the confidence interval for the mean coinside\n"
			+ "Therefore we'd expect to see only about 1 green-dot (incorrectly constructed CI)\n"
			+"in 1 out of 20 experiments, where alpha=0.05, no matter what is the sample-size\n"
			+"Of course, CI will be wider for smaller sample-sizes.\n\n"
			+"This simulation randomly samples from Normal(0, 1) distribution and constructs the \n"
			+"100(1-Alpha)% Confidence interval for the population mean using\n"
			+"\t\t y_bar +/- t(n-1, Alpha)*SE(y_bar)."
			+"\n\n http://wiki.stat.ucla.edu/socr/index.php/EBook";
	}


    private void generateNormalData(int nTrials, int n, double[][] normalData,
            double[] xBar, double[] s) {
        double sumx = 0;
        double sumxx = 0;
        for (int trial = 0; trial < nTrials; trial++) {
            for (int i = 0; i < n; i++) {
                normalData[trial][i] = random.nextGaussian();
                sumx += normalData[trial][i];
                sumxx += normalData[trial][i] * normalData[trial][i];
            }

            xBar[trial] = sumx / n;
            s[trial] = Math.sqrt((sumxx - sumx * sumx / n) / (n - 1));
            sumx = 0;
            sumxx = 0;
        }
    }

}

