package com.lld.patterns.structural.proxy;

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

// LEARNING: Proxy holds same interface as RealVideo — client sees no
// difference.
// WHY: Delays expensive object creation until play() is actually called.
class VideoProxy implements Video {
    private String filename;
    private String title;
    private RealVideo realVideo;

    public VideoProxy(String filename, String title) {
        this.filename = filename;
        this.title = title;
    }

    @Override
    public void play() {
        System.out.println("Video title: " + title);
        // LEARNING: Null check ensures RealVideo is created once — virtual proxy
        // pattern.
        if (realVideo == null) {
            realVideo = new RealVideo(filename);
        }
        realVideo.play();
    }
}

// LEARNING: Client depends on Video interface — unaware whether it holds proxy
// or real object.
class VideoPlayer {
    private Video video;

    public VideoPlayer(Video video) {
        this.video = video;
    }

    public void playVideo() {
        video.play();
    }
}

public class ProxyDemo {
    public static void main(String[] args) {
        VideoPlayer player = new VideoPlayer(new VideoProxy("amplifier.mp4", "Amplifier"));

        player.playVideo();

        System.out.println("\n--- Playing the same video again ---\n");
        // LEARNING: Second call skips loading — RealVideo already initialized.
        player.playVideo();
    }
}