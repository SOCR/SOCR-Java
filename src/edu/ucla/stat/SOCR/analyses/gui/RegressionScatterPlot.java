/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.analyses.gui;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.JTable;
import java.beans.*;
import javax.swing.*;

/** Regression Scatter Plot object */
public class RegressionScatterPlot extends JComponent implements MouseListener,
       MouseMotionListener, KeyListener, PropertyChangeListener {

  Image offscreenImage, startImage;
  Graphics offscreenGraphics, startGraphics;
  Vector<SOCRPoint2D> Points;
  int currentPoint;
  ScatterPlotRange xScatterPlot_Range, yScatterPlot_Range;

  double xy_product = 0, x_sum = 0, y_sum = 0, square_x_sum = 0;
  double top_calc, bottom_calc, slope_calc, y_intercept;
  double square_y_sum = 0.0, Correlation = -2.0;
  SOCRPoint2D RL1 = new SOCRPoint2D(), RL2 = new SOCRPoint2D();

  JTable dataTable;

  int NumXDec = 2, NumYDec = 2;
  int Width, Height, oldx, oldy;
  int FWidth, FHeight;
  int currInd;
  double xIntWidth, yIntWidth, currXCoord, currYCoord;
  int BORDER = 10;
  int LEFTBORDER, RIGHTBORDER, TOPBORDER, BOTTOMBORDER;
  boolean Interactive, ShowAxis, ShowGrid, ForcePositive;
  boolean ShowRLine, showErrorLines, showCurrentCoordinate, multipleSelect;
  boolean MovingPoint = false;
  Font font;
  FontMetrics fm;
    
  MenuBar menubar;
  Menu printMenu, formatMenu;
  ActionListener actionListener;
  
  public RegressionScatterPlot() {
    
    Points = new Vector<SOCRPoint2D>();
    
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);

    xIntWidth = 1.0;
    yIntWidth = 1.0;
    offscreenImage = null;
    LEFTBORDER = 30;
    RIGHTBORDER = 10;
    TOPBORDER = 10;
    BOTTOMBORDER = 25;

    currInd = -1;
    currentPoint = -1;

    font = new Font("Arial",Font.PLAIN,10);
    fm = getFontMetrics(font);

    xScatterPlot_Range = getXScatterPlot_Range();
    yScatterPlot_Range = getYScatterPlot_Range();
    ShowGrid = true;
    ShowAxis = true;
    Interactive = true;
    ShowRLine = true;
    showErrorLines = false;
    showCurrentCoordinate = false;
    computeFullRegression();
//    enableEvents( AWTEvent.ACTION_EVENT_MASK );
  }

  public void updatePoints() {
    	computeFullRegression();
	updateJTable();
    	repaint();
  }

  public void updateJTable() {
    	dataTable = new JTable();
	String [] columnNames = {"Y", "X"};
	Object[][] pointData = new Object[Points.size()][2];
	SOCRPoint2D pt;

	if ( Points != null ) {
	   for (int i = 0; i < Points.size(); i++)
	   {	pt = (SOCRPoint2D)Points.elementAt(i);
		pointData[i][0] = new Double(pt.x);
		pointData[i][1] = new Double(pt.y);	
	   }
	   dataTable = new JTable(pointData, columnNames);
	   firePropertyChange("DataUpdate", null, dataTable);
	}
  }

  public int getNumPoints() {
    return Points.size();
  }

  public double getCorrelation() {
    return Correlation;
  }

  public void setData(JTable _dataTable) {
 	xScatterPlot_Range = getXScatterPlot_Range();
        yScatterPlot_Range = getYScatterPlot_Range();
        double xval, yval;
    	SOCRPoint2D mp;
    	Points.removeAllElements();

	dataTable = _dataTable;

	if (dataTable != null && dataTable.getColumnCount() > 1)
	{
	   for (int i = 0 ; i < dataTable.getRowCount(); i++)
	   {	xval = (Double.valueOf((String)(dataTable.getValueAt(i, 0)))).doubleValue();
		yval = (Double.valueOf((String)(dataTable.getValueAt(i, 1)))).doubleValue();
		Points.addElement( new SOCRPoint2D(xval, yval) );
    	    }
	
	   computeFullRegression();
    	   repaint();
	}
  }

  public void setData(String data) {
//    String cleandata = data.replace(
    StringTokenizer tokens = new StringTokenizer( data, "\n" );
    String s = tokens.nextToken();
System.out.println("s = " + s);
    int i = 0;
    int group;
    double xval, yval;
    SOCRPoint2D mp;
    Points.removeAllElements();
    while ( tokens.hasMoreElements() ) {
      i++;
      s = tokens.nextToken();
      System.out.println("s = " + s);
      System.out.println("s.length = " + s.length());

      if ( s.length() > 1 ) {
        StringTokenizer tokens2 = new StringTokenizer(s,",");
        String s2 = tokens2.nextToken();
        System.out.println("s2 = " + s2);
        try {
          xval = Double.valueOf( s2 ).doubleValue();
        } catch (NumberFormatException e) {
          xval = 0.0;
        }
        if ( tokens2.hasMoreElements() ) {
          s2 = tokens2.nextToken();
          System.out.println("s2 = " + s2);
          try {
            yval = Double.valueOf( s2 ).doubleValue();
          } catch (NumberFormatException e) {
            yval = 0.0;
          }
          mp = new SOCRPoint2D(xval, yval);
System.out.println("my = " + mp);
          Points.addElement( mp );
        }
      }
    }
    computeFullRegression();
    repaint();
  }

