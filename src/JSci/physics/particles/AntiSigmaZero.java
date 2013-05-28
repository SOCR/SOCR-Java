package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing anti sigma0.
* @version 1.5
* @author Mark Hale
*/
public final class AntiSigmaZero extends AntiSigma {
        /**
        * Constructs an anti sigma0.
        */
        public AntiSigmaZero() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1192.642
        */
        public double restMass() {return 1192.642;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 0
        */
        public int isospinZ() {return 0;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new AntiUp(),new AntiDown(),new AntiStrange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new SigmaZero();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof SigmaZero);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antisigma0");
        }
}

