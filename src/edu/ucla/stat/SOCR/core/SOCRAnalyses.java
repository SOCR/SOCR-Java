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
/*
 * Created on Nov 30, 2004
 * Modified annieche 200508
 */

package edu.ucla.stat.SOCR.core;

import edu.ucla.stat.SOCR.analyses.gui.Analysis;
import edu.ucla.stat.SOCR.analyses.gui.AnalysisPanel;
import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.util.EditableHeader;

import java.awt.*;
//import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

/**
 * @author  <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class SOCRAnalyses extends SOCRApplet implements ActionListener {

    /**
	 * @uml.property  name="analysisPanel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    //right side pane

	private JSplitPane container;
	private JPanel rightPanel;
	private edu.ucla.stat.SOCR.analyses.gui.AnalysisPanel graphPanel;
	private SOCRTextArea statusTextArea = new SOCRTextArea();
    /**
     *
     * @uml.property name="analysis"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private edu.ucla.stat.SOCR.analyses.gui.Analysis analysis;

    public static String ABOUT ="ABOUT";
    public static String SNAPSHOT ="SNAPSHOT";
    public static String HELP ="HELP";
    public static String COPY ="COPY";
    public static String FILE = "FILE OPEN";
    public static String PASTE = "PASTE";
    public static String FORMAT = "FORMAT";
    
    public static String FORMAT001 = "0.001";
    public static String FORMAT00001 = "0.00001";
    public static String FORMATALL= "All";
    protected int numberOfFormat;
    String[] formatArray ={FORMAT001,FORMAT00001, FORMATALL};
    
    public static int PRESENT_PANEL_DEFAULT_WIDTH =700;
    public static int PRESENT_PANEL_DEFAULT_HEIGHT =700; // adjust the right panel

//for File Open button
    public File file;
	public FileInputStream Fileip;
	public FileDialog FileLocate;
	public Frame fDialog = new Frame();
	
    JFileChooser jfc;
    public Clipboard clipboard;
    public javax.swing.table.DefaultTableModel tModel;


    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.gui.SOCRApplet#getCurrentItem()
     */
    public Object getCurrentItem() {
        return analysis;
    }
	public void init()  {
		super.init();

	}
    public void initGUI() {
		controlPanelTitle = "SOCR Analyses";
		implementedFile = "implementedAnalysis.txt";

		try {
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		} catch (Exception e) {
			System.out.println("SOCRAnalyses:initGUI(): No Security Access!");
		}

//		 comment or uncomment to activate the buttons.
		addButton2(ABOUT, "Find Details about this Type of Statistical Analysis", this);
		addButton2(HELP, "Help with This type of Analysis", this);
		addButton2(SNAPSHOT, "Save a Snapshot/Image of this SOCRAnalysis Applet", this);
		addButton(COPY, "Copy data from table to mouse buffer", this);
		addButton(PASTE, "Paste in Data", this);
		addButton(FILE, "Open File", this);
		// end buttons.
		
		//formatArray = analysis.getFormatArray();  // analysis is null at this point;
		numberOfFormat =formatArray.length; 
		addRadioButton("Result RoundOff:", "Choose roundoff", formatArray, 0, this);
	
		
		graphPanel = new edu.ucla.stat.SOCR.analyses.gui.AnalysisPanel(this);
		container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		graphPanel, new JScrollPane(statusTextArea) );
		fPresentPanel.setViewportView(container);
		fPresentPanel.setPreferredSize(new Dimension(PRESENT_PANEL_DEFAULT_WIDTH,PRESENT_PANEL_DEFAULT_HEIGHT));
    }

    protected void itemChanged(String className) {
        try {
		  analysis = edu.ucla.stat.SOCR.analyses.gui.Analysis.getInstance(className);
		  analysis.init();
		  resetRadioButton(0);
		  fPresentPanel.setViewportView(analysis.getDisplayPane());
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
            e.printStackTrace();
        }
    }
    public void actionPerformed(ActionEvent evt) {
	    	//System.out.println("SOCRAnalyses actionPerformed");
	    	for (int i=0; i<numberOfFormat; i++)
	    		if (evt.getActionCommand().equals(formatArray[i])) {
	    			analysis.setFormat(formatArray[i]);	    		
	    	}
	    	
	    	if (evt.getActionCommand().equals(ABOUT)) {
	    	System.out.println("SOCRAnalyses actionPerformed evt.getActionCommand().equals(ABOUT");
            try {
                getAppletContext().showDocument(
                new java.net.URL(analysis.getOnlineDescription()),
                   "SOCR: Analysis Online Help (Mathematica)");
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                e.printStackTrace();
            }
        }
	    else if (evt.getActionCommand().equals(HELP)) {
	    	System.out.println("SOCRAnalyses actionPerformed evt.getActionCommand().equals(HELP");
		try {
			getAppletContext().showDocument(
			new java.net.URL(analysis.getOnlineHelp()),
			   "SOCR: Analysis Online Help (Mathematica)");
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
        }
	else if (evt.getActionCommand().equals(FILE)) {

		FileLocate = new FileDialog(fDialog);
		FileLocate.setVisible(true);
		fDialog.setVisible(true);
		if(FileLocate.getFile()!=null)
			loadFileData(analysis.dataTable, analysis.headerTable);
		
		FileLocate.dispose();
		fDialog.dispose();
  }
	else if (evt.getActionCommand().equals(SNAPSHOT)) {
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
              System.out.println("type " + type);
              try {
                	      javax.imageio.ImageIO.write(image,type,f);
              } catch (java.io.IOException ioe) {
                	      ioe.printStackTrace();
        	      	      JOptionPane.showMessageDialog(null, ioe,
        	      	    		  "Error Writing File",JOptionPane.ERROR_MESSAGE);
               }
            }
         });
     }
	else if (evt.getActionCommand().equals(COPY)) {
	    //	System.out.println("SOCRAnalyses actionPerformed evt.getActionCommand().equals(COPY");

		String cpBuff = "";
              	if(analysis.tabbedPanelContainer.getSelectedIndex()==0) {
                  	int[] cpRows =  analysis.dataTable.getSelectedRows();
                  	int[] cpCls =  analysis.dataTable.getSelectedColumns();
                  	if(cpCls.length<1 || cpRows.length<1) {
                      		OKDialog OKD = new OKDialog(null,
					true, "Make a selection to copy data");
                      		OKD.setVisible(true);
                  	} else {
                        for(int i = 0;i<cpRows.length;i++) {
                          for(int j = 0;j<cpCls.length;j++) {
                              if(analysis.dataTable.getValueAt(cpRows[i],cpCls[j])==null)
                                  cpBuff = cpBuff + " \t";
                              else
                                 cpBuff = cpBuff +
				 analysis.dataTable.getValueAt(cpRows[i],cpCls[j]) + "\t";
                          }
                          cpBuff = cpBuff.substring(0,cpBuff.length()-1) + "\n";
                      }
                  }

		  try{
                      StringSelection stTran = new StringSelection(cpBuff);
                      clipboard.setContents(stTran,stTran);

                  }catch(Exception e) {}
              }
        }
	else if (evt.getActionCommand().equals(PASTE)) {
	    int crtRow = analysis.dataTable.getSelectedRow();
	    int crtCl;
	    if (crtRow>=0)
	    	crtCl= analysis.dataTable.getSelectedColumn();
	    else crtCl = analysis.getSelectedHeaderColumn();
	 
	  // System.out.println("Inside PASTE! crtRow ="+crtRow+ " crtCl="+crtCl);
				
	    if(crtRow == -1) {  //table header 
	    	analysis.resetMappingList(); // in order to deselect the header cell 
	    	
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
	    		//System.out.println("analysis.dataTable.getColumnCount()="+(analysis.dataTable.getColumnCount()));
	    		if(crtCl+cellCnt> analysis.dataTable.getColumnCount()){
	    			analysis.appendTableColumns(crtCl+cellCnt- analysis.dataTable.getColumnCount()+1);
	    		
	    		//	System.out.println("append column"+ (crtCl+cellCnt+1- analysis.dataTable.getColumnCount()+1));
	    		}
	    			                    		
	    		TableColumnModel columnModel= analysis.dataTable.getColumnModel();
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
	    			if(crtRow+lineCt > analysis.dataTable.getRowCount())
	    				analysis.appendTableRows(crtRow+lineCt - analysis.dataTable.getRowCount());
	    			
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
	    				if(crtCl+colCt > analysis.dataTable.getColumnCount())
	    					analysis.appendTableColumns(crtCl+colCt - analysis.dataTable.getColumnCount());
	    				for (int i=0; i<tb.length; i++){
	    					//System.out.println(tb[i]);
	    					if (tb[i].length()==0)
	    						tb[i]="0";
	    					analysis.dataTable.setValueAt(tb[i],crtRow+r,i+crtCl);
	    					//System.out.println("setting "+tb[i]+" at "+(crtRow+r)+","+(i+crtCl));
	    				}
	    				
	    				r++;
	    			}  
	    		}
	    	
	    		analysis.updateEditableHeader(columnModel);

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
                    if(crtRow+lineCt > analysis.dataTable.getRowCount())
                    	analysis.appendTableRows(crtRow+lineCt - analysis.dataTable.getRowCount());
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
                    	if(crtCl+colCt > analysis.dataTable.getColumnCount())
                           	analysis.appendTableColumns(crtCl+colCt - analysis.dataTable.getColumnCount());
                   
                    	
                    	//System.out.println("SOCRChart columnCount="+analysis.dataTable.getColumnCount());
                    	
                        for (int i=0; i<tb.length; i++){
                        	//System.out.println(tb[i]);
                        	if (tb[i].length()==0)
                        		tb[i]="0";
                        	analysis.dataTable.setValueAt(tb[i],crtRow+r,i+crtCl);
                        }
                        
                    	r++;
                    }  
                    // this will update the mapping panel
                  
        //crop or not ?????
               /*     analysis.resetTableColumns(max_col+1);
                    analysis.resetTableRows(crtRow+r+1);*/
                    
                    analysis.resetMappingList();
                    
                    TableColumnModel columnModel= analysis.dataTable.getColumnModel();  
                    analysis.updateEditableHeader(columnModel);
                  } 
	      catch(Exception e){
                    OKDialog OKD = new OKDialog(null, true, 
			"Unalbe to paste. Check the datatype.\n");
                    OKD.setVisible(true);
                    e.printStackTrace();
                  }       
	    
                }	//else  *** if(crtRow != -1) 
	    
    } // END of last "else if"
    }

    private void loadFileData(JTable table, JTable headerTable){
    	String buffer = "";
    	ArrayList<String> headingName = new ArrayList<String>();
		try{
			 file = new File(FileLocate.getDirectory(),FileLocate.getFile());
               //////System.out.println(file.getAbsolutePath());
               //fileSelected.setText(Boolean.toString(file.canRead()));
               //fileSelected.repaint();
               Fileip = new FileInputStream(file);
               // Fileip.
               int eof = 0;
               String ch;
               int i = 0;
               int j = 0;
               int ct= analysis.dataTable.getColumnCount();
               //only the first line started wiht # will be treated as heading
               boolean heading = false;
              
               eof = Fileip.read();
               ch=Character.toString((char) eof);
               

               while(eof!=-1) {
            	   
            	 // System.out.println("eof="+ eof+":"+Character.toString((char) eof));
            	 
            	  if (eof ==35){
            		 // System.out.println("setting heading = true;");
            		  eof = Fileip.read();
            		  heading = true;
            		  continue;
            	  }
            	  else if(eof == 59 || eof == 44 || eof == 9 ){//44, 59; 9 tab 
					//System.out.println("setting buffer "+buffer+" at "+i+","+j);
					if(j==ct){
						/*tModel = (DefaultTableModel)table.getModel();	
						tModel.addColumn("C"+(ct+1),new java.util.Vector(ct)); 
						table.setModel(tModel);
						TableColumnModel columnModel = table.getColumnModel();
						table.setTableHeader(new EditableHeader(columnModel));*/
						analysis.appendTableColumns(1);
						ct++;
					}
					if (heading){
						headingName.add(buffer);
						//System.out.println("adding heading " +j +" :"+buffer);
						TableColumnModel columnModel = table.getColumnModel();
						columnModel.getColumn(j).setHeaderValue(buffer);
						table.setTableHeader(new EditableHeader(columnModel));
					}			
					else {
						if (i == (table.getRowCount()-1)){							
							appendTableRows(table, headerTable, 10);						    
						}
						table.setValueAt(buffer, i,j);
					}
					j++;
					buffer = "";
            	   } 
            	   else if(eof == 32){//space
            		   eof = Fileip.read();
            		   continue;
            	   }
            	   else {
            		   if(eof == 13 || eof ==10 ) { //return mac 13, dos 13+10
						//System.out.println("setting buffer "+buffer+" at "+i+","+j);
            			  
							if(j==ct){
								/*tModel = (DefaultTableModel)table.getModel();	
								tModel.addColumn("C"+(ct+1),new java.util.Vector(ct)); 
								table.setModel(tModel);
								TableColumnModel columnModel = table.getColumnModel();
								table.setTableHeader(new EditableHeader(columnModel));*/
								analysis.appendTableColumns(1);
								ct++;
							}
							if (heading==true){
								headingName.add(buffer);
							//	System.out.println("adding last heading " +j +" :"+buffer);
								TableColumnModel columnModel = table.getColumnModel();
								columnModel.getColumn(j).setHeaderValue(buffer);
								table.setTableHeader(new EditableHeader(columnModel));
							}								
							else {
								table.setValueAt(buffer, i,j);
								i++;
							}
							
							buffer = "";
							if (heading==true){
	            				 //  System.out.println("setting heading = false;");	   
	            				   heading = false;
							}
							
						for (j++; j<table.getColumnCount(); j++)
							table.setValueAt("", i,j);
						j=0;
						
						if(i == table.getRowCount()) {
							tModel = (DefaultTableModel)table.getModel();
							tModel.addRow(new Vector(2));
							table.setModel(tModel);

						}
						eof= Fileip.read();
						if (eof!=10)
							continue;
					}
					else{
						buffer = buffer + Character.toString((char) eof);
					}
				}
				eof = Fileip.read();
               } // end while

           for (; i<table.getRowCount(); i++)
            	   for (j=0; j<table.getColumnCount(); j++)
            		   table.setValueAt("", i,j);
          // System.out.println("ColumnCount after ="+table.getColumnCount());
           
         //  analysis.appendTableColumns(table.getColumnCount()-ct_orig);
           
           TableColumnModel columnModel = table.getColumnModel();
           for(int ii=0; ii<table.getColumnCount(); ii++){ 
        	   columnModel.getColumn(ii).setHeaderValue(headingName.get(ii));
           }
           table.setTableHeader(new EditableHeader(columnModel));
           analysis.resetMappingList();
           
           Fileip.close();

		}catch(Exception e) {
			e.printStackTrace();
			OKDialog fileOK = new OKDialog(new JFrame(),true,"Exception caught");
			fileOK.setVisible(true);
		}
		
		System.gc();
		
	}
    
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
    


	/** This is a method used to capture the images of the applet for saving as JPG
	*/
    private java.awt.image.BufferedImage capture() {

	java.awt.Robot robot;

	System.err.println("Before Robot! ");
	try {
            robot = new java.awt.Robot();
        } catch (java.awt.AWTException e) {
            throw new RuntimeException(e);
        }
	System.err.println("After Robot! ");

	java.awt.Rectangle screen = this.getContentPane().getBounds();
      	java.awt.Point loc = screen.getLocation();
      	SwingUtilities.convertPointToScreen(loc,this.getContentPane());

	System.err.println("Before screen.setLocation! ");

	screen.setLocation(loc);
      	return robot.createScreenCapture(screen);
     }


}
