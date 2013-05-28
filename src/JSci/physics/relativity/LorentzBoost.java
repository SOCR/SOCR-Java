package JSci.physics.relativity;

import JSci.maths.*;

/**
* The LorentzBoost class encapsulates the Lorentz boosts.
* @version 1.0
* @author Mark Hale
*/
public final class LorentzBoost extends Rank2Tensor {
        /**
        * Constructs a Lorentz boost.
        * @param v velocity
        */
        public LorentzBoost(DoubleVector v) {
                this(v.getComponent(0),v.getComponent(1),v.getComponent(2));
        }
        /**
        * Constructs a Lorentz boost.
        * @param vx x-velocity
        * @param vy y-velocity
        * @param vz z-velocity
        */
        public LorentzBoost(double vx,double vy,double vz) {
                final double vv=vx*vx+vy*vy+vz*vz;
                final double gamma=1.0/Math.sqrt(1.0-vv);
                final double k=(gamma-1.0)/vv;
                rank2[0][0]=gamma;
                rank2[0][1]=rank2[1][0]=-gamma*vx;
                rank2[0][2]=rank2[2][0]=-gamma*vy;
                rank2[0][3]=rank2[3][0]=-gamma*vz;
                rank2[1][1]=1.0+k*vx*vx;
                rank2[1][2]=rank2[2][1]=k*vx*vy;
                rank2[1][3]=rank2[3][1]=k*vx*vz;
                rank2[2][2]=1.0+k*vy*vy;
                rank2[2][3]=rank2[3][2]=k*vy*vz;
                rank2[3][3]=1.0+k*vz*vz;
        }
}

