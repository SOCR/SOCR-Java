package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML align mark element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLAlignMarkElementImpl extends MathMLElementImpl implements MathMLAlignMarkElement {
        /**
         * Constructs a MathML align mark element.
         */
        public MathMLAlignMarkElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getEdge() {
                return getAttribute("edge");
        }
        public void setEdge(String edge) {
                setAttribute("edge", edge);
        }
}

