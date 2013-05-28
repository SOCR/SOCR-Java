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


package edu.ucla.stat.SOCR.core;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.*;
import java.net.MalformedURLException;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.FloatSlider;
import edu.ucla.stat.SOCR.util.OKDialog;
import edu.ucla.stat.SOCR.util.ObservableWrapper;
//import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.modeler.gui.ModelerColor;
import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.modeler.gui.ModelerDimension;
import edu.ucla.stat.SOCR.modeler.gui.ModelerGui;
import edu.ucla.stat.SOCR.modeler.Modeler;

/** This class implements the main interface for SOCRModeler */
public class SOCRModeler extends SOCRApplet implements Observer, ActionListener, ItemListener{
	
	//************************ FINAL/CONSTANTS ********************************//
	private final static String ABOUT = "ABOUT";
	private final static String HELP = "HELP";
	private final static String SNAPSHOT = "SNAPSHOT";
	private final static String BIN_SLIDER_LABEL = "  Number of Bins"; //"  NUMBER OF BINS.  LESS BINS. ";
//	private final static String BIN_SLIDER_LABEL_RIGHT = "  More Bins.  "; //"  MORE BINS.  "
	private final static String X_SLIDER_LABEL = "  Horizontal Range. (X) "; //"  HORIZONTAL SCALE.  "
	private final static String Y_SLIDER_LABEL = "  Vertical Range. (Y)  "; //"  VERTICAL SCALE.  "

	//************************ STRING variables *******************************//
	public String buffer = "";

	boolean debug = false;
	
	//************************ BOOLEAN VARIABLES. *****************************//
	private boolean initClicked = true;
	private boolean rescaleClicked = false;

	//************************ ALL THE JPanel. ********************************//
	private JPanel rightPanel = new JPanel();
	private JPanel toolPanel = new JPanel();
	private JPanel choicePanel = new JPanel();

	//*************** Panes of the Present Panel(Right side) ******************//
	private JScrollPane choiceScrollPane = new JScrollPane();
     private JSplitPane fSOCRPane = null;

	//************************ ALL THE JToolBar. ******************************//
	private JToolBar ccpBar = new JToolBar(JToolBar.HORIZONTAL);
	private JToolBar aboutBar = new JToolBar(JToolBar.HORIZONTAL);
	private JToolBar toolBarButton = new JToolBar(" ",JToolBar.HORIZONTAL);
	private JToolBar toolBarScrolls = new JToolBar(" ",JToolBar.HORIZONTAL);
	private JToolBar zoomBar = new JToolBar(" ",JToolBar.HORIZONTAL);

	//************************ ALL THE JScrollBar. ********************************//
	//private JScrollBar bins = null;//JScrollBar.HORIZONTAL,10, 2,5,50);
	//private JScrollBar xScale = null;//JScrollBar.VERTICAL,20, 1,10,2000);
	//private JScrollBar yScale = null;//JScrollBar.VERTICAL,20, 1,10,2000);
	//************************ ALL THE JScrollBar. ********************************//
	protected FloatSlider xScaleSlider ;
	protected FloatSlider yScaleSlider ;
	protected FloatSlider binsSlider;
	private final static int xscaleDefault = 5;
	private final static int yscaleDefault =10;
	public final static int binDefault= 10;
	
	private int calledByModelerGui = 0;  // for some reason, the update been called twice once slider value is changed
	
	//************************ ALL THE JButton. *******************************//
	private JButton pasteButton = new JButton("PASTE");
	private JButton copyButton = new JButton("COPY");
	private JButton resetButton = new JButton("RESET");
	private JButton aboutButton = new JButton("ABOUT");
	// added by annieche 20060409
	private JButton helpButton = new JButton("HELP");
	private JButton snapShotButton = new JButton("SNAPSHOT");
	private JButton zoomOut = new JButton("ZOOM OUT");
	private JButton zoomIn = new JButton("ZOOM IN");
	private JButton usePosNegX = new JButton("USE +/- X");
	private JButton usePosX = new JButton("USE + X");
	private JButton fileButton = new JButton("FILE OPEN");
	private JButton reinit = new JButton("RE-INITIALIZE");

	//****************** ALL THE OTHER GUI/AWT/SWING WIDGETS **********************//
	public JCheckBox rawData = new JCheckBox("Raw Data",false);
	public JCheckBox rescale = new JCheckBox("Scale Up",false);

	public JTextField histBins = new JTextField("10",3);
	public JLabel histLabel = new JLabel("Bins");
	private DefaultTableModel tModel;
	private TableColumn clm2;
	private TitledBorder titleBorder;
	// Clipboard objects
	public Clipboard clipboard;

	//**************************** FILE COMPONENTS ********************************//
	public File file;
	public FileDialog FileLocate;
	public Frame fDialog = new Frame();
	private JFileChooser jfc;
	private final static int BUF_SIZE = 8192;

	//*****************************************************************************//
	private ModelerGui modelerGui;
	private ObservableWrapper observablewrapper = new ObservableWrapper();

	
	//************************FOR READING DATA FROM FILE***************************//
	private Vector<Float> xDataArray;
	private Vector<Float> yDataArray;
	
	private int xCount;
	private int yCount;
	private final static int initArraySize = 10;
	
	
	/********************************************************************************/

