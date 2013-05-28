package JSci.chemistry;

import JSci.physics.*;
import JSci.physics.particles.*;

/**
* A class representing atoms.
* @version 0.1
* @author Mark Hale
*/
public class Atom extends Particle {
        /**
        * Shell.
        */
        public Lepton shell[];
        /**
        * Nucleus.
        */
        public Nucleon nucleus[];
        /**
        * Constructs an atom.
        */
        public Atom(Element e) {
                int i;
                shell=new Lepton[e.getAtomicNumber()];
                nucleus=new Nucleon[e.getMassNumber()];
                for(i=0;i<shell.length;i++) {
                        shell[i]=new Electron();
                        nucleus[i]=new Proton();
                }
                for(;i<nucleus.length;i++)
                        nucleus[i]=new Neutron();
        }
        /**
        * Combine two atoms together.
        */
        public Molecule combine(Atom a) {
                return new Molecule(this,a);
        }
}

