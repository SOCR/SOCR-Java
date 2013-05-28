package JSci.awt;

import java.awt.*;
import java.awt.event.*;

/**
* A 3D line graph AWT component.
* @author Daniel Lemire
*/
public final class LineGraph3D extends DoubleBufferedCanvas
  implements Runnable, MouseListener, MouseMotionListener, GraphDataListener {
    protected Graph3DModel model;
    private Model3D md;
    private boolean painted = true;
    private float xfac;
    private int prevx, prevy;
    private float xtheta, ytheta;
    private float scalefudge = 1.0f;
    private Matrix3D amat = new Matrix3D(), tmat = new Matrix3D();
    private String mdname = null;
    private String message = null;

        /**
        * Constructs a 3D line graph.
        */
    public LineGraph3D(Graph3DModel data) {
      amat.yrot(20);
      amat.xrot(20);
      setSize(getSize().width <= 20 ? 400 : getSize().width,
             getSize().height <= 20 ? 400 : getSize().height);
      addMouseListener(this);
      addMouseMotionListener(this);
      setModel(data);
    }


    /**
    * Sets the data plotted by this graph to the specified data.
    */
    public void setModel(Graph3DModel gm) {
            if(model!=null)
              model.removeGraphDataListener(this);
            model=gm;
            model.addGraphDataListener(this);
            dataChanged(new GraphDataEvent(model));
    }

    public void dataChanged(GraphDataEvent e) {
      md=new Model3D();
      model.firstSeries();
      for(int k=0;k<model.seriesLength();k++) {
        md.addVert(model.getXCoord(k),model.getYCoord(k),model.getZCoord(k));
      }
      for(int k=1;k<model.seriesLength();k++) {
        md.add(k-1,k);
      }
      redraw();
      new Thread(this).start();
    }

    public void run() {
      try {
          md.findBB();
          md.compress();
          float xw = md.xmax - md.xmin;
          float yw = md.ymax - md.ymin;
          float zw = md.zmax - md.zmin;
          if (yw > xw)
        xw = yw;
          if (zw > xw)
        xw = zw;
          float f1 = getSize().width / xw;
          float f2 = getSize().height / xw;
          xfac = 0.7f * (f1 < f2 ? f1 : f2) * scalefudge;
      } catch(Exception e) {
          md = null;
          message = e.toString();
          e.printStackTrace();
      }
    	repaint();
    }

    public  void mouseClicked(MouseEvent e) {
    }
    public  void mousePressed(MouseEvent e) {
        prevx = e.getX();
        prevy = e.getY();
        e.consume();
    }
    public  void mouseReleased(MouseEvent e) {
    }
    public  void mouseEntered(MouseEvent e) {
    }
    public  void mouseExited(MouseEvent e) {
    }
    public  void mouseDragged(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      if((e.getModifiers() & MouseEvent.BUTTON3_MASK)!=0) {
        if(prevy==y)
          return; //don't need to do anything
        scalefudge+=(prevy-y)/(float)getSize().width;
        if(scalefudge==0)
          scalefudge=0.1f;//arbitrary
        float xw = md.xmax - md.xmin;
        float yw = md.ymax - md.ymin;
        float zw = md.zmax - md.zmin;
        if (yw > xw)
          xw = yw;
        if (zw > xw)
          xw = zw;
        float f1 = getSize().width / xw;
        float f2 = getSize().height / xw;
        xfac = 0.7f * (f1 < f2 ? f1 : f2) * scalefudge;

      }
      if((e.getModifiers() & MouseEvent.BUTTON1_MASK)!=0) {
          tmat.unit();
          float xtheta = (prevy - y) * 360.0f / getSize().width;
          float ytheta = (x - prevx) * 360.0f / getSize().height;
          tmat.xrot(xtheta);
          tmat.yrot(ytheta);
          amat.mult(tmat);
      }
      if (painted) {
          painted = false;
          redraw();
      }
      prevx = x;
      prevy = y;
      e.consume();

    }

    public  void mouseMoved(MouseEvent e) {
    }
        /**
        * Paint the graph.
        */
        protected void offscreenPaint(Graphics g) {
                if (md != null) {
//                        System.out.println("painting");
                        md.mat.unit();
                        md.mat.translate(-(md.xmin + md.xmax) / 2,
                                -(md.ymin + md.ymax) / 2,
                                -(md.zmin + md.zmax) / 2);
                        md.mat.mult(amat);
                        md.mat.scale(xfac, -xfac, 16 * xfac / getSize().width);
                        md.mat.translate(getSize().width / 2, getSize().height / 2, 8);
                        md.transformed = false;
                        md.paint(g);
//                        System.out.println("painting done");
                        setPainted();
                } else if (message != null) {
                        g.drawString("Error in model:", 3, 20);
                        g.drawString(message, 10, 40);
                }
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
                return new Dimension(400,400);
        }

    private synchronized void setPainted() {
    	painted = true;
    	notifyAll();
    }
}

