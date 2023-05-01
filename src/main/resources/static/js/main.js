function loadMain () {
    let registerButton = document.getElementById("register-button");
    let loginButton = document.getElementById("login-button");
    let infoButton = document.getElementById("info");
    let myTaxiButton = document.getElementById("h1-mytaxi");

    setButtonOnClick(registerButton, "/register");
    setButtonOnClick(loginButton, "/login");
    setButtonOnClick(infoButton, "/info");
    setButtonOnClick(myTaxiButton, "/");
}

function setButtonOnClick(button, path) {
    if (button != null) {
        button.onclick = function() {
            window.location.replace(path);
        };
    }
}