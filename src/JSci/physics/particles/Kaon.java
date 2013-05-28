package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing kaons.
* @version 1.5
* @author Mark Hale
*/
public abstract class Kaon extends Meson {
        /**
        * Constructs a kaon.
        */
        public Kaon() {}
        /**
        * Returns the number of 1/2 units of isospin.
        * @return 1
        */
        public final int isospin() {return 1;}
        /**
        * Returns the strangeness number.
        * @return 1
        */
        public final int strangeQN() {return 1;}
}

