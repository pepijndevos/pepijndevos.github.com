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
		a.appendChild(text);
		li.appendChild(a);
		results.appendChild(li);

	}
}

function run() {

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

        var drawer = document.getElementById("drawer");
        document.getElementById("chain").onclick = function(event) {
            if(drawer.className == "open") {
                drawer.className = "";
            } else {
                drawer.className = "open";
            }
        }
}

window.onload = run;
