import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javazoom.jl.player.Player;

public class Main extends JFrame {
    private JList<String> fileList;
    private JButton playButton;
    private JButton stopButton;
    private JButton refreshButton;
    private PlayerThread playerThread = null;

    public Main() {
        // Define the GUI layout
        setTitle("Music Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));
        setLayout(new BorderLayout());

        // define the list
        fileList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(fileList);
        add(scrollPane, BorderLayout.CENTER);

        // define the play buttons function
        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFile = fileList.getSelectedValue();
                if (selectedFile != null) {
                    playSelected();
                    System.out.println("Playing: " + selectedFile);
                }
            }
        });

        // define the stop buttons function
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFile = fileList.getSelectedValue();
                System.out.println("Stopping: " + selectedFile);
                stopSelected();
            }
        });

        // define the refresh buttons function
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFiles();
            }
        });

        // Create a panel to hold the buttons and center it to the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(playButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        loadFiles();
    }

    // method to load the audio files and display them into the ui block for file list
    private void loadFiles() {
        DefaultListModel<String> model = new DefaultListModel<>();
        File folder = new File("music");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    model.addElement(file.getName());
                }
            }
        }
        fileList.setModel(model);
    }

    // function to play the selected song, called when Play button is pressed
    public void playSelected() {
        String selectedFile = fileList.getSelectedValue();

        try {
            if (selectedFile != null) {
                String filePath = "music/" + selectedFile;
                FileInputStream fis = new FileInputStream(filePath);
                Player player = new Player(fis);

                if (playerThread != null) {
                    playerThread.stopPlayback();
                }

                playerThread = new PlayerThread(player);
                playerThread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void stopSelected(){
        if (playerThread != null) {
            playerThread.stopPlayback();
            playerThread = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }

    // class for threading the player, making it able to play audio
    // while letting user input be processed. This is because of the extended class "Thread"
    private class PlayerThread extends Thread {
        private Player player;
        private boolean isPlaying;

        public PlayerThread(Player player) {
            this.player = player;
        }

        public void run() {
            try {
                isPlaying = true;
                player.play();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                isPlaying = false;
                player.close();
            }
        }

        public void stopPlayback() {
            if (isPlaying) {
                player.close();
            }
        }
    }
}
