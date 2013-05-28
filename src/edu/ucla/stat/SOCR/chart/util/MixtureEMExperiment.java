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
 * MixtureEMExperiment.java MixtureEMExperiment.java contains the driver JApplet
 * for the EM algorithm @author Ivo Dinov
 * 
 */

package edu.ucla.stat.SOCR.chart.util;

import java.awt.Color;
import java.awt.Paint;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.chart.gui.CustomJTable;
import edu.ucla.stat.SOCR.core.Experiment;

public class MixtureEMExperiment extends Experiment {
	public final static int GL_MIX = 4;
	public final static int CG_MIX = 3;
	
//	boolean manualKernels= false;
	int mixSelected = CG_MIX;
	CurvedGaussMixture CGMix;
    GaussLineMixture   GLMix;
    Database           DB;
    double[]           ws, ws2;
    String EM_name;
    int num_group;
    Paint[] color_groups;
    
    XYDataset[] kernels = null;
    XYSeriesCollection segmentedPoints;
    XYSeriesCollection points;
    int num_kernels=1;
    int group_points;
    
    Paint[] color_kernels= new Paint[10]; 
    Paint color_mainGroup;
    
    JTable tempResultsTable;
    CustomJTable resultsTable;
    DefaultTableModel tModel_results;

    double               xSize      = 700;
    double                ySize      = 700;
    double xStart =0;
    double yStart =0;

    public MixtureEMExperiment(){
 	   init(xSize, ySize, xSize/2, ySize/2);
    }
    public MixtureEMExperiment(double x, double y){
 	   xSize = x;
 	   ySize = y;
 	   init(xSize, ySize, xSize/2, ySize/2);
    }
    public MixtureEMExperiment(double x, double y, double sx, double sy){
    	
 	   xSize = x;
 	   ySize = y;
 	   xStart= sx; yStart= sy;
 	   init(xSize, ySize,sx, sy);
    
    }
    
    public void setMixSelected(int m){
    	if (m==GL_MIX  ||m==CG_MIX )
    		mixSelected= m;
    	else return;
    }
    
    public int getMixSelected(){
    	return mixSelected;
    }
    
   /* public void setNumOfKernels(){
    	if (mixSelected==CG_MIX)
    		num_kernels =  CGMix.getnk()-1;
    	else 
    		num_kernels =  GLMix.getnk()-1;
    }*/
    
    
    public XYDataset[] getKernels(){
    	 if (mixSelected==CG_MIX)
    		  return CGMix.getDatasets();
    	 else return GLMix.getDatasets();
    }
    
   public void EM(double[] ws){
	   if (mixSelected==CG_MIX)
		   CGMix.EM(ws);
	   else GLMix.EM(ws);
	   
   }
   public void setNumOfKernels(int n){
	   num_kernels= n;
   }
   
   public void setManualKernel(boolean b){
	 //  manualKernels=b;
	   CGMix.setManualKernels(b);
	   GLMix.setManualKernels(b);
   }
  
   public void setnk(int nk, double[] ws){
	   
	   if (mixSelected==CG_MIX)
		   CGMix.setnk(nk+1, ws);
	   else GLMix.setnk(nk+1, ws);
   }
   
   /* public CurvedGaussMixture getCGMix(){
    		 return CGMix;
    }
    
    public GaussLineMixture getGLMix(){
		 return GLMix;
    }*/
    
    
    public Database getDB(){
    	return DB;
    }
      
    public void init(double x, double y, double sx, double sy) {
    	xSize = x;
    	ySize =y;
    	xStart = sx;
    	yStart = sy;
    	//System.out.println("MixtureEMExperiment init "+x+" ,"+y+" sx="+sx+" sy="+sy);
        //getContentPane().setLayout(new BorderLayout());
        DB = new Database(xSize, ySize,sx,sy);
        
        CGMix = new CurvedGaussMixture(xSize, ySize, DB);
        GLMix = new GaussLineMixture(xSize, ySize, DB);
        initResutlsTable();
        
        DefaultDrawingSupplier supplier = new DefaultDrawingSupplier();
        color_mainGroup = supplier.getNextPaint();
        for (int i=0; i<10; i++){
			color_kernels[i] = supplier.getNextPaint();
		}
    } 
    
    public void resetSize() {
    	DB.setRange();
    	
        xSize = DB.getXSize();
        ySize = DB.getYSize();
        double xStart = DB.getXStart();
        double yStart = DB.getYStart();
       // System.out.println("MixtureEM reset size xSize="+xSize+" ySize="+ySize+" center="+xStart+", "+yStart);
       // Exception e = new Exception();
      //  e.printStackTrace();
        CGMix.setRange(xSize, ySize, xStart, yStart);
        GLMix.setRange(xSize, ySize, xStart, yStart);
    }

