package org.sakaiproject.tool.qrcode.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Assert;
import org.junit.Test;


public class QRCodeTest {
	QRCode qrCode = new QRCode("D:\\Temp\\ThachLN.png", 400, 400);
	@Test
	public void testGenerateWithLogoOutputStream() {
		try {
			FileOutputStream fos = new FileOutputStream("Generated_QR_02.png");
			String text = "https://www.facebook.com/groups/ttdoanhnghiep";
			qrCode.generateWithLogo(text , fos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}
