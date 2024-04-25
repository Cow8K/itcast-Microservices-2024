package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * Created by LiuSheng at 2024/4/24 18:38
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductionMoneyById(Long id, Double money) {
        User user = baseMapper.selectById(id);

        if (user == null || user.getStatus() == 2)
            throw new RuntimeException("用户状态异常");

        if (user.getBalance() < money)
            throw new RuntimeException("用户余额不足");

        // update user set balance = balance - money where id = ?
        baseMapper.deductionMoney(id, money);
    }
}
