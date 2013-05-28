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

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class GraphBase extends JComponent {
    public int top;
    public int bottom;
    public int left;
    public int right;
    
    protected int titleHeight;
    protected int labelWidth;
    protected FontMetrics fm;
    protected int padding = 4;
    public String title;
    protected int min;
    protected int max;

    /**
     * 
     * @uml.property name="items"
     * @uml.associationEnd multiplicity="(0 -1)" elementType="edu.ucla.stat.SOCR.gui.GraphBase$GraphItem"
     */
    protected ArrayList<GraphItem> items = new ArrayList<GraphItem>();

    public GraphBase(String title, int min, int max) {
        this.title = title;
        this.min = min;
        this.max = max;
    }

    public GraphBase(String title) {
        this.title = title;
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        fm = getFontMetrics(getFont());
        titleHeight = fm.getHeight() + padding + padding;
        if (min != max) {
            labelWidth = Math.max(fm.stringWidth(new Integer(min).toString()),
                                  fm.stringWidth(new Integer(max).toString())) + 2;
        }
        top = padding + titleHeight;
        bottom = getSize().height - padding;
        left = padding + labelWidth;
        right = getSize().width - padding;
    }

    public void paint(Graphics g) {
        // draw the title
        fm = getFontMetrics(getFont());
        g.drawString(title, (getSize().width - fm.stringWidth(title))/2, titleHeight);
        if (min == max)
            return;
        // draw the max and min values
        g.drawString(new Integer(min).toString(), padding, bottom);
        g.drawString(new Integer(max).toString(), padding, top + titleHeight);
        // draw the vertical and horizontal lines
        g.drawLine(left, top, left, bottom);
        g.drawLine(left, bottom, right, bottom);
    } // end paint

    public Dimension getPreferredSize() {
        return(new Dimension(300, 200));
    }

    public void addItem(String name, int value, Color col) {
        items.add(new GraphItem(name, value, col));
    } 

    public void addItem(String name, int value) {
        addItem(name, value, Color.black);
    } 

    public static class GraphItem {
        String title;
        int value;
        Color color;

        public GraphItem(String title, int value, Color color) {
            this.title = title;
            this.value = value;
            this.color = color;
        }
    } // end GraphItem
}
