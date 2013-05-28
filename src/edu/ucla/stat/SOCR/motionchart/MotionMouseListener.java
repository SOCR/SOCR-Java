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

package edu.ucla.stat.SOCR.motionchart;

import edu.ucla.stat.SOCR.util.RowHeaderTable;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.XYItemEntity;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * Date: Dec 20, 2008
 * Time: 7:31:25 PM
 *
 * @author Jameel
 */
@SuppressWarnings({"InconsistentJavaDoc"})
public class MotionMouseListener implements ChartMouseListener
{
    /**
     * Callback method for receiving notification of a mouse click on a chart.
     *
     * @param event information about the event.
     */
    public void chartMouseClicked(ChartMouseEvent event)
    {
        if (event.getTrigger().getClickCount() > 1)
        {
            if (event.getEntity() instanceof XYItemEntity)
            {
                XYItemEntity item = (XYItemEntity) event.getEntity();
                Component c = event.getTrigger().getComponent();
                final JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(c)
                        , "Item Data", false);
                dialog.setSize(400, 300);
                dialog.setLocation(getDialogLocation(dialog, c));
                dialog.add(getItemPanel(dialog, item, event));
                dialog.setVisible(true);
            }
            else
            {
                XYItemEntity item = (XYItemEntity) event.getEntity();
                Component c = event.getTrigger().getComponent();
                final JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(c)
                        , "Item Data", false);
                dialog.setSize(400, 300);
                dialog.setLocation(getDialogLocation(dialog, c));
                dialog.add(getSeriesPanel(dialog, event));
                dialog.setVisible(true);
            }
        }
        else
        {
            if (!(event.getChart().getXYPlot().getRenderer() instanceof MotionBubbleRenderer))
            {
                return;
            }

            MotionBubbleRenderer renderer = (MotionBubbleRenderer) event.getChart().getXYPlot().getRenderer();
            MotionDataSet dataset = (MotionDataSet) event.getChart().getXYPlot().getDataset();

            if (event.getEntity() instanceof XYItemEntity)
            {
                boolean selected;
                XYItemEntity entity = (XYItemEntity) event.getEntity();
                int series = entity.getSeriesIndex();
                int item = entity.getItem();
                Object category = dataset.getCategory(series, item);

                if(category == null)
                {
                    selected = !renderer.isSelectedItem(series, item);
                    renderer.setSelectedItem(series, item, selected);
                }
                else
                {
                    selected = !renderer.isSelectedCategory(category);
                    renderer.setSelectedCategory(category, selected);
                }
            }
        }
    }

    protected Point getDialogLocation(JDialog dialog, Component c)
    {
        Frame frame = JOptionPane.getFrameForComponent(c);
        int x = frame.getX() + (frame.getWidth() - dialog.getWidth()) / 2;
        int y = frame.getY() + (frame.getHeight() - dialog.getHeight()) / 2;
        return new Point(x, y);
    }

    protected JPanel getItemPanel(final JDialog dialog, XYItemEntity item, ChartMouseEvent event)
    {
        MotionDataSet dataset = (MotionDataSet) event.getChart().getXYPlot().getDataset();
        MotionTableModel model = dataset.getTableModel();
        DefaultTableModel tModel = new DefaultTableModel(2, 0);
        ArrayList<String> rowIds = new ArrayList<String>();

        Integer row = model.getKeyMap().get(dataset.getSeriesKey(item.getSeriesIndex())).get(item.getItem());
        rowIds.add(row + ":");

        int columnCount = model.getColumnCount();

        for (int c = 0; c < columnCount; c++)
        {
            tModel.addColumn(model.getColumnName(c));
            tModel.setValueAt(model.getValueAt(row, c), 0, c);
        }

        rowIds.add("Mappings:");

        Object category = dataset.getCategory(item.getSeriesIndex(), item.getItem());
        if (category != null)
        {
            tModel.setValueAt(category, 1, model.getCategoryMapping());
        }

        Object key = dataset.getSeriesKey(item.getSeriesIndex());
        if (key != null)
        {
            tModel.setValueAt(key, 1, model.getKeyMapping());
        }

        Object x = dataset.getX(item.getSeriesIndex(), item.getItem());
        if (x != null)
        {
            tModel.setValueAt(x, 1, model.getXAxisMapping());
        }

        Object y = dataset.getY(item.getSeriesIndex(), item.getItem());
        if (y != null)
        {
            tModel.setValueAt(y, 1, model.getYAxisMapping());
        }

        Object size = dataset.getSize(item.getSeriesIndex(), item.getItem());
        if (size != null)
        {
            tModel.setValueAt(size, 1, model.getSizeMapping());
        }

        Color color = dataset.getColor(item.getSeriesIndex(), item.getItem());
        if (color != null)
        {
            //String colorText = "RGB(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
            tModel.setValueAt(color, 1, model.getColorMapping());
        }

        return getPanel(dialog, tModel, rowIds);
    }

    protected JPanel getSeriesPanel(final JDialog dialog, ChartMouseEvent event)
    {
        MotionDataSet dataset = (MotionDataSet) event.getChart().getXYPlot().getDataset();
        MotionBubbleRenderer renderer = (MotionBubbleRenderer) event.getChart().getXYPlot().getRenderer();
        MotionTableModel model = dataset.getTableModel();
        DefaultTableModel tModel = new DefaultTableModel();
        ArrayList<Integer> visibleSeries = renderer.getVisibleSeries();
        ArrayList<String> rowIds = new ArrayList<String>();

        int columnCount = model.getColumnCount();
        int r = 0;

        for (int c = 0; c < columnCount; c++)
        {
            tModel.addColumn(model.getColumnName(c));
        }

        Iterator<Integer> itr = visibleSeries.iterator();

        while (itr.hasNext())
        {
            ArrayList<Integer> rows = model.getKeyMap().get(dataset.getSeriesKey(itr.next()));

            Iterator<Integer> rItr = rows.iterator();

            while (rItr.hasNext())
            {
                int row = rItr.next();

                tModel.addRow(new Object[0]);
                rowIds.add(new String(row + ":"));

                for (int c = 0; c < columnCount; c++)
                {
                    tModel.setValueAt(model.getValueAt(row, c), r, c);
                }

                r++;
            }
        }

        return getPanel(dialog, tModel, rowIds);
    }

    protected JPanel getPanel(final JDialog dialog, DefaultTableModel tModel, ArrayList<String> rowIdentifiers)
    {
        JPanel panel = new JPanel();
        JButton close = new JButton("Close");
        close.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        dialog.setVisible(false);
                    }
                }
        );
        close.setAlignmentX(Component.CENTER_ALIGNMENT);

        RowHeaderTable table = new RowHeaderTable(tModel);
        table.getDataTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getDataTable().setDefaultRenderer(Object.class, new ColorRenderer(false));
        table.setCellsEditable(false);
        table.setHeadersEditable(false);

        for (int r = 0; r < tModel.getRowCount() && rowIdentifiers.size() <= tModel.getRowCount(); r++)
        {
            table.getRowHeaderModel().setValueAt(rowIdentifiers.get(r), r, 0);
        }

        Dimension d = table.getRowHeaderTable().getPreferredScrollableViewportSize();
        d.width = 55;
        table.getRowHeaderTable().setPreferredScrollableViewportSize(d);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(table);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(close);

        return panel;
    }

    /**
     * Callback method for receiving notification of a mouse movement on a
     * chart.
     *
     * @param event information about the event.
     */
    public void chartMouseMoved(ChartMouseEvent event)
    {
        if (!(event.getChart().getXYPlot().getRenderer() instanceof MotionBubbleRenderer))
        {
            return;
        }

        MotionBubbleRenderer renderer = (MotionBubbleRenderer) event.getChart().getXYPlot().getRenderer();

        if (!(event.getEntity() instanceof XYItemEntity))
        {
            renderer.setHighlightedItem(-1, -1);
            return;
        }

        XYItemEntity item = (XYItemEntity) event.getEntity();
        renderer.setHighlightedItem(item.getSeriesIndex(), item.getItem());
    }

    private class ColorRenderer extends DefaultTableCellRenderer
    {
        Border unselectedBorder = null;
        Border selectedBorder = null;
        boolean isBordered = true;
        Color defaultBackground;
        Color defaultForeground;

        public ColorRenderer(boolean isBordered)
        {
            this.isBordered = isBordered;
            setOpaque(true); //MUST do this for background to show up.
            defaultBackground = getBackground();
            defaultForeground = getForeground();
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column)
        {
            if(value instanceof Color)
            {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Color newColor = (Color) value;
                setBackground(newColor);
                int red = newColor.getRed();
                int green = newColor.getGreen();
                int blue = newColor.getBlue();
                setForeground(complement(newColor));
                setValue("RGB(" + red + "," + green + "," + blue + ")");

                if (isBordered)
                {
                    if (isSelected)
                    {
                        if (selectedBorder == null)
                        {
                            selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5,
                                    table.getSelectionBackground());
                        }
                        setBorder(selectedBorder);
                    }
                    else
                    {
                        if (unselectedBorder == null)
                        {
                            unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5,
                                    table.getBackground());
                        }
                        setBorder(unselectedBorder);
                    }
                }

                setToolTipText("RGB value: " + newColor.getRed() + ", "
                        + newColor.getGreen() + ", "
                        + newColor.getBlue());
            }
            else
            {
                setBackground(defaultBackground);
                setForeground(defaultForeground);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }

            return this;
        }

        protected Color complement(Color color)
        {
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();

            r = (127 + r) % 256;
            g = (127 + g) % 256;
            b = (127 + b) % 256;
//            float[] hsl = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
//
//            hsl[0] = hsl[0] + 0.5f;
//
//            if(hsl[0] > 1)
//                hsl[0] -= 1;
//
//            if(hsl[2] == 0)
//            {
//                hsl[2] = 1;
//            }
//
//            int rgb = Color.HSBtoRGB(hsl[0], hsl[1], hsl[2]);
//
//            return new Color(rgb);

            return new Color(r,g,b);
        }
    }
}
