package JSci.awt;

import java.awt.*;

/**
* A scatter graph AWT component.
* @version 1.2
* @author Mark Hale
*/
public class ScatterGraph extends Graph2D {
        /**
        * Constructs a scatter graph.
        */
        public ScatterGraph(Graph2DModel gm) {
                super(gm);
        }
        /**
        * Paint the graph.
        */
        protected void offscreenPaint(Graphics g) {
                drawAxes(g);
// points
                Point p;
                model.firstSeries();
                g.setColor(seriesColor[0]);
                int i;
                for(i=0;i<model.seriesLength();i++) {
                        p=dataToScreen(model.getXCoord(i),model.getYCoord(i));
                        g.fillRect(p.x,p.y,1,1);
                }
                for(int n=1;model.nextSeries();n++) {
                        g.setColor(seriesColor[n]);
                        for(i=0;i<model.seriesLength();i++) {
                                p=dataToScreen(model.getXCoord(i),model.getYCoord(i));
                                g.fillRect(p.x,p.y,1,1);
                        }
                }
        }
}

