package cn.shuwei.service.impl;

import cn.shuwei.common.Const;
import cn.shuwei.common.ServerResponse;
import cn.shuwei.common.TokenCache;
import cn.shuwei.dao.UserMapper;
import cn.shuwei.pojo.User;
import cn.shuwei.service.IUserService;
import cn.shuwei.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //todo 密码登录MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        // 检查用户名密码是否匹配
        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            // 密码错误
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {

        // 校验邮箱
        ServerResponse response = this.checkVaild(user.getEmail(), Const.VaildType.EMAIL);
        if (!response.isSuccess()) {
            return response;
        }

        // 检验用户名
        response = this.checkVaild(user.getUsername(), Const.VaildType.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }

        // 设置权限为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);

        // MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        // 插入数据
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            // 插入失败
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkVaild(String str, int type) {
        if (type == Const.VaildType.EMAIL) {
            int resultCount = userMapper.checkEmail(str);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("邮箱已存在");
            }
        } else if (type == Const.VaildType.USERNAME) {
            int resultCount = userMapper.checkEmail(str);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("用户名已存在");
            }
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        // 检验用户名
        ServerResponse response = this.checkVaild(username, Const.VaildType.USERNAME);
        if (response.isSuccess()) {
            // 用户名不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码问题为空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            // 问题答案正确
            String forgetToken = UUID.randomUUID().toString();
            // 放入本地缓存
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        // 检验用户名
        ServerResponse response = this.checkVaild(username, Const.VaildType.USERNAME);
        if (response.isSuccess()) {
            // 用户名不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String tokenCache = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(tokenCache)) {
            return ServerResponse.createByErrorMessage("token无效,过期");
        }
        if (StringUtils.equals(tokenCache, forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 防止横向越权 校验用户的旧密码 一定要指定用户
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }


    @Override
    public ServerResponse<User> update_info(User user) {
        // username 不能被更新
        // email 需要校验
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount> 0){
            // 邮箱已被其他人注册
            return ServerResponse.createByErrorMessage("email已经存在,请更新后重新尝试");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setQuestion(user.getQuestion());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0){
            // 更新成功
            ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getUserInfo(int userId) {
       User user = userMapper.selectByPrimaryKey(userId);
       if (user == null){
           return ServerResponse.createByErrorMessage("找不到当前用户");
       }
       user.setPassword(StringUtils.EMPTY);
       return ServerResponse.createBySuccess(user);
    }
}
