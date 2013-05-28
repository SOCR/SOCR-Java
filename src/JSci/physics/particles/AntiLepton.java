package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antileptons.
* @version 1.5
* @author Mark Hale
*/
public abstract class AntiLepton extends QuantumParticle {
        /**
        * Constructs an antilepton.
        */
        public AntiLepton() {}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 1
        */
        public final int spin() {return 1;}
        /**
        * Returns the baryon number.
        * @return 0
        */
        public final int baryonQN() {return 0;}
        /**
        * Returns the strangeness number.
        * @return 0
        */
        public final int strangeQN() {return 0;}
        /**
        * Returns the number of 1/2 units of isospin.
        * @return 0
        */
        public final int isospin() {return 0;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 0
        */
        public final int isospinZ() {return 0;}
}

