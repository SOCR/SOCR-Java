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

package edu.ucla.stat.SOCR.chart.data;
 
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;

/**
 * parse String into JFreeChart's date data type
 * @author jenny
 *
 */
public abstract class DateParser{
	/**
	 *  parse EEE MMM dd kk:mm:ss zzz yyyy  to Minute
	 *  */
   	public static Minute parseMinute(String date){
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");

		Date time = df.parse(date,new ParsePosition(0));
		return new Minute(time);

	}

   	/**
	 *  parse MMMMMMMMM yyyy to Month
	 *  */
	public static Month parseMonth(String date){
		SimpleDateFormat df = new SimpleDateFormat("MMMMMMMMM yyyy");

		Date time = df.parse(date,new ParsePosition(0));
		return new Month(time);

	}

	/**
	 *  parse dd-MMMMMMMMM-yyyy to Day 
	 *  */
	public static Day parseDay(String date){

		SimpleDateFormat df = new SimpleDateFormat("dd-MMMMMMMMM-yyyy");

		Date time = df.parse(date,new ParsePosition(0));
		return new Day(time);

	}

}
