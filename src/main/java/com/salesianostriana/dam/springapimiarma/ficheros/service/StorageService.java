package com.salesianostriana.dam.springapimiarma.ficheros.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    String storeAndResizeAvatar(MultipartFile file);

    String storeAndResizePostImage(MultipartFile file);

    String storeAndResizePostVideo(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteFile(String filename);

    void deleteAll();
}
