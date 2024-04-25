package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;

/**
 * Created by LiuSheng at 2024/4/24 18:38
 */

public interface IUserService extends IService<User> {
    void deductionMoneyById(Long id, Double money);
}
