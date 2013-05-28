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

/* 	modified annieche 200508.
	separate the gui part from the model part
*/

package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.*;

public class ChiSquareContingencyTable extends Analysis implements MouseListener, ActionListener, KeyListener, MouseMotionListener, PropertyChangeListener {

	//public JTabbedPane tabbedPanelContainer;
	private JToolBar toolBar;
	private Frame frame;

	private JComboBox alphaCombo = new JComboBox(new String[]{"0.1","0.05","0.01","0.001"}); //
	private JComboBox rowNumberCombo = new JComboBox(new String[]{"2","3","4","5", "6", "7", "8", "9", "10"}); //
	private JComboBox colNumberCombo = new JComboBox(new String[]{"2","3","4","5", "6", "7", "8", "9", "10"}); //

	private JPanel cellPanel = null;//new JPanel(new BorderLayout());
	private JPanel comboPanel = null;//new JPanel(new BorderLayout());

	private JPanel alphaPanel = null;//new JPanel();
	private JPanel rowNumberPanel = null;//new JPanel();
	private JPanel colNumberPanel = null;//new JPanel();;

	private JLabel alphaLabel = new JLabel("Select Significance Level:");
	private JLabel rowNumberLabel = new JLabel("Select Number of Rows:");
	private JLabel colNumberLabel = new JLabel("Select Number of Columns:");

	private JTextField[][] cellText = null;
	private JTextField[] rowNameText = null;
	private JTextField[] colNameText = null;
	private static final int MAX_ROW_SIZE = 5;
	private static final int MAX_COL_SIZE = 5;

	private static double[][] cell = new double[MAX_ROW_SIZE][MAX_COL_SIZE];
	private static double[][] observed = null;//new double[MAX_ROW_SIZE][MAX_COL_SIZE];
	private static String[] rowNames = new String[MAX_ROW_SIZE];
	private static String[] colNames = new String[MAX_COL_SIZE];

	private double alpha = 0.05;
	private static int rowNumber;
	private static int colNumber;
	private static int cellWidth = 10;
	private static int cellHeight = 1;

	ChiSquareContingencyTableResult result;

	/**Initialize the Analysis*/
	public void init(){
		mapIndep= false;
		showData = false;
		showMapping= false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		analysisType = AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE;
		analysisName = "Chi Square Test for Homogeneity or Independence";
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = false;
		useServerExample = false;
		useStaticExample = ChiSquareContingencyTableExamples.availableExamples;

		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName(analysisName);
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

		tools2.remove(addButton2);
		tools2.remove(removeButton2);
		tools2.remove(removeButton2);
		depLabel.setText(VARIABLE);
		//indLabel.setText("");
       //   listIndepRemoved.setBackground(Color.LIGHT_GRAY);
       //   mappingInnerPanel.remove(listIndepRemoved);

		alphaCombo.addActionListener(this);
		alphaCombo.addMouseListener(this);
		alphaCombo.setEditable(false);
		alphaCombo.setSelectedIndex(1);

		rowNumberCombo.addActionListener(this);
		rowNumberCombo.addMouseListener(this);
		rowNumberCombo.setEditable(false);
		rowNumberCombo.setSelectedIndex(0);

		colNumberCombo.addActionListener(this);
		colNumberCombo.addMouseListener(this);
		colNumberCombo.setEditable(false);
		colNumberCombo.setSelectedIndex(0);

		setInputPanel();
		validate();
		reset();
	}


