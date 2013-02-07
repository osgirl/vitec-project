package fr.vitec.fmk.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import fr.vitec.fmk.exception.VitecException;
import fr.vitec.fmk.nls.Messages;


public class ImageUtil {

	public static String getEncodedStringFromImageUrl(String urlStr) {
		String str;
		byte[] buffer = readImageUrl(urlStr);
		byte[] encode = Base64.encodeBase64(buffer);
		str = new String(encode);
		return str;
	}

	private static byte[] readImageUrl(String urlStr) {
		URL url;
		byte[] imageInByte = null;

		String extention = getExtention(urlStr);
		if(extention==null){
			throw new VitecException(Messages.ImageUtil_0+urlStr);
		}

		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			throw new VitecException(e);
		}
		try {
			BufferedImage image = ImageIO.read(url);

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, extention, baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();

		} catch (IOException e) {
			throw new VitecException(e);
		}

		return imageInByte;
	}

	private static String getExtention(String urlStr) {
		String extention = null;
		int index = urlStr.lastIndexOf("."); //$NON-NLS-1$
		if(index != -1){
			extention = urlStr.substring(index+1);
		}
		return extention;
	}

	//	private static byte[] readFile(String filename) throws IOException {
	//		java.io.File file = new java.io.File(filename);
	//		java.io.BufferedInputStream bis = new java.io.BufferedInputStream(new
	//				java.io.FileInputStream(file));
	//		int bytes = (int) file.length();
	//		byte[] buffer = new byte[bytes];
	//		bis.read(buffer);
	//		bis.close();
	//		return buffer;
	//	}


	public static BufferedImage getBufferedImageFromEncodedString(String encodedStr){
		byte[] imageInByte = Base64.decodeBase64(encodedStr.getBytes());

		// convert byte array back to BufferedImage
		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage bImageFromConvert;
		try {
			bImageFromConvert = ImageIO.read(in);
		} catch (IOException e) {
			throw new VitecException(e);
		}
		return bImageFromConvert;
	}
	
	public static Image getSwtImageFromEncodedString(String encodedStr){
		byte[] imageInByte = Base64.decodeBase64(encodedStr.getBytes());

		// convert byte array back to BufferedImage
		InputStream inputStream = new ByteArrayInputStream(imageInByte);
		
		return new Image(Display.getCurrent(), inputStream);
		
	}

//	private static void writeFile(byte[] tab,String fichier) throws IOException {
//		File file = new File(fichier);
//		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//		bos.write(tab);
//		bos.close();
//	}

}
