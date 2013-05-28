package JSci.io;

import java.util.Set;
import java.util.HashSet;
import JSci.maths.*;
import JSci.maths.fields.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;

/**
* The MathMLParser class will parse a MathML document into JSci objects.
* @version 0.8
* @author Mark Hale
*/
public final class MathMLParser extends DOMParser {
        /**
        * Constructs a MathMLParser.
        */
        public MathMLParser() {
                try {
                        setDocumentClassName("JSci.mathml.MathMLDocumentImpl");
                } catch(Exception e) {}
        }
        /**
        * Translates the document into JSci objects.
        */
        public Object[] translateToJSciObjects() {
                Translator translator=new JSciObjectTranslator();
                return translator.translate(getDocument().getDocumentElement());
        }
        /**
        * Translates the document into JSci code.
        */
        public Object[] translateToJSciCode() {
                Translator translator=new JSciCodeTranslator();
                return translator.translate(getDocument().getDocumentElement());
        }
        /**
        * Translator.
        */
        abstract class Translator extends Object {
                public Translator() {}
                public Object[] translate(Node root) {
                        return parseMATH(root);
                }
                /**
                * Parses the &lt;math&gt; node.
                */
                private Object[] parseMATH(Node n) {
                        int len=0;
                        final NodeList nl=n.getChildNodes();
                        final Object objList[]=new Object[nl.getLength()];
                        Object obj;
                        for(int i=0;i<objList.length;i++) {
                                obj=processNode(nl.item(i));
                                if(obj!=null) {
                                        objList[len]=obj;
                                        len++;
                                }
                        }
                        final Object parseList[]=new Object[len];
                        System.arraycopy(objList,0,parseList,0,len);
                        return parseList;
                }
                /**
                * Processes a node.
                */
                protected Object processNode(Node n) {
                        final String nodeName=n.getNodeName();
                        if(nodeName.equals("apply") || nodeName.equals("reln"))
                                return parseAPPLY(n);
                        else if(nodeName.equals("cn"))
                                return parseCN(n);
                        else if(nodeName.equals("ci"))
                                return parseCI(n);
                        else if(nodeName.equals("vector"))
                                return parseVECTOR(n);
                        else if(nodeName.equals("matrix"))
                                return parseMATRIX(n);
                        else if(nodeName.equals("set"))
                                return parseSET(n);
                        else if(nodeName.equals("ms"))
                                return parseMS(n);
                        else if(nodeName.equals("mtext"))
                                return parseMTEXT(n);
                        else
                                return null;
                }
                protected abstract Object parseAPPLY(Node n);
                protected abstract Object parseCN(Node n);
                protected abstract Object parseCI(Node n);
                protected abstract Object parseVECTOR(Node n);
                protected abstract Object parseMATRIX(Node n);
                protected abstract Object parseSET(Node n);
                protected abstract Object parseMS(Node n);
                protected abstract Object parseMTEXT(Node n);
        }
        /**
        * JSci object translator.
        */
        class JSciObjectTranslator extends Translator {
                private final int INTEGER=0;
                private final int DOUBLE=1;
                private final int COMPLEX=2;
                private int setID=1;

