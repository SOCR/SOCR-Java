/**
 * CoolColorMap.java
 * Author: ErhFang Lee
 * LONI UCLA
 */
package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

import java.lang.String;

/**
 * This is a gray level colormap
 */
public class CoolColorMap extends ColorMap
{
    /**
     * Default constructor
     */
    public CoolColorMap() {
    }


    /**
     * Constructor
     */
    public CoolColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */
    public String getName() {
        return ("Cool Colormap");
    }

    /**
     * Cool Color Map defined in Matlab.
     */
    protected void _setColorMap() {
        double slope;

        slope = 255.0/(double)(_upperLimit - _lowerLimit );

        // Fill the colormap in range with the mapped values
        int grayLevel = 0;

        for (int i = _lowerLimit; i <= _upperLimit; i++) {
            _rMap[i] = (byte)grayLevel;
            _gMap[i] = (byte)(255 - grayLevel);
            _bMap[i] = (byte)255;

            grayLevel += slope;
        }
    }
}

