<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!doctype html>
<html lang="it">
	<head>
	  <meta charset="utf-8">
		<title>Cambia Password</title>
	
		<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
 		<style>
		    .error_field {
		        color: red; 
		    }
		</style>
	
		 <!-- Custom styles for login -->
	    <link href="${pageContext.request.contextPath}/assets/css/signin.css" rel="stylesheet">
	</head>
	
	<body class="d-flex flex-column h-100" >
		
		<main class="flex-shrink-0">
			  <div class="container mb-5">
			  
			  <div class="form-signin" style="padding:2px ; background-color:gray ">
				    <div class='card-header' style="background-color: #0d6efd">
				        <h5 class="h3 mb-3 fw-normal" style="color: white">Cambia Password</h5> 
				    </div>
				    <div class='card-body' style="background-color: white">
		
							<h6 class="card-title">I campi con <span class="text-danger">*</span> sono obbligatori</h6>
		
		
							<form:form modelAttribute="change_password_utente_attr" method="post" action="${pageContext.request.contextPath}/account/eseguicambio" novalidate="novalidate" class="row g-3">
								
								<div class="col-md-12">
									<p class="form-label">Username: <sec:authentication property="name"/></p>
										<input type="hidden" class="form-control ${status.error ? 'is-invalid' : ''}" name="username" id="username" value = "<sec:authentication property="name"/>"  required>
									<form:errors  path="username" cssClass="error_field" />
								</div>
								
								<div class="col-md-12">
									<label for="password" class="form-label">Vecchia Password <span class="text-danger">*</span></label>
										<input type="password" class="form-control ${status.error ? 'is-invalid' : ''}" name="password" id="password" placeholder="Inserire vecchia Password"  required>
									<form:errors  path="password" cssClass="error_field" />
								</div>
								
								<div class="col-md-12">
									<label for="nuovaPassword" class="form-label">Nuova Password <span class="text-danger">*</span></label>
										<input type="password" class="form-control ${status.error ? 'is-invalid' : ''}" name="nuovaPassword" id="nuovaPassword" placeholder="Inserire nuova Password"  required>
									<form:errors  path="nuovaPassword" cssClass="error_field" />
								</div>
								
								<div class="col-md-12">
									<label for="confermaNuovaPassword" class="form-label">Conferma Password <span class="text-danger">*</span></label>
										<input type="password" class="form-control ${status.error ? 'is-invalid' : ''}" name="confermaNuovaPassword" id="confermaNuovaPassword" placeholder="Confermare Password"  required>
									<form:errors  path="confermaNuovaPassword" cssClass="error_field" />
								</div>
								
								<div class="col-12">
									<button type="submit" name="submit" value="submit" id="submit" class="btn btn-primary">Conferma</button>
									<input class="btn btn-outline-warning" type="reset" value="Ripulisci">
								</div>
						</form:form>
  
				    
				    
					<!-- end card-body -->			   
				    </div>
				<!-- end card -->
				</div>		
					  
			    
			  <!-- end container -->  
			  </div>
			  
			</main>
	</body>
</html>