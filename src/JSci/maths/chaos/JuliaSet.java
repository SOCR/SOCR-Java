package JSci.maths.chaos;

import JSci.maths.Complex;

/**
* The JuliaSet class provides an object that encapsulates Julia sets.
* @version 1.0
* @author Mark Hale
*/
public final class JuliaSet extends Object {
        public final static Complex RABBIT=new Complex(-0.123,0.745);
        public final static Complex SAN_MARCO=new Complex(-0.75,0.0);
        public final static Complex SIEGEL_DISK=new Complex(-0.391,-0.587);
        private final MandelbrotMap mbrot;
        /**
        * Constructs a Julia set.
        */
        public JuliaSet(Complex c) {
                mbrot=new MandelbrotMap(c);
        }
        /**
        * Returns 0 if z is a member of this set,
        * else the number of iterations it took for z to diverge to infinity.
        */
        public int isMember(Complex z,final int maxIter) {
                for(int i=0;i<maxIter;i++) {
                        z=mbrot.map(z);
                        if(z.mod()>MandelbrotMap.CONVERGENT_BOUND)
                                return i+1;
                }
                return 0;
        }
        public int isMember(double zRe,double zIm,final int maxIter) {
                final double cRe=mbrot.getConstant().real();
                final double cIm=mbrot.getConstant().imag();
                double tmp;
                for(int i=0;i<maxIter;i++) {
                        tmp=2.0*zRe*zIm+cIm;
                        zRe=zRe*zRe-zIm*zIm+cRe;
                        zIm=tmp;
                        if(zRe*zRe+zIm*zIm>MandelbrotMap.CONVERGENT_BOUND*MandelbrotMap.CONVERGENT_BOUND)
                                return i+1;
                }
                return 0;
        }
}

