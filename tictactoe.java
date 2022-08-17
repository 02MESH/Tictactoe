import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class tictactoe extends JFrame
{
    //Method that will call the tictactoe object
    public static void main(String[] args) {
        tictactoe game = new tictactoe();
    }

    /////////////////////////////////////////GUI///////////////////////////////////////
    //All 9 Jbuttons will saved in buttons.
    JButton[] buttons = new JButton[9];
    //String variable to hold the user's turn.
    String playerMark = "O";
    //Main container where the program will run.
    Container contentPane = getContentPane();

    String username = "";

    //Boolean variables to manage game flow.
    boolean gameOver = false;
    boolean startGame = false;

    //Boolean variable to manage username if it's set or not.
    boolean usernameSet = false;

    //Buttons and what names they are meant to be initialised with.
    JButton startButton = new JButton("Start");
    JButton replayButton = new JButton("Replay");

    //2 JPanels, one for the grid and the other one for the buttons.
    JPanel gamePanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JLabel dataBoard = new JLabel();

    //JTextField to enter the username.
    JTextField usernameInput = new JTextField();

    //Font cutomisations.
    Font font = new Font("SansSerif", Font.BOLD, 70);
    Font textPaneFont = new Font("SansSerif", Font.BOLD, 20);
    ////////////////////////////////////////GUI///////////////////////////////////////

    ////////////////////////////////////////AI////////////////////////////////////////
    boolean aiTurn = false;
    ////////////////////////////////////////AI////////////////////////////////////////

    public tictactoe() {
        /////////////////////////////////////////////////GUI//////////////////////
        gamePanel.setLayout(new BorderLayout());

        contentPane.add(gamePanel, BorderLayout.CENTER);
        contentPane.add(rightPanel, BorderLayout.EAST);
        contentPane.add(dataBoard, BorderLayout.NORTH);
        contentPane.add(usernameInput, BorderLayout.SOUTH);
        dataBoard.setHorizontalAlignment(JLabel.CENTER);

        dataBoard.setText("Welcome! What's your name?");
        dataBoard.setFont(textPaneFont);

        //3x3 layout
        gamePanel.setLayout(new GridLayout(3,3));

        rightPanel.add(startButton, BorderLayout.NORTH);
        rightPanel.add(replayButton, BorderLayout.CENTER);

        //Menubar code///////////////////////////////////
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu option = new JMenu("Menu");
        menuBar.add(option);
        JMenuItem resetGame = new JMenuItem("Reset");
        option.add(resetGame);
        resetGame.addActionListener(ev -> resetBoard());
        JMenuItem quitGame = new JMenuItem("Quit");
        option.add(quitGame);
        quitGame.addActionListener(ev -> System.exit(0));
        /////////////////////////////////////////////////
        //Start and replay buttons here//////////////////
        startButton.addActionListener(ev -> startGame());
        replayButton.setEnabled(false);
        replayButton.addActionListener(ev ->replayGame());
        ////////////////////////////////////////////////
        //Setting dimensions////////////////////////////
        setTitle("Tictactoe");
        setSize(800,500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        drawBoard();
        setVisible(true);
        ////////////////////////////////////////////////GUI///////////////////////////////////
    }

    //Method that will print and manage the buttons.
    private void drawBoard() {
        for (int i = 0; i <= 8; i++) {
            buttons[i] = new JButton();
            buttons[i].setText("");
            buttons[i].setFont(font);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = getUsername();
                    JButton clicked = (JButton) e.getSource();
                    if (clicked.getText().isEmpty() && !gameOver && startGame) {
                        replayButton.setEnabled(true);
                        if (playerMark == "O") {
                            clicked.setText(String.valueOf("O"));
                            clicked.setBackground(Color.CYAN);
                            displayWinner();
                            AI();
                        }
                        displayWinner();
                    }
                }
            });
            gamePanel.add(buttons[i]);
            buttons[i].setBackground(Color.BLACK);
        }
    }

    //Username setter method.
    private void setUsername() {
        username = usernameInput.getText();
    }

    //Username getter method.
    private String getUsername() {
        return username;
    }

    //Method that will manage game starts.
    private void startGame() {
        //If the box is empty.
        if (usernameInput.getText().trim().equals("")) {
            //JOptionPane.showMessageDialog(null, "Username field is still empty.");
            dataBoard.setText("What's your name again?");
            setUsername();
        }
        else {
            setUsername();
            dataBoard.setText(username + ", please place a nought. AI will place a cross after you.");
            replayButton.setEnabled(true);
            startButton.setEnabled(false);
            usernameInput.setEnabled(false);
            startGame = true;
        }
    }

    //Method that will manage whenever the user replays a game.
    private void replayGame() {
        reset();
        startButton.setEnabled(false);
        startGame = true;
        dataBoard.setText(username + ", please place a nought. AI will place a cross after you.");
    }

    //Method that will reset the board.
    private void resetBoard() {
        startGame();
        reset();
        usernameInput.setText("");
        usernameInput.setEnabled(true);
        dataBoard.setText("Welcome! What's your name?");
    }

    //Main reset method.
    private void reset() {
        playerMark = "O";
        startButton.setEnabled(true);
        replayButton.setEnabled(false);
        startGame = false;
        for (JButton button : buttons) {
            button.setText("");
            button.setBackground(Color.BLACK);
        }
        gameOver = false;
    }

    //Method that will print messages on the JTextPane on top.
    private void displayWinner() {
        //If a winning condition has been detected.
        if (checkWinner()) {
            //If the playerMark is X, it sets to O as it means that O had played
            //the last move before the switch.
            if (playerMark == "O") {
                playerMark = "X";
                dataBoard.setText("AI wins! Do better next time " + username + "!");
            }
            //If the playerMark is O, it set to X as it means that X had played
            //the last move before the switch.
            else if (playerMark == "X") {
                playerMark = "O";
                dataBoard.setText("You win! Congratulations, " + username + "!");
            }
        }
        //If the winning condition is a draw.
        else if (checkDraw()) {
            dataBoard.setText("Neck-and-neck! Good job, " + username + "!");
            gameOver = true;
        }
    }

    //Method that will check whether if or not the method hit the winning conditions.
    private boolean checkWinner() {
        if (checkRows() || checkColumns() || checkDiagonals()) {
            gameOver = true;
            return true;
        }
        else {
            return false;
        }
    }

    //Method that will check whether if or not the game ended in a draw.
    private boolean checkDraw() {
        boolean flag = true;
        int counter = 9;
        //Checks if all the 9 buttons are not empty.
        for (int i = 0; i <= 8; i++) {
            if (buttons[i].getText().isEmpty()) {
                flag = false;
            }
        }
        return flag;
    }

    //Method that will check whether if the AI/user won the game by the horizontal
    //winning condition.
    private boolean checkRows() {
        int i = 0;
        for (int j = 0; j < 3; j++) {
            if (buttons[i].getText().equals(buttons[i+1].getText()) && buttons[i].getText().equals(buttons[i+2].getText())
                    && !buttons[i].getText().equals("")) {
                return true;
            }
            i = i + 3;
        }
        return false;
    }

    //Method that will check whether if the AI/user won the game by the vertical
    //winning condition.
    private boolean checkColumns() {
        int i = 0;
        for (int j = 0; j < 3; j++) {
            if (buttons[i].getText().equals(buttons[i+3].getText()) && buttons[i].getText().equals(buttons[i+6].getText())
                    && !buttons[i].getText().equals("")) {
                return true;
            }
            i++;
        }
        return false;
    }

    //Method that will check whether if the AI/user won the game by the diagonal
    //winning condition.
    private boolean checkDiagonals() {
        if (buttons[0].getText().equals(buttons[4].getText()) && buttons[0].getText().equals(buttons[8].getText())
                && !buttons[0].getText().equals("")) {
            return true;
        }
        else if (buttons[2].getText().equals(buttons[4].getText()) && buttons[2].getText().equals(buttons[6].getText())
                && !buttons[2].getText().equals("")) {
            return true;
        }
        else {
            return false;
        }
    }

    //AI starts here
    //Main AI method that will play after the user has placed. It is semi-random in
    //sense that if the while loop doesn't hit an if condition, a random move will
    //be generated.
    private void AI() // easiest way for move method for Artificial intelligence and study how its work and then you can use loops optimize for the code
    {
        aiTurn = true;
        while (aiTurn && !gameOver) {
            if ((buttons[0].getText().equals("O")) && (buttons[1].getText().equals("O")) && (buttons[2].getText().equals(""))) {
                buttons[2].setText("X");
                buttons[2].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[3].getText().equals("O")) && (buttons[4].getText().equals("O")) && (buttons[5].getText().equals(""))) {
                buttons[5].setText("X");
                buttons[5].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[6].getText().equals("O")) && (buttons[7].getText().equals("O")) && (buttons[8].getText().equals(""))) {
                buttons[8].setText("X");
                buttons[8].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[1].getText().equals("O")) && (buttons[2].getText().equals("O")) && (buttons[0].getText().equals(""))) {
                buttons[0].setText("X");
                buttons[0].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[4].getText().equals("O")) && (buttons[5].getText().equals("O")) && (buttons[3].getText().equals(""))) {
                buttons[3].setText("X");
                buttons[3].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[7].getText().equals("O")) && (buttons[8].getText().equals("O")) && (buttons[6].getText().equals(""))) {
                buttons[6].setText("X");
                buttons[6].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[0].getText().equals("O")) && (buttons[2].getText().equals("O")) && (buttons[1].getText().equals(""))) {
                buttons[1].setText("X");
                buttons[1].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[3].getText().equals("O")) && (buttons[5].getText().equals("O")) && (buttons[4].getText().equals(""))) {
                buttons[4].setText("X");
                buttons[4].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[6].getText().equals("O")) && (buttons[8].getText().equals("O")) && (buttons[7].getText().equals(""))) {
                buttons[7].setText("X");
                buttons[7].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[0].getText().equals("O")) && (buttons[3].getText().equals("O")) && (buttons[6].getText().equals(""))) {
                buttons[6].setText("X");
                buttons[6].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[1].getText().equals("O")) && (buttons[4].getText().equals("O")) && (buttons[7].getText().equals(""))) {
                buttons[7].setText("X");
                buttons[7].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[2].getText().equals("O")) && (buttons[5].getText().equals("O")) && (buttons[8].getText().equals(""))) {
                buttons[8].setText("X");
                buttons[8].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[3].getText().equals("O")) && (buttons[6].getText().equals("O")) && (buttons[0].getText().equals(""))) {
                buttons[0].setText("X");
                buttons[0].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[4].getText().equals("O")) && (buttons[7].getText().equals("O")) && (buttons[1].getText().equals(""))) {
                buttons[1].setText("X");
                buttons[1].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[5].getText().equals("O")) && (buttons[8].getText().equals("O")) && (buttons[2].getText().equals(""))) {
                buttons[2].setText("X");
                buttons[2].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[0].getText().equals("O")) && (buttons[6].getText().equals("O")) && (buttons[3].getText().equals(""))) {
                buttons[3].setText("X");
                buttons[3].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[1].getText().equals("O")) && (buttons[7].getText().equals("O")) && (buttons[4].getText().equals(""))) {
                buttons[4].setText("X");
                buttons[4].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[2].getText().equals("O")) && (buttons[8].getText().equals("O")) && (buttons[5].getText().equals(""))) {
                buttons[5].setText("X");
                buttons[5].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[0].getText().equals("O")) && (buttons[4].getText().equals("O")) && (buttons[8].getText().equals(""))) {
                buttons[8].setText("X");
                buttons[8].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[4].getText().equals("O")) && (buttons[8].getText().equals("O")) && (buttons[0].getText().equals(""))) {
                buttons[0].setText("X");
                buttons[0].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[0].getText().equals("O")) && (buttons[8].getText().equals("O")) && (buttons[4].getText().equals(""))) {
                buttons[4].setText("X");
                buttons[4].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[2].getText().equals("O")) && (buttons[4].getText().equals("O")) && (buttons[6].getText().equals(""))) {
                buttons[6].setText("X");
                buttons[6].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[6].getText().equals("O")) && (buttons[4].getText().equals("O")) && (buttons[2].getText().equals(""))) {
                buttons[2].setText("X");
                buttons[2].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[6].getText().equals("O")) && (buttons[2].getText().equals("O")) && (buttons[4].getText().equals(""))) {
                buttons[4].setText("X");
                buttons[4].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[0].getText().equals("O")) && (buttons[4].getText().equals("X")) && (buttons[8].getText().equals("O"))) {
                buttons[5].setText("X");
                buttons[5].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if ((buttons[2].getText().equals("O")) && (buttons[4].getText().equals("X")) && (buttons[6].getText().equals("O"))) {
                buttons[3].setText("X");
                buttons[3].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if (buttons[4].getText().equals("")) {
                buttons[4].setText("X");
                buttons[4].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if (buttons[0].getText().equals("")) {
                buttons[0].setText("X");
                buttons[0].setBackground(Color.YELLOW);
                aiTurn = false;
            } else if (buttons[7].getText().equals("O") && buttons[6].getText().isEmpty()) {
                buttons[6].setText("X");
                buttons[6].setBackground(Color.YELLOW);
                aiTurn = false;
            } else {
                random();
                aiTurn = false;
            }
        }
    }

    //Random move  generator of the AI.
    private void random() {
        Random random = new Random();
        boolean flag = false;
        int buttonNumber;
        while (!flag) {
            buttonNumber = random.nextInt(8);
            if (buttons[buttonNumber].getText().isEmpty()) {
                buttons[buttonNumber].setText("X");
                buttons[buttonNumber].setBackground(Color.YELLOW);
                flag = true;
            }
        }
    }
};
