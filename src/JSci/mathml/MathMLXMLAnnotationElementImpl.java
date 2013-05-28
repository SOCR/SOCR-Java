package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML XML annotation element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLXMLAnnotationElementImpl extends MathMLElementImpl implements MathMLXMLAnnotationElement {
        /**
         * Constructs a MathML XML annotation element.
         */
        public MathMLXMLAnnotationElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getEncoding() {
                return getAttribute("encoding");
        }
        public void setEncoding(String encoding) {
                setAttribute("encoding", encoding);
        }
}

