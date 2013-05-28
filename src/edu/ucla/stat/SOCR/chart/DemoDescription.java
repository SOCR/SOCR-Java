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

package edu.ucla.stat.SOCR.chart;

/**
 * A description of a demo application (used by the SuperDemo.java application).
 */
public class DemoDescription {

    /**
	 * @uml.property  name="className"
	 */
    private String className;
    
    /**
	 * @uml.property  name="description"
	 */
    private String description;
    
    /**
     * Creates a new description.
     * 
     * @param demoClassName  the class name.
     * @param demoDescription  the description.
     */
    public DemoDescription(String demoClassName, String demoDescription) {
        this.className = demoClassName;
        this.description = demoDescription;
    }
    
    /**
     * Returns the class name.
     * 
     * @return The class name.
     */
    public String getClassName() {
        return this.className;
    }
    
    /**
     * Returns the description.
     * 
     * @return The description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Returns the class description.
     * 
     * @return The class description.
     */
    public String toString() {
        return this.description;
    }
    
}
