package JSci.physics.relativity;

import JSci.GlobalSettings;
import JSci.maths.*;

/**
* The Rank3Tensor class encapsulates 3rd rank tensors.
* @version 1.0
* @author Mark Hale
*/
public class Rank3Tensor extends Tensor {
        protected double rank3[][][];

        /**
        * Constructs a 3rd rank tensor.
        */
        public Rank3Tensor() {
                rank3=new double[4][4][4];
        }
        /**
        * Compares two tensors for equality.
        * @param a a 3rd rank tensor
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof Rank3Tensor)) {
                        Rank3Tensor v=(Rank3Tensor)a;
                        for(int j,i=0;i<4;i++) {
                                for(j=0;j<4;j++) {
                                        if(Math.abs(rank3[i][j][0]-v.rank3[i][j][0])>GlobalSettings.ZERO_TOL ||
                                                Math.abs(rank3[i][j][1]-v.rank3[i][j][1])>GlobalSettings.ZERO_TOL ||
                                                Math.abs(rank3[i][j][2]-v.rank3[i][j][2])>GlobalSettings.ZERO_TOL ||
                                                Math.abs(rank3[i][j][3]-v.rank3[i][j][3])>GlobalSettings.ZERO_TOL)
                                                return false;
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
        * @exception DimensionException If attempting to access an invalid component.
        */
        public double getComponent(int i, int j,int k) {
                if(i>=0 && i<4 && j>=0 && j<4 && k>=0 && k<4)
                        return rank3[i][j][k];
                else
                        throw new DimensionException("Invalid component.");
        }
        /**
        * Sets the value of a component of this tensor.
        * @param i 1st index
        * @param j 2nd index
        * @param k 3rd index
        * @param x value
        * @exception DimensionException If attempting to access an invalid component.
        */
        public void setComponent(int i,int j,int k,double x) {
                if(i>=0 && i<4 && j>=0 && j<4 && k>=0 && k<4)
                        rank3[i][j][k]=x;
                else
                        throw new DimensionException("Invalid component.");
        }

//============
// OPERATIONS
//============

// ADDITION

        /**
        * Returns the addition of this tensor and another.
        * @param t a 3rd rank tensor
        */
        public Rank3Tensor add(Rank3Tensor t) {
                Rank3Tensor ans=new Rank3Tensor();
                for(int j,i=0;i<4;i++) {
                        for(j=0;j<4;j++) {
                                ans.setComponent(i,j,0,rank3[i][j][0]+t.rank3[i][j][0]);
                                ans.setComponent(i,j,1,rank3[i][j][1]+t.rank3[i][j][1]);
                                ans.setComponent(i,j,2,rank3[i][j][2]+t.rank3[i][j][2]);
                                ans.setComponent(i,j,3,rank3[i][j][3]+t.rank3[i][j][3]);
                        }
                }
                return ans;
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this tensor by another.
        * @param t a 3rd rank tensor
        */
        public Rank3Tensor subtract(Rank3Tensor t) {
                Rank3Tensor ans=new Rank3Tensor();
                for(int j,i=0;i<4;i++) {
                        for(j=0;j<4;j++) {
                                ans.setComponent(i,j,0,rank3[i][j][0]-t.rank3[i][j][0]);
                                ans.setComponent(i,j,1,rank3[i][j][1]-t.rank3[i][j][1]);
                                ans.setComponent(i,j,2,rank3[i][j][2]-t.rank3[i][j][2]);
                                ans.setComponent(i,j,3,rank3[i][j][3]-t.rank3[i][j][3]);
                        }
                }
                return ans;
        }

// TENSOR PRODUCT

        /**
        * Returns the tensor product of this tensor and another.
        * @param t a 1st rank tensor
        */
        public Rank4Tensor tensorProduct(Rank1Tensor t) {
                Rank4Tensor ans=new Rank4Tensor();
                for(int k,j,i=0;i<4;i++) {
                        for(j=0;j<4;j++) {
                                for(k=0;k<4;k++) {
                                        ans.setComponent(i,j,k,0,rank3[i][j][k]*t.getComponent(0));
                                        ans.setComponent(i,j,k,1,rank3[i][j][k]*t.getComponent(1));
                                        ans.setComponent(i,j,k,2,rank3[i][j][k]*t.getComponent(2));
                                        ans.setComponent(i,j,k,3,rank3[i][j][k]*t.getComponent(3));
                                }
                        }
                }
                return ans;
        }
}

