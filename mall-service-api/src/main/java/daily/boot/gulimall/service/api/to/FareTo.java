package daily.boot.gulimall.service.api.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FareTo {
    private MemberAddressTo address;
    private BigDecimal fare;
}
