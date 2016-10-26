package com.yoshio3.services;

import java.io.Serializable;

public class FaceAttributes implements Serializable {
	private String gender;
	private Double age;

	public FaceAttributes() {
	}

	public FaceAttributes(String gender, Double age) {
		this.gender = gender;
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "FaceAttributes{" + "gender='" + gender + '\'' + ", age=" + age + '}';
	}
}
