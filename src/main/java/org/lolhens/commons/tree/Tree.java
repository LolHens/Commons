package org.lolhens.commons.tree;

import java.util.Collection;
import java.util.List;

public interface Tree<Leaf> {
    // Leaf
    boolean hasLeaf();

    Leaf getLeaf();

    void setLeaf(Leaf leaf);

    void removeLeaf();

    // Root
    boolean hasRoot();

    Tree<Leaf> getRoot();

    void setRoot(Tree<Leaf> root);

    void removeRoot();

    // Branch
    boolean hasBranches();

    int size();

    List<Tree<Leaf>> getBranches();

    boolean hasBranch(int index);

    boolean hasBranch(Tree<Leaf> branch);

    boolean hasBranch(Leaf leaf);

    Tree<Leaf> getBranch(int index);

    Tree<Leaf> getBranch(Tree<Leaf> branch);

    Tree<Leaf> getBranch(Leaf leaf);

    void addBranch(Tree<Leaf> branch);

    void addBranch(int index, Tree<Leaf> branch);

    void addBranches(Collection<Tree<Leaf>> branches);

    void removeBranch(int index);

    void removeBranch(Leaf leaf);

    void removeBranch(Tree<Leaf> branch);

    // Tree
    int getTreeSize();

    List<Leaf> getTreeLeaves();
}
