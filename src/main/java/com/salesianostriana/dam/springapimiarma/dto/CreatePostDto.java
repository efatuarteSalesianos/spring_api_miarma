package com.salesianostriana.dam.springapimiarma.dto;

import com.salesianostriana.dam.springapimiarma.model.PostType;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class CreatePostDto {

    @NotBlank(message = "{post.titulo.blank}")
    @Size(min = 2, max = 32, message = "{post.titulo.size}")
    private String titulo;

    @NotBlank(message = "{post.descripcion.blank}")
    @Lob
    private String descripcion;

    @NotBlank(message = "{post.media.blank}")
    @URL(message = "{post.media.url}")
    private String media;

    @NotBlank(message = "{post.tipo.blank}")
    private PostType tipo;
}
