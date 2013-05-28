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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;


/**
 * An <code>AbstractTableModel</code> implementation with sorting and multisorting support.
 * It uses supplied {@link RowComparator} to sort table rows.
 * The original <code>TableModel</code> is used only as the data
 * source and is not modified.
 * <p>Sample usage:<pre><code>    // Old code:
 *    //    JTable jTable = new JTable(tableModel);
 *    // New code:
 *    SortedTableModel sortedTableModel = new SortedTableModel(tableModel);
 *    JTable jTable = new JTable(sortedTableModel);
 *    // Adding mouse listener that invokes sorting when table column's header is clicked
 *    jTable.getTableHeader().addMouseListener(new SortedTableHeaderMouseListener(jTable, sortedTableModel));
 *    // Adding renderer to display sorting icon
 *    jTable.getTableHeader().setDefaultRenderer(new SortedTableHeaderRenderer(jTable, sortedTableModel));</code></pre>
 * After that your table has ability to sort its columns. If the user clicks on column's header it will be sorted.
 * The first left click will set ascending sort order, the second descending, and the third reset sort order back to unsorted.
 * The right click will show popup menu that allows set sort order exactly you want. To enable multisorting support just
 * hold CTRL key and click on several table headers. The column header will show an icon than represents column
 * sort order (ascending/descending) and column sort rank.
 *
 * @see RowComparator
 */
public class SortedTableModel extends TableModelMapping implements SortOrderConstants
{
    private Integer rowMapping[] = new Integer[0];
    /**
     * vector's index holds sorting rank
     */
    private Vector sortingColumns = new Vector();

    private RowComparator rowComparator = new DefaultRowComparator();

    /**
     * Creates <code>SortedTableModel</code> object with specified <code>TableModel</code>
     * as the data source.
     *
     * @param model <code>TableModel</code> used as the data source
     */
    public SortedTableModel(TableModel model) {
        setModel(model);
    }

    /**
     * Returns <code>RowComparator</code> used to sort table rows.
     * The default is {@link DefaultRowComparator} instance.
     *
     * @return <code>RowComparator</code> used to sort table rows.
     */
    public RowComparator getRowComparator() {
        return rowComparator;
    }

    /**
     * Sets <code>RowComparator</code> used to sort table rows. Invokes {@link #sort()}
     * before return. The default is {@link DefaultRowComparator} instance.
     *
     * @param rowComparator <code>RowComparator</code> used to sort table rows
     */
    public void setRowComparator(RowComparator rowComparator) {
        this.rowComparator = rowComparator;
        sort();
    }

    /**
     * Forces sorting of data contained in {@link #model}. The original {@link #model} is not modified.
     */
    public void sort() {
        checkModel();
        if (sortingColumns.size() > 0 && rowComparator != null)
            Arrays.sort(rowMapping, new ComparatorWrapper(rowComparator));
        else if (sortingColumns.size() == 0) {
            int rowCount = model.getRowCount();
            rowMapping = new Integer[rowCount];
            for (int row = 0; row < rowCount; row++)
                rowMapping[row] = new Integer(row);
        }
        fireTableDataChanged();
    }

    /**
     * Sorts specified column ascending allowing to reset or keep all others columns' sort order.
     * <br>Note: to specify several sorting columns at one time (multisort support) use {@link #setSortingColumns(java.util.List)}
     *
     * @param columnIndex model column index
     * @param resetSort   if <code>true</code> reset all previously sorted columns, if <code>false</code>
     *                    specified column is added to the sorted column set with incremental sort rank
     * @see #sortColumn(int, int, boolean)
     */
    public void sortColumn(int columnIndex, boolean resetSort) {
        sortColumn(columnIndex, -1, resetSort);
    }

