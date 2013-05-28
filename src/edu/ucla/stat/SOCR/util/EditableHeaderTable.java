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

import javax.swing.*;
import javax.swing.table.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

/**
 * Package: edu.ucla.stat.SOCR.util
 * User: Khashim
 * Date: Dec 23, 2008
 * Time: 12:52:18 PM
 *
 * @author Jameel
 */
public class EditableHeaderTable extends JTable implements PropertyChangeListener
{
    boolean headersEditable = true;
    Boolean cellsEditable;

    /**
     * Constructs a default <code>EditableHeaderTable</code> that is initialized with a default
     * data model, a default column model, and a default selection
     * model.
     *
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public EditableHeaderTable()
    {
        super();
    }

    /**
     * Constructs a <code>EditableHeaderTable</code> that is initialized with
     * <code>dm</code> as the data model, a default column model,
     * and a default selection model.
     *
     * @param dm the data model for the table
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public EditableHeaderTable(TableModel dm)
    {
        super(dm);
    }

    /**
     * Constructs a <code>EditableHeaderTable</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code>
     * as the column model, and a default selection model.
     *
     * @param dm the data model for the table
     * @param cm the column model for the table
     * @see #createDefaultSelectionModel
     */
    public EditableHeaderTable(TableModel dm, TableColumnModel cm)
    {
        super(dm, cm);
    }

    /**
     * Constructs a <code>EditableHeaderTable</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code> as the
     * column model, and <code>sm</code> as the selection model.
     * If any of the parameters are <code>null</code> this method
     * will initialize the table with the corresponding default model.
     * The <code>autoCreateColumnsFromModel</code> flag is set to false
     * if <code>cm</code> is non-null, otherwise it is set to true
     * and the column model is populated with suitable
     * <code>TableColumns</code> for the columns in <code>dm</code>.
     *
     * @param dm the data model for the table
     * @param cm the column model for the table
     * @param sm the row selection model for the table
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public EditableHeaderTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
    {
        super(dm, cm, sm);
    }

    /**
     * Constructs a <code>EditableHeaderTable</code> with <code>numRows</code>
     * and <code>numColumns</code> of empty cells using
     * <code>DefaultTableModel</code>.  The columns will have
     * names of the form "A", "B", "C", etc.
     *
     * @param numRows    the number of rows the table holds
     * @param numColumns the number of columns the table holds
     * @see javax.swing.table.DefaultTableModel
     */
    public EditableHeaderTable(int numRows, int numColumns)
    {
        super(numRows, numColumns);
    }

    /**
     * Constructs a <code>EditableHeaderTable</code> to display the values in the
     * <code>Vector</code> of <code>Vectors</code>, <code>rowData</code>,
     * with column names, <code>columnNames</code>.  The
     * <code>Vectors</code> contained in <code>rowData</code>
     * should contain the values for that row. In other words,
     * the value of the cell at row 1, column 5 can be obtained
     * with the following code:
     * <p/>
     * <pre>((Vector)rowData.elementAt(1)).elementAt(5);</pre>
     * <p/>
     *
     * @param rowData     the data for the new table
     * @param columnNames names of each column
     */
    public EditableHeaderTable(Vector rowData, Vector columnNames)
    {
        super(rowData, columnNames);
    }

    /**
     * Constructs a <code>EditableHeaderTable</code> to display the values in the two dimensional array,
     * <code>rowData</code>, with column names, <code>columnNames</code>.
     * <code>rowData</code> is an array of rows, so the value of the cell at row 1,
     * column 5 can be obtained with the following code:
     * <p/>
     * <pre> rowData[1][5]; </pre>
     * <p/>
     * All rows must be of the same length as <code>columnNames</code>.
     * <p/>
     *
     * @param rowData     the data for the new table
     * @param columnNames names of each column
     */
    public EditableHeaderTable(Object[][] rowData, Object[] columnNames)
    {
        super(rowData, columnNames);
    }

    /**
     * Returns true if the column headers are editable.
     *
     * @return true if the header is editable
     * @see #setHeadersEditable(boolean)
     */
    public boolean isHeadersEditable()
    {
        return headersEditable;
    }

    /**
     *  Sets whether the table allows the user to edit the column headers.
     *
     * @param   editable                 true if the user should be allowed to edit the headers
     *
     * @see     #isHeadersEditable
     */
    public void setHeadersEditable(boolean editable)
    {
        this.headersEditable = editable;
        TableColumnModel cm = getColumnModel();
        int colCount = cm.getColumnCount();

        for(int i = 0; i < colCount; i++)
        {
            ((EditableHeaderTableColumn)cm.getColumn(i)).setHeaderEditable(editable);
        }
    }

