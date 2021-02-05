function getTopicList() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			populateTableFromJSON(JSON.parse(this.responseText));
		}
	  };
	  id = document.getElementById("eventid").innerHTML;
	  url = "http://localhost:8080/moderator/newtopicslist/" + id;
	  console.log(url);
	  xhttp.open("GET", url, true);
	  xhttp.setRequestHeader("Content-type", "application/json");
	  xhttp.send();
}

function populateTableFromJSON(topics) {
    
	//headers of table, names must be the same as in responseText
	var headers = ['id', 'name'];
    var table = document.getElementById("tablebody");
            
    // populate table with received data
    //iterate through rows
    for (var i = 0; i < topics.length; i++) {
        tr = table.insertRow(-1);        
        //iterate inside rows
        for (var j = 0; j <= headers.length; j++) {
            var tabCell = tr.insertCell(-1);
            if(j != headers.length) {
            	tabCell.innerHTML = (vintageData[i][headers[j]]);            	
            } else {
            	tabCell.innerHTML = ("<input type=\"checkbox\" name=\"select\"/>"); 
            }
        }        
    }
    document.getElementById("addtopics").hidden = false;
}    