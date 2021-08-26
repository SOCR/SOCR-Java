//Normal Calculator
var dist, distGraph, muParam, sigmaParam;
var xParamLeft, xParamRight, pParamLeft, pParamBetween, pParamRight; 
var graphSelect;
var x1, x2, p1, p2;
var graphType = "pdf";

muInputUni = {min: 10, max: 10, step: 0.01, value: 2 };
sigmaInputUni = {min:10, max:10, step:0.01, value:3};
xInLeftUni = {min:-3, max:3, step: 0.01, value:-2};
xInRightUni = {min:-3, max:3, step: 0.01, value:2};
pInLeftUni = {min:0, max:1, step: 0.01, value:0.5};
pInBetUni = {min:0, max:1, step: 0.01, value:0.2};
pInRightUni = {min:0, max:1, step: 0.01, value:0.3};
muLabUni = {innerHTML: ""};
sigmaLabUni = {innerHTML: ""};
xLabLeftUni = {innerHTML: ""};
xLabRightUni = {innerHTML: ""};
pLabLeftUni = {innerHTML: ""};
pLabBetUni = {innerHTML: ""};
pLabRightUni = {innerHTML: ""};

function initialize(){

	distCanvas = document.getElementById("distCanvas");
	graphSelect = document.getElementById("graphSelect");
	distSelect = document.getElementById("distSelect");
	
	muParam = new Parameter(muInputUni,muLabUni);//document.getElementById("muInput"), document.getElementById("muLabel"));

	sigmaParam = new Parameter(sigmaInputUni,sigmaLabUni);//document.getElementById("sigmaInput"), document.getElementById("sigmaLabel"));

	if(setMargY){
		//muParam.setValue(mu2);
		//sigmaParam.setValue(sigma2);
		muParam.setProperties(mu2-50, mu2+50, 0.1, mu2, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigma2+50, 0.1, sigma2, "<var>\u03C3</var>");
		graphType = "pdf";
	}
	else if(setCondLowX){
		//muParam.setValue(mu1 + rowS*(sigma1/sigma2)*(((yLowCut==Number.NEGATIVE_INFINITY)? (mu2-4*sigma2):yLowCut) - mu1));
		//sigmaParam.setValue(sigma1*Math.sqrt(1-rowS*rowS));
		var muTmp = (mu1 + rowS*(sigma1/sigma2)*(((yLowCut==Number.NEGATIVE_INFINITY)? (mu2-4*sigma2):yLowCut) - mu2));
		var sigmaTmp = (sigma1*Math.sqrt(1-rowS*rowS));
		muParam.setProperties(muTmp-50, muTmp+50, 0.1, muTmp, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigmaTmp+50, 0.1, sigmaTmp, "<var>\u03C3</var>");
		graphType = "pdf";
	}
	else if(setCondLowY){
		//muParam.setValue(mu2 + rowS*(sigma2/sigma1)*(((xLowCut==Number.NEGATIVE_INFINITY)? (mu1-4*sigma1):xLowCut) - mu2));
		//sigmaParam.setValue(sigma2*Math.sqrt(1-rowS*rowS));
		var muTmp = (mu2 + rowS*(sigma2/sigma1)*(((xLowCut==Number.NEGATIVE_INFINITY)? (mu1-4*sigma1):xLowCut) - mu1));	
		var sigmaTmp = (sigma2*Math.sqrt(1-rowS*rowS));
		muParam.setProperties(muTmp-50, muTmp+50, 0.1, muTmp, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigmaTmp+50, 0.1, sigmaTmp, "<var>\u03C3</var>");
		graphType = "pdf";
	}
	else if(setCondX){//need to add other limit later
		//muParam.setValue(mu1 + rowS*(sigma1/sigma2)*(((yCut==Number.POSITIVE_INFINITY)? (mu2+4*sigma2):yLowCut) - mu1));
		//sigmaParam.setValue(sigma1*Math.sqrt(1-rowS*rowS));
		var muTmp = (mu1 + rowS*(sigma1/sigma2)*(((yCut==Number.POSITIVE_INFINITY)? (mu2+4*sigma2):yLowCut) - mu2));	
		var sigmaTmp = (sigma1*Math.sqrt(1-rowS*rowS));
		muParam.setProperties(muTmp-50, muTmp+50, 0.1, muTmp, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigmaTmp+50, 0.1, sigmaTmp, "<var>\u03C3</var>");
		graphType = "pdf";
	}
	else if(setCondY){
		//muParam.setValue(mu2 + rowS*(sigma2/sigma1)*(((xCut==Number.POSITIVE_INFINITY)? (mu1+4*sigma1):xCut) - mu2));
		//sigmaParam.setValue(sigma2*Math.sqrt(1-rowS*rowS));
		var muTmp = (mu2 + rowS*(sigma2/sigma1)*(((xCut==Number.POSITIVE_INFINITY)? (mu1+4*sigma1):xCut) - mu1));	
		var sigmaTmp = (sigma2*Math.sqrt(1-rowS*rowS));
		muParam.setProperties(muTmp-50, muTmp+50, 0.1, muTmp, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigmaTmp+50, 0.1, sigmaTmp, "<var>\u03C3</var>");
		graphType = "pdf";
	}
	else if (setMargX){
		muParam.setProperties(mu1-50, mu1+50, 0.1, mu1, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigma1+50, 0.1, sigma1, "<var>\u03C3</var>");
		graphType = "pdf";
	}
	else if (setCDFX){
		muParam.setProperties(mu1-50, mu1+50, 0.1, mu1, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigma1+50, 0.1, sigma1, "<var>\u03C3</var>");
		graphType = "cdf";
	}
	else if(setCDFY){
		muParam.setProperties(mu2-50, mu2+50, 0.1, mu2, "<var>\u03BC</var>");
		sigmaParam.setProperties(.1, sigma2+50, 0.1, sigma2, "<var>\u03C3</var>");
		graphType = "cdf";
	}
	
	xParamLeft = new Parameter(xInLeftUni, xLabLeftUni);//document.getElementById("xInputLeft"), document.getElementById("xLabelLeft"));
	xParamRight = new Parameter(xInRightUni,xLabRightUni);//document.getElementById("xInputRight"), document.getElementById("xLabelRight"));
	
	pParamLeft = new Parameter(pInLeftUni,pLabLeftUni);//document.getElementById("pInputLeft"), document.getElementById("pLabelLeft"));
	pParamLeft.setProperties(0.001, 0.999, 0.001, 0, "<var>p1</var>");
	pParamBetween = new Parameter(pInBetUni, pLabBetUni);//document.getElementById("pInputBetween"), document.getElementById("pLabelBetween"));
	pParamBetween.setProperties(0.001, 0.999, 0.001, 1, "<var>p2</var>");
	pParamRight = new Parameter(xLabRightUni,pLabRightUni);//document.getElementById("pInputRight"), document.getElementById("pLabelRight"));
	pParamRight.setProperties(0.001, 0.999, 0.001, 0.999, "<var>p3</var>");
	setDist();
	initi = true;
	textArea.scrollTop = textArea.scrollHeight;
	
	document.getElementById("MarginalX").checked=true;
}

