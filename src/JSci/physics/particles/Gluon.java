package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing gluons.
* @version 1.5
* @author Mark Hale
*/
public final class Gluon extends GaugeBoson {
        public final static int RED_ANTIGREEN=12;
        public final static int RED_ANTIBLUE=13;
        public final static int GREEN_ANTIRED=21;
        public final static int GREEN_ANTIBLUE=23;
        public final static int BLUE_ANTIRED=31;
        public final static int BLUE_ANTIGREEN=32;
        /**
        * The state (rr~-gg~)/sqrt(2).
        */
        public final static int MIXED_RED_GREEN=11-22;
        /**
        * The state (rr~+gg~-2bb~)/sqrt(6).
        */
        public final static int MIXED_RED_GREEN_2BLUE=11+22-2*(33);
        /**
        * Constructs a gluon.
        */
        public Gluon() {}
        /**
        * The color.
        */
        public int color;
        /**
        * Returns the rest mass (MeV).
        * @return 0.0
        */
        public double restMass() {return 0.0;}
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
                return new Gluon();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof Gluon);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Gluon");
        }
}

