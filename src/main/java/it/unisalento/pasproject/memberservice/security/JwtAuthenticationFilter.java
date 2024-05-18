package it.unisalento.pasproject.memberservice.security;


import it.unisalento.pasproject.memberservice.dto.UserDetailsDTO;
import it.unisalento.pasproject.memberservice.exceptions.UserNotAuthorized;
import it.unisalento.pasproject.memberservice.service.UserCheckService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The JwtAuthenticationFilter class is a filter that intercepts each request once to perform JWT authentication.
 * It extends the OncePerRequestFilter class provided by Spring Security.
 * It includes properties such as jwtUtilities and userCheckService.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * The JwtUtilities instance used for JWT-related operations.
     */
    @Autowired
    private JwtUtilities jwtUtilities ;

    /**
     * The UserCheckService instance used for user-related operations.
     */
    @Autowired
    private UserCheckService userCheckService;

    /**
     * The doFilterInternal method is overridden from OncePerRequestFilter.
     * It performs JWT authentication for each request.
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param chain the FilterChain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, UserNotAuthorized {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtilities.extractUsername(jwt);
            }
        } catch (Exception e) {
            throw new UserNotAuthorized("User not authorized");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetailsDTO user = this.userCheckService.loadUserByUsername(username);
            UserDetails userDetails = User.builder()
                    .username(user.getEmail()) // Assume email is username
                    .password("") // Password field is not used in JWT authentication
                    .authorities(user.getRole()) // Set roles or authorities from the UserDetailsDTO
                    .build();

            if (jwtUtilities.validateToken(jwt, userDetails) && userCheckService.isEnable(user.getEnabled())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                throw new UserNotAuthorized("User not authorized");
            }
        }

        chain.doFilter(request, response);
    }

}