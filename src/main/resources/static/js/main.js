function loadMain () {
    let registerButton = document.getElementById("register-button");
    let loginButton = document.getElementById("login-button");
    let infoButton = document.getElementById("info");
    let myTaxiButton = document.getElementById("h1-mytaxi");
    let orderPlease = document.getElementById("order-please");
    let myCabinetButton = document.getElementById("my-cabinet-btn");
    let paymentTypeCash = document.getElementById('payment-type-cash');

    setButtonRedirectOnClick(registerButton, "/register");
    setButtonRedirectOnClick(loginButton, "/login");
    setButtonRedirectOnClick(infoButton, "/info");
    setButtonRedirectOnClick(myTaxiButton, "/order");
    setButtonRedirectOnClick(orderPlease, "/order");
    setButtonRedirectOnClick(myCabinetButton, "/my-orders");

    if (paymentTypeCash != null) {
        paymentTypeCash.checked = true;
    }
}

function setButtonRedirectOnClick(button, path) {
    if (button != null) {
        button.onclick = function() {
            location.href=path;
        };
    }
}
