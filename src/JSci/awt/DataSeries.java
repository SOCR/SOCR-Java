package JSci.awt;

/**
* The DataSeries class encapsulates a data series for a graph.
* @version 0.1
* @author Mark Hale
*/
public final class DataSeries extends Object {
        private float series[];
        private boolean isVis=true;

        public DataSeries(float data[]) {
                series=new float[data.length];
                System.arraycopy(data,0,series,0,data.length);
        }
        public DataSeries(double data[]) {
                series=new float[data.length];
                for(int i=0;i<series.length;i++)
                        series[i]=(float)data[i];
        }
        public float[] getData() {
                return series;
        }
        public float getValueAt(int i) {
                return series[i];
        }
        public int length() {
                return series.length;
        }
        public void setVisible(boolean flag) {
                isVis=flag;
        }
        public boolean isVisible() {
                return isVis;
        }
}

