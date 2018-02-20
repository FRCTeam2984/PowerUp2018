package org.ljrobotics.lib.util.events;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Triggerer {

	private static Triggerer instance;
	public Triggers lastTrigger;
	
	public static Triggerer getInstance() {
		if(instance == null) {
			instance = new Triggerer();
		}
		return instance;
	}
	
	private EnumMap<Triggers, List<Command>> commandHandlers;
	
	public Triggerer() {
		this.commandHandlers = new EnumMap<Triggers, List<Command>>(Triggers.class);
	}
	
	public void addCommand(Triggers trigger, Command command) {
		if(!this.commandHandlers.containsKey(trigger)) {
			this.commandHandlers.put(trigger, new ArrayList<Command>());
		}
		this.commandHandlers.get(trigger).add(command);
	}
	
	/**
	 * @param trigger the trigger to execute upon
	 * @return whether or not it was successfully triggered
	 */
	public boolean trigger(Triggers trigger) {
		this.lastTrigger = trigger;
		if(!this.commandHandlers.containsKey(trigger)) {
			return false;
		}
		List<Command> commands = this.commandHandlers.get(trigger);
		for(Command command : commands) {
			Scheduler.getInstance().add(command);
		}
		return true;
	}
	
}
