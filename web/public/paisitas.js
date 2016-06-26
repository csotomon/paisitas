var paisitasApp=angular.module('PaisitasApp', ['ngMaterial']);

paisitasApp.controller('PaisitasController', ['$scope', function ($scope){
  $scope.vehiculos = [];
  $scope.saludo = "";
  var markers = [];

  /*
  //dispositvos desde donde se esta conectado, si el valor es nulo, es que se esta desconectado
  var refConexiones = firebase.database().ref('vehiculos/-KKunARFX41tKfDXP4qg/conexiones');

  //Se almacena el tiemstamp de la ultima conexion (la ultima vez que se estuvo conectado)
  var refUltimaConex = firebase.database().ref('vehiculos/-KKunARFX41tKfDXP4qg/ultimaConexion');

  //se trae la informacion de la conexion
  var refConexActual = firebase.database().ref('.info/connected');

  refConexActual.on('value', function(snap) {
    //Si el valor es verdadero es que se esta conectado
    if (snap.val() === true) {
      console.log("conectado");
      //Se agrega el dispositivo desde donde se esta conectado
      var con = refConexiones.push(true);

      // cuando se desconecta se elimina el registro
      con.onDisconnect().remove();

      // cuando se desconect se actualiza el timestamp
      refUltimaConex.onDisconnect().set(firebase.database.ServerValue.TIMESTAMP);
    }
    else {
      console.log("desconectado");
    }
  });
*/

  function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
      markers[i].setMap(map);
    }
  }

  // Deletes all markers in the array by removing references to them.
  function deleteMarkers() {
    clearMarkers();
    markers = [];
  }

  // Removes the markers from the map, but keeps them in the array.
  function clearMarkers() {
    setMapOnAll(null);
  }

  var queryVehiculos = firebase.database().ref("vehiculos").orderByKey();
  queryVehiculos.on('value', function(snapshot){
    deleteMarkers();
    snapshot.forEach(function(childSnapshot) {
      var key = childSnapshot.key;
      // childData will be the actual contents of the child
      var childData = childSnapshot.val();

      if(childData.conexiones !== undefined)
        {
          console.log(childData.coordenadas);
          var pos = {
            lat: childData.latitud,
            lng: childData.longitud
          };

          var marker = new google.maps.Marker({
          position: pos,
          map: map,
          title: ''
          });

          var infowindow = new google.maps.InfoWindow({
            content: key
          });

          infowindow.open(map, marker);
          markers.push(marker);
        }

    });
  });


}]);
