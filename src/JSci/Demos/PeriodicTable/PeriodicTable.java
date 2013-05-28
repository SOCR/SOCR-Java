package JSci.Demos.PeriodicTable;

import java.awt.*;
import java.awt.event.*;
import JSci.chemistry.*;
import JSci.chemistry.periodictable.*;

/**
* Periodic Table.
* @author Mark Hale
* @version 1.3
*/
public final class PeriodicTable extends Frame {
        private static final Color purple=new Color(0.75f,0.75f,1.0f);
        private GridBagLayout gb=new GridBagLayout();
        private GridBagConstraints gbc=new GridBagConstraints();

        public static void main(String argv[]) {
                new PeriodicTable();
        }
        public PeriodicTable() {
                super("Periodic Table");
                addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent evt) {
                                dispose();
                                System.exit(0);
                        }
                });
                System.out.print("Loading elements...");
                createTable();
                System.out.println("done.");
                setSize(650,400);
                setVisible(true);
        }
        private void createTable() {
                gbc.fill=GridBagConstraints.BOTH;
                setLayout(gb);
                gbc.gridx=1;
                gbc.gridy=0;
                createLabel("1");
                gbc.gridx++;
                createLabel("2");
                gbc.gridx++;
                createLabel("3");
                gbc.gridx++;
                createLabel("4");
                gbc.gridx++;
                createLabel("5");
                gbc.gridx++;
                createLabel("6");
                gbc.gridx++;
                createLabel("7");
                gbc.gridx++;
                createLabel("8");
                gbc.gridx++;
                createLabel("9");
                gbc.gridx++;
                createLabel("10");
                gbc.gridx++;
                createLabel("11");
                gbc.gridx++;
                createLabel("12");
                gbc.gridx++;
                createLabel("13");
                gbc.gridx++;
                createLabel("14");
                gbc.gridx++;
                createLabel("15");
                gbc.gridx++;
                createLabel("16");
                gbc.gridx++;
                createLabel("17");
                gbc.gridx++;
                createLabel("18");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("1");
                gbc.gridx++;
                createButton("Hydrogen");
                gbc.gridx+=17;
                createButton("Helium");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("2");
                gbc.gridx++;
                createButton("Lithium");
                gbc.gridx++;
                createButton("Beryllium");
                gbc.gridx+=11;
                createButton("Boron");
                gbc.gridx++;
                createButton("Carbon");
                gbc.gridx++;
                createButton("Nitrogen");
                gbc.gridx++;
                createButton("Oxygen");
                gbc.gridx++;
                createButton("Fluorine");
                gbc.gridx++;
                createButton("Neon");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("3");
                gbc.gridx++;
                createButton("Sodium");
                gbc.gridx++;
                createButton("Magnesium");
                gbc.gridx+=11;
                createButton("Aluminium");
                gbc.gridx++;
                createButton("Silicon");
                gbc.gridx++;
                createButton("Phosphorus");
                gbc.gridx++;
                createButton("Sulphur");
                gbc.gridx++;
                createButton("Chlorine");
                gbc.gridx++;
                createButton("Argon");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("4");
                gbc.gridx++;
                createButton("Potassium");
                gbc.gridx++;
                createButton("Calcium");
                gbc.gridx++;
                createButton("Scandium");
                gbc.gridx++;
                createButton("Titanium");
                gbc.gridx++;
                createButton("Vanadium");
                gbc.gridx++;
                createButton("Chromium");
                gbc.gridx++;
                createButton("Manganese");
                gbc.gridx++;
                createButton("Iron");
                gbc.gridx++;
                createButton("Cobalt");
                gbc.gridx++;
                createButton("Nickel");
                gbc.gridx++;
                createButton("Copper");
                gbc.gridx++;
                createButton("Zinc");
                gbc.gridx++;
                createButton("Gallium");
                gbc.gridx++;
                createButton("Germanium");
                gbc.gridx++;
                createButton("Arsenic");
                gbc.gridx++;
                createButton("Selenium");
                gbc.gridx++;
                createButton("Bromine");
                gbc.gridx++;
                createButton("Krypton");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("5");
                gbc.gridx++;
                createButton("Rubidium");
                gbc.gridx++;
                createButton("Strontium");
                gbc.gridx++;
                createButton("Yttrium");
                gbc.gridx++;
                createButton("Zirconium");
                gbc.gridx++;
                createButton("Niobium");
                gbc.gridx++;
                createButton("Molybdenum");
                gbc.gridx++;
                createButton("Technetium");
                gbc.gridx++;
                createButton("Ruthenium");
                gbc.gridx++;
                createButton("Rhodium");
                gbc.gridx++;
                createButton("Palladium");
                gbc.gridx++;
                createButton("Silver");
                gbc.gridx++;
                createButton("Cadmium");
                gbc.gridx++;
                createButton("Indium");
                gbc.gridx++;
                createButton("Tin");
                gbc.gridx++;
                createButton("Antimony");
                gbc.gridx++;
                createButton("Tellurium");
                gbc.gridx++;
                createButton("Iodine");
                gbc.gridx++;
                createButton("Xenon");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("6");
                gbc.gridx++;
                createButton("Caesium");
                gbc.gridx++;
                createButton("Barium");
                gbc.gridx++;
                createButton("Lanthanum");
                gbc.gridx++;
                createButton("Hafnium");
                gbc.gridx++;
                createButton("Tantalum");
                gbc.gridx++;
                createButton("Tungsten");
                gbc.gridx++;
                createButton("Rhenium");
                gbc.gridx++;
                createButton("Osmium");
                gbc.gridx++;
                createButton("Iridium");
                gbc.gridx++;
                createButton("Platinum");
                gbc.gridx++;
                createButton("Gold");
                gbc.gridx++;
                createButton("Mercury");
                gbc.gridx++;
                createButton("Thallium");
                gbc.gridx++;
                createButton("Lead");
                gbc.gridx++;
                createButton("Bismuth");
                gbc.gridx++;
                createButton("Polonium");
                gbc.gridx++;
                createButton("Astatine");
                gbc.gridx++;
                createButton("Radon");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("7");
                gbc.gridx++;
                createButton("Francium");
                gbc.gridx++;
                createButton("Radium");
                gbc.gridx++;
                createButton("Actinium");
                gbc.gridx++;
                createButton("Unnilquadium");
                gbc.gridx++;
                createButton("Unnilpentium");
                gbc.gridx++;
                createButton("Unnilhexium");
                gbc.gridx++;
                createButton("Unnilseptium");
                gbc.gridx++;
                createButton("Unniloctium");
                gbc.gridx++;
                createButton("Unnilennium");
                gbc.gridx++;
                createButton("Ununnilium");
                gbc.gridx=0;
                gbc.gridy++;
                Label blankline=new Label();
                gb.setConstraints(blankline,gbc);
                add(blankline);
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("6");
                gbc.gridx+=4;
                createButton("Cerium");
                gbc.gridx++;
                createButton("Praseodymium");
                gbc.gridx++;
                createButton("Neodymium");
                gbc.gridx++;
                createButton("Promethium");
                gbc.gridx++;
                createButton("Samarium");
                gbc.gridx++;
                createButton("Europium");
                gbc.gridx++;
                createButton("Gadolinium");
                gbc.gridx++;
                createButton("Terbium");
                gbc.gridx++;
                createButton("Dysprosium");
                gbc.gridx++;
                createButton("Holmium");
                gbc.gridx++;
                createButton("Erbium");
                gbc.gridx++;
                createButton("Thulium");
                gbc.gridx++;
                createButton("Ytterbium");
                gbc.gridx++;
                createButton("Lutetium");
                gbc.gridx=0;
                gbc.gridy++;
                createLabel("7");
                gbc.gridx+=4;
                createButton("Thorium");
                gbc.gridx++;
                createButton("Protactinium");
                gbc.gridx++;
                createButton("Uranium");
                gbc.gridx++;
                createButton("Neptunium");
                gbc.gridx++;
                createButton("Plutonium");
                gbc.gridx++;
                createButton("Americium");
                gbc.gridx++;
                createButton("Curium");
                gbc.gridx++;
                createButton("Berkelium");
                gbc.gridx++;
                createButton("Californium");
                gbc.gridx++;
                createButton("Einsteinium");
                gbc.gridx++;
                createButton("Fermium");
                gbc.gridx++;
                createButton("Mendelevium");
                gbc.gridx++;
                createButton("Nobelium");
                gbc.gridx++;
                createButton("Lawrencium");
        }
        private void createLabel(String text) {
                Label label=new Label(text,Label.CENTER);
                gb.setConstraints(label,gbc);
                add(label);
        }
        private void createButton(String elemName) {
                Element elem=JSci.chemistry.PeriodicTable.getElement(elemName);
                if(elem==null)
                        return;
                Button but=new Button(elem.toString());
                if(elem instanceof AlkaliMetal)
                        but.setBackground(Color.cyan);
                else if(elem instanceof AlkaliEarthMetal)
                        but.setBackground(purple);
                else if(elem instanceof NonMetal)
                        but.setBackground(Color.green);
                else if(elem instanceof TransitionMetal)
                        but.setBackground(Color.orange);
                else if(elem instanceof RareEarthMetal)
                        but.setBackground(Color.red);
                else if(elem instanceof Halogen)
                        but.setBackground(Color.yellow);
                else if(elem instanceof NobleGas)
                        but.setBackground(Color.pink);
                but.addActionListener(new ButtonAdapter(this,elem));
                gb.setConstraints(but,gbc);
                add(but);
        }
        class ButtonAdapter implements ActionListener {
                private Frame owner;
                private Element element;
                public ButtonAdapter(Frame f,Element e) {
                        owner=f;
                        element=e;
                }
                public void actionPerformed(ActionEvent evt) {
                        new InfoDialog(owner,element);
                }
        }
        static class InfoDialog extends Dialog {
                private void displayNumber(double x) {
                        if(x==x)
                                add(new Label(String.valueOf(x)));
                        else /* NaN */
                                add(new Label("Unknown"));
                }
                private void displayNumber(double x,String units) {
                        if(x==x)
                                add(new Label(String.valueOf(x)+' '+units));
                        else /* NaN */
                                add(new Label("Unknown"));
                }
                public InfoDialog(Frame parent,Element e) {
                        super(parent,e.getName());
                        addWindowListener(new WindowAdapter() {
                                public void windowClosing(WindowEvent evt) {
                                        dispose();
                                }
                        });
                        setLayout(new GridLayout(11,2));
                        add(new Label("Atomic Number"));
                        add(new Label(String.valueOf(e.getAtomicNumber())));
                        add(new Label("Mass Number"));
                        add(new Label(String.valueOf(e.getMassNumber())));
                        add(new Label("Density"));
                        displayNumber(e.getDensity(),"g/cm^3");
                        add(new Label("Boiling Point"));
                        displayNumber(e.getBoilingPoint(),"K");
                        add(new Label("Melting Point"));
                        displayNumber(e.getMeltingPoint(),"K");
                        add(new Label("Atomic Radius"));
                        displayNumber(e.getAtomicRadius());
                        add(new Label("Covalent Radius"));
                        displayNumber(e.getCovalentRadius());
                        add(new Label("Electronegativity"));
                        displayNumber(e.getElectronegativity());
                        add(new Label("Specific Heat"));
                        displayNumber(e.getSpecificHeat());
                        add(new Label("Electrical Conductivity"));
                        displayNumber(e.getElectricalConductivity());
                        add(new Label("Thermal Conductivity"));
                        displayNumber(e.getThermalConductivity());
                        setSize(300,300);
                        setVisible(true);
                }
        }
}

