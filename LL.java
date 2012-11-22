package switchGame;

import java.util.LinkedList;

public class LL<T extends Comparable<T>> extends LinkedList<T> 
{
	private void LL()
	{
		
	}

	
	public boolean swap(int index)
	{
		if (index < this.size() - 1)
		{
			T first = this.get(index);
			T second = this.get(index + 1);
			this.set(index, second);
			this.set(index + 1, first);
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public LL<T> copyLL()
	{
		LL copy = new LL();
		for (T elem : this)
		{
			copy.add(elem);
		}
		return copy;
	}
}