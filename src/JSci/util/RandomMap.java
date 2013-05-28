package JSci.util;

import JSci.maths.*;

/**
* This class defines a random map.
*/
public final class RandomMap implements Mapping, ComplexMapping {
        private final double min,max;
        /**
        * A random map that generates numbers
        * between 0.0 and 1.0.
        */
        public final static RandomMap MAP=new RandomMap();

        /**
        * Constructs a random map with the range [0.0,1.0].
        */
        public RandomMap() {
                this(0.0,1.0);
        }
        /**
        * Constructs a random map with a specified range.
        * @param minimum smallest random number to generate
        * @param maximum largest random number to generate
        */
        public RandomMap(double minimum,double maximum) {
                min=minimum;
                max=maximum;
        }
        public double map(double x) {
                return (max-min)*Math.random()+min;
        }
        public Complex map(Complex z) {
                return new Complex(map(z.real()),map(z.imag()));
        }
        public Complex map(double real,double imag) {
                return new Complex(map(real),map(imag));
        }
}

