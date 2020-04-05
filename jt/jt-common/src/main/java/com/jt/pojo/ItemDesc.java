package com.jt.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data
@Accessors(chain = true)
public class ItemDesc extends BasePojo implements Serializable{
	
	private static final long serialVersionUID = 1016593866051306734L;
	
	@TableId
	private Long itemId;
	private String itemDesc;
}
