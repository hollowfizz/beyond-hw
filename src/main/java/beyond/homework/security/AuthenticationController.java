package beyond.homework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for authentication, handling login and authentication
 */
@RestController
@CrossOrigin
public class AuthenticationController
{

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    /**
     * Instantiates a new Authentication controller.
     *
     * @param authenticationManager the authentication manager
     * @param jwtTokenUtil          the jwt token util
     * @param userDetailsService    the user details service
     */
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService)
    {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Return JWT token for the user if the authentication was successful
     *
     * @param authenticationRequest the authentication request
     * @return the AuthResponse what contains the JWT token
     * @throws Exception the exception
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception
    {
        this.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = this.jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    private void authenticate(String username, String password) throws Exception
    {
        try
        {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (DisabledException e)
        {
            throw new Exception("USER_DISABLED", e);
        }
        catch (BadCredentialsException e)
        {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
