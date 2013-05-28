package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antitaus.
* @version 1.5
* @author Mark Hale
*/
public final class AntiTau extends AntiLepton {
        /**
        * Constructs an antitau.
        */
        public AntiTau() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1777.03
        */
        public double restMass() {return 1777.03;}
        /**
        * Returns the electric charge.
        * @return 1
        */
        public int charge() {return 1;}
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
                return new Tau();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof Tau);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antitau");
        }
        /**
        * Emits a photon.
        */
        public AntiTau emit(Photon y) {
                momentum=momentum.subtract(y.momentum);
                return this;
        }
        /**
        * Absorbs a photon.
        */
        public AntiTau absorb(Photon y) {
                momentum=momentum.add(y.momentum);
                return this;
        }
        /**
        * Emits a W+.
        */
        public AntiTauNeutrino emit(WPlus w) {
                AntiTauNeutrino n=new AntiTauNeutrino();
                n.momentum=momentum.subtract(w.momentum);
                return n;
        }
        /**
        * Absorbs a W-.
        */
        public AntiTauNeutrino absorb(WMinus w) {
                AntiTauNeutrino n=new AntiTauNeutrino();
                n.momentum=momentum.add(w.momentum);
                return n;
        }
        /**
        * Emits a Z0.
        */
        public AntiTau emit(ZZero z) {
                momentum=momentum.subtract(z.momentum);
                return this;
        }
        /**
        * Absorbs a Z0.
        */
        public AntiTau absorb(ZZero z) {
                momentum=momentum.add(z.momentum);
                return this;
        }
}

