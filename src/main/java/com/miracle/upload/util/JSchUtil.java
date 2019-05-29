package com.miracle.upload.util;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Properties;

/**
 * SFTP文件上传工具类
 * Created at 2019-05-14 14:13:19
 * @author Allen
 */
@Component
public class JSchUtil {
	
	@Override
	public String toString() {
		return "JSchUtil [SFTP_HOST=" + SFTP_HOST + ", SFTP_PORT=" + SFTP_PORT + ", SFTP_USERNAME=" + SFTP_USERNAME
				+ ", SFTP_PASSWORD=" + SFTP_PASSWORD + ", PATH=" + PATH + "]";
	}

	// SFTP的IP地址
	@Value("${sftp.host}")
	private String SFTP_HOST;

	// SFTP的端口
	@Value("${sftp.port}")
	private int SFTP_PORT;
	
	// SFTP的用户名
	@Value("${sftp.username}")
	private String SFTP_USERNAME;
	
	// SFTP的用户密码
	@Value("${sftp.password}")
	private String SFTP_PASSWORD;
	
	// 文件存放路径
	@Value("${sftp.path}")
	private String PATH;

	// 文件访问地址
	@Value("${file.url}")
	private String FILE_URL;
	
	// Session对象
	private Session session = null;
	
	// Channel对象
	private Channel channel = null;
	
	/**
	 * 上传文件至FTP
	 * @param multipartFile 要上传的文件
	 * @return String 文件URL地址
	 */
	public String uploadFile(MultipartFile multipartFile) throws Exception {
		// 文件路径
        String filePath;
		// 文件名
        String fileName;
        // 文件类型
        String fileType = null;
		// 初始文件名
        String originalFilename = multipartFile.getOriginalFilename();
        // 获取文件类型
        fileType = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        if (fileType == null && !"jpg".equals(fileType) && !"png".equals(fileType) && !"gif".equals(fileType)) {
            System.out.println("文件类型有误：" + fileType);
            return null;
        }
        // 设置文件名
        fileName = System.currentTimeMillis() + "." + fileType;
		// 获取输入流
		InputStream inputStream = multipartFile.getInputStream();
		// 获取SFTP连接对象
		ChannelSftp channel = getChannel();
		// 上传模式
		int mode = ChannelSftp.OVERWRITE;
		/*
		 * OVERWRITE:完全覆盖模式(如果目标文件已经存在，传输的文件将完全覆盖目标文件，产生新的文件)
		 * RESUME:恢复模式(文件已经传输一部分，这时由于网络或其他任何原因导致文件传输中断，如果下一次传输相同的文件)
		 * APPEND:追加模式(如果目标文件已存在，传输的文件将在目标文件后追加)
		 */
		channel.put(inputStream, PATH + fileName, mode);
		channel.quit();
		System.out.println("上传完毕：" + PATH + fileName);
		// 设置文件URL地址
		filePath = FILE_URL + fileName;
		System.out.println(filePath);
		closeChannel();
		return filePath;
	}
	
	/**
	 * 关闭Channel
	 * @throws Exception
	 */
	public void closeChannel() throws Exception {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}
	
	/**
	 * 获取基于SSL协议的ChannelSftp对象
	 * @return ChannelSftp SSL协议的ChannelSftp对象
	 * @throws JSchException
	 */
	public ChannelSftp getChannel() throws JSchException {
		// 创建JSch对象
		JSch jsch = new JSch();
		// 获取session对象
		session = jsch.getSession(SFTP_USERNAME, SFTP_HOST, SFTP_PORT);
		// 设置密码
		session.setPassword(SFTP_PASSWORD);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		// 为session对象设置properties
		session.setConfig(config);
		// 通过session建立链接
		session.connect();
		// 打开SFTP通道
		channel = session.openChannel("sftp");
		// 建立SFTP通道的连接
		channel.connect();
		return (ChannelSftp)channel;
	}
}
