package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing K0.
* @version 1.5
* @author Mark Hale
*/
public final class KZero extends Kaon {
        /**
        * Constructs a K0.
        */
        public KZero() {}
        /**
        * Returns the rest mass (MeV).
        * @return 497.672
        */
        public double restMass() {return 497.672;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return -1
        */
        public int isospinZ() {return -1;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new Down(),new AntiStrange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiKZero();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiKZero);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("K0");
        }
}

