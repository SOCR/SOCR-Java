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

package edu.ucla.stat.SOCR.util.tablemodels;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.util.Vector;


/**
 * An <code>AbstractTableModel</code> implementation with filtering support.
 * It uses supplied {@link RowFilter} to filter table rows.
 * The original <code>TableModel</code> is used only as the data
 * source and is not modified.
 * <p>Sample usage:<pre><code>    // Old code:
 *    //    JTable jTable = new JTable(tableModel);
 *    // New code:
 *    JTable jTable = new JTable(new FilteredTableModel(tableModel));</code></pre>
 *
 * @see RowFilter
 */
public class FilteredTableModel extends TableModelMapping {
    private Vector rowMapping = new Vector();
    private RowFilter rowFilter;

    /**
     * Creates <code>FilteredTableModel</code> object with specified <code>TableModel</code>
     * as the data source.
     *
     * @param model <code>TableModel</code> used as the data source
     */
    public FilteredTableModel(TableModel model) {
        setModel(model);
    }

    /**
     * Returns <code>RowFilter</code> used to filter table rows.
     *
     * @return <code>RowFilter</code> used to filter table rows.
     */
    public RowFilter getRowFilter() {
        return rowFilter;
    }

    /**
     * Sets <code>RowFilter</code> used to filter table rows. Invokes {@link #filter()}
     * before return.
     *
     * @param rowFilter <code>RowFilter</code> used to filter table rows
     */
    public void setRowFilter(RowFilter rowFilter) {
        this.rowFilter = rowFilter;
        refreshRowMapping();
    }

    /**
     * Forces filtering of data contained in {@link #model}. The original {@link #model} is not modified.
     */
    public void filter() {
        refreshRowMapping();
    }

    /**
     * Returns row index in original {@link #model} by index in current model (view index).
     *
     * @param rowIndex index in current model (view index)
     * @return row index in original {@link #model}.
     */
    public int getRealRowIndex(int rowIndex) {
        return ((Number) rowMapping.get(rowIndex)).intValue();
    }


    //@exclude
    public void setModel(TableModel model) {
        super.setModel(model);
        refreshRowMapping();
    }

    //@exclude
    public Object getValueAt(int rowIndex, int columnIndex) {
        return model.getValueAt(((Number) rowMapping.get(rowIndex)).intValue(), columnIndex);
    }

    //@exclude
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        model.setValueAt(aValue, ((Number) rowMapping.get(rowIndex)).intValue(), columnIndex);
    }

    //@exclude
    public int getRowCount() {
        return rowMapping.size();
    }

    private void refreshRowMapping() {
        int rowCount = model.getRowCount();
        rowMapping.removeAllElements();
        if (rowFilter != null) {
            for (int i = 0; i < rowCount; i++)
                if (!rowFilter.exclude(i, model))
                    rowMapping.add(new Integer(i));
        } else {
            for (int i = 0; i < rowCount; i++)
                rowMapping.add(new Integer(i));
        }
        fireTableDataChanged();
    }

    //@exclude
    public void tableChanged(TableModelEvent e) {
        refreshRowMapping();
        super.tableChanged(e);
    }
}
