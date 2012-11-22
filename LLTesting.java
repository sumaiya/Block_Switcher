package switchGame;

public class LLTesting {

	public static void main(String[] args)
	{
		LL one = new LL();
		one.add(1);
		one.add(4);
		one.add(5);
		one.add(2);
		System.out.println(one.toString());
		one.swap(2);
		System.out.println(one.toString());
		one.swap(1);
		System.out.println(one.toString());
		one.swap(0);
		System.out.println(one.toString());
		one.swap(3);
		System.out.println(one.toString());

		LL two = new LL();
		two.add(2);
		two.add(1);
		two.add(5);
		two.add(4);
		System.out.println(two.equals(one));
	}
}
