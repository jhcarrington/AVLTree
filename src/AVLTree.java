/**
 * Filename: AVLTree.java Project: p2 Authors: Debra Deppeler, Jason Carrington
 *
 * Semester: Fall 2018 Course: CS400 Lecture: 001
 * 
 * Due Date: Before 10pm on September 24, 2018 Version: 1.0
 * 
 * Credits: none
 * 
 * Bugs: no known bugs
 */

import java.lang.IllegalArgumentException;

/**
 * Class that contains an abstract data type of BSTNode<k> stored within an AVL tree
 * 
 * @param <K>
 * @author Jason Carrington
 */
public class AVLTree<K extends Comparable<K>> implements AVLTreeADT<K> {

    /**
     * Class containing fields for each node that will be stored in the AVL tree
     * 
     * @param <K> K is anything that identifies as comparable
     */
    class BSTNode<K> {
        /* fields */
        private K key; // The key value that will be used to sort the tree
        private int height; // The height from the bottom of the tree that the node sits at
        private BSTNode<K> left, right; // The two children nodes of this node

        /**
         * Constructor for a BST node. Instantiates all the fields
         * 
         * @param key The specific ID that the node should be given
         */
        BSTNode(K key) {
            this.key = key;
            height = 1;
        }

        // *******************accessors***************//
        public int getHeight() {
            return height;
        }

        public K getKey() {
            return key;
        }

        public BSTNode<K> getRight() {
            return right;
        }

        public BSTNode<K> getLeft() {
            return left;
        }

        // *******************mutators***************//
        public void setRight(BSTNode<K> right) {
            this.right = right;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setLeft(BSTNode<K> left) {
            this.left = left;
        }

        public void setKey(K key) {
            this.key = key;
        }



    }

    private BSTNode<K> root; // stores the root of the the tree

    /**
     * Checks to see if there is no objects within the tree
     * 
     * @return true if the root of the true is null
     * 
     */
    @Override
    public boolean isEmpty() {
        if (root == null) {
            return true;
        }
        return false;
    }

    /**
     * Method that rotates the AVL to the left based on the node that is designated
     * 
     * @param gParent the desired pivotal node of rotation
     * @return the rotated subtree
     */
    private BSTNode<K> rotateLeft(BSTNode<K> gParent) {
        BSTNode<K> parent = gParent.getRight();// stores the right child as the parent
        BSTNode<K> temp = parent.getLeft();// stores the left child of the parent

        // moves the grandparent down and the parent up
        gParent.setRight(temp);
        parent.setLeft(gParent);

        // updates the heights based on what moved
        if (gParent.getRight() != null) {
            gParent.setHeight(gParent.getRight().getHeight() + 1);
        } else {
            gParent.setHeight(1);
        }
        parent.setHeight(gParent.getHeight() + 1);
        return parent;
    }

    /**
     * Method that rotates the AVL to the right based on the node that is designated
     * 
     * @param gParent the desired pivotal node of rotation
     * @return the rotated subtree
     */
    private BSTNode<K> rotateRight(BSTNode<K> gParent) {
        BSTNode<K> parent = gParent.getLeft();
        BSTNode<K> temp = parent.getRight();

        // moves the grandparent down and the parent up
        gParent.setLeft(temp);
        parent.setRight(gParent);

        // updates the heights based on what moved
        if (gParent.getLeft() != null) {
            gParent.setHeight(gParent.getLeft().getHeight() + 1);
        } else {
            gParent.setHeight(1);
        }
        parent.setHeight(gParent.getHeight() + 1);
        return parent;
    }

    /**
     * uses a helper method to correctly insert a new node with the key 'key'
     * 
     * @param key the key that will be inserted into the tree
     * @throws DuplicateKeyException thrown if the input key is already a key on the tree
     * @throws IllegalArgumentException thrown if a null value is inserted
     */
    @Override
    public void insert(K key) throws DuplicateKeyException, IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        // creates the new node with key value of 'key'
        BSTNode<K> newNode = new BSTNode<K>(key);
        try {
            root = insertHelper(root, newNode);
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException();
        }


    }

    private int loops; // once a value is inserted, this is used to keep track of how many nodes
                       // have been backtracked in order to properly do a double rotation

