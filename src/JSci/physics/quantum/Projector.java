package JSci.physics.quantum;

/**
* The Projector class provides an object for encapsulating projection operators.
* @version 1.5
* @author Mark Hale
*/
public class Projector extends Operator {
        /**
        * Constructs a projector from a ket vector.
        * @param ket a ket vector
        */
        public Projector(KetVector ket) {
                super(ket.multiply(ket.toBraVector()).getRepresentation());
        }
}

