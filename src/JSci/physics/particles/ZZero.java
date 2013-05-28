package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing Z0.
* @version 1.5
* @author Mark Hale
*/
public final class ZZero extends GaugeBoson {
        /**
        * Constructs a Z0.
        */
        public ZZero() {}
        /**
        * Returns the rest mass (MeV).
        * @return 91188.2
        */
        public double restMass() {return 91188.2;}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 2
        */
        public int spin() {return 2;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new ZZero();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof ZZero);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Z0");
        }
}

