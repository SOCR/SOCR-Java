$(document).ready(function(){
                                                            // Variable declaration
    var x = [];// Stores distribution X
    var y = [];// Stores distribution Y
    var toDisp = [];// Stores what radio is selected
    var z = [];// Stores matrix for f(x,y)
    var zCDF = [];// Stores matrix for CDF of f(x,y)
    var mux = 0;// mean of X
    var muy = 0;// mean of Y
    var sigmax = 0;// std dev of X
    var sigmay = 0;// std dev of Y
    var xmin = 0;// input value of x min
    var xmax = 0;// input value of x max
    var ymin = 0;// input value of y min
    var ymax = 0;// input value of y max
    var rho = 0;// value of rho
    var t1 = [];// time array for x
    var t2 = [];// time array for y
    var maxx = 0; // max value of X
    var maxy = 0; // max value of Y
    var c = [];// conditional distribution stored here
    var maxc = 0;// max of conditional prob
    var old = [];// stores old values to check if x,y,z need updating
    var des = 0; //0 = marginal x, 1 = marginal y, 2 = x|y at ymin, 3 = x|y at ymax, 4 = y|x at xmin, 5 = y|x at xmax, 6 = cdf x, 7 = cdf y
    var numPoints = 500;// number of points per distribution, min = 500
    var sigmaStep = 5;// bounds how many sigmas away are the distributions calculated
    var sigmaDisp = 3// bounds how many sigmas away are displayed
    var out = 0;// value to be displayed in the P box
    var des3d = 0;// 0 = bivariate PDF, 1 = bivariate CDF
    var maxZ = 0;
    window.glAllow = isWebGL();
    var $graph = $('#graph');// Canvas for 2d graph
    var surfacePlot = new SurfacePlot(document.getElementById("surfacePlot"));
                                                            //Functions
    var check = function(){// Checks if inputs are correct
        if(rho >= 1 || rho < 0){alert("Incorrect value for rho");return 1;}
        if(sigmay <=0 || sigmax <= 0){alert("Incorrect value for sigma");return 1;}
        if(xmin >= xmax){alert("Xmin can't be greater than Xmax");return 1;}
        if(ymin >= ymax){alert("Ymin can't be greater than Ymax");return 1;}
        return 0;
    }
    var checkUpdate = function(){// checks if any distribution parameters have been updated
        if(old[0] != mux){return 1;}
        if(old[1] != muy){return 1;}
        if(old[2] != sigmax){return 1;}
        if(old[3] != sigmay){return 1;}
        if(old[4] != rho){return 1;}
        return 0;
    }
    var makeTea = function(mu,sigma){// Makes array to use in creating the distribution
        var t = [];
        var lb = mu-sigmaStep*sigma;
        var ub = mu+sigmaStep*sigma;
        var delta = Math.abs((ub-lb)/numPoints);
        for (var i = lb; i < ub; i += delta){
            t.push(i).toFixed(6);
        }
        return t;
    }
    var pNorm = function(mu, sigma, val){// Returns p(X = val)
        var temp = (1/(sigma*Math.sqrt(2*Math.PI)))*Math.exp(-0.5*Math.pow((val-mu)/sigma,2));
        return temp;
    }
    var makeCDFZ = function(){// Makes bivariate normal distribution CDF
        var temp1 = [];
        for (var i = 0; i <= numPoints; i++){
            temp1 = [];
            for(var j = 0; j <= numPoints; j++){
                temp1.push(BVN(t1[i], t2[j]));
            }
            zCDF.push(temp1);
        }
    }
    var makeZ = function(){//Makes Z function from CDF of Z
        //binorm pdf and define it explicitly
        var step1 = Math.abs(t1[1] - t1[0]);
        var step2 = Math.abs(t2[1] - t2[0]);
        var temp = [];
        var tt2 = step1*step2;
        makeCDFZ();
        for (var i = 0; i < numPoints; i++){
            temp = [];
            for (var j = 0; j < numPoints; j++){
                if(i == 0 || j == 0){
                    temp.push(0);
                }
                else{
                    var tt1 = Math.abs((zCDF[i][j] + zCDF[i-1][j-1] - zCDF[i][j-1] - zCDF[i-1][j]));//use actual function
                    temp.push(tt1/tt2);
                }
            }
            z.push(temp);
        }
        maxZ = findMaxZ();
    }
    var CDF = function(dist, mu, sigma, val){// Calculates value of p(x <= val) for distribution
        var lb = mu-sigmaStep*sigma;
        var ub = mu+sigmaStep*sigma;
        if(val <= lb){return 0;}
        else if(val >= ub){return 1;}
        var delta = Math.abs((ub-lb)/numPoints);
        var numsteps = Math.floor((val-lb)/delta);
        var temp = 0;
        for (var i = 0; i < numsteps; i++){
            temp += dist[i]*delta;
        }
        return temp;
    }
    var makeCDF = function(mu, sigma){// makes CDF to display for x or y
        var lb = mu-sigmaDisp*sigma;
        var ub = mu+sigmaDisp*sigma;
        var delta = Math.abs((ub-lb)/500);
        var temp = [];
        var temp1 = [];
        if(mu == mux){temp1 = x}
        else{temp1 = y};
        for (i = 0; i < 500; i++){
            temp[i] = CDF(temp1, mu, sigma, (lb+i*delta))*350;
        }
        return temp;
    }
    var normalCDF = function(X){//returns CDF value at X
        // using Hastings algorithm with maximal error=10^{-6}
        var T=1/(1+.2316419*Math.abs(X));
        var D=0.3989423*Math.exp(-X*X/2);
        var Prob=D*T*(.3193815+T*(-0.3565638+T*(1.781478+T*(-1.821256+T*1.330274))));
        if (X>0) {Prob=1-Prob;}
        return Prob;
    }
    var binormalCDF = function(x,y,Rh){//Returns bivariate cdf value of adjusted to (0,1) Norm Dist X and Y
        with (Math){
            var s=(1-normalCDF(x))*(1-normalCDF(y));
            var sqr2pi=sqrt(2*PI);
            var h0=exp(-x*x/2)/sqr2pi;
            var k0=exp(-y*y/2)/sqr2pi;
            var h1=-x*h0;
            var k1=-y*k0;
            var factor=Rh*Rh/2;
            s=s+Rh*h0*k0+factor*h1*k1;
            var n=2;
            while ((n*(1-abs(Rh))<5)&&(n<101)) {
                factor=factor*Rh/(n+1);
                h2=-x*h1-(n-1)*h0;
                k2=-y*k1-(n-1)*k0;
                s=s+factor*h2*k2;
                h0=h1;
                k0=k1;
                h1=h2;
                k1=k2;
                n=n+1;
            }
            var v=0;
            if (Rh>.95) {
                v=1-normalCDF(max(h,k))
                s=v+20*(s-v)*(1-Rh);
            } else if ((Rh<-.95)&&(h+k<0)) {
                v=abs(normalCDF(h)-normalCDF(k))
                s=v+20*(s-v)*(1+Rh);
            }
        }
        return s;
    }
    var BVN = function(X,Y) {// Returns bivariate cdf value for a given X and Y
        var Prob=0;
        h=-(X-mux)/sigmax;
        k=-(Y-muy)/sigmay;   
        Prob=binormalCDF(h,k,rho);
        Prob=Math.round(100000*Prob)/100000;
        return(Prob);
    }
    var makeDisp = function(raw,max){// Samples 500 points to display from distribution and scales them to fit the graph
        var temp = [];
        var step = Math.floor(numPoints/500);
        for (i = 0; i < numPoints; i+=step){
            temp.push(raw[i]*350/max);
        }
        return temp;
    }
    var getDist = function(mu, sigma, val, dir){//requires mu/sigma of x or y, val = value of y or x at which to take distribution from, dir = 0 for x, 1 for y
        //Gets the row/col at a particular point from f(x,y)
        var lb = mu-sigmaStep*sigma;
        var ub = mu+sigmaStep*sigma;
        var delta = Math.abs((ub-lb)/numPoints);
        if(val <= lb && dir == 0){return z[0];}
        if(val <= lb){return getCol(0);}
        if(val >= ub && dir == 0){return z[numPoints-1];}
        if(val >= ub){return getCol(numPoints-1);}
        var loc = Math.floor((val-lb)/delta);
        if(dir == 0){return z[loc];}
        return getCol(loc);
    }
    var getCol = function(col){//Returns a column from f(x,y)
        var temp = [];
        for (var i = 0; i < numPoints; i++){
            temp.push(z[i][col]);
        }
        return temp;
    }
    var findMaxZ = function(){//Finds max of Z
        var tempy = 0;
        for(var i = 0; i < numPoints; i++){
            for (var j = 0; j < numPoints; j++){
                if(tempy < z[i][j]){
                    tempy = z[i][j];
                }
            }
        }
        return tempy;
    }
    var updateGraph = function(opt){// Core function, coordinates other functions based on the radio selected
        $graph.drawRect({fillStyle: 'white',strokeStyle: 'white',strokeWidth: 4,x: 150, y: 100,fromCenter: true,width: 1000,height: 1000});//clears canvas
        switch (opt){
            case 0:// Marginal of X
                drawAxis(mux-sigmaDisp*sigmax, mux+sigmaDisp*sigmax, maxx);
                toDisp = makeDisp(x,maxx);
                drawData(toDisp);
                out = CDF(x, mux, sigmax, xmax) - CDF(x, mux, sigmax, xmin);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + xmin + ' < X < ' + xmax + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(mux, sigmax, xmin, xmax);
                break;
            case 1:// Marginal of Y
                drawAxis(muy-sigmaDisp*sigmay, muy+sigmaDisp*sigmay, maxy);
                toDisp = makeDisp(y,maxy);
                drawData(toDisp);
                out = CDF(y, muy, sigmay, ymax) - CDF(y, muy, sigmay, ymin);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + ymin + ' < Y < ' + ymax + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(muy, sigmay, ymin, ymax);
                break;
            case 2:// p(X|Y=ymin)
                c = getDist(mux, sigmax, ymin, 0);
                maxc = Math.max(...c);
                drawAxis(mux-sigmaDisp*sigmax, mux+sigmaDisp*sigmax, maxc);
                toDisp = makeDisp(c,maxc);
                drawData(toDisp);
                out = (CDF(c, mux, sigmax, xmax) - CDF(c, mux, sigmax, xmin))/(pNorm(muy, sigmay, ymin)*2*sigmaStep*sigmax/numPoints);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + xmin + ' < X < ' + xmax + ' | Y = ' + ymin + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(mux, sigmax, xmin, xmax);
                break;
            case 3:// p(X|Y=max)
                c = getDist(mux, sigmax, ymax, 0);
                maxc = Math.max(...c);
                drawAxis(mux-sigmaDisp*sigmax, mux+sigmaDisp*sigmax, maxc);
                toDisp = makeDisp(c,maxc);
                drawData(toDisp);
                out = (CDF(c, mux, sigmax, xmax) - CDF(c, mux, sigmax, xmin))/(pNorm(muy, sigmay, ymax)*2*sigmaStep*sigmax*2*sigmaStep);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + xmin + ' < X < ' + xmax + ' | Y = ' + ymax + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(mux, sigmax, xmin, xmax);
                break;
            case 4:// p(Y|X=min)
                c = getDist(muy, sigmay, xmin, 1);
                maxc = Math.max(...c);
                drawAxis(muy-sigmaDisp*sigmay, muy+sigmaDisp*sigmay, maxc);
                toDisp = makeDisp(c,maxc);
                drawData(toDisp);
                out = (CDF(c, muy, sigmay, ymax) - CDF(c, muy, sigmay, ymin))/(pNorm(mux, sigmax, xmin)*2*sigmaStep*sigmax*2*sigmaStep);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + ymin + ' < Y < ' + ymax + ' | X = ' + xmin + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(muy, sigmay, ymin, ymax);
                break;
            case 5:// p(Y|X=max)
                c = getDist(muy, sigmay, xmax, 1);
                maxc = Math.max(...c);
                drawAxis(muy-sigmaDisp*sigmay, muy+sigmaDisp*sigmay, maxc);
                toDisp = makeDisp(c,maxc);
                drawData(toDisp);
                out = (CDF(c, muy, sigmay, ymax) - CDF(c, muy, sigmay, ymin))/(pNorm(mux, sigmax, xmax)*2*sigmaStep*sigmax*2*sigmaStep);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + ymin + ' < Y < ' + ymax + ' | X = ' + xmax + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(muy, sigmay, ymin, ymax);
                break;
            case 6:// CDF of X
                drawAxis(mux-sigmaDisp*sigmax, mux+sigmaDisp*sigmax, 1);
                toDisp = makeCDF(mux, sigmax);
                drawData(toDisp);
                out = CDF(x, mux, sigmax, xmax) - CDF(x, mux, sigmax, xmin);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + xmin + ' < X < ' + xmax + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(mux, sigmax, xmin, xmax);
                break;
            case 7:// CDF of Y
                drawAxis(muy-sigmaDisp*sigmay, muy+sigmaDisp*sigmay, 1);
                toDisp = makeCDF(muy, sigmay);
                drawData(toDisp);
                out = CDF(y, muy, sigmay, ymax) - CDF(y, muy, sigmay, ymin);
                $('#pout').replaceWith('<td rowspan = "3" id = "pout">P(' + ymin + ' < Y < ' + ymax + ') = ' + out.toFixed(3) + '</td>');
                shadeIn(muy, sigmay, ymin, ymax);
                break;
        }
    }
    var drawAxis = function(min, max, tall){// Draws axis on canvas
        $graph.drawPath({
            strokeStyle: '#000',
            strokeWidth: 1,
            x: 50, y: 370,
            p1: {
                type: 'line',
                rounded: true,
                endArrow: false,
                x1: -5, y1: 0,
                x2: 510, y2: 0
            },
            p2: {
                type: 'line',
                rounded: true,
                endArrow: false,
                x1: 0, y1: 5,
                x2: 0, y2: -360
            },
            p3: {
                type: 'line',
                rounded: true,
                endArrow: false,
                x1: 500, y1: 5,
                x2: 500, y2: -5
            },
            p4: {
                type: 'line',
                rounded: true,
                endArrow: false,
                x1: -5, y1: -350,
                x2: 5, y2: -350
            }
        }).drawText({
            text:min.toString(),
            fontFamily:'serif',
            fontSize: 20,
            x: 50, y:390,
            fillStyle: '#000',
            strokeStyle: '#000',
            strokeWidth: 0
        }).drawText({
            text:'0',
            fontFamily:'serif',
            fontSize: 20,
            x: 25, y:370,
            fillStyle: '#000',
            strokeStyle: '#000',
            strokeWidth: 0
        }).drawText({
            text:max.toString(),
            fontFamily:'serif',
            fontSize: 20,
            x: 550, y:390,
            fillStyle: '#000',
            strokeStyle: '#000',
            strokeWidth: 0
        }).drawText({
            text:tall.toFixed(3).toString(),
            fontFamily:'serif',
            fontSize: 20,
            x: 25, y:20,
            fillStyle: '#000',
            strokeStyle: '#000',
            strokeWidth: 0
        })
        for (var i = 10; i < 510; i+=10){
            $graph.drawPath({
                strokeStyle: '#ccc',
                strokeWidth: 1,
                x: 50, y: 370,
                p1: {
                    type: 'line',
                    rounded: true,
                    endArrow: false,
                    x1: i, y1: 5,
                    x2: i, y2: -360
                }
            })
        }
        for (var i = 10; i < 360; i+=10){
            $graph.drawPath({
                strokeStyle: '#ccc',
                strokeWidth: 1,
                x: 50, y: 370,
                p1: {
                    type: 'line',
                    rounded: true,
                    endArrow: false,
                    x1: -5, y1: -i,
                    x2: 510, y2: -i
                }
            })
        }
    }
    var drawData = function(data){// Draws the data on canvas
        for (var i = 0; i < 499; i++){
            $graph.drawPath({
                strokeStyle: '#00F',
                strokeWidth: 1,
                x: 50, y: 370,
                p1:{
                    type: 'line',
                    rounded: false,
                    endArrow: false,
                    x1:i,y1:-data[i],
                    x2:i+1,y2:-data[i+1]
                }
            })
        }
    }
    var shadeIn = function(mu, sigma, min, max){//Shades in a specified region of graph
        var lb = mu-sigmaStep*sigma;
        var ub = mu+sigmaStep*sigma;
        var delta = Math.abs((ub-lb)/500);
        if(min < lb){min = lb};
        if(max > ub){max = ub};
        var start = Math.floor((min-lb)/delta);
        var end = Math.floor((max-lb)/delta);
        for (var i = start; i <= end; i++){
            $graph.drawPath({
                strokeStyle: '#F00',
                strokeWidth: 1,
                x: 50, y: 370,
                p1:{
                    type: 'line',
                    rounded: false,
                    endArrow: false,
                    x1:i,y1:0,
                    x2:i,y2:-toDisp[i]
                }
            })
        }
    }
                                                            //Buttons
    $('#submit').click(function(){//Does all the work updating everything
        old = [mux, muy, sigmax, sigmay, rho];
        mux = Number($('#mux').val());
        muy = Number($('#muy').val());
        sigmax = Number($('#six').val());
        sigmay = Number($('#siy').val());
        rho = Number($('#rho').val());
        xmin = Number($('#xmin').val());
        xmax = Number($('#xmax').val());
        ymin = Number($('#ymin').val());
        ymax = Number($('#ymax').val());
        des = Number($("input[name='1']:checked").val());
        des3d = Number($("input[name='2']:checked").val());
        var alert = check();
        if(alert == 1){return;}
        var updating = checkUpdate();
        if(updating == 1){
            t1 = makeTea(mux,sigmax);
            t2 = makeTea(muy,sigmay);
            makeZ();
            x = getDist(mux,sigmax,mux,0);
            y = getDist(muy,sigmay,muy,1);
            maxx = Math.max(...x);
            maxy = Math.max(...y);
        }
        updateGraph(des);
        $('#surfacePlot').replaceWith('<div id = "surfacePlot"></div>');
        surfacePlot = new SurfacePlot(document.getElementById("surfacePlot"));
        setUp();
    })
    $("#allowWebGL").contents().find(":checkbox").bind('change', function(){
        toggleChart(this);
    });
                                                    //3D model
    function setUp(){//Sets up and draws the 3D graph
        window.rowS = 0;
        window.mu1 = mux;
        window.mu2 = muy;
        window.sigma1 = sigmax;
        window.sigma2 = sigmay;
        var numRows = 50; //smoothness of curve
        var numCols = 50; //smoothness of curve
        var tooltipStrings = [];
        var values = [];
        var data = {nRows: numRows, nCols: numCols, formattedValues: values};
        var xstep = 0.6*numPoints/numRows;
        var ystep = 0.6*numPoints/numCols;
        var xstart = numPoints*0.2;
        var ystart = numPoints*0.2;
        for (var i = xstart; i < numPoints-xstart; i+=xstep){
            var tempt = [];
            for (var j = ystart; j < numPoints - ystart; j+=ystep){
                if(des3d == 0){tempt.push(z[i][j]);}
                else if(des3d == 1){
                    if(t1[i] >= xmin && t1[i] <= xmax && t2[j] >= ymin && t2[j] <= ymax){tempt.push(z[i][j]);}
                    else{tempt.push(0);}
                }
                else{tempt.push(zCDF[i][j]);}
            }
            values.push(tempt);
        }
        //Touch Event Listeners for document
        document.addEventListener("touchstart", touchHandler, true);
        document.addEventListener("touchmove", touchHandler, true);
        document.addEventListener("touchend", touchHandler, true);
        document.addEventListener("touchcancel", touchHandler, true); 
        // Don't fill polygons in IE. It's too slow.
        var fillPly =false;// !isIE();
        // Define a colour gradient.
        var colour1 = {red:255, green:0, blue:0};
        var colour2 = {red:255, green:0, blue:0};
        var colour3 = {red:255, green:0, blue:0};
        var colour4 = {red:0, green:0, blue:0};
        var colour5 = {red:0, green:0, blue:0};
        var colours = [colour1, colour2, colour3, colour4, colour5];
        // Axis labels.
        var xAxisHeader = "X";
        var yAxisHeader = "Y";
        var zAxisHeader = "f(x, y)";
        var renderDataPoints = false;
        var background = '#ffffff';
        var axisForeColour = '#000000';
        var hideFloorPolygons = true;
        var chartOrigin = {x: 250, y:300};//origin of entire plot
        // Options for the basic canvas pliot.
        var basicPlotOptions = {fillPolygons: fillPly, tooltips: tooltipStrings, renderPoints: renderDataPoints }
        // use loop to get min of x vals and min of y vals, and change scale based on that
        // labels for the plot
        // Options for the webGL plot.
        //look up web gl option and see if any of those would help
        var xLab = [mux-3*sigmax, mux-2*sigmax, mux-sigmax, mux, mux+sigmax, mux+2*sigmax, mux+3*sigmax];
        var yLab = [muy-3*sigmay, muy-2*sigmay, muy-sigmay, muy, mux+sigmay, muy+2*sigmay, muy+3*sigmay];
        var zLabels = [0.2, 0.4, 0.6, 0.8, 1];
        if(des3d == 0 || des3d == 1){zLabels = [0, maxZ*0.2, maxZ*0.4, maxZ*0.6, maxZ*0.8, maxZ];}
        var glOptions = {xLabels: xLab, yLabels: yLab, zLabels: zLabels, chkControlId: "allowWebGL" , autoCalcZScale: true};
        // Options common to both types of plot.
        var options = {xPos: 0, yPos: 0, width: 500, height: 450, colourGradient: colours,
        xTitle: xAxisHeader, yTitle: yAxisHeader, zTitle: zAxisHeader,
        backColour: background, axisTextColour: axisForeColour, hideFlatMinPolygons: hideFloorPolygons, origin: chartOrigin};
        surfacePlot.draw(data, options, basicPlotOptions, glOptions);
    }
    function isWebGL() {//Identifies if WebGL is used
        try {
            return !!window.WebGLRenderingContext
                    && !!document.createElement('canvas').getContext(
                            'experimental-webgl');
        } catch (e) {
            return false;
        }
    }
    function toggleChart(chkbox)//Toggles WebGL
        {
            if(isWebGL()){
                glAllow = chkbox.checked;
            }
            //console.log(glAllow);
            $('canvas').remove('.surfacePlotCanvas')
            setUp();
            surfacePlot.redraw();
        }
                                                    //Testing
    $('#test').click(function(){
        c = getDist(mux, sigmax, ymin, 0);
        var temp2 = CDF(c, mux, sigmax, xmax);
        var temp3 = CDF(c, mux, sigmax, xmin);
        var temp4 = pNorm(muy, sigmay, ymin);
        var temp1 = (CDF(c, mux, sigmax, xmax) - CDF(c, mux, sigmax, xmin))/(pNorm(muy, sigmay, ymin)*2*sigmaStep*sigmax/numPoints);
        var temp5 = sum();
        var temp6 = maxZ();
        alert(temp1.toFixed(3) + ' ' + temp2 + ' ' + temp3 + ' ' + temp4 + ' ' + temp5 + ' ' + temp6);
    })
    var sum = function(){
        var temptemp = 0;
        for(var i = 0; i < numPoints; i++){for (var j = 0; j < numPoints; j++){temptemp += z[i][j];}}
        return temptemp;
    }
    var findMaxZ = function(){//Finds max of Z
        var tempy = 0;
        for(var i = 0; i < numPoints; i++){
            for (var j = 0; j < numPoints; j++){
                if(tempy < z[i][j]){
                    tempy = z[i][j];
                }
            }
        }
        return tempy;
    }
})