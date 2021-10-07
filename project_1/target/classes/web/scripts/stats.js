let role = sessionStorage.getItem('role');
let logoutUrl = 'http://localhost:8000/logout';
let statsUrl = 'http://localhost:8000/request/aggregate';

let back_button = document.getElementById('back_button');
let logout_button = document.getElementById('logout_button');
let table = document.getElementById('table');

function logout() {
	fetch(logoutUrl);
	sessionStorage.clear();
	window.location.replace('login.html');
}

function getData() {
	fetch(statsUrl).then(
		response => {
			response.json().then(
				json_body => {
					console.log(json_body);
					populateTable(json_body);
				}
			)
		}
	)
}

function populateTable(data) {
	let totalCount = parseInt(data['totalCount']);
	let totalValue = parseFloat(data['totalValue']).toFixed(2);
	let average;
	if(totalCount >= 1) {
		average = (totalValue / totalCount).toFixed(2);
	} else {
		average = (0).toFixed(2);
	}
	let maxUser = data['maxUser'];
	let maxAmount = parseFloat(data['maxAmount']).toFixed(2);
	let averageUser = data['averageUser'];
	let averageAmount = parseFloat(data['averageAmount']).toFixed(2);

	// var tr = document.createElement('tr');
	document.getElementById('value_column').innerHTML = '<td>' + totalCount + ' ($' + totalValue + ')</td>' +
					'<td>$' + average + '</td>' +
					'<td>' + maxUser + ' ($' + maxAmount + ')</td>' +
					'<td>' + averageUser + ' ($' + averageAmount + ')</td>';
	// table.appendChild(tr);
}



back_button.addEventListener('click', (event) => {
	window.location = 'dashboard.html';
});

logout_button.addEventListener('click', (event) => {
	logout();
})

if(role != 'admin') {
	window.location.replace('dashboard.html');
}
getData();