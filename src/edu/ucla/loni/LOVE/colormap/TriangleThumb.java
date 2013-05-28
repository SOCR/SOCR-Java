//$Id: TriangleThumb.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $

package edu.ucla.loni.LOVE.colormap;

/* TriangleThumb.java
 *
 * author : ErhFang Lee
 * date :  9/24/01
 *
 * Modified: Guogang 10/4/2001
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * This is the triangle for color bar.
 * It has a few important parameters:
 *   1. Type  may be up/down/left/right.
 *   2. Position  the current position of the triangle.
 */
public class TriangleThumb extends JLabel {
    /** Orientation of the triangle */
    public final static int DOWN = 0;

    /** Orientation of the triangle */
    public final static int UP = 1;

    /** Orientation of the triangle */
    public final static int LEFT = 2;

    /** Orientation of the triangle */
    public final static int RIGHT = 3;

    /** Orientation of the Trangle Vertex  */
    private int _type;

	/**
	 * length of side of the triangle thumb
	 *
	 * @uml.property name="_sideLength"
	 */
	protected int _sideLength;

	/**
	 * Size of the other dimension
	 *
	 * @uml.property name="_height"
	 */
	protected int _height;


    /**
     * x, y coordiation the vertex of the triangle
     * pointed to.
     */
    protected int _vertexX;

	/**
	 * x, y coordiation the vertex of the triangle
	 * pointed to.
	 *
	 * @uml.property name="_vertexY"
	 */
	protected int _vertexY;

	/**
	 * Reference position for calculation of position
	 *
	 * @uml.property name="_ref"
	 */
	protected int _ref;


    /** x, y range */
    protected int _minX;
    /** x, y range */
    protected int _maxX;
    /** x, y range */
    protected int _minY;
    /** x, y range */
    protected int _maxY;

    /** relative position of origin to vertex */
    private int _oriX;
    /** relative position of origin to vertex */
    private int _oriY;

    /** movable orientation */
    private boolean _horizontalMovable = true;
    private boolean _verticalMovable = true;

    /**
     * X, Y coordinate of the mouse cursor when button
     * is down. This parameter is used to calculate
     * new position of this thumb after drag.
     */
    private int _anchorY;
    /**
     * X, Y coordinate of the mouse cursor when button
     * is down. This parameter is used to calculate
     * new position of this thumb after drag.
     */
    private int _anchorX;

    /**  Default constructor.*/
    public TriangleThumb() {
        this(0, 0, 0, 25);
    }

    public TriangleThumb(int type) {
        this(type, 0, 0, 25);
    }

    public  TriangleThumb(int type, int iniX, int iniY) {
        this(type, iniX, iniY,25);
    }


