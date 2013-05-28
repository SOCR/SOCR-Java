package JSci.chemistry;

import JSci.physics.*;

/**
* A class representing molecules.
* @version 0.1
* @author Mark Hale
*/
public class Molecule extends Particle {
        /**
        * Atoms.
        */
        public Atom atoms[];
        /**
        * Constructs a molecule.
        */
        public Molecule(Molecule m,Atom a) {
                atoms=new Atom[m.atoms.length+1];
                for(int i=0;i<m.atoms.length;i++)
                        atoms[i]=m.atoms[i];
                atoms[m.atoms.length]=a;
        }
        /**
        * Constructs a molecule.
        */
        public Molecule(Atom a,Atom b) {
                atoms=new Atom[2];
                atoms[0]=a;
                atoms[1]=b;
        }
        /**
        * Combine with an atom.
        */
        public Molecule combine(Atom a) {
                return new Molecule(this,a);
        }
}

