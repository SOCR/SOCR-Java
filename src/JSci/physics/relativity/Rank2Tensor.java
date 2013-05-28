package JSci.physics.relativity;

import JSci.GlobalSettings;
import JSci.maths.*;

/**
* The Rank2Tensor class encapsulates 2nd rank tensors.
* @version 1.0
* @author Mark Hale
*/
public class Rank2Tensor extends Tensor {
        protected double rank2[][];

        /**
        * Constructs a 2nd rank tensor.
        */
        public Rank2Tensor() {
                rank2=new double[4][4];
        }
        /**
        * Compares two tensors for equality.
        * @param a a 2nd rank tensor
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof Rank2Tensor)) {
                        Rank2Tensor v=(Rank2Tensor)a;
                        for(int i=0;i<4;i++) {
                                if(Math.abs(rank2[i][0]-v.rank2[i][0])>GlobalSettings.ZERO_TOL ||
                                        Math.abs(rank2[i][1]-v.rank2[i][1])>GlobalSettings.ZERO_TOL ||
                                        Math.abs(rank2[i][2]-v.rank2[i][2])>GlobalSettings.ZERO_TOL ||
                                        Math.abs(rank2[i][3]-v.rank2[i][3])>GlobalSettings.ZERO_TOL)
                                        return false;
                        }
                        return true;
                }
                return false;
        }
        /**
        * Returns a component of this tensor.
        * @param i 1st index
        * @param j 2nd index
        * @exception DimensionException If attempting to access an invalid component.
        */
        public double getComponent(int i, int j) {
                if(i>=0 && i<4 && j>=0 && j<4)
                        return rank2[i][j];
                else
                        throw new DimensionException("Invalid component.");
        }
        /**
        * Sets the value of a component of this tensor.
        * @param i 1st index
        * @param j 2nd index
        * @param x value
        * @exception DimensionException If attempting to access an invalid component.
        */
        public void setComponent(int i,int j,double x) {
                if(i>=0 && i<4 && j>=0 && j<4)
                        rank2[i][j]=x;
                else
                        throw new DimensionException("Invalid component.");
        }

//============
// OPERATIONS
//============

// ADDITION

       /**
        * Returns the addition of this tensor and another.
        * @param t a 2nd rank tensor
        */
        public Rank2Tensor add(Rank2Tensor t) {
                Rank2Tensor ans=new Rank2Tensor();
                for(int i=0;i<4;i++) {
                        ans.setComponent(i,0,rank2[i][0]+t.rank2[i][0]);
                        ans.setComponent(i,1,rank2[i][1]+t.rank2[i][1]);
                        ans.setComponent(i,2,rank2[i][2]+t.rank2[i][2]);
                        ans.setComponent(i,3,rank2[i][3]+t.rank2[i][3]);
                }
                return ans;
        }

// SUBTRACTION

       /**
        * Returns the subtraction of this tensor by another.
        * @param t a 2nd rank tensor
        */
        public Rank2Tensor subtract(Rank2Tensor t) {
                Rank2Tensor ans=new Rank2Tensor();
                for(int i=0;i<4;i++) {
                        ans.setComponent(i,0,rank2[i][0]-t.rank2[i][0]);
                        ans.setComponent(i,1,rank2[i][1]-t.rank2[i][1]);
                        ans.setComponent(i,2,rank2[i][2]-t.rank2[i][2]);
                        ans.setComponent(i,3,rank2[i][3]-t.rank2[i][3]);
                }
                return ans;
        }

// MULTIPLY

        /**
        * Returns the multiplication of this tensor by another.
        * @param t a 1st rank tensor
        */
        public Rank1Tensor multiply(Rank1Tensor t) {
                Rank1Tensor ans=new Rank1Tensor();
                ans.setComponent(0,rank2[0][0]*t.getComponent(0)+rank2[0][1]*t.getComponent(1)+rank2[0][2]*t.getComponent(2)+rank2[0][3]*t.getComponent(3));
                ans.setComponent(1,rank2[1][0]*t.getComponent(0)+rank2[1][1]*t.getComponent(1)+rank2[1][2]*t.getComponent(2)+rank2[1][3]*t.getComponent(3));
                ans.setComponent(2,rank2[2][0]*t.getComponent(0)+rank2[2][1]*t.getComponent(1)+rank2[2][2]*t.getComponent(2)+rank2[2][3]*t.getComponent(3));
                ans.setComponent(3,rank2[3][0]*t.getComponent(0)+rank2[3][1]*t.getComponent(1)+rank2[3][2]*t.getComponent(2)+rank2[3][3]*t.getComponent(3));
                return ans;
        }

// TENSOR PRODUCT

        /**
        * Returns the tensor product of this tensor and another.
        * @param t a 1st rank tensor
        */
        public Rank3Tensor tensorProduct(Rank1Tensor t) {
                Rank3Tensor ans=new Rank3Tensor();
                for(int j,i=0;i<4;i++) {
                        for(j=0;j<4;j++) {
                                ans.setComponent(i,j,0,rank2[i][j]*t.getComponent(0));
                                ans.setComponent(i,j,1,rank2[i][j]*t.getComponent(1));
                                ans.setComponent(i,j,2,rank2[i][j]*t.getComponent(2));
                                ans.setComponent(i,j,3,rank2[i][j]*t.getComponent(3));
                        }
                }
                return ans;
        }
        /**
        * Returns the tensor product of this tensor and another.
        * @param t a 2nd rank tensor
        */
        public Rank4Tensor tensorProduct(Rank2Tensor t) {
                Rank4Tensor ans=new Rank4Tensor();
                for(int k,j,i=0;i<4;i++) {
                        for(j=0;j<4;j++) {
                                for(k=0;k<4;k++) {
                                        ans.setComponent(i,j,k,0,rank2[i][j]*t.rank2[k][0]);
                                        ans.setComponent(i,j,k,1,rank2[i][j]*t.rank2[k][1]);
                                        ans.setComponent(i,j,k,2,rank2[i][j]*t.rank2[k][2]);
                                        ans.setComponent(i,j,k,3,rank2[i][j]*t.rank2[k][3]);
                                }
                        }
                }
                return ans;
        }
}

