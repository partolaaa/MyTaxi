function loadMain () {
    let registerButton = document.getElementById("register-button");
    let loginButton = document.getElementById("login-button");
    let infoButton = document.getElementById("info");
    let myTaxiButton = document.getElementById("h1-mytaxi");
    let orderPlease = document.getElementById("order-please");
    let myCabinetButton = document.getElementById("my-cabinet-btn");
    let paymentTypeCash = document.getElementById('payment-type-cash');
    let carClassEconomy = document.getElementById('car-class-economy');
    let luffyGif = document.getElementById('luffy-doesnt-know');

    setButtonRedirectOnClick(registerButton, "/register");
    setButtonRedirectOnClick(loginButton, "/login");
    setButtonRedirectOnClick(infoButton, "/info");
    setButtonRedirectOnClick(myTaxiButton, "/order");
    setButtonRedirectOnClick(orderPlease, "/order");
    setButtonRedirectOnClick(myCabinetButton, "/my-orders");
    setButtonRedirectOnClick(luffyGif, "https://www.youtube.com/watch?v=HRaoYuRKBaA&t=3s");

    if (paymentTypeCash != null) {
        paymentTypeCash.checked = true;
    }
    if (carClassEconomy != null) {
        carClassEconomy.checked = true;
    }
}

function setButtonRedirectOnClick(button, path) {
    if (button != null) {
        button.onclick = function() {
            location.href=path;
        };
    }
}

function rateTrip(rating) {
    document.getElementById('rating-input').value = rating;

    $.ajax({
        url: $('#rating-form').attr('action'),
        type: 'POST',
        data: $('#rating-form').serialize(),
        error: function(error) {
            console.log("An error occurred: " + error);
        }
    });

    closeTripRatingMenu();
}

function showTripRatingMenu () {
    document.getElementById("trip-info-panel").style.display = "block";
}
function closeTripRatingMenu() {
    document.getElementById("trip-info-panel").style.display = "none";
}

