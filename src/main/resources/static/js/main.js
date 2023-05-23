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

function rateTrip(id, rating) {
    document.getElementById('order-id').value = id;
    document.getElementById('rating-input').value = rating;

    $.ajax({
        url: $('#rating-form').attr('action'),
        type: 'POST',
        data: $('#rating-form').serialize(),
        success: function(response) {
            let rateTheTripBtn= document.getElementById("rate-the-trip-" + id);

            rateTheTripBtn.onclick = null;
            rateTheTripBtn.style.background = "none";
            rateTheTripBtn.style.cursor = "context-menu";
            rateTheTripBtn.style.color = "darkorange";
            rateTheTripBtn.innerText = "Thanks for rating!";

            setTimeout(function() {
                rateTheTripBtn.style.color = "white";
                rateTheTripBtn.style.fontWeight = "normal";
                rateTheTripBtn.innerText = "Completed";
            }, 5000);
        },
        error: function(error) {
            console.log("An error occurred: " + error);
        }
    });

    closeTripRatingMenu();
}

function showTripRatingMenu(id) {
    const tripInfoPanel = document.getElementById("trip-info-panel");
    const overlay = document.getElementById("overlay");

    for (let i = 1; i <= 5; i++) {
        document.getElementById("rating" + i).onclick = function() {
            rateTrip(id, i);
        };
    }

    tripInfoPanel.style.display = "block";
    overlay.style.display = "block";

    setTimeout(function() {
        tripInfoPanel.style.opacity = "1";
        overlay.style.opacity = "1";
    }, 10);
}

function closeTripRatingMenu() {
    const tripInfoPanel = document.getElementById("trip-info-panel");
    const overlay = document.getElementById("overlay");

    tripInfoPanel.style.opacity = "0";
    overlay.style.opacity = "0";

    setTimeout(function() {
        tripInfoPanel.style.display = "none";
        overlay.style.display = "none";
    }, 500);
}

