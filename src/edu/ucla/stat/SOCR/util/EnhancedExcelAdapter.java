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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Package: edu.ucla.stat.SOCR.util
 * User: Khashim
 * Date: Dec 23, 2008
 * Time: 9:13:42 PM
 *
 * @author Jameel
 */
public class EnhancedExcelAdapter implements ActionListener
{
    private String rowstring, value;
    private Clipboard system;
    private ProgressMonitor monitor;
    private StringSelection stsel;
    private JTable jTable;
    private DefaultTableModel model;
    private EnhancedDefaultTableModel eModel;
    private boolean enhanced;
    private JDialog dialog;

    /**
     * The Excel Adapter is constructed with a
     * JTable on which it enables Copy-Paste and acts
     * as a Clipboard listener.
     */

    public EnhancedExcelAdapter(JTable myJTable)
    {
        jTable = myJTable;
        enhanced = false;

        if (jTable.getModel() instanceof DefaultTableModel)
        {
            model = (DefaultTableModel) jTable.getModel();
        }

        if(model instanceof EnhancedDefaultTableModel)
        {
            eModel = (EnhancedDefaultTableModel)model;
            enhanced = true;
        }

        int controlMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, controlMask, false);
        // Identifying the copy KeyStroke user can modify this
        // to copy on some other Key combination.
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, controlMask, false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        jTable.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);

        jTable.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
        system = Toolkit.getDefaultToolkit().getSystemClipboard();

    }

    protected Point getDialogLocation(JDialog dialog, Component c)
    {
        Frame frame = JOptionPane.getFrameForComponent(c);
        int x = frame.getX() + (frame.getWidth() - dialog.getWidth()) / 2;
        int y = frame.getY() + (frame.getHeight() - dialog.getHeight()) / 2;
        return new Point(x, y);
    }

    protected JPanel getDialogPanel(final JDialog dialog)
    {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Pasting! Please wait...");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(close);

        return panel;
    }

    protected void showWaitDialog()
    {
        dialog = new JDialog(JOptionPane.getFrameForComponent(jTable), "Pasting!", false);
        dialog.setSize(200, 100);
        dialog.setLocation(getDialogLocation(dialog, jTable));
        dialog.add(getDialogPanel(dialog));
        dialog.setVisible(true);
    }

    protected void hideWaitDialog()
    {
        dialog.setVisible(false);
    }

    /**
     * Public Accessor methods for the Table on which this adapter acts.
     */
    public JTable getJTable()
    {
        return jTable;
    }

    public void setJTable(JTable jTable1)
    {
        this.jTable = jTable1;
    }

    /**
     * This method is activated on the Keystrokes we are listening to
     * in this implementation. Here it listens for Copy and Paste ActionCommands.
     * Selections comprising non-adjacent cells result in invalid selection and
     * then copy action cannot be performed.
     * Paste is done by aligning the upper left corner of the selection with the
     * 1st element in the current selection of the JTable.
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().compareTo("Copy") == 0)
        {
            StringBuffer sbf;

            if (model != null)
            {
                sbf = copyFromModel();
            }
            else
            {
                sbf = copyFromTable();
            }

            if (sbf == null)
            {
                return;
            }

            stsel = new StringSelection(sbf.toString());
            system = Toolkit.getDefaultToolkit().getSystemClipboard();
            system.setContents(stsel, stsel);
        }
        if (e.getActionCommand().compareTo("Paste") == 0)
        {
            //System.out.println("Trying to Paste");
            final int startRow = (jTable.getSelectedRows())[0];
            final int startCol = (jTable.getSelectedColumns())[0];

            try
            {
                final String trstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
                //System.out.println("String is: " + trstring);

                showWaitDialog();

                if(model != null)
                {
                    pasteToModel(trstring, startRow, startCol);
                }
                else
                {
                    pasteToTable(trstring, startRow, startCol);
                }

                hideWaitDialog();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    protected StringBuffer copyFromModel()
    {
        StringBuffer sbf = new StringBuffer();
        // Check to ensure we have selected only a contiguous block of
        // cells
        int numcols = jTable.getSelectedColumnCount();
        int numrows = jTable.getSelectedRowCount();
        int[] rowsselected = jTable.getSelectedRows();
        int[] colsselected = jTable.getSelectedColumns();
        if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0] &&
                numrows == rowsselected.length) &&
                (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0] &&
                        numcols == colsselected.length)))
        {
            JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                    "Invalid Copy Selection",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        for(int c = 0; c < numcols; c++)
        {
            sbf.append(model.getColumnName(colsselected[c]));

            if(c < numcols - 1)
                sbf.append("\t");
        }

        sbf.append("\n");

        for (int i = 0; i < numrows; i++)
        {
            for (int j = 0; j < numcols; j++)
            {
                sbf.append(model.getValueAt(rowsselected[i], colsselected[j]));
                if (j < numcols - 1)
                {
                    sbf.append("\t");
                }
            }
            sbf.append("\n");
        }

        return sbf;
    }

    protected StringBuffer copyFromTable()
    {
        StringBuffer sbf = new StringBuffer();
        // Check to ensure we have selected only a contiguous block of
        // cells
        int numcols = jTable.getSelectedColumnCount();
        int numrows = jTable.getSelectedRowCount();
        int[] rowsselected = jTable.getSelectedRows();
        int[] colsselected = jTable.getSelectedColumns();
        if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0] &&
                numrows == rowsselected.length) &&
                (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0] &&
                        numcols == colsselected.length)))
        {
            JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                    "Invalid Copy Selection",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        for(int c = 0; c < numcols; c++)
        {
            sbf.append(jTable.getColumnName(colsselected[c]));

            if(c < numcols - 1)
                sbf.append("\t");
        }

        sbf.append("\n");

        for (int i = 0; i < numrows; i++)
        {
            for (int j = 0; j < numcols; j++)
            {
                sbf.append(jTable.getValueAt(rowsselected[i], colsselected[j]));
                if (j < numcols - 1)
                {
                    sbf.append("\t");
                }
            }
            sbf.append("\n");
        }

        return sbf;
    }

    protected void pasteToModel(String pasteString, int startRow, int startCol)
    {
        Vector<String> colIds = new Vector<String>();
        StringTokenizer st1 = new StringTokenizer(pasteString, "\n");

        setNotifications(false);

        if(st1.hasMoreTokens())
        {
            rowstring = st1.nextToken();
            StringTokenizer st2 = new StringTokenizer(rowstring, " \t"); //"\t");

            for(int c = 0; c < startCol; c++)
            {
                colIds.add(model.getColumnName(c));
            }

            for(int c = 0; st2.hasMoreTokens(); c++)
            {
                value = st2.nextToken();
                colIds.add(value);
            }

            model.setColumnIdentifiers(colIds);
            notifyStructureChanged();
        }

        for (int i = 0; st1.hasMoreTokens(); i++)
        {
            rowstring = st1.nextToken();
            StringTokenizer st2 = new StringTokenizer(rowstring, " \t"); //"\t");
            for (int j = 0; st2.hasMoreTokens(); j++)
            {
                value = st2.nextToken();
                if (startRow + i >= model.getRowCount())
                {
                    model.addRow(new Object[0]);
                }

                if(startCol + j >= model.getColumnCount())
                {
                    model.addColumn(null);
                    notifyStructureChanged();
                }

                model.setValueAt(value, startRow + i, startCol + j);

                //System.out.println("Putting " + value + " at row=" + startRow + i + ", column=" + startCol + j);
            }
        }

        notifyDataChanged();
        setNotifications(true);
    }

    protected void pasteToTable(String pasteString, int startRow, int startCol)
    {
        StringTokenizer st1 = new StringTokenizer(pasteString, "\n");

        if(st1.hasMoreTokens())
        {
            rowstring = st1.nextToken();
            StringTokenizer st2 = new StringTokenizer(rowstring, " \t"); //"\t");

            for(int c = 0; st2.hasMoreTokens(); c++)
            {
                value = st2.nextToken();
                if (startCol + c < jTable.getColumnCount())
                {
                    jTable.getColumnModel().getColumn(startCol + c).setHeaderValue(value);
                    jTable.getColumnModel().getColumn(startCol + c).setIdentifier(value);
                }
            }
        }

        for (int i = 0; st1.hasMoreTokens(); i++)
        {
            rowstring = st1.nextToken();
            StringTokenizer st2 = new StringTokenizer(rowstring, " \t"); //"\t");
            for (int j = 0; st2.hasMoreTokens(); j++)
            {
                value = st2.nextToken();
                if (startRow + i < jTable.getRowCount() &&
                        startCol + j < jTable.getColumnCount())
                {
                    jTable.setValueAt(value, startRow + i, startCol + j);
                }
                //System.out.println("Putting " + value + " at row=" + startRow + i + ", column=" + startCol + j);
            }
        }
    }

    protected void setNotifications(final boolean notify)
    {
        if(enhanced)
        {
            eModel.setNotify(notify);
        }
    }

    protected void notifyStructureChanged()
    {
        if(enhanced)
        {
            boolean prevNotify = eModel.getNotify();
            eModel.setNotify(true);
            eModel.fireTableStructureChanged();
            eModel.setNotify(prevNotify);
        }
    }

    protected void notifyDataChanged()
    {
        if(enhanced)
        {
            boolean prevNotify = eModel.getNotify();
            eModel.setNotify(true);
            eModel.fireTableDataChanged();
            eModel.setNotify(prevNotify);
        }
    }
}
