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
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


class TableModelMapping extends AbstractTableModel implements TableModelListener {
    /**
     * Original <code>TableModel</code> used as data source.
     */
    protected TableModel model;

    /**
     * Returns original <code>TableModel</code> used as data source.
     *
     * @return original <code>TableModel</code> used as data source.
     */
    public TableModel getModel() {
        return model;
    }

    /**
     * Sets original <code>TableModel</code> used as data source.
     *
     * @param model original <code>TableModel</code> used as data source
     */
    public void setModel(TableModel model) {
        this.model = model;
        model.addTableModelListener(this);
    }

    // By default implement TableModel by forwarding all messages to the model.
    //@exclude
    public Object getValueAt(int rowIndex, int columnIndex) {
        return model.getValueAt(rowIndex, columnIndex);
    }

    //@exclude
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        model.setValueAt(aValue, rowIndex, columnIndex);
    }

    //@exclude
    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }

    //@exclude
    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount();
    }

    //@exclude
    public String getColumnName(int columnIndex) {
        return model.getColumnName(columnIndex);
    }

    //@exclude
    public Class getColumnClass(int columnIndex) {
        return model.getColumnClass(columnIndex);
    }

    //@exclude
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return model.isCellEditable(rowIndex, columnIndex);
    }

    // By default forward all events to all the listeners.
    //@exclude
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
}
