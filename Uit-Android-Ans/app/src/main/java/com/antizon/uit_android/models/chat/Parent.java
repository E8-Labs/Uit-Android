package com.antizon.uit_android.models.chat;

import com.google.gson.annotations.SerializedName;

public class Parent{

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("bio")
	private String bio;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("account_status")
	private int accountStatus;

	@SerializedName("profile_image")
	private String profileImage;

	@SerializedName("profile_banner")
	private String profileBanner;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("web")
	private String web;

	@SerializedName("phone")
	private String phone;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("dob")
	private String dob;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("lang")
	private double lang;

	@SerializedName("job_title")
	private String jobTitle;

	@SerializedName("lat")
	private double lat;

	@SerializedName("greenhouse_access_token")
	private String greenhouseAccessToken;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setBio(String bio){
		this.bio = bio;
	}

	public String getBio(){
		return bio;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setAccountStatus(int accountStatus){
		this.accountStatus = accountStatus;
	}

	public int getAccountStatus(){
		return accountStatus;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public void setProfileBanner(String profileBanner){
		this.profileBanner = profileBanner;
	}

	public String getProfileBanner(){
		return profileBanner;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setWeb(String web){
		this.web = web;
	}

	public String getWeb(){
		return web;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setLang(int lang){
		this.lang = lang;
	}

	public double getLang(){
		return lang;
	}

	public void setJobTitle(String jobTitle){
		this.jobTitle = jobTitle;
	}

	public String getJobTitle(){
		return jobTitle;
	}

	public void setLat(int lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}

	public void setGreenhouseAccessToken(String greenhouseAccessToken){
		this.greenhouseAccessToken = greenhouseAccessToken;
	}

	public String getGreenhouseAccessToken(){
		return greenhouseAccessToken;
	}
}