package JSci.physics.quantum;

import JSci.physics.RelativisticParticle;

/**
* A class representing quantum particles.
* @version 1.5
* @author Mark Hale
*/
public abstract class QuantumParticle extends RelativisticParticle {
        /**
        * The number of 1/2 units of the z-component of spin.
        */
        public int spinZ;
        /**
        * Constructs a quantum particle.
        */
        public QuantumParticle() {}
        /**
        * Returns the number of 1/2 units of spin.
        */
        public abstract int spin();
        /**
        * Returns the number of 1/2 units of isospin.
        */
        public abstract int isospin();
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        */
        public abstract int isospinZ();
        /**
        * Returns the electric charge.
        */
        public abstract int charge();
        /**
        * Returns the electron lepton number.
        */
        public abstract int eLeptonQN();
        /**
        * Returns the muon lepton number.
        */
        public abstract int muLeptonQN();
        /**
        * Returns the tau lepton number.
        */
        public abstract int tauLeptonQN();
        /**
        * Returns the baryon number.
        */
        public abstract int baryonQN();
        /**
        * Returns the strangeness number.
        */
        public abstract int strangeQN();
        /**
        * Returns the antiparticle of this particle.
        */
        public abstract QuantumParticle anti();
        /**
        * Returns true if qp is the antiparticle.
        */
        public abstract boolean isAnti(QuantumParticle qp);
}

