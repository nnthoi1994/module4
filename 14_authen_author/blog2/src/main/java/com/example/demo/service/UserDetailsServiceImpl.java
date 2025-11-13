package com.example.demo.service;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUser appUser = this.appUserRepository.findByUserName(userName);
        if (appUser == null) {

            throw new UsernameNotFoundException("User " + userName + " was not found");
        }


        List<UserRole> userRoles = this.userRoleRepository.findByAppUser(appUser);
        List<GrantedAuthority> grantList = new ArrayList<>();
        if (userRoles != null) {
            for (UserRole userRole : userRoles) {
                grantList.add(new SimpleGrantedAuthority(userRole.getAppRole().getRoleName()));
            }
        }

        return new User(appUser.getUserName(), appUser.getEncrytedPassword(), grantList);
    }
}