package com.blackrook.swing.list;

import java.util.Iterator;

import javax.swing.AbstractListModel;

import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.list.SortedMap;

/**
 * Common list model for RSortedMapLists.
 * @author Matthew Tropiano
 * @param <K> the map key type.
 * @param <V> the value type.
 * @since 2.7.0 
 */
public class RSortedMapListModel<K extends Comparable<K>, V extends Object> extends AbstractListModel<ObjectPair<K, V>> implements Iterable<ObjectPair<K, V>>
{
	private static final long serialVersionUID = 3906580579881922706L;
	
	/** Data set. */
	private SortedMap<K, V> data;

	RSortedMapListModel()
	{
		this(new SortedMap<>());
	}
	
	RSortedMapListModel(SortedMap<K, V> data)
	{
		this.data = data;
	}
	
	/**
	 * Clears the model.
	 */
	public void clear()
	{
		data.clear();
	}

	/**
	 * Adds an object to the model and sorts it insertion-style.
	 * @param key the reference key.
	 * @param value the corresponding value.
	 */
	public void add(K key, V value)
	{
		data.add(key, value);
		int keyIndex = data.getIndexOf(key);
		fireIntervalAdded(this, keyIndex, keyIndex);
	}

	/**
	 * Replaces the value of a node in this structure.
	 * If it doesn't exist, it will be added.
	 * @param key the reference key.
	 * @param value the corresponding value.
	 */
	public void replace(K key, V value)
	{
		data.replace(key, value);
		int keyIndex = data.getIndexOf(key);
		fireContentsChanged(this, keyIndex, keyIndex);
	}

	/**
	 * Checks if an object exists in this map via comparison binary-search style.
	 * @param key the reference key.
	 * @return true if this contains the key, false if not.
	 */
	public boolean contains(K key)
	{
		return data.contains(key);
	}

	/**
	 * Checks if an object exists in this map via comparison binary-search style.
	 * @param key the reference key.
	 * @return the index of the desired key or -1 if it does not exist.
	 */
	public int getIndexOf(K key)
	{
		return data.getIndexOf(key);
	}

	/**
	 * Checks if an object exists in this map via comparison linear-search style.
	 * @param value the desired value.
	 * @return the index of the desired value or -1 if it does not exist.
	 */
	public int getIndexOfValue(V value)
	{
		return data.getIndexOfValue(value);
	}

	/**
	 * Returns a value using a key.
	 * @param key the requested key.
	 * @return the corresponding value, or null of the key is not found.
	 */
	public V get(K key)
	{
		return data.get(key);
	}

	/**
	 * Gets the key of a model item.
	 * @param index the index of an item in the model.
	 * @return the corresponding key or null if no such index.
	 */
	public K getKey(int index)
	{
		ObjectPair<K, V> pair = getElementAt(index);
		if (pair != null)
			return pair.getKey();
		else
			return null;
	}

	/**
	 * Gets the value of a model item.
	 * @param index the index of an item in the model.
	 * @return the corresponding value or null if no such index.
	 */
	public V getValue(int index)
	{
		ObjectPair<K, V> pair = getElementAt(index);
		if (pair != null)
			return pair.getValue();
		else
			return null;
	}

	/**
	 * Removes a value from this Map.
	 * @param key the requested key.
	 * @return the removed corresponding value or null it wasn't in the Map.
	 */
	public V remove(K key)
	{
		int index = data.getIndexOf(key);
		V out = data.remove(key);
		if (out != null)
			fireIntervalRemoved(this, index, index);
		return out;
	}

	/**
	 * Removes a value from the map.
	 * @param value the value to search for and remove. 
	 * @return true if removed, false if not.
	 */
	public boolean removeByValue(V value)
	{
		int index = data.getIndexOfValue(value);
		boolean out = data.removeByValue(value);
		if (out)
			fireIntervalRemoved(this, index, index);
		return out;
	}

	/**
	 * Removes the first object from this Map.
	 * @return the removed object or null the Map is empty.
	 */
	public V removeFirst()
	{
		V out = data.removeFirst();
		if (out != null)
			fireIntervalRemoved(this, 0, 0);
		return out;
	}

	/**
	 * Removes the last object from this Map.
	 * @return the removed object or null the Map is empty.
	 */
	public V removeLast()
	{
		V out = data.removeLast();
		if (out != null)
			fireIntervalRemoved(this, data.size() - 1, data.size() - 1);
		return out;
	}

	/**
	 * Removes the key, value pair at a particular index and returns the value.
	 * @param index the desired index.
	 * @return the removed object or null the Map is empty.
	 */
	public V removeValueAtIndex(int index)
	{
		V out = data.removeValueAtIndex(index);
		if (out != null)
			fireIntervalRemoved(this, index, index);
		return out;
	}

	@Override
	public int getSize()
	{
		return data.size();
	}

	@Override
	public ObjectPair<K, V> getElementAt(int index)
	{
		return data.getByIndex(index);
	}

	@Override
	public Iterator<ObjectPair<K, V>> iterator()
	{
		return data.iterator();
	}

}
