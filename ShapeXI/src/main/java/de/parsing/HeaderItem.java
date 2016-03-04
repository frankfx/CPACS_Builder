package de.parsing;


public class HeaderItem {
	
	private String name;
	private String description;
	private String creator;
	private String version;
	private String shapeVersion;	
	
	public String getName() {
		return name;
	}
	public void setName(String pName) {
		name = pName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String pDescription) {
		description = pDescription;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String pCreator) {
		creator = pCreator;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String pVersion) {
		version = pVersion;
	}
	public String getShapeVersion() {
		return shapeVersion;
	}
	public void setShapeVersion(String pShapeVersion) {
		shapeVersion = pShapeVersion;
	}

	public String toString(){
	    return "Item [name=" + name + ", description=" + description + ", creator="
	            + creator + ", version=" + version + ", shapeVersion=" + shapeVersion + "]";
	}
}
