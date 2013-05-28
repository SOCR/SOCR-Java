package edu.ucla.stat.SOCR.chart.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

public class SOCRChartPanel extends org.jfree.chart.ChartPanel implements MouseListener {

public SOCRChartPanel(JFreeChart chart, boolean useBuffer) {
		super(chart, useBuffer);
		this.chart = chart;
		addKeyEventListener();
	}



//private boolean isPressingCtrl = false;
private boolean isPressingAlt = false;
private JFreeChart chart = null;

/**
* constructor
* @param chart
*/
/*
public SOCRChartPanel(JFreeChart chart)
{
super(chart);
this.chart = chart;
addKeyEventListener();
}*/

/*
public SOCRChartPanel(JFreeChart chart,)
{
super(chart);
this.chart = chart;
addKeyEventListener();
}*/

public void setIsPressingAlt(boolean in){
	//System.out.println("not the keyevent SOCRChartPanel setting isPressingAlt " + in);
	isPressingAlt = in;
}

/**
* add key listener for this panel
*/
private void addKeyEventListener(){

	// add key listener for press control key event
	InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	
/*	KeyStroke controlPressedStroke = KeyStroke.getKeyStroke("ctrl CONTROL");
	
	Action controlPressedAction = new AbstractAction(){
			public void actionPerformed(ActionEvent actionEvent){
				if (!isPressingCtrl)
				{
				isPressingCtrl = true;
				}
			}
		};
	
	//inputMap.put(controlPressedStroke, "ctrl CONTROL");
	this.getActionMap().put("ctrl CONTROL", controlPressedAction);
	
	// add key listener for release control key event
	KeyStroke controlReleasedStroke = KeyStroke.getKeyStroke("released CONTROL");
	
	Action controlReleasedAction = new AbstractAction(){
		public void actionPerformed(ActionEvent actionEvent){
			if (isPressingCtrl){
			isPressingCtrl = false;
			}
		}
	};
	
	//inputMap.put(controlReleasedStroke, "released CONTROL");
	this.getActionMap().put("released CONTROL", controlReleasedAction);*/
	
	KeyStroke metaPressedStroke = KeyStroke.getKeyStroke("alt ALT");
	Action metaPressedAction = new AbstractAction(){
		public void actionPerformed(ActionEvent actionEvent){
			if (!isPressingAlt)
			{
			isPressingAlt = true;
			//System.out.println("key Event SOCRChartPanel set " + isPressingAlt);
			}
		}
	};
	inputMap.put(metaPressedStroke, "alt ALT");
	this.getActionMap().put("alt ALT", metaPressedAction);
//System.out.println("binding SOCRChartPanel alt ALT key Event");
	
KeyStroke metaReleasedStroke = KeyStroke.getKeyStroke("released ALT");
	
	Action metaReleasedAction = new AbstractAction(){
		public void actionPerformed(ActionEvent actionEvent){
			if (isPressingAlt){
			isPressingAlt = false;
			//System.out.println("key Event SOCRChartPanel reset " + isPressingAlt);
			}
		}
	};
	
	inputMap.put(metaReleasedStroke, "released ALT");
	this.getActionMap().put("released ALT", metaReleasedAction);
	//System.out.println("binding SOCRChartPanel released ALT key Event");
	
}


/**
* override the mouse pressed method
* Handles a 'mouse pressed' event.
* @param e information about the event.
*/
public void mousePressed(MouseEvent e){
	if (isPressingAlt){
		//System.out.println("SOCRChartPanel alt mouse pressed");
	//	System.out.println("SOCRChartPanel  off zoom");
		setMouseZoomable(false,true);
		super.mousePressed(e);
	//	getPointInChart(e);
	//	System.out.println("SOCRChartPanel on zoom");
		setMouseZoomable(true,false);	
	}
	else{
		super.mousePressed(e);
	}
}

/**
* override the mouse released method
* Handles a 'mouse released' event.
* @param e information about the event.
*/
public void mouseReleased(MouseEvent e){
	if (isPressingAlt){
	//	System.out.println("SOCRChartPanel alt mouse released");
	//	System.out.println("SOCRChartPanel  off zoom");
		setMouseZoomable(false,true);
		super.mouseReleased(e);
	//	getPointInChart(e);
	//	System.out.println("SOCRChartPanel on zoom");
		setMouseZoomable(true,false);
	
	}
	else{
		super.mouseReleased(e);
	}
}



/**
* Receives chart x,y axis.
*
* @param e mouse event.
*/
public void getPointInChart(MouseEvent e){
	Insets insets = getInsets();
	int mouseX = (int) ((e.getX() - insets.left) / this.getScaleX());
	int mouseY = (int) ((e.getY() - insets.top) / this.getScaleY());
	//System.out.println("x = " + mouseX + ", y = " + mouseY);
	
	Point2D p = this.translateScreenToJava2D(new Point(mouseX, mouseY));
	XYPlot plot = (XYPlot) chart.getPlot();
	ChartRenderingInfo info = this.getChartRenderingInfo();
	Rectangle2D dataArea = info.getPlotInfo().getDataArea();
	
	ValueAxis domainAxis = plot.getDomainAxis();
	RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
	ValueAxis rangeAxis = plot.getRangeAxis();
	RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
	
	double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
	double chartY = rangeAxis.java2DToValue(p.getY(), dataArea, rangeAxisEdge);
	//System.out.println("Chart: x = " + chartX + ", y = " + chartY);
}



}