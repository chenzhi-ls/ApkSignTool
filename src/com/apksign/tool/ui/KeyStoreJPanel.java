package com.apksign.tool.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.apksign.tool.utils.ALog;
import com.apksign.tool.utils.AUtils;

/**
 * Created by chenzhi on 2019年12月20日
 */
public class KeyStoreJPanel {

	private JTextField textField_1, textField_2, textField_3;
	private JCheckBox chckbxV2, chckbxV1, chckbxV3;

	public JComponent getContentJComponent() {
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "签名文件", TitledBorder.LEFT,
				TitledBorder.TOP, null, Color.BLACK));
		panel_1.setBounds(10, 100, 483, 187);
		panel_1.setLayout(null);

		textField_1 = new JTextField();
		textField_1.setBounds(71, 31, 248, 21);
		textField_1.setFont(new Font("宋体", Font.PLAIN, 14)); // 修改字体样式
		panel_1.add(textField_1);
		textField_1.setColumns(10);

		JButton button = new JButton("选择签名文件");
		button.setBounds(339, 31, 119, 21);
		panel_1.add(button);

		JLabel label = new JLabel("文件：");
		label.setBounds(20, 34, 71, 15);
		panel_1.add(label);

		JLabel label_1 = new JLabel("密码：");
		label_1.setBounds(20, 65, 71, 15);
		panel_1.add(label_1);

		JLabel label_2 = new JLabel("别名：");
		label_2.setBounds(20, 96, 71, 15);
		panel_1.add(label_2);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(71, 62, 248, 21);
		textField_2.setFont(new Font("宋体", Font.PLAIN, 14)); // 修改字体样式
		panel_1.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(71, 93, 248, 21);
		textField_3.setFont(new Font("宋体", Font.PLAIN, 14)); // 修改字体样式
		panel_1.add(textField_3);

		JPanel panel = new JPanel();
		panel.setBounds(18, 134, 418, 40);
		panel_1.add(panel);
		panel.setLayout(null);

		chckbxV2 = new JCheckBox(" V2签名");
		chckbxV2.setBounds(93, 6, 77, 23);
		panel.add(chckbxV2);

		chckbxV1 = new JCheckBox(" V1签名");
		chckbxV1.setBounds(0, 6, 77, 23);
		panel.add(chckbxV1);
		
		chckbxV3 = new JCheckBox(" Zip对齐");
		chckbxV3.setBounds(186, 6, 77, 23);
		panel.add(chckbxV3);
		chckbxV3.setSelected(true);

		chckbxV1.setSelected(true);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseApkNewIcon();
			}
		});

		return panel_1;
	}

	private String chooseApkNewIcon() {
		String filePath = "";
		JFileChooser jFileChooser = new JFileChooser();
		// 这里注意addChoosableFileFilter和setFileFilter的区别 前者是在原来的过滤器中添加
		// 后者是直接指定唯一的过滤器
		jFileChooser.setFileFilter(new FileNameExtensionFilter("签名文件", new String[] { "keystore", "jks" }));
		int result = jFileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			filePath = jFileChooser.getSelectedFile().toString();
			String formatStr = getFileFormat(filePath);

			if (!formatStr.equals("keystore") && !formatStr.equals("keystore")) {
				JOptionPane.showMessageDialog(null, "文件格式错误", "警告", JOptionPane.WARNING_MESSAGE);
				return "";
			}

			try {
				textField_1.setText(filePath);
			} catch (Exception e) {
				ALog.error(e.getMessage(), e);
			}

		} else if (result == JFileChooser.CANCEL_OPTION) {
			ALog.debug("取消文件选择");
		} else if (result == JFileChooser.ERROR_OPTION) {
			ALog.debug("选择文件出错");
			JOptionPane.showMessageDialog(null, "文件格式错误", "警告", JOptionPane.WARNING_MESSAGE);
		}

		return filePath;
	}

	public static String getFileFormat(String fileName) {
		if (fileName == null || fileName.length() <= 0)
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	public String getKeyStoreFilePath() {
		return textField_1 != null ? textField_1.getText() : "";
	}

	public String getPassword() {
		return textField_2 != null ? textField_2.getText() : "";
	}

	public String getAlias() {
		return textField_3 != null ? textField_3.getText() : "";
	}

	public int getSignType() {
		return chckbxV2.isSelected() ? 2 : 1;
	}
	
	public boolean getSignState() {
		return !chckbxV1.isSelected() && !chckbxV2.isSelected();
	}
	
	public boolean getZipAlignState() {
		return chckbxV3.isSelected();
	}

	private boolean isEmpty(String string) {
		return AUtils.isEmpty(string);
	}

	public boolean keyStoreInfoIsEmpty() {
		return isEmpty(textField_1.getText()) || isEmpty(textField_2.getText()) || isEmpty(textField_3.getText());
	}
}
