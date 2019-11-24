$(function() {
	iniTimerGlobal();
});

var intervalo_timer;
var timeout_timer;
function iniTimerGlobal() {
	var paginaLogout = "/login";
	var totalSegundos = 1800; // (30 min x 60 seg = 1800 seg)
	clearTimeout(timeout_timer);
	clearInterval(intervalo_timer);
	intervalo_timer = setInterval(setTime, 1000);

	function setTime() {
		if (totalSegundos <= 0) {
			timeout_timer = setTimeout('location="' + paginaLogout + '"', 1);
		}

		if (totalSegundos > 0) {
			totalSegundos -= 1;
		}

		var min = parseInt((totalSegundos / 60));
		var seg = totalSegundos % 60;
		document.getElementById('g_tiempo_ind').innerHTML = visor(min) + ":"
				+ visor(seg);
	}

	function visor(val) {
		var valString = val + "";
		if (valString.length < 2) {
			return "0" + valString;
		} else {
			return valString;
		}
	}
}
