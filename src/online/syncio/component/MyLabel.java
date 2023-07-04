package online.syncio.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import online.syncio.model.MyFont;

public class MyLabel extends JLabel {
    private int radius = 0;
    private Color borderColor = Color.WHITE;
    private int borderThickness = 0;

    
    
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
    

    
    public MyLabel() {
        setOpaque(false);
        setFont(new MyFont().SFProDisplayRegular);
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public MyLabel(String text) {
        setText(text);
        setOpaque(false);
        setFont(new MyFont().SFProDisplayRegular);
        setBackground(null);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    
    
    @Override
    public void paintComponent(Graphics g) {
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
        
        //Draw image if the label contains an icon
        if (getIcon() != null && getIcon() instanceof ImageIcon) {
            //smooth icon
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(rh);
        }

        super.paintComponent(g);
    }

}