    public TriangleThumb(int type,int iniX, int iniY, int sideLength) {
        _type = type;
        _sideLength = sideLength;

        setOpaque(false);

        _height = (int) ( 0.866* ((double) sideLength)  + 0.5);
        int lm = sideLength/2;

        switch ( _type ) {
            case DOWN:
                setSize(sideLength, _height);
                _oriX = -lm;
                _oriY = -_height;
                _verticalMovable = false;
                break;

            case UP:
                setSize(sideLength, _height);
                _oriX = -lm;
                _oriY = 0;
                _verticalMovable = false;
                break;

            case LEFT:
                setSize( _height, sideLength);
                _oriX = 0;
                _oriY = -lm;
                _horizontalMovable = false;

                break;

            case RIGHT:
                setSize( _height, sideLength);
                _oriX = -_height;
                _oriY = -lm;
                _horizontalMovable = false;	  	    /** Orientation of the triangle */

                break;

            default :
        }//end swich


        _vertexX =-_oriX + iniX;
        _vertexY= -_oriY + iniY;

        //setBorder(BorderFactory.createLineBorder(Color.black));

        //default ranges if not given by parent
        _minX = 0;
        _maxX =  300;

        _minY = 0;
        _maxY = 300;

        addMouseMotionListener( new MouseMotionAdapter() {
            public void  mouseDragged(MouseEvent e) {
                shift(e.getX() - _anchorX, e.getY() - _anchorY);
            }
        });

        addMouseListener( new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                _anchorX = e.getX();
                _anchorY = e.getY();
            }
            public void mouseReleased(MouseEvent e) {
                //notify change of position
                firePropertyChange("POSITION", -1, getPosition());
                //System.out.println("Mouse Released:" + getPosition());
            }
        });    /** Orientation of the triangle */


    }


    public Dimension getPreferredSize() {
        if ( _type == UP || _type == DOWN )
            return new Dimension( _sideLength, _height);
        else
            return new Dimension( _height,_sideLength );
    }

    /** Orientation of the triangle */
    /** Orientation of the triangle */


    public void paintComponent(Graphics g) {
        setLocation(_vertexX + _oriX,  _vertexY + _oriY);
        super.paintComponent(g);
        _drawTriangle(g);
    }

    /**
     * Routhine to draw the 3D shaded triangle.
     */
    private void _drawTriangle(Graphics g) {    /** Orientation of the triangle */

        int i;
        int lm = _sideLength/2;


        switch ( _type ) {
            case DOWN:
                for ( i=0; i<2 ; i++ ) {
                    g.setColor(Color.white);
                    g.drawLine(lm, _height-i, i, i); //   \
                    g.drawLine( i, i, _sideLength-i, i); //  --
                    g.setColor(Color.gray);
                    g.drawLine(lm, _height-i,  _sideLength-i,  +i); //  /
                }
                break;

            case UP:
                for ( i=0; i<2 ; i++ ) {
                    g.setColor(Color.gray);
                    g.drawLine(i, _height-i, _sideLength-i, _height-i);// --
                    g.drawLine(lm,  i, _sideLength-i, _height -i);// \
                    g.setColor(Color.white);
                    g.drawLine(lm, i, i, _height-i);// /
                }
                break;

            case LEFT:
                for ( i=0; i<2 ; i++ ) {
                    g.setColor(Color.gray);
                    g.drawLine(_height-i, i, _height-i, _sideLength-i); //  |
                    g.drawLine(i, lm , _height-i, _sideLength-i); //  \
                    g.setColor(Color.white);
                    g.drawLine(i, lm , _height-i, i); //  /
                }
                break;

            case RIGHT:
                for ( i=0; i<2 ; i++ ) {
                    g.setColor(Color.white);
                    g.drawLine(_height-i, lm ,i, i);  //  \
                    g.drawLine(i, i, i, _sideLength-i);//  |
                    g.setColor(Color.gray);
                    g.drawLine(_height-i, lm, i,  _sideLength-i); // /
                }
                break;

            default :

        }//end swich


    }//end drawTriangle

	/**
	 * return the heigh of the triangle thumb
	 *
	 * @uml.property name="_height"
	 */
	public int getheight() {
		return (_height);
	}



    /**
     * Shift the triangle by X, Y.
     */
    private void shift(int x, int y) {
        if (_horizontalMovable && inXRange(_vertexX+x)) {  //type UP or DOWN
            _vertexX += x;
        }
        if (_verticalMovable && inYRange(_vertexY+y)) {     //type LEFT or RIGHT
            _vertexY += y;
        }

        setLocation(_vertexX + _oriX, _vertexY + _oriY);

        //System.out.println("Position: " + _vertexX + " " + _vertexY);
    }


    private boolean inXRange(int x) {
        return ( (x >= _minX) && (x <= _maxX) );
    }


    private boolean inYRange( int y)    /** Orientation of the triangle */

    {
        return ((y >= _minY) && (y <= _maxY) );
    }


    /** set the X Range thumb can move */
    public void setXRange(int minX, int maxX) {
        _minX = minX;    /** Orientation of the triangle */

        _maxX = maxX;
    }

    /** set the X Range thumb can move */
    public void setYRange(int minY, int maxY) {
        _minY = minY;
        _maxY = maxY;
    }

    /**
     * Set the range a thumbe can move. It will
     * be assigned to X or Y range automatically
     * based on the type of the triangle.
     */
    public void setRange(int min, int max) {
        if(_type == UP || _type == DOWN) {
            setXRange(min + _ref, max + _ref);
        }
        else if(_type == RIGHT || _type == LEFT) {
            setYRange(min + _ref, max + _ref);
        }
    }

	/**
	 *
	 * @uml.property name="_sideLength"
	 */
	public int getSideLength() {
		return _sideLength;
	}

	/**
	 * TODO: later, it may still resize after initial settings.
	 * Temporary, never use it.
	 *
	 * @uml.property name="_vertexY"
	 */
	public void setPosition(int position) {
		//System.out.println("Position"+position);
		if (_horizontalMovable && inXRange(_ref + position)) {
			_vertexX = _ref + position;
		} else {
			_vertexY = _ref + position;
		}
		setLocation(_vertexX + _oriX, _vertexY + _oriY);
	}


    /**
     * Get the current position of the thumb.
     */
    public int getPosition() {
        int position;
        if(_horizontalMovable) {
            position = _vertexX - _ref;
        }
        else {
            position = _vertexY - _ref;
        }
        return position;
    }

	/**
	 * Set the reference position.
	 *
	 * @uml.property name="_ref"
	 */
	public void setRef(int reference) {
		_ref = reference;
	}

	/**
	 * Get the reference.
	 *
	 * @uml.property name="_ref"
	 */
	public int getRef() {
		return _ref;
	}

}


