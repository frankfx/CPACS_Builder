import java.util.ArrayList;
import java.util.List;

public class Mahnung {

	MahnStatus mMahnstatus;
	private static int count = 1;
	
	private int id = 1;

	List<ForderungVerbindlichkeitModel> mForderungen = new ArrayList<ForderungVerbindlichkeitModel>();

	public Mahnung(MahnStatus pMahnstatus) {
		mMahnstatus = pMahnstatus;
		id = count;
		count++;
	}

	public MahnStatus getMahnstatus() {
		return mMahnstatus;
	}

	public void addForderungen(ForderungVerbindlichkeitModel pForderung) {
		mForderungen.add(pForderung);
	}

	public List<ForderungVerbindlichkeitModel> getAlleForderungenUndGebuehrenZurMahnung() {
		return mForderungen;
	}

	@Override
	public String toString() {
		return "Mahnung[" + id + "]";
	}
}
