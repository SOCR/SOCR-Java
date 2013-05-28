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
package edu.ucla.stat.SOCR.util;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class JScrollPaneAdjuster
    implements PropertyChangeListener, Serializable
{
    private JScrollPane pane;

    private transient Adjuster x, y;


    public JScrollPaneAdjuster(JScrollPane pane)
    {
        this.pane = pane;

        this.x = new Adjuster(pane.getViewport(), pane.getColumnHeader(), Adjuster.X);
        this.y = new Adjuster(pane.getViewport(), pane.getRowHeader(), Adjuster.Y);

        pane.addPropertyChangeListener(this);
    }


    public void dispose()
    {
        x.dispose();
        y.dispose();

        pane.removePropertyChangeListener(this);
        pane = null;
    }


    public void propertyChange(PropertyChangeEvent e)
    {
        String name = e.getPropertyName();

        if (name.equals("viewport"))
        {
            x.setViewport((JViewport)e.getNewValue());
            y.setViewport((JViewport)e.getNewValue());
        }
        else if (name.equals("rowHeader"))
        {
            y.setHeader((JViewport)e.getNewValue());
        }
        else if (name.equals("columnHeader"))
        {
            x.setHeader((JViewport)e.getNewValue());
        }
    }


    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        x = new Adjuster(pane.getViewport(), pane.getColumnHeader(), Adjuster.X);
        y = new Adjuster(pane.getViewport(), pane.getRowHeader(), Adjuster.Y);
    }


    private static class Adjuster
        implements ChangeListener, Runnable
    {
        public static final int X = 1, Y = 2;

        private JViewport viewport, header;
        private int type;


        public Adjuster(JViewport viewport, JViewport header, int type)
        {
            this.viewport = viewport;
            this.header = header;
            this.type = type;

            if (header != null)
                header.addChangeListener(this);
        }

        public void setViewport(JViewport newViewport)
        {
            viewport = newViewport;
        }

        public void setHeader(JViewport newHeader)
        {
            if (header != null)
                header.removeChangeListener(this);

            header = newHeader;
            
            if (header != null)
                header.addChangeListener(this);
        }

        public void stateChanged(ChangeEvent e)
        {
            if (viewport == null || header == null)
                return;

            if (type == X)
            {
                if (viewport.getViewPosition().x != header.getViewPosition().x)
                    SwingUtilities.invokeLater(this);
            }
            else
            {
                if (viewport.getViewPosition().y != header.getViewPosition().y)
                    SwingUtilities.invokeLater(this);
            }
        }
        
        public void run()
        {
            if (viewport == null || header == null)
                return;


            Point v = viewport.getViewPosition(),
                h = header.getViewPosition();

            if (type == X)
            {
                if (v.x != h.x)
                    viewport.setViewPosition(new Point(h.x, v.y));
            }
            else
            {
                if (v.y != h.y)
                    viewport.setViewPosition(new Point(v.x, h.y));
            }
        }

        public void dispose()
        {
            if (header != null)
                header.removeChangeListener(this);

            viewport = header = null;
        }
    }
}
