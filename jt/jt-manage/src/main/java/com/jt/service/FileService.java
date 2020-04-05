package com.jt.service;

import java.util.List;


import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

public interface FileService {

	ImageVO uploadFile(MultipartFile uploadFile);


}
