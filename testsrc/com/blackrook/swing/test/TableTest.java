/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.test;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.swing.JFrame;

import com.blackrook.swing.SwingCommon;
import com.blackrook.swing.table.RTable;
import com.blackrook.swing.table.RTable.SelectPolicy;
import com.blackrook.swing.table.TableDescriptor;

public final class TableTest
{
	public static void main(String[] args)
	{
		SwingCommon.setSystemLAF();
		
		RTable<Junk> table = new RTable<Junk>(Junk.class, SelectPolicy.MULTIPLE_INTERVAL){
			private static final long serialVersionUID = -3784431132407163693L;
			@Override
			public void onAdd(Junk object)
			{
				System.out.println("ADD");
			}
			@Override
			public void onRemove(Junk object)
			{
				System.out.println("REMOVE");
			}
			@Override
			public void onUpdate(Junk object)
			{
				System.out.println("UPDATE "+object.name);
			}
			@Override
			public void onSelectRow()
			{
				System.out.println("SELECT "+Arrays.toString(getSelectedRows()));
			}
	};
		table.addItem(new Junk(1, "Matt", "Matt T", true));
		table.addItem(new Junk(2, "Robert", "Robert Boberson", true));
		table.addItem(new Junk(3, "Sue", "Sue Derkins", false));
		table.addItem(new Junk(4, "Dirk", "Dirk Diggler", true));
		table.addItem(new Junk(5, "John", "John Egbert", false));

		JFrame f = SwingCommon.createSimpleFrame("Test", table);
		f.setVisible(true);
	}
	
	public static class Junk
	{
		private int id;
		private String name;
		private String fullName;
		private String phone;
		private boolean active;
		private Date startDate;
		
		public Junk(int id, String name, String fullName, boolean active)
		{
			super();
			this.id = id;
			this.name = name;
			this.fullName = fullName;
			this.phone = "";
			this.active = active;
			this.startDate = new Date((new Random()).nextLong() % 10000000000L);
		}
		
		@TableDescriptor(name = "ID", hidden = true)
		public int getId()
		{
			return id;
		}

		@TableDescriptor(name = "Name", order = 0, width = 1)
		public String getName()
		{
			return name;
		}

		@TableDescriptor(name = "Full Name", order = 1, width = 2)
		public String getFullName()
		{
			return fullName;
		}

		@TableDescriptor(name = "Phone #", order = 2, width = 1, sortable = false)
		public String getPhone()
		{
			return phone;
		}

		@TableDescriptor(name = "Active?", order = 3, width = 1, tip = "Is this active?")
		public boolean getActive()
		{
			return active;
		}

		@TableDescriptor(order = 3, width = 4)
		public Date getStartDate()
		{
			return startDate;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public void setPhone(String phone)
		{
			this.phone = phone;
		}

		
	}

}
