package com.xoriant.xorpay.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.service.ExportService;

public class XorUtil {
	private static final Logger logger = LoggerFactory.getLogger(XorUtil.class);

	public static String getDDMMMYYYY(String date) {
		if (null != date) {
			LocalDate ld = LocalDate.parse(date.substring(0, 10), XorConstant.formateYYYYMMDD);

			return ld.format(XorConstant.formateddMMMYYYY).toString();
		}
		return null;
	}

	public static long getInstantEpochMili() {
		return Instant.now().toEpochMilli();
	}

	public static String getLocalDateTimeFormat(String val) {
		// 2021-05-08 18:50:53.014266
		// 2021-05-08T18:50:53
		int len = val.length();

		if (len > 18) {
			val = val.substring(0, 19);
			val = val.replace(" ", "T");
		}

		return val;
	}

	public static boolean creatFolder(String pathDir) throws IOException {
		Path pathToFile = Paths.get(pathDir);
		try {
			Files.createDirectories(pathToFile.getParent());

			Files.createFile(pathToFile);

		} catch (IOException e) {
			logger.error("Folder creation Exception", e);
			throw e;
		}
		return true;
	}

}
