package com.lld.patterns.structural.proxy;

// LEARNING: The Video interface defines the common operations for both RealVideo and VideoProxy.
interface Video {
    void play();
}

class RealVideo implements Video {
    private String filename;

    public RealVideo(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading video from disk: " + filename);
    }

    @Override
    public void play() {
        System.out.println("Playing video: " + filename);
    }
}

class VideoProxy implements Video {
    private String filename;
    private String title;
    private RealVideo realVideo;

    // LEARNING: fileName is required for RealVideo but title is just for proxy to show before loading real video.
    public VideoProxy(String filename, String title) {
        this.filename = filename;
        this.title = title;
    }

    @Override
    public void play() {
        System.out.println("Video title: " + title);
        // LEARNING: This null check will ensure we create RealVideo object only once.
        if (realVideo == null) {
            System.out.println("Creating RealVideo object for: " + filename);
            realVideo = new RealVideo(filename);
        } else {
            System.out.println("RealVideo object already created for: " + filename);
        }
        realVideo.play();
    }
}

class VideoPlayer {
    private Video video;

    // LEARNING: Client is not aware about RealVideo.
    public VideoPlayer(Video video) {
        this.video = video;
    }

    public void playVideo() {
        video.play();
    }
}

// LEARNING: Proxy pattern says that we can provide a surrogate or placeholder for another object to control access to it.
public class ProxyDemo {
    public static void main(String[] args) {
        VideoPlayer player = new VideoPlayer(new VideoProxy("amplifier.mp4", "Amplifier"));
        player.playVideo();

        System.out.println("\n--- Playing the same video again ---\n");
        // LEARNING: Played video second time but RealVideo only loads once.
        player.playVideo();
    }
}

// Benefits of Proxy pattern:
// 1. Lazy initialization: The real object is created only when it's actually needed.
// 2. Additional functionality: The proxy can add extra functionality without modifying the real object.
// 3. Control over access: The proxy can control access to the real object, for example, by adding security checks or logging.