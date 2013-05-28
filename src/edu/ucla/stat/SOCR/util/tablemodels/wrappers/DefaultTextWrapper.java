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

package edu.ucla.stat.SOCR.util.tablemodels.wrappers;

import javax.swing.*;
import java.awt.*;

/**
 * <code>DefaultTextWrapper</code>  class is used by {@link edu.ucla.stat.SOCR.util.tablemodels.renderers.DefaultTextCellRenderer}
 * as a container for text and its properties.
 */
public class DefaultTextWrapper {
    public static final Color DEFAULT_COLOR = null;
    public static final Color DEFAULT_BACKGROUND_COLOR = null;
    public static final Font DEFAULT_FONT = null;
    public static final int DEFAULT_HORIZONTAL_ALIGN = SwingConstants.LEFT;
    public static final int DEFAULT_VERTICAL_ALIGN = SwingConstants.TOP;

    private String text = null;
    private Color color = DEFAULT_COLOR;
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Font font = DEFAULT_FONT;

    private int horizontalAlignment = DEFAULT_HORIZONTAL_ALIGN;
    private int verticalAlignment = DEFAULT_VERTICAL_ALIGN;

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text, text color, background text color, font,
     * horizontal alignment and vertical alignment.
     *
     * @param text            text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     * @param color           text color
     * @param backgroundColor background text color
     * @param font            text font
     * @param hAlign          horizontal text alignment
     * @param vAlign          vertical text alignment
     */
    public DefaultTextWrapper(String text, Color color, Color backgroundColor, Font font, int hAlign, int vAlign) {
        this.text = text;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.font = font;
        this.horizontalAlignment = hAlign;
        this.verticalAlignment = vAlign;
    }

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text
     *
     * @param text text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     */
    public DefaultTextWrapper(String text) {
        this(text, DEFAULT_COLOR, DEFAULT_BACKGROUND_COLOR, DEFAULT_FONT, DEFAULT_HORIZONTAL_ALIGN, DEFAULT_VERTICAL_ALIGN);
    }

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text and text color.
     *
     * @param text  text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     * @param color text color
     */
    public DefaultTextWrapper(String text, Color color) {
        this(text, color, DEFAULT_BACKGROUND_COLOR, DEFAULT_FONT, DEFAULT_HORIZONTAL_ALIGN, DEFAULT_VERTICAL_ALIGN);
    }

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text, text color and background text color.
     *
     * @param text            text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     * @param color           text color
     * @param backgroundColor background text color
     */
    public DefaultTextWrapper(String text, Color color, Color backgroundColor) {
        this(text, color, backgroundColor, DEFAULT_FONT, DEFAULT_HORIZONTAL_ALIGN, DEFAULT_VERTICAL_ALIGN);
    }

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text and font.
     *
     * @param text text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     * @param font text font
     */
    public DefaultTextWrapper(String text, Font font) {
        this(text, DEFAULT_COLOR, DEFAULT_BACKGROUND_COLOR, font, DEFAULT_HORIZONTAL_ALIGN, DEFAULT_VERTICAL_ALIGN);
    }

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text, text color, font,
     * horizontal alignment and vertical alignment.
     *
     * @param text  text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     * @param color text color
     * @param font  text font
     */
    public DefaultTextWrapper(String text, Color color, Font font) {
        this(text, color, DEFAULT_BACKGROUND_COLOR, font, DEFAULT_HORIZONTAL_ALIGN, DEFAULT_VERTICAL_ALIGN);
    }

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text, text color, background text color and font.
     *
     * @param text            text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     * @param color           text color
     * @param backgroundColor background text color
     * @param font            text font
     */
    public DefaultTextWrapper(String text, Color color, Color backgroundColor, Font font) {
        this(text, color, backgroundColor, font, DEFAULT_HORIZONTAL_ALIGN, DEFAULT_VERTICAL_ALIGN);
    }

    /**
     * Constructs a <code>DefaultTextWrapper</code> with the specified text, horizontal alignment and vertical alignment.
     *
     * @param text   text to be displayed by the <code>DefaultTextWrapper</code>'s  renderer
     * @param hAlign horizontal text alignment
     * @param vAlign vertical text alignment
     */
    public DefaultTextWrapper(String text, int hAlign, int vAlign) {
        this(text, DEFAULT_COLOR, DEFAULT_BACKGROUND_COLOR, DEFAULT_FONT, hAlign, vAlign);
    }

    /**
     * Returns text that holds by this wrapper.
     *
     * @return text that holds by this wrapper.
     */
    public String getText() {
        return text;
    }

    /**
     * Specifies text that holds by this wrapper.
     *
     * @param text that holds by this wrapper
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns color of the text that holds by this wrapper.
     *
     * @return color of the text that holds by this wrapper.
     */
    public Color getColor() {
        return color;
    }


    /**
     * Specifies color of the text that holds by this wrapper.
     *
     * @param color color of the text that holds by this wrapper
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns background color of the text that holds by this wrapper.
     *
     * @return background color of the text that holds by this wrapper.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Specifies background color of the text that holds by this wrapper.
     *
     * @param backgroundColor background color of the text that holds by this wrapper
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Returns font of the text that holds by this wrapper.
     *
     * @return font of the text that holds by this wrapper.
     */
    public Font getFont() {
        return font;
    }

    /**
     * Specifies font of the text that holds by this wrapper.
     *
     * @param font font of the text that holds by this wrapper
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Returns the alignment of the wrapper's contents along the X axis.
     *
     * @return the value of the horizontalAlignment property, one of the
     *         following constants defined in <code>SwingConstants</code>:
     *         <code>LEFT</code>,
     *         <code>CENTER</code>,
     *         <code>RIGHT</code>,
     *         <code>LEADING</code> or
     *         <code>TRAILING</code>.
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Specifies the alignment of the wrapper's contents along the Y axis.
     * <p/>The default value of this property is CENTER.
     *
     * @param hAlign one of the following constants
     *               defined in <code>SwingConstants</code>:
     *               <code>LEFT</code>,
     *               <code>CENTER</code> (the default),
     *               <code>RIGHT</code>,
     *               <code>LEADING</code> or
     *               <code>TRAILING</code>.
     */
    public void setHorizontalAlign(int hAlign) {
        this.horizontalAlignment = hAlign;
    }

    /**
     * Returns the alignment of the wrapper's contents along the Y axis.
     *
     * @return the value of the verticalAlignment property, one of the
     *         following constants defined in <code>SwingConstants</code>:
     *         <code>TOP</code>,
     *         <code>CENTER</code>, or
     *         <code>BOTTOM</code>.
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Specifies the alignment of the wrapper's contents along the Y axis.
     * <p/>The default value of this property is CENTER.
     *
     * @param vAlign one of the following constants
     *               defined in <code>SwingConstants</code>:
     *               <code>TOP</code>,
     *               <code>CENTER</code> (the default), or
     *               <code>BOTTOM</code>.
     */
    public void setVerticalAlign(int vAlign) {
        this.verticalAlignment = vAlign;
    }
}
