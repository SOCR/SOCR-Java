package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML action element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLActionElementImpl extends MathMLPresentationContainerImpl implements MathMLActionElement {
        /**
         * Constructs a MathML action element.
         */
        public MathMLActionElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getActiontype() {
                return getAttribute("actiontype");
        }
        public void setActiontype(String actiontype) {
                setAttribute("actiontype", actiontype);
        }

        public String getSelection() {
                return getAttribute("selection");
        }
        public void setSelection(String selection) {
                setAttribute("selection", selection);
        }
}

