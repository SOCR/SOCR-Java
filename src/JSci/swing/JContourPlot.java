package JSci.swing;

import java.awt.*;
import javax.swing.*;
import JSci.awt.*;

/**
* A contour plot Swing component.
* @author Daniel Lemire
*/
public final class JContourPlot extends JDoubleBufferedComponent implements ColorScheme {
	private float data[][];
	private double min, max;
	private int deltay;
  private ColorScheme CS;
      //modifié
	private double[] deltax;
      //modifié
  private float c1=1;
	private float c2=1;
	private float c3=1;
	private int Contourx=2;
	private int Contoury=2;
	private int largeurMax;
	private int hauteurMax;
  //modifié
  private int ColorScaleWidth=40;
  private String MaxString, MinString, MiddleString;

        /**
        * Constructs a contour plot.
        * @param z an array of the form <code>z[i][j] = f(x<sub>i</sub>, y<sub>j</sub>)</code>.
        */
	public JContourPlot(double z[][]) {
                super();
                CS=this;
                setBackground(Color.white);
                setData(z);
                setContourX(1);
                setContourY(1);
	}
        /**
        * Sets the data plotted by this JContourPlot to the specified data.
        */
	public void setData(double feed[][]) {
    //invert the rows
                double[][] array=new double[feed.length][];
                for(int k=0;k<array.length;k++) {
                        array[k]=feed[array.length-k-1];
                }
		min=array[0][0];
		max=array[0][0];
		data=new float[array.length][];
		for(int k=0;k<array.length;k++) {
			data[k]=new float[array[k].length];
			for(int l=0;l<array[k].length;l++) {
				if(array[k][l]>max)
					max=array[k][l];
				if(array[k][l]<min)
					min=array[k][l];
			}
		}
		if(max==min) {
			for(int k=0;k<array.length;k++) {
				for(int l=0;l<array[k].length;l++) {
					data[k][l]=1;
				}
			}
		} else {
			Double D;
			for(int k=0;k<array.length;k++) {
				for(int l=0;l<array[k].length;l++) {
					D=new Double(1-(array[k][l]-min)/(max-min));
					data[k][l]=D.floatValue();
				}
			}
		}

    //modifié
    int precision=getPrecision(max-min);
    MaxString=Double.toString(round(max,precision));
    MinString=Double.toString(round(min,precision));
    MiddleString=Double.toString(round((max+min)/2.0,precision));
    //modifié
		rescale();
	}

  private int getPrecision (double d) {
    d=Math.abs(d);
    int ans=0; double compare=1;
    if(d<1) {
      while(compare>d){
        compare/=10;
        ans--;
      }
      return(ans);
    }
    while(compare<d) {
      compare*=10;
      ans++;
    }
    return(ans);
  }

  private double round(double d,int k) {
    if(d==0)
      return(0);
    double sign=d/Math.abs(d);
    d=Math.abs(d);
    if(k<0) {
      int k1=k;
      while(k1<0) {
        k1++;
        d*=10;
      }
      d=Math.round(d);
      while(k<0) {
        k++;
        d/=10;
      }
      return(d*sign);
    }
    int k1=k;
    while(k1<0) {
      k1++;
      d/=10;
    }
    d=Math.round(d);
    while(k<0) {
      k++;
      d*=10;
    }
    return(d*sign);

  }
        /**
        * Returns the preferred size of this component.
        */
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
        /**
        * Returns the minimum size of this component.
        */
	public Dimension getMinimumSize() {
		int widthMax=data[0].length;
		for(int k=1;k<data.length;k++) {
			if(data[k].length>widthMax)
				widthMax=data[k].length;
		}
		return new Dimension(widthMax+3*Contourx+ColorScaleWidth+5,(int)Math.max(data.length*2+2*Contoury,100));
	}
        /**
        * There is a box around the graph, this
        * sets how wide it will be in the horizontal direction.
        * @param k width (in pixel)
        * @exception IllegalArgumentException if the width is not at least one.
        */
	public void setContourX(int k) {
		if(k<1)
                        throw new IllegalArgumentException("This parameter must be greater than 1 : "+k+" < 1");
		Contourx=k;
	}
        /**
        * There is a box around the graph, this
        * sets how wide it will be in the vertical direction.
        * @param k width (in pixel).
        * @exception IllegalArgumentException if the width is not at least one.
        */
	public void setContourY(int k) {
		if(k<1)
			throw new IllegalArgumentException("This parameter must be greater than 1 : "+k+" < 1");
		Contoury=k;
	}
	public void setBounds(int x,int y,int width,int height) {
		super.setBounds(x,y,width,height);
		rescale();
	}

