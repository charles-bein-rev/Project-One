let role = sessionStorage.getItem('role');
let fetchUrl = 'http://localhost:8000/request/all';
let logoutUrl = 'http://localhost:8000/logout';

let stats_button = document.getElementById('stats_button');
let logout_button = document.getElementById('logout_button');
let new_button = document.getElementById('new_button');
let table = document.getElementById('table')

function getAllData() {
	

	fetch(fetchUrl).then(
		response => {
			response.json().then(
				json_body => {
					populateTable(json_body);
				}
			)
		}
	)
}

function populateTable(data) {
	data.forEach(element => {
		var tr = document.createElement('tr');
		tr.innerHTML = '<td>' + element['shortDescription'] + '</td>' +
						'<td>$' + element['amount'] + '</td>' + 
						'<td>' + element['status'] + '</td>' +
						'<td><a href = \'record.html?id=' + element['requestId'] + '\'>View</a></td>';
		if(element['status'] === 'accepted') {
			tr.classList.add('accepted');
		} else if (element['status'] === 'rejected') {
			tr.classList.add('rejected');
		} else {
			tr.classList.add('base');
		}
		table.appendChild(tr);
	});
}

function logout() {
	fetch(logoutUrl);
	sessionStorage.clear();
	window.location.replace('login.html');
}

new_button.addEventListener('click', (event) => {
	window.location = 'edit.html';
})

logout_button.addEventListener('click', (event) => {
	logout();
});

stats_button.addEventListener('click', (event) => {
	window.location = 'stats.html';
})

if(role === 'admin') {
	document.getElementById('stats_button').toggleAttribute('hidden');
} else if(role === 'associate') {
	fetchUrl = fetchUrl + '/' + sessionStorage.getItem('userid');
} else {
	logout();
}
getAllData();