function setDist(){
	

	dist = new NormalDistribution(muParam.getValue(), sigmaParam.getValue());

	xParamLeft.setProperties(dist.quantile(0.001), dist.quantile(0.999), 0.001, dist.quantile(0.001), "<var>x1</var>");
	xParamRight.setProperties(dist.quantile(0.001), dist.quantile(0.999), 0.001, dist.quantile(0.999), "<var>x2</var>");

	
	distGraph = new QuantileGraph2(distCanvas, dist, "X");
	distGraph.xFormat = 2;
	distGraph.setGraphType("pdf");
	setProb();
	///////////////////////////////
	if(setMargY||setCondLowY ||setCondY){
		xInLeftUni.value = yLowCut;
		xInRightUni.value = yCut;
	}
	else{
		xInLeftUni.value = xLowCut;
		xInRightUni.value = xCut;
	}

	setValue();
	if(initi){
	var text;
	if(setMargY){
		text = "P("+((yLowCut==Number.NEGATIVE_INFINITY)? "-∞":yLowCut)+" < Y < "+((yCut==Number.POSITIVE_INFINITY)? "∞":yCut)+") = "+pInBetUni.value+"\n";
		textArea.value += text; 
	}
	else if(setCondLowX){
		text = "P("+((xLowCut==Number.NEGATIVE_INFINITY)? "-∞":xLowCut)+" < X < "+((xCut==Number.POSITIVE_INFINITY)? "∞":xCut)+" | Y = "+((yLowCut==Number.NEGATIVE_INFINITY)? "-∞":yLowCut)+") = "+pInBetUni.value+"\n";
		textArea.value += text; 
	}
	else if(setCondLowY){
		text = "P("+((yLowCut==Number.NEGATIVE_INFINITY)? "-∞":yLowCut)+" < Y < "+((yCut==Number.POSITIVE_INFINITY)? "∞":yCut)+" | X = "+((xLowCut==Number.NEGATIVE_INFINITY)? "-∞":xLowCut)+") = "+pInBetUni.value+"\n";
		textArea.value += text;
	}
	else if(setCondX){
		text = "P("+((xLowCut==Number.NEGATIVE_INFINITY)? "-∞":xLowCut)+" < X < "+((xCut==Number.POSITIVE_INFINITY)? "∞":xCut)+" | Y = "+((yCut==Number.POSITIVE_INFINITY)? "∞":yCut)+") = "+pInBetUni.value+"\n";
		textArea.value += text; 
	}
	else if(setCondY){
		text = "P("+((yLowCut==Number.NEGATIVE_INFINITY)? "-∞":yLowCut)+" < Y < "+((yCut==Number.POSITIVE_INFINITY)? "∞":yCut)+" | X = "+((xCut==Number.POSITIVE_INFINITY)? "∞":xCut)+") = "+pInBetUni.value+"\n";
		textArea.value += text; 
	}
	else{
		text = "P("+((xLowCut==Number.NEGATIVE_INFINITY)? "-∞":xLowCut)+" < X < "+((xCut==Number.POSITIVE_INFINITY)? "∞":xCut)+") = "+pInBetUni.value+"\n";
		textArea.value += text; 
	}
	
	}
}

