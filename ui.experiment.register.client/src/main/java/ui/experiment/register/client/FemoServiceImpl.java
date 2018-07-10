package ui.experiment.register.client;

import java.util.ArrayList;
import java.util.List;

public class FemoServiceImpl implements FemoService {
	
	/**
	 *	A list of femos
	 */
	private List<Femo> femos = new ArrayList<Femo>();

	/**
	 *	The constructor 
	 *	@param user ID
	 *		the ID of the user
	 */
	public FemoServiceImpl(String userID) {		
		femos = FemoData.getAllFemos(userID);
	}

	/**
	 * Retrieve all femos
	 * 
	 * @return 
	 * 		the list containing all femos
	 */
	public List<Femo> findAll() {
		return femos;
	}
	
	/**
	 * Clears the list of femos
	 * 
	 * @return 
	 * 		the list of femos
	 */
	public List<Femo> clearFemos() {
		femos = new ArrayList<Femo>();
		return femos;
	}
}