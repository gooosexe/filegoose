package org.filegoose;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.Optional;

public class FileGoose {
    public FileGoose() {

    }

    public Optional<String[]> searchDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (!file.exists()) return Optional.empty();
        return Optional.ofNullable(file.list((dir, name) -> new File(dir, name).isDirectory()));
    }

    public DefaultMutableTreeNode createTree(String[] files) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        for (String file : files) {
            root.add(new DefaultMutableTreeNode(file));
            if (new File(file).isDirectory())
                root.add(createTree(searchDirectory(file).get()));
        }
        return root;
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        GUI gui = new GUI();
        gui.createFrame();
    }
}
