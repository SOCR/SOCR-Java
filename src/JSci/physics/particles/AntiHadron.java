package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antihadrons.
* @version 1.5
* @author Mark Hale
*/
public abstract class AntiHadron extends QuantumParticle {
        /**
        * Constructs an antihadron.
        */
        public AntiHadron() {}
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