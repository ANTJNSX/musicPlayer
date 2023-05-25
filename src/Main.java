import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javazoom.jl.player.Player;
import javazoom.jl.player.AudioDevice;

public class Main extends JFrame {
    private JList<String> fileList;
    private JButton playButton;
    private JButton stopButton;
    private PlayerThread playerThread = null;

    public Main() {
        setTitle("Music Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //
        setPreferredSize(new Dimension(500, 400));
        setLayout(new BorderLayout());

        fileList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(fileList);
        add(scrollPane, BorderLayout.CENTER);

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

        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFile = fileList.getSelectedValue();
                System.out.println("Stopping: " + selectedFile);
                stopSelected();
            }
        });

        // Create a panel to hold the buttons and center it to the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(playButton);
        buttonPanel.add(stopButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        loadFiles();
    }

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
