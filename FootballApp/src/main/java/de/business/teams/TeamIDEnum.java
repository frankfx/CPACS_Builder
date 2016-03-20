package de.business.teams;

public enum TeamIDEnum {
	BVB("Borussia Dortmund"), FCB("Bayern München"), LEV("Bayer 04 Leverkusen"), Hertha("Hertha BSC"), BMG("Borussia Mönchengladbach"), 
	S04("FC Schalke 04"), FSV("1. FSV Mainz 05"), VFL("VfL Wolfsburg"), FCK("1. FC Köln"), Ingol("FC Ingolstadt 04"), 
	VFB("VfB Stuttgart"), HSV("Hamburger SV"), FCA("FC Augsburg"), D89("SV Darmstadt 98"), SVW("Werder Bremen"), 
	EINTRACHT("Eintracht Frankfurt"), TSG("TSG 1899 Hoffenheim"), H96("Hannover 96"), EMTPY("Empty");

	String name = "";
	
	private TeamIDEnum(String name){
		this.name = name;
	}
	
	public static TeamIDEnum getType(String str){
		if (str == null)
			return TeamIDEnum.EMTPY;
		else if(str.equals(BVB.name))
			return TeamIDEnum.BVB;
		else if(str.equals(FCB.name))
			return TeamIDEnum.FCB;
		else if(str.equals(LEV.name))
			return TeamIDEnum.LEV;
		else if(str.equals(Hertha.name))
			return TeamIDEnum.Hertha;
		else if(str.equals(BMG.name))
			return TeamIDEnum.BMG;
		else if(str.equals(S04.name))
			return TeamIDEnum.S04;
		else if(str.equals(FSV.name))
			return TeamIDEnum.FSV;		
		else if(str.equals(VFL.name))
			return TeamIDEnum.VFL;
		else if(str.equals(FCK.name))
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
		else return TeamIDEnum.EMTPY;
	}	
}
