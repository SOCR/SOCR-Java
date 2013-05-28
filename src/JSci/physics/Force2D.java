package JSci.physics;

/**
* The Force2D class provides an object for
* encapsulating forces and torques in 2D.
* @version 1.0
* @author Mark Hale
*/
public class Force2D extends Object implements java.io.Serializable {
        protected double Fx,Fy;
        protected double T;
        /**
        * Constructs a force that acts on the centre of mass.
        */
        public Force2D(double fx,double fy) {
                Fx=fx;
                Fy=fy;
        }
        /**
        * Constructs a torque.
        */
        public Force2D(double w) {
                T=w;
        }
        /**
        * Constructs a force and a torque.
        */
        public Force2D(double fx,double fy,double w) {
                Fx=fx;
                Fy=fy;
                T=w;
        }
        /**
        * Constructs a force acting at a point.
        */
        public Force2D(double fx,double fy,double x,double y) {
                T=x*fy-y*fx;
                final double k=(x*fx+y*fy)/(x*x+y*y);
                Fx=k*x;
                Fy=k*y;
        }
        /**
        * Returns the addition of this force and another.
        */
        public Force2D add(Force2D F) {
                return new Force2D(Fx+F.Fx,Fy+F.Fy,T+F.T);
        }
        /**
        * Returns the subtraction of this force by another.
        */
        public Force2D subtract(Force2D F) {
                return new Force2D(Fx-F.Fx,Fy-F.Fy,T-F.T);
        }
        public double getXComponent() {
                return Fx;
        }
        public double getYComponent() {
                return Fy;
        }
        public double getTorque() {
                return T;
        }
}

