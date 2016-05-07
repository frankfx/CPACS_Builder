
public enum MahnBelegartType {
	SAEUMNISZUSCHLAG, VERSPAETUNGSZUSCHLAG, GEBÜHR, BEITRAG, ERROR;

	public static MahnBelegartType getRandomMahnbelegart() {

		int fac = (int) (2 * Math.random());
		int n = (int) (4 * Math.random()) * fac;
		
		switch (n) {
		case 0:
			return SAEUMNISZUSCHLAG;
		case 1:
			return VERSPAETUNGSZUSCHLAG;
		case 2:
			return GEBÜHR;
		case 3:
			return BEITRAG;
		default:
			return ERROR;
		}
	}
}
