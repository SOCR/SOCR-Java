//$Id
package edu.ucla.loni.LOVE.colormap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/*
 *   ColorBar.java
 *
 *   A class to draw color bar of desired color map.
 *
 *   Authour: ErhFang Lee
 *   Data: 09/20/01
 *
 */


 public class ColorBar extends JPanel {

	/**
	 * Color map of color bar
	 *
	 * @uml.property name="_colormap"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected BoundedColorMap _colormap;



     /** HORIZONTAL/VERTICAL */
     private int _type;

     public final static int HORIZONTAL = 0;
     public final static int VERTICAL = 1;

	/**
	 * width of the color bar
	 *
	 * @uml.property name="_height"
	 */
	private int _height;

	/**
	 * length of the bar
	 *
	 * @uml.property name="_length"
	 */
	private int _length;


     /**  Default constructor.*/
     public ColorBar(){
	 this(null, 0, 18, 256);
     }

     /** Constructor */
     public ColorBar(BoundedColorMap colormap, int type)
     {
	 this(colormap, type, 18, 256);
     }


     /** Constructor */
     public ColorBar(BoundedColorMap colormap, int type,
		     int height, int length)
     {
	 _colormap = colormap;
	 _type = type;
	 _length = length;
	 _height = height;

	 if ( _type == HORIZONTAL)
	     setSize(_length, _height);
	 else
	     setSize(_height, _length);

	 setBorder(BorderFactory.createLineBorder(Color.black));

     }

     public void paintComponent(Graphics g)
     {
	 super.paintComponent(g);
	 g.drawImage(getMapImage(_length),0,0,getWidth(), getHeight(), this);

     }


     //return image of the color map
     public BufferedImage getMapImage( int width )
     {
	 /*Creat color bufferimage */

	 BufferedImage buffer;
	 if (_type == HORIZONTAL)
	     buffer = new BufferedImage( _colormap.getSize(), 1,
					 BufferedImage.TYPE_INT_ARGB );
	 else
	     buffer = new BufferedImage(1,_colormap.getSize(),
					BufferedImage.TYPE_INT_ARGB );


	 byte red[] = _colormap.getRedMap();
	 byte green[] = _colormap.getGreenMap();
	 byte blue[] = _colormap.getBlueMap();
	 byte alpha[] = _colormap.getAlphaMap();


	 for  ( int pixelL = 0;  pixelL <_colormap.getSize(); pixelL ++ )
	 {
	     if ( _type ==  HORIZONTAL)
		 buffer.setRGB( pixelL, 0, getMapARGB(alpha[pixelL], red[pixelL], green[pixelL], blue[pixelL]) );

	     else
		 buffer.setRGB( 0, pixelL, getMapARGB(alpha[pixelL], red[pixelL], green[pixelL], blue[pixelL]) );
	 }

	 return buffer;
     }



     private int getMapARGB (byte a,  byte r, byte g, byte b)
     {
	 return ((int)(((a&0xff) << 24 ) | ((r&0xff) << 16) | ((g&0xff) << 8) | (b&0xff)));
     }


    public Dimension getPreferredSize()
    {
	if ( _type == HORIZONTAL )
	    return new Dimension(_length, _height);
	else
	    return new Dimension(_height, _length);
    }

	/**
	 * set the height of color bar
	 *
	 * @uml.property name="_height"
	 */
	public void setHeight(int height) {
		_height = height;
	}

	/**
	 * return the height of color bar
	 *
	 * @uml.property name="_height"
	 */
	public int getBarHeight() {
		return _height;
	}

	/**
	 * Get the length of this bar.
	 *
	 * @uml.property name="_length"
	 */
	public int getBarLength() {
		return _length;
	}

 }