	public void initGUI() {
		ModelerDimension.initScreenSize();

		//rightPanel.setPreferredSize(new Dimension(ModelerDimension.TOP_RIGHT_PANE_WIDTH, ModelerDimension.TOP_RIGHT_PANE_HEIGHT));
		//toolPanel.setPreferredSize(new Dimension(ModelerDimension.TOOL_PANEL_WIDTH, ModelerDimension.TOOL_PANEL_HEIGHT));

		/**************************************************************************/
		// BEGINNING MAKING THE LEFT PANEL (choicePanel)
		//configuration for fControlPanel: left side panel
		controlPanelTitle = "SOCR Models";
		implementedFile = "implementedModelers.txt";
		SOCRCodeBase.setCodeBase(this.getCodeBase());

		// BEGINNING MAKING THE LEFT PANEL'S WIDGETS
		addJCheckBox(rescale);
		addJCheckBox(rawData);
		rescale.setBackground(ModelerColor.CHECKBOX_RESCALE_BACKGROUND);
		rawData.setBackground(ModelerColor.CHECKBOX_RAWDATA_BACKGROUND);
		rawData.addItemListener(this);
		rescale.addActionListener(this);
		addJPanel(choicePanel);
		choicePanel.add(choiceScrollPane);
		choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
		choicePanel.setBackground(ModelerColor.PANEL_CHOICE_BACKGROUND);
		//choicePanel.setSize(ModelerDimension.PANEL_CHOICE_WIDTH, ModelerDimension.PANEL_CHOICE_HEIGHT);

		titleBorder = new TitledBorder(new EtchedBorder(), "Parameters");

		choicePanel.setBorder(titleBorder);
		choicePanel.add(new JButton("trial"));
		choicePanel.validate();
		choicePanel.repaint();
		// END MAKING THE LEFT PANEL'S WIDGETS, END choicePanel.
		/**************************************************************************/

		// init modeler components
		modelerGui = new ModelerGui();
		modelerGui.setCodeBase(codeBase);
		modelerGui.init();
		modelerGui.setGuiLink(this);
		modelerGui.setDebug(debug);
		
		//System.out.println("SOCRModler modelerGui init finished modelerGui.infiPanelTextAre is "+ modelerGui.infoPanelTextArea.getText());

		//init observer and add observers
		observablewrapper.addObserver(this);
		observablewrapper.addJCheckBox(rescale);
		observablewrapper.addJButton(reinit);

		try {
			toolBarScrolls.setFloatable(false);
			toolBarScrolls.setLayout(new BorderLayout());
			toolBarButton.setFloatable(false);
			toolBarButton.add(this.fileButton); // holds all the buttons.

			fileButton.addActionListener(this);
			toolBarButton.add(resetButton);
			resetButton.addActionListener(this);
			aboutBar.setFloatable(false);
			aboutBar.add(aboutButton);
			aboutBar.add(helpButton);
			aboutBar.add(snapShotButton);
			aboutButton.addActionListener(this);
			helpButton.addActionListener(this);
			snapShotButton.addActionListener(this);
			JPanel barPanelButton = new JPanel(new BorderLayout());
			JPanel barPanelBin = new JPanel(new BorderLayout()); // for the panel tha holds number of bins.

			//toolBarScrolls.setSize(new Dimension(ModelerDimension.TOOL_BAR_WIDTH, ModelerDimension.TOOL_BAR_HEIGHT));
			toolBarScrolls.setBackground(ModelerColor.BAR_BACKGROUND);
			toolBarButton.setBackground(ModelerColor.BUTTON_BACKGROUND);

			barPanelButton.setBackground(ModelerColor.BUTTON_BACKGROUND);
			zoomBar.setFloatable(false);
			zoomBar.add(zoomOut);
			zoomOut.addActionListener(this);
			zoomBar.add(zoomIn);
			zoomIn.addActionListener(this);

			//zoomBar.add(usePosX);
			usePosX.addActionListener(this);
			//zoomBar.add(usePosNegX);
			usePosNegX.addActionListener(this);

			// JScrollBar(int orientation,
			// int value(initial value of bar),
			// int extent(i.e. the bar length),
			// int min(min it can go),
			// int max(i.e. end point, max it can go.))

			
			//bins = new JScrollBar(JScrollBar.HORIZONTAL, 100, 1, 100, 500);
			// these attributes make the bins look good.
		/*	barPanelBin.setBackground(ModelerColor.BAR_BACKGROUND);
			barPanelBin.add(new JLabel(BIN_SLIDER_LABEL), BorderLayout.WEST);
			barPanelBin.add(bins, BorderLayout.CENTER);
			barPanelBin.add(new JLabel(BIN_SLIDER_LABEL_RIGHT), BorderLayout.EAST);*/
			//bins.addAdjustmentListener(this);

			//***** END MAKING THE NUMBER_OF_BINS BAR

			//***** BEGIN MAKING THE X_SCALE BAR
			//JPanel barPanelX = new JPanel(new BorderLayout());
		//	barPanelX.setBackground(ModelerColor.BAR_BACKGROUND);
		//	xScaleSlider =  new FloatSlider(X_SLIDER_LABEL, xscaleDefault, ModelerConstant.GRAPH_DEFAULT_XScale_Min, ModelerConstant.GRAPH_DEFAULT_XScale_Max);
			xScaleSlider =  new FloatSlider(X_SLIDER_LABEL, xscaleDefault, 1, ModelerConstant.GRAPH_UPPER_LIMIT);
			xScaleSlider.setTextFormat(new DecimalFormat("#0"));
			xScaleSlider.setBackground(ModelerColor.BAR_BACKGROUND);
			xScaleSlider.addObserver(this);
		/*	xScale = new JScrollBar(JScrollBar.HORIZONTAL, 5, 5, 1, (int)Math.ceil(ModelerConstant.GRAPH_UPPER_LIMIT));//2000);

			barPanelX.add(new JLabel(X_SLIDER_LABEL), BorderLayout.WEST);
			barPanelX.add(xScale, BorderLayout.CENTER);

			xScale.addAdjustmentListener(this);

			xScale.setValue(xScale.getMaximum()-5);

			try {
				if (modelerGui.modelObject.getModelType() == Modeler.FOURIER_TYPE || modelerGui.modelObject.getModelType() == Modeler.WAVELET_TYPE) {
					modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
				} else {
					modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
				}
			} catch (Exception ex) {
				modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
			}*/
			try {
				if (modelerGui.modelObject.getModelType() == Modeler.FOURIER_TYPE || modelerGui.modelObject.getModelType() == Modeler.WAVELET_TYPE) {
					//modelerGui.setXScale((xScaleSlider.getFloatMaximum()-xScaleSlider.getFloatValue()));
					modelerGui.setXScale(xScaleSlider.getFloatValue(), false);
				} else {
					//modelerGui.setXScale((int)(xScaleSlider.getFloatMaximum()-xScaleSlider.getFloatValue()));
					modelerGui.setXScale(xScaleSlider.getFloatValue(), false);

				}
			} catch (Exception ex) {
			//	modelerGui.setXScale((int)(xScaleSlider.getFloatMaximum()-xScaleSlider.getFloatValue()));
				modelerGui.setXScale(xScaleSlider.getFloatValue(), false);

			}
			//***** END MAKING THE X_SCALE BAR

			//***** BEGIN MAKING THE Y_SCALE BAR
			JPanel barPanelY = new JPanel(new BorderLayout());
			barPanelY.setBackground(ModelerColor.BAR_BACKGROUND);
		/*	yScale = new JScrollBar(JScrollBar.HORIZONTAL, 5, 2, 1, 201);//2000);
			barPanelY.add(new JLabel(Y_SLIDER_LABEL), BorderLayout.WEST);
			barPanelY.add(yScale, BorderLayout.CENTER);
			yScale.addAdjustmentListener(this);
			yScale.setValue(yScale.getMaximum()-10);
			modelerGui.setYScale(yScale.getMaximum()-yScale.getValue());
			*/

		//	yScaleSlider =  new FloatSlider(Y_SLIDER_LABEL, yscaleDefault, ModelerConstant.GRAPH_DEFAULT_YScale_Min, ModelerConstant.GRAPH_DEFAULT_YScale_Max);
			yScaleSlider =  new FloatSlider(Y_SLIDER_LABEL, yscaleDefault, 1, ModelerConstant.GRAPH_Y_UPPER_LIMIT);
			yScaleSlider.setTextFormat(new DecimalFormat("#0"));
			yScaleSlider.setBackground(ModelerColor.BAR_BACKGROUND);
			modelerGui.setYScale(yScaleSlider.getFloatValue());
			yScaleSlider.addObserver(this);
			
			//***** BEGIN MAKING THE NUMBER_OF_BINS BAR
			binsSlider =  new FloatSlider(BIN_SLIDER_LABEL, binDefault, 1, 500);
			binsSlider.setTextFormat(new DecimalFormat("#0"));
			modelerGui.setBins(binDefault);
			binsSlider.setBackground(ModelerColor.BAR_BACKGROUND);
			binsSlider.addObserver(this);

			// then staple these panels to the bar
			toolBarScrolls.add(binsSlider,  BorderLayout.NORTH);
			toolBarScrolls.add(xScaleSlider,  BorderLayout.SOUTH);
			toolBarScrolls.add(yScaleSlider,  BorderLayout.CENTER);
			
			//***** END MAKING THE Y_SCALE BAR
			// then staple these panels to the bar
		/*	toolBarScrolls.add(barPanelBin,  BorderLayout.NORTH);
			toolBarScrolls.add(barPanelX,  BorderLayout.SOUTH);
			toolBarScrolls.add(barPanelY,  BorderLayout.CENTER);*/
			toolBarScrolls.add(binsSlider,  BorderLayout.NORTH);
			toolBarScrolls.add(xScaleSlider,  BorderLayout.SOUTH);
			toolBarScrolls.add(yScaleSlider,  BorderLayout.CENTER);
			//centerPanel.add(toolBarScrolls, BorderLayout.CENTER);

			}	catch(Exception e) {
			e.printStackTrace();
		}
			
		// copy paste associated inits
		try {
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		} catch (Exception e) {
			System.out.println("SOCRModeler:initGUI(): No Security Access!");
		}

		ccpBar.setFloatable(false);
		ccpBar.add(pasteButton);
		ccpBar.add(copyButton);

		pasteButton.addActionListener(this);
		copyButton.addActionListener(this);
		rightPanel.setLayout(new BorderLayout());
		rightPanel.setBackground(ModelerColor.BLUE);
		toolPanel.setLayout(new BorderLayout());
		toolPanel.setBackground(ModelerColor.BLACK);

		JPanel moderlerPanel = new JPanel(new BorderLayout());
		moderlerPanel.add(modelerGui.getContentPane(), BorderLayout.CENTER);

		JPanel bottomTextPanel = new JPanel(new BorderLayout());
		bottomTextPanel.add(new JTextArea("If your data is outside of the range [-500:500], " +
				"you should scale/transform your data first to fit in this range.\n" +
				"The applet will not function properly otherwise."), BorderLayout.SOUTH);

		//rightPanel.add(toolBarScrolls, BorderLayout.SOUTH); // the big panel at
		rightPanel.add(toolPanel, BorderLayout.NORTH); // the buttons
		rightPanel.add(moderlerPanel, BorderLayout.CENTER); // the big panel at bottom-right.
		rightPanel.add(bottomTextPanel, BorderLayout.SOUTH); 
		
		toolPanel.add(toolBarButton, BorderLayout.NORTH);
		toolPanel.add(toolBarScrolls, BorderLayout.CENTER); // the sliders

		toolBarButton.add(ccpBar,BorderLayout.EAST);
		toolBarButton.add(aboutBar,BorderLayout.CENTER);
		toolBarButton.add(zoomBar,BorderLayout.WEST);

		/**************************************************************************/
		fPresentPanel.setViewportView(rightPanel);
		fPresentPanel.setBackground(ModelerColor.PANEL_PRESENT_BACKGROUND);
		//fPresentPanel.setTopComponent(rightPanel);
		//fPresentPanel.setBottomComponent(modelerGui.getContentPane());
		// uncomment the next two lines .
		//fControlScrollPane = new JScrollPane(fControlPanel);
        fSOCRPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fControlPanel, fPresentPanel);
        // fSCORPane is the whole thing: left + right.
		// int newOrientation, Component newLeftComponent, Component newRightComponent)

