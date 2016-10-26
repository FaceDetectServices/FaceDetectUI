package com.yoshio3.services;

public class EmotionAttributes {
	private Double anger;
	private Double contempt;
	private Double disgust;
	private Double fear;
	private Double happiness;
	private Double neutral;
	private Double sadness;
	private Double surprise;

	public EmotionAttributes() {
	}

	public EmotionAttributes(Double anger, Double contempt, Double disgust, Double fear,
			Double happiness, Double neutral, Double sadness, Double surprise) {
		this.anger = anger;
		this.contempt = contempt;
		this.disgust = disgust;
		this.fear = fear;
		this.happiness = happiness;
		this.neutral = neutral;
		this.sadness = sadness;
		this.surprise = surprise;
	}

	public Double getAnger() {
		return anger;
	}

	public void setAnger(Double anger) {
		this.anger = anger;
	}

	public Double getContempt() {
		return contempt;
	}

	public void setContempt(Double contempt) {
		this.contempt = contempt;
	}

	public Double getDisgust() {
		return disgust;
	}

	public void setDisgust(Double disgust) {
		this.disgust = disgust;
	}

	public Double getFear() {
		return fear;
	}

	public void setFear(Double fear) {
		this.fear = fear;
	}

	public Double getHappiness() {
		return happiness;
	}

	public void setHappiness(Double happiness) {
		this.happiness = happiness;
	}

	public Double getNeutral() {
		return neutral;
	}

	public void setNeutral(Double neutral) {
		this.neutral = neutral;
	}

	public Double getSadness() {
		return sadness;
	}

	public void setSadness(Double sadness) {
		this.sadness = sadness;
	}

	public Double getSurprise() {
		return surprise;
	}

	public void setSurprise(Double surprise) {
		this.surprise = surprise;
	}

	@Override
	public String toString() {
		return "EmotionAttributes{" + "anger=" + anger + ", contempt=" + contempt
				+ ", disgust=" + disgust + ", fear=" + fear + ", happiness=" + happiness
				+ ", neutral=" + neutral + ", sadness=" + sadness + ", surprise="
				+ surprise + '}';
	}
}
