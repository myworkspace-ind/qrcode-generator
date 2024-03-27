/**
 * 
 */
package org.sakaiproject.tool.qrcode.common;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.zxing.WriterException;

/**
 * 
 */
public class QRCodeTestLogoLink {

	/**
	 * Test method for {@link org.sakaiproject.tool.qrcode.common.QRCode#generateWithLogo(java.lang.String, java.io.OutputStream)}.
	 */
	@Test
	public void testGenerateWithLogo_001() {
		String logoPath = "D:/Temp/ThachLN.png";
		QRCode qrCode = new QRCode(logoPath , 500, 500);
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("QR_Code_Image-TTND.png");
			qrCode.generateWithLogo("https://facebook.com/groups/ttdoanhnghiep", fos);
			
			// Check
			File f = new File("QR_Code_Image-TTND.png");
			
			Assert.assertTrue(f.isFile());
			Assert.assertEquals(true, f.isFile());
		} catch (IOException | WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	}

}