                public JSciObjectTranslator() {}
                /**
                * Parses &lt;apply&gt; tags.
                * @return MathMLExpression.
                */
                protected Object parseAPPLY(Node n) {
                        final MathMLExpression expr=new MathMLExpression();
                        final NodeList nl=n.getChildNodes();
                        Object obj;
                        int i;
                        for(i=0;nl.item(i).getNodeType()==Node.TEXT_NODE;i++)
                                ;
                        expr.setOperation(nl.item(i).getNodeName());
                        for(;i<nl.getLength();i++) {
                                obj=processNode(nl.item(i));
                                if(obj!=null)
                                        expr.addArgument(obj);
                        }
                        return expr;
                }
                /**
                * Parses &lt;cn&gt; tags.
                * @return Ring.Member.
                */
                protected Object parseCN(Node n) {
                        return parseNumber(n);
                }
                private Ring.Member parseNumber(Node n) {
                        final NamedNodeMap attr=n.getAttributes();
                // support only base 10
                        if(!attr.getNamedItem("base").getNodeValue().equals("10"))
                                return null;
                // default type="real"
                        if(attr.getNamedItem("type")==null)
                                return new MathDouble(n.getFirstChild().getNodeValue());
                        final String attrType=attr.getNamedItem("type").getNodeValue();
                        if(attrType.equals("integer")) {
                                return new MathInteger(n.getFirstChild().getNodeValue());
                        } else if(attrType.equals("real")) {
                                return new MathDouble(n.getFirstChild().getNodeValue());
                        } else if(attrType.equals("rational")) {
                                final Node num=n.getFirstChild();
                                final Node denom=num.getNextSibling().getNextSibling();
                                return new MathDouble(num.getNodeValue()).divide(new MathDouble(denom.getNodeValue()));
                        } else if(attrType.equals("complex-cartesian")) {
                                final Node re=n.getFirstChild();
                                final Node im=re.getNextSibling().getNextSibling();
                                return new Complex(
                                        new Double(re.getNodeValue()).doubleValue(),
                                        new Double(im.getNodeValue()).doubleValue()
                                );
                        } else if(attrType.equals("complex-polar")) {
                                final Node mod=n.getFirstChild();
                                final Node arg=mod.getNextSibling().getNextSibling();
                                return Complex.polar(
                                        new Double(mod.getNodeValue()).doubleValue(),
                                        new Double(arg.getNodeValue()).doubleValue()
                                );
                        } else if(attrType.equals("constant")) {
                                final String value=n.getFirstChild().getNodeValue();
                                if(value.equals("&pi;"))
                                        return RealField.PI;
                                else if(value.equals("&ee;") || value.equals("&ExponentialE;"))
                                        return RealField.E;
                                else if(value.equals("&ii;") || value.equals("&ImaginaryI;"))
                                        return ComplexField.I;
                                else if(value.equals("&gamma;"))
                                        return RealField.GAMMA;
                                else if(value.equals("&infty;") || value.equals("&infin;"))
                                        return RealField.INFINITY;
                                else if(value.equals("&NaN;") || value.equals("&NotANumber;"))
                                        return RealField.NaN;
                                else
                                        return null;
                        } else
                                return null;
                }
                /**
                * Parses &lt;ci&gt; tags.
                * @return String.
                */
                protected Object parseCI(Node n) {
                        return n.getFirstChild().getNodeValue();
                }
                /**
                * Parses &lt;vector&gt; tags.
                * @return MathVector.
                */
                protected Object parseVECTOR(Node vectorNode) {
                        int len=0,type=INTEGER;
                        final NodeList nl=vectorNode.getChildNodes();
                        final Ring.Member num[]=new Ring.Member[nl.getLength()];
                        for(int i=0;i<num.length;i++) {
                                Node n = nl.item(i);
                                if(n.getNodeName().equals("cn")) {
                                        num[len]=parseNumber(n);
                                        if(num[len]!=null) {
                                        // work out number format needed
                                                if(num[len] instanceof MathDouble && type<DOUBLE)
                                                        type=DOUBLE;
                                                else if(num[len] instanceof Complex && type<COMPLEX)
                                                        type=COMPLEX;
                                                len++;
                                        }
                                }
                        }
                // output to JSci objects
                        if(type==INTEGER) {
                                final int array[]=new int[len];
                                for(int i=0;i<array.length;i++)
                                        array[i]=((MathInteger)num[i]).value();
                                return new IntegerVector(array);
                        } else if(type==DOUBLE) {
                                final double array[]=new double[len];
                                for(int i=0;i<array.length;i++) {
                                        if(num[i] instanceof MathInteger)
                                                array[i]=((MathInteger)num[i]).value();
                                        else
                                                array[i]=((MathDouble)num[i]).value();
                                }
                                return new DoubleVector(array);
                        } else {
                                final Complex array[]=new Complex[len];
                                for(int i=0;i<array.length;i++) {
                                        if(num[i] instanceof MathInteger)
                                                array[i]=new Complex(((MathInteger)num[i]).value(),0.0);
                                        else if(num[i] instanceof MathDouble)
                                                array[i]=new Complex(((MathDouble)num[i]).value(),0.0);
                                        else
                                                array[i]=(Complex)num[i];
                                }
                                return new ComplexVector(array);
                        }
                }
                /**
                * Parses &lt;matrix&gt; tags.
                * @return Matrix.
                */
                protected Object parseMATRIX(Node matrixNode) {
                        int rows=0,cols=Integer.MAX_VALUE;
                        final NodeList nl=matrixNode.getChildNodes();
                        final Ring.Member num[][]=new Ring.Member[nl.getLength()][];
                        for(int i=0;i<num.length;i++) {
                                Node n = nl.item(i);
                                if(n.getNodeName().equals("matrixrow")) {
                                        num[rows]=parseMatrixRow(n);
                                        if(num[rows].length<cols)
                                                cols=num[rows].length;
                                       rows++;
                                }
                        }
                // work out number format needed
                        int type=INTEGER;
                        for(int j,i=0;i<rows;i++) {
                                for(j=0;j<cols;j++) {
                                        if(num[i][j] instanceof MathDouble && type<DOUBLE)
                                                type=DOUBLE;
                                        else if(num[i][j] instanceof Complex && type<COMPLEX)
                                                type=COMPLEX;
                                }
                        }
                // output to JSci objects
                        if(type==INTEGER) {
                                final int array[][]=new int[rows][cols];
                                for(int j,i=0;i<rows;i++) {
                                        for(j=0;j<cols;j++)
                                                array[i][j]=((MathInteger)num[i][j]).value();
                                }
                                if(rows==cols)
                                        return new IntegerSquareMatrix(array);
                                else
                                        return new IntegerMatrix(array);
                        } else if(type==DOUBLE) {
                                final double array[][]=new double[rows][cols];
                                for(int j,i=0;i<rows;i++) {
                                        for(j=0;j<cols;j++) {
                                                if(num[i][j] instanceof MathInteger)
                                                        array[i][j]=((MathInteger)num[i][j]).value();
                                                else
                                                        array[i][j]=((MathDouble)num[i][j]).value();
                                        }
                                }
                                if(rows==cols)
                                        return new DoubleSquareMatrix(array);
                                else
                                        return new DoubleMatrix(array);
                        } else {
                                final Complex array[][]=new Complex[rows][cols];
                                for(int j,i=0;i<rows;i++) {
                                        for(j=0;j<cols;j++) {
                                                if(num[i][j] instanceof MathInteger)
                                                        array[i][j]=new Complex(((MathInteger)num[i][j]).value(),0.0);
                                                else if(num[i][j] instanceof MathDouble)
                                                        array[i][j]=new Complex(((MathDouble)num[i][j]).value(),0.0);
                                                else
                                                        array[i][j]=(Complex)num[i][j];
                                        }
                                }
                                if(rows==cols)
                                        return new ComplexSquareMatrix(array);
                                else
                                        return new ComplexMatrix(array);
                        }
                }
                /**
                * Parses &lt;matrixrow&gt; tags.
                */
                private Ring.Member[] parseMatrixRow(Node rowNode) {
                        int len=0;
                        final NodeList nl=rowNode.getChildNodes();
                        final Ring.Member num[]=new Ring.Member[nl.getLength()];
                        for(int i=0;i<num.length;i++) {
                                Node n = nl.item(i);
                                if(n.getNodeName().equals("cn")) {
                                        num[len]=parseNumber(n);
                                        if(num[len]!=null)
                                                len++;
                                }
                        }
                        final Ring.Member row[]=new Ring.Member[len];
                        System.arraycopy(num,0,row,0,len);
                        return row;
                }
                /**
                * Parses &lt;set&gt; tags.
                * @return FiniteSet.
                */
                protected Object parseSET(Node setNode) {
                        final NodeList nl = setNode.getChildNodes();
                        final Set elements = new HashSet(nl.getLength());
                        for(int i=0;i<nl.getLength();i++) {
                                Node n = nl.item(i);
                                if(n.getNodeName().equals("ci"))
                                        elements.add(parseCI(n));
                        }
                // output to JSci objects
                        return new FiniteSet(elements);
                }
                /**
                * Parses &lt;ms&gt; tags.
                * @return String.
                */
                protected Object parseMS(Node n) {
                        return n.getFirstChild().getNodeValue();
                }
                /**
                * Parses &lt;mtext&gt; tags.
                * @return String.
                */
                protected Object parseMTEXT(Node n) {
                        return n.getFirstChild().getNodeValue();
                }
        }
        /**
        * JSci code translator.
        */
        class JSciCodeTranslator extends Translator {
                public JSciCodeTranslator() {}
                /**
                * Parses &lt;apply&gt; tags.
                * @return String.
                */
                protected Object parseAPPLY(Node n) {
                        final StringBuffer buf=new StringBuffer();
                        final NodeList nl=n.getChildNodes();
                        Object obj;
                        int i;
                        for(i=0;nl.item(i).getNodeType()==Node.TEXT_NODE;i++)
                                ;
                        String op=nl.item(i).getNodeName();
                        if(op.equals("plus"))
                                op="add";
                        else if(op.equals("minus"))
                                op="subtract";
                        else if(op.equals("times"))
                                op="multiply";
                        boolean isFirst=true;
                        for(;i<nl.getLength();i++) {
                                obj=processNode(nl.item(i));
                                if(obj!=null) {
                                        if(isFirst) {
                                                buf.append(obj);
                                                isFirst=false;
                                        } else
                                                buf.append('.').append(op).append('(').append(obj).append(')');
                                }
                        }
                        return buf;
                }
                /**
                * Parses &lt;cn&gt; tags.
                * @return String.
                */
                protected Object parseCN(Node n) {
                        final NamedNodeMap attr=n.getAttributes();
                // support only base 10
                        if(!attr.getNamedItem("base").getNodeValue().equals("10"))
                                return null;
                // default type="real"
                        if(attr.getNamedItem("type")==null)
                                return "new MathDouble("+n.getFirstChild().getNodeValue()+')';
                        final String attrType=attr.getNamedItem("type").getNodeValue();
                        if(attrType.equals("integer")) {
                                return "new MathInteger("+n.getFirstChild().getNodeValue()+')';
                        } else if(attrType.equals("real")) {
                                return "new MathDouble("+n.getFirstChild().getNodeValue()+')';
                        } else if(attrType.equals("rational")) {
                                final Node num=n.getFirstChild();
                                final Node denom=num.getNextSibling().getNextSibling();
                                return "new MathDouble("+num.getNodeValue()+'/'+denom.getNodeValue()+')';
                        } else if(attrType.equals("complex-cartesian")) {
                                final Node re=n.getFirstChild();
                                final Node im=re.getNextSibling().getNextSibling();
                                return "new Complex("+re.getNodeValue()+','+im.getNodeValue()+')';
                        } else if(attrType.equals("complex-polar")) {
                                final Node mod=n.getFirstChild();
                                final Node arg=mod.getNextSibling().getNextSibling();
                                return "Complex.polar("+mod.getNodeValue()+','+arg.getNodeValue()+')';
                        } else if(attrType.equals("constant")) {
                                final String value=n.getFirstChild().getNodeValue();
                                if(value.equals("&pi;"))
                                        return "RealField.PI";
                                else if(value.equals("&ee;") || value.equals("&ExponentialE;"))
                                        return "RealField.E";
                                else if(value.equals("&ii;") || value.equals("&ImaginaryI;"))
                                        return "ComplexField.I";
                                else if(value.equals("&gamma;"))
                                        return "RealField.GAMMA";
                                else if(value.equals("&infty;") || value.equals("&infin;"))
                                        return "RealField.INFINITY";
                                else if(value.equals("&NaN;") || value.equals("&NotANumber;"))
                                        return "RealField.NaN";
                                else
                                        return null;
                        } else
                                return null;
                }
                /**
                * Parses &lt;ci&gt; tags.
                * @return String.
                */
                protected Object parseCI(Node n) {
                        return n.getFirstChild().getNodeValue();
                }
                /**
                * Parses &lt;vector&gt; tags.
                * @return String.
                */
                protected Object parseVECTOR(Node n) {
                        return null;
                }
                /**
                * Parses &lt;matrix&gt; tags.
                * @return String.
                */
                protected Object parseMATRIX(Node n) {
                        return null;
                }
                /**
                * Parses &lt;set&gt; tags.
                * @return String.
                */
                protected Object parseSET(Node n) {
                        return null;
                }
                /**
                * Parses &lt;ms&gt; tags.
                * @return String.
                */
                protected Object parseMS(Node n) {
                        return n.getFirstChild().getNodeValue();
                }
                /**
                * Parses &lt;mtext&gt; tags.
                * @return String.
                */
                protected Object parseMTEXT(Node n) {
                        return "/*\n"+n.getFirstChild().getNodeValue()+"\n*/";
                }
        }
}

