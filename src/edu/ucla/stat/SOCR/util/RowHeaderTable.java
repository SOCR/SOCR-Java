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

import edu.ucla.stat.SOCR.util.tablemodels.SortedTableModel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

/**
 * Package: edu.ucla.stat.SOCR.util
 * User: Khashim
 * Date: Aug 20, 2008
 * Time: 11:39:01 PM
 *
 * @author Jameel
 */
public class RowHeaderTable extends JPanel implements KeyListener, PropertyChangeListener, TableModelListener
{
    /**
     * @uml.property name="dataTable"
     * @uml.associationEnd readOnly="true" multiplicity="(1 1)"
     */
    protected EditableHeaderTable dataTable;
    protected JTable headerTable;

    /**
     * @uml.property name="columnCount"
     */
    protected int columnCount = 10;
    /**
     * @uml.property name="rowCount"
     */
    protected int rowCount = 10;

    /**
     * @uml.property name="dataText"
     */
    protected String dataText = "";
    /**
     * @uml.property name="defaultHeader"
     */
    protected String defaultHeader = "C";
    /**
     * @uml.property name="columnNames" multiplicity="(0 -1)" dimension="1"
     */
    protected Vector<String> columnNames;

    protected Vector<String> rowNames;

    /**
     * @uml.property name="tModel"
     * @uml.associationEnd readOnly="true" multiplicity="(1 1)"
     */
    protected DefaultTableModel tModel;
    protected DefaultTableModel hModel;

    protected SortedTableModel sortableModel;
    /**
     * @uml.property name="columnModel"
     * @uml.associationEnd readOnly="true" multiplicity="(1 1)"
     */
    protected TableColumnModel columnModel;

    public RowHeaderTable()
    {
        this(0, null, 0, null);
    }

    public RowHeaderTable(int numCols, int numRows)
    {
        this(numCols, null, numRows, null);
    }

    public RowHeaderTable(String[] columnHeaders, String[] rowHeaders)
    {
        this(0, columnHeaders, 0, rowHeaders);
    }

    public RowHeaderTable(int numCols, String[] columnHeaders, int numRows, String[] rowHeaders)
    {
        if(numCols != 0)
        {
            columnCount = numCols;
        }

        if(numRows != 0)
        {
            rowCount = numRows;
        }

        if(numCols == 0 && columnHeaders != null)
        {
            columnCount = columnHeaders.length;
        }

        if(numRows == 0 && rowHeaders != null)
        {
            rowCount = rowHeaders.length;
        }

        columnNames = new Vector<String>(columnCount);
        rowNames = new Vector<String>(rowCount);

        if(columnHeaders != null)
        {
            convertHeaders(columnNames, columnHeaders);
        }

        if(rowHeaders != null)
        {
            convertHeaders(rowNames, rowHeaders);
        }

        setDefaultColumnNames();
        setDefaultRowNames();

        tModel = new DefaultTableModel(columnNames, rowCount);

        initTable();
    }

    public RowHeaderTable(DefaultTableModel model)
    {
        tModel = model;

        rowCount = tModel.getRowCount();
        columnCount = tModel.getColumnCount();

        columnNames = new Vector<String>(columnCount);

        for(int i = 0; i < columnCount; i++)
        {
            columnNames.add(i, tModel.getColumnName(i));
        }

        tModel.setColumnIdentifiers(columnNames);

        rowNames = new Vector<String>();
        setDefaultRowNames();

        initTable();
    }

    protected void initTable()
    {
        tModel.addTableModelListener(this);

        dataTable = new EditableHeaderTable(tModel);
        dataTable.addKeyListener(this);
        dataTable.setGridColor(Color.gray);
        dataTable.setShowGrid(true);
        dataTable.setCellSelectionEnabled(true);
        dataTable.doLayout();

        hModel = new DefaultTableModel(rowCount,1);

        setRowNames();

        headerTable = new JTable(hModel);
        headerTable.setCellSelectionEnabled(false);
        headerTable.setEnabled(false);
        LookAndFeel.installColorsAndFont
                (headerTable, "TableHeader.background",
                        "TableHeader.foreground", "TableHeader.font");
        headerTable.setIntercellSpacing(new Dimension(0, 0));
        Dimension d = headerTable.getPreferredScrollableViewportSize();
        d.width = headerTable.getPreferredSize().width;
        headerTable.setPreferredScrollableViewportSize(d);
        headerTable.setRowHeight(dataTable.getRowHeight());
        headerTable.setDefaultRenderer(Object.class, new RowHeaderRenderer());

        JTableHeader corner = headerTable.getTableHeader();
        corner.setReorderingAllowed(false);
        corner.setResizingAllowed(false);

        // this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
        try
        {
            dataTable.setDragEnabled(true);
        }
        catch (Exception e)
        {
        }
        dataTable.getTableHeader().addPropertyChangeListener(this);
        columnModel = dataTable.getColumnModel();
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        hookTableAction();

        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JScrollPane tablePanel = new JScrollPane(dataTable);
        tablePanel.setRowHeaderView(headerTable);
        //tablePanel.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
        new JScrollPaneAdjuster(tablePanel);

        new JTableRowHeaderResizer(tablePanel).setEnabled(true);

        this.add(tablePanel);

        this.validate();
    }

