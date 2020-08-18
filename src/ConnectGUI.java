import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConnectGUI {

    public static final int ROW_COUNT = 6;
    public static final int COLUMN_COUNT = 7;
    private static final int SQUARE_SIZE = 100;
    public static final int CIRCLE_SIZE = 100;

    private static int[][] board = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0}
    };

    private JFrame frame = new JFrame(this.getClass().getSimpleName());
    private JPanel pane;
    private Image backgroundImage;
    private int turn;
    private boolean gameOver;
//    private int[][] board;


    private void createAndShowGUI() {
        //We create the JFrame
        frame.setFocusable(true);
        frame.setResizable(false);
//        board = Arrays.stream(START_BOARD).map(int[]::clone).toArray(int[][]::new);
        turn = 0;
        gameOver = false;

        try {
            backgroundImage = ImageIO.read(new File("img/board.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Creates our JPanel that's going to draw every rectangle
        pane = new JPanel() {
            //Specifies the size of our JPanel
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(700, 750);
            }

            // Paints the background image, adds numbers from the current grid and add a rectangle of selected color
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.fillRect(0, 0, 700, 800);
                g2d.setColor(Color.BLACK);

                for (int i = 0; i < COLUMN_COUNT; i++) {
                    for (int j = 0; j < ROW_COUNT; j++) {
                        if (board[j][i] == 1) {
                            g2d.setColor(Color.YELLOW);
                        }
                        if (board[j][i] == 2) {
                            g2d.setColor(Color.RED);
                        }
                        g2d.fillOval(i * SQUARE_SIZE, j * SQUARE_SIZE + 100, CIRCLE_SIZE, CIRCLE_SIZE);
                        g2d.setColor(Color.BLACK);
                    }
                }
                g2d.drawImage(backgroundImage, 0, 90, this);
                if (gameOver) {
                    g.setColor(Color.white);
                    g.setFont(new Font("arial", Font.BOLD, 45));
                    g.drawString("Player " + (turn + 1) + " wins!", 200, 50);
                    g.setFont(new Font("arial", Font.BOLD, 20));
                    g.drawString("Hit Enter to Play Again!", 230, 80);
                }
            }
        };

        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameOver) {
                    int i = 0;
//                    while (i < 2) {
                    System.out.println("Player turn: " + (turn + 1));
                    int col = e.getX() / 100;
//                        if ((turn + 1) == 2) {
//                            col = (int) (Math.random() * COLUMN_COUNT - 1);
//
//
//
//                        }
                    if (isValidLocation(col)) {
                        int row = getNextOpenRow(col);
                        dropPiece(row, col, turn + 1);
                        if (winningMove(turn + 1)) {
                            System.out.println("Player " + (turn + 1) + " wins!");
                            gameOver = true;
                        }

                        if (!gameOver) {
                            turn++;
                            turn = turn % 2;
                        }
                    }
                    i++;
                }
//                }
                pane.repaint();
            }
        });


        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        createAndShowGUI();
                        board = new int[6][7];
                    }
                }
            }
        });

        // Add everything to the frame
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void dropPiece(int row, int col, int piece) {
        board[row][col] = piece;
    }

    public boolean isValidLocation(int col) {
        return board[0][col] == 0;
    }

    public int getNextOpenRow(int col) {
        for (int row = ROW_COUNT - 1; row > 0; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return 0;
    }

    private void printBoard() {
        for (int i = 0; i < ROW_COUNT; i++) {
            System.out.println(Arrays.toString(board[i]));

        }
    }

    private boolean winningMove(int piece) {
        // Check horizontal
        for (int c = 0; c < COLUMN_COUNT - 3; c++) {
            for (int r = 0; r < ROW_COUNT; r++) {
                if (board[r][c] == piece && board[r][c + 1] == piece && board[r][c + 2] == piece && board[r][c + 3] == piece) {
                    return true;
                }
            }
        }
        // Check vertical
        for (int c = 0; c < COLUMN_COUNT; c++) {
            for (int r = 0; r < ROW_COUNT - 3; r++) {
                if (board[r][c] == piece && board[r + 1][c] == piece && board[r + 2][c] == piece && board[r + 3][c] == piece) {
                    return true;
                }
            }
        }
        // Check positive sloped diagonal
        for (int c = 0; c < COLUMN_COUNT - 3; c++) {
            for (int r = 0; r < ROW_COUNT - 3; r++) {
                if (board[r][c] == piece && board[r + 1][c + 1] == piece && board[r + 2][c + 2] == piece && board[r + 3][c + 3] == piece) {
                    return true;
                }
            }
        }
        // Check negative sloped diagonal
        for (int c = 0; c < COLUMN_COUNT - 3; c++) {
            for (int r = 3; r < ROW_COUNT; r++) {
                if (board[r][c] == piece && board[r - 1][c + 1] == piece && board[r - 2][c + 2] == piece && board[r - 3][c + 3] == piece) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ConnectGUI()::createAndShowGUI);
    }

}

