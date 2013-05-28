package JSci.awt;

import java.awt.*;
import javax.swing.*;

/**
* The DoubleBufferedCanvas class provides double buffering functionality.
* @version 1.2
* @author Mark Hale
*/
public abstract class DoubleBufferedCanvas extends JPanel {
        private Image buffer=null;
        /**
        * Constructs a double buffered canvas.
        */
        public DoubleBufferedCanvas() {}
        /**
        * Paints the canvas using double buffering.
        * @see #offscreenPaint
        */
        public final synchronized void paint(Graphics g) {
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
        * Redraws the canvas.
        */
        public final synchronized void redraw() {
                buffer=null;
                repaint();
        }
        /**
        * Paints the canvas off-screen.
        * Override this method instead of paint(Graphics g).
        */
        protected abstract void offscreenPaint(Graphics g);
}

