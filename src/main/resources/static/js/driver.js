function loadDriver () {
    let myCabinetButton = document.getElementById("my-cabinet-btn");
    let myTaxiButton = document.getElementById("h1-mytaxi");
    let doPlease = document.getElementById("do-please");

    setButtonRedirectOnClick(myCabinetButton, "/driver/orders");
    setButtonRedirectOnClick(doPlease, "/driver/orders");
    setButtonRedirectOnClick(myTaxiButton, "/driver/orders");
}