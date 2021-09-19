$(document).ready(function(){
                                                            //Canvas Resizing - must be first!
    var pagewidth = $(window).width();
    if(pagewidth >= 1600){//Default state
        $('#numpoints').replaceWith('<b id = "numpoints">1500</b>');
    }
    else if(pagewidth < 1600 && pagewidth >= 1300){
        $('#game').replaceWith('<canvas id = "game" width="1220" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="1220" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="1220" height="220"></canvas>');
        $('#numpoints').replaceWith('<b id = "numpoints">1200</b>');
        $('#mppairs').val(40).trigger('change');
        $('#pairs').replaceWith('<b id = "pairs">40</b>');
    }
    else if(pagewidth < 1300 && pagewidth >= 1000){
        $('#game').replaceWith('<canvas id = "game" width="920" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="920" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="920" height="220"></canvas>');
        $('#numpoints').replaceWith('<b id = "numpoints">900</b>');
        $('#mppairs').val(30).trigger('change');
        $('#pairs').replaceWith('<b id = "pairs">30</b>');
    }
    else if(pagewidth < 1000){
        $('#game').replaceWith('<canvas id = "game" width="620" height="220"></canvas>');
        $('#magnitudes').replaceWith('<canvas id = "magnitudes" width="620" height="220"></canvas>');
        $('#offsets').replaceWith('<canvas id = "offsets" width="620" height="220"></canvas>');
        $('#numpoints').replaceWith('<b id = "numpoints">600</b>');
        $('#mppairs').val(20).trigger('change');
        $('#pairs').replaceWith('<b id = "pairs">20</b>');
    }
                                                            //Notes and Comments:
    /*
    - Some terminology used here:
        Array = 1 dimensional vector
        Matrix = an array of arrays, is 2D
        Tensor = an array of arrays of arrays, is 3D
    - The mousemove event fires off only several times a second, so rapid movement cannot be accurately tracked
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
    var hastrue = 0;// 1 if the true wave needs to be drawn, 0 otherwise
    var pressed = 0;// Stores the mouse state, 1 = pressed, 0 = not pressed
    var oldx = 0;// Used for tracking rapid mouse movement
    var oldy = 0;// Used for tracking rapid mouse movement
    var yoink = 0;//Y offset, only used for user drawn graphs
    var showid = 0;// Stores what graph type is currently showing - 0 Sine, 1 Square, 2 Triangle, 3 Sawtooth, 4 Noise, 5 Custom drawing, 6 Custom slider input
    var gametitle = ["Sine Wave", "Square Wave", "Triange Wave", "Sawtooth Wave", "Random Noise", "Custom Drawing", "Custom Slider Input"];//Stores graph type titles
                                                            //Supportive Math Functions
    var generateSin = function(period, magnitude, phase){//Generates a sine curve based on inputs
        var out = [];
        var inc = (xmax-xmin)/xaxisrange;
        for (var i = 0; i < xaxisrange; i++){
            out.push(magnitude*Math.sin(inc*i*period + phase));
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
        if(absmax == 0){return arr;}
        for(var i = 0; i < arr.length; i++){
            arr[i] = arr[i]/absmax;
        }
        return arr;
    }
    var myfft = function(arr){//Discrete FFT function of my own making, based on Cooley-Tukey algorithm - https://en.wikipedia.org/wiki/Cooley%E2%80%93Tukey_FFT_algorithm 
        var real = [];
        var im = [];
        var N = arr.length;
        for(var k = 0; k < N; k++){//Running the FFT and generating the complex result
            var tempsum = 0;
            var tempsumi = 0;
            for(var n = 0; n < N; n++){
                tempsum += arr[n]*Math.cos(2*Math.PI*n*k/N);//Since JS cannot comprehend imaginary numbers, the FFT complex output is stored based on Euler's formula
                tempsumi += arr[n]*Math.sin(2*Math.PI*n*k/N);
            }
            real.push(tempsum);
            im.push(tempsumi);
        }
        var abs = [];
        var ang = [];
        for(var i = 0; i < real.length; i++){//Extracting Magnitude and Phase information
            abs.push(Math.sqrt(real[i]*real[i] + im[i]*im[i]));
            ang.push(Math.atan2(real[i],im[i]));
        }
        return [abs, ang];
    }
    var runfft = function(arr){//Runs the FFT algorithm on a provided array and sets the sineSum array ready for drawing
        var magtemp = [];
        var phasetemp = [];
        var combined = [];
        combined = myfft(arr);
        magtemp = combined[0];
        phasetemp = combined[1];
        var maxamp = Math.max(...magtemp);
        var maxphase = Math.max(...phasetemp);
        for(var i = 0; i < magtemp.length; i++){
            if(magtemp[i] <= maxamp*0.001 || i == 0 || i == 1){magtemp[i] = 0;}
            if(Math.abs(phasetemp[i]) <= maxphase*0.001 || i == 0 || i == 1){phasetemp[i] = 0;}
            if(magtemp[i] == 0){phasetemp[i] = 0;}
        }

        for(var i = 1; i <= numSines; i++){
            mags[i-1] = magtemp[3*i];
            phase[i-1] = phasetemp[3*i];
        }
        mags = normalize(mags);
        sineMat = [];
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
    }
    var yoinkaverage = function(){//Is only run on user drawn graphs
        yoink = 0;
        var tot = 0;
        for(var i = 0; i < truewave.length; i++){
            tot += truewave[i];
        }
        yoink = tot/truewave.length;
        var rangeyoink = Math.max(...truewave)-Math.min(...truewave);
        var rangeog = Math.max(...sineSum)-Math.min(...sineSum);
        for(var i = 0; i < sineSum.length; i++){
            sineSum[i] = sineSum[i]*(rangeyoink/rangeog) + yoink;
        }
    }
    var addyoink = function(num){//Adds the Y offset
        for(var i = 0; i < sineSum.length; i++){
            sineSum[i] = sineSum[i] + num;
        }
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
    var drawAxisGame = function(){//Draws the game canvas axis
        $('#yoink').replaceWith('<b id = "yoink">' + yoink.toFixed(3) + '</b>');
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
    var drawAxisMags = function(){//Draws the magnitudes canvas axis
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
    var drawAxisPhase = function(){//Draws the phases canvas axis
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
    var drawMags = function(){//Draws the magnitudes circles
        var mult = Math.floor(xaxisrange/numSines);
        for(var i = 0; i < numSines; i++){
            $magsCanvas.drawArc({
            layer: true,
            group: ['circles'],
            draggable: true,
            bringToFront: true,
            fillStyle: '#00f',
            x: xstart+(i+1)*mult, y: ystart+yaxisrange/2-mags[i]*yaxisrange,
            radius: 5,
            restrictDragToAxis: 'y',
            dragstop: function(shape) {
                showid = 6;
                hastrue = 0;
                truewave = [];
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
    var drawPhases = function(){//Draws the phases circles
        var mult = Math.floor(xaxisrange/numSines);
        for(var i = 0; i < numSines; i++){
            $phaseCanvas.drawArc({
                layer: true,
                group: ['circles'],
                draggable: true,
                bringToFront: true,
                fillStyle: '#00f',
                x: xstart+(i+1)*mult, y: ystart-phase[i]*yaxisrange/(2*Math.PI),
                radius: 5,
                restrictDragToAxis: 'y',
                dragstop: function(shape) {
                    showid = 6;
                    hastrue = 0;
                    truewave = [];
                    redrawFFT(shape.x, shape.y, 1);
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
    var redrawLines = function(canvas, choice){//Draws the lines from circles to x axis, choice = 0 for magnitudes, 1 for phases
        var mult = Math.floor(xaxisrange/numSines);
        canvas.removeLayers('lines').drawLayers();
        for (var i = 0; i < numSines; i++){
            var temp2 = 0;
            var temp3 = 0;
            if(choice == 0){temp2 = ystart+yaxisrange/2-mags[i]*yaxisrange;temp3 = 10+yaxisrange;}
            else{temp2 = ystart-phase[i]*yaxisrange/(2*Math.PI);temp3 = ystart;}
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
    $('#yoinkval').on('input', function(){//Adjusts the y offset and redraws all canvases
        showid = 6;
        yoink = this.value/100;
        hastrue = 0;
        truewave = [];
        for(var i = 0; i < numSines; i++){
            var temp = [];
            temp = generateSin(i+1, mags[i], phase[i]);
            sineMat.push(temp);
        }
        sineSum = sumAll(sineMat);
        addyoink(yoink);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#f00');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
    $('#mppairs').on('input', function(){//Adds/removes magnitude/phase pairs
        var oldsines = mags.length;
        numSines = this.value;
        $('#pairs').replaceWith('<b id = "pairs">' + numSines + '</b>');
        if(oldsines > numSines){//Removing pairs
            mags.splice(oldsines,numSines-oldsines);
            phase.splice(oldsines,numSines-oldsines);
        }
        else if(oldsines < numSines){//Adding pairs - all initially start at 0
            for(var i = oldsines-1; i<numSines;i++){
                mags.push(0);
                phase.push(0);
            }
        }
        if(hastrue == 1){//If there is a true wave drawn reruns fft to populate new mag/phase pairs
            runfft(truewave);
            yoinkaverage();
        }
        else{//No true wave means regenerating the sine matrix to accomodate a change in the number of sines
            sineMat = [];
            for(var i = 0; i < numSines; i++){
                var temp = [];
                temp = generateSin(i+1, mags[i], phase[i]);
                sineMat.push(temp);
            }
            sineSum = sumAll(sineMat);
            addyoink(yoink);
        }
        $gameCanvas.clearCanvas();
        drawAxisGame();
        if(hastrue == 1){drawCurve(truewave, '#00f');}
        drawCurve(sineSum, '#f00');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
                                                            //FFT making
    var redrawFFT = function(xpos, ypos, choice){//Does everything once the Game is updated, choice = 0 if magnitude is changed, 1 if 
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
            phase[loc] = -2*Math.PI*(ypos-ystart)/yaxisrange;
        }
        else if(choice == 2){
            $gameCanvas.clearCanvas();
            drawAxisGame();
            if(hastrue == 1){drawCurve(truewave, '#00f');}
            drawCurve(sineSum, '#f00');
            drawCurve(sineMat[loc], '#f0f');
            return;
        }
        else if(choice == 3){
            $gameCanvas.clearCanvas();
            drawAxisGame();
            if(hastrue == 1){drawCurve(truewave, '#00f');}
            drawCurve(sineSum, '#f00');
            return;
        }
        sineMat[loc] = generateSin(loc+1, mags[loc], phase[loc]);
        sineSum = sumAll(sineMat);
        if(hastrue == 0){addyoink(yoink);}
        $gameCanvas.clearCanvas();
        drawAxisGame();
        if(hastrue == 1){drawCurve(truewave, '#00f');}
        drawCurve(sineSum, '#f00');
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
        drawCurve(sineSum, '#f00');
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
    $('#sine').click(function(){//Sine wave
        showid = 0;
        mags = [];
        phase = [];
        sineMat = [];
        hastrue = 0;
        truewave = [];
        yoink = 0;
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
        drawCurve(sineSum, '#f00');
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
        truewave = [];
        hastrue = 1;
        yoink = 0;
        var delta = xaxisrange/6;
        for(var i = 0; i < xaxisrange; i++){
            if(i <= delta || (i > delta*2 && i <= delta*3) || (i > delta*4 && i <= delta*5)){truewave.push(0.85);}
            else{truewave.push(-0.85);}
        }
        runfft(truewave);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#f00');
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
        truewave = [];
        hastrue = 1;
        yoink = 0;
        var delta = xaxisrange/6;
        for(var i = 0; i < xaxisrange; i++){
            if(i <= delta || (i > delta*2 && i <= delta*3) || (i > delta*4 && i <= delta*5)){truewave.push(-1+2*((i-1)%delta)/delta);}
            else{truewave.push(1-2*((i-1)%delta)/delta);}
        }
        runfft(truewave);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#f00');
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
        truewave = [];
        hastrue = 1;
        yoink = 0;
        var delta = xaxisrange/6;
        for(var i = 0; i < xaxisrange; i++){
            truewave.push(-0.9+1.8*((i-1)%delta)/delta);
        }
        runfft(truewave);
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#f00');
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
        hastrue = 0;
        truewave = [];
        yoink = 0;
        for(var i = 0; i < numSines; i++){
            mags.push(Math.random());
            phase.push(2*Math.random()-1);
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
        drawCurve(sineSum, '#f00');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
    })
                                                            //User drawing commands
    $gameCanvas.mousedown(function(event) {// Starts drawing the trueline (blue) when the mouse button is pressed
        showid = 5;
        var newx = event.offsetX-10;//accounting for 10px at the edge of the game
        var newy = -2*(event.offsetY-ystart)/yaxisrange;
        if(newx >= xaxisrange){newx = xaxisrange-1;}
        pressed = 1;
        hastrue = 1;
        truewave = sineSum;
        if(newy > 1){newy = 1;}
        else if(newy < -1){newy = -1;}
        truewave[newx] = newy;
        if(newx <= xaxisrange/3){
            truewave[newx+xaxisrange/3]=newy;
            truewave[newx+2*xaxisrange/3]=newy;
        }
        else if(newx <= 2*xaxisrange/3){
            truewave[newx-xaxisrange/3]=newy;
            truewave[newx+xaxisrange/3]=newy;
        }
        else{
            truewave[newx-xaxisrange/3]=newy;
            truewave[newx-2*xaxisrange/3]=newy;
        }
        if(truewave.length > xaxisrange){truewave.splice(truewave.length, truewave.length-xaxisrange);}
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        oldx = newx;
        oldy = newy;
    });
    $gameCanvas.mousemove(function(event) {// Draws trueline when the mouse is moved
        if (pressed == 1){// If user's mouse is down
            var newx = event.offsetX-10;//accounting for 10px at the edge of the game
            if(newx >= xaxisrange){newx = xaxisrange-1;}
            var newy = -2*(event.offsetY-ystart)/yaxisrange;
            pressed = 1;
            hastrue = 1;
            truewave = sineSum;
            if(newy > 1){newy = 1;}
            else if(newy < -1){newy = -1;}
            var gradient = (newy-oldy)/(newx-oldx);
            if(gradient > 100){gradient = 100;}
            else if(gradient < -100){gradient = -100;}
            else if(newx == oldx){gradient = 0;}
            //Draws a straight line between the last recorded point and current point
            if(newx > oldx){//new point is right of old point
                for(i = oldx; i <= newx; i++){
                    truewave[i] = oldy+(i-oldx)*gradient;
                    if(i < xaxisrange/3){
                        truewave[i+xaxisrange/3]=oldy+(i-oldx)*gradient;
                        truewave[i+2*xaxisrange/3]=oldy+(i-oldx)*gradient;
                    }
                    else if(i < 2*xaxisrange/3){
                        truewave[i-xaxisrange/3]=oldy+(i-oldx)*gradient;
                        truewave[i+xaxisrange/3]=oldy+(i-oldx)*gradient;
                    }
                    else{
                        truewave[i-xaxisrange/3]=oldy+(i-oldx)*gradient;
                        truewave[i-2*xaxisrange/3]=oldy+(i-oldx)*gradient;
                    }
                }
            }
            else if(newx == oldx){// new point is directly above old point
                truewave[oldx] = newy;
                if(oldx < xaxisrange/3){
                    truewave[oldx+xaxisrange/3]=newy;
                    truewave[oldx+2*xaxisrange/3]=newy;
                }
                else if(oldx < 2*xaxisrange/3){
                    truewave[oldx-xaxisrange/3]=newy;
                    truewave[oldx+xaxisrange/3]=newy;
                }
                else{
                    truewave[oldx-xaxisrange/3]=newy;
                    truewave[oldx-2*xaxisrange/3]=newy;
                }
            }
            else{//new point is left of old point
                for(i = newx; i <= oldx; i++){
                    truewave[i] = oldy+(i-oldx)*gradient;
                    if(i < xaxisrange/3){
                        truewave[i+xaxisrange/3]=oldy+(i-oldx)*gradient;
                        truewave[i+2*xaxisrange/3]=oldy+(i-oldx)*gradient;
                    }
                    else if(i < 2*xaxisrange/3){
                        truewave[i-xaxisrange/3]=oldy+(i-oldx)*gradient;
                        truewave[i+xaxisrange/3]=oldy+(i-oldx)*gradient;
                    }
                    else{
                        truewave[i-xaxisrange/3]=oldy+(i-oldx)*gradient;
                        truewave[i-2*xaxisrange/3]=oldy+(i-oldx)*gradient;
                    }
                }
            }
            if(truewave.length > xaxisrange){truewave.splice(truewave.length, truewave.length-xaxisrange);}
            else{
                $gameCanvas.clearCanvas();
                drawAxisGame();
                drawCurve(truewave, '#00f');
                oldx = newx;
                oldy = newy;
            }
        }
    });
    $gameCanvas.mouseup(function() {// Derives the FFT from the drawn trueline and updates all canvases
        pressed = 0;
        runfft(truewave);
        yoinkaverage();
        $gameCanvas.clearCanvas();
        drawAxisGame();
        drawCurve(truewave, '#00f');
        drawCurve(sineSum, '#f00');
        redrawLines($magsCanvas, 0);
        drawAxisMags();
        drawMags();
        redrawLines($phaseCanvas, 1);
        drawAxisPhase();
        drawPhases();
        $('#yoinkval').val(parseInt($("#yoinkval").val())+yoink*100).trigger('change');
    });
    $gameCanvas.mouseout(function() {// Derives the FFT from the drawn trueline and updates all canvases
        if(pressed == 1){
            pressed = 0;
            runfft(truewave);
            yoinkaverage();
            $gameCanvas.clearCanvas();
            drawAxisGame();
            drawCurve(truewave, '#00f');
            drawCurve(sineSum, '#f00');
            redrawLines($magsCanvas, 0);
            drawAxisMags();
            drawMags();
            redrawLines($phaseCanvas, 1);
            drawAxisPhase();
            drawPhases();
            $('#yoinkval').val(parseInt($("#yoinkval").val())+yoink*100).trigger('change');
        }
        
    });
                                                            // Download Data and Settings
    $('#downdata').click(function(){//Downloading data as a txt file
        //Graph Settings
        var todown = "Magnitudes:\tPhases:\n";
        for(var i = 0; i < mags.length; i++){
            todown += mags[i] + "\t" + phase[i] + "\n";
        }
        todown += "Y Offset:\t" + yoink + "\n";
        if(hastrue == 1){todown += "Original Data:\t IFFT:\n";}
        else{todown += sineSum[i] + "\n";}
        for(var i = 0; i < sineSum.length; i++){
            if(hastrue == 1){todown += truewave[i] + "\t" + sineSum[i] + "\n";}
            else{todown += sineSum[i] + "\n";}
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
    $('#getset').click(function(){//Not used - Generates url string based on graph settings
        var toout = params[0];
        toout += "?";
        $('#setout').replaceWith('<textarea id = "setout" readonly>' + toout + '</textarea>');
    })
    $('#copyset').click(function(){//Not used - Copies settings url string to clipboard
        $('#setout').select();
        document.execCommand('copy');
    })
    $('#downfft').click(function(){//Generates a settings txt file
        var todown = 'FFT output: \n';
        for(var i = 0; i < mags.length; i++){
            if(phase[i] >= 0){todown += mags[i] + " + " + phase[i] + "i\n";}
            else{todown += (mags[i]*Math.cos(phase[i])) + " " + (mags[i]*Math.sin(phase[i])) + "i\n";}
        }
        todown += "Y Offset:\t" + yoink + "\n";
        //Downoad code
        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(todown));
        element.setAttribute('download', "fft.txt");
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    })
                                                            // URL splicing
    var url = window.location.href;
    var params = url.split('?');
    if(params[1] != undefined && isNaN(params[24]) == false){//If there is a custom URL then a custom mode is launched
        initial();
    }
    else{//If the custom URL is strange or not present, launches standard Fourier game
        initial();
    }
                                                            // Testing button
    $('#test').click(function(){
    })
})