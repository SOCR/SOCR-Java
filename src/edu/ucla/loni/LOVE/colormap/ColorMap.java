//$Id: ColorMap.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $

/**
 * ColorMap.java
 * Author: Guogang Hu
 * LONI UCLA
 */
package edu.ucla.loni.LOVE.colormap;

import java.lang.String;
import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;

/**
 * Base class for colormap, also provides all constants for colormap.
 * The <code>ColorMap</code> class is colormap for
 * indexed gray image. Given bits used per pixel, and
 * upper and lower limits. User can get an IndexColorModel
 * by calling <code> getColorModel()</code>  method.
 *
 * More advanced user can use <code> getRedMap, getGreenMap,
 * getBlueMap </code> to get those arrays directly.
 *
 * "Under Color" The color for intensity less than lower limit.
 *
 * "Over Color"  The color for intensity that is higher than upper limit.
 *
 * See the figure for a illustration of the concept.
 * <pre>
 * .                          Upper Limit
 * .          ^
 * .     255  |               |***********
 * .          |              *
 * .          |             *
 * .          |            *
 * .          |           *
 * .          |          *
 * .          |         *
 * .       0  *********|------------------->
 * .          0        Lower Limit        4095
 * </pre>
 */

public abstract class ColorMap {
    //constants of colormaps.

    /** Gray colormap */
    public static final int GRAY = 0;

    /** Hotmetal colormap */
    public static final int HOTMETAL = 1;

    /** Spectral colormap */
    public static final int SPECTRAL = 2;

    /** Red colormap */
    public static final int RED = 3;

    /** Green colormap */
    public static final int GREEN = 4;

    /** Blue colormap */
    public static final int BLUE = 5;

    /** Bone colormap */
    public static final int BONE = 6;

    /** Cool colormap */
    public static final int COOL = 7;

    /** Copper colormap */
    public static final int COPPER = 8;

    /** Flag colormap */
    public static final int FLAG = 9;

    /** Hot colormap */
    public static final int HOT = 10;

    /** HSV colormap */
    public static final int HSV = 11;

    /** Pink colormap */
    public static final int PINK = 12;

    /** Special Spectral colormap */
    public static final int SPECIAL_SPECTRAL = 12;

	/**
	 * under color
	 *
	 * @uml.property name="_underColor"
	 */
	protected Color _underColor;

	/**
	 * over color
	 *
	 * @uml.property name="_overColor"
	 */
	protected Color _overColor;

	/**
	 * Map of red color
	 *
	 * @uml.property name="_rMap"
	 */
	protected byte[] _rMap = null;

	/**
	 * Map of green color
	 *
	 * @uml.property name="_gMap"
	 */
	protected byte[] _gMap = null;

	/**
	 * Map of blue color
	 *
	 * @uml.property name="_bMap"
	 */
	protected byte[] _bMap = null;

	/**
	 * Map of alpha component
	 *
	 * @uml.property name="_aMap"
	 */
	protected byte[] _aMap = null;

	/**
	 * upper limit, exclusive [lower, upper)
	 *
	 * @uml.property name="_upperLimit"
	 */
	protected int _upperLimit;

	/**
	 * lower limit, inclusive [lower, upper)
	 *
	 * @uml.property name="_lowerLimit"
	 */
	protected int _lowerLimit;

	/**
	 * Total number of levels
	 *
	 * @uml.property name="_size"
	 */
	protected int _size;


    /** Number of bits used for each pixel */
    protected int _bits;

	/**
	 * Alpha value for this colormap
	 *
	 * @uml.property name="_alpha"
	 */
	protected byte _alpha;

	/**
	 * Flag to indicate if this is a inverted colormap.
	 *
	 * @uml.property name="_isInverted"
	 */
	protected boolean _isInverted;



    /**
     * Default constructor
     * Required for plugin usage. If you call this constructor.
     * Then you must call initializeColorMap(size, bits) immediately
     * after constructor.
     */
    public ColorMap() {
    }

    /**
     * Constructor
     */
    public ColorMap(int size, int bits) {
        initializeColorMap(size, bits);
    }

