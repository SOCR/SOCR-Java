/**
 * BlueColorMap.java
 * Author: Guogang Hu
 * LONI UCLA
 */
package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

/**
 * This is a gray level colormap
 */
public class BlueColorMap extends ColorMap
{
    /**
     * Default constructor
     */
    public BlueColorMap() {
    }


    /**
     * Constructor
     */
    public BlueColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */
    public String getName() {
        return ("Blue Colormap");
    }

    /**
     * Actual set up of color map in range.
     * Under color and over color will be set up
     * by base class: ColorMap.java
     */
    protected void _setColorMap() {
        double slope;

        slope = 255.0/(double)(_upperLimit - _lowerLimit);

        // Fill the colormap in range with the mapped values
        byte grayLevel = 0;

        for (int i = _lowerLimit; i <= _upperLimit; i++) {
            grayLevel = (byte) (slope * (i - _lowerLimit));

            _rMap[i] = 0;
            _gMap[i] = 0;
            _bMap[i] = grayLevel;
        }
    }
}

