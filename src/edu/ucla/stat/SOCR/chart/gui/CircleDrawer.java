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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.jfree.ui.Drawable;

/**
 * An implementation of the {@link Drawable} interface, to illustrate the use of the 
 * {@link org.jfree.chart.annotations.XYDrawableAnnotation} class.  
 */
public class CircleDrawer implements Drawable {

    /**
	 * The outline paint.
	 * @uml.property  name="outlinePaint"
	 */
    private Paint outlinePaint;

    /**
	 * The outline stroke.
	 * @uml.property  name="outlineStroke"
	 */
    private Stroke outlineStroke;

    /**
	 * The fill paint.
	 * @uml.property  name="fillPaint"
	 */
    private Paint fillPaint;

    /**
     * Creates a new instance.
     * 
     * @param outlinePaint  the outline paint.
     * @param outlineStroke  the outline stroke.
     * @param fillPaint  the fill paint.
     */
    public CircleDrawer(Paint outlinePaint, 
                        Stroke outlineStroke, 
                        Paint fillPaint) {
        this.outlinePaint = outlinePaint;
        this.outlineStroke = outlineStroke;
        this.fillPaint = fillPaint;
    }

    /**
     * Draws the circle.
     * 
     * @param g2  the graphics device.
     * @param area  the area in which to draw.
     */
    public void draw(Graphics2D g2, Rectangle2D area) {
        Ellipse2D ellipse = new Ellipse2D.Double(area.getX(), area.getY(),
                                                 area.getWidth(), area.getHeight());
        if (this.fillPaint != null) {
            g2.setPaint(this.fillPaint);
            g2.fill(ellipse);
        }
        if (this.outlinePaint != null && this.outlineStroke != null) {
            g2.setPaint(this.outlinePaint);
            g2.setStroke(this.outlineStroke);
            g2.draw(ellipse);
        }

        g2.setPaint(Color.black);
        g2.setStroke(new BasicStroke(1.0f));
        Line2D line1 = new Line2D.Double(area.getCenterX(), area.getMinY(),
                                         area.getCenterX(), area.getMaxY());
        Line2D line2 = new Line2D.Double(area.getMinX(), area.getCenterY(),
                                         area.getMaxX(), area.getCenterY());
        g2.draw(line1);
        g2.draw(line2);
    }
}
