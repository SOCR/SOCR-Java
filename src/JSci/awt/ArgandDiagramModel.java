package JSci.awt;

import JSci.maths.*;

/**
* The ArgandDiagramModel provides a convenient implementation of
* the Graph2DModel interface for creating Argand diagrams using
* the LineGraph component.
* @version 1.0
* @author Mark Hale
*/
public final class ArgandDiagramModel extends AbstractGraphModel implements Graph2DModel {
        private Complex data[];

        public ArgandDiagramModel() {}
        public void setData(Complex z[]) {
                data=z;
                fireDataChanged();
        }
        public Complex[] getData() {
                return data;
        }


        public float getXCoord(int i) {
                return (float)data[i].real();
        }
        public float getYCoord(int i) {
                return (float)data[i].imag();
        }
        public int seriesLength() {
                return data.length;
        }
        public void firstSeries() {}
        public boolean nextSeries() {
                return false;
        }
}

