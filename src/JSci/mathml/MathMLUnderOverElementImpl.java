package JSci.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.*;

/**
 * Implements a MathML under-over element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLUnderOverElementImpl extends MathMLElementImpl implements MathMLUnderOverElement {
        /**
         * Constructs a MathML under-over element.
         */
        public MathMLUnderOverElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getAccentunder() {
                if (getLocalName().equals("mover")) {
                        return null;
                }
                return getAttribute("accentunder");
        }
        public void setAccentunder(String accentunder) {
                setAttribute("accentunder", accentunder);
        }

        public String getAccent() {
                if (getLocalName().equals("munder")) {
                        return null;
                }
                return getAttribute("accent");
        }
        public void setAccent(String accent) {
                setAttribute("accent", accent);
        }

        public MathMLElement getBase() {
                return (MathMLElement) getFirstChild();
        }
        public void setBase(MathMLElement base) {
                replaceChild(base, getFirstChild());
        }

        public MathMLElement getUnderscript() {
                if (getLocalName().equals("mover")) {
                        return null;
                }
                return (MathMLElement) item(1);
        }
        public void setUnderscript(MathMLElement underscript) throws DOMException {
                if (getLocalName().equals("mover")) {
                        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "Cannot set a subscript for msup");
                }
                replaceChild(underscript, item(1));
        }

        public MathMLElement getOverscript() {
                if (getLocalName().equals("munder")) {
                        return null;
                }
                if (getLocalName().equals("mover")) {
                        return (MathMLElement) item(1);
                } else {
                        return (MathMLElement) item(2);
                }
        }
        public void setOverscript(MathMLElement overscript) throws DOMException {
                if (getLocalName().equals("munder")) {
                        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "Cannot set a superscript for msub");
                }
                if(getLocalName().equals("mover")) {
                        replaceChild(overscript, item(1));
                } else {
                        replaceChild(overscript, item(2));
                }
        }
}

