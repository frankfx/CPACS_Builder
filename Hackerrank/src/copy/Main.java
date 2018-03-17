package copy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {

	public void calc (Collection<Forderungen> collection) {
		
		System.out.println("default array");		
		System.out.println(collection);
		
		System.out.println();
		
		
		List<Forderungen> copy = collection.stream().map(f -> f.clone()).collect(Collectors.toList());
		
		
		copy.get(0).setBetrag(BigDecimal.TEN);
		
		
		System.out.println(collection);
		System.out.println(copy);
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		
		Collection<Forderungen> list = new ArrayList<>();
		list.add(new Forderungen(new BigDecimal("122"), new BigDecimal("3")));
		
		main.calc(list);
	}
}
