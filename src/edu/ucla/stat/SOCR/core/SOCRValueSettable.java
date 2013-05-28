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
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public abstract class SOCRValueSettable implements IValueSettable {

    private List<ValueSetter> fValueSetters = new ArrayList<ValueSetter>(5);
    private List<ComponentSetter> fComponentSetters = new ArrayList<ComponentSetter>(5);

    /**
     * implements IValueSettable
     */
    public ValueSetter[] getValueSetters() {
        return (ValueSetter[]) fValueSetters.toArray(new ValueSetter[0]);
    }

    public ComponentSetter[] getComponentSetters() {
        return (ComponentSetter[]) fComponentSetters.toArray(new ComponentSetter[0]);
    }

    protected void createValueSetter(String title, int type, int min, int max,
            int iv) {
        ValueSetter o = new ValueSetter(title, type, min, max, iv);
        o.addObserver(this);
        fValueSetters.add(o);
    }

    protected void createComponentSetter(String title, javax.swing.JComponent newComponent) {
        ComponentSetter o = new ComponentSetter(title, newComponent);
        o.addObserver(this);
        fComponentSetters.add(o);
    }

    protected void createValueSetter(String title, int type, int min, int max) {
        createValueSetter(title, type, min, max, (min + max) / 2);
    }

    protected void createValueSetter(String title, int min, int max, double scale) {
        createValueSetter(title, min, max, (min + max) / 2, scale);
    }

    protected void createValueSetter(String title, int min, int max, int iv,
            double scale) {
        ValueSetter o = new ValueSetter(title, min, max, iv, scale);
        o.addObserver(this);
        fValueSetters.add(o);
    }

    public ValueSetter getValueSetter(int index) {
        return (ValueSetter) fValueSetters.get(index);
    }

    public ComponentSetter getComponentSetter(int index) {
        return (ComponentSetter) fComponentSetters.get(index);
    }

}
