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

import edu.ucla.loni.LOVE.colormap.ColorMap;
import edu.ucla.loni.LOVE.colormap.plugins.SpecialSpectralColorMap;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.image.ColorModel;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Dec 1, 2008
 * Time: 10:43:13 PM
 *
 * @author Jameel
 */
public class MotionDataSet extends AbstractXYZDataset implements XYZDataset, TableModelListener
{
    protected MotionTableModel mtmodel;
    protected MotionKey[] keys;
    protected TreeSet[] mappingKeys = new TreeSet[4];
    protected HashMap[] mappings = new HashMap[4];
    private double sizeDivisor = Double.MAX_VALUE;

    protected static final ColorMap colorMap;
    protected static final ColorModel colorModel;
    protected static final float SIZE_MULTIPLIER = 1.0f;

    static
    {
        colorMap = new SpecialSpectralColorMap(512, 16);
        colorModel = colorMap.getColorModel();
    }

    /**
     * Creates a new MotionDataSet instance.
     *
     * @param model the table model of type MotionTableModel
     */
    public MotionDataSet(MotionTableModel model)
    {
        mtmodel = model;
        model.addTableModelListener(this);

        setKeys();
        setMappingKeys();
        calculateSizeDivisor();
    }

    protected void setKeys()
    {
        keys = mtmodel.getKeyMap().keySet().toArray(new MotionKey[0]);
    }

    protected void setMappingKeys()
    {
        Integer column = mtmodel.getXAxisMapping();
        if(column != null)
        {
            setMappingKeys(0,column);
            if(mtmodel.getColumnClass(column) == String.class)
            {
                setStringMapping(0, column);
            }
        }

        column = mtmodel.getYAxisMapping();
        if(column != null)
        {
            setMappingKeys(1,column);
            if(mtmodel.getColumnClass(column) == String.class)
            {
                setStringMapping(1, column);
            }
        }

        column = mtmodel.getSizeMapping();
        if(column != null)
        {
            setMappingKeys(2,column);
            if(mtmodel.getColumnClass(column) == String.class)
            {
                setStringMapping(2, column);
            }
        }

        column = mtmodel.getColorMapping();
        if(column != null)
        {
            setMappingKeys(3,column);
            if(mtmodel.getColumnClass(column) == String.class)
            {
                setStringMapping(3, column);
            }
        }
    }

    protected void setMappingKeys(int index, int column)
    {
        mappingKeys[index] = new TreeSet();

        for(int r = 0; r < mtmodel.getRowCount(); r++)
        {
            Object obj = mtmodel.getValueAt(r, column);
            if(obj == null || obj.equals(""))
            {
                continue;
            }
            mappingKeys[index].add(obj);
        }
    }

    protected void setStringMapping(int index, int column)
    {
        mappings[index] = new HashMap();
        Iterator itr = mappingKeys[index].iterator();
        int i = 1;

        while(itr.hasNext())
        {
            mappings[index].put(itr.next(), i);
            i++;
        }
    }

    /**
     * Returns the number of series in the dataset.
     *
     * @return The series count.
     */
    public int getSeriesCount()
    {
        return mtmodel.getKeyMap().size();
    }

    /**
     * Returns the key for a series.
     * <p/>
     * If <code>series</code> is not within the specified range, the
     * implementing method should throw an {@link IndexOutOfBoundsException}
     * (preferred) or an {@link IllegalArgumentException}.
     *
     * @param series the series index (in the range <code>0</code> to
     *               <code>getSeriesCount() - 1</code>).
     * @return The series key.
     */
    public Comparable getSeriesKey(int series)
    {
        if(series < 0 || series >= mtmodel.getKeyMap().size())
        {
            throw new IndexOutOfBoundsException();
        }

        return keys[series];
    }

    /**
     * Returns the number of items in a series.
     * <br><br>
     * It is recommended that classes that implement this method should throw
     * an <code>IllegalArgumentException</code> if the <code>series</code>
     * argument is outside the specified range.
     *
     * @param series the series index (in the range <code>0</code> to
     *               <code>getSeriesCount() - 1</code>).
     * @return The item count.
     */
    public int getItemCount(int series)
    {
        return mtmodel.getKeyMap().get(getSeriesKey(series)).size();
    }

    /**
     * Returns the x-value (as a double primitive) for an item within a series.
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     * @return The value.
     */
    @Override
    public double getXValue(int series, int item)
    {
        Number x = getX(series, item);
        if(x == null)
            return 0.0;
        return x.doubleValue();
    }

    /**
     * Returns the y-value (as a double primitive) for an item within a series.
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     * @return The value.
     */
    @Override
    public double getYValue(int series, int item)
    {
        Number y = getY(series, item);
        if(y == null)
            return 0.0;
        return y.doubleValue();
    }

