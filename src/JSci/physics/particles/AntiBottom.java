package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antibottoms.
* @version 1.5
* @author Mark Hale
*/
public final class AntiBottom extends AntiQuark {
        /**
        * Constructs an antibottom.
        */
        public AntiBottom() {}
        /**
        * Returns the rest mass (MeV).
        * @return 4250.0
        */
        public double restMass() {return 4250.0;}
        /**
        * Returns the number of 1/3 units of electric charge.
        * @return 1
        */
        public int charge() {return 1;}
        /**
        * Returns the strangeness number.
        * @return 0
        */
        public int strangeQN() {return 0;}
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new Bottom();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof Bottom);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antibottom");
        }
}

