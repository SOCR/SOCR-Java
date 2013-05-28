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

package edu.ucla.stat.SOCR.core;

import java.util.*;


/**
 * this interface means the object provides one or more <code>ValueSetter </code>
 * through which the client changes the value. All ValueSetters will be added
 * to the Controlpane of the Applet.
 * 
 * the user also have to implement <code>update()</code> mehtod of Observer to do
 * whatever needed corrospoiding the value change
 * 
 * @see edu.ucla.stat.SOCR.core.ValueSetter
 * @see java.util.Observer
 * 
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public interface IValueSettable extends Observer {
    ValueSetter[] getValueSetters();
    ComponentSetter[] getComponentSetters();
}
