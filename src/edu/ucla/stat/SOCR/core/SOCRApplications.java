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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;


import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import edu.ucla.stat.SOCR.applications.Application;
import edu.ucla.stat.SOCR.chart.Chart;
import edu.ucla.stat.SOCR.chart.ChartTree;
import edu.ucla.stat.SOCR.chart.ChartTree_dynamic;
import edu.ucla.stat.SOCR.chart.DemoDescription;
import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;
import edu.ucla.stat.SOCR.util.EditableHeader;


/**
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class SOCRApplications extends SOCRApplet2 implements ActionListener, TreeSelectionListener, AdjustmentListener{

    /**
	 * @uml.property  name="statusTextArea"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	private SOCRTextArea statusTextArea = new SOCRTextArea();  //right side lower
    /**
	 * @uml.property  name="analysis"
	 * @uml.associationEnd  
	 */
    //private edu.ucla.stat.SOCR.chart.Chart chart;
    private Application application;
	private static final String baseCode = "edu.ucla.stat.SOCR.applications.demo.";

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
	String appName; 
	
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

	/* public static String COVR = "COVRIANCE";
	 public static String CORR = "CORRELATION";
	 String[] formatArray ={COVR, CORR};
	 
	 public static String STOCK2 = "2";
	 public static String STOCK3 = "3";
	 public static String STOCK4 = "4";
	 public static String STOCK5 = "5";
	 String[] numStocksArray ={STOCK2,STOCK3,STOCK4,STOCK5};
	 
	 String[] on_off = {"On", "Off"};*/
	 
	    
    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.gui.SOCRApplet#getCurrentItem()
     */
    public Object getCurrentItem() {
        return  application;
    }
    
