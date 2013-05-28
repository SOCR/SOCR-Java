package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML presentation token.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLPresentationTokenImpl extends MathMLElementImpl implements MathMLPresentationToken {
        /**
         * Constructs a MathML presentation token.
         */
        public MathMLPresentationTokenImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getMathvariant() {
                return getAttribute("mathvariant");
        }
        public void setMathvariant(String mathvariant) {
                setAttribute("mathvariant", mathvariant);
        }

        public String getMathsize() {
                return getAttribute("mathsize");
        }
        public void setMathsize(String mathsize) {
                setAttribute("mathsize", mathsize);
        }

        public String getMathfamily() {
                return getAttribute("mathfamily");
        }
        public void setMathfamily(String mathfamily) {
                setAttribute("mathfamily", mathfamily);
        }

        public String getMathcolor() {
                return getAttribute("mathcolor");
        }
        public void setMathcolor(String mathcolor) {
                setAttribute("mathcolor", mathcolor);
        }

        public String getMathbackground() {
                return getAttribute("mathbackground");
        }
        public void setMathbackground(String mathbackground) {
                setAttribute("mathbackground", mathbackground);
        }

        public MathMLNodeList getContents() {
                return (MathMLNodeList) getChildNodes();
        }
}

