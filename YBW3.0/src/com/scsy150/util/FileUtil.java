package com.scsy150.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;

public class FileUtil {

	public static final String APP_CACHE_DIR = "/zdit";
	public static final String DATA_CACHE_DIR = "/cache/";// 存放数据缓存文件，如json数据等
	public static final String IMAGE_CACHE_DIR = "/images";// 存放图片文件缓存文件
	public static final String CRASH_CACHE_DIR = "/crash"; // 程序崩溃日志
	public static final String DOWNLOAD_CACHE_DIR = "/download";// 存放下载的apk
	public static final String FLASH_CACHE_DIR = "flashconfigure";
	public static String SDCARDPATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	/**
	 * 初始化缓存文件路径
	 */
	public static void prepareDir() {
		File dataCache = new File(getDataCachePath());
		if (!dataCache.exists()) {
			dataCache.mkdirs();
		}
		File imageCache = new File(getImageCachePath());
		if (!imageCache.exists()) {
			imageCache.mkdirs();
		}
		File crashCache = new File(getCrashCachePath());
		if (!crashCache.exists()) {
			crashCache.mkdirs();
		}
		File downloadCache = new File(getDownloadCachePath());
		if (!downloadCache.exists()) {
			downloadCache.mkdirs();
		}
	}

	public static String getFlashDataCache() {
		return getDataCachePath() + FLASH_CACHE_DIR;
	}

	public static String getAppCachePath() {
		return SDCARDPATH + APP_CACHE_DIR;
	}

	public static String getDataCachePath() {
		return getAppCachePath() + DATA_CACHE_DIR;
	}

	public static String getImageCachePath() {
		return getAppCachePath() + IMAGE_CACHE_DIR;
	}

	public static String getCrashCachePath() {
		return getAppCachePath() + CRASH_CACHE_DIR;
	}

	public static String getDownloadCachePath() {
		return getAppCachePath() + DOWNLOAD_CACHE_DIR;
	}

	/**
	 * 判断sd卡是否存在
	 * 
	 * @return
	 */
	public boolean isSDCardExist() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 判断文件夹是否存在 ， 若不存在 则创建
	 * 
	 * @param path
	 * @return
	 */
	public void isExists(String path) {

		StringTokenizer st = new StringTokenizer(path, "/");
		String path1 = st.nextToken() + "/";
		String path2 = path1;
		while (st.hasMoreTokens()) {
			path1 = st.nextToken() + "/";
			path2 += path1;
			File inbox = new File(path2);
			if (!inbox.exists())
				inbox.mkdir();
		}
	}

