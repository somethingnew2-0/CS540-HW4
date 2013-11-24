import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This is the main method that will load the application.
 * 
 * DO NOT MODIFY
 */

public class HW4 {
	/**
	 * Creates a fresh instance of the classifier.
	 * 
	 * @return	a classifier
	 */
	private static NaiveBayesClassifier getNewClassifier() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifierImpl();
		return nbc;
	}

	/**
	 * Main method reads training and test file.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("usage: java HW4 <trainingFilename> <testFilename>");
		}

		// Output classifications on test data
		File trainingFile = new File(args[0]);
		File testFile = new File(args[1]);

		Instance[] trainingData = createInstances(trainingFile);
		Instance[] testData= createInstances(testFile);

		NaiveBayesClassifier nbc = getNewClassifier();
		nbc.train(trainingData, vocabularySize(trainingData, testData));

        System.out.println("Probability of HAM: " + nbc.p_l(Label.HAM));
        System.out.println("Probability of SPAM: " + nbc.p_l(Label.SPAM));
        
        System.out.println("Probability of 'great' given HAM: " + nbc.p_w_given_l("great", Label.HAM));
        System.out.println("Probability of 'friday' given SPAM: " + nbc.p_w_given_l("friday", Label.SPAM));
        
		double correct = 0.0;
		System.out.println("\nPrediction in the test data set");
		for (Instance inst : testData) {
			Label prediction = nbc.classify(inst.words);
			System.out.println(String.format("%s %s", prediction, inst.label));
			if(prediction == inst.label)	correct++;
		}
		System.out.println(String.format("Test accuracy: %.2f", (correct / testData.length)));
		
		nbc.show_informative_5words();
	}
	
	private static int vocabularySize(Instance[]... data) {
		Set<String> all = new HashSet<String>();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				for (int k = 0; k < data[i][j].words.length; k++) {
					all.add(data[i][j].words[k]);
				}
			}
		}
		return all.size();
	}

	
	/**
	 * Reads the lines of the inputted file, treats the first token as the label
	 * and cleanses the remainder, returning an array of instances.
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	private static Instance[] createInstances(File f) throws IOException {
		String[] ls = lines(f);
		Instance[] is = new Instance[ls.length];
		for (int i = 0; i < ls.length; i++) {
			String[] ws = cleanse(ls[i]).split("\\s");
			is[i] = new Instance();
			is[i].words = drop(ws, 1);
			is[i].label = Label.valueOf(ws[0].toUpperCase());
		}
		return is;
	}

	/**
	 * Some cleansing helps "thicken" the densities of the data model.
	 * 
	 * @param s
	 * @return	the string with punctuation removed and uniform case
	 */
	private static String cleanse(String s) {
		s = s.replace("?", " ");
		s = s.replace(".", " ");
		s = s.replace(",", " ");
		s = s.replace("/", " ");
		s = s.replace("!", " ");
		return s.toLowerCase();
	}

	public static String[] lines(File f) throws IOException {
		FileReader fr = new FileReader(f);
		String[] l = lines(fr);
		fr.close();
		return l;
	}
	
	public static String[] lines(Reader r) throws IOException {
		BufferedReader br = new BufferedReader(r);
		String s;
		List<String> data = new ArrayList<String>();
		while ((s = br.readLine()) != null && !s.isEmpty()) {
			data.add(s);
		}
		br.close();
		return data.toArray(new String[data.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] drop(T[] xs, int i) {
		T[] ys = (T[]) Array.newInstance(xs[0].getClass(), xs.length - i);
		System.arraycopy(xs, i, ys, 0, xs.length - 1);
		return ys;		
	}
}
