package JSci.awt;

import java.awt.*;

/**
* A contour plot AWT component.
* @author Daniel Lemire
*/
public final class ContourPlot extends DoubleBufferedCanvas {
	private float data[][];
	private double min, max;
	private int deltay;
	private int[] deltax;
        private float c1=1;
	private float c2=1;
	private float c3=1;
	private int Contourx=1;
	private int Contoury=1;
	private int largeurMax;
	private int hauteurMax;

        /**
        * Constructs a contour plot.
        * @param z an array of the form <code>z[i][j] = f(x<sub>i</sub>, y<sub>j</sub>)</code>.
        */
	public ContourPlot(double z[][]) {
                setBackground(Color.white);
                setData(z);
                setContourX(1);
                setContourY(1);
	}
        /**
        * Sets the data plotted by this ContourPlot to the specified data.
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
		rescale();
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
		int largeurMax=data[0].length;
		for(int k=1;k<data.length;k++) {
			if(data[k].length>largeurMax) {
				largeurMax=data[k].length;
			}
		}
		return new Dimension(largeurMax+2*Contourx,data.length+2*Contoury);
	}
        /**
        * There is a box around the graph, this
        * sets how wide it will be in the horizontal direction.
        * @param k width (in pixel).
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
		Dimension d=getSize();
		int largeur=d.width-2*Contourx;
		int hauteur=d.height-2*Contoury;
		deltay=(int) Math.floor(hauteur/data.length);
		hauteurMax=deltay*data.length;
		deltax=new int[data.length];
		largeurMax=0;
		for(int k=0;k<data.length;k++) {
			deltax[k]=(int) Math.floor(largeur/data[k].length);
			if(data[k].length*deltax[k]>largeurMax) largeurMax=data[k].length*deltax[k];
		}
		redraw();
	}

        /**
        * Set the color for the graph. Note that the
        * box around the graph has this same color.
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
        /**
        * Paint the graph.
        */
	protected void offscreenPaint(Graphics g) {
		g.setColor(new Color(1f-c1,1f-c2,1f-c3));
		g.drawRect(Contourx-1,Contoury-1,largeurMax,hauteurMax);
		g.setClip(Contourx,Contoury,largeurMax-1,hauteurMax-1);
    for(int k=0;k<data.length;k++) {
			for(int l=0;l<data[k].length;l++) {
        g.setColor(Color.getHSBColor(data[k][l],1f,1f));
				g.fillRect(l*deltax[k]+Contourx,k*deltay+Contoury,(l+1)*deltax[k]+Contourx,(k+1)*deltay+Contoury);
			}
		}
	}
}

