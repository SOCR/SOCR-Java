package JSci.maths.chaos;

/**
* The KochCurve class provides an object that encapsulates the Koch curve.
* @version 0.1
* @author Mark Hale
*/
public abstract class KochCurve extends Object {
        public KochCurve() {}
        public double hausdorffDimension() {
                return Math.log(4.0)/Math.log(3.0);
        }
        /**
        * The recursive algorithm for the Koch curve.
        * @param startX the x-coordinate of the start of the line.
        * @param startY the x-coordinate of the start of the line.
        * @param endX the x-coordinate of the end of the line.
        * @param endY the x-coordinate of the end of the line.
        * @param n the number of recursions.
        */
        public void recurse(double startX, double startY, double endX, double endY, int n) {
                if(n==0)
                        return;
                final double l_3X=(endX-startX)/3.0;
                final double l_3Y=(endY-startY)/3.0;
                eraseLine(startX+l_3X, startY+l_3Y, endX-l_3X, endY-l_3Y);
                final double h=Math.sqrt(3.0)/2.0;
                final double pX=(startX+endX)/2.0-l_3Y*h;
                final double pY=(startY+endY)/2.0+l_3X*h;
                drawLine(startX+l_3X, startY+l_3Y, pX, pY);
                drawLine(pX, pY, endX-l_3X, endY-l_3Y);
                recurse(startX, startY, startX+l_3X, startY+l_3Y, n-1);
                recurse(startX+l_3X, startY+l_3Y, pX, pY, n-1);
                recurse(pX, pY, endX-l_3X, endY-l_3Y, n-1);
                recurse(endX-l_3X, endY-l_3Y, endX, endY, n-1);
        }
        /**
        * Draws a line segment in a 2D plane.
        * This need not be an actual graphical operation,
        * but some corresponding abstract operation.
        */
        protected abstract void drawLine(double startX, double startY, double endX, double endY);
        /**
        * Erases a line segment in a 2D plane.
        * This need not be an actual graphical operation,
        * but some corresponding abstract operation.
        */
        protected abstract void eraseLine(double startX, double startY, double endX, double endY);
}

