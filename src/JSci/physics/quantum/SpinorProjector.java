package JSci.physics.quantum;

import JSci.maths.*;

/**
* The SpinorProjector class encapsulates the left-handed and right-handed projection operators.
* @version 1.0
* @author Mark Hale
*/
public final class SpinorProjector extends Projector {
        private final static Complex ul[]={Complex.ONE,Complex.ZERO};
        private final static Complex ur[]={Complex.ZERO,Complex.ONE};
        /**
        * Left-handed projector (P<SUB>L</SUB>).
        */
        public final static SpinorProjector LEFT=new SpinorProjector(ul);
        /**
        * Right-handed projector (P<SUB>R</SUB>).
        */
        public final static SpinorProjector RIGHT=new SpinorProjector(ur);
        /**
        * Constructs a spinor projector from a ket vector.
        * @param ket a ket vector
        */
        private SpinorProjector(Complex array[]) {
                super(new KetVector(new ComplexVector(array)));
        }
}

