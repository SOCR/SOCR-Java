/**
 * SpectralColorMap.java
 * Author: ErhFang Lee
 * LONI UCLA
 */
package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

import java.lang.String;

/**
 * This is a Spectral colormap
 */
public class SpectralColorMap extends ColorMap
{
    // Spectral ColorMap lookup table defined in JIV
    private static final double[][] spectral_specification = {
        { 0.00, 0.0000,0.0000,0.0000 },
        { 0.05, 0.4667,0.0000,0.5333 },
        { 0.10, 0.5333,0.0000,0.6000 },
        { 0.15, 0.0000,0.0000,0.6667 },
        { 0.20, 0.0000,0.0000,0.8667 },
        { 0.25, 0.0000,0.4667,0.8667 },
        { 0.30, 0.0000,0.6000,0.8667 },
        { 0.35, 0.0000,0.6667,0.6667 },
        { 0.40, 0.0000,0.6667,0.5333 },
        { 0.45, 0.0000,0.6000,0.0000 },
        { 0.50, 0.0000,0.7333,0.0000 },
        { 0.55, 0.0000,0.8667,0.0000 },
        { 0.60, 0.0000,1.0000,0.0000 },
        { 0.65, 0.7333,1.0000,0.0000 },
        { 0.70, 0.9333,0.9333,0.0000 },
        { 0.75, 1.0000,0.8000,0.0000 },
        { 0.80, 1.0000,0.6000,0.0000 },
        { 0.85, 1.0000,0.0000,0.0000 },
        { 0.90, 0.8667,0.0000,0.0000 },
        { 0.95, 0.8000,0.0000,0.0000 },
        { 1.00, 0.8000,0.8000,0.8000 }
    };

    /**
     * Default constructor
     */
    public SpectralColorMap(){}

    /**
     * Constructor
     */
    public SpectralColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */
    public String getName() {
        return ("Spectral Colormap");
    }


    protected void _setColorMap() {
        int[] components= new int[ 3];
        int last_spec= spectral_specification.length - 1;
        final double step= 1.0 / last_spec;

        for (int pixel = _lowerLimit ; pixel <= _upperLimit; pixel++) {


            // Pixel is on the linear ramp

            if( _upperLimit == pixel) {
                for( int i= 0; i < 3; i++)
                    components[ i]=
                    (int)Math.round(255 * spectral_specification[ last_spec][ i+1]);
            }
            else {
                double fraction= (pixel-_lowerLimit) / (double)(_upperLimit - _lowerLimit );
                int interval= (int)Math.floor( fraction / step);
                // we're in: [ interval , interval+1 )
                double interpolator= (fraction - interval*step) / step; // [0,1)
                //System.out.println("Interval "+interval);
                for( int i= 0; i < 3; i++)
                    components[ i]=
                    (int)Math.round( 255 *
                    (spectral_specification[ interval][ i+1] *
                    (1.0-interpolator)
                    +
                    spectral_specification[ interval+1][ i+1] *
                    interpolator
                    )
                    );
            }

            _rMap[pixel] = (byte)components[ 0];
            _gMap[pixel] = (byte)components[ 1];
            _bMap[pixel] = (byte)components[ 2];

        }
    }



}


