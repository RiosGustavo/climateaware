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

//Boton para volver arriba

$(document).ready(function(){

	$('#ir-arriba').click(function(){
		$('body, html').animate({
			scrollTop: '0px'
		}, 300);
	});

	$(window).scroll(function(){
		if( $(this).scrollTop() > 0 ){
			$('#ir-arriba').slideDown(300);
		} else {
			$('#ir-arriba').slideUp(300);
		}
	});

});

//tema oscuro

const temaOscuro = () => {
document.querySelector("html").setAttribute("data-bs-theme", "dark");
document.querySelector("#d1-icon").setAttribute("class", "bi bi-brightness-high-fill");

}
const temaClaro = () => {
document.querySelector("html").setAttribute("data-bs-theme", "light");
document.querySelector("#d1-icon").setAttribute("class", "bi bi-moon-fill");
}

const cambiarTema=()=>{
document.querySelector("html").getAttribute("data-bs-theme")==="light"?
temaOscuro(): temaClaro();
}





