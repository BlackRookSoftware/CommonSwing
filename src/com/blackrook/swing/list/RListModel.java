package com.blackrook.swing.list;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import javax.swing.AbstractListModel;

import com.blackrook.commons.list.List;

/**
 * Common list model for RLists.
 * @author Matthew Tropiano
 * @param <T> the object type contained in the list.
 * @since 2.7.0 
 */
public class RListModel<T> extends AbstractListModel<T> implements Iterable<T>
{
	private static final long serialVersionUID = -4038392735611615114L;

	/** Data set. */
	private List<T> data;

	RListModel()
	{
		this(new List<>());
	}
	
	RListModel(List<T> data)
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
	 * Adds an object to the end of the model.
	 * @param object the object to add.
	 */
	public void add(T object)
	{
		data.add(object);
	}

	/**
	 * Adds an object at an index. 
	 * If index is greater than or equal to the size, it will add it at the end.
	 * If index is less than 0, it won't add it.
	 * @param index the index to add this at.
	 * @param object the object to add.
	 */
	public void add(int index, T object)
	{
		data.add(index, object);
	}

	/**
	 * Sets an object at an index. Used for replacing contents.
	 * If index is greater than or equal to the size, it will add it at the end.
	 * If index is less than 0, this does nothing.
	 * @param index the index to set this at.
	 * @param object the object to add.
	 */
	public void replace(int index, T object)
	{
		data.replace(index, object);
	}

	/**
	 * Removes an object from the model, if it exists in the model.
	 * Sequential search.
	 * @param object the object to search for and remove.
	 * @return true if removed, false if not in the model.
	 * @throws NullPointerException if object is null.
	 */
	public boolean remove(T object)
	{
		return data.remove(object);
	}

	/**
	 * Removes an object from an index in the model and shifts 
	 * everything after it down an index position.
	 * @param index the target index.
	 * @return null if the index is out of bounds or the object at that index.
	 */
	public T removeIndex(int index)
	{
		return data.removeIndex(index);
	}

	/**
	 * Checks if an object exists in this model.
	 * Implementation may dictate how the object is searched.
	 * @param object the object to look for.
	 * @return true if an equal object exists, or false if not.
	 * @see #getIndexOf(Object)
	 */
	public boolean contains(T object)
	{
		return data.contains(object);
	}

	/**
	 * Gets the index of an object, presumably in the model.
	 * @param object the object to search for.
	 * @return the index of the object if it is in the model, or -1 if it is not present.
	 * @throws NullPointerException if object is null.
	 */
	public int getIndexOf(T object)
	{
		return data.getIndexOf(object);
	}

	/**
	 * Gets the index of an object, presumably in the model via binary search.
	 * Expects the contents of this model to be sorted.
	 * @param object the object to search for.
	 * @param comparator the comparator to use for comparison or equivalence.
	 * @return the index of the object if it is in the model, or less than 0 if it is not present.
	 * If less than 0, it is equal to where it would be added in the array. Add 1 then negate.
	 * @throws NullPointerException if object or comparator is null.
	 */
	public int search(T object, Comparator<? super T> comparator)
	{
		return search(object, comparator);
	}

	/**
	 * Sorts this model using NATURAL ORDERING.
	 * Calls {@link Arrays#sort(Object[], int, int)} on the internal storage array.
	 */
	public void sort()
	{
		data.sort();
	}

	/**
	 * Sorts this model using a comparator.
	 * Calls {@link Arrays#sort(Object[], int, int, Comparator)} on the internal storage array, using the specified comparator.
	 * @param comparator the comparator to use.
	 */
	public void sort(Comparator<? super T> comparator)
	{
		data.sort(comparator);
	}

	/**
	 * Sorts this model using NATURAL ORDERING.
	 * Calls {@link Arrays#sort(Object[], int, int)} on the internal storage array.
	 * @param startIndex the starting index of the sort.
	 * @param endIndex the ending index of the sort, exclusive.
	 */
	public void sort(int startIndex, int endIndex)
	{
		data.sort(startIndex, endIndex);
	}

	/**
	 * Sorts this model using a comparator.
	 * Calls {@link Arrays#sort(Object[], int, int, Comparator)} on the internal storage array, using the specified comparator.
	 * @param comparator the comparator to use.
	 * @param startIndex the starting index of the sort.
	 * @param endIndex the ending index of the sort, exclusive.
	 */
	public void sort(Comparator<? super T> comparator, int startIndex, int endIndex)
	{
		data.sort(comparator, startIndex, endIndex);
	}

	/**
	 * Swaps the contents of two indices in the model.
	 * <p>If index0 is equal to index1, this does nothing.
	 * <p>If one index is outside the bounds of this model 
	 * (less than 0 or greater than or equal to {@link #getSize()}),
	 * this throws an exception. 
	 * @param index0 the first index.
	 * @param index1 the second index.
	 * @throws IllegalArgumentException if one index is outside the bounds of this model 
	 * (less than 0 or greater than or equal to {@link #getSize()}).
	 */
	public void swap(int index0, int index1)
	{
		data.swap(index0, index1);
	}

	/**
	 * Moves the object at an index in this model to another index,
	 * shifting the contents between the two selected indices in this model back or forward.
	 * <p>If sourceIndex is equal to targetIndex, this does nothing.
	 * <p>If one index is outside the bounds of this model 
	 * (less than 0 or greater than or equal to {@link #getSize()}),
	 * this throws an exception. 
	 * @param sourceIndex the first index.
	 * @param targetIndex the second index.
	 * @throws IllegalArgumentException if one index is outside the bounds of this model 
	 * (less than 0 or greater than or equal to {@link #getSize()}).
	 */
	public void shift(int sourceIndex, int targetIndex)
	{
		data.swap(sourceIndex, targetIndex);
	}

	/**
	 * Randomizes the order of the objects in this model,
	 * using a random number generator.
	 * @param random the random number generator to use.
	 */
	public void shuffle(Random random)
	{
		data.shuffle(random);
	}

	@Override
	public int getSize()
	{
		return data.size();
	}

	@Override
	public T getElementAt(int index)
	{
		return data.getByIndex(index);
	}

	@Override
	public Iterator<T> iterator()
	{
		return data.iterator();
	}

}
