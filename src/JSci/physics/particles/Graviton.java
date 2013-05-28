package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing gravitons.
* @version 1.5
* @author Mark Hale
*/
public final class Graviton extends GaugeBoson {
        /**
        * Constructs a graviton.
        */
        public Graviton() {}
        /**
        * Returns the rest mass (MeV).
        * @return 0.0
        */
        public double restMass() {return 0.0;}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 4
        */
        public int spin() {return 4;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new Graviton();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof Graviton);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Graviton");
        }
}

