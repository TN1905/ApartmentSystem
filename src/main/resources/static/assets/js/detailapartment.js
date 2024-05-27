app = angular.module("detail",[]);
app.controller("myDetailApartment",function($scope,$http){
    // $scope.apartmentId = document.getElementById('apartmentData');
    // console.log('Apartment ID:', $scope.apartmentId);
    // console.log('????');
    var apartmentDataElement = document.getElementById('apartmentData');
    $scope.apartmentId = apartmentDataElement.value;
    $scope.item ={};
    // Now you can use $scope.apartmentId in your AngularJS controller
    console.log('Apartment ID:', $scope.apartmentId);
    $scope.loadItem = function(){
        $http.get("/rest/apartments/" + $scope.apartmentId)
        .then(resp => {
            $scope.item = resp.data;
            console.log($scope.item);
        }).catch(error => {
            console.log("error" + error);
        });
            
        
    }
    $scope.loadItem();
})