    /**
     * Returns the z-value (as a double primitive) for an item within a series.
     *
     * @param series the series (zero-based index).
     * @param item   the item (zero-based index).
     * @return The z-value.
     */
    @Override
    public double getZValue(int series, int item)
    {
        Number z = getZ(series, item);
        if(z == null)
            return 0.0;
        return z.doubleValue();
    }

    /**
     * Returns the x-value for an item within a series.  The x-values may or
     * may not be returned in ascending order, that is up to the class
     * implementing the interface.
     *
     * @param series the series index (in the range <code>0</code> to
     *               <code>getSeriesCount() - 1</code>).
     * @param item   the item index (in the range <code>0</code> to
     *               <code>getItemCount(series)</code>).
     * @return The x-value (possibly <code>null</code>).
     */
    public Number getX(int series, int item)
    {
        Integer col = mtmodel.getXAxisMapping();
        Object value = getItem(series, item, col);

        if(value == null)
        {
            return null;
        }

        if(value instanceof Date)
        {
            return ((Date)value).getTime();
        }
        else if(value instanceof Double)
        {
            return (Double)value;
        }
        else
        {
            return (Integer)mappings[0].get(value);
        }
    }

    /**
     * Returns the y-value for an item within a series.
     *
     * @param series the series index (in the range <code>0</code> to
     *               <code>getSeriesCount() - 1</code>).
     * @param item   the item index (in the range <code>0</code> to
     *               <code>getItemCount(series)</code>).
     * @return The y-value (possibly <code>null</code>).
     */
    public Number getY(int series, int item)
    {
        Integer col = mtmodel.getYAxisMapping();
        Object value = getItem(series, item, col);

        if(value == null)
        {
            return null;
        }

        if(value instanceof Date)
        {
            return ((Date)value).getTime();
        }
        else if(value instanceof Double)
        {
            return (Double)value;
        }
        else
        {
            return (Integer)mappings[1].get(value);
        }
    }

    /**
     * Returns the z-value for the specified series and item.
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     * @return The z-value (possibly <code>null</code>).
     */
    public Number getZ(int series, int item)
    {
        Integer col = mtmodel.getSizeMapping();
        Object value = getItem(series, item, col);

        if(value == null)
        {
            return null;
        }

        double sizeOffset = getSizeOffset();

        if(value instanceof Date)
        {
            return ((((Date)value).getTime() + sizeOffset) / sizeDivisor) * SIZE_MULTIPLIER;
        }
        else if(value instanceof Double)
        {
            return (((Double)value + sizeOffset) / sizeDivisor * SIZE_MULTIPLIER);
        }
        else
        {
            return (Integer)mappings[2].get(value) / sizeDivisor * SIZE_MULTIPLIER;
        }
    }

    /**
     * Returns the size for the specified item in the series.
     * This method defaults to calling <code>getZ(series, item)</code>.
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     * @return The size (possibly <code>null</code>).
     */
    public Number getSize(int series, int item)
    {
        return getZ(series, item);
    }

    protected void calculateSizeDivisor()
    {
        Integer sizeCol = mtmodel.getSizeMapping();

        if(sizeCol == null)
        {
            return;
        }

        Class sizeClass = mtmodel.getColumnClass(sizeCol);
        DoubleValue val;

        if(sizeClass == Date.class)
        {
            val = new DateConverter();
        }
        else if(sizeClass == Double.class)
        {
            val = new DoubleConverter();
        }
        else if(sizeClass == String.class)
        {
            val = new StringConverter();
        }
        else
        {
            sizeDivisor = Double.MAX_VALUE;
            return;
        }

        double max = 0;
        double sizeOffset = getSizeOffset();
        int seriesCount = getSeriesCount();

        for(int s = 0; s < seriesCount; s++)
        {
            int itemCount = getItemCount(s);
            double sum = 0;

            for(int i = 0; i < itemCount; i++)
            {
                Object value = getItem(s,i,sizeCol);

                if(value == null)
                    continue;

                sum += val.getDoubleValue(value, 2);
            }

            sum += itemCount * sizeOffset;

            max = (sum > max) ? sum : max;
        }

        sizeDivisor = (max > 0) ? max : Double.MAX_VALUE;
    }

    protected double getSizeOffset()
    {
        Integer col = mtmodel.getSizeMapping();
        Class colClass;
        double offset;

        if(col == null)
        {
            return 0.0;
        }

        colClass = mtmodel.getColumnClass(col);

        if(colClass == Date.class)
        {
            offset = ((Date)mappingKeys[2].first()).getTime(); // Calculate the offset for negative values
        }
        else if(colClass == Double.class)
        {
            offset = (Double)mappingKeys[2].first(); // Calculate the offset for negative values
        }
        else
        {
            offset = 0.0;
        }

        return (offset < 0) ? 2*Math.abs(offset) : 0.0;
    }

