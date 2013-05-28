// 	$Id: ColorMapControlPanel.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $
package edu.ucla.loni.LOVE.colormap;


//import

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * User interface of colormap.
 * The elements will reflect the change of colormap.
 * And, it will affect the colormap as well.
 * For current version, it only provides the user
 * the ability to choose colormap.
 * Example of usage:
 * <code>
 * ColorMapcontrolPanel colorMapControl = new ColorMapControlPanel(colorMap);
 * <p/>
 * controlPane.add(colorMapControl);
 * </code>
 */
public class ColorMapControlPanel extends JPanel
        implements PropertyChangeListener
{

    /**
     * The colormap under control by this panel.
     *
     * @uml.property name="_colorMap"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private BoundedColorMap _colorMap;

    /**
     * Colormap chooser
     *
     * @uml.property name="_colorMapList"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JComboBox _colorMapList;

    /**
     * Invert check box.
     *
     * @uml.property name="_invertBox"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JCheckBox _invertBox;


//****************************************************
// Shin, Bae Cheol
// 9/03/2004
    // Objective: Communication Purpose For AddPopertyChange
    private boolean isPropertyChangeListenerAdded = false;

    //private Object nameOfColorMap1 = null;
    private String nameOfColorMap = null;

    /**
     * @uml.property name="activeBar"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    //private HistogramDrawing histogram = null;
    private ActiveBar activeBar;

//*****************************************************

    /**
     * Constructor of the panel.
     *
     * @param colorMap The color map to be controlled by this panel.
     */
    public ColorMapControlPanel(BoundedColorMap colorMap)
    {
        //store the colormap
        _colorMap = colorMap;
        //register to listen to the colormap
        _colorMap.addPropertyChangeListener(this);

        //create the UI elements
        _createUI();

        //update all the elements to current colormap
        _updateUI();

    }

    /**
     * Fired when colormap is changed.
     *
     * @param evt A <code>PropertyChangeEvent</code> object
     *            describing the change.
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        //update each UI elements accordingly
        _updateUI();

    }

    /**
     * Create the UI elements.
     */
    private void _createUI()
    {
        //setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setLayout(new GridLayout(4, 1));
        setBorder(BorderFactory.createTitledBorder("Colormap"));
        //the list to choos the color map
        _colorMapList = new ColorMapList(_colorMap);


        //a check box for inversion of this colormap
        _invertBox = new JCheckBox("Invert colormap");
        //_invertBox.setBorder(BorderFactory.createEtchedBorder());
        _invertBox.setToolTipText("Invert means bitwise XOR with 0xFF");
        _invertBox.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    _colorMap.setInverted(true);
                    //System.out.println("Going to set inverted = true");
                }
                else
                {
                    _colorMap.setInverted(false);
                    //System.out.println("Going to set inverted = false");
                }
            }
        });

        add(_colorMapList);
        add(new TransparencySlider(_colorMap));

        //****************************************************
        // Shin, Bae Cheol
        // 9/03/2004
        // Objective: Communication Purpose For AddPopertyChange


        activeBar = new ActiveBar(_colorMap, ActiveBar.HORIZONTAL);
        add(activeBar);
        //add(new ActiveBar(_colorMap, ActiveBar.HORIZONTAL));
        add(_invertBox);
        //****************************************************************
    }

    /**
     * Update the value of each UI elements according to
     * the colormap's settings.
     */
    private void _updateUI()
    {
        boolean inverted = _invertBox.isSelected();
        boolean trueInverted = _colorMap.getInverted();
        //update check box status
        if (inverted != trueInverted)
        {
            //update this classe's information
            _invertBox.setSelected(trueInverted);
        }
    }

    /**
     * Inner class which provide a slide bar to adjust transparency
     */

    private class TransparencySlider extends JSlider
            implements PropertyChangeListener
    {

        /**
         * @uml.property name="_color"
         * @uml.associationEnd multiplicity="(0 1)"
         */
        private BoundedColorMap _colorMap;


        /**
         * Constructor.
         */
        public TransparencySlider(BoundedColorMap colorMap)
        {
            super(0, 255, colorMap.getAlpha() & 255); //min, max, value

            setBorder(BorderFactory.createTitledBorder("Transparency"));
            _colorMap = colorMap;
            colorMap.addPropertyChangeListener(this);

            addChangeListener(new ChangeListener()
            {
                public void stateChanged(ChangeEvent e)
                {
                    if (!getValueIsAdjusting())
                    {
                        //if user stop dragging slider
                        _colorMap.setAlpha((byte) getValue());
                        setToolTipText("Transparency: " + getValue());
                    }
                }
            });

            //update to current state
            setValue(_colorMap.getAlpha() & 255);
            setToolTipText("Transparency: " + getValue());
        }

        /**
         * response to colormap change event.
         */
        public void propertyChange(PropertyChangeEvent e)
        {

            if (e.getSource() == _colorMap &&
                    (e.getPropertyName().equals("Alpha") ||
                            e.getPropertyName().equals("ColorMapName")) &&
                    !e.getNewValue().equals(new Byte((byte) getValue())))
            {
                //colormap name is changed accordingly
                //setValue(((Integer)e.getNewValue()).intValue());
                setValue(_colorMap.getAlpha() & 255);
                setToolTipText("Transparency: " + getValue());

            }
        }
    }

    /**
     * Inner class which specially adapted to listen to colormap.
     */

    private class ColorMapList extends JComboBox
            implements PropertyChangeListener
    {

        /**
         * @uml.property name="_color"
         * @uml.associationEnd multiplicity="(0 1)"
         */
        private BoundedColorMap _colorMap;

        /**
         * Constructor. Initialize the JComboBox according to the colormap.
         */
        public ColorMapList(BoundedColorMap colorMap)
        {
            super(colorMap.getColorMapNames());

            setToolTipText("Select the colormap");

            _colorMap = colorMap;
            setEditable(false);
            setSelectedItem(_colorMap.getCurrentColorMapName());
            colorMap.addPropertyChangeListener(this);
            addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JComboBox cb = (JComboBox) e.getSource();
                    Object colorMapName = cb.getSelectedItem();

                    //*****************************************************
                    // Shin, Bae Cheol
                    // 9/09/2004
                    nameOfColorMap = (String) cb.getSelectedItem();
                    activeBar.setColorMapName(nameOfColorMap);
                    //activeBar.setColorMapName((String)_colorMap.getCurrentColorMapName());

                    //*************************************************************


                    _colorMap.setColorMapName(colorMapName);
                }
            });
        }

        /**
         * response to colormap change event.
         */
        public void propertyChange(PropertyChangeEvent e)
        {
            //System.out.println("List box Get a notification: "+e.getPropertyName());
            //System.out.println("New Value: " + e.getNewValue());
            if (e.getSource() == _colorMap &&
                    e.getPropertyName().equals("ColorMapName") &&
                    !e.getNewValue().equals(getSelectedItem()))
            {
                //colormap name is changed accordingly
                setSelectedItem(e.getNewValue());
            }
        }
    }

    /**
     * @uml.property name="_colorMap"
     */
    //************************************************************************
    // Shin, Bae Cheol
    // 9/16/2004
    public BoundedColorMap getColorMap()
    {
        return _colorMap;
    }


    //************************************************************************


    /**
     * main method to test this class.
     */
    public static void main(String args[])
    {
        JFrame testFrame = new JFrame();
        BoundedColorMap colorMap = new BoundedColorMap(256, 8, 2);
        ColorMapControlPanel controlPanel = new ColorMapControlPanel(colorMap);
        Container pane = testFrame.getContentPane();
        pane.setLayout(new BoxLayout(pane,
                BoxLayout.Y_AXIS));
        pane.add(controlPanel);
        testFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        testFrame.pack();
        testFrame.setVisible(true);
    }

}
