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

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.ucla.stat.SOCR.chart.ChartTree_dynamic;
import edu.ucla.stat.SOCR.chart.DemoDescription;
import edu.ucla.stat.SOCR.chart.j3d.Chart3D;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinned2DData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinnedBinary2DData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinnedMatrix2DData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinnedTriplet2DData;
import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;
import edu.ucla.stat.SOCR.util.EditableHeader;


/**
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class SOCRChart3D extends SOCRApplet2 implements ActionListener, TreeSelectionListener, AdjustmentListener{

    /**
	 * @uml.property  name="statusTextArea"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	private SOCRTextArea statusTextArea = new SOCRTextArea();  //right side lower
    /**
	 * @uml.property  name="analysis"
	 * @uml.associationEnd  
	 */
    private Chart3D chart3d;
	private static final String baseCode = "edu.ucla.stat.SOCR.chart.j3d.demo.";
	/**
	 * @uml.property  name="chooseCategory"
	 */
	//protected  boolean chooseCategory = true;
	/**
	 * @uml.property  name="choose3D"
	 */
	//protected  boolean choose3D = false;

	/**
	 * @uml.property  name="rPanel"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	protected JPanel rPanel;
    public static String ABOUT ="ABOUT";
    public static String SNAPSHOT ="SNAPSHOT";
    public static String HELP ="HELP";
    public static String COPY ="COPY";
    public static String PASTE = "PASTE";
    public static String SEARCH = "SEARCH";
    public static String FILE = "FILE OPEN";
	// Radio buttons
    public File file;
	public FileInputStream Fileip;
	public FileDialog FileLocate;
	public Frame fDialog = new Frame();
	
    /**
	 * @uml.property  name="jfc"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
    JFileChooser jfc;
    /**
	 * @uml.property  name="clipboard"
	 */
    public Clipboard clipboard;
    /**
	 * @uml.property  name="tModel"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
    public javax.swing.table.DefaultTableModel tModel;
	/**
	 * @uml.property  name="tree"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	JTree tree;
	
	private String chosenChart="";

	
    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.gui.SOCRApplet#getCurrentItem()
     */
    public Object getCurrentItem() {
        return chart3d;
    }
    
	public void init()  {
		super.init();
		fControlPaneScrollBar.addAdjustmentListener(this);
	   
		//System.out.println("*"+chosenChart+"*");
		if(chosenChart!=null && chosenChart.length()!=0)
		return;
		
		try {
			//System.out.println("setting NoChart");
			chart3d = Chart3D.getInstance(baseCode+"NoChart");					
			chart3d.init();
			fPresentPanel.setViewportView(chart3d.getDisplayPane());
			fPresentPaneScrollBar.addAdjustmentListener(chart3d);														  
					
		} catch (Throwable e) {
			SOCROptionPane.showMessageDialog(this, "Sorry, not implemented yet");
			e.printStackTrace();
		}
		
	}	
 
	
	 public void setSelectedApplication(String chartName) {
	    	chosenChart = chartName;
	    	//System.out.println("(setSelectedApplication) chosenApp =" + chosenApp);
	    	
	    	if (tree!=null){
	    		gotoTreeChild(chosenChart, true);	// fullName match
	    		chart3d.updateStatus("set selected chart "+chosenChart);
	    	}
	    }
	    
	    public void getParameterFromHtml() {
	    	String choosen="";
	    	String view="";
	    	String input = "";
	    	String title ="";
	    	String xLabel="";
	    	String yLabel="";
	    	// 1. First see if the HTML Provided a starting Aplpication. If so, use this choice!
	    	try{
				choosen = getParameter("selectedItemName");
				view = getParameter("selectedView");
				input = getParameter("inputData");
			    title = getParameter("ChartTitle");
			    xLabel = getParameter("XLabel");
			    yLabel = getParameter("YLabel");
			}catch(Exception e){
				// SOCR_modeler has problem of getting applet parameter
				return;
			}
			
			
	    	
			// 2. Next if the Starting HTML driver did NOT provode a starting Application, 
			// then use chosenApp value (if this is set by the user class).
			if (choosen=="" || choosen==null) 
				choosen = chosenChart;	
				// If default App is set by the "setSelectedApplication()" method
	    	
			
	    	//System.out.println("(getParameterFromHtml) choosen =" + choosen);
	    	if (choosen!=null && choosen.length()!=0){
	    		chosenChart = choosen;
	    		choosen = choosen.toLowerCase(); 
	    		gotoTreeChild(choosen, true);	// fullName match	
	    		chart3d.updateStatus("set selected chart "+choosen);
	    	}
	    	
	    	if (view!=null && view.length()!=0){
	    		chart3d.setView(view);	    		
	    	}
	    	
	    	boolean chartRedo = false;
	    	if (input!=null && input.length()!=0){
	    		chart3d.setDataTable(input);	
	    	
	    		chartRedo = true;
	    	}
	    	if (title!=null && title.length()!=0){
	    		chart3d.setTitle(title);
	    		chartRedo = true;
	    	}
	    	if (xLabel!=null && xLabel.length()!=0){
	    		chart3d.setXLabel(xLabel); 
	    		chartRedo = true;
	    	}
	    	if (yLabel!=null && yLabel.length()!=0){
	    		chart3d.setYLabel(yLabel);	
	    		chartRedo = true;
	    	}
	    	
	    	
	    	if (chartRedo){
	    		chart3d.setIsDemo(false);
	    		chart3d.setMapping();
	    		chart3d.doChart();
	    	}
	    }
	    
   public void initGUI() {
		controlPanelTitle = "SOCR 3D Charts & Graphs";
		implementedFile = "implementedCharts3D.txt";

		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		// comment or uncomment to activate the buttons.
		addButton2(ABOUT, "Find Details about this Type of Chart", this);
		addButton2(HELP, "Help with This type of Chart", this);
		addButton2(SEARCH, "Searc for the right type of Chart", this);
		addButton(SNAPSHOT, "Save a Snapshot/Image of this SOCRChart Applet", this);
		addButton(COPY, "Copy data from table to mouse buffer", this);
		addButton(PASTE, "Paste in Data", this);
		addButton(FILE, "Open File", this);
		// end buttons.
		north.add(Box.createVerticalStrut(8));
        packControlPane();
        
	    tree = new ChartTree_dynamic(implementedFile, codeBase).getTree();
	    tree.addTreeSelectionListener(this);
			
		Color bg = fControlPanel.getBackground();
		DefaultTreeCellRenderer treeRenderer = new DefaultTreeCellRenderer();
        treeRenderer.setBackgroundNonSelectionColor(bg);
        //treeRenderer.setBackgroundSelectionColor(Color.white);
        tree.setCellRenderer(treeRenderer);
		tree.setOpaque(true);
		tree.setBackground(bg);

		//tree.setScrollsOnExpand(true);
		north.add(tree);
		north.add(Box.createVerticalStrut(8));	

		// the reset initGUI
		super.initGUI();
		getParameterFromHtml();
    }

	/*
    protected void itemChanged(String className) {
        try {
			
		  chart = edu.ucla.stat.SOCR.chart3d.Chart.getInstance(className);
		  chart3d.init();
          fPresentPanel.setViewportView(chart3d.getDisplayPane());
		  
		  System.out.println("SPCRChart: itemChanges "+className);
//	  if (className.indexOf("Pie")==-1)
	//		  addXChoices();
	//		  else add3DChoices();


        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
            e.printStackTrace();
        }
    }*/
   protected TreePath[] findByName(JTree tree, String name, boolean fullName) {
       TreeNode root = (TreeNode)tree.getModel().getRoot();
       return find(tree, new TreePath(root), name, fullName);
   }
   
   private TreePath[] find(JTree tree, TreePath parent, String name, boolean fullName) {
       TreeNode node = (TreeNode)parent.getLastPathComponent();
       ArrayList<TreePath> result = new ArrayList<TreePath>();
       String nodeName = node.toString();
      
       if(!fullName){
    	   if (nodeName.toLowerCase().indexOf(name)!=-1) 
    		   result.add(parent);
       }else{
    	   if (nodeName.toLowerCase().equals(name)) 
    		   result.add(parent);
       }
          
           // Traverse children
       if (node.getChildCount() >= 0) {
               for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                   TreeNode n = (TreeNode)e.nextElement();
                   TreePath path = parent.pathByAddingChild(n);
                   TreePath[] resultsFromChild = find(tree, path, name, fullName);
                   // Found match
                   if (resultsFromChild != null)    
                	   	for (int i=0; i<resultsFromChild.length; i++)
                    	   result.add(resultsFromChild[i]);                  
               }
        }	
       
        result.trimToSize();
        int itemFound = result.size();
        TreePath[] newResult = new TreePath[itemFound];
        for (int i=0; i<itemFound; i++)
        		newResult[i]=(TreePath)result.get(i);
        
       return newResult;
   }
   
   // Finds the path in tree as specified by the array of names. The names array is a
   // sequence of names where names[0] is the root and names[i] is a child of names[i-1].
   // Comparison is done using String.equals(). Returns null if not found.
   protected TreePath findByNamePath(JTree tree, String[] names) {
       TreeNode root = (TreeNode)tree.getModel().getRoot();
       return find2(tree, new TreePath(root), names, 0, true);
   }
   
   private TreePath find2(JTree tree, TreePath parent, Object[] nodes, int depth, boolean byName) {
       TreeNode node = (TreeNode)parent.getLastPathComponent();
       Object o = node;
   
       // If by name, convert node to a string
       if (byName) {
           o = o.toString();
          // System.out.println(o);
       }
   
       // If equal, go down the branch
       if (o.equals(nodes[depth])) {
           // If at end, return match
           if (depth == nodes.length-1) {
               return parent;
           }
   
           // Traverse children
           if (node.getChildCount() >= 0) {
               for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                   TreeNode n = (TreeNode)e.nextElement();
                   TreePath path = parent.pathByAddingChild(n);
                   TreePath result = find2(tree, path, nodes, depth+1, byName);
                   // Found a match
                   if (result != null) {
                       return result;
                   }
               }
           }
       }
   
       // No match at this branch
       return null;
   }
   
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(ABOUT)) {
        	try {
				popInfo(chart3d.getLocalAbout(), new java.net.URL(chart3d.getWikiAbout()), "SOCR3DChart: About");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
     }
	else if (evt.getActionCommand().equals(HELP)) {			 
		
		try {
			popInfo(chart3d.getLocalHelp(), new java.net.URL(chart3d.getWikiHelp()), "SOCR3DChart: Help");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	else if (evt.getActionCommand().equals(SEARCH)) {
		 String term = (String)JOptionPane.showInputDialog(this,
                 "Enter the search term:",
                 "Search SOCRCharts list",
                 JOptionPane.PLAIN_MESSAGE,
                 null,
                 null,
                 "Pie");
	/*	 System.out.println("term="+term);
		 TreePath path = findByNamePath(tree, new String[]{"SOCRCharts","Pie Charts", "RingChartDemo1"});
		 TreePath path2 = findByNamePath(tree, new String[]{"SOCRCharts","Line Charts", "QQNormalPlotDemo"});

		 System.out.println(path.toString());
		 System.out.println("----");
		 System.out.println(path2.toString());
		 tree.addSelectionPath(path);
		 tree.addSelectionPath(path2);*/
		 //System.out.println("term ="+term);
		 if (term.length()>0){
			gotoTreeChild(term, false); // part match
			chart3d.updateStatus("searching for chart "+term);
		 }		 
   }
	else if (evt.getActionCommand().equals(FILE)) {

		FileLocate = new FileDialog(fDialog);
		FileLocate.setVisible(true);
		fDialog.setVisible(true);
		if(FileLocate.getFile()!=null){
			if(chart3d.getInputFileType()==1)
			loadFileData();	
			else if (chart3d.getInputFileType()==2)
			loadTripletsFileData();
			else if (chart3d.getInputFileType()==3)
				loadMatrixFileData();
			else if (chart3d.getInputFileType()==4)
				loadBinaryFileData();
		}
		
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

			  //  System.out.println("image " + image);
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
		String cpBuff = "";
		

		if(chart3d.tabbedPanelContainer.getTitleAt(chart3d.tabbedPanelContainer.getSelectedIndex())==Chart3D.DATA 
		   ){
                  	int[] cpRows =  chart3d.dataTable.getSelectedRows();
                  	int[] cpCls =  chart3d.dataTable.getSelectedColumns();
                  	if(cpCls.length<1 || cpRows.length<1) {
                      		OKDialog OKD = new OKDialog(null, 
					true, "Make a selection to copy data");
                      		OKD.setVisible(true);
                  	} else {
						// System.out.println("copy"+chart3d.dataTable.getSelectedRowCount()+";"+chart3d.dataTable.getSelectedColumnCount());
                        for(int i = 0;i<cpRows.length;i++) {
                          for(int j = 0;j<cpCls.length;j++) {
                              if(chart3d.dataTable.getValueAt(cpRows[i],cpCls[j])==null)
                                  cpBuff = cpBuff + " \t";
                              else
                                 cpBuff = cpBuff +
									 chart3d.dataTable.getValueAt(cpRows[i],cpCls[j]) + "\t";
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
		    int crtRow = chart3d.dataTable.getSelectedRow();
		    int crtCl = chart3d.dataTable.getSelectedColumn();
		   // System.out.println("Inside PASTE! crtRow ="+crtRow);
					
		    if(crtRow == -1) {  //table header 
		    	chart3d.resetTable(); // in order to deselect the header cell 
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
		    		//System.out.println("chart3d.dataTable.getColumnCount()="+(chart3d.dataTable.getColumnCount()));
		    		if(crtCl+cellCnt+1 > chart3d.dataTable.getColumnCount()){
		    			chart3d.appendTableColumns(crtCl+cellCnt+1- chart3d.dataTable.getColumnCount()+1);
		    		
		    		//	System.out.println("append column"+ (crtCl+cellCnt+1- chart3d.dataTable.getColumnCount()+1));
		    		}
		    			                    		
		    		TableColumnModel columnModel= chart3d.dataTable.getColumnModel();
		    		int c = 0;   		 
		    		while(cellTkns.hasMoreTokens()) {   
		    			String h = cellTkns.nextToken();
		    			//System.out.println("adding header "+(c+crtCl+1) +": "+h);
	                    			
		    			columnModel.getColumn(c+crtCl+1).setHeaderValue(h);
		    			c++;
		    		}
   	
		    		//
		    		if(lineCt>1){
		    		//	System.out.println("adding table content") ;
		    			crtRow++;
		    			crtCl=0;			
		    			if(crtRow+lineCt > chart3d.dataTable.getRowCount())
		    				chart3d.appendTableRows(crtRow+lineCt - chart3d.dataTable.getRowCount());
		    			
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
		    				if(crtCl+colCt > chart3d.dataTable.getColumnCount())
		    					chart3d.appendTableColumns(crtCl+colCt - chart3d.dataTable.getColumnCount());
		    				for (int i=0; i<tb.length; i++){
		    					//System.out.println(tb[i]);
		    					if (tb[i].length()==0)
		    						tb[i]="0";
		    					chart3d.dataTable.setValueAt(tb[i],crtRow+r,i+crtCl);
		    				}
		    				
		    				r++;
		    			}  
		    		}
		    		EditableHeader headers = new EditableHeader(columnModel);
		  
		    		chart3d.dataTable.setTableHeader(headers);
		 

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
                        int lineCt = lnTkns.countTokens();
                        if(crtRow+lineCt > chart3d.dataTable.getRowCount())
                        	chart3d.appendTableRows(crtRow+lineCt - chart3d.dataTable.getRowCount());
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
                        	 if(crtCl+colCt > chart3d.dataTable.getColumnCount())
                               	chart3d.appendTableColumns(crtCl+colCt - chart3d.dataTable.getColumnCount());
                            for (int i=0; i<tb.length; i++){
                            	//System.out.println(tb[i]);
                            	if (tb[i].length()==0)
                            		tb[i]="0";
                            	chart3d.dataTable.setValueAt(tb[i],crtRow+r,i+crtCl);
                            }
                            
                        	r++;
                        }  
                        // this will update the mapping panel
                        
                        chart3d.resetMappingList();
                        TableColumnModel columnModel= chart3d.dataTable.getColumnModel();            
                        chart3d.dataTable.setTableHeader(new EditableHeader(columnModel));
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

    private void gotoTreeChild(String term, boolean fullName){
    	 tree.clearSelection();
		 //collapse all
		 TreeNode root = (TreeNode)tree.getModel().getRoot();
		 TreePath root_path = new TreePath(root);
		 if (root.getChildCount() >= 0) {
			 for (Enumeration e=root.children(); e.hasMoreElements(); ) {
				 TreeNode n = (TreeNode)e.nextElement();
				 	TreePath path = root_path.pathByAddingChild(n);
				 	tree.collapsePath(path);         
			 }
		 } 
		
		 TreePath[] paths = findByName(tree, term.toLowerCase(), fullName);
		
		 if (paths.length>0)
			 for (int i=paths.length-1; i>=0; i--)
				 tree.addSelectionPath(paths[i]);
		 else 
			 tree.setSelectionPath(root_path);	 
    }
    
    private void loadFileData(){
    	SOCRBinned2DData data;
     
    	try{
    		file = new File(FileLocate.getDirectory(),FileLocate.getFile());
    		Fileip = new FileInputStream(file);
    		data = new SOCRBinned2DData();
    		data.loadDataFloat(Fileip);
            chart3d.setJTable(data);
            chart3d.doChart();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    private void loadTripletsFileData(){
    	SOCRBinnedTriplet2DData data;
     
    	try{
    		file = new File(FileLocate.getDirectory(),FileLocate.getFile());
    		Fileip = new FileInputStream(file);
    		data = new SOCRBinnedTriplet2DData();
    		data.loadDataFloat(Fileip);
            chart3d.setJTable(data);
            chart3d.doChart();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    private void loadMatrixFileData(){
    	SOCRBinnedMatrix2DData data;
     
    	try{
    		file = new File(FileLocate.getDirectory(),FileLocate.getFile());
    		Fileip = new FileInputStream(file);
    		data = new SOCRBinnedMatrix2DData();
    		data.loadDataFloat(Fileip);
            chart3d.setJTable(data);
            chart3d.doChart();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    private void loadBinaryFileData(){
    	SOCRBinnedBinary2DData data;
     
    	try{
    		file = new File(FileLocate.getDirectory(),FileLocate.getFile());
    	//	Fileip = new FileInputStream(file);
    	//	data.loadDataFloat(Fileip);
    		data = new SOCRBinnedBinary2DData();
    		URL url = file.toURL();
    		data.loadDataFloat(url);
            chart3d.setJTable(data);
            chart3d.doChart();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    private void loadFileData(JTable table, JTable headerTable){
    	String buffer = "";
		try{
			 file = new File(FileLocate.getDirectory(),FileLocate.getFile());
               //////System.out.println(file.getAbsolutePath());
               //fileSelected.setText(Boolean.toString(file.canRead()));
               //fileSelected.repaint();
               Fileip = new FileInputStream(file);
               // Fileip.
               int eof = 0;
               String ch;
               int i = 0; //row
               int j = 0;  //column
               int ct= chart3d.dataTable.getColumnCount();
               //only the first line started wiht # will be treated as heading
               boolean heading = false;
              
               eof = Fileip.read();
               ch=Character.toString((char) eof);
               

               while(eof!=-1) {
            	   
            	 // System.out.println("eof="+ eof+":"+Character.toString((char) eof));
            	 
            	  if (eof ==35){
            		//  System.out.println("setting heading = true;");
            		  eof = Fileip.read();
            		  heading = true;
            		  continue;
            	  }
            	  else if(eof == 59 || eof == 44 || eof == 9 ){//44, 59; 9 tab 
					//System.out.println("setting buffer "+buffer+" at "+i+","+j);
					if(j==ct){
						tModel = (DefaultTableModel)table.getModel();	
						tModel.addColumn("C"+(ct+1),new java.util.Vector(ct)); 
						table.setModel(tModel);
						TableColumnModel columnModel = table.getColumnModel();
						table.setTableHeader(new EditableHeader(columnModel));
						ct++;
					}
					if (heading){
						//System.out.println("adding heading " +j +" :"+buffer);
						TableColumnModel columnModel = table.getColumnModel();
						columnModel.getColumn(j).setHeaderValue(buffer);
						table.setTableHeader(new EditableHeader(columnModel));
					}			
					else {
					//	System.out.println("i="+i+ " table.getRowCount()"+table.getRowCount());
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
            		   if(eof == 13  || eof ==10) { //return mac 13, dos 13+10
						//System.out.println("setting buffer "+buffer+" at "+i+","+j);
            			  
							if(j==ct){
								tModel = (DefaultTableModel)table.getModel();	
								tModel.addColumn("C"+(ct+1),new java.util.Vector(ct)); 
								table.setModel(tModel);
								TableColumnModel columnModel = table.getColumnModel();
								table.setTableHeader(new EditableHeader(columnModel));
								ct++;
							}
							if (heading==true){
								//System.out.println("adding last heading " +j +" :"+buffer);
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
               
			Fileip.close();

		}catch(Exception e) {
			e.printStackTrace();
			OKDialog fileOK = new OKDialog(new JFrame(),true,"Exception caught");
			fileOK.setVisible(true);
		}
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

    /** updates the collected information of chart */
    public void updateStatus() {

        statusTextArea.setText(" status about the chart should go here!");
    }

     
    public void valueChanged(TreeSelectionEvent event) {
        TreePath path = event.getPath();
        Object obj = path.getLastPathComponent();
        if (obj != null) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) obj;
            Object userObj = n.getUserObject();
           // System.out.println("obj="+obj);
			try {
					if (userObj instanceof DemoDescription) {
						//System.out.println("SOCRChart: obj = " +obj); // the description
					//	System.out.println("SOCRChart3D: ClassName = " +((DemoDescription) userObj).getClassName());//the className
						//chart = edu.ucla.stat.SOCR.chart3d.Chart.getInstance(baseCode+obj);
						chart3d = Chart3D.getInstance(((DemoDescription) userObj).getClassName());
						chart3d.setApplet(this);
						fPresentPanel.setViewportView(chart3d.getDisplayPane());
						fPresentPaneScrollBar.addAdjustmentListener(chart3d);			
					}
					else {
						//System.out.println("not demoDescription");
						chart3d = Chart3D.getInstance(baseCode+"NoChart3D");		
					}
					chart3d.init();
					fPresentPanel.setViewportView(chart3d.getDisplayPane());
					fPresentPaneScrollBar.addAdjustmentListener(chart3d);														  

				} catch (Throwable e) {
					SOCROptionPane.showMessageDialog(this, "Sorry, "+chart3d.getClass().getName()+" not implemented yet");
					e.printStackTrace();
				}
				
		}
		tree.scrollPathToVisible(path);
		// System.out.println("SOCRChart valueChanged: "+obj);
    }

	public void adjustmentValueChanged(AdjustmentEvent event) {
		//	System.out.println("SOCRChart adjustmentEv:"+event.paramString());

		tree.repaint();
	}
}
