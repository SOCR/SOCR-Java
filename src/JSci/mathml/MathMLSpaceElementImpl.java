package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML space element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLSpaceElementImpl extends MathMLElementImpl implements MathMLSpaceElement {
        /**
         * Constructs a MathML space element.
         */
        public MathMLSpaceElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getWidth() {
                return getAttribute("width");
        }
        public void setWidth(String width) {
                setAttribute("width", width);
        }

        public String getHeight() {
                return getAttribute("height");
        }
        public void setHeight(String height) {
                setAttribute("height", height);
        }

        public String getDepth() {
                return getAttribute("depth");
        }
        public void setDepth(String depth) {
                setAttribute("depth", depth);
        }
}

