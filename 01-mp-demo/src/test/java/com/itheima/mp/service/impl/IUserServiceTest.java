package com.itheima.mp.service.impl;

import com.itheima.mp.domain.enums.UserStatus;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuSheng at 2024/4/24 18:40
 */

@SpringBootTest
class IUserServiceTest {
    @Resource
    private IUserService userService;
    @Test
    void testBatchSaveUser() {
        long b = System.currentTimeMillis();
        saveUser(100000);
        System.out.println("共耗时：" + (System.currentTimeMillis() - b));
    }

    private void saveUser(int number) {
        int capacity = 1000;
        List<User> list = new ArrayList<>(capacity);
        for (int i = 1; i <= number; i++) {
            User user = new User();
            user.setUsername("user_" + i);
            user.setPassword("123456");
            user.setStatus(UserStatus.NORMAL);

            UserInfo userInfo = new UserInfo();
            user.setInfo(userInfo);

            list.add(user);

            // 按批保存到数据库
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
    }
}