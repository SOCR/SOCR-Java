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

package edu.ucla.stat.SOCR.util;

import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import java.util.Vector;

/**
 * Package: edu.ucla.stat.SOCR.util
 * User: Khashim
 * Date: Dec 29, 2008
 * Time: 2:08:00 PM
 *
 * @author Jameel
 */
public class EnhancedDefaultTableModel extends DefaultTableModel
{
    private boolean notify = true;

    /**
     * Constructs a default <code>EnhancedDefaultTableModel</code>
     * which is a table of zero columns and zero rows.
     */
    public EnhancedDefaultTableModel()
    {
        super();
    }

    /**
     * Constructs an <code>EnhancedDefaultTableModel</code> with as many
     * columns as there are elements in <code>columnNames</code>
     * and <code>rowCount</code> of <code>null</code>
     * object values.  Each column's name will be taken from
     * the <code>columnNames</code> array.
     *
     * @param columnNames <code>array</code> containing the names
     *                    of the new columns; if this is
     *                    <code>null</code> then the model has no columns
     * @param rowCount    the number of rows the table holds
     * @see #setDataVector
     * @see #setValueAt
     */
    public EnhancedDefaultTableModel(Object[] columnNames, int rowCount)
    {
        super(columnNames, rowCount);
    }

    /**
     * Constructs an <code>EnhancedDefaultTableModel</code> with as many columns
     * as there are elements in <code>columnNames</code>
     * and <code>rowCount</code> of <code>null</code>
     * object values.  Each column's name will be taken from
     * the <code>columnNames</code> vector.
     *
     * @param columnNames <code>vector</code> containing the names
     *                    of the new columns; if this is
     *                    <code>null</code> then the model has no columns
     * @param rowCount    the number of rows the table holds
     * @see #setDataVector
     * @see #setValueAt
     */
    public EnhancedDefaultTableModel(Vector columnNames, int rowCount)
    {
        super(columnNames, rowCount);
    }

    /**
     * Constructs an <code>EnhancedDefaultTableModel</code> and initializes the table
     * by passing <code>data</code> and <code>columnNames</code>
     * to the <code>setDataVector</code>
     * method. The first index in the <code>Object[][]</code> array is
     * the row index and the second is the column index.
     *
     * @param data        the data of the table
     * @param columnNames the names of the columns
     * @see #getDataVector
     * @see #setDataVector
     */
    public EnhancedDefaultTableModel(Object[][] data, Object[] columnNames)
    {
        super(data, columnNames);
    }

    /**
     * Constructs an <code>EnhancedDefaultTableModel</code> and initializes the table
     * by passing <code>data</code> and <code>columnNames</code>
     * to the <code>setDataVector</code> method.
     *
     * @param data        the data of the table, a <code>Vector</code>
     *                    of <code>Vector</code>s of <code>Object</code>
     *                    values
     * @param columnNames <code>vector</code> containing the names
     *                    of the new columns
     * @see #getDataVector
     * @see #setDataVector
     */
    public EnhancedDefaultTableModel(Vector data, Vector columnNames)
    {
        super(data, columnNames);
    }

    /**
     * Constructs an <code>EnhancedDefaultTableModel</code> with
     * <code>rowCount</code> and <code>columnCount</code> of
     * <code>null</code> object values.
     *
     * @param rowCount    the number of rows the table holds
     * @param columnCount the number of columns the table holds
     * @see #setValueAt
     */
    public EnhancedDefaultTableModel(int rowCount, int columnCount)
    {
        super(rowCount, columnCount);
    }

    /**
     *  Allows event notifications to be temporarily switched off for all
     *  listeneres. If <code>notify</code> is <code>true</code>, then event
     *  notifications are enabled. If <code>notify</code> is <code>false</code>,
     *  notifications are disabled.
     *
     *  @param notify   false to disable event notifications.
     *  @see #getNotify
     */
    public void setNotify(boolean notify)
    {
        this.notify = notify;
    }

    /**
     *  Returns <code>true</code> if notifications are enabled for all listeners.
     *  If notifications are disabled for all listeners, <code>getNotify</code>
     *  returns <code>false</code>.
     *
     *  @see #setNotify
     */
    public boolean getNotify()
    {
        return this.notify;
    }

    /**
     * Forwards the given notification event to all
     * <code>TableModelListeners</code> that registered
     * themselves as listeners for this table model.
     *
     * @param e the event to be forwarded
     * @see #addTableModelListener
     * @see javax.swing.event.TableModelEvent
     * @see javax.swing.event.EventListenerList
     */
    @Override
    public void fireTableChanged(TableModelEvent e)
    {
        if(notify)
        {
            super.fireTableChanged(e);
        }
    }
}
