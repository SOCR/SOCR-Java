package JSci.awt;

import java.util.Vector;

/**
* The DefaultCategoryGraph2DModel class provides a default implementation
* of the CategoryGraph2DModel interface.
* @version 1.0
* @author Mark Hale
*/
public final class DefaultCategoryGraph2DModel extends AbstractGraphModel implements CategoryGraph2DModel {
        private Object category[];
        private final Vector series=new Vector();
        private int pos=0;
        private DataSeries curSeries=null;

        public DefaultCategoryGraph2DModel() {}
        /**
        * Sets the x-axis values.
        * A copy of the values is made.
        */
        public void setCategories(Object cat[]) {
                category=new Object[cat.length];
                System.arraycopy(cat,0,category,0,cat.length);
                fireDataChanged();
        }
        /**
        * Get the x-axis values.
        */
        public Object[] getCategories() {
                return category;
        }
        /**
        * Add a data series.
        */
        public void addSeries(float newSeries[]) {
                series.addElement(new DataSeries(newSeries));
                fireDataChanged();
        }
        /**
        * Add a data series.
        */
        public void addSeries(double newSeries[]) {
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

// CategoryGraph2DModel interface

        public String getCategory(int i) {
                return category[i].toString();
        }
        public float getValue(int i) {
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

