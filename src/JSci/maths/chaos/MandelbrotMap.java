package JSci.maths.chaos;

import JSci.maths.*;

/**
* The MandelbrotMap class provides an object that encapsulates the Mandelbrot map.
* z<sub>n+1</sub> = z<sub>n</sub><sup>2</sup> + c.
* @version 1.0
* @author Mark Hale
*/
public final class MandelbrotMap extends Object implements ComplexMapping {
        /**
        * A complex number z such that |z| &gt; CONVERGENT_BOUND
        * will diverge under this map.
        */
        public final static double CONVERGENT_BOUND=2.0;
        private Complex a;
        /**
        * Constructs a Mandelbrot map.
        * @param aval the value of the constant.
        */
        public MandelbrotMap(double aval) {
                a=new Complex(aval,0.0);
        }
        /**
        * Constructs a Mandelbrot map.
        * @param aval the value of the constant.
        */
        public MandelbrotMap(Complex aval) {
                a=aval;
        }
        /**
        * Returns the constant.
        */
        public Complex getConstant() {
                return a;
        }
        /**
        * Sets the constant.
        */
        public void setConstant(Complex aval) {
                a=aval;
        }
        /**
        * Performs the mapping.
        * @param x a double
        */
        public double map(double x) {
                return x*x+a.real();
        }
        /**
        * Performs the mapping.
        * @param z a complex number.
        */
        public Complex map(Complex z) {
                return map(z.real(),z.imag());
        }
        /**
        * Performs the mapping.
        */
        public Complex map(double real,double imag) {
                return new Complex(real*real-imag*imag+a.real(),2.0*real*imag+a.imag());
        }
        /**
        * Iterates the map.
        * @param n the number of iterations
        * @param x the initial value
        */
        public double iterate(int n,double x) {
                for(int i=0;i<n;i++)
                        x=map(x);
                return x;
        }
        /**
        * Iterates the map.
        * @param n the number of iterations
        * @param z the initial value
        */
        public Complex iterate(int n,Complex z) {
                for(int i=0;i<n;i++)
                        z=map(z);
                return z;
        }
}

