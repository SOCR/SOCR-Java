package JSci.awt;

/**
* This is a generic interface for sending data to 3D graphs.
* @version 1.0
* @author Mark Hale
*/
public interface Graph3DModel {
        /**
        * Add a listener.
        */
        void addGraphDataListener(GraphDataListener l);
        /**
        * Remove a listener.
        */
        void removeGraphDataListener(GraphDataListener l);
        /**
        * Returns the x coordinate for the ith point.
        */
        float getXCoord(int i);
        /**
        * Returns the y coordinate for the ith point.
        */
        float getYCoord(int i);
        /**
        * Returns the z coordinate for the ith point.
        */
        float getZCoord(int i);
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

