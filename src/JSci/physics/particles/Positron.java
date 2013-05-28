package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing positrons.
* @version 1.5
* @author Mark Hale
*/
public final class Positron extends AntiLepton {
        /**
        * Constructs a positron.
        */
        public Positron() {}
        /**
        * Returns the rest mass (MeV).
        * @return 0.510998902
        */
        public double restMass() {return 0.510998902;}
        /**
        * Returns the electric charge.
        * @return 1
        */
        public int charge() {return 1;}
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
                return new Electron();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof Electron);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Positron");
        }
        /**
        * Emits a photon.
        */
        public Positron emit(Photon y) {
                momentum=momentum.subtract(y.momentum);
                return this;
        }
        /**
        * Absorbs a photon.
        */
        public Positron absorb(Photon y) {
                momentum=momentum.add(y.momentum);
                return this;
        }
        /**
        * Emits a W+.
        */
        public AntiElectronNeutrino emit(WPlus w) {
                AntiElectronNeutrino n=new AntiElectronNeutrino();
                n.momentum=momentum.subtract(w.momentum);
                return n;
        }
        /**
        * Absorbs a W-.
        */
        public AntiElectronNeutrino absorb(WMinus w) {
                AntiElectronNeutrino n=new AntiElectronNeutrino();
                n.momentum=momentum.add(w.momentum);
                return n;
        }
        /**
        * Emits a Z0.
        */
        public Positron emit(ZZero z) {
                momentum=momentum.subtract(z.momentum);
                return this;
        }
        /**
        * Absorbs a Z0.
        */
        public Positron absorb(ZZero z) {
                momentum=momentum.add(z.momentum);
                return this;
        }
}