class Model3D {
        private static Color greys[];
        float vert[];
        int tvert[];
        int nvert, maxvert;
        int con[];
        int ncon, maxcon;
        boolean transformed;
        Matrix3D mat;
        float xmin, xmax, ymin, ymax, zmin, zmax;
        private float axes[]=new float[18];
        private int taxes[]=new int[18];

        static {
                greys=new Color[16];
                int gr;
                for(int i=0;i<greys.length;i++) {
                        gr=(int)(170.0*(1.0-Math.pow(i/15.0,2.3)));
                        greys[i]=new Color(gr,gr,gr);
                }
        }

    Model3D() {
	mat=new Matrix3D();
	mat.xrot(20);
	mat.yrot(30);
    }

    /** Add a vertex to this model */
    int addVert(float x, float y, float z) {
	int i = nvert;
	if (i >= maxvert)
	    if (vert == null) {
		maxvert = 100;
		vert = new float[maxvert * 3];
	    } else {
		maxvert *= 2;
		float nv[] = new float[maxvert * 3];
		System.arraycopy(vert, 0, nv, 0, vert.length);
		vert = nv;
	    }
	i *= 3;
	vert[i] = x;
	vert[i + 1] = y;
	vert[i + 2] = z;
//  System.out.println("added a vertex "+nvert);
	return nvert++;
    }
    /** Add a line from vertex p1 to vertex p2 */
    void add(int p1, int p2) {
	int i = ncon;
	if (p1 >= nvert || p2 >= nvert) {
//      System.out.println("Line from "+p1+" to "+p2+" will not be drawn. ("+nvert+")");
	    return;
  }
	if (i >= maxcon)
	    if (con == null) {
		maxcon = 100;
		con = new int[maxcon];
	    } else {
		maxcon *= 2;
		int nv[] = new int[maxcon];
		System.arraycopy(con, 0, nv, 0, con.length);
		con = nv;
	    }
	if (p1 > p2) {
	    int t = p1;
	    p1 = p2;
	    p2 = t;
	}
	con[i] = (p1 << 16) | p2;
	ncon = i + 1;
    }
    /** Transform all the points in this model */
    void transform() {
	if (transformed || nvert <= 0)
	    return;
        mat.transform(axes,taxes,6);
	if (tvert == null || tvert.length < nvert * 3)
	    tvert = new int[nvert*3];
	mat.transform(vert, tvert, nvert);
	transformed = true;
    }

   /* Quick Sort implementation
    */
   private void quickSort(int a[], int left, int right)
   {
      int leftIndex = left;
      int rightIndex = right;
      int partionElement;
      if ( right > left)
      {

         /* Arbitrarily establishing partition element as the midpoint of
          * the array.
          */
         partionElement = a[ ( left + right ) / 2 ];

         // loop through the array until indices cross
         while( leftIndex <= rightIndex )
         {
            /* find the first element that is greater than or equal to
             * the partionElement starting from the leftIndex.
             */
            while( ( leftIndex < right ) && ( a[leftIndex] < partionElement ) )
               ++leftIndex;

            /* find an element that is smaller than or equal to
             * the partionElement starting from the rightIndex.
             */
            while( ( rightIndex > left ) &&
                   ( a[rightIndex] > partionElement ) )
               --rightIndex;

            // if the indexes have not crossed, swap
            if( leftIndex <= rightIndex )
            {
               swap(a, leftIndex, rightIndex);
               ++leftIndex;
               --rightIndex;
            }
         }

         /* If the right index has not reached the left side of array
          * must now sort the left partition.
          */
         if( left < rightIndex )
            quickSort( a, left, rightIndex );

         /* If the left index has not reached the right side of array
          * must now sort the right partition.
          */
         if( leftIndex < right )
            quickSort( a, leftIndex, right );

      }
   }

   private void swap(int a[], int i, int j)
   {
      int T;
      T = a[i];
      a[i] = a[j];
      a[j] = T;
   }

    /** eliminate duplicate lines */
    void compress() {
	int limit = ncon;
	int c[] = con;
	quickSort(con, 0, ncon - 1);
	int d = 0;
	int pp1 = -1;
	for (int i = 0; i < limit; i++) {
	    int p1 = c[i];
	    if (pp1 != p1) {
		c[d] = p1;
		d++;
	    }
	    pp1 = p1;
	}
	ncon = d;
    }

