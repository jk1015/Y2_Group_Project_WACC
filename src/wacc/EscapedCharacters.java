package wacc;

public class EscapedCharacters {

    public static int getAscii(char esc) {
        switch (esc) {
            case '0':   return 0x00;
            case 'b':   return 0x08;
            case 't':   return 0x09;
            case 'n':   return 0x0a;
            case 'f':   return 0x0c;
            case 'r':   return 0x0d;
            case '"':   return 0x22;
            case '\'':  return 0x27;
            case '\\':  return 0x5c;
            default:    throw new IllegalArgumentException();
        }
    }

}
