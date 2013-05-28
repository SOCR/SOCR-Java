package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing pi0.
* @version 1.5
* @author Mark Hale
*/
public final class PiZero extends Pion {
        /**
        * Constructs a pi0.
        */
        public PiZero() {}
        /**
        * Returns the rest mass (MeV).
        * @return 134.9766
        */
        public double restMass() {return 134.9766;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 0
        */
        public int isospinZ() {return 0;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new Down(),new AntiDown(),new Up(),new AntiUp()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new PiZero();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof PiZero);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Pi0");
        }
}