    /**
     * Inserts the new node into it's correct place recursively, and then balances the AVL tree as
     * it backtracks, as well as resets the heights
     * 
     * @param root The entire tree that you want to add the node to
     * @param newNode The newNode that is trying to be added
     * @return used to keep track of where we are in the recursion
     * @throws DuplicateKeyException thrown if the key of the newNode is already in the tree
     */
    private BSTNode<K> insertHelper(BSTNode<K> root, BSTNode<K> newNode)
        throws DuplicateKeyException {
        // base case of the recursion, stops once it finds an open spot
        if (root == null) {
            loops = 1;// tells us that we have inserted the newNode
            return newNode;
        } // if the new key is less than the current nodes key
        if (newNode.getKey().compareTo(root.getKey()) == -1) {
            BSTNode<K> temp = insertHelper(root.getLeft(), newNode);// recursion
            root.setLeft(temp);// resets the current root to the modified tree

            // adds 1 to the height of every recursively accessed node iff the new node was added
            // into the tree
            root.setHeight(root.getLeft().getHeight() + 1);

            // once we have confirmed the placement of our newNode, count back 2 more nodes
            if (loops > 0) {
                loops++;
            }
            // checks to see if there is any double rotation necessary nodes going towards the left
            // and then right.
            if (loops < 4 && root.getLeft() != null && root.getLeft().getRight() == newNode) {
                root.setLeft(rotateLeft(root.getLeft()));
                root.setHeight(root.getLeft().getHeight() + 1);
            }
            // decides whether to rotate and does it
            root = doRotate(root);
            return root;
        } // if the new key is greater than the current nodes key
        if (newNode.getKey().compareTo(root.getKey()) == 1) {
            BSTNode<K> temp = insertHelper(root.getRight(), newNode);// recursion
            root.setRight(temp);// resets the tree

            // update heights
            root.setHeight(root.getRight().getHeight() + 1);

            // starts the loop looking for a double rotation
            if (loops > 0) {
                loops++;
            }
            // checks to see if there is any double rotation necessary nodes going towards the right
            // and then left.
            if (loops < 4 && root.getRight() != null && root.getRight().getLeft() == newNode) {
                root.setRight(rotateRight(root.getRight()));
                root.setHeight(root.getRight().getHeight() + 1);
            }
            root = doRotate(root);
            return root;
        }
        // catches a duplicate key
        if (newNode.getKey().compareTo(root.getKey()) == 0) {
            throw new DuplicateKeyException();
        }

        return root;
    }

    /**
     * Decides whether the current location in the AVL tree based on 'root' needs to be balanced
     * 
     * @param root the starting node of which balancing is happening
     */
    private BSTNode<K> doRotate(BSTNode<K> root) {
        int balanceFactor = 0;// the balance factor of left child height - right child height

        // if the current node has a height greater than 1 then at least 1 of it's children is not
        // null
        if (root.getHeight() > 1) {
            if (root.getLeft() == null) {
                // if there is no left node
                balanceFactor = 0 - root.getRight().getHeight();
            } else if (root.getRight() == null) {
                // if there is no right node
                balanceFactor = root.getLeft().getHeight();
            } else {
                // if there are both nodes that need to be compared
                balanceFactor = root.getLeft().getHeight() - root.getRight().getHeight();
            }
        } else {// no balancing should be done if the root height is 1 or less
            return root;
        }
        if (balanceFactor < -1) {// if the AVL is right heavy then left rotate
            return rotateLeft(root);
        }
        if (balanceFactor > 1) {// if the AVL is left heavy then right rotate
            return rotateRight(root);
        }
        return root;
    }

