package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML fraction element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLFractionElementImpl extends MathMLElementImpl implements MathMLFractionElement {
        /**
         * Constructs a MathML fraction element.
         */
        public MathMLFractionElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getLinethickness() {
                return getAttribute("linethickness");
        }
        public void setLinethickness(String linethickness) {
                setAttribute("linethickness", linethickness);
        }

        public MathMLElement getNumerator() {
                return (MathMLElement) getFirstChild();
        }
        public void setNumerator(MathMLElement numerator) {
                replaceChild(numerator, getFirstChild());
        }

        public MathMLElement getDenominator() {
                return (MathMLElement) item(1);
        }
        public void setDenominator(MathMLElement denominator) {
                replaceChild(denominator, item(1));
        }
}

