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

import java.util.Date;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Dec 12, 2008
 * Time: 9:38:25 PM
 *
 * @author Jameel
 */
public class MotionKey implements Comparable<MotionKey>
{
    private enum KeyType { NUMBER, DATE, STRING }

    protected Date date;
    protected Double number;
    protected String string;
    protected KeyType keyType;

    public MotionKey(Object obj)
    {
        //Class objClass = MotionTypeParser.getClass(obj);

        if(obj instanceof Double)
        {
            keyType = KeyType.NUMBER;
            number = (Double)obj;//(Double)MotionTypeParser.parseType(obj, objClass);
        }
        else if(obj instanceof Date)
        {
            keyType = KeyType.DATE;
            date = (Date)obj; //(Date)MotionTypeParser.parseType(obj, objClass);
        }
        else
        {
            keyType = KeyType.STRING;
            string = obj.toString(); //(String)MotionTypeParser.parseType(obj, objClass);
        }
    }

    public int compareTo(MotionKey o)
    {
        if(o == null || o.equals(""))
        {
            return 1;
        }

        switch(keyType)
        {
            case NUMBER:
                return number.compareTo(o.number);
            case DATE:
                return date.compareTo(o.date);
            default:
                return string.compareTo(o.string);
        }
    }

    @Override
    public int hashCode()
    {
        switch(keyType)
        {
            case NUMBER:
                return number.hashCode();
            case DATE:
                return date.hashCode();
            default:
                return string.hashCode();
        }
    }

    @Override
    public String toString()
    {
        switch(keyType)
        {
            case NUMBER:
                return number.toString();
            case DATE:
                return date.toString();
            default:
                return string;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof MotionKey))
        {
            return false;
        }

        if(this == obj)
        {
            return true;
        }

        switch(keyType)
        {
            case NUMBER:
                return number.equals(obj);
            case DATE:
                return date.equals(obj);
            default:
                return string.equals(obj);
        }
    }
}
