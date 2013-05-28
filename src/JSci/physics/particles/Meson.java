package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing mesons.
* @version 1.5
* @author Mark Hale
*/
public abstract class Meson extends Hadron {
        /**
        * Constructs a meson.
        */
        public Meson() {}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 0
        */
        public final int spin() {return 0;}
        /**
        * Returns the baryon number.
        * @return 0
        */
        public final int baryonQN() {return 0;}
}

