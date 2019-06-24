package cn.shuwei.service;

import cn.shuwei.common.ServerResponse;
import cn.shuwei.pojo.User;

/**
 * 用户模块接口
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkVaild(String str, int type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew,User user);

    ServerResponse<User> update_info(User user);

    ServerResponse<User> getUserInfo(int userId);
}
