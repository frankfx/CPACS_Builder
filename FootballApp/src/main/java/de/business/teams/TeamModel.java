package de.business.teams;

import java.awt.Image;
import java.awt.image.BufferedImage;
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
		mIcon = new ImageIcon(getIconPath("images/" + pIcon), pName);
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
	
	private Image getIconPath(String pFilename){
		try {
			// Get current classloader
			ClassLoader cl = this.getClass().getClassLoader();
			BufferedImage image = ImageIO.read(cl.getResourceAsStream(pFilename));
			return image.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString(){
		return this.mName;
	}
}