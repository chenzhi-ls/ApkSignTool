package com.apksign.tool.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.apksign.tool.utils.ALog;
import com.apksign.tool.utils.AUtils;

/**
 * Created by chenzhi on 2019年12月25日
 */
public class ApkPathJPanal {
	
	private JTextField textField;

	public JComponent getApkPathContentComponent() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u539F\u59CBAPK\u5305", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 483, 81);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("文件：");
		lblNewLabel.setBounds(20, 34, 54, 15);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(71, 31, 248, 21);
		textField.setFont(new Font("宋体", Font.PLAIN, 14)); // 修改字体样式
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("选择APK文件");
		btnNewButton_1.setBounds(339, 31, 119, 21);
		panel.add(btnNewButton_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseApkFile();
			}
		});
		
		return panel;
	}
	
	private void chooseApkFile() {
		String filePath = "";
		JFileChooser jFileChooser = new JFileChooser();
		// 这里注意addChoosableFileFilter和setFileFilter的区别 前者是在原来的过滤器中添加
		// 后者是直接指定唯一的过滤器
		jFileChooser.setFileFilter(new FileNameExtensionFilter("apk文件", new String[] { "apk" }));
		int result = jFileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			filePath = jFileChooser.getSelectedFile().toString();
			String formatStr = AUtils.getFileFormat(filePath);

			if (!formatStr.equals("apk")) {
				JOptionPane.showMessageDialog(null, "文件格式错误", "警告", JOptionPane.WARNING_MESSAGE);
				return;
			}

			try {
				textField.setText(filePath);
			} catch (Exception e) {
				ALog.error(e.getMessage(), e);
			}

		} else if (result == JFileChooser.CANCEL_OPTION) {
			ALog.debug("取消文件选择");
		} else if (result == JFileChooser.ERROR_OPTION) {
			ALog.debug("选择文件出错");
			JOptionPane.showMessageDialog(null, "文件格式错误", "警告", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public String getApkPath() {
		return textField != null ? textField.getText() : "";
	}
}
