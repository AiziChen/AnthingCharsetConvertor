package quanye.charsetconverter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

import tools.charset.CharsetDetector;

/**
 * 编码转换工具
 * @author QuanyeChen
 * License on Apache 2.0
 *
 */
public class Converter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] TEXT_TYPES = {
		".txt",
		".xml",
		".java"
	};
	
	private JPanel panel = new JPanel();
	private JFileChooser chooser = new JFileChooser();

	public static void main(String[] args) throws IOException {
		new Converter().initWindow();
	}

	/**
	 * 初始化窗口
	 */
	private void initWindow() {
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogTitle("第一步：请指定一个需要批量修改编码的文件夹");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		panel.add(chooser);

		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			add(showCharsetSelectWindow(file));

			setTitle("第二步：请选择目标编码");
			setVisible(true);
			setBounds(500, 500, 420, 280);
			Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
			Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
			int screenWidth = screenSize.width / 2; // 获取屏幕的宽
			int screenHeight = screenSize.height / 2; // 获取屏幕的高
			int height = this.getHeight();
			int width = this.getWidth();
			setBounds(screenWidth - width / 2, screenHeight - height / 2 - 100, 380, 260);
		} else {
			System.exit(0);
		}

	}

	/**
	 * “选择编码”窗口
	 * 
	 * @return
	 */
	private JPanel showCharsetSelectWindow(File filePath) {
		JPanel panel = new JPanel();

		// Map<String, Charset> map = Charset.availableCharsets();
		// String[] items = new String[map.size()];
		// items = map.keySet().toArray(items);
		String[] items = { "UTF-8", "GBK", };

		JComboBox<String> comboBox = new JComboBox<>(items);

		JLabel processing = new JLabel("");

		JButton okBtn = new JButton("确定");
		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				processing.setText("正在转换...");
				try {
					String item = (String) comboBox.getSelectedItem();
					convert(filePath, item);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				processing.setText("转换完成");
			}
		});

		panel.add(new JLabel("请选择需要转换的编码"));
		panel.add(comboBox);
		panel.add(okBtn);
		panel.add(processing);
		panel.setVisible(true);
		return panel;
	}

	/**
	 * 实现编码的转换
	 * 
	 * @param charSet
	 * @throws IOException
	 */
	private void convert(File filePath, String charSet) throws IOException {
		File[] files = filePath.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (isAllEndWith(TEXT_TYPES, fileName)) {
					// 转换
					String coding = CharsetDetector.getInstance().getCharSet(file).name();
					// 相同编码不用转
					if (!coding.equals(charSet)) {
						FileUtils.writeLines(file, charSet, FileUtils.readLines(file, coding));
					}
				}

			} else {
				convert(file, charSet);
			}
		}
	}

	/**
	 * fileName是否以textTypes中的文本为结尾
	 * @param textTypes
	 * @param fileName
	 * @return
	 */
	private boolean isAllEndWith(String[] textTypes, String fileName) {
		for (String type : textTypes) {
			if (fileName.endsWith(type)) {
				return true;
			}
		}
		return false;
	}

}
