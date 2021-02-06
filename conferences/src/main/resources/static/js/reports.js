$(window).on('load', function(){
	downloadSpeakers();
});

function downloadSpeakers(){
		var xhttp = new XMLHttpRequest();		
		xhttp.onload = function() {
			if (this.readyState == 4 && this.status == 200) {
				console.log(JSON.parse(this.responseText));
				fillInSpeakerTable(JSON.parse(this.responseText));
			} else {
				alert(`network error`);
			}
		  };		  
		xhttp.onerror = function() { 
			alert(`network error`);
		};
		xhttp.open("GET", "http://localhost:8080/report/speakers", true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send();
}

function fillInSpeakerTable(speakers){
	var selection = document.createElement('select');
	var opt = document.createElement('option');
	opt.appendChild( document.createTextNode(''));
	selection.appendChild(opt);
	for(var i = 0; i< speakers.length; i++){
		var opt = document.createElement('option');
		opt.appendChild( document.createTextNode(speakers[i].name));
		opt.value = speakers[i].name; 
		selection.appendChild(opt);
	}
	selection.options[0].selected = 'selected';
	var td = document.getElementsByClassName('speakers');
	for (var i = 0; i < td.length; i++) {
		td[i].appendChild(selection.cloneNode(true));
	}
}

function getTopic(button) {
	var report = button.closest('td').closest('tr').cells[0].textContent;
	url = "http://localhost:8080/report/update/"+ report;
	console.log(url);
}


