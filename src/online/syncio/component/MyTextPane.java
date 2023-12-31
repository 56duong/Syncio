package online.syncio.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import online.syncio.utils.TextHelper;

public class MyTextPane extends JTextPane {
    private static final Map<String, Color> emojiColorMap = new HashMap<>();
    static {
        // Add emojis and their corresponding colors to the map
        emojiColorMap.put("👌", new Color(255, 204, 0));
        emojiColorMap.put("✨", new Color(255, 204, 0));
        emojiColorMap.put("👍", new Color(255, 204, 0));
        emojiColorMap.put("❤", new Color(255, 0, 0));
        emojiColorMap.put("📸", new Color(102, 102, 102));
        emojiColorMap.put("😂", Color.BLACK);
        emojiColorMap.put("😁", Color.BLACK);
        // Add more emojis and colors as needed
    }
    
    private int radius = 0;
    private Color borderColor = new Color(219, 219, 219);
    private int borderThickness = 1;
    private Color disabledBackgroundColor = getBackground();
    private String placeholder;

    
    
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }
    
    public void setBorderThicknessColor(int borderThickness, Color borderColor) {
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        TextHelper.addPlaceholderText(this, placeholder);
    }
    

    
    public MyTextPane() {
        setOpaque(false);
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
        setBorder(new EmptyBorder(1, 5, 1, 5));
    }

    
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Paint Border
        if(borderThickness > 0) {
            g2.setColor(borderColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
        
        //Paint inside, Border set thickness pix
        g2.setColor(getBackground());
        g2.fillRoundRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2, radius, radius);
        
        super.paintComponent(g);
    }
    
    

    @Override
    public Color getBackground() {
        if (isEnabled()) {
            return super.getBackground();
        } else {
            return disabledBackgroundColor;
        }
    }

    public Color getDisabledBackgroundColor() {
        return disabledBackgroundColor;
    }

    public void setDisabledBackgroundColor(Color disabledBackgroundColor) {
        this.disabledBackgroundColor = disabledBackgroundColor;
    }
    
    
    
    public void append(String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "SansSerif");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = getDocument().getLength();
        setCaretPosition(len);
        setCharacterAttributes(aset, false);
        replaceSelection(msg);
    }
    
    
    
    public void appendIcon(Icon icon) {
        insertIcon(icon);
    }
   
        
        
    public void appendBoldText(String text) {
        SimpleAttributeSet boldStyle = new SimpleAttributeSet();
        StyleConstants.setBold(boldStyle, true);

        SimpleAttributeSet regularStyle = new SimpleAttributeSet();
        StyleConstants.setBold(regularStyle, false);

        Document doc = getDocument();
        try {
            doc.insertString(doc.getLength(), text, boldStyle);
            doc.insertString(doc.getLength(), " ", regularStyle); // Set the style to regular
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void appendTextWithEmojis(String text) {
        char[] characters = text.toCharArray();

        for (int i = 0; i < characters.length; ) {
            int codePoint = Character.codePointAt(characters, i);
            int charCount = Character.charCount(codePoint);
            String token = new String(characters, i, charCount);

            Color emojiColor = emojiColorMap.getOrDefault(token, Color.BLACK);
            append(token, emojiColor);

            i += charCount; // Move the index to the next code point
        }
    }
    
    
    
    public void appendColoredText(String text, Color color, int fontSize) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "SansSerif");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, fontSize);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);

        int len = getDocument().getLength();
        setCaretPosition(len);
        setCharacterAttributes(aset, false);
        replaceSelection(text);
        
        // Reset font size to default
        AttributeSet defaultStyle = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.FontSize, 14); // Change 14 to your default font size
        setCharacterAttributes(defaultStyle, false);
    }
    
}

