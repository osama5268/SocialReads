/**
 * 
 */
//console.log('hello');
const form = document.querySelector('#checkusernameform');
const usernamefield = document.querySelector('#username');
const resultDiv = document.querySelector("#username-feedback");
//const form = document.querySelector("form");
let username = "";
let timeout = setTimeout(function(){}, 0);
function checkUsername(){
	fetch('/signup/checkusername?username='+username)
		//.then(response => console.log(response))
		.then(response => response.json())
 		//.then(json => console.log(json))
 	.then(function (json){
				console.log(json)
				if(json.result === "false"){
					console.log("0");
					resultDiv.innerText = "Username " + username + " is already taken";
					resultDiv.style.color = "red";
				}
				else{
					console.log("1");
					resultDiv.innerText = "Username " + username + " is valid"; 
					//div.classList.add("valid-feedback");
					resultDiv.style.color = "green";
				}
				//usernamefield.parentElement.appendChild(div);
})
}

usernamefield.addEventListener('keyup', function(){
	clearTimeout(timeout);

	timeout = setTimeout(function(){
		username = usernamefield.value;
		//console.log(username);
		//call the api
		username.trim();
		if(username.length > 0){
			checkUsername();
		}		
	},1000);

})
