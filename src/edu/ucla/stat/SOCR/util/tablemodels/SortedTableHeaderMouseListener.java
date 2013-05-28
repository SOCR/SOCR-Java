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

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;


/**
 * <code>MouseListener</code> that invokes sorting when a button is pressed on the
 * column's header.
 */
public class SortedTableHeaderMouseListener extends MouseAdapter {
    private class SortActionListener implements ActionListener {
        private int modelColumnIndex = -1;
        private int sortMode;
        private boolean resetSort;

        public SortActionListener(int sortMode) {
            this.sortMode = sortMode;
        }

        public void setModelColumnIndex(int modelColumnIndex) {
            this.modelColumnIndex = modelColumnIndex;
        }

        public void setResetSort(boolean resetSort) {
            this.resetSort = resetSort;
        }

        public void actionPerformed(ActionEvent e) {
            if (modelColumnIndex != -1) {
                Cursor oldCursor = table.getCursor();
                table.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (table.getTableHeader() != null)
                    table.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                sortedTableModel.sortColumn(modelColumnIndex, sortMode, resetSort);
                table.getTableHeader().repaint();
                table.setCursor(oldCursor != null ? oldCursor : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (table.getTableHeader() != null)
                    table.getTableHeader().setCursor(oldCursor != null ? oldCursor : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
    private JTable table;
    private SortedTableModel sortedTableModel;
    private JPopupMenu jPopupMenu = new JPopupMenu();
    private JMenuItem jMenuItemAscending = new JMenuItem("Sort column ascending");
    private JMenuItem jMenuItemDescending = new JMenuItem("Sort column descending");
    private JMenuItem jMenuItemResetSort = new JMenuItem("Reset column sort");
    private SortActionListener ascendingSortActionListener = new SortActionListener(SortOrderConstants.ASCENDING);
    private SortActionListener descendingSortActionListener = new SortActionListener(SortOrderConstants.DESCENDING);
    private SortActionListener resetSortActionListener = new SortActionListener(SortOrderConstants.NOT_SORTED);


    /**
     * Creates <code>SortedTableHeaderMouseListener</code> object with specified <code>JTable</code>
     * and <code>SortedTableModel</code>.
     *
     * @param table            <code>JTable</code> component
     * @param sortedTableModel <code>SortedTableModel</code> object
     */
    public SortedTableHeaderMouseListener(JTable table, SortedTableModel sortedTableModel) {
        this.table = table;
        this.sortedTableModel = sortedTableModel;
        jMenuItemAscending.addActionListener(ascendingSortActionListener);
        jMenuItemDescending.addActionListener(descendingSortActionListener);
        jMenuItemResetSort.addActionListener(resetSortActionListener);
    }

    /**
     * Invoked when the mouse has been clicked on a component.
     * Invokes sorting of the column.
     */
    public void mouseClicked(MouseEvent e) {
        TableColumnModel tableColumnModel = table.getColumnModel();
        int viewColumnIndex = tableColumnModel.getColumnIndexAtX(e.getX());
        if (viewColumnIndex != tableColumnModel.getColumnIndexAtX(e.getX() - 3))
            return;
        if (viewColumnIndex != tableColumnModel.getColumnIndexAtX(e.getX() + 3))
            return;
        int modelColumnIndex = table.convertColumnIndexToModel(viewColumnIndex);
        if (modelColumnIndex != -1) {
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
                Cursor oldCursor = table.getCursor();
                table.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (table.getTableHeader() != null)
                    table.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                sortedTableModel.sortColumn(modelColumnIndex, !e.isControlDown());
                table.getTableHeader().repaint();
                table.setCursor(oldCursor != null ? oldCursor : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (table.getTableHeader() != null)
                    table.getTableHeader().setCursor(oldCursor != null ? oldCursor : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            } else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
                jPopupMenu.add(jMenuItemAscending);
                jPopupMenu.add(jMenuItemDescending);
                jPopupMenu.add(jMenuItemResetSort);

                ascendingSortActionListener.setModelColumnIndex(modelColumnIndex);
                ascendingSortActionListener.setResetSort(!e.isControlDown());
                descendingSortActionListener.setModelColumnIndex(modelColumnIndex);
                descendingSortActionListener.setResetSort(!e.isControlDown());
                resetSortActionListener.setModelColumnIndex(modelColumnIndex);
                resetSortActionListener.setResetSort(!e.isControlDown());


                if (sortedTableModel.isColumnSorted(modelColumnIndex)) {
                    if (sortedTableModel.isColumnAscending(modelColumnIndex))
                        jPopupMenu.remove(jMenuItemAscending);
                    if (sortedTableModel.isColumnDescending(modelColumnIndex))
                        jPopupMenu.remove(jMenuItemDescending);
                } else {
                    jPopupMenu.remove(jMenuItemResetSort);
                }

                jPopupMenu.show(table.getTableHeader(), e.getX(), e.getY());
            }
        }
    }
}
