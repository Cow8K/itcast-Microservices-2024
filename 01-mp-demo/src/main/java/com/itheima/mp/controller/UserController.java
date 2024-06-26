package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by LiuSheng at 2024/4/25 19:26
 */

@RestController
@RequiredArgsConstructor
@Api(tags = "用户管理接口")
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    @PostMapping
    @ApiOperation("新增用户接口")
    public void saveUser(@RequestBody UserFormDTO userDTO) {
        User user = BeanUtil.copyProperties(userDTO, User.class);
        userService.save(user);
    }

    @DeleteMapping("{id}")
    @ApiOperation("根据ID删除用户接口")
    public void deleteUser(@ApiParam("用户ID") @PathVariable("id") Long id) {
        userService.removeById(id);
    }

    @GetMapping("{id}")
    @ApiOperation("根据ID查询用户接口")
    public UserVO getUserById(@ApiParam("用户ID") @PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    @ApiOperation("获取用户集合")
    public List<UserVO> getUsers(@ApiParam("用户ID集合") @RequestParam("ids") List<Long> ids) {
        return userService.getUserByIds(ids);
    }

    @ApiOperation("根据用户ID扣减用户余额")
    @PutMapping("/users/{id}/deduction/{money}")
    public void deductionMoney(
            @ApiParam("用户ID") @PathVariable Long id,
            @ApiParam("扣减金额") @PathVariable Double money
    )
    {
        userService.deductionMoneyById(id, money);
    }

    @GetMapping("/list")
    @ApiOperation("根据条件查询用户")
    public List<UserVO> getUserWithCondition(UserQuery query) {
        return userService.getUserWithCondition(
                query.getName(),
                query.getStatus(),
                query.getMinBalance(),
                query.getMaxBalance()
        );
    }

    @GetMapping("/page")
    @ApiOperation("分页查询用户")
    public PageDTO<UserVO> getUserPage(UserQuery query) {
        return userService.getUserPage(query);
    }
}
