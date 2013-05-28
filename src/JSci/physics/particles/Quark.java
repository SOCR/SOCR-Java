package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing quarks.
* @version 1.5
* @author Mark Hale
*/
public abstract class Quark extends QuantumParticle {
        public final static int RED=1;
        public final static int GREEN=2;
        public final static int BLUE=3;
        /**
        * The color.
        */
        public int color;
        /**
        * Constructs a quark.
        */
        public Quark() {}
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
        * @return 1
        */
        public final int baryonQN() {return 1;}
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
        public Quark emit(Gluon g) {
                momentum=momentum.subtract(g.momentum);
                return this;
        }
        /**
        * Absorbs a gluon.
        */
        public Quark absorb(Gluon g) {
                momentum=momentum.add(g.momentum);
                return this;
        }
}

