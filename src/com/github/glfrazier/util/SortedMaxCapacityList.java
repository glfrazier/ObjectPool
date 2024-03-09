package com.github.glfrazier.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class SortedMaxCapacityList<E extends Comparable<E>> implements Iterable<E> {

	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private Integer capacity = 500;

	private SortedMaxCapacityList<E>.Node root;

	private int size;

	public void setCapacity(int len) {
		capacity = len;
	}

	public int getCapacity() {
		return capacity;
	}

	public SortedMaxCapacityList(int capacity) {
		this.capacity = capacity;
	}

	public void add(E item) {
		insertNode(item);
		while (size > capacity) {
			removeLeast();
		}
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void addAll(Collection<? extends E> items) {
		for (E item : items) {
			add(item);
		}
	}

	public Iterator<E> iterator() {
		Node node = findMinimum(root);
		Iterator<E> iter = new Iterator<>() {

			Node iterNode = node;
			E value = null;

			@Override
			public boolean hasNext() {
				return iterNode != null;
			}

			@Override
			public E next() {
				if (iterNode == null) {
					throw new NoSuchElementException();
				}
				value = iterNode.data;
				iterNode = getNext(iterNode);
				return value;
			}

			@Override
			public void remove() {
				if (value == null) {
					throw new IllegalStateException();
				}
				deleteNode(value);
				value = null;
			}

		};
		return iter;
	}

	public E getLeast() {
		Node node = findMinimum(root);
		if (node == null || node instanceof SortedMaxCapacityList.NilNode) {
			return null;
		}
		return node.data;
	}

	public E removeLeast() {
		E value = getLeast();
		deleteNode(value);
		return value;
	}

	public E getGreatest() {
		Node node = findMaximum(root);
		if (node == null || node instanceof SortedMaxCapacityList.NilNode) {
			return null;
		}
		return node.data;
	}

	public E removeGreatest() {
		E value = getGreatest();
		deleteNode(value);
		return value;
	}

	private void insertNode(E key) {
		Node node = root;
		Node parent = null;

		// Traverse the tree to the left or right depending on the key
		while (node != null) {
			parent = node;
			if (key.compareTo(node.data) < 0) {
				node = node.left;
			} else if (key.compareTo(node.data) > 0) {
				node = node.right;
			} else {
				throw new IllegalArgumentException("List already contains a node with value " + key);
			}
		}

		// Insert new node
		Node newNode = new Node(key);
		newNode.color = RED;
		if (parent == null) {
			root = newNode;
		} else if (key.compareTo(parent.data) < 0) {
			parent.left = newNode;
		} else {
			parent.right = newNode;
		}
		newNode.parent = parent;

		fixRedBlackPropertiesAfterInsert(newNode);

		size++;
	}

	private void fixRedBlackPropertiesAfterInsert(Node node) {
		Node parent = node.parent;

		// Case 1: Parent is null, we've reached the root, the end of the recursion
		if (parent == null) {
			// Uncomment the following line if you want to enforce black roots (rule 2):
			// node.color = BLACK;
			return;
		}

		// Parent is black --> nothing to do
		if (parent.color == BLACK) {
			return;
		}

		// From here on, parent is red
		Node grandparent = parent.parent;

		// Case 2:
		// Not having a grandparent means that parent is the root. If we enforce black
		// roots
		// (rule 2), grandparent will never be null, and the following if-then block can
		// be
		// removed.
		if (grandparent == null) {
			// As this method is only called on red nodes (either on newly inserted ones -
			// or -
			// recursively on red grandparents), all we have to do is to recolor the root
			// black.
			parent.color = BLACK;
			return;
		}

		// Get the uncle (may be null/nil, in which case its color is BLACK)
		Node uncle = getUncle(parent);

		// Case 3: Uncle is red -> recolor parent, grandparent and uncle
		if (uncle != null && uncle.color == RED) {
			parent.color = BLACK;
			grandparent.color = RED;
			uncle.color = BLACK;

			// Call recursively for grandparent, which is now red.
			// It might be root or have a red parent, in which case we need to fix more...
			fixRedBlackPropertiesAfterInsert(grandparent);
		}

		// Note on performance:
		// It would be faster to do the uncle color check within the following code.
		// This way
		// we would avoid checking the grandparent-parent direction twice (once in
		// getUncle()
		// and once in the following else-if). But for better understanding of the code,
		// I left the uncle color check as a separate step.

		// Parent is left child of grandparent
		else if (parent == grandparent.left) {
			// Case 4a: Uncle is black and node is left->right "inner child" of its
			// grandparent
			if (node == parent.right) {
				rotateLeft(parent);

				// Let "parent" point to the new root node of the rotated sub-tree.
				// It will be recolored in the next step, which we're going to fall-through to.
				parent = node;
			}

			// Case 5a: Uncle is black and node is left->left "outer child" of its
			// grandparent
			rotateRight(grandparent);

			// Recolor original parent and grandparent
			parent.color = BLACK;
			grandparent.color = RED;
		}

		// Parent is right child of grandparent
		else {
			// Case 4b: Uncle is black and node is right->left "inner child" of its
			// grandparent
			if (node == parent.left) {
				rotateRight(parent);

				// Let "parent" point to the new root node of the rotated sub-tree.
				// It will be recolored in the next step, which we're going to fall-through to.
				parent = node;
			}

			// Case 5b: Uncle is black and node is right->right "outer child" of its
			// grandparent
			rotateLeft(grandparent);

			// Recolor original parent and grandparent
			parent.color = BLACK;
			grandparent.color = RED;
		}
	}

	private Node getUncle(Node parent) {
		Node grandparent = parent.parent;
		if (grandparent.left == parent) {
			return grandparent.right;
		} else if (grandparent.right == parent) {
			return grandparent.left;
		} else {
			throw new IllegalStateException("Parent is not a child of its grandparent");
		}
	}

	private void deleteNode(E key) {
		Node node = root;

		// Find the node to be deleted
		while (node != null && !node.data.equals(key)) {
			// Traverse the tree to the left or right depending on the key
			if (key.compareTo(node.data) < 0) {
				node = node.left;
			} else {
				node = node.right;
			}
		}

		// Node not found?
		if (node == null) {
			return;
		}

		size--;

		// At this point, "node" is the node to be deleted

		// In this variable, we'll store the node at which we're going to start to fix
		// the R-B
		// properties after deleting a node.
		Node movedUpNode;
		boolean deletedNodeColor;

		// Node has zero or one child
		if (node.left == null || node.right == null) {
			movedUpNode = deleteNodeWithZeroOrOneChild(node);
			deletedNodeColor = node.color;
		}

		// Node has two children
		else {
			// Find minimum node of right subtree ("inorder successor" of current node)
			Node inOrderSuccessor = findMinimum(node.right);

			// Copy inorder successor's data to current node (keep its color!)
			node.data = inOrderSuccessor.data;

			// Delete inorder successor just as we would delete a node with 0 or 1 child
			movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
			deletedNodeColor = inOrderSuccessor.color;
		}

		if (deletedNodeColor == BLACK) {
			fixRedBlackPropertiesAfterDelete(movedUpNode);

			// Remove the temporary NIL node
			if (movedUpNode.getClass().equals(NilNode.class)) {
				replaceParentsChild(movedUpNode.parent, movedUpNode, null);
			}
		}
	}

	private Node deleteNodeWithZeroOrOneChild(Node node) {
		// Node has ONLY a left child --> replace by its left child
		if (node.left != null) {
			replaceParentsChild(node.parent, node, node.left);
			return node.left; // moved-up node
		}

		// Node has ONLY a right child --> replace by its right child
		else if (node.right != null) {
			replaceParentsChild(node.parent, node, node.right);
			return node.right; // moved-up node
		}

		// Node has no children -->
		// * node is red --> just remove it
		// * node is black --> replace it by a temporary NIL node (needed to fix the R-B
		// rules)
		else {
			Node newChild = (node.color == BLACK ? new NilNode() : null);
			replaceParentsChild(node.parent, node, newChild);
			return newChild;
		}
	}

	private Node findMinimum(Node node) {
		if (node == null) {
			return null;
		}
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	private Node findMaximum(Node node) {
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}

	private Node getNext(Node n) {
		if (n.right != null) {
			Node next = n.right;
			while (next.left != null) {
				next = next.left;
			}
			return next;
		}
		if (n.parent != null && n == n.parent.left) {
			return n.parent;
		}
		// n is the right child
		Node ancester = n.parent;
		while (ancester.parent != null) {
			if (ancester == ancester.parent.left) {
				return ancester.parent;
			}
			ancester = ancester.parent;
		}
		return null;
	}

	private void fixRedBlackPropertiesAfterDelete(Node node) {
		// Case 1: Examined node is root, end of recursion
		if (node == root) {
			// Uncomment the following line if you want to enforce black roots (rule 2):
			// node.color = BLACK;
			return;
		}

		Node sibling = getSibling(node);

		// Case 2: Red sibling
		if (sibling.color == RED) {
			handleRedSibling(node, sibling);
			sibling = getSibling(node); // Get new sibling for fall-through to cases 3-6
		}

		// Cases 3+4: Black sibling with two black children
		if (isBlack(sibling.left) && isBlack(sibling.right)) {
			sibling.color = RED;

			// Case 3: Black sibling with two black children + red parent
			if (node.parent.color == RED) {
				node.parent.color = BLACK;
			}

			// Case 4: Black sibling with two black children + black parent
			else {
				fixRedBlackPropertiesAfterDelete(node.parent);
			}
		}

		// Case 5+6: Black sibling with at least one red child
		else {
			handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
		}
	}

	private void handleRedSibling(Node node, Node sibling) {
		// Recolor...
		sibling.color = BLACK;
		node.parent.color = RED;

		// ... and rotate
		if (node == node.parent.left) {
			rotateLeft(node.parent);
		} else {
			rotateRight(node.parent);
		}
	}

	private void handleBlackSiblingWithAtLeastOneRedChild(Node node, Node sibling) {
		boolean nodeIsLeftChild = node == node.parent.left;

		// Case 5: Black sibling with at least one red child + "outer nephew" is black
		// --> Recolor sibling and its child, and rotate around sibling
		if (nodeIsLeftChild && isBlack(sibling.right)) {
			sibling.left.color = BLACK;
			sibling.color = RED;
			rotateRight(sibling);
			sibling = node.parent.right;
		} else if (!nodeIsLeftChild && isBlack(sibling.left)) {
			sibling.right.color = BLACK;
			sibling.color = RED;
			rotateLeft(sibling);
			sibling = node.parent.left;
		}

		// Fall-through to case 6...

		// Case 6: Black sibling with at least one red child + "outer nephew" is red
		// --> Recolor sibling + parent + sibling's child, and rotate around parent
		sibling.color = node.parent.color;
		node.parent.color = BLACK;
		if (nodeIsLeftChild) {
			sibling.right.color = BLACK;
			rotateLeft(node.parent);
		} else {
			sibling.left.color = BLACK;
			rotateRight(node.parent);
		}
	}

	private Node getSibling(Node node) {
		Node parent = node.parent;
		if (node == parent.left) {
			return parent.right;
		} else if (node == parent.right) {
			return parent.left;
		} else {
			throw new IllegalStateException("Parent is not a child of its grandparent");
		}
	}

	private boolean isBlack(Node node) {
		return node == null || node.color == BLACK;
	}

	private void rotateRight(Node node) {
		Node parent = node.parent;
		Node leftChild = node.left;

		node.left = leftChild.right;
		if (leftChild.right != null) {
			leftChild.right.parent = node;
		}

		leftChild.right = node;
		node.parent = leftChild;

		replaceParentsChild(parent, node, leftChild);
	}

	private void rotateLeft(Node node) {
		Node parent = node.parent;
		Node rightChild = node.right;

		node.right = rightChild.left;
		if (rightChild.left != null) {
			rightChild.left.parent = node;
		}

		rightChild.left = node;
		node.parent = rightChild;

		replaceParentsChild(parent, node, rightChild);
	}

	private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
		if (parent == null) {
			root = newChild;
		} else if (parent.left == oldChild) {
			parent.left = newChild;
		} else if (parent.right == oldChild) {
			parent.right = newChild;
		} else {
			throw new IllegalStateException("Node is not a child of its parent");
		}

		if (newChild != null) {
			newChild.parent = parent;
		}
	}

	private class NilNode extends Node {
		private NilNode() {
			super(null);
			this.color = BLACK;
		}
	}

	private class Node {
		E data;

		Node left;
		Node right;
		Node parent;

		boolean color;

		public Node(E data) {
			this.data = data;
		}
	}

	public static void main(String[] args) {
		SortedMaxCapacityList<Integer> list = new SortedMaxCapacityList<>(40);

		for (int i = 0; i < 50; i++) {
			System.out.println("Adding " + i);
			list.add(i);
		}
		for (Integer i : list) {
			System.out.println(i);
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("Greatest: " + list.removeGreatest());
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("Least: " + list.removeLeast());
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("Greatest: " + list.removeGreatest());
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("Least: " + list.removeLeast());
		}
		for (Integer i : list) {
			System.out.print(" " + i);
		}
		System.out.println();
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 20; j++) {
				try {
					list.add(rand.nextInt(50));
				} catch (IllegalArgumentException e) {

				}
			}
			int size = list.size();
			int m = 0;
			for (Iterator<Integer> iter = list.iterator(); iter.hasNext();) {
				int x = iter.next();
				m++;
			}
			if (m != size) {
				throw new RuntimeException("Test Failure!");
			}
			if (i%100 == 0) {
				System.out.println("Completed iteration " + i);
			}
		}
		System.out.println("Done!");
	}

}
