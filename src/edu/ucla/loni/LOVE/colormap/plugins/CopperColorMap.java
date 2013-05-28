//$Id: CopperColorMap.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $

/**
 * CopperColorMap.java
 * Author: Guogang Hu
 * LONI UCLA
 */
package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

/**
 * This is a gray level colormap
 */
public class CopperColorMap extends ColorMap
{
    /**
     * Default constructor
     */
    public CopperColorMap() {
    }


    /**
     * Constructor
     */
    public CopperColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */
    public String getName() {
        return ("Copper Colormap");
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
        double grayLevel = 0;
        int r, g, b;

        for (int i = _lowerLimit; i <= _upperLimit; i++) {
            grayLevel = slope * (i - _lowerLimit);
            r = Math.min((int)(grayLevel * 1.25), 255);
            g = Math.min((int)(grayLevel * 0.7812), 255);
            b = Math.min((int)(grayLevel * 0.4975), 255);

            _rMap[i] = (byte) r;
            _gMap[i] = (byte) g;
            _bMap[i] = (byte) b;
        }
    }
}
