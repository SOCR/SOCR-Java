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

import edu.ucla.stat.SOCR.util.tablemodels.RowComparator;
import edu.ucla.stat.SOCR.util.tablemodels.SortedTableModel;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Dec 2, 2008
 * Time: 12:37:16 AM
 *
 * @author Jameel
 */
public class MotionTableModel extends SortedTableModel
{
    private Integer[] columnMapping = new Integer[6];
    private TreeMap<MotionKey, ArrayList<Integer>> keyMap;
    private Class[] columnClasses;
    private String[] parseStrings;
    private static final Double DEFAULT_KEY = 0.0;

    /**
     * Creates <code>MotionTableModel</code> object with specified <code>TableModel</code>
     * as the data source.
     *
     * @param model <code>TableModel</code> used as the data source
     */
    public MotionTableModel(TableModel model)
    {
        super(model);
        parseStrings = new String[getColumnCount()];
        setColumnClasses();
        setRowComparator(new MotionRowComparator());
        setTreeMappings();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object value = super.getValueAt(rowIndex, columnIndex);

        return MotionTypeParser.parseType(value, columnClasses[columnIndex], parseStrings[columnIndex]);
    }

    @Override
    public Class getColumnClass(int columnIndex)
    {
        return columnClasses[columnIndex];
    }

    public Integer getKeyMapping()
    {
        return (columnMapping[0] != null &&  columnMapping[0] < 0) ? null : columnMapping[0];
    }

    public void setKeyMapping(int columnIndex)
    {
        columnMapping[0] = columnIndex;
        setTreeMappings();
        fireTableChanged(new TableModelEvent(this));
    }

    public Integer getXAxisMapping()
    {
        return (columnMapping[1] != null &&  columnMapping[1] < 0) ? null : columnMapping[1];
    }

    public void setXAxisMapping(int columnIndex)
    {
        columnMapping[1] = columnIndex;
        fireTableChanged(new TableModelEvent(this));
    }

    public Integer getYAxisMapping()
    {
        return (columnMapping[2] != null &&  columnMapping[2] < 0) ? null : columnMapping[2];
    }

    public void setYAxisMapping(int columnIndex)
    {
        columnMapping[2] = columnIndex;
        fireTableChanged(new TableModelEvent(this));
    }

    public Integer getSizeMapping()
    {
        return (columnMapping[3] != null &&  columnMapping[3] < 0) ? null : columnMapping[3];
    }

    public void setSizeMapping(int columnIndex)
    {
        columnMapping[3] = columnIndex;
        fireTableChanged(new TableModelEvent(this));
    }

    public Integer getColorMapping()
    {
        return (columnMapping[4] != null &&  columnMapping[4] < 0) ? null : columnMapping[4];
    }

    public void setColorMapping(int columnIndex)
    {
        columnMapping[4] = columnIndex;
        fireTableChanged(new TableModelEvent(this));
    }

    public Integer getCategoryMapping()
    {
        return (columnMapping[5] != null &&  columnMapping[5] < 0) ? null : columnMapping[5];
    }

    public void setCategoryMapping(int columnIndex)
    {
        columnMapping[5] = columnIndex;
        fireTableChanged(new TableModelEvent(this));
    }

    public void setColumnParseString(String parseString, int columnIndex)
    {
        parseStrings[columnIndex] = parseString;
        tableChanged(new TableModelEvent(this));
    }

    public String getColumnParseString(int columnIndex)
    {
        return parseStrings[columnIndex];
    }

    public TreeMap<MotionKey, ArrayList<Integer>> getKeyMap()
    {
        return keyMap;
    }

    private void setTreeMappings()
    {
        keyMap = new TreeMap<MotionKey, ArrayList<Integer>>();
        Integer timeCol = getKeyMapping();

        for(int r = 0; r < getRowCount(); r++)
        {
            Object key;

            if(timeCol == null)
            {
                key = DEFAULT_KEY;
            }
            else
            {
                key = getValueAt(r, timeCol);
            }

            if(key == null)
            {
                continue;
            }

            MotionKey mkey = new MotionKey(key);

            if(!keyMap.containsKey(mkey))
            {
                keyMap.put(mkey, new ArrayList<Integer>());
            }

            keyMap.get(mkey).add(r);
        }
    }

    protected void setColumnClasses()
    {
        columnClasses = new Class[getColumnCount()];
        for(int c = 0; c < getColumnCount(); c++)
        {
            Class objClass = super.getColumnClass(c);

            if(objClass == Object.class)
            {
                objClass = MotionTypeParser.getColumnClass(model, c, parseStrings[c]);
            }

            columnClasses[c] = objClass;
        }
    }

    protected void checkMappings()
    {
        int columnCount = getColumnCount();

        if(columnMapping[0] != null && columnMapping[0] >= columnCount)
        {
            columnMapping[0] = null;
        }

        if(columnMapping[1] != null && columnMapping[1] >= columnCount)
        {
            columnMapping[1] = null;
        }

        if(columnMapping[2] != null && columnMapping[2] >= columnCount)
        {
            columnMapping[2] = null;
        }

        if(columnMapping[3] != null && columnMapping[3] >= columnCount)
        {
            columnMapping[3] = null;
        }

        if(columnMapping[4] != null && columnMapping[4] >= columnCount)
        {
            columnMapping[4] = null;
        }

        if(columnMapping[5] != null && columnMapping[5] >= columnCount)
        {
            columnMapping[5] = null;
        }
    }

    @Override
    public void tableChanged(TableModelEvent e)
    {
        checkMappings();

        super.tableChanged(e);

        if(e.getFirstRow() == TableModelEvent.HEADER_ROW)
            parseStrings = new String[getColumnCount()];

        setColumnClasses();
        setTreeMappings();
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Notifies all listeners that all cell values in the table's
     * rows may have changed. The number of rows may also have changed
     * and the <code>JTable</code> should redraw the
     * table from scratch. The structure of the table (as in the order of the
     * columns) is assumed to be the same.
     *
     * @see javax.swing.event.TableModelEvent
     * @see javax.swing.event.EventListenerList
     * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
     */
    @Override
    public void fireTableDataChanged()
    {
        return; //Do nothing
    }

    protected class MotionRowComparator implements RowComparator
    {
        /**
         * Compares two rows for order. Returns a negative integer, zero,
         * or a positive integer as the first row is less than, equal to,
         * or greater than the second.
         *
         * @param row1       the first row to be compared
         * @param row2       the second row to be compared
         * @param column     sorting column
         * @param tableModel table's model
         * @return a negative integer, zero, or a positive integer as the
         *         first row is less than, equal to, or greater than the
         *         second.
         */
        public int compare(int row1, int row2, int column, TableModel tableModel)
        {
            Comparable o1 = (Comparable)MotionTypeParser.parseType(tableModel.getValueAt(row1, column), columnClasses[column]);
            Comparable o2 = (Comparable)MotionTypeParser.parseType(tableModel.getValueAt(row2, column), columnClasses[column]);

            // null is less than any object
            if (o1 == null && o2 == null)
                return 0;
            else if (o1 == null)
                return -1;
            else if (o2 == null)
                return 1;
            try {
                return o1.compareTo(o2);
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
