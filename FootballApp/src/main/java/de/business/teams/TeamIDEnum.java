package de.business.teams;

public enum TeamIDEnum {
	BVB("Borussia Dortmund", "964"), FCB("Bayern", "961"), LEV("Bayer 04 Leverkusen", "963"), Hertha("Hertha BSC"), BMG("Borussia M"), 
	S04("FC Schalke 04"), FSV("1. FSV Mainz 05"), VFL("VfL Wolfsburg"), FCK("1. FC K"), Ingol("FC Ingolstadt 04"), 
	VFB("VfB Stuttgart"), HSV("Hamburger SV", "967"), FCA("FC Augsburg"), D89("SV Darmstadt 98"), SVW("Werder Bremen"), 
	EINTRACHT("Eintracht Frankfurt"), TSG("TSG 1899 Hoffenheim"), H96("Hannover 96"), ZWICKAU("FSV Zwickau", "2370"),
	ESSEN("Rot Weiss Essen", "994"), VFR("VFR Aalen", "1002"), EMPTY("Empty");
	
	// soccerway id	
	String id = "";
	String name = "";
	
	private TeamIDEnum(String pName, String pId){
		this.name = pName;
		this.id = pId;
	}
	
	private TeamIDEnum(String pName){
		this(pName, "");
	}
	
	public static TeamIDEnum getType(String str){
		if (str == null)
			return TeamIDEnum.EMPTY;
		else if(str.equals(BVB.name))
			return TeamIDEnum.BVB;
		else if(str.contains(FCB.name)) // use "contains" because the web service uses an Umlaut instead of ue in Bayern Muenchen 
			return TeamIDEnum.FCB;
		else if(str.equals(LEV.name))
			return TeamIDEnum.LEV;
		else if(str.equals(Hertha.name))
			return TeamIDEnum.Hertha;
		else if(str.contains(BMG.name)) // use "contains" because the web service uses an Umlaut instead of oe in Mönchengladbach
			return TeamIDEnum.BMG;
		else if(str.equals(S04.name))
			return TeamIDEnum.S04;
		else if(str.equals(FSV.name))
			return TeamIDEnum.FSV;		
		else if(str.equals(VFL.name))
			return TeamIDEnum.VFL;
		else if(str.contains(FCK.name)) // use "contains" because the web service uses an Umlaut instead of oe in Koeln
			return TeamIDEnum.FCK;
		else if(str.equals(Ingol.name))
			return TeamIDEnum.Ingol;		
		else if(str.equals(VFB.name))
			return TeamIDEnum.VFB;
		else if(str.equals(HSV.name))
			return TeamIDEnum.HSV;
		else if(str.equals(FCA.name))
			return TeamIDEnum.FCA;
		else if(str.equals(D89.name))
			return TeamIDEnum.D89;
		else if(str.equals(SVW.name))
			return TeamIDEnum.SVW;		
		else if(str.equals(EINTRACHT.name))
			return TeamIDEnum.EINTRACHT;
		else if(str.equals(TSG.name))
			return TeamIDEnum.TSG;
		else if(str.equals(H96.name))
			return TeamIDEnum.H96;
		else return TeamIDEnum.EMPTY;
	}	
	
	@Override
	public String toString(){
		return this.name();
	}
	
	public String getID(){
		return this.id;
	}
}
