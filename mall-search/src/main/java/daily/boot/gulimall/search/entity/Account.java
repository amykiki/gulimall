package daily.boot.gulimall.search.entity;

import daily.boot.gulimall.search.elasticsearch.annotation.ESDocument;
import daily.boot.gulimall.search.elasticsearch.annotation.ESId;
import lombok.Data;

@Data
@ESDocument(indexName = "bank_account")
public class Account {
    @ESId
    private Long accountNumber;
    private Long balance;
    private Long age;
    private String firstname;
    private String lastname;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;
}
