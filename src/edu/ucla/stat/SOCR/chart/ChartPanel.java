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


package edu.ucla.stat.SOCR.chart;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import edu.ucla.stat.SOCR.core.SOCRChart;

public class ChartPanel extends JPanel// implements MouseListener
								//		 ,MouseMontionListener
    {
    /**
	 * @uml.property  name="leftMargin"
	 */
    private int leftMargin;
    /**
	 * @uml.property  name="rightMargin"
	 */
    private int rightMargin;
    /**
	 * @uml.property  name="topMargin"
	 */
    private int topMargin;
    /**
	 * @uml.property  name="bottomMargin"
	 */
    private int bottomMargin;
    /**
	 * @uml.property  name="xMin"
	 */
    private double xMin;
    /**
	 * @uml.property  name="xMax"
	 */
    private double xMax;
    /**
	 * @uml.property  name="yMin"
	 */
    private double yMin;
    /**
	 * @uml.property  name="yMax"
	 */
    private double yMax;

    /**
	 * @uml.property  name="analysis"
	 * @uml.associationEnd  
	 */
    private edu.ucla.stat.SOCR.chart.Chart chart;

    /**
	 * @uml.property  name="domain"
	 * @uml.associationEnd  
	 */
    //private Domain domain;


    private int type;
    /**
	 * @uml.property  name="xPosition"
	 */
    private int xPosition = 0 ;
    /**
	 * @uml.property  name="yPosition"
	 */
    private int yPosition = 0 ;
    /**
	 * @uml.property  name="left"
	 */
    private double left;
    /**
	 * @uml.property  name="right"
	 */
    private double right;
    /**
	 * @uml.property  name="leftx"
	 */
    private double leftx;
    /**
	 * @uml.property  name="rightx"
	 */
    private double rightx;

    /**
	 * @uml.property  name="container"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="graphPanel:edu.ucla.stat.SOCR.core.SOCRDistributions"
	 */
    SOCRChart container;


    /**
	 * @uml.property  name="font"
	 */
    private Font font = new Font("sansserif", Font.PLAIN, 11);


    /**
	 * @uml.property  name="decimalFormat"
	 */
    public DecimalFormat decimalFormat = new DecimalFormat();


    public ChartPanel(SOCRChart container) {
        this.container = container;
        //setScale(0, 1, 0, 1);
        //setMargins(30, 30, 30, 30);
        setBackground(Color.BLUE);
        setFont(font);
		//   addMouseMotionListener(this);
		//   addMouseListener(this);
    }

    /**
     *
     * @uml.property name="analysis"
     */

   public void setChart(Chart chart) {
        if (chart != null) {
            this.chart = chart;
            //domain = analysis.getDomain();
            //type = analysis.getType();
            //left = domain.getLowerBound();
            //right = domain.getUpperBound();

        }
        repaint();
    }

    /** This method sets the minimum and maximum values on the x and y axes */
}
