/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.devices;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.ImageIcon;

/**
* This class defines a door that can be opened or closed. When closed, the
* door shows a label; when opened the door shows an image and a label. The
* object comes supplied with car and goat images, suitable for the Monty Hall problem.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Door extends JComponent implements Serializable{
	//Variables
	private int width, height;
	private boolean opened = false;
	//Objects
	private Image image;
	private String label;
	private Color doorColor, openColor, labelColor;
	private Font font = new Font("Sans Serif", Font.BOLD, 14);
	private static Image goatImage, carImage;
	public final static int GOAT = 0, CAR = 1;

	/**
	* This general constructor creates a new door with width and height, and
	* specified colors. The width and height should be chosen according to the size
	* of the images to be supplied.
	* @param w the width
	* @param h the height
	* @param dc door color
	* @param oc open color
	* @param lc label color
	*/
	public Door(int w, int h, Color dc, Color oc, Color lc){
		width = w;
		height = h;
		ImageIcon icon;
		doorColor = dc; openColor = oc; labelColor = lc;
		setToolTipText("Door");
		icon = new ImageIcon(getClass().getResource("goat.gif"));
		goatImage = icon.getImage();
		icon = new ImageIcon(getClass().getResource("car.gif"));
		carImage = icon.getImage();
		if (Math.random() < 0.5) setImage(goatImage);
		else setImage(carImage);
		setLabel("Door");
	}

	/**
	* This special constructor creates a new door with specified
	* height and width and with default door color yellow, open
	* color white, and label color red.
	* @param w the width
	* @param h the height
	*/
	public Door(int w, int h){
		this(w, h, Color.yellow, Color.white, Color.red);
	}

	/**
	* This default constructor creates a new door with default
	* size 150 by 200 and with default door color yellow,
	* open color white, and label color red.
	*/
	public Door(){
		this(150, 200);
	}

	/**
	* This method opens the door to display a specified image and caption.
	* @param i the image
	* @param s the label
	*/
	public void open(Image i, String s){
		label = s;
		image = i;
		setOpened(true);
	}

	/**
	* This method opens the door to display one of the standard images
	* (the car or the goat) and a caption
	* @param i the index (0 goat, 1 car)
	* @param s the caption
	*/
	public void open(int i, String s){
		label = s;
		if (i == 1) image = carImage;
		else image = goatImage;
		setOpened(true);
	}

	/**
	* This method closes the door to display a specified caption.
	* @param s the label.
	*/
	public void close(String s){
		label = s;
		setOpened(false);
	}

	/**
	* This method sets the image. The state of the door is not changed.
	* @param i the image
	*/
	public void setImage(Image i){
		image = i;
	}

	/**
	* This method sets the image to one of the standard images (car or goat).
	* The state of the door is not changed.
	*/
	public void setImage(int i){
		if (i == 1) image = carImage;
		else image = goatImage;
	}

	/**
	* This method returns the image. The state of the door is not changed.
	* @return the image.
	*/
	public Image getImage(){
		return image;
	}

	/**
	* This method sets the label. The state of the door is not changed.
	* @param s the string
	*/
	public void setLabel(String s){
		label = s;
	}

	/**
	* This method returns the label. The state of the door is not changed.
	* @return the label
	*/
	public String getLabel(){
		return label;
	}

	/**
	* This method paints the door.
	* @param g the graphics context.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(font);
		int w = getSize().width, h = getSize().height,
			y = g.getFontMetrics(font).getHeight();
		//Draw the rectangle
		if (opened) g.setColor(openColor); else g.setColor(doorColor);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.black);
		g.drawRect(0, 0, w, h);
		//Draw the label
		g.setColor(labelColor);
		g.drawString(label, 5, y);
		//If opened, draw the image
		if (opened){
			int a = image.getWidth(this), b = image.getHeight(this);
			g.drawImage(image, (w - a) / 2, (h - b) / 2, this);
		}
		else{ //Draw door knob
			g.setColor(Color.black);
			g.fillOval(w - 16, h / 2 - 6, 12, 12);
		}
	}

	/**
	* This method set the open state of the door.
	* @param b true if the door is to be opened
	*/
	public void setOpened(boolean b){
		opened = b;
		repaint();
	}

	/**
	* This method tests to see if the door is opened.
	* @return true if opened, false if closed.
	*/
	public boolean isOpened(){
		return opened;
	}

	/**
	* This method sets the door color.
	* @param c the door color
	*/
	public void setDoorColor(Color c){
		doorColor = c;
	}

	/**
	* This method returns the door color
	* @return the door color
	*/
	public Color getDoorColor(){
		return doorColor;
	}

	/**
	* This method sets the open color.
	* @param c the open color
	*/
	public void setOpenColor(Color c){
		openColor = c;
	}

	/**
	* This method returns the open color
	* @return the open color
	*/
	public Color getOpenColor(){
		return openColor;
	}

	/**
	* This method sets the label color.
	* @param c the label color
	*/
	public void setLabelColor(Color c){
		labelColor = c;
	}

	/**
	* This method returns the label color
	* @return the label color
	*/
	public Color getLabelColor(){
		return labelColor;
	}

	/**
	* This method specifies the minimum size.
	* @return the 100 by 300 dimension
	*/
	public Dimension getMinimumSize(){
		return new Dimension(width, height);
	}

	/**
	* This method specifies the preferred size.
	* @return the 100 by 300 dimension
	*/
	public Dimension getPreferredSize(){
		return new Dimension(width, height);
	}
}
