/**
 * HotColorMap.java
 * Author: ErhFang Lee
 * LONI UCLA
 */


package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

import java.lang.String;


/**
 * This is a hot colormap
 */
public class HotColorMap extends ColorMap
{

    /**
     * Default constructor
     */
    public HotColorMap(){}

    /**
     * Constructor
     */
    public HotColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */

    public String getName() {
        return ("Hot Colormap");
    }

    protected void _setColorMap(){

        // System.out.println("-------------------------");
        //System.out.println("upper limit " +_upperLimit + "  lower limit " + _lowerLimit);

        double  window = 3*(_upperLimit - _lowerLimit+1)/8  ;

        // set hot color map
        int red_start =  _lowerLimit ;
        int red_end = _lowerLimit +(int)(window) ;

        int green_start = _lowerLimit + (int)( window ) ;
        int green_end = _lowerLimit + (int)(2* window) ;

        int blue_start = _lowerLimit+ (int)( 2* window ) ;
        int blue_end = _upperLimit ;



        // Hot Color Map defined in Matlab
        //System.out.println("red start " + red_start + "  red end " + red_end);
        _compute_linear_ramp( _lowerLimit, _upperLimit, red_start, red_end, _rMap);

        //System.out.println("green start " + green_start + "  green end " + green_end);
        _compute_linear_ramp( _lowerLimit, _upperLimit, green_start, green_end, _gMap);

        //System.out.println("blue start " + blue_start + "  blue end " + blue_end);
        _compute_linear_ramp( _lowerLimit, _upperLimit, blue_start, blue_end , _bMap);


        //System.out.println("red upper " + _rMap[_upperLimit]+"  lower " + _rMap[_lowerLimit]);
        //System.out.println("green upper " + _gMap[_upperLimit]+ "  lower " + _gMap[_lowerLimit]);
        //System.out.println("blue upper " + _bMap[_upperLimit]+"  lower " + _bMap[_lowerLimit]);
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
            destination[ start]= (byte)0;
        else {
            final int end_minus_start= end - start ;
            for( i= start; i <= end; i++)
                destination[ i]= (byte)( 255 * (i - start ) / end_minus_start);
        }

        for( i= end; i <=  range_max ; i++)
            destination[ i]= (byte)255;


    }
}