    /**
     * Initialize this colormap. Only need to be called once.
     *
     * @param size  Size of the colormap.
     * @param bits  Bits of each pixel.
     */
    public void initializeColorMap(int size, int bits) {
        //set size of the colormap, allocate memory
        _setSize(size, bits);

        //setup default under color and over color

        //default under color: transparent
        setUnderColor(new Color(0,0,0,0));
        //default over color: opaque white;
        setOverColor(new Color(255, 255, 255, 255));

        //setup default opaque settings
        _alpha = (byte) 127; //default alpha value
        setLimits(0, _size - 1);
    }

    /**
     * Set the size and bits. Allocate memory.
     * Typically, this function will only be called when
     * the class is initialized.
     */
    private void _setSize(int size, int bits) {
        _size = size;
        _bits = bits;

        //allocate memory for each map
        _rMap = new byte[_size];
        _gMap = new byte[_size];
        _bMap = new byte[_size];
        _aMap = new byte[_size];
    }

	/**
	 * Get the size of the colormap.
	 *
	 * @return Size of this colormap
	 *
	 * @uml.property name="_size"
	 */
	public int getSize() {
		return _size;
	}

	/**
	 * Gets the upper limit.
	 *
	 * @return Upper limit of the colormap.
	 *
	 * @uml.property name="_upperLimit"
	 */
	public int getUpperLimit() {
		return _upperLimit;
	}

	/**
	 * Gets the lower limit.
	 *
	 * @return Lower limit of the colormap
	 *
	 * @uml.property name="_lowerLimit"
	 */
	public int getLowerLimit() {
		return _lowerLimit;
	}


    /**
     * Get a colormodel of this give colormap
     *
     * @return  An IndexColorModel object of this colormap
     */
    public ColorModel getColorModel() {
        //calculate the colormap
        IndexColorModel cm = new IndexColorModel(_bits, _size,
        _rMap, _gMap,
        _bMap, _aMap);
        //System.out.println(cm.getTransparency());
        return cm;
        //   return (new IndexColorModel(_bits, _size,
        //                              _rMap, _gMap,
        //                              _bMap, _aMap));
    }

	/**
	 * Get red map
	 *
	 * @return Red component of this colormap.
	 *
	 * @uml.property name="_rMap"
	 */
	public byte[] getRedMap() {
		return _rMap;
	}

	/**
	 * Get green map
	 *
	 * @return Green component of this colormap.
	 *
	 * @uml.property name="_gMap"
	 */
	public byte[] getGreenMap() {
		return _gMap;
	}

	/**
	 * Get blue map
	 *
	 * @return Blue component of this colormap.
	 *
	 * @uml.property name="_bMap"
	 */
	public byte[] getBlueMap() {
		return _bMap;
	}

	/**
	 * Get alpha map
	 *
	 * @return Alpha component of this colormap.
	 *
	 * @uml.property name="_aMap"
	 */
	public byte[] getAlphaMap() {
		return _aMap;
	}

	/**
	 * Set the alpha value of this map
	 *
	 * @param alpha alpha value
	 *
	 * @uml.property name="_alpha"
	 */
	public void setAlpha(byte alpha) {
		int i;

		//System.out.println("Alpha in colormap" + alpha);
		_alpha = alpha;

		//update the alpha map

		//set the under color if necessary
		if (_lowerLimit >= 0) {
			for (i = 0; i <= _lowerLimit; i++) {
				//System.out.println("Lower i="+i);
				//_aMap[i] = (byte) 0;
				_aMap[i] = _alpha;
			}
		}

		//set the alpha value in range
		for (i = _lowerLimit + 1; i <= _upperLimit; i++) {
			//System.out.println("In range I="+i);
			_aMap[i] = _alpha;
		}

		//set the over color if necessary
		if (_upperLimit < _size - 1) {
			for (i = _upperLimit + 1; i < _size; i++) {
				//System.out.println("i="+i);

				//_aMap[i] = (byte) 0; //over color will be transparent
				_aMap[i] = _alpha;
			}
		}
		//test
		_aMap[0] = 0;
	}

	/**
	 * Get the current alpha value
	 *
	 * @return alpha value
	 *
	 * @uml.property name="_alpha"
	 */
	public byte getAlpha() {
		return _alpha;
	}

	/**
	 * Get over color.
	 *
	 * @return over color
	 *
	 * @uml.property name="_overColor"
	 */
	public Color getOverColor() {
		return _overColor;
	}

