package com.tafayor.selfcamerashot.remoteControl.sound;

public interface SoundControlListener
{
	public abstract void onWhistleDetected();
    public abstract void onClappingDetected();
    public abstract void onException();
}