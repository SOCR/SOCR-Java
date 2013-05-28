/**
 * ThreeColorMap.java
 * Author: Shin, Bae Cheol
 * LONI UCLA
 */

package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;


public class ThreeColorMap extends ColorMap
{

    /**
     * Default constructor
     */
    public ThreeColorMap() {
       _lowerLimit = 74;
       _upperLimit = 186;
   }


    /**
     * Constructor
     */
    public ThreeColorMap(int leftCut, int rightCut) {
        _lowerLimit = leftCut;
	_upperLimit = rightCut;

    }

    /**
     * Get the names of the current map
     */
    public String getName() {
        return ("Three Colormap");
    }

    /**
     * Actual set up of color map in range.
     * Under color and over color will be set up
     * by base class: ColorMap.java
     */
    protected void _setColorMap() {

        // Fill the colormap in range with the mapped values

        for(int i = 0; i<= _lowerLimit; i++){
                _rMap[i] =  (byte) 255;
                _gMap[i] = 0;
                _bMap[i] = 0;
        }
        for(int j= _lowerLimit+1; j< _upperLimit; j++){

                _rMap[j] = 0;
                _gMap[j] = (byte) 255;
                _bMap[j] = 0;
        }
        for(int k=_upperLimit; k<256; k++){

                _rMap[k] = 0;
                _gMap[k] = 0;
                _bMap[k] =  (byte) 255;
      }
    }
 }
