
package org.w3c.dom.mathml;

import org.w3c.dom.Node;

public interface MathMLContentToken extends MathMLContentElement
{
    public MathMLNodeList         getArguments();
    public String                 getDefinitionURL();
    public void                   setDefinitionURL(String definitionURL);
    public String                 getEncoding();
    public void                   setEncoding(String encoding);
    public MathMLElement          getArgument(int index);
    public MathMLElement          insertArgument(MathMLElement newArgument,
                                                 int index);
    public MathMLElement          setArgument(MathMLElement newArgument,
                                              int index);
    public void                   deleteArgument(int index);
    public MathMLElement          removeArgument(int index);
};
  