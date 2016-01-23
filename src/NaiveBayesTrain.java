import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class NaiveBayesTrain {
	static File train2list = new File("data\\train2.list");
	static File train2label = new File("data\\train2.rlabelclass");
	static String traindataroot = "data\\train2\\";
	static File Chinese_stopwordFile = new File("src\\Chinese-stop-words.dic");
	static List<String> stopwords = new ArrayList<String>();
	static double []P = new double[2];//P[0]:-1,P[1]:+1
	static Map<String, Double>wordP0 = new HashMap<String,Double>();
	static Map<String, Double>wordP1 = new HashMap<String,Double>();
	//dictionary of all words
	static Vector<String>dictionary = new Vector<String>();
	//label of file
	static Map<String,Integer> trainlabel = new HashMap<String, Integer>();
	static int positivecount = 0;
	static int negativecount = 0;
	
	
	public static void main(String[]args){
		long begintime = System.currentTimeMillis();
		try {
			//read Chinese stop word file
			InputStreamReader read1 = new InputStreamReader(new FileInputStream(Chinese_stopwordFile));
			BufferedReader reader1 = new BufferedReader(read1);
			String line = null;
			while((line = reader1.readLine())!=null){
				stopwords.add(line.trim());
			}
			read1.close();
			reader1.close();
			
			//read the label of the file
			InputStreamReader train2labelreader = new InputStreamReader(new FileInputStream(train2label));
			BufferedReader train2labelbufferreader = new BufferedReader(train2labelreader);
			line = null;
			while((line = train2labelbufferreader.readLine())!=null){
				String []tmp = line.split(" ");
				trainlabel.put(tmp[0].trim(), Integer.valueOf(tmp[1].trim()));
				if(Integer.valueOf(tmp[1].trim())==1)positivecount++;
				else negativecount++;
			}
			P[0] = ((double)negativecount)/((double)(negativecount+positivecount));
			P[1] = ((double)positivecount)/((double)(negativecount+positivecount));
			train2labelreader.close();
			train2labelbufferreader.close();
			
			//read the list of traindata
			InputStreamReader train2listreader = new InputStreamReader(new FileInputStream(train2list));
			BufferedReader train2listbufferreader = new BufferedReader(train2listreader);
			line = null;
			int count = 0;
			while((line = train2listbufferreader.readLine())!=null){
				//read traindata one by one
				File traindata = new File(traindataroot+line);
				InputStreamReader trandatareader = new InputStreamReader(new FileInputStream(traindata));
				BufferedReader traindatabufferreader = new BufferedReader(trandatareader);
				String line2 = null;
				//temp wordlist
				List<String>tmpwordlist = new ArrayList<String>();
				while((line2 = traindatabufferreader.readLine())!=null){
					IKSegmenter ik = new IKSegmenter(new StringReader(line2), true);
					Lexeme lexeme = null;
					String word = null;
					while((lexeme=ik.next())!=null){
						word = lexeme.getLexemeText().trim();
						if(dictionary.contains(word)==false&&stopwords.contains(word)==false)
							dictionary.add(word);
						if(stopwords.contains(word)==false){
							if(count<positivecount){
								if(tmpwordlist.contains(word)==false){
									if(wordP1.containsKey(word))wordP1.replace(word, wordP1.get(word)+1);
									else wordP1.put(word,1.0);
									tmpwordlist.add(word);
								}
							}
							else{
								if(tmpwordlist.contains(word)==false){
									if(wordP0.containsKey(word))wordP0.replace(word, wordP0.get(word)+1);
									else wordP0.put(word,1.0);
									tmpwordlist.add(word);
								}
							}
						}
					}
				}
				if(count==0||(count+1)%((positivecount+negativecount)/100)==0)System.out.println("Training: "+(count+1)/((positivecount+negativecount)/100)+"%");
				count++;
				trandatareader.close();
				traindatabufferreader.close();
			}
			train2listreader.close();
			train2listbufferreader.close();
			
			File trainresult0 = new File("trainresultwordP0.txt");
			File trainresult1 = new File("trainresultwordP1.txt");
			FileOutputStream fos0 = new FileOutputStream(trainresult0);
			FileOutputStream fos1 = new FileOutputStream(trainresult1);
			for(String a:wordP0.keySet()){
				String input = a+":"+wordP0.get(a)+"\n";
				fos0.write(input.getBytes());
			}
			fos0.close();
			for(String a:wordP1.keySet()){
				String input = a+":"+wordP1.get(a)+"\n";
				fos1.write(input.getBytes());
			}
			fos1.close();
			
			File trainresult = new File("trainresult.txt");
			FileOutputStream fos = new FileOutputStream(trainresult);
			String result = positivecount+"\n"+negativecount+"\n"+P[0]+"\n"+P[1]+"\n";
			fos.write(result.getBytes());
			fos.close();
			long endtime = System.currentTimeMillis();
			System.out.println("Train costs "+(endtime-begintime)/1000+"s");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
