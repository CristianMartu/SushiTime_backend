package cristianmartucci.SushiTime_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cristianmartucci.SushiTime_backend.entities.User;
import cristianmartucci.SushiTime_backend.exceptions.UnauthorizedException;
import cristianmartucci.SushiTime_backend.payloads.ErrorsDTO;
import cristianmartucci.SushiTime_backend.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper;

    public JWTFilter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String authHeader = request.getHeader("Authorization");
//            if (authHeader == null || !authHeader.startsWith("Bearer "))
//                throw new UnauthorizedException("Inserisci correttamente il token nell'header!");
//            String accessToken = authHeader.substring(7);
//            jwtTools.verifyToken(accessToken);
//            String id = jwtTools.extractIdFromToken(accessToken);
//            User user = userService.findById(UUID.fromString(id));
//            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            filterChain.doFilter(request, response);
//        }catch (UnauthorizedException ex) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType("application/json");
//            ErrorsDTO errorsDTO = new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
//            String jsonResponse = objectMapper.writeValueAsString(errorsDTO);
//            response.getWriter().write(jsonResponse);
//        }
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            System.out.println("Request URL: " + request.getRequestURL());
            System.out.println("Request Method: " + request.getMethod());
            System.out.println(Arrays.toString(cookies));
            if (cookies == null) {
                throw new BadRequestException("No cookies received");
            }
            if (cookies != null) {
                String accessToken = null;
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        System.out.println(accessToken);
                        break;
                    }
                }
                if (accessToken == null || accessToken.isEmpty()) {
                    throw new UnauthorizedException("Token non presente nei cookie!");
                }
                jwtTools.verifyToken(accessToken);
                String id = jwtTools.extractIdFromToken(accessToken);
                User user = userService.findById(UUID.fromString(id));
                if (user == null) {
                    throw new UnauthorizedException("Utente non trovato per l'ID fornito!");
                }
                if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
                    throw new UnavailableException("L'utente non ha ruoli assegnati!");
                }
                System.out.println(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            ErrorsDTO errorsDTO = new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
            String jsonResponse = objectMapper.writeValueAsString(errorsDTO);
            response.getWriter().write(jsonResponse);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/login", request.getServletPath());
    }
}
