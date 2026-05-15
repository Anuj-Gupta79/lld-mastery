package com.lld.patterns.structural.facade;

class Lights {
    public void turnOn() {
        System.out.println("Turning on the lights...");
    }

    public void dim() {
        System.out.println("Dimming the lights to 30%...");
    }
}

class SoundSystem {
    public void turnOn() {
        System.out.println("Turning on the sound system...");
    }

    public void turnOff() {
        System.out.println("Turning off the sound system...");
    }

    public void setVolume(int level) {
        System.out.println("Setting volume to " + level + "%...");
    }
}

class Projector {
    public void turnOn() {
        System.out.println("Turning on the projector...");
    }

    public void turnOff() {
        System.out.println("Turning off the projector...");
    }

    public void setInput(String source) {
        System.out.println("Setting projector input to " + source + "...");
    }
}

class StreamingPlayer {
    public void turnOn() {
        System.out.println("Turning on the streaming player...");
    }

    public void turnOff() {
        System.out.println("Turning off the streaming player...");
    }

    public void play(String movie) {
        System.out.println("Playing movie: " + movie);
    }
}

// LEARNING: HomeTheater is the facade class which coordinates subsystems and  hides their complexity from the client.
class HomeTheater {
    private Lights lights;
    private SoundSystem soundSystem;
    private Projector projector;
    private StreamingPlayer streamingPlayer;

    // LEARNING: We are passing all subsystem rather than creating them inside facade.
    // WHY? This will follow the DIP, further we can create factory to handle it more elegantly.
    public HomeTheater(Lights lights, SoundSystem soundSystem, Projector projector, StreamingPlayer streamingPlayer) {
        this.lights = lights;
        this.soundSystem = soundSystem;
        this.projector = projector;
        this.streamingPlayer = streamingPlayer;
    }

    // LEARNING: Single call to facade, hides all complexity of subsystem.
    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        lights.dim();
        soundSystem.turnOn();
        soundSystem.setVolume(50);
        projector.turnOn();
        projector.setInput("Wide Screen");
        streamingPlayer.turnOn();
        streamingPlayer.play(movie);
    }

    public void endMovie() {
        System.out.println("Shutting down the home theater...");
        streamingPlayer.turnOff();
        projector.turnOff();
        soundSystem.turnOff();
        lights.turnOn();
    }
}

// LEARNING: Facade pattern says that we can provide a simplified interface to a complex subsystem.
public class FacadeDemo {

    public static void main(String[] args) {
        Lights lights = new Lights();
        SoundSystem soundSystem = new SoundSystem();
        Projector projector = new Projector();
        StreamingPlayer streamingPlayer = new StreamingPlayer();
        HomeTheater homeTheater = new HomeTheater(lights, soundSystem, projector, streamingPlayer);
        homeTheater.watchMovie("Inception");
        System.out.println("\n--- Movie is playing ---\n");
        homeTheater.endMovie();
    }
}
