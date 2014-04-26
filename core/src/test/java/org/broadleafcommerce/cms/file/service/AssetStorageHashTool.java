package org.broadleafcommerce.cms.file.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

/**
 * 用于将原始资源文件hash到assets目录的工具
 * 
 * @author Ju
 * 
 */
public class AssetStorageHashTool {

	private static File srcDir;
	private static File destDir;
	private static int layers;

	/**
	 * @param args
	 *            srcDir destDir layers
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args == null || args.length < 3) {
			System.out
					.println("用法：AssetStorageHashTool srcDir destDir layers\n"
							+ "\tsrcDir: assets源目录\n"
							+ "\tdestDir: assets输出目录\n"
							+ "\tlayers: hash的层数(不大于3)"
							+ "\tfrom_fn: 文件名中待替换的字串" + "\tto_fn: 文件名替换字符串");
			return;
		}

		srcDir = new File(args[0]);
		destDir = new File(args[1]);
		layers = Integer.parseInt(args[2]);
		if (layers > 3 || layers < 1) {
			System.out.println("hash的层数= [1, 3]！");
			return;
		}
		String fnFrom = null;
		String fnTo = null;
		if (args.length > 3) {
			fnFrom = args[3];
			fnTo = args[4];
		}

		File[] fls = srcDir.listFiles();
		if (fls == null) {
			System.out.println("srcDir不是目录：" + srcDir);
			return;
		}

		processFiles(fls, "/", fnFrom, fnTo);
	}

	private static void processFiles(File[] fls, String root, String fnFrom,
			String fnTo) throws IOException {
		for (int i = 0; i < fls.length; i++) {
			if (fls[i].isDirectory()) {
				File[] subFls = fls[i].listFiles();
				if (subFls == null || subFls.length == 0)
					continue;
				processFiles(subFls, root + fls[i].getName() + "/", fnFrom,
						fnTo);
			} else {
				hashFile(fls[i], root, fnFrom, fnTo);
			}
		}
	}

	/**
	 * @throws IOException
	 * @see StaticAssetStorageServiceImpl#generateStorageFileName(String,
	 *      boolean)
	 */
	private static void hashFile(File file, String root, String fnFrom,
			String fnTo) throws IOException {
		if ("thumbs.db".equals(file.getName().toLowerCase()))
			return;

		String fName = (fnFrom == null) ? file.getName() : file.getName()
				.replaceFirst(fnFrom, fnTo);
		String fn = root + fName;

		File dirDest = destDir;
		String fileHash = DigestUtils.md5Hex(fn);
		for (int i = 0; i < layers; i++) {
			dirDest = new File(dirDest, fileHash.substring(i * 2, (i + 1) * 2));
			if (dirDest.exists()) {
				if (dirDest.isFile()) {
					throw new RuntimeException("dest不是个目录"
							+ dirDest.getAbsolutePath());
				}
			} else {
				if (!dirDest.mkdir()) {
					throw new RuntimeException("创建dest目录失败"
							+ dirDest.getAbsolutePath());
				}
			}
		}
		System.out.println("processing: " + fn + "\t" + dirDest.getPath());
		FileUtils.copyFile(file, new File(dirDest, fName));
	}
}