package JSci.physics.relativity;

import JSci.GlobalSettings;
import JSci.maths.*;

/**
* The Rank4Tensor class encapsulates 4th rank tensors.
* @version 1.0
* @author Mark Hale
*/
public class Rank4Tensor extends Tensor {
        protected double rank4[][][][];

        /**
        * Constructs a 4th rank tensor.
        */
        public Rank4Tensor() {
                rank4=new double[4][4][4][4];
        }
        /**
        * Compares two tensors for equality.
        * @param a a 4th rank tensor
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof Rank4Tensor)) {
                        Rank4Tensor v=(Rank4Tensor)a;
                        for(int k,j,i=0;i<4;i++) {
                                for(j=0;j<4;j++) {
                                        for(k=0;k<4;k++) {
                                                if(Math.abs(rank4[i][j][k][0]-v.rank4[i][j][k][0])>GlobalSettings.ZERO_TOL ||
                                                        Math.abs(rank4[i][j][k][1]-v.rank4[i][j][k][1])>GlobalSettings.ZERO_TOL ||
                                                        Math.abs(rank4[i][j][k][2]-v.rank4[i][j][k][2])>GlobalSettings.ZERO_TOL ||
                                                        Math.abs(rank4[i][j][k][3]-v.rank4[i][j][k][3])>GlobalSettings.ZERO_TOL)
                                                        return false;
                                        }
                                }
                        }
                        return true;
                }
                return false;
        }
        /**
        * Returns a component of this tensor.
        * @param i 1st index
        * @param j 2nd index
        * @param k 3rd index
        * @param l 4th index
        * @exception DimensionException If attempting to access an invalid component.
        */
        public double getComponent(int i,int j,int k,int l) {
                if(i>=0 && i<4 && j>=0 && j<4 && k>=0 && k<4 && l>=0 && l<4)
                        return rank4[i][j][k][l];
                else
                        throw new DimensionException("Invalid component.");
        }
        /**
        * Sets the value of a component of this tensor.
        * @param i 1st index
        * @param j 2nd index
        * @param k 3rd index
        * @param l 4th index
        * @param x value
        * @exception DimensionException If attempting to access an invalid component.
        */
        public void setComponent(int i,int j,int k,int l,double x) {
                if(i>=0 && i<4 && j>=0 && j<4 && k>=0 && k<4 && l>=0 && l<4)
                        rank4[i][j][k][l]=x;
                else
                        throw new DimensionException("Invalid component.");
        }

//============
// OPERATIONS
//============

// ADDITION

        /**
        * Returns the addition of this tensor and another.
        * @param t a 4th rank tensor
        */
        public Rank4Tensor add(Rank4Tensor t) {
                Rank4Tensor ans=new Rank4Tensor();
                for(int k,j,i=0;i<4;i++) {
                        for(j=0;j<4;j++) {
                                for(k=0;k<4;k++) {
                                        ans.setComponent(i,j,k,0,rank4[i][j][k][0]+t.rank4[i][j][k][0]);
                                        ans.setComponent(i,j,k,1,rank4[i][j][k][1]+t.rank4[i][j][k][1]);
                                        ans.setComponent(i,j,k,2,rank4[i][j][k][2]+t.rank4[i][j][k][2]);
                                        ans.setComponent(i,j,k,3,rank4[i][j][k][3]+t.rank4[i][j][k][3]);
                                }
                        }
                }
                return ans;
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this tensor by another.
        * @param t a 4th rank tensor
        */
        public Rank4Tensor subtract(Rank4Tensor t) {
                Rank4Tensor ans=new Rank4Tensor();
                for(int k,j,i=0;i<4;i++) {
                        for(j=0;j<4;j++) {
                                for(k=0;k<4;k++) {
                                        ans.setComponent(i,j,k,0,rank4[i][j][k][0]-t.rank4[i][j][k][0]);
                                        ans.setComponent(i,j,k,1,rank4[i][j][k][1]-t.rank4[i][j][k][1]);
                                        ans.setComponent(i,j,k,2,rank4[i][j][k][2]-t.rank4[i][j][k][2]);
                                        ans.setComponent(i,j,k,3,rank4[i][j][k][3]-t.rank4[i][j][k][3]);
                                }
                        }
                }
                return ans;
        }
}

