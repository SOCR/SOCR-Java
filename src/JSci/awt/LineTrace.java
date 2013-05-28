package JSci.awt;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

/**
* A line trace AWT component.
* @version 1.2
* @author Mark Hale
*/
public final class LineTrace extends DoubleBufferedCanvas {
        /**
        * Data points.
        */
        private float xData[],yData[];
        /**
        * Data cursor.
        */
        private int dataCursor;
        /**
        * Data resolution.
        */
        private float dataRes;
        /**
        * Axis numbering.
        */
        private boolean numbering=true;
        /**
        * Origin.
        */
        private Point origin=new Point();
        /**
        * Min and max data points.
        */
        private float minX,minY,maxX,maxY;
        /**
        * Axis scaling.
        */
        private float xScale,yScale;
        /**
        * Padding.
        */
        private final int scalePad=5;
        private final int axisPad=25;
        private int leftAxisPad;
        /**
        * Constructs a line trace.
        */
        public LineTrace(float minx,float maxx,float miny,float maxy) {
                addMouseMotionListener(new MouseLineAdapter());
                setXExtrema(minx,maxx);
                setYExtrema(miny,maxy);
                setSampleNumber(50);
        }
        /**
        * Gets the data sampled by this LineTrace.
        */
        public Graph2DModel getModel() {
                final DefaultGraph2DModel model=new DefaultGraph2DModel();
                model.setXAxis(xData);
                model.addSeries(yData);
                return model;
        }
        /**
        * Turns axis numbering on/off.
        */
        public final void setNumbering(boolean flag) {
                numbering=flag;
                leftAxisPad=axisPad;
                if(numbering) {
                        int yNumPad=8*Math.max(String.valueOf(maxY).length(),String.valueOf(minY).length());
                        if(minX<0.0f) {
                                final int negXLen=(int)((Math.max(getSize().width,getMinimumSize().width)-2*(axisPad+scalePad))*minX/(minX-maxX));
                                yNumPad=Math.max(yNumPad-negXLen,0);
                        }
                        leftAxisPad+=yNumPad;
                }
        }
        /**
        * Sets the minimum/maximum values on the x-axis.
        */
        public void setXExtrema(float min,float max) {
                if(max<min)
                        throw new IllegalArgumentException("Maximum should be greater than minimum; max = "+max+" and min = "+min);
                minX=min;
                maxX=max;
                rescale();
        }
        /**
        * Sets the minimum/maximum values on the y-axis.
        */
        public void setYExtrema(float min,float max) {
                if(max<min)
                        throw new IllegalArgumentException("Maximum should be greater than minimum; max = "+max+" and min = "+min);
                minY=min;
                maxY=max;
                rescale();
        }
        /**
        * Sets the number of samples.
        */
        public void setSampleNumber(int n) {
                xData=new float[n];
                yData=new float[n];
                dataRes=(maxX-minX)/n;
        }
        /**
        * Clear the trace.
        */
        public void clear() {
                dataCursor=0;
                redraw();
        }
        /**
        * Reshapes the LineTrace to the specified bounding box.
        */
        public final void setBounds(int x,int y,int width,int height) {
                super.setBounds(x,y,width,height);
                rescale();
        }
        /**
        * Rescales the LineTrace.
        */
        private void rescale() {
                final Dimension s=getMinimumSize();
                final int thisWidth=Math.max(getSize().width,s.width);
                final int thisHeight=Math.max(getSize().height,s.height);
                xScale=(thisWidth-(leftAxisPad+axisPad))/(maxX-minX);
                yScale=(thisHeight-2*axisPad)/(maxY-minY);
                origin.x=leftAxisPad-Math.round(minX*xScale);
                origin.y=thisHeight-axisPad+Math.round(minY*yScale);
                redraw();
        }
        /**
        * Returns the preferred size of this component.
        */
        public Dimension getPreferredSize() {
                return getMinimumSize();
        }
        /**
        * Returns the minimum size of this component.
        */
        public Dimension getMinimumSize() {
                return new Dimension(200,200);
        }
        /**
        * Converts a screen point to data coordinates.
        */
        private Point2D.Float screenToData(Point p) {
                return new Point2D.Float((p.x-origin.x)/xScale,(origin.y-p.y)/yScale);
        }
        /**
        * Converts a data point to screen coordinates.
        */
        private Point dataToScreen(float x,float y) {
                return new Point(origin.x+Math.round(xScale*x),origin.y-Math.round(yScale*y));
        }
        /**
        * Paint the trace.
        */
        protected void offscreenPaint(Graphics g) {
                drawAxes(g);
// lines
                Point p1,p2;
                p1=dataToScreen(xData[0],yData[0]);
                for(int i=1;i<dataCursor;i++) {
                        p2=dataToScreen(xData[i],yData[i]);
                        g.drawLine(p1.x,p1.y,p2.x,p2.y);
                        p1=p2;
                }
        }
        /**
        * Draws the graph axes.
        */
        private void drawAxes(Graphics g) {
// axis
                g.setColor(getForeground());
                if(minY>0.0f)
                        g.drawLine(leftAxisPad-scalePad,getSize().height-axisPad,getSize().width-(axisPad-scalePad),getSize().height-axisPad);
                else
                        g.drawLine(leftAxisPad-scalePad,origin.y,getSize().width-(axisPad-scalePad),origin.y);
                if(minX>0.0f)
                        g.drawLine(leftAxisPad,axisPad-scalePad,leftAxisPad,getSize().height-(axisPad-scalePad));
                else
                        g.drawLine(origin.x,axisPad-scalePad,origin.x,getSize().height-(axisPad-scalePad));
// numbering
                if(numbering) {
                        String str;
                        int strWidth;
                        final FontMetrics metrics=g.getFontMetrics();
                        final int strHeight=metrics.getHeight();
                        Point p;
// x-axis numbering
                        float x;
                        final float dx=Graph2D.round(40.0f/xScale);
                        for(x=(minX>0.0f)?minX:dx;x<=maxX;x+=dx) {
                                str=String.valueOf(Graph2D.round(x));
// add a + prefix to compensate for - prefix in negative number strings when calculating length
                                strWidth=metrics.stringWidth('+'+str);
                                p=dataToScreen(x,(minY>0.0f)?minY:0.0f);
                                g.drawLine(p.x,p.y,p.x,p.y+5);
                                g.drawString(str,p.x-strWidth/2,p.y+5+strHeight);
                        }
                        for(x=-dx;x>=minX;x-=dx) {
                                str=String.valueOf(Graph2D.round(x));
                                strWidth=metrics.stringWidth(str);
                                p=dataToScreen(x,(minY>0.0f)?minY:0.0f);
                                g.drawLine(p.x,p.y,p.x,p.y+5);
                                g.drawString(str,p.x-strWidth/2,p.y+5+strHeight);
                        }
// y-axis numbering
                        float y;
                        final float dy=Graph2D.round(40.0f/yScale);
                        for(y=(minY>0.0f)?minY:dy;y<=maxY;y+=dy) {
                                str=String.valueOf(Graph2D.round(y));
                                strWidth=metrics.stringWidth(str);
                                p=dataToScreen((minX>0.0f)?minX:0.0f,y);
                                g.drawLine(p.x,p.y,p.x-5,p.y);
                                g.drawString(str,p.x-8-strWidth,p.y+strHeight/4);
                        }
                        for(y=-dy;y>=minY;y-=dy) {
                                str=String.valueOf(Graph2D.round(y));
                                strWidth=metrics.stringWidth(str);
                                p=dataToScreen((minX>0.0f)?minX:0.0f,y);
                                g.drawLine(p.x,p.y,p.x-5,p.y);
                                g.drawString(str,p.x-8-strWidth,p.y+strHeight/4);
                        }
                }
        }
        class MouseLineAdapter extends MouseMotionAdapter {
                public void mouseDragged(MouseEvent evt) {
                        Point2D.Float p=screenToData(evt.getPoint());
                        if(p.x>=dataCursor*dataRes+minX && dataCursor<xData.length) {
                                xData[dataCursor]=p.x;
                                yData[dataCursor]=p.y;
                                dataCursor++;
                        }
                        redraw();
                }
        }
}