/*
  public void addActionListener(ActionListener l) {
    actionListener = AWTEventMulticaster.add(actionListener, l);
    enableEvents(AWTEvent.ACTION_EVENT_MASK);
  }
*/

  public void setScatterPlot_Ranges() {
    xScatterPlot_Range = getXScatterPlot_Range();
    yScatterPlot_Range = getYScatterPlot_Range();
    xIntWidth = Math.floor( xScatterPlot_Range.width() / 10.0 );
    yIntWidth = Math.floor( yScatterPlot_Range.width() / 10.0 );
    if ( Math.abs( xIntWidth ) < 0.1 )
      xIntWidth = 0.5;
    if ( Math.abs( yIntWidth ) < 0.1 )
      yIntWidth = 0.5;
  }

  public void resetPoints() {
    xScatterPlot_Range = getXScatterPlot_Range();
    yScatterPlot_Range = getYScatterPlot_Range();
    xIntWidth = Math.floor( xScatterPlot_Range.width() / 10.0 );
    yIntWidth = Math.floor( yScatterPlot_Range.width() / 10.0 );
    if ( Math.abs( xIntWidth ) < 0.1 )
      xIntWidth = 0.5;
    if ( Math.abs( yIntWidth ) < 0.1 )
      yIntWidth = 0.5;
    computeFullRegression();
    if ( ShowAxis || ShowGrid && ( (Width>0) && (Height>0)) ) {
      startImage = createImage( Width, Height);
      startGraphics = startImage.getGraphics();
      initStartImage(startGraphics);
      startGraphics.dispose();
    }
    repaint();
  }

  public boolean toggleRegressionLine() {
    ShowRLine = !ShowRLine;
    repaint();
    return ShowRLine;
  }

  public boolean toggleCurrentCoordinate() {
    showCurrentCoordinate = !showCurrentCoordinate;
    repaint();
    return showCurrentCoordinate;
  }

  public boolean toggleErrorLines() {
    showErrorLines = !showErrorLines;
    repaint();
    return showErrorLines;
  }

  public void setOptions(ScatterPlotRange xScatterPlot_Range, ScatterPlotRange yScatterPlot_Range, 
         double xint, double yint) {
    //xScatterPlot_Range = xScatterPlot_Range;
    //yScatterPlot_Range = yScatterPlot_Range;
    xIntWidth = xint;
    yIntWidth = yint;
    computeFullRegression();
    if ( ShowAxis || ShowGrid ) {
      startImage = createImage( Width, Height);
      startGraphics = startImage.getGraphics();
      initStartImage(startGraphics);
      startGraphics.dispose();
    }
    repaint();
  }

  public void setCurrentPoint(SOCRPoint2D mp, int curr) {
    for (int i=0;i<Points.size();i++)
      ((SOCRPoint2D)Points.elementAt(i)).setSelected(false);
    mp.setSelected(true);
    currentPoint = curr;
    repaint();
  }

  public int getCurrentPoint() {
    return currentPoint;
  }

  int  math2screenY(double y) {
    double Y;
    Y = (yScatterPlot_Range.start-y)/yScatterPlot_Range.width()*(Height-TOPBORDER-BOTTOMBORDER)
      + Height - BOTTOMBORDER;
    return (int)(Y);
  }

  Point math2screen(double x, double y) {
    double X,Y;
    X = (x-xScatterPlot_Range.start)/xScatterPlot_Range.width()*(Width-LEFTBORDER-RIGHTBORDER)
      + LEFTBORDER;
    Y = (yScatterPlot_Range.start-y)/yScatterPlot_Range.width()*(Height-TOPBORDER-BOTTOMBORDER)
      + Height - BOTTOMBORDER;
    return new Point((int)(X), (int)(Y) );
  }

  Point math2screen(SOCRPoint2D pt) {
    double X,Y;
    X = (pt.x-xScatterPlot_Range.start)/xScatterPlot_Range.width()*(Width-LEFTBORDER-RIGHTBORDER)
      + LEFTBORDER;
    Y = (yScatterPlot_Range.start-pt.y)/yScatterPlot_Range.width()*(Height-TOPBORDER-BOTTOMBORDER)
      + Height - BOTTOMBORDER;
    return new Point((int)(X), (int)(Y) );
  }
 
  double screen2mathX(int x) {
    double X = (x-LEFTBORDER)/(double)(Width-LEFTBORDER-RIGHTBORDER) * 
          xScatterPlot_Range.width()+xScatterPlot_Range.start;
    return X;
  }

  double screen2mathY(int y) {
    return (Height-BOTTOMBORDER-y)/(double)(Height-TOPBORDER-BOTTOMBORDER)*
           yScatterPlot_Range.width()+yScatterPlot_Range.start;
  }
 
  void updateRegression(SOCRPoint2D oldpt) {
    if ( Points == null )
      return;
    int index = getCurrentPoint();
    SOCRPoint2D pt = (SOCRPoint2D)Points.elementAt(index);
    xy_product = xy_product - oldpt.x*oldpt.y + pt.x*pt.y;
    x_sum += pt.x - oldpt.x;
    y_sum += pt.y - oldpt.y;
    square_x_sum = square_x_sum - oldpt.x*oldpt.x + pt.x*pt.x;
    computeRegressionLine();
    repaint();
  }

  void computeFullRegression() {
    int i;
    xy_product = 0.0;
    x_sum = 0.0;
    y_sum = 0.0;
    square_x_sum = 0.0;
    square_y_sum = 0.0;

    if ( Points != null ) {
      for (i=0;i<Points.size();i++) {
        SOCRPoint2D pt = (SOCRPoint2D)Points.elementAt(i);
        xy_product += pt.x * pt.y;
        x_sum += pt.x;
        y_sum += pt.y;
        square_x_sum += pt.x*pt.x;
        square_y_sum += pt.y*pt.y;
      }
    }

    double Sx=-1.0, Sy = -1.0;
    if ( Points.size() > 1 ) {
      Sx = Math.sqrt((square_x_sum-x_sum*x_sum/Points.size())/(Points.size()-1));
      Sy = Math.sqrt((square_y_sum-y_sum*y_sum/Points.size())/(Points.size()-1));
    }

    computeRegressionLine();
    if ( Sx > 0 ) {
      Correlation = slope_calc*Sx/Sy;
      //SPlotUser.setCorrelation( Correlation );
    } else {
      //SPlotUser.setCorrelation( -1.0);
      Correlation = -2.0;
    }
  }

  void computeRegressionLine() {
    if (Points != null && Points.size()>1) {
      top_calc = ( (Points.size() * xy_product) - (x_sum * y_sum) );
      bottom_calc = ((Points.size() * square_x_sum) - (x_sum * x_sum));
      if ( Math.abs(bottom_calc)<0.0001 ) {
        RL1.setValues( 0.0, 0.0 );
        RL2.setValues( 0.0, 0.0 );
        return;
      }
      slope_calc = top_calc / bottom_calc;
      if ( Math.abs(slope_calc)>1000.0 ) {
        RL1.setValues( 0.0, 0.0 );
        RL2.setValues( 0.0, 0.0 );
        return;
      }

      y_intercept = (y_sum / Points.size()) - 
             (slope_calc * (x_sum / Points.size() ));
      if ( Math.abs( slope_calc ) < 0.001 ) {
        RL1.setValues(xScatterPlot_Range.start, y_intercept);
        RL2.setValues(xScatterPlot_Range.end, y_intercept);
        return;
      }

      if ( y_intercept< yScatterPlot_Range.start )
        RL1.setValues( (yScatterPlot_Range.start-y_intercept)/slope_calc, yScatterPlot_Range.start );
      else if ( y_intercept > yScatterPlot_Range.end )
        RL1.setValues( (yScatterPlot_Range.end-y_intercept)/slope_calc, yScatterPlot_Range.end );
      else
        RL1.setValues(0.0,y_intercept);

      double yEnd = slope_calc*(xScatterPlot_Range.end)+y_intercept;
      if ( yEnd < yScatterPlot_Range.start )
        RL2.setValues( (yScatterPlot_Range.start-y_intercept)/slope_calc, yScatterPlot_Range.start );
      else if ( yEnd > yScatterPlot_Range.end )
        RL2.setValues( (yScatterPlot_Range.end-y_intercept)/slope_calc, yScatterPlot_Range.end );
      else
        RL2.setValues(xScatterPlot_Range.end,yEnd);
    }
  }

  public void updateSnapPoints() {
    int xdec = NumXDec;
    double xfact = Math.pow(10.0, xdec );
    int ydec = NumYDec;
    double yfact = Math.pow(10.0, ydec );

    for (int i=0;i<Points.size();i++) {
      SOCRPoint2D mpt = (SOCRPoint2D)Points.elementAt(i);
      mpt.x = Math.round( mpt.x * xfact ) / xfact; 
      mpt.y = Math.round( mpt.y * yfact ) / yfact; 
    }
    computeFullRegression();
  }

    /** Implementation of PropertyChageListener.*/
    public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();
		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			JTable dataTable = (JTable)(e.getNewValue());
			setData(dataTable);
		}
   }

  public void mouseClicked(MouseEvent evt) {}
  public void mouseEntered(MouseEvent evt) {}
  public void mouseExited(MouseEvent evt) {}
  public void mousePressed(MouseEvent evt) {
    int x = evt.getX();
    int y = evt.getY();

    boolean Found = false;
    SOCRPoint2D mpt;

    for (int i=0;(i<Points.size()) && ! Found;i++) {
      mpt = (SOCRPoint2D)Points.elementAt(i);
      Point pt = math2screen(mpt.x,mpt.y);
      if ( Math.abs(pt.x-x)+Math.abs(pt.y-y) < 8 ) {
        setCurrentPoint(mpt,i);
        Found = true;
      }
    }

    if ( Found && Interactive )
      MovingPoint = true;

    if ( !Found && Interactive ) {
      mpt = new SOCRPoint2D( screen2mathX(x), screen2mathY(y) );
      if ( (mpt.x >= xScatterPlot_Range.start) && (mpt.y>=yScatterPlot_Range.start)
               && ( mpt.x <= xScatterPlot_Range.end) && (mpt.y <= yScatterPlot_Range.end ) ) {
        addPoint( mpt );
        MovingPoint = true;
      }
    }
    repaint();
  }
  public void mouseReleased(MouseEvent evt) {}

  public void mouseDragged(MouseEvent evt) {
    int x = evt.getX();
    int y = evt.getY();
    if ( Interactive ) {
      double sx = screen2mathX(x);
      double sy = screen2mathY(y);

      if ( sx < xScatterPlot_Range.start ) sx = xScatterPlot_Range.start;
      if ( sy < yScatterPlot_Range.start ) sy = yScatterPlot_Range.start;

      currXCoord = sx;
      currYCoord = sy;
      int dec = NumXDec;
      double factor = Math.pow(10.0, dec );
      currXCoord = Math.round( currXCoord * factor ) / factor; 
      dec = NumYDec;
      factor = Math.pow(10.0, dec );
      currYCoord = Math.round( currYCoord * factor ) / factor;
      sx = currXCoord;
      sy = currYCoord;

      if ( MovingPoint ) {
        if ( sx > xScatterPlot_Range.end )
          sx = xScatterPlot_Range.end;
        if ( sy > yScatterPlot_Range.end )
          sy = yScatterPlot_Range.end;
        changePoint( new SOCRPoint2D(sx,sy), getCurrentPoint() );
        computeFullRegression();
        repaint();
      }
    }
  }

  public void mouseMoved(MouseEvent evt) {
    int x = evt.getX();
    int y = evt.getY();
    if ( showCurrentCoordinate ) {
      double sx = screen2mathX(x);
      double sy = screen2mathY(y);

      if ( sx < xScatterPlot_Range.start ) sx = xScatterPlot_Range.start;
      if ( sy < yScatterPlot_Range.start ) sy = yScatterPlot_Range.start;
      currXCoord = sx;
      currYCoord = sy;

      int dec = NumXDec;
      double factor = Math.pow(10.0, dec );
      currXCoord = Math.round( currXCoord * factor ) / factor; 
      dec = NumYDec;
      factor = Math.pow(10.0, dec );
      currYCoord = Math.round( currYCoord * factor ) / factor;
      repaint();
    }
  }

  public void keyTyped(KeyEvent evt) {}
  public void keyPressed(KeyEvent evt) {}
  public void keyReleased(KeyEvent evt) {
    System.out.println("keyReleased: " + evt.getKeyCode() );
    int key = evt.getKeyCode();
    System.out.println("keyTyped: " + key);
    if ( key== KeyEvent.VK_SPACE )
      updatePoints();
    else if (Interactive && (key== KeyEvent.VK_D || key==KeyEvent.VK_DELETE))
      deleteCurrentPoint();
  }

  public double getSlope()
  { return slope_calc;
   }

  public double getYIntercept()
  { return y_intercept;
   }

  public double getXIntWidth() {
    return xIntWidth;
  }

  public double getYIntWidth() {
    return yIntWidth;
  }

  public ScatterPlotRange getXScatterPlot_Range() {
    double min, max;
    if ( Points == null || Points.size()==0 )
      return new ScatterPlotRange(0,10);
    SOCRPoint2D pt = (SOCRPoint2D)Points.elementAt( 0 );
    min = pt.x;
    max = pt.x;
    for (int i=1;i<Points.size();i++) {
      pt = (SOCRPoint2D)Points.elementAt( i );
      if ( pt.x < min )
        min = pt.x;
      if ( pt.x > max )
        max = pt.x;
    }
    min = Math.floor(min);
    max = Math.ceil(max);
    return new ScatterPlotRange(min,max);
  }

  public ScatterPlotRange getYScatterPlot_Range() {
    double min, max;
    if ( Points == null || Points.size()==0 )
      return new ScatterPlotRange(0,10);
    SOCRPoint2D pt = (SOCRPoint2D)Points.elementAt( 0 );
    min = pt.y;
    max = pt.y;
    for (int i=1;i<Points.size();i++) {
      pt = (SOCRPoint2D)Points.elementAt( i );
      if ( pt.y < min )  min = pt.y;
      if ( pt.y > max )  max = pt.y;
    }
    min = Math.floor(min);
    max = Math.ceil(max);
    return new ScatterPlotRange(min,max);
  }

  public void changePoint(SOCRPoint2D pt, int index) {
    SOCRPoint2D mp = (SOCRPoint2D)Points.elementAt(index);
    mp.x = pt.x;
    mp.y = pt.y;
    computeFullRegression();
    repaint();
  }

  public void removeAllPoints() {
    Correlation = -2.0;
    Points.removeAllElements();
    MovingPoint = false;
    updatePoints();
  }

  public void deleteCurrentPoint() {
    for (int i=Points.size()-1; i>=0; i--) {
      SOCRPoint2D pt = (SOCRPoint2D)Points.elementAt(i);
      if ( pt.isSelected() )
        Points.removeElementAt(i);
    }
    currentPoint -= 1;
    MovingPoint = false;
    updatePoints();
  }

  public void addPoint(SOCRPoint2D pt) {
    Points.addElement( pt );
    currentPoint = Points.size()-1;

    updatePoints();
    computeFullRegression();
    repaint();
  }

  public void paintComponent(Graphics g){
		super.paintComponent(g);

    // Initialize screen buffer for double buffering, if necessary
    if ( offscreenImage==null ||
         getSize().width!=FWidth || getSize().height!=FHeight ) {

      if ( offscreenGraphics!=null ) offscreenGraphics.dispose();
      FWidth = getSize().width;
      FHeight = getSize().height;
      Width = FWidth;
      Height = FHeight;
      offscreenImage = createImage( Width, Height);
      offscreenGraphics = offscreenImage.getGraphics();
      startImage = createImage( Width, Height);
      startGraphics = startImage.getGraphics();
      initStartImage(startGraphics);
      startGraphics.dispose();
    }

    // erase previous points
    offscreenGraphics.drawImage(startImage, 0, 0, this);

    // draw points
    offscreenGraphics.setColor(Color.red);
    for (int i = 0; i< Points.size(); i++) {
      SOCRPoint2D mp = (SOCRPoint2D)Points.elementAt(i);
      boolean found = false;

      Point pt = math2screen( mp );
      if ( mp.isSelected() )
        offscreenGraphics.setColor(Color.blue);
      else
        offscreenGraphics.setColor(Color.red);
      offscreenGraphics.fillOval(pt.x-3,pt.y-3,6,6);
      offscreenGraphics.setColor(Color.black);
      offscreenGraphics.drawOval(pt.x-3,pt.y-3,6,6);
    }

    offscreenGraphics.setColor(Color.green);

    Polygon poly = new Polygon();
    poly.addPoint( 0,  3);
    poly.addPoint(-3, -3);
    poly.addPoint( 3, -3);

    if ( showErrorLines ) {
      offscreenGraphics.setColor(new Color(128,128,128) );   
      for (int i = 0; i< Points.size(); i++) {
        SOCRPoint2D mp = (SOCRPoint2D)Points.elementAt(i);
        Point pt1 = math2screen( mp );
        Point pt2 = math2screen( mp.x, regressionLineValueAt( mp.x ) );
        offscreenGraphics.drawLine(pt1.x,pt1.y,pt2.x,pt2.y);
      }
    }

    offscreenGraphics.setColor(Color.black);
    // draw regression line
    if (ShowRLine && Points.size() > 1){
      Point pt1, pt2;
      pt1 = math2screen(RL1);
      pt2 = math2screen(RL2);
      offscreenGraphics.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
    }

    if ( showCurrentCoordinate ) {
      Point p;
      p = math2screen(currXCoord, currYCoord);
      offscreenGraphics.setColor(new Color(0,139,139) );
      offscreenGraphics.fillOval(p.x-3,p.y-3,6,6);
      offscreenGraphics.drawString("("+currXCoord+","+currYCoord+")",p.x+3,p.y);
    }

    g.drawImage(offscreenImage, 0, 0, this);
  }

  public double regressionLineValueAt(double x) {
    if ( Math.abs(RL2.x-RL1.x) > 0.05 ) {
      double m = (RL2.y-RL1.y)/(RL2.x-RL1.x);
      return RL1.y + m * (x-RL1.x);
    } else
      return 0.0;
  }

  public int getXMinLocation() {
    return getLocation().x + math2screen(xScatterPlot_Range.start,0).x;
  }
  public int getXMaxLocation() {
    return getLocation().x + math2screen(xScatterPlot_Range.end,0).x;
  }
  public int getYMinLocation() {
    return getLocation().y + math2screen(0,yScatterPlot_Range.start).y;
  }
  public int getYMaxLocation() {
    return getLocation().y + math2screen(0,yScatterPlot_Range.end).y;
  }

  void initStartImage(Graphics g) {
    Point pt1, pt2, pt3;
    Color GridColor = new Color( 215, 215, 215 );
    double xincr, yincr;
    g.setColor(Color.white);
    g.fillRect(0, 0, Width, Height );
    g.setColor(Color.black);
    g.setFont(font);

    xincr = xIntWidth;
    yincr = yIntWidth;

    for (double x= xScatterPlot_Range.start; x<=xScatterPlot_Range.end;x+=xincr) {
      if ( ShowGrid ) {
   pt1 = math2screen(x,yScatterPlot_Range.start);
   pt2 = math2screen(x,yScatterPlot_Range.end);
   g.setColor(GridColor);
   g.drawLine(pt1.x,pt1.y,pt2.x,pt2.y);
   Point pt = math2screen(x,yScatterPlot_Range.start);
   String xstr = ""+ Math.round(x*100)/100.0;
   g.setColor(Color.black);
   g.drawString(xstr,pt.x-fm.stringWidth(xstr)/2,
           pt.y+fm.getAscent()+3 );
      }
    }

    for (double y=yScatterPlot_Range.start; y<=yScatterPlot_Range.end; y+=yincr) {
      if ( ShowGrid ) {
   pt1 = math2screen(xScatterPlot_Range.start,y);
   pt2 = math2screen(xScatterPlot_Range.end,y);
   g.setColor(GridColor);
   g.drawLine(pt1.x,pt1.y,pt2.x,pt2.y);
   Point pt = math2screen(xScatterPlot_Range.start,y);
   String ystr = ""+Math.round(y*100)/100.0;
   g.setColor(Color.black);
   g.drawString(ystr,pt.x-fm.stringWidth(ystr),
           pt.y+fm.getAscent()/2 );
      }
    }

    pt1 = math2screen( xScatterPlot_Range.start, yScatterPlot_Range.start );
    pt2 = math2screen( xScatterPlot_Range.start, yScatterPlot_Range.end );
    pt3 = math2screen( xScatterPlot_Range.end, yScatterPlot_Range.end );
    g.setColor( Color.blue );
    g.drawRect( pt2.x, pt2.y, pt3.x - pt2.x-1, pt1.y - pt3.y );

    if ( ShowAxis ) {
      g.setColor(Color.black);
      pt1 = math2screen( 0, yScatterPlot_Range.start );
      pt2 = math2screen( 0, yScatterPlot_Range.end );
      g.drawLine(pt1.x,pt1.y,pt2.x,pt2.y);

      pt1 = math2screen(xScatterPlot_Range.start, 0);
      pt2 = math2screen(xScatterPlot_Range.end, 0);
      g.drawLine(pt1.x,pt1.y,pt2.x,pt2.y);
    }
  }

  public void update(Graphics g) {
    repaint();
  }

  void print(String s) {
    System.out.println(s);
  }

  public Dimension getMinimumSize() {
    return new Dimension( 300, 300 );
  }
  
  public Dimension getPreferredSize() {
    return new Dimension( 300, 300 );
  }
}