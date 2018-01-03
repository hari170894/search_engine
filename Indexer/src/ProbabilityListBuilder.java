import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

public class ProbabilityListBuilder {
	
	public static void buildAndSaveProbabilities(int N) throws Exception{
		Random random=new Random();
		double randomValues[]=new double[N];
		double uniformValues[]=new double[N];
		double sum=0.0;
		for(int i=0;i<N;i++){
			
			int  n = random.nextInt(50) + 1;
			uniformValues[i]=(Math.log(1.0/N));
			randomValues[i]=n;
			sum+=n;
		}
		for(int i=0;i<N;i++){
			randomValues[i]/=sum;
			randomValues[i]=Math.log(randomValues[i]);
		}
		writeBinaryandTextFile(randomValues,"random.prior");

		writeBinaryandTextFile(uniformValues,"uniform.prior");
	}

	private static void writeBinaryandTextFile(double[] values, String fName) throws IOException {
		// TODO Auto-generated method stub
		File file;
		file=new File("saved/"+fName+".txt");
		RandomAccessFile binfile=new RandomAccessFile("saved/"+fName+".bin", "rw");
		FileOutputStream fos=new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		for(double i:values){
			bw.write(i+"\n");
			binfile.writeDouble(i);
		}
		bw.close();
		osw.close();
		fos.close();
		binfile.close();
	}
	public static double readBinaryfile(String name,int offset) {
		
		RandomAccessFile binfile;
		try {
			binfile = new RandomAccessFile("saved/"+name+".prior.bin", "rw");
			binfile.seek(offset);
			double d=binfile.readDouble();
			binfile.close();
			return d;
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
