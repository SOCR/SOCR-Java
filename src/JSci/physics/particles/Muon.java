package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing muons.
* @version 1.5
* @author Mark Hale
*/
public final class Muon extends Lepton {
        /**
        * Constructs a muon.
        */
        public Muon() {}
        /**
        * Returns the rest mass (MeV).
        * @return 105.658357
        */
        public double restMass() {return 105.658357;}
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
        * @return 1
        */
        public int muLeptonQN() {return 1;}
        /**
        * Returns the tau lepton number.
        * @return 0
        */
        public int tauLeptonQN() {return 0;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiMuon();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiMuon);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Muon");
        }
        /**
        * Emits a photon.
        */
        public Muon emit(Photon y) {
                momentum=momentum.subtract(y.momentum);
                return this;
        }
        /**
        * Absorbs a photon.
        */
        public Muon absorb(Photon y) {
                momentum=momentum.add(y.momentum);
                return this;
        }
        /**
        * Emits a W-.
        */
        public MuonNeutrino emit(WMinus w) {
                MuonNeutrino n=new MuonNeutrino();
                n.momentum=momentum.subtract(w.momentum);
                return n;
        }
        /**
        * Absorbs a W+.
        */
        public MuonNeutrino emit(WPlus w) {
                MuonNeutrino n=new MuonNeutrino();
                n.momentum=momentum.add(w.momentum);
                return n;
        }
        /**
        * Emits a Z0.
        */
        public Muon emit(ZZero z) {
                momentum=momentum.subtract(z.momentum);
                return this;
        }
        /**
        * Absorbs a Z0.
        */
        public Muon absorb(ZZero z) {
                momentum=momentum.add(z.momentum);
                return this;
        }
}

