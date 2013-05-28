package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing protons.
* @version 1.5
* @author Mark Hale
*/
public final class Proton extends Nucleon {
        /**
        * Constructs a proton.
        */
        public Proton() {}
        /**
        * Returns the rest mass (MeV).
        * @return 938.27231
        */
        public double restMass() {return 938.27231;}
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
                QuantumParticle comp[]={new Up(),new Up(),new Down()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiProton();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiProton);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Proton");
        }
}