    /**
     * Returns the color for the specified item in the series.
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     * @return The color (possibly <code>null</code>).
     */
    public Color getColor(int series, int item)
    {
        Integer col = mtmodel.getColorMapping();
        Object value = getItem(series, item, col);
        double percent;
        int index;

        if(value == null)
        {
            return null;
        }

        if(value instanceof Date)
        {
            long offset = ((Date)mappingKeys[3].first()).getTime(); // Calculate the offset for negative values
            offset = (offset < 0) ? Math.abs(offset) : 0;
            long max = ((Date)mappingKeys[3].last()).getTime() + offset;
            percent = (((Date)value).getTime() + offset) / (double)max;
        }
        else if(value instanceof Double)
        {
            double offset = (Double)mappingKeys[3].first(); // Calculate the offset for negative values
            offset = (offset < 0) ? Math.abs(offset) : 0;
            double max = (Double)mappingKeys[3].last() + offset;
            percent = ((Double)value + offset) / max;
        }
        else
        {
            Object[] keys = mappingKeys[3].toArray();
            int max = (Integer)mappings[3].get(keys[keys.length - 1]);
            percent = (Integer)mappings[3].get(value) / (double)max;
        }

        index = (int)(percent * 511);

        return new Color(colorModel.getRGB(index));
    }

    /**
     * Returns the category for the specified item in the series.
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     * @return The category (possibly <code>null</code>).
     */
    public Object getCategory(int series, int item)
    {
        Integer col = mtmodel.getCategoryMapping();

        return getItem(series, item, col);
    }

     /**
     * Returns the x-axis label for the dataset.
     *
     * @return The x-axis label (possibly <code>null</code>).
     */
    public String getXLabel()
    {
        Integer col = mtmodel.getXAxisMapping();

        if(col == null)
        {
            return null;
        }

        return mtmodel.getColumnName(col);
    }

    /**
     * Returns the y-axis label for the dataset.
     *
     * @return The y-axis label (possibly <code>null</code>).
     */
    public String getYLabel()
    {
        Integer col = mtmodel.getYAxisMapping();

        if(col == null)
        {
            return null;
        }

        return mtmodel.getColumnName(col);
    }

    /**
     * Returns the z-axis label for the dataset.
     *
     * @return The z-axis label (possibly <code>null</code>).
     */
    public String getZLabel()
    {
        Integer col = mtmodel.getSizeMapping();

        if(col == null)
        {
            return null;
        }

        return mtmodel.getColumnName(col);
    }

    /**
     * Returns the size label for the dataset.
     * This method defaults to calling <code>getZLabel()</code>.
     *
     * @return The size label (possibly <code>null</code>).
     */
    public String getSizeLabel()
    {
        return getZLabel();
    }

    /**
     * Returns the color label for the dataset.
     *
     * @return The color label (possibly <code>null</code>).
     */
    public String getColorLabel()
    {
        Integer col = mtmodel.getColorMapping();

        if(col == null)
        {
            return null;
        }

        return mtmodel.getColumnName(col);
    }

    /**
     * Returns the category label for the dataset.
     *
     * @return The category label (possibly <code>null</code>).
     */
    public String getCategoryLabel()
    {
        Integer col = mtmodel.getCategoryMapping();

        if(col == null)
        {
            return null;
        }

        return mtmodel.getColumnName(col);
    }

    /**
     * Returns the table model for the dataset.
     *
     * @return The table model (never <code>null</code>).
     */
    public MotionTableModel getTableModel()
    {
        return mtmodel;
    }

    /**
     * Returns the column item for the specified item in the series.
     *
     * @param series the series index (zero-based).
     * @param item the item index (zero-based).
     * @param col the column index (zero-based).
     * @return The item (possibly <code>null</code>).
     */
    protected Object getItem(int series, int item, Integer col)
    {
        Object key = getSeriesKey(series);

        if(col != null)
        {
            Integer row = mtmodel.getKeyMap().get(key).get(item);
            return mtmodel.getValueAt(row, col);
        }

        return null;
    }

    /**
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed.
     */
    public void tableChanged(TableModelEvent e)
    {
        if(e.getSource() != mtmodel)
            return;

        setKeys();
        setMappingKeys();
        calculateSizeDivisor();
        notifyListeners(new DatasetChangeEvent(this, this));
    }

    protected interface DoubleValue<T>
    {
        public Double getDoubleValue(T obj, int index);
    }

    protected class DateConverter implements DoubleValue<Date>
    {
        public Double getDoubleValue(Date obj, int index)
        {
            return new Double(obj.getTime());
        }
    }

    protected class DoubleConverter implements DoubleValue<Double>
    {
        public Double getDoubleValue(Double obj, int index)
        {
            return obj;
        }
    }

    protected class StringConverter implements DoubleValue<String>
    {
        public Double getDoubleValue(String obj, int index)
        {
            return new Double((Integer)mappings[index].get(obj));
        }
    }
}
