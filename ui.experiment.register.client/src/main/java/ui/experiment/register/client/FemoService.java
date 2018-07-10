package ui.experiment.register.client;

import java.util.List;

public interface FemoService {
	public List<Femo> findAll();
	
	public List<Femo> clearFemos();
}
