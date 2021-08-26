
// Provided  by Neil Roberts http://ross.posterous.com/2008/08/19/iphone-touch-events-in-javascript/
// Touch handler
function touchHandler(event)
{
    var touches = event.changedTouches, first = touches[0], type = "";
	
    switch(event.type)
    {	
        case "touchstart": type = "mousedown"; break;
        case "touchmove":  type="mousemove"; break;        
        case "touchend":   type="mouseup"; break;
        default: return;
    }
    
	//Creating MouseEvent
    var simulatedEvent = document.createEvent("MouseEvent");
    simulatedEvent.initMouseEvent(type, true, true, window, 1, first.screenX, first.screenY, first.clientX, first.clientY, false, false, false, false, 0/*left*/, null);
	first.target.dispatchEvent(simulatedEvent);
	//With preventDefault commented out, the WebGL checkbox is clickable again
    //event.preventDefault();
}

