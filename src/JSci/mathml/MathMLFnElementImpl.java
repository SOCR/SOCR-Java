package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML function element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLFnElementImpl extends MathMLContentContainerImpl implements MathMLFnElement {
        /**
         * Constructs a MathML function element.
         */
        public MathMLFnElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public String getDefinitionURL() {
                return getAttribute("definitionURL");
        }
        public void setDefinitionURL(String definitionURL) {
                setAttribute("definitionURL", definitionURL);
        }

        public String getEncoding() {
                return getAttribute("encoding");
        }
        public void setEncoding(String encoding) {
                setAttribute("encoding", encoding);
        }
}

