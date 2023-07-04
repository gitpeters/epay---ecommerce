package com.peters.Epay.security;

import com.peters.Epay.user.entity.UserEntity;
import com.peters.Epay.exception.ApplicationAuthenticationException;
import com.peters.Epay.user.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByEmail(username).orElseThrow(()-> new ApplicationAuthenticationException("Invalid username and password combination"));
        if(!user.isEnabled()){
            throw new ApplicationAuthenticationException("Access denied, your account is currently locked.");
        }

        return new CustomUserDetails(user);
    }
}
