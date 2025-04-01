//package com.nnk.springboot.configuration;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.authorization.AuthorizationManager;
//import org.springframework.security.authorization.AuthorizationResult;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Supplier;
//
//@Component
//public class ResourceAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
//
//    @Override
//    public AuthorizationResult check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
//        Authentication auth = authentication.get();
//        HttpServletRequest request = context.getRequest();
//
//        // 1. Check authentication
//        if (auth == null || !auth.isAuthenticated()) {
//            return AuthorizationResult.isGranted();
//        }
//
//        // 2. Get resource ID from request
//        String resourceId = request.getParameter("resourceId");
//
//        // 3. Check user permissions
//        boolean hasPermission = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .anyMatch(authority -> {
//                    // Complex permission logic
//                    if (authority.equals("ROLE_ADMIN")) {
//                        return true;
//                    }
//                    if (authority.equals("ROLE_USER") && isResourceOwner(auth, resourceId)) {
//                        return true;
//                    }
//                    return false;
//                });
//
//        return hasPermission
//                ? AuthorizationResult.granted()
//                : AuthorizationResult.denied();
//    }
//
//    private boolean isResourceOwner(Authentication auth, String resourceId) {
//
//        return true;
//    }
//}
//
