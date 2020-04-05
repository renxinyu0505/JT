package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;


@Controller
public class FileController {
	
	@Autowired
	private FileService fileService;
	/**
	 * 思路
	 * 1.获取用户文件信息 包含文件名称
	 * 2.指定文件上传路径 （判断路径是否存在）
	 * 3.实现文件上传
	 * @param fileImage
	 * @return
	 */
	//当用户上传完成时重定向到上传页面
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) {
		//1.获取input标签中的name属性
		String inputName = fileImage.getName();
		//2.获取文件name
		String fileName = fileImage.getOriginalFilename();
		//3.定义文件夹路径
		File fileDir = new File("E:/JAVA/Projects/jt/image");
		
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}
		//4.实现文件上传
		try {
			fileImage.transferTo(new File("E:/JAVA/Projects/jt/image/"+fileName));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/file.jsp";
	}
	///pic/upload?dir=image
	@RequestMapping("/pic/upload")
	@ResponseBody
	public ImageVO uploadFile(MultipartFile uploadFile) {
		
		return fileService.uploadFile(uploadFile);
		
	}
}
