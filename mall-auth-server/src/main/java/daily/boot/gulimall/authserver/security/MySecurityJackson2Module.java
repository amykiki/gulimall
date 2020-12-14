package daily.boot.gulimall.authserver.security;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daily.boot.gulimall.authserver.security.jacksonmixins.DisabledExceptionMixin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Slf4j
public class MySecurityJackson2Module extends SimpleModule {
    
    private static final long serialVersionUID = 8056951763941297686L;
    
    public MySecurityJackson2Module() {
        super(MySecurityJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }
    
    @Override
    public void setupModule(SetupContext context) {
        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
        context.setMixInAnnotations(DisabledException.class, DisabledExceptionMixin.class);
    }
    
}
