package org.sakaiproject.tool.qrcode.common;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility of QR Code.
 * <br/>
 * Generate QR Code by text and image.
 */
@Slf4j
public class QRCode {
    /** Directory path or URL of logo image. **/
    private String logoPath = null;
    
    /** File logo image. This is skip if logoPath not empty.**/
    private File logo;

    private int width = 300;
    private int height = 300;
    
    public QRCode(String logoPath, int width, int height) {
    	this.logoPath = logoPath;
    	this.width = width;
    	this.height = height;
    }

    public QRCode(File logo, int width, int height) {
    	this.logo = logo;
    	this.width = width;
    	this.height = height;
    }
	/**
	 * Create QR code basing logoPath and text.
	 * @see https://simplifyingtechcode.wordpress.com/2021/05/23/generating-qr-code-in-java-within-5minutes/
	 * @param text content of the QR Code
	 * @param os Output of QR Image
	 * @throws WriterException Error when write result to output stream.
	 * @throws IOException Error when read logoPath.
	 */
	public void generateWithLogo(String text, OutputStream os) throws WriterException, IOException {
		 // Create new configuration that specifies the error correction
       Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
       hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

       QRCodeWriter writer = new QRCodeWriter();
       BitMatrix bitMatrix = null;

       try {
           // init directory
//           cleanDirectory(DIR);
//           initDirectory(DIR);
           // Create a qr code with the url as content and a size of WxH px
           bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

           // Load QR image
           BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

           // Load logo image
//           BufferedImage overly = getOverly(logoPath);
           BufferedImage overly = (logoPath != null) ? ImageIO.read(new File(logoPath)) : ImageIO.read(logo);

           // Calculate the delta height and width between QR code and logo
           int deltaHeight = qrImage.getHeight() - overly.getHeight();
           int deltaWidth = qrImage.getWidth() - overly.getWidth();

           // Initialize combined image
           BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
           Graphics2D g = (Graphics2D) combined.getGraphics();

           // Write QR code to new image at position 0/0
           g.drawImage(qrImage, 0, 0, null);
           g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

           // Write logo into combine image at position (deltaWidth / 2) and
           // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
           // the same space for the logo to be centered
           g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

           // Write combined image as PNG to OutputStream
           ImageIO.write(combined, "png", os);
           // Store Image
           // Files.copy( new ByteArrayInputStream(os.toByteArray()), Paths.get(DIR + generateRandoTitle(new Random(), 9) +ext), StandardCopyOption.REPLACE_EXISTING);

       } catch (WriterException wEx) {
    	   log.error("Could not write to output.", wEx);
           throw wEx;
       } catch (IOException ioEx) {
    	   log.error(String.format("Could not logoPath at %s", logoPath), ioEx);
           throw ioEx;
       }
	}

    private MatrixToImageConfig getMatrixConfig() {
        // ARGB Colors
        // Check Colors ENUM
        return new MatrixToImageConfig(Colors.WHITE.getArgb(), Colors.ORANGE.getArgb());
    }

    public enum Colors {

        BLUE(0xFF40BAD0),
        RED(0xFFE91C43),
        PURPLE(0xFF8A4F9E),
        ORANGE(0xFFF4B13D),
        WHITE(0xFFFFFFFF),
        BLACK(0xFF000000);

        private final int argb;

        Colors(final int argb){
            this.argb = argb;
        }

        public int getArgb(){
            return argb;
        }
    }
}
