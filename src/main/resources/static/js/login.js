window.onload = function () {
    loadMain();

    document.getElementById("register-span").onclick = function() {
        window.location.replace("/register");
    };
}

function loadLogin () {
    let registerSpan = document.getElementById("register-span");

    setButtonRedirectOnClick(registerSpan, "/register")
}