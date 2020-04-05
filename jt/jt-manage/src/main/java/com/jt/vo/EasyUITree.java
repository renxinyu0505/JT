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
public class EasyUITree implements Serializable{
	private static final long serialVersionUID = -7581622089536899625L;
	private Long id;//节点id值
	private String text;//名称
	private String state;//状态 closed/open
}