function setValue(){
	if (x1 != xParamLeft.getValue()) {
		x1 = xParamLeft.getValue();
		p1 = dist.CDF(x1);
		if (p2<p1) {  // increase p2 and x2
			p2 = p1;
			x2=dist.quantile(p2);
			xParamRight.setValue(x2);
		}
		pParamLeft.setValue(p1);
		pParamBetween.setValue(p2-p1);
		pParamRight.setValue(1-p2);
		
		// Need to set both x1 and x2 limits on distGraph = QuantileGraph2
		distGraph.setValue(x2);
	}
	if (x2 != xParamRight.getValue()) {
		x2 = xParamRight.getValue();
		p2 = dist.CDF(x2);
		if (p2<p1) { // reduce p1 and x1
			p1=p2; 
			x1=dist.quantile(p1);
			xParamLeft.setValue(x1);
		}
		pParamLeft.setValue(p1);
		pParamBetween.setValue(p2-p1);
		pParamRight.setValue(1-p2);
		
		// Need to set both x1 and x2 limits on distGraph = QuantileGraph2
		distGraph.setValue(x2);
	}
}

function setProb(){
	if (p1 != pParamLeft.getValue()) {  // p2 = fixed
		p1 = pParamLeft.getValue();
		if (p1+p2>=0.9999) {  //reduce p1
			p1 = 0.9999-p2;
			pParamLeft.setValue(p1);
		}
		x1 = dist.quantile(p1);
		x2 = dist.quantile(p1+p2);
		pParamRight.setValue(1-(p1+p2));
		xParamLeft.setValue(x1);
		xParamRight.setValue(x2);
		// Need to set p1, p2 and (1-(p2+p1)) probabilities on distGraph = QuantileGraph2
		distGraph.setValue(x2);
	}
	if (p2 != pParamBetween.getValue()) {  // p1 = fixed
		p2 = pParamBetween.getValue();
		if (p1+p2>=0.9999) {  //reduce p2
			p2 = 0.9999-p1;
			pParamBetween.setValue(p2);
		}
		x1 = dist.quantile(p1);
		x2 = dist.quantile(p1+p2);
		pParamRight.setValue(1-(p1+p2));
		xParamLeft.setValue(x1);
		xParamRight.setValue(x2);
		// Need to set p1, p2 and (1-(p2+p1)) probabilities on distGraph = QuantileGraph2
		distGraph.setValue(x2);
	}
}

