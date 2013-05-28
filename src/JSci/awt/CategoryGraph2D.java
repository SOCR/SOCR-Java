package JSci.awt;

import java.awt.*;

/**
* The CategoryGraph2D superclass provides an abstract encapsulation
* of 2D category graphs.
* @version 1.2
* @author Mark Hale
*/
public abstract class CategoryGraph2D extends DoubleBufferedCanvas implements GraphDataListener {
        /**
        * Data model.
        */
        protected CategoryGraph2DModel model;
        /**
        * Origin.
        */
        protected Point origin=new Point();
        /**
        * Padding.
        */
        protected final int scalePad=5;
        protected final int axisPad=25;
        protected int leftAxisPad;
        /**
        * Constructs a 2D category graph.
        */
        public CategoryGraph2D(CategoryGraph2DModel cgm) {
                model=cgm;
                model.addGraphDataListener(this);
        }
        /**
        * Sets the data plotted by this graph to the specified data.
        */
        public final void setModel(CategoryGraph2DModel cgm) {
                model.removeGraphDataListener(this);
                model=cgm;
                model.addGraphDataListener(this);
                dataChanged(new GraphDataEvent(model));
        }
        /**
        * Returns the model used by this graph.
        */
        public final CategoryGraph2DModel getModel() {
                return model;
        }
        public abstract void dataChanged(GraphDataEvent e);
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
}

