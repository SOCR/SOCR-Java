package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing W+.
* @version 1.5
* @author Mark Hale
*/
public final class WPlus extends GaugeBoson {
        /**
        * Constructs a W+.
        */
        public WPlus() {}
        /**
        * Returns the rest mass (MeV).
        * @return 80419.0
        */
        public double restMass() {return 80419.0;}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 2
        */
        public int spin() {return 2;}
        /**
        * Returns the electric charge.
        * @return 1
        */
        public int charge() {return 1;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new WPlus();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof WPlus);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("W+");
        }
}

