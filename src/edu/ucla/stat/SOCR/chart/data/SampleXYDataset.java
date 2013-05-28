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

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * A dummy dataset for an XY plot.
 * <P>
 * Note that the aim of this class is to create a self-contained data source for demo purposes -
 * it is NOT intended to show how you should go about writing your own datasets.
 */
public class SampleXYDataset extends AbstractXYDataset implements XYDataset {

    /**
	 * Use the translate to change the data and demonstrate dynamic data changes.
	 * @uml.property  name="translate"
	 */
    private double translate;

    /**
     * Default constructor.
     */
    public SampleXYDataset() {
        this.translate = 0.0;
    }

    /**
     * Returns the translation factor.
     *
     * @return  the translation factor.
     */
    public double getTranslate() {
        return this.translate;
    }

    /**
     * Sets the translation constant for the x-axis.
     *
     * @param translate  the translation factor.
     */
    public void setTranslate(double translate) {
        this.translate = translate;
        notifyListeners(new DatasetChangeEvent(this, this));
    }

    /**
     * Returns the x-value for the specified series and item.  Series are numbered 0, 1, ...
     *
     * @param series  the index (zero-based) of the series.
     * @param item  the index (zero-based) of the required item.
     *
     * @return the x-value for the specified series and item.
     */
    public Number getX(int series, int item) {
        return new Double(-10.0 + this.translate + (item / 10.0));
    }

    /**
     * Returns the y-value for the specified series and item.  Series are numbered 0, 1, ...
     *
     * @param series  the index (zero-based) of the series.
     * @param item  the index (zero-based) of the required item.
     *
     * @return the y-value for the specified series and item.
     */
    public Number getY(int series, int item) {
        if (series == 0) {
            return new Double(Math.cos(-10.0 + this.translate + (item / 10.0)));
        }
        else {
            return new Double(2 * (Math.sin(-10.0 + this.translate + (item / 10.0))));
        }
    }

    /**
     * Returns the number of series in the dataset.
     *
     * @return the number of series in the dataset.
     */
    public int getSeriesCount() {
        return 2;
    }

    /**
     * Returns the key for a series.
     *
     * @param series  the index (zero-based) of the series.
     *
     * @return The key for the series.
     */
    public Comparable getSeriesKey(int series) {
        if (series == 0) {
            return "y = cosine(x)";
        }
        else if (series == 1) {
            return "y = 2*sine(x)";
        }
        else {
            return "Error";
        }
    }

    /**
     * Returns the number of items in the specified series.
     *
     * @param series  the index (zero-based) of the series.
     * @return the number of items in the specified series.
     *
     */
    public int getItemCount(final int series) {
        return 200;
    }

}







