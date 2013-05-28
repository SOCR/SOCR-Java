package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing charms.
* @version 1.5
* @author Mark Hale
*/
public final class Charm extends Quark {
        /**
        * Constructs a charm.
        */
        public Charm() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1500.0
        */
        public double restMass() {return 1500.0;}
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
                return new AntiCharm();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiCharm);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Charm");
        }
}

