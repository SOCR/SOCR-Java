package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing xi+.
* @version 1.5
* @author Mark Hale
*/
public final class XiPlus extends AntiXi {
        /**
        * Constructs a xi+.
        */
        public XiPlus() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1321.31
        */
        public double restMass() {return 1321.31;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 1
        */
        public int isospinZ() {return 1;}
        /**
        * Returns the electric charge.
        * @return 1
        */
        public int charge() {return 1;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new AntiDown(),new AntiStrange(),new AntiStrange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new XiMinus();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof XiMinus);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Xi+");
        }
}

