//$Id: BoneColorMap.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $

/**
 * BoneColorMap.java
 * Author: ErhFang Lee
 * LONI UCLA
 */
//package myclasses.colormap;
package edu.ucla.loni.LOVE.colormap.plugins;

import edu.ucla.loni.LOVE.colormap.ColorMap;

/**
 * This is a bone colormap
 */
public class BoneColorMap extends ColorMap
{

    /**
     * Default constructor
     */
    public BoneColorMap(){}

    /**
     * Constructor
     */
    public BoneColorMap(int size, int bits) {
        super(size, bits);
    }

    /**
     * Get the names of the current map
     */

    public String getName() {
        return ("Bone Colormap");
    }


    // Bone Color Map defined in Matlab

    protected void _setColorMap(){
        //bone = (7*gray + fliplr(hot))/8
        double  window = _upperLimit - _lowerLimit ;

        // fliplr(hot)
        int red_start = _lowerLimit+ (int)( 0.75 * window + 0.5) ;
        int red_end =  _upperLimit  ;
        int green_start = _lowerLimit + (int)(0.375* window  + 0.5) ;
        int green_end = _lowerLimit + (int)(0.75* window ) ;
        int blue_start = _lowerLimit ;
        int blue_end =  _lowerLimit +(int)(0.375 * window ) ;

        //(7*gray + fliplr(hot))/8
        _compute_bone( _lowerLimit, _upperLimit, red_start, red_end, _rMap);
        _compute_bone( _lowerLimit, _upperLimit, green_start, green_end, _gMap);
        _compute_bone( _lowerLimit, _upperLimit, blue_start, blue_end , _bMap);
    }


    private static final void _compute_bone( final int range_min,
    final int range_max,
    final int start,
    final int end,
    byte[] destination ) {

        double slope;
        slope = 255.0/(double)( range_max - range_min );
        int grayLevel = 0;

        int i= range_min ;
        //////////////////////////////////////////////////////////
        // Changed by hgg, 12-21-2001
        // Gray level calculation precision improved
        while ( i <= range_max ) {
            if( i < start)
                destination[ i]= (byte) ((0 +7*grayLevel)/8) ;
            else if ( i >= start && i <= end) {
                if( start == end)
                    destination[ start]= (byte)( 127 + 7*grayLevel );
                else {
                    final int end_minus_start= end - start ;
                    destination[ i]= (byte)(( (255 * (i - start) / end_minus_start)+
                    7*grayLevel)/8);
                }
            }
            else
                destination[i]= (byte)((255 + 7*grayLevel)/8);

            i++;
            grayLevel =(int) (slope * (i - range_min));
            //System.out.println("gray level:" + grayLevel);
            //System.out.println("intensity: " + destination[i-1]);
        }
    }

}
