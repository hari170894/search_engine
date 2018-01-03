import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class VByteEncoder {
	public static void encode(RandomAccessFile out, int value) throws IOException {
		if(value<0) {
			throw new IllegalArgumentException("Only can encode VByte of positive values");
		}
		while( value > 127) {
			out.write((value & 127));
			value>>>=7;
		}
		out.write((value|0x80));
	}
	
	public static int decode(RandomAccessFile in) throws IOException {
		int out = 0;
		int shift=0;
		long readbyte = in.read(); if(readbyte==-1) throw new EOFException();
		
		while( (readbyte & 0x80)==0) {
			if(shift>=50) { // We read more bytes than required to load the max long
				throw new IllegalArgumentException();
			}
			
			out |= (readbyte & 127) << shift;
			
			readbyte = in.read(); if(readbyte==-1) throw new EOFException();
			
			shift+=7;
		}
		out |= (readbyte & 127) << shift;
		return out;
	}

}
