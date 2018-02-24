package model;

import java.util.ArrayList;

import observer.Observable;
import observer.Observer;

/*
 * We took the same behavior as in the TP session for that class
 */
public abstract class AbstractWorld implements Observable {

	public ArrayList<Observer> observerTab = new ArrayList<>();	

	// The override methods from Observable
	@Override
	public void addObserver(Observer obs) {
		observerTab.add(obs);
		
	}

	@Override
	public void removeObserver() {
		observerTab = new ArrayList<>();	
		
	}

	@Override
	public void notifyObserver() {
		for(Observer obs : observerTab)
		{
			obs.update();
		}	
	}
	
	@Override
	public void isWonObserver()
	{
		for(Observer obs : observerTab)
		{
			obs.isWon();
		}	
	}

}
