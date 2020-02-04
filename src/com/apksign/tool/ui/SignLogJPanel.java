package com.apksign.tool.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by chenzhi on 2019年12月20日
 */
public class SignLogJPanel {
	
	private JTextArea textArea;
	
	public JComponent getContentComponent() {
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "操作日志", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK));
		panel_2.setBounds(10, 296, 483, 200);
		panel_2.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		textArea.setLineWrap(true); // 设置文本域中的文本为自动换行
		textArea.setForeground(Color.GREEN); // 设置组件的背景色
		textArea.setFont(new Font("宋体", Font.PLAIN, 14)); // 修改字体样式
		textArea.setAutoscrolls(true);
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent documentevent) {
			}

			@Override
			public void insertUpdate(DocumentEvent documentevent) {
				// 自动滑动到光标处
				textArea.setCaretPosition(textArea.getText().length());
			}

			@Override
			public void changedUpdate(DocumentEvent documentevent) {
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane(textArea);
		scrollPane_1.setBounds(10, 21, 463, 170);
		
		panel_2.add(scrollPane_1);
		
		return panel_2;
	}
	
    private ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory() {
        
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "output");
        }
    });
    
    private void updateLog(final String content) {
        service.submit(new Runnable() {
            
            @Override
            public void run() {
                textArea.append(content);
            }
        });
    }
	
	public void printSignLog(String log) {
		updateLog(log + "\n");
	}
}
