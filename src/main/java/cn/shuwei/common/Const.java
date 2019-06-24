package cn.shuwei.common;

public class Const {

    public static final String CURRENT_USER = "currentUser";

    public interface Role {
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }

    public interface VaildType {
        int EMAIL = 1; // 邮箱
        int USERNAME = 2; // 用户名
    }
}
