package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML radical element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLRadicalElementImpl extends MathMLElementImpl implements MathMLRadicalElement {
        /**
         * Constructs a MathML radical element.
         */
        public MathMLRadicalElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public MathMLElement getRadicand() {
                return (MathMLElement) getFirstChild();
        }
        public void setRadicand(MathMLElement radicand) {
                replaceChild(radicand, getFirstChild());
        }

        public MathMLElement getIndex() {
                if (getLocalName().equals("msqrt")) {
                        return null;
                }
                return (MathMLElement) item(1);
        }
        public void setIndex(MathMLElement index) {
                replaceChild(index, item(1));
        }
}

