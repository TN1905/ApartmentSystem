var app = angular.module("app", []);
app.controller("ApartmentController", function($scope, $http) {
    $scope.nhatros = [];
    $scope.sortField = 'price';
    $scope.sortOrder = 1;
    $scope.page = 1;
    $scope.limit = 6;
    $scope.start = ($scope.page - 1) * $scope.limit;

    $scope.changePage = function(newPage) {
        if (newPage < 1 || newPage > $scope.totalPage) return;
        $scope.page = newPage;
        $scope.start = ($scope.page - 1) * $scope.limit;
    };

    $http.get('/rest/apartments').then(resp => {
        $scope.nhatros = resp.data;
        $scope.totalPage = Math.ceil($scope.nhatros.length / $scope.limit);
    }).catch(error => {
        console.log("Error", error);
    });

    $scope.sortBy = function(field) {
        if ($scope.sortField === field) {
            $scope.sortOrder = $scope.sortOrder === 1 ? -1 : 1;
        } else {
            $scope.sortField = field;
            $scope.sortOrder = 1;
        }
    };
    
});
