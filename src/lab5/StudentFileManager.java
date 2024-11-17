package lab5;

import java.io.IOException;
import java.io.RandomAccessFile;


public class StudentFileManager {
	private RandomAccessFile raf;
	private int count;
	private int recSize;
	private int nLeng;
	private static final int nameLeng = 25;

	public StudentFileManager(String fileName) throws IOException {
		raf = new RandomAccessFile(fileName, "rw");
		if (raf.length() != 0) { // read header
			count = raf.readInt();
			recSize = raf.readInt();
			nLeng = (recSize - 4 - 4 - 8) / 2;
		} else { // write header
			nLeng = nameLeng;
			count = 0;
			recSize = 4 + 4 + 8 + 2 * nLeng;
			raf.writeInt(count);
			raf.writeInt(recSize);
		}
//		file outputstream can tao moi file

	}

	public void addStudent(Student st) throws IOException {
		raf.seek(raf.length());
		st.write(raf, nLeng);
		count++;
		raf.seek(0);
		raf.writeInt(0);

	}

	public void updateStudent(int index, Student newSt) throws IOException {
		if ((index <= 0) || (index > count - 1))
			throw new IndexOutOfBoundsException();
		long pos = 4 + 4 + index * recSize;
		raf.seek(pos);
		newSt.write(raf, nLeng);
	}

	public void getStudent(int index) throws IOException {
		if ((index <= 0) || (index > count - 1))
			throw new IndexOutOfBoundsException();
		long pos = 4 + 4 + index * recSize;
		raf.seek(pos);
		Student st = new Student();
		st.load(raf, nLeng);
		return;

	}

	public void close() throws IOException {
		raf.close();
	}

	public static void main(String[] args) {

	}
}