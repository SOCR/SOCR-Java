package JSci.awt;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

/**
 * An extension of LineTrase.java that traces a line and draws the model fit.
 * @version 1.0
 * @author Ivo Dinov
 */
public class ModelerCanvas extends DoubleBufferedCanvas {
    /**
     * Data points.
     */
    
    public class XYPair {
        float xSorted[],ySorted[];
        XYPair(int n) {
            xSorted = new float[n];
            ySorted = new float[n];
        }
        public void sortXY(float[] x,float[] y,int n) {
            int g,r;
            float c;
            for ( r=0; r <n-1; r++) {
                for ( g=r+1;g<n;g++) {
                    if ( x[r]  < x[g] ) {
                        c=x[r];                // these 3 statements swap values
                        x[r] = x[g];          // in the 2 cells being compared
                        x[g] = c;
                        c = y[r];
                        y[r] = y[g];
                        y[g] = c;
                        
                    }
                }
            }
            
            xSorted = x;
            ySorted = y;
            
        }
        
    }
    public XYPair srt;
    private float xData[],yData[];
    
    /**
     * Model points.
     */
    public float modelX[], modelY[];
    
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
    
    private int numberSamplePoints = 50;
    
    Graphics gGlobal;
    
    /**
     * Constructs a line trace.
     */
    public ModelerCanvas(float minx,float maxx,float miny,float maxy) {
        this.setBackground(Color.WHITE);
        addMouseMotionListener(new MouseLineAdapter());
        addMouseListener(new MousePointAdapter());
        setXExtrema(minx,maxx);
        setYExtrema(miny,maxy);
        setSampleNumber(numberSamplePoints);
    }
    /**
     * Gets the data sampled by this LineTrace.
     */
    public DefaultGraph2DModel getModel() {
        final DefaultGraph2DModel model=new DefaultGraph2DModel();
        if(dataCursor == 0) {
            return null;
        }else{
           
            float xtemp[] = new float[dataCursor];
            float ytemp[] = new float[dataCursor];
            System.arraycopy(xData, 0, xtemp, 0, dataCursor);
            System.arraycopy(yData, 0, ytemp, 0, dataCursor);
            model.setXAxis(xtemp);
            model.addSeries(ytemp);
            return model;}
    }
    
    public float getXData(int i) {
    return xData[i];
    }
    
    public float getYData(int i) {
    return yData[i];
    }
    
    public int getdataCursor() {
    return dataCursor;
    }
    
    /**
     * Sets the model fit to the data.
     */
    public void setModel(int size, double[] _modelX, double[] _modelY) {
        modelX = new float[size];
        modelY = new float[size];
        
        for (int i = 0; i < _modelX.length; i++)
        {   	modelX[i] = (float)(_modelX[i]);
                modelY[i] = (float)(_modelY[i]);
        }
        //modelPaint();
        redraw();
        return;
    }
    public float[] getModelX() {
    return modelX;
    
    }
    
    public float[] getModelY() {
    return modelY;
    
    }
    
    public void setxy(float[] xin,float[] yin,int n,int freq) {
        float[] x,y;
        x = new float[xin.length];
        y = new float[xin.length];
        System.arraycopy(xin, 0, x, 0, xin.length);
        System.arraycopy(yin,0,y,0,yin.length);
        float minx = x[0],miny = y[0],maxx = x[0],maxy = y[0];
        freq=0;
        
        if(freq==1){
            float count = 0;
            for(int i = 0; i<x.length;i++) {
                count = count + y[i];
            }
            for(int i = 0; i<y.length;i++) y[i] = y[i]/count;
            maxy = y[0];
            for(int i = 0; i< x.length;i++) {
                if(x[i]<minx)
                    minx = x[i];
                if(x[i]>maxx)
                    maxx = x[i];
                if(y[i]>maxy)
                    maxy = y[i];
            }
            miny = 0;
            
            
        }
        else{
            
            for(int i = 0; i< x.length;i++) {
                if(x[i]<minx)
                    minx = x[i];
                if(x[i]>maxx)
                    maxx = x[i];
                if(y[i]<miny)
                    miny = y[i];
                if(y[i]>maxy)
                    maxy = y[i];
            }
        }
        
        
        if(miny == maxy)
            miny = miny-1;
        
        setXExtrema(minx,maxx);
        setYExtrema(miny,maxy);
        
        // setSampleNumber(n);
        for (int i = 0; i<n;i++) {
            xData[i] = x[i];
            yData[i] = y[i];
            dataCursor++;
        }
        // XYPair rawData = new ModelerCanvas.XYPair(xData.length);
        //     rawData.sortXY(xData, yData, xData.length);
        //     xData = srt.xSorted;
        //     yData = srt.ySorted;
        redraw();
        
        
        
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
        numberSamplePoints = n;
    }
    /**
     * Clear the trace.
     */
    public void clear() {
        dataCursor=0;
        setSampleNumber(numberSamplePoints);
       // xData = null;
       // yData = null;
       // modelX = null;
        modelY = null;
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
    public float getMinX() {
        return minX;
    }
    public float getMaxX() {
        return maxX;
    }
    public float getMaxY() {
        return maxY;
    }
     public float getMinY() {
        return minY;
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
        
        gGlobal = g;
        drawAxes(g);
        g.setColor(Color.black);
        // lines
        Point p1,p2,p3;
        //p1=dataToScreen(xData[0],yData[0]);
        //p3=dataToScreen(0,0);
        for(int i=0;i<dataCursor;i++) {
            p2=dataToScreen(xData[i],yData[i]);
            //g.drawRect(p1.x, p1.y, p2.x-p1.x,p3.y -p1.y);
            //g.drawString(Float.toString(xData[i])+","+Float.toString(yData[i]),p3.x+5,p3.y+20*i);
            //g.drawOval(p1.x, p1.y,3,3);
            
            g.drawOval(p2.x, p2.y,5,5);
            g.fillOval(p2.x, p2.y,5,5);
            
            //g.drawLine(p1.x,p1.y,p2.x,p2.y);
            //g.drawLine(p1.x,p1.y,p2.x,p2.y);
            // p1=p2;
        }
        if (modelY != null) modelPaint(g);
    }
    
    /**
     * Paint OVER the data the actual Model that we fit.
     */
    protected void modelPaint(Graphics g) {
        // lines
        srt = new ModelerCanvas.XYPair(modelX.length);
        srt.sortXY(modelX, modelY, modelX.length);
        modelX = srt.xSorted;
        modelY = srt.ySorted;
        if (gGlobal != null)
        {   gGlobal.setColor(Color.red);
            Point p1,p2;
            p1=dataToScreen(modelX[0], modelY[0]);
            for(int i=1;i<modelX.length;i++) {
                p2=dataToScreen(modelX[i], modelY[i]);
                gGlobal.drawLine(p1.x,p1.y,p2.x,p2.y);
                p1=p2;
            }
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
    class MousePointAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
            Point2D.Float p=screenToData(evt.getPoint());
            //if(p.x>=dataCursor*dataRes+minX && dataCursor<xData.length) {
            if(dataCursor<xData.length) {
                xData[dataCursor]=p.x;
                yData[dataCursor]=p.y;
                dataCursor++;
            }
            redraw();
            
        }
        
        
        
        
    }
    
    
    
}
