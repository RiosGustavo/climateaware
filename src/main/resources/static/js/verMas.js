// Cantidad inicial de publicaciones visibles
var cantidadInicial = 4;

// Obtener todas las publicaciones y ocultar las que excedan la cantidad inicial
var publicaciones = document.querySelectorAll('.campanas-list');
for (var i = cantidadInicial; i < publicaciones.length; i++) {
  publicaciones[i].style.display = 'none';
}

// Manejar el evento clic del botón "Más"
var btnCargarMas = document.getElementById('btn-cargar-mas');
btnCargarMas.addEventListener('click', function() {
  // Mostrar las siguientes publicaciones ocultas
  for (var i = cantidadInicial; i < cantidadInicial + 10 && i < publicaciones.length; i++) {
    publicaciones[i].style.display = 'block';
  }
  // Incrementar la cantidad inicial para la próxima carga
  cantidadInicial += 4;
  // Ocultar el botón si no quedan más publicaciones por mostrar
  if (cantidadInicial >= publicaciones.length) {
    btnCargarMas.style.display = 'none';
  }
});
  