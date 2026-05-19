package com.lld.patterns.behavioral.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// LEARNING: Concrete class which hold necessary info.
class Song {
    private String title;
    private String author;

    public Song(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

// LEARNING: Playlist implement Iterable interface already provided by java.
class Playlist implements Iterable<Song> {
    private List<Song> songs;

    public Playlist() {
        this.songs = new ArrayList<>();
    }

    // LEARNING: Creating song iterator to iterate over the playlist. Iterator will return new instance every time.
    // WHY: By returning new instance of playlistIterator every time will help in handling playlist by two iterator simultaneously.
    @Override
    public Iterator<Song> iterator() {
        Iterator<Song> playlistIterator = new PlaylistIterator(songs);
        return playlistIterator;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

}

// LEARNING: Creating actual Iterator which holds cursor, songs, hasNext and next
class PlaylistIterator implements Iterator<Song> {
    private int cursor;
    private List<Song> songs;

    public PlaylistIterator(List<Song> songs) {
        // LEARNING: We always initialize cursor to 0, because traversal always begins at first element.
        this.cursor = 0;
        this.songs = songs;
    }

    // LEARNING: We have hasNext which guards our iterator, so that it should not go out of bound for collection.
    @Override
    public boolean hasNext() {
        return cursor < songs.size();
    }

    @Override
    public Song next() {
        return songs.get(cursor++);
    }
}

// LEARNING: MusicPlayer is the actual client which is holding the playlist.
class MusicPlayer {
    private Playlist playlist;

    public MusicPlayer(Playlist playlist) {
        this.playlist = playlist;
    }

    public void display() {
        Iterator<Song> pIterator = playlist.iterator();
        while (pIterator.hasNext()) {
            Song song = pIterator.next();
            System.out.println("Song Title: " + song.getTitle() + " Song Author: " + song.getAuthor());
        }
    }

}

public class IteratorDemo {
    public static void main(String[] args) {

        Playlist playlist = new Playlist();
        playlist.addSong(new Song("Song1", "Auth1"));
        playlist.addSong(new Song("Song2", "Auth2"));
        playlist.addSong(new Song("Song3", "Auth3"));

        MusicPlayer musicPlayer = new MusicPlayer(playlist);
        musicPlayer.display();

        for (Song s : playlist) {
            System.err.println("Current Songs title: " + s.getTitle());
        }
        
        // LEARNING: Two different iterator would be handle separately.
        Iterator<Song> it1 = playlist.iterator();
        Iterator<Song> it2 = playlist.iterator();
        System.out.println("Playing song with it1: " + it1.next().getTitle());
        System.out.println("Playing song with it2: " + it2.next().getTitle());
    }
}