/*????	public void init()  {
		super.init();
		fControlPaneScrollBar.addAdjustmentListener(this);
	   
		try {
			application  = edu.ucla.stat.SOCR.applications.Application.getInstance(baseCode+"NoApp");					
			application .init();
			fPresentPanel.setViewportView(application.getDisplayPane());
			fPresentPaneScrollBar.addAdjustmentListener(application);														  
			
		} catch (Throwable e) {
			SOCROptionPane.showMessageDialog(this, "Sorry, not implemented yet");
			e.printStackTrace();
		}
	}	*/
 
   public void initGUI() {
		controlPanelTitle = "SOCR Applications/";
		implementedFile = "implementedApplications.txt";

		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		// comment or uncomment to activate the buttons.
		addButton2(ABOUT, "Find Details about this Type of Application", this);
		addButton2(HELP, "Help with This type of Application", this);
		addButton2(SEARCH, "Searc for the right type of application", this);
		addButton(SNAPSHOT, "Save a Snapshot/Image of this SOCRApplication Applet", this);
		addButton(COPY, "Copy data from table to mouse buffer", this);
		
	//	addRadioButton("Switch Input Matrix:", "Switch Input Matrix", formatArray, 0, this);
		/*show_tangent = new JCheckBox();
		addJLabel(new JLabel("Show"));
		addJCheckBox(show_tangent);*/
		
		//System.out.println("SOCRApplications initGUI get called");
	//	addRadioButton("Number of Stocks:", "", numStocksArray, 0, this);		
	//	addRadioButton2("Show Tangent Line :", "", on_off, 0, this);
		
	//	addButton(PASTE, "Paste in Data", this);
		addButton(FILE, "Open File", this);
		// end buttons.
		north.add(Box.createVerticalStrut(8));
        packControlPane();

		/*try{
				tree = new ChartTree().getTree();
			}  catch (Exception e1) {
			         JOptionPane.showMessageDialog(this, e1.getMessage());
			        e1.printStackTrace();
			}*/
        
	    tree = new ChartTree_dynamic(implementedFile, codeBase, "SOCRApplications").getTree();
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
    }

	
    protected void itemChanged(String className) {
        try {
			
		  application = edu.ucla.stat.SOCR.applications.Application.getInstance(className);
		  application.setApplet(this);
		  application.init();
        //  resetRadioButton(0);
          fPresentPanel.setViewportView(application.getDisplayPane());
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
            e.printStackTrace();
        }
    }
   protected TreePath[] findByName(JTree tree, String name) {
       TreeNode root = (TreeNode)tree.getModel().getRoot();
       return find(tree, new TreePath(root), name);
   }
   
   private TreePath[] find(JTree tree, TreePath parent, String name) {
       TreeNode node = (TreeNode)parent.getLastPathComponent();
       ArrayList<TreePath> result = new ArrayList<TreePath>();
       String nodeName = node.toString();
      
       if (nodeName.toLowerCase().indexOf(name)!=-1) 
           result.add(parent);
          
           // Traverse children
       if (node.getChildCount() >= 0) {
               for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                   TreeNode n = (TreeNode)e.nextElement();
                   TreePath path = parent.pathByAddingChild(n);
                   TreePath[] resultsFromChild = find(tree, path, name);
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
    	
    	/*for (int i=0; i<numStocksArray.length; i++)
    		if (evt.getActionCommand().equals(numStocksArray[i])) {
    			application.setNumberStocks(numStocksArray[i]);	    		
    	}
    	
    	for (int i=0; i<on_off.length; i++)
    		if (evt.getActionCommand().equals(on_off[i])) {
    			if (i==0)
    				application.setTangent(true);	 
    			else application.setTangent(false);	 
    	}*/
    	
        if (evt.getActionCommand().equals(ABOUT)) {
        	try {
    			if (application == null) {
    				application  = new edu.ucla.stat.SOCR.applications.Application();
    				popInfo(application.getLocalAbout(), new java.net.URL(application.getWikiAbout()), 
    						"SOCRApplication: About");
    			} else 
				popInfo(application.getLocalAbout(), new java.net.URL(application.getWikiAbout()), 
						"SOCRApplication: About");
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			}			
     }
	else if (evt.getActionCommand().equals(HELP)) {			 
		
		try {
			if (application == null) {
				application  = new edu.ucla.stat.SOCR.applications.Application();
				popInfo(application.getLocalHelp(), new java.net.URL(application.getWikiHelp()), 
						"SOCRApplication: Help");
			} else
				popInfo(application.getLocalHelp(), new java.net.URL(application.getWikiHelp()), 
						"SOCRApplication: Help");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }
	else if (evt.getActionCommand().equals(SEARCH)) {
		 String term = (String)JOptionPane.showInputDialog(this, 
                 "Enter the search term:",
                 "Search SOCRApplications list",
                 JOptionPane.PLAIN_MESSAGE,
                 null,
                 null,
                 "Portfolio");
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
			
			 TreePath[] paths = findByName(tree, term.toLowerCase());
			
			 if (paths.length>0)
				 for (int i=paths.length-1; i>=0; i--)
					 tree.addSelectionPath(paths[i]);
			 else tree.setSelectionPath(root_path);
		 }		 
   }//search
	else if (evt.getActionCommand().equals(FILE)) {

		FileLocate = new FileDialog(fDialog);
		FileLocate.setVisible(true);
		fDialog.setVisible(true);
		if(FileLocate.getFile()!=null){
			if (appName.indexOf("Portfolio")!=-1)
			 loadFileData4Portfolio();
		}
		
		FileLocate.dispose();
		fDialog.dispose();
		
  }else if (evt.getActionCommand().equals(SNAPSHOT)) {

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

     
    public void valueChanged(TreeSelectionEvent event)
    {
        TreePath path = event.getPath();
        Object obj = path.getLastPathComponent();
        if (obj != null)
        {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) obj;
            Object userObj = n.getUserObject();
            // System.out.println("obj="+obj);
            try
            {
                if (userObj instanceof DemoDescription)
                {
                    //System.out.println("SOCRChart: obj = " +obj); // the description
                    //System.out.println("SOCRChart: ClassName = " +((DemoDescription) userObj).getClassName());//the className
                    //chart = edu.ucla.stat.SOCR.chart.Chart.getInstance(baseCode+obj);
                    appName = ((DemoDescription) userObj).getClassName();
                    //System.out.println("appName="+appName);
                    application = edu.ucla.stat.SOCR.applications.Application.getInstance(appName);
                    application.init();
                    application.setApplet(this);
                }
                else
                {
                    appName = baseCode+"NoApp";
                    application =
edu.ucla.stat.SOCR.applications.Application.getInstance(appName);
                }
                application.init();
                fPresentPanel.setViewportView(application.getDisplayPane());
            }
                //????fPresentPaneScrollBar.addAdjustmentListener(application);            }
            catch (Throwable e)
            {
                SOCROptionPane.showMessageDialog(this, "Sorry, " + appName + " not implemented yet");
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
	
	 private void loadFileData4Portfolio(){
		 double[] r1, c1, m1;
		 r1= new double[5];
		 c1= new double[5];
		 m1= new double[10];
			 
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
	               boolean comment= false;
	               String current = "r1";
	               int currentId= 0;
	               eof = Fileip.read();
	               ch=Character.toString((char) eof);
	               

	               while(eof!=-1) {
	            	   
	            	 // System.out.println("eof="+ eof+":"+Character.toString((char) eof));
	            	 
	            	  if (eof ==35){// #
	            		//  System.out.println("setting heading = true;");
	            		  eof = Fileip.read();
	            		  comment = true;
	            		  continue;
	            	  }
	            	  else if(eof == 59 || eof == 44 || eof == 9 ){//44, 59; 9 tab 
						//System.out.println("setting buffer "+buffer+" at "+i+","+j);
							if (!comment && buffer.length()!=0) {
							//	System.out.println("i="+i+ " table.getRowCount()"+table.getRowCount());
								if (current.equals("r1"))
									r1[currentId]= Double.parseDouble(buffer);
								else if (current.equals("c1"))
									c1[currentId]= Double.parseDouble(buffer);
								else if (current.equals("m1"))
									m1[currentId]= Double.parseDouble(buffer);
								currentId++;
							}
							
							buffer = "";
	            	  } 
	            	   else if(eof == 32){//space
	            		   eof = Fileip.read();
	            		   continue;
	            	   }
	            	   else {
	            		   if(eof == 13  || eof ==10) { //return mac 13, dos 13+10
							//System.out.println("setting buffer "+buffer+" at "+i+","+j);
	            			   if (!comment && buffer.length()!=0) {
	   							//	System.out.println("i="+i+ " table.getRowCount()"+table.getRowCount());
	   								if (current.equals("r1"))
	   									r1[currentId]= Double.parseDouble(buffer);
	   								else if (current.equals("c1"))
	   									c1[currentId]= Double.parseDouble(buffer);
	   								else if (current.equals("m1"))
	   									m1[currentId]= Double.parseDouble(buffer);
	   								currentId++;
	   							}
								
	            			   buffer = "";
	            			  
							if (comment ==false){
								if (current.equals("r1"))
									current="c1";
								else if (current.equals("c1"))
									current= "m1";							
							}
							if (comment==true){
	            				   //  System.out.println("setting heading = false;");	   
	            				   comment = false;
	            			   }
							currentId= 0;
							
		
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
       
	               if (!comment && buffer.length()!=0) {
							//	System.out.println("i="+i+ " table.getRowCount()"+table.getRowCount());
								if (current.equals("r1"))
									r1[currentId]= Double.parseDouble(buffer);
								else if (current.equals("c1"))
									c1[currentId]= Double.parseDouble(buffer);
								else if (current.equals("m1"))
									m1[currentId]= Double.parseDouble(buffer);
								currentId++;
							}
				Fileip.close();

			}catch(Exception e) {
				e.printStackTrace();
				OKDialog fileOK = new OKDialog(new JFrame(),true,"Exception caught");
				fileOK.setVisible(true);
			}
			 application.loadSlider(r1,c1,m1);
		}
}