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

import javax.swing.table.TableModel;


/**
 * A comparison function used for sorting table rows.
 */
public interface RowComparator {

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
    public int compare(int row1, int row2, int column, TableModel tableModel);
}
