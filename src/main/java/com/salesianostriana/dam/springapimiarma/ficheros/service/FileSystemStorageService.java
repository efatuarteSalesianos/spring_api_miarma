package com.salesianostriana.dam.springapimiarma.ficheros.service;

import com.salesianostriana.dam.springapimiarma.ficheros.errores.FileNotFoundException;
import com.salesianostriana.dam.springapimiarma.ficheros.errores.StorageException;
import com.salesianostriana.dam.springapimiarma.ficheros.utils.FileCompressor;
import com.salesianostriana.dam.springapimiarma.ficheros.utils.MediaTypeUrlResource;
import com.salesianostriana.dam.springapimiarma.ficheros.utils.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    FileCompressor compressor;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String newFilename = "";
        String extension = StringUtils.getFilenameExtension(filename);

        try {
            if (file.isEmpty())
                throw new StorageException("El fichero subido está vacío");

            newFilename = filename;
            while (Files.exists(rootLocation.resolve(newFilename))) {
                String name = newFilename.replace("." + extension, "");

                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length() - 6);

                newFilename = name + "_" + suffix + "." + extension;

            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(newFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }


        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, ex);
        }

        return newFilename;

    }

    public String storeAndResizeAvatar(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        String newFilename = "";

        try {
            byte[] byteImage = compressor.compressAvatar(file);

            File image = new File(file.getOriginalFilename());

            newFilename = image.getName();

            while (Files.exists(rootLocation.resolve(newFilename))) {
                String name = newFilename.replace("." + extension, "");

                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length() - 6);

                newFilename = name + "_" + suffix + "." + extension;

            }

            FileUtils.writeByteArrayToFile(rootLocation.resolve(newFilename).toFile(), byteImage);
            } catch (IOException ex) {
                throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, ex);
            }
        return newFilename;
    }

    public String storeAndResizePostImage(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        String newFilename = "";

        try {
            byte[] byteImage = compressor.compressPostImage(file);

            File image = new File(file.getOriginalFilename());

            newFilename = image.getName();

            while (Files.exists(rootLocation.resolve(newFilename))) {
                String name = newFilename.replace("." + extension, "");

                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length() - 6);

                newFilename = name + "_" + suffix + "." + extension;

            }

            FileUtils.writeByteArrayToFile(rootLocation.resolve(newFilename).toFile(), byteImage);
        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, ex);
        }
        return newFilename;
    }

    public String storeAndResizePostVideo(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        String newFilename = "";

        try {
            byte[] byteImage = compressor.compressPostVideo(file);

            File image = new File(file.getOriginalFilename());

            newFilename = image.getName();

            while (Files.exists(rootLocation.resolve(newFilename))) {
                String name = newFilename.replace("." + extension, "");

                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length() - 6);

                newFilename = name + "_" + suffix + "." + extension;

            }

            FileUtils.writeByteArrayToFile(rootLocation.resolve(newFilename).toFile(), byteImage);
        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, ex);
        }
        return newFilename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Error al leer los ficheros almacenados", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {

        try {
            Path file = load(filename);
            MediaTypeUrlResource resource = new MediaTypeUrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteFile(String filename) {
        Path file = load(filename);
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException("No se pudo borrar el fichero: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}