package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityHelper {


    public static boolean isConnected(){
        return SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null
                || !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User);
    }
    /**
     * get the user currently connected
     * @return user currently signed-in
     */
    public static User getConnectedUser()
    {
        if(isConnected()) {
            throw new BadCredentialsException("User not connected");
        }

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
