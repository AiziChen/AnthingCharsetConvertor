package tools.charset;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
/**
 * 文件编码获取工具
 * @author QuanyeChen
 * License on Apache 2.0
 *
 */
public class CharsetDetector {

	private static CharsetDetector instance;

	private CharsetDetector() {
	}

	public static CharsetDetector getInstance() {
		if (instance == null) {
			instance = new CharsetDetector();
		}
		return instance;
	}

	/**
	 * 根据文件获取其文件编码
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public Charset getCharSet(File file) throws IOException {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ByteOrderMarkDetector());
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		return detector.detectCodepage(file.toURL());
	}

}
