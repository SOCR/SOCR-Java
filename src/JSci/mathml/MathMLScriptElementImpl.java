package JSci.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.*;

/**
 * Implements a MathML script element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLScriptElementImpl extends MathMLElementImpl implements MathMLScriptElement {
        /**
         * Constructs a MathML script element.
         */
        public MathMLScriptElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getSubscriptshift() {
                if (getLocalName().equals("msup")) {
                        return null;
                }
                return getAttribute("subscriptshift");
        }
        public void setSubscriptshift(String subscriptshift) {
                setAttribute("subscriptshift", subscriptshift);
        }

        public String getSuperscriptshift() {
                if (getLocalName().equals("msub")) {
                        return null;
                }
                return getAttribute("superscriptshift");
        }
        public void setSuperscriptshift(String superscriptshift) {
                setAttribute("superscriptshift", superscriptshift);
        }

        public MathMLElement getBase() {
                return (MathMLElement) getFirstChild();
        }
        public void setBase(MathMLElement base) {
                replaceChild(base, getFirstChild());
        }

        public MathMLElement getSubscript() {
                if (getLocalName().equals("msup")) {
                        return null;
                }
                return (MathMLElement) item(1);
        }
        public void setSubscript(MathMLElement subscript) throws DOMException {
                if (getLocalName().equals("msup")) {
                        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "Cannot set a subscript for msup");
                }
                replaceChild(subscript, item(1));
        }

        public MathMLElement getSuperscript() {
                if (getLocalName().equals("msub")) {
                        return null;
                }
                if (getLocalName().equals("msup")) {
                        return (MathMLElement) item(1);
                } else {
                        return (MathMLElement) item(2);
                }
        }
        public void setSuperscript(MathMLElement superscript) throws DOMException {
                if (getLocalName().equals("msub")) {
                        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "Cannot set a superscript for msub");
                }
                if(getLocalName().equals("msup")) {
                        replaceChild(superscript, item(1));
                } else {
                        replaceChild(superscript, item(2));
                }
        }
}

