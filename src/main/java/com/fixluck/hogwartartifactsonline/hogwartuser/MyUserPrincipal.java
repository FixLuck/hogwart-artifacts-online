package com.fixluck.hogwartartifactsonline.hogwartuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

public class MyUserPrincipal implements UserDetails {

    private HogwartsUser hogwartsUser;

    public MyUserPrincipal(HogwartsUser hogwartsUser) {
        this.hogwartsUser = hogwartsUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Convert a user's role from space-delimited string to a List of SimpleGrantedAuthority object.
        //E.g., max's roles are stored in a string like "admin user moderator", we need to convert it to a list of GrantedAuthority
        //Before conversion, we need to add this "ROLE_" prefix to each role

        return Arrays.stream(StringUtils.tokenizeToStringArray(this.hogwartsUser.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.hogwartsUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.hogwartsUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getHogwartsUser().isEnabled();
    }

    public HogwartsUser getHogwartsUser() {
        return hogwartsUser;
    }

    public void setHogwartsUser(HogwartsUser hogwartsUser) {
        this.hogwartsUser = hogwartsUser;
    }
}
