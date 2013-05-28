package JSci.mathml;

import org.w3c.dom.*;
import org.w3c.dom.mathml.*;

/**
 * Implements a MathML content container.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLContentContainerImpl extends MathMLElementImpl implements MathMLContentContainer {
        /**
         * Constructs a MathML content container.
         */
        public MathMLContentContainerImpl(MathMLDocumentImpl owner, String qualifiedName) {
                super (owner, qualifiedName);
        }

        public MathMLConditionElement getCondition() {
                return (MathMLConditionElement) getNodeByName("condition");
        }
        public void setCondition(MathMLConditionElement condition) throws DOMException {
                setNodeByName(condition, "condition");
        }

        public MathMLElement getOpDegree() {
                return (MathMLElement) getNodeByName("degree");
        }
        public void setOpDegree(MathMLElement opDegree) throws DOMException {
                setNodeByName(opDegree, "degree");
        }

        public MathMLElement getDomainOfApplication() {
                return (MathMLElement) getNodeByName("domainofapplication");
        }
        public void setDomainOfApplication(MathMLElement domainOfApplication) throws DOMException {
                setNodeByName(domainOfApplication, "domainofapplication");
        }

        public MathMLElement getMomentAbout() {
                return (MathMLElement) getNodeByName("momentabout");
        }
        public void setMomentAbout(MathMLElement momentAbout) throws DOMException {
                setNodeByName(momentAbout, "momentabout");
        }

        public int getNBoundVariables() {
                return getBoundVariablesGetLength();
        }
        public MathMLBvarElement getBoundVariable(int index) {
                Node bvar = getBoundVariablesItem(index-1);

                return (MathMLBvarElement) bvar;
        }
        public MathMLBvarElement setBoundVariable(MathMLBvarElement newBvar, int index) throws DOMException {
                final int bvarsLength = getBoundVariablesGetLength();

                return (MathMLBvarElement) replaceChild(newBvar, getBoundVariablesItem(index-1));
        }
        public MathMLBvarElement insertBoundVariable(MathMLBvarElement newBvar, int index) throws DOMException {
                final int bvarsLength = getBoundVariablesGetLength();

                if (index == 0) {
                        return (MathMLBvarElement) appendChild(newBvar);
                } else {
                        return (MathMLBvarElement) insertBefore(newBvar, getBoundVariablesItem(index-1));
                }
        }
        public MathMLBvarElement removeBoundVariable(int index) {
                Node bvar = getBoundVariablesItem(index-1);

                return (MathMLBvarElement) removeChild(bvar);
        }
        public void deleteBoundVariable(int index) {
                removeBoundVariable(index);
        }

        public int getNArguments() {
                return getArgumentsGetLength();
        }
        public MathMLNodeList getArguments() {
                return new MathMLNodeList() {
                        public int getLength() {
                                return getArgumentsGetLength();
                        }
                        public Node item(int index) {
                                return getArgumentsItem(index);
                        }
                };
        }
        public MathMLNodeList getDeclarations() {
                return new MathMLNodeList() {
                        public int getLength() {
                                return getDeclarationsGetLength();
                        }
                        public Node item(int index) {
                                return getDeclarationsItem(index);
                        }
                };
        }

        public MathMLElement getArgument(int index) throws DOMException {
                Node arg = getArgumentsItem(index-1);
                if (arg == null) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                return (MathMLElement) arg;
        }
        public MathMLElement setArgument(MathMLElement newArgument, int index) throws DOMException {
                final int argsLength = getArgumentsGetLength();

                if ((index < 1) || (index > argsLength+1)) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                if (index == argsLength+1) {
                        return (MathMLElement) appendChild(newArgument);
                } else {
                        return (MathMLElement) replaceChild(newArgument, getArgumentsItem(index-1));
                }
        }
        public MathMLElement insertArgument(MathMLElement newArgument, int index) throws DOMException {
                final int argsLength = getArgumentsGetLength();

                if ((index < 0) || (index > argsLength+1)) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                if ((index == 0) || (index == argsLength+1)) {
                        return (MathMLElement) appendChild(newArgument);
                } else {
                        return (MathMLElement) insertBefore(newArgument, getArgumentsItem(index-1));
                }
        }
        public MathMLElement removeArgument(int index) throws DOMException {
                Node arg = getArgumentsItem(index-1);
                if (arg == null) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                return (MathMLElement) removeChild(arg);
        }
        public void deleteArgument(int index) throws DOMException {
                removeArgument(index);
        }

        public MathMLDeclareElement getDeclaration(int index) throws DOMException {
                Node decl = getDeclarationsItem(index-1);
                if (decl == null) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                return (MathMLDeclareElement) decl;
        }
        public MathMLDeclareElement setDeclaration(MathMLDeclareElement newDeclaration, int index) throws DOMException {
                final int declsLength = getDeclarationsGetLength();

                if ((index < 1) || (index > declsLength+1)) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                if (index == declsLength+1) {
                        return (MathMLDeclareElement) appendChild(newDeclaration);
                } else {
                        return (MathMLDeclareElement) replaceChild(newDeclaration, getDeclarationsItem(index-1));
                }
        }
        public MathMLDeclareElement insertDeclaration(MathMLDeclareElement newDeclaration, int index) throws DOMException {
                final int declsLength = getDeclarationsGetLength();

                if ((index < 0) || (index > declsLength+1)) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                if ((index == 0) || (index == declsLength+1)) {
                        return (MathMLDeclareElement) appendChild(newDeclaration);
                } else {
                        return (MathMLDeclareElement) insertBefore(newDeclaration, getDeclarationsItem(index-1));
                }
        }
        public MathMLDeclareElement removeDeclaration(int index) throws DOMException {
                Node decl = getDeclarationsItem(index-1);
                if (decl == null) {
                        throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
                }
                return (MathMLDeclareElement) removeChild(decl);
        }
        public void deleteDeclaration(int index) throws DOMException {
                removeDeclaration(index);
        }

        private int getBoundVariablesGetLength() {
                final int length = getLength();
                int numBvars = 0;

                for (int i = 0; i < length; i++) {
                        if (item(i) instanceof MathMLBvarElement) {
                                numBvars++;
                        }
                }
                return numBvars;
        }
        private Node getBoundVariablesItem(int index) {
                final int bvarLength = getDeclarationsGetLength();

                if ((index < 0) || (index >= bvarLength))
                        return null;

                Node node = null;
                int n = -1;
                for (int i = 0; n < index; i++) {
                        node = item(i);
                        if (node instanceof MathMLBvarElement) {
                                n++;
                        }
                }
                return node;
        }

        private int getArgumentsGetLength() {
                final int length = getLength();
                int numArgs = 0;

                for (int i = 0; i < length; i++) {
                        if (!(item(i) instanceof MathMLDeclareElement)) {
                                numArgs++;
                        }
                }
                return numArgs;
        }
        private Node getArgumentsItem(int index) {
                final int argsLength = getArgumentsGetLength();

                if ((index < 0) || (index >= argsLength))
                        return null;

                Node node = null;
                int n = -1;
                for (int i = 0; n < index; i++) {
                        node = item(i);
                        if (!(node instanceof MathMLDeclareElement)) {
                                n++;
                        }
                }
                return node;
        }

        private int getDeclarationsGetLength() {
                final int length = getLength();
                int numDecls = 0;

                for (int i = 0; i < length; i++) {
                        if (item(i) instanceof MathMLDeclareElement) {
                                numDecls++;
                        }
                }
                return numDecls;
        }
        private Node getDeclarationsItem(int index) {
                final int declLength = getDeclarationsGetLength();

                if ((index < 0) || (index >= declLength))
                        return null;

                Node node = null;
                int n = -1;
                for (int i = 0; n < index; i++) {
                        node = item(i);
                        if (node instanceof MathMLDeclareElement) {
                                n++;
                        }
                }
                return node;
        }

        private Node getNodeByName(String name) {
                final int length = getLength();
                Node node;

                for (int i = 0; i < length; i++) {
                        node = item(i);
                        if (node.getLocalName().equals(name)) {
                                return node;
                        }
                }
                return null;
        }
        private void setNodeByName(Node newNode, String name) {
                final int length = getLength();
                Node node;

                for (int i = 0; i < length; i++) {
                        node = item(i);
                        if (node.getLocalName().equals(name)) {
                                replaceChild(newNode, node);
                                return;
                        }
                }
        }
}

