package com.salesianostriana.dam.springapimiarma.users.services;

import com.salesianostriana.dam.springapimiarma.model.Inmobiliaria;
import com.salesianostriana.dam.springapimiarma.services.InmobiliariaService;
import com.salesianostriana.dam.springapimiarma.services.base.BaseService;
import com.salesianostriana.dam.springapimiarma.users.dto.CreateUserDto;
import com.salesianostriana.dam.springapimiarma.users.model.UserEntity;
import com.salesianostriana.dam.springapimiarma.users.model.UserRoles;
import com.salesianostriana.dam.springapimiarma.users.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("userDetailService")
@RequiredArgsConstructor
public class UserEntityService extends BaseService<UserEntity, UUID, UserEntityRepository> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;
    private final InmobiliariaService inmobiliariaService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.repositorio.findFirstByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email + " no encontrado"));
    }

    public UserEntity save(CreateUserDto newUser, UserRoles role) {
        if(newUser.getPassword().contentEquals(newUser.getPassword2())) {
            UserEntity userEntity = UserEntity.builder()
                    .email(newUser.getEmail())
                    .full_name(newUser.getFull_name())
                    .avatar(newUser.getAvatar())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .role(role)
                    .build();
            return save(userEntity);
        }
        else {
            return null;
        }
    }

    public List<UserEntity> findByRole(UserRoles role) {
        return userEntityRepository.findUserEntityByRole(role);
    }

    public List<UserEntity> findGestoresDeInmobiliaria(Long inmobiliariaId) {
        Optional<Inmobiliaria> inmo = inmobiliariaService.findById(inmobiliariaId);
        if(inmo.isEmpty())
            return null;
        return userEntityRepository.findByInmobiliariaIdUsingQuery(inmobiliariaId);
    }

    public List<UserEntity> findInteresados() {
        if(repositorio.findInteresados().isEmpty())
            return null;
        return repositorio.findInteresados();
    }
}
