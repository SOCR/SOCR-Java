package JSci.mathml;

import java.lang.reflect.*;
import java.util.Hashtable;
import org.w3c.dom.*;
import org.w3c.dom.mathml.*;
import org.apache.xerces.dom.*;

/**
 * Implements a MathML document.
 * @version 1.0
 * @author Mark Hale
 */
public class MathMLDocumentImpl extends DocumentImpl implements MathMLDocument {
        private static final Hashtable _elementTypesMathML = new Hashtable(130);
        private static final Class[] _elemClassSigMathML = new Class [] {MathMLDocumentImpl.class, String.class};

        static {
                _elementTypesMathML.put("math", MathMLMathElementImpl.class);
                _elementTypesMathML.put("annotation", MathMLAnnotationElementImpl.class);
                _elementTypesMathML.put("xml-annotation", MathMLXMLAnnotationElementImpl.class);
                // presentation
                _elementTypesMathML.put("mi", MathMLPresentationTokenImpl.class);
                _elementTypesMathML.put("mn", MathMLPresentationTokenImpl.class);
                _elementTypesMathML.put("msub", MathMLScriptElementImpl.class);
                _elementTypesMathML.put("msup", MathMLScriptElementImpl.class);
                _elementTypesMathML.put("msubsup", MathMLScriptElementImpl.class);
                _elementTypesMathML.put("munder", MathMLUnderOverElementImpl.class);
                _elementTypesMathML.put("mover", MathMLUnderOverElementImpl.class);
                _elementTypesMathML.put("munderover", MathMLUnderOverElementImpl.class);
                _elementTypesMathML.put("mfrac", MathMLFractionElementImpl.class);
                _elementTypesMathML.put("msqrt", MathMLRadicalElementImpl.class);
                _elementTypesMathML.put("mroot", MathMLRadicalElementImpl.class);
                _elementTypesMathML.put("mrow", MathMLPresentationContainerImpl.class);
                _elementTypesMathML.put("mpadded", MathMLPaddedElementImpl.class);
                _elementTypesMathML.put("mfenced", MathMLFencedElementImpl.class);
                _elementTypesMathML.put("menclose", MathMLEncloseElementImpl.class);
                _elementTypesMathML.put("mglyph", MathMLGlyphElementImpl.class);
                _elementTypesMathML.put("maligngroup", MathMLAlignGroupElementImpl.class);
                _elementTypesMathML.put("malignmark", MathMLAlignMarkElementImpl.class);
                _elementTypesMathML.put("mtext", MathMLPresentationTokenImpl.class);
                _elementTypesMathML.put("mspace", MathMLSpaceElementImpl.class);
                _elementTypesMathML.put("ms", MathMLStringLitElementImpl.class);
                _elementTypesMathML.put("mphantom", MathMLPresentationContainerImpl.class);
                _elementTypesMathML.put("maction", MathMLActionElementImpl.class);
                _elementTypesMathML.put("merror", MathMLPresentationContainerImpl.class);
                // content
                _elementTypesMathML.put("ci", MathMLCiElementImpl.class);
                _elementTypesMathML.put("csymbol", MathMLCsymbolElementImpl.class);
                _elementTypesMathML.put("bvar", MathMLBvarElementImpl.class);
                _elementTypesMathML.put("condition", MathMLConditionElementImpl.class);
                _elementTypesMathML.put("uplimit", MathMLContentContainerImpl.class);
                _elementTypesMathML.put("lowlimit", MathMLContentContainerImpl.class);
                _elementTypesMathML.put("domainofapplication", MathMLContentContainerImpl.class);
                _elementTypesMathML.put("degree", MathMLContentContainerImpl.class);
                _elementTypesMathML.put("otherwise", MathMLContentContainerImpl.class);
                _elementTypesMathML.put("momentabout", MathMLContentContainerImpl.class);
                // arithmetic
                _elementTypesMathML.put("plus", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("minus", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("times", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("divide", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("quotient", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("rem", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("power", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("root", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("min", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("max", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("gcd", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("lcm", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("floor", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("ceiling", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("factorial", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("conjugate", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("abs", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arg", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("real", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("imaginary", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("factorof", MathMLPredefinedSymbolImpl.class);
                // calculus
                _elementTypesMathML.put("int", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("diff", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("partialdiff", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("divergence", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("grad", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("curl", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("laplacian", MathMLPredefinedSymbolImpl.class);
                // functional
                _elementTypesMathML.put("inverse", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("compose", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("ident", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("domain", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("codomain", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("image", MathMLPredefinedSymbolImpl.class);
                // sequences
                _elementTypesMathML.put("sum", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("product", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("limit", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("tendsto", MathMLPredefinedSymbolImpl.class);
                // logic
                _elementTypesMathML.put("and", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("or", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("xor", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("not", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("implies", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("forall", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("exists", MathMLPredefinedSymbolImpl.class);
                // relations
                _elementTypesMathML.put("eq", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("neq", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("gt", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("lt", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("geq", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("leq", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("equivalent", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("approx", MathMLPredefinedSymbolImpl.class);
                // set theory
                _elementTypesMathML.put("union", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("intersect", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("cartesianproduct", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("in", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("notin", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("subset", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("prsubset", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("notsubset", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("notprsubset", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("setdiff", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("card", MathMLPredefinedSymbolImpl.class);
                // functions
                _elementTypesMathML.put("exp", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("ln", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("log", MathMLPredefinedSymbolImpl.class);
                // trig functions
                _elementTypesMathML.put("sin", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("cos", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("tan", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("csc", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("sec", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("cot", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("sinh", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("cosh", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("tanh", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("csch", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("sech", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("coth", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arcsin", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arccos", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arctan", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arccsc", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arcsec", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arccot", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arcsinh", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arccosh", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arctanh", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arccsch", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arcsech", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("arccoth", MathMLPredefinedSymbolImpl.class);
                // statistics
                _elementTypesMathML.put("mean", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("mode", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("median", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("sdev", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("variance", MathMLPredefinedSymbolImpl.class);
                // linear algebra
                _elementTypesMathML.put("determinant", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("transpose", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("selector", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("vectorproduct", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("scalarproduct", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("outerproduct", MathMLPredefinedSymbolImpl.class);
                // symbols
                _elementTypesMathML.put("integers", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("reals", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("rationals", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("naturalnumbers", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("complexes", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("primes", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("exponentiale", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("imaginaryi", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("notanumber", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("true", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("false", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("emptyset", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("pi", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("eulergamma", MathMLPredefinedSymbolImpl.class);
                _elementTypesMathML.put("infinity", MathMLPredefinedSymbolImpl.class);
                // deprecated
                _elementTypesMathML.put("fn", MathMLFnElementImpl.class);
                _elementTypesMathML.put("reln", MathMLContentContainerImpl.class);
        }

        public MathMLDocumentImpl() {
                super();
        }

        public DocumentType getDoctype() {
                if (docType == null) {
                        docType = new DocumentTypeImpl(this, "math", "-//W3C//DTD MathML 2.0//EN", "http://www.w3.org/TR/MathML2/dtd/mathml2.dtd");
                }
                return docType;
        }

        public Element getDocumentElement() {
                Node math;

                math = super.getDocumentElement();
                if (math == null) {
                        math = new MathMLMathElementImpl(this, "math");
                        appendChild(math);
                }
                return (MathMLElement) math;
        }

        public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
                if (namespaceURI.equals(MathMLElementImpl.mathmlURI)) {
                        String localName;
                        Class elemClass;
                        Constructor cnst;

                        int index = qualifiedName.indexOf(':');
                        if (index < 0) {
                                localName = qualifiedName;
                        } else {
                                localName = qualifiedName.substring(index+1);
                        }

                        elemClass = (Class) _elementTypesMathML.get(localName);
                        if (elemClass != null) {
                                try {
                                        cnst = elemClass.getConstructor(_elemClassSigMathML);
                                        return (Element) cnst.newInstance(new Object[] {this, qualifiedName});
                                } catch (Exception except) {
                                        Throwable thrw;

                                        if (except instanceof InvocationTargetException) {
                                                thrw = ((InvocationTargetException) except).getTargetException();
                                        } else {
                                                thrw = except;
                                        }

                                        System.out.println("Exception " + thrw.getClass().getName());
                                        System.out.println(thrw.getMessage());

                                        throw new IllegalStateException("Tag '" + qualifiedName + "' associated with an Element class that failed to construct.");
                                }
                        } else {
                                return new MathMLElementImpl(this, qualifiedName);
                        }
                } else {
                        return super.createElementNS(namespaceURI, qualifiedName);
                }
        }

        public String getReferrer() {
                return null;
        }
        public String getDomain() {
                return null;
        }
        public String getURI() {
                return null;
        }
}

