package com.radixshock.radixcore.entity;

/**
 * Allows an entity to use tick markers.
 */
public interface ITickableEntity 
{
	/**
	 * @return This should return an integer defined in the entity that increments once per tick
	 * and is <b>never</b> reset. MCA calls this field <code>lifeTicks</code> in <code>AbstractEntity.java</code>.
	 */
	int getTimeAlive();
	
	/**
	 * Should call update() on all tick markers used by this entity.
	 */
	void updateTickMarkers();
}
