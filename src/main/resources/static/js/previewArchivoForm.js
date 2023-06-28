// Obtén el elemento de la imagen de vista previa y el campo de entrada de archivo
let imagePreview = document.getElementById('image-preview');
let fileInput = document.getElementById('file-input');

// Agrega un evento de cambio al campo de entrada de archivo
fileInput.addEventListener('change', function(event) {
  // Obtén el archivo seleccionado del campo de entrada de archivo
  let file = event.target.files[0];

  // Verifica si se seleccionó un archivo
    if (file) {
    // Crea un objeto de URL para cargar la imagen seleccionada
    let imageURL = URL.createObjectURL(file);

    // Establece la URL de la imagen como la fuente de la vista previa
    imagePreview.src = imageURL;
  }
});
