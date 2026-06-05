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

// LEARNING: Facade coordinates multiple subsystems behind a single simplified
// interface.
// WHY: Client calls one method instead of orchestrating 6-7 subsystem calls in
// the right order.
class HomeTheater {
    private Lights lights;
    private SoundSystem soundSystem;
    private Projector projector;
    private StreamingPlayer streamingPlayer;

    // LEARNING: Subsystems injected, not created inside — follows DIP.
    // WHY: Facade stays testable and swappable; a factory can handle wiring if
    // needed.
    public HomeTheater(Lights lights, SoundSystem soundSystem, Projector projector, StreamingPlayer streamingPlayer) {
        this.lights = lights;
        this.soundSystem = soundSystem;
        this.projector = projector;
        this.streamingPlayer = streamingPlayer;
    }

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

public class FacadeDemo {
    public static void main(String[] args) {
        HomeTheater homeTheater = new HomeTheater(
                new Lights(), new SoundSystem(), new Projector(), new StreamingPlayer());

        homeTheater.watchMovie("Inception");
        System.out.println("\n--- Movie is playing ---\n");
        homeTheater.endMovie();
    }
}