package JSci.physics.relativity;

import JSci.maths.*;

/**
* The EMFieldTensor class encapsulates the electromagnetic field tensor.
* @version 1.0
* @author Mark Hale
*/
public final class EMFieldTensor extends Rank2Tensor {
        /**
        * Constructs an electromagnetic field tensor.
        * @param E electric field
        * @param B magnetic field
        */
        public EMFieldTensor(DoubleVector E,DoubleVector B) {
                rank2[0][0]=rank2[1][1]=rank2[2][2]=rank2[3][3]=0.0;
                rank2[1][0]=E.getComponent(0);
                rank2[2][0]=E.getComponent(1);
                rank2[3][0]=E.getComponent(2);
                rank2[3][2]=B.getComponent(0);
                rank2[1][3]=B.getComponent(1);
                rank2[2][1]=B.getComponent(2);
                rank2[0][1]=-rank2[1][0];
                rank2[0][2]=-rank2[2][0];
                rank2[0][3]=-rank2[3][0];
                rank2[2][3]=-rank2[3][2];
                rank2[3][1]=-rank2[1][3];
                rank2[1][2]=-rank2[2][1];
        }
}

