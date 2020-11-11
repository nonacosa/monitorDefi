package com.online.book.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.SneakyThrows;

/**
 * @author wenda.zhuang
 * @Date 2020/9/1 12:27 上午
 * @Description ...
 * @E-mail sis.nonacosa@gmail.com
 */
public class FileUtil {

	static Path findFile(Path targetDir, String fileName) throws Exception {
		return Files.list(targetDir).filter( (p) -> {
			if (Files.isRegularFile(p)) {
				return p.getFileName().toString().equals(fileName);
			} else {
				return false;
			}
		}).findFirst().orElse(null);
	}


	@SneakyThrows
	public static File multipartFileToFile(MultipartFile multipartFile) {
		MultipartFile mf = new MockMultipartFile("sourceFile.tmp", multipartFile.getBytes());

		File file = new File("src/main/resources/" + new SnowFlake(2,2).nextId());

		try (OutputStream os = new FileOutputStream(file)) {
			os.write(mf.getBytes());
		}
		return file;
	}
}
