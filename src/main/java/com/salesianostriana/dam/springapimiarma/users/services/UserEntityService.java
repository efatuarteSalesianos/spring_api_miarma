package com.salesianostriana.dam.springapimiarma.users.services;

import com.salesianostriana.dam.springapimiarma.ficheros.service.FileSystemStorageService;
import com.salesianostriana.dam.springapimiarma.ficheros.service.StorageService;
import com.salesianostriana.dam.springapimiarma.services.base.BaseService;
import com.salesianostriana.dam.springapimiarma.users.dto.CreateUserDto;
import com.salesianostriana.dam.springapimiarma.users.dto.GetFollowDto;
import com.salesianostriana.dam.springapimiarma.users.dto.UserDtoConverter;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("userDetailService")
@RequiredArgsConstructor
public class UserEntityService extends BaseService<UserEntity, UUID, UserEntityRepository> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final FileSystemStorageService fileSystemStorageService;
    private final UserDtoConverter dtoConverter;
    private final UserEntityRepository userEntityRepository;
    private final StorageService storageService;

    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.repositorio.findFirstByNickname(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick + " no encontrado"));
    }

    public UserEntity save(CreateUserDto newUser, MultipartFile file) {

        String filename = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();

        if(newUser.getPassword().contentEquals(newUser.getVerifyPassword())) {
            UserEntity userEntity = UserEntity.builder()
                    .email(newUser.getEmail())
                    .full_name(newUser.getFull_name())
                    .avatar(uri)
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .build();
            return save(userEntity);
        }
        else {
            return null;
        }
    }

    public List<GetFollowDto> findAllPeticionesSeguimiento() {
        return userEntityRepository.findByPeticiones_seguimiento().stream().map(dtoConverter::convertFollowToGetFollowDto).toList();
    }
}
