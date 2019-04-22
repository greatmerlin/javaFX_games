package sample;

public class MoveResult {

    private MoveType type;

    public MoveType getType(){

        return type;
    }

    // if we kill, we want to know about the piece we are killing

    private Stein stein;

    public Stein getStein() {

        return stein;
    }

    // for the cases that we don't have pieces, we call another constructor

    public MoveResult(MoveType type){

        this(type,null);
    }

    public MoveResult(MoveType type, Stein stein){

        this.type = type;
        this.stein = stein;
    }
}