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
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.EventObject;


/**
 * @version 1.0 08/21/99
 */

public class EditableHeader extends JTableHeader
                         implements CellEditorListener {
  public final int HEADER_ROW = -10;
  protected transient int editingColumn;
  protected transient TableCellEditor cellEditor;
  protected transient Component       editorComp;


  public EditableHeader(TableColumnModel columnModel) {
    super(columnModel);
    setReorderingAllowed(false);
    cellEditor = null;
    recreateTableColumn(columnModel);
  }

  public void updateUI(){
    setUI(new EditableHeaderUI());
    resizeAndRepaint();
    invalidate();
  }

  protected void recreateTableColumn(TableColumnModel columnModel) {
    int n = columnModel.getColumnCount();
    EditableHeaderTableColumn[] newCols = new EditableHeaderTableColumn[n];
    TableColumn[] oldCols = new TableColumn[n];
    for (int i=0;i<n;i++) {
      oldCols[i] = columnModel.getColumn(i);
      newCols[i] = new EditableHeaderTableColumn();
      newCols[i].copyValues(oldCols[i]);
    }
    for (int i=0;i<n;i++) {
      columnModel.removeColumn(oldCols[i]);
    }
    for (int i=0;i<n;i++) {
      columnModel.addColumn(newCols[i]);
    }
  }

  public boolean editCellAt(int index){
    return editCellAt(index, null);
  }

  public boolean editCellAt(int index, EventObject e){
    if (cellEditor != null && !cellEditor.stopCellEditing()) {
      return false;
    }
    if (!isCellEditable(index)) {
      return false;
    }
    TableCellEditor editor = getCellEditor(index);


    if (editor != null && editor.isCellEditable(e)) {
      editorComp = prepareEditor(editor, index);
      editorComp.setBounds(getHeaderRect(index));
      add(editorComp);
      editorComp.validate();
      setCellEditor(editor);
      setEditingColumn(index);
      editor.addCellEditorListener(this);

      return true;
    }
    return false;
  }


  public boolean isCellEditable(int index) {
    if (getReorderingAllowed()) {
      return false;
    }
    int columnIndex = columnModel.getColumn(index).getModelIndex();

    EditableHeaderTableColumn col = (EditableHeaderTableColumn)columnModel.getColumn(columnIndex);
    return col.isHeaderEditable();
  }

  public TableCellEditor getCellEditor(int index) {
    int columnIndex = columnModel.getColumn(index).getModelIndex();
    EditableHeaderTableColumn col = (EditableHeaderTableColumn)columnModel.getColumn(columnIndex);
    return col.getHeaderEditor();
  }

  public void setCellEditor(TableCellEditor newEditor) {
    TableCellEditor oldEditor = cellEditor;
    cellEditor = newEditor;

    // firePropertyChange

    if (oldEditor != null && oldEditor instanceof TableCellEditor) {
      ((TableCellEditor)oldEditor).removeCellEditorListener((CellEditorListener)this);
    }
    if (newEditor != null && newEditor instanceof TableCellEditor) {
      ((TableCellEditor)newEditor).addCellEditorListener((CellEditorListener)this);
    }
  }

  public Component prepareEditor(TableCellEditor editor, int index) {
    Object       value = columnModel.getColumn(index).getHeaderValue();
    boolean isSelected = true;
    int            row = HEADER_ROW;
    JTable       table = getTable();
    Component comp = editor.getTableCellEditorComponent(table,
                       value, isSelected, row, index);
    if (comp instanceof JComponent) {
          //  ((JComponent)comp).setNextFocusableComponent(this);
            ((JComponent)comp).setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
    }
    return comp;
  }

  public TableCellEditor getCellEditor() {
    return cellEditor;
  }

  public Component getEditorComponent() {
    return editorComp;
  }

  public void setEditingColumn(int aColumn) {
	 // System.out.println("EditableHeader setting editing column"+aColumn);
	  editingColumn = aColumn;
  }

  public int getEditingColumn() {
	 // System.out.println("EditableHeader getting editing column"+editingColumn);
    return editingColumn;
  }

  public void removeEditor() {
    TableCellEditor editor = getCellEditor();
    if (editor != null) {
      editor.removeCellEditorListener(this);

      requestFocus();
      remove(editorComp);

      int index = getEditingColumn();
      Rectangle cellRect = getHeaderRect(index);

      setCellEditor(null);
      setEditingColumn(-1);
      editorComp = null;

      repaint(cellRect);
    }
  }

  public boolean isEditing() {
    return (cellEditor == null)? false : true;
  }


  //
  // CellEditorListener
  //
  public void editingStopped(ChangeEvent e) {
    TableCellEditor editor = getCellEditor();
    if (editor != null) {
      Object value = editor.getCellEditorValue();
      int index = getEditingColumn();
      Object old = columnModel.getColumn(index).getHeaderValue();
      columnModel.getColumn(index).setHeaderValue(value);
      columnModel.getColumn(index).setIdentifier(value);
      firePropertyChange("headerValue", old, value);
      removeEditor();
    }
  }

  public void editingCanceled(ChangeEvent e) {
	  removeEditor();
  }

  
  //
  // public void setReorderingAllowed(boolean b) {
  //   reorderingAllowed = false;
  // }

}


