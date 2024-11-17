package pack;

import java.io.*;
import java.util.*;

public class Pack {
	public static List<File> getAllFileOneLever(String folder) {
		List<File> files = new ArrayList<File>();
		for (File f : new File(folder).listFiles()) {
			if (f.isFile()) {
				files.add(f);
			}
		}

		return files;
	}

	public static void pack(String folder, String des) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(des, "rw");
		List<File> files = getAllFileOneLever(folder);
		raf.writeInt(files.size());
		long position = 0;
		long arrPos[] = new long[files.size()];
		int countPos = 0;
		for (File f : files) {
			arrPos[countPos] = raf.getFilePointer();
			System.out.println(arrPos[countPos]);
			countPos++;
			raf.writeLong(position);
			raf.writeLong(f.length());
			raf.writeUTF(f.getName());
		}
		countPos = 0;
		byte[] buff = new byte[102400];
		int data;
		for (File f : files) {
			raf.seek(arrPos[countPos]);
			raf.writeLong(raf.length());
			raf.seek(raf.length());
			FileInputStream fis = new FileInputStream(f);
			while ((data = fis.read(buff)) > 0) {
				raf.write(buff, 0, data);
			}

			fis.close();
			countPos++;
		}
		raf.close();
	}

	private static boolean dataCopy(RandomAccessFile raf, FileOutputStream fos, long pSize) throws IOException {
		byte[] buff = new byte[102400];
		long remain = pSize;

		while (remain > 0) {
			int byteMustRead = (int) (remain > buff.length ? buff.length : remain);
			int byteRead = raf.read(buff, 0, byteMustRead);
			if (byteRead == -1)
				return false; //
			fos.write(buff, 0, byteRead);
			remain -= byteRead;
		}

		return true;
	}

	public static void unPack(String pack, String nameFileUnpack, String des) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(pack, "rw");
		int lengthPack = raf.readInt();
		for (int i = 0; i < lengthPack; i++) {
			long position = raf.readLong();
			long length = raf.readLong();
			String name = raf.readUTF();

			if (name.equals(nameFileUnpack)) {
				raf.seek(position);
				FileOutputStream dest = new FileOutputStream(des);
				dataCopy(raf, dest, length);
			}

		}

		raf.close();
	}

	public static void main(String[] args) throws IOException {
		String source = "D:\\abc";
		String des = "D:\\b";
		pack(source, des);
		unPack(des, "download.jfif", "D:\\d.jfif");
	}
}