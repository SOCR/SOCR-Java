package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antisigmas.
* @version 1.5
* @author Mark Hale
*/
public abstract class AntiSigma extends Hyperon {
        /**
        * Constructs an antisigma.
        */
        public AntiSigma() {}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 1
        */
        public final int spin() {return 1;}
        /**
        * Returns the number of 1/2 units of isospin.
        * @return 2
        */
        public final int isospin() {return 2;}
        /**
        * Returns the strangeness number.
        * @return 1
        */
        public final int strangeQN() {return 1;}
}

