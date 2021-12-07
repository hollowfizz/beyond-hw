package beyond.homework.security;

import lombok.*;

import java.io.Serializable;

/**
 * AuthRequest class that need to be send from client with the credentials in order to authenticate
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthRequest implements Serializable
{
    private String username;
    private String password;
}
