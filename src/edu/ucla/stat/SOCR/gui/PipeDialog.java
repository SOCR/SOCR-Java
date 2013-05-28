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


/**
 * A subclass of JDialog displaying in the middle of the main frame
 *
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 **/

public class PipeDialog extends JDialog{
    private Window _f;
    /**
     * Constructs a dialog initialized with an owner, a  title and its modality.
     */
    public PipeDialog(Frame f, String title, boolean modal) {
        super(f,"LONI SOCR:" + title,modal);
        _f = f;
    }

    public PipeDialog(Dialog f, String title, boolean modal) {
        super(f,"LONI SOCR:" + title,modal);
        _f = f;
    }
    
    public void setVisible(boolean _show) {
        if ( _f != null){
            int x=_f.getLocationOnScreen().x+_f.getSize().width/2- getSize().width/2;
            int y=_f.getLocationOnScreen().y+_f.getSize().height/2-getSize().height/2;
            setLocation(x,y);
        }

        //super.show();
        super.setVisible(_show);
    }
}
