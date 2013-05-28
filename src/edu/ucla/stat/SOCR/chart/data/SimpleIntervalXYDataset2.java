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
public class SimpleIntervalXYDataset2 extends AbstractIntervalXYDataset 
                                      implements IntervalXYDataset {

    /**
	 * The start values.
	 * @uml.property  name="yStart" multiplicity="(0 -1)" dimension="1"
	 */
    private Double[] yStart;
    
    /**
	 * The end values.
	 * @uml.property  name="yEnd" multiplicity="(0 -1)" dimension="1"
	 */
    private Double[] yEnd = new Double[3];

    /**
	 * The x values.
	 * @uml.property  name="xValues" multiplicity="(0 -1)" dimension="1"
	 */
    private Double[] xValues = new Double[3];
    
    private String serieKey="Serie 1";

    /**
     * Creates a new dataset.
     * 
     * @param itemCount  the number of items to generate.
     */
    public SimpleIntervalXYDataset2(int itemCount) {

        this.xValues = new Double[itemCount];
        this.yStart = new Double[itemCount];
        this.yEnd = new Double[itemCount];
        
        double base = 100;
        for (int i = 1; i <= itemCount; i++) {
            this.xValues[i - 1] = new Double(i);
            base = base * (1 + (Math.random() / 10 - 0.05));
            this.yStart[i - 1] = new Double(base);
            this.yEnd[i - 1] = new Double(this.yStart[i - 1].doubleValue() + Math.random() * 30);

        }
		
    }

  public SimpleIntervalXYDataset2(int itemCount, double x[], double y1[], double y2[]) {

        this.xValues = new Double[itemCount];
        this.yStart = new Double[itemCount];
        this.yEnd = new Double[itemCount];
        
        for (int i = 0; i < itemCount; i++) {
            this.xValues[i] = new Double(x[i]);
			this.yStart[i] = new Double(y1[i]);
			this.yEnd[i] = new Double(y2[i]);
        }
	
    }

    /**
     * Returns the number of series in the dataset.
     *
     * @return the number of series in the dataset.
     */
    public int getSeriesCount() {
        return 1;
    }

    /**
     * Returns the key for a series.
     *
     * @param series the series (zero-based index).
     *
     * @return The series key.
     */
    public Comparable getSeriesKey(int series) {
    	 
    	return serieKey;
    }

    public void setSeriesKey(String name ) {
       serieKey = name;
    }
    
    /**
     * Returns the number of items in a series.
     *
     * @param series the series (zero-based index).
     *
     * @return the number of items within a series.
     */
    public int getItemCount(int series) {
        return this.xValues.length;
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
        return this.xValues[item];
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
        return this.yEnd[item];
    }

    /**
     * Returns the starting X value for the specified series and item.
     *
     * @param series  the series (zero-based index).
     * @param item  the item within a series (zero-based index).
     *
     * @return the start x value.
     */
    public Number getStartX(int series, int item) {
        return this.xValues[item];
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
        return this.xValues[item];
    }

    /**
     * Returns the starting Y value for the specified series and item.
     *
     * @param series  the series (zero-based index).
     * @param item  the item within a series (zero-based index).
     *
     * @return the start y value.
     */
    public Number getStartY(int series, int item) {
        return this.yStart[item];
    }

    /**
     * Returns the ending Y value for the specified series and item.
     *
     * @param series  the series (zero-based index).
     * @param item  the item within a series (zero-based index).
     *
     * @return the end y value.
     */
    public Number getEndY(int series, int item) {
        return this.yEnd[item];
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
