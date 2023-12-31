package online.syncio.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.UserDAO;
import online.syncio.model.User;
import org.bson.types.Binary;

/**
 * The ImageHelper class provides utility methods for handling and manipulating
 * images.
 */
public class ImageHelper {

    private static UserDAO userDAO = MongoDBConnect.getUserDAO();
    private static final Image defaultImage = new javax.swing.ImageIcon(ImageHelper.class.getResource("/online/syncio/resources/images/icons/avt_128px.png")).getImage();

    /**
     * Converts an Icon to a BufferedImage.
     *
     * @param icon the Icon to be converted
     * @return the converted BufferedImage
     */
    public static BufferedImage iconToBufferedImage(Icon icon) {
        BufferedImage bi = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        // paint the Icon to the BufferedImage.
        icon.paintIcon(null, g, 0, 0);
        g.setColor(Color.WHITE);
        g.dispose();

        return bi;
    }

    /**
     * Converts an Image to a BufferedImage.
     *
     * @param image the Image to be converted
     * @return the converted BufferedImage
     */
    public static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bimage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    /**
    * Converts an image file specified by the given file path to a BufferedImage.
    *
    * @param imagePath The path to the image file to be converted.
    * @return A BufferedImage representation of the image file.
    */
    public static BufferedImage stringToBufferedImage(String imagePath) {
        File file = new File(imagePath);
        BufferedImage bimage = null;

        try {
            bimage = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return bimage;
    }

    /**
     * Converts a BufferedImage to Binary data.
     *
     * @param image the BufferedImage to be converted
     * @return the Binary data representation of the image
     */
    public static Binary bufferedImageToBinary(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] byteArray = baos.toByteArray();
        return new Binary(byteArray);
    }

