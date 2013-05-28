package JSci.mathml;

import org.w3c.dom.*;
import org.w3c.dom.mathml.*;
import org.apache.xerces.dom.*;

/**
 * Implements a MathML element (and node list).
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLElementImpl extends ElementNSImpl implements MathMLElement, MathMLNodeList {
        static final String mathmlURI = "http://www.w3.org/1998/Math/MathML";

        /**
         * Constructs a MathML element.
         */
        public MathMLElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, mathmlURI, qualifiedName);
        }

        public String getClassName() {
                return getAttribute("class");
        }
        public void setClassName(String className) {
                setAttribute("class", className);
        }

        public String getMathElementStyle() {
                return getAttribute("style");
        }
        public void setMathElementStyle(String mathElementStyle) {
                setAttribute("style", mathElementStyle);
        }

        public String getId() {
                return getAttribute("id");
        }
        public void setId(String id) {
                setAttribute("id", id);
        }

        public String getHref() {
                return getAttribute("xlink:href");
        }
        public void setHref(String href) {
                setAttribute("xlink:href", href);
        }

        public String getXref() {
                return getAttribute("xref");
        }
        public void setXref(String xref) {
                setAttribute("xref", xref);
        }

        public MathMLMathElement getOwnerMathElement() {
                if (this instanceof MathMLMathElement) {
                        return null;
                }
                Node parent = getParentNode();
                while (!(parent instanceof MathMLMathElement)) {
                        parent = parent.getParentNode();
                }
                return (MathMLMathElement) parent;
        }
}

