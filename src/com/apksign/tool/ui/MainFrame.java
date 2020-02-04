package com.apksign.tool.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.apksign.tool.AConstant;
import com.apksign.tool.sign.Sign;
import com.apksign.tool.sign.SignV1;
import com.apksign.tool.sign.SignV2;
import com.apksign.tool.utils.ALog;
import com.apksign.tool.utils.AUtils;
import com.apksign.tool.utils.ZipUtils;

/**
 * Created by chenzhi on 2019年12月19日
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private ApkPathJPanal apkPathJPanal;
	private KeyStoreJPanel keyStoreJPanel;
	private SignLogJPanel signLogJPanel;
	private JButton btnSign;
	private String tmpApkPath = "";

	public MainFrame() {
		initFrameView();
	}

	private void initFrameView() {
		setTitle("安卓APK二次签名工具" + "_" + AConstant.VERSION);
		setIconImage(Toolkit.getDefaultToolkit().getImage("res" + File.separator + "icon_p.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 511, 583);

		Toolkit tk = this.getToolkit(); // 得到窗口工具条
		Dimension dm = tk.getScreenSize();
		setLocation((int) (dm.getWidth() - 511) / 2, (int) (dm.getHeight() - 583) / 2);// 显示在屏幕中央
		setResizable(false);
		getContentPane().setLayout(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		apkPathJPanal = new ApkPathJPanal();
		contentPane.add(apkPathJPanal.getApkPathContentComponent());

		keyStoreJPanel = new KeyStoreJPanel();
		contentPane.add(keyStoreJPanel.getContentJComponent());

		signLogJPanel = new SignLogJPanel();
		contentPane.add(signLogJPanel.getContentComponent());

		btnSign = new JButton("开始签名");
		btnSign.setFont(new Font("宋体", Font.PLAIN, 14));
		btnSign.setBounds(196, 507, 98, 37);
		getContentPane().add(btnSign);

		btnSign.addActionListener(signActionListener);
	}

	// 签名按钮操作监听实例
	private ActionListener signActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent actionevent) {
			//
			String apkFilePath = apkPathJPanal.getApkPath();

			if (AUtils.isEmpty(apkFilePath)) {
				showTipDialog("APK路径不能为空！", "警告");
				return;
			}

			if (!new File(apkFilePath).exists()) {
				showTipDialog("APK文件不存在！\n" + apkFilePath, "警告");
				return;
			}

			if (keyStoreJPanel.keyStoreInfoIsEmpty()) {
				showTipDialog("签名文件信息不能为空！", "警告");
				return;
			}

			if (!new File(keyStoreJPanel.getKeyStoreFilePath()).exists()) {
				showTipDialog("签名文件不存在！\n" + keyStoreJPanel.getKeyStoreFilePath(), "警告");
				return;
			}

			apkSignHandler(apkFilePath);
		}
	};

	private ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "execCmd");
		}
	});

	private void apkSignHandler(final String filePath) {
		service.submit(new Runnable() {

			@Override
			public void run() {
				apkOption(filePath);
			}
		});
	}

	private void apkOption(String filePath) {
		try {
			if (keyStoreJPanel.getSignState()) {
				showTipDialog("请先选择对应的签名类型！", "警告");
				return;
			}

			// 打印初始化信息
			printConsoleLog("\n===============================\n");
			// 设置签名操作按钮不可点击
			btnSign.setEnabled(false);

			String tmpApkName = AUtils.getsignApkFileName(filePath) + "_st.apk";
			tmpApkPath = AUtils.getSignApkPath(filePath) + tmpApkName;

			// 先复制apk到临时目录
			ZipUtils.copyFileUsingJava7(filePath, tmpApkPath);
			// 删除临时apk文件中的META-INF文件夹
			boolean deleteState = ZipUtils.removeDirFromZipArchive(tmpApkPath, "META-INF");

			if (!deleteState) {
				showTipDialog("删除APK原有签名文件失败！", "警告");
				// 回复按钮可点击
				resetBtnEnable();
				return;
			}

			if (!new File(tmpApkPath).exists()) {
				showTipDialog("复制临时APK文件失败！", "警告");
				// 回复按钮可点击
				resetBtnEnable();
				return;
			}

			String keyStoreName = keyStoreJPanel.getKeyStoreFilePath();
			String keyStorePwd = keyStoreJPanel.getPassword();
			String keyStoreAlais = keyStoreJPanel.getAlias();

			boolean isZipAlign = keyStoreJPanel.getZipAlignState();
			if (keyStoreJPanel.getSignType() == 1) {
				//
				signV1(keyStoreName, keyStoreAlais, keyStorePwd, isZipAlign, tmpApkPath);
			} else {
				//
				signV2(keyStoreName, keyStoreAlais, keyStorePwd, isZipAlign, tmpApkPath);
			}
		} catch (Exception e) {
			ALog.error(e.getMessage(), e);
		}
	}

	private Sign.SignListener signListener = new Sign.SignListener() {
		
		@Override
		public void printSignLog(String line) {
			signLogJPanel.printSignLog(line);
		}

		@Override
		public void onSuccess() {
			onSignFinish(tmpApkPath);
			// 完成提示
			showOkDialog("签名成功！", "提示");
		}

		@Override
		public void onFialed(String msg) {
			onSignFinish(tmpApkPath);
			showTipDialog(msg, "警告");				
		}
	};

	private void signV1(String keyStoreName, String keyStoreAlais, String keyStorePwd, boolean isZipAlign,
			String unSignApkPath) {
		SignV1 signV1 = new SignV1(keyStoreName, keyStoreAlais, keyStorePwd);
		signV1.setListener(signListener);
		signV1.execute(isZipAlign, unSignApkPath);
	}

	private void signV2(String keyStoreName, String keyStoreAlais, String keyStorePwd, boolean isZipAlign,
			String unSignApkPath) {
		SignV2 signV2 = new SignV2(keyStoreName, keyStoreAlais, keyStorePwd);
		signV2.setListener(signListener);
		signV2.execute(isZipAlign, unSignApkPath);
	}
	
	private void onSignFinish(String tmpApkPath) {
		// 删除临时文件
		AUtils.deleteFile(new File(tmpApkPath));
		// 回复按钮可点击
		resetBtnEnable();
	}

	private void resetBtnEnable() {
		btnSign.setEnabled(true);
	}

	private void printConsoleLog(String msg) {
		signLogJPanel.printSignLog(msg);
	}

	private void showTipDialog(String msg, String title) {
		printConsoleLog(msg);
		AUtils.showTipDialog(msg, title);
	}
	
	private void showOkDialog(String msg, String title) {
		AUtils.showOkDialog(msg, title);
	}
}
