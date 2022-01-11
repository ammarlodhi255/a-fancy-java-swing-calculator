import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class GUICalc extends JFrame {
    private JLabel label;
    private JTextField operandField;
    private JTextField resultField;
    private JPanel panel, panel1, panel2;
    private JButton b1 = new JButton("1"), b2 = new JButton("2"), b3 = new JButton("3"),
    b4 = new JButton("4"), b5 = new JButton("5"), b6 = new JButton("6"), b7 = new JButton("7"),
    b8 = new JButton("8"), b9 = new JButton("9"), b0 = new JButton("0"), plus = new JButton("+"),
    minus = new JButton("-"), mult = new JButton("x"), div = new JButton("/"), clear = new JButton("C"),
    decimalButton = new JButton("."), equal = new JButton("="), pi = new JButton("π"), sq = new JButton("n²"),
    reset = new JButton("Reset");
    private JButton[] buttonArray = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, plus, minus,
            mult, div, clear, decimalButton, equal, pi, reset, sq};
    private double result;
    private String append = new String(), previous = new String();
    private byte buttonCode = -1;

    public GUICalc() {
        label = new JLabel("SWING CALCULATOR ", SwingConstants.CENTER);
        operandField = new JTextField();
        resultField = new JTextField();
        panel = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        ListenToButtonEvent listen = new ListenToButtonEvent();

        setLayout(new BoxLayout (getContentPane(), BoxLayout.Y_AXIS));
        panel.setLayout(new GridLayout(5, 4, 3, 3));
        panel1.setLayout(null);
        panel2.setLayout(null);

        panel.setBackground(new Color(0, 0, 0));
        panel1.setBackground(new Color(0, 140, 180));
        panel1.setBorder(BorderFactory.createTitledBorder("RESULT"));
        panel2.setBorder(BorderFactory.createLineBorder(Color.black));

        label.setFont(new Font("Shrikhand", Font.BOLD + Font.ITALIC, 30));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(label);

        resultField.setFont(new Font("Aerial", Font.BOLD, 25));
        resultField.setBounds(5, 18, 475, 80);
        resultField.setFocusable(false);
        resultField.setEditable(false);
        panel1.add(resultField);

        operandField.setFont(new Font("Aerial", Font.BOLD, 50));
        operandField.setBounds(4, 3, 480, 100);
        operandField.setHorizontalAlignment(JTextField.RIGHT);
        operandField.setFocusable(false);
        panel2.add(operandField);

        for(int i = 0; i < buttonArray.length; i++) {
            if(i < 10)
                buttonArray[i].setBackground(new Color(231, 231, 231));
            else
                buttonArray[i].setBackground(new Color(0, 140, 186));

            buttonArray[i].setFont(new Font("Garamond", Font.BOLD, 25));
            buttonArray[i].setFocusable(false);
            buttonArray[i].addActionListener(listen);
        }

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(clear);
        panel.add(b4);
        panel.add(b5);
        panel.add(b6);
        panel.add(plus);
        panel.add(b7);
        panel.add(b8);
        panel.add(b9);
        panel.add(minus);
        panel.add(decimalButton);
        panel.add(b0);
        panel.add(div);
        panel.add(mult);
        panel.add(reset);
        panel.add(pi);
        panel.add(sq);
        panel.add(equal);

        add(panel1);
        add(panel2);
        add(panel);
        setTitle("Swing Calculator");
        setSize(500, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class ListenToButtonEvent implements ActionListener {
        boolean init = true, perform = false;
        public void actionPerformed(ActionEvent evt) {
            boolean preview = false;
            JButton buttonPressed = (JButton) evt.getSource();
            try {
                if(buttonPressed == plus) {
                    performCalc();
                    buttonCode = 1;
                } else if(buttonPressed == minus) {
                    performCalc();
                    buttonCode = 2;
                } else if(buttonPressed == mult) {
                    performCalc();
                    buttonCode = 3;
                } else if(buttonPressed == div) {
                    performCalc();
                    buttonCode = 4;
                } else if(buttonPressed == clear) {
                    operandField.setText(new String());
                } else if(buttonPressed == equal) {
                    performCalc();
                    resultField.setText(previous + append + " =");
                    previous = new String();
                    append = result == Math.floor(result) ? String.valueOf((int)result) : String.valueOf(result);
                    buttonCode = 0;
                } else if(buttonPressed == reset) {
                    append = new String();
                    previous = new String();
                    resultField.setText("0.0");
                    init = true;
                } else if(buttonPressed == pi) {
                    operandField.setText("3.14");
                    preview = true;
                } else if(buttonPressed == sq) {
                    if(operandField.getText().length() != 0) {
                        double num = Double.valueOf(operandField.getText());
                        if(num == Math.floor(num)) 
                            resultField.setText((int) num + "² = " + ((int) (num*num)));
                        else    
                            resultField.setText(num + "² = " + (num*num));
                    }
                } else {
                    if(!perform)
                        operandField.setText(operandField.getText() + evt.getActionCommand());
                    else
                        operandField.setText(evt.getActionCommand());
                    perform = false;
                    preview = true;
                }

                if(preview)
                    append += evt.getActionCommand();
                else if(buttonPressed != sq && buttonPressed != equal && buttonPressed != reset
                        && buttonPressed != clear && buttonPressed != pi) {
                    previous += append + " " + evt.getActionCommand() + " ";
                    resultField.setText(previous);
                    append = new String();
                    init = false;
                }
            } catch(DivisionByZeroException divEx) {
                operandField.setText(divEx.toString());
                append = new String();
            }
        }

        public void performCalc() throws DivisionByZeroException {
            perform = true;
            if(operandField.getText().length() > 0) {
                double currentNum = Double.valueOf(operandField.getText());
                if(init)
                    result = currentNum;
                else {
                    if(buttonCode == 1) result += currentNum;
                    else if(buttonCode == 2) result -= currentNum;
                    else if(buttonCode == 3) result *= currentNum;
                    else if(buttonCode == 4) {
                        if(currentNum >= -1.0e-10 && currentNum <= 1.0e-10)
                            throw new DivisionByZeroException("Division by zero!");
                        else
                            result /= currentNum;
                    }
                }
                operandField.setText(result == Math.floor(result) ? String.valueOf((int) result) : String.format("%.5g%n", result));
            }
        }
    }

    private class DivisionByZeroException extends Exception {
        String description;

        public DivisionByZeroException(String description) {
            this.description = description;
        }

        public String toString() { return description; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUICalc().setVisible(true);
            }
        });
    }
}