function QuantileGraph2(canvas, dist, label){
	var distColor = "blue", dataColor = "rgba(255, 0, 0, 0.7)";
	var leftMargin = 30, rightMargin = 20, topMargin = 20, bottomMargin = 30;
	var ctx = canvas.getContext("2d");
	var width = canvas.width, height = canvas.height;
	var minValue = dist.minValue, maxValue = dist.maxValue, step = dist.step;
	var xMin = minValue - step / 2, xMax = maxValue + step / 2;
	var value = xMin, prob = 0;
	var xFormat = Math.max(Math.round(Math.log(1 / step) / Math.log(10)), 0);
	var yFormat = 3;
	var yMax = dist.maxDensity();
		
	//This function returns the horizontal coordinate in canvas units for a given x in scaled units
	function xCanvas(x){
		return leftMargin + Math.round(((x - xMin)/(xMax - xMin)) * (width - leftMargin - rightMargin));
	}
	
	//This function returns the vertical coordinate in canvas units for a given y in scaled units
	function yCanvas(y){
		return height - bottomMargin - Math.round((y / yMax) * (height - bottomMargin - topMargin));
	}
	
	this.draw = function(){
		var xc, yc, x, y, xc1, x_new, y_new, yc1, w, h, n;
		ctx.clearRect(0, 0, width, height);
		//Draw axes
		ctx.strokeStyle = "gray";
		ctx.fillStyle = "gray";
		//Horizontal axis
		ctx.beginPath();
		ctx.moveTo(xCanvas(xMin), yCanvas(0));
		ctx.lineTo(xCanvas(xMax), yCanvas(0));
		for (x = minValue; x < maxValue + step / 2; x = x + step){
			xc = xCanvas(x), yc = yCanvas(0);
			ctx.moveTo(xc, yc - 5);
			ctx.lineTo(xc, yc + 5);
		}
		ctx.stroke();
		ctx.fillText(minValue.toFixed(xFormat), xCanvas(minValue) - 3 * (xFormat + 1), yc + 15);
		ctx.fillText(maxValue.toFixed(xFormat), xCanvas(maxValue) - 3 * (xFormat + 1), yc + 15);
		//Vertical axis
		ctx.beginPath();
		xc = xCanvas(xMin);
		yc = yCanvas(yMax);
		ctx.moveTo(xc, yCanvas(0));
		ctx.lineTo(xc, yc);
		ctx.moveTo(xc - 5, yc);
		ctx.lineTo(xc + 5, yc);
		ctx.stroke();
		ctx.fillText(0, xc - 10, yCanvas(0) + 5);
		ctx.fillText(yMax.toFixed(yFormat), xc - 5 * (yFormat + 3), yc + 5);
		
		//Draw distribution graph
		ctx.strokeStyle = distColor;
		ctx.fillStyle = dataColor;
		w = xCanvas(xMin + step) - xCanvas(xMin);
		if (dist.type == 0){
			for (x = minValue; x < maxValue + step / 2; x = x + step){
				if (graphType == "cdf") y = dist.CDF(x); else y = dist.density(x);
				xc = xCanvas(x - step / 2);
				yc = yCanvas(y); 
				h = yCanvas(0) - yc;
				ctx.strokeRect(xc, yc, w, h);
			}
		}
		else{
			ctx.beginPath();
			x = minValue;
			if (graphType == "cdf") {
				//y = dist.CDF(x); else y = dist.density(x);
				y = dist.CDF(x); 
			} else y = dist.density(x);
			ctx.moveTo(xCanvas(x), yCanvas(y));
			for (x = minValue; x < maxValue; x = x + step){
				x_new = x + step;
				if (graphType == "cdf") y_new = dist.CDF(x_new); else y_new = dist.density(x_new);
				ctx.lineTo(xCanvas(x_new), yCanvas(y_new));
			}
			ctx.stroke();
			ctx.beginPath();
			if (graphType == "cdf"){
				ctx.strokeStyle = dataColor;
				ctx.moveTo(xCanvas(minValue), yCanvas(dist.CDF(value)));
				ctx.lineTo(xCanvas(value), yCanvas(dist.CDF(value)));
				ctx.lineTo(xCanvas(value), yCanvas(0));
				ctx.stroke();
			}
			else{	// PDF Area!
				x = x1;
				ctx.moveTo(xCanvas(x), yCanvas(0));
				y = dist.density(x);
				ctx.lineTo(xCanvas(x), yCanvas(y));
				for (x = x1; x < value; x = x + step){
					x_new = x + step;
					y_new = dist.density(x_new);
					ctx.lineTo(xCanvas(x_new), yCanvas(y_new));
				}
				ctx.lineTo(xCanvas(x), yCanvas(0))
				ctx.fill();
			}
		}
	}

	this.setValue = function(x){
		value = x;
		prob = dist.CDF(x);
		this.draw();
	}

	this.setProb = function(p){
		prob = p;
		value = dist.quantile(p);
		this.draw();
	}

	this.setXFormat = function(n){
		xFormat = n;
		this.draw();
	}

	this.setYFormat = function(n){
		yFormat = n;
		this.draw();
	}

	this.setColors = function(c1, c2){
		distColor = c1;
		dataColor = c2;
		this.draw();
	}

	this.setMargins = function(l, r, t, b){
		leftMargin = l;
		rightMargin = r;
		topMargin = t;
		bottomMargin = b;
		this.draw();
	}

	this.setGraphType = function(t){
		graphType = t;
		if (graphType == "cdf") yMax = 1
		else yMax = dist.maxDensity();
		this.draw();
	}
}
