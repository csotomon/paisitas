var paisitasApp=angular.module('PaisitasApp', ['ngMaterial']);

paisitasApp.controller('PaisitasController', ['$scope', function ($scope){
  $scope.vehiculos = [];
  $scope.saludo = "Hola mundo cruel";

//traemos los datos desde firebase
  firebase.database().ref('vehiculos').on('value', function(snapshot) {
    //alert('el valor es:'+ snapshot.val().coordenada);
    console.log(snapshot.val());
  });


}]);
