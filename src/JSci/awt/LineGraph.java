package JSci.awt;

import java.awt.*;

/**
* A line graph AWT component.
* @version 1.2
* @author Mark Hale
*/
public class LineGraph extends Graph2D {
        /**
        * Constructs a line graph.
        */
        public LineGraph(Graph2DModel gm) {
                super(gm);
        }
        /**
        * Paint the graph.
        */
        protected void offscreenPaint(Graphics g) {
                drawAxes(g);
// lines
                Point p1,p2;
                model.firstSeries();
                g.setColor(seriesColor[0]);
                p1=dataToScreen(model.getXCoord(0),model.getYCoord(0));
                int i;
                for(i=1;i<model.seriesLength();i++) {
                        p2=dataToScreen(model.getXCoord(i),model.getYCoord(i));
                        g.drawLine(p1.x,p1.y,p2.x,p2.y);
                        p1=p2;
                }
                for(int n=1;model.nextSeries();n++) {
                        g.setColor(seriesColor[n]);
                        p1=dataToScreen(model.getXCoord(0),model.getYCoord(0));
                        for(i=1;i<model.seriesLength();i++) {
                                p2=dataToScreen(model.getXCoord(i),model.getYCoord(i));
                                g.drawLine(p1.x,p1.y,p2.x,p2.y);
                                p1=p2;
                        }
                }
        }
}

