import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Tic-Tac-Toe с графическим интерфейсом на Java Swing.
 */
public class SwingVersion extends JFrame {

    private JButton[][] buttons;
    private char[][] board;
    private String currentPlayer = "X";
    private String gameMode = "PvP"; // "PvP" или "PvCPU"
    private boolean gameActive = true;
    private int cpuLevel = 1; // 1 = случайный, 2 = минимакс

    public SwingVersion() {
        setTitle("Tic-Tac-Toe (Swing)");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Панель заголовка
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(400, 60));
        headerPanel.setBackground(new Color(52, 73, 94));

        JLabel titleLabel = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JButton newGameBtn = new JButton("Новая игра");
        newGameBtn.addActionListener(e -> startNewGame());

        JButton settingsBtn = new JButton("Настройки");
        settingsBtn.addActionListener(e -> openSettingsDialog());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameBtn);
        buttonPanel.add(settingsBtn);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Основная панель игры
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3, 5, 5));
        gamePanel.setBackground(new Color(248, 250, 255));

        // Создаём кнопки
        buttons = new JButton[3][3];
        board = new char[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(e -> handleMove(i, j));
                gamePanel.add(buttons[i][j]);
            }
        }

        // Индикатор текущего хода
        JLabel statusLabel = new JLabel("Ход: X");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gamePanel.add(statusLabel);

        // Кнопка сброса
        JButton resetBtn = new JButton("Сброс");
        resetBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        resetBtn.addActionListener(e -> startNewGame());
        gamePanel.add(resetBtn);

        add(gamePanel, BorderLayout.CENTER);

        // Панель подвала
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(400, 40));
        footerPanel.setOpaque(true);

        JLabel footerLabel = new JLabel("Tic-Tac-Toe — версия 1.0 | Swing GUI");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.LIGHT_GRAY);

        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void startNewGame() {
        gameActive = true;
        currentPlayer = "X";
        gameMode = "PvP";
        cpuLevel = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(null);
                board[i][j] = ' ';
            }
        }
        statusLabel.setText("Ход: X");
        statusLabel.setForeground(Color.WHITE);
    }

    private void handleMove(int row, int col) {
        if (!gameActive) return;
        if (board[row][col] != ' ') return;

        board[row][col] = currentPlayer;
        buttons[row][col].setText(String.valueOf(currentPlayer));
        buttons[row][col].setBackground(currentPlayer.equals("X") ?
                new Color(231, 76, 60) : new Color(41, 128, 185));

        if (checkWinner()) {
            statusLabel.setText(currentPlayer + " выиграл!");
            statusLabel.setForeground(currentPlayer.equals("X") ? Color.RED : Color.BLUE);
            gameActive = false;
        } else if (checkDraw()) {
            statusLabel.setText("Ничья!");
            statusLabel.setForeground(Color.YELLOW);
            gameActive = false;
        } else {
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
            statusLabel.setText("Ход: " + currentPlayer);
            statusLabel.setForeground(Color.WHITE);
        }
    }

    private boolean checkWinner() {
        String[] rows = {
            String.valueOf(board[0][0]) + board[0][1] + board[0][2],
            String.valueOf(board[1][0]) + board[1][1] + board[1][2],
            String.valueOf(board[2][0]) + board[2][1] + board[2][2],
            String.valueOf(board[0][0]) + board[1][0] + board[2][0],
            String.valueOf(board[0][1]) + board[1][1] + board[2][1],
            String.valueOf(board[0][2]) + board[1][2] + board[2][2],
            String.valueOf(board[0][0]) + board[1][1] + board[2][2],
            String.valueOf(board[2][0]) + board[1][1] + board[0][2]
        };
        for (String row : rows) {
            if (row.length() >= 3 && row.charAt(0) != ' ' &&
                    row.charAt(0) == row.charAt(1) && row.charAt(1) == row.charAt(2)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    private void openSettingsDialog() {
        JDialog dialog = new JDialog(this, "Настройки", true);
        dialog.setSize(280, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(4, 1, 0, 10));

        // Игрок 1
        JPanel p1Panel = new JPanel();
        p1Panel.setLayout(new BorderLayout());
        p1Panel.setBackground(new Color(52, 73, 94));
        p1Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel p1Label = new JLabel("Игрок 1:");
        p1Label.setFont(new Font("Arial", Font.PLAIN, 14));
        p1Label.setForeground(Color.WHITE);

        JCheckBox p1X = new JCheckBox("X");
        p1X.setSelected(true);
        p1X.setFont(new Font("Arial", Font.PLAIN, 14));

        p1Panel.add(p1Label, BorderLayout.NORTH);
        p1Panel.add(p1X, BorderLayout.CENTER);

        // Игрок 2
        JPanel p2Panel = new JPanel();
        p2Panel.setLayout(new BorderLayout());
        p2Panel.setBackground(new Color(52, 73, 94));
        p2Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel p2Label = new JLabel("Игрок 2:");
        p2Label.setFont(new Font("Arial", Font.PLAIN, 14));
        p2Label.setForeground(Color.WHITE);

        JCheckBox p2O = new JCheckBox("O");
        p2O.setSelected(true);
        p2O.setFont(new Font("Arial", Font.PLAIN, 14));

        p2Panel.add(p2Label, BorderLayout.NORTH);
        p2Panel.add(p2O, BorderLayout.CENTER);

        // Режим игры
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new GridLayout(1, 2, 0, 0));
        modePanel.setBackground(new Color(52, 73, 94));
        modePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JRadioButton rbPvP = new JRadioButton("PvP");
        rbPvP.setSelected(true);
        rbPvP.setFont(new Font("Arial", Font.PLAIN, 14));

        JRadioButton rbPvCPU = new JRadioButton("PvCPU");
        rbPvCPU.setFont(new Font("Arial", Font.PLAIN, 14));

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(rbPvP);
        modeGroup.add(rbPvCPU);

        modePanel.add(rbPvP);
        modePanel.add(rbPvCPU);

        // Уровень CPU
        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new BorderLayout());
        cpuPanel.setBackground(new Color(52, 73, 94));
        cpuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel cpuLabel = new JLabel("Уровень CPU:");
        cpuLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cpuLabel.setForeground(Color.WHITE);

        JSpinner cpuSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        cpuSpinner.setPreferredSize(new Dimension(50, 25));

        cpuPanel.add(cpuLabel, BorderLayout.NORTH);
        cpuPanel.add(cpuSpinner, BorderLayout.CENTER);

        // Кнопки
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(52, 73, 94));
        btnPanel.setLayout(new GridLayout(1, 2, 10, 0));

        JButton okBtn = new JButton("OK");
        okBtn.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.setFont(new Font("Arial", Font.PLAIN, 12));

        okBtn.addActionListener(e -> {
            boolean p1X = p1X.isSelected();
            boolean p2O = p2O.isSelected();
            boolean isPvCPU = rbPvCPU.isSelected();
            int cpuLevelVal = (int) cpuSpinner.getValue();

            if (!p1X || !p2O) {
                JOptionPane.showMessageDialog(dialog,
                        "Каждый игрок должен выбрать свою фигуру!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isPvCPU) {
                gameMode = "PvCPU";
                cpuLevel = cpuLevelVal;
            } else {
                gameMode = "PvP";
            }

            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);

        dialog.add(p1Panel);
        dialog.add(p2Panel);
        dialog.add(modePanel);
        dialog.add(cpuPanel);
        dialog.add(btnPanel);
        dialog.setVisible(true);
    }

    private static void main(String[] args) {
        SwingUtilities.invokeLater(SwingVersion::new);
    }
}
