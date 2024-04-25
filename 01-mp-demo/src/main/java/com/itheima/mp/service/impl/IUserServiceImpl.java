package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

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

        double newBalance = user.getBalance() - money;
        // update user set balance = balance - money where id = ?
        lambdaUpdate()
                .set(User::getBalance, newBalance)
                .set(newBalance <= 0, User::getStatus, 2)
                .eq(User::getId, id)
                .update();
    }

    @Override
    public List<UserVO> getUserWithCondition(String name, Integer status, Integer minBalance, Integer maxBalance) {
        List<User> userList = lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
        return BeanUtil.copyToList(userList, UserVO.class);
    }
}
