import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

class ValidationTestFrame extends JFrame implements DocumentListener {
    private IntTextField hourField;
    private IntTextField minuteField;
    private ClockPanel clock;

    public ValidationTestFrame() {
        setTitle("ValidationTest");
        setSize(500, 500);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();

        JPanel p = new JPanel();
        hourField = new IntTextField(12, 3);
        p.add(hourField);
        hourField.getDocument().addDocumentListener(this);

        minuteField = new IntTextField(0, 3);
        p.add(minuteField);
        minuteField.getDocument().addDocumentListener(this);

        contentPane.add(p, "South");
        clock = new ClockPanel();
        contentPane.add(clock, "Center");
    }

    public void insertUpdate(DocumentEvent e) {
        setClock();
    }

    public void removeUpdate(DocumentEvent e) {
        setClock();
    }

    public void changedUpdate(DocumentEvent e) {
        setClock();
    }

    public void setClock() {
        if (hourField.checkValid() && minuteField.checkValid()) {
            int hours = hourField.getValue();
            int minutes = minuteField.getValue();
            clock.setTime(hours, minutes);
        }
    }
}

class IntTextDocument extends PlainDocument {
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null)
            return;
        String oldString = getText(0, getLength());
        String newString = oldString.substring(0, offs);
        try {
            Integer.parseInt(newString + "0");
            super.insertString(offs, str, a);
        } catch (NumberFormatException e) {

        }
    }
}

class IntTextField extends JTextField {
    public IntTextField(int defval, int size) {
        super("" + defval, size);
    }

    protected Document createDefaultModel() {
        return new IntTextDocument();
    }

    public boolean checkValid() {
        try {
            Integer.parseInt(getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getValue() {
        try {
            return Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

class ClockPanel extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(0, 0, 100, 100);
        double hourAngle = 2 * Math.PI * (minutes - 3 * 60) / (12 * 60);
        double minuteAngle = 2 * Math.PI * (minutes - 15) / 60;
        g.drawLine(50, 50,
                50 + (int) (30 * Math.cos(hourAngle)),
                50 + (int) (30 * Math.sin(hourAngle)));
        g.drawLine(50, 50,
                50 + (int) (45 * Math.cos(minuteAngle)),
                50 + (int) (45 * Math.sin(minuteAngle)));
    }

    public void setTime(int h, int m) {
        minutes = h * 60 + m;
        repaint();
    }

    public void tick() {
        minutes++;
        repaint();
    }

    private int minutes = 0;
}

public class Main {
    public static void main(String[] args) {
        JFrame frame = new ValidationTestFrame();
        frame.setVisible(true);
    }
}