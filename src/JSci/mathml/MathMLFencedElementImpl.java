package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML fenced element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLFencedElementImpl extends MathMLPresentationContainerImpl implements MathMLFencedElement {
        /**
         * Constructs a MathML fenced element.
         */
        public MathMLFencedElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getOpen() {
                return getAttribute("open");
        }
        public void setOpen(String open) {
                setAttribute("open", open);
        }

        public String getClose() {
                return getAttribute("close");
        }
        public void setClose(String close) {
                setAttribute("close", close);
        }

        public String getSeparators() {
                return getAttribute("separators");
        }
        public void setSeparators(String separators) {
                setAttribute("separators", separators);
        }
}

