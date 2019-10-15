/**
 * Filename: TestAVLTree.java Project: p2 Authors: Debra Deppeler, Jason Carrington
 *
 * Semester: Fall 2018 Course: CS400 Lecture: 001
 * 
 * Due Date: Before 10pm on September 24, 2018 Version: 1.0
 * 
 * Credits:
 * 
 * Bugs: no known bugs
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import java.lang.IllegalArgumentException;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;


/**
 * @author Jason Carrington Class that tests the implementation of an AVL tree
 */
public class TestAVLTree {
    @Rule
    public Timeout globalTimeout = new Timeout(4000);
    private AVLTree<Integer> tree;

    @Before
    public void setUp() throws Exception {
        tree = new AVLTree<Integer>();
    }

    @After
    public void tearDown() throws Exception {
        tree = null;
    }

    /**
     * Tests that an AVLTree is empty upon initialization.
     */
    @Test
    public void test01isEmpty() {
        assertTrue(tree.isEmpty());
    }

    /**
     * Tests that an AVLTree is not empty after adding a node.
     */
    @Test
    public void test02isNotEmpty() {
        try {
            tree.insert(1);
            assertFalse(tree.isEmpty());
        } catch (DuplicateKeyException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Tests functionality of a single delete following several inserts.
     */
    @Test
    public void test03insertManyDeleteOne() {
        try {
            for (int i = 0; i < 10000; i++) {
                tree.insert(i);
            }
            tree.delete(5);
            assertFalse("Delete did not correctly remove the item with key 5 from the tree",
                tree.search(5));
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }

    }

    /**
     * Tests functionality of many deletes following several inserts.
     */
    @Test
    public void test04insertManyDeleteMany() {
        try {
            for (int i = 0; i < 10000; i++) {
                tree.insert(i);
            }
            tree.delete(5000);// tests deleting from the middle
            for (int i = 0; i < 5000; i++) {// tests deleting on the lower end
                tree.delete(i);
            }
            for (int i = 9999; i > 5000; i--) {// tests deleting from the higher end
                tree.delete(i);
            }
            assertTrue("When deleting all items, some were not removed", tree.isEmpty());
        } catch (IllegalArgumentException e) {
            fail(
                "When deleting a value that should have been in the tree, threw IllegalArgumentException");
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * checks to make sure a DuplicateKeyException is thrown when trying to insert a duplicate item
     */
    @Test
    public void test05insertDuplicate() {
        try {
            tree.insert(1);
            tree.insert(1);
            fail("Inserting a duplicate item did not return DuplicateKeyException");
        } catch (DuplicateKeyException e) {

        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * checks to make sure an IllegalArgumentException is thrown when inserting a null value
     */
    @Test
    public void test06insertNull() {
        try {
            tree.insert(null);
            fail("Inserting a null item did not return IllegalArgumentException");
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * tests to make sure the delete method returns a IllegalArgumentException when trying to delete
     * an item with a key that is not in the tree
     */
    @Test
    public void test07deleteInvalidKey() {
        try {
            tree.insert(2);
            tree.insert(1);
            tree.insert(3);
            tree.delete(4);
            fail("Did not return IllegalArgumentException");
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * tests to make sure search finds a value that is inside a tree
     */
    @Test
    public void test08searchValidKey() {
        try {
            tree.insert(2);
            tree.insert(1);
            tree.insert(3);
            assertTrue(
                "When searching for key 3 in a tree containing key 3 in it, search() did not return true",
                tree.search(3));
        } catch (IllegalArgumentException e) {
            fail("Threw IllegalArgumentException when the input value was not null");
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * tests searching for an invalid key such as one that is not in the tree or a null key
     */
    @Test
    public void test09searchInvalidKey() {
        try {
            tree.insert(2);
            tree.insert(1);
            tree.insert(3);
            assertFalse(
                "A node with the key 4 was found in a tree that should not have had the item 4",
                tree.search(4));
            tree.search(null);
            fail("Did not return IllegalArgumentException when the input was null");
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * tests to make sure the print method prints an inorder list of the items that are in the tree
     */
    @Test
    public void test10print() {
        try {
            tree.insert(2);
            tree.insert(1);
            tree.insert(3);
            assertEquals("1 2 3", tree.print().trim());
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * Tests to make make sure if you add items and remove some, that you can correctly add more
     * items and then remove them all
     */
    @Test
    public void test11insertDeleteInsertDelete() {
        try {
            for (int i = 0; i < 10000; i++) {
                tree.insert(i);
            }
            for (int i = 0; i < 5000; i++) {
                tree.delete(i);
            }
            for (int i = 10000; i < 15000; i++) {
                tree.insert(i);
            }
            for (int i = 14999; i >= 5000; i--) {
                tree.delete(i);
            }
            assertTrue("When deleting items a second time, some were not removed", tree.isEmpty());
        } catch (IllegalArgumentException e) {
            fail(
                "When deleting a value that should have been in the tree, threw IllegalArgumentException");
        } catch (DuplicateKeyException e) {
            fail(
                "When inserting into a tree that has been recently emptied, a DuplicateKeyException was thrown");
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * checks to make sure that after a lot of items are added and a few deleted that the structure
     * of the tree fits a balanced binary search tree.
     */
    @Test
    public void test12checkTreeStructure() {
        try {
            for (int i = 0; i < 100000; i++) {
                tree.insert(i);
            }
            tree.delete(100);
            tree.delete(200);
            tree.delete(10000);
            assertTrue("Tree is not balanced after inserting a lot of items and removing a few.",
                tree.checkForBalancedTree());
            assertTrue(
                "Tree is not a binary search tree after inserting a lot of items and removing a few.",
                tree.checkForBinarySearchTree());
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * Tests to make sure if a tree is left heavy that it rotates right.
     */
    @Test
    public void test13rightRotate() {
        try {
            tree.insert(3);
            tree.insert(2);
            tree.insert(1);
            assertTrue("Tree was not balanced after a right rotate.", tree.checkForBalancedTree());
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * Tests to make sure if a tree is right heavy that it rotates left.
     */
    @Test
    public void test14leftRotate() {
        try {
            tree.insert(1);
            tree.insert(2);
            tree.insert(3);
            assertTrue("Tree was not balanced after a left rotate.", tree.checkForBalancedTree());
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * Tests to make sure a tree that needs a double rotate, left-right, is balanced correctly
     */
    @Test
    public void test15rotateLeftRight() {
        try {
            tree.insert(3);
            tree.insert(1);
            tree.insert(2);
            assertTrue("Tree was not balanced after a left-right rotate.",
                tree.checkForBalancedTree());
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * Tests to make sure a tree that needs a double rotate, right-left, is balanced correctly
     */
    @Test
    public void test16rotateRightLeft() {
        try {
            tree.insert(1);
            tree.insert(3);
            tree.insert(2);
            assertTrue("Tree was not balanced after a right-left rotate.",
                tree.checkForBalancedTree());
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * Tests deleting the root and then replacing it with the left most child of the right side, but
     * that key that should be switched has a right child
     */
    @Test
    public void test15deleteMoveUpKeyWithRightChild() {
        try {
            tree.insert(5);
            tree.insert(4);
            tree.insert(9);
            tree.insert(7);
            tree.insert(3);
            tree.insert(10);
            tree.insert(8);
            tree.delete(5);// should delete the root and search to right and then left to find 7,
                           // which has a right child
            assertTrue("Did correctly deal with the right child of the node that was moved up.",
                tree.search(8));
            assertTrue(
                "Did not correctly move 7 up and attach it's right child 8 to the parent 9's left child",
                tree.checkForBalancedTree());

        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }

    /**
     * Tests deleting the root without a right side, makes sure that it moves 2 up
     */
    @Test
    public void test16deleteMoveUpKeyIsLeft() {
        try {
            tree.insert(5);
            tree.insert(2);
            tree.delete(5);
            assertTrue(
                "Did not correctly assign the left child to the root when deleting the root with no right child.",
                tree.search(2));
        } catch (Exception e) {
            fail("Unexpectedly threw " + e.getClass().getName());
        }
    }
    

}
