package com.salesianostriana.dam.springapimiarma.security;

import com.salesianostriana.dam.springapimiarma.model.Vivienda;
import com.salesianostriana.dam.springapimiarma.security.jwt.JwtProvider;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.security.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtUserConverter jwtUserConverter;

    @Operation(summary = "Se loguea un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "El usuario inicia sesión correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Hay un error en los datos",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           loginDto.getEmail(),
                           loginDto.getPassword()
                   )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jwtUserConverter.convertUserToJwtUserResponse(user, jwt));
    }

    @GetMapping("/me")
    public ResponseEntity<?> quienSoy(@AuthenticationPrincipal UserEntity user){
        return ResponseEntity.ok(jwtUserConverter.convertUserToJwtUserResponse(user, null));
    }
}
