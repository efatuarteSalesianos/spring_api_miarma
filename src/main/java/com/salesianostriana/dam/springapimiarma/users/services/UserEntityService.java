package com.salesianostriana.dam.springapimiarma.users.services;

import com.salesianostriana.dam.springapimiarma.errores.excepciones.BadRequestException;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.PrivateAccountException;
import com.salesianostriana.dam.springapimiarma.errores.excepciones.SingleEntityNotFoundException;
import com.salesianostriana.dam.springapimiarma.ficheros.errores.StorageException;
import com.salesianostriana.dam.springapimiarma.ficheros.service.StorageService;
import com.salesianostriana.dam.springapimiarma.services.base.BaseService;
import com.salesianostriana.dam.springapimiarma.users.dto.CreateUserDto;
import com.salesianostriana.dam.springapimiarma.users.dto.GetUserDto;
import com.salesianostriana.dam.springapimiarma.users.dto.SaveUserDto;
import com.salesianostriana.dam.springapimiarma.users.dto.UserDtoConverter;
import com.salesianostriana.dam.springapimiarma.users.model.Rol;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.salesianostriana.dam.springapimiarma.users.model.ProfileType.PUBLIC;

@Service("userDetailService")
@RequiredArgsConstructor
public class UserEntityService extends BaseService<UserEntity, UUID, UserEntityRepository> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserDtoConverter dtoConverter;
    private final StorageService storageService;
    private final List<String> imagesType = new ArrayList<>(List.of("image/jpeg", "image/png", "image/jpg", "image/svg"));

    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.repositorio.findFirstByNickname(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick + " no encontrado"));
    }

    public UserEntity save(CreateUserDto newUser, MultipartFile file) throws BadRequestException {

        if(imagesType.contains(file.getContentType())) {

            String filename = storageService.storeAndResizeAvatar(file);
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(filename)
                    .toUriString();

            if(newUser.getPassword().contentEquals(newUser.getVerifyPassword())) {
                UserEntity userEntity = UserEntity.builder()
                        .full_name(newUser.getFull_name())
                        .fecha_nacimiento(newUser.getFecha_nacimiento())
                        .direccion(newUser.getDireccion())
                        .email(newUser.getEmail())
                        .nickname(newUser.getNickname())
                        .avatar(uri)
                        .rol(Rol.USER)
                        .telefono(newUser.getTelefono())
                        .privacidad(newUser.getPrivacidad())
                        .password(passwordEncoder.encode(newUser.getPassword()))
                        .build();

                return save(userEntity);
            } else {
                throw new BadRequestException("Las contraseñas no coinciden.");
            }
        } else {
            throw new StorageException("No puedes subir un avatar con esa extensión");
        }

    }

    public GetUserDto findUserProfileById(UUID id, UserEntity user) throws PrivateAccountException {
        Optional<UserEntity> encontrado = repositorio.findById(id);
        if (encontrado.isEmpty())
            throw new SingleEntityNotFoundException(id.toString(), UserEntity.class);
        else {
            if (encontrado.get().getPrivacidad() == PUBLIC || encontrado.get().getSeguidores_list().contains(user))
                return dtoConverter.convertUserEntityToGetUserDto(encontrado.get());
            else
                throw new PrivateAccountException("No se pudo mostrar el perfil del usuario que buscas, ya que su cuenta es privada y no te encuentras entre sus seguidores.");
        }
    }

    public GetUserDto editProfile(UserEntity logueado, SaveUserDto user, MultipartFile file) {

        if (!file.isEmpty()) {
            if (imagesType.contains(file.getContentType())) {

                String filename = storageService.storeAndResizeAvatar(file);
                storageService.deleteFile(logueado.getAvatar().substring(logueado.getAvatar().lastIndexOf("/") + 1));

                String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(filename)
                        .toUriString();

                logueado.setFull_name(user.getFull_name());
                logueado.setFecha_nacimiento(user.getFecha_nacimiento());
                logueado.setDireccion(user.getDireccion());
                logueado.setEmail(user.getEmail());
                logueado.setNickname(user.getNickname());
                logueado.setAvatar(uri);
                logueado.setTelefono(user.getTelefono());
                logueado.setPrivacidad(user.getPrivacidad());

                return dtoConverter.convertUserEntityToGetUserDto(save(logueado));
            }
            else
                return null;
        } else {

            logueado.setFull_name(user.getFull_name());
            logueado.setFecha_nacimiento(user.getFecha_nacimiento());
            logueado.setDireccion(user.getDireccion());
            logueado.setEmail(user.getEmail());
            logueado.setNickname(user.getNickname());
            logueado.setAvatar(user.getAvatar());
            logueado.setTelefono(user.getAvatar());
            logueado.setPrivacidad(user.getPrivacidad());

            return dtoConverter.convertUserEntityToGetUserDto(save(logueado));
        }

    }
}
