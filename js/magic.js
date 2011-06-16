var timeout = false;

function run() {

	document.getElementById("chain").onclick = function() {
		if(!timeout) {
			timeout = window.setInterval("window.scrollBy(0,1);", 50);
		} else {
			window.clearInterval(timeout);
			timeout = false;
		}
	}

	canvas = document.getElementById("wheel").getContext("2d");
	image  = new Image();
	image.src = "/style/wheel.gif";
	image.onload = function() {
		canvas.translate(-200, 400)
		setInterval("canvas.rotate(0.005); canvas.drawImage(image, -400, -400);", 100)
	}
}

window.onload = run;
