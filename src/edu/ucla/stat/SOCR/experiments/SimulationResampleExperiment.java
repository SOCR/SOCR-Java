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
package edu.ucla.stat.SOCR.experiments;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Observable;
import java.util.StringTokenizer;
import java.util.Observer;

import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Card;
import edu.uah.math.devices.CardHand;
import edu.uah.math.devices.Parameter;
import edu.uah.math.experiments.Experiment;
import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.experiments.util.BootStrap;
import edu.ucla.stat.SOCR.experiments.util.SOCRJTablePanel;
import edu.ucla.stat.SOCR.experiments.util.SimulationResampleInferencePanel;
import edu.ucla.stat.SOCR.experiments.util.SimulationResampleType;
import edu.ucla.stat.SOCR.experiments.util.TransformAnim;
import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;

/**
* The card experiment consists of drawing a sample of cards, without replacement,
* form a standard deck.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class SimulationResampleExperiment extends Experiment implements Serializable, ActionListener, Observer{
	
	private JToolBar toolBar = new JToolBar();

	protected SOCRJTablePanel dataPanel;
	protected SimulationResampleInferencePanel infoPanel;
	protected JPanel reSamplePanel;
	protected JPanel bPanel;
	protected JPanel aniPanel;
	
	protected JScrollPane dataPanelContainer;
	protected JScrollPane infoPanelContainer;
	protected JScrollPane bPanelContainer;
	protected JScrollPane aniPanelContainer;
	
	public JTabbedPane tabbedPanelContainer;
	protected int tabbedPaneCount = 0;
	
	JPanel tabPanel;
	JPanel expPanel;
	JScrollPane mainPanel;
	JComboBox expChoices;  
	TransformAnim anim;
	
	int[][] bootStrapDataIndex;
	double[][] bootStrapData;
	
	double lowerBound=0, upperBound=1;
	int sampleSize, numberOfResample;
	 
	public static final String EXPERIMENT 	= "EXPERIMENT";
	protected final String INFERENCE 	= "INFERENCE";
	protected final String RESAMPLING 	= "RESAMPLING";
	protected final String ANIMATION 	= "ANIMATION";
	static edu.uah.math.experiments.CardExperiment card;
	static edu.uah.math.experiments.CoinSampleExperiment coin;
	
	SOCRJTablePanel origRow, resampleRow;
	
	int animRowIndex=0;
	 
	/**
	* This method initializes the experiment: including the scrollbar, cards, record table.
	*/
	public void init(){
		super.init();
		//clipboard = applet.cl;
		setName("SamplingExperiment");
		card = new edu.uah.math.experiments.CardExperiment();
		card.init();
		card.addListener(this);
		
		coin = new edu.uah.math.experiments.CoinSampleExperiment();	
		coin.init();
		coin.addListener(this);
		
		expPanel = new JPanel();
		reSamplePanel = new JPanel();		
		aniPanel = new JPanel();
		
		anim = new TransformAnim();
 		anim.init();
 	//	anim.addObserver(this);
		
		expChoices = new JComboBox(new String[]{"User Input","Card", "Coin"});
    	expChoices.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {		
				refreshTabPanel();
				tabbedPanelContainer.validate();
			 	if(mainPanel!=null){
		    	
		    		/*mainPanel.setLeftComponent(new JScrollPane(expPanel));
			    	tabPanel.removeAll();
			 		tabPanel.add(tabbedPanelContainer);			 
			 		mainPanel.setRightComponent(new JScrollPane(tabPanel));
			    	mainPanel.setDividerLocation(0.4);*/
			 
			    	mainPanel.validate();
			    	
			    	anim = new TransformAnim();
			 		anim.init();
			
		    	}
				
			}
    		
    	});
    	expChoices.setSelectedIndex(1);
    	
    	JPanel p = new JPanel();
    	p.add(new JLabel("Experiment Choices:"));
    	p.add(expChoices);
    	clearToolBars();
    	toolBar.removeAll();
    	toolBar.add(p);
    
		addToolBar(toolBar);
	
		tabPanel = new JPanel();
		refreshTabPanel();
		tabPanel.add(tabbedPanelContainer);
		mainPanel =  new JScrollPane(tabPanel);
		addComponent(mainPanel, 0, 0, 1, 1);
		
		//Final actions
		validate();
		reset();
	}

	public void refreshTabPanel(){
		tabbedPanelContainer= new JTabbedPane();
		tabbedPanelContainer.setPreferredSize(new Dimension(800, 600));
    	initTable();
     	initExperimentPanel();
     	
 		//setResamplingPanel();
 		initAniPanel();
 				
 		dataPanelContainer = new JScrollPane(new JSplitPane(JSplitPane.VERTICAL_SPLIT , new JScrollPane(expPanel), new JScrollPane(dataPanel)));
 		bPanelContainer = new JScrollPane(bPanel);
 		aniPanelContainer = new JScrollPane(aniPanel);
 		infoPanelContainer = new JScrollPane(infoPanel);
 		
 		dataPanelContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
 		dataPanelContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 		bPanelContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
 		bPanelContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 		aniPanelContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
 		aniPanelContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 		infoPanelContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
 		infoPanelContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 		
 		
 		addTabbedPane(EXPERIMENT, dataPanelContainer);
 		addTabbedPane(RESAMPLING, bPanelContainer );
 		addTabbedPane(ANIMATION, aniPanelContainer );
 		addTabbedPane(INFERENCE, infoPanelContainer );
 		
 		
 		tabbedPanelContainer.addChangeListener(new ChangeListener(){
 			public void stateChanged(ChangeEvent e) {
 			if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==INFERENCE) {
 				bPanel.removeAll();
 					//setGraphPanel();
 					//infoPanel.validate();
 				setInfo();
 			
 			}else 	if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==EXPERIMENT) {
 					//					
 					setTablePane();
 					
 			}else 	if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==RESAMPLING) {
 					setResamplingPanel(false);
 					bPanel.removeAll();
 					bPanel.add(reSamplePanel,BorderLayout.CENTER);
 					bPanel.validate();
 					
 				}	
 			else 	if(	tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())==ANIMATION) {
 					
 				animation(animRowIndex);
 			}
 			if (mainPanel !=null)
				mainPanel.validate();
 			}
 			
 		});
 		
 		tabbedPanelContainer.setSelectedComponent(dataPanelContainer);	
    }
	
	/*public void animation(int currentRowIndex){
		
			aniPanel.removeAll();
			
			anim.init(currentRowIndex);
			anim.addObserver(this);
			int[] fromCards = new int[getSampleSize()];
			int[] toCards = new int[getSampleSize()];
			double[] cards = origRow.getRowValue(0);
			
			for (int i=0;i<getSampleSize(); i++){
				//System.out.println(cards[i]);
				fromCards[i]=(int)cards[i];
			}
		
			aniPanel.add(anim);
	
			anim.setFromCards(fromCards, getSampleSize());
			
			anim.setPreferredSize(new Dimension(aniPanel.getWidth(), aniPanel.getHeight()));
//testing
        	cards = resampleRow.getRowValue(currentRowIndex);
        	for (int j=0;j<getSampleSize(); j++){
					//System.out.println(cards[i]);
					toCards[j]=(int)cards[j];
				}
        	anim.setToCards(toCards, bootStrapDataIndex[currentRowIndex], getSampleSize());
        	anim.start();
        
        	aniPanel.validate();
	}*/
	
	public void animation(int currentRowIndex){
		
		//aniPanel.removeAll();
		
		anim.init(currentRowIndex, expChoices.getSelectedItem().toString());
		//anim.addObserver(this);
		double[] fromCards = new double[getSampleSize()];
		double[] toCards = new double[getSampleSize()];
		double[] cards = origRow.getRowValue(0);
		
		//System.out.println("getSampleSize="+getSampleSize());
		
		for (int i=0;i<getSampleSize(); i++){
			//System.out.println(cards[i]);
			fromCards[i]=cards[i];
		}

		//if(currentRowIndex ==0) // add from card only once
		anim.setFromObs(fromCards, getSampleSize());
		
		anim.setPreferredSize(new Dimension(aniPanel.getWidth(), aniPanel.getHeight()));
//testing
		try{
	    	cards = resampleRow.getRowValue(currentRowIndex);
	    	for (int j=0;j<getSampleSize(); j++){
					//System.out.println(cards[i]);
					toCards[j]=cards[j];
				}
	    	anim.setToObs(toCards, bootStrapDataIndex[currentRowIndex], getSampleSize());
	    //	anim.print();
	    	
	    	//aniPanel.add(anim);
	    	if(currentRowIndex ==0) 
	    		anim.start();
	    
	    	aniPanel.validate();
		}catch(Exception e){
			e.printStackTrace();
			SOCROptionPane.showMessageDialog(this, "Empty resample data table, please go to resample tab and do resampling first.");
			
		}
}
	
    
	 public void addTabbedPane(String name, JComponent c){
	        tabbedPanelContainer.addTab(name,c);
	        tabbedPaneCount++;
	    }
	 
	 public void initTable(){
	    	dataPanel = new SOCRJTablePanel();
	    	dataPanel.resetTableRows(1);
	    }
	 
	 public void initAniPanel(){
		 //System.out.println("initAniPanel get called");
		 animRowIndex=0;
		 anim = new TransformAnim();
		 anim.init();
		 anim.addObserver(this);
		 aniPanel.removeAll();
		 aniPanel.add(anim);
	 }
	 protected void initExperimentPanel(){
		 	
		 	expPanel.removeAll();
			//System.out.println("initGraphPanel called");
	    	if (expChoices.getSelectedItem().equals("Card")){	    			    	
		    	card.reset();
		    	expPanel.add(card.getAnimation());	    
	    	}	else if (expChoices.getSelectedItem().equals("Coin")){
		    	
		    	coin.reset();
		    	expPanel.add(coin.getAnimation());	
	    	}else if (expChoices.getSelectedItem().equals("User Input")){
	    		expPanel.setLayout(new BoxLayout(expPanel, BoxLayout.Y_AXIS));
	    		expPanel.add(new JLabel("User Input"));
	    		expPanel.add(new JLabel("use PASTE button on the left to add data"));
	    		JButton clear = new JButton("Clear Table");
	    		clear.addActionListener(new ActionListener(){
	    			public void actionPerformed(ActionEvent arg0) {	
	    				dataPanel.resetTable();
	    			}});
	    		expPanel.add(clear);	
	    	}
	    	setTablePane(); 
	    	
			infoPanel = new SimulationResampleInferencePanel(this);
			infoPanel.addObserver(this);
			bPanel =new JPanel();
			//bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.Y_AXIS));
			
		}
	  
	 public void addTable(JTable table){
		 
	 }

	 public void setResamplingPanel(boolean clearAll) {
		 if (!clearAll)			 
			 return;
		 
	    	reSamplePanel.removeAll();
	    	if (dataPanel!=null && dataPanel.getRowCount()!=0){
	    		String[] rowName = new String[dataPanel.getRowCount()+1];
	    		rowName[0] = "Choose a row to resample";
	    		for (int i=1; i<rowName.length; i++)
	    			rowName[i]="Row:"+(i);
	    		final JComboBox rowList = new JComboBox (rowName);
	    		
	    		reSamplePanel.setLayout(new BoxLayout(reSamplePanel, BoxLayout.Y_AXIS));
	    	//	reSamplePanel.add(new JLabel("Choose a row to resample"));
	    		reSamplePanel.add(rowList);
	    		final JPanel orig = new JPanel();
	    		final JButton runResample = new JButton("Resample");
	    	
	    		String[] ks = new String[]{"100", "200", "300","500", "1000"};
	    		final JComboBox kChoices = new JComboBox (ks);
	    		kChoices.setPreferredSize(new Dimension(20, 100));
	    		kChoices.setSelectedIndex(4);
	    		final JToolBar tool = new JToolBar();
	    		
	    		
	    		origRow = new SOCRJTablePanel();
	    		origRow.setPreferredSize(new Dimension(reSamplePanel.getWidth(), 100));
	    		
	    		final JPanel resample = new JPanel();
	    		
	    		runResample.addActionListener(new ActionListener(){
	    			public void actionPerformed(ActionEvent arg0) {
	    				resampleRow = new SOCRJTablePanel();
	    				TableColumnModel columnModel = origRow.getColumnModel();
	    				
	    				String columnNames[] = new String[columnModel.getColumnCount()];
	    				
	    				numberOfResample = Integer.valueOf((String)kChoices.getSelectedItem());
	    				
	    				Object[][] rowData = new Object[numberOfResample][columnModel.getColumnCount()];    
	    			
	    				BootStrap bs = new BootStrap();
	    				bs.setBootStrapSize(numberOfResample);
	    			//	System.out.println("origRow 0 0 ="+origRow.getValueAt(0, 0));
	    				lowerBound=0;
	    				upperBound=0;
	    				bootStrapData = bs.getBootStrapData(origRow.getRowValue(0), origRow.getColumnCount());
	    				bootStrapDataIndex = bs.getBootStrapDataIndex();
	    				
	    				for (int i =0; i<numberOfResample; i++)
	    					for (int j =0; j<columnModel.getColumnCount(); j++){
	    						if (bootStrapData[i][j]<lowerBound)
	    							lowerBound= bootStrapData[i][j];
	    						if(bootStrapData[i][j]>upperBound)
	    							upperBound= bootStrapData[i][j];
	    						
	    						if(expChoices.getSelectedItem().equals("User Input")){
	    							
	    							rowData[i][j]=(double)bootStrapData[i][j];
	    						}
	    						else 
	    							rowData[i][j]=(int)bootStrapData[i][j];
	    						
	    						columnNames[j]=origRow.getColumnNameAt(j);
	    					}
	    			
	    				 					
	    				JTable t = new JTable(rowData, columnNames);
	    					
	    				resampleRow.setTable(t);
	    				resample.removeAll();
	    				resample.add(new JLabel("Resample result:"));
	    				resample.add(resampleRow);
	    				reSamplePanel.validate();
	    				initAniPanel();
	    			}});
	    		
	    		
	    		origRow.resetTableRows(1);
				orig.removeAll();
				orig.setLayout(new BoxLayout(orig, BoxLayout.Y_AXIS));
				
				tool.setFloatable(false);
				tool.add(new JLabel("ReSample Size K:"));
				tool.add(kChoices);
				tool.add(runResample);
				tool.setPreferredSize(new Dimension(50, 100));
				orig.add(tool);
				
				orig.add(new JLabel("Orig Row:"));
				orig.add(origRow);
			
	    		orig.setBackground(Color.LIGHT_GRAY);
	    	
	    		
	    		//resample.setBackground(Color.blue);
	    		resample.setLayout(new BoxLayout(resample, BoxLayout.Y_AXIS));
	    		resample.add(new JLabel("Resample result:"));
	    		SOCRJTablePanel resampleRow = new SOCRJTablePanel();
	    		resample.add(resampleRow);
	    		
	    		reSamplePanel.add(orig);
	    		reSamplePanel.add(resample);
	    		
	    		rowList.addActionListener(new ActionListener(){
	    			
	    			public void actionPerformed(ActionEvent arg0) {
	    				System.out.println("rowlist listener called ");
	    				
	    				int chosenRowIndex = rowList.getSelectedIndex()-1;
	    				if (chosenRowIndex==-1)
	    					return;
	    				System.out.println("chosenRowIndex="+chosenRowIndex);
	    				
	    				orig.removeAll();
	    				TableColumnModel columnModel = dataPanel.getColumnModel();
	    				int colCount =columnModel.getColumnCount();
	    				
	    				
	    				if(expChoices.getSelectedItem().equals("Coin"))			
	    					colCount --;
	    				else if(expChoices.getSelectedItem().equals("Card"))
	    					colCount = (colCount-1)/2;
	    			
	    				sampleSize = colCount;
	    				Object rowData[][] = new Object[1][colCount];
	    				String columnNames[] = new String[colCount];
	    				
	    				if(expChoices.getSelectedItem().equals("Coin"))
	    					for (int j =0; j<colCount; j++){
	    						rowData[0][j]=(int) Double.parseDouble(dataPanel.getValueAt(chosenRowIndex, j+1).toString());
		    					columnNames[j]=dataPanel.getColumnNameAt(j+1);
		    				}
	    				else if(expChoices.getSelectedItem().equals("Card"))
	    					for (int j =0; j<colCount; j++){
	    						int y= (int) Double.parseDouble(dataPanel.getValueAt(chosenRowIndex, j*2+1).toString());
	    						int z =(int) Double.parseDouble(dataPanel.getValueAt(chosenRowIndex, j*2+2).toString());
	    						
	    						rowData[0][j]=(Object)(getCardValue(y,z));
		    					columnNames[j]=dataPanel.getColumnNameAt(j*2+1);
		    				}
	    				else  if(expChoices.getSelectedItem().equals("User Input")){
	    					int realColumnCount=0;
		    				for (int j =0; j<columnModel.getColumnCount(); j++){
		    					if(dataPanel.getValueAt(chosenRowIndex, j)!=null && dataPanel.getValueAt(chosenRowIndex, j).toString().length()!=0){
		    						rowData[0][j]=dataPanel.getValueAt(chosenRowIndex, j);
		    						System.out.println("user input "+ rowData[0][j]);
		    						realColumnCount++;
		    					}
		    					else break;
		    					columnNames[j]=dataPanel.getColumnNameAt(j);
		    				}
		    				sampleSize = realColumnCount;
		    				columnNames= new String[realColumnCount];
		    				rowData= new Object[1][realColumnCount];
		    				for (int j =0; j<realColumnCount; j++){
		    						
		    					rowData[0][j]=dataPanel.getValueAt(chosenRowIndex, j);
		    					columnNames[j]=dataPanel.getColumnNameAt(j);
		    				}
	    				}
	    			
	    				JTable t = new JTable(rowData, columnNames);
	    					
	    				origRow.setTable(t);
	    				origRow.resetTableRows(1);
	    				origRow.setPreferredSize(new Dimension(reSamplePanel.getWidth(), 100));
	    			//	System.out.println("1 origRow 0 0 ="+origRow.getValueAt(0, 0));
	    				orig.add(tool);
	    				orig.add(origRow);
	    				
	    				reSamplePanel.validate();;
	    		
	    			}
	    		});
	    		
	    		if(rowName.length>1 && rowList.getSelectedIndex()==0)
	    			rowList.setSelectedIndex(1);
	    	}	    		
	    }
	    
	    public double getLowerBound(){
	    	//System.out.println("get lowerBound"+lowerBound);
	    	return lowerBound;
	    }
	    
	    public double getUpperBound(){
	    	//System.out.println("get upperBound"+upperBound);
	    	return upperBound;
	    }
	    
	    protected void setInfo(){
	    	infoPanel.addSlider();
	    	infoPanel.updateDisplay();
	    }
	    
	    protected void setTablePane(){
	   	
	    //	System.out.println("SimulateionResampleEdp getcard resultTable column count  "+card.getResultTable().getColumnCount());
	    //	System.out.println("SimulateionResampleEdp getcard resultTable row count  "+card.getResultTable().getRowCount());
	    	
	    	if (expChoices.getSelectedItem().equals("Card")){
	    		//System.out.println("SimulateionResampleEdp getcard resultTable row count  "+card.getResultTable().getRowCount());	
	    		dataPanel.setTable(card.getResultTable());
	    	}
	    	else if (expChoices.getSelectedItem().equals("Coin")){
	    		dataPanel.setTable(coin.getResultTable());
	    		//System.out.println("setTablePane coin table row count "+coin.getResultTable().getColumnCount());
	    	}else if (expChoices.getSelectedItem().equals("User Input")){
	    		
	    	}
	    	
	    	dataPanel.validate();
	    	
	    	setResamplingPanel(true);
	    }
	
	    public int getCardValue(int y, int z){
	    	return y+z*13-1;
	    }
	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment consists of dealing n cards at random from a standard deck of 52 cards.\n"
			+ "The denomination Yi and suit Zi of the i'th card are recorded for i = 1, ..., n on each update.\n"
			+ "The denominations are encoded as follows: 1 (ace), 2-10, 11 (jack), 12 (queen), 13 (king). \n"
			+ "The suits are encoded as follows: 0 (clubs), 1 (diamonds), 2 (hearts), 3 (spades). \n"
			+ "The parameter n can be varied with a scroll bar.";
	}

	public double bootStrapMean(double[] distData) {
		double sumX = 0;
		if (distData.length == 0) return (0);
		else sumX = distData[0];

		for (int i = 1; i < distData.length; i++)
			sumX += distData[i];
		double result = (sumX / distData.length);
		return result;
	}
	
	public double bootStrapProportion(double[] distData) {
		return 0.5;
		
	}
	public double bootStrapVariance(double[] distData) {
		double sumX = 0;
		double sampleMean = bootStrapMean(distData);
		if (distData.length<2) return 0;
		
		for (int i = 0; i < distData.length; i++)
			sumX += Math.pow(distData[i]-sampleMean, 2);
		
		return (sumX/(distData.length-1));	
	}

	public double[] getBootStrapXBar(int choosenCI){
		double[] xBarBootStrap;
		xBarBootStrap = new double[numberOfResample];
		for (int i =0; i<numberOfResample; i++)
			for (int j =0; j<sampleSize; j++){
				if(choosenCI == 0)
					xBarBootStrap[i]= bootStrapMean(bootStrapData[i]);
				if(choosenCI == 1)
					xBarBootStrap[i]= bootStrapProportion(bootStrapData[i]);
				if(choosenCI == 2)
					xBarBootStrap[i]= bootStrapVariance(bootStrapData[i]);
			}
		return xBarBootStrap;
	}
	
	/**
	* This method handles the timer events associated with the step process. The cards
	* are shown one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		//System.out.println("Action ");
		
		setTablePane();
	}
	
	
	 public int getSampleSize(){
		 return sampleSize;
	 }
	 
	 public int getNumberOfResample(){
		 return numberOfResample;
	 }
	 
	public void pasteData(Clipboard clipboard){
		 int crtRow = dataPanel.getSelectedRow();
		// System.out.println("crtRow"+crtRow);
		    int crtCl;
		    if (crtRow>=0)
		    	crtCl= dataPanel.getSelectedColumn();
		    else crtCl = dataPanel.getSelectedHeaderColumn();
		    
		   // why the second time crtRow return in correctly if clicked on header
		    // temp fix
		    if (crtCl<0){
		    	crtRow= -1;
		    	crtCl = dataPanel.getSelectedHeaderColumn();
		    }
		 
		  // System.out.println("Inside PASTE! crtRow ="+crtRow+ " crtCl="+crtCl);
					
		    if(crtRow == -1) {  //table header 
		    	//chart.resetMappingList(); // in order to deselect the header cell 
		    	
		    	try{
		    		//System.out.println("crtRow=-1 paste header: crtCol="+crtCl);
		    		clipboard.getContents(this);
		    		DataFlavor[] flavs = clipboard.getContents(this).getTransferDataFlavors();
		    		String tabData = clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor).toString();
	                    		
		    		StringTokenizer lnTkns = new StringTokenizer(tabData,"\r\n");
		    		int lineCt = lnTkns.countTokens();
		    		String firstLine = lnTkns.nextToken();
	                             
		    		StringTokenizer cellTkns = new StringTokenizer(firstLine, "\t \f");// IE use "space" Mac use tab as cell separator
		    		int cellCnt = cellTkns.countTokens();                  		
	                   
		    		//System.out.println("crtCl+cellCnt="+(crtCl+cellCnt));
		    		//System.out.println("chart.dataTable.getColumnCount()="+(chart.dataTable.getColumnCount()));
		    		if(crtCl+cellCnt> dataPanel.getColumnCount()){
		    			dataPanel.appendTableColumns(crtCl+cellCnt- dataPanel.getColumnCount()+1);
		    		
		    		//	System.out.println("append column"+ (crtCl+cellCnt+1- chart.dataTable.getColumnCount()+1));
		    		}
		    			                    		
		    		TableColumnModel columnModel= dataPanel.getColumnModel();
		    		int c = 0;   		 
		    		while(cellTkns.hasMoreTokens()) {   
		    			String h = cellTkns.nextToken();
		    		//	System.out.println("adding header "+(c+crtCl) +": "+h);   			
		    			columnModel.getColumn(c+crtCl).setHeaderValue(h);
		    			c++;
		    		}
	
		    		//
		    		if(lineCt>1){
		    		//	System.out.println("adding table content") ;
		    			crtRow++;
		    			if (crtCl<0)
		    				crtCl=0;			
		    			if(crtRow+lineCt > dataPanel.getRowCount())
		    				dataPanel.appendTableRows(crtRow+lineCt - dataPanel.getRowCount());
		    			
		    			String line;
		    			int r = 0;
		    			while(lnTkns.hasMoreTokens()) {
		    				line = lnTkns.nextToken();
	                                	
		    				//	String tb[] =line.split("\t");
		    				cellTkns = new StringTokenizer(line, " \t\f");// IE use "space" Mac use tab as cell separator
		    				cellCnt = cellTkns.countTokens();
		    				String tb[]= new String[cellCnt];
		    				int r1=0;
		    				while(cellTkns.hasMoreTokens()) {
		    					tb[r1]=cellTkns.nextToken();
		    					r1++;
		    				}
		    				//System.out.println("tb.length="+tb.length);
		    				int colCt=tb.length;
		    				if(crtCl+colCt > dataPanel.getColumnCount())
		    					dataPanel.appendTableColumns(crtCl+colCt - dataPanel.getColumnCount());
		    				for (int i=0; i<tb.length; i++){
		    					//System.out.println(tb[i]);
		    					if (tb[i].length()==0)
		    						tb[i]="0";
		    					dataPanel.setValueAt(tb[i],crtRow+r,i+crtCl);
		    					//System.out.println("setting "+tb[i]+" at "+(crtRow+r)+","+(i+crtCl));
		    				}
		    				
		    				r++;
		    			}  
		    		}
		    	
		    		dataPanel.updateEditableHeader(columnModel);

		    	}catch(Exception e){
                         OKDialog OKD = new OKDialog(null, true, 
         				"Unalbe to paste header. Check the datatype.\n");
                                 OKD.setVisible(true);
                                 e.printStackTrace();
                               }      
                 } 
		    else  // if(crtRow != -1)  adding table content only
                 { try  
                
                   { //System.out.println("crtRow="+crtRow+" paste cell: crtCol="+crtCl);
                 	clipboard.getContents(this);
                     DataFlavor[] flavs = clipboard.getContents(this).getTransferDataFlavors();
                     String tabData = clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor).toString();
                    // System.err.println(tabData);

                     StringTokenizer lnTkns = new StringTokenizer(tabData,"\r\n");

                     String line;
                     int max_col= 0;
    
                     int lineCt = lnTkns.countTokens();
                     if(crtRow+lineCt > dataPanel.getRowCount())
                    	 dataPanel.appendTableRows(crtRow+lineCt - dataPanel.getRowCount());
                     int r = 0;
                     while(lnTkns.hasMoreTokens()) {
                     	line = lnTkns.nextToken();
                     	
                    //	String tb[] =line.split("\t");
                         StringTokenizer cellTkns = new StringTokenizer(line, " \t\f");// IE use "space" Mac use tab as cell separator
                         int cellCnt = cellTkns.countTokens();
                         String tb[]= new String[cellCnt];
                         int r1=0;
                           while(cellTkns.hasMoreTokens()) {
                            tb[r1]=cellTkns.nextToken();
                            r1++;
                           }
                         //System.out.println("tb.length="+tb.length);
                     	int colCt=tb.length;
                     	//System.out.println("crtCl+colCt="+(crtCl+colCt));
                     	if(crtCl+colCt>max_col)
                     		max_col=crtCl+colCt;
                     	if(crtCl+colCt > dataPanel.getColumnCount())
                     		dataPanel.appendTableColumns(crtCl+colCt - dataPanel.getColumnCount());
                    
                     	
                     	//System.out.println("SOCRChart columnCount="+chart.dataTable.getColumnCount());
                     	
                         for (int i=0; i<tb.length; i++){
                         	//System.out.println(tb[i]);
                         	if (tb[i].length()==0)
                         		tb[i]="0";
                         	dataPanel.setValueAt(tb[i],crtRow+r,i+crtCl);
                         }
                         
                     	r++;
                     }  
                     // this will update the mapping panel
                   
         //crop or not ?????
                /*     chart.resetTableColumns(max_col+1);
                     chart.resetTableRows(crtRow+r+1);*/
                     
                   //  chart.resetMappingList();
                     
                     TableColumnModel columnModel= dataPanel.getColumnModel();  
                     dataPanel.updateEditableHeader(columnModel);
                   } 
		      catch(Exception e){
                     OKDialog OKD = new OKDialog(null, true, 
				"Unalbe to paste. Check the datatype.\n");
                     OKD.setVisible(true);
                     e.printStackTrace();
                   }       
		    
                 }	//else  *** if(crtRow != -1) 
		    
 
	}

	public void update(Observable arg0, Object arg1) {
		bPanel.validate();	
		//System.out.println("Simu update get called");
		
		animRowIndex++;
		
		animation(animRowIndex);
		
	}

}

