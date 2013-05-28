package JSci.swing;

import java.awt.*;
import javax.swing.*;

/**
* The JDoubleBufferedComponent class provides double buffering functionality.
* @version 1.2
* @author Mark Hale
*/
public abstract class JDoubleBufferedComponent extends JComponent {
        private Image buffer=null;
        /**
        * Constructs a double buffered canvas.
        */
        public JDoubleBufferedComponent() {
                super.setDoubleBuffered(false);
        }
        /**
        * Paints the canvas using double buffering.
        * @see #offscreenPaint
        */
        public final void paint(Graphics g) {
                if(buffer==null) {
                        final int width=getSize().width;
                        final int height=getSize().height;
                        buffer=createImage(width,height);
                        final Graphics graphics=buffer.getGraphics();
                        graphics.setColor(getBackground());
                        graphics.fillRect(0,0,width,height);
                        offscreenPaint(graphics);
                }
                g.drawImage(buffer,0,0,this);
        }
        /**
        * Updates the canvas.
        */
        public final void update(Graphics g) {
                paint(g);
        }
        /**
        * Prints the canvas.
        */
        public final void print(Graphics g) {
                offscreenPaint(g);
        }
        /**
        * Double buffering cannot be controlled for this component.
        * This method always throws an exception.
        */
        public void setDoubleBuffered(boolean flag) {
                throw new IllegalArgumentException();
        }
        /**
        * Double buffering is always enabled.
        * @return true.
        */
        public boolean isDoubleBuffered() {
                return true;
        }
        /**
        * Redraws the canvas.
        */
        public final void redraw() {
                buffer=null;
                repaint();
        }
        /**
        * Paints the canvas off-screen.
        * Override this method instead of paint(Graphics g).
        */
        protected abstract void offscreenPaint(Graphics g);
}

