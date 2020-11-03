package daily.boot.gulimall.search.service.impl;

import daily.boot.gulimall.search.entity.Account;
import daily.boot.gulimall.search.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BaseESServiceImplTest {
    
    @Autowired
    private AccountService accountService;
    
    @Test
    void saveOrUpdate() {
        Account account = new Account();
        account.setAccountNumber(1000L);
        account.setAddress("Shanghai Beijiangyan Road 227");
        account.setAge(25L);
        account.setBalance(38383L);
        account.setCity("Shanghai");
        account.setState("SH");
        account.setEmail("test2@126.com");
        account.setEmployer("Amy Wong");
        account.setFirstname("Amy");
        account.setLastname("Wong");
        account.setGender("M");
    
        accountService.saveOrUpdate(account);
    }
}