	private void rescale() {
    int thiswidth=getWidth();
    int thisheight=getHeight();
    Dimension s=getMinimumSize();
    thiswidth=Math.max(thiswidth,s.width);
    thisheight=Math.max(thisheight,s.height);
		int largeur=thiswidth-3*Contourx;
		int hauteur=thisheight-2*Contoury-ColorScaleWidth;
		deltay=(int) Math.floor(hauteur/data.length);
		hauteurMax=deltay*data.length;
      //modifié
		deltax=new double[data.length];
      //modifié
		largeurMax=0;
    int MaxNumberOfElements=data[0].length;
    for(int k=1;k<data.length;k++) {
      MaxNumberOfElements=Math.max(data[k].length,MaxNumberOfElements);
    }
    int UsableWidth=(int)Math.floor(largeur/(double)MaxNumberOfElements)*MaxNumberOfElements;
		for(int k=0;k<data.length;k++) {
			deltax[k]=UsableWidth/(double)data[k].length;
      largeurMax=Math.max(largeurMax,(int)Math.floor(data[k].length*deltax[k]));
		}
		redraw();
	}

        /**
        * Set the color for the contour of the graph.
        * @exception IllegalArgumentException if
        *   one of the parameters is not between 0 and 1.
        * @exception IllegalArgumentException if
        *   all three arguments are set to zero.
        */
	public void setColor(float x1,float x2, float x3) {
		if((x1<0)||(x1>1)||(x2<0)||(x2>1)||(x3<0)||(x3>1)) {
			throw new IllegalArgumentException("Incorrect parameters : "+x1+", "+x2+", "+x3);
		}
    if(x1+x2+x3==0)  {
      throw new IllegalArgumentException("You have chosen black at the specified color. This would generate a completly black graph. Please choose another color.");
    }
		c1=x1;c2=x2;c3=x3;
	}

  public Color getColor(float f) {
    if((f<0)||(f>1))
      throw new IllegalArgumentException("Color are given for values between 0 and 1 : "+f);
    return(Color.getHSBColor(f,1f,1f));
  }

  public void setColorScheme (ColorScheme cs) {
    CS=cs;
  }
        /**
        * Paint the graph.
        */
	protected void offscreenPaint(Graphics g) {
    int FontHeight=g.getFontMetrics(g.getFont()).getHeight();
		g.setColor(new Color(1f-c1,1f-c2,1f-c3));
		g.drawRect(Contourx+ColorScaleWidth,Contoury-1,largeurMax,hauteurMax);
 		g.drawRect(Contourx-1,Contoury-1,ColorScaleWidth,hauteurMax);
    g.setClip(Contourx,Contoury,ColorScaleWidth-1,hauteurMax-1);
    //double step255=hauteurMax/255;
    for(float y=0;y<hauteurMax;y+=hauteurMax/255.0f) {
      g.setColor(CS.getColor(y/(float)hauteurMax));
      g.fillRect(Contourx,Contoury+(int)Math.floor(y),ColorScaleWidth-1,2);
    }
    g.setColor(Color.black);
    g.drawString(MaxString, Contourx,+FontHeight+Contoury);
    g.drawString(MiddleString, Contourx,+Contoury+(int)Math.round((hauteurMax+FontHeight)/2.0));
    g.drawString(MinString, Contourx,hauteurMax-1);
		g.setClip(2*Contourx+ColorScaleWidth,Contoury,largeurMax-1,hauteurMax-1);
    for(int k=0;k<data.length;k++) {
			for(int l=0;l<data[k].length;l++) {
				g.setColor(CS.getColor(data[k][l]));
				g.fillRect((int)Math.floor(l*deltax[k])+2*Contourx+ColorScaleWidth,k*deltay+Contoury,(int)Math.floor((l+1.0)*deltax[k])+Contourx,(k+1)*deltay+Contoury);
			}
      //modifié
      g.setColor(this.getBackground());
      g.fillRect((int) Math.floor(data[k].length*deltax[k])+2*Contourx+ColorScaleWidth,k*deltay+Contoury,largeurMax-1,(k+1)*deltay+Contoury);
      //modifié
		}
	}
}

