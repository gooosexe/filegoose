package org.filegoose;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

public class GUI {
    FileGoose fileGoose;
    JFrame mainFrame;
    JPanel mainPanel, explorerPanel, searchPanel;
    JTree sideBarTree;
    BorderLayout borderLayout = new BorderLayout();
    FlowLayout flowLayout = new FlowLayout();
    ScrollPaneLayout scrollPaneLayout = new ScrollPaneLayout();
    JScrollPane sideBarScrollPane, explorerScrollPane;

    JTextField searchField;
    JComboBox<String> directoryField;
    String[] directories = {".", ".."};
    String currentDirectory = "C:\\";

    public GUI() {
        fileGoose = new FileGoose();
    }

    public void createFrame() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        mainFrame = new JFrame("FileGoose");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);

        createPanels();
        createMenuBar();
        mainFrame.add(mainPanel);

        mainFrame.setVisible(true);
        mainFrame.setResizable(true);
    }

    public void createExplorerPanel() {
        explorerPanel = new JPanel();
        explorerScrollPane = new JScrollPane(explorerPanel);
        // add all files within the directory to the explorerPanel
    }

    public void createSideBar() {
        sideBarTree = new JTree(fileGoose.createTree(fileGoose.searchDirectory("C:\\")));
        sideBarScrollPane = new JScrollPane(sideBarTree,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // add cascading directories to the sideBar
        sideBarScrollPane.updateUI();
    }

    public void createSearchPanel() {
        searchPanel = new JPanel();
        searchPanel.setLayout(flowLayout);

        if (fileGoose.searchDirectory(currentDirectory).isPresent())
            directoryField = new JComboBox<>(fileGoose.searchDirectory(currentDirectory).get());
        else
            System.err.println("why dont you have a home directory");

        directoryField.setEditable(true);
        directoryField.addActionListener(e -> {
            currentDirectory = directoryField.getEditor().getItem().toString();
            // check whether the inputted folder exists
            if (fileGoose.searchDirectory(currentDirectory).isEmpty()) {
                // alert the user that the directory does not exist
            } else {
                directoryField.removeAllItems();
                if (fileGoose.searchDirectory(currentDirectory).isPresent()) {
                    for (String directory : Objects.requireNonNull(fileGoose.searchDirectory(directoryField.getEditor().getItem().toString()).get())) {
                        if (!directory.equalsIgnoreCase("$recycle.bin")) directoryField.addItem(directory);
                    }
                }
            }
        });

        searchField = new JTextField();
        searchField.setEditable(true);
        searchField.setColumns(15);

        JButton backButton = new JButton("Back");
        JButton forwardButton = new JButton("Forward");
        JButton upButton = new JButton("Up");
        JButton searchButton = new JButton("Search");

        searchPanel.add(backButton);
        searchPanel.add(forwardButton);
        searchPanel.add(upButton);
        searchPanel.add(directoryField);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
    }

    public void createMainPanel() {
        borderLayout.setHgap(5);
        borderLayout.setVgap(5);

        mainPanel = new JPanel();
        mainPanel.setLayout(borderLayout);
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.add(explorerScrollPane, BorderLayout.CENTER);
        mainPanel.add(sideBarScrollPane, BorderLayout.WEST);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

    }

    public void createPanels() {
        createExplorerPanel();
        createSearchPanel();
        createSideBar();
        createMainPanel();
    }

    public void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        mainFrame.setJMenuBar(menuBar);
    }
}
