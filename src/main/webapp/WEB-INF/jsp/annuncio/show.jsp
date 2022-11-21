<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
	   
	   <title>Dettaaglio annuncio</title>
	 </head>
	   <body class="d-flex flex-column h-100">
	   
	   		<!-- Fixed navbar -->
	   		<jsp:include page="../navbar.jsp"></jsp:include>
	    
			
			<!-- Begin page content -->
			<main class="flex-shrink-0">
			  <div class="container">
			  
			  		<div class="alert alert-danger alert-dismissible fade show ${errorMessage==null?'d-none':'' }" role="alert">
					  ${errorMessage}
					  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
					</div>
			  
			  <div class='card'>
				    <div class='card-header'>
				        <h5>Visualizza dattaglio Annuncio</h5> 
				    </div>
				    <div class='card-body'>
				    
						<dl class="row">
						  <dt class="col-sm-3 text-right">Id:</dt>
						  <dd class="col-sm-9">${show_annuncio_attr.id}</dd>
			    		</dl>
			    		
			    		<dl class="row">
						  <dt class="col-sm-3 text-right">Testo annunncio</dt>
						  <dd class="col-sm-9">${show_annuncio_attr.testoAnnuncio}</dd>
			    		</dl>
			    		
			    		<dl class="row">
						  <dt class="col-sm-3 text-right">Prezzo</dt>
						  <dd class="col-sm-9">${show_annuncio_attr.prezzo}</dd>
			    		</dl>
			    		
			    		<dl class="row">
						  <dt class="col-sm-3 text-right">Ancora aperto:</dt>
						  <dd class="col-sm-9"> ${show_annuncio_attr.aperto}</dd>
			    		</dl>
							
  						<dl class="row">
						  <dt class="col-sm-3 text-right">Postato in data :</dt>
						  <dd class="col-sm-9"><fmt:formatDate type = "date" value = "${show_annuncio_attr.data}" /></dd>
			    		</dl>
			    		
			    		<dl class="row">
						  <dt class="col-sm-3 text-right">Postato da:</dt>
						  <dd class="col-sm-9">${show_annuncio_attr.utenteInserimento}</dd>
			    		</dl>
			    		
			    		<dl class="row">
						  <dt class="col-sm-3 text-right">Categorie dell'annuncio:</dt>
						  <dd class="col-sm-12"></dd>
						  <c:forEach items="${categorie_annuncio_attr}" var="idCategoria">
						  	<dd class="col-sm-12">${idCategoria.descrizione}</dd>
						  </c:forEach>
			    		</dl>
				    	
				    	<div class='card-footer'>
			     		   	<a href="${pageContext.request.contextPath }/annuncio/open" class='btn btn-outline-secondary' style='width:80px'>
			            		<i class='fa fa-chevron-left'></i> Back
			        		</a>
			        		<c:if test="${ show_annuncio_attr.aperto}">
			        			<a id="acquistaLink_#_${show_annuncio_attr.id }" class="btn btn-outline-success link-for-modal" data-bs-toggle="modal" data-bs-target="#confirmOperationModal"  >Acquista</a>
			        		</c:if>
			        		
			        		
			    		</div>
					<!-- end card-body -->			   
				    </div>
				<!-- end card -->
				</div>		
					  
			    
			  <!-- end container -->  
			  </div>
			  
			</main>
			
			<!-- Footer -->
			<jsp:include page="../footer.jsp" />
			
			<!-- Modal -->
			<div class="modal fade" id="confirmOperationModal" tabindex="-1"  aria-labelledby="confirmOperationModalLabel"
			    aria-hidden="true">
			    <div class="modal-dialog" >
			        <div class="modal-content">
	    		        <div class="modal-header">
	    		            <h5 class="modal-title" id="confirmOperationModalLabel">Conferma Operazione</h5>
	        		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      				 </div>
	      				 <div class="modal-body">
	      		        	Continuare con l'operazione?
	      		     	 </div>
	        			 <form method="post" action="/annuncio/buy" >
		  	          <div class="modal-footer">
		            	<input type="hidden" name="callerPage" id="callerPage" value="/annuncio/show/${idAnnuncio}">
		            	<input type="hidden" name="currentUser" id="currentUser" value="<sec:authentication property="name"/>">
		            	<input type="hidden" name="idAnnuncioDaComprare" id="idAnnuncioDaComprare">
		                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Chiudi</button>
		                <input type="submit" value="Continua"  class="btn btn-primary">
		            </div>
	            </form>
	        </div>
	    </div>
	</div>
	<!-- end Modal -->
	<script type="text/javascript">
		<!-- aggancio evento click al conferma del modal  -->
		$(".link-for-modal").click(function(){
			<!-- mi prendo il numero che poi sarà l'id. Il 15 è perché 'cquistaLink_#_' è appunto lungo 15  -->
			var callerId = $(this).attr('id').substring(15);
			<!-- imposto nell'hidden del modal l'id da postare alla servlet -->
			$('#idAnnuncioDaComprare').val(callerId);
		});
	</script>
	  </body>
</html>