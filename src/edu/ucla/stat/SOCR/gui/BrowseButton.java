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
package edu.ucla.stat.SOCR.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class BrowseButton extends JButton {

    /**
     * 
     * @uml.property name="textComponent"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JTextComponent textComponent = null;

    /**
     * 
     * @uml.property name="fc"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    JFileChooser fc = null;

    public BrowseButton(JTextComponent text, JFileChooser fchooser) {
        super("Brows...");
        textComponent = text;
        this.fc = fchooser;
        addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fc.showOpenDialog(BrowseButton.this) == fc.APPROVE_OPTION) {
                    textComponent.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }});
    }
}
