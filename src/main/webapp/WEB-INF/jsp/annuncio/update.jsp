<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!doctype html>
<html lang="it" class="h-100" >
	 <head>
	 
	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
	 	 <style>
		    .error_field {
		        color: red; 
		    }
		</style>
	   
	   <title>Aggiorna Annuncio</title>
	 </head>
	   <body class="d-flex flex-column h-100">
	   
	   		<!-- Fixed navbar -->
	   		<jsp:include page="../navbar.jsp"></jsp:include>
	    
			
			<!-- Begin page content -->
			<main class="flex-shrink-0">
			  <div class="container">
			  
			  		<%-- se l'attributo in request ha errori --%>
					<spring:hasBindErrors  name="insert_annuncio_attr">
						<%-- alert errori --%>
						<div class="alert alert-danger " role="alert">
							Attenzione!! Sono presenti errori di validazione
						</div>
					</spring:hasBindErrors>
			  
			  		<div class="alert alert-danger alert-dismissible fade show ${errorMessage==null?'d-none':'' }" role="alert">
					  ${errorMessage}
					  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
					</div>
			  
			  <div class='card'>
				    <div class='card-header'>
				        <h5>Crea nuovo annuncio</h5> 
				    </div>
				    <div class='card-body'>
		
							<h6 class="card-title">I campi con <span class="text-danger">*</span> sono obbligatori</h6>
		
		
							<form:form modelAttribute="toUpdate_annuncio_attr" method="post" action="${pageContext.request.contextPath }/annuncio/update" novalidate="novalidate" class="row g-3">
					
							
								<div class="col-md-6">
									<label for="testoAnnuncio" class="form-label">Testo Annuncio <span class="text-danger">*</span></label>
									<spring:bind path="testoAnnuncio">
										<input type="text" name="testoAnnuncio" id="testoAnnuncio" class="form-control ${status.error ? 'is-invalid' : ''}" placeholder="Inserire il contenuto sell'annuncio" value="${toUpdate_annuncio_attr.testoAnnuncio }" required>
									</spring:bind>
									<form:errors  path="testoAnnuncio" cssClass="error_field" />
								</div>
								
								<div class="col-md-6">
									<label for="prezzo" class="form-label">Prezzo <span class="text-danger">*</span></label>
									<spring:bind path="prezzo">
										<input type="text" name="prezzo" id="prezzo" class="form-control ${status.error ? 'is-invalid' : ''}" placeholder="Inserire il prezzo" value="${toUpdate_annuncio_attr.prezzo}" required>
									</spring:bind>
									<form:errors  path="prezzo" cssClass="error_field" />
								</div>
																
								<%--  checkbox ruoli 	--%>
								<%-- facendolo con i tag di spring purtroppo viene un po' spaginato quindi aggiungo class 'a mano'	--%>
								<div class="col-md-6 form-check" id="categorieDivId">
									<p>Categorie:</p>
									<form:checkboxes itemValue="id" itemLabel="codice"  element="div class='form-check'" items="${categorie_tutte_attr}" path="categorieIds" />
								</div>
								<script>
									$(document).ready(function(){
										
										$("#categorieDivId :input").each(function () {
											$(this).addClass('form-check-input'); 
										});
										$("#categorieDivId label").each(function () {
											$(this).addClass('form-check-label'); 
										});
										
									});
								</script>
								<%-- fine checkbox ruoli 	--%>
								<spring:bind path="utenteInserimento">
									<input type="hidden" name="utenteInserimento" id="utenteInserimento" value="${ toUpdate_annuncio_attr.utenteInserimento}" required>
								</spring:bind>
								<spring:bind path="aperto">
									<input type="hidden" name="aperto" id="aperto" value="${ toUpdate_annuncio_attr.aperto}" required>
								</spring:bind>
								<spring:bind path="id">
									<input type="hidden" name="id" id="id" value="${ toUpdate_annuncio_attr.id}" required>
								</spring:bind>
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
			
			<!-- Footer -->
			<jsp:include page="../footer.jsp" />
	  </body>
</html>