    public void resetSize(double _xSize, double _ySize, double _xStart, double _yStart) {
    	xSize = _xSize;
	    ySize = _ySize;
	    xStart = _xStart;
	    yStart = _yStart;
	   // System.out.println("MixtureEM reset size xSize="+xSize+" ySize="+ySize+" center="+xStart+", "+yStart);
	    CGMix.setRange(xSize, ySize, xStart, yStart);
	    GLMix.setRange(xSize, ySize, xStart, yStart);
    }
    
    public void resetSize(int kernel_index, double _xSize, double _ySize, double _xStart, double _yStart) {
    	xSize = _xSize;
    	ySize = _ySize;
    	xStart = _xStart;
    	yStart = _yStart;
    	//System.out.println("MixtureEM reset size xSize="+xSize+" ySize="+ySize+" center="+xStart+", "+yStart);
    	CGMix.setRange(kernel_index, xSize, ySize, xStart, yStart);
    	GLMix.setRange(kernel_index, xSize, ySize, xStart, yStart);
    }
    
    public void reset() {
        super.reset();
        DB.clearPoints();
    } //END::reset()

    protected void initResutlsTable(){
   	 
   	 String[] resultsHeading = new String[2];
   	 resultsHeading[0] = "Kernel";
   	 resultsHeading[1] = "Resutls";
   	 
   	Object[][] dataObject = new Object[10][2];
   	
   	tModel_results = new DefaultTableModel(dataObject, resultsHeading);

   	resultsTable = new CustomJTable(tModel_results);
   	
   	/*{
    	 public Component prepareRenderer(TableCellRenderer renderer,
              int rowIndex, int vColIndex) {
   		 Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
   		// 
   		 if (rowIndex % 2 == 0) {
   			 c.setBackground(Color.yellow);
   		 } else {
   			 	// If not shaded, match the table's background
   			 c.setBackground(getBackground());
   		 }
   		 return c;
   	 }
   };*/
   	resultsTable.setGridColor(Color.gray);
   	resultsTable.setShowGrid(true);

   	// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
   	try { 
   		resultsTable.setDragEnabled(true); 
   	} catch (Exception e) { 
   	}
   	//columnModel = resultsTable.getColumnModel();
   	//resultsTable.setTableHeader(new EditableHeader(columnModel));
   	resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
   	resultsTable.setCellSelectionEnabled(true);
   	resultsTable.setColumnSelectionAllowed(true);
   	resultsTable.setRowSelectionAllowed(true);
   }
    

    protected void resetRTableRows(int n) {
    	DefaultTableModel tModel_results = (javax.swing.table.DefaultTableModel) resultsTable.getModel();
    	tModel_results.setRowCount(n);
    	resultsTable.setModel(tModel_results);
    }

    
    public String getName()
    {
    	if (EM_name==null || EM_name.equals(""))
    		return new String ("Mixture EM Experiment");
    	else return EM_name;
     }

    public void setName(String n){
    	EM_name=n;
    	DB.setDbName(n);
    }
    
    /**
     * This method updates the display, including the ball panel, the record
     * table, and the random variable graph and table
     */
    public void update() {
        super.update();
        xSize = DB.getXSize();
        ySize = DB.getYSize();
     
    }
    
    public XYDataset getPoints(){
    	/* points= new XYSeriesCollection();
    	 System.out.println("MixtureEMExperiment: getPoints: np="+DB.nPoints());
    	 XYSeries s = new XYSeries("Points", false);
    	 for (int i=0; i<DB.nPoints(); i++)
    		 s.add(DB.xVal(i), DB.yVal(i));
    	 
    	 points.addSeries(s);
    	return points;*/
    	return DB.getDataset();
    }
    public XYDataset getSegmentedPoints(){
    	pointSegmenting();
    	return segmentedPoints;
    }

