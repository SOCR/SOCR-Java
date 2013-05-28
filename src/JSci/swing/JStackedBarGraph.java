package JSci.swing;

import java.awt.*;
import JSci.awt.*;

/**
* A stacked bar graph Swing component.
* Multiple series are stacked.
* @version 1.2
* @author Lindsay Laird
*/
public class JStackedBarGraph extends JBarGraph {
        /**
        * Constructs a stacked bar graph.
        */
        public JStackedBarGraph(CategoryGraph2DModel gm) {
                super(gm);
        }
        /**
        * Implementation of GraphDataListener.
        */
        public void dataChanged(GraphDataEvent e) {
                float tmp;
                minY=0.0f;
                maxY=Float.NEGATIVE_INFINITY;
                model.firstSeries();
                for(int i=0;i<model.seriesLength();i++) {
                        tmp=model.getValue(i);
                        while(model.nextSeries())
                                tmp+=model.getValue(i);
                        minY=Math.min(tmp,minY);
                        maxY=Math.max(tmp,maxY);
                        model.firstSeries();
                }
                if(minY==maxY) {
                        minY-=0.5f;
                        maxY+=0.5f;
                }
                setNumbering(numbering);
                rescale();
        }
        /**
        * Draws the graph bars.
        */
        protected void drawBars(Graphics g) {
// bars
                int dy,totalDy;
                for(int i=0;i<model.seriesLength();i++) {
                        model.firstSeries();
                        dy = drawStackedBar(g, i, model.getValue(i), barColor[0], 0);
                        totalDy=dy;
                        for(int n=1;model.nextSeries();n++) {
                                dy = drawStackedBar(g, i, model.getValue(i), barColor[n], -totalDy);
                                totalDy+=dy;
                        }
                }
        }
        /**
        * Draws a bar.
        * @return the height of the bar.
        */
        private int drawStackedBar(Graphics g, int pos, float value, Color color, int yoffset) {
                Point p;
                if(value<0.0f)
                        p=dataToScreen(pos,0.0f);
                else
                        p=dataToScreen(pos,value);
                g.setColor(color);
                final int dy=Math.abs(p.y-origin.y);
                g.fillRect(p.x+barPad, p.y+yoffset, barWidth, dy);
                g.setColor(Color.black);
                g.drawRect(p.x+barPad, p.y+yoffset, barWidth, dy);
                return dy;
        }
}

