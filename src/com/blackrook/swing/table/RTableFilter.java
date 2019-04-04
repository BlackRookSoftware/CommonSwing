/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.table;

import javax.swing.RowFilter;

/**
 * The table filter for RTables. When set on the table, rows are filtered.
 * @author Matthew Tropiano
 * @param <T> matching object.
 * @since 2.5.0
 */
public abstract class RTableFilter<T> extends RowFilter<RTableModel<T>, Integer>
{

	@Override
	public boolean include(Entry<? extends RTableModel<T>, ? extends Integer> entry)
	{
		return includeItem(entry.getModel().getRowAt(entry.getIdentifier()));
	}
	
	/**
	 * Called to check if an item in included in the table.
	 * @param item the item to test.
	 * @return true to include, false to not include.
	 */
	public abstract boolean includeItem(T item);

}
