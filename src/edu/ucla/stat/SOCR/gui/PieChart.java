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
import java.awt.*;

public class PieChart extends GraphBase {
    private int totalValue;

    public PieChart(String title) {
        super(title);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        top += 15; // give more space between title and the real chart

        int D = Math.min(bottom - top, right - left) - padding - 30;
        int R = D / 2;
        int arcX = left + (right - left - D) / 2;
        int arcY = top + (bottom - top - D) / 2;
        int centerX = arcX + R, centerY = arcY + R;
        
        Color temp = g.getColor();
        
        int adjustedV = 0;        
        for (int i = 0, startV = 0; i < items.size(); i++, startV += adjustedV) {
            GraphItem item = (GraphItem)items.get(i);
            
            adjustedV = item.value * 360 / totalValue;
            g.setColor(item.color);
            g.fillArc(arcX, arcY, D, D, startV, adjustedV);
            
            String label = item.title + " (" + (item.value * 100 / totalValue) + "%)";
            int midA = startV + adjustedV / 2;
            int titleY = centerY - (int)(Math.sin(Math.toRadians(midA)) * R);
            
            int offX =     Math.abs((int)(Math.cos(Math.toRadians(midA)) * R));
            int titleX = centerX;
            if (midA >= 270 || midA < 90)
                titleX += offX;
            else 
                titleX -= offX;
            
            if (titleX <= centerX) {
                titleX -= fm.stringWidth(label) + 1;
            } else if (titleX > centerX) {
                titleX += 1;
            }
            if (titleY > centerY) {
                titleY += fm.getHeight() + 1;
            } else if (titleY <= centerY) {
                titleY -= 1;
            }
            
            g.setColor(temp);
            g.drawString(label, titleX, titleY);
        }
    } // end paint
    
    public void addItem(String name, int value, Color col) {
        items.add(new GraphItem(name, value, col));
        totalValue += value;
    } 
}
