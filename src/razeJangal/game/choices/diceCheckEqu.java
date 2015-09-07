package razeJangal.game.choices;

/**
 * when the number of dices are equal
 * @author Pegah Jandaghi
 *
 */
public class diceCheckEqu extends diceCheck{
	private int[]emptyOranges;
	private int[]Violets;
	
	public int[] getEmptyOranges() {
		return emptyOranges;
	}
	public void setEmptyOranges(int[] emptyOranges) {
		this.emptyOranges = emptyOranges;
	}
	public int[] getViolets() {
		return Violets;
	}
	public void setViolets(int[] violets) {
		Violets = violets;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof diceCheckEqu))
			return false;
		diceCheckEqu d = (diceCheckEqu)o;
		diceCheck.sort(d.getEmptyOranges());
		diceCheck.sort(getEmptyOranges());
		diceCheck.sort(d.getViolets());
		diceCheck.sort(getViolets());
		//if(!eq(d.getEmptyOranges(), getEmptyOranges()))
		//	return false;
		if (!eq(d.getViolets(), getViolets()))
			return false;
		if (!eq(((diceCheck)d).getpossibleMoves()[0],((diceCheck)this).getpossibleMoves()[0] )){
			return false;
		}
		return true;
	}
}
