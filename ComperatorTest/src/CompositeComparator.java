import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CompositeComparator<T> implements Comparator<T> {

	private List<Comparator<T>> defaultFoobarComparison;

	public CompositeComparator(Comparator<T>... args) {
		defaultFoobarComparison = Arrays.<Comparator<T>> asList(args);
	}

	@Override
	public int compare(T o1, T o2) {
		for (Comparator<T> comp : defaultFoobarComparison) {
			int result = comp.compare(o1, o2);
			if (result != 0)
				return result;
		}
		return 0;
	}

	final static Comparator<ForderungVerbindlichkeitModel> sFORDERUNGVERBINDLICHKEIT_MAHNBELEGART_COMPARATOR = new Comparator<ForderungVerbindlichkeitModel>() {
		@Override
		public int compare(ForderungVerbindlichkeitModel o1, ForderungVerbindlichkeitModel o2) {
			return o1.getMahnbelegart().toString().compareTo(o2.getMahnbelegart().toString());
		}
	};
}
