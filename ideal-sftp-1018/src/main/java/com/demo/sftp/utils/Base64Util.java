package com.demo.sftp.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.Base64;

@Slf4j
public class Base64Util {


	public static void generateImage(String imageStr, String path,String imageFile) {
		try {
			if (imageStr == null) {
				throw new RuntimeException("图片不能为空！");
			}
			File dir = new File(path);
			if (!dir.isDirectory()) {
				log.error("图片存储的路径为：{}",dir.getAbsolutePath());
				throw new RuntimeException("图片存储的路径不存在！");
			}
			String absolutePath = path + "/" + imageFile;
			File file = new File(absolutePath);
			if(file.exists()) {
				log.error("图片文件已经存在：{}",file.getAbsolutePath());
				throw new RuntimeException("图片文件已经存在！");
			}
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b = decoder.decodeBuffer(imageStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(absolutePath);
			out.write(b);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("图片存储时IO异常！");
		}
	}

	/**
	 * 把指定的图片文件转换成 Base64编码
	 *
	 * @param imgFileUrl 指定的图片文件路径
	 * @return
	 */
	public static String generateBase64(String imgFileUrl) {
		InputStream inputStream = null;
		try {
			File file = new File(imgFileUrl);
			if (!file.isFile()) {
				log.error("图片文件是：{}",file.getAbsolutePath());
				throw new RuntimeException("图片文件不存在！");
			}
			inputStream = new FileInputStream(imgFileUrl);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("图片文件不存在！");
		}
		return generateBase64(inputStream);

	}

	/**
	 * InputStream 转换成 Base64 编码
	 * @param imgFileIo 入参必须是读取图片文件的 IO 流
	 * @return Base64 编码字串
	 */
	public static String generateBase64(InputStream imgFileIo) {
		if (null == imgFileIo){
			throw new RuntimeException("图片转码时IO异常: 入参为空！");
		}
		try {
			byte[] bytes = IOUtils.toByteArray(imgFileIo);
			return Base64.getEncoder().encodeToString(bytes);
		}
		catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException("图片转码时IO异常！");
		}
		finally {
			try {
				imgFileIo.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * base64编码转InputStream
	 * @param base64String base64编码
	 */
	public static InputStream baseToInputStream(String base64String) {
		ByteArrayInputStream stream = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytes1 = decoder.decodeBuffer(base64String);
			stream = new ByteArrayInputStream(bytes1);
		} catch (Exception e) {
			log.error("base64编码转InputStream出错: {}", e.getMessage());
		}
		return stream;
	}

}