    public boolean isHeadersEditable()
    {
        return dataTable.isHeadersEditable();
    }

    public void setHeadersEditable(boolean editable)
    {
        dataTable.setHeadersEditable(editable);
    }

    public boolean isCellsEditable()
    {
        return dataTable.isCellsEditable();
    }

    public void setCellsEditable(boolean editable)
    {
        dataTable.setCellsEditable(editable);
    }

    public JTable getDataTable()
    {
        return dataTable;
    }

    public JTable getRowHeaderTable()
    {
        return headerTable;
    }

    public DefaultTableModel getTableModel()
    {
        return tModel;
    }

    public DefaultTableModel getRowHeaderModel()
    {
        return hModel;
    }

    public void resetTable()
    {
        initTable();
    }

    protected void setRowNames()
    {
        for (int i = 0; i < hModel.getRowCount(); i++)
        {
            hModel.setValueAt(rowNames.get(i), i, 0);
        }
    }

    protected void setDefaultColumnNames()
    {
        int currCols = columnNames.size();

        for (int i = currCols; i < columnCount; i++)
        {
            columnNames.add(i, new String(defaultHeader + (i + 1)));
        }
    }

    protected void setDefaultRowNames()
    {
        int currRows = rowNames.size();

        for (int i = currRows; i < rowCount; i++)
        {
            rowNames.add(i, new String((i + 1) + ":"));
        }
    }

    protected void convertHeaders(Vector<String> vHeaders, String[] headers)
    {
        for(int i = 0; i < headers.length; i++)
        {
            vHeaders.add(headers[i]);
        }
    }

    protected void resetTableRows(int n)
    {
        tModel = (DefaultTableModel) dataTable.getModel();
        tModel.setRowCount(n);
        dataTable.setModel(tModel);


        hModel = (DefaultTableModel) headerTable.getModel();
        hModel.setRowCount(n);
        headerTable.setModel(hModel);

        setDefaultRowNames();
        setRowNames();
    }

    protected void resetTableColumns(int n)
    {
        tModel = (DefaultTableModel) dataTable.getModel();
        tModel.setColumnCount(n);
        dataTable.setModel(tModel);

        setDefaultColumnNames();
        tModel.setColumnIdentifiers(columnNames);
    }

    public void appendTableRows(int n)
    {
        int ct = dataTable.getColumnCount();
        tModel = (DefaultTableModel) dataTable.getModel();
        for (int j = 0; j < n; j++)
        {
            tModel.addRow(new java.util.Vector(ct));
        }
    }

    public void appendTableRows(String[] rowNames)
    {
        int n = rowNames.length;

        int ct = dataTable.getColumnCount();
        tModel = (DefaultTableModel) dataTable.getModel();
        for (int j = 0; j < n; j++)
        {
            tModel.addRow(new java.util.Vector(ct));
        }

        hModel = (DefaultTableModel) headerTable.getModel();
        for (int j = 0; j < n; j++)
        {
            hModel.addRow(new Object[]{ rowNames[j] });
        }
    }

    public void appendTableColumns(int n)
    {
        int ct = dataTable.getColumnCount();
        tModel = (DefaultTableModel) dataTable.getModel();

        for (int j = 0; j < n; j++)
        {
            tModel.addColumn(defaultHeader + (ct + j + 1), new java.util.Vector(ct));
        }
    }

    public void appendTableColumns(String[] columnNames)
    {
        int n = columnNames.length;
        int ct = dataTable.getColumnCount();
        tModel = (DefaultTableModel) dataTable.getModel();

        for (int j = 0; j < n; j++)
        {
            tModel.addColumn(columnNames[j], new java.util.Vector(ct));
        }
    }

