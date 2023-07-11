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

// Función para establecer el tema en el localStorage
const establecerTema = (tema) => {
  localStorage.setItem('tema', tema);
}

// Función para obtener el tema del localStorage
const obtenerTema = () => {
  return localStorage.getItem('tema');
}

// Función para aplicar el tema oscuro
const temaOscuro = () => {
  document.querySelector("html").setAttribute("data-bs-theme", "dark");
  document.querySelector("#dl-icon").setAttribute("class", "bi bi-sun-fill");
}

// Función para aplicar el tema claro
const temaClaro = () => {
  document.querySelector("html").setAttribute("data-bs-theme", "light");
  document.querySelector("#dl-icon").setAttribute("class", "bi bi-moon-fill");
}

// Función para cambiar el tema
const cambiarTema = () => {
  if (obtenerTema() === "light") {
    temaOscuro();
    establecerTema("dark");
  } else {
    temaClaro();
    establecerTema("light");
  }
}

// Verificar el tema guardado y aplicarlo al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  const temaGuardado = obtenerTema();
  if (temaGuardado === "dark") {
    temaOscuro();
  } else {
    temaClaro();
  }
});