		fSOCRPane.setDividerLocation(ModelerDimension.PANEL_DIVISION_PROPORTION);
		//fPresentPanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_PRESENT_WIDTH, ModelerDimension.PANEL_PRESENT_HEIGHT));
		//fControlPanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_CONTROL_WIDTH, ModelerDimension.PANEL_CONTROL_HEIGHT));
		fSOCRPane.resetToPreferredSizes();
		fPresentPanel.validate();
		fPresentPanel.repaint();
	}

	public void actionPerformed(ActionEvent evt) {

		Object source = evt.getSource();
		//String command = evt.getActionCommand();


		if(evt.getSource()==resetButton) {
			modelerGui.clearData();
			modelerGui.graph.clear();
			//initClicked = false;
			//reinitMixtureModel = false;
		}

		if(evt.getSource() == fileButton) {
			FileLocate = new FileDialog(fDialog);
			FileLocate.setVisible(true);
			fDialog.setVisible(true);
			
			xDataArray = new Vector(initArraySize);
			yDataArray = new Vector(initArraySize);
			xCount = 0;
			yCount = 0;

			
			if(FileLocate.getFile()!=null)
				loadFileData(modelerGui.dataTable, modelerGui.dataTable);
			//initClicked = false;
			//reinitMixtureModel = false;
			
			//modelerGui.useDataFromFile(true);
			
			FileLocate.dispose();
			fDialog.dispose();
			
			float[] xData = new float[xDataArray.capacity()];
			float[] yData = new float[yDataArray.capacity()];
			
			for(int i = 0; i < xCount ; i++){
				xData[i] = xDataArray.get(i);
			//	System.out.println("xx=" + xData[i]);
			}
				
			for(int i = 0; i < yCount; i++){
				yData[i] = yDataArray.get(i);
			//	System.out.println("yy=" + yData[i]);
			}

			modelerGui.setDataFromFile(xData, yData);
			
			
		
		//	modelerGui.syncData();
		}

		if(evt.getSource() == copyButton) {
			//initClicked = false;
			//reinitMixtureModel = false;

			String cpBuff = "";
			if(modelerGui.tabbedPanelContainer.getSelectedIndex()==0) {
				int[] cpRows =  modelerGui.dataTable.getSelectedRows();
				int[] cpCls =  modelerGui.dataTable.getSelectedColumns();
				if(cpCls.length<1 || cpRows.length<1) {
				OKDialog OKD = new OKDialog(null, true, "Make a selection to copy data");
				OKD.setVisible(true);
			} else {
				//   String cpBuff = "";
				for(int i = 0;i<cpRows.length;i++) {
					for(int j = 0;j<cpCls.length;j++) {
						if(modelerGui.dataTable.getValueAt(cpRows[i],cpCls[j])==null)
							cpBuff = cpBuff + " \t";
						else
							cpBuff = cpBuff + modelerGui.dataTable.getValueAt(cpRows[i],cpCls[j]) + "\t";
					} // end for j
					cpBuff = cpBuff.substring(0,cpBuff.length()-1) + "\n";
				} // end for i
			} // end  inner else

		     }else{
				if(modelerGui.tabbedPanelContainer.getSelectedIndex() ==2)
					if(modelerGui.resultPanelTextArea.getSelectedText() !=null)
						cpBuff = modelerGui.resultPanelTextArea.getSelectedText();
					}
					//modelerGui.resultPanelTextArea.append(cpBuff);
					try{
						StringSelection stTran = new StringSelection(cpBuff);
						clipboard.setContents(stTran,stTran);
						}catch(Exception e) {}


			} // end if copyButton


	     if(evt.getSource() == pasteButton) {
	    	// System.out.println("SOCRmodeler PASATE called");
			//initClicked = false;
			//reinitMixtureModel = false;
			int crtRow = modelerGui.dataTable.getSelectedRow();
			int crtCl = modelerGui.dataTable.getSelectedColumn();

			if(crtRow == -1) {
				OKDialog OKD = new OKDialog(null, true, "Select a row to paste data");
				OKD.setVisible(true);
			}else{
				try {
					clipboard.getContents(this);
					DataFlavor[] flavs = clipboard.getContents(this).getTransferDataFlavors();
					String tabData = clipboard.getContents(this).getTransferData(flavs[0].stringFlavor).toString();
					StringTokenizer lnTkns = new StringTokenizer(tabData,"\n");

					String line;
					int lineCt = lnTkns.countTokens();
					if(crtRow+lineCt > modelerGui.dataTable.getRowCount())
					modelerGui.dataTable.appendTableRows(crtRow+lineCt - modelerGui.dataTable.getRowCount());
					int r = 0;
					while(lnTkns.hasMoreTokens()) {

						line = lnTkns.nextToken();
						StringTokenizer strTok = new StringTokenizer(line,"\t\r\f");
						// StringTokenizer strttt = new StringTokenizer(
						int c = 0;
						while(strTok.hasMoreTokens()) {
							//resultPanelTextArea.append(strTok.nextToken());
							modelerGui.dataTable.setValueAt(strTok.nextToken(),crtRow+r,c+crtCl);
							c++;
						} // end while strTok
						r++;
					} // end while lnTkns
				} catch(Exception e){
					OKDialog OKD = new OKDialog(null, true, "Unalbe to paste. Check the datatype.\n" +			"Number of columns from PASTE data cannot exceed number of columns in Table");
					OKD.setVisible(true);
					e.printStackTrace();
				}

			}
			modelerGui.syncData();
		} // end if pasteButton
		 // added by annie che 20060408. with error pop-up removed.
		/*if (evt.getActionCommand().equals(ABOUT)) {
			//initClicked = false;
			//reinitMixtureModel = false;
			//JOptionPane.showMessageDialog(this, this.getLocalHelp());
		}*/

		if (evt.getActionCommand().equals(HELP)) {
			try {
				getAppletContext().showDocument(
					new java.net.URL(modelerGui.getOnlineDescription()),
					   "SOCR Modeler Online Help");
			} catch (MalformedURLException e) {
				////JOptionPane.showMessageDialog(this, e.getMessage());
				e.printStackTrace();
			}
		} else if (evt.getActionCommand().equals(ABOUT)) {
			try {
				getAppletContext().showDocument(
					new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/About_pages_for_SOCR_Modeler"),
					   "SOCR Modeler About");
			} catch (MalformedURLException e) {
				////JOptionPane.showMessageDialog(this, e.getMessage());
				e.printStackTrace();
			}
		} else if (evt.getActionCommand().equals(SNAPSHOT)) {
		      SwingUtilities.invokeLater( new Runnable() {
		          public void run() {
		                java.awt.image.BufferedImage image = capture();

		                if (jfc==null) jfc = new JFileChooser();
		  			  else jfc.setVisible(true);
		        		  int option = jfc.showSaveDialog(null);
		  			  java.io.File f = jfc.getSelectedFile();
		  			  jfc.setVisible(false);

		  			  //System.out.println("image " + image);
		  			  // Fix the problems with users not entering proper file extensions (.gif or .jpg)
		                if (!f.getName().endsWith(".jpg"))
		  				 f = new java.io.File(f.getAbsolutePath()+ ".jpg");

		                String type =
		  				f.getName().substring(f.getName().lastIndexOf('.') + 1);
		             //   System.out.println("type " + type);
		                try {
		                  	      javax.imageio.ImageIO.write(image,type,f);
		                } catch (java.io.IOException ioe) {
		                  	      ioe.printStackTrace();
		          	      	      JOptionPane.showMessageDialog(null, ioe,
		          	      	    		  "Error Writing File",JOptionPane.ERROR_MESSAGE);
		                 }
		              }
		           });
		} // end if snapshot
		else if(source==zoomIn) {
			//initClicked = false;
			//reinitMixtureModel = false;

			////System.out.println("SOCRModeler actionPerformed zoomIn bins.getValue() = " + bins.getValue());

			// arg = false means it's not just positive but both pos and neg in x doamin.
			/*
			if (modelerGui.modelObject.getModelType() == Modeler.FOURIER_TYPE) {
				////System.out.println("SOCRModeler zoomIn is FOURIER");
				modelerGui.zoomIn(bins.getValue(), true);
			} else {
				modelerGui.zoomIn(bins.getValue(), false);
			}*/
			modelerGui.zoomIn((int)binsSlider.getFloatValue(), false);

		}
		else if(source==zoomOut) {
			//initClicked = false;
			//reinitMixtureModel = false;

			////System.out.println("SOCRModeler actionPerformed zoomOut bins.getValue() = " + bins.getValue());
			/*if (modelerGui.modelObject.getModelType() == Modeler.FOURIER_TYPE) {
				////System.out.println("SOCRModeler zoomIn is FOURIER");
				modelerGui.zoomOut(bins.getValue(), true);
			} else {
				modelerGui.zoomOut(bins.getValue(), false);
			}*/
			modelerGui.zoomOut((int)binsSlider.getFloatValue(), false);
			//bins.setValue(bins.getValue()-2);
		}
	/*	else if(source==usePosX) {
			//initClicked = false;
			////System.out.println("SOCRModeler actionPerformed usePosX");
			modelerGui.usePosX();
			//bins.setValue(bins.getValue()-2);
		}
		else if(source==usePosNegX) {
			//initClicked = false;
			////System.out.println("SOCRModeler actionPerformed usePosNegX");
			modelerGui.usePosNegX();
			//bins.setValue(bins.getValue()-2);
		}*/
		else if(source==reinit) {
			//System.out.println("SOCRModeler actionPerformed reinit is clicked");
			initClicked = true;
			rescaleClicked = false;
			modelerGui.fitC(rescaleClicked, rescale.isSelected(), initClicked);//reinit.isSelected());
	    } 
		else if(source==rescale) {
			//System.out.println("SOCRModeler actionPerformed rescale is clicked");
			initClicked = false;
			rescaleClicked = true;

			modelerGui.fitC(rescaleClicked, rescale.isSelected(), initClicked);//reinit.isSelected());
	    } /*else {
			initClicked = false;
			rescaleClicked = false; }*/
	}

	protected void itemChanged(String className) {
		try {
			//modelerGui.modelObject = (Modeler)Class.forName(className).newInstance();
			//clearvalueSetterPane();
			choicePanel.removeAll();
			choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
			choicePanel.setBorder(titleBorder);
			Object[] obj = new Object[1];
			obj[0] = choicePanel;

			// Modeler object is alive now.

			modelerGui.modelObject = (Modeler)Class.forName(className).getConstructor(new Class[] {JPanel.class}).newInstance(obj);

			modelerGui.modelObject.registerObservers(observablewrapper);
			observablewrapper.addObserver(modelerGui);
			//////System.out.println("SOCRModeler itemChanged rescale.isSelected() = " + rescale.isSelected());
			//////System.out.println("SOCRModeler itemChanged modelerGui.modelObject.useInitButton = " + modelerGui.modelObject.useInitButton());

			// add button if the model needs to have initial value reset. annie che.
			//useInitButton = modelerGui.modelObject.useInitButton();
			if (modelerGui.modelObject.useInitButton()) {
				choicePanel.add(reinit);
				reinit.setBackground(ModelerColor.WHITE);
				reinit.addActionListener(this);

				observablewrapper.addJCheckBox(rescale);

				choicePanel.repaint();
			}
			//System.out.println("SOCRModeler itemChanged rescaleClicked = " + rescaleClicked + ", rescale.isSelected() = " + rescale.isSelected() +", initClicked = " + initClicked);

			// 1 = DISTRIBUTION, 101 = FOURIER, 102 = WAVELET

			modelerGui.fitC(rescaleClicked, rescale.isSelected(), initClicked);//reinit.isSelected());
			// these are for Plotting only
			double lowerLimit = modelerGui.modelObject.getLowerLimit();
			double upperLimit = modelerGui.modelObject.getUpperLimit();
		//	System.out.println("before SOCRModeler itemChanged class = " + className + " model's lowerLim = " + lowerLimit + " upperLim= " + upperLimit);

			
			if (lowerLimit == Double.NEGATIVE_INFINITY) {
				//	lowerLimit = ModelerConstant.GRAPH_LOWER_LIMIT;
				lowerLimit= -xScaleSlider.getFloatValue();  //Jenny
			}
			if (upperLimit == Double.POSITIVE_INFINITY) {
				//	upperLimit = ModelerConstant.GRAPH_UPPER_LIMIT;
				upperLimit= xScaleSlider.getFloatValue();  //Jenny
			}
			if(debug)
				System.out.println("after SOCRModeler itemChanged class = " + className + " model's lowerLim = " + lowerLimit + " upperLim= " + upperLimit);

				
				//modelerGui.setUpperLimit(upperLimit);
				//modelerGui.setLowerLimit(lowerLimit);
			modelerGui.setxMax(upperLimit);
			updateXScaleSlider(upperLimit);
			modelerGui.setxMin(lowerLimit);
			
			//////System.out.println("SOCRModeler itemChanged class = " + className + " gui's lowerLim = " + lowerLimit);

			String iText = "Description: \n" + modelerGui.modelObject.getDescription()+ "\n\nInstructions: \n"  + modelerGui.modelObject.getInstructions() + "\n\nReferences: \n" + modelerGui.modelObject.getResearch();
			//modelerGui.infoPanelTextArea.setBorder(new BevelBorder(BevelBorder.RAISED));
			////////System.out.println("SOCRModeler itemChanged iText = " + iText);

			if(modelerGui.infoPanelTextArea!=null)
				modelerGui.infoPanelTextArea.setText(iText);
			addJPanel(choicePanel);
			choicePanel.validate();
			choicePanel.repaint();

		} catch (Throwable e) {
			//JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
			e.printStackTrace();
		}
    }

	public void itemStateChanged(ItemEvent evt) {
		//System.out.println("SOCRModler itemStateChanged evt.getSource() = " + evt.getSource());
	
		if(evt.getSource() == rawData) {
		//	System.out.println("SOCRModler itemStateChanged evt.getSource() = rawData");
			if(rawData.isSelected())
				modelerGui.setRawData(true);
			else 
				modelerGui.setRawData(false);
			
			modelerGui.fitC();
			setColumns();
			modelerGui.syncMouseData();
		}
	}

