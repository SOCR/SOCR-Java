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
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Renderer for table header. It displays arrows on sorted column's header.
 */
public class SortedTableHeaderRenderer implements TableCellRenderer {
    private static final ImageIcon ARROW_UP = new ImageIcon(SortedTableHeaderRenderer.class.getClassLoader().getResource("edu/ucla/stat/SOCR/util/tablemodels/img/ArrowUp.gif"));
    private static final ImageIcon ARROW_DOWN = new ImageIcon(SortedTableHeaderRenderer.class.getClassLoader().getResource("edu/ucla/stat/SOCR/util/tablemodels/img/ArrowDown.gif"));
    private JTable table;
    private SortedTableModel sortedTableModel;
    private TableCellRenderer defaultRenderer;

    /**
     * Creates <code>SortedTableHeaderRenderer</code> object with specified <code>JTable</code>
     * and <code>SortedTableModel</code>.
     *
     * @param table            <code>JTable</code> component
     * @param sortedTableModel <code>SortedTableModel</code> object
     */
    public SortedTableHeaderRenderer(JTable table, SortedTableModel sortedTableModel) {
        this.table = table;
        this.sortedTableModel = sortedTableModel;
        defaultRenderer = table.getTableHeader().getDefaultRenderer();
    }


    /**
     * Returns the component used for drawing the cell.  This method is
     * used to configure the renderer appropriately before drawing.
     *
     * @param    table        the <code>JTable</code> that is asking the
     * renderer to draw; can be <code>null</code>
     * @param    value        the value of the cell to be rendered.  It is
     * up to the specific renderer to interpret
     * and draw the value.  For example, if
     * <code>value</code>
     * is the string "true", it could be rendered as a
     * string or it could be rendered as a check
     * box that is checked.  <code>null</code> is a
     * valid value
     * @param    isSelected    true if the cell is to be rendered with the
     * selection highlighted; otherwise false
     * @param    hasFocus    if true, render cell appropriately.  For
     * example, put a special border on the cell, if
     * the cell can be edited, render in the color used
     * to indicate editing
     * @param    row     the row index of the cell being drawn.  When
     * drawing the header, the value of
     * <code>row</code> is -1
     * @param    column     the column index of the cell being drawn
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component tableCellRendererComponent = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return new SortHeaderRendererComponent2(tableCellRendererComponent, table.getTableHeader().getFont(), column);
    }

    private class SortRendererComponent extends JLabel {
        private Component tableCellRendererComponent;
        private int currentColumn;
        private final static int offset_x = 2;
        private final static int offset_y = 2;
        private final static int offset_x_index = 2;
        private final static float index_font_size = 10;

        public SortRendererComponent(Component tableCellRendererComponent, int currentColumn) {
            this.tableCellRendererComponent = tableCellRendererComponent;
            this.currentColumn = table.convertColumnIndexToModel(currentColumn);
            setPreferredSize(tableCellRendererComponent.getPreferredSize());
            setMaximumSize(tableCellRendererComponent.getMaximumSize());
            setMinimumSize(tableCellRendererComponent.getMinimumSize());
        }

        public void paint(Graphics g) {
            tableCellRendererComponent.setBounds(getBounds());
            tableCellRendererComponent.paint(g);
            int width = getWidth();
            int height = getHeight();
            if (sortedTableModel.isColumnAscending(currentColumn)) {
                int iconWidth = ARROW_DOWN.getIconWidth();
                int iconHeight = ARROW_DOWN.getIconHeight();
                if (sortedTableModel.getSortingColumnsCount() == 1)
                    g.drawImage(ARROW_DOWN.getImage(), width - iconWidth - offset_x, offset_y, iconWidth, iconHeight, null);
                else if (sortedTableModel.getSortingColumnsCount() > 1) {
                    String index = String.valueOf(sortedTableModel.getColumnSortRank(currentColumn));
                    g.setFont(g.getFont().deriveFont(index_font_size));
                    FontMetrics fontmetrics = getFontMetrics(g.getFont());
                    int indexWidth = fontmetrics.stringWidth(index);
                    int indexHeight = fontmetrics.getHeight();
                    g.drawString(index, width - indexWidth - offset_x, offset_y + height - (iconHeight / 2));
                    g.drawImage(ARROW_DOWN.getImage(), width - iconWidth - offset_x - indexWidth - offset_x_index, offset_y, iconWidth, iconHeight, null);
                }

            }
            if (sortedTableModel.isColumnDescending(currentColumn)) {
                int iconWidth = ARROW_UP.getIconWidth();
                int iconHeight = ARROW_UP.getIconHeight();
                if (sortedTableModel.getSortingColumnsCount() == 1)
                    g.drawImage(ARROW_UP.getImage(), width - iconWidth - offset_x, offset_y, iconWidth, iconHeight, null);
                else if (sortedTableModel.getSortingColumnsCount() > 1) {
                    String index = String.valueOf(sortedTableModel.getColumnSortRank(currentColumn));
                    g.setFont(g.getFont().deriveFont(index_font_size));
                    FontMetrics fontmetrics = getFontMetrics(g.getFont());
                    int indexWidth = fontmetrics.stringWidth(index);
                    int indexHeight = fontmetrics.getHeight();
                    g.drawString(index, width - indexWidth - offset_x, offset_y + height - (iconHeight / 2));
                    g.drawImage(ARROW_UP.getImage(), width - iconWidth - offset_x - indexWidth - offset_x_index, offset_y, iconWidth, iconHeight, null);
                }
            }
        }
    }

    private class SortHeaderRendererComponent2 extends JPanel {
        private class IconRendererComponent extends JLabel {
            public IconRendererComponent() {
                setIcon(getSortImage());
                if (!"Windows".equals(UIManager.getLookAndFeel().getName()))
                    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
//                    setBorder(BorderFactory.createEmptyBorder());
            }

            private ImageIcon getSortImage() {
                int sortingColumnsCount = sortedTableModel.getSortingColumnsCount();

                int iconWidth = Math.max(ARROW_DOWN.getIconWidth(), ARROW_UP.getIconWidth());
                int iconHeight = Math.max(ARROW_DOWN.getIconHeight(), ARROW_UP.getIconHeight());
                int indexWidth;
                int indexHeight;
                String index = String.valueOf(sortedTableModel.getColumnSortRank(currentColumn));
                FontMetrics fontmetrics = getFontMetrics(tableHeaderFont.deriveFont(index_font_size));

                if (sortingColumnsCount >= 1 && sortingColumnsCount <= 9) {
                    indexWidth = fontmetrics.stringWidth("w");
                    indexHeight = fontmetrics.getHeight();
                } else {
                    indexWidth = fontmetrics.stringWidth(index);
                    indexHeight = fontmetrics.getHeight();
                }

                ImageIcon imageIcon = null;
                try {
                    BufferedImage image = new BufferedImage(iconWidth + offset_x_index + indexWidth + offset_x, (iconHeight / 2) + indexHeight + offset_y, BufferedImage.TYPE_INT_ARGB);
                    Graphics g = image.getGraphics();

                    if (sortedTableModel.isColumnAscending(currentColumn))
                        g.drawImage(ARROW_DOWN.getImage(), offset_x, offset_y, ARROW_DOWN.getIconWidth(), ARROW_DOWN.getIconHeight(), null);
                    if (sortedTableModel.isColumnDescending(currentColumn))
                        g.drawImage(ARROW_UP.getImage(), offset_x, offset_y, ARROW_UP.getIconWidth(), ARROW_UP.getIconHeight(), null);

                    if (sortedTableModel.getSortingColumnsCount() > 1) {
                        g.setColor(Color.black);
                        g.setFont(tableHeaderFont.deriveFont(index_font_size));
                        g.drawString(index, iconWidth + offset_x_index + offset_x, offset_y + (iconHeight / 2) + (fontmetrics.getAscent() - fontmetrics.getDescent()));
                    }
                    imageIcon = new ImageIcon(image);
                } catch (Exception e) {}
                return imageIcon;
            }

            public Dimension getPreferredSize() {
                int sortingColumnsCount;
                sortingColumnsCount = sortedTableModel.getSortingColumnsCount();
                if (sortingColumnsCount == 0)
                    return new Dimension(0, 0);

                int iconWidth = Math.max(ARROW_DOWN.getIconWidth(), ARROW_UP.getIconWidth());
                int iconHeight = Math.max(ARROW_DOWN.getIconHeight(), ARROW_UP.getIconHeight());
                int indexWidth;
                int indexHeight;
                String index = String.valueOf(sortedTableModel.getColumnSortRank(currentColumn));
                FontMetrics fontmetrics = getFontMetrics(tableHeaderFont.deriveFont(index_font_size));

                if (sortingColumnsCount >= 1 && sortingColumnsCount <= 9) {
                    indexWidth = fontmetrics.stringWidth("w");
                    indexHeight = fontmetrics.getHeight();
                } else {
                    indexWidth = fontmetrics.stringWidth(index);
                    indexHeight = fontmetrics.getHeight();
                }
                int prefferedWidth = offset_x + iconWidth + offset_x_index + indexWidth + offset_x;
                int prefferedHeight = offset_y + (iconHeight / 2) + indexHeight + offset_y;
                return new Dimension(prefferedWidth, prefferedHeight);
            }
        }

        private Component tableCellRendererComponent;
        private Font tableHeaderFont;
        private Component iconRendererComponent;
        private int currentColumn;
        private final static int offset_x = 3;
        private final static int offset_y = 3;
        private final static int offset_x_index = 2;
        private final static float index_font_size = 10;

        public SortHeaderRendererComponent2(Component tableCellRendererComponent, Font tableHeaderFont, int currentColumn) {
            this.tableCellRendererComponent = tableCellRendererComponent;
            this.tableHeaderFont = tableHeaderFont;
            this.currentColumn = table.convertColumnIndexToModel(currentColumn);
            setPreferredSize(tableCellRendererComponent.getPreferredSize());
            setMaximumSize(tableCellRendererComponent.getMaximumSize());
            setMinimumSize(tableCellRendererComponent.getMinimumSize());

            setLayout(null);

            iconRendererComponent = new IconRendererComponent();

            add(tableCellRendererComponent);
            if (sortedTableModel.isColumnSorted(currentColumn))
                add(iconRendererComponent);
        }

        public Dimension getPreferredSize() {
            if (sortedTableModel.isColumnSorted(currentColumn))
                return new Dimension(super.getPreferredSize().width + iconRendererComponent.getPreferredSize().width, super.getPreferredSize().height);
            else
                return super.getPreferredSize();
        }

        public void doLayout() {
            int width = getWidth();
            int height = getHeight();
            int iconWidth = iconRendererComponent.getPreferredSize().width;
            if (sortedTableModel.isColumnSorted(currentColumn)) {
                tableCellRendererComponent.setBounds(0, 0, width - iconWidth, height);
                iconRendererComponent.setBounds(width - iconWidth, 0, iconWidth, height);
            } else {
                tableCellRendererComponent.setBounds(0, 0, width, height);
                iconRendererComponent.setBounds(0, 0, 0, 0);
            }
        }
    }
}
