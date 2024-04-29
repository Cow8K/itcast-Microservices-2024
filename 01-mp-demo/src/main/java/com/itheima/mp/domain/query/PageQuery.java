package com.itheima.mp.domain.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by LiuSheng at 2024/4/29 20:57
 */


@Data
@ApiModel(description = "分页查询条件实体")
public class PageQuery {
    @ApiModelProperty("页码")
    private Long pageNo = 1L;
    @ApiModelProperty("记录数")
    private Long pageSize = 5L;
    @ApiModelProperty("排序字段")
    private String sortBy;
    @ApiModelProperty("是否升序")
    private boolean isAsc = true;

    public <T> Page<T> toMpPage(OrderItem ...orderItem) {
        // 1. 构建分页条件
        Page<T> page = Page.of(pageNo, pageSize);
        if (StrUtil.isNotBlank(sortBy)) {
            OrderItem item = new OrderItem();
            item.setAsc(isAsc);
            item.setColumn(sortBy);
            page.addOrder(item);
        }
        else if (orderItem.length != 0){
            page.addOrder(orderItem);
        }
        return page;
    }

    public <T> Page<T> toMpPage(String sortBy, boolean isAsc) {
        OrderItem item = new OrderItem();
        item.setAsc(isAsc);
        item.setColumn(sortBy);
        return toMpPage(item);
    }

    public <T> Page<T> orderByCreationTime(String sortBy) {
        String defaultSortBy = "create_time";
        return StrUtil.isBlank(sortBy) ? toMpPage(defaultSortBy, isAsc) : toMpPage(sortBy, isAsc);
    }

    public <T> Page<T> orderByUpdateTime(String sortBy) {
        String defaultSortBy = "update_time";
        return StrUtil.isBlank(sortBy) ? toMpPage(defaultSortBy, isAsc) : toMpPage(sortBy, isAsc);
    }
}
