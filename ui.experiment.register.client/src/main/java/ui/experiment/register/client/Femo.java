package ui.experiment.register.client;

public class Femo {
	
	/**
	 * The femo's name
	 */
	private String femoName;
	
	/**
	 * The femo's id
	 */
	private String femoID;	
	
	/**
	 * The default constructor
	 */
	public Femo() {}
	
	/**
	 * The constructor
	 * @param femoName 
	 * 		the femo's name
	 * @param femoID
	 * 		the femo's id 
	 */
	public Femo(String femoName, String femoID) {
		this.femoName = femoName;
		this.femoID = femoID;
	}

	/**
	 * Getter method for femoName 
	 */
	public String getFemoName() {
		return femoName;
	}

	/**
	 * Setter method for femoName 
	 */
	public void setFemoName(String femoName) {
		this.femoName = femoName;
	}

	/**
	 * Getter method for femoID 
	 */
	public String getFemoID() {
		return femoID;
	}
	
	/**
	 * Setter method for femoID 
	 */
	public void setFemoID(String femoID) {
		this.femoID = femoID;
	}
}