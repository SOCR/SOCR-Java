package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing anti xi0.
* @version 1.5
* @author Mark Hale
*/
public final class AntiXiZero extends AntiXi {
        /**
        * Constructs an anti xi0.
        */
        public AntiXiZero() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1314.83
        */
        public double restMass() {return 1314.83;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return -1
        */
        public int isospinZ() {return -1;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new AntiUp(),new AntiStrange(),new AntiStrange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new XiZero();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof XiZero);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antixi0");
        }
}

