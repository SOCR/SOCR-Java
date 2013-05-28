//$Id: ActiveBar.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $

package edu.ucla.loni.LOVE.colormap;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ActiveBar extends JPanel
        implements PropertyChangeListener
{

    /**
     * @uml.property name="_map"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private BoundedColorMap _map;

    /**
     * @uml.property name="_colorBar"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private ColorBar _colorBar;

    /**
     * @uml.property name="_upper"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private TriangleThumb _upper;

    /**
     * @uml.property name="_lower"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private TriangleThumb _lower;


    /**
     * HORIZONTAL/VERTICAL
     */
    private int _type;

    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;

    /**
     * Ratio between colorbar and size of colormap
     */
    private double _ratio;

    /**
     * @uml.property name="colorMapName"
     */
    // *****************************************************************************
    // Shin, Bae Cheol
    // 9/10/2004
    // Changes of  loewerLimit and upperLimit affect Vertical bars in Histogram
    private String colorMapName = null;

    //********************************************************************************


    /**
     * Default constructor.
     */
    public ActiveBar()
    {
        this(null, 0);
    }

    public ActiveBar(BoundedColorMap map, int type)
    {
        _type = type;
        _map = map;

        //use no layout in order to put every component on
        //a absolute position
        setLayout(null);

        if (_type == HORIZONTAL)
        {//horizontal color bar
            //upper limit indicator
            _upper = new TriangleThumb(TriangleThumb.DOWN, 0, 0, 18);
            add(_upper);
            _upper.setRef(_upper.getSideLength() / 2);
            _upper.setPosition(255);
            _upper.setRange(2, 255);

            //color bar which reflect color in the map
            _colorBar = new ColorBar(_map, ColorBar.HORIZONTAL);
            add(_colorBar);
            _colorBar.setLocation(_upper.getSideLength() / 2, _upper.getheight());

            //lower limit indicator
            _lower = new TriangleThumb(TriangleThumb.UP, 0,
                    _upper.getHeight() + _colorBar.getHeight(),
                    18);
            add(_lower);
            _lower.setRef(_upper.getSideLength() / 2);
            _lower.setPosition(0);
            _lower.setRange(0, 253);
            setSize(_colorBar.getWidth() + _upper.getWidth() / 2 + _lower.getWidth() / 2,
                    _colorBar.getHeight() + _lower.getHeight() + _upper.getHeight());

            _upper.addPropertyChangeListener(this);
            _lower.addPropertyChangeListener(this);
            _map.addPropertyChangeListener(this);

            _ratio = _map.getSize() / _colorBar.getBarLength();
        }
        //vertical color bar
        else
        {
            //TODO: finish the codes for vertical case
            //right = new TriangleThumb(TriangleThumb.RIGHT);
            //add(right);


            //bar = new ColorBar(_map,ColorBar.VERTICAL);
            //add(bar);

            //left = new TriangleThumb(TriangleThumb.LEFT);
            //add(left);


        }

        _updateUI();
        setBorder(BorderFactory.createLineBorder(Color.black));
    }


    public Dimension getPreferredSize()
    {
        int length = _colorBar.getBarLength() + _lower.getSideLength() / 2 + _upper.getSideLength() / 2;
        int heigth = _colorBar.getBarHeight() + _lower.getheight() + _upper.getheight();

        if (_type == HORIZONTAL)
        {
            return new Dimension(length, heigth);
        }
        else
        {
            return new Dimension(heigth, length);
        }
    }

    /**
     * Implementation of PropertyChangeListener.
     */
    public void propertyChange(PropertyChangeEvent e)
    {
        if (e.getSource() == _upper && e.getPropertyName().equals("POSITION"))
        {
            //System.out.println("Get message from upper");
            //System.out.println(e.getPropertyName());
            //System.out.println(e.getNewValue());
            //upper limit changes
            _map.setUpperLimit((int) (_upper.getPosition() * _ratio));

            _updateUI();
        }
        else if (e.getSource() == _lower && e.getPropertyName().equals("POSITION"))
        {
            //System.out.println("Get Message from lower");
            //System.out.println(e.getPropertyName());
            //System.out.println(e.getNewValue());
            //lower limit changes
            _map.setLowerLimit((int) (_lower.getPosition() * _ratio));

            _updateUI();
        }
        else if (e.getSource() == _map)
        {
            _updateUI();
        }
    }

    /**
     * update all the elements based on the colormap
     */
    private void _updateUI()
    {
        _colorBar.repaint();

        //update constraint of upper and lower limit indicator
        int upperPosition = (int) (_map.getUpperLimit() / _ratio);
        int lowerPosition = (int) (_map.getLowerLimit() / _ratio);
        _upper.setPosition(upperPosition);
        _lower.setPosition(lowerPosition);
        _upper.setToolTipText("Upper Limit: " + _map.getUpperLimit());
        _lower.setToolTipText("Lower Limit: " + _map.getLowerLimit());

        _upper.setRange(lowerPosition + 2, 255);
        _lower.setRange(0, upperPosition - 2);
    }

    /**
     * @uml.property name="colorMapName"
     */
    public void setColorMapName(String name) {
		colorMapName = name;
	}

}
