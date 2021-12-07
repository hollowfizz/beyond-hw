package beyond.homework.security;

import beyond.homework.model.User;
import beyond.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * JwtUserDetailsService check if the user exists in the database
 */
@Service
public class JwtUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    /**
     * Instantiates a new Jwt user details service.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public JwtUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<User> user = this.userRepository.findByUsernameIgnoreCase(username);

        if (!user.isPresent())
        {
            throw new UsernameNotFoundException(username);
        }
        else
        {
            return new org.springframework.security.core.userdetails.User(user.get()
                    .getUsername(), user.get()
                    .getPassword(), new ArrayList<GrantedAuthority>());
        }
    }
}
