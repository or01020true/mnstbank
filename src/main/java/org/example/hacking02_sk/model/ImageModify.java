package org.example.hacking02_sk.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageModify {
	MultipartFile inputimage;
}
