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

package edu.ucla.stat.SOCR.util.tablemodels.renderers;

import edu.ucla.stat.SOCR.util.tablemodels.wrappers.DefaultTextWrapper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * The standard class for rendering cells with {@link DefaultTextWrapper} value.
 */
public class DefaultTextCellRenderer extends DefaultTableCellRenderer {
    /**
     * Returns the default table cell renderer.
     *
     * @param table      the <code>JTable</code>
     * @param value      the {@link edu.ucla.stat.SOCR.util.tablemodels.wrappers.DefaultTextWrapper} value to assign to the cell at <code>[row, column]</code>
     * @param isSelected true if cell is selected
     * @param hasFocus   true if cell has focus
     * @param row        the row of the cell to render
     * @param column     the column of the cell to render
     * @return the default table cell renderer.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DefaultTextWrapper textWrapper = (DefaultTextWrapper) value;
        JLabel tableCellRendererComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        tableCellRendererComponent.setOpaque(true);
        Color unselectedForeground = textWrapper.getColor();
        Color unselectedBackground = textWrapper.getBackgroundColor();

        if (isSelected) {
            tableCellRendererComponent.setForeground(table.getSelectionForeground());
            tableCellRendererComponent.setBackground(table.getSelectionBackground());
        } else {
            tableCellRendererComponent.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
            tableCellRendererComponent.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
        }

        Font font = textWrapper.getFont();
        if (font != null)
            tableCellRendererComponent.setFont(font);

        tableCellRendererComponent.setText(textWrapper.getText());

        tableCellRendererComponent.setHorizontalAlignment(textWrapper.getHorizontalAlignment());
        tableCellRendererComponent.setVerticalAlignment(textWrapper.getVerticalAlignment());

        return tableCellRendererComponent;
    }

}
