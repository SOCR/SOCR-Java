package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML annotation element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLAnnotationElementImpl extends MathMLElementImpl implements MathMLAnnotationElement {
        /**
         * Constructs a MathML annotation element.
         */
        public MathMLAnnotationElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getBody() {
                return getFirstChild().getNodeValue();
        }
        public void setBody(String body) {
                getFirstChild().setNodeValue(body);
        }

        public String getEncoding() {
                return getAttribute("encoding");
        }
        public void setEncoding(String encoding) {
                setAttribute("encoding", encoding);
        }
}

