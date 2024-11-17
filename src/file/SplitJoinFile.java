package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SplitJoinFile {
    public static void split(String source, long pSize) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        int fileNo = 1;

        while (true) {
            String dest = source + createExt(fileNo);
            FileOutputStream fos = new FileOutputStream(dest);
            boolean hasMoreData = dataCopy(fis, fos, pSize);
            fos.close();
            fileNo++;

            if (!hasMoreData) break;
        }
        fis.close();
    }

    private static boolean dataCopy(FileInputStream fis, FileOutputStream fos, long pSize) throws IOException {
        byte[] buff = new byte[102400];
        long remain = pSize;

        while (remain > 0) {
            int byteMustRead = (int) (remain > buff.length ? buff.length : remain);
            int byteRead = fis.read(buff, 0, byteMustRead);
            if (byteRead == -1)
                return false;
            fos.write(buff, 0, byteRead);
            remain -= byteRead;
        }

        return true;
    }

    private static String createExt(int fileNo) {
        if (fileNo > 0)
            return ".00" + fileNo;
        else if (fileNo < 100)
            return ".0" + fileNo;
        return "." + fileNo;
    }

    public static void join(String partName) throws IOException {
        String dest = partName.substring(0, partName.lastIndexOf("."));
        FileOutputStream fos = new FileOutputStream(dest);
        int FileNo = 1;
        while (true) { 
            String source = dest + createExt(FileNo);
            File file = new File(source);
            if (!file.exists())
                break;
            FileInputStream fis = new FileInputStream(source);
            dataCopy(fis, fos, file.length());
            fis.close();
            FileNo++;
        }
        fos.close();
    }

    public static void main(String[] args) {
    }
}
