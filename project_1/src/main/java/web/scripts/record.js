let role = sessionStorage.getItem('role');
let logoutUrl = 'http://localhost:8000/logout';

const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const id = urlParams.get('id');

let fetchUrl = 'http://localhost:8000/request/' + id;

let pageTitle = document.getElementsByTagName('title')[0];
let content = document.getElementById('content');
let admin_only = document.getElementById('admin_only');
let approve_button = document.getElementById('approve_button');
let deny_button = document.getElementById('deny_button');
let logout_button = document.getElementById('logout_button');
let back_button = document.getElementById('back_button');

let title = document.getElementById('short_description');
let amount = document.getElementById('amount');
let description = document.getElementById('long_description');
let step = document.getElementById('status');
let requester = document.getElementById('requester');

function logout() {
	fetch(logoutUrl);
	sessionStorage.clear();
	window.location.replace('login.html');
}

function approveOrDeny(status) {
	let updateUrl = fetchUrl + '/update/' + status;
	fetch(updateUrl, {
		method: 'post'
	});
	window.location.reload();
}

approve_button.addEventListener('click', (event) => {
	approveOrDeny('accepted');
});

deny_button.addEventListener('click', (event) => {
	approveOrDeny('rejected');
});

logout_button.addEventListener('click', (event) => {
	logout();
});

back_button.addEventListener('click', (event) => {
	window.location = 'dashboard.html';
});


if(role === null) {
	logout();
} else if (role === 'admin') {
	admin_only.toggleAttribute('hidden');
}

fetch(fetchUrl).then(
	response => {
		response.json().then(
			json_body => {
				title.innerHTML += json_body['shortDescription'];
				amount.innerHTML += json_body['amount'];
				description.innerHTML += json_body['longDescription'];
				step.innerHTML += json_body['status'];
				requester.innerHTML += json_body['requester']['username'];
				pageTitle.innerHTML = json_body['shortDescription'];
			}
		)
	}
)

