package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing sigma+.
* @version 1.5
* @author Mark Hale
*/
public final class SigmaPlus extends Sigma {
        /**
        * Constructs a sigma+.
        */
        public SigmaPlus() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1189.37
        */
        public double restMass() {return 1189.37;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 2
        */
        public int isospinZ() {return 2;}
        /**
        * Returns the electric charge.
        * @return 1
        */
        public int charge() {return 1;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new Up(),new Up(),new Strange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiSigmaPlus();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiSigmaPlus);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Sigma+");
        }
}

