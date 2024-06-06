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
  $scope.filesImage = [];
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
        $scope.filesImage = resp.data;
        console.log($scope.filesImage);
        $scope.currentfiles = [];
        $scope.currentfiles = $scope.currentfiles.concat(
          $scope.filesImage.imageData
        );
        console.log($scope.currentfiles);
      })
      .catch(function (error) {
        console.log("Errors", error);
      });
    console.log(item);
  };

  $scope.update = function () {
    // Tạo một đối tượng Apartment
    var item = angular.copy($scope.form);
    item.ward = $scope.selectedWardId;
    item.district = $scope.selectedDistrictId;
    item.city = $scope.selectedCityId;
   
    console.log(item);
    // Gửi request lên server bằng phương thức POST
    $http
      .put(urlApartment + `/${item.id}`, item)
      .then(function (resp) {
        var index = $scope.items.findIndex((a) => a.id == item.id);
        $scope.items[index] = item;
        var images = $scope.currentfiles.map(function (fileName) {
          return {
            imageData: fileName,
            apartment: item, // assume that resp.data contains the created apartment
          };
          
        });
        if (!$scope.currentfiles){
          $http
            .post(urlImage + "/save", images)
            .then(function (resp) {
              console.log("Images saved");
            })
            .catch(function (error) {
              console.log("Error saving images", error);
            });
        }else{
            $http
              .put(urlImage + "/update", images)
              .then(function (resp) {
                $scope.currentfiles = [];
                for(let i=0;i < 4;i++){
                  $scope.currentfiles = $scope.currentfiles.push(
                    images[i].fileName
                  );
                }
                console.log("Images updated");
              })
              .catch(function (error) {
                console.log("Error updated images", error);
              });     
        }
          
        $scope.reset();
        alert("Thêm mới sản phẩm thành công");
      })
      .catch(function (error) {
        console.log("error", error);
        alert("Thêm mới sản phẩm thất bại");
      });
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
        console.log(files + "???");
        console.log(resp.data + "????");
        $scope.filenames = resp.data;
        console.log("filename", $scope.filenames);
        $scope.currentfiles = [];
        $scope.currentfiles = $scope.currentfiles.concat($scope.filenames);   
        console.log("currentfiles",$scope.currentfiles);
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

  // Function to load JSON data from URL
  async function loadJsonData(url) {
    const response = await fetch(url);
    return await response.json();
  }

  $scope.import = async function (files) {
    var reader = new FileReader();
    const cityData = await loadJsonData(urlAddress);
    reader.onloadend = async () => {
      var workbook = new ExcelJS.Workbook();
      await workbook.xlsx.load(reader.result);
      const worksheet = workbook.getWorksheet("Sheet1");
      worksheet.eachRow((row, index) => {
        if (index > 1) {
          const cityName = row.getCell(12).value;
          const districtName = row.getCell(11).value;
          const wardName = row.getCell(10).value;

          // Find city ID
          const city = cityData.find((city) => city.Name === cityName);
          if (!city) {
            console.error(`City not found: ${cityName}`);
            return;
          }

          // Find district ID
          const district = city.Districts.find(
            (district) => district.Name === districtName
          );
          if (!district) {
            console.error(
              `District not found in city ${cityName}: ${districtName}`
            );
            return;
          }

          // Find ward ID
          const ward = district.Wards.find((ward) => ward.Name === wardName);
          if (!ward) {
            console.error(
              `Ward not found in district ${districtName}: ${wardName}`
            );
            return;
          }

          // Construct the apartment type correctly
          const apartType = {
            id: row.getCell(3).value,
            name: row.getCell(4).value,
          };

          let apartment = {
            id: row.getCell(1).value,
            content: row.getCell(2).value,
            apartmentType: apartType, // Gửi dưới dạng đối tượng
            price: row.getCell(5).value,
            address: row.getCell(6).value,
            acreage: row.getCell(7).value,
            description: row.getCell(8).value,
            status: row.getCell(9).value,
            city: city.Id,
            district: district.Id,
            ward: ward.Id,
          };

          $http
            .post(urlApartment, apartment)
            .then(function (resp) {
              console.log(resp.data);
              $scope.items.push(resp.data);
              $scope.reset();
              alert("Thêm mới sản phẩm thành công");
            })
            .catch(function (error) {
              console.log("Error: ", error);
            });
        }
      });
    };
    reader.readAsArrayBuffer(files[0]);
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
