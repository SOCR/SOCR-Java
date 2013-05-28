package JSci.physics.particles;

import JSci.physics.quantum.QuantumParticle;

/**
* A class representing neutrons.
* @version 1.5
* @author Mark Hale
*/
public final class Neutron extends Nucleon {
        /**
        * Constructs a neutron.
        */
        public Neutron() {}
        /**
        * Returns the rest mass (MeV).
        * @return 939.56563
        */
        public double restMass() {return 939.56563;}
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
                QuantumParticle comp[]={new Up(),new Down(),new Down()};
                return comp;
        }
        /**
        * Returns the antiparticle of this particle.
        */
        public QuantumParticle anti() {
                return new AntiNeutron();
        }
        /**
        * Returns true if qp is the antiparticle.
        */
        public boolean isAnti(QuantumParticle qp) {
                return (qp!=null) && (qp instanceof AntiNeutron);
        }
        /**
        * Returns a string representing this class.
        */
        public String toString() {
                return new String("Neutron");
        }
}

