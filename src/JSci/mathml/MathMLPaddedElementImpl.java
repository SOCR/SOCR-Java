package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML padded element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLPaddedElementImpl extends MathMLPresentationContainerImpl implements MathMLPaddedElement {
        /**
         * Constructs a MathML padded element.
         */
        public MathMLPaddedElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getWidth() {
                return getAttribute("width");
        }
        public void setWidth(String width) {
                setAttribute("width", width);
        }

        public String getLspace() {
                return getAttribute("lspace");
        }
        public void setLspace(String lspace) {
                setAttribute("lspace", lspace);
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

