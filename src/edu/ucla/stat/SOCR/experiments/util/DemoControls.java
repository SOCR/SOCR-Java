package edu.ucla.stat.SOCR.experiments.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


    /**
     * The DemoControls class provides controls for selecting the amount
     * of Shapes, Strings or Images to display and selecting the 
     * transformation to perform.
     */
     class DemoControls extends JPanel implements ActionListener, ChangeListener {

        Demo demo;
        TransformAnim anim;
        JSlider shapeSlider, stringSlider, imageSlider;
        Font font = new Font("serif", Font.PLAIN, 10);
        JToolBar toolbar;
       
       
        
        public DemoControls(TransformAnim anim, Demo demo, int count) {
            this.demo = demo;
            this.anim = anim;
            setBackground(Color.gray);
            setLayout(new BorderLayout());
            JToolBar bar = new JToolBar(JToolBar.VERTICAL);
            bar.setBackground(Color.gray);
            bar.setFloatable(false);
            shapeSlider = new JSlider(JSlider.HORIZONTAL,0,20,demo.numShapes);
            shapeSlider.addChangeListener(this);
            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitleFont(font);
            tb.setTitle(String.valueOf(demo.numShapes) + " Shapes");
            shapeSlider.setBorder(tb);
            shapeSlider.setPreferredSize(new Dimension(80,44));
           // bar.add(shapeSlider);
           // bar.addSeparator();

            stringSlider = new JSlider(JSlider.HORIZONTAL,0,10,demo.numStrings);
            stringSlider.addChangeListener(this);
            tb = new TitledBorder(new EtchedBorder());
            tb.setTitleFont(font);
            tb.setTitle(String.valueOf(demo.numStrings) + " Strings");
            stringSlider.setBorder(tb);
            stringSlider.setPreferredSize(new Dimension(80,44));
          //  bar.add(stringSlider);
          //  bar.addSeparator();

            imageSlider = new JSlider(JSlider.HORIZONTAL,0,10,demo.numImages);
            imageSlider.addChangeListener(this);
            tb = new TitledBorder(new EtchedBorder());
            tb.setTitleFont(font);
            tb.setTitle(String.valueOf(demo.numImages) + " Images");
            imageSlider.setBorder(tb);
            imageSlider.setPreferredSize(new Dimension(80,44));
         //   bar.add(imageSlider);
         //   bar.addSeparator();

            toolbar = new JToolBar();
            toolbar.setFloatable(false);
            addButton("T", "translate", demo.doTranslate);
            addButton("R", "rotate", demo.doRotate);
            addButton("SC", "scale", demo.doScale);
            addButton("SH", "shear", demo.doShear);
            
            bar.add(new JLabel("Animating Row #"+count));
            bar.add(toolbar);

            add(bar);
        }
        
        public DemoControls(TransformAnim anim, Demo demo) {
        	this(anim, demo, 0);
        }

        public void addButton(String s, String tt, boolean state) {
            JButton b = (JButton) toolbar.add(new JButton(s));
            b.setFont(font);
            b.setSelected(state);
            b.setToolTipText(tt);
            b.setBackground(state ? Color.green : Color.lightGray);
            b.addActionListener(this);
        }


        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            b.setSelected(!b.isSelected());
            b.setBackground(b.isSelected() ? Color.green : Color.lightGray);
            if (b.getText().equals("T")) {
            	
                demo.doTranslate = b.isSelected();
                anim.g_doTranslate =  demo.doTranslate;
            } else if (b.getText().equals("R")) {
                demo.doRotate = b.isSelected();
                anim.g_doRotate = demo.doRotate;
            } else if (b.getText().equals("SC")) {
                demo.doScale = b.isSelected();
                anim.g_doScale= demo.doScale;
            } else if (b.getText().equals("SH")) {
                demo.doShear = b.isSelected();
                anim.g_doShear= demo.doShear;
            }
        }


        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();
            int value = slider.getValue();
            TitledBorder tb = (TitledBorder) slider.getBorder();
          /*  if (slider.equals(shapeSlider)) {
                tb.setTitle(String.valueOf(value) + " Shapes");
                demo.setShapes(value);
            } else if (slider.equals(stringSlider)) {
                tb.setTitle(String.valueOf(value) + " Strings");
                demo.setStrings(value);
            } else if (slider.equals(imageSlider)) {
                tb.setTitle(String.valueOf(value) + " Images");
                demo.setImages(value);
            } */
        }
    } // End DemoControls class