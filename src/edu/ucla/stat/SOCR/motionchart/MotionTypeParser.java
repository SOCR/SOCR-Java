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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.motionchart;

import javax.swing.table.TableModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Dec 16, 2008
 * Time: 9:58:35 AM
 *
 * @author Jameel
 */
public class MotionTypeParser
{
    public static Class getColumnClass(TableModel tm, int column)
    {
        return getColumnClass(tm, column, null);
    }

    public static Class getColumnClass(TableModel tm, int column, String parseString)
    {
        Class colClass = Object.class;

        for(int r = 0; r < tm.getRowCount(); r++)
        {
            Object obj = tm.getValueAt(r, column);
            Class objClass = getClass(obj, parseString);

            if(objClass == null)
                continue;

            if(colClass == Object.class)
            {
                if(objClass == Date.class)
                {
                    colClass = Date.class;
                    continue;
                }
                else if(objClass == Double.class)
                {
                    colClass = Double.class;
                    continue;
                }
                else if(objClass == String.class)
                {
                    colClass = String.class;
                    break;
                }
                else
                {
                    break;
                }
            }
            else if(colClass != objClass)
            {
                colClass = String.class;
                break;
            }
        }

        return colClass;
    }

    public static Class getClass(Object obj)
    {
        return getClass(obj, null);
    }

    public static Class getClass(Object obj, String parseString)
    {
        if(obj == null || obj.equals(""))
        {
            return null;
        }

        try
        {
            double value = Double.valueOf(obj.toString());

            return Double.class;
        }
        catch(NumberFormatException ignored) { }

        try
        {
            DateFormat df;

            if(parseString != null)
            {
                df = new SimpleDateFormat(parseString);
                df.setLenient(false);
            }
            else
            {
                df = DateFormat.getInstance();
            }

            Date value = df.parse(obj.toString());

            return Date.class;
        }
        catch(ParseException ignored) { }

        return String.class;
    }

    public static Object parseType(TableModel tm, int row, int column)
    {
        return parseType(tm.getValueAt(row, column), tm.getColumnClass(column));
    }

    public static Object parseType(MotionTableModel tm, int row, int column)
    {
        return parseType(tm.getValueAt(row, column), tm.getColumnClass(column), tm.getColumnParseString(column));
    }

    public static Object parseType(Object value, TableModel tm, int column)
    {
        return parseType(value, tm.getColumnClass(column));
    }

    public static Object parseType(Object value, MotionTableModel tm, int column)
    {
        return parseType(value, tm.getColumnClass(column), tm.getColumnParseString(column));
    }

    public static Object parseType(Object value, Class type)
    {
        return parseType(value, type, null);
    }

    public static Object parseType(Object value, Class type, String parseString)
    {
        if(value == null || value.equals(""))
        {
            return null;
        }

        if(type == Double.class)
        {
            try
            {
                return Double.parseDouble(value.toString());
            } catch(NumberFormatException ignored) { }
        }

        if(type == Date.class)
        {
            try
            {
                DateFormat df;

                if(parseString != null)
                {
                    df = new SimpleDateFormat(parseString);
                    df.setLenient(false);
                }
                else
                {
                    df = DateFormat.getInstance();
                }

                return df.parse(value.toString());
            } catch(ParseException ignored) { }
        }

        return value.toString();
    }
}
