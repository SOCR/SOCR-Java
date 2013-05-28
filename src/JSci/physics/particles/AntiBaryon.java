package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antibaryons.
* @version 1.5
* @author Mark Hale
*/
public abstract class AntiBaryon extends AntiHadron {
        /**
        * Constructs an antibaryon.
        */
        public AntiBaryon() {}
        /**
        * Returns the baryon number.
        * @return -1
        */
        public final int baryonQN() {return -1;}
}

