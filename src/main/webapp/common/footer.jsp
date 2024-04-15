
<footer class="site-footer section-padding">
    <div class="container">
        <div class="row">

            <div class="col-lg-3 col-md-5 col-12 mb-3">
                <h3><a href="index.html" class="custom-link mb-1"><s:message code="lo.a.cheeproom"/></a></h3>

                <p class="text-white"><s:message code="lo.a.since"/></p>
                
                <p class="text-white"><a href="#" target="_parent">Web: nhom2@fpt.edu.vn</a></p>
            </div>

            <div class="col-lg-3 col-md-3 col-12 ms-lg-auto mb-3">
                <h3 class="text-white mb-3"><s:message code="lo.a.store"/></h3>

                <p class="text-white mt-2">
                    <i class="bi-geo-alt"></i>
                    Ho Chi Minh
                </p>
            </div>

            <div class="col-lg-3 col-md-4 col-12 mb-3">
                <h3 class="text-white mb-3"><s:message code="lo.a.contactinfo"/></h3>

                    <p class="text-white mb-1">
                        <i class="bi-telephone me-1"></i>

                        <a href="" class="text-white">
                            090-080-0760
                        </a>
                    </p>

                    <p class="text-white mb-0">
                        <i class="bi-envelope me-1"></i>

                        <a href="#" class="text-white">
                          nhom2@fpt.edu.vn
                        </a>
                    </p>
            </div>

            <div class="col-lg-6 col-md-7 copyright-text-wrap col-12 d-flex flex-wrap align-items-center mt-4 ms-auto">
                <p class="copyright-text mb-0 me-4">Copyright © 2011 - 2024 asm_java5.com.</p>
            </div>

        </div>
    </div>
</footer>
<script src="../js/jquery.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
	<script src="../js/isotope.min.js"></script>
	<script src="../js/owl-carousel.js"></script>
	<script src="../js/counter.js"></script>
	<script src="../js/custom.js"></script>
	<script src="../js/init.js"></script>
	<script>
		function populateDropdown(dropdownId, data) {
			var dropdown = $("#" + dropdownId);
			dropdown.empty();
			dropdown.append('<option value="" selected disabled>'
					+ dropdownId + '</option>');
			$.each(data, function(key, value) {
				dropdown.append('<option value="' + value.Id + '">'
						+ value.Name + '</option>');
			});
		}

		function loadDistricts() {
			console.log("City Value: " + $("#province").val());
			var provinceId = $("#province").val();
			if (provinceId) {
				$
						.getJSON(
								'https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json?fbclid=IwAR0rFIXdhDePPDSI5SeQZarnl_UazlS2REGaXelLWT74AGV9DEpjHlXKGdY',
								function(data) {
									var selectedProvince = data.find(function(
											province) {
										return province.Id === provinceId;
									});
									if (selectedProvince) {
										populateDropdown('district',
												selectedProvince.Districts);
										$("#ward").empty();
										$("#hiddenProvince").val(
												selectedProvince.Name);
										$("#ward")
												.change(
														function() {
															$("#hiddenWard")
																	.val(
																			$(
																					"#ward option:selected")
																					.text());
														});
									}
								}).fail(function() {
							console.error('Error loading districts.');
						});
			} else {
				$("#district").empty();
				$("#ward").empty();
			}
		}

		function loadWards() {
			console.log("City Value: " + $("#province").val());
			console.log("District Value: " + $("#district").val());
			var provinceId = $("#province").val();
			var districtId = $("#district").val();
			if (provinceId && districtId) {
				$
						.getJSON(
								'https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json?fbclid=IwAR0rFIXdhDePPDSI5SeQZarnl_UazlS2REGaXelLWT74AGV9DEpjHlXKGdY',
								function(data) {
									var selectedProvince = data.find(function(
											province) {
										return province.Id === provinceId;
									});
									if (selectedProvince) {
										var selectedDistrict = selectedProvince.Districts
												.find(function(district) {
													return district.Id === districtId;
												});
										if (selectedDistrict) {
											populateDropdown('ward',
													selectedDistrict.Wards);
											$("#hiddenDistrict").val(
													selectedDistrict.Name);

										}
									}
								}).fail(function() {
							console.error('Error loading wards.');
						});
			} else {
				$("#ward").empty();
				$("#hiddenWard").val("");

			}
		}

		$
				.getJSON(
						'https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json?fbclid=IwAR0rFIXdhDePPDSI5SeQZarnl_UazlS2REGaXelLWT74AGV9DEpjHlXKGdY',
						function(data) {
							populateDropdown('province', data);
						}).fail(function() {
					console.error('Error loading provinces.');
				});
	</script>

	<script>
		function updatePreview(inputId, previewId) {
			var input = document.getElementById(inputId);
			var preview = document.getElementById(previewId);

			var file = input.files[0];

			if (file) {
				var reader = new FileReader();

				reader.onload = function(e) {
					preview.src = e.target.result;
				};

				reader.readAsDataURL(file);
			}
		}
	</script>
	<script>
		$(document)
				.ready(
						function() {
							const cityValue = "79";
							const districtValue = "760";
							const wardValue = "26758";

							// Log giá trị để kiểm tra
							console.log("City Value: " + cityValue);
							console.log("District Value: " + districtValue);
							console.log("Ward Value: " + wardValue);

							// Chọn giá trị cho dropdown tỉnh/thành phố
							$("#province").prop('value', cityValue);

							// Gọi hàm loadDistricts và điền dữ liệu trực tiếp từ server
							$
									.getJSON(
											'https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json?fbclid=IwAR0rFIXdhDePPDSI5SeQZarnl_UazlS2REGaXelLWT74AGV9DEpjHlXKGdY',
											function(data) {
												populateDropdown('province',
														data);

												// Điền dữ liệu cho dropdown quận/huyện và phường/xã từ server
												var selectedProvince = data
														.find(function(province) {
															return province.Id === cityValue;
														});
												$("#province").prop('value',
														cityValue);
												if (selectedProvince) {
													populateDropdown(
															'district',
															selectedProvince.Districts);
													$("#hiddenProvince")
															.val(
																	selectedProvince.Name);
													$("#district").prop(
															'value',
															districtValue);

													var selectedDistrict = selectedProvince.Districts
															.find(function(
																	district) {
																return district.Id === districtValue;
															});
													if (selectedDistrict) {
														populateDropdown(
																'ward',
																selectedDistrict.Wards);
														$("#hiddenDistrict")
																.val(
																		selectedDistrict.Name);
														$("#ward").prop(
																'value',
																wardValue);
													}
												}
											})
									.fail(
											function() {
												console
														.error('Error loading provinces.');
											});
						});
	</script>