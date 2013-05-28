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
 It's Online, Therefore, It Exists!
 ***************************************************/


package edu.uah.math.experiments;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.Coin;
import edu.uah.math.devices.CoinBox;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;

/**
* This experiment consists of tossing n coins, each with probability of heads p.
* The Proportion of Heads (P) and the differences of Heads and Tails (D) are recorded on each update.
* The discrete probability density function is shown in blue in the distribution
* graph and are recorded in the distribution table. On each update, the empirical
* probability density function and moments of the selected variable are shown in red
* in the distribution graph and are recorded in the distribution table. The parameters
* n and p can be varied with scroll bars.
* @author Ivo Dinov
* @version February 16, 2007
*/
public class LLN_SimpleExperiment extends edu.uah.math.experiments.Experiment implements Serializable{
	//Variables
	private int n = 50, rvIndex = 0, x, trial;
	private double p = 0.5, m;
	private double Pe, De;  // estimates for proportion and difference

	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{
			"Run", "Number of Heads", "Proportion of Heads", "Norm. Difference (H-T)"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox rvChoice = new JComboBox();
	private CoinBox coinBox = new CoinBox(n, p, 16);
	private Parameter nScroll = new Parameter(1, 1000, 1, n, "Number of coins", "n");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Probability of heads", "p");
	
	private BinomialDistribution sumDist = new BinomialDistribution(n, p);
	private LocationScaleDistribution averageDist = new LocationScaleDistribution(sumDist, 0, 1.0 / n);
	private RandomVariable sumRV = new RandomVariable(sumDist, "X");
	
	private JButton goToSOCR_LLN_Activity = new JButton("Go To LLN Activity");

	private RandomVariable averageRV = new RandomVariable(averageDist, "Proportion-Of-Heads");

	private edu.uah.math.devices.CoinTossLLNGraph coinTossLLNGraph = new edu.uah.math.devices.CoinTossLLNGraph(n);
	private RandomVariableGraph rvGraph = new RandomVariableGraph(averageRV);

    private RandomVariableTable rvTable = new RandomVariableTable(averageRV);
	private Timer timer = new Timer(20, this);


	/**
	* This method initializes the experiment, including the coin box,
	* the toolbar with the random variable choice, and the random variable table and
	* graph.
	*/
	public void init(){
		super.init();
		setName("Coin Toss Law Of Large Numbers Experiment");
		//Parameters
		nScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);

		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(nScroll);
		toolBar.add(pScroll);
		goToSOCR_LLN_Activity.setToolTipText("Click This Button to Get a Browser Open with a matching SOCR LLN Activity");
		goToSOCR_LLN_Activity.addActionListener(this);
		toolBar.add(goToSOCR_LLN_Activity);
		addToolBar(toolBar);
		setStopChoiceTipText("Sample-Size (n): Stop Frequency");
		
		//		coinTossLLNGraph
		coinTossLLNGraph.setMinimumSize(new Dimension(200, 100));
		coinTossLLNGraph.setProbability((float)p);
		addComponent(coinTossLLNGraph, 0, 0, 1, 1, 20, 10);
		
		//	Distribution variable graph
		rvGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(rvGraph, 1, 0, 2, 1, 1, 1);

				
		//Record table
		recordTable.setDescription("COLUMNS: Index; Number of Heads; " +
				"Sample Proportion of Heads; Normalized Difference |Heads - Tails|");
		addComponent(recordTable, 0, 1, 1, 1);
		
		//Random variable table
		addComponent(rvTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return "\nCopyright (C) 2007, Juana Sanchez, Nicolas Christou, Robert Gould and Ivo Dinov.\n\n"
		+ "This SOCR Applet demonstrates a Coin Toss experiment that shows interactively\n"
		+ "the Law of Large Numbers and several of it's misconseptions, in the case of studying the\n"
		+ "population proportion of Heads in a coin-toss experiment (using sample proportions).\n\n"
		+ "The complete description of the LLN may be found online at:\n"
		+ "http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_LLN.\n\n"
		+ "The user may select n, the sample-size, and p, the probability of Heads."
		+ "The Graph panel (top-left) \nshows the sample-proportion of Heads (RED)\n and the"
		+ "normalized differences (Heads - Tails). The graph on the right\n"
		+ "illustrates the theoretical and the sample distributions of the proportion of heads.";
	}

	/**
	* This method defines the experiment. The coins are tossed and the number of heads
	* and proportion of heads are computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		coinBox.toss();
		x = coinBox.getHeadCount();
		m = (double)x / n;
		sumRV.setValue(x);
		averageRV.setValue(m);
		
		//coinTossLLNGraph.setAllValues(coinBox.getValues());
		//coinTossLLNGraph.setIndex(trial);
		//coinTossLLNGraph.computeStats(trial);
		
		coinTossLLNGraph.setAllValues(coinBox.getValues());
		coinTossLLNGraph.setIndex(n);
		coinTossLLNGraph.computeStats(n);
		coinTossLLNGraph.repaint();
		
		Pe = m;
		De = p + (float)((1-p)*x-p*(coinBox.getTailCount()));
	}

	/**
	* This method starts the step process.
	*/
	public void step(){
		stop();
		coinBox.setTossed(false);
		coinBox.toss();
		trial = 0;
		timer.start();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the record table, coin box,
	* the random variables, and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		coinBox.setTossed(false);
		sumRV.reset();
		averageRV.reset();
		coinTossLLNGraph.reset();
		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method updates the experiment, including the coin box, the record table
	* and the random variable and graph.
	*/
	public void update(){
		super.update();
		coinBox.setTossed(true);
		coinTossLLNGraph.repaint();
		rvGraph.repaint();
		rvTable.repaint();
		recordTable.addRecord(new double[] {getTime(), coinTossLLNGraph.getHeads(), Pe, De});
	}

	public void graphUpdate(){
		super.update();
		coinTossLLNGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.setShowModelDistribution(showModelDistribution);
		coinTossLLNGraph.repaint();
		rvGraph.repaint();
		rvTable.setShowModelDistribution(showModelDistribution);
		rvTable.repaint();
	}
	/**
	* This method handles the events associated with changes in the slider. These sliders
	* are used to adjust the parameters of the experiment: the number of coins and
	* the probability of heads.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			coinBox.setCoinCount(n);
			coinTossLLNGraph.setTotal(n);
			setDistribution();
			/* if (n>15) rvGraph.setDomain(new edu.uah.math.distributions.Domain(
							n*p-3*Math.sqrt(n*p*(1-p)), n*p+3*Math.sqrt(n*p*(1-p)), 
							1, edu.uah.math.distributions.Domain.DISCRETE));
			*/ 
			reset();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			coinTossLLNGraph.setProbability((float)p);
			setDistribution();
			reset();
		}
	}

