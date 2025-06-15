package com.course.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

public class CommonUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getCaptchaBase64String(String captcha) {

        String result = null;
        int offset = 0;
        if (captcha.length() > 4) {
            offset += 22 * (captcha.length() - 4);
        }
        int width = 86 + offset;
        int height = 25;
        SecureRandom random = new SecureRandom();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        Font f = new Font("黑體", Font.BOLD, 20);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setFont(f);
        g.setColor(Color.CYAN);

        for (int i = 0; i < 50; i++) {
            int x1 = random.nextInt(width - 1);
            int y1 = random.nextInt(height - 1);
            int x2 = random.nextInt(6) + 1;
            int y2 = random.nextInt(12) + 1;

            BasicStroke bsk = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            Line2D line = new Line2D.Double(x1, y1, x1 + x2, y1 + y2);
            g2d.setStroke(bsk);
            g2d.draw(line);
        }

        g.setColor(Color.BLUE);
        Graphics2D g2d2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.scale(1.1, 1.3);
        at.rotate(random.nextInt(10) * 3.14 / 180, 28, 14);
        g2d2.setTransform(at);
        g.drawString(captcha, 18, 14);
        g.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            String base64Img = Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
            result = "data:image/jpg;base64," + base64Img;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
            }
        }

        return result;
    }
}
