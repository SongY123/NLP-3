import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class NaiveBayesTestAndEvaluate {
	static File test2label = new File("data\\test2.rlabelclass");
	static String testdataroot = "data\\test2\\";
	static double []P = new double[2];//P[0]:-1,P[1]:+1
	static Map<String, Double>wordP0 = new HashMap<String,Double>();
	static Map<String, Double>wordP1 = new HashMap<String,Double>();
	static Map<String,Integer> testlabel = new HashMap<String, Integer>();
	static Vector<String>dictionary = new Vector<String>();
	static int positivecount = 0;
	static int negativecount = 0;
	static File trainresult0 = new File("trainresultwordP0.txt");
	static File trainresult1 = new File("trainresultwordP1.txt");
	static File trainresult = new File("trainresult.txt");
	static File resultfile = new File("resultfile.rlabelclass");
	static public int Test(String pathname){
		double []p = new double[2];
		p[0] = P[0];
		p[1] = P[1];
		try {
			String line = null;
			InputStreamReader r = new InputStreamReader(new FileInputStream(new File(pathname)));
			BufferedReader r2 = new BufferedReader(r);
			line = null;
			List<String> wordlist = new ArrayList<String>();
			while((line = r2.readLine())!=null){
				IKSegmenter ik = new IKSegmenter(new StringReader(line), true);
				Lexeme lexeme = null;
				String word = null;
				while((lexeme=ik.next())!=null){
					word = lexeme.getLexemeText().trim();
					if(wordlist.contains(word)==false)wordlist.add(word);
				}
			}
			for(String a:dictionary){
				if(wordlist.contains(a)){
					if(wordP0.containsKey(a))
						p[0] = p[0]*(wordP0.get(a)+(double)1)/(double)(negativecount+2);
					else p[0] = p[0]*(double)1/(double)(negativecount+2);
					if(wordP1.containsKey(a))
						p[1] = p[1]*(wordP1.get(a)+(double)1)/(double)(positivecount+2);
					else p[1] = p[1]*(double)1/(double)(positivecount+2);
				}
				else {
					if(wordP0.containsKey(a))
						p[0] = p[0]*((double)1-(wordP0.get(a)+(double)1)/(double)(negativecount+2));
					else p[0] = p[0]*((double)1-(double)1/(double)(negativecount+2));
					if(wordP1.containsKey(a))
						p[1] = p[1]*((double)1-(wordP1.get(a)+(double)1)/(double)(positivecount+2));
					else 
						p[1] = p[1]*((double)1-(double)1/(double)(positivecount+2));
				}
			}
			r.close();
			r2.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(p[0]<p[1])return 1;
		else return -1;
	}
	
	public static void main(String[]args){
		//read the label of the file
		
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		int count2 = 0;
		long begintime = System.currentTimeMillis();
		try {
			String line = null;
			//read positivecount,negative,P[0] and P[1]
			InputStreamReader r00 = new InputStreamReader(new FileInputStream(trainresult));
			BufferedReader r01 = new BufferedReader(r00);
			positivecount = Integer.valueOf(r01.readLine());
			negativecount = Integer.valueOf(r01.readLine());
			P[0] = Double.valueOf(r01.readLine());
			P[1] = Double.valueOf(r01.readLine());
			r00.close();
			r01.close();
			
			//read wordP0
			InputStreamReader r10 = new InputStreamReader(new FileInputStream(trainresult0));
			BufferedReader r11 = new BufferedReader(r10);
			while((line = r11.readLine())!=null){
				String []result = line.split(":");
				dictionary.add(result[0]);
				wordP0.put(result[0], Double.valueOf(result[1]));
			}
			r10.close();
			r11.close();
			
			//read wordP1
			line = null;
			InputStreamReader r20 = new InputStreamReader(new FileInputStream(trainresult1));
			BufferedReader r21 = new BufferedReader(r20);
			while((line = r21.readLine())!=null){
				String []result = line.split(":");
				if(dictionary.contains(result[0].trim())==false)dictionary.add(result[0]);
				wordP1.put(result[0], Double.valueOf(result[1]));
			}
			r20.close();
			r21.close();
			
			FileOutputStream fosresult = new FileOutputStream(resultfile);
			InputStreamReader test2labelreader = new InputStreamReader(new FileInputStream(test2label));
			BufferedReader test2labelbufferreader = new BufferedReader(test2labelreader);
			
			while((line = test2labelbufferreader.readLine())!=null){
				if(count2%40==0)System.out.println("Testing: "+(count2+1)/40+"%");
				count2++;
				String []tmp = line.split(" ");
				int result = Test(testdataroot+tmp[0].trim());
				if(result==1){
					fosresult.write((tmp[0]+" "+"+1\n").getBytes());
					if(Integer.valueOf(tmp[1].trim())==1)
						TP++;
					else 
						FP++;
				}
				else{//result==-1
					fosresult.write((tmp[0]+" "+"-1\n").getBytes());
					if(Integer.valueOf(tmp[1].trim())==-1)
						TN++;
					else 
						FN++;
				}
			}
			fosresult.close();
			test2labelreader.close();
			test2labelbufferreader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int correctcount = TP+TN;
		int wrongcount = FP+FN;
		double Accuracy = (double)(TP+TN)/(double)(TP+TN+FP+FN);
		double Precision = (double)TP/(double)(TP+FP);
		double Recall = (double)TP/(double)(TP+FN);
		double F_measure = ((double)2)/(((double)1)/Recall+((double)1)/Precision);
		
		long endtime = System.currentTimeMillis();
		System.out.println("Test costs "+(endtime-begintime)/1000+"s");
		System.out.println("Accuracy is: "+Accuracy);
		System.out.println("Precision is: "+Precision);
		System.out.println("Recall is: "+Recall);
		System.out.println("F-measure is: "+F_measure);
	}
}
