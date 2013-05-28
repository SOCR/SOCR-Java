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

/**
 * SortedColumnInfo class provides information about sorted column.
 *
 * @see SortedTableModel#getSortingColumns()
 * @see SortedTableModel#setSortingColumns(java.util.List)
 */
public class SortedColumnInfo {
    private int columnIndex = -1;
    private boolean ascending = true;

    /**
     * Creates <code>SortedColumnInfo</code> object with specified
     * <code>columnIndex</code> and sort order.
     *
     * @param columnIndex model column index
     * @param ascending   <code>true</code> to set ascending sort order,
     *                    <code>false</code> to set descending sort order
     * @see SortedTableModel#setSortingColumns(java.util.List)
     */
    public SortedColumnInfo(int columnIndex, boolean ascending) {
        this.columnIndex = columnIndex;
        this.ascending = ascending;
    }

    /**
     * Returns model column index.
     *
     * @return model column index.
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Specifies model column index.
     * <br><b>Note:</b> SortedColumnInfo is an immutable object due to optimization. Be accurate overriding this
     * method when using {@link SortedTableModel#getSortingColumns()} because this
     * data is used for internal purpose.
     *
     * @param columnIndex model column index
     */
    protected void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * Returns columns sort order
     *
     * @return <code>true</code> if sort order ascending,
     *         <code>false</code> if sort order descending
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Specifies column sort order.
     * <br><b>Note:</b> SortedColumnInfo is an immutable object due to optimization. Be accurate overriding this
     * method when using {@link SortedTableModel#getSortingColumns()} because this
     * data is used for internal purpose.
     *
     * @param ascending <code>true</code> to set ascending sort order,
     *                  <code>false</code> to set descending sort order
     */
    protected void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
