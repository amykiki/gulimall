package daily.boot.gulimall.authserver.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class OAuthClientPasswordEncoder implements PasswordEncoder {
    
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
    
    private OAuthClientPasswordEncoder() {
    
    }
    
    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }
    
    private static final PasswordEncoder INSTANCE = new OAuthClientPasswordEncoder();
}