    /**
     * Sorts specified column using <code>sortOrder</code> and allowing to reset or keep all others columns' sort order.
     * <br>Note: to specify several sorting columns at one time (multisort support) use {@link #setSortingColumns(java.util.List)}
     *
     * @param columnIndex model column index
     * @param sortOrder   column sort order, must be one of the following values:
     *                    {@link SortOrderConstants#ASCENDING}, {@link SortOrderConstants#DESCENDING} or {@link SortOrderConstants#NOT_SORTED}
     * @param resetSort   if <code>true</code> reset all previously sorted columns, if <code>false</code>
     *                    specified column is added to the sorted column set with incremental sort rank
     * @see #sortColumn(int, boolean)
     */
    public void sortColumn(int columnIndex, int sortOrder, boolean resetSort) {
        boolean changeSortMode = false;
        for (int i = 0; i < sortingColumns.size(); i++) {
            SortedColumnInfo sortedColumnInfo = (SortedColumnInfo) sortingColumns.get(i);
            if (sortedColumnInfo.getColumnIndex() == columnIndex) {
                if (resetSort) {
                    sortingColumns.removeAllElements();
                    sortingColumns.add(sortedColumnInfo);
                }
                if (sortOrder == -1) {
                    // circle change sort mode
                    if (sortedColumnInfo.isAscending())
                        sortedColumnInfo.setAscending(false);
                    else if (!sortedColumnInfo.isAscending())
                        sortingColumns.remove(sortedColumnInfo);
                } else if (sortOrder == SortOrderConstants.ASCENDING)
                    sortedColumnInfo.setAscending(true);
                else if (sortOrder == SortOrderConstants.DESCENDING)
                    sortedColumnInfo.setAscending(false);
                else if (sortOrder == SortOrderConstants.NOT_SORTED)
                    sortingColumns.remove(sortedColumnInfo);

                changeSortMode = true;
                break;
            }
        }
        if (!changeSortMode) {
            if (resetSort)
                sortingColumns.removeAllElements();
            if (sortOrder == -1)
                sortingColumns.add(new SortedColumnInfo(columnIndex, true));
            else if (sortOrder == SortOrderConstants.ASCENDING)
                sortingColumns.add(new SortedColumnInfo(columnIndex, true));
            else if (sortOrder == SortOrderConstants.DESCENDING)
                sortingColumns.add(new SortedColumnInfo(columnIndex, false));
        }

        sort();
    }

    /**
     * Returns row index in original {@link #model} by index in current model (view index).
     *
     * @param rowIndex index in current model (view index)
     * @return row index in original {@link #model}.
     */
    public int getRealRowIndex(int rowIndex) {
        return rowMapping[rowIndex].intValue();
    }

    /**
     * Returns <code>true</code> if column is sorted, <code>false</code> otherwise.
     *
     * @param columnIndex model column index
     * @return <code>true</code> if column is sorted, <code>false</code> otherwise.
     */
    public boolean isColumnSorted(int columnIndex) {
        for (int i = 0; i < sortingColumns.size(); i++)
            if (columnIndex == ((SortedColumnInfo) sortingColumns.get(i)).getColumnIndex())
                return true;
        return false;
    }

    /**
     * Returns <code>true</code> if <code>columnIndex</code> is sorted
     * and sort mode is {@link SortOrderConstants#ASCENDING}, <code>false</code> otherwise.
     *
     * @param columnIndex model column index
     * @return <code>true</code> if <code>columnIndex</code> is sorted ascending.
     * @see #isColumnDescending(int)
     * @see #isColumnSorted(int)
     */
    public boolean isColumnAscending(int columnIndex) {
        return getColumnSortOrder(columnIndex) == SortOrderConstants.ASCENDING;
    }

    /**
     * Returns <code>true</code> if <code>columnIndex</code> is sorted
     * and sort mode is {@link SortOrderConstants#DESCENDING}, <code>false</code> otherwise.
     * <br>Note: to check whether column's sort order is ascending use {}
     *
     * @param columnIndex model column index
     * @return <code>true</code> if <code>columnIndex</code> is sorted desending.
     * @see #isColumnAscending(int)
     * @see #isColumnSorted(int)
     */
    public boolean isColumnDescending(int columnIndex) {
        return getColumnSortOrder(columnIndex) == SortOrderConstants.DESCENDING;
    }

