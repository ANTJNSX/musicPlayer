import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;
import javazoom.jl.player.advanced.*;
import javazoom.jl.player.Player;

public class Main extends JFrame {
    private JList<String> fileList;
    private JButton playButton;
    private JButton stopButton;
    private AdvancedPlayer player = null;

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
                stopSelected();
            }
        });

        playButton.setSize(100, 50);
        stopButton.setSize(100, 50);


        add(playButton, BorderLayout.SOUTH);
        add(stopButton, BorderLayout.EAST);

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

    // Inside the actionPerformed method of the ActionListener for the "Play" button
    public void playSelected() {
        String selectedFile = fileList.getSelectedValue();
        try {
            if (selectedFile != null) {

                String filePath = "music/" + selectedFile;
                FileInputStream fis = new FileInputStream(filePath);
                player = new AdvancedPlayer(fis);
                player.play();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // fix the stop playing button, it after playing a song the program is stuck on only playing the song so perhaps open a thread to play the song the call "this.player.close();" to close the current player, this would also let the user play multiple songs at once.
    public void stopSelected(){
        try {

            this.player.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