	/**
	 * @param fileName
	 */
	public static String readFileByLines(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length + 1024];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return res.trim();
	}

	public static void cacheStringToFile(String str, String filename) {
		File f = new File(filename);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(f, true);
			fos.write(str.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getFileSize(String path) {
		File file = new File(path);
		long s = 0;
		if (file.exists()) {
			try {
				FileInputStream fis = null;
				fis = new FileInputStream(file);
				s = fis.available();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			LogUtil.e("file", "file is not exist");
		}
		return s / 1024;

	}

	public static byte[] fileToByte(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		File file = new File(path);
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return null;
		}

	}

	public static long getCacheSize(String path) {
		long total = 0;
		File file = new File(path);
		if (file == null || !file.exists()) {
			return total;
		}
		if (file.isFile()) {
			total = file.length();
		} else if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				total += getCacheSize(fileList[i].getAbsolutePath());
			}
		}

		return total;
	}

	// public static String calculateSizeToString(long size){
	// if(size == 0){
	// return "0B";
	// }
	// String str = "";
	// DecimalFormat df = new DecimalFormat("#.00");
	// if(size < 1024){
	// str = df.format(size) + "B";
	// } else if(size < 1024 * 1024){
	// str = df.format(size / 1024) + "KB";
	// } else if(size < 1024 * 1024 * 1024){
	// str = df.format(size / 1024 / 1024) + "M";
	// } else {
	// str = df.format(size / 1024 / 1024 / 1024) + "G";
	// }
	// return str;
	// }

	public static String calculateSizeToString(Context ctx, long size) {
		return Formatter.formatFileSize(ctx, size);
	}

	public static boolean deleteFiles(String path) {
		File file = new File(path);
		boolean flag = false;
		if (file == null || !file.exists()) {
			return flag;
		}
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				flag = deleteFiles(fileList[i].getAbsolutePath());
				// if(!flag){
				// break;
				// }
			}
		}
		return flag;
	}

	public static boolean checkFilesIsNeedDelete(String path, long intervalTime) {
		File file = new File(path);
		if (file == null || !file.exists()) {
			return false;
		}
		if (file.isFile()) {
			return deleteFileByIntervalTime(file, intervalTime);
		} else {
			File[] fileList = file.listFiles();
			if (fileList != null && fileList.length > 0) {
				for (int i = 0; i < fileList.length; i++) {
					File temp = fileList[i];
					if (temp.isDirectory()) {
						checkFilesIsNeedDelete(temp.getAbsolutePath(),
								intervalTime);
					} else {
						deleteFileByIntervalTime(temp, intervalTime);
					}
				}
			}
		}
		return true;
	}

	public static boolean deleteFileByIntervalTime(File file, long intervalTime) {
		long time = System.currentTimeMillis();
		if (time - file.lastModified() > intervalTime) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将数据写入一个文件
	 * 
	 * @param destFilePath
	 *            要创建的文件的路径
	 * @param data
	 *            待写入的文件数据
	 * @param startPos
	 *            起始偏移量
	 * @param length
	 *            要写入的数据长度
	 * @return 成功写入文件返回true,失败返回false
	 */
	public static boolean writeFile(String destFilePath, byte[] data,
			int startPos, int length) {
		try {
			if (!createFile(destFilePath)) {
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			fos.write(data, startPos, length);
			fos.flush();
			if (null != fos) {
				fos.close();
				fos = null;
			}
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 从一个输入流里写文件
	 * 
	 * @param destFilePath
	 *            要创建的文件的路径
	 * @param in
	 *            要读取的输入流
	 * @return 写入成功返回true,写入失败返回false
	 */
	public static boolean writeFile(String destFilePath, InputStream in) {
		try {
			if (!createFile(destFilePath)) {
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1) {
				fos.write(buffer, 0, readCount);
			}
			fos.flush();
			if (null != fos) {
				fos.close();
				fos = null;
			}
			if (null != in) {
				in.close();
				in = null;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean appendFile(String filename, byte[] data, int datapos,
			int datalength) {
		try {

			createFile(filename);

			RandomAccessFile rf = new RandomAccessFile(filename, "rw");
			rf.seek(rf.length());
			rf.write(data, datapos, datalength);
			if (rf != null) {
				rf.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 读取文件，返回以byte数组形式的数据
	 * 
	 * @param filePath
	 *            要读取的文件路径名
	 * @return
	 */
	public static byte[] readFile(String filePath) {
		try {
			if (isFileExist(filePath)) {
				FileInputStream fi = new FileInputStream(filePath);
				return readInputStream(fi);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从一个数量流里读取数据,返回以byte数组形式的数据。 </br></br>
	 * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，
	 * 但如果是用于网络操作，就经常会遇到一些麻烦(available()方法的问题)。所以如果是网络流不应该使用这个方法。
	 * 
	 * @param in
	 *            要读取的输入流
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream in) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			byte[] b = new byte[in.available()];
			int length = 0;
			while ((length = in.read(b)) != -1) {
				os.write(b, 0, length);
			}

			b = os.toByteArray();

			in.close();
			in = null;

			os.close();
			os = null;

			return b;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取网络流
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] readNetWorkInputStream(InputStream in) {
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();

			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1) {
				os.write(buffer, 0, readCount);
			}

			in.close();
			in = null;

			return os.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}
		return null;
	}

	/**
	 * 将一个文件拷贝到另外一个地方
	 * 
	 * @param sourceFile
	 *            源文件地址
	 * @param destFile
	 *            目的地址
	 * @param shouldOverlay
	 *            是否覆盖
	 * @return
	 */
	public static boolean copyFiles(String sourceFile, String destFile,
			boolean shouldOverlay) {
		try {
			if (shouldOverlay) {
				deleteFile(destFile);
			}
			FileInputStream fi = new FileInputStream(sourceFile);
			writeFile(destFile, fi);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 *            路径名
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 创建一个文件，创建成功返回true
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean createFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filePath
	 *            要删除的文件路径名
	 * @return true if this file was deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
	 * 
	 * @param directoryPath
	 */
	public static void deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				deleteDirectory(listFiles[i]);
			}
		}
		dir.delete();
	}

	/**
	 * 字符串转流
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * 流转字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String inputStream2String(InputStream is) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";

		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	// 批量更改文件后缀
	public static void reNameSuffix(File dir, String oldSuffix, String newSuffix) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				reNameSuffix(listFiles[i], oldSuffix, newSuffix);
			}
		} else {
			dir.renameTo(new File(dir.getPath().replace(oldSuffix, newSuffix)));
		}
	}

	public static void writeImage(Bitmap bitmap, String destPath, int quality) {
		try {
			FileUtil.deleteFile(destPath);
			if (FileUtil.createFile(destPath)) {
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
