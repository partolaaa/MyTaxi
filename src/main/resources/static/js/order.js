var departureMarker;
var arrivalMarker;
var directionsRenderer;

function initMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 49.988358, lng: 36.232845},
        zoom: 10
    });

    var departureInput = document.getElementById('pickup-input');
    var arrivalInput = document.getElementById('destination-input');

    var departureAutocomplete = new google.maps.places.Autocomplete(departureInput);
    var arrivalAutocomplete = new google.maps.places.Autocomplete(arrivalInput);

    directionsRenderer = new google.maps.DirectionsRenderer({ map: map });

    departureAutocomplete.addListener('place_changed', function() {
        var place = departureAutocomplete.getPlace();
        console.log(place);

        // Set map center to selected location
        map.setCenter(place.geometry.location);

        // If departure marker doesn't exist, create it
        if (!departureMarker) {
            departureMarker = new google.maps.Marker({
                position: place.geometry.location,
                map: map,
                title: place.name
            });
        } else {
            // Move existing departure marker to new location
            departureMarker.setPosition(place.geometry.location);
        }

        // If both markers have been set, calculate and display route
        if (departureMarker && arrivalMarker) {
            var directionsService = new google.maps.DirectionsService();

            var request = {
                origin: departureMarker.getPosition(),
                destination: arrivalMarker.getPosition(),
                travelMode: 'DRIVING'
            };

            directionsService.route(request, function(result, status) {
                if (status === 'OK') {
                    directionsRenderer.setDirections(result);
                }
            });
        } else {
            directionsRenderer.setDirections(null); // remove previous route
        }
    });

    arrivalAutocomplete.addListener('place_changed', function() {
        var place = arrivalAutocomplete.getPlace();
        console.log(place);

        // Set map center to selected location
        map.setCenter(place.geometry.location);

        // If arrival marker doesn't exist, create it
        if (!arrivalMarker) {
            arrivalMarker = new google.maps.Marker({
                position: place.geometry.location,
                map: map,
                title: place.name
            });
        } else {
            // Move existing arrival marker to new location
            arrivalMarker.setPosition(place.geometry.location);
        }

        // If both markers have been set, calculate and display route
        if (departureMarker && arrivalMarker) {
            var directionsService = new google.maps.DirectionsService();

            var request = {
                origin: departureMarker.getPosition(),
                destination: arrivalMarker.getPosition(),
                travelMode: 'DRIVING'
            };

            directionsService.route(request, function(result, status) {
                if (status === 'OK') {
                    directionsRenderer.setDirections(result);
                }
            });
        } else {
            directionsRenderer.setDirections(null); // remove previous route
        }
    });
}

function modifyPageAccordingToTheOrderInfo() {
    const passengerNameRow = document.getElementById("passenger-name-row");
    const passengerPhoneRow = document.getElementById("passenger-phone-row");
    const orderForAnotherPerson = document.getElementById("order-for-another-person");

    if (!orderForAnotherPerson.checked) {
        passengerNameRow.setAttribute("hidden", "");
        passengerPhoneRow.setAttribute("hidden", "");
        document.getElementById("order-form").style.minHeight = "555px";
    } else {
        passengerNameRow.removeAttribute("hidden");
        passengerPhoneRow.removeAttribute("hidden");
        document.getElementById("order-form").style.minHeight = "600px";
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const orderForAnotherPerson = document.getElementById("order-for-another-person");
    orderForAnotherPerson.addEventListener("change", function() {
        modifyPageAccordingToTheOrderInfo();
    });
});
