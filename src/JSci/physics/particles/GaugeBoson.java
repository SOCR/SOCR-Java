package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing gauge bosons.
* @version 1.5
* @author Mark Hale
*/
public abstract class GaugeBoson extends QuantumParticle {
        /**
        * Constructs a gauge boson.
        */
        public GaugeBoson() {}
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
}

