package JSci.awt;

import java.awt.*;
import java.awt.geom.Point2D;

/**
* The Graph2D superclass provides an abstract encapsulation of 2D graphs.
* @version 1.3
* @author Mark Hale
*/
public abstract class Graph2D extends DoubleBufferedCanvas implements GraphDataListener {
        public final static int LINEAR_SCALE = 0;
        public final static int LOG_SCALE = 1;
        /**
        * Data model.
        */
        protected Graph2DModel model;
        /**
        * Axis numbering.
        */
        protected boolean numbering=true;
        /**
        * Origin.
        */
        protected Point origin=new Point();
        /**
        * Series colors.
        */
        protected Color seriesColor[]={Color.black,Color.blue,Color.green,Color.red,Color.yellow,Color.cyan,Color.lightGray,Color.magenta,Color.orange,Color.pink};
        /**
        * Axis scaling.
        */
        private float xScale,yScale;
        /**
        * Axis scaling type.
        */
        private int xScaleType, yScaleType;
        private float scaledMinX, scaledMinY, scaledMaxX, scaledMaxY;
        private boolean autoYExtrema=true;
        /**
        * Axis numbering increment.
        */
        private float xInc,yInc;
        private boolean autoXInc=true,autoYInc=true;
        /**
        * Padding.
        */
        protected final int scalePad=5;
        protected final int axisPad=25;
        protected int leftAxisPad;
        /**
        * Constructs a 2D graph.
        */
        public Graph2D(Graph2DModel gm) {
                model=gm;
                model.addGraphDataListener(this);
                dataChanged(new GraphDataEvent(model));
        }
        /**
        * Sets the data plotted by this graph to the specified data.
        */
        public final void setModel(Graph2DModel gm) {
                model.removeGraphDataListener(this);
                model=gm;
                model.addGraphDataListener(this);
                dataChanged(new GraphDataEvent(model));
        }
        /**
        * Returns the model used by this graph.
        */
        public final Graph2DModel getModel() {
                return model;
        }
        /**
        * Implementation of GraphDataListener.
        */
        public void dataChanged(GraphDataEvent e) {
                // determine minX and maxX from model
                float minX=Float.POSITIVE_INFINITY;
                float maxX=Float.NEGATIVE_INFINITY;
                float tmpX;
                model.firstSeries();
                for(int i=0;i<model.seriesLength();i++) {
                        tmpX=model.getXCoord(i);
                        minX=Math.min(tmpX,minX);
                        maxX=Math.max(tmpX,maxX);
                }
                int n;
                for(n=1;model.nextSeries();n++) {
                        for(int i=0;i<model.seriesLength();i++) {
                                tmpX=model.getXCoord(i);
                                minX=Math.min(tmpX,minX);
                                maxX=Math.max(tmpX,maxX);
                        }
                }

                if(minX==Float.POSITIVE_INFINITY || maxX==Float.NEGATIVE_INFINITY) {
                        // default values if no data
                        scaledMinX = -5.0f;
                        scaledMaxX = 5.0f;
                } else {
                        if(xScaleType == LOG_SCALE) {
                                scaledMinX = (float) Math.log(minX);
                                scaledMaxX = (float) Math.log(maxX);
                        } else {
                                scaledMinX = minX;
                                scaledMaxX = maxX;
                        }
                }

                // ensure there are enough colors
                if(n>seriesColor.length) {
                        Color tmp[]=seriesColor;
                        seriesColor=new Color[n];
                        System.arraycopy(tmp,0,seriesColor,0,tmp.length);
                        for(int i=tmp.length;i<n;i++)
                                seriesColor[i]=seriesColor[i-tmp.length];
                }
                if(autoYExtrema)
                        setYExtrema(0.0f,0.0f);
                setNumbering(numbering);
        }
        /**
        * Turns axis numbering on/off.
        * Default is on.
        */
        public final void setNumbering(boolean flag) {
                numbering=flag;
                leftAxisPad=axisPad;
                if(numbering) {
                        int minYNumLen, maxYNumLen;
                        if(yScaleType == LOG_SCALE) {
                                maxYNumLen = String.valueOf(round((float)Math.exp(scaledMaxY))).length();
                                minYNumLen = String.valueOf(round((float)Math.exp(scaledMinY))).length();
                        } else {
                                maxYNumLen = String.valueOf(round(scaledMaxY)).length();
                                minYNumLen = String.valueOf(round(scaledMinY)).length();
                        }
                        int yNumPad=8*Math.max(minYNumLen, maxYNumLen);
                        if(scaledMinX<0.0f) {
                                final int negXLen=(int)((Math.max(getSize().width,getMinimumSize().width)-2*(axisPad+scalePad))*scaledMinX/(scaledMinX-scaledMaxX));
                                yNumPad=Math.max(yNumPad-negXLen,0);
                        }
                        leftAxisPad+=yNumPad;
                }
                rescale();
        }
        /**
        * Sets the x-axis scale type.
        * @param t a _SCALE constant.
        */
        public final void setXScale(int t) {
                xScaleType = t;
                dataChanged(new GraphDataEvent(model));
        }
        /**
        * Sets the y-axis scale type.
        * @param t a _SCALE constant.
        */
        public final void setYScale(int t) {
                yScaleType = t;
                dataChanged(new GraphDataEvent(model));
        }
        /**
        * Sets the x-axis numbering increment.
        * @param dx use 0.0f for auto-adjusting (default).
        */
        public final void setXIncrement(float dx) {
                if(dx<0.0f)
                        throw new IllegalArgumentException("Increment should be positive.");
                else if(dx==0.0f)
                        autoXInc=true;
                else
                        autoXInc=false;
                xInc=dx;
        }
        /**
        * Sets the y-axis numbering increment.
        * @param dy use 0.0f for auto-adjusting (default).
        */
        public final void setYIncrement(float dy) {
                if(dy<0.0f)
                        throw new IllegalArgumentException("Increment should be positive.");
                else if(dy==0.0f)
                        autoYInc=true;
                else
                        autoYInc=false;
                yInc=dy;
        }
        /**
        * Sets the minimum/maximum values on the y-axis.
        * Set both min and max to 0.0f for auto-adjusting (default).
        */
        public final void setYExtrema(float min, float max) {
                if(min==0.0f && max==0.0f) {
                        autoYExtrema=true;
                        // determine min and max from model
                        min=Float.POSITIVE_INFINITY;
                        max=Float.NEGATIVE_INFINITY;
                        float tmp;
                        model.firstSeries();
                        for(int i=0;i<model.seriesLength();i++) {
                                tmp=model.getYCoord(i);
                                min=Math.min(tmp,min);
                                max=Math.max(tmp,max);
                        }
                        while(model.nextSeries()) {
                                for(int i=0;i<model.seriesLength();i++) {
                                        tmp=model.getYCoord(i);
                                        min=Math.min(tmp,min);
                                        max=Math.max(tmp,max);
                                }
                        }
                        if(min==max) {
                                if(yScaleType == LOG_SCALE) {
                                        min/=10.0f;
                                        max*=10.0f;
                                } else {
                                        min-=0.5f;
                                        max+=0.5f;
                                }
                        }
                        if(min==Float.POSITIVE_INFINITY || max==Float.NEGATIVE_INFINITY) {
                                if(yScaleType == LOG_SCALE) {
                                        min = 1.0f;
                                        max = 100.0f;
                                } else {
                                        min=-5.0f;
                                        max=5.0f;
                                }
                        }
                } else if(max<=min) {
                        throw new IllegalArgumentException("Maximum should be greater than minimum; max = "+max+" and min = "+min);
                } else {
                        autoYExtrema=false;
                }
                if(yScaleType == LOG_SCALE) {
                        scaledMinY = (float) Math.log(min);
                        scaledMaxY = (float) Math.log(max);
                } else {
                        scaledMinY = min;
                        scaledMaxY = max;
                }
                rescale();
        }
        /**
        * Sets the color of the nth y-series.
        * @param n the index of the y-series.
        * @param c the line color.
        */
        public final void setColor(int n,Color c) {
                seriesColor[n]=c;
        }
        /**
        * Reshapes the Graph2D to the specified bounding box.
        */
        public final void setBounds(int x,int y,int width,int height) {
                super.setBounds(x,y,width,height);
                rescale();
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
        * Rescales the Graph2D.
        */
        protected final void rescale() {
                final Dimension s=getMinimumSize();
                final int thisWidth=Math.max(getSize().width,s.width);
                final int thisHeight=Math.max(getSize().height,s.height);
                xScale=(thisWidth-(leftAxisPad+axisPad))/(scaledMaxX-scaledMinX);
                yScale=(thisHeight-2*axisPad)/(scaledMaxY-scaledMinY);
                if(autoXInc)
                        xInc=round(40.0f/xScale);
                if(autoYInc)
                        yInc=round(40.0f/yScale);
                origin.x=leftAxisPad-Math.round(scaledMinX*xScale);
                origin.y=thisHeight-axisPad+Math.round(scaledMinY*yScale);
                redraw();
        }
        /**
        * Converts a data point to screen coordinates.
        */
        protected final Point dataToScreen(float x,float y) {
                if(xScaleType == LOG_SCALE)
                        x = (float) Math.log(x);
                if(yScaleType == LOG_SCALE)
                        y = (float) Math.log(y);
                return scaledDataToScreen(x, y);
        }
        /**
        * Converts a scaled data point to screen coordinates.
        */
        protected final Point scaledDataToScreen(float x,float y) {
                return new Point(origin.x+Math.round(xScale*x), origin.y-Math.round(yScale*y));
        }
        /**
        * Converts a screen point to data coordinates.
        */
        protected final Point2D.Float screenToData(Point p) {
                float x = (p.x-origin.x)/xScale;
                float y = (origin.y-p.y)/yScale;
                if(xScaleType == LOG_SCALE)
                        x = (float) Math.exp(x);
                if(yScaleType == LOG_SCALE)
                        y = (float) Math.exp(y);
                return new Point2D.Float(x, y);
        }
        /**
        * Draws the graph axes.
        */
        protected final void drawAxes(Graphics g) {
// axis
                g.setColor(getForeground());
                if(scaledMinY>0.0f)
                        g.drawLine(leftAxisPad-scalePad, getSize().height-axisPad, getSize().width-(axisPad-scalePad), getSize().height-axisPad);
                else
                        g.drawLine(leftAxisPad-scalePad, origin.y, getSize().width-(axisPad-scalePad), origin.y);
                if(scaledMinX>0.0f)
                        g.drawLine(leftAxisPad, axisPad-scalePad, leftAxisPad, getSize().height-(axisPad-scalePad));
                else
                        g.drawLine(origin.x, axisPad-scalePad, origin.x, getSize().height-(axisPad-scalePad));
// numbering
                if(numbering) {
                        String str;
                        int strWidth;
                        final FontMetrics metrics=g.getFontMetrics();
                        final int strHeight=metrics.getHeight();
                        Point p;
// x-axis numbering
                        float x;
                        for(x=(scaledMinX>0.0f)?scaledMinX:xInc; x<=scaledMaxX; x+=xInc) {
                                if(xScaleType == LOG_SCALE)
                                        str=String.valueOf(round((float)Math.exp(x)));
                                else
                                        str=String.valueOf(round(x));
// add a + prefix to compensate for - prefix in negative number strings when calculating length
                                strWidth=metrics.stringWidth('+'+str);
                                p=scaledDataToScreen(x,(scaledMinY>0.0f)?scaledMinY:0.0f);
                                g.drawLine(p.x,p.y,p.x,p.y+5);
                                g.drawString(str,p.x-strWidth/2,p.y+5+strHeight);
                        }
                        for(x=-xInc; x>=scaledMinX; x-=xInc) {
                                if(xScaleType == LOG_SCALE)
                                        str=String.valueOf(round((float)Math.exp(x)));
                                else
                                        str=String.valueOf(round(x));
                                strWidth=metrics.stringWidth(str);
                                p=scaledDataToScreen(x,(scaledMinY>0.0f)?scaledMinY:0.0f);
                                g.drawLine(p.x,p.y,p.x,p.y+5);
                                g.drawString(str,p.x-strWidth/2,p.y+5+strHeight);
                        }
// y-axis numbering
                        float y;
                        for(y=(scaledMinY>0.0f)?scaledMinY:yInc; y<=scaledMaxY; y+=yInc) {
                                if(yScaleType == LOG_SCALE)
                                        str=String.valueOf(round((float)Math.exp(y)));
                                else
                                        str=String.valueOf(round(y));
                                strWidth=metrics.stringWidth(str);
                                p=scaledDataToScreen((scaledMinX>0.0f)?scaledMinX:0.0f,y);
                                g.drawLine(p.x,p.y,p.x-5,p.y);
                                g.drawString(str,p.x-8-strWidth,p.y+strHeight/4);
                        }
                        for(y=-yInc; y>=scaledMinY; y-=yInc) {
                                if(yScaleType == LOG_SCALE)
                                        str=String.valueOf(round((float)Math.exp(y)));
                                else
                                        str=String.valueOf(round(y));
                                strWidth=metrics.stringWidth(str);
                                p=scaledDataToScreen((scaledMinX>0.0f)?scaledMinX:0.0f,y);
                                g.drawLine(p.x,p.y,p.x-5,p.y);
                                g.drawString(str,p.x-8-strWidth,p.y+strHeight/4);
                        }
                }
        }
        /**
        * Rounds numbers to so many significant figures.
        */
        protected final static float round(float x) {
                final int SIG_FIG=2;
                int sign=1;
                if(x<0.0f)
                        sign=-1;
                final float mag=Math.abs(x);
                int places;
                float tmp,factor;
                if(mag<1.0f) {
                        tmp=10.0f*mag;
                        for(places=1;tmp<1.0f;places++)
                                tmp*=10.0f;
                        factor=(float)Math.pow(10.0f,places+SIG_FIG-1);
                } else {
                        tmp=mag/10.0f;
                        for(places=1;tmp>1.0f;places++)
                                tmp/=10.0f;
                        factor=(float)Math.pow(10.0f,SIG_FIG-places);
                }
                return (sign*Math.round(mag*factor))/factor;
        }
}

