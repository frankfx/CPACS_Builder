package de.business.teams;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class TeamModel {
	private String mName;
	private int mPosition;
	private ImageIcon mIcon;	
	private TeamIDEnum mId;
	
	public TeamModel(String pName) {
		this.mName = pName;
	}

	public TeamModel(String pName, String pIcon) {
		this(pName);
		mIcon = createImageIcon(getIconPath(pIcon), pName);
	}	

	public String getName() {
		return mName;
	}

	public void setName(String pName) {
		this.mName = pName;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int pPosition) {
		this.mPosition = pPosition;
	}

	public ImageIcon getIcon() {
		return mIcon;
	}

	public void setIcon(ImageIcon pIcon) {
		this.mIcon = pIcon;
	}	
	
	public TeamIDEnum getId() {
		return mId;
	}

	public void setId(TeamIDEnum pId) {
		this.mId = pId;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	private ImageIcon createImageIcon(File pFile, String pDescription) {
		Image lDimg = null;
		try {
		    lDimg = ImageIO.read(pFile).getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		} catch (IOException e) {
		    e.printStackTrace();
		}	
		return new ImageIcon(lDimg, pDescription);
	}	
	
	private File getIconPath(String pFilename){
        StringBuilder sb =new StringBuilder();
        sb.append(System.getProperty("user.dir"));
        sb.append(File.separator);
        sb.append("src");
        sb.append(File.separator);
        sb.append("main");
        sb.append(File.separator);
        sb.append("resources");
        sb.append(File.separator);
        sb.append("images");
        sb.append(File.separator);
        sb.append(pFilename);
        return new File(sb.toString());
	}
	
	public String toString(){
		return this.mName;
	}
}