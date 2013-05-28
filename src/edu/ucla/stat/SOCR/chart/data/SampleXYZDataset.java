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

import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;

/**
 * A quick-and-dirty implementation of the {@link XYZDataset interface}.  
 * Hard-coded and not useful beyond the demo.
 */
public class SampleXYZDataset extends AbstractXYZDataset implements XYZDataset {

    /**
	 * The x values.
	 * @uml.property  name="xVal" multiplicity="(0 -1)" dimension="1"
	 */
    private double[] xVal = {2.1, 2.375625, 2.375625, 2.232928726, 2.232928726, 
        1.860415253, 1.840842668, 1.905415253, 2.336029412, 3.8};

    /**
	 * The y values.
	 * @uml.property  name="yVal" multiplicity="(0 -1)" dimension="1"
	 */
    private double[] yVal = {14.168, 11.156, 10.089, 8.884, 8.719, 8.466, 5.489,
                             4.107, 4.101, 25};

    /**
	 * The z values.
	 * @uml.property  name="zVal" multiplicity="(0 -1)" dimension="1"
	 */
    private double[] zVal = {2.45, 2.791285714, 2.791285714, 2.2125, 2.2125, 
                             2.22, 2.1, 2.22, 1.64875, 4};

    /**
     * Returns the number of series in the dataset.
     *
     * @return The series count.
     */
    public int getSeriesCount() {
        return 1;
    }

    /**
     * Returns the key for a series.
     *
     * @param series  the series (zero-based index).
     *
     * @return The key for the series.
     */
    public Comparable getSeriesKey(int series) {
        return "Series 1";
    }

    /**
     * Returns the number of items in a series.
     *
     * @param series  the series (zero-based index).
     *
     * @return The number of items within the series.
     */
    public int getItemCount(final int series) {
        return this.xVal.length;
    }

    /**
     * Returns the x-value for an item within a series.
     * <P>
     * The implementation is responsible for ensuring that the x-values are
     * presented in ascending order.
     *
     * @param series  the series (zero-based index).
     * @param item  the item (zero-based index).
     *
     * @return The x-value.
     */
    public Number getX(int series, int item) {
        return new Double(this.xVal[item]);
    }

    /**
     * Returns the y-value for an item within a series.
     *
     * @param series  the series (zero-based index).
     * @param item  the item (zero-based index).
     *
     * @return The y-value.
     */
    public Number getY(int series, int item) {
        return new Double(this.yVal[item]);
    }

    /**
     * Returns the z-value for the specified series and item.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The value.
     */
    public Number getZ(final int series, final int item) {
        return new Double(this.zVal[item]);
    }
    
}
