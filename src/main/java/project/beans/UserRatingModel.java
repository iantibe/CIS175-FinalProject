package project.beans;

public class UserRatingModel {
	private String userName;
	private String rating;
	public UserRatingModel(String userName, String rating) {
		super();
		this.userName = userName;
		this.rating = rating;
	}
	public UserRatingModel() {
		super();
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}

}
