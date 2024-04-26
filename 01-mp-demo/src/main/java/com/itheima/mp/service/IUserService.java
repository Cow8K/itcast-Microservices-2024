package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

/**
 * Created by LiuSheng at 2024/4/24 18:38
 */

public interface IUserService extends IService<User> {
    void deductionMoneyById(Long id, Double money);

    List<UserVO> getUserWithCondition(String name, Integer status, Integer minBalance, Integer maxBalance);

    UserVO getUserById(Long id);

    List<UserVO> getUserByIds(List<Long> ids);
}
