//var L;
//var epsilon  = new Array();
function sample()
{
	var L;
	var epsilon  = new Array();
	var A = new Array();
	A[0] = new Array();
	A[1] = new Array();
	
	A[0][0] = sigma1*sigma1;//.0061;
	A[0][1] = sigma1*sigma2*rowS;//0.11*Math.sqrt(0.0061)*Math.sqrt(0.0046);
	A[1][0] = sigma1*sigma2*rowS;//0.11*Math.sqrt(0.0061)*Math.sqrt(0.0046);;
	A[1][1] = sigma2*sigma2;//0.0046;
//	////console.log(A);
	L = choleskyFactor(A);
	L[0][1] = 0;
	//////console.log("L",L[0][0], L[0][1], L[1][0], L[1][1]);
	//add matrix multiplication code
	var n1 = new NormalDistribution(0,1);
	var inrange = 0;
	var xy;
	var Mu = [mu1,mu2];
	var counter = 500000;
	for(var i=0; i<counter;i++)
	{
			epsilon[0] = n1.simulate().toFixed(7);
			epsilon[1] = n1.simulate().toFixed(7);
			
			xy = mult(L,epsilon);
			xy[0]+=Mu[0];
			xy[1]+=Mu[1];
	//		////console.log(xCut,yCut,xLowCut,yLowCut);
			if(xy[0]<xCut &&xy[1]<yCut && xy[0]> xLowCut &&xy[1]>yLowCut){inrange++;}
			
	}
	
	//////console.log("inrange",inrange);
	return inrange/counter;
}
//sample()
////////console.log("L",L);
////////console.log("epsilon",epsilon);
////////console.log("xy mat",mult());
//var epsilon  = 
////////console.log(epsilon);
function mult(L, epsilon){
	////////console.log("test called");
	var result = new Array();
	for(var i = 0; i<2;i++){
		var sum = 0;
		
		for(var j = 0; j<2;j++){
		//	//////console.log(L[i][j]);
			////////console.log(L[i][j]*epsilon[j]);
			sum=sum+L[i][j]*epsilon[j];
		}
		result[i] = sum;
	}
	//result[0] +=mu1;
	//result[1]+=mu2;
	return result;
}

//NOTE ABOUT TOOL TIP, EVERYTHING IS BEING SHIFTED TO LEFT