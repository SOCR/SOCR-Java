package JSci.awt;

/**
* This is a generic interface for sending data to 2D category graphs.
* @version 1.0
* @author Mark Hale
*/
public interface CategoryGraph2DModel {
        /**
        * Add a listener.
        */
        void addGraphDataListener(GraphDataListener l);
        /**
        * Remove a listener.
        */
        void removeGraphDataListener(GraphDataListener l);
        /**
        * Returns the ith category.
        */
        String getCategory(int i);
        /**
        * Returns the value for the ith category.
        */
        float getValue(int i);
        /**
        * Returns the number of data points in the current series.
        */
        int seriesLength();
        /**
        * Selects the first data series.
        */
        void firstSeries();
        /**
        * Selects the next data series.
        * Returns false if there is no next series.
        */
        boolean nextSeries();
}

