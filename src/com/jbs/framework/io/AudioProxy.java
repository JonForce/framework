package com.jbs.framework.io;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class AudioProxy {
	
	protected final AssetManager assets;
	protected final ArrayList<Music> currentlyPlayingMusic;
	
	/** The volume to play Sounds at when no volume is specified. */
	private float defaultSoundVolume = 1;
	/** Whether or not to loop music when it is not specified one way or the other. */
	private boolean defaultMusicLooping = false;
	/** True when the AudioProxy should produce no noise. */
	private boolean isMuted;
	/** True if the AudioProxy should treat redundant/superfluous usage as an error. */
	private final boolean isStrict;
	
	public AudioProxy(AssetManager assets, boolean isStrict) {
		this.assets = assets;
		this.isStrict = isStrict;
		this.currentlyPlayingMusic = new ArrayList<Music>();
	}
	
	/** Play the Sound located at the specified source. */
	public void playSound(FileHandle soundSource, float volume) {
		// If the AudioProxy is muted,
		if (this.isMuted())
			// Return without playing.
			return;
		
		getSound(soundSource).play(volume);
	}
	
	/** Play the Sound located at the specified source. Uses the AudioProxy's defaultSoundVolume. */
	public void playSound(FileHandle soundSource) {
		playSound(soundSource, this.defaultSoundVolume());
	}
	
	/** Retrieve the specified Music from the AudioProxy's AssetManager then play it. If
	 * the AudioProxy isStrict and the Music is already playing, this method will throw
	 * the AudioProxy's musicAlreadyPlayingException. */
	public void playMusic(FileHandle musicSource, boolean shouldLoopMusic) {
		Music music = getMusic(musicSource);
		
		// Add the Music to our List of playing Music.
		// This assures that if the Audio is muted, when the Audio becomes unmuted the Music will play.
		currentlyPlayingMusic.add(music);
		
		// If the AudioProxy is muted,
		if (this.isMuted())
			// Return without playing the Music.
			return;
		
		// If our AudioProxy is being strict and the Music is already playing,
		if (this.isStrict() && music.isPlaying())
			throw new RuntimeException("Cannot play Music that is already playing.");
		
		// Set whether the Music should loop or not.
		music.setLooping(shouldLoopMusic);
		// Play the aforementioned Music.
		music.play();
	}
	
	public void pauseMusic(FileHandle musicSource) {
		Music music = getMusic(musicSource);
		
		if (!currentlyPlayingMusic.contains(music))
			throw new RuntimeException("Cant pause, not playing music : " + music);
		
		currentlyPlayingMusic.remove(music);
		
		music.pause();
	}
	
	/** Retrieve the specified Music from the AudioProxy's AssetManager then play it. If
	 * the AudioProxy isStrict and the Music is already playing, this method will throw
	 * the AudioProxy's musicAlreadyPlayingException. Uses the AudioProxy's defaultMusicLooping. */
	public void playMusic(FileHandle musicSource) {
		playMusic(musicSource, this.defaultMusicLooping());
	}
	
	/** Retrieve the specified Music from the AudioProxy's AssetManager then play it. If
	 * the AudioProxy isStrict and the Music not playing, this method will throw
	 * the AudioProxy's musicNotPlayingException. */
	public void stopMusic(FileHandle musicSource) {
		Music music = getMusic(musicSource);
		
		// Remove the Music from our currentlyPlayingMusic List.
		currentlyPlayingMusic.remove(music);
		
		// If the Audio is muted,
		if (this.isMuted())
			// The Music is not audible, do not attempt to stop it from playing.
			return;
			
		// If our AudioProxy is being strict and the Music is not playing,
		if (this.isStrict() && !music.isPlaying())
			throw new RuntimeException("Cannot stop Music that is not playing.");
		
		// Stop the Music.
		music.stop();
	}
	
	/* Set the AudioProxy to not play Audio and stop Music currently playing. */
	public void mute() {
		// Stop all currently playing Music,
		for (Music m : currentlyPlayingMusic)
			m.stop();
		
		// Mark the AudioProxy as muted.
		this.isMuted = true;
	}
	
	/* Enable the AudioProxy's ability to play Audio and resume Music that was playing when
	 * the Audio was muted. */
	public void unmute() {
		for (Music m : currentlyPlayingMusic)
			m.play();
		
		this.isMuted = false;
	}
	
	/* Set whether or not to loop Music when looping is not specified. */
	public void setDefaultMusicLooping(boolean newDefault) {
		this.defaultMusicLooping = newDefault;
	}
	
	/* Set the volume to play Sounds at when no volume is specified. */
	public void setDefaultSoundVolume(float newDefault) {
		this.defaultSoundVolume = newDefault;
	}
	
	/* @return the volume to play Sounds at when no volume is specified. */
	public float defaultSoundVolume() {
		return this.defaultSoundVolume;
	}
	
	/* @return true if the Music should loop by default. */
	public boolean defaultMusicLooping() {
		return this.defaultMusicLooping;
	}
	
	/* @return true if no audio should be heard. */
	public boolean isMuted() {
		return this.isMuted;
	}
	
	/* Retrieve the Sound from the Game's AssetManager. Throws RuntimeException if
	 * the Game has not yet been created with it's create() method. */
	protected Sound getSound(FileHandle soundSource) {
		if (this.isStrict())
			assertAssetIsValid(soundSource);
		
		// Retrieve the sound from the Game's assets.
		return (Sound) assets.get(soundSource.path());
	}
	
	/* Retrieve the Music from the Game's AssetManager. Throws RuntimeException if
	 * the Game has not yet been created with it's create() method. */
	protected Music getMusic(FileHandle musicSource) {
		if (this.isStrict())
			assertAssetIsValid(musicSource);
		
		// Retrieve the music from the Game's assets.
		return (Music) assets.get(musicSource.path());
	}
	
	protected void assertAssetIsValid(FileHandle asset) {
		// If the asset has not been loaded,
		if (!assets.isLoaded(asset.path())) {
			// If the asset does not exist,
			if (!asset.exists())
				throw new RuntimeException(asset.path() + " does not exist.");
			else
				throw new RuntimeException(asset.path() + " is not yet loaded.");
		}
	}
	
	/* @return true if the AudioProxy should throw Exceptions if there is redundant/superfluous usage. */
	protected boolean isStrict() {
		return isStrict;
	}
}