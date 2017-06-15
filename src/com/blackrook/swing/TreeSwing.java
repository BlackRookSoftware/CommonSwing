package com.blackrook.swing;

import java.awt.Container;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.border.Border;

import com.blackrook.commons.linkedlist.Queue;

/**
 * The main TreeSwing class.
 * TreeSwing is used for creating complex layouts quickly, without the tedium of creating
 * panels and layouts and borders.
 * @author Matthew Tropiano
 * @since 2.7.0
 */
public class TreeSwing
{
	/** Root node in the TreeSwing. */
	protected Branch root; 
	
	/**
	 * Builds the panels and returns the head panel.
	 * @return the head panel.
	 */
	public Container build()
	{
		JPanel out = new JPanel();
		buildRecurse(out, root);
		return out;
	}

	// Build recurse.
	private void buildRecurse(Container container, Node node)
	{
		if (node instanceof Leaf)
		{
			Leaf leaf = (Leaf)node;
			container.add(leaf.container, leaf.constraints);
		}
		else if (node instanceof Branch)
		{
			Branch branch = (Branch)node;
			JPanel branchPanel = new JPanel();
			
			if (branch.layout != null)
				branchPanel.setLayout(branch.layout);
			
			if (branch.border != null)
				branchPanel.setBorder(branch.border);
			
			for (Node edge : branch.edges)
				buildRecurse(branchPanel, edge);
			
			container.add(branchPanel, branch.constraints);
		}
		else
			throw new RuntimeException("INTERNAL ERROR");
	}

	/**
	 * Starts a branch in the TreeSwing.
	 * @param root the layout to use for this branch's children.
	 * @return a new branch.
	 */
	public static TreeSwing trunk(Branch root)
	{
		TreeSwing out = new TreeSwing();
		out.root = root;
		return out;
	}

	/**
	 * Starts a new branch off of this branch. 
	 * @param constraints the constraints to use for the added branch (using parent layout).
	 * @param border the border to add to the panel.
	 * @param layout the layout to use for this branch's children.
	 * @param edges the edges on the branch.
	 * @return a new branch.
	 */
	public static Branch branch(Object constraints, Border border, LayoutManager layout, Node ... edges)
	{
		Branch out = new Branch(layout, border, constraints);
		for (Node e : edges)
			out.edges.add(e); 
		return out;
	}

	/**
	 * Starts a new branch off of this branch. 
	 * @param border the border to add to the panel.
	 * @param layout the layout to use for this branch's children.
	 * @return a new branch.
	 */
	public static Branch branch(Border border, LayoutManager layout, Node ... edges)
	{
		Branch out = new Branch(layout, border, null);
		for (Node e : edges)
			out.edges.add(e); 
		return out;
	}

	/**
	 * Starts a new branch off of this branch. 
	 * @param constraints the constraints to use for the added branch (using parent layout).
	 * @param layout the layout to use for this branch's children.
	 * @return a new branch.
	 */
	public static Branch branch(Object constraints, LayoutManager layout, Node ... edges)
	{
		Branch out = new Branch(layout, null, constraints);
		for (Node e : edges)
			out.edges.add(e); 
		return out;
	}

	/**
	 * Starts a new branch off of this branch.
	 * @param layout the layout to use for this branch's children.
	 * @return a new branch.
	 */
	public static Branch branch(LayoutManager layout, Node ... edges)
	{
		Branch out = new Branch(layout, null, null); 
		for (Node e : edges)
			out.edges.add(e); 
		return out;
	}

	/**
	 * Creates a leaf node (no children).
	 * @param constraints the constraints to use for the added leaf (using parent layout).
	 * @param container the container to add.
	 * @return a new leaf.
	 */
	public static Leaf leaf(Object constraints, Container container)
	{
		return new Leaf(constraints, container);
	}

	/**
	 * Creates a leaf node, no constraints (no children).
	 * @param container the container to add.
	 * @return a new leaf.
	 */
	public static Leaf leaf(Container container)
	{
		return new Leaf(null, container);
	}

	/**
	 * Common TreeSwing node.
	 */
	public static class Node
	{
		/** Constraints on the layout. */
		protected Object constraints;
		protected Node(Object constraints)
		{
			this.constraints = constraints;
		}
	}

	/**
	 * Leaf node in TreeSwing.
	 */
	public static class Leaf extends Node
	{
		/** Container on the leaf. */
		protected Container container;
		protected Leaf(Object constraints, Container container)
		{
			super(constraints);
			this.container = container;
		}
		
	}
	
	/**
	 * Branch node in TreeSwing.
	 */
	public static class Branch extends Node
	{
		protected Border border;
		protected LayoutManager layout; 
		protected Queue<Node> edges;
		
		protected Branch(LayoutManager layout, Border border, Object constraints)
		{
			super(constraints);
			this.border = border;
			this.layout = layout;
			this.edges = new Queue<>();
		}
		
	}
	
	
	
}
