package JSci.chemistry;

/**
* A class representing chemical elements.
* @version 1.6
* @author Mark Hale
*/
public class Element extends Object implements java.io.Serializable {
        private String name;
        private String symbol;
        private int atomicNumber;
        private int massNumber;
        private double electronegativity;
        private double covalentRadius;
        private double atomicRadius;
        private double meltingPoint;
        private double boilingPoint;
        private double density;
        private double specificHeat;
        private double electricalConductivity;
        private double thermalConductivity;
        /**
        * Constructs an element.
        */
        public Element(String title,String label) {
                name=title;
                symbol=label;
        }
        /**
        * Returns the name.
        */
        public String getName() {
                return name;
        }
        /**
        * Returns the atomic number.
        */
        public int getAtomicNumber() {
                return atomicNumber;
        }
        protected void setAtomicNumber(int z) {
                atomicNumber=z;
        }
        /**
        * Returns the mass number.
        */
        public int getMassNumber() {
                return massNumber;
        }
        protected void setMassNumber(int m) {
                massNumber=m;
        }
        /**
        * Returns the electronegativity.
        */
        public double getElectronegativity() {
                return electronegativity;
        }
        protected void setElectronegativity(double en) {
                electronegativity=en;
        }
        /**
        * Returns the covalent radius.
        */
        public double getCovalentRadius() {
                return covalentRadius;
        }
        protected void setCovalentRadius(double covRadius) {
                covalentRadius=covRadius;
        }
        /**
        * Returns the atomic radius.
        */
        public double getAtomicRadius() {
                return atomicRadius;
        }
        protected void setAtomicRadius(double atomRadius) {
                atomicRadius=atomRadius;
        }
        /**
        * Returns the melting point (K).
        */
        public double getMeltingPoint() {
                return meltingPoint;
        }
        protected void setMeltingPoint(double melt) {
                meltingPoint=melt;
        }
        /**
        * Returns the boiling point (K).
        */
        public double getBoilingPoint() {
                return boilingPoint;
        }
        protected void setBoilingPoint(double boil) {
                boilingPoint=boil;
        }
        /**
        * Returns the density (293K).
        */
        public double getDensity() {
                return density;
        }
        protected void setDensity(double rho) {
                density=rho;
        }
        /**
        * Returns the specific heat.
        */
        public double getSpecificHeat() {
                return specificHeat;
        }
        protected void setSpecificHeat(double heat) {
                specificHeat=heat;
        }
        /**
        * Returns the electrical conductivity.
        */
        public double getElectricalConductivity() {
                return electricalConductivity;
        }
        protected void setElectricalConductivity(double elect) {
                electricalConductivity=elect;
        }
        /**
        * Returns the thermal conductivity.
        */
        public double getThermalConductivity() {
                return thermalConductivity;
        }
        protected void setThermalConductivity(double therm) {
                thermalConductivity=therm;
        }
        /**
        * Compares two elements for equality.
        * @param e an element.
        */
        public boolean equals(Object e) {
                return (e!=null) && (e instanceof Element) &&
                        (atomicNumber==((Element)e).atomicNumber) &&
                        (massNumber==((Element)e).massNumber);
        }
        /**
        * Returns the chemical symbol.
        */
        public String toString() {
                return symbol;
        }
}

