package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML string literal element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLStringLitElementImpl extends MathMLPresentationTokenImpl implements MathMLStringLitElement {
        /**
         * Constructs a MathML string literal element.
         */
        public MathMLStringLitElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getLquote() {
                return getAttribute("lquote");
        }
        public void setLquote(String lquote) {
                setAttribute("lquote", lquote);
        }

        public String getRquote() {
                return getAttribute("rquote");
        }
        public void setRquote(String rquote) {
                setAttribute("rquote", rquote);
        }
}

