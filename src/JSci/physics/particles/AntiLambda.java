package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing antilambdas.
* @version 1.5
* @author Mark Hale
*/
public final class AntiLambda extends Hyperon {
        /**
        * Constructs an antilambda.
        */
        public AntiLambda() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1115.683
        */
        public double restMass() {return 1115.683;}
        /**
        * Returns the number of 1/2 units of spin.
        * @return 1
        */
        public int spin() {return 1;}
        /**
        * Returns the number of 1/2 units of isospin.
        * @return 0
        */
        public int isospin() {return 0;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 0
        */
        public int isospinZ() {return 0;}
        /**
        * Returns the electric charge.
        * @return 0
        */
        public int charge() {return 0;}
        /**
        * Returns the strangeness number.
        * @return 1
        */
        public int strangeQN() {return 1;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new AntiUp(),new AntiDown(),new AntiStrange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new Lambda();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof Lambda);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antilambda");
        }
}