/*	public void adjustmentValueChanged(AdjustmentEvent e) {
		if(e.getSource()==binsSlider) {
			////System.out.println("SOCRModeler adjustmentValueChanged bins bins.getValue() = " + bins.getValue() );
			modelerGui.setBins((int)binsSlider.getFloatValue());
		}
		if(e.getSource() == xScaleSlider) {
			try {	// at first modelObject is null (not initialized yet.)
				if (modelerGui.modelObject.getModelType() == Modeler.FOURIER_TYPE ||modelerGui.modelObject.getModelType() == Modeler.WAVELET_TYPE) {
					////System.out.println("SOCRModeler zoomIn is FOURIER/WAVELET");
					modelerGui.setXScale(xScaleSlider.getFloatValue());
					//modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
				} else {
					modelerGui.setXScale(xScaleSlider.getFloatValue());
				//	modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
					//	May be NOT: modelerGui.setXScale((int)modelerGui.graph.getMinX(), true);
					//	modelerGui.graph.setxMin(modelerGui.graph.getMinX());
					//	modelerGui.graph.setxMax(modelerGui.graph.getMaxX());
				}
			} catch (Exception ex) {
				modelerGui.setXScale(xScaleSlider.getFloatValue());
					//modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
			}
		}
		if(e.getSource() == yScaleSlider) {
			modelerGui.setYScale(yScaleSlider.getFloatValue());
			//modelerGui.setYScale(yScale.getMaximum()-yScale.getValue());
		     ////System.out.println("SOCRModeler adjustmentValueChanged yScale.getMaximum() = " + yScale.getMaximum());
		     //////System.out.println("SOCRModeler adjustmentValueChanged yScale.getValue() = " + yScale.getValue());
		}		////System.out.println("SOCRModeler adjustmentValueChanged finsih");
	}

	public void stateChanged(ChangeEvent e) {

		if(e.getSource()==binsSlider) {
			modelerGui.setBins((int)binsSlider.getFloatValue());
		}
		if(e.getSource() == xScaleSlider) {
			if (modelerGui.modelObject.getModelType() == Modeler.FOURIER_TYPE) {
				////System.out.println("SOCRModeler zoomIn is FOURIER xScale.getValue() = " + xScale.getValue());
				modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), true);
			} else {
				modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
			}
			//modelerGui.setXScale(xScale.getMaximum()-xScale.getValue(), false);
			modelerGui.setXScale(xScaleSlider.getFloatValue());
		}
		if(e.getSource() == yScaleSlider) {
			//modelerGui.setYScale(yScale.getMaximum()-yScale.getValue());
			modelerGui.setYScale(yScaleSlider.getFloatValue());
		}

	}*/

	public void updateXScaleSlider(double v){
		calledByModelerGui= 2;
		xScaleSlider.setFloatValue(v);
	}
	public void updateYScaleSlider(double v){
		calledByModelerGui= 2;
		yScaleSlider.setFloatValue(v);
	}
	public void updateBinsScaleSlider(int v){
		binsSlider.setFloatValue(v);
	}
	
    public void update(Observable o, Object arg) {
     //  if(calledByModelerGui== 0){
    //	  System.out.println("--- not calledByModelerGui---");
    //	  Exception e = new Exception();
    //	  e.printStackTrace();
    //	  System.out.println("------");
    	  
    //  }
    	if (arg==null||o==null)
    		return;
    	
    	// System.out.println("SOCRModeler update called scaleSlider updated calledByModelerGui="+calledByModelerGui);
    	   
    		if(arg.equals(binsSlider)) {
    		//	System.out.println("SOCRModeler update bins  = " + binsSlider.getFloatValue() );
    			//if(!callFromGui) //mouse dragged
    			if(modelerGui.histBinNos!=binsSlider.getFloatValue())
    				modelerGui.setBins((int) binsSlider.getFloatValue());
    		//	else callFromGui = false;
    			return;
    		}
    	
    		if(arg.equals(xScaleSlider)) {
    			if(calledByModelerGui>0){
    				calledByModelerGui--;
    			//	System.out.println("xScaleSlider: turn calledByModelerGui"+calledByModelerGui);
    				
    				return;
    			}
    			try {	// at first modelObject is null (not initialized yet.)
    				//System.out.println("SOCRModeler update xSlider  = " + xScaleSlider.getFloatValue() );
    				if(modelerGui.getXScaleMax()!=(int)xScaleSlider.getFloatValue()){
    					if (modelerGui.modelObject.getModelType() == Modeler.FOURIER_TYPE ||modelerGui.modelObject.getModelType() == Modeler.WAVELET_TYPE) {
    					////System.out.println("SOCRModeler zoomIn is FOURIER/WAVELET");
    						modelerGui.setXScale(xScaleSlider.getFloatValue(), false);
    					} else {
    						modelerGui.setXScale(xScaleSlider.getFloatValue(), false);
    					}
    				}
    			} catch (Exception ex) {
    					modelerGui.setXScale(xScaleSlider.getFloatValue(), false);
    			}
    			return;
    		}
    		
    		if(arg.equals(yScaleSlider)) {
    			if(calledByModelerGui>0){
    				
    				calledByModelerGui --;
    			//	System.out.println("yScaleSlider: turn calledByModelerGui"+calledByModelerGui);
    				return;
    			}
    			if(modelerGui.getYScaleMax()!=(int)yScaleSlider.getFloatValue())
    				modelerGui.setYScale(yScaleSlider.getFloatValue());
    			return;
    		}		///
    		
    		System.out.println("SOCRModeler update calling ModelerGui.syncData()");
		modelerGui.syncData();
    	//	modelerGui.fitC(rescaleClicked, rescale.isSelected(), false);//reinit.isSelected());
 	 }

    public Object getCurrentItem(){return this;}

    public void setColumns() {
	    if (rawData.isSelected()) {
    		clm2 = modelerGui.dataTable.getColumn(ModelerGui.FREQUENCY_COLUMN_NAME); //testColumn_2
    	    modelerGui.dataTable.removeColumn(clm2);

    	} else {
    		modelerGui.dataTable.addColumn(clm2);
    	}

    }

	/*private void loadFileData(){
		try{
			file = new File(FileLocate.getDirectory(),FileLocate.getFile());
               //////System.out.println(file.getAbsolutePath());
               //fileSelected.setText(Boolean.toString(file.canRead()));
               //fileSelected.repaint();
               Fileip = new FileInputStream(file);
               // Fileip.
               int eof = 0;
               int i = 0;
               int j = 0;
               eof = Fileip.read();

               while(eof!=-1) {
				if(eof == 59 || eof == 44 || eof == 9){//44, 59; 9 tab 
					modelerGui.dataTable.setValueAt(buffer, i,j);
					if(j==1)
						j=0;
					else
						j=1;
					buffer = "";
				} else {
					if(eof == 13 ) {
						modelerGui.dataTable.setValueAt(buffer, i,j);
						buffer = "";
						Fileip.read();
						j=0;
						i=i+1;
						if(i == modelerGui.dataTable.getRowCount()) {
							tModel = (DefaultTableModel)modelerGui.dataTable.getModel();
							tModel.addRow(new Vector(2));
							modelerGui.dataTable.setModel(tModel);

						}
					}
					else{
						buffer = buffer + Character.toString((char) eof);
					}
				}
				eof = Fileip.read();
			} // end while

			if(modelerGui.dataTable.getColumnCount()==1)
				j=0;
			else
				j=1;

			modelerGui.dataTable.setValueAt(buffer, i,j);
			buffer = "";
			Fileip.close();
			modelerGui.fitC(rescaleClicked, rescale.isSelected(), initClicked);//reinit.isSelected());

		}catch(Exception e) {
			modelerGui.resultPanelTextArea.setText(e.getMessage());
			OKDialog fileOK = new OKDialog(new JFrame(),true,e.getMessage());
			fileOK.setVisible(true);
		}
	}*/
    
    public void appendTableRows(JTable dataTable,  JTable headerTable, int n) {
        int ct = dataTable.getColumnCount();
        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
        for(int j=0;j<n;j++) 
                  tModel.addRow(new java.util.Vector(ct));  
        dataTable.setModel(tModel);
        TableColumnModel columnModel = dataTable.getColumnModel();
        dataTable.setTableHeader(new EditableHeader(columnModel));
        
        javax.swing.table.DefaultTableModel hModel = (javax.swing.table.DefaultTableModel) headerTable.getModel();
        int rowCount= hModel.getRowCount();
        for(int j=0;j<n;j++) {
            hModel.addRow(new Object[] {(rowCount+j+1)+":"});  
        }
        headerTable.setModel(hModel);
    }

    
    private void loadFileData(JTable table, JTable headerTable){
    
    	BufferedReader reader = null;

        int row = 0;	//row
        int col = 0;  	//column
        //int ct= table.getColumnCount();
        String line = "";
        
        //show warning if user supply raw data without checking the raw data option
        boolean showWarning = false;

        	//what is this used for??
        //only the first line started with # will be treated as heading
        //boolean heading = false;

		try{
			file = new File(FileLocate.getDirectory(),FileLocate.getFile());
            reader = new BufferedReader(new FileReader(file), BUF_SIZE);
                      
    		//System.out.println("file length = " + file.length());
    		   
            while( (line = reader.readLine()) != null){
        		String num1 = "";
        		String num2 = "";
        		//read a line at a time
        		int i = 0;
        		while(i < line.length()){
            		char ch = line.charAt(i);
            		
            		if(ch == ','){
            			i++;
            			
            			if(table.getColumnCount() > 1){
	            			//extract 2nd column
	            			for(; i < line.length(); i++){
	            				ch = line.charAt(i);
	            				num2 += ch;
	            			}
            			}
            			break;
            		}
            		
            		num1 += ch;
            		i++;
        		}
            	//put value into table
            	
            	//if not rawData
    			if(table.getColumnCount() > 1){
                	table.setValueAt(num1, row, col);
                	xDataArray.add(Float.parseFloat(num1));
                	xCount++;

                	//make sure file contains data for both columns
    				if(num2.length() != 0){
	    				col=1;
		            	table.setValueAt(num2, row, col);
		            	yDataArray.add(Float.parseFloat(num2));
		            	yCount++;
		            	col=0;
    				}
    				else
    					showWarning = true;
    			}
    			else{
    				//is raw Data
                	table.setValueAt(num1, row, col);
                	yDataArray.add(Float.parseFloat(num1));
                	yCount++;
    			}
    				
            	
            	//if not enough row in table, add more rows
            	if(row == (table.getRowCount()-1))
            		appendTableRows(table, headerTable, 10);
            	
            	row++;
            	
            }
               
            reader.close();
            
            if(showWarning){
				//give warning
				String str = "Please check the Raw Data option if file contains only raw data, e.g." +
						" frequency only.";
	        	OKDialog fileOK = new OKDialog(new JFrame(), true, str);
	        	fileOK.setVisible(true);
            }
            
            //fill extra rows with blank
            for(; row < table.getRowCount(); row++)
            	for(col = 0; col < table.getColumnCount(); col++)
            		table.setValueAt("", row, col);
		
		}catch(Exception e) {
			e.printStackTrace();
            	OKDialog fileOK = new OKDialog(new JFrame(),true,"Exception caught");
            	fileOK.setVisible(true);
            }
	}



