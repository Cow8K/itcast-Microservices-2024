package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.enums.UserStatus;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by LiuSheng at 2024/4/24 18:38
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductionMoneyById(Long id, Double money) {
        User user = baseMapper.selectById(id);

        if (user == null || user.getStatus() == UserStatus.FROZEN)
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

    @Override
    public UserVO getUserById(Long id) {
        User user = getById(id);
        if (user == null || user.getStatus() == UserStatus.FROZEN)
            throw new RuntimeException("用户状态异常");

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setAddress(getAddressVOList(id));

        return userVO;
    }

    @Override
    public List<UserVO> getUserByIds(List<Long> ids) {
        List<User> users = this.listByIds(ids);
        if (users == null || users.isEmpty())
            return Collections.emptyList();

        List<UserVO> userVOs = BeanUtil.copyToList(users, UserVO.class);

        /*userVOs.forEach(user -> {
            List<AddressVO> addressVOList = getAddressVOList(user.getId());
            user.setAddress(addressVOList);
        });*/
        Map<Long, List<AddressVO>> addressVOMap = getAddressVOList(ids)
                .stream()
                .collect(Collectors.groupingBy(AddressVO::getUserId));

        for (UserVO userVO : userVOs) {
            userVO.setAddress(addressVOMap.get(userVO.getId()));
        }

        return userVOs;
    }

    private List<AddressVO> getAddressVOList(Long userId) {
        // 获取用户地址列表
        List<Address> addressList = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, userId)
                .list();

        if (addressList == null || addressList.isEmpty()) {
            return Collections.emptyList();
        }

        return BeanUtil.copyToList(addressList, AddressVO.class);
    }

    private List<AddressVO> getAddressVOList(List<Long> ids) {
        List<Address> addressList = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, ids)
                .list();

        if (addressList == null || addressList.isEmpty())
            return Collections.emptyList();

        return BeanUtil.copyToList(addressList, AddressVO.class);
    }
}