    /**
     *  Sets whether the table allows the user to edit cells.
     *
     * @param   editable                 true if the user should be allowed to edit cells
     *
     * @see     #isCellsEditable
     * @see     #isCellEditable(int,int)
     */
    public void setCellsEditable(boolean editable)
    {
        this.cellsEditable = editable;
    }

    /**
     * Returns true if the cells in the table are editable.
     * Otherwise, invoking <code>setValueAt</code> on the cell
     * will have no effect.
     * <p/>
     * <b>Note</b>: The column is specified in the table view's display
     * order, and not in the <code>TableModel</code>'s column
     * order.  This is an important distinction because as the
     * user rearranges the columns in the table,
     * the column at a given index in the view will change.
     * Meanwhile the user's actions never affect the model's
     * column ordering.
     *
     * @return true if the cells are editable
     * @see #setValueAt
     * @see #setCellsEditable(boolean)
     * @see #isCellEditable(int,int)
     */
    public boolean isCellsEditable()
    {
        if(cellsEditable != null)
        {
            return cellsEditable;
        }
        else
        {
            return super.isCellEditable(0,0);
        }
    }

    /**
     * Returns true if the cell at <code>row</code> and <code>column</code>
     * is editable.  Otherwise, invoking <code>setValueAt</code> on the cell
     * will have no effect.
     * <p/>
     * <b>Note</b>: The column is specified in the table view's display
     * order, and not in the <code>TableModel</code>'s column
     * order.  This is an important distinction because as the
     * user rearranges the columns in the table,
     * the column at a given index in the view will change.
     * Meanwhile the user's actions never affect the model's
     * column ordering.
     *
     * @param row    the row whose value is to be queried
     * @param column the column whose value is to be queried
     * @return true if the cell is editable
     * @see #setValueAt
     * @see #isCellsEditable
     * @see #setCellsEditable(boolean)
     */
    @Override
    public boolean isCellEditable(int row, int column)
    {
        if(cellsEditable != null)
        {
            return cellsEditable;
        }

        return super.isCellEditable(row, column);
    }

    /**
     * Returns the default table header object, which is
     * a <code>EditableHeader</code>.  A subclass can override this
     * method to return a different table header object.
     *
     * @return the default table header object
     * @see javax.swing.table.JTableHeader
     */
    @Override
    protected JTableHeader createDefaultTableHeader()
    {
        EditableHeader header = new EditableHeader(columnModel);
        header.addPropertyChangeListener(this);
        return header;
    }

    /**
     * Creates default columns for the table from
     * the data model using the <code>getColumnCount</code> method
     * defined in the <code>TableModel</code> interface.
     * <p/>
     * Clears any existing columns before creating the
     * new columns based on information from the model.
     *
     * @see #getAutoCreateColumnsFromModel
     */
    @Override
    public void createDefaultColumnsFromModel()
    {
        super.createDefaultColumnsFromModel();

        TableColumnModel cm = getColumnModel();

        int n = cm.getColumnCount();
        EditableHeaderTableColumn[] newCols = new EditableHeaderTableColumn[n];
        TableColumn[] oldCols = new TableColumn[n];
        for (int i = 0; i < n; i++)
        {
            oldCols[i] = cm.getColumn(i);
            newCols[i] = new EditableHeaderTableColumn();
            newCols[i].copyValues(oldCols[i]);
        }
        for (int i = 0; i < n; i++)
        {
            cm.removeColumn(oldCols[i]);
        }
        for (int i = 0; i < n; i++)
        {
            cm.addColumn(newCols[i]);
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        if(evt.getPropertyName().equals("headerValue"))
        {
            int index = getColumnModel().getColumnIndex(evt.getNewValue());

            if(getModel() instanceof DefaultTableModel)
            {
                DefaultTableModel model = (DefaultTableModel)getModel();
                TableColumnModel cm = getColumnModel();
                int count = cm.getColumnCount();
                Vector identifiers = new Vector(count);

                for(int c = 0; c < count; c++)
                {
                    identifiers.add(cm.getColumn(c).getIdentifier());
                }

                model.setColumnIdentifiers(identifiers);
            }
        }
    }
}
