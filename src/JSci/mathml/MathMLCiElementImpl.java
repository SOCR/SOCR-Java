package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML content identifier element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLCiElementImpl extends MathMLContentTokenImpl implements MathMLCiElement {
        /**
         * Constructs a MathML content identifier element.
         */
        public MathMLCiElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getType() {
                return getAttribute("type");
        }
        public void setType(String type) {
                setAttribute("type", type);
        }
}