	/**
	* This method handles the timer events associated with the step process. The coins are shown
	* one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		if (source == timer){
			Coin coin;
			if (trial < n){
				coin = coinBox.getCoin(trial);
				coin.setTossed(true);
				//playnote(coin.getValue());
				//if (coin.getValue()==0) coinTossLLNGraph.addATail(trial);
				//else coinTossLLNGraph.addAHead(trial);
				
				coinTossLLNGraph.setAllValues(coinBox.getValues());
				coinTossLLNGraph.setIndex(trial);
				coinTossLLNGraph.computeStats(trial);
				coinTossLLNGraph.repaint();
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				x = coinBox.getHeadCount();
				m = (double)x / n;
				sumRV.setValue(x);
				averageRV.setValue(m);
				
				Pe = (double)x / trial;
				De = p + (float)((1-p)*x-p*coinBox.getTailCount());
				System.out.println("!!!!!Index="+ trial+
						"Heads="+x+"\tPe="+Pe+"\tDe="+De+"!!!!!!");
				recordTable.addRecord(new double[] {getTime(), x, Pe, De});
				
				//coinTossLLNGraph.setAllValues(coinBox.getValues());
				//coinTossLLNGraph.setIndex(trial);
				//coinTossLLNGraph.computeStats(trial);
				//coinTossLLNGraph.repaint();
				
				rvGraph.repaint();
				rvTable.repaint();
			}
		}
		else if (source==goToSOCR_LLN_Activity) {
    		try { //System.out.println("TEST TEST");
    			  applet.getAppletContext().showDocument(
                        new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_LawOfLargeNumbers2"), 
                			"SOCR: LLN Activity (Part II)");
               } catch (MalformedURLException Exc) {
                        JOptionPane.showMessageDialog(this, Exc.getMessage());
                        Exc.printStackTrace();
               }
    	}
		else super.actionPerformed(e);
	}


	/**
	* This method sets the appropriate distribution of the random variable. This
	* method is called when the parameters are changed or when the type of random
	* variable is changed. The {@link #reset() reset} method is called.
	*/
	public void setDistribution(){
		coinBox.setProbability(p);
		coinTossLLNGraph.setProbability((float)p);
		coinTossLLNGraph.setParameters(n);
		sumDist.setParameters(n, p);
		averageDist.setParameters(sumDist, 0, 1.0 / n);
		if (n>20) {
			double 	min=p-4*Math.sqrt(p*(1-p)/n), 
					max=p+4*Math.sqrt(p*(1-p)/n), 
					step= 1.0/n;
			int type = edu.uah.math.distributions.Domain.DISCRETE;
			averageDist.setDomain(min, max, step, type);
		}
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

