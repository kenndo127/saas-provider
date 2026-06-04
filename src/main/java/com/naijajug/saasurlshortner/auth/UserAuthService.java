package com.naijajug.saasurlshortner.auth;

import com.naijajug.saasurlshortner.exceptions.ResourceNotFoundException;
import com.naijajug.saasurlshortner.model.UserModel;
import com.naijajug.saasurlshortner.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {
    private final UserModelRepository userModelRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<UserModel> userModelOptional = userModelRepository.findByEmail(email);

        if (userModelOptional.isEmpty()) throw new ResourceNotFoundException("User not found");
        UserModel user = userModelOptional.get();
        // .orElseThrow( error ) can work here

        Set<GrantedAuthority> authorities = user.getRoles().stream().map(item -> new SimpleGrantedAuthority(item.name())).collect(Collectors.toSet());

        AuthUserDetails userDetails = new AuthUserDetails(user.getEmail(), user.getPassword(), authorities);

        return userDetails;
    }
}
