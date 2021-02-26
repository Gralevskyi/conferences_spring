function downloadSpeakers(){
		var xhttp = new XMLHttpRequest();		
		xhttp.onload = function() {
			if (this.readyState == 4 && this.status == 200) {
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
	selection.id = "selectSpeakers";
	var opt = document.createElement('option');
	opt.appendChild( document.createTextNode(''));
	opt.value = -1; 
	selection.appendChild(opt);
	for(var i = 0; i< speakers.length; i++){
		var opt = document.createElement('option');
		opt.appendChild( document.createTextNode(speakers[i].localName));
		opt.value = speakers[i].id; 
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
}

function updateReport(button) {
	var row = button.closest('td').closest('tr');
	var report = row.cells[0].textContent;
	var creator = row.cells[1].children[0].textContent;
	var newAccepted = row.cells[4].children[0].value;
	var newSpeakerId = row.cells[5].children[0].value;
	if(newSpeakerId == -1 && newAccepted == 'true'){
		alert("You can not save accepted true on report without speaker");
	} else {
		container = {
				report: report, 
				creator: creator,
				newSpeakerId: newSpeakerId,
				newAccepted: newAccepted
		}
		
		sendUpdateRequest(container);
	}	
}

function sendUpdateRequest(contrainer){
	url = "http://localhost:8080/report/update/"+ container.report;
	var xhttp = new XMLHttpRequest();		
	xhttp.onload = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.location.reload();
		} else {
			alert(`network error`);
		}
	  };		  
	xhttp.onerror = function() { 
		alert(`network error`);
	};
	var report = {topicEn: container.report,
			topicUk: container.report,
			creator: container.creator,
			speakerId: container.newSpeakerId, 
			accepted: container.newAccepted == 'true'}
	var csrfCookie = document.getElementsByTagName('meta')[0].getAttribute('content');
	xhttp.open("POST", url, true);
	if (csrfCookie) {
		xhttp.setRequestHeader("X-CSRF-TOKEN", csrfCookie);
	};
	
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(JSON.stringify(report));
}

function addEmptyOption(){
	var sel = document.getElementById("speakers_select");
	var opt = document.createElement('option');
	opt.selected = 'selected';
	opt.appendChild(document.createTextNode(''));	
	sel.appendChild(opt);
}

$(function() {
    $("#navigation").load("navigation.html");
});

