package JSci.mathml;

import org.w3c.dom.mathml.*;

/**
 * Implements a MathML condition element.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLConditionElementImpl extends MathMLElementImpl implements MathMLConditionElement {
        /**
         * Constructs a MathML condition element.
         */
        public MathMLConditionElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super(owner, qualifiedName);
        }

        public MathMLApplyElement getCondition() {
                return (MathMLApplyElement) getFirstChild();
        }
        public void setCondition(MathMLApplyElement condition) {
                replaceChild(condition, getFirstChild());
        }
}

