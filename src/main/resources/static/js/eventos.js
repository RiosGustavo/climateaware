window.addEventListener('scroll', function() {
    var elementos = document.querySelectorAll('.campanaPost');
    var alturaVentana = window.innerHeight;
  
    elementos.forEach(function(elemento) {
      var posicionElemento = elemento.getBoundingClientRect().top;
  
      if (posicionElemento - alturaVentana <= 0) {
        elemento.classList.add('mover-derecha-izquierda');
      } else {
        elemento.classList.remove('mover-derecha-izquierda');
      }
  
    });
  });
 

  
  
  
  
  