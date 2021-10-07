let role = sessionStorage.getItem('role');
let formElement = document.getElementById('form');
let cancel_button = document.getElementById('cancel_button');
let logoutUrl = 'http://localhost:8000/logout';

function logout() {
	fetch(logoutUrl);
	sessionStorage.clear();
	window.location.replace('login.html');
}

function submitRequest() {
	let url = 'http://localhost:8000/request/new';
	let xhr = new XMLHttpRequest();
	const data = new URLSearchParams();

	for(const pair of new FormData(formElement)) {
		data.append(pair[0], pair[1]);
	}

	xhr.onreadystatechange = function(){
		if(xhr.readyState === 4 && xhr.status === 201) {
			window.location = 'dashboard.html';
		}
	}

	xhr.open('POST', url);
	xhr.send(data);
}

cancel_button.addEventListener('click', (event) => {
	if(event.cancelable) {
		event.preventDefault();
	}
	let choice = confirm('Unsubmitted content will be lost, are you sure you want to cancel?');
	if(choice) {
		window.location = 'dashboard.html'
	}
});

formElement.addEventListener('submit', (event) => {
	if(event.cancelable) {
		event.preventDefault();
	}
	submitRequest();
});

if(role === null) {
	logout();
}