    private void  pointSegmenting() {
		 segmentedPoints = new XYSeriesCollection();
		 
		 XYSeries mainGroup = new XYSeries("");	 // mainGroup saves the points not included in any kernel
		 XYSeries[] otherGroups = new XYSeries[num_kernels];
		 int num_pts= DB.nPoints();

		 
		 double[][] kernel_x = new double[num_pts][num_kernels];
		 double[][] kernel_y = new double[num_pts][num_kernels];
		 int[] num_pts_in_kernel = new int[num_kernels];
		 
        for (int i = 0; i < DB.nPoints(); i++) // For each point find the 2-nd level Gaussian
        {
       	 boolean found = false;
       	 double x_value= DB.xVal(i);
       	 double y_value= DB.yVal(i);
            for (int k = 1; k <= num_kernels; k++) // for each kernel
            {
           	 
                if (CGMix.getKernel(k) instanceof CurvedGaussian
                        && ((CurvedGaussian) (CGMix.getKernel(k))).getPolygon()
                                .contains(x_value, y_value))
                // point inside the 2nd polygon Ellipse
                // "CGMix")
                { 
               	// System.out.println("Inside::pointSegmenting()");
                  //  System.out.println("found one point in "+k);  // k is index of kernel+1
               	 if (otherGroups[k-1]==null)
               		 otherGroups[k-1] = new XYSeries("");
               	 otherGroups[k-1].add(x_value, y_value);    
               	 found=true;
               	
               	 kernel_x[num_pts_in_kernel[k-1]][k-1]= (x_value);
               	 kernel_y[num_pts_in_kernel[k-1]][k-1]= (y_value);
               	 num_pts_in_kernel[k-1]++;
               	 
               	 k = num_kernels; // exit inner
                    // loop
                }
                
            }
            if (!found){
           	 mainGroup.add(x_value, y_value);
           	 
            }
        }
        
        num_group=0;
        color_groups = new Paint[num_kernels];
        int results_rowCount=0;
        
        String[][] results = new String[num_kernels*6+num_pts+2][2];
        Paint[] row_color = new Paint[num_kernels*6+num_pts+2];
        
        for (int i=0; i<num_kernels; i++ ){
       	if (otherGroups[i]!=null){
       	 color_groups[num_group]  = color_kernels[i];
       	 segmentedPoints.addSeries(otherGroups[i]);
       	 num_group++;
       	 
       	 results[results_rowCount][0] = "Kernel:"+num_group;
       	 results[results_rowCount][1] = color_kernels[i].toString();
       	 row_color[results_rowCount]=color_kernels[i];
       	 results_rowCount++;
       	 results[results_rowCount][0] = "mX = "+CGMix.getkmx(i+1);;
       	 results[results_rowCount][1] = "mY = "+CGMix.getkmy(i+1);;
       	 row_color[results_rowCount]=color_kernels[i];
       	 results_rowCount++;
       	 results[results_rowCount][0] = "sXX = "+CGMix.getksx(i+1);;
       	 results[results_rowCount][1] = "sXY = "+CGMix.getksxy(i+1);;
       	 row_color[results_rowCount]=color_kernels[i];
       	 results_rowCount++;
       	 results[results_rowCount][0] = "sYX = "+CGMix.getksxy(i+1);;
       	 results[results_rowCount][1] = "sYY = "+CGMix.getksy(i+1);
       	 row_color[results_rowCount]=color_kernels[i];
       	 results_rowCount++;
       	 results[results_rowCount][0] = "weight = "+CGMix.getWeight(i+1);
       	 results[results_rowCount][1] = "likelihood = "+CGMix.likelihood();
       	 row_color[results_rowCount]=color_kernels[i];
       	 results_rowCount++;
       	 results[results_rowCount][0]= "Points";
       	 results[results_rowCount][1]= "Count = "+num_pts_in_kernel[i];
       	 row_color[results_rowCount]=color_kernels[i];
       	 for (int j=0; j<num_pts_in_kernel[i]; j++){
       		 results_rowCount++;
       		 results[results_rowCount][0]=Double.toString(kernel_x[j][i]);
       		 results[results_rowCount][1]=Double.toString(kernel_y[j][i]);
       		 row_color[results_rowCount]=color_kernels[i];
       	 }       	
       	 results_rowCount++; 
       	}
        }
        
        results[results_rowCount][0] = "The rest";
        results[results_rowCount][1] = color_mainGroup.toString() ;
        row_color[results_rowCount]=color_mainGroup;
        results_rowCount++;
        results[results_rowCount][0] = "Points";
        results[results_rowCount][1] =  "Count = " +Integer.toString(mainGroup.getItemCount());
        row_color[results_rowCount]=color_mainGroup;
        results_rowCount++;
        for (int i=0; i<mainGroup.getItemCount(); i++){
       	 results[results_rowCount][0]=mainGroup.getX(i).toString();
   		 results[results_rowCount][1]=mainGroup.getY(i).toString();
   		 row_color[results_rowCount]=color_mainGroup;
   		 results_rowCount++;
        }
       	 
        
        String[] resultsHeading = new String[2];
		 resultsHeading[0] = "Kernel";
		 resultsHeading[1] = "Resutls";
        tempResultsTable = new JTable(results, resultsHeading);
        
        resetRTableRows(tempResultsTable.getRowCount());        
      
        for(int i=0;i<tempResultsTable.getRowCount();i++)
	         for(int j=0;j<tempResultsTable.getColumnCount();j++) {
	             resultsTable.setValueAt(tempResultsTable.getValueAt(i,j),i,j);
			 }
        resultsTable.setColor(row_color, num_kernels*6+num_pts);
        
        num_group++;
        segmentedPoints.addSeries(mainGroup);

    }
    
    public CustomJTable getResultsTable(){
    	return resultsTable;
    }
    
    public int getNumOfKernels(){
    	return num_kernels;
    }
    
    public int getNumOfGroup(){
    	return num_group;
    }
	 
    public Paint[] getColorOfGroups(){
	 return color_groups;
    }
    
    public Paint[] getColorOfKernels(){
   	 return color_kernels;
       }
    
    public Paint getColorOfMainGroup(){
   	 return color_mainGroup;
       }
} 
