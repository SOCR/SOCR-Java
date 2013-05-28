package org.freehep.j3d.plot;

import java.text.NumberFormat;

/** Format a double number.
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: DoubleNumberFormatter.java,v 1.1 2010/05/10 17:43:44 jiecui Exp $
 */
final class DoubleNumberFormatter
{
   DoubleNumberFormatter(int power)
   {
	   if (formatter == null)
		   formatter = NumberFormat.getInstance();
	   this.power = power;
   }
   void setFractionDigits(int fractDigits)
   {
	   formatter.setMinimumFractionDigits(fractDigits);
	   formatter.setMaximumFractionDigits(fractDigits);
   }
   String format(final double d)
   {
	   return formatter.format(power != 0 ? d / Math.pow(10.0, power) : d);
   }
   private static NumberFormat formatter = null;
   private int power;
}

