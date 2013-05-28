package JSci.physics.relativity;

/**
* The LeviCivita class encapsulates the Levi-Civita tensor.
* @version 1.0
* @author Mark Hale
*/
public final class LeviCivita extends Rank4Tensor {
        /**
        * Constructs the Levi-Civita tensor.
        */
        public LeviCivita() {
                rank4[0][1][2][3]=1.0;
                rank4[0][2][3][1]=1.0;
                rank4[0][3][1][2]=1.0;
                rank4[1][0][3][2]=1.0;
                rank4[1][2][0][3]=1.0;
                rank4[1][3][2][0]=1.0;
                rank4[2][0][1][3]=1.0;
                rank4[2][1][3][0]=1.0;
                rank4[2][3][0][1]=1.0;
                rank4[3][0][2][1]=1.0;
                rank4[3][1][0][2]=1.0;
                rank4[3][2][1][0]=1.0;

                rank4[0][1][3][2]=-1.0;
                rank4[0][2][1][3]=-1.0;
                rank4[0][3][2][1]=-1.0;
                rank4[1][0][2][3]=-1.0;
                rank4[1][2][3][0]=-1.0;
                rank4[1][3][0][2]=-1.0;
                rank4[2][0][3][1]=-1.0;
                rank4[2][1][0][3]=-1.0;
                rank4[2][3][1][0]=-1.0;
                rank4[3][0][1][2]=-1.0;
                rank4[3][1][2][0]=-1.0;
                rank4[3][2][0][1]=-1.0;
        }
}

