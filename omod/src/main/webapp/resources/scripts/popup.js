/*******************************************************************************
 * Version 1.0 
 * Author: Yvgak
 ******************************************************************************/

function createPopUp(obj,title,label) {
	var id = obj.name.substring(obj.name.indexOf("_") + 1);
	var html = "<div class='pp_hdr'><div style='float:left; width:93%;'>"+title+"</div> <div style='float:right; width:5%;' class='pp_close' onclick='distroyDiv(this);' id='close_"
			+ id
			+ "'>X</div><div style='clear:both;'></div></div><div>"+label+" <input type='text' size='45' name='n_"
			+ id + "' id='n_" + id + "' onKeyUp='javascript:patientListInTable(this,"+id+",0);'/><div id='resultOfSearch'></div></div>";
	var div = document.createElement('div');
	div.className = "pp_div";
	div.setAttribute('id', 'pp_divId');
	div.innerHTML = html;
	div.style.top = getTopPosition(obj) + 20 + "px";
	div.style.left = getLeftPosition(obj) + "px";
	div.style.position = 'absolute';
	document.getElementById("popup").innerHTML = "";
	document.getElementById("popup").appendChild(div);

	document.getElementById("n_" + id).focus();
}

function distroyDiv(obj) {
	var id = obj.id.substring(obj.id.indexOf("_") + 1);
	document.getElementById("popup").removeChild(
			document.getElementById("pp_divId"));
}

function getLeftPosition(obj) {
	var leftValue = 0;
	while (obj) {
		leftValue += obj.offsetLeft;
		obj = obj.offsetParent;
	}
	return leftValue;
}

function getTopPosition(obj) {
	var topValue = 0;
	while (obj) {
		topValue += obj.offsetTop;
		obj = obj.offsetParent;
	}
	return topValue;
}