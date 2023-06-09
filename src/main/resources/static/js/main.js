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

function rateTrip(hash, rating) {
    document.getElementById('order-hash').value = hash;
    document.getElementById('rating-input').value = rating;

    $.ajax({
        url: $('#rating-form').attr('action'),
        type: 'POST',
        data: $('#rating-form').serialize(),
        success: function(response) {
            let rateTheTripBtn= document.getElementById("rate-the-trip-" + hash);

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

function showTripRatingMenu(element) {
    let hash = element.getAttribute('data-hash');
    const tripInfoPanel = document.getElementById("trip-info-panel");
    const overlay = document.getElementById("overlay");

    for (let i = 1; i <= 5; i++) {
        document.getElementById("rating" + i).onclick = function() {
            rateTrip(hash, i);
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

function cancelTrip(element) {
    let hash = element.getAttribute('data-hash');
    $.ajax({
        url: $('#cancel-form').attr('action'),
        type: 'POST',
        data: $('#cancel-form').serialize(),
        success: function(response) {
            let rateTheTripBtn = document.getElementById("cancel-" + hash);
            let bonuses = document.getElementById("bonuses");
            bonuses.innerText = response.toFixed(1);
            rateTheTripBtn.onclick = null;
            rateTheTripBtn.style.background = "none";
            rateTheTripBtn.style.cursor = "context-menu";
            rateTheTripBtn.style.color = "darkorange";
            rateTheTripBtn.innerText = "Order cancelled";

            setTimeout(function() {
                rateTheTripBtn.style.color = "white";
                rateTheTripBtn.style.fontWeight = "normal";
                rateTheTripBtn.innerText = "Cancelled";
            }, 5000);
        },
        error: function(error) {
            console.log("An error occurred: " + error);
        }
    });
}

function subscribe() {
    let socket = new SockJS('/websocket');
    let stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/orderStatus/' + orderHash, function(order){
            let status = JSON.parse(order.body).orderStatus;

            switch (status) {
                case "WAITING_FOR_CLIENT":
                    status = "Waiting for client";
                    break;
                case "ACCEPTED":
                    status = "Accepted";
                    break;
                case "IN_PROCESS":
                    status = "In process";
                    break;
                case "COMPLETED":
                    status = "Completed";
                    break;
                default:
                    status = "Unknown";
            }
            document.getElementById("trip-info-title").innerText = status;
        });
    });
}

