package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML bounded variable element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLBvarElementImpl extends MathMLContentContainerImpl implements MathMLBvarElement {
        /**
         * Constructs a MathML bounded variable element.
         */
        public MathMLBvarElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }
}

