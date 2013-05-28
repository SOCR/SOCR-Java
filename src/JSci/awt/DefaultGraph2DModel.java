package JSci.awt;

import java.util.Vector;

/**
* The DefaultGraph2DModel class provides a default implementation
* of the Graph2DModel interface.
* @version 1.1
* @author Mark Hale
*/
public final class DefaultGraph2DModel extends AbstractGraphModel implements Graph2DModel {
        private float defaultXAxis[]=new float[0];
        private final Vector<float[]> xData=new Vector<float[]>();
        private final Vector<DataSeries> series=new Vector<DataSeries>();
        private int pos=0;
        private float curXAxis[]=null;
        private DataSeries curSeries=null;

        public DefaultGraph2DModel() {}
        /**
        * Sets the default x-axis values.
        * A copy of the values is made.
        */
        public void setXAxis(float x[]) {
                if(defaultXAxis.length!=x.length)
                        defaultXAxis=new float[x.length];
                System.arraycopy(x,0,defaultXAxis,0,x.length);
                fireDataChanged();
        }
        /**
        * Sets the default x-axis values.
        * A copy of the values is made.
        */
        public void setXAxis(double x[]) {
                if(defaultXAxis.length!=x.length)
                        defaultXAxis=new float[x.length];
                for(int i=0;i<x.length;i++)
                        defaultXAxis[i]=(float)x[i];
                fireDataChanged();
        }
        /**
        * Sets the default x-axis values.
        * @param a start of interval.
        * @param b end of interval.
        * @param n number of values.
        */
        public void setXAxis(float a,float b,int n) {
                if(defaultXAxis.length != n)
                        defaultXAxis = new float[n];
                final float scale = (b-a)/(n-1);
                for(int i=0; i<n; i++)
                        defaultXAxis[i] = scale*i+a;
                fireDataChanged();
        }
        /**
        * Gets the default x-axis values.
        */
        public float[] getXAxis() {
                return defaultXAxis;
        }
        /**
        * Adds a data series for the default x-axis values.
        */
        public void addSeries(float newSeries[]) {
                addSeries(defaultXAxis,newSeries);
        }
        /**
        * Adds a data series for the default x-axis values.
        */
        public void addSeries(double newSeries[]) {
                xData.addElement(defaultXAxis);
                series.addElement(new DataSeries(newSeries));
                fireDataChanged();
        }
        /**
        * Adds a data series.
        */
        public void addSeries(float newXAxis[],float newSeries[]) {
                xData.addElement(newXAxis);
                series.addElement(new DataSeries(newSeries));
                fireDataChanged();
        }
        /**
        * Change a data series.
        */
        public void changeSeries(int i,float newSeries[]) {
                series.setElementAt(new DataSeries(newSeries),i);
                fireDataChanged();
        }
        /**
        * Change a data series.
        */
        public void changeSeries(int i,double newSeries[]) {
                series.setElementAt(new DataSeries(newSeries),i);
                fireDataChanged();
        }
        /**
        * Remove a data series.
        */
        public void removeSeries(int i) {
                xData.removeElementAt(i);
                series.removeElementAt(i);
                fireDataChanged();
        }
        public DataSeries getSeries(int i) {
                return (DataSeries)series.elementAt(i);
        }
        public void setSeriesVisible(int i,boolean flag) {
                ((DataSeries)series.elementAt(i)).setVisible(flag);
                fireDataChanged();
        }

// Graph2DModel interface

        public float getXCoord(int i) {
                return curXAxis[i];
        }
        public float getYCoord(int i) {
                return curSeries.getValueAt(i);
        }
        public int seriesLength() {
                  if (curSeries != null) return curSeries.length();
			else return (0);
        }
        public void firstSeries() {
                if(series.isEmpty()) { }
                else{
                curSeries=(DataSeries)series.elementAt(0);
                for(pos=0;!curSeries.isVisible() && pos<series.size()-1;)
                        curSeries=(DataSeries)series.elementAt(++pos);
                curXAxis=(float[])xData.elementAt(pos);}
        }
        public boolean nextSeries() {
                if(pos==series.size()-1)
                        return false;
                do {
                        curSeries=(DataSeries)series.elementAt(++pos);
                } while(!curSeries.isVisible() && pos<series.size()-1);
                curXAxis=(float[])xData.elementAt(pos);
                return curSeries.isVisible();
        }
}

