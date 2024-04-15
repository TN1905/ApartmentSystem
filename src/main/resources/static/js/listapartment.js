let host = "http://localhost:8080/rest";
const app = angular.module("app", []);
app.controller("ctrl", function($scope, $http) {
	$scope.form = {}
	$scope.items = []
	$scope.reset = function() {
		$scope.load_all();
	}

	$scope.sortByPrice = function() {
		$scope.items.sort(function(apart1, apart2) {
			return apart1.price - apart2.price;
		});
	}

	$scope.sortByAcreage = function() {
		$scope.items.sort(function(apart1, apart2) {
			return apart1.acreage - apart2.acreage;
		});
	}

	$scope.sortByPostingTime = function() {
		$scope.items.sort(function(apart1, apart2) {
			var timestamp1 = new Date(apart1.createdate).getTime();
			var timestamp2 = new Date(apart2.createdate).getTime();

			// Kiểm tra xem chuyển đổi có thành công không
			if (!isNaN(timestamp1) && !isNaN(timestamp2)) {
				// So sánh thời gian tạo của hai mục
				return timestamp1 - timestamp2;
			} else {
				// Nếu không thể chuyển đổi, báo lỗi và không thực hiện phép trừ
				console.error('Invalid createdate format.');
				return 0; // Trả về 0 để không làm thay đổi vị trí của các mục trong mảng
			}
		});
	}
	
	$scope.searchApart = function(apartFilter,priceFilter,provinceFilter,districtFilter,wardFilter){
		console.log(apartFilter);
		console.log(priceFilter);
		console.log(provinceFilter);
		console.log(districtFilter);
		console.log(wardFilter);
		$scope.items = $scope.items.filter(function(item) {
        var meetsCriteria = true;

        // Check if apartment type matches
        if (apartFilter && item.apartmentType.id !== apartFilter) {
            meetsCriteria = false;
        }

        // Check if price range matches
        if (priceFilter) {
            var minPrice, maxPrice;
            switch (priceFilter) {
                case "1to2":
                    minPrice = 1000000;
                    maxPrice = 2000000;
                    break;
                case "2to3":
                    minPrice = 2000000;
                    maxPrice = 3000000;
				case "3to5":
                    minPrice = 3000000;
                    maxPrice = 5000000;
                case "5to10":
                    minPrice = 5000000;
                    maxPrice = 10000000;
                case "upto10":
                    minPrice = 10000000;
                    maxPrice = 100000000000;
                    break;
                // Add other cases for different price ranges if needed
            }
            if (item.price < minPrice || item.price > maxPrice) {
                meetsCriteria = false;
            }
        }

        // Check if province matches
        if (provinceFilter && item.city !== provinceFilter) {
            meetsCriteria = false;
        }

        // Check if district matches
        if (districtFilter && item.district !== districtFilter) {
            meetsCriteria = false;
        }

        // Check if ward matches
        if (wardFilter && item.ward !== wardFilter) {
            meetsCriteria = false;
        }

        return meetsCriteria;
    });
	}

	$scope.page = 1;
	$scope.limit = 6;
	$scope.start = ($scope.page - 1) * $scope.limit;
	$scope.changePage = function(newPage) {
		$scope.page = newPage;
		$scope.start = ($scope.page - 1) * $scope.limit;
		// Thực hiện các hành động khác liên quan đến việc thay đổi trang ở đây
		// Ví dụ: load dữ liệu từ server cho trang mới, v.v.
	};
	$scope.totalPage = 0;
	$scope.numberOfPage = 0;

	$scope.load_all = function() {
		var url = `${host}/apartments`;
		$http.get(url).then(resp => {
			$scope.items = resp.data;
			$scope.totalPage = Math.ceil($scope.items.length / $scope.limit);
			$scope.numberOfPage = Array.from(Array($scope.totalPage).keys());
			console.log("Success", resp.data);
		}).catch(error => {
			console.log("Error", error);
		});
	}

	$scope.edit = function(id) {
		var url = `${host}/apartments/${id}`;
		$http.get(url).then(resp => {
			$scope.form = resp.data;
			console.log("Success", data);
		}).catch(error => {
			console.log("Error", error);
		});
	};

	$scope.create = function() {
		var item = angular.copy($scope.form);
		var url = `${host}/apartments`;
		$http.post(url, item).then(resp => {
			$scope.items.push(item);
			$scope.reset();
			console.log("Success", data);
		}).catch(error => {
			console.log("Error", error);
		});
	};

	$scope.update = function() {
		var item = angular.copy($scope.form);
		var url = `${host}/apartments/${$scope.form.id}`;
		$http.put(url, item).then(resp => {
			var index = $scope.items.findIndex(item => item.id == $scope.form.id);
			$scope.items[index] = resp.data;
			$scope.reset();
			console.log("Success", data);
		}).catch(error => {
			console.log("Error", error);
		});
	};

	$scope.delete = function(id) {
		var url = `${host}/apartments/${id}`;
		$http.delete(url).then(resp => {
			var index = $scope.items.findIndex(item => item.id == id);
			$scope.items.splice(index, 1);
			$scope.reset();
			console.log("Success", data);
		}).catch(error => {
			console.log("Error", error);
		});
	}

	$scope.load_all();

	$scope.form1 = {}
	$scope.items1 = []
	$scope.reset = function() {
		$scope.load_all();
		$scope.load_all1();
	}

	$scope.load_all1 = function() {
		var url = "http://localhost:8080/rest/aparttypes";
		$http.get(url).then(resp => {
			$scope.items1 = resp.data;
			console.log("Success", resp.data);
		}).catch(error => {
			console.log("Error", error);
		});
	}

	$scope.edit = function(id) {
		var url = `${host}/aparttypes/${id}`;
		$http.get(url).then(resp => {
			$scope.form1 = resp.data;
			console.log("Success", resp.data);
		}).catch(error => {
			console.log("Error", error);
		});
	};

	$scope.create = function() {
		var item = angular.copy($scope.form1);
		var url = `${host}/aparttypes`;
		$http.post(url, item).then(resp => {
			$scope.items1.push(item);
			$scope.reset();
			console.log("Success", resp.data);
		}).catch(error => {
			console.log("Error", error);
		});
	};

	$scope.update = function() {
		var item = angular.copy($scope.form1);
		var url = `${host}/aparttypes/${$scope.form1.id}`;
		$http.put(url, item).then(resp => {
			var index = $scope.items1.findIndex(item => item.id == $scope.form.id);
			$scope.items1[index] = resp.data;
			$scope.reset();
			console.log("Success", resp.data);
		}).catch(error => {
			console.log("Error", error);
		});
	};

	$scope.delete = function(id) {
		var url = `${host}/aparttypes/${id}`;
		$http.delete(url).then(resp => {
			var index = $scope.items1.findIndex(item => item.id == id);
			$scope.items1.splice(index, 1);
			$scope.reset();
			console.log("Success", resp.data);
		}).catch(error => {
			console.log("Error", error);
		});
	}

	$scope.load_all();
	$scope.load_all1();
})