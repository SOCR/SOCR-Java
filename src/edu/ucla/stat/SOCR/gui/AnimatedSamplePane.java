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
import java.awt.event.*;
import java.util.*;

/**
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class AnimatedSamplePane extends JPanel {
    static Color bg = Color.WHITE;
    static Color fg = Color.black;
    
    int cellHeight = 20;
    long sleeptime = 150L;
    int delta = 20;

    /**
     * 
     * @uml.property name="createdSample"
     * @uml.associationEnd multiplicity="(0 -1)" inverse="this$0:edu.ucla.stat.SOCR.gui.AnimatedSamplePane$Cell"
     */
    java.util.List<Cell> createdSample = new ArrayList<Cell>();

    
    private class Cell extends Rectangle {
        Cell() {
            super(10, cellHeight);
        }
        
        void clear(Graphics g) {
            g.setColor(bg);
            g.fillRect(x, y, width, height); 
        }
        
        void setLocation(int x, int y, Graphics g) {
            super.setLocation(x, y);
            paint(g);
        }
        
        void startmove(Graphics g, int start, int end) {
            for (int delty = start; delty < end; delty += delta) {
            try {
                Thread.sleep(sleeptime);
                clear(g);
                setLocation(x, y += delta, g);
            } catch (InterruptedException e) { }
            }
            System.out.println("Finished");             
        }
        
        void paint(Graphics g) {
            g.setColor(fg);
            g.fillRect(x, y, width, height); 
        }
    }
    
    public AnimatedSamplePane() {
        setSize(400, 300);
        setBackground(bg);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < createdSample.size(); i ++) {
            ((Cell)createdSample.get(i)).paint(g);
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("test animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final AnimatedSamplePane ap = new AnimatedSamplePane();
        frame.getContentPane().add(ap, "Center");
        JButton button = new JButton("Click");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ap.start();
            }
        });
        frame.getContentPane().add(button, "South");
        frame.setSize(400,250);
        frame.validate();
        frame.setVisible(true);
        
    }

    public void start() {
        int offset = cellHeight;
        int x = 200;
        for ( int i = 0; i < createdSample.size(); i++) {
            if (((Cell)createdSample.get(i)).x == x) offset += cellHeight;           
        }
        final int end = getHeight()- offset;
        new Thread(new Runnable() {
            public void run() {
                Cell cell = new Cell();
                createdSample.add(cell);
                cell.setLocation(200, 0);
                cell.startmove(getGraphics(), 0, end);
            }
        }).start();
    }
}
