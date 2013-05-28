package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing downs.
* @version 1.5
* @author Mark Hale
*/
public final class Down extends Quark {
        /**
        * Constructs a down.
        */
        public Down() {}
        /**
        * Returns the rest mass (MeV).
        * @return 8.0
        */
        public double restMass() {return 8.0;}
        /**
        * Returns the number of 1/3 units of electric charge.
        * @return -1
        */
        public int charge() {return -1;}
        /**
        * Returns the strangeness number.
        * @return 0
        */
        public int strangeQN() {return 0;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiDown();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiDown);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Down");
        }
}

