package com.salesianostriana.dam.springapimiarma.ficheros.utils;

import com.salesianostriana.dam.springapimiarma.ficheros.errores.StorageException;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@AllArgsConstructor
@Builder
public class FileCompressor {

    static IVCompressor compressor = new IVCompressor();

    public byte[] compressAvatar(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        BufferedImage image = null;

        try {

            image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

            BufferedImage imageResize = Scalr.resize(image, 128);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            ImageIO.write(imageResize, extension, os);

            return os.toByteArray();

        } catch (IOException e) {
            throw new StorageException("Error al comprimir la imagen " + file.getOriginalFilename(), e);
        }
    }

    public byte[] compressPostImage(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        BufferedImage image = null;

        try {

            image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

            BufferedImage imageResize = Scalr.resize(image, 1024);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            ImageIO.write(imageResize, extension, os);

            return os.toByteArray();

        } catch (IOException e) {
            throw new StorageException("Error al comprimir la imagen " + file.getOriginalFilename(), e);
        }
    }

    public byte[] compressPostVideo(MultipartFile file) {
        try {
            return compressor.reduceVideoSize(file.getBytes(), VideoFormats.MP4, ResizeResolution.R720P);
        } catch (IOException | VideoException e) {
            throw new StorageException("Error al comprimir el video " + file.getOriginalFilename(), e);
        }
    }
}
