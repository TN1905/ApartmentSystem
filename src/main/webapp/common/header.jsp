
<div class="sub-header">
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8">
				<ul class="info">
					<li><i class="fa fa-envelope"></i> nhom2@fpt.edu.vn</li>
					<li><i class="fa fa-map"></i><s:message code="lo.a.city"/></li>
					<li><a href="?lang=en"><s:message code="lo.a.english"/></a></li>
					<li><a href="?lang=vi"><s:message code="lo.a.vietnam"/></a></li>
					<li><a href="?lang=jp"><s:message code="lo.a.nhatban"/></a></li>
				</ul>
			</div>
			<div class="col-lg-4 col-md-4">
				<ul class="social-links">
					<li><a href="#"><i class="fab fa-facebook"></i></a></li>
					<li><a href="https://x.com/minthu" target="_blank"><i
							class="fab fa-twitter"></i></a></li>
					<li><a href="#"><i class="fab fa-linkedin"></i></a></li>
					<li><a href="#"><i class="fab fa-instagram"></i></a></li>
				</ul>
			</div>
		</div>
	</div>
</div>

<!-- ***** Header Area Start ***** -->
<header class="header-area header-sticky">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<nav class="main-nav">
					<!-- ***** Logo Start ***** -->
					<a href="index.html" class="logo"> <img
						src="../images/anh/tieude.png" alt=""
						style="height: 60px; margin-top: 15px;">
					</a>
					<!-- ***** Logo End ***** -->
					<!-- ***** Menu Start ***** -->
					<ul class="nav">
						<li><a href="/user/index" class="active"><s:message code="lo.a.home"/></a></li>
						<li><a href="/user/listapartment"><s:message code="lo.a.apartment"/></a></li>
						<li><a href="/user/historyrent"><s:message code="lo.a.historyrent"/></a></li>
						<li><a href="contact.html"><s:message code="lo.a.contact"/></a></li>
						<c:choose>
							<c:when test="${not empty currentUser}">
								<li><a href="<c:url value='/user/userInfo'/>"><s:message code="lo.a.hello"/>
										${currentUser.username}</a></li>
								<li><a href="/user/logout"><s:message code="lo.a.logout"/></a></li>
							</c:when>
							<c:otherwise>
								<li><a href="/user/register"><s:message code="lo.a.signup"/></a></li>
								<li><a href="/user/login"><s:message code="lo.a.signin"/></a></li>
							</c:otherwise>
						</c:choose>

					</ul>

					<a class='menu-trigger'> <span>Menu</span>
					</a>
					<!-- ***** Menu End ***** -->
				</nav>
			</div>
		</div>
	</div>
</header>
<!-- ***** Header Area End ***** -->