package cn.shuwei.dao;

import cn.shuwei.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 校验用户是否存在
     *
     * @param username
     * @return
     */
    int checkUsername(String username);

    /**
     * 检验用户名密码是否正确
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 通过邮箱校验用户是否存在
     *
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     * 通过用户名查找问题
     *
     * @param username
     * @return
     */
    String selectQuestionByUsername(String username);

    /**
     * 通过问题寻找答案
     * @param username 用户名
     * @param question 问题
     * @param answer 答案
     * @return
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String passwordNew);

    /**
     * 校验密码
     * @param password
     * @param userId
     * @return
     */
    int checkPassword(@Param(value = "password") String password,@Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email, @Param("id") Integer userId);
}