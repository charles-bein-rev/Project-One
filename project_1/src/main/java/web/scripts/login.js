let formElement = document.getElementById('login_form');

function login() {
	let url = 'http://localhost:8000/login';
	let xhr = new XMLHttpRequest();
	const data = new URLSearchParams();

	for(const pair of new FormData(formElement)) {
		data.append(pair[0], pair[1]);
	}

	xhr.onreadystatechange = function(){
		if(xhr.readyState === 4 && xhr.status === 200) {
			let result = JSON.parse(xhr.response);
			sessionStorage.setItem('userid', result['userId']);
			sessionStorage.setItem('username', result['username']);
			sessionStorage.setItem('role', result['role']);
			window.location = 'dashboard.html';
		}
	}

	xhr.open('POST', url);

	xhr.send(data);
}

formElement.addEventListener('submit', (event) => {
	if(event.cancelable) {
		event.preventDefault();
	}
	login();
});

window.onload = function() {
	if(sessionStorage.length > 0) {
		window.location.replace('dashboard.html');
	}
}