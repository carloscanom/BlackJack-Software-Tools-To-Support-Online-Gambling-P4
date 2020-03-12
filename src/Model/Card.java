package Model;



public class Card{
    
    private char suit;
    private int value;
    private char charValue;
    private String rutaRepresentacion;
    
    public Card(String carta){
        this.suit = setSuit(carta.charAt(1));
        this.value = setValue(carta.charAt(0));
        this.charValue = carta.charAt(0);
        this.rutaRepresentacion = asignarImagen();
    }
    
    private char setSuit(char c){
        switch(c){
            case 'h': return 'h';
            case 'd': return 'd';
            case 's': return 's';
            case 'c': return 'c';
            default : return ' ';
           
        }
        
    }
    
     public int setValue(char c){
        switch(c){   
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            case 'T': return 10;
            case 'J': return 10;
            case 'Q': return 10;
            case 'K': return 10;
            case 'A': return 11;
            default: return 0;
        }
    }
    
    public int getValue(){
        return this.value;
    }
    
    public char getSuit(){
        return this.suit;
    }
    
    public char getCharValue(){
        return this.charValue;
    }
   
    
    public String toString(){
        return "" + this.charValue+this.suit;
    }

    public String asignarImagen(){
        switch(toString()){
            case "As": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\ace_of_spades.jpg";
            case "2s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\2_of_spades.jpg";
            case "3s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\3_of_spades.jpg";
            case "4s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\4_of_spades.jpg";
            case "5s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\5_of_spades.jpg";
            case "6s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\6_of_spades.jpg";
            case "7s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\7_of_spades.jpg";
            case "8s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\8_of_spades.jpg";
            case "9s": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\9_of_spades.jpg";
            case "Ts": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\10_of_spades.jpg";
            case "Js": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\jack_of_spades2.jpg";
            case "Qs": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\queen_of_spades2.jpg";
            case "Ks": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\king_of_spades2.jpg";
            case "Ac": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\ace_of_clubs.jpg";
            case "2c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\2_of_clubs.jpg";
            case "3c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\3_of_clubs.jpg";
            case "4c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\4_of_clubs.jpg";
            case "5c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\5_of_clubs.jpg";
            case "6c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\6_of_clubs.jpg";
            case "7c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\7_of_clubs.jpg";
            case "8c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\8_of_clubs.jpg";
            case "9c": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\9_of_clubs.jpg";
            case "Tc": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\10_of_clubs.jpg";
            case "Jc": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\jack_of_clubs2.jpg";
            case "Qc": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\queen_of_clubs2.jpg";
            case "Kc": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\king_of_clubs2.jpg";
            case "Ad": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\ace_of_diamonds.jpg";
            case "2d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\2_of_diamonds.jpg";
            case "3d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\3_of_diamonds.jpg";
            case "4d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\4_of_diamonds.jpg";
            case "5d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\5_of_diamonds.jpg";
            case "6d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\6_of_diamonds.jpg";
            case "7d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\7_of_diamonds.jpg";
            case "8d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\8_of_diamonds.jpg";
            case "9d": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\9_of_diamonds.jpg";
            case "Td": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\10_of_diamonds.jpg";
            case "Jd": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\jack_of_diamonds2.jpg";
            case "Qd": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\queen_of_diamonds2.jpg";
            case "Kd": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\king_of_diamonds2.jpg";
            case "Ah": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\ace_of_hearts.jpg";
            case "2h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\2_of_hearts.jpg";
            case "3h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\3_of_hearts.jpg";
            case "4h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\4_of_hearts.jpg";
            case "5h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\5_of_hearts.jpg";
            case "6h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\6_of_hearts.jpg";
            case "7h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\7_of_hearts.jpg";
            case "8h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\8_of_hearts.jpg";
            case "9h": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\9_of_hearts.jpg";
            case "Th": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\10_of_hearts.jpg";
            case "Jh": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\jack_of_hearts2.jpg";
            case "Qh": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\queen_of_hearts2.jpg";
            case "Kh": return System.getProperty("user.dir") + "\\src\\Imágenes\\Repr Cartas\\king_of_hearts2.jpg";
            default: return "";
            
        }
    }
    
    public String getRutaRep(){
        return this.rutaRepresentacion;
    }

    public boolean isAce(){
        return this.charValue == 'A';
    }
    
}
