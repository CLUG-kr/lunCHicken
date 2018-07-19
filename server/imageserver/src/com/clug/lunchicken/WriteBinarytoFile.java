package com.clug.lunchicken;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WriteBinarytoFile {
	public int writeFile(String fileName, BufferedInputStream inputStream) throws IOException {
        File f = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
        int bytesWritten = 0;
        int b;
        while ((b = inputStream.read()) != -1) {
            bos.write(b);
            bytesWritten++;
        }
        bos.close();
        
        return bytesWritten;
    }

}