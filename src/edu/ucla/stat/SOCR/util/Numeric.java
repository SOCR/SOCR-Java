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

package edu.ucla.stat.SOCR.util;

/* House-Keeping Calculation module. overcomes a precision shortcoming in Java 1.02.
	All toString() conversions returned at most six
	digits. The class also provides an easy way to produce
	truncated decimal output for nice display. */
public class Numeric extends Number {

  public static  double MIN_VALUE = 5e-324;
  public static  double MAX_VALUE = 1.7976931348623157e+308;
  public static  double NEGATIVE_INFINITY = -1.0/0.0;
  public static  double POSITIVE_INFINITY = 1.0/0.0;
  public static  double NaN = 0.0/0.0;
  public double value;
  
  public Numeric(double d) {
    // constructor method
    value = d;		// double value of this object
  }
  
  public int intValue() {
    // conversion method to return an integer value
    return (int)value;
  }
  public long longValue() {
    // conversion method to return a long value
    return (long)value;
  }
  public float floatValue() {
    // conversion method to return a float value
    return (float)value;
  }
  public double doubleValue() {
    // conversion method to return a double value
    return value;
  }
  
  public String toString(int digits) {
    // this is the important method in the class
    // returns a string representation with digits digits
    int i, j;
    double t;
    char n = ' ';
    String s = new String();
    // initialize...
    t = Math.abs(value);
    j = (int) t;
    if (value < 0) {
      n = '-';
    }
    s = s + n + j + ".";
    t = t - j;
    for (i=0; i<digits; i++) {
      t = Math.abs(10 * t);
      j = (int) t;
      s = s + "" + j;
      t = t - j;
    }
    return s;
  }
  
}
