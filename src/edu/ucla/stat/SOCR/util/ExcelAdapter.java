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

/**
 * Package: edu.ucla.stat.SOCR.util
 * User: Khashim
 * Date: Dec 23, 2008
 * Time: 8:25:14 PM
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;

/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by Excel. This provides for clipboard
 * interoperability between enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener
{
    private String rowstring, value;
    private Clipboard system;
    private StringSelection stsel;
    private JTable jTable;

    /**
     * The Excel Adapter is constructed with a
     * JTable on which it enables Copy-Paste and acts
     * as a Clipboard listener.
     */

    public ExcelAdapter(JTable myJTable)
    {
        jTable = myJTable;
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        // Identifying the copy KeyStroke user can modify this
        // to copy on some other Key combination.
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        jTable.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);

        jTable.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
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
                return;
            }
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
            stsel = new StringSelection(sbf.toString());
            system = Toolkit.getDefaultToolkit().getSystemClipboard();
            system.setContents(stsel, stsel);
        }
        if (e.getActionCommand().compareTo("Paste") == 0)
        {
            //System.out.println("Trying to Paste");
            int startRow = (jTable.getSelectedRows())[0];
            int startCol = (jTable.getSelectedColumns())[0];

            try
            {
                String trstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
                //System.out.println("String is: " + trstring);
                StringTokenizer st1 = new StringTokenizer(trstring, "\n");
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
            catch (Exception ex){ex.printStackTrace();}
      }
   }
}
