$(document).ready(function(){
                                                            //Canvas Resizing - must be first!
    var pagewidth = $(window).width();
    var oldwidth = 0;
    if(pagewidth >= 1600){
        $('#screensize').hide();
        $('#ignorebutt').hide();
        $('#backtosmall').hide();
    }
    else if(pagewidth < 1600 && pagewidth >= 1300){
        $('#game').replaceWith('<canvas id = "game" width="1220" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="1220" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="1220" height="220"></canvas>');
        $('#numsines').replaceWith('<u id = "numsines">40</u>');
        $('#backtosmall').hide();
        oldwidth = 1220;
    }
    else if(pagewidth < 1300 && pagewidth >= 1000){
        $('#game').replaceWith('<canvas id = "game" width="920" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="920" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="920" height="220"></canvas>');
        $('#numsines').replaceWith('<u id = "numsines">30</u>');
        $('#backtosmall').hide();
        oldwidth = 920;
    }
    else if(pagewidth < 1000){
        $('#game').replaceWith('<canvas id = "game" width="620" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="620" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="620" height="220"></canvas>');
        $('#numsines').replaceWith('<u id = "numsines">20</u>');
        $('#backtosmall').hide();
        oldwidth = 620;
    }
    $('#done').hide();
                                                            //Notes and Comments:
    /*
    - Some terminology used here:
        Array = 1 dimensional vector
        Matrix = an array of arrays, is 2D
        Tensor = an array of arrays of arrays, is 3D
    - Plotly cannot draw the same graph in two different locations, so two distinct graphs are drawn - 1 is the enlarged version, one is the regular version
    - Canvas currently does not have a well-defined resize functionality. The graph is currently fixed to be 1520 by 220 pixels in the HTML file, but changing that number manually should not impact any of the mathematics involved
        - Allow for 10px on either side / top / bottom for ease of drawing
    - 
    */
                                                            // Variable declaration
    var starting = 0;// used for initial settings to be displayed
    var rulesOpen = false;// Logs of the rules window is open
    var downloadsOpen = false;// Logs if the downloads window is open
    var historyOpen = false;// Logs if the version history is open
    var $gameCanvas = $('#game');// Fourier game canvas
    var $magsCanvas = $('#magnitudes');// Magnitude adjustment canvas
    var $phaseCanvas = $('#offsets');// Phase offsets adjustment canvas
    var xaxisrange = $("#game").width()-20;// Counts the number of pixels available horizontally for drawing on the canvases, excluding the 10px margins on all edges
    var yaxisrange = $("#game").height()-20;// Counts the number of pixels available vertically for drawing on the canvases, excluding the 10px margins on all edges
    var xstart = 10;// Unless the margin changes should always be at 10
    var ystart = yaxisrange/2+10;// Puts the y start in the middle of the canvas, adjusted for margins
    var sineMat = [];// Stores each sine curve on a separate row
    var numSines = xaxisrange/30;// Number of sine curves used to generate the signal
    var sineSum = [];// Stores the resulting sine curve
    var truewave = [];// Stores the original curve
    var mags = [];// Stores the magnitudes array
    var phase = [];// Stores the phases array
    var xmin = 0;// Starting value for x axis
    var xmax = Math.PI*6;// Ending value for x axis, currently shows 3 full periods
    var showid = 0;// Stores what graph type is currently showing - 0 Sine, 1 Square, 2 Triangle, 3 Sawtooth, 4 Noise, 5 Custom drawing, 6 Custom slider input
    var gametitle = ["Sine Wave", "Square Wave", "Triange Wave", "Sawtooth Wave", "Random Noise", "Custom Drawing", "Custom Slider Input"];//Stores graph type titles
                                                            //Supportive Math Functions
    var generateSin = function(period, magnitude, phase){//Generates a sine curve based on inputs
        var out = [];
        var inc = (xmax-xmin)/xaxisrange;
        for (var i = 0; i < xaxisrange; i++){
            out.push(magnitude*Math.sin(inc*i*period + phase*Math.PI*2));
        }
        return out;
    }
    var sumAll = function(matrix){//Sums all sine curves to create the transform
        var len = matrix.length;
        var wid = matrix[0].length;
        var out = [];
        for (var j = 0; j < wid; j++){
            var temp = 0;
            for (var i = 0; i < len; i++){
                temp+=matrix[i][j];
            }
            out.push(temp);
        }
        return normalize(out);
    }
    var normalize = function(arr){//Normalizes an array such that its absolute max (i.e. max of |array|) is 1
        var arrmax = Math.abs(Math.max(...arr));
        var arrmin = Math.abs(Math.min(...arr));
        var absmax = 0;
        if(arrmax >= arrmin){absmax = arrmax;}
        else{absmax = arrmin}
        for(var i = 0; i < arr.length; i++){
            arr[i] = arr[i]/absmax;
        }
        return arr;
    }
                                                            //Supportive jCanvas Functions
    var drawCurve = function(array, color){//Draws the required array using the provided color string - must be of format '#000'
        var xdist = Math.floor(xaxisrange/array.length);
        for(var i = 1; i < array.length; i++){
            $gameCanvas.drawPath({
                strokeStyle: color,
                strokeWidth: 1,
                p1: {
                    type: 'line',
                    x1: xstart+(i-1)*xdist, y1: ystart-0.5*yaxisrange*array[i-1],
                    x2: xstart+i*xdist, y2: ystart-0.5*yaxisrange*array[i]
                }
            });
        }
    }
    var drawAxisGame = function(){
        $('#graphtitle').replaceWith('<b id = "graphtitle">' + gametitle[showid] + '</b>');
        $gameCanvas.drawPath({
            strokeStyle: '#999',
            strokeWidth: 1,
            p1: {
                type: 'vector',
                endArrow: true,
                arrowAngle: 60,
                arrowRadius: 10,
                x: xstart-5, y: ystart,
                a1: 90, l1: xaxisrange+10
            },
            p2: {
                type: 'vector',
                endArrow: true,
                arrowAngle: 60,
                arrowRadius: 10,
                x: xstart, y: yaxisrange+15,
                a1: 0, l1: yaxisrange+10
            },
            p3: {
                type: 'line',
                x1: xstart+xaxisrange/3, y1: yaxisrange+10,
                x2: xstart+xaxisrange/3, y2: 10,
            },
            p4: {
                type: 'line',
                x1: xstart+2*xaxisrange/3, y1: yaxisrange+10,
                x2: xstart+2*xaxisrange/3, y2: 10,
            }
        })
    }
    var drawAxisMags = function(){
        $magsCanvas.drawPath({
            strokeStyle: '#999',
            strokeWidth: 1,
            layer: true,
            groups: ['axis'],
            p1: {
                type: 'vector',
                endArrow: true,
                arrowAngle: 60,
                arrowRadius: 10,
                x: xstart-5, y: 10+yaxisrange,
                a1: 90, l1: xaxisrange+10
            },
            p2: {
                type: 'vector',
                endArrow: true,
                arrowAngle: 60,
                arrowRadius: 10,
                x: xstart, y: yaxisrange+15,
                a1: 0, l1: yaxisrange+10
            },
            p3: {
                type: 'line',
                x1: xstart, y1: 10,
                x2: xstart+xaxisrange, y2: 10,
            },
        })
    }
    var drawAxisPhase = function(){
        $phaseCanvas.drawPath({
            strokeStyle: '#999',
            strokeWidth: 1,
            layer: true,
            groups: ['axis'],
            p1: {
                type: 'vector',
                endArrow: true,
                arrowAngle: 60,
                arrowRadius: 10,
                x: xstart-5, y: ystart,
                a1: 90, l1: xaxisrange+10
            },
            p2: {
                type: 'vector',
                endArrow: true,
                arrowAngle: 60,
                arrowRadius: 10,
                x: xstart, y: yaxisrange+15,
                a1: 0, l1: yaxisrange+10
            },
            p3: {
                type: 'line',
                x1: xstart, y1: 10,
                x2: xstart+xaxisrange, y2: 10,
            },
            p4: {
                type: 'line',
                x1: xstart, y1: yaxisrange+10,
                x2: xstart+xaxisrange, y2: yaxisrange+10,
            }
        })
    }
    var drawMags = function(){
        var mult = Math.floor(xaxisrange/numSines);
        for(var i = 0; i < numSines; i++){
            $magsCanvas.drawArc({
            layer: true,
            group: ['circles'],
            draggable: true,
            bringToFront: true,
            fillStyle: '#00f',
            x: xstart+(i+1)*mult, y: ystart+yaxisrange/2-mags[i]*yaxisrange,
            radius: 10,
            restrictDragToAxis: 'y',
            dragstop: function(shape) {
                showid = 6;
                redrawFFT(shape.x, shape.y, 0);
            },
            mouseover: function(shape) {
                redrawFFT(shape.x, shape.y, 2);
                shape.fillStyle =  '#f00';
            },
            mouseout: function(shape){
                redrawFFT(shape.x, shape.y, 3);
                shape.fillStyle =  '#00f';
            },
          });
        }
    }
    var drawPhases = function(){
        var mult = Math.floor(xaxisrange/numSines);
        for(var i = 0; i < numSines; i++){
            $phaseCanvas.drawArc({
                layer: true,
                group: ['circles'],
                draggable: true,
                bringToFront: true,
                fillStyle: '#00f',
                x: xstart+(i+1)*mult, y: ystart-phase[i]*yaxisrange/2,
                radius: 10,
                restrictDragToAxis: 'y',
                dragstop: function(shape) {
                    redrawFFT(shape.x, shape.y, 1);
                    showid = 6;
                },
                mouseover: function(shape) {
                    redrawFFT(shape.x, shape.y, 2);
                    shape.fillStyle =  '#f00';
                },
                mouseout: function(shape){
                    redrawFFT(shape.x, shape.y, 3);
                    shape.fillStyle =  '#00f';
                },
            });
        }
    }
    var redrawLines = function(canvas, choice){
        var mult = Math.floor(xaxisrange/numSines);
        canvas.removeLayers('lines').drawLayers();
        for (var i = 0; i < numSines; i++){
            var temp2 = 0;
            var temp3 = 0;
            if(choice == 0){temp2 = ystart+yaxisrange/2-mags[i]*yaxisrange;temp3 = 10+yaxisrange;}
            else{temp2 = ystart-phase[i]*yaxisrange/2;temp3 = ystart;}
            canvas.drawPath({
                strokeStyle: '#444',
                layer: true,
                group: ['lines'],
                strokeWidth: 1,
                p1: {
                    type: 'line',
                    x1: xstart+(i+1)*mult, y1: temp3,
                    x2: xstart+(i+1)*mult, y2: temp2
                }
            });
        }
    }
                                                            //FFT making
    
    var redrawFFT = function(xpos, ypos, choice){//Does everything once the Game is updated
        var loc = ((xpos-xstart)/(Math.floor(xaxisrange/numSines)))-1;
        if(ypos < 10){
            ypos = 10;
        }
        else if(ypos > 10+yaxisrange){
            ypos = 10+yaxisrange;
        }
        if(choice == 0){
            mags[loc] = -(ypos-ystart-yaxisrange/2)/yaxisrange;
        }
        else if(choice == 1){
            phase[loc] = -2*(ypos-ystart)/yaxisrange;
        }
        else if(choice == 2){
            $gameCanvas.clearCanvas();
            drawAxisGame();
            drawCurve(sineSum, '#000');
            drawCurve(sineMat[loc], '#f00');
            return;
        }
        sineMat[loc] = generateSin(loc+1, mags[loc], phase[loc]);
        sineSum = sumAll(sineMat); 
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(sineSum, '#000');
        if(choice == 0){
            redrawLines($magsCanvas, 0);
            drawAxisMags();
            drawMags();
        }
        if(choice == 1){
            redrawLines($phaseCanvas, 1);
            drawAxisPhase();
            drawPhases();
        }
    }
                                                            // On loading
    var initial = function(){// loads initial functions to be displayed, is only called when no custom url is used
        if(starting != 0){return;}
        starting++;
        for(var i = 0; i < numSines; i++){
            /*mags.push(Math.random());
            phase.push(Math.random()*2-1);*/
            mags.push(0);
            phase.push(0);
        }
        mags[0] = 1;
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        drawAxisGame();
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    }
                                                            //Helpful Buttons
    $('#backdim').click(function(){//Clicking outside the settings box closes the corresponding window
        if(rulesOpen){$('#rules').hide(1000);$('#backdim').hide(1000);rulesOpen = false;}
        else if(downloadsOpen){$('#downloads').hide(1000);$('#backdim').hide(1000);downloadsOpen = false;}
        else if(historyOpen){$('#versionHist').hide(1000);$('#backdim').hide(1000);historyOpen = false;}
    })
    $('#showR').click(function(){//Shows the rules
        $('#backdim').show(1000);
        $('#rules').show(1000);
        rulesOpen = true;
    })
    $('#closeR').click(function(){//Closes rules
        $('#rules').hide(1000);
        $('#backdim').hide(1000);
        rulesOpen = false;
    })
    $('#showD').click(function(){//Shows the downloads
        $('#backdim').show(1000);
        $('#downloads').show(1000);
        downloadsOpen = true;
    })
    $('#closeD').click(function(){//Closes downloads
        $('#downloads').hide(1000);
        $('#backdim').hide(1000);
        downloadsOpen = false;
    })
    $('#showV').click(function(){//Shows the history
        $('#versionHist').show(1000);
        $('#backdim').show(1000);
        historyOpen = true;
    })
    $('#closeV').click(function(){//Closes history
        $('#versionHist').hide(1000);
        $('#backdim').hide(1000);
        historyOpen = false;
    })
    $(document).keyup(function(e){// Pressing Esc key closes the corresponding window
        if(e.which == 27 && rulesOpen){
            $('#rules').hide(1000);
            $('#backdim').hide(1000);
            rulesOpen = false;
        }
        else if(e.which == 27 && downloadsOpen){
            $('#downloads').hide(1000);
            $('#backdim').hide(1000);
            downloadsOpen = false;
        }
        else if(e.which == 27 && historyOpen){
            $('#versionHist').hide(1000);
            $('#backdim').hide(1000);
            historyOpen = false;
        }
    });
    $('#ignore').click(function(){//Resizes canvases to original 1520 by 220 size and redraws everything
        $('#game').replaceWith('<canvas id = "game" width="1520" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="1520" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="1520" height="220"></canvas>');
        $('#screensize').hide();
        $('#ignorebutt').hide();
        $('#backtosmall').show();
        $gameCanvas = $('#game');
        $magsCanvas = $('#magnitudes');
        $phaseCanvas = $('#offsets');
        xaxisrange = $("#game").width()-20;
        yaxisrange = $("#game").height()-20;
        var oldnum = numSines;
        numSines = xaxisrange/30;
        sineMat = [];
        for(var i = oldnum; i < numSines; i++){
            mags.push(0);
            phase.push(0);
        }
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        drawAxisGame();
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
    $('#reduce').click(function(){//Resizes canvases back down to how they started at window launch and redraws everything
        $('#game').replaceWith('<canvas id = "game" width="' + oldwidth + '" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="' + oldwidth + '" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="' + oldwidth + '" height="220"></canvas>');
        $('#screensize').hide();
        $('#ignorebutt').hide();
        $('#backtosmall').show();
        $gameCanvas = $('#game');
        $magsCanvas = $('#magnitudes');
        $phaseCanvas = $('#offsets');
        xaxisrange = $("#game").width()-20;
        yaxisrange = $("#game").height()-20;
        var oldnum = numSines;
        numSines = xaxisrange/30;
        sineMat = [];
        for(var i = oldnum; i > numSines; i--){
            mags.splice(-1);
            phase.splice(-1);
        }
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        drawAxisGame();
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
    $('#sine').click(function(){//Sine wave
        showid = 0;
        mags = [];
        phase = [];
        sineMat = [];
        for(var i = 0; i < numSines; i++){
            mags.push(0);
            phase.push(0);
        }
        mags[0] = 1;
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
    $('#square').click(function(){//Square wave
        showid = 1;
        mags = [];
        phase = [];
        sineMat = [];
        for(var i = 0; i < numSines; i++){
            mags.push(0);
            phase.push(0);
        }
        mags[0] = 1;
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
    $('#triangle').click(function(){//Triangle wave
        showid = 2;
        mags = [];
        phase = [];
        sineMat = [];
        for(var i = 0; i < numSines; i++){
            mags.push(0);
            phase.push(0);
        }
        mags[0] = 1;
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
    $('#sawtooth').click(function(){//Sawtooth wave
        showid = 3;
        mags = [];
        phase = [];
        sineMat = [];
        for(var i = 0; i < numSines; i++){
            mags.push(0);
            phase.push(0);
        }
        mags[0] = 1;
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
    $('#noise').click(function(){//Noise wave
        showid = 4;
        mags = [];
        phase = [];
        sineMat = [];
        for(var i = 0; i < numSines; i++){
            mags.push(Math.random());
            phase.push(Math.random()*2-1);
        }
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
                                                            // Download Data and Settings
    $('#downdata').click(function(){//Downloading data as a txt file
        //Graph Settings
        var todown = "Magnitudes:\tPhases:\n";
        for(var i = 0; i < mags.length; i++){
            todown += mags[i] + "\t" + phase[i] + "\n";
        }
        todown += "\nCurve Data:\n";
        for(var i = 0; i < sineSum.length; i++){
            todown += sineSum[i] + "\n";
        }
        //Download code
        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(todown));
        element.setAttribute('download', "data.txt");
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    })
    $('#getset').click(function(){//Generates url string based on graph settings
        var toout = params[0];
        toout += "?";
        $('#setout').replaceWith('<textarea id = "setout" readonly>' + toout + '</textarea>');
    })
    $('#copyset').click(function(){//Copies settings url string to clipboard
        $('#setout').select();
        document.execCommand('copy');
    })
    $('#downset').click(function(){//Generates a settings JSON file
        var todown = '{\n\t"TVN Settings": {\n';
        todown += '\t}\n}';
        //Downoad code
        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(todown));
        element.setAttribute('download', "settings.json");
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    })
    // URL splicing
    var url = window.location.href;
    var params = url.split('?');
    if(params[1] != undefined && isNaN(params[24]) == false){//If there is a custom URL then a custom calculator is launched
        initial();
    }
    else{//If the custom URL is strange or not present, launches standard tri-normal
        initial();
    }
                                                            // Testing button
    $('#test').click(function(){
        var real = sineSum;
        var yeeting = normalize(sineSum);
        var imaginary = new Array(real.length); 
        imaginary.fill(0);
        var fft = new FFT(); 
        fft.calc(1, real, imaginary);
        var amp = [];//fft.amplitude(real, imaginary, 1);
        for(var i = 0; i < real.length;i++){
            amp.push(Math.sqrt(real[i]*real[i] + imaginary[i]*imaginary[i]));
        }
        var maxamp = Math.max(...amp);
        for(var i = 0; i < amp.length; i++){
            if(amp[i] <= maxamp*0.01 || i == 0 || i == 1){amp[i] = 0;}
        }
        amp = normalize(amp);
        var tempmag = [];
        for(var i = 1; i <= numSines; i++){
            tempmag.push(amp[5*i]);
        }
        var yeet = [];
        yeet = mags;
        mags = tempmag;
        //mags = normalize(tempmag);

        var todown = "Mine:\tTheirs:\n";
        todown += yeet.length + "\t" + mags.length + "\n";
        for(var i = 0; i < yeet.length; i++){
            todown += yeet[i] + "\t" + mags[i]*10000 + "\n";
        }
        todown="Raw:\n";
        for(var i = 0; i < amp.length; i++){
            todown += amp[i] + "\n";
        }
        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(todown));
        element.setAttribute('download', "data.txt");
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);

        mags = normalize(tempmag);
        sineMat = [];
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        $gameCanvas.clearCanvas();
        sineSum = sumAll(sineMat);
        drawAxisGame();
        drawCurve(sineSum, '#000');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
})