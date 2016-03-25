package de.presentation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import de.utils.RessourceService;

public class JSplitButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenuItem mCommitItem = new JMenuItem("commit");
	private JMenuItem mPullItem = new JMenuItem("pull");
	private JMenuItem mRevertItem = new JMenuItem("revert");
	
	public JSplitButton(String pTitle) {
		super(pTitle);
        
        setHorizontalAlignment(SwingConstants.RIGHT);
        setIconTextGap(-getText().length()*18);

        BufferedImage lBuffImg;
		try {
			lBuffImg = ImageIO.read(new File(RessourceService.IMGAGE_ICON_ARROW_DOWN));
	        ImageIcon lIcon = new ImageIcon(lBuffImg.getScaledInstance(13, 15, Image.SCALE_SMOOTH), "Arrow down");
	        this.setIcon(lIcon);
		} catch (IOException e) {
			System.out.println("Could not set arrow icon: " + e.getMessage());
		}

		addListener();
	}
	
	private void addListener() {
		addMouseListener(new MouseAdapter() {
			JPopupMenu lPopupMenu = getPopupMenu();

			public void mousePressed(MouseEvent e) {
				lPopupMenu.show(JSplitButton.this, getWidth()-30, getHeight());
			}
		});
	}

	private JPopupMenu getPopupMenu() {
		JPopupMenu lPopupMenu = new JPopupMenu();

		lPopupMenu.add(mCommitItem);
		lPopupMenu.add(mPullItem);
		lPopupMenu.add(mRevertItem);
		
		return lPopupMenu;
	}
	
	public JMenuItem getCommitItem() {
		return mCommitItem;
	}

	public void setCommitItem(JMenuItem pCommitItem) {
		this.mCommitItem = pCommitItem;
	}

	public JMenuItem getPullItem() {
		return mPullItem;
	}

	public void setPullItem(JMenuItem pPullItem) {
		this.mPullItem = pPullItem;
	}

	public JMenuItem getRevertItem() {
		return mRevertItem;
	}

	public void setRevertItem(JMenuItem pRevertItem) {
		this.mRevertItem = pRevertItem;
	}	
}