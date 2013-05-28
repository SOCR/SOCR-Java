package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML enclose element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLEncloseElementImpl extends MathMLPresentationContainerImpl implements MathMLEncloseElement {
        /**
         * Constructs a MathML enclose element.
         */
        public MathMLEncloseElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getNotation() {
                return getAttribute("notation");
        }
        public void setNotation(String notation) {
                setAttribute("notation", notation);
        }
}

