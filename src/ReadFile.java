
/* Daniel Felsenthal

air  -->  mir  -->  mis  -->  pis  -->  pes  -->  oes  -->  ods  -->  odd  -->  old  -->  ole  -->  ore  -->  ire  -->  ice
rum  -->  sum  -->  sup  -->  yup  -->  yip  -->  yid  -->  yod  -->  yow  -->  wow  -->  woo  -->  zoo
sax  -->  say  -->  shy  -->  sha  -->  sea  -->  pea  -->  pes  -->  oes  -->  ohs  -->  oho  -->  mho  -->  moo  -->  mon  -->  ion  -->  inn  -->  ink  -->  ick  -->  ice
 I thought about using breadth first, but I thought the simplicity of the the stack
 used in depth first would make the code easier to read, though
 I'm not sure of the time differences.
 This program spends most of its runtime looking through arrays for neighboring items.
 On average, it needs to look at half the total items for  the exhaustive search, so this has
 about theta(n/2).
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ReadFile extends Application{

    public  ArrayList<String> wordList=new ArrayList<String>();
    public static void main(String[] args) {

        launch(args);



    }
    public void start(Stage stage) {
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext g = canvas.getGraphicsContext2D();
        BorderPane root = new BorderPane(canvas);
        stage.setScene(new Scene(root));
        stage.setTitle("Random Borbs");
        stage.setResizable(false);
        stage.show();
        writeState();
        readFile("C:\\Users\\Daniel\\Documents\\Write.txt",g);

    }
     static void writeState() {
        Scanner scnr=new Scanner(System.in);
        File dataFile= new File("C:\\Users\\Daniel\\Documents\\Write.txt");
        try(BufferedWriter out=new BufferedWriter( new FileWriter(dataFile))) {
            for (int n=0; n!=8; n++) {
                for (int j=0; j!=8; j++) {
                    if (n==1 || n==6) {
                        out.write(" P ");
                    }
                    if (n==0) {
                        if (j==0 || j==7)
                            out.write(" R ");
                        if (j==1 || j==6)
                            out.write(" H ");
                        if (j==2 || j==5)
                            out.write( " B ");
                        if (j==3)
                            out.write(" K ");
                        if (j==4)
                            out.write(" Q ");
                    }
                    if (n==7) {
                        if (j==0 || j==7)
                            out.write(" R ");
                        if (j==1 || j==6)
                            out.write(" H ");
                        if (j==2 || j==5)
                            out.write( " B ");
                        if (j==3)
                            out.write(" K ");
                        if (j==4)
                            out.write(" Q ");
                    }
                }
                out.newLine();
            }
        }
        catch (IOException iox) {
            System.out.println("You fucked up");
        }
    }

    static void stateWriter(HashMap<ArrayList<Integer>, Chess.chessPiece> map, boolean save) {
        File dataFile = new File("C:\\Users\\Daniel\\Documents\\Write.txt");

        try (BufferedWriter cw = new BufferedWriter(new FileWriter(dataFile))) {
            cw.write(String.valueOf(save));
            cw.newLine();
            if (save == false) {
                return;
            } else {
                for (Map.Entry<ArrayList<Integer>, Chess.chessPiece> cursor :
                        map.entrySet()) {
                    String x = String.valueOf(cursor.getKey().get(0));
                    String y = String.valueOf(cursor.getKey().get(1));
                    String type = cursor.getValue().typeSay();
                    String color;
                    if (cursor.getValue().isWhite)
                        color = "W";
                    else
                        color = "B";

                    cw.write(x + y + type + color);
                    cw.newLine();
                }
            }
        }
        catch (IOException IOex) {
            System.out.println("Handle the problem, dumbo");
        }
    }

    public static Chess.pieceType typeTranslate(String str) {
        if (str.equals("P"))
            return Chess.pieceType.PAWN;
        else if (str.equals("H"))
            return Chess.pieceType.KNIGHT;
        else if (str.equals("R"))
            return Chess.pieceType.ROOK;
        else if (str.equals("K"))
            return Chess.pieceType.KING;
        else if (str.equals("Q"))
            return Chess.pieceType.QUEEN;
        else
            return Chess.pieceType.BISHOP;
    }

  static void stateReader(String fileName) {

        Chess C=new Chess();
        C.pieceMap=new HashMap<ArrayList<Integer>, Chess.chessPiece>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line=bufferedReader.readLine();
                            //Chess c=new Chess();
                            System.out.println("kROOL");
                            while((line!=null)) {
                            char[] chary = line.toCharArray();
                            int x = (int) chary[0];
                            int y = (int) chary[1];
                            Chess.chessPiece piece = C.new chessPiece(x, y);
                            piece.type = typeTranslate(String.valueOf(chary[2]));
                            if (chary[3] == 'B')
                                piece.isWhite = false;
                            else
                                piece.isWhite = true;
                        }
                        C.setUp();
                    }
catch (IOException IOex) {
            System.out.println("Shite");
}



return;
    }

    static boolean falseReader(String fileName) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = bufferedReader.readLine();

            if (line.equals("false")) {
                return false;

            }

            return true;
        } catch (IOException IOex) {
            System.out.println("Shite");
        }
        return true;
    }




    public static void readFile(String fileName,GraphicsContext g) {


        // important data
       // String line = "";
        int lineCount = 0;
        int wordCount = 0 ;
        ArrayList<String> words2=new ArrayList<>();

        //  Read file from drive

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName)))
       { String line= bufferedReader.readLine();
           int n=25;
            //int ch=bufferedReader.read();
            //  Read line from file
            while ((line!=null)) {



               // System.out.println(line + " Printed!");
               // ch=bufferedReader.read();
               //// String[] words = line.split(" ");
              //
                g.setFill(Color.RED);
                g.setStroke(Color.FORESTGREEN);
               // g.fillRect(0,0,500,500);
                g.strokeText(line,30,30+n);
                n=n+25;
                line=bufferedReader.readLine();
                //  add word count length
              //  wordCount = wordCount + words.length;
            }

            //  print the count value of line & word
            for (String str:
                 words2) {
                System.out.print(str + " ");
            }
            System.out.println("Number of lines is : " + lineCount);
            System.out.println("Number of words is : " + wordCount);

        } //deal with checked exceptions
        catch (FileNotFoundException fnfex) {
            fnfex.printStackTrace();
        }
        catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
}

