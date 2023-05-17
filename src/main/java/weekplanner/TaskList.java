package weekplanner;

import java.util.ArrayList;

public class TaskList
{
	public int id;
	public String name;
	public ArrayList<String> tasks;
	
	public TaskList(int id, String name)
	{
		this.id = id;
		this.name = name;
		this.tasks = new ArrayList<String>();
	}
	
	
}
