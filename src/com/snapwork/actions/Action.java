package com.snapwork.actions;




//gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg



public class Action
{
	private byte command;
	private Object commandData;

	public Action(byte command,Object commandData)
	{
		setCommand(command);
		setCommandData(commandData);
	}

	public byte getCommand()
	{
		return command;
	}

	public void setCommand(byte command)
	{
		this.command = command;
	}

	public Object getCommandData()
	{
		return commandData;
	}

	public void setCommandData(Object commandData)
	{
		this.commandData = commandData;
	}

}
