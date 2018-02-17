package org.ljrobotics.frc2018.commands;

import edu.wpi.first.wpilibj.command.Command;

public class WaitSecond extends Command{

	private long startTime = Long.MAX_VALUE;
	private int wait;
	private boolean run = false;
	
	public WaitSecond(int milis) {
		this.wait = milis;
	}
	
	public void initilize() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void execute() {
		if(!this.run) {
			startTime = System.currentTimeMillis();
			this.run = true;
		}
	}
	
	public boolean isFinished() {
		return System.currentTimeMillis() > startTime + this.wait;
	}
	
}
