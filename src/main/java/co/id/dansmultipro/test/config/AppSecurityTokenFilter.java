package co.id.dansmultipro.test.config;

import co.id.dansmultipro.test.service.AppJwtTokenService;
import co.id.dansmultipro.test.service.AppUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class AppSecurityTokenFilter extends OncePerRequestFilter {

    private AppJwtTokenService appJwtTokenService;
    private AppUserDetailService appUserDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String reqHeader = request.getHeader("Authorization");

        if(reqHeader == null || !reqHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = reqHeader.substring("Bearer ".length());

        if(!appJwtTokenService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = appJwtTokenService.extractUsernameFromToken(token);

        if(username != null) {
            UserDetails userDetails = appUserDetailService.loadUserByUsername(username);

            if(appJwtTokenService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
