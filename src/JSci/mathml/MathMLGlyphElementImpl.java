package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML glyph element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLGlyphElementImpl extends MathMLElementImpl implements MathMLGlyphElement {
        /**
         * Constructs a MathML glyph element.
         */
        public MathMLGlyphElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getAlt() {
                return getAttribute("alt");
        }
        public void setAlt(String alt) {
                setAttribute("alt", alt);
        }

        public String getFontfamily() {
                return getAttribute("fontfamily");
        }
        public void setFontfamily(String fontfamily) {
                setAttribute("fontfamily", fontfamily);
        }

        public int getIndex() {
                return Integer.parseInt(getAttribute("index"));
        }
        public void setIndex(int index) {
                setAttribute("index", Integer.toString(index));
        }
}

