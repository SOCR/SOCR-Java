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

package edu.ucla.stat.SOCR.core;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

/**
 * this class used to let the user add new components to SOCR Applets
 * <p>
 * 
 * When value changed, it will notify any registered Observers.
 * The Observers can get the value from getValue().
 *
 * @author <A HREF="mailto:dinov@stat.ucla.edu">Ivo Dinov </A>
 */

public class ComponentSetter extends JPanel
{
    private int type;

    private Observable observable = new Observable()
    {
        public void notifyObservers()
        {
            setChanged();
            super.notifyObservers(ComponentSetter.this);
        }
    };

    /**
     * create a new ComponentSetter object
     *
     * @param title
     * @param component
     */
    public ComponentSetter(String title, javax.swing.JComponent component)
    {
        constructing(title, component);
    }

    private void constructing(String title, javax.swing.JComponent component)
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(component);
        add(Box.createVerticalStrut(8));
 
        //create border
        TitledBorder tb = new TitledBorder(new EtchedBorder(), title);
        setBorder(tb);
    }

    public void addObserver(Observer o)
    {
        observable.addObserver(o);
    }

    public void update()
    {
         observable.notifyObservers();
    }

    public void setTitle(String title)
    {
        ((TitledBorder)getBorder()).setTitle(title);
    }

}