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
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * @version 1.0 08/21/99
 */
public class EditableHeaderUI extends BasicTableHeaderUI {

    @Override
    public void installUI(JComponent c)
    {
        super.installUI(c);

        ComponentUI cui = UIManager.getUI(c);
        cui.installUI(c);
    }

  protected MouseInputListener createMouseInputListener() {
    return new MouseInputHandler();
  }

  public class MouseInputHandler extends BasicTableHeaderUI.MouseInputHandler {
    private Component dispatchComponent;

    private void setDispatchComponent(MouseEvent e) {
      Component editorComponent = ((EditableHeader)header).getEditorComponent();
      Point p = e.getPoint();
      Point p2 = SwingUtilities.convertPoint(header, p, editorComponent);
      dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent,
                                                               p2.x, p2.y);
    }

    private boolean repostEvent(MouseEvent e) {
      if (dispatchComponent == null) {
        return false;
      }
      MouseEvent e2 = SwingUtilities.convertMouseEvent(header, e, dispatchComponent);
      dispatchComponent.dispatchEvent(e2);
      return true;
    }

    public void mousePressed(MouseEvent e) {
      if (!SwingUtilities.isLeftMouseButton(e)) {
        return;
      }
      super.mousePressed(e);

      if (header.getResizingColumn() == null) {
        Point p = e.getPoint();
        TableColumnModel columnModel = header.getColumnModel();
        int index = columnModel.getColumnIndexAtX(p.x);
        if (index != -1) {
          if (((EditableHeader)header).editCellAt(index, e)) {
            setDispatchComponent(e);
            repostEvent(e);
          }
        }
      }
    }

    public void mouseReleased(MouseEvent e) {
      super.mouseReleased(e);
      if (!SwingUtilities.isLeftMouseButton(e)) {
        return;
      }
      repostEvent(e);
      dispatchComponent = null;
    }

  }
}
