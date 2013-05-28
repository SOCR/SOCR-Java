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

//import edu.ucla.stat.SOCR.analyses.jri.gui.Analysis;
//import edu.ucla.stat.SOCR.analyses.jri.gui.AnalysisPanel;
import edu.ucla.stat.SOCR.gui.OKDialog;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * @author  <A HREF="mailto:chea@stat.ucla.edu">Annie Che </A>
 */
public class SOCRJRIAnalyses extends SOCRAnalyses implements ActionListener {

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
    //private edu.ucla.stat.SOCR.analyses.jri.gui.Analysis analysis;

    public static String ABOUT ="ABOUT";
    public static String SNAPSHOT ="SNAPSHOT";
    public static String HELP ="HELP";
    public static String COPY ="COPY";
    public static String PASTE = "PASTE";

    JFileChooser jfc;
    public Clipboard clipboard;
    public javax.swing.table.DefaultTableModel tModel;


    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.gui.SOCRApplet#getCurrentItem()
     */
 /*
 	public Object getCurrentItem() {
        return analysis;
    }
	public void init()  {
		super.init();

	}
    public void initGUI() {
		controlPanelTitle = "Analysis/Parameters";
		implementedFile = "implementedJRIAnalysis.txt";

		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		// comment or uncomment to activate the buttons.
		addButton(ABOUT, "Find Details about this Type of Statistical Analysis", this);
		addButton(HELP, "Help with This type of Analysis", this);
		addButton(SNAPSHOT, "Save a Snapshot/Image of this SOCRAnalysis Applet", this);
		addButton(COPY, "Copy data from table to mouse buffer", this);
		addButton(PASTE, "Paste in Data", this);
		// end buttons.

		graphPanel = new edu.ucla.stat.SOCR.analyses.gui.AnalysisPanel(this);
		container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		graphPanel, new JScrollPane(statusTextArea) );
		fPresentPanel.setViewportView(container);
    }

    protected void itemChanged(String className) {
        try {
		  analysis = edu.ucla.stat.SOCR.analyses.jri.gui.Analysis.getInstance(className);
		  analysis.init();
            fPresentPanel.setViewportView(analysis.getDisplayPane());
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
            e.printStackTrace();
        }
    }
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(ABOUT)) {
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
		try {
			getAppletContext().showDocument(
			new java.net.URL(analysis.getOnlineHelp()),
			   "SOCR: Analysis Online Help (Mathematica)");
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
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
                    int crtCl = analysis.dataTable.getSelectedColumn();
                    //System.out.println("Inside PASTE!");

                    if(crtRow == -1) {
                        OKDialog OKD = new OKDialog(null, true,
				"Select a row to paste data");
                        OKD.setVisible(true);
                    }
		    else  // if(crtRow != -1)
                    { try
                      { clipboard.getContents(this);
                        DataFlavor[] flavs =
				clipboard.getContents(this).getTransferDataFlavors();
                        String tabData =
				clipboard.getContents(this).getTransferData(
					flavs[0].stringFlavor).toString();
			System.err.println(tabData);

                        StringTokenizer lnTkns = new StringTokenizer(tabData,"\r\n");

                        String line;
                        int lineCt = lnTkns.countTokens();
                        if(crtRow+lineCt > analysis.dataTable.getRowCount())
                            appendTableRows(crtRow+lineCt -
				 analysis.dataTable.getRowCount());
                        int r = 0;
                        while(lnTkns.hasMoreTokens()) {
                        	line = lnTkns.nextToken();
                        	StringTokenizer strTok = new
					StringTokenizer(line,"\t\f");
                                int c = 0;
                                while(strTok.hasMoreTokens()) {                                					analysis.dataTable.setValueAt(
						strTok.nextToken(),crtRow+r,c+crtCl);
                                	c++;
                                }
                        	r++;
                        }
                      }
		      catch(Exception e){
                        OKDialog OKD = new OKDialog(null, true,
				"Unalbe to paste. Check the datatype.\n" +
                        	"Number of columns from PASTE data cannot exceed "+
				"number of columns in Table");
                        OKD.setVisible(true);
                        e.printStackTrace();
                      }
                    }	//else  *** if(crtRow != -1)
        } // END of last "else if"
      }

   public void appendTableRows(int n) {
        int cl= analysis.dataTable.getSelectedColumn();
        int ct = analysis.dataTable.getColumnCount();
        tModel = (javax.swing.table.DefaultTableModel) analysis.dataTable.getModel();
        for(int j=0;j<n;j++)
                  tModel.addRow(new java.util.Vector(ct));
        analysis.dataTable.setModel(tModel);
    }


	/** This is a method used to capture the images of the applet for saving as JPG
	*
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
	*/
}
