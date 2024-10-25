package de.doppelkool.knockit.service;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class GameDesignService {

	private static GameDesignService instance = null;



	public static GameDesignService getInstance() {
		if(instance == null) {
			instance = new GameDesignService();
		}
		return instance;
	}

}
