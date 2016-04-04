package de.business.teams;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;

import data.Tes;

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
	private ImageIcon createImageIcon(Image pFile, String pDescription) {
		Image lDimg = pFile;
//		try {
//		    lDimg = ImageIO.read(pFile).getScaledInstance(25, 25, Image.SCALE_SMOOTH);
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}	
		return new ImageIcon(lDimg, pDescription);
	}	
	
	private Image getIconPath(String pFilename){
		
//		final URL url = Thread.currentThread().getContextClassLoader().getResource( "images" + File.separator + pFilename);
//	    return Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		
//		String t = RessourceService.getRessourceImages(pFilename).getFile();
		
//		System.out.println(t + ", " +  new File(t).exists());
		
//		System.out.println("Hier1 : " + RessourceService.getRessourceImages(pFilename));
		
//		System.out.println(Toolkit.getDefaultToolkit().getImage(RessourceService.getRessourceImages(pFilename)));
		
		System.out.println(pFilename);
		System.out.println(Tes.getInstance().getRessourceImages(pFilename));
		
		return Toolkit.getDefaultToolkit().getImage(Tes.getInstance().getRessourceImages(pFilename)).getScaledInstance(25, 25, Image.SCALE_SMOOTH);
	}
	
	public String toString(){
		return this.mName;
	}
}