    void paint(Graphics g) {
//      System.out.println("painting model 3D");
	    if (vert == null || nvert <= 0)
	      return;
	    transform();
	    Color lastGrey=null;
	    int lim = ncon;
	    int c[] = con;
	    int v[] = tvert;
//      System.out.println(lim+" "+nvert);
	    if (lim <= 0 || nvert <= 0)
	      return;
// draw axes
                int a[]=taxes;
                g.setColor(depthColor(a[2],a[5]));
                g.drawLine(a[0],a[1],a[3],a[4]);
                g.setColor(depthColor(a[8],a[11]));
                g.drawLine(a[6],a[7],a[9],a[10]);
                g.setColor(depthColor(a[14],a[17]));
                g.drawLine(a[12],a[13],a[15],a[16]);
// draw points
                int T,p1,p2;
                Color grey;
                for(int i=0;i<lim;i++) {
                        T=c[i];
                        p1=((T >> 16) & 0xFFFF)*3;
                        p2=(T & 0xFFFF)*3;
                        grey=depthColor(v[p1+2],v[p2+2]);
                        if(grey!=lastGrey) {
                                lastGrey=grey;
                                g.setColor(grey);
                        }
//        System.out.println("drawing line "+v[p1]+" "+v[p1 + 1]+" "+v[p2]+" "+v[p2 + 1]);
                        g.drawLine(v[p1], v[p1 + 1],v[p2], v[p2 + 1]);
                }
        }
        /**
        * Converts z coordinates to shades of grey.
        * @param z1 z coord of start of line.
        * @param z2 z coord of end of line.
        */
        private Color depthColor(int z1,int z2) {
                int gr=z1+z2;
                if(gr<0)
                        gr=0;
                else if(gr>15)
                        gr=15;
                return greys[gr];
        }

    /** Find the bounding box of this model */
    void findBB() {
	if (nvert <= 0)
	    return;
	float v[] = vert;
	float xmin = v[0], xmax = xmin;
	float ymin = v[1], ymax = ymin;
	float zmin = v[2], zmax = zmin;
	for (int i = nvert * 3; (i -= 3) > 0;) {
	    float x = v[i];
	    if (x < xmin)
		xmin = x;
	    if (x > xmax)
		xmax = x;
	    float y = v[i + 1];
	    if (y < ymin)
		ymin = y;
	    if (y > ymax)
		ymax = y;
	    float z = v[i + 2];
	    if (z < zmin)
		zmin = z;
	    if (z > zmax)
		zmax = z;
	}
	this.xmax = xmax;
	this.xmin = xmin;
	this.ymax = ymax;
	this.ymin = ymin;
	this.zmax = zmax;
	this.zmin = zmin;
        axes[0]=xmax;
        axes[3]=xmin;
        axes[7]=ymax;
        axes[10]=ymin;
        axes[14]=zmax;
        axes[17]=zmin;
    }
}

