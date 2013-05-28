package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antiquarks.
* @version 1.5
* @author Mark Hale
*/
public abstract class AntiQuark extends QuantumParticle {
        public final static int ANTIRED=-1;
        public final static int ANTIGREEN=-2;
        public final static int ANTIBLUE=-3;
        /**
        * The color.
        */
        public int color;
        /**
        * Constructs an antiquark.
        */
        public AntiQuark() {}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 1
        */
        public final int spin() {return 1;}
        /**
        * Returns the electron lepton number.
        * @return 0
        */
        public final int eLeptonQN() {return 0;}
        /**
        * Returns the muon lepton number.
        * @return 0
        */
        public final int muLeptonQN() {return 0;}
        /**
        * Returns the tau lepton number.
        * @return 0
        */
        public final int tauLeptonQN() {return 0;}
        /**
        * Returns the number of 1/3 units of baryon number.
        * @return -1
        */
        public final int baryonQN() {return -1;}
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
        /**
        * Emits a gluon.
        */
        public AntiQuark emit(Gluon g) {
                momentum=momentum.subtract(g.momentum);
                return this;
        }
        /**
        * Absorbs a gluon.
        */
        public AntiQuark absorb(Gluon g) {
                momentum=momentum.add(g.momentum);
                return this;
        }
}

