//$Id: HotmetalColorMap.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $

/**
 * HotmetalColorMap.java
 * Author: ErhFang Lee
 * LONI UCLA
 */
package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

import java.lang.String;

/**
 * This is a hotmetal colormap
 */
public class HotmetalColorMap extends ColorMap
{

    /**
     * Default constructor
     */
    public HotmetalColorMap(){}

    /**
     * Constructor
     */
    public HotmetalColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */

    public String getName() {
        return ("Hotmetal Colormap");
    }

    protected void _setColorMap(){
        //System.out.println("_setColorMap");
        //System.out.println(_upperLimit);
        //System.out.println(_lowerLimit);

        double  window = _upperLimit - _lowerLimit;

        // set hotmetal_lookup
        int red_start =  _lowerLimit;
        int red_end = _lowerLimit + (int)( 0.5 * window );
        int green_start = _lowerLimit + (int)(0.25* window  + 0.5) ;
        int green_end = _lowerLimit + (int)(0.75* window ) ;
        int blue_start = _lowerLimit + (int)(0.5* window + 0.5 ) ;
        int blue_end =  _upperLimit ;

        //System.out.println(red_start);
        //System.out.println(green_start);
        //System.out.println(blue_start);

        //System.out.println(red_end);
        //System.out.println(green_end);
        //System.out.println(blue_end);
        // Hotmetal Color Map defined in JIV

        _compute_linear_ramp( _lowerLimit, _upperLimit, red_start, red_end, _rMap);
        _compute_linear_ramp( _lowerLimit, _upperLimit, green_start, green_end, _gMap);
        _compute_linear_ramp( _lowerLimit, _upperLimit, blue_start, blue_end , _bMap);

        //System.out.println("Color of upper limit");
        //System.out.println(_rMap[_upperLimit]);
        //System.out.println(_gMap[_upperLimit]);
        //System.out.println(_bMap[_upperLimit]);



    }





    private static final void _compute_linear_ramp( final int range_min,
    final int range_max,
    final int start,
    final int end,
    byte[] destination ) {
        int i;
        for( i= range_min; i < start; i++)
            destination[ i]= (byte)0;

        if( start == end)
            destination[ start]= (byte)127;
        else {
            final int end_minus_start= end - start;
            for( i= start; i <= end; i++)
                destination[ i]= (byte)( 255 * (i - start) / end_minus_start);
        }

        for( i= end+1; i <= range_max ; i++) {
            destination[ i]= (byte)255;
        }
    }
}
