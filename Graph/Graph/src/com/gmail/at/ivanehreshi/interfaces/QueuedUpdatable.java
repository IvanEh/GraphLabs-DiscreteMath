package com.gmail.at.ivanehreshi.interfaces;

/**
 * used for objects which need to updated but not immediately
 * @author Ivan
 *
 */
public interface QueuedUpdatable {
	
/*
 *  query an update
 */
	public void queryUpdate();
	
/*
 *  if the object need to be updated(queryUpdate called) - update 
 */
	public void updateIfNeeded();
	
}
