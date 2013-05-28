package JSci.awt;

import java.util.Vector;

/**
* The DefaultGraph3DModel class provides a default implementation
* of the Graph3DModel interface.
* @version 1.0
* @author Mark Hale
*/
public final class DefaultGraph3DModel extends AbstractGraphModel implements Graph3DModel {
        private float xAxis[];
        private float yAxis[];
        private final Vector series=new Vector();
        private int pos=0;
        private DataSeries curSeries=null;

        public DefaultGraph3DModel() {}
        /**
        * Sets the x-axis values.
        */
        public void setXAxis(float x[]) {
                xAxis=x;
                fireDataChanged();
        }
        /**
        * Get the x-axis values.
        */
        public float[] getXAxis() {
                return xAxis;
        }
        /**
        * Sets the y-axis values.
        */
        public void setYAxis(float y[]) {
                yAxis=y;
                fireDataChanged();
        }
        /**
        * Get the y-axis values.
        */
        public float[] getYAxis() {
                return yAxis;
        }
        /**
        * Add a data series.
        */
        public void addSeries(float newSeries[]) {
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
        * Remove a data series.
        */
        public void removeSeries(int i) {
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



        public float getXCoord(int i) {
                return xAxis[i];
        }
        public float getYCoord(int i) {
                return yAxis[i];
        }
        public float getZCoord(int i) {
                return curSeries.getValueAt(i);
        }
        public int seriesLength() {
                return curSeries.length();
        }
        public void firstSeries() {
                curSeries=(DataSeries)series.elementAt(0);
                for(pos=0;!curSeries.isVisible() && pos<series.size()-1;)
                        curSeries=(DataSeries)series.elementAt(++pos);
        }
        public boolean nextSeries() {
                if(pos==series.size()-1)
                        return false;
                do {
                        curSeries=(DataSeries)series.elementAt(++pos);
                } while(!curSeries.isVisible() && pos<series.size()-1);
                return curSeries.isVisible();
        }
}

