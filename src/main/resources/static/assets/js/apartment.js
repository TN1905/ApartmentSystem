const app = angular.module("app", []);
app.controller("apartmentCtrl", function ($scope, $http) {
  const urlApartment = `http://localhost:8080/rest/apartments`;
  const urlImage = `http://localhost:8080/rest/files/images`;
  const urlTypeApart = `http://localhost:8080/rest/aparttypes`;
  const urlAddress = `https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json?fbclid=IwAR0rFIXdhDePPDSI5SeQZarnl_UazlS2REGaXelLWT74AGV9DEpjHlXKGdY`;

  $scope.items = [];
  $scope.cates = [];
  $scope.form = {};
  $scope.cities = [];
  $scope.districts = [];
  $scope.wards = [];
  $scope.numberApart = 0;
  $scope.page = 1;
  $scope.limit = 10;
  $scope.start = ($scope.page - 1) * $scope.limit;
  $scope.filenames = [];
  $scope.currentfiles = [];
  $scope.totalPage = 0;
  $scope.numberOfPage = 0;

  $scope.changePage = function (newPage) {
    $scope.page = newPage;
    $scope.start = ($scope.page - 1) * $scope.limit;
    // Thực hiện các hành động khác liên quan đến việc thay đổi trang ở đây
    // Ví dụ: load dữ liệu từ server cho trang mới, v.v.
  };

  $scope.loadAddress = function () {
    $http
      .get(urlAddress)
      .then((resp) => {
        $scope.cities = resp.data;
        console.log($scope.cities);
      })
      .catch((error) => {
        console.log("error", error);
      });
  };

  $scope.url = function (filename) {
    return `${urlImage}/${filename}`;
  };

  $scope.getApartment = function () {
    $http
      .get(urlApartment)
      .then((resp) => {
        $scope.items = resp.data;
        $scope.numberApart = "Apart" + ($scope.items.length + 1);
        console.log($scope.items);
        console.log($scope.numberApart);
        $scope.totalPage = Math.ceil($scope.items.length / $scope.limit);
        $scope.numberOfPage = Array.from(Array($scope.totalPage).keys());
      })
      .catch((error) => {
        console.log("error", error);
      });
  };

  $scope.reset = function () {
    $scope.form = {
      createdate: new Date(),
    };
  };

  $scope.create = function () {
    // Tạo một đối tượng Apartment

    var apartmentData = {
      id: $scope.numberApart,
      content: $scope.form.content,
      apartmentType: $scope.form.apartmentType,
      price: $scope.form.price,
      address: $scope.form.address,
      acreage: $scope.form.acreage,
      description: $scope.form.description,
      status: $scope.form.status,
      ward: $scope.selectedWardId,
      district: $scope.selectedDistrictId,
      city: $scope.selectedCityId,
    };

    console.log(apartmentData);

    // Gửi request lên server bằng phương thức POST
    $http
      .post(urlApartment, apartmentData)
      .then(function (resp) {
        console.log(resp.data);
        $scope.items.push(resp.data);
        var images = $scope.currentfiles.map(function (fileName) {
          return {
            imageData: fileName,
            apartment: resp.data, // assume that resp.data contains the created apartment
          };
        });
        $http
          .post(urlImage + "/save", images)
          .then(function (resp) {
            console.log("Images saved");
          })
          .catch(function (error) {
            console.log("Error saving images", error);
          });
        $scope.reset();
        alert("Thêm mới sản phẩm thành công");
      })
      .catch(function (error) {
        console.log("error", error);
        alert("Thêm mới sản phẩm thất bại");
      });
  };

  // Hiển thị lên form
  $scope.edit = function (item) {
    $scope.form = angular.copy(item);
    $scope.selectedCityId = item.city;
    $scope.updateDistricts();
    $scope.selectedDistrictId = item.district;
    $scope.updateWards();
    $scope.selectedWardId = item.ward;
    
    
    $http
      .get(urlImage + "/" + $scope.form.id)
      .then(function (resp) {
		$scope.currentfiles = [];
        $scope.currentfiles = resp.data;
        console.log($scope.currentfiles);
      })
      .catch(function (error) {
        console.log("Errors", error);
      });
    console.log(item);
  };


  $scope.getTypeApart = function () {
    $http
      .get(urlTypeApart)
      .then((resp) => {
        $scope.cates = resp.data;
        console.log($scope.cates);
      })
      .catch((error) => {
        console.log("error", error);
      });
  };

  $scope.loadImages = function () {
    $http
      .get(urlImage)
      .then((resp) => {
        $scope.filenames = resp.data;
      })
      .catch((error) => {
        console.log("Errors", error);
      });
  };

  // Call loadImages function when controller initializes
  $scope.loadImages();

  // Function to upload images
  $scope.upload = function (files) {
    var formData = new FormData();
    for (var i = 0; i < files.length; i++) {
      formData.append("files", files[i]);
    }

    $http
      .post(urlImage, formData, {
        transformRequest: angular.identity,
        headers: { "Content-Type": undefined },
      })
      .then((resp) => {
        // Update filenames array with the uploaded files
        console.log(files);
        console.log(resp.data);
        $scope.filenames = resp.data;
        $scope.currentfiles = $scope.currentfiles.concat($scope.filenames);
        console.log($scope.filenames);
      })
      .catch((error) => {
        console.log("Errors", error);
      });
  };

  // Function to delete image
  $scope.deleteImage = function (filename) {
    $http
      .delete(`${urlImage}/${filename}`)
      .then((resp) => {
        // Reload images after deleting
        $scope.loadImages();
      })
      .catch((error) => {
        console.log("Errors", error);
      });
  };

  $scope.updateDistricts = function () {
    const city = $scope.cities.find(
      (city) => city.Id === $scope.selectedCityId
    );
    $scope.districts = city ? city.Districts : [];
    $scope.selectedDistrictId = "";
    $scope.wards = [];
    $scope.selectedWardId = "";
  };

  $scope.updateWards = function () {
    const selectedDistrict = $scope.districts.find(
      (district) => district.Id === $scope.selectedDistrictId
    );
    $scope.wards = selectedDistrict ? selectedDistrict.Wards : [];
    $scope.selectedWardId = "";
  };

  $scope.excelData = [];

  $scope.readExcel = function (files) {
    const file = files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
      const data = e.target.result;
      const workbook = XLSX.read(data, { type: "binary" });
      const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
      const excelRows = XLSX.utils.sheet_to_row_object_array(firstSheet);

      // Gán dữ liệu vào biến tạm
      $scope.excelData = excelRows.map((row) => ({
        id: row["id"],
        content: row["content"],
        ward: row["ward"],
        district: row["district"],
        city: row["city"],
        address: row["address"],
        price: row["price"],
        acreage: row["acreage"],
        description: row["description"],
        status: row["status"],
        apartmentType: row["apartmentType"],
      }));

      $scope.$apply(); // Cập nhật scope
      console.log($scope.excelData);
    };

    reader.onerror = function (ex) {
      console.log(ex);
    };

    reader.readAsBinaryString(file);
  };

  $scope.uploadFile = function () {
    if ($scope.excelData.length) {
      $http
        .post(urlApartment + "/bulk-upload", $scope.excelData)
        .then(function (response) {
          alert("File uploaded and processed successfully!");
        })
        .catch(function (error) {
          console.log("Error uploading file", error);
        });
    } else {
      alert("Please select a valid Excel file first.");
    }
  };

  $scope.loadAddress();
  $scope.loadImages();
  $scope.getApartment();
  $scope.getTypeApart();
});
app.filter('removeBrackets', function() {
    return function(input) {
        // Kiểm tra xem input có phải là mảng không
        if (Array.isArray(input)) {
            // Trả về chuỗi đầu tiên từ mảng
            return input[0];
        }
        // Nếu input không phải là mảng, trả về chính nó
        return input;
    };
});
