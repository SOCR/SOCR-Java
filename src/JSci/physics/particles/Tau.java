package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing taus.
* @version 1.5
* @author Mark Hale
*/
public final class Tau extends Lepton {
        /**
        * Constructs a tau.
        */
        public Tau() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1777.03
        */
        public double restMass() {return 1777.03;}
        /**
        * Returns the electric charge.
        * @return -1
        */
        public int charge() {return -1;}
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
        * @return 1
        */
        public int tauLeptonQN() {return 1;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiTau();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiTau);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Tau");
        }
        /**
        * Emits a photon.
        */
        public Tau emit(Photon y) {
                momentum=momentum.subtract(y.momentum);
                return this;
        }
        /**
        * Absorbs a photon.
        */
        public Tau absorb(Photon y) {
                momentum=momentum.add(y.momentum);
                return this;
        }
        /**
        * Emits a W-.
        */
        public TauNeutrino emit(WMinus w) {
                TauNeutrino n=new TauNeutrino();
                n.momentum=momentum.subtract(w.momentum);
                return n;
        }
        /**
        * Absorbs a W+.
        */
        public TauNeutrino absorb(WPlus w) {
                TauNeutrino n=new TauNeutrino();
                n.momentum=momentum.add(w.momentum);
                return n;
        }
        /**
        * Emits a Z0.
        */
        public Tau emit(ZZero z) {
                momentum=momentum.subtract(z.momentum);
                return this;
        }
        /**
        * Absorbs a Z0.
        */
        public Tau absorb(ZZero z) {
                momentum=momentum.add(z.momentum);
                return this;
        }
}

