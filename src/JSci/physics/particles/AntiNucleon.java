package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antinucleons.
* @version 1.5
* @author Mark Hale
*/
public abstract class AntiNucleon extends AntiBaryon {
        /**
        * Constructs an antinucleon.
        */
        public AntiNucleon() {}
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
        * @return 0
        */
        public final int strangeQN() {return 0;}
}

