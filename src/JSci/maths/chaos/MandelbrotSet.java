package JSci.maths.chaos;

import JSci.maths.Complex;

/**
* The MandelbrotSet class provides an object that encapsulates the Mandelbrot set.
* @version 1.0
* @author Mark Hale
*/
public final class MandelbrotSet extends Object {
        private final MandelbrotMap mbrot=new MandelbrotMap(Complex.ZERO);
        /**
        * Constructs a Mandelbrot set.
        */
        public MandelbrotSet() {}
        /**
        * Returns 0 if z is a member of this set,
        * else the number of iterations it took for z to diverge to infinity.
        */
        public int isMember(final Complex z,final int maxIter) {
                mbrot.setConstant(z);
                Complex w=Complex.ZERO;
                for(int i=0;i<maxIter;i++) {
                        w=mbrot.map(w);
                        if(w.mod()>MandelbrotMap.CONVERGENT_BOUND)
                                return i+1;
                }
                return 0;
        }
        public int isMember(final double zRe,final double zIm,final int maxIter) {
                double re=0.0,im=0.0;
                double tmp;
                for(int i=0;i<maxIter;i++) {
                        tmp=2.0*re*im+zIm;
                        re=re*re-im*im+zRe;
                        im=tmp;
                        if(re*re+im*im>MandelbrotMap.CONVERGENT_BOUND*MandelbrotMap.CONVERGENT_BOUND)
                                return i+1;
                }
                return 0;
        }
}

