import java.util.*;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier {
	private static final double DELTA = 0.00001;
	private int vocabularySize;
	private SpamProbability totalProbability;
	private Map<String, SpamProbability> spamProbabilities;
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	@Override
	public void train(Instance[] trainingData, int v) {
		this.vocabularySize = v;
		this.totalProbability = new SpamProbability("");
		this.spamProbabilities = new HashMap<String, SpamProbability>(vocabularySize);
		for (Instance instance : trainingData) {
//			Set<String> words = new HashSet<String>();
			for (String word : instance.words) {
				SpamProbability probability = spamProbabilities.get(word);
				if (probability == null) {
					probability = new SpamProbability(word);
					spamProbabilities.put(word, probability);
				}
//				if (!words.contains(word)) {
					if(Label.SPAM.equals(instance.label)) {
						probability.spamCount++;
					}
					probability.totalCount++;
//					words.add(word);
//				}
			}
			if(Label.SPAM.equals(instance.label)) {
				totalProbability.spamCount++;
			}
			totalProbability.totalCount++;
		}		
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(SPAM) or P(HAM)
	 */
	@Override
	public double p_l(Label label) {
		return (Label.SPAM.equals(label)?totalProbability.spamCount:(totalProbability.totalCount-totalProbability.spamCount))/(double)totalProbability.totalCount;
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|SPAM) or P(word|HAM)
	 */
	@Override
	public double p_w_given_l(String word, Label label) {
		SpamProbability probability = spamProbabilities.get(word);
		if (probability == null) {
			return 0;
		} else {
			return ((Label.SPAM.equals(label)?probability.spamCount:(probability.totalCount-probability.spamCount))+DELTA)/((double)probability.totalCount + vocabularySize*DELTA);
		}
	}
	
	/**
	 * Classifies an array of words as either SPAM or HAM. 
	 */
	@Override
	public Label classify(String[] words) {
		// Implement
		return null;
	}
	
	/**
	 * Print out 5 most informative words.
	 */
	public void show_informative_5words() {
		PriorityQueue<SpamProbability> maxQueue = new PriorityQueue<SpamProbability>(vocabularySize);
		for (SpamProbability spamProbability : spamProbabilities.values()) {
			maxQueue.add(spamProbability);
		}
		for (int i = 0; i < 5 && !maxQueue.isEmpty(); i++) {
			SpamProbability spamProbability = maxQueue.poll();
			System.out.println(spamProbability.value + " " + spamProbability.informativeness());
		}
	}
}
