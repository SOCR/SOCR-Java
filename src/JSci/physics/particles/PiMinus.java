package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing pi-.
* @version 1.5
* @author Mark Hale
*/
public final class PiMinus extends AntiPion {
        /**
        * Constructs a pi-.
        */
        public PiMinus() {}
        /**
        * Returns the rest mass (MeV).
        * @return 139.57018
        */
        public double restMass() {return 139.57018;}
        /**
        * Returns the electric charge.
        * @return -1
        */
        public int charge() {return -1;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return -2
        */
        public int isospinZ() {return -2;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new AntiUp(),new Down()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new PiPlus();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof PiPlus);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Pi-");
        }
}

