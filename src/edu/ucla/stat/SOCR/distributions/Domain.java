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

package edu.ucla.stat.SOCR.distributions;

/**
 * This class defines a partition of an interval into subintervals of equal
 * width. These objects are used to define default domains. A finite domain can
 * be modeled by the values (midpoints) of the partition. The boundary points
 * are a + i * w for i = 0, ..., n, where n is the size of the partition, a is
 * the lower bound and w the width. The values (midpoints) are a + (i + 1/2) *
 * w, for i = 0, ..., n - 1.
 */
public class Domain {

    /**
     * @uml.property name="lowerBound"
     */
    //Variables
    public double lowerBound;

    /**
     * @uml.property name="upperBound"
     */
    //Variables
    public double upperBound;

    /**
     * @uml.property name="width"
     */
    //Variables
    public double width;

    /**
     * @uml.property name="lowerValue"
     */
    //Variables
    public double lowerValue;

    /**
     * @uml.property name="upperValue"
     */
    //Variables
    public double upperValue;

    /**
     * @uml.property name="size"
     */
    private int size;

    /**
     * @uml.property name="type"
     */
    private int type;

    public final static int DISCRETE = 0;
    public final static int CONTINUOUS = 1;

    /**
     * This general constructor creates a new partition of a specified interval
     * [a, b] into subintervals of width w
     */
    public Domain(double a, double b, double w) {
        if (w <= 0) w = 1;
        width = w;
        if (b < a + w) b = a + w;
        lowerBound = a;
        upperBound = b;
        lowerValue = lowerBound + 0.5 * width;
        upperValue = upperBound - 0.5 * width;
        size = (int) Math.rint((b - a) / w);
        //////System.out.println("Domain a = " + a + ", b = " + b + ", lv = " + lowerValue + ", upperValue = " + upperValue + ", size = " + size);
    }

    /**
     * This general constructor creates a new partition of a specified interval
     * [a, b] into subintervals of width w. The underlying variable has a
     * specified name and symbol.
     *
     * @param a the lower bound or value
     * @param b the upper bound or value
     * @param w the step size
     * @param t the type of domain
     */
    public Domain(double a, double b, double w, int t) {
        if (w <= 0) w = 1;
        width = w;
        if (t < 0) t = 0;
        else if (t > 1) t = 1;
        type = t;
        if (type == DISCRETE) {
            if (b < a) b = a;
            lowerBound = a - 0.5 * width;
            upperBound = b + 0.5 * width;
        } else {
            if (b < a + w) b = a + w;
            lowerBound = a;
            upperBound = b;
        }
        lowerValue = lowerBound + 0.5 * width;
        upperValue = upperBound - 0.5 * width;
        size = (int) Math.rint((upperBound - lowerBound) / width);
        //////System.out.println("Domain constructor 2 a = " + a + ", b = " + b + ", lv = " + lowerValue + ", upperValue = " + upperValue + ", size = " + size + ", type = " + type);

    }

    /**
     * This special constructor creates a new partition of [0, b] into 10 equal
     * subintervals
     */
    public Domain(double b) {
        this(0, b, 0.1 * b);
    }

    /**
     * This default constructor creates a new partition of [0, 1] into 10 equal
     * subintervals
     */
    public Domain() {
        this(1);
    }

    /**
     * This method returns the index of the interval containing a given value of
     * x
     */


    public int getIndex(double x) {
	    double original = (x - lowerValue) / width;
	    int result = (int) Math.rint((x - lowerValue) / width);
		if (x < lowerBound) {
			//////System.out.println("Domain getIndex IF");
			return -1;
		}
		else if (x > upperBound) {
			//////System.out.println("Domain getIndex ELSE IF");
			return size;
		}
		else {
			//////System.out.println("Domain getIndex ELSE");
			return (int) Math.rint((x - lowerValue) / width);
		}
    }

    /** This method returns the boundary point corresponding to a given index */
    public double getBound(int i) {
        return lowerBound + i * width;
    }

    /**
     * This method return the midpoint of the interval corresponding to a given
     * index
     */
    public double getValue(int i) {
		return lowerValue + i * width;
    }

    /**
     * This method returns the lower bound
     *
     * @uml.property name="lowerBound"
     */
    public double getLowerBound() {
	   //////System.out.println("Domain getLowerBound = " + lowerBound);
        return lowerBound;
    }

    /**
     * This method returns the upper bound
     *
     * @uml.property name="upperBound"
     */
    public double getUpperBound() {
	   //////System.out.println("Domain getUpperBound = " + upperBound);

        return upperBound;
    }

    /**
     * This method returns the lower midpoint
     *
     * @uml.property name="lowerValue"
     */
    public double getLowerValue() {
	   //////System.out.println("Domain getLowerValue = " + lowerValue);

        return lowerValue;
    }

    /**
     * This method returns the upper midpoint
     *
     * @uml.property name="upperValue"
     */
    public double getUpperValue() {
 	   //////System.out.println("Domain getUpperValue = " + upperValue);

 		return upperValue;
    }

    /**
     * This method returns the width of the partition
     *
     * @uml.property name="width"
     */
    public double getWidth() {
        return width;
    }

    /**
     * This method returns the size of the partition (the number of
     * subintervals)
     *
     * @uml.property name="size"
     */
    public int getSize() {
        return size;
    }

    /**
     * This method returns the type of the domain (DISCRETE or CONTINUOUS).
     *
     * @return the type
     * @uml.property name="type"
     */
    public int getType() {
        return type;
    }
}

