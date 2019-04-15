package compile;

import java.util.Stack;

public class RegisterManager {

	private Writer writer;
	private StackCounter stackCounter;
	private int available;
	private Stack<Integer> previousCounts;
	private int currentCount;

	public RegisterManager(Writer writer, StackCounter stackCounter, int available) {
		this.writer = writer;
		this.stackCounter = stackCounter;
		this.available = available;
		this.previousCounts = new Stack<Integer>();
		this.currentCount = 0;
	}

	public void descend() {
		this.previousCounts.push(this.currentCount);
		this.currentCount = 0;
	}

	public void ascend() {
		this.currentCount = this.previousCounts.pop();
	}

	public void saveAll(int current) {
		for (int i = 1, l = this.currentCount > this.available ? this.available : this.currentCount; i <= l; ++i) {
			if (i != current) {
				this.save(i);
			}
		}
	}

	public void restoreAll(int current) {
		for (int i = this.currentCount > this.available ? this.available : this.currentCount; i >= 1; --i) {
			if (i != current) {
				this.restore(i);
			}
		}
	}

	private void save(int register) {
		writer.writeFunction(String.format("STW R%d, -(SP) // Empile un registre", register));
		this.stackCounter.addCount(2);
	}

	private void restore(int register) {
		this.stackCounter.addCount(-2);
		writer.writeFunction(String.format("LDW R%d, (SP)+ // Dépile un registre", register));
	}

	public int provideRegister() {
		if (this.currentCount < this.available) {
			return ++this.currentCount;
		} else {
			int register = ++this.currentCount % this.available;
			this.save(register);
			return register;
		}
	}

	public void freeRegister() {
		if (this.currentCount <= this.available) {
			this.currentCount--;
		} else {
			this.restore(this.currentCount-- % this.available);
		}
	}

}
