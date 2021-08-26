

//	var EPSILON = 1/10000000000; //CONST
	
	//checks if it is symmetric
	function isSymmetric(A){
		var N = A.length;
		for(var i=0;i<N;i++)
		{
			for(var j =0; j<i;j++)
			{
				if(A[i][j] != A[j][i]){
					return false;
				}
			}
		}
		return true;
	}

	function isSquare(A){
        var N = A.length;
        for (var i = 0; i < N; i++) {
            if (A[i].length != N) {return false;}
        }
        return true;
    }
/////////////////////////////////////converted til here////////////////////

    // return Cholesky factor L of psd matrix A = L L^T
    function choleskyFactor(A){
        if (!isSquare(A)) {
            throw "RuntimeException: Matrix is not square";
        }
        if (!isSymmetric(A)) {
            throw "RuntimeException: Matrix is not symmetric";
        }

        var N  = A.length;
        var L = new Array(); //set every element to an array later to make 2D

        for (var i = 0; i < N; i++)  {
			L[i] = new Array();
            for (var j = 0; j <= i; j++) {
                var sum = 0.0;
                for (var k = 0; k < j; k++) {
                    sum += L[i][k] * L[j][k];
                }
                if (i == j){ 
					L[i][i] = Math.sqrt(A[i][i] - sum);
				}
                else{
				L[i][j] = 1.0 / L[j][j] * (A[i][j] - sum);
				}
            }
            if (L[i][i] <= 0) {
                throw "RuntimeException: Matrix not positive definite";
            }
        }
        return L;
    }