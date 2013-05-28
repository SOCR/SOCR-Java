package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing sigma0.
* @version 1.5
* @author Mark Hale
*/
public final class SigmaZero extends Sigma {
        /**
        * Constructs a sigma0.
        */
        public SigmaZero() {}
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
                QuantumParticle comp[]={new Up(),new Down(),new Strange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiSigmaZero();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiSigmaZero);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Sigma0");
        }
}

