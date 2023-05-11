let departureMarker;
let arrivalMarker;
let directionsRenderer;
let journeyDistance;

function initMap() {
    let map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 49.988358, lng: 36.232845 },
        zoom: 10
    });

    let departureInput = document.getElementById('pickup-input');
    let arrivalInput = document.getElementById('destination-input');

    let departureAutocomplete = new google.maps.places.Autocomplete(departureInput);
    let arrivalAutocomplete = new google.maps.places.Autocomplete(arrivalInput);

    directionsRenderer = new google.maps.DirectionsRenderer({ map: map });

    departureAutocomplete.addListener('place_changed', function () {
        let place = departureAutocomplete.getPlace();
        //console.log(place);

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
            calculateAndDisplayRoute();
        } else {
            directionsRenderer.setDirections(null); // remove previous route
        }
    });

    arrivalAutocomplete.addListener('place_changed', function () {
        let place = arrivalAutocomplete.getPlace();
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
            calculateAndDisplayRoute();
        } else {
            directionsRenderer.setDirections(null); // remove previous route
        }
    });

    function calculateAndDisplayRoute() {
        let directionsService = new google.maps.DirectionsService();

        let request = {
            origin: departureMarker.getPosition(),
            destination: arrivalMarker.getPosition(),
            travelMode: 'DRIVING'
        };

        directionsService.route(request, function (result, status) {
            if (status === 'OK') {
                directionsRenderer.setDirections(result);
                let route = result.routes[0];
                let distance = 0;

                // Calculate total distance of all legs
                for (let i = 0; i < route.legs.length; i++) {
                    distance += route.legs[i].distance.value;
                }

                //console.log('Journey Distance: ' + (distance / 1000) + ' kilometers');
                journeyDistance = distance;
                document.getElementById("display-price").innerHTML = ((journeyDistance / 1000) * 15).toFixed(2);
                document.getElementById("price").value = ((journeyDistance / 1000) * 15).toFixed(2);
            }
        });
    }
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

function payWithBonuses (bonusesAmount) {
    if (document.getElementById("pay-with-bonuses").checked) {
        document.getElementById("display-price").innerHTML = (((journeyDistance / 1000) * 15) - bonusesAmount).toFixed(2);
        document.getElementById("price").value = (((journeyDistance / 1000) * 15) - bonusesAmount).toFixed(2);
    } else {
        document.getElementById("display-price").innerHTML = (((journeyDistance / 1000) * 15)).toFixed(2);
        document.getElementById("price").value = (((journeyDistance / 1000) * 15)).toFixed(2);
    }
}
