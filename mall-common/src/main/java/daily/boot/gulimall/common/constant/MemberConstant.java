package daily.boot.gulimall.common.constant;

public class MemberConstant {
    public enum MemberStatus {
        INUSE(0,"账号在使用"),
        DISABLE(1,"账号停用"),
        LOCKED(2,"账号被锁定"),
        PWD_EXPIRE(3,"密码过期"),
        ACCOUT_EXPIRE(4,"账号过期");
        private Integer code;
        private String msg;
    
        MemberStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getMsg() {
            return msg;
        }
    }
}
