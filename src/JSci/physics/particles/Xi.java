package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing xis.
* @version 1.5
* @author Mark Hale
*/
public abstract class Xi extends Hyperon {
        /**
        * Constructs a xi.
        */
        public Xi() {}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 1
        */
        public final int spin() {return 1;}
        /**
        * Returns the number of 1/2 units of isospin.
        * @return 1
        */
        public final int isospin() {return 1;}
        /**
        * Returns the strangeness number.
        * @return -2
        */
        public final int strangeQN() {return -2;}
}

