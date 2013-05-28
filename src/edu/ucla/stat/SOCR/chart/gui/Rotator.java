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
package edu.ucla.stat.SOCR.chart.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;

/**
 * The rotator.
 */
public class Rotator extends Timer implements ActionListener {

    /**
	 * The plot.
	 * @uml.property  name="plot"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private PiePlot plot;

    /**
	 * The angle.
	 * @uml.property  name="angle"
	 */
    private int angle = 270;

    /**
     * Constructor.
     *
     * @param plot  the plot.
     */
    public Rotator(PiePlot3D plot) {
        super(100, null);
        this.plot = plot;
        addActionListener(this);
    }
    
    public Rotator(PiePlot plot) {
        super(100, null);
        this.plot = plot;
        addActionListener(this);
    }

    /**
     * Modifies the starting angle.
     *
     * @param event  the action event.
     */
    public void actionPerformed(ActionEvent event) {
        this.plot.setStartAngle(this.angle);
        this.angle = this.angle + 1;
        if (this.angle == 360) {
            this.angle = 0;
        }
    }

}
