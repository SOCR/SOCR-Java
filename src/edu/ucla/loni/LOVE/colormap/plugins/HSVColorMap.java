//$Id: HSVColorMap.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $

/**
 * HSVColorMap.java
 * Author: ErhFang Lee
 * LONI UCLA
 */
package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

import java.lang.String;
import java.awt.Color;


/**
 * This is a gray level colormap
 */
public class HSVColorMap extends ColorMap
{
    /**
     * Default constructor
     */
    public HSVColorMap() {
    }


    /**
     * Constructor
     */
    public HSVColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */
    public String getName() {
        return ("HSV Colormap");
    }

    /**
     * Actual set up of color map in range.
     * Under color and over color will be set up
     * by base class: ColorMap.java
     */
    protected void _setColorMap() {
        float increment = 1/(float)(_upperLimit - _lowerLimit);
        float elem = 0;
        // define HSV array
        float hue;
        float sat;
        float val;

        for (int i = _lowerLimit; i <= _upperLimit; i++) {
            hue = elem;
            sat = 1;
            val = 1;

            //Convert hue-saturation-value colors to red-green-blue.
            Color rgb = new Color(Color.HSBtoRGB(hue,sat,val));
            _rMap[i] = (byte)rgb.getRed();
            _gMap[i] = (byte)rgb.getGreen();
            _bMap[i] = (byte)rgb.getBlue();

            elem += increment ;
        }


    }


}

