package de.presentation;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import de.utils.ResourceService;


public class JSplitButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenuItem mCommitItem = new JMenuItem("commit");
	private JMenuItem mPullItem = new JMenuItem("pull");
	private JMenuItem mPullDetailItem = new JMenuItem("pull...");
	private JMenuItem mRemoveItem = new JMenuItem("remove");
	private JMenuItem mRevertItem = new JMenuItem("revert");
	private JMenuItem mDBBrowserItem = new JMenuItem("database browser");
	
	
	public JSplitButton(String pTitle) {
		super(pTitle);
        
        setHorizontalAlignment(SwingConstants.RIGHT);
        setIconTextGap(-getText().length()*18);

		Image lImg = ResourceService.getInstance().IMGAGE_ICON_ARROW_DOWN.getScaledInstance(13, 15, Image.SCALE_SMOOTH);

		this.setIcon(new ImageIcon(lImg, "ARROW DOWN"));

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
		lPopupMenu.add(mPullDetailItem);
		lPopupMenu.addSeparator();
		lPopupMenu.add(mRemoveItem);
		lPopupMenu.add(mRevertItem);
		lPopupMenu.addSeparator();
		lPopupMenu.add(mDBBrowserItem);
		
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

	public JMenuItem getRemoveItem() {
		return mRemoveItem;
	}

	public void setmRemoveItem(JMenuItem pRemoveItem) {
		this.mRemoveItem = pRemoveItem;
	}	
	
	public JMenuItem getRevertItem() {
		return mRevertItem;
	}

	public void setRevertItem(JMenuItem pRevertItem) {
		this.mRevertItem = pRevertItem;
	}

	public JMenuItem getDBBrowserItem() {
		return mDBBrowserItem;
	}

	public void setDBBrowserItem(JMenuItem pDBBrowser) {
		this.mDBBrowserItem = pDBBrowser;
	}	

	public JMenuItem getPullDetailItem() {
		return mPullDetailItem;
	}

	public void setPullDetailItem(JMenuItem pPullDetailItem) {
		this.mPullDetailItem = pPullDetailItem;
	}

}