    /**
     * uses a helper method to delete the node in the AVL tree with key 'key'
     * 
     * @param key, the key that you are looking for
     * @throws IllegalArgumentException if the key is not found
     */
    @Override
    public void delete(K key) throws IllegalArgumentException {
        try {
            root = deleteHelper(root, key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * recursively finds the node that matches the key, and then deletes it and rearanges the nodes
     * that are around it, then recursively goes back to the top of the tree as it balances the tree
     * and sets the heights
     * 
     * @param root The current node position that is being looked at
     * @param key The key that we are trying to find
     * @return A node of the next place that should be checked
     * @throws IllegalArgumentException if the key is not found
     */
    private BSTNode<K> deleteHelper(BSTNode<K> root, K key) throws IllegalArgumentException {
        if (root == null) {
            throw new IllegalArgumentException();
        }
        // System.out.println(root.getHeight());
        // if the key has been found, base case
        if (root.getKey().compareTo(key) == 0) {

            // if there is a right node to traverse after the node has been found
            if (root.getRight() != null) {

                BSTNode<K> temp1 = root.getRight();// stores the right node as a variable that will
                                                   // change as we traverse through it to the left
                BSTNode<K> temp2 = root;// final updated version of this subtree
                int i = 0;// Repositioning may occur within the while statement, if this value is 1
                          // that means it has already been repositioned


                while (temp1.getLeft() != null) {// while there is another value to the left

                    // looks 2 nodes in advance, checks to see if the node that will be
                    // moved up(parent) has a right child that needs to be assigned to the left
                    // child
                    // of the current node(grandparent)
                    if (temp1.getLeft().getLeft() == null && temp1.getLeft().getRight() != null) {
                        i = 1;
                        temp2 = temp1.getLeft();// stores the node that will be moved
                        temp1.setLeft(temp2.getRight());// sets the left child of the
                                                        // parent to the grandparent
                        temp2.setRight(null);

                        if (temp1.getRight() != null
                            && temp1.getLeft().getHeight() + 1 >= temp1.getRight().getHeight()) {
                            temp1.setHeight(temp1.getLeft().getHeight() + 1);// sets the
                                                                             // height
                        }
                        temp2.setLeft(root.getLeft());// moves the parent node that was to be moved
                                                      // up by setting a pointer to the left child
                                                      // of the root node which is the node that we
                                                      // are removing

                        // if there is a right child of the removed node and it is not equal to the
                        // node we moved up, then set it
                        if (root.getRight() != null
                            && root.getRight().getKey().compareTo(temp2.getKey()) != 0) {
                            temp2.setRight(root.getRight());
                        }


                        // compares the two heights and determine what the new height will be
                        if (temp2.getLeft() != null && temp2.getRight() != null
                            && temp2.getLeft().getHeight() > temp2.getRight().getHeight()) {
                            temp2.setHeight(temp2.getLeft().getHeight() + 1);
                        } else if (temp2.getLeft() != null) {
                            temp2.setHeight(temp2.getLeft().getHeight() + 1);
                        } else if (temp2.getRight() != null) {
                            temp2.setHeight(temp2.getRight().getHeight() + 1);
                        } else {
                            temp2.setHeight(1);
                        }

                        root = temp2;// updates the AVL subtree
                        break;
                    }
                    // looks 2 nodes in advance, no children to worry about
                    if (temp1.getLeft().getLeft() == null) {
                        i = 1;
                        temp2 = temp1.getLeft();// stores the parent, that will be moved
                        temp1.setLeft(null);// removed the grandparents pointer to the parent

                        // checks to see if there is a right node that needs to have a new pointer
                        // other than the root over to the parent
                        if (root.getRight() != null) {
                            temp2.setRight(root.getRight());
                            temp2.setHeight(root.getRight().getHeight() + 1);
                        }
                        // sets the parent nodes left child to the root nodes left child
                        temp2.setLeft(root.getLeft());

                        // sets the heights
                        if (temp2.getLeft() != null
                            && temp2.getHeight() < temp2.getLeft().getHeight() + 1) {
                            temp2.setHeight(temp2.getLeft().getHeight() + 1);
                        }

                        root = temp2;// returns the corrected AVL subtree
                        break;
                    }
                    temp1 = temp1.getLeft();// for the while loop to continue checking leftchildren
                }
                if (i == 0) {
                    // if no repositioning has been done thus far
                    temp1.setLeft(temp2.getLeft());// temp2 has root stored still, temp1 has the
                                                   // location of the smallest right child
                    if (temp2.getLeft() != null
                        && temp2.getLeft().getHeight() + 1 > temp1.getHeight()) {
                        temp1.setHeight(temp2.getLeft().getHeight() + 1);
                    }
                    root = temp1;
                }
                // if there is not a right child of the node we are removing, move the left child up
            } else if (root.getLeft() != null) {
                root = root.getLeft();
            } else {
                root = null;
            } // FIXED
            return root;
        }

        // these two if statements determine which direction in the tree to search for the key we
        // are trying to remove
        if (key.compareTo(root.getKey()) == -1) {//
            BSTNode<K> temp = deleteHelper(root.getLeft(), key);
            root.setLeft(temp);
            // removes 1 from the height of every recursively accessed node iff the node was found
            // and deleted.
            root.setHeight(root.getHeight() - 1);
            // decides whether to rotate and does it
            root = doRotate(root);
            return root;
        }
        if (key.compareTo(root.getKey()) == 1) {
            BSTNode<K> temp = deleteHelper(root.getRight(), key);
            root.setRight(temp);
            root.setHeight(root.getHeight() - 1);
            root = doRotate(root);
            return root;
        }

        return root;
    }

    /**
     * Searches for a node that has a matching key with 'key'
     * 
     * @param key the key in which you want to look for
     * @return true if the key value is in the AVL tree, false if not
     * @throws IllegalArgumentException thrown if the input key is null
     */
    @Override
    public boolean search(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        BSTNode<K> temp = root;// stores a temporary version of root so we can use a binary search
                               // to find the node
        while (temp != null) {// while there are more nodes
            if (key.compareTo(temp.getKey()) == 0) {
                // if found
                return true;
            } else if (key.compareTo(temp.getKey()) == -1) {
                // if the key we are looking for is in the left subtree
                temp = temp.getLeft();
            } else if (key.compareTo(temp.getKey()) == 1) {
                // if the key we are looking for is in the left subtree
                temp = temp.getRight();
            }
        }
        return false;
    }

    /**
     * Uses a helper method to traverse through a list
     * 
     * @return String, a string of the in-order traversal of the tree
     */
    @Override
    public String print() {
        return printHelper(root).trim();
    }

    /**
     * helper method to recursively print out the entire tree by accessing left child, parent, right
     * child
     * 
     * @param root The top of the tree
     * @return string with all of the key values from the tree
     */
    private String printHelper(BSTNode<K> root) {
        if (root == null) {
            return "";
        }
        return (printHelper(root.getLeft()) + "" + root.getKey() + " "
            + printHelper(root.getRight()));
    }

    /**
     * checks to see if the heights are within a balancing factor of 1.
     * 
     * @return boolean, true if the true is balanced, false if not
     */
    @Override
    public boolean checkForBalancedTree() {
        if (root.getLeft() == null && root.getRight() == null && root.getHeight() == 1) {
            return true;
        } else if (root.getLeft() == null && 0 - root.getRight().getHeight() > -2) {
            return true;
        } else if (root.getRight() == null && root.getLeft().getHeight() < 2) {
            return true;
        } else if (root.getLeft().getHeight() - root.getRight().getHeight() < 2
            && root.getLeft().getHeight() - root.getRight().getHeight() > -2) {
            return true;
        }
        return false;
    }

    /**
     * uses a helper method to determine whether the AVL tree is a binary search tree
     * 
     * @return boolean, true if it is a binary search true, false otherwise
     */
    @Override
    public boolean checkForBinarySearchTree() {
        return checkForBinaryHelper(root);
    }

    /**
     * recursively checks the 2 child nodes of the root node and makes sure that there are all left
     * children have keys less than the parent, and all right are greater
     * 
     * @param root the node that we are starting on
     * @return true if it meets the qualifications for BST, false if not
     */
    private boolean checkForBinaryHelper(BSTNode<K> root) {
        if (root == null) {
            return true;
        }
        boolean left = false;
        boolean right = false;
        // checks left side
        if (root.getLeft() != null && root.getLeft().getKey().compareTo(root.getKey()) < 0) {
            left = checkForBinaryHelper(root.getLeft());
        }
        // checks right side
        if (root.getRight() != null && root.getRight().getKey().compareTo(root.getKey()) > 0) {
            right = checkForBinaryHelper(root.getRight());
        }

        // next two determine whether there was no left or right side
        if (root.getLeft() == null) {
            left = true;
        }
        if (root.getRight() == null) {
            right = true;
        }
        // tree with 1 node case
        if (root.getHeight() == 1) {
            return true;
        }
        if (left && right) {
            return true;
        }
        return false;

    }
}
