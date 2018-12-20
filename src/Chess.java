import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class Chess extends Application {



    public boolean saving=false;
    public ArrayList<Integer> lister;
    public HashMap<ArrayList<Integer>,chessPiece> pieceMap;
    Canvas canvas;
    public boolean whiteFocus=false;
    public boolean blackFocus=false;
    GraphicsContext g;



    enum pieceType { KNIGHT, KING, QUEEN, BISHOP, PAWN, ROOK}
    class chessPiece {
        public String typeSay() {
            if (this.type==pieceType.PAWN)
                return "P";
            else if (this.type==pieceType.KNIGHT)
                return "H";
           else if (this.type==pieceType.QUEEN)
                return "Q";
           else  if (this.type==pieceType.ROOK)
                return "R";
           else  if (this.type==pieceType.KING)
                return "K";
            else
                return "B";
        }

        int x;
        int y;
        boolean isWhite;
        boolean hasFocus;
        pieceType type;
       private ArrayList<Integer> lister2;
        HashMap<ArrayList<Integer>,Boolean> moveSet=new HashMap<>();
        boolean special;


        chessPiece(int x, int y) {

            special=false;
            hasFocus=false;
            this.lister2=new ArrayList<Integer>();
            this.lister2.add(x);
            this.lister2.add(1,y);
            this.x = x;
            this.y = y;
            if (y == 1 || y == 6)
                type = pieceType.PAWN;
            else if (x == 0 || x == 7)
                this.type = pieceType.ROOK;
            else if (x == 1 || x == 6)
                this.type = pieceType.KNIGHT;

            else if (x == 2 || x == 5)
                this.type = pieceType.BISHOP;
            else if (x == 3)
                this.type = pieceType.KING;
            else
                this.type = pieceType.QUEEN;
            pieceMap.put(lister2,this);
            this.moveSet=new HashMap<>();



       //     System.out.println(this.x + " , " + this.y + "Ra!");
        }


        void addToMoveList(int x, int y, chessPiece piece, boolean booler) {
            ArrayList<Integer> addedList=new ArrayList<>(2);
            addedList.add(x);
            addedList.add(y);
            piece.moveSet.put(addedList,booler);
        }
    }



    public void setUp() {
        System.out.println("set");
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.PAPAYAWHIP);
        g.setFont(Font.font("Helvetica",50));
        g.setLineWidth(3);
        // g.strokeRect(0, 0, 25, 25);
        for (int n = 1; n < 73; n++) {
            if (n % 2 == 0) {
                g.setFill(Color.BLACK);
                if ((n / 9) < 4 && n / 9 != 0) {
                    g.setFill(Color.BLACK);
                }
            } else g.setFill(Color.WHITE);
            g.fillRect(100 + (n % 9) * 50, (((n / 9) * 50) + 100), 50, 50);
            if (((n / 9) >= 5) && n % 2 == 0) {
            }
            if (n % 9 == 8) {
                n++;

            }
        }
            g.setStroke(Color.SADDLEBROWN);
            g.strokeRect(150,100,400,400);
            g.setStroke(Color.RED);
            for (Map.Entry<ArrayList<Integer>,chessPiece> cursor:
                 pieceMap.entrySet()) {
                 int tempX=cursor.getValue().x;
                 int tempY=cursor.getValue().y;
                if (cursor.getValue().isWhite)
                    g.setFill(Color.AQUA);
                else
                    g.setFill(Color.FORESTGREEN);

              //  System.out.println("I screwed it " + pieceMap.entrySet().size());
                g.fillText(cursor.getValue().typeSay(),155+tempX*50,145+tempY*50);
                if(cursor.getValue().hasFocus) {
                    for (ArrayList<Integer> inti:
                    cursor.getValue().moveSet.keySet() ) {
                        g.strokeRect((inti.get(0)*50+150),(inti.get(1)*50+100),50,50);
                    }
                }

            }


}


    public void start(Stage stage) {

        lister=new ArrayList<Integer>(2);
        pieceMap=new HashMap<ArrayList<Integer>, chessPiece>();
        lister.add(0);
        lister.add(0);


        canvas = new Canvas(600, 600);
        stage.setOnCloseRequest(event -> CloseRequest(event));
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.FORESTGREEN);
        g.fillRect(0,0,1000,1000);
        canvas.setOnMousePressed(event -> mousePressed(event));
        Button redraw = new Button("Deal Again!");
        redraw.setOnAction(e -> start(stage));
        Button save=new Button("Save your game?");
        save.setOnAction(event -> isSaving(event));
        StackPane top=new StackPane(save);
        StackPane bottom = new StackPane(redraw);
        BorderPane root = new BorderPane(canvas);
        root.setTop(top);
        root.setBottom(bottom);
        stage.setScene(new Scene(root, Color.BLACK));
        stage.setTitle("Random Borbs");
        stage.setResizable(false);
        stage.show();
        g.setStroke(Color.PAPAYAWHIP);
        g.setLineWidth(2.5);
        if (ReadFile.falseReader("C:\\Users\\Daniel\\Documents\\Write.txt")) {
            System.out.println("Gross");
            ReadFile.stateWriter(pieceMap, true);
            setUp();

        }
     // if (false)
      //    return;
        else {
            for (int n = 0; n < 8; n++) {
                for (int j = 0; j < 8; j++) {
                    if (n < 2) {

                        chessPiece piece = new chessPiece(j, n);
                        //     System.out.println(j + " , " + n);
                        piece.isWhite = true;
                        //    System.out.println("Penis");

                    } else if (n > 5) {

                        chessPiece piece = new chessPiece(j, n);
                        //     System.out.println(j + " , " + n);
                        piece.isWhite = false;
                        //    System.out.println("Penis");


                    }
                }
            }
            setUp();
        }
    }

    public boolean isSaving( ActionEvent evt) {
        saving=!saving;
        return saving;
    }

    public void CloseRequest(WindowEvent evt) {
        if (saving==true) {
            ReadFile.stateWriter(pieceMap,true);
            System.out.println("Cavity");
        }
        System.out.println("Huuuge Penis");
    }

    public void mousePressed(MouseEvent event) {
      //  System.out.println(event.getX() + " , " + event.getY());
        if ((event.getX() > 149 && event.getX() < 551) && (event.getY() > 99 && event.getY() < 501)) {
            int x = (int) (event.getX() - 150) / 50;
            int y = (int) (event.getY() - 100) / 50;

            System.out.println(x + " , " + y + "Regr");

            if ((whiteFocus == false) && blackFocus == false) {

                lister.set(0, x);
                lister.set(1, y);
                System.out.println(lister.get(0) + " Igbo " + lister.get(1) );
                if (!pieceMap.containsKey(lister))
                    System.out.println("Boobies");
                if (pieceMap.containsKey(lister)) {
                    System.out.println("Yurrgurt");
                    moveSet(pieceMap.get(lister));
                    pieceMap.get(lister).hasFocus=true;
                    if (pieceMap.get(lister).isWhite) {
                        whiteFocus = true;
                        System.out.println("Dab");
                    } else {
                        blackFocus = true;
                        System.out.println("Bad");
                    }


                }
            }
            else if (event.isSecondaryButtonDown()) {
                System.out.println("RightClick");
                whiteFocus = false;
                blackFocus = false;
                setUp();
                return;
            }

            else if (whiteFocus) {

                System.out.println(x + " , " + y);

                ArrayList<Integer> tempList = new ArrayList<>();
                tempList.add(x);
                tempList.add(1, y);
                System.out.println(lister.get(0));
                if (!pieceMap.get(lister).moveSet.containsKey(tempList)) {
                    if (!pieceMap.containsKey(tempList)) {
                        whiteFocus = false;
                        System.out.println("Dongers");
                        pieceMap.get(lister).hasFocus = false;
                        setUp();
                        return;
                    } else {
                        pieceMap.get(tempList).hasFocus = true;
                        System.out.println(pieceMap.get(tempList).x + " Berra " + pieceMap.get(tempList).y);
                        moveSet(pieceMap.get(tempList));


                        pieceMap.get(lister).hasFocus = false;
                        //  pieceMap.get(lister).moveSet=null;
                        lister.set(0, x);
                        lister.set(1, y);
                        System.out.println("Angel");
                    }
                }

               else if (pieceMap.get(lister).moveSet.containsKey(tempList)) {
                    System.out.println(tempList.get(0) + "Yub" + tempList.get(1));
                    whiteFocus = false;


                    if (pieceMap.get(lister).moveSet.get(tempList)) {
                        pieceMap.remove(tempList);
                        System.out.println("Mr. Clean");
                    }

                    chessPiece newPiece = new chessPiece(tempList.get(0), tempList.get(1));
                    newPiece.isWhite = pieceMap.get(lister).isWhite;
                    newPiece.special=pieceMap.get(lister).special;
                    if (pieceMap.get(lister).type == pieceType.PAWN && ((newPiece.isWhite && newPiece.y == 7) || (!newPiece.isWhite && newPiece.y == 0)))
                        newPiece.type = pieceType.QUEEN;
                    else
                        newPiece.type = pieceMap.get(lister).type;
                    pieceMap.remove(lister);

                }
                System.out.println("Yogi");

            }
                    else if (blackFocus) {

                    System.out.println(x + " , " + y);

                    ArrayList<Integer> tempList=new ArrayList<>();
                    tempList.add(x);
                    tempList.add(1,y);


                if (!pieceMap.get(lister).moveSet.containsKey(tempList)) {
                    if (!pieceMap.containsKey(tempList)) {
                        whiteFocus = false;
                        System.out.println("Dongers");
                        pieceMap.get(lister).hasFocus = false;
                        setUp();
                        return;
                    } else {
                        pieceMap.get(tempList).hasFocus = true;
                        System.out.println(pieceMap.get(tempList).x + " Berra " + pieceMap.get(tempList).y);
                        moveSet(pieceMap.get(tempList));


                        pieceMap.get(lister).hasFocus = false;
                        //  pieceMap.get(lister).moveSet=null;
                        lister.set(0, x);
                        lister.set(1, y);
                        System.out.println("Angel");
                    }
                }

                if (pieceMap.get(lister).moveSet.containsKey(tempList)) {
                        System.out.println(tempList.get(0) + "Yub" + tempList.get(1));
                        blackFocus=false;


                        if (pieceMap.get(lister).moveSet.get(tempList)) {
                            pieceMap.remove(tempList);
                            System.out.println("Mr. Clean");
                        }
                        chessPiece newPiece=new chessPiece(tempList.get(0),tempList.get(1));
                        newPiece.isWhite=pieceMap.get(lister).isWhite;
                        newPiece.special=pieceMap.get(lister).special;

                        if (pieceMap.get(lister).type==pieceType.PAWN && ((!newPiece.isWhite && newPiece.y==7) || (!newPiece.isWhite && newPiece.y==0)))
                            newPiece.type=pieceType.QUEEN;
                        else
                            newPiece.type=pieceMap.get(lister).type;
                        pieceMap.remove(lister);

                    }
                }
                    g.strokeRect(150 + (x % 9) * 50, (((y % 9) * 50) + 100), 50, 50);
                }
                else {
                     blackFocus=false;
                     whiteFocus=false;
                     System.out.println("Dualies!");

        }


        setUp();
            }





    boolean legalMove (chessPiece piece, int x, int y) {
        ArrayList<Integer> list =new ArrayList<Integer>();
        list.add(x);
        list.add(y);
        int[] ary=new int[2];
        System.out.println(list.get(0) + " " + list.get(1) + " and the piece is " + piece.x + " " +piece.y);
        if (piece.type == pieceType.PAWN) {

            if (piece.isWhite == true) {

                if (!pieceMap.containsKey(list) && list.get(0) == piece.x && (list.get(1) - piece.y == 1 || ((list.get(1) - piece.y == 2 && piece.y==1 )))) {
                    System.out.println("Yobo");
                    return true;

                }
                else if ((list.get(0)-piece.x==-1) && list.get(1)-piece.y==1) {

                    if (pieceMap.containsKey(list) && !pieceMap.get(list).isWhite) {
                        pieceMap.remove(list);
                        return true; }
                    else
                        return false;
                }
               else if((list.get(0)-piece.x==1) && list.get(1)-piece.y==1) {
                    if (pieceMap.containsKey(list)&& !pieceMap.get(list).isWhite) {
                        pieceMap.remove(list);
                        return true; }
                    else
                        return false;
                }
            }
            else{

                if (!pieceMap.containsKey(list) && list.get(0) == piece.x && (list.get(1) - piece.y == -1 || ((list.get(1) - piece.y == -2 && piece.y==6 )))) {
                    System.out.println("Yobo");
                    return true;

                }
                else if ((list.get(0)-piece.x==1) && list.get(1)-piece.y==-1 ) {

                    if (pieceMap.containsKey(list)&& pieceMap.get(list).isWhite) {
                    pieceMap.remove(list);
                    return true; }
                    else
                        return false;
                }
                else if((list.get(0)-piece.x==-1) && list.get(1)-piece.y==-1) {
                    if (pieceMap.containsKey(list) && pieceMap.get(list).isWhite) {
                        pieceMap.remove(list);
                        return true;
                    }
                    else
                        return false;
                }
            }
        }
        else if (piece.type==pieceType.KNIGHT) {
            System.out.println((list.get(0)-piece.x) + " " + (list.get(1)-piece.y));
              //  if (((list.get(0) - piece.x) == -1 && (list.get(1) - piece.y) == -2) || ((list.get(0)-piece.x)==1 && (list.get(1)-piece.y)==-2) || ((list.get(0)-piece.x)==-1 && (list.get(1)-piece.y)==2) || ((list.get(0)-piece.x)==1 && (list.get(1)-piece.y)==2) ) {
               if (((list.get(0)-piece.x==1 || list.get(0)-piece.x==-1) && (list.get(1)-piece.y==2 || list.get(1)-piece.y==-2)) || ((list.get(0)-piece.x==2 || list.get(0)-piece.x==-2) && (list.get(1)-piece.y==1 || list.get(1)-piece.y==-1))) {
                    System.out.println("Drog");
                    if(!pieceMap.containsKey(list)) {
                        System.out.println("Orgo");    return true; }
                       if (pieceMap.containsKey(list) && pieceMap.get(list).isWhite!=piece.isWhite) {
                        pieceMap.remove(list);
                           System.out.println("Blrg");
                        return true;
                    }

                }

        }
        else if (piece.type==pieceType.KING) {
            System.out.println("Kingly");
            if ((list.get(0)-piece.x>=-1 && list.get(0)-piece.x<=1) && (list.get(1)-piece.y>=-1 && list.get(1)-piece.y<=1)) {
                if (!pieceMap.containsKey(list))
                    return true;
                else if (pieceMap.containsKey(list) && pieceMap.get(list).isWhite!=piece.isWhite) {
                    pieceMap.remove(list);
                    return true;
                }
            }
        }
        else if (piece.type==pieceType.QUEEN) {
            if (Math.abs(list.get(0)-piece.x)==Math.abs(list.get(1)-piece.y) || (list.get(0)-piece.x==0 && list.get(1)-piece.y!=0) || ((list.get(0)-piece.x)!=0&& list.get(1)-piece.y==0))
            if(piecesOnPath(piece,list)){
                if (!pieceMap.containsKey(list))
                return true;
                else if (pieceMap.containsKey(list) && pieceMap.get(list).isWhite!=piece.isWhite) {
                    pieceMap.remove(list);
                    return true;
                }
            }
        }
        else if (piece.type==pieceType.BISHOP) {
            if (list.get(0)!= piece.x && list.get(1)!=piece.y) {
                if(piecesOnPath(piece,list)) {
                    if (!pieceMap.containsKey(list))
                        return true;

                    else if (pieceMap.containsKey(list) && pieceMap.get(list).isWhite != piece.isWhite) {
                        pieceMap.remove(list);
                        return true;
                    }
                }
            }
        }
        else if (piece.type==pieceType.ROOK) {
            if ((list.get(0) != piece.x && list.get(1) == piece.y) || (list.get(0)==piece.x && list.get(1)!=piece.y)) {
                if (piecesOnPath(piece, list)) {
                    if (!pieceMap.containsKey(list))
                        return true;

                    else if (pieceMap.containsKey(list) && pieceMap.get(list).isWhite != piece.isWhite) {
                        pieceMap.remove(list);
                        return true;
                    }
                }
            }
        }
        return false;
    }
   boolean piecesOnPath (chessPiece piece, ArrayList<Integer> list) {
        ArrayList <Integer> tempList=new ArrayList<>();
        int xDiff=Integer.signum(list.get(0)-piece.x);
        int yDiff=Integer.signum(list.get(1)-piece.y);
       tempList.add(0,piece.x+xDiff);
       tempList.add(1,piece.y+yDiff);
        System.out.println(list.get(0) + " greg " + list.get(1));
        System.out.println(piece.x + " diffles " + piece.y);
        while ((tempList.get(0)!= list.get(0) || tempList.get(1)!=list.get(1))) {
           System.out.println("whilechecker");

            System.out.println(tempList.get(0) + " glorbo " + tempList.get(1));
            if (pieceMap.containsKey(tempList)) {
                System.out.println(tempList.get(0) + "Falseies!" +tempList.get(1));
             return false; }
            tempList.set(0,(tempList.get(0)+xDiff));
            tempList.set(1,(tempList.get(1)+yDiff));
        }
        System.out.println("Trusies!");
        return true;
   }

   public void moveSet (chessPiece piece) {
       // System.out.println(piece.x + "Yaga");
        piece.moveSet=new HashMap<ArrayList<Integer>, Boolean>();
        ArrayList<Integer> tempList=new ArrayList<>();
        tempList.add(0,piece.x);
        tempList.add(1,piece.y);
        int pMultiplier;
        piece.special=false;
        if (piece.type==pieceType.PAWN) {
            if (piece.isWhite) {
                 pMultiplier=1;
            }
            else
                pMultiplier=-1;
                if (piece.y==1 && piece.isWhite ) {
                    tempList.set(1,piece.y+2);
                    if(!pieceMap.containsKey(tempList)) {
                        piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
                    piece.special=true;}
                }
                if(piece.y==6 && !piece.isWhite)  {
                    tempList.set(1,piece.y-2);
                    if(!pieceMap.containsKey(tempList)) {
                        piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
                        piece.special=true;
                    }
                }
                tempList.set(1,(piece.y+1*pMultiplier));
                if (!pieceMap.containsKey(tempList)) {
                    piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
                }
                tempList.set(0,(piece.x+1));
                tempList.set(1,(piece.y+1*pMultiplier));
                if ((pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite) ) {
                    piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
                }

                tempList.set(0,(piece.x-1));
                if (pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite) {
                    piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);}

                tempList.set(1,piece.y);
                if (pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite && pieceMap.get(tempList).special) {
                    ArrayList<Integer> worstList=new ArrayList<>();
                    worstList.add(piece.x-1);
                    worstList.add(piece.y+(1*pMultiplier));
                    pieceMap.remove(worstList);
                    piece.addToMoveList(worstList.get(0),worstList.get(1),piece,true); System.out.println("Napaoleon");}

            tempList.set(0,piece.x+1) ;
            if (pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite && pieceMap.get(tempList).special) {
                ArrayList<Integer> worstList=new ArrayList<>();
                worstList.add(piece.x+1);
                worstList.add(piece.y+(1*pMultiplier));
                pieceMap.remove(worstList);
                piece.addToMoveList(worstList.get(0),worstList.get(1),piece,true); System.out.println("Napaoleon");}
               



        }
        else if (piece.type==pieceType.QUEEN) {
            diagMoves(piece);
            horiMoves(piece);
            vertiMoves(piece);
        }
        else if (piece.type==pieceType.KNIGHT) {
            tempList.set(0,piece.x+2);
            tempList.set(1,piece.y+1);
            ArrayList<Integer> newList;
            if (!pieceMap.containsKey(tempList) && tempList.get(0)<8 && tempList.get(1) <8 && tempList.get(0)>-1 && tempList.get(1)>-1) {
              piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println(tempList.get(0) +  " A " + tempList.get(1));}
              if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite ) {
                 piece.moveSet.put(tempList,true); }
            tempList.set(0,piece.x-2);
            tempList.set(1,piece.y+1);
            System.out.println(tempList.get(0) +  " Gorgon " + tempList.get(1));
            if (!pieceMap.containsKey(tempList)   && tempList.get(0)<8 && tempList.get(1) <8 && tempList.get(0)>-1 && tempList.get(1)>-1) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);  System.out.println(tempList.get(0) +  " B " + tempList.get(1));}
             if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite )
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x+2);
            tempList.set(1,piece.y-1);
            if (!pieceMap.containsKey(tempList) && tempList.get(0)<8 && tempList.get(1) <8 && tempList.get(0)>-1 && tempList.get(1)>-1) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println(tempList.get(0) +  " C " + tempList.get(1)); }
             if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite )
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x-2);
            tempList.set(1,piece.y-1);
            if (!pieceMap.containsKey(tempList)  && tempList.get(0)<8 && tempList.get(1) <8 && tempList.get(0)>-1 && tempList.get(1)>-1) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println(tempList.get(0) +  " D " + tempList.get(1));}
             if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite )
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x+1);
            tempList.set(1,piece.y+2);
            if (!pieceMap.containsKey(tempList)  && tempList.get(0)<8 && tempList.get(1) <8 && tempList.get(0)>-1 && tempList.get(1)>-1) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println(tempList.get(0) +  " E " + tempList.get(1));}
             if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x+1);
            tempList.set(1,piece.y-2);
            if (!pieceMap.containsKey(tempList) && tempList.get(0)<8 && tempList.get(1) <8 && tempList.get(0)>-1 && tempList.get(1)>-1) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println(tempList.get(0) +  " F " + tempList.get(1));}
             if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x-1);
            tempList.set(1,piece.y+2);
            if (!pieceMap.containsKey(tempList)  && tempList.get(0)<8 && tempList.get(1)<8 && tempList.get(1)>-1 && tempList.get(0)>-1) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println(tempList.get(0) +  " G " + tempList.get(1));}
             if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x-1);
            tempList.set(1,piece.y-2);
            if (!pieceMap.containsKey(tempList)  && tempList.get(0)<8 && tempList.get(1) <8 && tempList.get(0)>-1 && tempList.get(1)>-1) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println(tempList.get(0) +  " H " + tempList.get(1));}
             if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite )
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            for (ArrayList<Integer> inti:
                 piece.moveSet.keySet()) {
                System.out.println(inti.get(0) + " rasp " + inti.get(1));
            }
        }
        else if(piece.type==pieceType.ROOK) {
            vertiMoves(piece);
            horiMoves(piece);
        }
        else if (piece.type==pieceType.BISHOP) {
            diagMoves(piece);
       }
       else if (piece.type==pieceType.KING) {
           tempList.set(0,piece.x+1);
            if (!pieceMap.containsKey(tempList))
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            else if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(1,piece.y+1);
            if (!pieceMap.containsKey(tempList))
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            else if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x);
            if (!pieceMap.containsKey(tempList))
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            else if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x-1);
            if (!pieceMap.containsKey(tempList))
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            else if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(1,piece.y);
            if (!pieceMap.containsKey(tempList))
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            else if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(1, piece.y+1);
            if (!pieceMap.containsKey(tempList))
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            else if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
            tempList.set(0,piece.x);
             if (!pieceMap.containsKey(tempList))
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            else if(pieceMap.containsKey(tempList) && pieceMap.get(tempList).isWhite!=piece.isWhite)
                 piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);

       }
   }

   void diagMoves(chessPiece piece) {
       System.out.println("Diag");

       ArrayList<Integer> tempList = new ArrayList<>();
       tempList.add(piece.x);
       tempList.add(piece.y);
       for (int n = 1; n < 9; n++) {
           tempList.set(0, piece.x + n);
           tempList.set(1, piece.y + n);
           if (tempList.get(0) > 7 || tempList.get(1) > 7 || tempList.get(0) < 0 || tempList.get(1) < 0) {
               break;
           } else if (pieceMap.containsKey(tempList)) {
               if (pieceMap.get(tempList).isWhite != piece.isWhite)
                   piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
               break;
           }
           if (!pieceMap.containsKey(tempList)) {
               piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
           }
       }
       for (int j = 1; j < 9; j++) {
           tempList.set(0, piece.x + j);
           tempList.set(1, piece.y - j);
           if (tempList.get(0) > 7 || tempList.get(1) > 7 || tempList.get(0) < 0 || tempList.get(1) < 0) {
               break;
           } else if (pieceMap.containsKey(tempList)) {
               if (pieceMap.get(tempList).isWhite != piece.isWhite)
                   piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
               break;
           }
           if (!pieceMap.containsKey(tempList)) {
               piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
           }
       }
       for (int j = 1; j < 9; j++) {
           tempList.set(0, piece.x - j);
           tempList.set(1, piece.y + j);
           if (tempList.get(0) > 7 || tempList.get(1) > 7 || tempList.get(0) < 0 || tempList.get(1) < 0) {
               break;
           } else if (pieceMap.containsKey(tempList)) {
               if (pieceMap.get(tempList).isWhite != piece.isWhite)
                   piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
               break;
           }
           if (!pieceMap.containsKey(tempList)) {
               piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
           }
       }
       for (int j = 1; j < 9; j++) {
           tempList.set(0, piece.x - j);
           tempList.set(1, piece.y - j);
           if (tempList.get(0) > 7 || tempList.get(1) > 7 || tempList.get(0) < 0 || tempList.get(1) < 0) {
               break;
           } else if (pieceMap.containsKey(tempList)) {
               if (pieceMap.get(tempList).isWhite != piece.isWhite)
                   piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
               break;
           }
           if (!pieceMap.containsKey(tempList)) {
               piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
           }
       }
   }

       void horiMoves(chessPiece piece) {
           System.out.println("Hori");
           ArrayList<Integer> tempList = new ArrayList<>();
           tempList.add(piece.x);
           tempList.add(piece.y);
           for (int n = 1; n < 8; n++) {
               tempList.set(0, piece.x + n);

               if (tempList.get(0) > 7 || tempList.get(0) < 0) {

                   break;
               } else if (pieceMap.containsKey(tempList)) {
                   System.out.println("Rainbow");
                   if (pieceMap.get(tempList).isWhite != piece.isWhite) {
                       piece.addToMoveList(tempList.get(0), tempList.get(1), piece, true);
                   }
                   break;
               }
               if (!pieceMap.containsKey(tempList)) {
                   piece.addToMoveList(tempList.get(0), tempList.get(1), piece, false);
                   System.out.println("Indigo");
               }
           }
           for (int n = 1; n < 8; n++) {
               System.out.println(n + " Irreverant " + (piece.x-n));
               tempList.set(0, (piece.x - n));
               if (tempList.get(0) > 7 || tempList.get(0) < 0)
                   break;
             else  if (pieceMap.containsKey(tempList)) {
                   System.out.println("DeathGlance");
                   if (pieceMap.get(tempList).isWhite != piece.isWhite) {
                       System.out.println("Land's end");
                       piece.addToMoveList(tempList.get(0), tempList.get(1), piece, true);
                   }
                       break;
               }
               if (!pieceMap.containsKey(tempList)) {
                   piece.addToMoveList(tempList.get(0), tempList.get(1), piece, false);
               }
           }
       }

    void vertiMoves(chessPiece piece) {
        System.out.println("Verti");

        ArrayList<Integer> tempList = new ArrayList<>();
        tempList.add(piece.x);
        tempList.add(piece.y);
        for (int n = 1; n < 8; n++) {
            tempList.set(1, piece.y + n);
            if (tempList.get(1) > 7 || tempList.get(1) < 0) {
                break;
            } else if (pieceMap.containsKey(tempList)) {
                if (pieceMap.get(tempList).isWhite != piece.isWhite)
                    piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true);
                break;
            }
            if (!pieceMap.containsKey(tempList)) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false);
            }
        }
        for (int n = 1; n < 8; n++) {
            tempList.set(1, piece.y - n);
            if (tempList.get(1) > 7 || tempList.get(1) < 0) {
                System.out.println("Thanos");
                break;
            } else if (pieceMap.containsKey(tempList)) {
                if (pieceMap.get(tempList).isWhite != piece.isWhite) {
                    piece.addToMoveList(tempList.get(0),tempList.get(1),piece,true); System.out.println("Brobro");}
                  break;
            }
            if (!pieceMap.containsKey(tempList)) {
                piece.addToMoveList(tempList.get(0),tempList.get(1),piece,false); System.out.println("Dankies");
            }
        }
    }





    public static void main (String[]args){
        launch(args);
    }
}

