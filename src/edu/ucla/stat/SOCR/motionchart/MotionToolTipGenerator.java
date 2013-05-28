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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.motionchart;

import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.labels.XYZToolTipGenerator;
import org.jfree.data.xy.XYZDataset;
import org.jfree.util.ObjectUtilities;

import java.text.DateFormat;
import java.text.NumberFormat;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Dec 20, 2008
 * Time: 7:19:20 PM
 *
 * @author Jameel
 */
public class MotionToolTipGenerator extends StandardXYZToolTipGenerator implements XYZToolTipGenerator
{
    /** The default tooltip format. */
    public static final String DEFAULT_TOOL_TIP_FORMAT =
            "{0}: ({1}, {2}, {3}, {4}, {5})";

    /**
     * A number formatter for the key value - if this is null, then keyDateFormat
     * must be non-null.
     */
    private NumberFormat keyFormat;

    /**
     * A date formatter for the key - if this is null, then keyFormat must be
     * non-null.
     */
    private DateFormat keyDateFormat;

    /**
     * A number formatter for the color value - if this is null, then colorDateFormat
     * must be non-null.
     */
    private NumberFormat colorFormat;

    /**
     * A date formatter for the color - if this is null, then colorFormat must be
     * non-null.
     */
    private DateFormat colorDateFormat;

    /**
     * Creates a new tool tip generator using default number formatters for the
     * x, y and z-values.
     */
    public MotionToolTipGenerator()
    {
        this(
            DEFAULT_TOOL_TIP_FORMAT,
            NumberFormat.getNumberInstance(),
            NumberFormat.getNumberInstance(),
            NumberFormat.getNumberInstance(),
            NumberFormat.getNumberInstance(),
            NumberFormat.getNumberInstance()
        );
    }

    /**
     * Constructs a new tool tip generator using the specified number
     * formatters.
     *
     * @param formatString the format string.
     * @param keyFormat    the format object for the key values (<code>null</code>
     *                     not permitted).
     * @param xFormat      the format object for the x values (<code>null</code>
     *                     not permitted).
     * @param yFormat      the format object for the y values (<code>null</code>
     *                     not permitted).
     * @param zFormat   the format object for the size values (<code>null</code>
     *                     not permitted).
     * @param colorFormat  the format object for the color values (<code>null</code>
     *                     not permitted).
     */
    public MotionToolTipGenerator(String formatString, NumberFormat keyFormat, NumberFormat xFormat,
                                  NumberFormat yFormat, NumberFormat zFormat, NumberFormat colorFormat)
    {
        super(formatString, xFormat, yFormat, zFormat);
        if (keyFormat == null) {
            throw new IllegalArgumentException("Null 'keyFormat' argument.");
        }
        if (colorFormat == null) {
            throw new IllegalArgumentException("Null 'colorFormat' argument.");
        }
        this.keyFormat = keyFormat;
        this.colorFormat = colorFormat;
    }

    /**
     * Constructs a new tool tip generator using the specified date formatters.
     *
     * @param formatString the format string.
     * @param keyFormat    the format object for the key values (<code>null</code>
     *                     not permitted).
     * @param xFormat      the format object for the x values (<code>null</code>
     *                     not permitted).
     * @param yFormat      the format object for the y values (<code>null</code>
     *                     not permitted).
     * @param zFormat   the format object for the size values (<code>null</code>
     *                     not permitted).
     * @param colorFormat  the format object for the color values (<code>null</code>
     *                     not permitted).
     */
    public MotionToolTipGenerator(String formatString, DateFormat keyFormat, DateFormat xFormat,
                                  DateFormat yFormat, DateFormat zFormat, DateFormat colorFormat)
    {
        super(formatString, xFormat, yFormat, zFormat);
        if (keyFormat == null) {
            throw new IllegalArgumentException("Null 'keyFormat' argument.");
        }
        if (colorFormat == null) {
            throw new IllegalArgumentException("Null 'colorFormat' argument.");
        }
        this.keyDateFormat = keyFormat;
        this.colorDateFormat = colorFormat;
    }

    /**
     * Returns the number formatter for the key values.
     *
     * @return The number formatter (possibly <code>null</code>).
     */
    public NumberFormat getKeyFormat() {
        return this.keyFormat;
    }

    /**
     * Returns the date formatter for the key values.
     *
     * @return The date formatter (possibly <code>null</code>).
     */
    public DateFormat getKeyDateFormat() {
        return this.keyDateFormat;
    }

    /**
     * Returns the number formatter for the color values.
     *
     * @return The number formatter (possibly <code>null</code>).
     */
    public NumberFormat getColorFormat() {
        return this.colorFormat;
    }

    /**
     * Returns the date formatter for the color values.
     *
     * @return The date formatter (possibly <code>null</code>).
     */
    public DateFormat getColorDateFormat() {
        return this.colorDateFormat;
    }

    /**
     * Creates the array of items that can be passed to the
     * {@link java.text.MessageFormat} class for creating labels.
     *
     * @param dataset the dataset (<code>null</code> not permitted).
     * @param series  the series (zero-based index).
     * @param item    the item (zero-based index).
     * @return The items (never <code>null</code>).
     */
    @Override
    protected Object[] createItemArray(XYZDataset dataset, int series, int item)
    {
        MotionDataSet mds = (MotionDataSet)dataset;
        MotionTableModel model = mds.getTableModel();
        Object[] result = new Object[6];
        Object key = mds.getItem(series, item, model.getKeyMapping());
        Object x = mds.getItem(series, item, model.getXAxisMapping());
        Object y = mds.getItem(series, item, model.getYAxisMapping());
        Object size = mds.getItem(series, item, model.getSizeMapping());
        Object color = mds.getItem(series, item, model.getColorMapping());
        Object category = mds.getItem(series, item, model.getCategoryMapping());

        result[0] = (key != null) ? key.toString() : null;
        result[1] = (x != null) ? x.toString() : null;
        result[2] = (y != null) ? y.toString() : null;
        result[3] = (size != null) ? size.toString() : null;
        result[4] = (color != null) ? color.toString() : null;
        result[5] = (category != null) ? category.toString() : null;

//        Number x = dataset.getX(series, item);
//        DateFormat xf = getXDateFormat();
//        if (xf != null) {
//            result[1] = xf.format(x);
//        }
//        else {
//            result[1] = getXFormat().format(x);
//        }
//
//        Number y = dataset.getY(series, item);
//        DateFormat yf = getYDateFormat();
//        if (yf != null) {
//            result[2] = yf.format(y);
//        }
//        else {
//            result[2] = getYFormat().format(y);
//        }
//
//        Number z = dataset.getZ(series, item);
//        DateFormat zf = getZDateFormat();
//        if (zf != null) {
//            result[3] = zf.format(z);
//        }
//        else {
//            result[3] = getZFormat().format(z);
//        }

        return result;
    }

    /**
     * Tests this object for equality with an arbitrary object.
     *
     * @param obj the other object (<code>null</code> permitted).
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardXYZToolTipGenerator)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        MotionToolTipGenerator that = (MotionToolTipGenerator) obj;
        if (!ObjectUtilities.equal(this.keyFormat, that.keyFormat)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.keyDateFormat, that.keyDateFormat)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.colorFormat, that.colorFormat)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.colorDateFormat, that.colorDateFormat)) {
            return false;
        }
        return true;
    }
}
