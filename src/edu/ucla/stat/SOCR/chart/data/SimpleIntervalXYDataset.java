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
package edu.ucla.stat.SOCR.chart.data;

import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;

/**
 * A quick and dirty sample dataset.
 */
public class SimpleIntervalXYDataset extends AbstractIntervalXYDataset 
                                     implements IntervalXYDataset {
	private int seriesCount;

	private int count;
    /**
	 * The start values.
	 * @uml.property  name="xStart" multiplicity="(0 -1)" dimension="1"
	 */
    private Double[][] xStart ;
    
    /**
	 * The end values.
	 * @uml.property  name="xEnd" multiplicity="(0 -1)" dimension="1"
	 */
    private Double[][] xEnd ;

    /**
	 * The y values.
	 * @uml.property  name="yValues" multiplicity="(0 -1)" dimension="1"
	 */
    private Double[][] yValues ;
    
    private String serieKeys[];

    /**
     * Creates a new dataset.
     */
    public SimpleIntervalXYDataset() {
    	count = 3;
    	seriesCount = 1;
    	serieKeys = new String[1];
    	serieKeys[0]= "Serie 1";
    	
    	xStart = new Double[3][1];
    	xEnd = new Double[3][1];
    	yValues = new Double[3][1];
        this.xStart[0][0] = new Double(0.0);
        this.xStart[1][0] = new Double(2.0);
        this.xStart[2][0] = new Double(3.5);

        this.xEnd[0][0] = new Double(2.0);
        this.xEnd[1][0]  = new Double(3.5);
        this.xEnd[2][0]  = new Double(4.0);

        this.yValues[0][0]  = new Double(3.0);
        this.yValues[1][0]  = new Double(4.5);
        this.yValues[2][0]  = new Double(2.5);
    }

   
    public SimpleIntervalXYDataset(int length, double[] xstart, double[] xend, double[] y) {
     count = length;
     seriesCount = 1;
     serieKeys = new String[1];
 	 serieKeys[0]= "Serie 1";
 	
    	xStart= new Double[length][1];
    	xEnd = new Double[length][1];;
    	yValues = new Double[length][1];;
    	
    	for (int i = 0; i<length; i++){
    		xStart[i][0] = new Double(xstart[i]);
    		xEnd[i][0] = new Double(xend[i]);
    		yValues[i][0] = new Double(y[i]);
    	}    	
    }
 
    public SimpleIntervalXYDataset(int length, double[] xstart, double[] xend, int[] y) {
        count = length;
        seriesCount = 1;
        serieKeys = new String[1];
    	serieKeys[0]= "Serie 1";
    	
       	xStart= new Double[length][1];
       	xEnd = new Double[length][1];
       	yValues = new Double[length][1];
       	
       	for (int i = 0; i<length; i++){
       		xStart[i][0] = new Double(xstart[i]);
       		xEnd[i][0] = new Double(xend[i]);
       		yValues[i][0] = new Double(y[i]);
       	}    	
       }
    
     public void add(int length, double[] xstart, double[] xend, int[] y){
    	 seriesCount++;

    	 serieKeys = new String[seriesCount];
    	 for (int j=0; j<seriesCount; j++)	
    		 serieKeys[j]= "Serie "+j;
     	
    	 Double[][] t_xStart= new Double[length][seriesCount];
    	 Double[][] t_xEnd = new Double[length][seriesCount];
    	 Double[][] t_yValues = new Double[length][seriesCount];
      
    	 for (int j=0; j<seriesCount-1; j++)	
    		 for (int i = 0; i<length; i++){
        		t_xStart[i][j] = xStart[i][j];
        		t_xEnd[i][j] = xEnd[i][j];
        		t_yValues[i][j] = yValues[i][j];
    		 }
    	 for (int i = 0; i<length; i++){
     		t_xStart[i][seriesCount-1] = new Double(xstart[i]);
     		t_xEnd[i][seriesCount-1] = new Double(xend[i]);
     		t_yValues[i][seriesCount-1] =new Double(y[i]);
 		 }
    	 
    	xStart= new Double[length][seriesCount];
    	xEnd = new Double[length][seriesCount];
    	yValues = new Double[length][seriesCount];
     
    	 for (int j=0; j<seriesCount; j++)	
    		 for (int i = 0; i<length; i++){
        		xStart[i][j] = t_xStart[i][j];
        		xEnd[i][j] = t_xEnd[i][j];
        		yValues[i][j] = t_yValues[i][j];
    		 }
     }
    /**
     * Returns the number of series in the dataset.
     *
     * @return the number of series in the dataset.
     */
    public int getSeriesCount() {
        return seriesCount;
    }

    /**
     * Returns the key for a series.
     *
     * @param series the series (zero-based index).
     *
     * @return The series key.
     */
 
    public Comparable getSeriesKey(int series) {
    	 
    	return serieKeys[series];
    }

    public void setSeriesKey(int index, String name ) {
       serieKeys[index] = name;
    }
    


    /**
     * Returns the number of items in a series.
     *
     * @param series the series (zero-based index).
     *
     * @return the number of items within a series.
     */
    public int getItemCount(int series) {
        return count;
    }

    /**
     * Returns the x-value for an item within a series.
     * <P>
     * The implementation is responsible for ensuring that the x-values are presented in ascending
     * order.
     *
     * @param series  the series (zero-based index).
     * @param item  the item (zero-based index).
     *
     * @return  the x-value for an item within a series.
     */
    public Number getX(int series, int item) {
        return this.xStart[item][series];
    }

    /**
     * Returns the y-value for an item within a series.
     *
     * @param series  the series (zero-based index).
     * @param item  the item (zero-based index).
     *
     * @return the y-value for an item within a series.
     */
    public Number getY(int series, int item) {
        return this.yValues[item][series];
    }

    /**
     * Returns the starting X value for the specified series and item.
     *
     * @param series  the series (zero-based index).
     * @param item  the item within a series (zero-based index).
     *
     * @return The value.
     */
    public Number getStartX(int series, int item) {
        return this.xStart[item][series];
    }

    /**
     * Returns the ending X value for the specified series and item.
     *
     * @param series  the series (zero-based index).
     * @param item  the item within a series (zero-based index).
     *
     * @return the end x value.
     */
    public Number getEndX(int series, int item) {
        return this.xEnd[item][series];
    }

    /**
     * Returns the starting Y value for the specified series and item.
     *
     * @param series  the series (zero-based index).
     * @param item  the item within a series (zero-based index).
     *
     * @return The value.
     */
    public Number getStartY(int series, int item) {
        return this.yValues[item][series];
    }

    /**
     * Returns the ending Y value for the specified series and item.
     *
     * @param series  the series (zero-based index).
     * @param item  the item within a series (zero-based index).
     *
     * @return The value.
     */
    public Number getEndY(int series, int item) {
        return this.yValues[item][series];
    }

    /**
     * Registers an object for notification of changes to the dataset.
     *
     * @param listener  the object to register.
     */
    public void addChangeListener(DatasetChangeListener listener) {
        // ignored
    }

    /**
     * Deregisters an object for notification of changes to the dataset.
     *
     * @param listener  the object to deregister.
     */
    public void removeChangeListener(DatasetChangeListener listener) {
        // ignored
    }

}
