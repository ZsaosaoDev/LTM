package lab5;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Student {
	private int id;
	private String name;
	private int bYear;
	private double grade;

	public Student(int id, String name, int bYear, double grade) {
		this.id = id;
		this.name = name;
		this.bYear = bYear;
		this.grade = grade;
	}

	public Student() {
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", bYear=" + bYear + ", grade=" + grade + "]";
	}

	public void write(RandomAccessFile raf, int nLeng) throws IOException {

		raf.writeInt(id);
		// name
		int c;
		for (int i = 0; i < nLeng; i++) {
			if (i < name.length())
				c = name.charAt(i);
			else
				c = 0;
			raf.writeChar(c);
		}
		raf.writeInt(bYear);
		raf.writeDouble(grade);
	}

	public void load(RandomAccessFile raf, int nLeng) throws IOException {
		id = raf.readInt();
		int c;
		name = "";
		for (int i = 0; i < nLeng; i++) {
			c = raf.readChar();
			if (c != 0)
				name += c;
		}
		bYear = raf.readInt();
		grade = raf.readDouble();
	}

}
