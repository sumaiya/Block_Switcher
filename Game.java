package switchGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

	private static final LL<Integer> goal = new LL<Integer>();
	private static final LL<Integer> initial = new LL<Integer>();

	
	public LL<Integer> getGoal()
	{
		return goal;
	}
	
	public boolean haveWon(LL<Integer> guess)
	{
		return goal.equals(guess);
	}
	
	public static LL<Integer> generateGoal(int length)
	{
		goal.clear();
		List<Integer> nums = new ArrayList<Integer>();
		for (Integer i = 0; i < length; i++)
		{
			nums.add(i);
		}
		Collections.shuffle(nums);
		for (int i = 0; i < nums.size(); i++)
		{
			goal.add(nums.get(i));
		}
		return goal;
	}
	
	public static LL<Integer> generateInitial(Integer length)
	{
		initial.clear();
		List<Integer> nums = new ArrayList<Integer>();
		for (Integer i = 0; i < length; i++)
		{
			nums.add(i);
		}
		Collections.shuffle(nums);
		for (int i = 0; i < nums.size(); i++)
		{
			initial.add(nums.get(i));
		}
		while(initial.equals(goal))
		{
			Collections.shuffle(nums);
			for (int i = 0; i < nums.size(); i++)
			{
				initial.add(nums.get(i));
			}
		}
		return initial;	
	}
}
