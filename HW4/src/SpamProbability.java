
public class SpamProbability implements Comparable<SpamProbability> {
	public String value;
	public int spamCount, totalCount; // hamCount = totalCount - spamCount;
	
	public SpamProbability(String value) {
		this.value = value;
	}
	
	public double informativeness() {
		double spamProbablility = spamCount/(double)totalCount;
		double hamProbability = (totalCount-spamCount)/(double)totalCount;
		return Math.max(spamProbablility/hamProbability, hamProbability/spamProbablility);
	}

	@Override
	public int compareTo(SpamProbability other) {
		return Double.compare(other.informativeness(), informativeness());
	}
}
