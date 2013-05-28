package JSci.physics;

import JSci.maths.NumericalConstants;

/**
* A collection of fundamental physical constants.
* All values expressed in SI units.
* (Source: CODATA Bulletin No. 63 Nov 1986)
* @version 1.0
* @author Mark Hale
*/
public interface PhysicalConstants extends NumericalConstants {
        /**
        * Planck's constant.
        */
        double PLANCK=6.6260755E-34;
        /**
        * Planck's constant divided by 2*Pi (defined).
        */
        double H_BAR=PLANCK/TWO_PI;
        /**
        * Speed of light in vacuo (exact).
        */
        double SPEED_OF_LIGHT=2.99792458E+8;
        /**
        * Permeability constant (exact).
        */
        double PERMEABILITY=Math.PI*4E-7;
        /**
        * Permittivity constant (defined).
        */
        double PERMITTIVITY=1.0/(PERMEABILITY*SPEED_OF_LIGHT*SPEED_OF_LIGHT);
        /**
        * Gravitational constant.
        */
        double GRAVITATION=6.67259E-11;
        /**
        * Elementary charge.
        */
        double CHARGE=1.60217733E-19;
        /**
        * Electron rest mass.
        */
        double ELECTRON_MASS=9.1093897E-31;
        /**
        * Proton rest mass.
        */
        double PROTON_MASS=1.6726231E-27;
        /**
        * Neutron rest mass.
        */
        double NEUTRON_MASS=1.6749286E-27;
        /**
        * Avogadro's constant.
        */
        double AVOGADRO=6.0221367E+23;
        /**
        * Molar gas constant.
        */
        double MOLAR_GAS=8.314510;
        /**
        * Boltzmann's constant (defined).
        */
        double BOLTZMANN=MOLAR_GAS/AVOGADRO;
        /**
        * Stefan-Boltzmann constant.
        */
        double STEFAN_BOLTZMANN=5.67051E-8;
        /**
        * Rydberg constant (defined).
        */
        double RYDBERG=SPEED_OF_LIGHT*PERMEABILITY*SPEED_OF_LIGHT*PERMEABILITY*SPEED_OF_LIGHT*ELECTRON_MASS*CHARGE*CHARGE/PLANCK*CHARGE/PLANCK*CHARGE/PLANCK/8.0;
        /**
        * Fine structure constant (defined).
        */
        double FINE_STRUCTURE=PERMEABILITY*SPEED_OF_LIGHT*CHARGE/PLANCK*CHARGE/2.0;
        /**
        * Faraday constant.
        */
        double FARADAY=96485.309;
        /**
        * Magnetic flux quantum (defined).
        */
        double FLUX_QUANTUM=PLANCK/(2.0*CHARGE);
        /**
        * Bohr magneton (defined).
        */
        double BOHR_MAGNETON=CHARGE/ELECTRON_MASS*H_BAR/2.0;
        /**
        * Magnetic moment of electron.
        */
        double ELECTRON_MOMENT=9.2847701E-24;
        /**
        * Magnetic moment of proton.
        */
        double PROTON_MOMENT=1.41060761E-26;
}