    /**
     * Resizes an image from a file path to the specified dimensions.
     *
     * @param imagePath the path of the image file
     * @param width the desired width of the resized image
     * @param height the desired height of the resized image
     * @return the resized ImageIcon
     */
    public static ImageIcon resizing(String imagePath, int width, int height) {
        Image image = new ImageIcon(imagePath).getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    /**
     * Resizes an image to the specified dimensions.
     *
     * @param img the Image to be resized
     * @param width the desired width of the resized image
     * @param height the desired height of the resized image
     * @return the resized ImageIcon
     */
    public static ImageIcon resizing(Image img, int width, int height) {
        Image image = new ImageIcon(img).getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    /**
     * Resizes a BufferedImage to the specified dimensions.
     *
     * @param bufferedImage the BufferedImage to be resized
     * @param width the desired width of the resized image
     * @param height the desired height of the resized image
     * @return the resized ImageIcon
     */
    public static ImageIcon resizing(BufferedImage bufferedImage, int width, int height) {
        Image image = new ImageIcon(bufferedImage).getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    /**
     * Resizes a BufferedImage to the specified width while maintaining aspect ratio.
     *
     * @param bufferedImage the BufferedImage to be resized
     * @param width the desired width of the resized image
     * @return the resized Image
     */
    public static Image resizeImageToWidth(BufferedImage bufferedImage, int width) {
        return bufferedImage.getScaledInstance(width, -1, Image.SCALE_DEFAULT);
    }

    /**
 * Resizes a BufferedImage to the specified height while maintaining aspect ratio.
 *
 * @param bufferedImage the BufferedImage to be resized
 * @param height the desired height of the resized image
 * @return the resized Image
 */
    public static Image resizeImageToHeight(BufferedImage bufferedImage, int height) {
        return bufferedImage.getScaledInstance(-1, height, Image.SCALE_DEFAULT);
    }
    
    /**
 * Resizes a BufferedImage to fit within a square of the specified size.
 * Adds a circular mask to the image.
 *
 * @param bufferedImage the BufferedImage to be resized and masked
 * @param size the desired size of the square image
 * @return the masked BufferedImage
 */
    public static Image resizeImageToFit(BufferedImage bufferedImage, int size) {
        if (bufferedImage.getWidth() < bufferedImage.getHeight()) {
            bufferedImage = imageToBufferedImage(resizeImageToWidth(bufferedImage, size));
        } else {
            bufferedImage = imageToBufferedImage(resizeImageToHeight(bufferedImage, size));
        }
        
        BufferedImage mask = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.fillRect(0, 0, size - 1, size - 1);
        g2d.dispose();

        BufferedImage masked = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.drawImage(bufferedImage, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();
        
        return masked;
    }

    /**
 * Resizes and compresses a BufferedImage to the specified width and converts it to Binary data.
 *
 * @param bufferedImage the BufferedImage to be resized and compressed
 * @param width the desired width of the resized image
 * @param compressionQuality the quality of compression (0.0 to 1.0)
 * @return the compressed Binary data representation of the image
 */
    public static Binary resizingAndCompressingWidthToBinary(BufferedImage bufferedImage, int width, float compressionQuality) {
        try {
            //resize image
            bufferedImage = imageToBufferedImage(bufferedImage.getScaledInstance(width, -1, Image.SCALE_DEFAULT));

            BufferedImage compressedImage = new BufferedImage(
                    bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D g2d = compressedImage.createGraphics();
            g2d.drawImage(bufferedImage, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(compressionQuality);
            writer.write(null, new IIOImage(compressedImage, null, null), param);

            ios.close();
            writer.dispose();

            return ImageHelper.bufferedImageToBinary(compressedImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
 * Reads the contents of a file as a byte array.
 *
 * @param path the path of the file to read
 * @return the byte array containing the file's contents
 * @throws RuntimeException if an I/O error occurs
 */
    public static byte[] readAsByte(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            int n = fis.available(); //tra ve so byte uoc tinh co the doc (hoac bo qua)
            byte[] data = new byte[n];

            fis.read(data);
            fis.close();

            return data;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
    * Converts an Image to a byte array in PNG format.
    *
    * @param image the Image to convert
    * @return the byte array representing the Image in PNG format
    */
    public static byte[] readAsByte(Image image) {
        byte[] imageBytes = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage bufferedImage = imageToBufferedImage(image);

        try {
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            imageBytes = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
        }

        return imageBytes;
    }

    /**
    * Writes the provided byte array data to a file at the specified path.
    *
    * @param path The path where the file will be created.
    * @param data The byte array data to be written to the file.
    * @throws RuntimeException If an IOException occurs during the file write operation.
    */
    public static void write(String path, byte[] data) {
        try {
            FileOutputStream fos = new FileOutputStream(path);

            fos.write(data);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * Reads binary data from a Binary object and converts it into a BufferedImage.
    *
    * @param binary The Binary object containing the binary data.
    * @return A BufferedImage created from the binary data, or null if reading fails.
    */
    public static BufferedImage readBinaryAsBufferedImage(Binary binary) {
        byte[] byteArray = binary.getData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        try {
            return ImageIO.read(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
    * Converts a given BufferedImage to a round image with a specified size.
    * The image is masked with a circular shape.
    *
    * @param image The BufferedImage to be converted.
    * @param size The size of the resulting round image.
    * @return An ImageIcon containing the round image.
    */
    public static ImageIcon toRoundImage(BufferedImage image, int size) {
        if (image.getWidth() < image.getHeight()) {
            image = imageToBufferedImage(resizeImageToWidth(image, size));
        } else {
            image = imageToBufferedImage(resizeImageToHeight(image, size));
        }

        BufferedImage mask = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.fillOval(0, 0, size - 1, size - 1);
        g2d.dispose();

        BufferedImage masked = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.drawImage(image, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(masked);
    }

    /**
    * Sets the avatar image for a JLabel using the username and specified size.
    * The avatar image is retrieved from the user's data and resized to fit the given size.
    *
    * @param username The username of the user whose avatar is to be displayed.
    * @param label The JLabel where the avatar image will be set.
    * @param size The size of the avatar image.
    */
    public static void setAvatarToLabel(String username, JLabel label, int size) {
        User user = userDAO.getByUsername(username);
        setAvatarToLabel(user, label, size);
    }

    /**
    * Sets the avatar image for a JLabel using the User object and specified size.
    * The avatar image is retrieved from the user's data and resized to fit the given size.
    *
    * @param user The User object containing avatar information.
    * @param label The JLabel where the avatar image will be set.
    * @param size The size of the avatar image.
    */
    public static void setAvatarToLabel(User user, JLabel label, int size) {
        ImageIcon avatarImage;
        Binary avtByteArray = null;

        try {
            avtByteArray = user.getAvt();
        } catch (NullPointerException e) {
            // Handle the case when the user or avatar is not found
        }

        if (avtByteArray != null) {
            BufferedImage bufferedImage = readBinaryAsBufferedImage(avtByteArray);
            avatarImage = toRoundImage(bufferedImage, size);
        } else {
            avatarImage = resizing(defaultImage, size, size);
        }

        label.setIcon(avatarImage);
    }

    /**
     * Retrieves the default image.
     *
     * @return the default Image
     */
    public static Image getDefaultImage() {
        return defaultImage;
    }

}
