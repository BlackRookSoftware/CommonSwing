/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.table;

import java.util.Comparator;
import java.util.Date;

/**
 * A class that contains table sorters/renderers for classes.
 * @author Matthew Tropiano
 * @since 2.5.0
 */
public interface RTableUtils
{
	/** Comparator for Booleans. */
	@SuppressWarnings("rawtypes")
	public static final Comparator<Enum> ENUM_COMPARATOR = new Comparator<Enum>()
	{
		@Override
		public int compare(Enum obj1, Enum obj2)
		{
			if (obj1 == obj2)
				return 0;
			else if (obj1 == null && obj2 != null)
				return -1;
			else if (obj1 != null && obj2 == null)
				return 1;
			else if (obj1.equals(obj2))
				return 0;
			else
				return obj1.ordinal() - obj2.ordinal();
		};
	};  

	/** Comparator for Booleans. */
	public static final Comparator<Boolean> BOOLEAN_COMPARATOR = new Comparator<Boolean>()
	{
		@Override
		public int compare(Boolean obj1, Boolean obj2)
		{
			if (obj1 == obj2)
				return 0;
			else if (obj1 == null && obj2 != null)
				return -1;
			else if (obj1 != null && obj2 == null)
				return 1;
			else if (obj1.equals(obj2))
				return 0;
			else
				return obj1.compareTo(obj2);
		};
	};  

	/** Comparator for Numbers. */
	public static final Comparator<Number> NUMBER_COMPARATOR = new Comparator<Number>()
	{
		@Override
		public int compare(Number obj1, Number obj2)
		{
			if (obj1 == obj2)
				return 0;
			else if (obj1 == null && obj2 != null)
				return -1;
			else if (obj1 != null && obj2 == null)
				return 1;
			else if (obj1.equals(obj2))
				return 0;
			else
				return obj1.doubleValue() > obj2.doubleValue() ? 1 : -1;
		};
	};
		
	/** Comparator for Dates. */
	public static final Comparator<Date> DATE_COMPARATOR = new Comparator<Date>()
	{
		@Override
		public int compare(Date obj1, Date obj2)
		{
			if (obj1 == obj2)
				return 0;
			else if (obj1 == null && obj2 != null)
				return -1;
			else if (obj1 != null && obj2 == null)
				return 1;
			else if (obj1.equals(obj2))
				return 0;
			else
				return obj1.getTime() > obj2.getTime() ? 1 : -1;
		};
	};  
}
