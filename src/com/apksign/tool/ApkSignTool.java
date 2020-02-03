package com.apksign.tool;

import java.awt.EventQueue;

import com.apksign.tool.ui.MainFrame;

/**
 * Created by chenzhi on 2019年12月20日
 */
public class ApkSignTool {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
