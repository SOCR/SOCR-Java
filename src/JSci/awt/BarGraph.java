package JSci.awt;

import java.awt.*;
import java.awt.geom.Point2D;

/**
* A bar graph AWT component.
* Multiple series are side-by-side.
* @version 1.0
* @author Ismael Orenstein
*/
public class BarGraph extends CategoryGraph2D {
        /**
        * Bar colors.
        */
        protected Color barColor[]={Color.blue,Color.green,Color.red,Color.yellow,Color.cyan,Color.lightGray,Color.magenta,Color.orange,Color.pink};
        /**
        * Min and max data points.
        */
        protected float minY,maxY;
        /**
        * Axis scaling.
        */
        private float xScale,yScale;
        protected int barWidth;
        /**
        * Padding.
        */
        protected final int barPad=0;
        /**
        * Axis numbering.
        */
        protected boolean numbering=true;
        /**
        * Constructs a bar graph.
        */
        public BarGraph(CategoryGraph2DModel cgm) {
                super(cgm);
                dataChanged(new GraphDataEvent(model));
        }
        /**
        * Implementation of GraphDataListener.
        */
        public void dataChanged(GraphDataEvent e) {
                float tmp;
                model.firstSeries();
                minY=0.0f;
                maxY=Float.NEGATIVE_INFINITY;
                for(int i=0;i<model.seriesLength();i++) {
                        tmp=model.getValue(i);
                        minY=Math.min(tmp,minY);
                        maxY=Math.max(tmp,maxY);
                }
                while(model.nextSeries()) {
                        for(int i=0;i<model.seriesLength();i++) {
                                tmp=model.getValue(i);
                                minY=Math.min(tmp,minY);
                                maxY=Math.max(tmp,maxY);
                        }
                }
                if(minY==maxY) {
                        minY-=0.5f;
                        maxY+=0.5f;
                }
                setNumbering(numbering);
                rescale();
        }
        /**
        * Sets the bar color of the nth series.
        * @param n the index of the series.
        * @param c the line color.
        */
        public final void setColor(int n,Color c) {
                barColor[n]=c;
        }
        /**
        * Turns axis numbering on/off.
        */
        public final void setNumbering(boolean flag) {
                numbering=flag;
                leftAxisPad=axisPad;
                if(numbering) {
                        final int yNumPad=8*Math.max(String.valueOf(maxY).length(),String.valueOf(minY).length());
                        leftAxisPad+=yNumPad;
                }
        }
        /**
        * Draws the graph axes.
        */
        protected final void drawAxes(Graphics g) {
// axis
                g.setColor(getForeground());
                if(minY>0.0f)
                        g.drawLine(leftAxisPad-scalePad,getSize().height-axisPad,getSize().width-(axisPad-scalePad),getSize().height-axisPad);
                else
                        g.drawLine(leftAxisPad-scalePad,origin.y,getSize().width-(axisPad-scalePad),origin.y);
                g.drawLine(origin.x,axisPad-scalePad,origin.x,getSize().height-(axisPad-scalePad));
// x-axis labels
                int strWidth;
                final FontMetrics metrics=g.getFontMetrics();
                final int strHeight=metrics.getHeight();
                for(int x=0;x<model.seriesLength();x++) {
                        strWidth=metrics.stringWidth(model.getCategory(x));
                        g.drawString(model.getCategory(x),dataToScreen(x+0.5f-0.5f*strWidth/xScale,0.0f).x,origin.y+strHeight);
                }
// numbering
                if(numbering) {
                        String str;
                        Point p;
// y-axis numbering
                        final float dy=Graph2D.round(40.0f/yScale);
                        float y;
                        for(y=dy;y<=maxY;y+=dy) {
                                str=String.valueOf(Graph2D.round(y));
                                strWidth=metrics.stringWidth(str);
                                p=dataToScreen(0.0f,y);
                                g.drawLine(p.x,p.y,p.x-5,p.y);
                                g.drawString(str,p.x-8-strWidth,p.y+strHeight/4);
                        }
                        for(y=-dy;y>=minY;y-=dy) {
                                str=String.valueOf(Graph2D.round(y));
                                strWidth=metrics.stringWidth(str);
                                p=dataToScreen(0.0f,y);
                                g.drawLine(p.x,p.y,p.x-5,p.y);
                                g.drawString(str,p.x-8-strWidth,p.y+strHeight/4);
                        }
                }
        }
        /**
        * Draws the graph bars.
        */
        protected void drawBars(Graphics g) {
// bars
                int numSeries=1;
                model.firstSeries();
                while(model.nextSeries())
                        numSeries++;
                if(numSeries==1) {
                        for(int i=0;i<model.seriesLength();i++)
                                drawBar(g, i, model.getValue(i), barColor[0], barWidth, 0);
                } else {
                        final int subBarWidth = barWidth/numSeries;
                        for(int i=0;i<model.seriesLength();i++) {
                                // draw
                                model.firstSeries();
                                for(int j=0;j<numSeries;j++) {
                                        drawBar(g, i, model.getValue(i), barColor[j], subBarWidth, j*subBarWidth);
                                        model.nextSeries();
                                }
                        }
                }
        }
        /**
        * Draws a bar.
        */
        private void drawBar(Graphics g, int pos, float value, Color color, int width, int xoffset) {
                Point p;
                if(value<0.0f)
                        p=dataToScreen(pos,0.0f);
                else
                        p=dataToScreen(pos,value);
                g.setColor(color);
                final int dy=Math.abs(p.y-origin.y);
                g.fillRect(p.x+barPad+xoffset, p.y, width, dy);
                g.setColor(Color.black);
                g.drawRect(p.x+barPad+xoffset, p.y, width, dy);
        }
        /**
        * Paint the graph.
        */
        protected final void offscreenPaint(Graphics g) {
                drawAxes(g);
                drawBars(g);
        }
        /**
        * Reshapes the BarGraph to the specified bounding box.
        */
        public final void setBounds(int x,int y,int width,int height) {
                super.setBounds(x,y,width,height);
                rescale();
        }
        /**
        * Rescales the BarGraph.
        */
        protected final void rescale() {
                final Dimension s=getMinimumSize();
                final int thisWidth=Math.max(getSize().width,s.width);
                final int thisHeight=Math.max(getSize().height,s.height);
                xScale=(thisWidth-(leftAxisPad+axisPad))/model.seriesLength();
                yScale=(thisHeight-2*axisPad)/(maxY-minY);
                barWidth=Math.round(xScale-2*barPad);
                origin.x=leftAxisPad;
                origin.y=thisHeight-axisPad+Math.round(minY*yScale);
                redraw();
        }
        /**
        * Converts a data point to screen coordinates.
        */
        protected final Point dataToScreen(float x,float y) {
                return new Point(origin.x+Math.round(xScale*x),origin.y-Math.round(yScale*y));
        }
        /**
        * Converts a screen point to data coordinates.
        */
        protected final Point2D.Float screenToData(Point p) {
                return new Point2D.Float((p.x-origin.x)/xScale,(origin.y-p.y)/yScale);
        }
}

