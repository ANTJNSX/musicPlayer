# Music Player
Simple music player written in java.

## Parts
The parts can be split into two groups, 
The GUI and the player

### GUI
The GUI begins by defining the parts such as playButton, stopButton, and the refreshButton.
After that each button gets created as a new JButton object, and then added a function to their .addActionListener method.
Then they are called when the button is pressed. Same goes for the selection panel and the volume changer, they are defined
and created as objects with their method functions, and then generated and designed together with the other objects.

### Player
The PlayerThread is a private class that extends thread to be able to run the audio player while letting the user intervene with new inputs.
The PlayerTread then creates a private player, then the PlayerThread creates a public PlayerThread defined with the argument of a Player object,
and with that then it defines this.player as the argument given(player).
the player also has a run method which is simply used to actually play the audio file given to the object as well as a stopPlayback function to force stop the audio.


# Conclusion
This was a very simple audio player, but it shows very well how classes and inheritance works within classes and objects, as well as threading and how that allows 
programs to keep running with multiple processes at once.