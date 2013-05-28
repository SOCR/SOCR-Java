package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing K-.
* @version 1.5
* @author Mark Hale
*/
public final class KMinus extends AntiKaon {
        /**
        * Constructs a K-.
        */
        public KMinus() {}
        /**
        * Returns the rest mass (MeV).
        * @return 493.677
        */
        public double restMass() {return 493.677;}
        /**
        * Returns the electric charge.
        * @return -1
        */
        public int charge() {return -1;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return -1
        */
        public int isospinZ() {return -1;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new AntiUp(),new Strange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new KPlus();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof KPlus);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("K-");
        }
}

