package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antitau neutrinos.
* @version 1.5
* @author Mark Hale
*/
public final class AntiTauNeutrino extends AntiLepton {
        /**
        * Constructs an antitau neutrino.
        */
        public AntiTauNeutrino() {}
        /**
        * Returns the rest mass (MeV).
        * @return 0.0
        */
        public double restMass() {return 0.0;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the electron lepton number.
        * @return 0
        */
        public int eLeptonQN() {return 0;}
        /**
        * Returns the muon lepton number.
        * @return 0
        */
        public int muLeptonQN() {return 0;}
        /**
        * Returns the tau lepton number.
        * @return -1
        */
        public int tauLeptonQN() {return -1;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new TauNeutrino();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof TauNeutrino);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antitau neutrino");
        }
        /**
        * Emits a W-.
        */
        public AntiTau emit(WMinus w) {
                AntiTau e=new AntiTau();
                e.momentum=momentum.subtract(w.momentum);
                return e;
        }
        /**
        * Absorbs a W+.
        */
        public AntiTau absorb(WPlus w) {
                AntiTau e=new AntiTau();
                e.momentum=momentum.add(w.momentum);
                return e;
        }
        /**
        * Emits a Z0.
        */
        public AntiTauNeutrino emit(ZZero z) {
                momentum=momentum.subtract(z.momentum);
                return this;
        }
        /**
        * Absorbs a Z0.
        */
        public AntiTauNeutrino absorb(ZZero z) {
                momentum=momentum.add(z.momentum);
                return this;
        }
}

