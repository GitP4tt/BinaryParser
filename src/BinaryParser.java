import java.io.IOException;
import java.io.Reader;

class SyntaxError extends RuntimeException {

    public SyntaxError(String s) {
        super(s);
    }

}

enum Token {
    zero, one, dollar, decpoint
}


public class BinaryParser {

    Token Symbol;
    Reader input;

    private static class L {
        double val;
        double exp;
        String side;
    }


    public void parse() throws IOException {
        nextToken();

        System.out.println(S());

    }

    public double S() throws IOException {

        if (Symbol == Token.one || Symbol == Token.zero) {
            L L = new L();
            L.side = "left";
            double Lval = L(L).val / 2;
            double Rval = R();
            match(Token.dollar); // last Token must be $
            return Lval + Rval;

        } else{
            return R(); // damit auch .0101010101 funktioniert.
        }

    }

    public double R() throws IOException {
        if (Symbol == Token.decpoint) {
            match(Token.decpoint);
            L L = new L();
            L.side = "right";
            L.exp = 0.5;
            L = L(L);
            return L.val;
        } else {
            // epsilon alternative
            match(Token.dollar);
            return 0 ;
        }



    }

     public L L(L L) throws IOException {
        int Bval = B();
        L LS = new L();
        LS.side = L.side;
        if(L.side == "right"){
            LS.exp = L.exp * 0.5;
        }
        LS = LS(LS);
        if(L.side == "right") {
            L.val = Bval * L.exp + LS.val;
        }else{
            L.exp = LS.exp * 2;
            L.val = LS.val + Bval * L.exp;
        }

        return  L ;
    }

    public L LS(L LS) throws IOException {
        if (Symbol == Token.one || Symbol == Token.zero) { // FIRST set
           int Bval = B();
            L LS1 = new L();
            LS1.side = LS.side;
            if(LS.side == "right") LS1.exp = LS.exp * 0.5;
            LS1 = LS(LS1);
            if(LS.side == "right") {
                LS.val = Bval * LS.exp + LS1.val;
            }
            else {
                LS.exp = LS1.exp * 2;
                LS.val = LS1.val + Bval * LS.exp;
            }
            return LS;

        } else if (Symbol == Token.dollar || Symbol == Token.decpoint) { // FOLLOW set
            if(LS.side == "left") LS.exp = 1;
            LS.val = 0;
            return LS;
        } else
            throw new SyntaxError("illegal symbol " + Symbol);

    }

    public int B() throws IOException {
        if (Symbol == Token.one || Symbol == Token.zero) {
            int iVal = Symbol == Token.one ? 1 : 0;
            nextToken();
            return iVal;
        } else
            throw new SyntaxError("illegal symbol " + Symbol);

    }

    public Token nextToken() throws IOException {

        int i = input.read();

        switch (i) {
            case -1: {  // end of input
                input.close();
                return Symbol = Token.dollar;
            }
            case '0': {
                return Symbol = Token.zero;
            }
            case '1': {
                return Symbol = Token.one;
            }
            case '.': {
                return Symbol = Token.decpoint;
            }
            default: {
                throw new SyntaxError("illegal symbol " + i);
            }
        }

    }

    public void match(Token t) throws IOException {

        if (t == Symbol) {
            if (t != Token.dollar)
                nextToken();
        } else
            throw new SyntaxError("syntax error " + t + " expected " + Symbol + " found");

    }


}
