package beyond.homework.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * AuthResponse class returns the JWT token for the client
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthResponse implements Serializable
{
    private final String jwtToken;
}