    /**
     * Returns column's sort order. The return values are:
     * {@link SortOrderConstants#ASCENDING}, {@link SortOrderConstants#DESCENDING} or {@link SortOrderConstants#NOT_SORTED}
     *
     * @param columnIndex model column index
     * @return column's sort order
     * @see SortOrderConstants
     */
    public int getColumnSortOrder(int columnIndex) {
        for (int i = 0; i < sortingColumns.size(); i++) {
            SortedColumnInfo sortedColumnInfo = (SortedColumnInfo) sortingColumns.get(i);
            if (sortedColumnInfo.getColumnIndex() == columnIndex)
                if (sortedColumnInfo.isAscending())
                    return SortOrderConstants.ASCENDING;
                else
                    return SortOrderConstants.DESCENDING;
        }
        return SortOrderConstants.NOT_SORTED;
    }

    /**
     * Returns column's sort rank within all sorted columns.
     *
     * @param columnIndex model column index
     * @return column's sort rank.
     */
    public int getColumnSortRank(int columnIndex) {
        for (int i = 0; i < sortingColumns.size(); i++) {
            SortedColumnInfo sortedColumnInfo = (SortedColumnInfo) sortingColumns.get(i);
            if (sortedColumnInfo.getColumnIndex() == columnIndex)
                return i + 1;
        }
        return -1;
    }

    /**
     * Returns array that contains sorted columns indexes.
     *
     * @return array that contains sorted columns indexes.
     * @see #getSortingColumns()
     */
    public int[] getSortingColumnIndexes() {
        int[] sortingColumnIndexes = new int[sortingColumns.size()];
        for (int i = 0; i < sortingColumns.size(); i++)
            sortingColumnIndexes[i] = ((SortedColumnInfo) sortingColumns.get(i)).getColumnIndex();
        return sortingColumnIndexes;
    }

    /**
     * Returns list with sorted columns info.
     *
     * @return list with sorted columns info.
     * @see #getSortingColumnIndexes()
     * @see SortedColumnInfo
     */
    public List getSortingColumns() {
        return (List) sortingColumns.clone();
    }

    /**
     * Specifies sorting columns infos and calls {@link #sort()} method.
     *
     * @param sortingColumns list of {@link SortedColumnInfo}
     */
    public void setSortingColumns(List sortingColumns) {
        if (sortingColumns != null)
            this.sortingColumns = new Vector(sortingColumns);
        else
            this.sortingColumns = new Vector();
        sort();
    }

    /**
     * Returns the number of sorting columns.
     *
     * @return the number of sorting columns.
     */
    public int getSortingColumnsCount() {
        return sortingColumns.size();
    }

    //@exclude
    public void setModel(TableModel model) {
        super.setModel(model);
        refreshRowMapping();
        sortingColumns = new Vector();
    }

    //@exclude
    public Object getValueAt(int rowIndex, int columnIndex) {
        return model.getValueAt(rowMapping[rowIndex].intValue(), columnIndex);
    }

    //@exclude
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        model.setValueAt(aValue, rowMapping[rowIndex].intValue(), columnIndex);
    }

    private void refreshRowMapping() {
        int rowCount = model.getRowCount();
        rowMapping = new Integer[rowCount];
        for (int row = 0; row < rowCount; row++)
            rowMapping[row] = new Integer(row);
        sort();
    }

    //@exclude
    public void tableChanged(TableModelEvent e) {
        refreshRowMapping();
        super.tableChanged(e);
    }

    protected boolean checkModel()
    {
        return true;
    }

    private class ComparatorWrapper implements Comparator {
        private RowComparator rowComparator;

        public ComparatorWrapper(RowComparator rowComparator) {
            this.rowComparator = rowComparator;
        }

        public int compare(Object o1, Object o2) {
            int row1 = ((Number) o1).intValue();
            int row2 = ((Number) o2).intValue();
            boolean sort = false;
            int sortColumnRank = 0;
            while (!sort) {
                SortedColumnInfo sortedColumnInfo = (SortedColumnInfo) sortingColumns.get(sortColumnRank);
                int res = compare(row1, row2, sortedColumnInfo.getColumnIndex(), sortedColumnInfo.isAscending(), model);
                if (res != 0)
                    return res;
                else if (sortColumnRank + 1 < sortingColumns.size())
                    sortColumnRank++;
                else
                    sort = true;
            }
            return 0;
        }

        private int compare(int row1, int row2, int sortingColumn, boolean ascending, TableModel model) {
            int result = rowComparator.compare(row1, row2, sortingColumn, model);
            if (result != 0)
                return ascending ? result : -result;
            return 0;

        }
    }
}
