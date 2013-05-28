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
import javax.swing.JComponent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

/**
* This class models the cell grid used in the Coupon Collector Experiment. The grid can show
* a ball in a particualr cell, or can show ball counts for each cell.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CellGrid extends JComponent implements MouseMotionListener, Serializable{
	//Constants
	public final static int COUNTS = 2, BALL = 1, NONE = 0;
	//Variables
	private int cells, rows, columns, bigColumns, currentCell, cellWidth, cellHeight, show = NONE;
	private int[] cellCount;
	//Font
	private static Font font = new Font("Arial", Font.BOLD, 12);
	//Colors
	private Color gridColor, countColor, ballColor;

	/**
	* This general constructor: creates a new cell grid with a specified number of cells.
	* @param m the number of cells.
	*/
	public CellGrid(int m){
		gridColor = Color.blue;
		countColor = Color.red;
		ballColor = Color.red;
		addMouseMotionListener(this);
		setCells(m);
	}

	/**
	* This default constructor creates a new cell grid with 20 cells.
	*/
	public CellGrid(){
		this(20);
	}

	/**
	* This method paints the cell grid.
	* @param g the graphics context.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int width = getSize().width, height = getSize().height;
		cellWidth = width / columns;
		cellHeight = height / columns;
		rows = cells / columns + 1;
		bigColumns = cells % columns;
		int ballWidth = Math.min(cellWidth, cellHeight), x, y, labelWidth, labelHeight;
		String label;
		//draw grid
		width = columns * cellWidth;
		g.setColor(gridColor);
		y = 0;
		for(int i = 0; i < rows; i++){
			g.drawLine(0, y, width, y);
			y = y + cellHeight;
		}
		if (bigColumns > 0){
			y = rows * cellHeight;
			x = bigColumns * cellWidth;
			g.drawLine(0, y, x, y);
		}
		x = 0;
		for(int i = 0; i <= columns; i++){
			if(i <= bigColumns) y = rows * cellHeight;
			else y = (rows - 1) * cellHeight;
			g.drawLine(x, 0, x, y);
			x = x + cellWidth;
		}
		if (show == COUNTS){
			//draw counts
			g.setFont(font);
			labelHeight = g.getFontMetrics(font).getAscent();
			g.setColor(countColor);
			for (int i = 0; i < cells; i++){
				if(cellCount[i] > 0){
					y = (i / columns) * cellHeight + cellHeight / 2 + labelHeight / 2;
					x = (i % columns) * cellWidth + cellWidth / 2;
					label = String.valueOf(cellCount[i]);
					labelWidth = g.getFontMetrics(font).stringWidth(label);
					g.drawString(label, x - labelWidth / 2, y);
				}
			}
		}
		else if (show == BALL){
			y = (currentCell / columns) * cellHeight;
			x = (currentCell % columns) * cellWidth;
			g.setColor(ballColor);
			g.fillOval(x, y, ballWidth, ballWidth);
		}
	}

	/**
	* This method sets the counts for all of the cells.
	* @param c the array of cell counts.
	*/
	public void setCellCount(int[] c){
		cellCount = c;
	}

	/**
	* This method sets the count for an individual cell.
	* @param i the index of the cell.
	* @param k the cell count.
	*/
	public void setCellCount(int i, int k){
		if (i > 0 & i < cells) cellCount[i] = k;
	}

	/**
	* This method returns the counts for all of the cells.
	* @return the array of cell counts.
	*/
	public int[] getCellCount(){
		return cellCount;
	}

	/**
	* This method returns the cell count for an individual cell.
	* @param i the index of the cell.
	* @return the cell count.
	*/
	public int getCellCount(int i){
		if (i <= 0) i = 0;
		else if (i >= cells) i = cells - 1;
		return cellCount[i];
	}

	/**
	* This method resets the cell grid. The cell counts are all set to 0.
	*/
	public void reset(){
		cellCount = new int[cells];
		setShow(NONE);
	}

	/**
	* This mtheod sets the number of cells.
	* @param m the number of cells.
	*/
	public void setCells(int m){
		if (m < 1) m = 1;
		cells = m;
		columns = (int) Math.ceil(Math.sqrt(cells));
		reset();
	}

	/**
	* This method returns the number of cells.
	* @return the number of cells.
	*/
	public int getCells(){
		return cells;
	}

	/**
	* This method sets the grid color.
	* @param c the grid color
	*/
	public void setGridColor(Color c){
		gridColor = c;
		repaint();
	}

	/**
	* This method returns the grid color
	* @return the grid color
	*/
	public Color getGridColor(){
		return gridColor;
	}

	/**
	* This method sets the count color.
	* @param c the count color
	*/
	public void setCountColor(Color c){
		countColor = c;
		repaint();
	}

	/**
	* This method returns the count color
	* @return the count color
	*/
	public Color getCountColor(){
		return countColor;
	}

	/**
	* This method sets the ball color.
	* @param c the ball color
	*/
	public void setBallColor(Color c){
		ballColor = c;
		repaint();
	}

	/**
	* This method returns the ball color
	* @return the ball color
	*/
	public Color getBallColor(){
		return ballColor;
	}

	/**
	* This method sets the show parameter.
	* @param i the show parameter
	*/
	public void setShow(int i){
		if (i < 0) i = 0; else if (i > 2) i = 2;
		show = i;
		repaint();
	}

	/**
	* This method gets the show parameter.
	* @return the show parameter
	*/
	public int getShow(){
		return show;
	}

	/**
	* This method sets the currently selected cell
	* @param c the current cell
	*/
	public void setCurrentCell(int c){
		if (c < 0) c = 0; else if (c > cells - 1) c = cells - 1;
		currentCell = c;
	}

	/**
	* This method gets the currently selected cell
	* @return the current cell
	*/
	public int getCurrentCell(){
		return currentCell;
	}

	/**
	* This method returns the number of rows in the cell grid.
	* @return the number of rows
	*/
	public int getRows(){
		return rows;
	}

	/**
	* This method returns the number of columns in the cell grid.
	* @return the number of columns
	*/
	public int getColumns(){
		return columns;
	}

	/**
	* This method returns the number of big columns in the cell grid.
	* @return the number of big columns
	*/
	public int getBigColumns(){
		return bigColumns;
	}

	/**
	* This method handles the mouse move event. The number of the cell is given in the tool tip.
	* @param e the mouse event
	*/
	public void mouseMoved(MouseEvent e){
		if (e.getSource() == this){
			int i = e.getX() / cellWidth, j = e.getY() / cellHeight;
			int mouseCell = j * columns + i;
			if (mouseCell < cells) setToolTipText("Cell " + mouseCell);
			else setToolTipText("Cell Grid");
		}
	}

	//These mouse events are not handled.
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}


}



