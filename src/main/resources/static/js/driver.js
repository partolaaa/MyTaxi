function loadDriver () {
    let myCabinetButton = document.getElementById("my-cabinet-btn");
    let doPlease = document.getElementById("do-please");

    setButtonRedirectOnClick(myCabinetButton, "/driver/orders");
    setButtonRedirectOnClick(doPlease, "/driver/orders");
}