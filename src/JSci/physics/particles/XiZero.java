package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing xi0.
* @version 1.5
* @author Mark Hale
*/
public final class XiZero extends Xi {
        /**
        * Constructs a xi0.
        */
        public XiZero() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1314.83
        */
        public double restMass() {return 1314.83;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 1
        */
        public int isospinZ() {return 1;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new Up(),new Strange(),new Strange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiXiZero();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiXiZero);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Xi0");
        }
}

