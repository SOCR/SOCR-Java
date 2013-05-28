package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing anti sigma-.
* @version 1.5
* @author Mark Hale
*/
public final class AntiSigmaMinus extends AntiSigma {
        /**
        * Constructs an anti sigma-.
        */
        public AntiSigmaMinus() {}
        /**
        * Returns the rest mass (MeV).
        * @return 1197.449
        */
        public double restMass() {return 1197.449;}
        /**
        * Returns the number of 1/2 units of the z-component of isospin.
        * @return 2
        */
        public int isospinZ() {return 2;}
        /**
        * Returns the electric charge.
        * @return 1
        */
        public int charge() {return 1;}
        /**
        * Returns the quark composition.
        */
        public QuantumParticle[] quarks() {
                QuantumParticle comp[]={new AntiDown(),new AntiDown(),new AntiStrange()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new SigmaMinus();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof SigmaMinus);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Antisigma-");
        }
}

