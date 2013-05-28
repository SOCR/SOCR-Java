package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing pions.
* @version 1.5
* @author Mark Hale
*/
public abstract class Pion extends Meson {
        /**
        * Constructs a pion.
        */
        public Pion() {}
        /**
        * Returns the number of 1/2 units of isospin.
        * @return 2
        */
        public final int isospin() {return 2;}
        /**
        * Returns the strangeness number.
        * @return 0
        */
        public final int strangeQN() {return 0;}
}

