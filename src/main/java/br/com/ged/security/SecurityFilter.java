package br.com.ged.security;
import br.com.ged.domains.User;
import br.com.ged.domains.enums.Profiles;
import br.com.ged.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {


    @Autowired
    JwtService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null){
            var user = tokenService.validateToken(token);
            Optional<User> userDetails = userRepository.findByUsername(user);
            var authentication = new UsernamePasswordAuthenticationToken(userDetails.get(), null,  getAuthorities(userDetails.get().getProfile()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<Profiles> profiles) {
             return profiles.stream()
                .map(p -> new SimpleGrantedAuthority("ROLE_" + p.name()))
                .toList();
    }

}
