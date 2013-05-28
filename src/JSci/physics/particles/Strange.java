package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing stranges.
* @version 1.5
* @author Mark Hale
*/
public final class Strange extends Quark {
        /**
        * Constructs a strange.
        */
        public Strange() {}
        /**
        * Returns the rest mass (MeV).
        * @return 160.0
        */
        public double restMass() {return 160.0;}
        /**
        * Returns the number of 1/3 units of electric charge.
        * @return -1
        */
        public int charge() {return -1;}
        /**
        * Returns the strangeness number.
        * @return -1
        */
        public int strangeQN() {return -1;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiStrange();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiStrange);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Strange");
        }
}

