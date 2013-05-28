
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLMathElement extends MathMLElement, MathMLContainer
{
    public String                 getMacros();
    public void                   setMacros(String macros);
    public String                 getDisplay();
    public void                   setDisplay(String display);
};
  