    /**
     * Add customized table actions.
     * Clicking  tab in the last cell will add one new column.
     * Clicking return in the last cell will add one new row.
     */
    protected void hookTableAction()
    {
        //Tab--add column
        String actionName = "selectNextColumnCell";
        final Action tabAction = dataTable.getActionMap().get(actionName);
        Action myAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (isLastCell())
                {
                    appendTableColumns(1);
                }
                else
                {
                    tabAction.actionPerformed(e);
                }
            }
        };
        dataTable.getActionMap().put(actionName, myAction);

        //Enter--append row
        String actionName2 = "selectNextRowCell";
        final Action enterAction = dataTable.getActionMap().get(actionName2);

        Action myAction2 = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (isLastCell())
                {
                    appendTableRows(1);
                }
                else
                {
                    enterAction.actionPerformed(e);
                }
            }
        };

        dataTable.getActionMap().put(actionName2, myAction2);


        String actionName3 = "deleteSelectedData";
        final Action delAction = dataTable.getActionMap().get(actionName3);

        Action myAction3 = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                if ((dataTable.getSelectedRow() >= 0) && (dataTable.getSelectedColumn() >= 0))
                {
                    int[] rows = dataTable.getSelectedRows();
                    int[] cols = dataTable.getSelectedColumns();
                    for (int i = rows[0]; i < cols[rows.length - 1]; i++)
                    {
                        for (int j = cols[0]; j < cols[cols.length - 1]; j++)
                        {
                            dataTable.setValueAt("", i, j);
                        }
                    }
                }

            }
        };

        dataTable.getActionMap().put(actionName3, myAction3);
    }

    private boolean isLastCell()
    {
        int rows = dataTable.getRowCount();
        int cols = dataTable.getColumnCount();
        int selectedRow = dataTable.getSelectedRow();
        int selectedCol = dataTable.getSelectedColumn();

        return (rows == (selectedRow + 1)) && (cols == (selectedCol + 1));
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == 127)
        {
            if ((dataTable.getSelectedRow() >= 0) && (dataTable.getSelectedColumn() >= 0))
            {
                int[] rows = dataTable.getSelectedRows();
                int[] cols = dataTable.getSelectedColumns();
                for (int i = rows[0]; i <= rows[rows.length - 1]; i++)
                {
                    for (int j = cols[0]; j <= cols[cols.length - 1]; j++)
                    {
                        dataTable.setValueAt(null, i, j);
                    }
                }
            }
//            this.removeAll();
//            JScrollPane tablePanel = new JScrollPane(dataTable);
//            tablePanel.setRowHeaderView(headerTable);
//            this.add(tablePanel);
//            dataTable.setGridColor(Color.gray);
//            dataTable.setShowGrid(true);
            this.validate();
        }
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
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
            int index = columnModel.getColumnIndex(evt.getNewValue());
            if(index >= columnNames.size())
            {
                columnNames.add(index, (String)columnModel.getColumn(index).getHeaderValue());
            }
            else
            {
                columnNames.set(index, (String)columnModel.getColumn(index).getHeaderValue());
            }
        }
    }

    /**
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed.
     */
    public void tableChanged(TableModelEvent e)
    {
        if (e.getType() == TableModelEvent.INSERT) {
            tableRowsInserted(e);
        }
        else if (e.getType() == TableModelEvent.DELETE) {
            tableRowsDeleted(e);
        }
        else
        {
            tableDataChanged(e);
        }
    }

    /*
    * Invoked when the table data has changed.
    *
    * @param e the TableModelEvent encapsulating the insertion
    */
    private void tableDataChanged(TableModelEvent e)
    {
        int start = 0;
        int end = tModel.getRowCount();

        int length = (end - start) - rowCount;
        rowCount = rowCount + length;

        if(length > 0)
        {
            setDefaultRowNames();

            for(int r = 0; r < length; r++)
            {
                hModel.addRow(new Object[0]);
            }

            setRowNames();
        }
        else if(length < 0)
        {
            length = Math.abs(length);
            start = hModel.getRowCount() - length;
            for(int r = 0; r < length; r++)
            {
                rowNames.remove(start);
                hModel.removeRow(start);
            }
        }
    }

    /*
    * Invoked when rows have been inserted into the table.
    *
    * @param e the TableModelEvent encapsulating the insertion
    */
    private void tableRowsInserted(TableModelEvent e)
    {
        int start = e.getFirstRow();
        int end = e.getLastRow();
        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = tModel.getRowCount() - 1;
        }

        int length = end - start + 1;

        rowCount = rowCount + length;
        setDefaultRowNames();

        for(int r = 0; r < length; r++)
        {
            hModel.addRow(new Object[0]);
        }

        setRowNames();
    }

    /*
     * Invoked when rows have been removed from the table.
     *
     * @param e the TableModelEvent encapsulating the deletion
     */
    private void tableRowsDeleted(TableModelEvent e)
    {
        int start = e.getFirstRow();
        int end = e.getLastRow();
        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = tModel.getRowCount() - 1;
        }

        int deletedCount = end - start + 1;
        rowCount = rowCount - deletedCount;

        for(int r = 0; r < deletedCount; r++)
        {
            rowNames.remove(start);
            hModel.removeRow(start);
        }
    }
}
