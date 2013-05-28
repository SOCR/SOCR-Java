package JSci.swing;

import java.awt.*;
import javax.swing.*;

/**
* The JImageCanvas class allows an image to be directly added to a container.
* @author Daniel Lemire
*/
public class JImageCanvas extends JComponent {
        protected Image image;

        /**
        * Constructs an image canvas.
        */
        public JImageCanvas(Image img) {
                image = img;
                waitForImage();
        }
        /**
        * Paints the canvas.
        */
        public void paint(Graphics g) {
                g.drawImage(image, 0, 0, this);
        }
        private void waitForImage() {
                MediaTracker tracker = new MediaTracker(this);
                try {
                        tracker.addImage(image, 0);
                        tracker.waitForAll();
                        tracker.waitForID(0);
                        if (tracker.checkID(0))
                                repaint();
                        else
                                System.out.println("Could not load the image.");
                } catch(InterruptedException e) {}
        }
}

