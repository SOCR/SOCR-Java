package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antielectron neutrinos.
* @version 1.5
* @author Mark Hale
*/
public final class AntiElectronNeutrino extends AntiLepton {
        /**
        * Constructs an antielectron neutrino.
        */
        public AntiElectronNeutrino() {}
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
        * @return -1
        */
        public int eLeptonQN() {return -1;}
        /**
        * Returns the muon lepton number.
        * @return 0
        */
        public int muLeptonQN() {return 0;}
        /**
        * Returns the tau lepton number.
        * @return 0
        */
        public int tauLeptonQN() {return 0;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new ElectronNeutrino();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof ElectronNeutrino);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antielectron neutrino");
        }
        /**
        * Emits a W-.
        */
        public Positron emit(WMinus w) {
                Positron e=new Positron();
                e.momentum=momentum.subtract(w.momentum);
                return e;
        }
        /**
        * Absorbs a W+.
        */
        public Positron absorb(WPlus w) {
                Positron e=new Positron();
                e.momentum=momentum.add(w.momentum);
                return e;
        }
        /**
        * Emits a Z0.
        */
        public AntiElectronNeutrino emit(ZZero z) {
                momentum=momentum.subtract(z.momentum);
                return this;
        }
        /**
        * Absorbs a Z0.
        */
        public AntiElectronNeutrino absorb(ZZero z) {
                momentum=momentum.add(z.momentum);
                return this;
        }
}

