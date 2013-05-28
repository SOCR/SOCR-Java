package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing omega-.
* @version 1.5
* @author Mark Hale
*/
public final class OmegaMinus extends Hyperon {
        /**
        * Constructs an omega-.
        */
        public OmegaMinus() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1672.45
        */
        public double restMass() {return 1672.45;}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 3
        */
        public int spin() {return 3;}
        /**
        * Returns the number of 1/2 units of isospin.
        * @return 0
        */
        public int isospin() {return 0;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 0
        */
        public int isospinZ() {return 0;}
        /**
        * Returns the electric charge.
        * @return -1
        */
        public int charge() {return -1;}
        /**
        * Returns the strangeness number.
        * @return -3
        */
        public int strangeQN() {return -3;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new Strange(),new Strange(),new Strange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiOmegaMinus();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiOmegaMinus);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Omega-");
        }
}