	/**
	 * Set over color.
	 *
	 * @param overColor over color
	 *
	 * @uml.property name="_overColor"
	 */
	public void setOverColor(Color overColor) {
		int i;
		_overColor = overColor;

		//set the over color if necessary
		if (_upperLimit < _size - 1) {
			for (i = _upperLimit + 1; i < _size; i++) {
				_rMap[i] = (byte) _overColor.getRed();
				_gMap[i] = (byte) _overColor.getGreen();
				_bMap[i] = (byte) _overColor.getBlue();
			}
		}
	}

	/**
	 * Get under color.
	 *
	 * @return under color
	 *
	 * @uml.property name="_underColor"
	 */
	public Color getUnderColor() {
		return _underColor;
	}

	/**
	 * Set under color.
	 *
	 * @param underColor under color
	 *
	 * @uml.property name="_underColor"
	 */
	public void setUnderColor(Color underColor) {
		int i;

		_underColor = underColor;

		//set the under color if necessary
		if (_lowerLimit > 0) {
			for (i = 0; i <= _lowerLimit; i++) {
				_rMap[i] = (byte) _underColor.getRed();
				_gMap[i] = (byte) _underColor.getGreen();
				_bMap[i] = (byte) _underColor.getBlue();
			}
		}
	}


    /**
     * Set the upper limit.
     *
     * @param upperLimit
     *
     * @return True if the limits are valid, false if not.
     */
    public boolean setUpperLimit(int upperLimit) {
        //System.out.println("ColorMap::Using setUpperLimit Method");
        return setLimits(_lowerLimit, upperLimit);
    }

    /**
     * Set the lower limit.
     *
     * @param lowerLimit Lower limit.
     *
     * @return True if the limits are valid, false if not.
     */
    public boolean setLowerLimit(int lowerLimit) {
        return setLimits(lowerLimit, _upperLimit);
    }

    /**
     * Set the upper limit and lower limit of this colormap,
     * Nothing will be changed if the input is invalid.
     *
     * @param lowerLimit  window of this colormap
     * @param upperLimit   level of this colormap
     *
     * @return True if the limits are valid, false if not.
     */
    public boolean setLimits(int lowerLimit, int upperLimit) {
        int i;

        // Validate the parameter settings
        if (lowerLimit < 0 || upperLimit > _size ||

        upperLimit <= lowerLimit) {
            //invalid limit setting
            return false;
        }

        _upperLimit = upperLimit;
        _lowerLimit = lowerLimit;

        //set the color in range
        _setColorMap();
        invertColorMap();

        //set the alpha map
        setAlpha(_alpha);

        //setup the "under color" and "over color"
        //setUnderColor(_underColor);
        //setOverColor(_overColor);
        //System.out.println((int)(_rMap[_lowerLimit]&0xff));
        //System.out.println((int)(_rMap[_upperLimit]&0xff));
        setUnderColor(new Color(_rMap[_lowerLimit]&0xff,
        _gMap[_lowerLimit]&0xff,
        _bMap[_lowerLimit]&0xff));
        setOverColor(new Color(_rMap[_upperLimit]&0xff,
        _gMap[_upperLimit]&0xff,
        _bMap[_upperLimit]&0xff));

        return true;
    }

	/**
	 * set the invert flag.
	 *
	 * @uml.property name="_isInverted"
	 */
	public void setInverted(boolean flag) {
		//System.out.println("BoundedColorMap.java set " +flag);
		_isInverted = flag;
		_setColorMap();
		invertColorMap();
		setAlpha(_alpha);

	}


    public void invertColorMap() {
        //System.out.println(_isInverted);

        if(_isInverted) {
            for (int i = _lowerLimit; i <= _upperLimit; i++) {
                //System.out.println("Inverting--->"+_rMap[i]);
                _rMap[i] = (byte) (_rMap[i]^0xFF);
                _gMap[i] = (byte) (_gMap[i]^0xFF);
                _bMap[i] = (byte) (_bMap[i]^0xFF);
                //System.out.println("To--->"+_rMap[i]);
            }
        }
    }

	/**
	 * iget the invert flag.
	 *
	 * @uml.property name="_isInverted"
	 */
	public boolean getInverted() {
		return _isInverted;
	}


    /**
     * Get a String representation of the colormap
     *
     * @return String representation of the colormap
     */
    public String toString() {
        return(_size+" "+_bits);
    }

	/**
	 * Get the names of the actual color map
	 *
	 * @uml.property name="name"
	 */
	public abstract String getName();


    /**
     * Actual set up of color map in range.
     *
     */
    protected abstract void _setColorMap();




}


