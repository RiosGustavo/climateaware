package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.repositorios.ImagenRepositorio;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws Exception {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Imagen actualizar(MultipartFile archivo, String id) throws Exception {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();

                if (id != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(id);

                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }

                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Imagen obtenerImagenPorDefecto() throws IOException {
        // Ruta relativa a la carpeta 'static/img' donde se encuentra la imagen por defecto
        String rutaImagenPorDefecto = "static/img/logo.png"; // Actualiza con el nombre y extensión de tu imagen por defecto

        Resource resource = new ClassPathResource(rutaImagenPorDefecto);
        byte[] contenido = Files.readAllBytes(resource.getFile().toPath());

        Imagen imagen = new Imagen();
        imagen.setMime("image/png"); // Actualiza con el MIME type correspondiente a tu imagen por defecto
        imagen.setNombre("logo.png"); // Actualiza con el nombre de tu imagen por defecto
        imagen.setContenido(contenido);

        return imagenRepositorio.save(imagen);
    }

}
