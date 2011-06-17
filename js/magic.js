var timeout = false;

function search(data) {
	var results = document.getElementById("searchresults");
	results.innerHTML = "";
	if(data == false) {
		var li = document.createElement('li');
		var text = document.createTextNode("Sad Tapir, nothing found.");
		li.appendChild(text);
		results.appendChild(li);	
	}

	for(idx in data) {
		var li = document.createElement('li');
		var a = document.createElement('a');
		var text = document.createTextNode(data[idx]['title']);
		a.href = data[idx]['link'];
		li.appendChild(a);
		a.appendChild(text);
		results.appendChild(li);

	}
}

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
		canvas.translate(-200, 400);
		setInterval("canvas.rotate(0.005); canvas.drawImage(image, -400, -400);", 100);
	}

	document.getElementById("search").onsubmit = function(form) {
		var url = this.action + '?';
		var params = [
			'token=' + this[0]['value'],
			'callback=' + this[1]['value'],
			'query=' + this[2]['value'],
		];
		url = url + params.join('&');
		var script = document.createElement('script');
		script.src = url;
		this.appendChild(script);
		return false;
	}
}

window.onload = run;
