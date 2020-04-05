package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{
	//定义本地磁盘路径
	//声明在了properties文件里
	/*
	 * private String localDirPath = "E:/JAVA/Projects/jt/image/";
		private String urlPath = "http://image.jt.com/";
	 */
	@Value("${image.localDirPath}")
	private String localDirPath;
	@Value("${image.urlPath}")
	private String urlPath;

	/**
	 * 1.获取图片名称
	 * 2.校验是否为图片类型 jpg/png/gif
	 * 3.校验是否为恶意程序 木马.exe.jpg
	 * 4.分文件保存 按照时间存储 yyyy/MM/dd
	 * 5.防止文件重名 UUID 32位16进制数+毫秒数
	 * 
	 * 正则常用字符
	 * 1.^ 标识开始字符
	 * 2.$ 标识结束字符
	 * 3.点. 任意单个字符
	 * 4.* 表示任意个 0-无穷
	 * 5.+ 表示1-无穷个
	 * 6. \. 标识特殊字符 .
	 * 7. (xx|xx|xx) 代表分组， 满足其中一个条件即可
	 */
	@Override
	public ImageVO uploadFile(MultipartFile uploadFile) {
		ImageVO imageVO = new ImageVO();
		//1.获取图片名称
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();
		//2.校验是否为图片类型 使用正则表达式判断字符串
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			imageVO.setError(1);
			return imageVO;
		}
		//3.判断是否为恶意程序
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());

			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();

			if(width == 0 || height == 0) {
				imageVO.setError(1);
				return imageVO;
			}
		
		//4.时间转化为字符串
		String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		//5.准备文件夹
		String localDir = localDirPath + dateDir;
		File dirFile = new File(localDir);
		
		if(!dirFile.exists()) {
			//如果文件不存在 则创建文件夹
			dirFile.mkdirs();
		}
		//6.使用UUID 定义文件名称 uuid.jpg
		String uuid = UUID.randomUUID().toString().replace("-", "");
		//图片类型a.jpg 动态获取".jpg"
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		
		//拼接新的文件名称
		String realLocalPath = localDir + "/" + uuid + fileType;
		
		//7.完成文件上传
			uploadFile.transferTo(new File(realLocalPath));
			
			//E:/JAVA/Projects/jt/image/2020/03/01/95cf74bf1a17458cb31b9f2413d46487.jpg
		//8.拼接url路径 http://image.jt.com/yyyy/MM/dd/
			String realUrlPath = urlPath + dateDir + "/" +uuid + fileType;
			//将文件信息回传给页面
			imageVO.setError(0)
					.setHeight(height)
					.setWidth(width)
					.setUrl(realUrlPath);
			//暂时写死
					
	
		}catch(Exception e) {
			e.printStackTrace();
			imageVO.setError(1);
			return imageVO;
		}
		
		
		return imageVO; 
		
	}
	

}
