package com.jt.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO implements Serializable{
	private static final long serialVersionUID = -8019017074323385599L;
	
	private Integer error=0;	//判断是否有误
	private String url;			//图片存储地址
	private Integer width;		//图片宽度
	private Integer height;		//图片高度
	
}
