package JSci.chemistry;

import JSci.chemistry.periodictable.*;
import java.util.WeakHashMap;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
* This class provides access to the elements of the periodic table.
* @version 0.2
* @author Mark Hale
*/
public final class PeriodicTable {
        private static final WeakHashMap table=new WeakHashMap();

        private PeriodicTable() {}
        /**
        * Returns an element.
        */
        public static Element getElement(String name) {
                name=name.toLowerCase();
                Object element=table.get(name);
                if(element==null) {
                        element=loadElement("periodictable/"+name+".xml");
                        table.put(name,element);
                }
                return (Element)element;
        }
        /**
        * Loads an element from its XML resource.
        */
        private static Element loadElement(String resname) {
                DocumentBuilderFactory docFactory=DocumentBuilderFactory.newInstance();
                try {
                        DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
                        Document doc=docBuilder.parse(PeriodicTable.class.getResourceAsStream(resname));
                        Node root=doc.getDocumentElement();
                        String group=root.getAttributes().getNamedItem("series").getNodeValue();
                        NodeList nl=root.getChildNodes();
                        String name=findStringValue(nl,"name");
                        String symbol=findStringValue(nl,"symbol");
                        Element elem;
                        if(group.equals("non-metal"))
                                elem=new NonMetal(name,symbol);
                        else if(group.equals("halogen"))
                                elem=new Halogen(name,symbol);
                        else if(group.equals("noble-gas"))
                                elem=new NobleGas(name,symbol);
                        else if(group.equals("metal"))
                                elem=new Metal(name,symbol);
                        else if(group.equals("alkali-metal"))
                                elem=new AlkaliMetal(name,symbol);
                        else if(group.equals("alkali-earth-metal"))
                                elem=new AlkaliEarthMetal(name,symbol);
                        else if(group.equals("rare-earth-metal"))
                                elem=new RareEarthMetal(name,symbol);
                        else if(group.equals("transition-metal"))
                                elem=new TransitionMetal(name,symbol);
                        else
                                elem=new Element(name,symbol);
                        elem.setAtomicNumber(Integer.parseInt(findStringValue(nl,"atomic-number")));
                        elem.setMassNumber(Integer.parseInt(findStringValue(nl,"mass-number")));
                        elem.setElectronegativity(findDoubleValue(nl,"electronegativity"));
                        elem.setCovalentRadius(findDoubleValue(nl,"covalent-radius"));
                        elem.setAtomicRadius(findDoubleValue(nl,"atomic-radius"));
                        elem.setMeltingPoint(findDoubleValue(nl,"melting-point"));
                        elem.setBoilingPoint(findDoubleValue(nl,"boiling-point"));
                        elem.setDensity(findDoubleValue(nl,"density"));
                        elem.setSpecificHeat(findDoubleValue(nl,"specific-heat"));
                        elem.setElectricalConductivity(findDoubleValue(nl,"electrical-conductivity"));
                        elem.setThermalConductivity(findDoubleValue(nl,"thermal-conductivity"));
                        return elem;
                } catch(Exception e) {
                        return null;
                }
        }
        private static String findStringValue(NodeList nl,String name) {
                Node item;
                for(int i=0;i<nl.getLength();i++) {
                        item=nl.item(i);
                        if(item.getNodeName().equals(name))
                                return item.getFirstChild().getNodeValue();
                }
                return "";
        }
        private static double findDoubleValue(NodeList nl,String name) {
                Node item;
                for(int i=0;i<nl.getLength();i++) {
                        item=nl.item(i);
                        if(item.getNodeName().equals(name))
                                return Double.parseDouble(item.getFirstChild().getNodeValue());
                }
                return Double.NaN;
        }
}

