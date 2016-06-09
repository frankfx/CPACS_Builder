package de.business.teams;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import de.business.PositionModel;

public class TeamModel {
	private TeamIDEnum mId;
	private String mName;
	private ImageIcon mIcon;
	private PositionModel mPosition;
	
	public TeamModel(String pName) {
		this.mName = pName;
		this.mPosition = new PositionModel();
	}

	public TeamModel(String pName, String pIcon) {
		this(pName);
		mIcon = pIcon == null ? null : new ImageIcon(getIconPath("images/" + pIcon), pName);
	}	

	public String getName() {
		return mName;
	}

	public void setName(String pName) {
		this.mName = pName;
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
	
	public void setPositon(PositionModel pPostionModel){
		this.mPosition = pPostionModel;
	}
	
	public void setPosition(int pPlace, int pPoints){
		mPosition.setPlace(pPlace);
		mPosition.setPoints(pPoints);
	}

	public PositionModel getPosition(){
		return mPosition;
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