class Matrix3D {
    float xx, xy, xz, xo;
    float yx, yy, yz, yo;
    float zx, zy, zz, zo;
    /** Create a new unit matrix */
    Matrix3D () {
	xx = 1.0f;
	yy = 1.0f;
	zz = 1.0f;
    }
    /** Scale by f in all dimensions */
    void scale(float f) {
	xx *= f;
	xy *= f;
	xz *= f;
	xo *= f;
	yx *= f;
	yy *= f;
	yz *= f;
	yo *= f;
	zx *= f;
	zy *= f;
	zz *= f;
	zo *= f;
    }
    /** Scale along each axis independently */
    void scale(float xf, float yf, float zf) {
	xx *= xf;
	xy *= xf;
	xz *= xf;
	xo *= xf;
	yx *= yf;
	yy *= yf;
	yz *= yf;
	yo *= yf;
	zx *= zf;
	zy *= zf;
	zz *= zf;
	zo *= zf;
    }
    /** Translate the origin */
    void translate(float x, float y, float z) {
	xo += x;
	yo += y;
	zo += z;
    }
    /** rotate theta degrees about the y axis */
    void yrot(double theta) {
	theta *= (Math.PI / 180.0);
	double ct = Math.cos(theta);
	double st = Math.sin(theta);

	float Nxx = (float) (xx * ct + zx * st);
	float Nxy = (float) (xy * ct + zy * st);
	float Nxz = (float) (xz * ct + zz * st);
	float Nxo = (float) (xo * ct + zo * st);

	float Nzx = (float) (zx * ct - xx * st);
	float Nzy = (float) (zy * ct - xy * st);
	float Nzz = (float) (zz * ct - xz * st);
	float Nzo = (float) (zo * ct - xo * st);

	xo = Nxo;
	xx = Nxx;
	xy = Nxy;
	xz = Nxz;
	zo = Nzo;
	zx = Nzx;
	zy = Nzy;
	zz = Nzz;
    }
    /** rotate theta degrees about the x axis */
    void xrot(double theta) {
	theta *= (Math.PI / 180.0);
	double ct = Math.cos(theta);
	double st = Math.sin(theta);

	float Nyx = (float) (yx * ct + zx * st);
	float Nyy = (float) (yy * ct + zy * st);
	float Nyz = (float) (yz * ct + zz * st);
	float Nyo = (float) (yo * ct + zo * st);

	float Nzx = (float) (zx * ct - yx * st);
	float Nzy = (float) (zy * ct - yy * st);
	float Nzz = (float) (zz * ct - yz * st);
	float Nzo = (float) (zo * ct - yo * st);

	yo = Nyo;
	yx = Nyx;
	yy = Nyy;
	yz = Nyz;
	zo = Nzo;
	zx = Nzx;
	zy = Nzy;
	zz = Nzz;
    }
    /** rotate theta degrees about the z axis */
    void zrot(double theta) {
	theta *= (Math.PI / 180.0);
	double ct = Math.cos(theta);
	double st = Math.sin(theta);

	float Nyx = (float) (yx * ct + xx * st);
	float Nyy = (float) (yy * ct + xy * st);
	float Nyz = (float) (yz * ct + xz * st);
	float Nyo = (float) (yo * ct + xo * st);

	float Nxx = (float) (xx * ct - yx * st);
	float Nxy = (float) (xy * ct - yy * st);
	float Nxz = (float) (xz * ct - yz * st);
	float Nxo = (float) (xo * ct - yo * st);

	yo = Nyo;
	yx = Nyx;
	yy = Nyy;
	yz = Nyz;
	xo = Nxo;
	xx = Nxx;
	xy = Nxy;
	xz = Nxz;
    }
    /** Multiply this matrix by a second: M = M*R */
    void mult(Matrix3D rhs) {
	float lxx = xx * rhs.xx + yx * rhs.xy + zx * rhs.xz;
	float lxy = xy * rhs.xx + yy * rhs.xy + zy * rhs.xz;
	float lxz = xz * rhs.xx + yz * rhs.xy + zz * rhs.xz;
	float lxo = xo * rhs.xx + yo * rhs.xy + zo * rhs.xz + rhs.xo;

	float lyx = xx * rhs.yx + yx * rhs.yy + zx * rhs.yz;
	float lyy = xy * rhs.yx + yy * rhs.yy + zy * rhs.yz;
	float lyz = xz * rhs.yx + yz * rhs.yy + zz * rhs.yz;
	float lyo = xo * rhs.yx + yo * rhs.yy + zo * rhs.yz + rhs.yo;

	float lzx = xx * rhs.zx + yx * rhs.zy + zx * rhs.zz;
	float lzy = xy * rhs.zx + yy * rhs.zy + zy * rhs.zz;
	float lzz = xz * rhs.zx + yz * rhs.zy + zz * rhs.zz;
	float lzo = xo * rhs.zx + yo * rhs.zy + zo * rhs.zz + rhs.zo;

	xx = lxx;
	xy = lxy;
	xz = lxz;
	xo = lxo;

	yx = lyx;
	yy = lyy;
	yz = lyz;
	yo = lyo;

	zx = lzx;
	zy = lzy;
	zz = lzz;
	zo = lzo;
    }

    /** Reinitialize to the unit matrix */
    void unit() {
	xo = 0;
	xx = 1;
	xy = 0;
	xz = 0;
	yo = 0;
	yx = 0;
	yy = 1;
	yz = 0;
	zo = 0;
	zx = 0;
	zy = 0;
	zz = 1;
    }
    /** Transform nvert points from v into tv.  v contains the input
        coordinates in floating point.  Three successive entries in
	the array constitute a point.  tv ends up holding the transformed
	points as integers; three successive entries per point */
    void transform(float v[], int tv[], int nvert) {
	float lxx = xx, lxy = xy, lxz = xz, lxo = xo;
	float lyx = yx, lyy = yy, lyz = yz, lyo = yo;
	float lzx = zx, lzy = zy, lzz = zz, lzo = zo;
	for (int i = nvert * 3; (i -= 3) >= 0;) {
	    float x = v[i];
	    float y = v[i + 1];
	    float z = v[i + 2];
	    tv[i    ] = (int) (x * lxx + y * lxy + z * lxz + lxo);
	    tv[i + 1] = (int) (x * lyx + y * lyy + z * lyz + lyo);
	    tv[i + 2] = (int) (x * lzx + y * lzy + z * lzz + lzo);
	}
    }
    public String toString() {
	return ("[" + xo + "," + xx + "," + xy + "," + xz + ";"
		+ yo + "," + yx + "," + yy + "," + yz + ";"
		+ zo + "," + zx + "," + zy + "," + zz + "]");
    }
}

