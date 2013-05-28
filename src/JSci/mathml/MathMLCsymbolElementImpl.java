package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML content symbol element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLCsymbolElementImpl extends MathMLContentTokenImpl implements MathMLCsymbolElement {
        /**
         * Constructs a MathML content symbol element.
         */
        public MathMLCsymbolElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }
}

