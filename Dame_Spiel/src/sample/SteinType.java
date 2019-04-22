package sample;

public enum SteinType{
    RED(1), WHITE(-1);   //we have red and white pieces

    //we also want to know which direction they move

    final int moveDir;

    SteinType(int moveDir){
        this.moveDir = moveDir;
    }
}