package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing baryons.
* @version 1.5
* @author Mark Hale
*/
public abstract class Baryon extends Hadron {
        /**
        * Constructs a baryon.
        */
        public Baryon() {}
        /**
        * Returns the baryon number.
        * @return 1
        */
        public final int baryonQN() {return 1;}
}

