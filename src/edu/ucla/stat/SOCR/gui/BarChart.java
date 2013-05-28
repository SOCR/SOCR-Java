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

import java.awt.*;
import javax.swing.*;

public class BarChart extends GraphBase {   
    
    public BarChart(String title, int min, int max) {
        super(title, min, max);
    } 
    
    public void paint(Graphics g) {
        super.paint(g);
        
        int increment = (right - left)/(items.size());
        int position = left;
        Color temp = g.getColor();
        
        for (int i = 0; i < items.size(); i++) {
            GraphItem item = (GraphItem)items.get(i);
            int adjustedValue = bottom - (((item.value - min)*(bottom - top))
            /(max - min));
            g.drawString(item.title, position + (increment -
            fm.stringWidth(item.title))/2, adjustedValue - 5);
            g.setColor(item.color);
            g.fillRect(position + 5, adjustedValue, increment - 5 ,
            bottom - adjustedValue);
            position+=increment;
            g.setColor(temp);
        }
    }
}
