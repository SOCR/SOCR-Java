/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.experiments;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.CoinScatter;
import edu.uah.math.devices.CoinFloor;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;


/**
* Buffon's coin experiment consists of tossing a coin on a floor covered in
* square tiles. The event of interest is that the coin crosses a crack between tiles.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BuffonCoinExperiment extends Experiment implements Serializable{
	//Variables
	private double x, y, r = 0.1;
	private int crossValue;
	//Objects
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter radiusScroll = new Parameter(0.0, 0.5, 0.05, 0.1, "Coin radius", "r");
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "Y", "I"});
	private CoinScatter coinScatter = new CoinScatter(r);
	private CoinFloor floor = new CoinFloor(r);
	private BernoulliDistribution crossDist = new BernoulliDistribution(floor.getProbability());
	private RandomVariable crossRV = new RandomVariable(crossDist, "I");
	private RandomVariableGraph crossGraph = new RandomVariableGraph(crossRV);
	private RandomVariableTable crossTable = new RandomVariableTable(crossRV);

	/**
	* This method initializes the experiment, including the graphs, labels, scrollbars,
	* and tables.
	*/
	public void init(){
		super.init();
		setName("Buffon's Coin Experiment");
		//Radius scroll
		radiusScroll.applyDecimalPattern("0.00");
		radiusScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(radiusScroll);
		addToolBar(toolBar);
		//Floor
		floor.setMinimumSize(new Dimension(100, 100));
		addComponent(floor, 0, 0, 1, 1);
		//Scatterplot
		coinScatter.setMinimumSize(new Dimension(100, 100));
		addComponent(coinScatter, 1, 0, 1, 1);
		//Cross graph
		crossGraph.setMomentType(0);
		crossGraph.setMargins(35, 20, 20, 20);
		crossGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(crossGraph, 2, 0, 1, 1);
		//Record table
		recordTable.setDescription("(X, Y): coin center, I: coin crosses crack");
		addComponent(recordTable, 0, 1, 2, 1);
		//Cross table
		crossTable.setStatisticsType(0);
		addComponent(crossTable, 2, 1, 1, 1);;
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
		return super.getAppletInfo() + "\n\n"
			+ "The experiment consists of tossing a coin on a floor with square tiles, and is shown\n"
			+ "graphically in the first picture box. The center (X, Y) of the coin relative to the \n"
			+ "center of the square is recorded on each update in the first table, and is shown \n"
			+ "graphically as a red dot in the scatterplot. Variable I indicates the event that the \n"
			+ "coin crosses a crack, and is recorded on each update in the first table. The density of\n"
			+ "I is shown in the last graph in blue and is given in the  second table. When the \n"
			+ "experiment runs, the empirical density of I is shown in the last graph in red and is \n"
			+ "given in the second table. The parameter of the process is the radius r of the coin, \n"
			+ "and can be varied with the scroll bar.";
	}

	/**
	* This method handles the slider events associated with changes in the radius of the coin.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == radiusScroll.getSlider()){
			r = radiusScroll.getValue();
			floor.setRadius(r);
			coinScatter.setRadius(r);
			crossDist.setProbability(floor.getProbability());
			reset();
		}
	}

	/**
	* This method defines the experiment: the coin center is randomly selected and then
	* the tile crack is tested.
	*/
	public void doExperiment(){
		super.doExperiment();
		floor.setValues();
		if (floor.crossEvent()) crossValue = 1; else crossValue = 0;
		crossRV.setValue(crossValue);
		x = floor.getXCenter(); y = floor.getYCenter();
		coinScatter.addPoint(x, y);
	}

	/**
	* This method updates the display, including the floor, random variable graph and
	* table, record table.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), x, y, crossValue});
		floor.setCoinDropped(true);
		coinScatter.repaint();
		crossGraph.repaint();
		crossTable.repaint();
	}
	
	public void graphUpdate(){
		super.update();
		//recordTable.addRecord(new double[]{getTime(), x, y, crossValue});
		//floor.setCoinDropped(true);
		coinScatter.setShowModelDistribution(showModelDistribution);
		crossGraph.setShowModelDistribution(showModelDistribution);
		coinScatter.repaint();
		crossGraph.repaint();
		crossTable.setShowModelDistribution(showModelDistribution);
		crossTable.repaint();
	}
	/**
	* This method peforms the experiment one time, and play sounds depending on the
	* outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote(crossValue);
	}

	/**
	* This method resets the experiment, including the record table, the floor,
	* the scatterplot, the cross event random variable, and the random variable
	* graph and table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		floor.setCoinDropped(false);
		coinScatter.reset();
		crossRV.reset();
		crossGraph.reset();
		crossTable.reset();
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

