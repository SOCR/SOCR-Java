
package org.w3c.dom.mathml;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;

public interface MathMLDOMImplementation extends DOMImplementation
{
    public MathMLDocument         createMathMLDocument();
};