	/** Create the actions for the buttons */
	protected void createActionComponents (JToolBar toolBar){
		super.createActionComponents(toolBar);
	}


	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){
	}

	/**This method defines the specific statistical Analysis to be carried our on the user specified data. ANOVA is done in this case. */
	public void doAnalysis(){

		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();

		String dependentHeader =  null;
		try {
			dependentHeader= columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		} catch (Exception e) {
		}
/*

		if (! hasExample || !hasInput) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}
/*
		if (dependentIndex < 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}
*/

		////System.out.println("hasExample = " + hasExample);


		 String rowMessage = "Enter number of rows. Default is 2.";
		 String colMessage = "Enter number of columns. Default is 2.";

		 String msgWarning = "You didn't enter a number. Default 2 will be used. \nClick on CALCULATE if you'd like to change it.";

		/*
		if (!hasInput) {
			try {
				rowNumber = Integer.parseInt(JOptionPane.showInputDialog(rowMessage));
				hasInput = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, msgWarning);
				hasInput = true;
			} catch (Exception e) {
			}

			try {
				colNumber = Integer.parseInt(JOptionPane.showInputDialog(colMessage));
				hasInput = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, msgWarning);
				hasInput = true;
			} catch (Exception e) {
			}
		}
		*/


		Data data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		/******* add option to get test mean not zero, user input. *******/

		 //String alphaMessage = "Enter the significance level for test mean. Default is zero.";
		 //String alphaMeanWarning = "You didn't enter a number. Default zero will be used. Click on CALCULATE if you'd like to change it.";
		alpha = 0.05;

		////System.out.println("in gui alpha = " + alpha);

		try {
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		} catch (Exception e) {
			alpha = 0.05;
			////////////////System.out.println("in gui use random data Exception alpha = " + alpha);
		}

		// get data and row and col names.
		for (int i = 0; i < rowNumber; i++) {
			for (int j = 0; j < colNumber; j++) {
				try {
					rowNames[i] = (String)(rowNameText[i].getText());
				} catch (NullPointerException e) {
					////System.out.println("rowNames  NullPointerException = " + e);

				}

				try {
					colNames[j] = (String)(colNameText[j].getText());
				} catch (NullPointerException e) {
					////System.out.println("colNames  NullPointerException = " + e);
				}

				try {
					cell[i][j] = Double.parseDouble((String)(cellText[i][j].getText()));
					} catch (NumberFormatException e) {
						////System.out.println("cellText  NumberFormatException = " + e);

					} catch (NullPointerException e) {
						////System.out.println("cellText  NullPointerException = " + e);
					}
				}
			}



		observed = new double[rowNumber][colNumber];
		String[] rowNamesData = new String[rowNumber];
		String[] colNamesData = new String[colNumber];

		// trim observed array.

		for (int i = 0; i < rowNumber; i++) {
			rowNamesData[i] = rowNames[i];
			for (int j = 0; j < colNumber; j++) {
				observed[i][j] = cell[i][j];
			}
		}
		for (int j = 0; j < colNumber; j++) {
			colNamesData[j] = colNames[j];
		}

		data.setParameter(analysisType, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.SIGNIFICANCE_LEVEL, alpha + "");
		data.setInput(analysisType, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.OBSERVED_DATA, observed);
		data.setInput(analysisType, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.ROW_NAMES, rowNamesData);
		data.setInput(analysisType, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.COL_NAMES, colNamesData);

		result = null;
		try {
			result = (ChiSquareContingencyTableResult)data.getAnalysis(AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE);
		} catch (Exception e) {
			////System.out.println("ChiSquareContingencyTableResult e = " + e);
		}
		/* every one has to have its own, otherwise one Exception spills the whole. */

		updateResults();

	}

	public void updateResults(){

		if (result==null)
		return;

		result.setDecimalFormat(dFormat);


	setDecimalFormat(dFormat);

	resultPanelTextArea.setText("\n"); //clear first

		double[][] expected = null;
		int df = 0;
		int[] rowSum = null;
		int[] colSum = null;
		int grandTotal = 0;
		double chiStat = 0;

		try {
			chiStat = result.getPearsonChiSquareStat();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}
		try {
			df = result.getDF();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}
		try {
			grandTotal = result.getGrandTotal();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}

		try {
			expected = result.getExpectedData();
		} catch (Exception e) {
			////////////////System.out.println("Exception = " + e);
		}

		try {
			rowSum = result.getRowSum();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}
		try {
			colSum = result.getColSum();
		} catch (Exception e) {
			////////////////System.out.println("Exception = " + e);
		}

	//	int NumberDigitKept = 2;
		//String[][] expectedTruncated = AnalysisUtility.truncateDigits(expected, NumberDigitKept);
		String[][] expectedTruncated = result.getFormattedGroupArray(expected);

		resultPanelTextArea.append("\tResults of Chi-Square Test for Independent or Homogeneity\n" );
		//resultPanelTextArea.append("\n\tVariable  = " + dependentHeader + " \n" );
		//resultPanelTextArea.append("\n\tGroup " + valueList[0] + ": \tFrequency = " + sampleProportion[0]);

		resultPanelTextArea.append("\n\tNumber of Rows = " + rowNumber);
		resultPanelTextArea.append("\n\tNumber of Columns = " + colNumber);


		resultPanelTextArea.append("\n\n\n\t");

		for (int j = 0; j < colNumber; j++) {
			resultPanelTextArea.append("\t" + colNames[j]);
		}

		resultPanelTextArea.append("\tRow Total");
		resultPanelTextArea.append("\n\t------------------------------------------------------------------------------------------");

		for (int i = 0; i < rowNumber; i++) {

			resultPanelTextArea.append("\n\t" + rowNames[i]);
			for (int j = 0; j < colNumber; j++) {

				resultPanelTextArea.append("\t" + observed[i][j]);
				resultPanelTextArea.append("  (" + expectedTruncated[i][j] + ")");
			}
			resultPanelTextArea.append("\t" + rowSum[i]);

			resultPanelTextArea.append("\n");
			}

		resultPanelTextArea.append("\n\t------------------------------------------------------------------------------------------");
		resultPanelTextArea.append("\n\tCol Total");

		for (int j = 0; j < colNumber; j++) {
			resultPanelTextArea.append("\t" + colSum[j]);
		}

		resultPanelTextArea.append("\t" + grandTotal + "\n");
		resultPanelTextArea.append("\n\n\tDegrees of Freedom = " + df);

		resultPanelTextArea.append("\n\n\tPearson Chi-Square Statistics = " + result.getFormattedDouble(chiStat));
		double pValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(
				df)).getCDF(chiStat);
		resultPanelTextArea.append("\n\n\tP-Value = " + pValue);

		resultPanelTextArea.setForeground(Color.BLUE);
	}


	/** convert a generic string s to a fixed length one. */
    	public String monoString(String s) {
		String sAdd = new String(s + "                                      ");
		return sAdd.substring(0,14);
    	}

	/** convert a generic double s to a "nice" fixed length string */
    	public String monoString(double s) {
		final double zero = 0.00001;
        	Double sD = new Double(s);
		String sAdd = new String();
		if(s>zero)
	    		sAdd = new String(sD.toString());
		else  sAdd = "<0.00001";

		sAdd=sAdd.toLowerCase();
		int i=sAdd.indexOf('e');
		if(i>0)
	    		sAdd = sAdd.substring(0,4)+"E"+sAdd.substring(i+1,sAdd.length());
		else if(sAdd.length()>10)
				sAdd = sAdd.substring(0,10);

		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}

	/** convert a generic integer s to a fixed length string */
    	public String monoString(int s) {
		Integer sD = new Integer(s);
		String sAdd = new String(sD.toString());
		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}

    /** Implementation of PropertyChageListener.*/
    public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		//System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));

			//System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
   }
	public Container getDisplayPane() {
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		return this.getContentPane();
	}
    public String getOnlineDescription(){
        return new String("http://en.wikipedia.org/wiki/Pearson's_chi-square_test");
    }
    protected void setInputPanel() {
		inputPanel.removeAll();

		cellPanel = new JPanel();//new BorderLayout());
		cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.Y_AXIS));

		comboPanel = new JPanel();
		alphaPanel = new JPanel();
		rowNumberPanel = new JPanel();
		colNumberPanel = new JPanel();;

		alphaPanel.setBackground(Color.LIGHT_GRAY);
		rowNumberPanel.setBackground(Color.LIGHT_GRAY);
		colNumberPanel.setBackground(Color.LIGHT_GRAY);

		alphaPanel.add(alphaLabel, BorderLayout.EAST);
		alphaPanel.add(alphaCombo, BorderLayout.WEST);
		rowNumberPanel.add(rowNumberLabel, BorderLayout.EAST);
		rowNumberPanel.add(rowNumberCombo, BorderLayout.WEST);

		colNumberPanel.add(colNumberLabel, BorderLayout.EAST);
		colNumberPanel.add(colNumberCombo, BorderLayout.WEST);

		comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.Y_AXIS));

		comboPanel.add(alphaPanel);//, BorderLayout.NORTH);
		comboPanel.add(rowNumberPanel);//, BorderLayout.CENTER);
		comboPanel.add(colNumberPanel);//, BorderLayout.SOUTH);
		comboPanel.add(cellPanel);//, BorderLayout.NORTH);
		inputPanel.add(comboPanel, BorderLayout.NORTH);
		createContingencyTable();

	}
     public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);

		if (event.getSource() == alphaCombo){
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		}
		if (event.getSource() == rowNumberCombo){
			rowNumber = Integer.parseInt((String)(rowNumberCombo.getSelectedItem()));
			//System.out.println("actionPerformed rowNumber changed to " + rowNumber);
			createContingencyTable();

		}
		if (event.getSource() == colNumberCombo){

			colNumber = Integer.parseInt((String)(colNumberCombo.getSelectedItem()));
			//System.out.println("actionPerformed colNumber changed to " + colNumber);
			createContingencyTable();

		}

		for (int i = 0; i < rowNumber; i++) {
			for (int j = 0; j < colNumber; j++) {
				if (event.getSource() == rowNameText[i]) {
				//////System.out.println("rowNames  NullPointerException = " + e);
					try {
						rowNames[i] = (String)(rowNameText[i].getText());
						//rowNameList.add((String)(rowNameText[i].getText()));
					} catch (NullPointerException e) {
						////System.out.println("rowNames  NullPointerException = " + e);

					}
					////System.out.println("rowNames  = " + rowNames[i]);
				}

				if (event.getSource() == colNameText[j]) {
					try {
						colNames[j] = (String)(colNameText[j].getText());
						//colNameList.add((String)(colNameText[i].getText()));
					} catch (NullPointerException e) {

						////System.out.println("colNames  NullPointerException = " + e);
					}
					////System.out.println("colNames  = " + colNames[j]);
				}

				if (event.getSource() == cellText[i][j]) {

					try {
						cell[i][j] = Double.parseDouble((String)(cellText[i][j].getText()));
						//cellList[i].add((String)(cellText[i][j].getText()));
						////System.out.println("actionPerformed cell[i][j] = " + cell[i][j]);
						if (i + 1 == rowNumber && j + 1 == colNumber) {
							cellText[0][0].requestFocus();
							////System.out.println("last one");

						}

						else if (j + 1 == colNumber) {
							cellText[i+1][0].requestFocus();
							////System.out.println(" change to next row");

						}else {
							cellText[i][j + 1].requestFocus();
							////System.out.println(" change to next column");

						}
					} catch (NumberFormatException e) {
						//System.out.println("cellText  NumberFormatException = " + e);

					} catch (NullPointerException e) {
						//System.out.println("cellText  NullPointerException = " + e);
					}
				}
			}
		}
		createContingencyTable();
	}

	public void mouseClicked(MouseEvent event) {
		//super.mouseClicked(event);
		//System.out.println("mouse clicked");
		if (event.getSource() == alphaCombo){
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		}
		if (event.getSource() == rowNumberCombo){
			rowNumber = Integer.parseInt((String)(rowNumberCombo.getSelectedItem()));
			//System.out.println("mouseClicked rowNumber changed to " + rowNumber);
			createContingencyTable();
		}
		if (event.getSource() == colNumberCombo){
			colNumber = Integer.parseInt((String)(colNumberCombo.getSelectedItem()));
			//System.out.println("mouseClicked colNumber changed to " + colNumber);
			createContingencyTable();
		}


		for (int i = 0; i < rowNumber; i++) {
			for (int j = 0; j < colNumber; j++) {
				if (event.getSource() == rowNameText[i]) {
				//////System.out.println("rowNames  NullPointerException = " + e);
					try {
						rowNames[i] = (String)(rowNameText[i].getText());
						//rowNameList.add((String)(rowNameText[i].getText()));
					} catch (NullPointerException e) {
						////System.out.println("rowNames  NullPointerException = " + e);

					}
					////System.out.println("rowNames  = " + rowNames[i]);
				}

				if (event.getSource() == colNameText[j]) {
					try {
						colNames[j] = (String)(colNameText[j].getText());
						//colNameList.add((String)(colNameText[i].getText()));
					} catch (NullPointerException e) {

						////System.out.println("colNames  NullPointerException = " + e);
					}
					////System.out.println("colNames  = " + colNames[j]);
				}

				if (event.getSource() == cellText[i][j]) {
					////System.out.println("rowNames event is cellText");
					try {
						cell[i][j] = Double.parseDouble((String)(cellText[i][j].getText()));
						//cellList[i].add((String)(cellText[i][j].getText()));
						////System.out.println("actionPerformed cell[i][j] = " + cell[i][j]);
						if (i + 1 == rowNumber && j + 1 == colNumber) {
							//cellText[0][0].requestFocus();
							////System.out.println("last one");

						}

						else if (j + 1 == colNumber) {
							//cellText[i+1][0].requestFocus();
							////System.out.println(" change to next row");

						}else {
							//cellText[i][j + 1].requestFocus();
							////System.out.println(" change to next column");

						}
					} catch (NumberFormatException e) {
						////System.out.println("cellText  NumberFormatException = " + e);

					} catch (NullPointerException e) {
						////System.out.println("cellText  NullPointerException = " + e);
					}
				}
			}

		}
	}


	private void createContingencyTable() { //int rowNumber, int colNumber) {
		cellPanel.removeAll();
		this.cellText = new JTextField[rowNumber][colNumber];
		this.rowNameText = new JTextField[rowNumber];
		this.colNameText = new JTextField[colNumber];

		//cellPanel.setBackground(Color.RED);
		for (int j = 0; j < colNumber; j++) {
			colNameText[j] = new JTextField("Column " + (j + 1), cellWidth);
			colNameText[j].addActionListener(this);
			colNameText[j].addMouseListener(this);
			colNameText[j].addKeyListener(this);

		}

		for (int i = 0; i < rowNumber; i++) {
			rowNameText[i] = new JTextField("Row " + (i + 1), cellWidth);
			rowNameText[i].addActionListener(this);
			rowNameText[i].addMouseListener(this);
			rowNameText[i].addKeyListener(this);

			for (int j = 0; j < colNumber; j++) {
				cellText[i][j] = new JTextField("", cellWidth);//new JTextField("(" + i + ", " + j + ")", cellWidth);
				cellText[i][j].addActionListener(this);
				cellText[i][j].addMouseListener(this);
				cellText[i][j].addKeyListener(this);
			}
		}

		JPanel[] cellSubPanel = new JPanel[rowNumber];
		JPanel colSubPanel = new JPanel();
		colSubPanel.setBackground(Color.WHITE);
		JTextField empty = new JTextField("", cellWidth);
		empty.setEditable(false);
		empty.setVisible(true);
		empty.setBackground(Color.WHITE);
		colSubPanel.add(empty);
		colSubPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		colSubPanel.setPreferredSize(new Dimension(cellWidth * 4, 40));
		colSubPanel.setBackground(Color.WHITE);

		for (int j = 0; j < colNumber; j++) {
			colSubPanel.add(colNameText[j]);
		}
		cellPanel.add(colSubPanel);

		for (int i = 0; i < rowNumber; i++) { // somehow the reverse works, not sure why.
			cellSubPanel[i] = new JPanel();
			cellSubPanel[i].setBackground(Color.WHITE);
			cellSubPanel[i].setPreferredSize(new Dimension(cellWidth * 4, 40));
			//cellSubPanel[i].setLayout(new BoxLayout(cellSubPanel[i], BoxLayout.X_AXIS));

			for (int j = colNumber-1; j >=0; j--) {  // somehow the reverse works, not sure why.
				cellSubPanel[i].add(cellText[i][j], BoxLayout.X_AXIS);
				cellText[i][j].setBackground(Color.WHITE);
			}

			cellSubPanel[i].add(rowNameText[i], BoxLayout.X_AXIS);
			cellPanel.add(cellSubPanel[i]);//, BoxLayout.X_AXIS);

		}

		inputPanel.validate();
		//repaint();
	}
	public void mouseReleased(MouseEvent event) {
		mouseClicked(event);
	}

	public void mouseExited(MouseEvent event) {
		//mouseClicked(event);
	}
	public void mousePressed(MouseEvent event) {
		mouseClicked(event);
	}


	public void keyReleased(KeyEvent  event){
		//System.out.println("rowNames  keyReleased event = " + event);
		keyTyped(event);
	}
	public void keyPressed(KeyEvent  event){
		//System.out.println("rowNames  keyPressed event = " + event);
		keyTyped(event);

	}
	public void keyTyped(KeyEvent  event){
		////System.out.println("rowNames  keyTyped event = " + event);

		//if(event.getKeyCode() == KeyEvent.VK_TAB) {
		//	//System.out.println("rowNames  keyTyped event.getKeyCode() == KeyEvent.VK_TAB");
		//}

		if(event.getKeyCode() == KeyEvent.VK_ENTER) {
			////System.out.println("rowNames  keyTyped event.getKeyCode() == KeyEvent.VK_ENTER");
			//must have this setKeyCode otherwise number disappears. I don't know why. annie.
			event.setKeyCode(KeyEvent.VK_TAB);
			//event.setKeyCode(KeyEvent.VK_TAB);
			//transferFocus(); // doesn't seem to do anything.

/*
		if (event.getSource() == alphaCombo){
			alpha = Double.parseDouble((String)(alphaCombo.getSelectedItem()));
		}
		if (event.getSource() == rowNumberCombo){
			rowNumber = Integer.parseInt((String)(rowNumberCombo.getSelectedItem()));

			createContingencyTable();
		}
		if (event.getSource() == colNumberCombo){
			colNumber = Integer.parseInt((String)(colNumberCombo.getSelectedItem()));
			createContingencyTable();
		}
*/
		int count1 = 0, count2 = 0, count3 = 0, count4 = 0;
		for (int i = 0; i < rowNumber; i++) {
			for (int j = 0; j < colNumber; j++) {
				if (i == 0 && j == 0)
					System.out.println("RESTART FOR LOOP");
				/*
				if (event.getSource() == rowNameText[i]) {
				//////System.out.println("rowNames  NullPointerException = " + e);
					try {
						rowNames[i] = (String)(rowNameText[i].getText());
						//rowNameList.add((String)(rowNameText[i].getText()));
					} catch (NullPointerException e) {
						////System.out.println("rowNames  NullPointerException = " + e);

					}
					////System.out.println("rowNames  = " + rowNames[i]);
				}

				if (event.getSource() == colNameText[j]) {
					try {
						colNames[j] = (String)(colNameText[j].getText());
						//colNameList.add((String)(colNameText[i].getText()));
					} catch (NullPointerException e) {

						////System.out.println("colNames  NullPointerException = " + e);
					}
					////System.out.println("colNames  = " + colNames[j]);
				}
				*/
				//if (event.getSource() == cellText[i][j]) {
					System.out.println("i = " + i + ", j = " + j);
					try {
						cell[i][j] = Double.parseDouble((String)(cellText[i][j].getText()));
						//cellList[i].add((String)(cellText[i][j].getText()));
						//System.out.println("actionPerformed cell[i][j] = " + cell[i][j]);

						if (i + 1 == rowNumber && j + 1 == colNumber) {

							//cellText[0][0].requestFocus();
							System.out.println("i + 1 == rowNumber && j + 1 == colNumber requestFocus");
							count1++;
						}

						else if (j + 1 == colNumber) {
							//cellText[i+1][0].requestFocus();
							System.out.println("j + 1 == colNumbe requestFocus");
							count2++;
						}

						else {
							//cellText[i][j + 1].requestFocus();
							System.out.println("everything else requestFocus");
							count3++;
						}
						//cell[i][j] = Double.parseDouble((String)(cellText[i][j].getText()));

					} catch (NumberFormatException e) {
						System.out.println("cellText  NumberFormatException = " + e);

					} catch (NullPointerException e) {
						System.out.println("cellText  NullPointerException = " + e);
					}

				}
			}
			System.out.println("count1 = " + count1 + " count2 = " + count2 + " count3 = " + count3 + " count4 = " + count4);

		}
	}
/*
	public void adjustmentValueChanged(AdjustmentEvent event) {

		//System.out.println("adjustmentValueChanged  ");

		if (event.getSource() == rowNumberBar){
			rowNumber = rowNumberBar.getValue();
			//System.out.println("adjustmentValueChanged  rowNumber = " + rowNumber);
			rowNumberText.setText(rowNumber + "");
			createContingencyTable();
		}
		if (event.getSource() == colNumberBar){
			colNumber = colNumberBar.getValue();
			//System.out.println("adjustmentValueChanged  colNumber = " + colNumber);
			colNumberText.setText(colNumber + "");
			createContingencyTable();

		}

	}
*/
	public void reset() {
		super.reset();
		hasExample = false;
		hasInput = false;
		setInputPanel();

		repaint();

	}
}

