import javax.swing.*;
import java.awt.*;


/**
 * Tic-Tac-Toe с графическим интерфейсом на Java Swing.
 * Поддерживает выбор размера поля (3x3, 4x4, 5x5 и т.д.).
 * Поле теперь квадратное (400x400) для правильного отображения.
 */
public class SwingVersion extends JFrame {

    private JButton[][] buttons;
    private String[][] board;
    private String currentPlayer = "X";
    private String gameMode = "PvP"; // "PvP" или "PvCPU"
    private boolean gameActive = true;
    private int cpuLevel = 1; // 1 = случайный, 2 = минимакс
    private int boardSize = 3; // размер поля (3x3 по умолчанию)
    private JLabel statusLabel;
    private int cellSize = 100; // размер ячейки в пикселях
    private int gap = 1; // минимальный отступ между ячейками для сохранения квадратной формы
    private JButton resetBtn; // кнопка сброса
    private JPanel gamePanel; // ПОЛЕ КЛАССА — панель для отображения сетки

    public SwingVersion() {
        setVisible(true);
        setTitle("Tic-Tac-Toe (Swing)");
        setSize(440, 500); // увеличиваем размер для панели результатов
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Панель заголовка (тулбар)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        headerPanel.setPreferredSize(new Dimension(440, 60));
        headerPanel.setBackground(new Color(52, 73, 94));

        JLabel titleLabel = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JButton newGameBtn = new JButton("Новая игра");
        newGameBtn.addActionListener(e -> startNewGame());

        JButton settingsBtn = new JButton("Настройки");
        settingsBtn.addActionListener(e -> openSettingsDialog());

        // Кнопка сброс — перенесена в тулбар
        resetBtn = new JButton("Сброс");
        resetBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        resetBtn.addActionListener(e -> startNewGame());

        headerPanel.add(titleLabel);
        headerPanel.add(newGameBtn);
        headerPanel.add(settingsBtn);
        headerPanel.add(resetBtn);

        add(headerPanel, BorderLayout.NORTH);

        // Основная панель игры - квадратная
        // ИСПРАВЛЕНИЕ: используем поле класса gamePanel вместо локальной переменной!
        gamePanel = new JPanel(); // теперь это поле класса, а не локальная переменная
        gamePanel.setBackground(new Color(248, 250, 255));
        gamePanel.setPreferredSize(new Dimension(400, 400));

        // Создаём кнопки
        buttons = new JButton[boardSize][boardSize];
        board = new String[boardSize][boardSize];

        // Вычисляем количество кнопок
        int numButtons = boardSize * boardSize;

        // Создаём все кнопки с одинаковым размером
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int curI = i;
                int curJ = j;
                // Устанавливаем одинаковый размер для всех кнопок
                int buttonSize = cellSize - gap;
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 30));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(e -> handleMove(curI, curJ));
                board[i][j] = " ";
                buttons[i][j].setPreferredSize(new Dimension(buttonSize, buttonSize));
                buttons[i][j].setMaximumSize(new Dimension(buttonSize, buttonSize));
                buttons[i][j].setBackground(Color.WHITE);
            }
        }

        // Добавляем кнопки в панель с GridLayout
        gamePanel.setLayout(new GridLayout(boardSize, boardSize, gap, gap));
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gamePanel.add(buttons[i][j]);
            }
        }

        // Индикатор текущего хода
        statusLabel = new JLabel("Ход: X");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(400, 30));
        gamePanel.add(statusLabel);

        add(gamePanel, BorderLayout.CENTER);

        // Панель результатов игры - отдельная строка
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBackground(new Color(52, 73, 94));
        resultPanel.setPreferredSize(new Dimension(440, 50));
        resultPanel.setOpaque(true);

        JLabel resultLabel = new JLabel("Результат: Игра в процессе...");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setForeground(Color.YELLOW);
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        add(resultPanel, BorderLayout.SOUTH);
    }

    private void startNewGame() {
        gameActive = true;
        currentPlayer = "X";
        gameMode = "PvP";
        cpuLevel = 1;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.WHITE);
                board[i][j] = " ";
            }
        }
        statusLabel.setText("Ход: X");
        statusLabel.setForeground(Color.WHITE);
        updateResultLabel();
    }

    private void updateResultLabel() {
        if (!gameActive) {
            if (checkWinner()) {
                statusLabel.setText(currentPlayer + " выиграл!");
                statusLabel.setForeground(currentPlayer.equals("X") ? Color.RED : Color.BLUE);
            } else if (checkDraw()) {
                statusLabel.setText("Ничья!");
                statusLabel.setForeground(Color.YELLOW);
            }
        }
    }

    private void handleMove(int row, int col) {
        if (!gameActive) return;
        if (!board[row][col].equals(" ")) return;

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
        // Проверяем все возможные линии выигрыша для заданного размера поля
        int size = boardSize;

        // Горизонтали
        for (int i = 0; i < size; i++) {
            String row = "";
            for (int j = 0; j < size; j++) {
                row += board[i][j];
            }
            if (row.length() >= 3 && row.charAt(0) != ' ' &&
                    row.charAt(0) == row.charAt(1) && row.charAt(1) == row.charAt(2)) {
                return true;
            }
        }

        // Вертикали
        for (int j = 0; j < size; j++) {
            String col = "";
            for (int i = 0; i < size; i++) {
                col += board[i][j];
            }
            if (col.length() >= 3 && col.charAt(0) != ' ' &&
                    col.charAt(0) == col.charAt(1) && col.charAt(1) == col.charAt(2)) {
                return true;
            }
        }

        // Диагонали
        String diag1 = "";
        String diag2 = "";
        for (int i = 0; i < size; i++) {
            diag1 += board[i][i];
            diag2 += board[i][size - 1 - i];
        }
        if (diag1.length() >= 3 && diag1.charAt(0) != ' ' &&
                diag1.charAt(0) == diag1.charAt(1) && diag1.charAt(1) == diag1.charAt(2)) {
            return true;
        }
        if (diag2.length() >= 3 && diag2.charAt(0) != ' ' &&
                diag2.charAt(0) == diag2.charAt(1) && diag2.charAt(1) == diag2.charAt(2)) {
            return true;
        }

        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].equals(" ")) return false;
            }
        }
        return true;
    }

    private void openSettingsDialog() {
        JDialog dialog = new JDialog(this, "Настройки", true);
        dialog.setSize(350, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 1, 0, 10));

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

        // Размер поля
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BorderLayout());
        sizePanel.setBackground(new Color(52, 73, 94));
        sizePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel sizeLabel = new JLabel("Размер поля:");
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sizeLabel.setForeground(Color.WHITE);

        JComboBox<Integer> sizeCombo = new JComboBox<>(new Integer[]{3, 4, 5});
        sizeCombo.setSelectedIndex(0);
        sizeCombo.addActionListener(e -> {
            boardSize = sizeCombo.getSelectedIndex() + 1;
            rebuildBoard(); // ПЕРЕСБОРКА ПАНЕЛИ ПРИ ИЗМЕНЕНИИ РАЗМЕРА
        });

        sizePanel.add(sizeLabel, BorderLayout.NORTH);
        sizePanel.add(sizeCombo, BorderLayout.CENTER);

        // Режим игры
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new BorderLayout());
        modePanel.setBackground(new Color(52, 73, 94));
        modePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel modeLabel = new JLabel("Режим игры:");
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        modeLabel.setForeground(Color.WHITE);

        JComboBox<String> modeCombo = new JComboBox<>(new String[]{"PvP", "PvCPU"});
        modeCombo.setSelectedIndex(0);
        modeCombo.addActionListener(e -> {
            gameMode = modeCombo.getSelectedItem().toString();
        });

        modePanel.add(modeLabel, BorderLayout.NORTH);
        modePanel.add(modeCombo, BorderLayout.CENTER);

        // Уровень сложности
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BorderLayout());
        levelPanel.setBackground(new Color(52, 73, 94));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel levelLabel = new JLabel("Уровень сложности:");
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        levelLabel.setForeground(Color.WHITE);

        JComboBox<Integer> levelCombo = new JComboBox<>(new Integer[]{1, 2});
        levelCombo.setSelectedIndex(0);
        levelCombo.addActionListener(e -> {
            cpuLevel = levelCombo.getSelectedIndex() + 1;
        });

        levelPanel.add(levelLabel, BorderLayout.NORTH);
        levelPanel.add(levelCombo, BorderLayout.CENTER);

        // Кнопки OK / Отмена
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(e -> dialog.dispose());

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(okBtn);
        buttonsPanel.add(cancelBtn);

        dialog.add(p1Panel);
        dialog.add(p2Panel);
        dialog.add(sizePanel);
        dialog.add(modePanel);
        dialog.add(levelPanel);
        dialog.add(buttonsPanel);
        dialog.setVisible(true);
    }

    /**
     * МЕТОД ПЕРЕСБОРКИ ПАНЕЛИ ПРИ ИЗМЕНЕНИИ РАЗМЕРА ПОЛЯ
     * Исправление ошибки: теперь используется поле класса gamePanel,
     * а не локальная переменная, что устраняет проблему асинхронности.
     * При смене размера сетки кнопки корректно удаляются и добавляются заново.
     */
    private void rebuildBoard() {
        // ИСПРАВЛЕНИЕ: теперь используем поле класса gamePanel,
        // которое уже заполнено кнопками из конструктора
        // Удаляем старые кнопки из панели
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (buttons[i][j] != null) {
                    gamePanel.remove(buttons[i][j]);
                    buttons[i][j].setText("");
                    buttons[i][j].setBackground(Color.WHITE);
                    board[i][j] = " ";
                }
            }
        }

        // Обновляем размер поля
        int newSize = boardSize;
        buttons = new JButton[newSize][newSize];
        board = new String[newSize][newSize];

        // Вычисляем количество кнопок
        int numButtons = newSize * newSize;

        // Создаём все кнопки с одинаковым размером
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                int curI = i;
                int curJ = j;
                // Устанавливаем одинаковый размер для всех кнопок
                int buttonSize = cellSize - gap;
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 30));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(e -> handleMove(curI, curJ));
                board[i][j] = " ";
                buttons[i][j].setPreferredSize(new Dimension(buttonSize, buttonSize));
                buttons[i][j].setMaximumSize(new Dimension(buttonSize, buttonSize));
                buttons[i][j].setBackground(Color.WHITE);
            }
        }

        // Добавляем кнопки в панель с GridLayout
        gamePanel.setLayout(new GridLayout(newSize, newSize, gap, gap));
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                gamePanel.add(buttons[i][j]);
            }
        }

        // Обновляем размер окна под новую сетку
        int windowSize = newSize * (cellSize - gap) + 10 + 30; // 10 - отступы, 30 - statusLabel
        setSize(windowSize, windowSize + 80); // +80 для панели результатов

        // Перерисовываем компонент
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingVersion::new);
    }
}