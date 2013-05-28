package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing ups.
* @version 1.5
* @author Mark Hale
*/
public final class Up extends Quark {
        /**
        * Constructs an up.
        */
        public Up() {}
        /**
        * Returns the rest mass (MeV).
        * @return 5.0
        */
        public double restMass() {return 5.0;}
        /**
        * Returns the number of 1/3 units of electric charge.
        * @return 2
        */
        public int charge() {return 2;}
        /**
        * Returns the strangeness number.
        * @return 0
        */
        public int strangeQN() {return 0;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiUp();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiUp);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Up");
        }
}

