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
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.util;
import javax.swing.*;
import javax.swing.table.*;


/**
 * @version 1.0 08/21/99
 */
public class EditableHeaderTableColumn extends TableColumn {

  protected TableCellEditor headerEditor;
  protected boolean isHeaderEditable;

  public EditableHeaderTableColumn() {
    setHeaderEditor(createDefaultHeaderEditor());
    isHeaderEditable = true;
  }
  
  public void setHeaderEditor(TableCellEditor headerEditor) {
    this.headerEditor = headerEditor;
  }
  
  public TableCellEditor getHeaderEditor() {
    return headerEditor;
  }
  
  public void setHeaderEditable(boolean isEditable) {
    isHeaderEditable = isEditable;
  }
  
  public boolean isHeaderEditable() {
    return isHeaderEditable;
  }
  
  public void copyValues(TableColumn base) {    
    modelIndex     = base.getModelIndex();
    identifier     = base.getIdentifier();
    width          = base.getWidth();
    minWidth       = base.getMinWidth();
    setPreferredWidth(base.getPreferredWidth());
    maxWidth       = base.getMaxWidth();
    headerRenderer = base.getHeaderRenderer();
    headerValue    = base.getHeaderValue();
    cellRenderer   = base.getCellRenderer();
    cellEditor     = base.getCellEditor();
    isResizable    = base.getResizable();
  }
  
  protected TableCellEditor createDefaultHeaderEditor() {
    return new DefaultCellEditor(new JTextField());
  }
  
}
