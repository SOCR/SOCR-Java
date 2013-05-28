package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML align group element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLAlignGroupElementImpl extends MathMLElementImpl implements MathMLAlignGroupElement {
        /**
         * Constructs a MathML align group element.
         */
        public MathMLAlignGroupElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getGroupalign() {
                return getAttribute("groupalign");
        }
        public void setGroupalign(String groupalign) {
                setAttribute("groupalign", groupalign);
        }
}

