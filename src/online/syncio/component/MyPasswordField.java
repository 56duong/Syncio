package online.syncio.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import online.syncio.model.MyFont;

public class MyPasswordField extends JPasswordField {
    private int radius = 5;
    private Color borderColor = new Color(219, 219, 219);
    private int borderThickness = 1;
    private Color disabledBackgroundColor = getBackground();

    
    
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
    

    
    public MyPasswordField() {
        setOpaque(false);
        setFont(new MyFont().SFProDisplayRegular);
        setFont(getFont().deriveFont(Font.PLAIN, 14f));
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
}

