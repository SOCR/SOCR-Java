
				function showValueSigmaX(newValue,e)
				{
					if(((e.keyCode == 13))){
						oldSig = sigma1;
						tmp = Math.round(newValue*100);
						newValue = tmp/100;
						sigma1 = newValue;
						for(var i = 0; i<7; i++)
						{
							xLab[i] = (i-3)*sigma1 + mu1;
						}
						$('canvas').remove('.surfacePlotCanvas')
						setUp();
						document.getElementById("rangeSigmaX").innerHTML=newValue;
						console.log(document.getElementById("xProb_Input"));
					}
				}

			function showValueSigmaY(newValue,e)
			{
				console.log(e, e.type, is_chrome);
				if(((e.keyCode == 13))){
				oldSig = sigma2;
				tmp = Math.round(newValue*100);
				newValue = tmp/100;
				sigma2 = newValue;
				for(var i = 0; i<7; i++)
				{	yLab[i] = (i-3)*sigma2 + mu2;  }
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("rangeSigmaY").innerHTML=newValue;
				console.log(document.getElementById("xProb_Input"));
				}
			}
			function xProbFunc(value,e){
				var yProbObj = document.getElementById("yProb_Input");
				var xLowProbObj = document.getElementById("xLowProb_Input");
				var yLowProbObj = document.getElementById("yLowProb_Input");
				console.log(e);
				if(e==undefined||e.keyCode ==13){

				if(yProbObj==undefined || isNaN(yProbObj.value)){
					yCut = Number.POSITIVE_INFINITY;
				}
				if(xLowProbObj==undefined || isNaN(xLowProbObj.value))
				{
					xLowCut = Number.NEGATIVE_INFINITY;
				}
				if(yLowProbObj==undefined || isNaN(yLowProbObj.value))
				{
					yLowCut = Number.NEGATIVE_INFINITY;
				}
				if(!isNaN(value)){
					xCut = value;
					Xset=true;
				}
				else{
					xCut = Number.POSITIVE_INFINITY;
				}

				var prob = 0;
				prob = sample().toFixed(4);
				var text;
				// if y not number, cut == infinity
				text = "P("+((xLowCut==Number.NEGATIVE_INFINITY)? "-"+ '&infin;'):xLowCut)+" < X < "+((xCut==Number.POSITIVE_INFINITY)? ('&infin;'):xCut)+" n "+((yLowCut==Number.NEGATIVE_INFINITY)? "-8":yLowCut)+" < Y< "+((yCut==Number.POSITIVE_INFINITY)? "8":yCut)+") = "+prob+"\n";
				textArea.value += text;
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("highY").innerHTML=((xCut==Number.POSITIVE_INFINITY)? 4:xCut);
				}
			}

			function xLowProbFunc(value,e){
			var xProbObj = document.getElementById("xProb_Input");
			var yProbObj = document.getElementById("yProb_Input");
			var yLowProbObj = document.getElementById("yLowProb_Input");
			if(e==undefined||e.keyCode ==13){

				if(yProbObj==undefined || isNaN(yProbObj.value)){
					yCut = Number.POSITIVE_INFINITY;
				}
				if(xProbObj==undefined || isNaN(xProbObj.value))
				{
					xCut = Number.POSITIVE_INFINITY;
				}
				if(yLowProbObj==undefined || isNaN(yLowProbObj.value))
				{
					yLowCut = Number.NEGATIVE_INFINITY;
				}
				if(!isNaN(value)){
					xLowCut = value;
					Xset=true;
				}
				else{
					xLowCut = Number.NEGATIVE_INFINITY;
				}
	
				var prob = 0;
				prob = sample().toFixed(4);
				var text;
				// if y not number, cut == infinity
				text = "P("+((xLowCut==Number.NEGATIVE_INFINITY)? "-8":xLowCut)+" < X < "+((xCut==Number.POSITIVE_INFINITY)? "8":xCut)+" n "+((yLowCut==Number.NEGATIVE_INFINITY)? "-8":yLowCut)+" < Y< "+((yCut==Number.POSITIVE_INFINITY)? "8":yCut)+") = "+prob+"\n";
				textArea.value += text;
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("lowY").innerHTML=((xLowCut==Number.NEGATIVE_INFINITY)? -4:xLowCut);
			}
		}

		function yProb(value, e){
		var xProbObj = document.getElementById("xProb_Input");
		var xLowProbObj = document.getElementById("xLowProb_Input");
		var yLowProbObj = document.getElementById("yLowProb_Input");
		yProbObj = document.getElementById("yProb_Input");
		if(e.keyCode ==13){
		console.log("pressed enter");

		if(xLowProbObj==undefined || isNaN(xLowProbObj.value)){
			xLowCut = Number.NEGATIVE_INFINITY;
		}
		if(xProbObj==undefined || isNaN(xProbObj.value))
		{
			xCut = Number.POSITIVE_INFINITY;
		}
		if(yLowProbObj==undefined || isNaN(yLowProbObj.value))
		{
			yLowCut = Number.NEGATIVE_INFINITY;
		}
		if(!isNaN(value)){
			yCut = value;
			Yset = true;
		}
		else{
			yCut = Number.POSITIVE_INFINITY;
		}

		var prob = 0;
		prob = sample().toFixed(4);
		var text;
		text = "P("+((xLowCut==Number.NEGATIVE_INFINITY)? "-8":xLowCut)+" < X < "+((xCut==Number.POSITIVE_INFINITY)? "8":xCut)+" n "+((yLowCut==Number.NEGATIVE_INFINITY)? "-8":yLowCut)+" < Y< "+((yCut==Number.POSITIVE_INFINITY)? "8":yCut)+") = "+prob+"\n";
		textArea.value += text;
		$('canvas').remove('.surfacePlotCanvas')
		setUp();
		document.getElementById("highX").innerHTML=((yCut==Number.POSITIVE_INFINITY)? 4:yCut);
		}
		}

		function yLowProb(value, e){
		var xProbObj = document.getElementById("xProb_Input");
		var yProbObj = document.getElementById("yProb_Input");
		var xLowProbObj = document.getElementById("xLowProb_Input");

		console.log(e);
		console.log(xProbObj);
		if(e.keyCode ==13){
			console.log("pressed enter");
			if(xLowProbObj==undefined || isNaN(xLowProbObj.value)){
				xLowCut = Number.NEGATIVE_INFINITY;
			}
			if(xProbObj==undefined || isNaN(xProbObj.value))
			{
				xCut = Number.POSITIVE_INFINITY;
			}
			if(yProbObj==undefined || isNaN(yProbObj.value))
			{
				yCut = Number.POSITIVE_INFINITY;
			}
			if(!isNaN(value)){
				yLowCut = value;
				console.log(yLowCut);
				Yset = true;
			}
			else{
				yLowCut = Number.NEGATIVE_INFINITY;
			}

			var prob = 0;
			prob = sample().toFixed(4);
			var text;
			text = "P("+((xLowCut==Number.NEGATIVE_INFINITY)? "-8":xLowCut)+" < X < "+((xCut==Number.POSITIVE_INFINITY)? "8":xCut)+" n "+((yLowCut==Number.NEGATIVE_INFINITY)? "-8":yLowCut)+" < Y< "+((yCut==Number.POSITIVE_INFINITY)? "8":yCut)+") = "+prob+"\n";
			textArea.value += text;
			$('canvas').remove('.surfacePlotCanvas')
			setUp();
			document.getElementById("lowX").innerHTML=((yLowCut==Number.NEGATIVE_INFINITY)? -4:yLowCut);
			}
		}


		is_chrome = /chrome/i.test( navigator.userAgent );
		function showValue(newValue,e)
		{
			console.log(e, e.type, is_chrome);
			if((newValue<.9999 && newValue>-0.9999)&&((e.keyCode == 13))){
				tmp = Math.round(newValue*10000);
				newValue = tmp/10000;
				rowS = newValue;
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("range").innerHTML=newValue;
			}
		} 
	
		
		function showValueMu1(newValue,e)
		{
			console.log(e, e.type, is_chrome);
			if(((e.keyCode == 13))){
				tmp = Math.round(newValue*100);
				newValue = tmp/100;
				mu1 = newValue;
				
				for(var i = 0; i<7; i++)
				{
					xLab[i] = (Math.round((newValue - 3 +i)*100))/100;
				} // update labels when using webGL
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("rangeMu1").innerHTML=newValue;
			}
		} 
	
		function showValueMu2(newValue, e)
		{
			if(((e.keyCode == 13))){
				tmp = Math.round(newValue*100);
				newValue = tmp/100;
				mu2 = newValue;
				var j=6;
				for(var i = 0; i<7; i++)
				{
					// Does this need to be inverted as listed below???
					//yLab[j] = (Math.round((newValue - 3 +i)*100))/100;
					// update tooltip vals when this is called
					
					// or does in need to direct (reverting) the above
					yLab[i] = (Math.round((newValue - 3 +i)*100))/100;
					j--;
				}
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("rangeMu2").innerHTML=newValue;
				// yProb(newValue);
			}
		} 

		function showValueSigmaX(newValue, e)
		{
			if(((e.keyCode == 13))){
				oldSig = sigma1;
				sigma1 = newValue;// .toFixed(3);
				
				for(var i = 0; i<7; i++)
				{	xLab[i] = (i-3)*sigma1 + mu1;
				}
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("rangeSigmaX").innerHTML=newValue;
			}
		} 
		
		function showValueSigmaY(newValue, e)
		{
			console.log(e, e.type, is_chrome);
			if(((e.keyCode == 13))){
				oldSig = sigma2;
				sigma2 = newValue;// .toFixed(3);
				
				for(var i = 0; i<7; i++)
				{	yLab[i] = (i-3)*sigma2 + mu2;
				}
				$('canvas').remove('.surfacePlotCanvas')
				setUp();
				document.getElementById("rangeSigmaY").innerHTML=newValue;
				
				// Refresh with SigmaX to fix annoying bug with Y axis inverse labels
			}
		} 
		
		setCondX=false;
		setCondLowX = false;
		setCondLowY = false;
		setCondY=false;
		setMargX=false;
		setMargY=false;
		function CondHighX(){
			// console.log(document.getElementById("CondLow").style.display);
			setCondX=true;
			setCondLowX = false;
			setCondLowY = false;
			setCondY=false;
			setMargX=false;
			setMargY=false;
		}
		function CondLowX(){
			setCondX=false;
			setCondLowX = true;
			setCondLowY = false;
			setCondY=false;
			setMargX=false;
			setMargY=false;
		}
		function CondHighY(){
			setCondX=false;
			setCondLowX = false;
			setCondLowY = false;
			setCondY=true;
			setMargX=false;
			setMargY=false;
		}
		function CondLowY(){
			setCondX=false;
			setCondLowX = false;
			setCondLowY = true;
			setCondY=false;
			setMargX=false;
			setMargY=false;
		}
		
		function setMarginalX(box){
			setCondX=false;
			setCondLowX = false;
			setCondLowY = false;
			setCondY=false;
			setMargX=true;
			setMargY=false;
		}
		function setMarginalY(box){
			setCondX=false;
			setCondLowX = false;
			setCondLowY = false;
			setCondY=false;
			setMargX=false;
			setMargY=true;
		}
      