// added by annie che 20060408.
// The following 1 method's verbatim are directly copied from Analysis.
	public String getOnlineDescription(){
		return new String("http://socr.stat.ucla.edu/");
	}

// The following 2 methods' verbatim are directly copied from Chart.

	public String getLocalHelp() {
		return "Introduction: The SOCRChart applet is a collection of data plotting tool, and it allows you to:\n"+
		"\t1. Visualize your own data graphically.\n"+
		"\t2. Generate some statistical plots such as:\n\t    StatisticalBarChart(BoxPlot), StatisticalLineChart(ScatterPlot), NormalDistributionPlot, BoxAndWhiskerChart.\n\n"+
	      "How to use the SOCRChart Applets:\n"+
		"\t1. Select a chart from the SOCRChart list from the left control panel, a demo of the selected chart type will be\n\t    shown in the right display panel.\n"+
		"\t2. Click on the \"DATA\" tab in the right display panel to view the data used in the demo in a table form. \n\t   The data and the headings are all editable for you to enter your own data. \n"+
		"\t3. For each chart type, the \"MAPPING\" is set for the demo data.  You can make change as needed using the \n\t   \"Add\"/\"Remove\" button.\n"+
		"\t4. After making data/mapping changes, click on the \"UPDATE_CHART\" button to get the plot updated.\n"+
		"\t5. Click the \"DEMO\" button will reset everything to the demo state.\n"+
		"\t6. Click the \"CLEAR\" button will clear all for you to start entering your own data.\n\n"+
	     "Notes: \n"+
	    "\t1.You can select table cells and use the \"COPY\"/\"PASTE\" button to copy/paste data in the data table.\n"+
	    "\t2.The \"SNAPSHOT\" button can be used to save a snapshot of the graph to your own computer.\n"+
		"\t3.To add a extra row to the data table, hit \"enter\" key in the last cell. Hit \"tab\" key in the last cell will add a extra \n\t   column.\n"+
		"\t4. To report bugs or make recommendations please visit: http://www.socr.ucla.edu/\n" +
	    "\n SOCRChart is based on JFreeChart and uses it's rendering engine to render the chart image. See www.jfree.org/jfreechart/ \n \t   for more information."
		;
	}

	private java.awt.image.BufferedImage capture() {

		java.awt.Robot robot;

		try {
			robot = new java.awt.Robot();
		} catch (java.awt.AWTException e) {
			throw new RuntimeException(e);
		}
		java.awt.Rectangle screen = this.getContentPane().getBounds();
		java.awt.Point loc = screen.getLocation();
		SwingUtilities.convertPointToScreen(loc,this.getContentPane());

		screen.setLocation(loc);
		return robot.createScreenCapture(screen);
	}
}
